package org.free.todolist.plugin;
import javax.script.*;

import java.io.*;
import java.util.*;

public abstract class ScriptCache {
    public static final String ENGINE_NAME = "JavaScript";
    private Compilable scriptEngine;
    private LinkedHashMap<String, CachedScript> cacheMap;

    public ScriptCache(final int maxCachedScripts) {
        ScriptEngineManager manager = new ScriptEngineManager();
        scriptEngine = (Compilable) manager.getEngineByName(ENGINE_NAME);
        cacheMap = new LinkedHashMap<String, CachedScript>(
                maxCachedScripts, 1, true) {
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return size() > maxCachedScripts;
            }
        };
    }

    public abstract File getScriptFile(String key);

    public synchronized CompiledScript getScript(String key)
            throws ScriptException, IOException {
        CachedScript script = cacheMap.get(key);
        if (script == null) {
            script = new CachedScript(scriptEngine, getScriptFile(key));
            cacheMap.put(key, script);
        }
        return script.getCompiledScript();
    }
    
    public ScriptEngine getEngine() {
        return (ScriptEngine) scriptEngine;
    }
    
}