package uz.md.shopapp.dtos.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import uz.md.shopapp.dtos.institution.LocationDto;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AddressAddDTO {

    @NotNull(message = "Uy raqami kiritilishi shart")
    private Integer houseNumber;

    @NotNull(message = "koçha kiritilishi shart")
    private String street;

    @NotBlank(message = "Shahar kiritilishi shart")
    private String city;

    private LocationDto location;

    @NotNull(message = "Foydalanuvchi idsi kiritilishi shart")
    private Long userId;
}
