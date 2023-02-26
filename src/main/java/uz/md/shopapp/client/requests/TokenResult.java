package uz.md.shopapp.client.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TokenResult {
    private String message;
    private SmsToken data;
    private String tokenType;
}
