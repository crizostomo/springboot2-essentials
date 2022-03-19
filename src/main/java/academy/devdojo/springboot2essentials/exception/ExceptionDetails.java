package academy.devdojo.springboot2essentials.exception;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
public class ExceptionDetails {
    protected String title;
    protected int status;
    protected String detail;
    protected LocalDateTime timestamp;
    protected String developerMessage;
}
