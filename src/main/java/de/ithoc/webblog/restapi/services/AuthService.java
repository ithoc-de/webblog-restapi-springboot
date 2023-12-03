package de.ithoc.webblog.restapi.services;

import de.ithoc.webblog.restapi.api.AuthClientResponseBody;
import de.ithoc.webblog.restapi.api.ValidationBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthService {

    @Value("${authorization.server.apiKey}")
    private String apiKey;

    private final RestTemplate authClient;

    public AuthService(RestTemplate authClient) {
        this.authClient = authClient;
    }

    public boolean validateToken(String token) {

        if (!token.startsWith("Bearer ")) {
            return false;
        }
        token = token.substring(7);

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", apiKey);
        headers.set(HttpHeaders.CONTENT_TYPE, "application/json");

        ValidationBody validationBody = new ValidationBody();
        validationBody.setAccessToken(token);
        HttpEntity<ValidationBody> httpEntity = new HttpEntity<>(validationBody, headers);

        ResponseEntity<AuthClientResponseBody> responseEntity =
                authClient.postForEntity("/validation", httpEntity, AuthClientResponseBody.class);
        HttpStatusCode statusCode = responseEntity.getStatusCode();

        return statusCode.is2xxSuccessful();
    }

}
