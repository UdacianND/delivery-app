package uz.md.shopapp.dtos.order;


import jakarta.validation.constraints.NotNull;
import lombok.*;
import uz.md.shopapp.dtos.address.AddressAddDTO;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrderAddDTO {

    @NotNull(message = "order user id must not be null")
    private UUID userId;
    private AddressAddDTO address;
    private Long addressId;
    private Double overallPrice;

    @NotNull(message = "ordered products must not be null")
    private List<OrderProductAddDTO> orderProducts;

}
