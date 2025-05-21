package org.free.todolist.plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.graalvm.polyglot.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class TodoPlugin implements Plugin {
	private final String name;
	private final String desc;

	private final Map<String, Object> context;

	private final String scriptPath;
	private final Context polyglotContext;

	public TodoPlugin(Context context, String filePath, String name, String desc) {
		this.polyglotContext = context;
		this.name = name;
		this.desc = desc;
		this.scriptPath = filePath;
		this.context = new HashMap<>();
	}

	@Override
	public void activate() {
		try (FileReader reader = new FileReader(new File(scriptPath))) {
			polyglotContext.eval(Source.newBuilder("js", reader, scriptPath).build());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Value execute(String functionName, Object... args) {
		try {
			Value jsFunction = polyglotContext.getBindings("js").getMember(functionName);
			if (jsFunction != null && jsFunction.canExecute()) {
				return jsFunction.execute(args);
			} else {
				System.err.println("Function not found or not executable: " + functionName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public List<String> getAvailableFunctions() {
		// Optional: you can extract available JS function names if needed
		return Collections.emptyList();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return desc;
	}

	public void putValueToContext(String key, Object obj) {
		context.put(key, obj);
		polyglotContext.getBindings("js").putMember(key, obj);
	}
}
