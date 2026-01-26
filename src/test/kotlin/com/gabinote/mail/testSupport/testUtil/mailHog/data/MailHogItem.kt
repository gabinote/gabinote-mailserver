package com.gabinote.mail.testSupport.testUtil.mailHog.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class MailHogItem(
    val ID: String,
    val Content: MailContent,
    val Raw: String? = null
)