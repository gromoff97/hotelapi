package tisbi.gromov.hotelapi.config

import org.flywaydb.core.Flyway
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.flyway.FlywayDataSource
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import javax.sql.DataSource

@Configuration
class DataAccessConfig(
    // central
    @param:Value("\${spring.datasource.url}") private val centralUrl: String,
    @param:Value("\${spring.datasource.username}") private val centralUser: String,
    @param:Value("\${spring.datasource.password}") private val centralPass: String,

    // branch1
    @param:Value("\${BRANCH1_DB_URL}") private val branch1Url: String,
    @param:Value("\${BRANCH1_DB_USER}") private val branch1User: String,
    @param:Value("\${BRANCH1_DB_PASS}") private val branch1Pass: String,

    // branch2
    @param:Value("\${BRANCH2_DB_URL}") private val branch2Url: String,
    @param:Value("\${BRANCH2_DB_USER}") private val branch2User: String,
    @param:Value("\${BRANCH2_DB_PASS}") private val branch2Pass: String,
) {

    // ---------- DataSources ----------

    /**
     * Центральный DataSource:
     * - @Primary: чтобы его брали по умолчанию
     * - @FlywayDataSource: чтобы Spring Boot Flyway мигрировал именно central (locations = central)
     */
    @Bean("centralDataSource")
    @Primary
    @FlywayDataSource
    fun centralDataSource(): DataSource =
        DataSourceBuilder.create()
            .driverClassName("org.postgresql.Driver")
            .url(centralUrl)
            .username(centralUser)
            .password(centralPass)
            .build()

    @Bean("branch1DataSource")
    fun branch1DataSource(): DataSource =
        DataSourceBuilder.create()
            .driverClassName("org.postgresql.Driver")
            .url(branch1Url)
            .username(branch1User)
            .password(branch1Pass)
            .build()

    @Bean("branch2DataSource")
    fun branch2DataSource(): DataSource =
        DataSourceBuilder.create()
            .driverClassName("org.postgresql.Driver")
            .url(branch2Url)
            .username(branch2User)
            .password(branch2Pass)
            .build()

    // ---------- jOOQ DSLContexts ----------

    /**
     * Центральный DSLContext — по умолчанию для central-репозиториев.
     */
    @Bean("centralDsl")
    @Primary
    fun centralDsl(@Qualifier("centralDataSource") ds: DataSource): DSLContext =
        DSL.using(ds, SQLDialect.POSTGRES)

    @Bean("branch1Dsl")
    fun branch1Dsl(@Qualifier("branch1DataSource") ds: DataSource): DSLContext =
        DSL.using(ds, SQLDialect.POSTGRES)

    @Bean("branch2Dsl")
    fun branch2Dsl(@Qualifier("branch2DataSource") ds: DataSource): DSLContext =
        DSL.using(ds, SQLDialect.POSTGRES)

    // ---------- Flyway for branches (manual) ----------

    /**
     * Миграции для branch БД делаем вручную, чтобы:
     * - central Flyway оставался авто-конфигом Spring Boot
     * - branch миграции гонялись на обеих branch БД
     */
    @Bean
    fun branchMigrationsRunner(
        @Qualifier("branch1DataSource") ds1: DataSource,
        @Qualifier("branch2DataSource") ds2: DataSource,
    ): ApplicationRunner = ApplicationRunner {
        migrateBranch(ds1)
        migrateBranch(ds2)
    }

    private fun migrateBranch(dataSource: DataSource) {
        Flyway.configure()
            .dataSource(dataSource)
            .locations("classpath:db/migration/branch")
            .load()
            .migrate()
    }
}