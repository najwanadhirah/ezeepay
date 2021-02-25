/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rt.qpay99.ws;

/**
 * @author victor.kwok
 */
public class XmlParserException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = 7416354225765076164L;

    public XmlParserException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    /**
     * Creates a new instance of <code>XmlParserException</code> without detail
     * message.
     */
    public XmlParserException() {
    }

    /**
     * Constructs an instance of <code>XmlParserException</code> with the
     * specified detail message.
     * @param msg
     *            the detail message.
     */
    public XmlParserException(String msg) {
        super(msg);
    }
}
