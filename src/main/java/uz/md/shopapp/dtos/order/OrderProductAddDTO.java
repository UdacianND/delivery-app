package uz.md.shopapp.dtos.order;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrderProductAddDTO {

    @NotNull(message = "ordered product id must not be null ")
    private Long productId;
    @NotNull(message = "order product quantity must not be null ")
    private Integer quantity;
}
