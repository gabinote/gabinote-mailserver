package com.gabinote.mail.testSupport.testUtil.time

import com.gabinote.mail.common.util.time.TimeProvider
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class TestTimeConfig {

    @Bean
    fun timeProvider(): TimeProvider {
        return TestTimeProvider()
    }
}