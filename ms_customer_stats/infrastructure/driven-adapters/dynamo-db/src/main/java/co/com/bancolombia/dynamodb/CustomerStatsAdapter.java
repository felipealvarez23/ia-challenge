package co.com.bancolombia.dynamodb;

import co.com.bancolombia.dynamodb.helper.TemplateAdapterOperations;
import co.com.bancolombia.dynamodb.model.customerstats.CustomerStatsData;
import co.com.bancolombia.model.customerstats.CustomerStats;
import co.com.bancolombia.model.customerstats.gateways.CustomerStatsRepository;
import co.com.bancolombia.model.exception.CustomerStatsException;
import org.reactivecommons.utils.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;

import static co.com.bancolombia.model.constants.ExceptionMessage.DEFAULT_ERROR_MESSAGE;


@Repository
public class CustomerStatsAdapter extends TemplateAdapterOperations<CustomerStats, String, CustomerStatsData> implements CustomerStatsRepository {

    private final static Logger logger = LoggerFactory.getLogger(CustomerStatsAdapter.class);

    public CustomerStatsAdapter(DynamoDbEnhancedAsyncClient connectionFactory, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(connectionFactory, mapper, d -> mapper.map(d, CustomerStats.class), "customer_stats");
    }

    @Override
    public Mono<CustomerStats> saveStats(CustomerStats customerStats) {
        customerStats.setTimestamp(String.valueOf(System.currentTimeMillis()));
        return save(customerStats)
                .doOnSuccess(stats -> logger.info("Customer stats saved on Dynamodb successfully. Timestamp: {}",
                        customerStats.getTimestamp()))
                .doOnError(error -> logger.error("Error saving customer stats on Dynamodb: ",error))
                .onErrorResume(error ->
                        Mono.error(() -> new CustomerStatsException(DEFAULT_ERROR_MESSAGE, error.getMessage())));
    }

}
