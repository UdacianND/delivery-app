package uz.md.shopapp.dtos.institutionType;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class InstitutionTypeEditDTO extends InstitutionTypeAddDTO {

    @NotNull(message = " category id must not be null")
    private Long id;

    public InstitutionTypeEditDTO(@NotBlank(message = "Category name must not be empty") String nameUz,
                                  @NotBlank(message = "Category name must not be empty") String nameRu,
                                  String descriptionUz,
                                  String descriptionRu,
                                  Long id) {
        super(nameUz, nameRu, descriptionUz, descriptionRu);
        this.id = id;
    }
}
