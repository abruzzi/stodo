package org.free.todolist.model;

import org.free.todolist.util.JSONException;
import org.free.todolist.util.JSONObject;

/**
 * todo item is a simple tree data-model, it can be used to build a swing-jtree
 * component
 * 
 * @author juntao.qiu@china.jinfonet.com
 * 
 */
public class TodoItem {
	//inner id of each item
	private String id;
	
	//description of what you about doing
	private String desc;

	//the item type, may be the {personal, work, misc, etc}
	private String type;

	//when of the task timeout?
	private String timeout;
	
	//period of the task, {each Monday or the like}
	private String period;

	//current status of a task {new, finished, pending, cancel}
	private String status;

	//content, more that one line information should be added into <note>
	private String note;

	public TodoItem() {
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTimeout() {
		return timeout;
	}

	public void setTimeout(String timeout) {
		this.timeout = timeout;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	public String toString(){
		StringBuffer buffer = new StringBuffer();
		buffer
		.append("[")
			.append("id : "+id)
			.append(",desc : "+desc)
			.append(",type : "+type)
			.append(",timeout : "+timeout)
			.append(",period : "+period)
			.append(",status : "+status)
			.append(",note : "+note)
		.append("]");
		
		return buffer.toString();
	}
	
	public static TodoItem parse(String json) throws Exception{
		JSONObject obj = new JSONObject(json);
		return _parse_(obj);
	}
	
	private static TodoItem _parse_(JSONObject json){
		TodoItem item = new TodoItem();
		
		item.setDesc(json.optString("desc", ""));
		item.setType(json.optString("type", "node"));
		item.setTimeout(json.optString("timeout"));
		item.setPeriod(json.optString("period", "never"));
		item.setStatus(json.optString("status", "new"));
		item.setNote(json.optString("note"));
		
		return item;
	}
	
	public static void main(String[] args) {
		
	}
}
