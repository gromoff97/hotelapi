val jooqOutputDir = file("src/main/generated/jooq")

plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.5.10"
	id("io.spring.dependency-management") version "1.1.7"
    id("org.jooq.jooq-codegen-gradle") version "3.19.29"
}

group = "tisbi.gromov"
version = "0.0.1-SNAPSHOT"
description = "Hotel Chain Api"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-jooq")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("com.scalar.maven:scalar:0.4.4")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.flywaydb:flyway-core")
	implementation("org.flywaydb:flyway-database-postgresql")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	runtimeOnly("org.postgresql:postgresql")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    jooqCodegen("org.postgresql:postgresql:42.7.9")
}

sourceSets {
    main {
        java.srcDir(jooqOutputDir)
    }
}

kotlin {
	jvmToolchain(21)
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

jooq {
    executions {

        // 1) CENTRAL: генерируем классы по central_db
        create("central") {
            configuration {
                jdbc {
                    driver = "org.postgresql.Driver"
                    url = "jdbc:postgresql://localhost:5432/central_db"
                    user = "central_user"
                    password = "central_pass"
                }

                generator {
                    database {
                        name = "org.jooq.meta.postgres.PostgresDatabase"
                        inputSchema = "public"
                        includes = ".*"
                        excludes = "flyway_schema_history"
                    }

                    target {
                        packageName = "tisbi.gromov.hotelapi.jooq.central"
                        directory = jooqOutputDir.path
                    }
                }
            }
        }

        // 2) BRANCH: генерируем по branch1_db (schema одинаковая для branch1/branch2)
        create("branch") {
            configuration {
                jdbc {
                    driver = "org.postgresql.Driver"
                    url = "jdbc:postgresql://localhost:5433/branch1_db"
                    user = "branch_user"
                    password = "branch_pass"
                }

                generator {
                    database {
                        name = "org.jooq.meta.postgres.PostgresDatabase"
                        inputSchema = "public"
                        includes = ".*"
                        excludes = "flyway_schema_history"
                    }

                    target {
                        packageName = "tisbi.gromov.hotelapi.jooq.branch"
                        directory = jooqOutputDir.path
                    }
                }
            }
        }
    }
}
