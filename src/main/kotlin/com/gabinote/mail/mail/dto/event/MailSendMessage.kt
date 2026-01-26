package com.gabinote.mail.mail.dto.event

import com.gabinote.mail.mail.enums.MailTemplate
import com.gabinote.mail.mail.enums.MailType

data class MailSendMessage(
    val serviceName: String,
    val type: MailType,
    var recipients : MutableList<String> = mutableListOf(),
    val title: String,
    val contents: Map<String, List<String>> = emptyMap(),
    val template: MailTemplate,
){
    fun changeRecipients(recipients: List<String>)  {
        this.recipients.clear()
        this.recipients.addAll(recipients)
    }
}