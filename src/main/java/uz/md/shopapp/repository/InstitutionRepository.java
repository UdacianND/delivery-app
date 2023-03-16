package uz.md.shopapp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.md.shopapp.domain.Institution;
import uz.md.shopapp.dtos.institution.InstitutionInfoDTO;

import java.util.List;

public interface InstitutionRepository extends JpaRepository<Institution, Long> {

    boolean existsByNameUzOrNameRu(String nameUz, String nameRu);

    boolean existsByNameUzOrNameRuAndIdIsNot(String nameUz, String nameRu, Long id);

    @Query("select new uz.md.shopapp.dtos.institution.InstitutionInfoDTO(i.id, i.nameUz,i.nameRu, i.imageUrl, i.descriptionUz,i.descriptionRu, i.type.id, i.manager.id) from Institution i where i.deleted = false ")
    List<InstitutionInfoDTO> findAllForInfo();

    @Query("select new uz.md.shopapp.dtos.institution.InstitutionInfoDTO(i.id, i.nameUz,i.nameRu, i.imageUrl, i.descriptionUz,i.descriptionRu, i.type.id, i.manager.id) from Institution i where i.deleted = false and i.type.id = :typeId")
    List<InstitutionInfoDTO> findAllForInfoByTypeId(Long typeId);

    @Query("select new uz.md.shopapp.dtos.institution.InstitutionInfoDTO(i.id, i.nameUz,i.nameRu, i.imageUrl, i.descriptionUz,i.descriptionRu, i.type.id, i.manager.id) from Institution i where i.deleted = false and i.manager.id = :managerId")
    List<InstitutionInfoDTO> findAllForInfoByManagerId(Long managerId);

    @Query("select new uz.md.shopapp.dtos.institution.InstitutionInfoDTO(i.id, i.nameUz,i.nameRu, i.imageUrl, i.descriptionUz,i.descriptionRu, i.type.id, i.manager.id) from Institution i where i.deleted = false and i.id = :id")
    InstitutionInfoDTO findForInfoById(Long id);

    @Query("select new uz.md.shopapp.dtos.institution.InstitutionInfoDTO(i.id, i.nameUz,i.nameRu,i.imageUrl, i.descriptionUz,i.descriptionRu, i.type.id, i.manager.id) from Institution i where i.deleted = false")
    Page<InstitutionInfoDTO> findAllForInfo(Pageable pageable);
}
