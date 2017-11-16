package com.lq.test;

import javax.mail.internet.MimeUtility;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;

public class CommonMail {
	public static void main(String[] args) {
		CommonMail mail = new CommonMail();
		mail.sendSimpleMail();
	}

	// 发送简单邮件
	public void sendSimpleMail() {
		try {
			Email email = new SimpleEmail();
			email.setHostName("smtp.qq.com"); // 发送服务器
			// email.setSmtpPort(465);
			email.setAuthenticator(new DefaultAuthenticator("nirvana172@qq.com", "ynavycjkecffddgi")); // 发送邮件的用户名和密码
			email.setSSLOnConnect(true);
			email.setFrom("nirvana172@qq.com"); // 发送邮箱
			email.setSubject("test");// 主题
			email.setMsg("content..."); // 内容
			// email.setCharset("GBK"); // 必须放在前面，否则乱码
			email.addTo("172446628@qq.com"); // 接收邮箱

			email.send();
		} catch (EmailException ex) {
			ex.printStackTrace();
		}
	}

	// 发送带附件的邮件
	public void sendMutiMail() throws Exception {
		EmailAttachment attachment = new EmailAttachment();
		attachment.setDisposition(EmailAttachment.ATTACHMENT);
		attachment.setDescription("python resource");
		attachment.setPath("src/com/beckham/common/email/附件.txt");
		attachment.setName(MimeUtility.encodeText("附件.txt")); // 设置附件的中文编码

		MultiPartEmail email = new MultiPartEmail();
		email.setHostName("smtp.163.com"); // 发送服务器
		email.setAuthentication("gaowm0207@163.com", "password"); // 发送邮件的用户名和密码
		email.addTo("459978392@qq.com", "a"); // 接收邮箱
		email.setFrom("gaowm0207@163.com", "a"); // 发送邮箱
		email.setSubject("测试主题");// 主题
		email.setMsg("这里是邮件内容"); // 内容
		email.setCharset("GBK"); // 编码
		// 添加附件
		email.attach(attachment);

		// 发送邮件
		email.send();

	}
}
