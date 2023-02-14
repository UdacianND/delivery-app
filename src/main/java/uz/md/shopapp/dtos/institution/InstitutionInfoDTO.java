package uz.md.shopapp.dtos.institution;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class InstitutionInfoDTO {
    private Long id;
    private String nameUz;
    private String nameRu;
    private String imageUrl;
    private String descriptionUz;
    private String descriptionRu;
}
