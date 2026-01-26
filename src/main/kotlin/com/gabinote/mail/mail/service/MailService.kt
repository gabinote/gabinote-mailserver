package com.gabinote.mail.mail.service

import com.gabinote.mail.mail.dto.event.MailSendMessage
import com.gabinote.mail.mail.dto.service.MailSendReqServiceDto
import com.gabinote.mail.mail.enums.MailTemplate
import com.gabinote.mail.mail.enums.MailType
import com.gabinote.mail.mail.template.EmailTemplate
import com.gabinote.mail.mail.template.TemplateStrategyFactory
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine

private val logger = KotlinLogging.logger {}

@Service
class MailService(
    private val javaMailSender: JavaMailSender,
    private val templateEngine: TemplateEngine,
    private val templateStrategyFactory: TemplateStrategyFactory
) {

    @Value("\${gabinote.admin-email.address}")
    private lateinit var adminEmailAddress: List<String>

    @Value("\${gabinote.from.address}")
    private lateinit var fromAddress: String

    /**
     * 이메일 전송 처리
     * @param message MailSendMessage
     */
    fun sendMail(message: MailSendMessage){
        val template = templateStrategyFactory.getStrategy(message.template)

        when(message.type){
            MailType.ADMIN_ALERT -> sendToAdmin(template,message)
            MailType.ALL_ALERT -> sendToAll(template,message)
            MailType.PER_USER_ALERT -> sendToPerUser(template,message)
            MailType.ALL_MARKET_ALERT -> sendToAllMarket(template,message)

        }
    }


    private fun sendToAdmin(template: EmailTemplate,message: MailSendMessage){
        val context = template.setupTemplate(message)
        val body = templateEngine.process(template.fileName, context)

        // 어드민 알림의 경우에는 수신자를 강제로 어드민 이메일로 변경
        message.changeRecipients(adminEmailAddress)

        send(
            body = body,
            message = message
        )
    }

    private  fun sendToAll(template: EmailTemplate,message: MailSendMessage){
        logger.warn { "received sendToAll mail request - not implemented yet"}
    }

    private fun sendToPerUser(template: EmailTemplate,message: MailSendMessage){
        logger.warn { "received sendToPerUser mail request - not implemented yet"}
    }

    private fun sendToAllMarket(template: EmailTemplate,message: MailSendMessage){
        logger.warn { "received sendToAllMarket mail request - not implemented yet"}
    }

    private fun send(
        body: String,
        message: MailSendMessage
    ) {
        val mail = javaMailSender.createMimeMessage()

        val mailHelper = MimeMessageHelper(mail, false, "UTF-8").apply {
            setFrom(fromAddress)
            setTo(message.recipients.toTypedArray())
            setSubject(message.title)
            setText(body, true)
        }

        logger.info { "Sending email to: ${message.recipients}, subject: ${message.title}" }
        javaMailSender.send(mail)
        logger.info { "Email sent to: ${message.recipients}, subject: ${message.title}" }
    }
}