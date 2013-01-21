/**
 * Copyright (c) Jinfonet Inc. 2000-2009, All rights reserved. 
 * 
 * File: TaskContext.java
 * Create: 2009-3-16
 */
package org.free.todolist.manager;

/**
 * A <code>TaskContext</code> is a task dependent on context environment.
 * 
 * @author dong.hu@china.jinfonet.com
 * 
 */
public interface TaskContext {
    /**
     * Return the object with specified string key.
     * 
     * @param key
     *            the object key name
     * @return the object
     */
    Object getObject(String key);

    /**
     * Put an object with specified string key to the context.
     * 
     * @param key
     *            the object key name
     * @param o
     *            the object
     */
    void putObject(String key, Object o);

}
