package uz.md.shopapp.client;

import org.springframework.http.ResponseEntity;
import uz.md.shopapp.client.requests.*;

public interface SmsSender {

    String BASE_URL = "https://notify.eskiz.uz/api";

    ResponseEntity<TokenResult> login(LoginRequest loginRequest);

    ResponseEntity<SMSUser> getUser();

    ResponseEntity<SMSResult> sendSms(SendRequest sendRequest);

}
