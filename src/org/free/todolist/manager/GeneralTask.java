/**
 * File: GeneralTask.java
 * Create: 2009-3-16
 */
package org.free.todolist.manager;


/**
 * A general task implementation, this task does nothing.
 * 
 * @author dong.hu@china.jinfonet.com
 * 
 */
public class GeneralTask implements Task {

    private String task_id;

    protected TaskListener listener;
    protected TaskContext context;

    public GeneralTask(String task_id) {
        this.task_id = task_id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.jinfonet.webreport.util.concurrent.Task#getTaskID()
     */
    public String getId() {
        return task_id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.jinfonet.webreport.util.concurrent.Task#setTaskListener(com.jinfonet
     * .webreport.util.concurrent.TaskListener)
     */
    public final void setTaskListener(TaskListener listener) {
        this.listener = listener;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.jinfonet.webreport.util.concurrent.Task#setTaskContext(com.jinfonet
     * .webreport.util.concurrent.TaskContext)
     */
    public final void setTaskContext(TaskContext context) {
        this.context = context;
    }

    /**
     * Note: User should overload this method
     * 
     * @see com.jinfonet.webreport.util.concurrent.Task#execute()
     */
    public void execute() {
        if (listener != null) {
            listener.handleTaskEvent(new TaskEventImpl(task_id,
                    TaskEvent.TASK_FINISHED));
        }
    }

}
