package com.gabinote.mail.mail.dto.service

import com.gabinote.mail.mail.enums.MailTemplate
import com.gabinote.mail.mail.enums.MailType
import com.gabinote.mail.mail.template.EmailTemplate

data class MailSendReqServiceDto(
    val recipients : List<String> = emptyList(),
    val title: String,
    val template: MailTemplate,
)