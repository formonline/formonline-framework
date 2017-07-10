package formOnLine;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import formOnLine.actions.QuestionnaireAction;
import formOnLine.actions.TemplateAction;
import formOnLine.msBeans.Questionnaire;
import formOnLine.msBeans.Template;
import formOnLine.msBeans.UserData;

import com.triangle.lightfw.AbstractServlet;
import com.triangle.lightfw.SessionInfos;

/**
 * @version 	1.0
 * @author
 */
public class ServTemplateMaj extends HttpServlet implements Servlet {

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
        boolean bMaj = false;
        boolean bCreat = false;

        QuestionnaireAction qa = new QuestionnaireAction();

        // contrôle profil
        if (sessionInfos.getRole()< SessionInfos.ROLE_GESTION ) {
            message = "Droits Insuffisants" ;
            sessionInfos.setMessage(message);
            return;
        }


        if (req.getParameter("bok_maj")!= null) bMaj = true;
        if (req.getParameter("bok_creat")!= null) bCreat = true;

        int t_id = -1;
        try { t_id = Integer.parseInt(req.getParameter("t_id"));
        } catch (NumberFormatException  e) {}



        Template t = null;
        TemplateAction ta = new TemplateAction(); 



        if (!bCreat && !bMaj && t_id == -1) {
            t = new Template();

            message="Création Template...";

        } else if (!bCreat && !bMaj && t_id > -1) {

            try {
                t = ta.selectOne(t_id);
            } catch (SQLException  e) {}

            if (t == null) message = "Aucune donnée sélectionnée";

            message="Modification Template...";

        } else if ( bMaj || bCreat) {

            t = new Template();
            if (t_id >0) t.setId(t_id);


            t.setName(req.getParameter("t_name"));

            t.setType(req.getParameter("t_type"));
            t.setContent(req.getParameter("t_content")); 


            try { t.setFid(Integer.parseInt(req.getParameter("f_id")));
            } catch (NumberFormatException  e) {}

            try {

                
                UserData ud = (UserData)session.getAttribute(ServControl.USERDATA);

                Questionnaire f = ud.getQuest(t.getFid());
                if (f==null) f = qa.selectById(t.getFid());
                
                // contrôle droits avant MAJ
                if ((f!=null && f.getF_connected() <= Questionnaire.ACCESS_RESTRICTED      
                        && ud != null && ud.getRole() == SessionInfos.ROLE_GESTION) 
                        ||
                        (ud.getRole() >= SessionInfos.ROLE_ADMIN))  {


                    // update
                    if (bMaj) {
                        ta.update(t);
                        //message = "MAJ ok";
                    }
                    
                    // create
                    if (bCreat) { 
                        ta.insert(t);
                        //message = "CREATION ok";
                    }

                } else {
                    message = "Droits Insuffisants" ;
                    sessionInfos.setMessage(message);
                    return;
                }




            } catch (SQLException e) {
                message = e.getMessage();
            }

        } 

        if (!Controls.isBlank(message))
            session.setAttribute(Template.SESSION_NAME,t);

        try {
            session.setAttribute(Template.SESSION_LIST, ta.selectAllByRole(sessionInfos.getRole()));
        } catch (SQLException e) {}

        sessionInfos.setMessage(message);

    }

}
