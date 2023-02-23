package uz.md.shopapp.exceptions;

import lombok.Builder;

@Builder
public class InvalidUserNameOrPasswordException extends RuntimeException {

    private String messageUz;
    private String messageRu;

    public InvalidUserNameOrPasswordException(String messageUz, String messageRu) {
        super(messageUz);
        this.messageUz = messageUz;
        this.messageRu = messageRu;
    }

}
