package uz.md.shopapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import uz.md.shopapp.domain.InstitutionType;
import uz.md.shopapp.dtos.institutionType.InstitutionTypeAddDTO;
import uz.md.shopapp.dtos.institutionType.InstitutionTypeDTO;
import uz.md.shopapp.dtos.institutionType.InstitutionTypeEditDTO;

@Mapper(componentModel = "spring")
public interface InstitutionTypeMapper extends EntityMapper<InstitutionType, InstitutionTypeDTO> {
    InstitutionType fromAddDTO(InstitutionTypeAddDTO dto);

    InstitutionType fromEditDTO(InstitutionTypeEditDTO editDTO, @MappingTarget InstitutionType editing);
}
