package co.com.bancolombia.usecase.processcustomerstats;

import co.com.bancolombia.model.customerstats.CustomerStats;
import co.com.bancolombia.model.customerstats.gateways.CustomerStatsPublisherRepository;
import co.com.bancolombia.model.customerstats.gateways.CustomerStatsRepository;
import co.com.bancolombia.model.exception.CustomerStatsException;
import co.com.bancolombia.usecase.processcustomerstats.util.HashGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ProcessCustomerStatsUseCaseTest {

    @Mock
    private CustomerStatsRepository customerStatsRepository;

    @Mock
    private CustomerStatsPublisherRepository customerStatsPublisherRepository;

    @InjectMocks
    private ProcessCustomerStatsUseCase useCase;

    private CustomerStats customerStats;

    @BeforeEach
    void setUp() {
        customerStats = CustomerStats.builder()
                .totalCustomerContacts(250)
                .reasonClaim(25)
                .reasonWarranty(10)
                .reasonQuery(100)
                .reasonPurchase(100)
                .reasonCongratulations(7)
                .reasonChange(8)
                .build();
    }

    @Test
    @DisplayName("when valid hash then should save stats")
    void processSaveStats() {
        var validHash = HashGenerator.generateMd5(customerStats.toValidationString());
        customerStats.setHash(validHash);
        when(customerStatsRepository.saveStats(any(CustomerStats.class))).thenReturn(Mono.just(customerStats));
        when(customerStatsPublisherRepository.publishValidStats(any(CustomerStats.class))).thenReturn(Mono.empty());
        Mono<Void> processMono = useCase.process(customerStats);
        StepVerifier.create(processMono).verifyComplete();
        verify(customerStatsRepository, only()).saveStats(any(CustomerStats.class));
    }

    @Test
    @DisplayName("when valid hash then should publish stats")
    void processPublishStats() {
        var validHash = HashGenerator.generateMd5(customerStats.toValidationString());
        customerStats.setHash(validHash);
        when(customerStatsRepository.saveStats(any(CustomerStats.class))).thenReturn(Mono.just(customerStats));
        when(customerStatsPublisherRepository.publishValidStats(any(CustomerStats.class))).thenReturn(Mono.empty());
        Mono<Void> processMono = useCase.process(customerStats);
        StepVerifier.create(processMono).verifyComplete();
        verify(customerStatsPublisherRepository, only()).publishValidStats(any(CustomerStats.class));
    }

    @Test
    @DisplayName("when invalid hash then should return business error")
    void processInvalidHash() {
        var customerStats = new CustomerStats();
        customerStats.setHash("invalidHash");
        Mono<Void> processMono = useCase.process(customerStats);
        StepVerifier.create(processMono)
                .expectError(CustomerStatsException.class)
                .verify();
        verify(customerStatsRepository, never()).saveStats(any(CustomerStats.class));
        verify(customerStatsPublisherRepository, never()).publishValidStats(any(CustomerStats.class));
    }
}