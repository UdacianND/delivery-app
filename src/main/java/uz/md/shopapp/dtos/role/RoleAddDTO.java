package uz.md.shopapp.dtos.role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uz.md.shopapp.domain.enums.PermissionEnum;

import java.util.Set;

@Getter
@AllArgsConstructor
public class RoleAddDTO {

    @NotBlank(message = "Role name must not be null")
    private String name;
    private String description;

    @NotNull
    @NotEmpty
    private Set<PermissionEnum> permissions;
}
