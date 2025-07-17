package co.com.bancolombia.model.exception;

import lombok.Getter;

@Getter
public class CustomerStatsException extends RuntimeException  {

    private final String technicalMessage;

    public CustomerStatsException(String message, String technicalMessage) {
        super(message);
        this.technicalMessage = technicalMessage;
    }
}
