/**
 * 
 * File: HTMLExporter.java
 * Create: Sep 11, 2009
 */
package org.free.todolist.export;

import org.free.todolist.model.TodoItem;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

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
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

}
