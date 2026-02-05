package com.gabinote.mail.mail.template

import com.gabinote.mail.mail.enums.MailTemplate
import com.gabinote.mail.testSupport.testTemplate.ServiceTestTemplate
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.assertThrows

class TemplateStrategyFactoryTest : ServiceTestTemplate() {

    private lateinit var templateStrategyFactory: TemplateStrategyFactory

    init {
        describe("[Mail] TemplateStrategyFactory Test") {
            describe("TemplateStrategyFactory.getStrategy") {
                context("ADMIN_ALERT 타입이 주어지면,") {
                    val adminAlert = mockk<AdminAlert>()

                    beforeTest {
                        clearAllMocks()
                        every { adminAlert.type } returns MailTemplate.ADMIN_ALERT
                        templateStrategyFactory = TemplateStrategyFactory(listOf(adminAlert))
                    }

                    it("AdminAlert 전략을 반환한다.") {
                        val result = templateStrategyFactory.getStrategy(MailTemplate.ADMIN_ALERT)

                        result shouldBe adminAlert
                        result.type shouldBe MailTemplate.ADMIN_ALERT
                    }
                }

                context("등록되지 않은 전략 타입이 주어지면,") {
                    beforeTest {
                        clearAllMocks()
                        templateStrategyFactory = TemplateStrategyFactory(emptyList())
                    }

                    it("IllegalArgumentException 예외를 던진다.") {
                        val ex = assertThrows<IllegalArgumentException> {
                            templateStrategyFactory.getStrategy(MailTemplate.ADMIN_ALERT)
                        }

                        ex.message shouldBe "Invalid strategy key: ADMIN_ALERT"
                    }
                }

                context("여러 전략이 등록되어 있을 때,") {
                    val adminAlert = mockk<AdminAlert>()
                    val userGoodbye = mockk<EmailTemplate>()

                    beforeTest {
                        clearAllMocks()
                        every { adminAlert.type } returns MailTemplate.ADMIN_ALERT
                        every { userGoodbye.type } returns MailTemplate.USER_GOODBYE
                        templateStrategyFactory = TemplateStrategyFactory(listOf(adminAlert, userGoodbye))
                    }

                    it("ADMIN_ALERT 타입으로 AdminAlert 전략을 반환한다.") {
                        val result = templateStrategyFactory.getStrategy(MailTemplate.ADMIN_ALERT)

                        result shouldBe adminAlert
                        result.type shouldBe MailTemplate.ADMIN_ALERT
                    }

                    it("USER_GOODBYE 타입으로 해당 전략을 반환한다.") {
                        val result = templateStrategyFactory.getStrategy(MailTemplate.USER_GOODBYE)

                        result shouldBe userGoodbye
                        result.type shouldBe MailTemplate.USER_GOODBYE
                    }
                }
            }
        }
    }
}

