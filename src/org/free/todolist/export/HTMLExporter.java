/**
 * 
 * File: HTMLExporter.java
 * Create: Sep 11, 2009
 */
package org.free.todolist.export;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.free.todolist.model.TodoItem;

/**
 *
 * @author juntao.qiu@gmail.com
 *
 */
public class HTMLExporter implements Exporter {
	private List<TodoItem> items;
	private String content;
	private String file;
	
	public HTMLExporter(List<TodoItem> items, String file){
		this.items = items;
		this.file = file;
	}
	
	/* (non-Javadoc)
	 * @see org.free.todolist.export.Exporter#doExport()
	 */
	public String doExport() {
		return content;
	}

	/* (non-Javadoc)
	 * @see org.free.todolist.export.Exporter#store()
	 */
	public void store(){
		FileWriter writer = null;
		try {
			writer = new FileWriter(file);
			writer.write(content);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(writer != null){
				try{writer.close();}catch(Exception e){}
			}
		}
	}

}
