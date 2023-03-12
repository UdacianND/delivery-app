package uz.md.shopapp.dtos.category;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CategoryAddDTO {

    @NotBlank(message = "Category name must not be empty")
    private String nameUz;

    @NotBlank(message = "Category name must not be empty")
    private String nameRu;

    private String descriptionUz;
    private String descriptionRu;

    @NotNull(message = "Kategoriya muassasa idsi bolishi shart")
    private Long institutionId;

}
