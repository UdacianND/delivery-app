package uz.md.shopapp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.md.shopapp.domain.Category;
import uz.md.shopapp.dtos.category.CategoryInfoDTO;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByNameUzOrNameRu(String nameUz, String nameRu);

    boolean existsByNameUzOrNameRuAndIdIsNot(String nameUz, String nameRu, Long id);

    @Query("select new uz.md.shopapp.dtos.category.CategoryInfoDTO(id, nameUz, nameRu, descriptionUz, descriptionRu, institution.id) from Category where deleted=false")
    List<CategoryInfoDTO> findAllForInfo();

    @Query("select new uz.md.shopapp.dtos.category.CategoryInfoDTO(c.id, c.nameUz, c.nameRu, c.descriptionUz, c.descriptionRu, i.id) from Category c join c.institution i  where c.deleted = false and i.id = :id")
    List<CategoryInfoDTO> findAllForInfoByInstitutionId(Long id);

    @Query(countQuery = "SELECT COUNT(c) FROM Category c WHERE c.institution.id= :id and c.deleted = false ",
            value = "FROM Category WHERE deleted = false and institution.id= :id")
    Page<Category> findAllForInfoByInstitutionId(Long id, Pageable pageable);

    @Query("select c.institution.manager.id from Category c where c.id = :id")
    Long findMangerIdByCategoryId(Long id);
}

