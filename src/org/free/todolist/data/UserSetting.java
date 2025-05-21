package org.free.todolist.data;

import org.free.todolist.model.Preference;

import java.io.*;
import java.util.Properties;

public class UserSetting {
	private final Preference preference;
	private final Properties prop;

	/**
	 * load default settings from preference.properties
	 */
	public UserSetting(){
		preference = new Preference();
		prop = new Properties();
		try {
			prop.load(new FileInputStream(new File("preference.properties")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		initPreference();
	}
	
	private void initPreference() {
		preference.setProxyAutoDetect(
				Boolean.parseBoolean(prop.getProperty("proxy.autodetect", "true")));
		preference.setProxyHost(prop.getProperty("proxy.host", ""));
		preference.setProxyPort(prop.getProperty("proxy.port", ""));
		
		preference.setExportPath(prop.getProperty("export.path", ""));
		
		preference.setUseSSL(
				Boolean.parseBoolean(prop.getProperty("useSSL", "true")));
		preference.setMailHost(prop.getProperty("mail.host", ""));
		preference.setMailPort(prop.getProperty("mail.port", ""));
		preference.setUsername(prop.getProperty("username", "abruzzi"));
		preference.setPassword(prop.getProperty("password", ""));
	}

	/**
	 * return the preference defined in a properties file
     */
	public Preference getPreference(){
		return preference;
	}
	
	
	/**
	 * store user edited preference into the properties file again.
     */
	public void savePreference(Preference preference){
		prop.setProperty(
				"proxy.autodetect", String.valueOf(preference.isProxyAutoDetect()));
		prop.setProperty("proxy.host", preference.getProxyHost());
		prop.setProperty("proxy.port", preference.getProxyPort());
		
		prop.setProperty("export.path", preference.getExportPath());
		
		prop.setProperty("useSSL", String.valueOf(preference.isUseSSL()));
		prop.setProperty("mail.host", preference.getMailHost());
		prop.setProperty("mail.port", preference.getMailPort());
		prop.setProperty("username", preference.getUsername());
		prop.setProperty("password", preference.getPassword());
		
		try {
			prop.store(new FileOutputStream(new File("preference.properties")), "");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
