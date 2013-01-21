package org.free.todolist.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author dong.hu@china.jinfonet.com
 */
public class PropLoader {

    public static Properties getProperties(String file){
                
        Class o = getCallerClass(2);
        
        InputStream ins = o.getResourceAsStream(file);
        
        Properties props = new Properties();
        try {
            props.load(ins);
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try{ins.close();}catch(Exception e){}
        }
        
        return props;
    }

    public static Class getCallerClass(int level){
        
        StackTraceElement[] stack = 
            (new Throwable()).getStackTrace();
        
        Class o = null;
        try {
            o = Class.forName(stack[level].getClassName());
        } catch (ClassNotFoundException e1) {
            // Never run here
        }
        
        return o;
    }
}
