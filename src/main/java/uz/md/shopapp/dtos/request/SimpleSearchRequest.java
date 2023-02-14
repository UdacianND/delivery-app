package uz.md.shopapp.dtos.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.domain.Sort;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class SimpleSearchRequest {
    @NotNull(message = "search request fields is required")
    private String[] fields;

    @NotNull(message = "search request key is required")
    private String key;
    private String sortBy = "id";
    private int page;
    private int pageCount = 10;
    private Sort.Direction sortDirection = Sort.Direction.ASC;
}
