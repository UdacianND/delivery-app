package uz.md.shopapp.dtos;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class TokenDTO {

    private String accessToken;

    /**
     * token type
     */
    private final String tokenType = "Bearer ";
}
