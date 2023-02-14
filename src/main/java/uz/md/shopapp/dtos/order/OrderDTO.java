package uz.md.shopapp.dtos.order;


import lombok.*;
import uz.md.shopapp.domain.enums.OrderStatus;
import uz.md.shopapp.dtos.address.AddressDTO;
import uz.md.shopapp.dtos.orderProduct.OrderProductDTO;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrderDTO {
    private Long id;
    private Long userId;
    private OrderStatus status;
    private Double overallPrice;
    private AddressDTO address;
    private List<OrderProductDTO> orderProducts;
}
