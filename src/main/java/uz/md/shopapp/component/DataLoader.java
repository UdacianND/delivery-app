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
            }
        }
    }

    private void initLocations() {
        locationRepository.saveAndFlush(new Location(15.0, 15.0));
        locationRepository.saveAndFlush(new Location(19.0, 19.0));
        locationRepository.saveAndFlush(new Location(29.0, 29.0));
        locationRepository.saveAndFlush(new Location(87.0, 87.0));
    }

    private void initCategories() {
        Random random = new Random();
        String[] categories = {"Burger", "Ichimliklar", "Pitsa", "Lavash"};
        String[][] products = {
                {"Big burger", "Pishloqli burger", "Kichik burger", "Double burger"},
                {"Choy", "Coca-cola", "Pepsi", "Mirinda"},
                {"Pepperoni", "Pishloqli", "Qazili", "Kolbasali"},
                {"Mini lavash", "Standard", "Pishloqli lavash", "Tandir lavash"}
        };

        String[][] images = {
                {
                    "https://www.osieurope.com/wp-content/uploads/2022/05/My-project-1.jpg",
                        "https://yukber.uz/image/cache/catalog/2019-08-12%2012.14.44-700x700.jpg",
                        "https://yukber.uz/image/cache/catalog/photo_2022-05-31_15-22-37-700x700.jpg",
                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRqAFf-fyomEryCt82DRzht57DLyPmsRwJo4g&s"
                },
                {
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTY8odp6o4NM_TTs7m1MIGWZWGUfZ4Ffjj0Cw&s",
                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTx16h574aPADTqUecK9HrhrviSWz5maolmUA&s",
                        "https://m.media-amazon.com/images/I/91LnEhnul6L._SL1500_.jpg",
                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQn0GvgdwqI63xYplflSULYPrJzZkAPR7sX1g&s"
                },
                {
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSfMBWosyYPlVV0H3ARHU61az1GJkjzzkD6uw&s",
                        "https://api.choparpizza.uz/storage/products/2023/11/02/Q9J9FxLJuawNj4yZFKSqSzMkC3tIVc9hG7R8KB2l.webp",
                        "https://api.choparpizza.uz/storage/products/2022/03/03/2GRLwqqQb0h1YslPViBqoZLOl7SxroY0PLoxHJCn.webp",
                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQHHYTfQS0jWmEbj4dFMSJ9lD2tjJQRDLXNrw&s"
                },
                {
                    "https://yukber.uz/image/cache/catalog/kavash-700x700.jpg",
                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQey9eSr9IWxyNYduGL5beUQS5Xr8vOJgkQkw&s",
                        "https://yukber.uz/image/cache/catalog/pishloqli%20lavash-700x700.jpg",
                        "https://nafistaqvo.uz/wp-content/uploads/2024/11/lavash-tandir-300x300.png"}
        };

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Category category = categoryRepository.save(new Category(
                        categories[j],
                        categories[j],
                        categories[j],
                        categories[j],
                        null,
                        institutionRepository.findById(i + 1L).orElseThrow()));
                String[] productNames = products[j];
                String[] imageLinks = images[j];
                for (int k = 0; k < productNames.length; k++) {
                    productRepository.save(new Product(
                            productNames[k],
                            productNames[k],
                            imageLinks[k],
                            productNames[k],
                            productNames[k],
                            (long) (Math.round(random.nextLong(100000) * 500) + 100),
                            category
                    ));
                }
            }
        }
    }

    private void initInstitutions() {
        List<Location> locations = locationRepository.findAll();
        institutionRepository.saveAll(List.of(
                new Institution("Max Way", "Max Way", "", "","https://dostavkainfo.uz/wp-content/uploads/2020/03/oqtepa_lavash.jpg",
                        locations.get(0),
                        institutionTypeRepository.findById(1L).orElseThrow(),
                        userRepository.findById(1L).orElseThrow()
                ),

                new Institution("Feed UP", "Feed UP", "", "","https://static.tildacdn.com/tild3433-3335-4663-b633-393138303263/feed_up.png",
                        locations.get(1),
                        institutionTypeRepository.findById(3L).orElseThrow(),
                        userRepository.findById(1L).orElseThrow()
                ),

                new Institution("Evos", "Evos", "", "","https://play-lh.googleusercontent.com/SAK0dhiUcvN5RBqClmWKb0bZbbn4Urs05yg_7-LtYlIs-SS0Ewvnzzi9v_X9sVKPD8hh",
                        locations.get(2),
                        institutionTypeRepository.findById(4L).orElseThrow(),
                        userRepository.findById(1L).orElseThrow()
                ),

                new Institution("Oqtepa Lavash", "Oqtepa Lavash", "", "","https://th.bing.com/th/id/OIP.com4sMfga2gwMCziijiREAHaHa?w=178&h=180&c=7&r=0&o=5&pid=1.7",
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
                addAdminRole(),
                1345758544L));
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
