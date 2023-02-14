package uz.md.shopapp.dtos.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AddressAddDTO {

    @NotNull(message = "houseNumber must not be null")
    private Integer houseNumber;

    @NotNull(message = "street must not be blank")
    private String street;

    @NotBlank(message = "city must not be blank")
    private String city;

    @NotNull(message = "user id must not be null")
    private Long userId;
}
