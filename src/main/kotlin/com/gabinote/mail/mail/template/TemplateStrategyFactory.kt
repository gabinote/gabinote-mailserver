package com.gabinote.mail.mail.template

import com.gabinote.mail.common.util.strategy.AbstractStrategyFactory
import com.gabinote.mail.common.util.strategy.Strategy
import com.gabinote.mail.mail.enums.MailTemplate
import org.springframework.stereotype.Component

/**
 * 이메일 템플릿 전략 팩토리
 */
@Component
class TemplateStrategyFactory(
    strategies : List<EmailTemplate>
) : AbstractStrategyFactory<MailTemplate, EmailTemplate>(strategies) {
}