package formOnLine;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import formOnLine.actions.ConfigAction;
import formOnLine.msBeans.Config;

import com.triangle.lightfw.AbstractServlet;
import com.triangle.lightfw.SessionInfos;

/**
 * @version 	1.0
 * @author
 */
public class ServConfigMaj extends HttpServlet implements Servlet {
    
    private static final long serialVersionUID = 1L;
    
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException {
        doPost(req,resp);
    }
    
    
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException {
        
        HttpSession session = req.getSession(true);
        SessionInfos sessionInfos =  (SessionInfos)session.getAttribute( AbstractServlet.ID_SESSION_INFOS);
        
        
        String message="";  
        
        // contrôle profil
        if (sessionInfos.getRole()< SessionInfos.ROLE_ADMIN) {
            message = "Profil ADMIN nécessaire" ;
            sessionInfos.setMessage(message);
            return;
        }
        
        HashMap params = (HashMap)session.getAttribute(Config.SESSION_NAME );
        ConfigAction ca = new ConfigAction();
        
        
        Set cles = params.keySet();
        Iterator it = cles.iterator();
        
        while (it.hasNext()){
            
            String cle = (String)it.next(); 
            String valeur = req.getParameter(cle);   
            
            
            if (cle.startsWith("is")) {
                if (valeur !=null && valeur.equals("on")) {
                    valeur="true";
                } else {
                    valeur="false";
                }
            }
            
            Config conf = new Config();
            conf.setParam(cle);
            conf.setVal(valeur);
            try {
                ca.updateValByParam(conf);
            } catch (SQLException e) {
                message = e.getLocalizedMessage();
            }
            
            
        }
        
        // reload params
        try {
            params = ca.getParams();
        } catch (SQLException e) {
            message = e.getLocalizedMessage();
        }
        
        if (message==null || message.equals("")) message = "Configuration enregistrée";
        
        session.setAttribute(Config.SESSION_NAME,params);
        //sessionInfos.setMessage(message);
        session.setAttribute(ServControl.MESSAGE ,  message);
        
    }
}
