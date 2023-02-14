package uz.md.shopapp.dtos.orderProduct;

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
    private int quantity;
    private double price;
}
