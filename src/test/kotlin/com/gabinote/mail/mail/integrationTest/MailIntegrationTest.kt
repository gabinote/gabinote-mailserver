package com.gabinote.mail.mail.integrationTest

import com.gabinote.mail.mail.consumer.MailTopic.MAIL_SEND_TOPIC
import com.gabinote.mail.mail.dto.event.MailSendMessage
import com.gabinote.mail.mail.enums.MailTemplate
import com.gabinote.mail.mail.enums.MailType
import com.gabinote.mail.testSupport.testTemplate.IntegrationTestTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.MailSender
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context

class MailIntegrationTest() : IntegrationTestTemplate() {

    @Value("\${gabinote.admin-email.address}")
    private lateinit var adminEmail: List<String>

    @Autowired
    private lateinit var templateEngine: TemplateEngine

    init {

        beforeTest {
            testKafkaHelper.deleteAllTopics()
            testMailHogHelper.clean()
        }

        feature("[Mail] Integration Test") {
            feature("${MailType.ADMIN_ALERT} 메일 전송 테스트") {


                scenario("관리자 알림 메일의 서비스명과 메시지가 올바르게 렌더링된다.") {
                    val message = MailSendMessage(
                        serviceName = "payment-service",
                        type = MailType.ADMIN_ALERT,
                        recipients = mutableListOf(""),
                        title = "결제 오류 알림",
                        contents = mapOf("message" to listOf("결제 처리 중 오류가 발생했습니다.")),
                        template = MailTemplate.ADMIN_ALERT
                    )

                    testKafkaHelper.sendMessage(MAIL_SEND_TOPIC, objectMapper.writeValueAsString(message))

                    // MailHog에서 메일 수신 대기
                    testMailHogHelper.shouldHaveReceivedMail(minCount = 1)
                    val testContext = Context().apply {
                        setVariable("serviceName", message.serviceName)
                        setVariable("message", message.contents["message"]?.firstOrNull() ?: "")
                    }
                    val expectedBody = templateEngine.process("admin_alert", testContext)
                    // 발송된 메일 HTML body 검증
                    testMailHogHelper.verifyLatestMail(
                        to = adminEmail[0],
                        subjectKeyword = "결제 오류 알림",
                        expected = expectedBody
                    )
                }
            }

        }
    }
}