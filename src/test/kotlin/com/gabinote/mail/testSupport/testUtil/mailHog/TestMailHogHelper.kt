package com.gabinote.mail.testSupport.testUtil.mailHog

import com.gabinote.mail.testSupport.testUtil.mailHog.data.MailHogResponse
import io.github.oshai.kotlinlogging.KotlinLogging
import io.kotest.assertions.nondeterministic.eventually
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.jsoup.Jsoup
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.TestComponent
import org.springframework.web.client.RestTemplate
import kotlin.time.Duration.Companion.seconds

private val logger = KotlinLogging.logger {}

@TestComponent
class TestMailHogHelper(
    @Value("\${test.mailhog.api-url}")
    private val mailHogBaseUrl: String,
    private val restTemplate: RestTemplate = RestTemplate()
) {

    /**
     * MailHog에 저장된 모든 이메일 메시지를 삭제합니다.
     */
    fun clean() {
        logger.info { "Cleaning MailHog messages at $mailHogBaseUrl" }
        restTemplate.delete("$mailHogBaseUrl/api/v1/messages")
        logger.info { "Cleaning MailHog messages at $mailHogBaseUrl" }
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
        val response = getMessages()

        logger.info { "Verifying latest messages at $to $to $subjectKeyword $expected" }

        // 메일이 하나도 없으면 실패
        response.items.shouldNotBeNull()
        response.items.size shouldBeGreaterThanOrEqual 1

        val latestMail = response.items.first()
        val headers = latestMail.Content.Headers
        val body = latestMail.Content.Body

        // 수신자 검증
        headers["To"]?.firstOrNull() shouldContain to

        if (subjectKeyword != null) {
            headers["Subject"]?.firstOrNull() shouldContain subjectKeyword
        }

        val actualDoc = Jsoup.parse(body)
        val expectedDoc = Jsoup.parse(expected)

        logger.info { "Comparing $to $subjectKeyword $expectedDoc" }

        actualDoc.html() shouldBe expectedDoc.html()
    }

    // 내부 API 호출
    private fun getMessages(): MailHogResponse {
        return restTemplate.getForObject(
            "$mailHogBaseUrl/api/v2/messages",
            MailHogResponse::class.java
        ) ?: throw IllegalStateException("MailHog 응답이 비어있습니다.")
    }

}