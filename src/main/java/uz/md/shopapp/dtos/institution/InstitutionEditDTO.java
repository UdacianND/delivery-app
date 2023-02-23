package uz.md.shopapp.dtos.institution;


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
public class InstitutionEditDTO extends InstitutionAddDTO {

    @NotNull(message = " category id must not be null")
    private Long id;

    public InstitutionEditDTO(@NotBlank(message = "Category name must not be empty") String nameUz,
                              @NotBlank(message = "Category name must not be empty") String nameRu,
                              String descriptionUz,
                              String descriptionRu,
                              Long id,
                              LocationDto location,
                              Long institutionTypeId,
                              Long managerId) {
        super(nameUz, nameRu, descriptionUz, descriptionRu, location, institutionTypeId, managerId);
        this.id = id;
    }

    public InstitutionEditDTO(Long id) {
        this.id = id;
    }
}
