package com.gabinote.mail.mail.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.gabinote.mail.mail.dto.service.MailSendReqServiceDto
import com.gabinote.mail.mail.enums.MailTemplate
import com.gabinote.mail.mail.enums.MailType
import com.gabinote.mail.mail.mapping.MailMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger {}

const val MAIL_SEND_TOPIC = "mail-send-event"

@Service
class MailService(
    private val mailMapper: MailMapper,
) {



}