package com.gabinote.mail.mail.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import com.gabinote.mail.mail.dto.event.MailSendEvent
import com.gabinote.mail.mail.service.MailService
import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Service


private val logger = KotlinLogging.logger {}
const val MAIL_SEND_TOPIC = "mail-send-event"
@Service
class MailConsumer(
    private val mailService: MailService,
    private val objectMapper: ObjectMapper
) {


}