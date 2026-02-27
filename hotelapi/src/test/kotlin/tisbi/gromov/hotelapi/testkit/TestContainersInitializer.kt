package tisbi.gromov.hotelapi.testkit

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ApplicationListener
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.event.ContextClosedEvent
import org.testcontainers.containers.PostgreSQLContainer

class TestContainersInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        val central: PostgreSQLContainer<*> =
            PostgreSQLContainer("postgres:16").withDatabaseName("central_db")
        val branch1: PostgreSQLContainer<*> =
            PostgreSQLContainer("postgres:16").withDatabaseName("branch1_db")
        val branch2: PostgreSQLContainer<*> =
            PostgreSQLContainer("postgres:16").withDatabaseName("branch2_db")

        central.start()
        branch1.start()
        branch2.start()

        TestPropertyValues.of(
            "spring.datasource.url=${central.jdbcUrl}",
            "spring.datasource.username=${central.username}",
            "spring.datasource.password=${central.password}",
            "BRANCH1_DB_URL=${branch1.jdbcUrl}",
            "BRANCH1_DB_USER=${branch1.username}",
            "BRANCH1_DB_PASS=${branch1.password}",
            "BRANCH2_DB_URL=${branch2.jdbcUrl}",
            "BRANCH2_DB_USER=${branch2.username}",
            "BRANCH2_DB_PASS=${branch2.password}"
        ).applyTo(applicationContext.environment)

        applicationContext.addApplicationListener(ApplicationListener<ContextClosedEvent> {
            if (central.isRunning) central.stop()
            if (branch1.isRunning) branch1.stop()
            if (branch2.isRunning) branch2.stop()
        })
    }
}
