/**
 * File: Task.java
 * Create: 2009-3-16
 */
package org.free.todolist.manager;

/**
 * A <code>Task</code> is a computable(executable) mission.
 * 
 */
public interface Task{

	String getId();
    /**
     * Set <code>TaskListener</code> to the task.
     * 
     * @param listener
     *            a <code>TaskListener</code> object
     * @see com.jinfonet.webreport.util.concurrent.TaskListener
     */
    void setTaskListener(TaskListener listener);

    /**
     * Set <code>TaskConext<code> to the task.
     * 
     * @param context
     *            a <code>TaskContext</code> object
     * @see com.jinfonet.webreport.util.concurrent.TaskContext
     */
    void setTaskContext(TaskContext context);

    /**
     * Execute the task.
     */
    void execute();

}
