package org.free.todolist.plugin;

import javax.script.*;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TodoPlugin implements Plugin {
	private String name;
	private String desc;

	private Map<String, Object> context;

	private ScriptEngine sengine;
	private Invocable engine;

	private Bindings bindings;

	public TodoPlugin(String file, String name, String desc) {
		this.name = name;
		this.desc = desc;

		context = new HashMap<String, Object>();
		sengine = RuntimeEnv.getScriptEngine();
		engine = RuntimeEnv.getInvocableEngine();
		try {
			sengine.eval(new java.io.FileReader(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ScriptException e) {
			e.printStackTrace();
		}
	}

	public TodoPlugin(String file, ScriptContext context){
	    
	}
	
	public TodoPlugin(URL url) {

	}

	public Object execute(String function, Object... objects) {
		Object result = null;
		try {
			result = engine.invokeFunction(function, objects);
		} catch (ScriptException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		return result;
	}

	public List<String> getAvailiableFunctions() {
		return null;
	}

	public String getDescription() {
		return desc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String desc) {
		this.desc = desc;
	}

	/**
	 * put value to plug-in context, and then put it into engine context
	 */
	public void putValueToContext(String key, Object obj) {
		Bindings bindings = sengine.getBindings(ScriptContext.ENGINE_SCOPE);
		bindings.put(key, obj);
		context.put(key, obj);
		sengine.put(key, obj);
	}

}
