package io.github.nomisrev

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class HealthCheckSpec :
  StringSpec({
    val config = envConfig().copy(dataSource = PostgreSQLContainer.config())
    val module by resource(module(config))

    "healthy" {
      testApplication {
        application { app(module) }
        val response = client.get("/health")
        response.status shouldBe HttpStatusCode.OK
        response.bodyAsText() shouldBe Json.encodeToString(HealthCheck("14.1"))
      }
    }
  })