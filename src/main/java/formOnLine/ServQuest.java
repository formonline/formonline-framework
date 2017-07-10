package formOnLine;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Enumeration;

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
import formOnLine.msBeans.Questionnaire;
import formOnLine.msBeans.SubmitForm;
import formOnLine.msBeans.UserData;

/**
 * @version   1.0
 * @author
 */
public class ServQuest extends HttpServlet implements Servlet {
    
    
    private static final long serialVersionUID = 1L;
    private static final String MSG_NOFORM = "Aucun formulaire trouvé";
    
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException {
        doPost(req,resp);
    }
    
    
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException {
        
        HttpSession session = req.getSession(true);
        //javax.servlet.ServletContext application = getServletContext();
        UserData userData = (UserData)session.getAttribute(ServControl.USERDATA);
        SessionInfos sessionInfos = (SessionInfos)session.getAttribute(AbstractServlet.ID_SESSION_INFOS);
        
        Questionnaire q = null;
        SubmitForm sf = null;
        String message = null;
        
        //recup IDs
        int sid = 0;
        if (req.getParameter("sid")!=null) sid = Integer.parseInt( req.getParameter("sid"));
        if (req.getParameter("s_id")!=null) sid = Integer.parseInt( req.getParameter("s_id"));
        
        int sid_parent = -1;
        if (req.getParameter("s_id_parent")!=null) sid_parent = Integer.parseInt( req.getParameter("s_id_parent"));

        
        int qid = 0;
        if (req.getParameter("qid")!=null) qid = Integer.parseInt( req.getParameter("qid"));
        if (req.getParameter("f_id")!=null) qid = Integer.parseInt( req.getParameter("f_id"));
        
        int sidToDuplicate = -1;
        if (req.getParameter("s_id_to_duplicate")!=null) sidToDuplicate = Integer.parseInt( req.getParameter("s_id_to_duplicate"));
        
        
        //reinit du Q courant de la session si différent
        if (session.getAttribute("q")!=null) {
            q = (Questionnaire)session.getAttribute("q");
            if (q.getId()!= qid || req.getParameter("f_id")!=null) q=null;
        }
        
        //reinit du SF courant de la session si différent
        if (session.getAttribute("sf")!=null) {
            sf = (SubmitForm)session.getAttribute("sf");
            if (sf.getS_id()!= sid) sf=null;
        }
        
        //recherche dans le userdata
        if (q==null && userData.getQuest(qid)!=null) q=userData.getQuest(qid);
        
        // si le questionnaire n'a pas été chargé avec ses questions, on l'ignore
        if (q!=null && q.getGroupes().size()==0 ) q= null;
        
        
        try {
            //init SubmitForm si présent dans URL 
            if (sf==null && sid>0) {
                SubmitFormAction sfa = new SubmitFormAction();
                sf = sfa.selectBySId(sid);
                if (sf!=null) userData.addOrReplaceRep(sf);
                
                if (sid>0 && sf==null) {
                    // le sid n'existe pas
                    message = MSG_NOFORM;
                    session.setAttribute(ServControl.MESSAGE,message);
                    return;
                }
                
                if (qid<0 && sf!=null) qid = sf.getF_id();
            }
            
            //init submitform à partir d'un submitform à dupliquer
            if (sf==null && sidToDuplicate >0) {
                SubmitFormAction sfa = new SubmitFormAction();
                SubmitForm sftd = sfa.selectBySId(sidToDuplicate);
                
                if (sftd != null) {
                    // deep copy du SF
                    sf = sftd.copy();
                    sf.setS_lock(0);
                    sf.setS_login_maj(userData.getLogin());
                    
                    if (qid<0) qid = sf.getF_id();
                }
            }
            
            
            // récup questionnaire complet
            if (q==null && qid>=0 )  {
                QuestionnaireAction qa = new QuestionnaireAction();
                q = qa.getFullQuest(qid,sf);
                
                userData.addOrReplaceQuest(q);
            }
            
            // réinit questionnaire 
            if  (q!=null ) { // && q.getS_id()!=sid) {
                q.resetFullQuest();
                if (sf!=null) q.setFullQuest(sf) ;
                userData.addOrReplaceQuest(q);
            }
            
            //init values si présentes dans URL
            Enumeration e = req.getParameterNames();
            while (e.hasMoreElements() 
                    && sessionInfos.getActionToDo()!=ServControl.QMAJ ) { //&& sf!=null
            
                String sParam = e.nextElement().toString();
                String sVal = req.getParameter(sParam);
                
                if (sParam.length()>5 && sParam.startsWith("initG")) {
                    //String sType = sParam.substring(4,7);
                    int gid = BasicTools.getIntFrom5DigitFormatedInputName(sParam,"G");
                    int quid = BasicTools.getIntFrom5DigitFormatedInputName(sParam,"Q");
                    int pid = BasicTools.getIntFrom5DigitFormatedInputName(sParam,"P");
                    
                    q.setQuestValById(gid,quid,pid,sVal);
                    
                }
            }
            
            //init s_id_parent
            q.setS_id_parent(sid_parent);

            
        } catch (SQLException e ) {
            message = e.getMessage(); //erreur SQL
            session.setAttribute(ServControl.MESSAGE,message);
        } 
        
        // init complémentaires
        if (Controls.isBlank(message) && sessionInfos.getActionToDo()==ServControl.QUEST) {
            try {
                message = new UserActionFactory().getUserAction("ContextUserAction").initBeforeNewSf(q, userData);
                session.setAttribute(ServControl.MESSAGE,message);
                
                
            } catch (Exception e ) {
                message = e.getMessage(); //erreur 
                session.setAttribute(ServControl.MESSAGE,message);
            } 
            
        }
        
        if (message != null) {
            session.setAttribute(ServControl.MESSAGE, message);
            session.removeAttribute("q");
            session.removeAttribute("sf");
            return;
        }
        
        
        /***********************
         * CONTROLE DES DROITS *
         ***********************/
        
        boolean access = false;
        
        message = q.checkAccess(sessionInfos.getActionToDo(), userData, sf, sid, true, true);
        if (message == null) access = true;
        
        
        if (access) {
            
            // accès autorisé
            
            // cas de la suppression
            if (sf!=null && sessionInfos.getActionToDo()==ServControl.RDEL) {
                SubmitFormAction sfa = new SubmitFormAction();
                try {
                    
                    // actions
                    if (Controls.isBlank(message)) message = new UserActionFactory().getUserAction("ContextUserAction").testBeforeDeleteSf(sf,userData);
                    
                    
                    //  mail
                    if (q.getT_id_mail_on_delete()>0 && sf!=null) {
                        
                        String adr = sf.getPropVal(q.getP_id_mail());
                        
                        if (q.getP_id_mail()>0 && Controls.isBlank(adr) && sf.getS_id_parent()>0) {
                            SubmitForm sfp = sfa.selectBySId(sf.getS_id_parent());
                            if (sfp!= null ) adr=sfp.getPropVal(q.getP_id_mail());
                        }
                        sfa.sendMailOnDelete(sf,q, adr, userData );
                    }
                    
                    
                    if (Controls.isBlank(message)) sfa.delete(sf);
                    
                    // actions
                    if (Controls.isBlank(message)) message = new UserActionFactory().getUserAction("ContextUserAction").doAfterDeleteSf(sf, userData);
                     
                    
                } catch (Exception e) {
                    session.setAttribute(ServControl.MESSAGE, "Une erreur est survenue lors de la suppression : "+e.getLocalizedMessage());
                }
                userData.removeRep(sf);
                sf=null;
            }
            
            
            // alimentation des beans de session pour l'affichage
            if (q==null) session.removeAttribute("q");
            if (sf==null) session.removeAttribute("sf");
            
            if (q!=null) session.setAttribute("q",q);
            if (sf!=null) session.setAttribute("sf",sf);
            
            
            
        } else {
            // accès refusé
            session.setAttribute(ServControl.MESSAGE, message);
            session.removeAttribute("q");
            session.removeAttribute("sf");
            
        }
        
        session.setAttribute(ServControl.USERDATA,userData);
        
        
        
        
    }
    
}
