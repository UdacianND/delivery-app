package uz.md.shopapp.dtos.orderProduct;

import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.*;
import uz.md.shopapp.dtos.product.ProductDTO;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrderProductDTO {
    private Long id;
    private Long orderId;
    private ProductDTO product;
    private Integer quantity;
    private Long price;
}
