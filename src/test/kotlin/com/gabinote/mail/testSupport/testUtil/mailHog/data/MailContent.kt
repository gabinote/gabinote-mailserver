package com.gabinote.mail.testSupport.testUtil.mailHog.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class MailContent(
    val Headers: Map<String, List<String>>,
    val Body: String
)