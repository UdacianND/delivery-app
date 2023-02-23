package uz.md.shopapp.exceptions;

import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
@Builder
public class NotAllowedException extends RuntimeException {
    private String messageUz;
    private String messageRu;

    public NotAllowedException(String messageUz, String messageRu) {
        super(messageUz);
        this.messageUz = messageUz;
        this.messageRu = messageRu;
    }
}
