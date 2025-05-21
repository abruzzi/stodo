package org.free.todolist.plugin;

import org.graalvm.polyglot.Context;

/**
 * 
 * @author juntao.qiu@gmail.com
 *
 */
public interface PluginManager {
    /**
     * activate a plug-in
     */
    void activate(Plugin plugin);
    
    /**
     * de-activate a plug-in
     */
    void deactivate(Plugin plugin);
    
    /**
     * install a plug-in to system
     */
    void install(Plugin plugin);
    
    /**
     * delete a plug-in from the system
     */
    void uninstall(Plugin plugin);
    
    /**
     * list all the plug-ins out
     */
    java.util.List<Plugin> listPlugins();
    
    /**
     * get plug-in by given name
     */
    Plugin getPlugin(String name);

    /**
     * remove plug-in by given name
     */
    void removePlugin(String name);
    
    /**
     * get the number of plug-ins currently in using
     */
    int getPluginNumber();

    Context getContext();
}
