package org.free.todolist.plugin;

import javax.script.*;

public class RuntimeEnv {
	private static ScriptEngineManager manager;
	private static ScriptEngine engine;

	static {
		manager = new ScriptEngineManager();
		engine = manager.getEngineByName("graal.js");

		Bindings bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);
		bindings.put("polyglot.js.allowAllAccess", true);
	}

	public static ScriptEngine getScriptEngine() {
		return engine;
	}

	public static Invocable getInvocableEngine() {
		return (Invocable) engine;
	}
}
