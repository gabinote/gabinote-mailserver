package com.gabinote.mail.mail.service

import com.gabinote.mail.mail.dto.event.MailSendMessage
import com.gabinote.mail.mail.enums.MailTemplate
import com.gabinote.mail.mail.enums.MailType
import com.gabinote.mail.mail.template.EmailTemplate
import com.gabinote.mail.mail.template.TemplateStrategyFactory
import com.gabinote.mail.testSupport.testTemplate.ServiceTestTemplate
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import jakarta.mail.internet.MimeMessage
import org.springframework.mail.javamail.JavaMailSender
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context

class MailServiceTest : ServiceTestTemplate() {

    private lateinit var mailService: MailService

    @MockK
    private lateinit var javaMailSender: JavaMailSender

    @MockK
    private lateinit var templateEngine: TemplateEngine

    @MockK
    private lateinit var templateStrategyFactory: TemplateStrategyFactory

    init {
        beforeTest {
            clearAllMocks()
            mailService = MailService(
                javaMailSender,
                templateEngine,
                templateStrategyFactory
            )
            // adminEmailAddress 필드 설정
            val adminEmailField = MailService::class.java.getDeclaredField("adminEmailAddress")
            adminEmailField.isAccessible = true
            adminEmailField.set(mailService, listOf("admin@gabinote.com", "admin2@gabinote.com"))

            // fromAddress 필드 설정
            val fromAddressField = MailService::class.java.getDeclaredField("fromAddress")
            fromAddressField.isAccessible = true
            fromAddressField.set(mailService, "test@test.local")
        }

        describe("[Mail] MailService Test") {
            describe("MailService.sendMail") {
                describe("ADMIN_ALERT 타입") {
                    context("올바른 ADMIN_ALERT 메시지가 주어지면,") {
                        val message = MailSendMessage(
                            serviceName = "test-service",
                            type = MailType.ADMIN_ALERT,
                            recipients = mutableListOf("user@test.com"),
                            title = "Test Admin Alert",
                            contents = mapOf("message" to listOf("테스트 메시지입니다.")),
                            template = MailTemplate.ADMIN_ALERT
                        )

                        val mockTemplate = mockk<EmailTemplate>()
                        val mockContext = Context()
                        val mockMimeMessage = mockk<MimeMessage>(relaxed = true)
                        val processedBody = "<html>processed body</html>"

                        beforeTest {
                            every {
                                templateStrategyFactory.getStrategy(MailTemplate.ADMIN_ALERT)
                            } returns mockTemplate
                            every {
                                mockTemplate.fileName
                            } returns "admin-alert"
                            every {
                                mockTemplate.setupTemplate(message)
                            } returns mockContext

                            every {
                                templateEngine.process("admin-alert", mockContext)
                            } returns processedBody

                            every {
                                javaMailSender.createMimeMessage()
                            } returns mockMimeMessage

                            every {
                                javaMailSender.send(mockMimeMessage)
                            } returns Unit
                        }

                        it("수신자를 어드민 이메일로 변경하고 메일을 전송한다.") {
                            mailService.sendMail(message)

                            // 수신자가 어드민 이메일로 변경되었는지 확인
                            message.recipients shouldBe listOf("admin@gabinote.com", "admin2@gabinote.com")

                            verify(exactly = 1) {
                                templateStrategyFactory.getStrategy(MailTemplate.ADMIN_ALERT)
                            }

                            verify(exactly = 1){
                                mockTemplate.fileName
                            }

                            verify(exactly = 1) {
                                mockTemplate.setupTemplate(message)
                            }

                            verify(exactly = 1) {
                                templateEngine.process("admin-alert", mockContext)
                            }

                            verify(exactly = 1) {
                                javaMailSender.createMimeMessage()
                            }

                            verify(exactly = 1) {
                                javaMailSender.send(mockMimeMessage)
                            }
                        }
                    }
                }

                describe("ALL_ALERT 타입") {
                    context("ALL_ALERT 메시지가 주어지면,") {
                        val message = MailSendMessage(
                            serviceName = "test-service",
                            type = MailType.ALL_ALERT,
                            recipients = mutableListOf("user@test.com"),
                            title = "Test All Alert",
                            contents = mapOf("message" to listOf("전체 알림 테스트")),
                            template = MailTemplate.ADMIN_ALERT
                        )

                        val mockTemplate = mockk<EmailTemplate>()

                        beforeTest {
                            every {
                                templateStrategyFactory.getStrategy(MailTemplate.ADMIN_ALERT)
                            } returns mockTemplate
                        }

                        it("아직 구현되지 않아 로그만 출력한다.") {
                            mailService.sendMail(message)

                            verify(exactly = 1) {
                                templateStrategyFactory.getStrategy(MailTemplate.ADMIN_ALERT)
                            }

                            // send 호출이 없어야 함
                            verify(exactly = 0) {
                                javaMailSender.createMimeMessage()
                            }
                        }
                    }
                }

                describe("PER_USER_ALERT 타입") {
                    context("PER_USER_ALERT 메시지가 주어지면,") {
                        val message = MailSendMessage(
                            serviceName = "test-service",
                            type = MailType.PER_USER_ALERT,
                            recipients = mutableListOf("user@test.com"),
                            title = "Test Per User Alert",
                            contents = mapOf("message" to listOf("개별 유저 알림 테스트")),
                            template = MailTemplate.ADMIN_ALERT
                        )

                        val mockTemplate = mockk<EmailTemplate>()

                        beforeTest {
                            every {
                                templateStrategyFactory.getStrategy(MailTemplate.ADMIN_ALERT)
                            } returns mockTemplate
                        }

                        it("아직 구현되지 않아 로그만 출력한다.") {
                            mailService.sendMail(message)

                            verify(exactly = 1) {
                                templateStrategyFactory.getStrategy(MailTemplate.ADMIN_ALERT)
                            }

                            // send 호출이 없어야 함
                            verify(exactly = 0) {
                                javaMailSender.createMimeMessage()
                            }
                        }
                    }
                }

                describe("ALL_MARKET_ALERT 타입") {
                    context("ALL_MARKET_ALERT 메시지가 주어지면,") {
                        val message = MailSendMessage(
                            serviceName = "test-service",
                            type = MailType.ALL_MARKET_ALERT,
                            recipients = mutableListOf("user@test.com"),
                            title = "Test All Market Alert",
                            contents = mapOf("message" to listOf("마켓 전체 알림 테스트")),
                            template = MailTemplate.ADMIN_ALERT
                        )

                        val mockTemplate = mockk<EmailTemplate>()

                        beforeTest {
                            every {
                                templateStrategyFactory.getStrategy(MailTemplate.ADMIN_ALERT)
                            } returns mockTemplate
                        }

                        it("아직 구현되지 않아 로그만 출력한다.") {
                            mailService.sendMail(message)

                            verify(exactly = 1) {
                                templateStrategyFactory.getStrategy(MailTemplate.ADMIN_ALERT)
                            }

                            // send 호출이 없어야 함
                            verify(exactly = 0) {
                                javaMailSender.createMimeMessage()
                            }
                        }
                    }
                }
            }
        }
    }
}

