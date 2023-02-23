package uz.md.shopapp.service.impl;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.md.shopapp.domain.Category;
import uz.md.shopapp.domain.Product;
import uz.md.shopapp.domain.User;
import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.product.ProductAddDTO;
import uz.md.shopapp.dtos.product.ProductDTO;
import uz.md.shopapp.dtos.product.ProductEditDTO;
import uz.md.shopapp.dtos.request.SimpleSearchRequest;
import uz.md.shopapp.dtos.request.SimpleSortRequest;
import uz.md.shopapp.exceptions.AlreadyExistsException;
import uz.md.shopapp.exceptions.NotAllowedException;
import uz.md.shopapp.exceptions.NotFoundException;
import uz.md.shopapp.mapper.ProductMapper;
import uz.md.shopapp.repository.CategoryRepository;
import uz.md.shopapp.repository.ProductRepository;
import uz.md.shopapp.repository.UserRepository;
import uz.md.shopapp.service.QueryService;
import uz.md.shopapp.service.contract.FilesStorageService;
import uz.md.shopapp.service.contract.ProductService;
import uz.md.shopapp.utils.CommonUtils;

import java.nio.file.Path;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    @Value("${app.images.products.root.path}")
    private String productsPath;

    private Path productsImagesRoot;

    @PostConstruct
    public void init() {
        productsImagesRoot = Path.of(productsPath);
    }

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;
    private final QueryService queryService;
    private final FilesStorageService filesStorageService;
    private final UserRepository userRepository;

    private Product getById(Long id) {
        return productRepository
                .findById(id)
                .orElseThrow(() -> {
                    throw NotFoundException.builder()
                            .messageRu("")
                            .messageUz("PRODUCT_NOT_FOUND_WITH_ID" + id)
                            .build();

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

        Long managerId = categoryRepository.findMangerIdByCategoryId(dto.getCategoryId());

        if (managerId == null)
            throw NotFoundException.builder()
                    .messageRu("")
                    .messageUz("PRODUCT_OR_ITS_CATEGORY_NOT_FOUND")
                    .build();


        User currentUser = getCurrentUser();

        if (!currentUser.getRole().getName().equals("ADMIN"))
            if (!currentUser.getId().equals(managerId))
                throw NotAllowedException.builder()
                        .messageRu("")
                        .messageUz("YOU HAVE NO PERMISSION")
                        .build();


        if (productRepository.existsByNameUzOrNameRu(dto.getNameUz(), dto.getNameRu()))
            throw AlreadyExistsException.builder()
                    .messageRu("")
                    .messageUz("PRODUCT_NAME_ALREADY_EXISTS")
                    .build();


        Product product = productMapper.fromAddDTO(dto);

        Category category = categoryRepository
                .findById(dto.getCategoryId())
                .orElseThrow(() -> NotFoundException.builder()
                        .messageUz("CATEGORY_NOT_FOUND")
                        .messageRu("")
                        .build());
        product.setCategory(category);
        return ApiResult
                .successResponse(productMapper
                        .toDTO(productRepository
                                .save(product)));
    }

    private User getCurrentUser() {
        String phoneNumber = CommonUtils.getCurrentUserPhoneNumber();
        return userRepository
                .findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> NotFoundException.builder()
                        .messageUz("USER_NOT_FOUND")
                        .messageRu("")
                        .build());
    }

    @Override
    public ApiResult<ProductDTO> edit(ProductEditDTO editDTO) {

        Long managerId = categoryRepository.findMangerIdByCategoryId(editDTO.getCategoryId());

        if (managerId == null)
            throw NotFoundException.builder()
                    .messageRu("")
                    .messageUz("PRODUCT_OR_ITS_CATEGORY_NOT_FOUND")
                    .build();


        User currentUser = getCurrentUser();

        if (!currentUser.getRole().getName().equals("ADMIN"))
            if (!currentUser.getId().equals(managerId))
                throw NotAllowedException.builder()
                        .messageRu("")
                        .messageUz("YOU HAVE NO PERMISSION")
                        .build();


        Product product = productRepository
                .findById(editDTO.getId())
                .orElseThrow(() -> {
                    throw NotFoundException.builder()
                            .messageRu("")
                            .messageUz("PRODUCT_NOT_FOUND")
                            .build();

                });

        if (productRepository.existsByNameUzOrNameRuAndIdIsNot(editDTO.getNameUz(), editDTO.getNameRu(), product.getId()))

            throw AlreadyExistsException.builder()
                    .messageRu("")
                    .messageUz("PRODUCT_NAME_ALREADY_EXISTS")
                    .build();

        Product edited = productMapper.fromEditDTO(editDTO, product);

        edited.setCategory(categoryRepository
                .findById(editDTO.getCategoryId())
                .orElseThrow(() -> NotFoundException.builder()
                        .messageUz("CATEGORY_NOT_FOUND")
                        .messageRu("")
                        .build()));
        return ApiResult
                .successResponse(productMapper
                        .toDTO(productRepository.save(edited)));
    }

    @Override
    public ApiResult<Void> delete(Long id) {

        Long managerId = productRepository.findMangerIdById(id);

        if (managerId == null)
            throw NotFoundException.builder()
                    .messageRu("")
                    .messageUz("PRODUCT_OR_ITS_CATEGORY_NOT_FOUND")
                    .build();


        User currentUser = getCurrentUser();

        if (!currentUser.getRole().getName().equals("ADMIN"))
            if (!currentUser.getId().equals(managerId))
                throw NotAllowedException.builder()
                        .messageRu("")
                        .messageUz("YOU HAVE NO PERMISSION")
                        .build();


        if (!productRepository.existsById(id))
            throw NotFoundException.builder()
                    .messageRu("")
                    .messageUz("PRODUCT_DOES_NOT_EXIST")
                    .build();

        productRepository.deleteById(id);
        return ApiResult.successResponse();
    }

    @Override
    public ApiResult<List<ProductDTO>> getAllByCategory(Long id) {
        if (!categoryRepository.existsById(id))
            throw NotFoundException.builder()
                    .messageRu("")
                    .messageUz("CATEGORY_NOT_FOUND_WITH_ID" + id)
                    .build();

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

    @Override
    public ApiResult<Void> setImage(Long productId, MultipartFile image) {
        Product product = productRepository
                .findById(productId)
                .orElseThrow(() -> NotFoundException.builder()
                        .messageUz("PRODUCT NOT FOUND")
                        .messageRu("")
                        .build());
        filesStorageService.save(image, productsImagesRoot);
        product.setImageUrl(productsImagesRoot.toUri() + image.getOriginalFilename());
        productRepository.save(product);
        return ApiResult.successResponse();
    }
}
