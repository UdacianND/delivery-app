package uz.md.shopapp.component;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.md.shopapp.domain.*;
import uz.md.shopapp.domain.enums.PermissionEnum;
import uz.md.shopapp.repository.*;

import java.util.ArrayList;
import java.util.List;
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
@Profile("!test")
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.firstName}")
    private String firstName;

    @Value("${app.admin.phoneNumber}")
    private String phoneNumber;

    @Value("${app.admin.password}")
    private String password;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String modeType;

    @Value("${app.running}")
    private String activeProfile;
    private List<Category> categories;
    private List<Product> products;
    private Role userRole;
    private List<User> users;
    private List<Address> addresses;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final AddressRepository addressRepository;

    @Override
    public void run(String... args) {
        System.out.println("activeProfile = " + activeProfile);
        if (Objects.equals("create", modeType) && !Objects.equals("test", activeProfile)) {
            addAdmin();
            saveManagerRole();
            saveUserRole();
        }
    }

    private void saveManagerRole() {
        userRole = roleRepository.save(
                new Role("MANAGER",
                        "Institution MANAGER",
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

    private void saveUserRole() {
        userRole = roleRepository.save(
                new Role("USER",
                        "System USER",
                        Set.of(GET_PRODUCT, GET_CATEGORY, GET_ORDER)
                )
        );
    }


    private void addAdmin() {
        userRepository.save(new User(
                firstName,
                "",
                phoneNumber,
                passwordEncoder.encode(password),
                addAdminRole()
        ));
    }

    private Role addAdminRole() {
        return roleRepository.save(
                new Role("ADMIN",
                        "System owner",
                        Set.of(PermissionEnum.values())
                )
        );
    }


}
