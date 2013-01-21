package org.free.todolist.ui;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;

import org.free.todolist.data.DataService;
import org.free.todolist.export.Exporter;
import org.free.todolist.export.HTMLExporter;
import org.free.todolist.manager.TaskService;
import org.free.todolist.model.FilterableListModel;
import org.free.todolist.model.TodoItem;
import org.free.todolist.plugin.Plugin;
import org.free.todolist.plugin.TodoPluginManager;

/**
 * 
 * @author juntao.qiu@gmail.com
 * 
 *         created :
 * 
 *         modified : 2009/09/16
 * 
 */
public class MainFrame extends JFrame {
	private static final long serialVersionUID = 320412556766404024L;

	//new task dialog, used to add task
	private NewTaskDialog newTaskDialog;
	
	//preference dialog, used to configure user-preference
	private PreferenceDialog preferenceDialog;

	//system tray icon
	private TrayIcon trayIcon;

	//system tray
	private SystemTray systemTray;

	// pop-up menu
	//pop-up menu
	private JPopupMenu pmOnItem;

	// menu-bar of the frame
	private JMenuBar mbar;

	// tool-bar of the frame
	private JToolBar toolbar;

	// the search-box on tool-bar
	private JTextField filter;

	// scroller of list panel
	private JScrollPane scroller;

	// pop-up listener,
	private MouseListener popupListener;

	// FilterableList is extended from JList, and it has a data-model
	// FilterableListModel, overrided the default one.

	//the filter-able list, this is the main content of sTodo
	private FilterableList ftodolist;
	
	//open the frame, which can be accessed from outside of the frame
	
	public JMenuBar getMenubar(){
	    return mbar;
	}

	public JFrame getFrame(){
	    return this;
	}
	
	public JToolBar getToolbar(){
	    return toolbar;
	}
	
	public JTextField getFilter(){
	    return filter;
	}
	
	public FilterableList getTodoList(){
		return ftodolist;
	}

	public MainFrame(String title) {
		super(title);
	}

