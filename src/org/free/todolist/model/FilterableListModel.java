package org.free.todolist.model;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the data-model of the filterable-list, the items in the list
 * can be searched by key-down event
 * 
 * @author juntao.qiu@gmail.com
 *
 */
public class FilterableListModel extends AbstractListModel implements
		DocumentListener {
	@Serial
	private static final long serialVersionUID = -2409529218176332776L;
	
	private final List<Object> list;
	private final List<Object> filteredList;
	private String lastFilter = "";

	public FilterableListModel() {
		list = new ArrayList<Object>();
		filteredList = new ArrayList<Object>();
	}

	public void addElement(Object element) {
		list.add(element);
		filter(lastFilter);
	}

	public int getSize() {
		return filteredList.size();
	}

	public Object getElementAt(int index) {
		Object returnValue;
		if (index < filteredList.size()) {
			returnValue = filteredList.get(index);
		} else {
			returnValue = null;
		}
		return returnValue;
	}
	
	public void removeElement(int index){
		list.remove(index);
		filter(lastFilter);
	}
	
	/*
	 * search the list, compare two strings, indexOf will return
	 * the index, if != -1, then added it into filtered-list.
	 */
	private void filter(String search) {
		filteredList.clear();
		for (Object element : list) {
			if (element.toString().contains(search)) {
				filteredList.add(element);
			}
		}
		fireContentsChanged(this, 0, getSize());
	}

	public void insertUpdate(DocumentEvent event) {
		Document doc = event.getDocument();
		try {
			lastFilter = doc.getText(0, doc.getLength());
			filter(lastFilter);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	public void removeUpdate(DocumentEvent event) {
		Document doc = event.getDocument();
		try {
			lastFilter = doc.getText(0, doc.getLength());
			filter(lastFilter);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	public void changedUpdate(DocumentEvent event) {
	}

	public void clear() {
		list.clear();
		filteredList.clear();
	}
}
