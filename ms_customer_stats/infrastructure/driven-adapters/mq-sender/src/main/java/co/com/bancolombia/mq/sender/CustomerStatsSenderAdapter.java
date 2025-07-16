package co.com.bancolombia.mq.sender;

import co.com.bancolombia.model.customerstats.CustomerStats;
import co.com.bancolombia.model.customerstats.gateways.CustomerStatsPublisherRepository;
import lombok.RequiredArgsConstructor;
import org.reactivecommons.api.domain.DomainEvent;
import org.reactivecommons.api.domain.DomainEventBus;
import org.reactivecommons.async.impl.config.annotations.EnableDomainEventBus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@EnableDomainEventBus
@RequiredArgsConstructor
public class CustomerStatsSenderAdapter implements CustomerStatsPublisherRepository {

    private final DomainEventBus eventBus;

    @Override
    public Mono<Void> publishValidStats(CustomerStats customerStats) {
        return Mono.from(eventBus.emit(new DomainEvent<>("event.stats.validated", UUID.randomUUID().toString(), customerStats)))
                .doOnError(e-> System.out.println(e.getMessage()));
    }
}
