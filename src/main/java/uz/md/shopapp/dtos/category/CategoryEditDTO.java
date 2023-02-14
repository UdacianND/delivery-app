package uz.md.shopapp.dtos.category;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class CategoryEditDTO extends CategoryAddDTO {

    @NotNull(message = " category id must not be null")
    private Long id;

    public CategoryEditDTO(@NotBlank(message = "Category name must not be empty") String nameUz,
                           @NotBlank(message = "Category name must not be empty") String nameRu,
                           String descriptionUz,
                           String descriptionRu,
                           Long institutionId,
                           Long id) {
        super(nameUz, nameRu, descriptionUz, descriptionRu, institutionId);
        this.id = id;
    }

    public CategoryEditDTO(Long id) {
        this.id = id;
    }
}
