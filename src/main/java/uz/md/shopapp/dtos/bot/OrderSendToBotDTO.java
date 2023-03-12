package uz.md.shopapp.dtos.bot;


import jakarta.validation.constraints.NotNull;
import lombok.*;
import uz.md.shopapp.dtos.institution.LocationDto;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrderSendToBotDTO {

    @NotNull(message = "phone number must not be null")
    private String clientPhoneNumber;
    @NotNull(message = "location must not be null")
    private LocationDto location;
    private LocalDateTime deliveryTime = LocalDateTime.now();
    @NotNull(message = "ordered products must not be null")
    private List<OrderProductAddToBotDTO> orderProducts;

    @NotNull(message = "total price must not be null")
    private double totalPrice;
}
