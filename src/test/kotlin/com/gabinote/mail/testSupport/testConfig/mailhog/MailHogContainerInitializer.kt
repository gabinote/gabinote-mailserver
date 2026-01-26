package com.gabinote.mail.testSupport.testConfig.mailhog


import com.gabinote.mail.testSupport.testConfig.container.ContainerNetworkHelper
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.DockerImageName

private val logger = KotlinLogging.logger {}

class MailHogContainerInitializer :  ApplicationContextInitializer<ConfigurableApplicationContext>  {
    companion object {
        val mailhog = GenericContainer(DockerImageName.parse("mailhog/mailhog:latest")).apply {
            withLabel("test-container", "mailhog")
            withNetwork(ContainerNetworkHelper.testNetwork)
            withNetworkAliases("mailhog")
            withExposedPorts(1025, 8025)
            waitingFor(Wait.forHttp("/api/v2/messages").forPort(8025))
            withReuse(true)
        }
    }

    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        mailhog.start()
        logger.info { "Started KafkaContainerInitializer at ${mailhog.getMappedPort(1025)}, api = http://${mailhog.host}:${mailhog.getMappedPort(8025)}" }
        TestPropertyValues.of(
            "spring.mail.host=${mailhog.host}",
            "spring.mail.port=${mailhog.getMappedPort(1025)}",
            "spring.mail.properties.mail.smtp.auth=false",
            "spring.mail.properties.mail.smtp.starttls.enable=false",
            "test.mailhog.api-url=http://${mailhog.host}:${mailhog.getMappedPort(8025)}"
        ).applyTo(applicationContext.environment)
    }

}