package com.gabinote.mail.mail.template

import com.gabinote.mail.mail.dto.event.MailSendMessage
import com.gabinote.mail.mail.enums.MailTemplate
import com.gabinote.mail.mail.enums.MailType
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class AdminAlertTest : DescribeSpec({

    describe("[Mail] AdminAlert Test") {
        val adminAlert = AdminAlert()

        describe("AdminAlert 속성") {
            it("type은 ADMIN_ALERT 이다.") {
                adminAlert.type shouldBe MailTemplate.ADMIN_ALERT
            }

            it("fieldKeys는 message와 serviceName을 포함한다.") {
                adminAlert.fieldKeys shouldBe setOf("message", "serviceName")
            }

            it("fileName은 admin-alert 이다.") {
                adminAlert.fileName shouldBe "admin_alert"
            }
        }

        describe("AdminAlert.setupTemplate") {
            context("정상적인 MailSendMessage가 주어지면,") {
                val message = MailSendMessage(
                    serviceName = "test-service",
                    type = MailType.ADMIN_ALERT,
                    recipients = mutableListOf("admin@test.com"),
                    title = "Test Alert",
                    contents = mapOf("message" to listOf("테스트 메시지입니다.")),
                    template = MailTemplate.ADMIN_ALERT
                )

                it("Context에 serviceName이 설정된다.") {
                    val context = adminAlert.setupTemplate(message)

                    context.getVariable("serviceName") shouldBe "test-service"
                }

                it("Context에 message가 설정된다.") {
                    val context = adminAlert.setupTemplate(message)

                    context.getVariable("message") shouldBe "테스트 메시지입니다."
                }
            }

            context("contents에 message가 없으면,") {
                val message = MailSendMessage(
                    serviceName = "test-service",
                    type = MailType.ADMIN_ALERT,
                    recipients = mutableListOf("admin@test.com"),
                    title = "Test Alert",
                    contents = emptyMap(),
                    template = MailTemplate.ADMIN_ALERT
                )

                it("Context의 message는 빈 문자열이 된다.") {
                    val context = adminAlert.setupTemplate(message)

                    context.getVariable("message") shouldBe ""
                }

                it("Context에 serviceName은 정상적으로 설정된다.") {
                    val context = adminAlert.setupTemplate(message)

                    context.getVariable("serviceName") shouldBe "test-service"
                }
            }

            context("contents의 message 리스트가 비어있으면,") {
                val message = MailSendMessage(
                    serviceName = "test-service",
                    type = MailType.ADMIN_ALERT,
                    recipients = mutableListOf("admin@test.com"),
                    title = "Test Alert",
                    contents = mapOf("message" to emptyList()),
                    template = MailTemplate.ADMIN_ALERT
                )

                it("Context의 message는 빈 문자열이 된다.") {
                    val context = adminAlert.setupTemplate(message)

                    context.getVariable("message") shouldBe ""
                }
            }

            context("contents의 message 리스트에 여러 값이 있으면,") {
                val message = MailSendMessage(
                    serviceName = "test-service",
                    type = MailType.ADMIN_ALERT,
                    recipients = mutableListOf("admin@test.com"),
                    title = "Test Alert",
                    contents = mapOf("message" to listOf("첫번째 메시지", "두번째 메시지")),
                    template = MailTemplate.ADMIN_ALERT
                )

                it("Context의 message는 첫번째 값만 설정된다.") {
                    val context = adminAlert.setupTemplate(message)

                    context.getVariable("message") shouldBe "첫번째 메시지"
                }
            }
        }
    }
})

