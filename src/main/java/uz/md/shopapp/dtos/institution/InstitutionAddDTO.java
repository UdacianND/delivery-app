package uz.md.shopapp.dtos.institution;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class InstitutionAddDTO {

    @NotBlank(message = "Institution name must not be empty")
    private String nameUz;

    @NotBlank(message = "Institution name must not be empty")
    private String nameRu;
    private String descriptionUz;
    private String descriptionRu;

    @NotNull(message = "Institution location must not be null")
    private LocationDto location;

    @NotNull(message = "Institution type id must not be null")
    private Long institutionTypeId;
    @NotNull(message = "Institution manager id must not be null")
    private Long managerId;
}
