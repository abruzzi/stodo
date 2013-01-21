/**
 * Copyright (c) Jinfonet Inc. 2000-2009, All rights reserved. 
 * 
 * File: TimeoutException.java
 * Create: 2009-3-17
 */
package org.free.todolist.manager;

/**
 * @author dong.hu@china.jinfonet.com
 * 
 */
public class TimeoutException extends Exception {

    private static final long serialVersionUID = -8058842573766222578L;

    /**
     * Constructs a <tt>TimeoutException</tt> with no specified detail message.
     */
    public TimeoutException() {
    }

    /**
     * Constructs a <tt>TimeoutException</tt> with the specified detail message.
     * 
     * @param message
     *            the detail message
     */
    public TimeoutException(String message) {
        super(message);
    }

}
