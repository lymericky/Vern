package com.denommeinc.vern.exceptions;

/**
 * Thrown when there is a "BUS INIT... ERROR" message
 *
 */
public class BusInitException extends ResponseException {

    /**
     * <p>Constructor for BusInitException.</p>
     */
    public BusInitException() {
        super("BUS INIT... ERROR");
    }

}
