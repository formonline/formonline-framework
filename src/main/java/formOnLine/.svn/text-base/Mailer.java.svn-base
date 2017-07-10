/*
 * Created on 4 nov. 2005
 *
 */
package formOnLine;

import java.util.LinkedList;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;
import org.apache.log4j.Logger;

/**
 * @author seleridon
 *
 */
public class Mailer {
    /**
     * Envoie un mail en fonction des param�tres pass�s
     * @param from Adresse "de"
     * @param to Adresse(s) destinataire(s) (s�par�es par ";")
     * @param copy Adresse(s) destinataire(s) en copie 
     * @param subject Titre du mail
     * @param content Texte du mail
     */
    static protected Logger traceLog = Logger.getLogger("FOLE");
    
    public static String sendHtmlMail(String from, String to, String copy, 
            String subject, String htmContent, String textContent)
    {
        try
        {
            // Cr�ation du message
            
            HtmlEmail email=new HtmlEmail();
            email = (HtmlEmail)addLists(email,to,copy);
            
            email.setFrom(from,from);
            email.setSubject(subject);
            //String cid=email.embed(new URL(MailSettings.inlineImage),"UK Builder Logo");
            //email.setHtmlMsg("<H1>Hi! From HtmlMailCommons</H1><img src=\"cid:"+cid+"\">");
            email.setHtmlMsg(htmContent);
            email.setTextMsg(textContent);
            
            email.setCharset("UTF-8");
            email.setHostName(InitServlet.getMailServerAddress());
            
            
            // Envoie le email
            email.send();
            // trace (directive t�l�services ??)
            traceLog.info("# Envoi de mail : "+subject + 
                    "[from: "+ from + "]"+
                    "[to: " + to + "]"+
                    "[copy: " + copy + "]");
            return null;
            
            
        }
        catch (Exception e)
        {
            String err = "#### Problème d'envoi Mail ("+ to +") : " + e.getLocalizedMessage();
            traceLog.error(err);
            return err;
        }
    }
    
    public static String sendHtmlWithfiles(String from, String to, String copy, 
            String subject, String htmContent, String textContent, 
            String path1, String fileName1, String path2, String fileName2) {
        
        
        try
        {
            // Cr�ation du message
            
            HtmlEmail email = new HtmlEmail();
            //MultiPartEmail email=new MultiPartEmail();
            
            email = (HtmlEmail)addLists(email,to,copy);
            email.setFrom(from,from);
            email.setSubject(subject);
            email.setCharset("UTF-8");
            email.setHostName(InitServlet.getMailServerAddress());
            email.setHtmlMsg(htmContent);
            email.setTextMsg(textContent);
            
            //String cid=email.embed(new URL(MailSettings.inlineImage),"UK Builder Logo");
            //email.setHtmlMsg("<H1>Hi! From HtmlMailCommons</H1><img src=\"cid:"+cid+"\">");
            
            if (!Controls.isBlank(fileName1)) {
                EmailAttachment eat = new EmailAttachment();
                eat.setPath(path1+fileName1);
                eat.setDisposition(EmailAttachment.ATTACHMENT);
                eat.setDescription(fileName1);
                eat.setName(fileName1);
                email = (HtmlEmail)email.attach(eat);
            }
            
            if (!Controls.isBlank(fileName2)) {
                EmailAttachment eat = new EmailAttachment();
                eat.setPath(path2+fileName2);
                eat.setDisposition(EmailAttachment.ATTACHMENT);
                eat.setDescription(fileName2);
                eat.setName(fileName2);
                email = (HtmlEmail)email.attach(eat);
            }
            
            // Envoie le email
            email.send();
            // trace (directive t�l�services ??)
            traceLog.info("# Envoi de mail : "+subject + 
                    "[from: "+ from + "]"+
                    "[to: " + to + "]"+
                    "[copy: " + copy + "]");
            return null;
            
            
        }
        catch (Exception e)
        {
            String err = "#### Probl�me d'envoi Mail >("+ to +"): " + e;
            traceLog.error(err);
            return err;
        }
        
        
        /*
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.addRecipient(Message.RecipientType.TO,
        new InternetAddress(to));
        message.setSubject("Pi�ce jointe");
        */

    }
    
    private static Email addLists(Email email, String to, String copy) {
        
        LinkedList<String> vec = new LinkedList<String>();
        try
        {
            // ajout des adresses (s�parateurs ; ou ,)
            if (to.contains(",")) {
                vec = BasicTools.parseLineToList(to,",");
            } else {
                vec = BasicTools.parseLineToList(to,";");
            }
            
            for (int i=0; i < vec.size(); i++) {
                if (!Controls.isBlank((String)vec.get(i))) {
                    String adr =(String)vec.get(i);
                    if (Controls.checkEmail(adr,true)==null) email.addTo(adr);
                }
            }
            
            // ajout des adresses en copie (s�parateurs ; ou ,)
            if (copy != null) {
                if (copy.contains(",")) {
                    vec = BasicTools.parseLineToList(copy,",");
                } else {
                    vec = BasicTools.parseLineToList(copy,";");
                }
                
                for (int i=0; i < vec.size(); i++) {
                    if (!Controls.isBlank((String)vec.get(i))) {
                        String adr =(String)vec.get(i);
                        if (Controls.checkEmail(adr,true)==null) email.addCc(adr);
                    }
                }
            }
        }
        catch (Exception e)
        {
            String err = "#### Probl�me d'envoi Mail >("+ to +"): " + e;
            traceLog.error(err);
            return null;
        }
        return email;
    }
    
    public static String sendMail(String from, String to, String copy, 
            String subject, String content)
    {
      try
      {
        // Cr�ation du message
        SimpleEmail email = new SimpleEmail();
        
        email.setCharset("UTF-8");
        email.setHostName(InitServlet.getMailServerAddress());
        email.setFrom(from,from);
        email.setSubject(subject);
        email.setMsg(content);
        
        LinkedList<String> vec = new LinkedList<String>();
        
        // ajout des adresses (s�parateurs ; ou ,)
        vec = BasicTools.parseLineToList(to,";");
        for (int i=0; i < vec.size(); i++) {
            if (!Controls.isBlank((String)vec.get(i))) {
                String adr =(String)vec.get(i);
                if (Controls.checkEmail(adr,true)==null) email.addTo(adr);
            }
        }
        
        // ajout des adresses en copie (s�parateurs ; ou ,)
        if (copy != null) {
            vec = BasicTools.parseLineToList(copy,";");
        
	        for (int i=0; i < vec.size(); i++) {
	            if (!Controls.isBlank((String)vec.get(i))) {
	                String adr =(String)vec.get(i);
	                if (Controls.checkEmail(adr,true)==null) email.addCc(adr);
	            }
	        }
        }
        
        // Envoie le email
        email.send();
        // trace (directive t�l�services ??)
        traceLog.info("# Envoi de mail : "+subject + 
                "[from: "+ from + "]"+
                "[to: " + to + "]"+
                "[copy: " + copy + "]");
        return null;
      }
      catch (Exception e)
      {
          String err = "#### Probl�me d'envoi Mail >("+ to +"): " + e;
          traceLog.error(err);
          return err;
      }

    }
    
    public static String sendMail(String from, String to, String subject, String content)
    { 
        return sendMail(from, to, null, subject, content);
    }
    
}
