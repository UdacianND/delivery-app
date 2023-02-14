package uz.md.shopapp.dtos.user;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserLoginDTO {

    @NotBlank(message = "PhoneNumber can't be empty")
    private String phoneNumber;

    @NotBlank(message = "Sms code can't be empty")
    private String smsCode;

}
