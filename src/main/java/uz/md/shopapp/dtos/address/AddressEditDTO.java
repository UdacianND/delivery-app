package uz.md.shopapp.dtos.address;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AddressEditDTO extends AddressAddDTO {

    @NotNull(message = "address id must be specified")
    private Long id;
}
