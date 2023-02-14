package uz.md.shopapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.product.ProductAddDTO;
import uz.md.shopapp.dtos.product.ProductDTO;
import uz.md.shopapp.dtos.product.ProductEditDTO;
import uz.md.shopapp.dtos.request.SimpleSearchRequest;
import uz.md.shopapp.dtos.request.SimpleSortRequest;
import uz.md.shopapp.service.contract.ProductService;
import uz.md.shopapp.utils.AppConstants;

import java.util.List;

@RestController
@RequestMapping(ProductController.BASE_URL + "/")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    public static final String BASE_URL = AppConstants.BASE_URL + "product";
    private final ProductService productService;

    @GetMapping("/category/{id}")
    @Operation(description = "Get products by category")
    public ApiResult<List<ProductDTO>> getAllByCategory(@PathVariable Long id) {
        log.info("getAllByCategory called with category id {}", id);
        return productService.getAllByCategory(id);
    }

    @GetMapping("/{id}")
    @Operation(description = "Get product by id")
    public ApiResult<ProductDTO> getById(@PathVariable Long id) {
        log.info("getById called with id {}", id);
        return productService.findById(id);
    }

    @PostMapping("/add")
    @Operation(description = "add product")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResult<ProductDTO> add(@RequestBody @Valid ProductAddDTO dto, @RequestParam MultipartFile image) {
        log.info("Product Add");
        log.info("Request body {}", dto);
        dto.setImage(image);
        return productService.add(dto);
    }

    @PutMapping("/edit")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(description = "edit product")
    public ApiResult<ProductDTO> edit(@RequestBody @Valid ProductEditDTO editDTO) {
        log.info("edit product");
        log.info("Request body {}", editDTO);
        return productService.edit(editDTO);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(description = "Delete a product")
    public ApiResult<Void> delete(@PathVariable Long id) {
        log.info("delete product with id {}", id);
        return productService.delete(id);
    }

    @GetMapping("/search")
    @Operation(description = "Searching products")
    public ApiResult<List<ProductDTO>> getProductsBySimpleSearch(@RequestBody SimpleSearchRequest request) {
        log.info("get products by simple search request");
        log.info("Request body {}", request);
        return productService.findAllBySimpleSearch(request);
    }

    @GetMapping("/sorting")
    @Operation(description = "List of products sorted")
    public ApiResult<List<ProductDTO>> getProductsBySort(@RequestBody SimpleSortRequest request) {
        log.info("getProductsBySort");
        log.info("Request body is: {}", request);
        return productService.findAllBySort(request);
    }

}
