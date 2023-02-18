package uz.md.shopapp.dtos.category;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CategoryInfoDTO {
    private Long id;
    private String nameUz;
    private String nameRu;
    private String descriptionUz;
    private String descriptionRu;
    private Long institutionId;
}
