package pages;

import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;

public class Miscellaneous {

	private final String user;
	private final String pwd;
	private final String emailText;
	
	public Miscellaneous(String emailText) {
		this.user = "";
		this.pwd = "";
		this.emailText = emailText;
	}
	
	public String getEmailText() {
		return emailText;
	}

	public String getUser() {
		return user;
	}

	public String getPwd() {
		return pwd;
	}

	public void sendEmail() {
        Properties pro=new Properties();
        pro.put("mail.smtp.host", "smtp.gmail.com");
        pro.put("mail.smtp.starttls.enable","true");
        pro.put("mail.smtp.auth","true");
        pro.put("mail.smtp.port","587");
        Session ss=Session.getInstance(pro, new javax.mail.Authenticator()
                {
                   
                    protected PasswordAuthentication getPasswordAuthentication()
                    {
                        return new PasswordAuthentication(getUser(),getPwd() );
                 }
    });
        try
        {
            Message msg=new MimeMessage(ss);
            msg.setFrom(new InternetAddress(this.getUser()));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(this.getUser()));
            msg.setSubject("ESO Tamriel Price Found");
            
            BodyPart messageBodyPart = new MimeBodyPart();
            msg.setText(this.getEmailText());
            messageBodyPart.setText(this.getEmailText());
            Transport.send(msg);
            System.out.println("message sent");
        }
        catch(Exception e)
        {
           System.out.println(e.getMessage());
        }
	}
}
