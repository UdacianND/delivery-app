package uz.md.shopapp.service.impl;

import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import uz.md.shopapp.domain.Category;
import uz.md.shopapp.domain.Product;
import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.product.ProductAddDTO;
import uz.md.shopapp.dtos.product.ProductDTO;
import uz.md.shopapp.dtos.product.ProductEditDTO;
import uz.md.shopapp.dtos.request.SimpleSearchRequest;
import uz.md.shopapp.dtos.request.SimpleSortRequest;
import uz.md.shopapp.exceptions.AlreadyExistsException;
import uz.md.shopapp.exceptions.NotFoundException;
import uz.md.shopapp.mapper.ProductMapper;
import uz.md.shopapp.repository.CategoryRepository;
import uz.md.shopapp.repository.ProductRepository;
import uz.md.shopapp.service.QueryService;
import uz.md.shopapp.service.contract.ProductService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;
    private final QueryService queryService;

    private Product getById(Long id) {
        return productRepository
                .findById(id)
                .orElseThrow(() -> {
                    throw new NotFoundException("PRODUCT_NOT_FOUND_WITH_ID" + id);
                });
    }

    @Override
    public ApiResult<ProductDTO> findById(Long id) {
        Product byId = getById(id);
        return ApiResult.successResponse(
                productMapper.toDTO(byId));
    }

    @Override
    public ApiResult<ProductDTO> add(ProductAddDTO dto) {

        if (productRepository.existsByNameUzOrNameRu(dto.getNameUz(), dto.getNameRu()))
            throw new AlreadyExistsException("PRODUCT_NAME_ALREADY_EXISTS");

        Product product = productMapper.fromAddDTO(dto);

        Category category = categoryRepository
                .findById(dto.getCategoryId())
                .orElseThrow(() -> new NotFoundException("CATEGORY_NOT_FOUND"));

        product.setCategory(category);
        return ApiResult
                .successResponse(productMapper
                        .toDTO(productRepository
                                .save(product)));
    }

    @Override
    public ApiResult<ProductDTO> edit(ProductEditDTO editDTO) {

        Product product = productRepository
                .findById(editDTO.getId())
                .orElseThrow(() -> {
                    throw new NotFoundException("PRODUCT_NOT_FOUND");
                });

        if (productRepository.existsByNameUzOrNameRuAndIdIsNot(editDTO.getNameUz(), editDTO.getNameRu(), product.getId()))

            throw new AlreadyExistsException("PRODUCT_NAME_ALREADY_EXISTS");
        Product edited = productMapper.fromEditDTO(editDTO, product);

        edited.setCategory(categoryRepository
                .findById(editDTO.getCategoryId())
                .orElseThrow(() -> new NotFoundException("CATEGORY_NOT_FOUND")));

        return ApiResult
                .successResponse(productMapper
                        .toDTO(productRepository.save(edited)));
    }

    @Override
    public ApiResult<Void> delete(Long id) {
        if (!productRepository.existsById(id))
            throw new NotFoundException("PRODUCT_DOES_NOT_EXIST");
        productRepository.deleteById(id);
        return ApiResult.successResponse();
    }

    @Override
    public ApiResult<List<ProductDTO>> getAllByCategory(Long id) {
        if (!categoryRepository.existsById(id))
            throw new NotFoundException("CATEGORY_NOT_FOUND_WITH_ID" + id);
        return ApiResult.successResponse(
                productMapper
                        .toDTOList(productRepository
                                .findAllByCategory_Id(id)));
    }

    @Override
    public ApiResult<List<ProductDTO>> findAllBySimpleSearch(SimpleSearchRequest request) {

        TypedQuery<Product> productTypedQuery = queryService
                .generateSimpleSearchQuery(Product.class, request);

        return ApiResult
                .successResponse(productMapper
                        .toDTOList(productTypedQuery.getResultList()));
    }

    @Override
    public ApiResult<List<ProductDTO>> findAllBySort(SimpleSortRequest request) {
        TypedQuery<Product> productTypedQuery = queryService
                .generateSimpleSortQuery(Product.class, request);

        return ApiResult
                .successResponse(productMapper
                        .toDTOList(productTypedQuery.getResultList()));
    }
}
