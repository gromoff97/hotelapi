package tisbi.gromov.hotelapi.testkit

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext
import org.springframework.context.annotation.Bean

@TestConfiguration
class TestApiClientConfig {
    @Bean
    fun baseUrlProvider(context: ServletWebServerApplicationContext): BaseUrlProvider =
        BaseUrlProvider(context)

    @Bean
    fun apiClient(
        restTemplate: TestRestTemplate,
        baseUrlProvider: BaseUrlProvider
    ): ApiClient =
        ApiClient(restTemplate, baseUrlProvider)

    @Bean
    fun centralRestApiClient(apiClient: ApiClient): CentralRestApiClient =
        CentralRestApiClient(apiClient)

    @Bean("branch1ApiClient")
    fun branch1ApiClient(apiClient: ApiClient): BranchApiClient =
        BranchApiClient(apiClient, "branch1")

    @Bean("branch2ApiClient")
    fun branch2ApiClient(apiClient: ApiClient): BranchApiClient =
        BranchApiClient(apiClient, "branch2")
}
