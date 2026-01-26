package com.gabinote.mail.testSupport.testTemplate

import com.fasterxml.jackson.databind.ObjectMapper
import com.gabinote.mail.testSupport.testConfig.kafka.KafkaContainerInitializer
import com.gabinote.mail.testSupport.testConfig.mailhog.MailHogContainerInitializer
import com.gabinote.mail.testSupport.testUtil.kafka.TestKafkaHelper
import com.gabinote.mail.testSupport.testUtil.mailHog.TestMailHogHelper
import com.gabinote.mail.testSupport.testUtil.time.TestTimeProvider
import com.gabinote.mail.testSupport.testUtil.uuid.TestUuidSource
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.core.test.TestCaseOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.annotation.Import
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.junit.jupiter.Testcontainers

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(
    initializers = [MailHogContainerInitializer::class, KafkaContainerInitializer::class],
)
@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
@Import(
    TestUuidSource::class,
    TestTimeProvider::class,
    TestKafkaHelper::class,
    TestMailHogHelper::class,

)
@Testcontainers
@SpringBootTest
abstract class IntegrationTestTemplate : FeatureSpec() {

    @Autowired
    lateinit var objectMapper: ObjectMapper
    @Autowired
    lateinit var  testMailHogHelper: TestMailHogHelper

    @Autowired
    lateinit var testKafkaHelper: TestKafkaHelper

    override fun testCaseOrder(): TestCaseOrder = TestCaseOrder.Random

    init {

        beforeTest{
            testKafkaHelper.deleteAllTopics()
        }

    }
}