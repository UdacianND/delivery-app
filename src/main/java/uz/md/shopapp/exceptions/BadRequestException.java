package uz.md.shopapp.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
public class BadRequestException extends RuntimeException {

    private String messageUz;
    private String messageRu;

    public BadRequestException(String messageUz, String messageRu){
        super(messageUz);
        this.messageUz = messageUz;
        this.messageRu = messageRu;
    }

}