	public void setLookAndFeel(String lnf) {
		String className = UIManager.getCrossPlatformLookAndFeelClassName();
		if (lnf.equalsIgnoreCase("windows")) {
			className = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
		} else if (lnf.equalsIgnoreCase("metal")) {
			className = "javax.swing.plaf.metal.MetalLookAndFeel";
		} else if (lnf.equalsIgnoreCase("motif")) {
			className = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
		} else if (lnf.equalsIgnoreCase("system")) {
			className = UIManager.getSystemLookAndFeelClassName();
		} else {
			className = UIManager.getCrossPlatformLookAndFeelClassName();
		}

		try {
			UIManager.setLookAndFeel(className);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * quick edit on an item
	 * 
	 * @author juntao.qiu
	 * 
	 */
	class ListItemListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				JList list = (JList) e.getSource();
				int index = list.locationToIndex(e.getPoint());
				TodoItem item = (TodoItem) list.getModel().getElementAt(index);

				// show the Edit-Task-Dialog
				EditTaskDialog editTaskDialog = new EditTaskDialog(
						MainFrame.this, "Edit exist task", item);
				editTaskDialog.setLocationRelativeTo(null);// center of the
															// screen
				editTaskDialog.setVisible(true);
			}
		}
	}

	/**
	 * a popup menu, for do actions on list-item easily
	 * 
	 * @author juntao.qiu
	 * 
	 */
	class PopupListener extends MouseAdapter implements ActionListener {
		JPopupMenu popupMenu;
		Component selected;
		Point point;

		PopupListener(JPopupMenu popupMenu) {
			this.popupMenu = popupMenu;
			initEventHandlers();
		}

		private void initEventHandlers() {
			Component[] menus = popupMenu.getComponents();
			for (Component item : menus) {
				if (!(item instanceof JPopupMenu.Separator)) {
					((JMenuItem) item).addActionListener(this);
				}
			}
		}

		public void mousePressed(MouseEvent e) {
			showPopupMenu(e);
		}

		public void mouseReleased(MouseEvent e) {
			showPopupMenu(e);
		}

		private void showPopupMenu(MouseEvent e) {
			if (e.isPopupTrigger()) {
				selected = e.getComponent();
				point = e.getPoint();
				if (selected instanceof JList) {
					JList list = (JList) selected;
					int index = list.locationToIndex(point);
					list.setSelectedIndex(index);
				}
				popupMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		}

		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();

			JList list = (JList) selected;
			int index = list.locationToIndex(point);
			TodoItem item = (TodoItem) list.getModel().getElementAt(index);

			if (command.equals("Delete item")) {
				if (JOptionPane.showConfirmDialog(MainFrame.this,
						"Are you sure you want to delete item?",
						"Confirm - sTodo", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					deleteItem(item);// not delete the item from database by
										// now.
				}
			} else if (command.equals("Edit item")) {
				EditTaskDialog editTaskDialog = new EditTaskDialog(
						MainFrame.this, "Edit exist task", item);
				editTaskDialog.setLocationRelativeTo(null);
				editTaskDialog.setVisible(true);
			} else if (command.equals("Mail this item")) {
				NewMailDialog newMailDialog = new NewMailDialog(MainFrame.this,
						"Send this item ", item);
				newMailDialog.setLocationRelativeTo(null);
				newMailDialog.setVisible(true);
			} else if (command.equals("Get help")) {
				// show help doc
			}
		}
	}

	/*
	 * format tooltip of one item
	 */
	private String formatTooltip(TodoItem item) {
		StringBuffer formatted = new StringBuffer();

		formatted.append("<html>");
		formatted.append("<b>Description : </b>").append(item.getDesc())
				.append(", ");
		formatted.append("<b>Status : </b>").append(item.getStatus()).append(
				", ");
		formatted.append("<b>Timeout : </b>").append(item.getTimeout());
		formatted.append("</html>");

		return formatted.toString();
	}

	/*
	 * get all tasks from database, and then schedule them in TaskService
	 */
	private void initTasks() {
		DataService ds = DataService.getInstance();
		TaskService ts = TaskService.getInstance();

		List<TodoItem> list = ds.getAllItems();
		for (TodoItem item : list) {
			ts.scheduleItem(item);
		}
	}

	/*
	 * the key component of sTodo, the TODO-List, load items form
	 * database, render them in a sub-class of JList
	 */
	private void initContentList() {
		DataService ds = DataService.getInstance();
		List<TodoItem> tlist = ds.getAllItems();

		// load all items from database, and schedule
		// them one by one. initialize the task manager
		initTasks();

		ftodolist = new FilterableList();
		FilterableListModel model = ftodolist.getContents();
		for (TodoItem item : tlist) {
			model.addElement(item);
		}

		ftodolist.setCellRenderer(new TodoListCellRenderer());
		ftodolist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scroller = new JScrollPane(ftodolist);

		ToolTipManager.sharedInstance().registerComponent(ftodolist);

		ftodolist.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseMoved(MouseEvent e) {
				JList list = (JList) e.getSource();
				int index = list.locationToIndex(e.getPoint());
				if (index <= 0)
					return;
				TodoItem item = (TodoItem) list.getModel().getElementAt(index);
				list.setToolTipText(null);
				String tooltip = formatTooltip(item);
				list.setToolTipText(tooltip);
			}
		});

		ftodolist.addMouseListener(new ListItemListener());
		ftodolist.addMouseListener(popupListener);
	}

	/*
	 * search box of sTodo
	 */
	private void initSearchBox() {
		toolbar = new JToolBar();
		filter = new JTextField();
		ftodolist.installFilterField(filter);
		toolbar.add(filter, BorderLayout.CENTER);
		toolbar.setVisible(false);

		/*
		 * add keyboard shortcuts : Ctrl + F : search ... Slash '/': search ...
		 */
		KeyListener searchTrigger = new KeyListener() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_F
						&& e.getModifiers() == KeyEvent.CTRL_MASK
						|| e.getKeyCode() == KeyEvent.VK_SLASH) {
					if (toolbar.isVisible()) {
						toolbar.setVisible(false);
					} else {
						toolbar.setVisible(true);
						filter.requestFocus();
					}
				}
			}

			public void keyReleased(KeyEvent e) {
			}

			public void keyTyped(KeyEvent e) {
			}
		};

		/*
		 * Escape : hide the search panel
		 */
		KeyListener escapeTrigger = new KeyListener() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					if (toolbar.isVisible()) {
						filter.setText("");
						toolbar.setVisible(false);
						MainFrame.this.requestFocus();
					}
				}
			}

			public void keyReleased(KeyEvent e) {
			}

			public void keyTyped(KeyEvent e) {
			}
		};

		filter.addKeyListener(escapeTrigger);

		addKeyListener(searchTrigger);
		ftodolist.addKeyListener(searchTrigger);
	}

	/*
	 * the menubar of sTodo, besides some hard-code here, sTodo allows
	 * user to define them Menubar, using the functon "_customizeMenuBar_"
	 * in scripts/menubar.js
	 */
	private void initMenuBar() {
		mbar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		fileMenu.setIcon(new ImageIcon("imgs/file.gif"));
		JMenuItem newTask = new JMenuItem("New task", new ImageIcon(
				"imgs/schedule_new.gif"));
		newTask.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				InputEvent.CTRL_MASK));
		newTask.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (newTaskDialog == null) {
					newTaskDialog = new NewTaskDialog(MainFrame.this,
							"New Task");
				}
				newTaskDialog.setLocationRelativeTo(null);
				newTaskDialog.setVisible(true);
			}
		});

		JMenuItem exit = new JMenuItem("Exit", new ImageIcon("imgs/Exit.png"));
		exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4,
				InputEvent.ALT_MASK));
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int y = JOptionPane.showConfirmDialog(null, "Confirm exit",
						"Confirm Exit Dialog", JOptionPane.YES_NO_OPTION);
				if (y == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}

		});

		JMenu export = new JMenu("Export...");
		export.setIcon(new ImageIcon("imgs/export.gif"));

		JMenuItem exportText = new JMenuItem("Export Text", new ImageIcon(
				"imgs/exptotext.gif"));
		JMenuItem exportExcel = new JMenuItem("Export Excel", new ImageIcon(
				"imgs/exptoexcel.gif"));
		JMenuItem exportHtml = new JMenuItem("Export HTML", new ImageIcon(
				"imgs/exptohtml.gif"));

		exportHtml.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List<TodoItem> list = new LinkedList<TodoItem>();
				for (int i = 0; i < ftodolist.getContents().getSize(); i++) {
					list
							.add((TodoItem) ftodolist.getContents()
									.getElementAt(i));
				}
				Exporter exporter = new HTMLExporter(list, "temp.html");
				exporter.store();
			}
		});

		export.add(exportText);
		export.add(exportExcel);
		export.add(exportHtml);

		fileMenu.add(newTask);
		fileMenu.add(export);
		fileMenu.add(exit);

		JMenu editMenu = new JMenu("Edit");
		editMenu.setIcon(new ImageIcon("imgs/edit.gif"));

		JMenuItem settings = new JMenuItem("Preference", new ImageIcon(
				"imgs/customize.gif"));
		settings.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
				InputEvent.CTRL_MASK));

		settings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (preferenceDialog == null) {
					preferenceDialog = new PreferenceDialog(MainFrame.this,
							"Preference");
				}
				preferenceDialog.setLocationRelativeTo(null);
				preferenceDialog.setVisible(true);
			}
		});

		editMenu.add(settings);
		// editMenu.addSeparator();

		JMenuItem search = new JMenuItem("Search", new ImageIcon(
				"imgs/filter.gif"));
		search.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F,
				InputEvent.CTRL_MASK));
		search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				toolbar.setVisible(true);
			}
		});

		editMenu.add(search, 0);

		mbar.add(fileMenu);
		mbar.add(editMenu);

		Plugin pMenuBar = TodoPluginManager.getInstance().getPlugin("menubar");
		pMenuBar.execute("_customizeMenuBar_", mbar);

		setJMenuBar(mbar);
	}

	/*
	 * pop-up menu on each item on the FilterableList
	 */
	private void initPopupMenu() {
		JMenuItem delMenuItem = new JMenuItem("Delete item", new ImageIcon(
				"imgs/delete.gif"));
		JMenuItem editMenuItem = new JMenuItem("Edit item", new ImageIcon(
				"imgs/edit2.gif"));
		JMenuItem mailMenuItem = new JMenuItem("Mail this item", new ImageIcon(
				"imgs/mail.gif"));
		JMenuItem helpMenuItem = new JMenuItem("Get help", new ImageIcon(
				"imgs/help.gif"));

		pmOnItem = new JPopupMenu("Edit menu");
		pmOnItem.addSeparator();
		pmOnItem.add(delMenuItem);
		pmOnItem.add(editMenuItem);
		pmOnItem.add(mailMenuItem);
		pmOnItem.addSeparator();
		pmOnItem.add(helpMenuItem);

		popupListener = new PopupListener(pmOnItem);
	}

	/*
	 * system tray of stodo, its now a clock-icon
	 */
	private void initSystemTray() {
		systemTray = SystemTray.getSystemTray();
		try {
			trayIcon = new TrayIcon(ImageIO.read(new File("imgs/icon.png")));
			systemTray.add(trayIcon);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (AWTException e) {
			e.printStackTrace();
		}
    }
    

	/**
	 * initialize the UI of sTodo, and bind event handlers then.
	 */
	public void initUI() {
		initSystemTray();
		initPopupMenu();
		initContentList();
		initMenuBar();
		initSearchBox();

		getContentPane().add(toolbar, BorderLayout.NORTH);
		getContentPane().add(scroller);
		setSize(400, 650);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setLocationRelativeTo(null);// center of the screen
		setFocusable(true);
		setVisible(true);
	}

	/**
	 * this is a call-back for edit dialog, when edit finished, just update the
	 * todo list data-model of main Frame
	 * 
	 * @param item
	 */
	/*
	 * public void updateList(TodoItem item){ FilterableListModel model =
	 * ftodolist.getContents();
	 * 
	 * for(int i = 0;i < model.getSize();i++){ TodoItem titem =
	 * (TodoItem)model.getElementAt(i); if(titem.getId().equals(item.getId())){
	 * model.removeElement(i); model.addElement(item); return; } }
	 * 
	 * model.addElement(item); }
	 */

	/**
	 * delete item from todo list and database, all changes we made must mapped
	 * to database and the data-model of the FilterableList
	 * 
	 * @param item
	 */
	public void deleteItem(TodoItem item) {
		FilterableListModel model = ftodolist.getContents();

		for (int i = 0; i < model.getSize(); i++) {
			TodoItem titem = (TodoItem) model.getElementAt(i);
			if (titem.getId().equals(item.getId())) {
				model.removeElement(i);
				break;
			}
		}

		// remove the item from database
		DataService ds = DataService.getInstance();
		boolean s = ds.removeItem(item);

		TaskService as = TaskService.getInstance();
		// if the deleted item has be scheduled, need cancel it.
		if (as.isItemScheduled(item)) {
			as.cancelSchedule(item);
		}

		if (s) {
			JOptionPane.showMessageDialog(null, "Task is deleted", "Success",
					JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(null, ds.getMessage(),
					"Something is wrong", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * refresh data-model of todo list
	 * 
	 * @param list
	 */
	public void refreshModel(List<TodoItem> list) {
		FilterableListModel model = ftodolist.getContents();
		model.clear();

		for (TodoItem item : list) {
			model.addElement(item);
		}
	}

}
