package co.com.bancolombia.api.model.processcustomerstats.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CustomerStatsResponse {
    private ErrorResponse error;
}
