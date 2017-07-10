package formOnLine;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException; 
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.triangle.lightfw.AbstractServlet;
import com.triangle.lightfw.SessionInfos;

import formOnLine.actions.QuestionnaireAction;
import formOnLine.msBeans.Questionnaire;
import formOnLine.msBeans.UserData;

/**
 * @version     1.0
 * @author
 */
public class ServGetFile extends HttpServlet implements Servlet {
    
    private static final long serialVersionUID = 1L;
    private static String MSG_BADFILE = "Impossible de télécharger le document";
    private static String MSG_BADRIGHTS = "Droits insuffisants";
    private static String MSG_FILENOTFOUND = "Fichier non trouvé";
    
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException {
        
        doPost(req,resp);
    }
    
    
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException {
        
        HttpSession session = req.getSession(true);
        QuestionnaireAction qa = new QuestionnaireAction();
        
        String message= null; 
        
        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        
        
        int sid = -1;
        String fileName = null;
        
        SessionInfos sessionInfos =  (SessionInfos)session.getAttribute( AbstractServlet.ID_SESSION_INFOS);
        if (sessionInfos == null) message = MSG_BADRIGHTS;
        
        UserData uData = (UserData)session.getAttribute(ServControl.USERDATA);
        if (uData != null 
                && !uData.isConnected()
                && uData.getRole() < SessionInfos.ROLE_CONSULTATION) {            
            message = MSG_BADRIGHTS;
        }
        
        if (message == null) {
            try {
                
                if (!Controls.isBlank(req.getParameter("sid"))) 
                    sid=Integer.parseInt( req.getParameter("sid"));
                
                if (!Controls.isBlank(req.getParameter("file"))) {
                    fileName = (String)req.getParameter("file");
                }
                
                if (!uData.isItsOwnData(sid,true) 
                        && !(uData.getRole() >= SessionInfos.ROLE_GESTION) ) {
                    
                    Questionnaire q = qa.selectById(uData.getRep(sid).getF_id());
                    if (q.getF_connected()>0) {
                        // droits insuffisants si le questionnaire n'est pas public
                        message = MSG_BADRIGHTS;
                    }
                } 
                
                if (message == null) {
                    File fileToDwnld = new File(
                            InitServlet.getFileUploadDir() + File.separator +
                            sid + File.separator +
                            fileName );
                    
                    if (!fileToDwnld.exists()) {
                        message = MSG_FILENOTFOUND;
                    } else {
                        String ext = fileName.substring(fileName.lastIndexOf('.')+1).toLowerCase();
                        
                        resp.setContentType("application/"+ext);
                        
                        /*String user_agent = req.getHeader("user-agent");
                        boolean isIE = (user_agent.indexOf("MSIE") > -1);
                        boolean isChrome = (user_agent.indexOf("Chrome") > -1);
                        if (isIE || isChrome) {
                            resp.addHeader("Content-disposition", "attachment; filename=\"" + fileName + "\"");
                        } else {
                            resp.addHeader("Content-disposition", "attachment; filename*=UTF-8''" +fileName );
                        }*/
                        
                        resp.addHeader("Content-Disposition", "attachment;filename=\"" 
                                + fileName + "\"; filename*=UTF-8''" +URLEncoder.encode(fileName,"UTF-8").replaceAll("\\+", "%20"));
                        
                        
                        
                        OutputStream out = resp.getOutputStream();
                        BufferedInputStream bis=new BufferedInputStream(
                                new FileInputStream(fileToDwnld));
                        
                        int readBytes = 0;
                        while ((readBytes = bis.read()) != -1)
                            out.write(readBytes);
                        
                        
                        out.flush();
                        bis.close();
                        
                    }
                    
                }  
                
                
                
            } catch (Exception e) {
                message = MSG_BADFILE;
            }
        }
        
        
        if (message != null) {
            
            // telechargement raté
            
            if ( sessionInfos != null ) sessionInfos.setMessage(message);
            
            ServletContext sc = getServletConfig().getServletContext();
            RequestDispatcher rd = sc.getRequestDispatcher("/ServControl");
            
            rd.forward (req,resp);
        }
        
        
        
        
    }
    
}
