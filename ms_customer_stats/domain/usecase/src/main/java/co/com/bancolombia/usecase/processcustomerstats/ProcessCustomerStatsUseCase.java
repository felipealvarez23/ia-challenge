package co.com.bancolombia.usecase.processcustomerstats;

import co.com.bancolombia.model.customerstats.CustomerStats;
import co.com.bancolombia.model.customerstats.gateways.CustomerStatsPublisherRepository;
import co.com.bancolombia.model.customerstats.gateways.CustomerStatsRepository;
import co.com.bancolombia.model.exception.InvalidHashException;
import co.com.bancolombia.usecase.processcustomerstats.util.HashGenerator;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ProcessCustomerStatsUseCase {

    private final CustomerStatsRepository customerStatsRepository;
    private final CustomerStatsPublisherRepository statsPublisherRepository;

    public Mono<Void> process(CustomerStats customerStats) {
        return validateHash(customerStats)
                .then(customerStatsRepository.saveStats(customerStats))
                .flatMap(statsPublisherRepository::publishValidStats)
                .then();
    }

    private Mono<Void> validateHash(CustomerStats stats) {
        String expectedHash = HashGenerator.generateMd5(stats.toValidationString());
        if (!expectedHash.equals(stats.getHash())) {
            return Mono.error(new InvalidHashException("Invalid hash"));
        }
        return Mono.empty();
    }

}
