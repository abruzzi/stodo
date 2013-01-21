package org.free.todolist.ui;

import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.ListModel;

import org.free.todolist.model.FilterableListModel;

/**
 * This is a filterable list which extends from JList, and its
 * datamodel is from FilterableListModel. Just like JList, it is
 * using the MVC model as Swing does.
 * 
 * @author juntao.qiu@gmail.com
 *
 */
public class FilterableList extends JList {
	private static final long serialVersionUID = 2827679372675804255L;

	public FilterableList() {
		FilterableListModel model = new FilterableListModel();
		setModel(model);
	}

	/**
	 * register the search box on list
	 */
	public void installFilterField(JTextField input) {
		if (input != null) {
			FilterableListModel model = (FilterableListModel) getModel();
			input.getDocument().addDocumentListener(model);
		}
	}

	/**
	 * unregister the search box on list.
	 */
	public void uninstallFilterField(JTextField input) {
		if (input != null) {
			FilterableListModel model = (FilterableListModel) getModel();
			input.getDocument().removeDocumentListener(model);
		}
	}

	public void setModel(ListModel model) {
		if (!(model instanceof FilterableListModel)) {
			throw new IllegalArgumentException();
		} else {
			super.setModel(model);
		}
	}

	public void addElement(Object element) {
		((FilterableListModel) getModel()).addElement(element);
	}
	
	/**
	 * get the filterable list model of current list
	 * @return
	 */
	public FilterableListModel getContents(){
		return (FilterableListModel)getModel();
	}

}
