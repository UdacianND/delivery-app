package uz.md.shopapp.dtos.user;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ClientRegisterDTO {

    private String firstName;

    private String lastName;

    @NotBlank(message = "PhoneNumber must not be empty")
    private String phoneNumber;

}
