package autoconfigure.com.pannoniaexpertise.httpclient.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.web.client.RestOperations;

@Configuration
public class HttpClientConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConfigurationProperties("authorization.server")
    public OAuth2ProtectedResourceDetails oAuth2ProtectedResourceDetails() {
        return new ClientCredentialsResourceDetails();
    }

    @Bean
    @ConditionalOnMissingBean
    public RestOperations oAuth2RestTemplate() {
        return new OAuth2RestTemplate(oAuth2ProtectedResourceDetails());
    }

}
