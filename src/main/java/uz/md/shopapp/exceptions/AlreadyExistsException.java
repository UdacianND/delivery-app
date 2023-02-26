package uz.md.shopapp.exceptions;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AlreadyExistsException extends RuntimeException {
    private String messageUz;
    private String messageRu;

    public AlreadyExistsException(String messageUz, String messageRu) {
        super(messageUz);
        this.messageUz = messageUz;
        this.messageRu = messageRu;
    }
}
