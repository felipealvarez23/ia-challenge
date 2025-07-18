package co.com.bancolombia.api.model.processcustomerstats.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CustomerStatsRequest {
    @NotNull
    private Integer totalContactoClientes;
    @NotNull
    private Integer motivoReclamo;
    @NotNull
    private Integer motivoGarantia;
    @NotNull
    private Integer motivoDuda;
    @NotNull
    private Integer motivoCompra;
    @NotNull
    private Integer motivoFelicitaciones;
    @NotNull
    private Integer motivoCambio;
    @NotNull
    private String hash;
}
