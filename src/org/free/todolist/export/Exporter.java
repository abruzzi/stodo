package org.free.todolist.export;

public interface Exporter {
	/**
	 * do export action, export the result to string
	 * @return
	 */
	public String doExport();
	
	/**
	 * store the content to a file
	 */
	public void store();
}
