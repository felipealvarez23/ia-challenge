package co.com.bancolombia.api.handler;

import co.com.bancolombia.api.RouterRest;
import co.com.bancolombia.api.mapper.CustomerStatsMapper;
import co.com.bancolombia.api.model.processcustomerstats.request.CustomerStatsRequest;
import co.com.bancolombia.api.model.processcustomerstats.response.CustomerStatsResponse;
import co.com.bancolombia.model.customerstats.CustomerStats;
import co.com.bancolombia.model.exception.CustomerStatsException;
import co.com.bancolombia.usecase.processcustomerstats.ProcessCustomerStatsUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.validation.Validator;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerStatsHandlerTest {

    @Mock
    private ProcessCustomerStatsUseCase processCustomerStatsUseCase;

    @Mock
    private CustomerStatsMapper mapper;

    @Mock
    private Validator validator;

    @InjectMocks
    private CustomerStatsHandler customerStatsHandler;

    private WebTestClient client;

    private RouterRest router;

    @BeforeEach
    void setUp() {
        router = new RouterRest();
        client = WebTestClient
                .bindToRouterFunction(router.routerFunction(customerStatsHandler))
                .build();
    }

    @Test
    void processCustomerStats() {
        var request = new CustomerStatsRequest();
        when(processCustomerStatsUseCase.process(any(CustomerStats.class))).thenReturn(Mono.empty());
        when(mapper.toDomain(any(CustomerStatsRequest.class))).thenReturn(new CustomerStats());
        client.post()
                .uri("/api/stats")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerStatsRequest.class)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void processCustomerStatError() {
        var request = new CustomerStatsRequest();
        when(mapper.toDomain(any(CustomerStatsRequest.class))).thenReturn(new CustomerStats());
        when(processCustomerStatsUseCase.process(any(CustomerStats.class)))
                .thenReturn(Mono.error(new CustomerStatsException("","")));
        client.post()
                .uri("/api/stats")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerStatsRequest.class)
                .header("User-Agent", "Mozilla/5.0 (Linux; Android 7.0)")
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void processCustomerStatEmptyError() {
        client.post()
                .uri("/api/stats")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.empty(), CustomerStatsRequest.class)
                .exchange()
                .expectStatus().is4xxClientError().expectBody(CustomerStatsResponse.class);
    }
}