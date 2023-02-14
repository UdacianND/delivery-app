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
import uz.md.shopapp.domain.Category;
import uz.md.shopapp.domain.Order;
import uz.md.shopapp.domain.Product;
import uz.md.shopapp.dtos.category.CategoryDTO;
import uz.md.shopapp.dtos.category.CategoryInfoDTO;
import uz.md.shopapp.dtos.product.ProductDTO;

import java.io.IOException;
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
            assertEquals(actual.get(i).getNameUz(), expected.get(i).getNameUz());
            assertEquals(actual.get(i).getNameRu(), expected.get(i).getNameRu());
            assertEquals(actual.get(i).getDescriptionUz(), expected.get(i).getDescriptionUz());
            assertEquals(actual.get(i).getDescriptionRu(), expected.get(i).getDescriptionRu());
        }
    }

}
