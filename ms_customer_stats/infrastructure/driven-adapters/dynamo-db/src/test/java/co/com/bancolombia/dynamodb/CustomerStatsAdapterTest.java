package co.com.bancolombia.dynamodb;

import co.com.bancolombia.dynamodb.model.customerstats.CustomerStatsData;
import co.com.bancolombia.model.customerstats.CustomerStats;
import co.com.bancolombia.model.exception.CustomerStatsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.test.StepVerifier;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerStatsAdapterTest {

    @Mock
    private DynamoDbAsyncTable<CustomerStatsData> table;

    @Mock
    private DynamoDbEnhancedAsyncClient asyncClient;

    @Mock
    private ObjectMapper mapper;

    private CustomerStatsAdapter customerStatsAdapter;

    private CustomerStatsData customerStatsData;

    @BeforeEach
    void setUp() {
        when(asyncClient.table("customer_stats", TableSchema.fromBean(CustomerStatsData.class)))
                .thenReturn(table);
        customerStatsData = new CustomerStatsData();
        customerStatsAdapter = new CustomerStatsAdapter(asyncClient, mapper);
    }

    @Test
    @DisplayName("should save customer stats")
    void saveStats() {
        var customerStats = new CustomerStats();
        when(table.putItem(customerStatsData)).thenReturn(CompletableFuture.runAsync(()->{}));
        when(mapper.map(customerStats, CustomerStatsData.class)).thenReturn(customerStatsData);
        var saveStatsMono = customerStatsAdapter.saveStats(customerStats);
        StepVerifier.create(saveStatsMono)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void saveStatsError() {
        var customerStats = new CustomerStats();
        when(mapper.map(customerStats, CustomerStatsData.class)).thenReturn(customerStatsData);
        when(table.putItem(customerStatsData)).thenReturn(CompletableFuture.failedFuture(new NullPointerException()));
        var saveStatsMono = customerStatsAdapter.saveStats(customerStats);
        StepVerifier.create(saveStatsMono)
                .expectErrorMatches(ex -> ex instanceof CustomerStatsException)
                .verify();
    }
}