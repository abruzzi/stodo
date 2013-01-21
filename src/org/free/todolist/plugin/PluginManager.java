package org.free.todolist.plugin;

/**
 * 
 * @author juntao.qiu@gmail.com
 *
 */
public interface PluginManager {
    /**
     * activate a plug-in
     * @param plugin
     */
    public void activate(Plugin plugin);
    
    /**
     * de-activate a plug-in
     * @param plugin
     */
    public void deactivate(Plugin plugin);
    
    /**
     * install a plug-in to system
     * @param plugin
     */
    public void install(Plugin plugin);
    
    /**
     * delete a plug-in to system
     * @param plugin
     */
    public void uninstall(Plugin plugin);
    
    /**
     * list all of plug-ins out
     * @return
     */
    public java.util.List<Plugin> listPlugins();
    
    /**
     * get plug-in by given name
     * @param name
     * @return
     */
    public Plugin getPlugin(String name);
    
    /**
     * remove plug-in by given name
     * @param name
     */
    public void removePlugin(String name);
    
    /**
     * get number of plug-ins current in using
     * @return
     */
    public int getPluginNumber();
}
