package uz.md.shopapp.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import uz.md.shopapp.domain.*;
import uz.md.shopapp.dtos.category.CategoryDTO;
import uz.md.shopapp.dtos.category.CategoryInfoDTO;
import uz.md.shopapp.dtos.institution.InstitutionDTO;
import uz.md.shopapp.dtos.institution.InstitutionInfoDTO;
import uz.md.shopapp.dtos.product.ProductDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class TestUtil {

    private static final ObjectMapper mapper = createObjectMapper();

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        return mapper.writeValueAsBytes(object);
    }

    public static byte[] createByteArray(int size, String data) {
        byte[] byteArray = new byte[size];
        for (int i = 0; i < size; i++) {
            byteArray[i] = Byte.parseByte(data, 2);
        }
        return byteArray;
    }


    public static <T> List<T> findAll(EntityManager em, Class<T> clss) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(clss);
        Root<T> rootEntry = cq.from(clss);
        CriteriaQuery<T> all = cq.select(rootEntry);
        TypedQuery<T> allQuery = em.createQuery(all);
        return allQuery.getResultList();
    }

    private TestUtil() {
    }

    public static void checkProductsEquality(List<ProductDTO> actual, List<Product> expected) {
        assertEquals(actual.size(), expected.size());
        for (int i = 0; i < actual.size(); i++) {
            assertEquals(actual.get(i).getId(), expected.get(i).getId());
            assertEquals(actual.get(i).getNameUz(), expected.get(i).getNameUz());
            assertEquals(actual.get(i).getNameRu(), expected.get(i).getNameRu());
            assertEquals(actual.get(i).getDescriptionUz(), expected.get(i).getDescriptionUz());
            assertEquals(actual.get(i).getDescriptionRu(), expected.get(i).getDescriptionRu());
        }
    }

    public static void checkCategoriesEquality(List<CategoryDTO> actual, List<Category> expected) {
        assertEquals(actual.size(), expected.size());
        for (int i = 0; i < actual.size(); i++) {
            assertEquals(actual.get(i).getId(), expected.get(i).getId());
            assertEquals(actual.get(i).getNameUz(), expected.get(i).getNameUz());
            assertEquals(actual.get(i).getNameRu(), expected.get(i).getNameRu());
            assertEquals(actual.get(i).getDescriptionUz(), expected.get(i).getDescriptionUz());
            assertEquals(actual.get(i).getDescriptionRu(), expected.get(i).getDescriptionRu());
        }
    }

    public static void checkCategoriesInfoEquality(List<CategoryInfoDTO> actual, List<Category> expected) {
        assertEquals(actual.size(), expected.size());
        for (int i = 0; i < actual.size(); i++) {
            assertEquals(actual.get(i).getId(), expected.get(i).getId());
            assertEquals(actual.get(i).getInstitutionId(), expected.get(i).getInstitution().getId());
            assertEquals(actual.get(i).getNameUz(), expected.get(i).getNameUz());
            assertEquals(actual.get(i).getNameRu(), expected.get(i).getNameRu());
            assertEquals(actual.get(i).getDescriptionUz(), expected.get(i).getDescriptionUz());
            assertEquals(actual.get(i).getDescriptionRu(), expected.get(i).getDescriptionRu());
        }
    }

    public static void checkInstitutionsEqualityEntityAndDto(List<Institution> expected, List<InstitutionDTO> actual) {
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).getNameUz(), actual.get(i).getNameUz());
            assertEquals(expected.get(i).getNameRu(), actual.get(i).getNameRu());
            assertEquals(expected.get(i).getDescriptionUz(), actual.get(i).getDescriptionUz());
            assertEquals(expected.get(i).getDescriptionRu(), actual.get(i).getDescriptionRu());
        }
    }

    public static void checkInstitutionsEqualityEntityAndInfo(List<Institution> expected, List<InstitutionInfoDTO> actual) {
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).getNameUz(), actual.get(i).getNameUz());
            assertEquals(expected.get(i).getNameRu(), actual.get(i).getNameRu());
            assertEquals(expected.get(i).getDescriptionUz(), actual.get(i).getDescriptionUz());
            assertEquals(expected.get(i).getDescriptionRu(), actual.get(i).getDescriptionRu());
            assertEquals(expected.get(i).getManager().getId(), actual.get(i).getManagerId());
        }
    }

    public static List<Institution> generateMockInstitutions(int count, InstitutionType institutionType, User manager) {
        List<Institution> instances = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            instances.add(new Institution("nameUz" + (i + 1), "nameRu" + (i + 1), "description", "description", institutionType, manager));
        }
        return instances;
    }

    public static List<Product> generateMockProducts(int count, Category category) {
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            products.add(new Product("nameUz" + (i + 1), "nameRu" + (i + 1), null, "description", "description", ((i + 1) * 100.0), category));
        }
        return products;
    }

    public static List<Category> generateMockCategories(int count, Institution institution) {
        List<Category> products = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            products.add(new Category("nameUz" + (i + 1), "nameRu" + (i + 1), "description", "description", null, institution));
        }
        return products;
    }

    public static List<InstitutionType> generateMockInstitutionTypes(int count) {
        List<InstitutionType> institutionTypes = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            institutionTypes.add(new InstitutionType("nameUz" + (i + 1), "nameRu" + (i + 1), "description", "description"));
        }
        return institutionTypes;
    }
}
