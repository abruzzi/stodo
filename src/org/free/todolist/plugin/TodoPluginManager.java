package org.free.todolist.plugin;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author juntao.qiu@gmail.com
 *
 */
public class TodoPluginManager implements PluginManager {
	private List<Plugin> plist;
	private static TodoPluginManager instance;

	private final Context polyglotContext;

	public static TodoPluginManager getInstance() {
		if (instance == null) {
			instance = new TodoPluginManager();
		}
		return instance;
	}

	private TodoPluginManager() {
		plist = new ArrayList<Plugin>(1);
		this.polyglotContext = Context.newBuilder("js")
				.allowAllAccess(true)
				.allowHostAccess(HostAccess.ALL)
				.allowHostClassLookup(s -> true)
				.option("js.nashorn-compat", "true")
				.option("js.foreign-object-prototype", "true")
				.option("js.java-package-globals", "true")
				.build();
	}

	public Context getContext() {
		return polyglotContext;
	}

	public void activate(Plugin plugin) {

	}

	public void deactivate(Plugin plugin) {

	}

	public Plugin getPlugin(String name) {
		for (Plugin p : plist) {
			if (p.getName().equals(name)) {
				return p;
			}
		}
		return null;
	}

	public void install(Plugin plugin) {
		plist.add(plugin);
		plugin.activate();
	}

	public List<Plugin> listPlugins() {
		return plist;
	}

	public void removePlugin(String name) {
		for (int i = 0; i < plist.size(); i++) {
			plist.get(i).getName().equals(name);
			plist.remove(i);
			break;
		}
	}

	public void uninstall(Plugin plugin) {
		plist.remove(plugin);
	}

	public int getPluginNumber() {
		return plist.size();
	}

}
