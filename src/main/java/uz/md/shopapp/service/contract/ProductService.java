package uz.md.shopapp.service.contract;

import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.product.ProductAddDTO;
import uz.md.shopapp.dtos.product.ProductDTO;
import uz.md.shopapp.dtos.product.ProductEditDTO;
import uz.md.shopapp.dtos.request.SimpleSearchRequest;
import uz.md.shopapp.dtos.request.SimpleSortRequest;

import java.util.List;

public interface ProductService {

    ApiResult<ProductDTO> findById(Long id);

    ApiResult<ProductDTO> add(ProductAddDTO dto);

    ApiResult<ProductDTO> edit(ProductEditDTO editDTO);

    ApiResult<Void> delete(Long id);

    ApiResult<List<ProductDTO>> getAllByCategory(Long id);

    ApiResult<List<ProductDTO>> findAllBySimpleSearch(SimpleSearchRequest request);

    ApiResult<List<ProductDTO>> findAllBySort(SimpleSortRequest request);
}
