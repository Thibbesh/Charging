package com.evbox.charging.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * ChargingDataNotFoundException is userDefinedException
 * This exception class will handle runTimeException
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ChargingDataNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -9054724507398243555L;

    /**
     * If no session then will throw ChargingDataNotFoundException.
     * @param arg
     */
    public ChargingDataNotFoundException(String arg) {
        super(arg);
    }
}
