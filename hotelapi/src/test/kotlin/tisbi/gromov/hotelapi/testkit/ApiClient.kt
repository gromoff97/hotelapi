package tisbi.gromov.hotelapi.testkit

import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity

class BaseUrlProvider(private val context: ServletWebServerApplicationContext) {
    fun baseUrl(): String = "http://localhost:${context.webServer.port}"
}

class ApiClient(
    private val restTemplate: TestRestTemplate,
    private val baseUrlProvider: BaseUrlProvider
) {
    private fun url(path: String): String = baseUrlProvider.baseUrl() + path

    fun <T> get(path: String, clazz: Class<T>): ResponseEntity<T> =
        restTemplate.getForEntity(url(path), clazz)

    fun <T> post(path: String, body: Any?, clazz: Class<T>): ResponseEntity<T> =
        restTemplate.postForEntity(url(path), body, clazz)

    fun <T> put(path: String, body: Any?, clazz: Class<T>): ResponseEntity<T> {
        val entity: HttpEntity<*> = if (body == null) HttpEntity.EMPTY else HttpEntity(body)
        return restTemplate.exchange(url(path), HttpMethod.PUT, entity, clazz)
    }

    fun delete(path: String): ResponseEntity<Void> =
        delete(path, Void::class.java)

    fun <T> delete(path: String, clazz: Class<T>): ResponseEntity<T> =
        restTemplate.exchange(url(path), HttpMethod.DELETE, HttpEntity.EMPTY, clazz)
}
