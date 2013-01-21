package org.free.todolist.manager;

import java.text.DateFormat;
import java.text.ParseException;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.free.todolist.data.SimpleTextMail;
import org.free.todolist.mail.MailSender;
import org.free.todolist.model.TodoItem;
import org.free.todolist.ui.AlarmDialog;

/**
 * This class used to do some back-ground tasks, such as:<br>
 * <code>send a email</code><br>
 * <code>schedule a todo</code><br>
 * 
 * @author juntao.qiu@gmail.com
 *
 */
public class TaskService {
	private TaskService(){
		taskMgr = TaskManager.getInstance();
	}
	
	private static TaskService alerm;
	private static TaskManager taskMgr;
	
	public static TaskService getInstance(){
		synchronized(TaskService.class){
			if(alerm == null){
				alerm = new TaskService();
			}
		}
		return alerm;
	}
	
	public void sendMail(SimpleTextMail mail){
		Task carrier = new Carrier(mail);
		taskMgr.scheduleTask(carrier, 0);
	}
	
	public void scheduleItem(TodoItem item){
		Task nt = new Alert(item);
		long now = System.currentTimeMillis();
		long timeout = convert(item.getTimeout());
		long delay = timeout - now;
		
		//if the delay big than 5 seconds, then schedule
		//it, else just ignore it.
		if(delay > 5000){
			taskMgr.scheduleTask(nt, delay);	
		}
	}
	
	public boolean isItemScheduled(TodoItem item){
		return taskMgr.has(new Alert(item));
	}
	
	private long convert(String timeout){
	    long later = 0L;
	    DateFormat format = DateFormat.getDateTimeInstance();
	    try {
            later = format.parse(timeout).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
	    return later;
	}
	
	public void cancelSchedule(TodoItem item){
		Task nt = new Alert(item);
		taskMgr.cancelTask(nt.getId());
	}
	
	class Carrier implements Task{
		private SimpleTextMail mail; 
		
		public Carrier(SimpleTextMail mail){
			this.mail = mail;
		}
		
		public void execute() {
			MailSender sender = new MailSender(mail);
			boolean st = sender.send();
			if(st){
				JOptionPane.showMessageDialog(
						null, 
						"mail is sent", 
						"mail is sent!!", 
						JOptionPane.INFORMATION_MESSAGE);
			}
		}

		public String getId() {
			return null;
		}

		public void setTaskContext(TaskContext context) {
			
		}

		public void setTaskListener(TaskListener listener) {
			
		}
		
	}
	
	class Alert implements Task{
		private TodoItem item;
		
		public Alert(TodoItem item) {
			this.item = item;
		}
		
		public void execute(){
			AlarmDialog dialog = new AlarmDialog(item);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setAlwaysOnTop(true);
			dialog.setLocationRelativeTo(null);
			dialog.setSize(396, 180);
			dialog.setVisible(true);
		}

		public String getId() {
			String id = item.getId();
			return id == null ? "" : id;
		}

		public void setTaskContext(TaskContext context) {
			
		}

		public void setTaskListener(TaskListener listener) {
			
		}
	}
}
