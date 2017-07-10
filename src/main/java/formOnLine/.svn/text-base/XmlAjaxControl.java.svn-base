package formOnLine;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.triangle.lightfw.AbstractServlet;
import com.triangle.lightfw.SessionInfos;
import com.triangle.lightfw.ValueBeanList;

import formOnLine.actions.PropositionAction;
import formOnLine.actions.QuestionnaireAction;
import formOnLine.actions.ReponseAction;
import formOnLine.actions.SubmitFormAction;
import formOnLine.msBeans.Captcha;
import formOnLine.msBeans.Proposition;
import formOnLine.msBeans.Questionnaire;
import formOnLine.msBeans.Reponse;
import formOnLine.msBeans.SubmitForm;
import formOnLine.msBeans.UserData;





/**
 * @version   1.0
 * @author
 */
public class XmlAjaxControl extends HttpServlet implements Servlet {
    
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String START_XML = "<?xml version=\"1.0\"  encoding=\"UTF-8\"?><message>" ;
    private static final String END_XML = "</message>";
    private static final String ERROR_MSG = "Erreur appel xml";
    
    
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException {
        
        String msg = ERROR_MSG;
        
        UserData user = (UserData)req.getSession().getAttribute(ServControl.USERDATA);
        
        
        // parse paramètres
        String action = (String)req.getParameter("action");
        
        if (action != null && action.equals("TEST_SIRET")) {
            String value = (String)req.getParameter("value");
            
            msg = Controls.checkSiret(value,true);

            
            
        }
        
        if (action != null && action.equals("TEST_RIB")) {
            String value = (String)req.getParameter("value");
            
            msg = Controls.checkRIB(value,true);

            
            
        }
        
        if (action != null && action.equals("TEST_UNIQUE")) {
            String input = (String)req.getParameter("input");
            String val = (String)req.getParameter("value");
            
            int sid= -1;
            try { 
                sid = Integer.parseInt(req.getParameter("s_id"));
            } catch (Exception e) {}
            
            int pid = -1;
            if (input !=null)
                pid = BasicTools.getIntFrom5DigitFormatedInputName(input,"p");
            
            try {
                if ((new ReponseAction()).existeReponse(sid, pid, val)) {
                    msg = "Erreur : Cette valeur existe déjà dans la base de données !";
                } else {
                    msg = "";
                }
            } catch (Exception e) {}

            
            
        }    
        
        // récupération d'une data du parent
        if (action != null && action.equals("PARENT_DATA") && user.isConnected()) {
            
            
            int sid= -1;
            try { 
                sid = Integer.parseInt(req.getParameter("s_id"));
            } catch (Exception e) {}
            
            int pid = -1;
            try { 
                pid = Integer.parseInt(req.getParameter("p_id"));
            } catch (Exception e) {}
            
            SubmitForm sf = user.getRep(sid);
            
            try {
                Reponse r = null;
                if (sf != null)
                    r = new ReponseAction().selectOne(pid, sf.getS_id_parent());
                
                if (r !=null && r.getSVal() != null) {
                    msg = r.getSVal();
                } else {
                    msg = "";
                }
            } catch (Exception e) {}

            
            
        } 
        
        // gestion de la mise à jour du captcha
        if (action != null && action.equals("CAPTCHA")) {
            Captcha c = new Captcha();
            req.getSession().setAttribute(Captcha.NAME, c);
            
            msg = c.getValue();
            
            
        }
        
        
        // gestion récupération liste de formulaires avec filtre par p_id
        if (action != null && action.equals("LIST_FORM")  ) {
            
            
            
            int fid= -1;
            int pid= -1;
            String val = "";
            int pid2= -1;
            String val2 = "";
            int sid =-1 ;
            int lockedOnly = -1;
            
            try { 
                if (req.getParameter("f_id")!=null) fid = Integer.parseInt(req.getParameter("f_id"));
                if (req.getParameter("p_id")!=null) pid = Integer.parseInt(req.getParameter("p_id"));
                if (req.getParameter("s_id")!=null) sid = Integer.parseInt(req.getParameter("s_id"));
                if (req.getParameter("val")!=null) val = req.getParameter("val");
                if (req.getParameter("p_id2")!=null) pid2 = Integer.parseInt(req.getParameter("p_id2"));
                if (req.getParameter("val2")!=null) val2 = req.getParameter("val2");
                if (req.getParameter("lockedOnly")!=null) lockedOnly = (req.getParameter("lockedOnly").equals("true")?1:-1);
                
                
                if (fid<0) throw new Exception("Le FID doit être renseigné");
                
                QuestionnaireAction qa = new QuestionnaireAction();
                Questionnaire f = qa.selectById(fid);
                
                // contrôle droits
                if (f.getF_connected()>0  && !user.isConnected()) 
                    throw new Exception("Formulaire accessible en mode connecté");
                
                if (f.getF_connected() > 1  &&  user.getRole() < f.getF_connected() ) 
                    throw new Exception("Droits insuffisants");
                
                
                SubmitFormAction sfa = new SubmitFormAction();
                ValueBeanList listsf = null;
                
                if (sid>0) {
                    // sélection des forms sur une proposition de type "LIST_FORM"
                    listsf =  sfa.selectAllRefairsBySidAndPidAndVal(sid, pid, val, false);
                    
                } else if (pid>0) {
                    // selection de tous les forms de type FID avec un filtre sur une valeur de prop 
                    listsf =  sfa.selectAllByPropIdAndVal(pid, val, pid2, val2, lockedOnly, " order by R.r_text " ) ;
                } else {
                    // selection de tous les forms de type FID
                    listsf =  sfa.selectAllByFormId(fid, -1, lockedOnly, null, null, " order by reponse.r_text") ;
                }
                
                
                msg="";
                

                for (int i=0; listsf!=null && i<listsf.size(); i++  ) {
                    SubmitForm sf = (SubmitForm)listsf.get(i);
                    sfa.getInitVals(sf);
                    
                    msg += sf.toXML(); 
                    
                }
                
            } catch (Exception e) { 
                msg = ERROR_MSG + " : " + e.getMessage();
            } 
            
        }
        
        // gestion récupération liste de propositions
        if (action != null && action.equals("LIST_PROP")) {
            
            // contrôle des droits
            // ...
            
            int qid = -1;
            
            try { 
                qid = Integer.parseInt(req.getParameter("q_id"));
                
                if (qid>0) {
                    PropositionAction pa = new PropositionAction();
                    ValueBeanList listp =  pa.selectAllByQuestId(qid);
                    msg="";
                    for (int i=0; listp!=null && i<listp.size(); i++  ) {
                        Proposition p = (Proposition)listp.get(i);
                        
                        msg += p.toXML(); 
                        
                    }
                }
                
            } catch (Exception e) { 
                msg = ERROR_MSG;
            } 
            
        }
        
        
        // renvoi flux xml
        resp.setContentType("application/xml charset=UTF-8");
        resp.addHeader("Content-Disposition", "attachment;filename=message.xml;");
        
        try {
            OutputStream out = resp.getOutputStream();
            PrintStream fichier = new PrintStream(out);
            fichier.print( START_XML );
            
            if (msg != null && !action.equals("LIST_PROP") && !action.equals("LIST_FORM")) 
                fichier.print( BasicTools.xmlEncode(msg) );
            if (msg != null && (action.equals("LIST_PROP") || action.equals("LIST_FORM"))) 
                fichier.print( msg );
            

            fichier.print( END_XML );
            out.flush();
            
        } catch (Exception e) {
            InitServlet.traceLog.error("Erreur lors de l'exportation XML : " + e);
        }
        
        
        
    }
    
    
    
    
}
