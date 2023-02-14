package uz.md.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class ErrorData {

    private String devMsg;
    private String userMsg;
    private String fieldName;
    private Integer errorCode;

    public ErrorData(String devMsg, String userMsg, Integer errorCode) {
        this.devMsg = devMsg;
        this.userMsg = userMsg;
        this.errorCode = errorCode;
    }

    public ErrorData(String devMsg, String userMsg) {
        this.devMsg = devMsg;
        this.userMsg = userMsg;
    }
}
