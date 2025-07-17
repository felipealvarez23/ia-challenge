package co.com.bancolombia.model.customerstats.gateways;

import co.com.bancolombia.model.customerstats.CustomerStats;
import reactor.core.publisher.Mono;

public interface CustomerStatsPublisherRepository {
    Mono<CustomerStats> publishValidStats(CustomerStats customerStats);
}
