/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rt.qpay99.ws;

/**
 * @author victor.kwok
 */
public class TimeoutException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = 9033003807904656557L;

    /**
     * Creates a new instance of <code>TimeoutException</code> without detail
     * message.
     */
    public TimeoutException() {
    }

    /**
     * Constructs an instance of <code>TimeoutException</code> with the
     * specified detail message.
     * @param msg
     *            the detail message.
     */
    public TimeoutException(String msg) {
        super(msg);
    }
}
