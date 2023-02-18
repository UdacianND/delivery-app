package uz.md.shopapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.service.contract.InstitutionService;
import uz.md.shopapp.service.contract.ProductService;
import uz.md.shopapp.utils.AppConstants;

@RestController
@RequiredArgsConstructor
@RequestMapping(FileUploadController.BASE_URL)
public class FileUploadController {

    public static final String BASE_URL = AppConstants.BASE_URL + "images/upload";

    private final InstitutionService institutionService;
    private final ProductService productService;

    @PostMapping(value = "/to_institution/{id}", consumes = "multipart/form-data")
    public ApiResult<Void> uploadImageToInstitution(@RequestParam("file") MultipartFile image, @PathVariable("id") Long institutionId) {
        return institutionService.setImage(institutionId, image);
    }

    @PostMapping(value = "/to_product/{id}", consumes = "multipart/form-data")
    public ApiResult<Void> uploadImageToProduct(@RequestParam("file") MultipartFile image, @PathVariable("id") Long productId) {
        return productService.setImage(productId, image);
    }


}
