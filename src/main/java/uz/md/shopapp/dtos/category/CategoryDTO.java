package uz.md.shopapp.dtos.category;

import lombok.*;
import uz.md.shopapp.dtos.product.ProductDTO;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CategoryDTO {
    private Long id;
    private String nameUz;
    private String nameRu;
    private String descriptionUz;
    private String descriptionRu;
    private Long institutionId;
    private List<ProductDTO> products;

    public CategoryDTO(String nameUz, String nameRu, String descriptionUz, String descriptionRu) {
        this.nameUz = nameUz;
        this.nameRu = nameRu;
        this.descriptionUz = descriptionUz;
        this.descriptionRu = descriptionRu;
    }
}
