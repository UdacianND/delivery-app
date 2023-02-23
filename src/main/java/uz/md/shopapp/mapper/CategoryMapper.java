package uz.md.shopapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import uz.md.shopapp.domain.Category;
import uz.md.shopapp.dtos.category.CategoryAddDTO;
import uz.md.shopapp.dtos.category.CategoryDTO;
import uz.md.shopapp.dtos.category.CategoryEditDTO;
import uz.md.shopapp.dtos.category.CategoryInfoDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper extends EntityMapper<Category, CategoryDTO> {

    Category fromAddDTO(CategoryAddDTO dto);

    Category fromEditDTO(CategoryEditDTO dto, @MappingTarget Category category);

    List<CategoryInfoDTO> toInfoDTOList(List<Category> all);

    @Override
    @Mapping(target = "institutionId" , source = "institution.id")
    CategoryDTO toDTO(Category entity);

    @Mapping(target = "institutionId", source = "institution.id")
    CategoryInfoDTO toInfoDTO(Category category);
}