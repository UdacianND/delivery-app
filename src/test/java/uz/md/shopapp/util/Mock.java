package uz.md.shopapp.util;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomUtils;
import uz.md.shopapp.domain.*;
import uz.md.shopapp.domain.enums.PermissionEnum;
import uz.md.shopapp.dtos.user.UserDTO;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.Set;

public class Mock {

    private static final Faker faker = new Faker();

    public static InstitutionType getInstitutionType() {
        long l = RandomUtils.nextLong(1, 100);
        return new InstitutionType(
                "Restaurant" + l,
                "Restaurant" + l,
                " All restaurants " + l,
                " All restaurants " + l);
    }

    public static User getUser(Role role) {
        return new User(
                "Ali",
                "Yusupov",
                "+998902002020",
                role);
    }

    public static Address getAddress(User manager) {
        return new Address(
                manager,
                15,
                "street",
                "city"
        );
    }

    public static Institution getInstitution(Location location, InstitutionType institutionType, User manager) {
        return new Institution(
                "Max Way",
                "Max Way",
                " Cafe ",
                " Cafe ",
                null,
                location,
                institutionType,
                null,
                manager);
    }

    public static Category getCategory(Institution institution) {
        Category category = new Category();
        category.setNameUz("Uzbek meals");
        category.setNameRu("Uzbek meals");
        category.setDescriptionUz("uzbek national meals");
        category.setDescriptionRu("uzbek national meals");
        category.setDeleted(false);
        category.setActive(true);
        category.setInstitution(institution);
        return category;
    }

    public static Location getLocation() {
        long la = RandomUtils.nextLong(15, 100);
        long lo = RandomUtils.nextLong(15, 100);
        return new Location((double) la, (double) lo);
    }

    public static Product getProduct(Category category) {
        Random random = new Random();
        long price = random.nextLong() * 300 + 200;
        int v = random.nextInt(5)+1;
        Product product = new Product();
        product.setNameUz("Plov " + v);
        product.setNameRu("Plov " + v);
        product.setDescriptionUz(" Andijan Plov ");
        product.setDescriptionRu("Andijan Plov");
        product.setPrice(price);
        product.setDeleted(false);
        product.setActive(true);
        product.setCategory(category);
        return product;
    }

    private static Role getAdminRole() {
        return Role.builder()
                .name("ADMIN")
                .description("admin description")
                .permissions(Set.of(PermissionEnum.values()))
                .build();
    }

    public static User getEmployeeUser() {
        Role adminRole = getAdminRole();
        User user = getUser();
        user.setRole(adminRole);
        return user;
    }

    private static User getUser() {
        Name name = faker.name();
        String phoneNumber = "+99893" + RandomStringUtils.random(7, false, true);
        String password = RandomStringUtils.random(5, false, true);
        return User.builder()
                .firstName(name.firstName())
                .lastName(name.lastName())
                .phoneNumber(phoneNumber)
                .password(password)
                .build();
    }

    public static User getMockClient() {
        Role clientRole = getClientRole();
        User user = getUser();
        user.setRole(clientRole);
        user.setCodeValidTill(LocalDateTime.now().plusDays(1));
        return user;
    }

    private static Role getClientRole() {
        return Role.builder()
                .name("CLIENT")
                .description("client description")
                .permissions(Set.of(PermissionEnum.values()))
                .build();
    }

    public static Institution getInstitution() {
        String random = RandomStringUtils.random(5, true, false);
        Location location = getLocation();
        InstitutionType institutionType = getInstitutionType();
        return Institution.builder()
                .nameUz("Cafe " + random)
                .nameRu("Cafe ru " + random)
                .descriptionUz("Cafe description " + random)
                .descriptionUz("Cafe description ru" + random)
                .location(location)
                .type(institutionType)
                .build();
    }
}
