package uz.md.shopapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.md.shopapp.domain.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByNameUzOrNameRu(String nameUz, String nameRu);

    boolean existsByNameUzOrNameRuAndIdIsNot(String nameUz, String nameRu, Long id);

    List<Product> findAllByCategory_Id(Long category_id);

    @Query("select category.institution.manager.id from Product where id = :id")
    Long findMangerIdByProductId(Long id);

    @Query("select category.institution.manager.id from Product where id = :id")
    Long findMangerIdById(Long id);
}
