package framework.email;


import java.io.*;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;

import framework.util.IOUtils;
import sun.net.smtp.SmtpClient;


public class EmailSender
{
    private static final String defaultFrom = "Octopus Automation <chandra@valsatech.com>";
    private static final String mailhost = "smtp.gmail.com";
    
    
    
    /**
     * <B>SendMail</B>
     * <P>
     *      This will send a basic mail using the outbound.cisco.com smtp
     *      server and pass in the parameters given.  If from is null
     *      nas-automation mailing alias will be used.
     *      
     * @param to
     * @param from
     * @param subject
     * @param body
     * 
     * @throws IOException
     */
    public static void sendMail(String to, String from, String subject, String body)
        throws IOException
    {
        if (from == null)
            from = defaultFrom;
        
        SmtpClient smtp = new SmtpClient(mailhost);
        smtp.from(from);
        smtp.to(to);
        PrintStream msg = smtp.startMessage();

        msg.println("To: "+to);  // so mailers will display the To: address
        msg.println("Subject: "+subject);
        msg.println();
        msg.println(body);
        msg.println();

        smtp.closeServer();
    }

    /**
     * <B>SendHtmlMail</B>
     * <P>
     *      This will send a basic mail using the outbound.cisco.com smtp
     *      server and pass in the parameters given.  If from is null
     *      nas-automation mailing alias will be used.
     *      
     * @param to
     * @param from
     * @param subject
     * @param body
     * @throws IOException 
     * 
     * 
     */
    public static void sendHtmlMail(String to, String from, String subject, String body)
        throws MessagingException, IOException
    {
        Properties props = System.getProperties();
        // XXX - could use Session.getTransport() and Transport.connect()
        // XXX - assume we're using SMTP
        if (mailhost != null)
            props.put("mail.smtp.host", mailhost);

        // Get a Session object
        Session session = Session.getDefaultInstance(props, null);
        // construct the message
        Message msg = new MimeMessage(session);
        if (from == null)
            from = defaultFrom;
        
        msg.setFrom(new InternetAddress(from));

        msg.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(to, false));

        msg.setSubject(subject);
        msg.setDataHandler(new DataHandler(
                new ByteArrayDataSource(body, "text/html")));

        msg.setHeader("X-Mailer", "sendhtml");
        
        // send the thing off
        Transport.send(msg);
    }
    

    /**
     * <B>SendHtmlMail</B>
     * <P>Send HTML formatted mail.  The body of the message is a FileInputStream.
     * @param to
     * @param subject
     * @param fin
     * @throws IOException
     */
    public static void sendHtmlMail(String to, String from, String subject, InputStream is)
        throws MessagingException, IOException
    {
        String body = formatData(subject, is);
        
        sendHtmlMail(to,from,subject,body);
    }

    
    /**
     * <B>SendHtmlMail</B>
     * <P>
     *      This will send a basic mail using the outbound.cisco.com smtp
     *      server and pass in the parameters given.  If from is null
     *      nas-automation mailing alias will be used.
     *      
     * @param to
     * @param from
     * @param subject
     * @param body
     * @throws IOException 
     * 
     * 
     */
    public static void sendHtmlMail(String to, String cc, String bcc, String from, String subject, String body)
        throws MessagingException, IOException
    {
        Properties props = System.getProperties();
        // XXX - could use Session.getTransport() and Transport.connect()
        // XXX - assume we're using SMTP
        if (mailhost != null)
            props.put("mail.smtp.host", mailhost);

        // Get a Session object
        Session session = Session.getDefaultInstance(props, null);
        // construct the message
        Message msg = new MimeMessage(session);
        if (from == null)
            from = defaultFrom;
        msg.setFrom(new InternetAddress(from));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
        if (cc != null)
        	msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc, false));
        if (bcc != null)
        	msg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(bcc, false));
        
        msg.setSubject(subject);
        msg.setDataHandler(new DataHandler(
                new ByteArrayDataSource(body, "text/html")));

        msg.setHeader("X-Mailer", "sendhtml");
        
        // send the thing off
        Transport.send(msg);
    }


    /**
     * <B>collect</B>
     * <P>
     * Private method that wraps the text message in HTML formatted text.
     * @param in
     * @param msg
     * @throws MessagingException
     * @throws IOException
     */
    private static String formatData(String title, InputStream is)
        throws IOException 
    {
        StringBuffer sb = new StringBuffer(1024+is.available());
        sb.append("<HTML>\n");
        sb.append("<HEAD>\n");
        sb.append("<TITLE>\n");
        sb.append(title + "\n");
        sb.append("</TITLE>\n");
        sb.append("</HEAD>\n");

        sb.append("<BODY>\n");
        sb.append("<H1>" + title + "</H1>" + "\n");

        sb.append( new String(IOUtils.getStreamAsByteArray(is)) );


        sb.append("</BODY>\n");
        sb.append("</HTML>\n");

        return sb.toString();
    }
    
    
    public static void main(String[] args) throws Exception
    {
    	String from = "octopus-auto@octopus.com";
    	String to = "chandra@valsatech.com";
    	String cc ="chandra@valsatech.com";
    	String bcc = "chandra@valsatech.com";

    	EmailSender.sendHtmlMail(to, cc, bcc, from, "Testing", "This is a test");
	}

}

