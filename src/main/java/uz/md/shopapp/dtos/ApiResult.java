package uz.md.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResult<T> implements Serializable {

    private boolean success;

    private T data;

    private ErrorData errors;
    private Integer statusCode;

    private ApiResult(Boolean success) {
        this.success = success;
        this.statusCode = HttpStatus.OK.value();
    }

    private ApiResult(T data, Boolean success) {
        this.data = data;
        this.success = success;
        this.statusCode = HttpStatus.OK.value();
    }

    private ApiResult(String messageUz, String messageRu, String devMsg, String userMsg, Integer statusCode) {
        this.success = false;
        this.statusCode = statusCode;
        this.errors = new ErrorData(messageUz, messageRu, devMsg, userMsg);
    }

    public static <E> ApiResult<E> successResponse(E data) {
        return new ApiResult<>(data, true);
    }

    public static <E> ApiResult<E> successResponse() {
        return new ApiResult<>(true);
    }

    public static <E> ApiResult<E> errorResponse(String messageUz, String messageRu, String devMsg, String userMsg, Integer errorCode) {
        return new ApiResult<>(messageUz, messageRu, devMsg, userMsg, errorCode);
    }

}
