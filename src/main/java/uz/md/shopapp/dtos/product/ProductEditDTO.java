package uz.md.shopapp.dtos.product;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class ProductEditDTO extends ProductAddDTO {

    @NotNull(message = "product id must not be null")
    private Long id;

    public ProductEditDTO(@NotBlank(message = "Product name must not be empty") String nameUz,
                          @NotBlank(message = "Product name must not be empty") String nameRu,
                          MultipartFile image,
                          String descriptionUz,
                          String descriptionRu,
                          @NotNull(message = "Product price must not be null") Double price,
                          @NotNull(message = "Product category must not be null") Long categoryId,
                          Long id) {
        super(nameUz, nameRu, image, descriptionUz, descriptionRu, price, categoryId);
        this.id = id;
    }

    public ProductEditDTO(Long id) {
        this.id = id;
    }
}
