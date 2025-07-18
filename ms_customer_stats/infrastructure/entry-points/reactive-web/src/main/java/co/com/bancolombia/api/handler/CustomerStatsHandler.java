package co.com.bancolombia.api.handler;

import co.com.bancolombia.api.helper.AbstractValidationHandler;
import co.com.bancolombia.api.mapper.CustomerStatsMapper;
import co.com.bancolombia.api.model.processcustomerstats.request.CustomerStatsRequest;
import co.com.bancolombia.api.model.processcustomerstats.response.CustomerStatsResponse;
import co.com.bancolombia.api.model.processcustomerstats.response.ErrorResponse;
import co.com.bancolombia.model.exception.CustomerStatsException;
import co.com.bancolombia.usecase.processcustomerstats.ProcessCustomerStatsUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.model.constants.ExceptionMessage.DEFAULT_ERROR_MESSAGE;

@Component
public class CustomerStatsHandler extends AbstractValidationHandler<CustomerStatsRequest, Validator> {

    private final ProcessCustomerStatsUseCase processCustomerStatsUseCase;
    private final CustomerStatsMapper customerStatsMapper;

    public CustomerStatsHandler(@Autowired Validator validator,
                                CustomerStatsMapper customerStatsMapper,
                                ProcessCustomerStatsUseCase processCustomerStatsUseCase) {
        super(CustomerStatsRequest.class, validator);
        this.customerStatsMapper = customerStatsMapper;
        this.processCustomerStatsUseCase = processCustomerStatsUseCase;
    }

//    public Mono<ServerResponse> processCustomerStats(ServerRequest serverRequest) {
//        return serverRequest.bodyToMono(CustomerStatsRequest.class)
//                .map(customerStatsMapper::toDomain)
//                .flatMap(processCustomerStatsUseCase::process)
//                .then(ServerResponse.ok().build())
//                .onErrorResume(CustomerStatsException.class, this::buildErrorResponse);
//    }

    private Mono<ServerResponse> buildErrorResponse(CustomerStatsException ex) {
        return ServerResponse.badRequest().contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(CustomerStatsResponse.builder()
                        .error(ErrorResponse.builder()
                                .message(ex.getMessage())
                                .technicalMessage(ex.getTechnicalMessage())
                                .build())
                        .build()), CustomerStatsResponse.class);
    }

    @Override
    protected Mono<ServerResponse> processBody(CustomerStatsRequest validBody, ServerRequest originalRequest) {
        return Mono.just(customerStatsMapper.toDomain(validBody))
                .flatMap(processCustomerStatsUseCase::process)
                .then(ServerResponse.ok().build())
                .onErrorResume(CustomerStatsException.class, this::buildErrorResponse);
    }

    @Override
    protected Mono<ServerResponse> exceptionProcessBody(CustomerStatsRequest body, Exception ex) {
        return ServerResponse.badRequest().contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(CustomerStatsResponse.builder()
                        .error(ErrorResponse.builder()
                                .message(DEFAULT_ERROR_MESSAGE)
                                .technicalMessage(ex.getMessage())
                                .build())
                        .build()), CustomerStatsResponse.class);
    }
}
