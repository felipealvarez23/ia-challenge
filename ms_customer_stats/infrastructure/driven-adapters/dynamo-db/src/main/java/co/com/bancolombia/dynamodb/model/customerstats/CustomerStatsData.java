package co.com.bancolombia.dynamodb.model.customerstats;

import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Getter
@Setter
@DynamoDbBean
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CustomerStatsData {

    private String timestamp;
    private Integer totalCustomerContacts;
    private Integer reasonClaim;
    private Integer reasonWarranty;
    private Integer reasonQuery;
    private Integer reasonPurchase;
    private Integer reasonCongratulations;
    private Integer reasonChange;
    private String hash;

    @DynamoDbPartitionKey
    public String getTimestamp() {
        return timestamp;
    }
}
