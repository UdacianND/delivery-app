package uz.md.shopapp.dtos.institutionType;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class InstitutionTypeAddDTO {

    @NotBlank(message = "Category name must not be empty")
    private String nameUz;

    @NotBlank(message = "Category name must not be empty")
    private String nameRu;

    private String descriptionUz;
    private String descriptionRu;
}
