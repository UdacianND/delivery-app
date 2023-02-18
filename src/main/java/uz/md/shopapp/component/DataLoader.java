package uz.md.shopapp.component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.md.shopapp.domain.Role;
import uz.md.shopapp.domain.User;
import uz.md.shopapp.domain.enums.PermissionEnum;
import uz.md.shopapp.repository.RoleRepository;
import uz.md.shopapp.repository.UserRepository;
import uz.md.shopapp.service.contract.FilesStorageService;

import java.util.Objects;
import java.util.Set;

import static uz.md.shopapp.domain.enums.PermissionEnum.*;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(
        prefix = "simulation",
        name = "dataloader",
        havingValue = "true",
        matchIfMissing = true
)
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FilesStorageService filesStorageService;

    @Value("${app.admin.firstName}")
    private String firstName;

    @Value("${app.admin.lastName}")
    private String lastName;

    @Value("${app.admin.phoneNumber}")
    private String phoneNumber;

    @Value("${app.role.admin.name}")
    private String adminRoleName;

    @Value("${app.role.admin.description}")
    private String adminRoleDescription;

    @Value("${app.role.manager.name}")
    private String managerRoleName;

    @Value("${app.role.manager.description}")
    private String managerRoleDescription;

    @Value("${app.role.client.name}")
    private String clientRoleName;

    @Value("${app.role.client.description}")
    private String clientRoleDescription;

    @Value("${app.admin.password}")
    private String password;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String modeType;

    @Value("${app.running}")
    private String activeProfile;

    @Override
    public void run(String... args) {
        filesStorageService.init();
        System.out.println("activeProfile = " + activeProfile);
        if (Objects.equals("create", modeType)) {
            addAdmin();
            saveManagerRole();
            saveClientRole();
        }
    }

    private void saveManagerRole() {
        roleRepository.save(
                new Role(managerRoleName,
                        managerRoleDescription,
                        Set.of(ADD_PRODUCT,
                                GET_PRODUCT,
                                DELETE_PRODUCT,
                                EDIT_PRODUCT,
                                ADD_CATEGORY,
                                GET_CATEGORY,
                                DELETE_CATEGORY,
                                EDIT_CATEGORY,
                                ADD_ORDER,
                                GET_ORDER,
                                DELETE_ORDER,
                                EDIT_ORDER)));
    }

    private void saveClientRole() {
        roleRepository.save(
                new Role(clientRoleName,
                        clientRoleDescription,
                        Set.of(GET_PRODUCT, GET_CATEGORY, GET_ORDER)
                )
        );
    }


    private void addAdmin() {
        userRepository.save(new User(
                firstName,
                lastName,
                phoneNumber,
                passwordEncoder.encode(password),
                addAdminRole()
        ));
    }

    private Role addAdminRole() {
        return roleRepository.save(
                new Role(adminRoleName,
                        adminRoleDescription,
                        Set.of(PermissionEnum.values())
                )
        );
    }


}
