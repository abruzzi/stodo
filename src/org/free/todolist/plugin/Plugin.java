package org.free.todolist.plugin;

import org.graalvm.polyglot.Value;

/**
 *
 * @author juntao.qiu@gmail.com
 *
 */
public interface Plugin {
    /**
     * get the name of a plugin
     */
    String getName();
    
    /**
     * get a short description of a plugin
     */
    String getDescription();
    
    /**
     * get all available function names
     */
    java.util.List<String> getAvailableFunctions();

    void activate();

    /**
     * execute the function and return the result (Object)
     */
    Value execute(String function, Object... objects);
    
    /**
     * put value into context by {key:String, value:Object}
     */
    void putValueToContext(String key, Object obj);
}
