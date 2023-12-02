package de.ithoc.webblog.restapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class RestClient {

    @Value("${authorization.server.baseUrl}")
    private String authServerBaseUrl;

    @Bean
    public RestTemplate authClient() {

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(authServerBaseUrl));

        return restTemplate;
    }

}
