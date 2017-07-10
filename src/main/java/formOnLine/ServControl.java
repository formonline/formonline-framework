package formOnLine;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import com.triangle.lightfw.AbstractServlet;
import com.triangle.lightfw.SessionInfos;
import com.triangle.lightfw.ValueBeanList;

import formOnLine.actions.QuestionnaireAction;
import formOnLine.actions.TemplateAction;
import formOnLine.msBeans.Questionnaire;
import formOnLine.msBeans.Template;
import formOnLine.msBeans.UserData;

/**
 * @version   1.0
 * @author
 */
public class ServControl extends HttpServlet implements Servlet {

    private static final long serialVersionUID = 1L;
    
    public static final int LOGIN = 0 ;
    public static final int REP = 1 ;
    public static final int FIN = 2 ;
    public static final int INDEX = 3 ;
    public static final int QUEST = 4 ; // nouveau submitform
    
    public static final int QLIST = 5 ;
    public static final int QMAJ = 6 ; // modif modèle de questionnaire
    public static final int RLIST = 7 ;
    public static final int RMAJ = 8 ; // modif submitform
    public static final int SEXP = 9 ;
    public static final int EXPO = 10 ;
    public static final int TABOP = 11 ;
    public static final int LOCK = 12 ;
    public static final int UNLOCK = 13 ;
    public static final int RDET = 14 ; // consultation questionnaire
    public static final int LOCKALL = 15 ;
    public static final int RSUP = 16 ;
    public static final int RDEL = 17 ;
    public static final int ERREUR = -1 ;
    public static final int ADMIN_MAJ_QUESTIONNAIRE = 20 ;
    public static final int ADMIN_MAJ_GROUPE = 21 ;
    public static final int ADMIN_MAJ_QUESTION = 22 ;
    public static final int ADMIN_MAJ_PROPOSITION = 23 ;
    public static final int ADMIN_MAJ_TEMPLATE = 24 ;
    
    public static final int ADMIN_TAB = 30 ;
    public static final int ADMIN_MAJ_TAB = 31 ;
     
    public static final int IMP = 25 ;
    public static final int STAT = 26 ;
    public static final int MPWD = 27 ;
    
    
    
    
    public static final String USERDATA = "userdata";
    public static final String ACTION = "action";
    public static final String MESSAGE = "message";
    public static final String ROLE_REGION = "REGION";
    
    static protected Logger    traceLog  = Logger.getLogger("FOLE");
    
    
    
    private int init (String sAction) {
        int i=LOGIN ;
        if (sAction != null) {
            if (sAction.equals("LOGIN")) i=LOGIN ;
            if (sAction.equals("FIN")) i=FIN ;
            if (sAction.equals("INDEX")) i=INDEX ;
            if (sAction.equals("REP")) i=REP ;
            if (sAction.equals("QUEST")) i=QUEST ;
            if (sAction.equals("QLIST")) i=QLIST ;
            if (sAction.equals("QMAJ")) i=QMAJ ;
            if (sAction.equals("RMAJ")) i=RMAJ ;
            if (sAction.equals("SEXP")) i=SEXP ;
            if (sAction.equals("EXPO")) i=EXPO ;
            if (sAction.equals("TABOP")) i=TABOP ;
            if (sAction.equals("RLIST")) i=RLIST ;
            if (sAction.equals("LOCK")) i=LOCK ;
            if (sAction.equals("UNLOCK")) i=UNLOCK ;
            if (sAction.equals("LOCKALL")) i=LOCKALL ;
            if (sAction.equals("RSUP")) i=RSUP ;
            if (sAction.equals("RDEL")) i=RDEL ;
            if (sAction.equals("RDET")) i=RDET ;
            if (sAction.equals("ADMF")) i=ADMIN_MAJ_QUESTIONNAIRE ;
            if (sAction.equals("ADMG")) i=ADMIN_MAJ_GROUPE ;
            if (sAction.equals("ADMQ")) i=ADMIN_MAJ_QUESTION ;
            if (sAction.equals("ADMP")) i=ADMIN_MAJ_PROPOSITION ;
            if (sAction.equals("MODT")) i=ADMIN_MAJ_TEMPLATE ;
            if (sAction.equals("MODTAB")) i=ADMIN_TAB ;
            if (sAction.equals("MODTABMAJ")) i=ADMIN_MAJ_TAB ;
            if (sAction.equals("IMP")) i=IMP ;
            if (sAction.equals("STAT")) i=STAT ;
            if (sAction.equals("MPWD")) i=MPWD ; 
            
        }
        return i;
    }
    
