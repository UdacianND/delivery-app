package uz.md.shopapp.dtos.institution;


import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class InstitutionAddDTO {

    @NotBlank(message = "Category name must not be empty")
    private String nameUz;

    @NotBlank(message = "Category name must not be empty")
    private String nameRu;

    private MultipartFile image;

    private String descriptionUz;
    private String descriptionRu;
}
