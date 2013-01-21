package org.free.todolist.mail;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.free.todolist.data.SimpleTextMail;
import org.free.todolist.data.UserSetting;
import org.free.todolist.model.Preference;

/**
 * 
 * @author juntao.qiu@gmail.com
 *
 */
public class MailSender {
	private UserSetting setting;
	private SimpleTextMail mail;
	
	public MailSender(SimpleTextMail mail){
		setting = new UserSetting();
		this.mail = mail;
	}
	
	public boolean send(){
		boolean status;
		HtmlEmail email = new HtmlEmail();
		Preference preference = setting.getPreference();
		
		email.setHostName(preference.getMailHost());
		email.setSSL(preference.isUseSSL());
		email.setSslSmtpPort(preference.getMailPort());
		email.setAuthentication(preference.getUsername(), preference.getPassword());
		
		try {
			String[] tos = mail.getSendTo().split(";");
			for(String to : tos){
				email.addTo(to, to);
			}
			String[] ccs = mail.getCcTo().split(";");
			for(String cc : ccs){
				email.addCc(cc, cc.trim());
			}
			email.setFrom(preference.getUsername());
			email.setSubject(mail.getSubject());
			email.setHtmlMsg(mail.getContent());
			email.setCharset("UTF-8");
			
			email.send();
			status = true;
		} catch (EmailException e) {
			e.printStackTrace();
			status = false;
		}
		
		return status;
	}
}
