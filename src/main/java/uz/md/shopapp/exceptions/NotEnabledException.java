package uz.md.shopapp.exceptions;

import lombok.Builder;

@Builder
public class NotEnabledException extends RuntimeException {
    private String messageUz;
    private String messageRu;

    public NotEnabledException(String messageUz, String messageRu) {
        super(messageUz);
        this.messageUz = messageUz;
        this.messageRu = messageRu;
    }
}
