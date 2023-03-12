package uz.md.shopapp.client;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import uz.md.shopapp.client.repo.SmsTokenRepository;
import uz.md.shopapp.client.requests.*;
import uz.md.shopapp.exceptions.BadRequestException;
import uz.md.shopapp.exceptions.NotFoundException;

import java.util.List;

@Service
public class SmsSenderImpl implements SmsSender {

    private final RestTemplate restTemplate;
    private final SmsTokenRepository smsTokenRepository;

    public SmsSenderImpl(RestTemplate restTemplate,
                         SmsTokenRepository smsTokenRepository) {
        this.restTemplate = restTemplate;
        this.smsTokenRepository = smsTokenRepository;
    }

    @Override
    public ResponseEntity<TokenResult> login(LoginRequest loginRequest) {

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(BASE_URL + "/auth/login")
                .queryParam("email", loginRequest.getEmail())
                .queryParam("password", loginRequest.getPassword());

        ResponseEntity<TokenResult> exchange = restTemplate
                .exchange(builder.toUriString(),
                        HttpMethod.POST,
                        HttpEntity.EMPTY,
                        TokenResult.class);
        TokenResult body = exchange.getBody();

        if (body == null)
            throw BadRequestException.builder()
                    .messageUz("Nimadir xato")
                    .messageRu("")
                    .build();

        smsTokenRepository.save(body.getData());
        return exchange;
    }

    @Override
    public ResponseEntity<SMSUser> getUser() {

        List<SmsToken> all = smsTokenRepository.findAll();

        if (all.size() == 0)
            throw NotFoundException.builder()
                    .messageUz("SMS tokenlar topilmadi")
                    .messageRu("")
                    .build();

        SmsToken smsToken = all.get(0);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + smsToken.getToken());

        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        return restTemplate
                .exchange(BASE_URL + "/auth/user",
                        HttpMethod.GET,
                        entity,
                        SMSUser.class);

    }

    @Override
    public ResponseEntity<SMSResult> sendSms(SendRequest sendRequest) {

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(BASE_URL + "/message/sms/send")
                .queryParam("mobile_phone", sendRequest.getMobilePhone())
                .queryParam("message", sendRequest.getMessage())
                .queryParam("from", sendRequest.getFrom())
                .queryParam("callback_url", sendRequest.getCallback_url());

        SmsToken smsToken = smsTokenRepository.findAll().get(0);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + smsToken.getToken());
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        return restTemplate
                .exchange(builder.toUriString(),
                        HttpMethod.POST,
                        entity,
                        SMSResult.class);
    }

}
