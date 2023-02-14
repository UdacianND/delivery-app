package uz.md.shopapp.dtos.institution;

import lombok.*;
import uz.md.shopapp.dtos.category.CategoryDTO;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class InstitutionDTO {
    private Long id;
    private String nameUz;
    private String nameRu;
    private String imageUrl;
    private String descriptionUz;
    private String descriptionRu;
    private List<CategoryDTO> categories;

    public InstitutionDTO(Long id,
                          String nameUz,
                          String nameRu,
                          String descriptionUz,
                          String descriptionRu,
                          List<CategoryDTO> categories) {
        this.id = id;
        this.nameUz = nameUz;
        this.nameRu = nameRu;
        this.descriptionUz = descriptionUz;
        this.descriptionRu = descriptionRu;
        this.categories = categories;
    }
}
