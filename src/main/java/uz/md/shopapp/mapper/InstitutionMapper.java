package uz.md.shopapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import uz.md.shopapp.domain.Institution;
import uz.md.shopapp.dtos.institution.InstitutionAddDTO;
import uz.md.shopapp.dtos.institution.InstitutionDTO;
import uz.md.shopapp.dtos.institution.InstitutionEditDTO;

@Mapper(componentModel = "spring")
public interface InstitutionMapper extends EntityMapper<Institution, InstitutionDTO> {
    Institution fromAddDTO(InstitutionAddDTO dto);

    Institution fromEditDTO(InstitutionEditDTO editDTO, @MappingTarget Institution editing);
}
