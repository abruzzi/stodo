package org.free.todolist.plugin;

/**
 *
 * @author juntao.qiu@gmail.com
 *
 */
public interface Plugin {
    /**
     * get the name of a plug-in
     * @return
     */
    public String getName();
    
    /**
     * get short description of a plug-in
     * @return
     */
    public String getDescription();
    
    /**
     * get all available function names 
     * @return
     */
    public java.util.List<String> getAvailiableFunctions();
    
    /**
     * execute the function and return the result (Object)
     * @param function
     * @return
     */
    public Object execute(String function, Object...objects);
    
    /**
     * put value into context by {key:String, value:Object}
     * @param key
     * @param obj
     */
    public void putValueToContext(String key, Object obj);
}
