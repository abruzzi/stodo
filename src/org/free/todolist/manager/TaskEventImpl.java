/**
 * 
 * File: TaskEventImpl.java
 * Create: 2009-3-16
 */
package org.free.todolist.manager;

/**
 * @author dong.hu@china.jinfonet.com
 * 
 */
public final class TaskEventImpl implements TaskEvent {

    private String task_id;
    private int type;

    private Object result;

    public TaskEventImpl(String task_id, int type) {
        this.task_id = task_id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.jinfonet.webreport.util.concurrent.TaskEvent#getTaskID()
     */
    public String getTaskId() {
        return task_id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.jinfonet.webreport.util.concurrent.TaskEvent#getEventType()
     */
    public int getEventType() {
        return type;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.jinfonet.webreport.util.concurrent.TaskEvent#getResult()
     */
    public Object getResult() {
        return result;
    }

    /**
     * Attach result to this event.
     * 
     * @param result
     */
    public void setResult(Object result) {
        this.result = result;
    }

}
