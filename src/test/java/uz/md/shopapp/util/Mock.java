package uz.md.shopapp.util;

import uz.md.shopapp.domain.*;

import java.util.Random;

public class Mock {

    public static InstitutionType getInstitutionType() {
        return new InstitutionType(
                "Restaurant",
                "Restaurant",
                " All restaurants",
                " All restaurants");
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
        return new Location(15.0, 15.0);
    }

    public static Product getProduct(Category category) {
        Random random = new Random();
        double price = random.nextDouble() * 300 + 200;
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
}
