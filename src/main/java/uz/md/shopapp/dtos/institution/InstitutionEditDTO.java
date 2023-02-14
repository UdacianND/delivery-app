package uz.md.shopapp.dtos.institution;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class InstitutionEditDTO extends InstitutionAddDTO {

    @NotNull(message = " category id must not be null")
    private Long id;

    public InstitutionEditDTO(@NotBlank(message = "Category name must not be empty") String nameUz,
                              @NotBlank(message = "Category name must not be empty") String nameRu,
                              MultipartFile image,
                              String descriptionUz,
                              String descriptionRu,
                              Long id) {
        super(nameUz, nameRu, image, descriptionUz, descriptionRu);
        this.id = id;
    }

    public InstitutionEditDTO(Long id) {
        this.id = id;
    }
}
