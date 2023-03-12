package uz.md.shopapp.component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.md.shopapp.client.SmsSender;
import uz.md.shopapp.client.requests.LoginRequest;
import uz.md.shopapp.domain.*;
import uz.md.shopapp.domain.enums.PermissionEnum;
import uz.md.shopapp.repository.*;
import uz.md.shopapp.service.contract.FilesStorageService;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

import static uz.md.shopapp.domain.enums.PermissionEnum.*;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile(value = {"test", "dev"})
public class DataLoader implements CommandLineRunner {

    private final LocationRepository locationRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final InstitutionRepository institutionRepository;
    private final InstitutionTypeRepository institutionTypeRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FilesStorageService filesStorageService;
    private final SmsSender smsSender;

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

    @Value("${app.sms.sender-email}")
    private String senderEmail;

    @Value("${app.sms.sender-password}")
    private String senderPassword;

    @Override
    public void run(String... args) {
        filesStorageService.init();
        System.out.println("activeProfile = " + activeProfile);
        if (Objects.equals("create", modeType)) {
            smsSender.login(LoginRequest
                    .builder()
                    .email(senderEmail)
                    .password(senderPassword)
                    .build());
            addAdmin();
            saveManagerRole();
            saveClientRole();
            if (!activeProfile.equals("test")) {
                initLocations();
                initInstitutionTypes();
                initInstitutions();
                initCategories();
                initProducts();
            }
        }
    }

    private void initLocations() {
        locationRepository.saveAndFlush(new Location(15.0, 15.0));
        locationRepository.saveAndFlush(new Location(19.0, 19.0));
        locationRepository.saveAndFlush(new Location(29.0, 29.0));
        locationRepository.saveAndFlush(new Location(87.0, 87.0));
    }

    private void initProducts() {
        Random random;
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 3; j++) {
                random = new Random();
                productRepository.save(new Product(
                        "nameUz" + i + "-" + j,
                        "NameRu" + i + "-" + j,
                        "",
                        "description",
                        "description",
                        Math.round(random.nextDouble() * 500) + 100.0,
                        categoryRepository.findById(i + 1L).orElseThrow()
                ));
            }
        }
    }

    private void initCategories() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                categoryRepository.save(new Category(
                        "nameUz" + i + "-" + j,
                        "NameRu" + i + "-" + j,
                        "description",
                        "description",
                        null,
                        institutionRepository.findById(i + 1L).orElseThrow()));
            }
        }
    }

    private void initInstitutions() {
        List<Location> locations = locationRepository.findAll();
        institutionRepository.saveAll(List.of(
                new Institution("Max Way", "Max Way", "", "",
                        locations.get(0),
                        institutionTypeRepository.findById(1L).orElseThrow(),
                        userRepository.findById(1L).orElseThrow()
                ),

                new Institution("Korzinka", "Korzinka", "", "",
                        locations.get(1),
                        institutionTypeRepository.findById(3L).orElseThrow(),
                        userRepository.findById(1L).orElseThrow()
                ),

                new Institution("Shopping", "Shopping", "", "",
                        locations.get(2),
                        institutionTypeRepository.findById(4L).orElseThrow(),
                        userRepository.findById(1L).orElseThrow()
                ),

                new Institution("Moida By Azan", "Moida By Azan", "", "",
                        locations.get(3),
                        institutionTypeRepository.findById(2L).orElseThrow(),
                        userRepository.findById(1L).orElseThrow()
                )
        ));
    }

    private void initInstitutionTypes() {
        institutionTypeRepository.saveAll(List.of(
                new InstitutionType("Restaurant", "Restaurant", "All restaurants", ""),
                new InstitutionType("Cafe", "Cafe", "All restaurants", ""),
                new InstitutionType("Market", "Market", "All restaurants", ""),
                new InstitutionType("MiniMarket", "MiniMarket", "All restaurants", "")
        ));
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

    private @NotNull Role addAdminRole() {
        return roleRepository.save(
                new Role(adminRoleName,
                        adminRoleDescription,
                        Set.of(PermissionEnum.values())
                )
        );
    }

}
