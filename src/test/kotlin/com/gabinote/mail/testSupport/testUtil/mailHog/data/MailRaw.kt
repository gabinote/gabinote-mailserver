package com.gabinote.mail.testSupport.testUtil.mailHog.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class MailRaw(
    @JsonProperty("From")
    val From: String? = null,
    @JsonProperty("To")
    val To: List<String>? = null,
    @JsonProperty("Data")
    val Data: String? = null
)

