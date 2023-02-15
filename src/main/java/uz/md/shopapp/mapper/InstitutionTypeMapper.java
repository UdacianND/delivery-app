package uz.md.shopapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import uz.md.shopapp.domain.InstitutionType;
import uz.md.shopapp.dtos.institution_type.InstitutionTypeAddDTO;
import uz.md.shopapp.dtos.institution_type.InstitutionTypeDTO;
import uz.md.shopapp.dtos.institution_type.InstitutionTypeEditDTO;

@Mapper(componentModel = "spring")
public interface InstitutionTypeMapper extends EntityMapper<InstitutionType, InstitutionTypeDTO> {
    InstitutionType fromAddDTO(InstitutionTypeAddDTO dto);

    InstitutionType fromEditDTO(InstitutionTypeEditDTO editDTO, @MappingTarget InstitutionType editing);
}
