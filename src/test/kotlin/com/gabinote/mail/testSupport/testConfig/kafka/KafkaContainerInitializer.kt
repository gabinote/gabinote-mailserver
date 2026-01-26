package com.gabinote.mail.testSupport.testConfig.kafka

import com.gabinote.mail.testSupport.testConfig.container.ContainerNetworkHelper
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.kafka.ConfluentKafkaContainer
import org.testcontainers.utility.DockerImageName


private val logger = KotlinLogging.logger {}

class KafkaContainerInitializer: ApplicationContextInitializer<ConfigurableApplicationContext> {
    companion object {
        val kafka = ConfluentKafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:8.0.3")).apply {
            withNetwork(ContainerNetworkHelper.testNetwork)
            withNetworkAliases("kafka")
            withLabel("test-container", "kafka")
            withReuse(true)
        }
    }

    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        kafka.start()
        logger.info { "Started KafkaContainerInitializer at ${kafka.bootstrapServers}" }
        TestPropertyValues.of(
            "spring.kafka.bootstrap-servers=${kafka.bootstrapServers}"
        ).applyTo(applicationContext.environment)
    }
}