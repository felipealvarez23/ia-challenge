package co.com.bancolombia.api.model.processcustomerstats.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ErrorResponse {
    private String message;
    private String technicalMessage;
}
