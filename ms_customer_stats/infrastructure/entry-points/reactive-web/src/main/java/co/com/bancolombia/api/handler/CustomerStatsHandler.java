package co.com.bancolombia.api.handler;

import co.com.bancolombia.api.mapper.CustomerStatsMapper;
import co.com.bancolombia.api.model.processcustomerstats.request.CustomerStatsRequest;
import co.com.bancolombia.api.model.processcustomerstats.response.CustomerStatsResponse;
import co.com.bancolombia.api.model.processcustomerstats.response.ErrorResponse;
import co.com.bancolombia.model.exception.CustomerStatsException;
import co.com.bancolombia.usecase.processcustomerstats.ProcessCustomerStatsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CustomerStatsHandler {

    private final ProcessCustomerStatsUseCase processCustomerStatsUseCase;
    private final CustomerStatsMapper customerStatsMapper;

    public Mono<ServerResponse> processCustomerStats(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CustomerStatsRequest.class)
                .map(customerStatsMapper::toDomain)
                .flatMap(processCustomerStatsUseCase::process)
                .then(ServerResponse.ok().build())
                .onErrorResume(CustomerStatsException.class, this::buildErrorResponse);
    }

    private Mono<ServerResponse> buildErrorResponse(CustomerStatsException ex) {
        return ServerResponse.badRequest().contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(CustomerStatsResponse.builder()
                        .error(ErrorResponse.builder()
                                .message(ex.getMessage())
                                .technicalMessage(ex.getTechnicalMessage())
                                .build())
                        .build()), CustomerStatsResponse.class);
    }

}
