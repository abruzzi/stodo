package org.free.todolist.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.free.todolist.model.Preference;

public class UserSetting {
	private Preference preference;
	private Properties prop;

	/**
	 * load default settings from preference.properties
	 */
	public UserSetting(){
		preference = new Preference();
		prop = new Properties();
		try {
			prop.load(new FileInputStream(new File("preference.properties")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		initPreference();
	}
	
	private void initPreference() {
		preference.setProxyAutoDetect(
				Boolean.valueOf(prop.getProperty("proxy.autodetect", "true")));
		preference.setProxyHost(prop.getProperty("proxy.host", ""));
		preference.setProxyPort(prop.getProperty("proxy.port", ""));
		
		preference.setExportPath(prop.getProperty("export.path", ""));
		
		preference.setUseSSL(
				Boolean.valueOf(prop.getProperty("useSSL", "true")));
		preference.setMailHost(prop.getProperty("mail.host", ""));
		preference.setMailPort(prop.getProperty("mail.port", ""));
		preference.setUsername(prop.getProperty("username", "abruzzi"));
		preference.setPassword(prop.getProperty("password", ""));
	}

	/**
	 * return the preference defined in properties file
	 * @return
	 */
	public Preference getPreference(){
		return preference;
	}
	
	
	/**
	 * store user edited preference into the properties file again.
	 * @param preference
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
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
