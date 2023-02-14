package uz.md.shopapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import uz.md.shopapp.domain.Product;
import uz.md.shopapp.dtos.product.ProductAddDTO;
import uz.md.shopapp.dtos.product.ProductDTO;
import uz.md.shopapp.dtos.product.ProductEditDTO;

@Mapper(componentModel = "spring")
public interface ProductMapper extends EntityMapper<Product, ProductDTO> {

    Product fromAddDTO(ProductAddDTO dto);

    @Override
    @Mapping(target = "categoryId", source = "category.id")
    ProductDTO toDTO(Product entity);

    Product fromEditDTO(ProductEditDTO editDTO, @MappingTarget Product product);
}
