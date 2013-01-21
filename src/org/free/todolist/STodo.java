package org.free.todolist;

import java.util.List;

import javax.script.ScriptContext;
import javax.swing.SwingUtilities;

import org.free.todolist.plugin.Plugin;
import org.free.todolist.plugin.PluginManager;
import org.free.todolist.plugin.TodoPlugin;
import org.free.todolist.plugin.TodoPluginManager;
import org.free.todolist.ui.MainFrame;

/**
 * the main entry of sTodo
 * 
 * @author juntao.qiu@gmail.com
 *
 */
public class STodo {
	private MainFrame mainFrame;
	
	public STodo(MainFrame frame){
		this.mainFrame = frame;
	}

	public void initEnv(){
		PluginManager pManager = TodoPluginManager.getInstance();
		
		Plugin system = 
			new TodoPlugin("scripts/system.js", "system", "system initialize");		
		pManager.install(system);
		
		ScriptContext context = initContext();
	}
	
	private static String parseScriptName(String name){
		String scriptName;
		int slash = name.lastIndexOf("/");
		if(slash < 0){
			scriptName = name.substring(0);
		}else{
			scriptName = name.substring(slash+1);
		}
		
		int dot = scriptName.lastIndexOf(".");
		if(dot >= 0){
			scriptName = scriptName.substring(0, dot);
		}
		
		return scriptName;
	}
	
	public ScriptContext initContext(){
	    return null;
	}
	
	public void activePlugin(String scriptFile){
		Plugin newPlugin = new TodoPlugin(scriptFile, 
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
		STodo sTodo = new STodo(new MainFrame("My todo list"));
		sTodo.initEnv();
		
		Plugin system = TodoPluginManager.getInstance().getPlugin("system");
		system.putValueToContext("Application", sTodo);
		system.putValueToContext("DataModel", new DataModel());
		system.putValueToContext("Util", new Util());
		
		system.execute("main", new Object());
	}
}
