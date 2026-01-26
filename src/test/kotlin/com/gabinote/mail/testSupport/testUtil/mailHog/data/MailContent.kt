package com.gabinote.mail.testSupport.testUtil.mailHog.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class MailContent(
    @JsonProperty("Headers")
    val Headers: Map<String, List<String>>,
    @JsonProperty("Body")
    val Body: String
)