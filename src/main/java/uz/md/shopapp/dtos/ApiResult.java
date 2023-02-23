package uz.md.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResult<T> implements Serializable {

    private boolean success;

    private T data;

    private List<ErrorData> errors;

    private ApiResult(Boolean success) {
        this.success = success;
    }

    private ApiResult(T data, Boolean success) {
        this.data = data;
        this.success = success;
    }

    private ApiResult(String messageUz, String messageRu,String devMsg, String userMsg, Integer errorCode) {
        this.success = false;
        this.errors = Collections.singletonList(new ErrorData(messageUz, messageRu, devMsg, userMsg, errorCode));
    }

    private ApiResult(List<ErrorData> errors) {
        this.success = false;
        this.errors = errors;
    }

    public static <E> ApiResult<E> successResponse(E data) {
        return new ApiResult<>(data, true);
    }

    public static <E> ApiResult<E> successResponse() {
        return new ApiResult<>(true);
    }

    public static <E> ApiResult<E> errorResponse(String messageUz, String messageRu,String devMsg, String userMsg, Integer errorCode) {
        return new ApiResult<>(messageUz, messageRu, devMsg, userMsg, errorCode);
    }


}
