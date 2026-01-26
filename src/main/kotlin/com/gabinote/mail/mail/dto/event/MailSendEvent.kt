package com.gabinote.mail.mail.dto.event

import com.gabinote.mail.mail.enums.MailTemplate
import com.gabinote.mail.mail.enums.MailType

data class MailSendEvent(
    val serviceName: String,
    val type: MailType,
    val recipients : List<String> = emptyList(),
    val title: String,
    val contents: Map<String, List<String>> = emptyMap(),
    val template: MailTemplate,
)