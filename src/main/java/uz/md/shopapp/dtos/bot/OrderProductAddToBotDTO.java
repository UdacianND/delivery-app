package uz.md.shopapp.dtos.bot;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrderProductAddToBotDTO {
    @NotNull(message = "ordered product name can not be null ")
    private String name;
    @NotNull(message = "ordered product count can not be null ")
    private int count;
    @NotNull(message = "ordered product price can not be null ")
    private double price;
}
