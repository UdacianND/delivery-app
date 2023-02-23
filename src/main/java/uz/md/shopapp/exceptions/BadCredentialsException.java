package uz.md.shopapp.exceptions;

import lombok.Builder;

@Builder
public class BadCredentialsException extends RuntimeException {
    private String messageUz;
    private String messageRu;

    public BadCredentialsException(String messageUz, String messageRu) {
        super(messageUz);
        this.messageUz = messageUz;
        this.messageRu = messageRu;
    }
}
