package co.com.bancolombia.mq.sender;

import co.com.bancolombia.model.customerstats.CustomerStats;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.api.domain.DomainEvent;
import org.reactivecommons.api.domain.DomainEventBus;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerStatsSenderAdapterTest {

    @InjectMocks
    private CustomerStatsSenderAdapter adapter;

    @Mock
    private DomainEventBus eventBus;

    @Test
    @DisplayName("should return custom stats")
    void publishValidStats() {
        var customerStats = new CustomerStats();
        when(eventBus.emit(any(DomainEvent.class))).thenReturn(Mono.just(""));
        Mono<CustomerStats> publishMono = adapter.publishValidStats(customerStats);
        StepVerifier.create(publishMono).expectNextCount(1).verifyComplete();
    }

    @Test
    @DisplayName("should return a error")
    void publishValidStatsError() {
        var customerStats = new CustomerStats();
        when(eventBus.emit(any(DomainEvent.class))).thenReturn(Mono.error(new Exception()));
        Mono<CustomerStats> publishMono = adapter.publishValidStats(customerStats);
        StepVerifier.create(publishMono).expectError().verify();
    }
}