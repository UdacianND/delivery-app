package uz.md.shopapp.exceptions;

import lombok.Builder;

@Builder
public class IllegalRequestException extends RuntimeException {
    private String messageUz;
    private String messageRu;

    public IllegalRequestException(String messageUz, String messageRu) {
        super(messageUz);
        this.messageUz = messageUz;
        this.messageRu = messageRu;
    }
}
