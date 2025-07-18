package co.com.bancolombia.mq.sender;

import co.com.bancolombia.model.customerstats.CustomerStats;
import co.com.bancolombia.model.customerstats.gateways.CustomerStatsPublisherRepository;
import lombok.RequiredArgsConstructor;
import org.reactivecommons.api.domain.DomainEvent;
import org.reactivecommons.api.domain.DomainEventBus;
import org.reactivecommons.async.impl.config.annotations.EnableDomainEventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;


@Service
@EnableDomainEventBus
@RequiredArgsConstructor
public class CustomerStatsSenderAdapter implements CustomerStatsPublisherRepository {

    private static final Logger logger = LoggerFactory.getLogger(CustomerStatsSenderAdapter.class);

    private final DomainEventBus eventBus;

    @Override
    public Mono<CustomerStats> publishValidStats(CustomerStats customerStats) {
        return Mono.from(eventBus.emit(new DomainEvent<>("event.stats.validated", UUID.randomUUID().toString(), customerStats)))
                .doOnSuccess(stats -> logger.info("Customer stats published on RabbitMQ successfully. Timestamp: {}",
                        customerStats.getTimestamp()))
                .thenReturn(customerStats)
                .doOnError(e-> logger.error("Error publishing customer stats event on RabbitMQ", e));
    }
}
