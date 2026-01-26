package com.gabinote.mail.mail.mapping

import com.gabinote.mail.mail.dto.event.MailSendEvent
import com.gabinote.mail.mail.dto.service.MailSendReqServiceDto
import com.gabinote.mail.mail.enums.MailTemplate
import com.gabinote.mail.mail.enums.MailType
import org.mapstruct.Mapper

@Mapper(
    componentModel = "spring"
)
interface MailMapper {

    fun toEvent(dto: MailSendReqServiceDto,serviceName:String): MailSendEvent

    fun typeToString(type: MailType): String {
        return type.value
    }

    fun templateToString(template: MailTemplate): String {
        return template.value
    }
}