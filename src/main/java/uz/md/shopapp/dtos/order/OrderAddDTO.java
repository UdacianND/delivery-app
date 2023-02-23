package uz.md.shopapp.dtos.order;


import jakarta.validation.constraints.NotNull;
import lombok.*;
import uz.md.shopapp.dtos.address.AddressAddDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrderAddDTO {

    @NotNull(message = "order user id must not be null")
    private Long userId;
    private AddressAddDTO address;
    private Long addressId;
    private LocalDateTime deliveryTime;

    @NotNull(message = "ordered products must not be null")
    private List<OrderProductAddDTO> orderProducts;

}
