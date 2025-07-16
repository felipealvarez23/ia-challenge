package co.com.bancolombia.model.customerstats;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CustomerStats {
    private String timestamp;
    private Integer totalCustomerContacts;
    private Integer reasonClaim;
    private Integer reasonWarranty;
    private Integer reasonQuery;
    private Integer reasonPurchase;
    private Integer reasonCongratulations;
    private Integer reasonChange;
    private String hash;

    /**
     * Método de negocio para generar la cadena de texto que se usará
     * para validar el hash MD5.
     * @return Una cadena con los valores concatenados, ej: "250,25,10,100,100,7,8"
     */
    public String toValidationString() {
        return String.join(",",
                String.valueOf(totalCustomerContacts),
                String.valueOf(reasonClaim),
                String.valueOf(reasonWarranty),
                String.valueOf(reasonQuery),
                String.valueOf(reasonPurchase),
                String.valueOf(reasonCongratulations),
                String.valueOf(reasonChange)
        );
    }
}
