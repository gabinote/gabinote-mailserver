package com.gabinote.mail.mail.template

import com.gabinote.mail.mail.dto.event.MailSendMessage
import com.gabinote.mail.mail.enums.MailTemplate
import org.springframework.stereotype.Component
import org.thymeleaf.context.Context

@Component
class AdminAlert : EmailTemplate() {
    override val type = MailTemplate.ADMIN_ALERT
    override val fieldKeys = setOf(
        "message",
        "serviceName"
    )

    override val fileName: String = "admin_alert"

    override fun setupTemplate(message: MailSendMessage) : Context {
        val context = Context()
        context.setVariable("serviceName", message.serviceName)
        context.setVariable("message", message.contents["message"]?.firstOrNull() ?: "")
        return context
    }
}