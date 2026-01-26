package com.gabinote.mail.testSupport.testUtil.mailHog.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class MailHogResponse(
    val total: Int,
    val count: Int,
    val items: List<MailHogItem>
)