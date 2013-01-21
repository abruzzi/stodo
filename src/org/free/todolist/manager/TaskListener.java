/**
 * Copyright (c) Jinfonet Inc. 2000-2009, All rights reserved. 
 * 
 * File: TaskListener.java
 * Create: 2009-3-16
 */
package org.free.todolist.manager;

/**
 * A <code>TaskListener</code> define a call back interface when catch a task
 * event.
 * 
 * @author dong.hu@china.jinfonet.com
 * 
 */
public interface TaskListener {
    /**
     * Handle the task event.
     * 
     * @param event
     *            a <code>TaskEvent</code> object.
     * @see com.jinfonet.webreport.util.concurrent.TaskEvent
     */
    void handleTaskEvent(TaskEvent event);

}
