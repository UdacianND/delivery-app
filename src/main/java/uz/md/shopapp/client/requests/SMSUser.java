package uz.md.shopapp.client.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SMSUser {
    private Long id;
    private String name;
    private String email;
    private String role;
    private String api_token;
    private String status;
    private String sms_api_login;
    private String sms_api_password;
    private float uz_price;
    private float ucell_price;
    private float test_ucell_price;
    private float balance;
    private float is_vip;
    private String host;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
