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

    @Query("select new uz.md.shopapp.dtos.category.CategoryInfoDTO(c.id, c.nameUz, c.nameRu, c.descriptionUz, c.descriptionRu) from Category c where c.deleted=false")
    List<Category> findAllForInfo();

    @Query("select new uz.md.shopapp.dtos.category.CategoryInfoDTO(c.id, c.nameUz, c.nameRu, c.descriptionUz, c.descriptionRu) from Category c where c.deleted=false and c.institution.id = :id")
    List<CategoryInfoDTO> findAllForInfoByInstitutionId(Long id);


    Optional<Category> findByNameUzAndNameRu(String nameUz, String nameRu);

    Page<CategoryInfoDTO> findAllForInfoByInstitutionId(Long id, Pageable pageable);

    @Query("select c.institution.manager.id from Category c where c.id = :id")
    Long findMangerIdByCategoryId(Long id);
}

