package uz.md.shopapp.service.impl;

import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.md.shopapp.domain.Category;
import uz.md.shopapp.domain.Product;
import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.category.CategoryDTO;
import uz.md.shopapp.dtos.institution.InstitutionDTO;
import uz.md.shopapp.dtos.institution.InstitutionInfoDTO;
import uz.md.shopapp.mapper.ProductMapper;
import uz.md.shopapp.repository.InstitutionRepository;
import uz.md.shopapp.service.QueryService;
import uz.md.shopapp.service.contract.SearchService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final QueryService queryService;
    private final ProductMapper productMapper;
    private final InstitutionRepository institutionRepository;

    @Override
    public ApiResult<List<InstitutionDTO>> getBySearch(String value) {
        TypedQuery<Product> productTypedQuery = queryService.generateSearchQuery(Product.class, value);

        List<Product> productList = productTypedQuery.getResultList();

        List<CategoryDTO> categories = groupProductsByCategory(productList);

        return ApiResult.successResponse(groupCategoriesByInstitution(categories));
    }

    private List<InstitutionDTO> groupCategoriesByInstitution(List<CategoryDTO> categories) {
        Map<Long, List<CategoryDTO>> map = new HashMap<>();
        for (CategoryDTO category : categories) {
            List<CategoryDTO> orDefault = map.getOrDefault(category.getInstitutionId(), new ArrayList<>());
            orDefault.add(category);
            map.put(category.getInstitutionId(), orDefault);
        }

        List<InstitutionDTO> result = new ArrayList<>();
        map.forEach((institutionId, categoryDTOS) -> {
            InstitutionInfoDTO info = institutionRepository.findForInfoById(institutionId);
            result.add(new InstitutionDTO(
                    info.getId(),
                    info.getNameUz(),
                    info.getNameRu(),
                    info.getDescriptionUz(),
                    info.getDescriptionRu(),
                    categoryDTOS
            ));
        });
        return result;
    }

    private List<CategoryDTO> groupProductsByCategory(List<Product> productList) {
        Map<Category, List<Product>> map = new HashMap<>();
        for (Product product : productList) {
            List<Product> orDefault = map.getOrDefault(product.getCategory(), new ArrayList<>());
            orDefault.add(product);
            map.put(product.getCategory(), orDefault);
        }

        List<CategoryDTO> result = new ArrayList<>();
        map.forEach((category, products) -> {
            result.add(new CategoryDTO(
                    category.getId(),
                    category.getNameUz(),
                    category.getNameRu(),
                    category.getDescriptionUz(),
                    category.getDescriptionRu(),
                    category.getInstitution().getId(),
                    productMapper.toDTOList(products)));
        });
        return result;
    }

}
