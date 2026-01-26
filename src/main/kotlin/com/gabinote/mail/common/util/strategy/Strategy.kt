package com.gabinote.mail.common.util.strategy

interface Strategy<T : Enum<T>> {
    val type: T
}