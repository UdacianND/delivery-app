package uz.md.shopapp.client.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SendRequest {

    private String mobilePhone;
    private String message;
    private float from;
    private String callback_url;

}
