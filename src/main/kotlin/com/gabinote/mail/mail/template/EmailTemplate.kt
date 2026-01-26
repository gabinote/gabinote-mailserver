package com.gabinote.mail.mail.template

import com.gabinote.mail.common.util.strategy.Strategy
import com.gabinote.mail.mail.dto.event.MailSendMessage
import com.gabinote.mail.mail.enums.MailTemplate
import org.thymeleaf.context.Context

abstract class EmailTemplate : Strategy<MailTemplate> {
    abstract val fieldKeys: Set<String>

    /**
     * 템플릿 파일명 /resources/templates/
     */
    abstract val fileName: String

    /**
     * 템플릿 셋업
     * @param message 메일 발송 메시지
     * @return thymeleaf context
     */
    abstract fun setupTemplate(message: MailSendMessage) : Context
}