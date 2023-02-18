package uz.md.shopapp.repository;

import jakarta.persistence.QueryHint;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import uz.md.shopapp.domain.InstitutionType;

import java.util.Optional;

public interface InstitutionTypeRepository extends JpaRepository<InstitutionType, Long> {
    boolean existsByNameUzOrNameRu(String nameUz, String nameRu);

    boolean existsByNameUzOrNameRuAndIdIsNot(String nameUz, String nameRu, Long id);

}
