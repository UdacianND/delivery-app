package uz.md.shopapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import uz.md.shopapp.domain.Institution;
import uz.md.shopapp.dtos.institution.InstitutionAddDTO;
import uz.md.shopapp.dtos.institution.InstitutionDTO;
import uz.md.shopapp.dtos.institution.InstitutionEditDTO;
import uz.md.shopapp.dtos.institution.InstitutionInfoDTO;

@Mapper(componentModel = "spring")
public interface InstitutionMapper extends EntityMapper<Institution, InstitutionDTO> {


    Institution fromAddDTO(InstitutionAddDTO dto);

    Institution fromEditDTO(InstitutionEditDTO editDTO, @MappingTarget Institution editing);

    @Mapping(target = "managerId", source = "manager.id")
    @Mapping(target = "institutionTypeId", source = "type.id")
    InstitutionInfoDTO toInfoDTO(Institution institution);

}
