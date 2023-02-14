package uz.md.shopapp.dtos.request;

import lombok.*;
import org.springframework.data.domain.Sort;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class SimpleSortRequest {

    private String sortBy = "id";

    private Sort.Direction direction = Sort.Direction.ASC;

    private int page;

    private int pageCount = 10;
}
