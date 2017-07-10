package formOnLine;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import formOnLine.actions.ConfigAction;
import formOnLine.actions.UserActionFactory;
import formOnLine.actions.UserActionInterface;
import formOnLine.msBeans.Config;
import formOnLine.msBeans.SubmitForm;
import formOnLine.msBeans.UserData;

import com.triangle.lightfw.AbstractServlet;
import com.triangle.lightfw.SessionInfos;

/**
 * @version 	1.0
 * @author
 */
public class ServLogin extends HttpServlet implements Servlet {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;


    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req,resp);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(true);
        SessionInfos sessionInfos = (SessionInfos)session.getAttribute(AbstractServlet.ID_SESSION_INFOS);
        
        UserActionInterface ua = null;
        String msg = null;
        HashMap<String, String> conf = null;
        boolean firstConnection = false;

        UserData newUserData = new UserData();

        String login = req.getParameter("login");
        if (login == null) login = req.getParameter("adm_login");
        String pwd = req.getParameter("pwd");
        if (pwd == null) pwd = req.getParameter("adm_pwd");
        
        // cryptage MD5 du pwd admin
        if ("admin".equals(login)) pwd = BasicTools.crypt(pwd);

        newUserData.setLogin(login);
        newUserData.setName(login);
        newUserData.setPwd(pwd);

        int loginPid = -1;
        if (req.getParameter("loginPid") != null)
            loginPid = Integer.parseInt(req.getParameter("loginPid"));

        int roleQid = -1;
        if (req.getParameter("roleQid") != null)
            roleQid = Integer.parseInt(req.getParameter("roleQid"));

        String ldapSource = null;
        if (req.getParameter("ldapSource") != null) 
            ldapSource = req.getParameter("ldapSource"); 
        
        
        UserData activeUserData = (UserData)session.getAttribute(ServControl.USERDATA);

        try {   

            // ----------------------------------------------------
            // cas 1 : pas de user connecté, demande de connexion
            if (login!=null && (activeUserData==null || !activeUserData.isConnected() )) {

                // Connection admin (sans acces base) 
                if (login != null && pwd!=null && login.equals("admin") && pwd.equals(InitServlet.getAdminPwd())) {
                    sessionInfos = (SessionInfos)session.getAttribute(AbstractServlet.ID_SESSION_INFOS);
                    sessionInfos.setRole(SessionInfos.ROLE_ADMIN  );

                    newUserData.setRole(SessionInfos.ROLE_ADMIN);
                    firstConnection = true;

                } else {

                    // LOGIN tentative connexion normale
                    if ( login!=null && !login.equals("admin") && (activeUserData==null || !activeUserData.isConnected()))   {

                        // lancement du contrôle + initialisation userData
                        ua = new UserActionFactory().getUserAction("ContextUserAction") ;

                        msg = ua.check(newUserData, loginPid, roleQid, ldapSource);
                        if (msg == null) firstConnection = true;

                    }

                }
            }
            
            // ----------------------------------------------------
            //cas 2 :  LOGIN de type "CONNECT AS" : uniquement en admin & pour les délégations
            if (login!=null && !login.equals("admin") 
                    && activeUserData!=null && activeUserData.isConnected() && roleQid>=0) {

                ua = new UserActionFactory().getUserAction("ContextUserAction") ;

                msg = ua.connectAs(activeUserData, newUserData, roleQid);
                if (msg == null) firstConnection = true;
            }
            

            // ----------------------------------------------------
            // inits complémentaires
            if (firstConnection) { 

                // init configuration
                if (sessionInfos.getRole() > SessionInfos.ROLE_PUBLIC) {
                    conf = new ConfigAction().getParams();
                } else {
                    conf = new ConfigAction().getParams(true, newUserData.getId() );
                }

                session.setAttribute(Config.SESSION_NAME ,conf);

                //base URL pour les images  dans les templates
                StringBuffer url = req.getRequestURL();
                String uri = req.getRequestURI();
                String ctx = req.getContextPath();
                String baseURL = url.substring(0, url.length() - uri.length() + ctx.length()) + "/";
                newUserData.setBaseUrl(baseURL);
                
                // inits spécifiques après authentification, en fonction du user (sauf admin :bakoffice only)
                if (!"admin".equals(login)) msg = ua.initAfterCheck(newUserData, conf);
                if (msg==null) {
                    sessionInfos.setRole(newUserData.getRole());    
                    session.setAttribute(ServControl.USERDATA,newUserData);
                }
            }

            
            // Probleme de connexion
            if (msg!=null) {
                session.setAttribute(ServControl.MESSAGE,msg);
                newUserData = new UserData();   
                session.setAttribute(ServControl.USERDATA,newUserData);
            } 


        } catch (Exception e) {
            session.setAttribute(ServControl.MESSAGE,e.getMessage());
            newUserData = new UserData();     
        }

        
        // ----------------------------------------------------
        // INITialisations  SPECIFIQUES à chaque appel de la home
        try {
            if (ua == null) ua = new UserActionFactory().getUserAction("ContextUserAction") ;

            activeUserData = (UserData)session.getAttribute(ServControl.USERDATA);
            msg= ua.initBeforeIndex(activeUserData,session,req);
            
            // Probleme d'init
            if (msg!=null) {
                session.setAttribute(ServControl.MESSAGE,msg);
                
            } 

        } catch (Exception e) {
            session.setAttribute(ServControl.MESSAGE,e.getMessage());
            
        }

    } 
}
