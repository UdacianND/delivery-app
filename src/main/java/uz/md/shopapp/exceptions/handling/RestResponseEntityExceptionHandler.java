package uz.md.shopapp.exceptions.handling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import uz.md.shopapp.dtos.ErrorData;
import uz.md.shopapp.exceptions.*;


@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({AlreadyExistsException.class})
    public ResponseEntity<ErrorData> handleAlreadyExisted(AlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ErrorData.builder()
                        .messageUz(ex.getMessageUz())
                        .messageRu(ex.getMessageRu())
                        .userMsg(ex.getMessage())
                        .devMsg(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorData> handleMaxSizeException(MaxUploadSizeExceededException exc) {
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                .body(ErrorData.builder()
                        .messageUz("Fayl hajmi katta")
                        .messageRu("")
                        .build());
    }

    @ExceptionHandler(AccessKeyInvalidException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ErrorData> handleAllException(AccessKeyInvalidException ex) {
        return new ResponseEntity<>(ErrorData.builder()
                .messageUz(ex.getMessageUz())
                .messageRu(ex.getMessageRu())
                .build(),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({IllegalRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorData> handleIllegalRequest(IllegalRequestException exception) {
        return new ResponseEntity<>(ErrorData.builder()
                .messageUz(exception.getMessageUz())
                .messageRu(exception.getMessageRu())
                .build(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ConflictException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorData> handleConflictException(ConflictException exception) {
        return new ResponseEntity<>(ErrorData.builder()
                .messageUz(exception.getMessageUz())
                .messageRu(exception.getMessageRu())
                .build(),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<ErrorData> handleNotFoundException(NotFoundException exception) {
        return new ResponseEntity<>(ErrorData.builder()
                .messageUz(exception.getMessageUz())
                .messageRu(exception.getMessageRu())
                .build(),
                HttpStatus.valueOf(508));
    }

    @ExceptionHandler({BadCredentialsException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorData> handleBadCredentialsException(BadCredentialsException exception) {
        return new ResponseEntity<>(ErrorData
                .builder()
                .messageUz(exception.getMessageUz())
                .messageRu(exception.getMessageRu())
                .build(),
                HttpStatus.NOT_FOUND);
    }


}