package uz.md.shopapp.dtos.product;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ProductDTO {
    private Long id;
    private String nameUz;
    private String nameRu;
    private String imageUrl;
    private String descriptionUz;
    private String descriptionRu;
    private Double price;
    private Long categoryId;
}
