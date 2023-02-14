package uz.md.shopapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.md.shopapp.domain.InstitutionType;

public interface InstitutionTypeRepository extends JpaRepository<InstitutionType, Long> {
    boolean existsByNameUzOrNameRu(String nameUz, String nameRu);

    boolean existsByNameUzOrNameRuAndIdIsNot(String nameUz, String nameRu, Long id);

}
