package uz.md.shopapp.dtos.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.md.shopapp.domain.enums.PermissionEnum;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ClientDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Set<PermissionEnum> permissions;

}
