package co.com.bancolombia.usecase.processcustomerstats;

import co.com.bancolombia.model.customerstats.CustomerStats;
import co.com.bancolombia.model.customerstats.gateways.CustomerStatsPublisherRepository;
import co.com.bancolombia.model.customerstats.gateways.CustomerStatsRepository;
import co.com.bancolombia.model.exception.CustomerStatsException;
import co.com.bancolombia.usecase.processcustomerstats.util.HashGenerator;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.model.constants.ExceptionMessage.DEFAULT_ERROR_MESSAGE;

@RequiredArgsConstructor
public class ProcessCustomerStatsUseCase {

    private final CustomerStatsRepository customerStatsRepository;
    private final CustomerStatsPublisherRepository statsPublisherRepository;

    public Mono<Void> process(CustomerStats customerStats) {
        return validateHash(customerStats)
                .then(Mono.defer(()-> Mono.zip(
                        customerStatsRepository.saveStats(customerStats),
                        statsPublisherRepository.publishValidStats(customerStats))))
                .then();
    }

    private Mono<Void> validateHash(CustomerStats stats) {
        String expectedHash = HashGenerator.generateMd5(stats.toValidationString());
        if (!expectedHash.equals(stats.getHash())) {
            return Mono.error(new CustomerStatsException(DEFAULT_ERROR_MESSAGE,"Invalid hash: " + stats.getHash()));
        }
        return Mono.empty();
    }

}
