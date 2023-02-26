package uz.md.shopapp.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Configuration
public class Config {

    @Bean
    public RestTemplate getRestTemplate() {

        RestTemplate restTemplate;
        if (log.isDebugEnabled()) {
            SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
            restTemplate = new RestTemplate(requestFactory);
        } else {
            restTemplate = new RestTemplate();
        }

        return restTemplate;
    }


}
