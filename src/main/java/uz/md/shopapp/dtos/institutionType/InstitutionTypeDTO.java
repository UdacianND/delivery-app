package uz.md.shopapp.dtos.institutionType;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class InstitutionTypeDTO {
    private Long id;
    private String nameUz;
private String nameRu;
private String descriptionUz;
    private String descriptionRu;


}
