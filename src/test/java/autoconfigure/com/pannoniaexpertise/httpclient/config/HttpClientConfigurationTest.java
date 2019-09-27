package autoconfigure.com.pannoniaexpertise.httpclient.config;

import org.junit.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.web.client.RestOperations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class HttpClientConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(HttpClientConfiguration.class));

    @Test
    public void oAuth2RestTemplate_is_configured_if_user_did_not_provide_any() {
        contextRunner
                .run((context) -> {
                    assertThat(context).hasSingleBean(RestOperations.class);
                    assertThat(context.getBean(RestOperations.class)).isEqualTo(
                            context.getBean(HttpClientConfiguration.class).oAuth2RestTemplate()
                    );
                });
    }

    @Test
    public void auto_configuration_backs_off_if_restOperation_is_provided() {
        contextRunner
                .withUserConfiguration(HttpClientUserTestConfiguration.class)
                .run((context) -> {
                    assertThat(context).hasSingleBean(RestOperations.class);
                    assertThat(context).hasBean("restOperations");
                    assertThat(context).doesNotHaveBean("oAuth2RestTemplate");

                });
    }

    @Test
    public void oAuth2ProtectedResourceDetails_if_user_did_not_provide_any() {

        contextRunner.
                run((context -> {
                    assertThat(context).hasSingleBean(OAuth2ProtectedResourceDetails.class);
                    assertThat(context).getBean(OAuth2ProtectedResourceDetails.class).isEqualTo(
                            context.getBean(HttpClientConfiguration.class).oAuth2ProtectedResourceDetails());
                }));
    }

    @Test
    public void auto_configuration_backs_off_if_oAuth2_is_provided() {
        contextRunner.
                withUserConfiguration(HttpClientUserTestConfiguration.class)
                .run((context -> {
                    assertThat(context).hasSingleBean(OAuth2ProtectedResourceDetails.class);
                    assertThat(context).hasBean("testOAuth2ProtectedResourceDetails");
                    assertThat(context).doesNotHaveBean("oAuth2ProtectedResourceDetails");
                }));
    }

    @Configuration
    static class HttpClientUserTestConfiguration {

        @Bean
        public RestOperations restOperations() {
            return mock(RestOperations.class);
        }

        @Bean
        public OAuth2ProtectedResourceDetails testOAuth2ProtectedResourceDetails() {
            return mock(OAuth2ProtectedResourceDetails.class);
        }
    }

}