    private void initRole (HttpServletRequest req,
                            SessionInfos sessionInfos ) {
        
        if (sessionInfos == null) sessionInfos = new SessionInfos();
        
        // On détermine si on est dans Jahia
        String portal = (String)req.getAttribute("org.portletapi.portal");
        if (portal == null) {
              // On n'est pas dans Jahia
              sessionInfos.setInJahia(false);
              
        } else {
              // On est dans Jahia
              sessionInfos.setInJahia(true);
              if (req.isUserInRole(ROLE_REGION)
                  || req.getRemoteUser().equals("root") 
                || req.getRemoteUser().equals("admin"))  {
                  sessionInfos.setLogin(ROLE_REGION);
                  sessionInfos.setInRoleRegion(true);
              } else {
                  sessionInfos.setLogin(req.getRemoteUser());
                  sessionInfos.setInRoleRegion(false);
              }
        }
        }

    
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException {
        doPost(req,resp);
    }
    
    
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException {
        
        HttpSession session = req.getSession(true);   
        ServletContext sc = getServletConfig().getServletContext();
        RequestDispatcher rd ;
        
        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        
        SessionInfos sessionInfos = null;
        //UserData userData = null;
        int action =0; 
        
        // reinit message
        session.setAttribute(MESSAGE,"");
        
        
//      test si session ouverte 
        if (session.getAttribute(AbstractServlet.ID_SESSION_INFOS)!=null)
          sessionInfos = (SessionInfos)session.getAttribute(AbstractServlet.ID_SESSION_INFOS);
        
        if (sessionInfos==null || 
                (!sessionInfos.isInJahia() 
                 && !Controls.isBlank(req.getParameter("adm_login")))) {
            //init des roles
            initRole(req, sessionInfos);
            
            if (sessionInfos!=null && sessionInfos.getRole()>SessionInfos.ROLE_PUBLIC ) {
                action= RLIST;
            } else {
                action= LOGIN;
            }
            
        } else {
            
            initRole( req, sessionInfos);
            
            boolean isMultipart = ServletFileUpload.isMultipartContent(req);
            
            if (isMultipart) {
                // reponse à un formulaire avec upload
                
                int lastAction = sessionInfos.getActionToDo();
                
                
                if (lastAction == SEXP || lastAction == IMP) {
                    action = IMP; // cas de l'import de fichier
                } else {
                    action = REP; // réponse standard à un formulaire (pouvant inclure des fichiers)
                }
                
                
            } else {
                action = init (req.getParameter(ACTION));
            }           
            
        }
        
        
        sessionInfos = (SessionInfos)session.getAttribute(AbstractServlet.ID_SESSION_INFOS);
        
        if (sessionInfos==null) sessionInfos = new SessionInfos();
        sessionInfos.setActionToDo(action);
        session.setAttribute( AbstractServlet.ID_SESSION_INFOS,sessionInfos );
        
        UserData user = (UserData)session.getAttribute(USERDATA);
        
        // init liste templates
        if ( sessionInfos.getRole() > SessionInfos.ROLE_PUBLIC 
                && session.getAttribute(Template.SESSION_LIST)==null ) {
            try {
                TemplateAction ta = new TemplateAction();
                ValueBeanList lt = ta.selectAllByRole( sessionInfos.getRole());
                session.setAttribute(Template.SESSION_LIST,lt);
            } catch (SQLException e ) {}
        }
        
        // montée en session d'une variable de contexte pour la gestion de l'affichage 
        if (req.getParameter("currentContext")!= null)
            session.setAttribute("currentContext", req.getParameter("currentContext"));
       
        // DISPATCH
        switch (action) {
        //----------------------------------------------------------
        case LOGIN : 
            rd =  sc.getRequestDispatcher("/ServLogin");
            rd.include (req,resp);
            
            rd =  sc.getRequestDispatcher("/login.jsp");
            rd.forward(req,resp);
            break;
            
        //----------------------------------------------------------          
        case QLIST :
            
            if (user.getRole() < SessionInfos.ROLE_GESTION) break;
            
            rd =  sc.getRequestDispatcher("/ServListQuest");
            rd.include (req,resp);
            
            rd =  sc.getRequestDispatcher("/qlist.jsp");
            rd.forward (req,resp);
            break;
            

        
        //----------------------------------------------------------          
        case ADMIN_TAB :
            
            if (user.getRole() < SessionInfos.ROLE_ADMIN) break;
            
            
            rd =  sc.getRequestDispatcher("/tablist.jsp");
            rd.forward (req,resp);
            break;
        
        //----------------------------------------------------------  
        case ADMIN_MAJ_TAB :
            
            if (user.getRole() < SessionInfos.ROLE_ADMIN) break;
            
            
            rd =  sc.getRequestDispatcher("/ServConfigMaj");
            rd.include (req,resp);
            
            rd =  sc.getRequestDispatcher("/tablist.jsp");
            rd.forward (req,resp);
            
            break;
            
        
        //----------------------------------------------------------
        case RLIST :
            
            if (user.getRole() < SessionInfos.ROLE_CONSULTATION) break;
            
            rd =  sc.getRequestDispatcher("/ServListRep");
            rd.include (req,resp);
            
            rd =  sc.getRequestDispatcher("/rlist.jsp");
            rd.forward (req,resp);
            break;
            
//          ----------------------------------------------------------
        case RSUP :
            
            if (user.getRole() < SessionInfos.ROLE_GESTION
                    && !user.isItsOwnData()) break;
            
            rd =  sc.getRequestDispatcher("/confirmSupRep.jsp");
            rd.forward (req,resp);
            break;
            
//          ----------------------------------------------------------
        case RDEL :
            
            if (user.getRole() < SessionInfos.ROLE_GESTION
                    && !user.isItsOwnData()) break;
            
            rd =  sc.getRequestDispatcher("/ServQuest");
            rd.include (req,resp);
                        
            rd =  sc.getRequestDispatcher("/ServLogin");
            rd.include (req,resp);
            
            rd =  sc.getRequestDispatcher("/login.jsp");
            rd.forward (req,resp);
            break;
            
//          ----------------------------------------------------------
        case RDET :
            
            if (user.getRole() < SessionInfos.ROLE_CONSULTATION
                    && !user.isItsOwnData()) break;
            
            rd =  sc.getRequestDispatcher("/ServQuest");
            rd.include (req,resp);
            
            rd =  sc.getRequestDispatcher("/rdetail.jsp");
            rd.forward (req,resp);
            break;
            
            //----------------------------------------------------------
        case LOCK  : case UNLOCK : 
            
            if (user.getRole() < SessionInfos.ROLE_GESTION
                    && !user.isItsOwnData()) break;
            
            rd =  sc.getRequestDispatcher("/ServLock");
            rd.include (req,resp);
            
            if (user.getRole() < SessionInfos.ROLE_GESTION) {
                rd =  sc.getRequestDispatcher("/login.jsp");
            } else {
                rd =  sc.getRequestDispatcher("/rlist.jsp");
            }
            
            rd.forward (req,resp);
            break;
            
            //----------------------------------------------------------
        case MPWD : 
            
            
            rd =  sc.getRequestDispatcher("/ServMailPwd");
            rd.include (req,resp);
            
            rd =  sc.getRequestDispatcher("/login.jsp");
            rd.forward (req,resp);
            break;
            
            //----------------------------------------------------------
        case LOCKALL :
            
            if (user.getRole() < SessionInfos.ROLE_GESTION
                    && !user.isItsOwnData()) break;
            
            rd =  sc.getRequestDispatcher("/ServLock");
            rd.include (req,resp);
            
            rd =  sc.getRequestDispatcher("/login.jsp");
            rd.forward (req,resp);
            break;
            
            //----------------------------------------------------------
        case QUEST : case RMAJ :
            
            //if (?) break;
            
            rd =  sc.getRequestDispatcher("/ServQuest");
            rd.include(req,resp);
            
            rd =  sc.getRequestDispatcher("/quest.jsp");
            rd.forward (req,resp);

            break;
            //----------------------------------------------------------
        case QMAJ :
            
            if (user.getRole() < SessionInfos.ROLE_ADMIN) break;
            
            rd =  sc.getRequestDispatcher("/ServQuest");
            rd.include(req,resp);
            
            rd =  sc.getRequestDispatcher("/quest.jsp");
            rd.forward (req,resp);

            break;
            
            //----------------------------------------------------------
        case REP :
            
            //if (user.getRole() < SessionInfos.ROLE_GESTION && !user.isItsOwnData()) break;
            
            rd =  sc.getRequestDispatcher("/ServRep");
            rd.include (req,resp);
            
            // PLA 19/04/2005: Orientation du résultat
            String actionBis = (String)req.getAttribute(ACTION);
            if ((actionBis != null) && (actionBis.equals("RMAJ")))
            {
                action = init(actionBis);
                sessionInfos.setActionToDo(action);
                rd =  sc.getRequestDispatcher("/quest.jsp");
                rd.forward (req,resp);
            }
            else
                // Comme avant!
            {
                rd =  sc.getRequestDispatcher("/conclusion.jsp");
                rd.forward (req,resp);
            }
            
            break;
            
            //----------------------------------------------------------
        case TABOP :
            
            if ( sessionInfos.getRole()< SessionInfos.ROLE_GESTION) break;
            
            rd =  sc.getRequestDispatcher("/ServRep");
            rd.include (req,resp);
            
            rd =  sc.getRequestDispatcher("/tabop.jsp");
            rd.forward (req,resp);
            
            break;
            
            //----------------------------------------------------------
        case SEXP :
            
            if ( sessionInfos.getRole()< SessionInfos.ROLE_GESTION) break;
            
            if (session.getAttribute(Template.SESSION_LIST)==null) {
                try {
                    TemplateAction ta = new TemplateAction();
                    ValueBeanList lt = ta.selectAllByRole(sessionInfos.getRole());
                    session.setAttribute(Template.SESSION_LIST,lt);
                } catch (SQLException e ) {}
            }
            if (session.getAttribute(Questionnaire.SESSION_LIST)==null) {
                try {
                    QuestionnaireAction qa = new QuestionnaireAction();
                    ValueBeanList lq = qa.selectAll();
                    session.setAttribute(Questionnaire.SESSION_LIST,lq);
                } catch (SQLException e ) {}
            }
                
            
            rd =  sc.getRequestDispatcher("/export.jsp");
            rd.forward (req,resp);
            
            break;
            
            //----------------------------------------------------------
        case EXPO :
            
            // remettre des droits d'export
            //if ( sessionInfos.getRole()< SessionInfos.ROLE_GESTION) break;
            
            rd =  sc.getRequestDispatcher("/ServExport");
            rd.forward (req,resp);
            
            break;
            //----------------------------------------------------------
        case IMP :
            
            if ( sessionInfos.getRole()< SessionInfos.ROLE_GESTION) break;
            
            rd =  sc.getRequestDispatcher("/ServImport");
            rd.include (req,resp);
            rd =  sc.getRequestDispatcher("/export.jsp");
            rd.forward (req,resp);
            break;
              
//          ----------------------------------------------------------
        case ADMIN_MAJ_QUESTIONNAIRE : 
            
            if (!sessionInfos.isInRoleRegion() 
                    && sessionInfos.getRole()< SessionInfos.ROLE_ADMIN) break;
            
            rd =  sc.getRequestDispatcher("/ServAdmMaj");
            rd.include (req,resp);
            
            sessionInfos.setActionToDo(ServControl.QMAJ);
            
            if (!Controls.isBlank(sessionInfos.getMessage())) {
                
                rd =  sc.getRequestDispatcher("/majForm.jsp");
                rd.forward (req,resp);
            } else {
                rd =  sc.getRequestDispatcher("/quest.jsp");
                rd.forward (req,resp);
            }
            break;
            
            
//          ----------------------------------------------------------
        case ADMIN_MAJ_GROUPE : 
            
            if (!sessionInfos.isInRoleRegion() 
                    && sessionInfos.getRole()< SessionInfos.ROLE_ADMIN) break;
             
            rd =  sc.getRequestDispatcher("/ServAdmMaj");
            rd.include(req,resp);
            
            sessionInfos.setActionToDo(ServControl.QMAJ);
            
            if (!Controls.isBlank(sessionInfos.getMessage())) {
                
                rd =  sc.getRequestDispatcher("/majGroup.jsp");
                rd.forward (req,resp);
            } else {
                
                rd =  sc.getRequestDispatcher("/quest.jsp");
                rd.forward (req,resp);
            }
            
            
            break;
            
            
//          ----------------------------------------------------------
        case ADMIN_MAJ_QUESTION :
            
            if (!sessionInfos.isInRoleRegion() 
                    && sessionInfos.getRole()< SessionInfos.ROLE_ADMIN) break;
            
            rd =  sc.getRequestDispatcher("/ServAdmMaj");
            rd.include(req,resp);
            
            sessionInfos.setActionToDo(ServControl.QMAJ);
            
            if (!Controls.isBlank(sessionInfos.getMessage())) {
                
                rd =  sc.getRequestDispatcher("/majQuest.jsp");
                rd.forward (req,resp);
            } else {
                
                rd =  sc.getRequestDispatcher("/quest.jsp");
                rd.forward (req,resp);
            }
            break;
            
            
//          ----------------------------------------------------------
        case ADMIN_MAJ_PROPOSITION :
            
            if (!sessionInfos.isInRoleRegion() 
                    && sessionInfos.getRole()< SessionInfos.ROLE_ADMIN) break;
            
            rd =  sc.getRequestDispatcher("/ServAdmMaj");
            rd.include(req,resp);
            
            sessionInfos.setActionToDo(ServControl.QMAJ);
            
            if (!Controls.isBlank(sessionInfos.getMessage())) {
                
                rd =  sc.getRequestDispatcher("/majProp.jsp");
                rd.forward (req,resp);
            } else {
                
                rd =  sc.getRequestDispatcher("/quest.jsp");
                rd.forward (req,resp);
            }
            break;
            
//          ----------------------------------------------------------
        case ADMIN_MAJ_TEMPLATE : 
            
            //if (sessionInfos == null || sessionInfos.getRole()< SessionInfos.ROLE_GESTION) break;
            
            rd =  sc.getRequestDispatcher("/ServTemplateMaj");
            rd.include (req,resp);
            
            sessionInfos.setActionToDo(ServControl.SEXP);
            
            if (!Controls.isBlank(sessionInfos.getMessage())) {
                
                rd =  sc.getRequestDispatcher("/majTemplate.jsp");
                rd.forward (req,resp);
            } else {
                rd =  sc.getRequestDispatcher("/export.jsp");
                rd.forward (req,resp);
            }
            break;
            
            //          ----------------------------------------------------------
        case STAT : 
            if (!sessionInfos.isInRoleRegion() 
                    && sessionInfos.getRole()<= SessionInfos.ROLE_GESTION) break;
            
            rd =  sc.getRequestDispatcher("/ServStat");
            rd.include (req,resp);
            
            rd =  sc.getRequestDispatcher("/stats.jsp");
            rd.forward (req,resp);
            break;
            
            //----------------------------------------------------------          
            
            
            //----------------------------------------------------------
        case FIN :
            
            /*rd =  sc.getRequestDispatcher("/fin.jsp");
             rd.forward (req,resp);
             session.invalidate();*/
            session.removeAttribute(AbstractServlet.ID_SESSION_INFOS);
            session.removeAttribute(USERDATA);
            session.removeAttribute("q");
            session.removeAttribute("sf");
            session.removeAttribute(ACTION);
            session.removeAttribute("searchQ1options");
            session.removeAttribute("searchQ2options");
            session.removeAttribute("findQ1options");
            session.removeAttribute("findQ2options");
            session.removeAttribute("rlist");
            
            rd =  sc.getRequestDispatcher("/fin.jsp");
            rd.forward (req,resp);
            break;
            
            //----------------------------------------------------------
        case ERREUR : default :
            rd =  sc.getRequestDispatcher("/erreur.jsp");
            rd.forward (req,resp);
            break;
        } 
        
        
    }
    
}
