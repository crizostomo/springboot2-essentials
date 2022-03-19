package academy.devdojo.springboot2essentials.exception;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

//@Data
@Getter
@SuperBuilder
public class ResourceNotFoundDetails extends ExceptionDetails {
//    private String title;
//    private int status;
//    private String detail;
//    private LocalDateTime timestamp;
//    private String developerMessage;
}
