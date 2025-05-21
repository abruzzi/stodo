package org.free.todolist;

import org.free.todolist.plugin.Plugin;
import org.free.todolist.plugin.PluginManager;
import org.free.todolist.plugin.TodoPlugin;
import org.free.todolist.plugin.TodoPluginManager;
import org.free.todolist.ui.MainFrame;

import javax.swing.*;
import java.util.List;

/**
 * the main entry of sTodo
 * 
 * @author juntao.qiu@gmail.com
 *
 */
public class STodo {
	private final MainFrame mainFrame;
	
	public STodo(MainFrame frame){
		this.mainFrame = frame;
	}

	public void initEnv(){
		PluginManager pManager = TodoPluginManager.getInstance();

		Plugin system = 
			new TodoPlugin(pManager.getContext(), "scripts/system.js", "system", "system initialize");
		pManager.install(system);
    }
	
	private static String parseScriptName(String name){
		String scriptName;
		int slash = name.lastIndexOf("/");
		if(slash < 0){
			scriptName = name;
		}else{
			scriptName = name.substring(slash+1);
		}
		
		int dot = scriptName.lastIndexOf(".");
		if(dot >= 0){
			scriptName = scriptName.substring(0, dot);
		}
		
		return scriptName;
	}
	
	public void activePlugin(String scriptFile){
		PluginManager pManager = TodoPluginManager.getInstance();

		Plugin newPlugin = new TodoPlugin(pManager.getContext(), scriptFile,
				parseScriptName(scriptFile), 
				parseScriptName(scriptFile));

		TodoPluginManager.getInstance().install(newPlugin);
	}
	
	public List<Plugin> getPluginList(){
		return TodoPluginManager.getInstance().listPlugins();
	}
	
	public MainFrame getUI(){
		return mainFrame;
	}
	
	public void launch(){
		SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				mainFrame.initUI();
			}
		});
	}
	
	public static void main(String[] args){
		System.out.println("java.vm.name = " + System.getProperty("java.vm.name"));
		System.out.println("java.home = " + System.getProperty("java.home"));

		STodo sTodo = new STodo(new MainFrame("My todo list"));
		sTodo.initEnv();
		
		Plugin system = TodoPluginManager.getInstance().getPlugin("system");

		system.putValueToContext("Application", sTodo);
		system.activate();
		
		system.execute("main", new Object());
	}
}
