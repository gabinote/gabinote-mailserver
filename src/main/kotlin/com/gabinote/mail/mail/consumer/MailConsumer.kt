package com.gabinote.mail.mail.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import com.gabinote.mail.mail.consumer.MailTopic.MAIL_SEND_TOPIC
import com.gabinote.mail.mail.dto.event.MailSendMessage
import com.gabinote.mail.mail.service.MailService
import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Service


private val logger = KotlinLogging.logger {}

@Service
class MailConsumer(
    private val mailService: MailService,
    private val objectMapper: ObjectMapper,
    private val consumerHelper: ConsumerHelper
) {

    @KafkaListener(
        topics = [MAIL_SEND_TOPIC],
        groupId = "mail-send-group",
    )
    fun processMailSendEvent(record: ConsumerRecord<String, String>, ack: Acknowledgment) {
        val message = record.value()
        logger.debug { "Received mail send event message: $message" }
        if (message.isNullOrBlank()) {
            logger.warn { "Received null/empty message, skipping." }
            ack.acknowledge()
            return
        }
        runCatching {
            val mailSendEvent = objectMapper.readValue(message, MailSendMessage::class.java)
            mailService.sendMail(mailSendEvent)

        }.onFailure { e ->
            logger.error(e) { "Failed to process mail send event." }
            consumerHelper.sendToDlq(
                record,
                "${MAIL_SEND_TOPIC}-dlq",
                e
            )
        }
        ack.acknowledge()
    }


}