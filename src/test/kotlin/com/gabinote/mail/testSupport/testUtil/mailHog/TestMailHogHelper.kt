package com.gabinote.mail.testSupport.testUtil.mailHog

import com.fasterxml.jackson.databind.ObjectMapper
import com.gabinote.mail.testSupport.testUtil.mailHog.data.MailHogResponse
import io.github.oshai.kotlinlogging.KotlinLogging
import io.kotest.assertions.nondeterministic.eventually
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import jakarta.mail.internet.MimeUtility
import org.jsoup.Jsoup
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.TestComponent
import org.springframework.web.client.RestClient
import java.io.ByteArrayInputStream
import kotlin.time.Duration.Companion.seconds

private val logger = KotlinLogging.logger {}

@TestComponent
class TestMailHogHelper(
    @Value("\${test.mailhog.api-url}")
    private val mailHogBaseUrl: String
) {

    private val restClient: RestClient = RestClient.create()
    private val objectMapper: ObjectMapper = ObjectMapper()

    /**
     * MailHog에 저장된 모든 이메일 메시지를 삭제합니다.
     */
    fun clean() {
        logger.info { "Cleaning MailHog messages at $mailHogBaseUrl" }
        restClient.delete()
            .uri("$mailHogBaseUrl/api/v1/messages")
            .retrieve()
            .toBodilessEntity()
        logger.info { "Cleaned MailHog messages at $mailHogBaseUrl" }
    }

    suspend fun shouldHaveReceivedMail(minCount: Int = 1) {
        eventually(5.seconds) {
            val response = getMessages()
            response.total shouldBeGreaterThanOrEqual minCount
        }
    }

    fun verifyLatestMail(
        to: String,
        subjectKeyword: String? = null,
        expected: String
    ) {
        logger.info { "Verifying latest messages at $to $subjectKeyword" }
        val response = getMessages()

        // 메일이 하나도 없으면 실패
        response.items.shouldNotBeNull()
        response.items.size shouldBeGreaterThanOrEqual 1

        val latestMail = response.items.first()
        val headers = latestMail.Content.Headers
        val rawBody = latestMail.Content.Body

        // Quoted-Printable 디코딩
        val body = decodeQuotedPrintable(rawBody)


        // 수신자 검증
        headers["To"]?.firstOrNull() shouldContain to

        // 제목 검증
        val rawSubject = headers["Subject"]?.firstOrNull()
        val decodedSubject = rawSubject?.let { MimeUtility.decodeText(it) }
        if (subjectKeyword != null) {
            decodedSubject shouldContain subjectKeyword
        }

        val actualDoc = Jsoup.parse(body)
        val expectedDoc = Jsoup.parse(expected)

        logger.info { "Comparing $to $subjectKeyword" }

        actualDoc.html() shouldBe expectedDoc.html()
    }

    /**
     * Quoted-Printable 인코딩된 문자열을 디코딩합니다.
     */
    private fun decodeQuotedPrintable(encoded: String): String {
        return try {
            val inputStream = ByteArrayInputStream(encoded.toByteArray(Charsets.UTF_8))
            val decodedStream = MimeUtility.decode(inputStream, "quoted-printable")
            decodedStream.bufferedReader(Charsets.UTF_8).readText()
        } catch (e: Exception) {
            logger.warn { "Failed to decode quoted-printable, using original: ${e.message}" }
            encoded
        }
    }

    // 내부 API 호출
    private fun getMessages(): MailHogResponse {
        val responseBody = restClient.get()
            .uri("$mailHogBaseUrl/api/v2/messages")
            .retrieve()
            .body(String::class.java)
            ?: throw IllegalStateException("MailHog 응답이 비어있습니다.")
        logger.info { "Fetched MailHog messages: $responseBody" }
        return objectMapper.readValue(responseBody, MailHogResponse::class.java)
    }

}