package uz.md.shopapp.dtos.role;

import lombok.*;
import uz.md.shopapp.domain.enums.PermissionEnum;

import java.util.Set;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class RoleDTO {

    private Integer id;

    private String nameUz;
    private String nameRu;

    private String descriptionUz;
    private String descriptionRu;

    private Set<PermissionEnum> permissions;
}
