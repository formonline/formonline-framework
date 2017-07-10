package formOnLine;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.triangle.lightfw.AbstractServlet;
import com.triangle.lightfw.SessionInfos;

import formOnLine.actions.QuestionnaireAction;
import formOnLine.actions.SubmitFormAction;
import formOnLine.actions.UserActionFactory;
import formOnLine.msBeans.Captcha;
import formOnLine.msBeans.Questionnaire;
import formOnLine.msBeans.SubmitForm;
import formOnLine.msBeans.UserData;

/**
 * @version 	1.0
 * @author
 */
public class ServMailPwd extends HttpServlet implements Servlet {
    
    private static final long serialVersionUID = 1L;
    private static String MSG_ERREUR = "Identifiant incorrect.";
    private static final String MSG_CAPTCHA = 
        "Les caractères saisis ne correspondent pas." ;
    
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException {
        doPost(req,resp);
    }
    
    
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException {
        
        HttpSession session = req.getSession(true);
        SubmitFormAction sfa = new SubmitFormAction();
        QuestionnaireAction qa = new QuestionnaireAction();
        
        String message= null; 
        
        int sid =-1;
        int action = -1;
        
        SessionInfos sessionInfos =  (SessionInfos)session.getAttribute( AbstractServlet.ID_SESSION_INFOS);
        action = sessionInfos.getActionToDo();
        UserData userData = (UserData)session.getAttribute(ServControl.USERDATA);
        
        SubmitForm sf = null;
        Questionnaire q = null;
        
        
        
        switch (action) { 
        
        case ServControl.MPWD: //  mail mot de passe
            
            if (Controls.isBlank(req.getParameter("login"))) {
                message = MSG_ERREUR;
                break;
            }
            // conctrole captcha
            String userCaptcha = req.getParameter("captcha");
            Captcha captcha = (Captcha) session.getAttribute(Captcha.NAME);
            if (userCaptcha != null ) {
                if (captcha == null || !captcha.isCorrect(userCaptcha)) {
                    message = MSG_CAPTCHA;
                    session.removeAttribute(Captcha.NAME);
                    break;
                }
            }
            if (captcha != null) session.removeAttribute(Captcha.NAME);
            
            
            /* old version
             * String sessionCaptcha = (String)session.getAttribute(SimpleCaptchaServlet   servlet.Constants.SIMPLE_CAPCHA_SESSION_KEY);
            String userCaptcha = req.getParameter("captcha");
            if (sessionCaptcha == null
                    || userCaptcha == null
                    || !sessionCaptcha.equals(userCaptcha)) {
                message = MSG_CAPTCHA;
                break;                
            } */
            
            
            try {
                String login =  req.getParameter("login");
                
                // recherche sur une proposition de login
                sf = sfa.selectByAuthProps(login, true);
                
                if (sf == null) {
                    // tentative sur le sid
                    try { 
                        sf = sfa.selectByAuthSid( Integer.parseInt(login), true);
                    } catch (Exception e) {}
                    
                    if (sf == null)  message = MSG_ERREUR;
                }
                
                if (message !=null) break;
                
                // contrôle règle de blocage potentielle : appel au userAction du contexte 
                message = new UserActionFactory().getUserAction("ContextUserAction").doBeforeSendPwd(sf,userData);
                
                if (message !=null) break;
                
                // récupération du questionnaire
                q = qa.selectById(sf.getF_id());
                                
                int mailPid = q.getP_id_mail();
                String adr = sf.getPropVal(mailPid);
                String appName = getServletContext().getServletContextName();
                String object = "Identifiants " ;
                if (appName!=null) object += appName;
                StringBuffer txt = new StringBuffer();
                
                txt.append("<strong>").append(object).append("</strong><br><br>");
                txt.append("Nom utilisateur : ").append(login).append("<br>");
                txt.append("Mot de passe : ").append(sf.getPwd()).append("<br>");
                
                message = Mailer.sendHtmlMail(InitServlet.getMailFromAddress(),adr,null, object, txt.toString(),txt.toString());
                
            } catch (Exception e) {
                message = MSG_ERREUR;
            }
            
            
            
            
            if (Controls.isBlank(message)) 
                message = "Identifiants envoyés";
            
            
            break;
            
        } //switch
        session.setAttribute(ServControl.MESSAGE,message);
    }
    
}
