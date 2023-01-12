package ewmservice.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ErrorResponse {
    private List<String> errors;
    private String reason;
    private ErrorState status;
    private LocalDateTime timestamp;

    public ErrorResponse(List<String> errors, String reason, ErrorState status, LocalDateTime timestamp) {
        this.errors = errors;
        this.reason = reason;
        this.status = status;
        this.timestamp = timestamp;
    }
}