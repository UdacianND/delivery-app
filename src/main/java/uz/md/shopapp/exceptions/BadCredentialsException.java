package uz.md.shopapp.exceptions;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class BadCredentialsException extends RuntimeException {
    private String messageUz;
    private String messageRu;

    public BadCredentialsException(String messageUz, String messageRu) {
        super(messageUz);
        this.messageUz = messageUz;
        this.messageRu = messageRu;
    }
}
