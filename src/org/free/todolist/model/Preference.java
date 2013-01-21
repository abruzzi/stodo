package org.free.todolist.model;

/**
 * User preference, include network settings, mail account information
 * and other useful information. Those information stored in a single 
 * properties file in application folder.
 * <br>
 * Just a simple java-bean
 * <br>
 * @author juntao.qiu@gmail.com
 *
 */
public class Preference {
	//is proxy auto detect?
	private boolean isProxyAutoDetect;
	
	//proxy hostname or ip address
	private String proxyHost;
	
	//proxy port
	private String proxyPort;
	
	//where you want to export your tasks to?
	private String exportPath;
	
	//use ssl in mail server or not
	private boolean useSSL;
	
	//the email server address
	private String mailHost;
	
	//the email server port
	private String mailPort;
	
	//your account on the email server
	private String username;
	
	//password
	private String password;
	
	public boolean isProxyAutoDetect() {
		return isProxyAutoDetect;
	}
	public void setProxyAutoDetect(boolean isProxyAutoDetect) {
		this.isProxyAutoDetect = isProxyAutoDetect;
	}
	public String getProxyHost() {
		return proxyHost;
	}
	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}
	public String getProxyPort() {
		return proxyPort;
	}
	public void setProxyPort(String proxyPort) {
		this.proxyPort = proxyPort;
	}
	public String getExportPath() {
		return exportPath;
	}
	public void setExportPath(String exportPath) {
		this.exportPath = exportPath;
	}
	public boolean isUseSSL() {
		return useSSL;
	}
	public void setUseSSL(boolean useSSL) {
		this.useSSL = useSSL;
	}
	public String getMailHost() {
		return mailHost;
	}
	public void setMailHost(String mailHost) {
		this.mailHost = mailHost;
	}
	public String getMailPort() {
		return mailPort;
	}
	public void setMailPort(String mailPort) {
		this.mailPort = mailPort;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
}
