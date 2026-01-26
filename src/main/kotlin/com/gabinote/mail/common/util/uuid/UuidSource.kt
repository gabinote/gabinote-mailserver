package com.gabinote.mail.common.util.uuid

import java.util.*


interface UuidSource {
    fun generateUuid(): UUID
}