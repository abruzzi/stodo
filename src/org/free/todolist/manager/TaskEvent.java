/**
 * Copyright (c) Jinfonet Inc. 2000-2009, All rights reserved. 
 * 
 * File: TaskEvent.java
 * Create: 2009-3-16
 */
package org.free.todolist.manager;

/**
 * The <code>TaskEvent</code> is used to indicate various possible events which
 * produce in task executing process, and it can be handled by a
 * <code>TaskListener</code>.
 * 
 * @author dong.hu@china.jinfonet.com
 * 
 */
public interface TaskEvent {
    /**
     * Preserved task event type, indicate the current task is finished
     */
    static final int TASK_FINISHED = 1 << 0;
    /**
     * Preserved task event type, indicate the current task will generate more
     * tasks.
     */
    static final int TASK_NEWTASKS = 1 << 1;

    /**
     * Return event type of this event.
     * 
     * @return the event type
     */
    int getEventType();

    /**
     * Return the task ID which raised this event.
     * 
     * @return the task ID
     */
    String getTaskId();

    /**
     * Return the result that attached to the event.
     * 
     * @return
     */
    Object getResult();
}
