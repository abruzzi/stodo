/**
 * Copyright (c) Jinfonet Inc. 2000-2009, All rights reserved. 
 * 
 * File: Executor.java
 * Create: 2009-3-16
 */
package org.free.todolist.manager;

/**
 * A <code>Executor</code> is an abstract and reusable computing resources can
 * be used to complete a task calculation. The <code>Executor</code> is got from
 * a executor pool, and it will be return back to pool after calculating.
 * 
 * @author dong.hu@china.jinfonet.com
 * 
 */
public interface Executor {
    /**
     * Set a <code>Task</code> to a <code>Executor<code>.
     * 
     * @param task
     *            A <code>Task</code> object
     * @see com.jinfonet.webreport.util.concurrent.Task
     */
    void setTask(Task task);

    /**
     * Start the task.
     */
    void begin();
    
    /**
     * Cancel the task
     */
    void cancel();

}
