package formOnLine;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import formOnLine.actions.GroupeAction;
import formOnLine.actions.PropositionAction;
import formOnLine.actions.QuestionAction;
import formOnLine.actions.QuestionnaireAction;
import formOnLine.msBeans.Groupe;
import formOnLine.msBeans.Proposition;
import formOnLine.msBeans.Question;
import formOnLine.msBeans.Questionnaire;
import formOnLine.msBeans.UserData;

import com.triangle.lightfw.AbstractServlet;
import com.triangle.lightfw.BasicType;
import com.triangle.lightfw.SessionInfos;

/**
 * @version 	1.0
 * @author
 */
public class ServAdmMaj extends HttpServlet implements Servlet {
    
    private static final long serialVersionUID = 1L;
    
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException {
        doPost(req,resp);
    }
    
    
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException {
        
        HttpSession session = req.getSession(true);
        SessionInfos sessionInfos =  (SessionInfos)session.getAttribute( AbstractServlet.ID_SESSION_INFOS);
        
        int action = sessionInfos.getActionToDo();
        String message=""; 
        boolean bMaj = false;
        boolean bCreat = false;
        
        
        UserData uData = (UserData)session.getAttribute(ServControl.USERDATA);
        
        // contrôle profil
        if (uData == null || uData.getName() == null || !uData.getName().equals("admin")) {
            //message = "Profil ADMIN nécessaire" ;
            //action = -1;
        }
        
        if (req.getParameter("bok_maj")!= null) bMaj = true;
        if (req.getParameter("bok_creat")!= null) bCreat = true;
        
        int f_id = -1;
        try { f_id = Integer.parseInt(req.getParameter("f_id"));
        } catch (NumberFormatException  e) {}
        
        int g_id = -1;
        try { g_id = Integer.parseInt(req.getParameter("g_id"));
        } catch (NumberFormatException  e) {}
        
        int q_id = -1;
        try { q_id = Integer.parseInt(req.getParameter("q_id"));
        } catch (NumberFormatException  e) {}
        
        int p_id = -1;
        try { p_id = Integer.parseInt(req.getParameter("p_id"));
        } catch (NumberFormatException  e) {}
        
        
        
        switch (action) {
        /* **************************************************************** */
        case ServControl.ADMIN_MAJ_QUESTIONNAIRE :{
            
            Questionnaire f = null;
            QuestionnaireAction qa = new QuestionnaireAction(); 
            
            
            
            if (!bCreat && !bMaj && f_id == -1) {
                f = new Questionnaire();
                f.setDate_creation(BasicType.getTodaysDateIso());
                f.setDate_debut(BasicType.getTodaysDateIso());
                if (uData != null && uData.getName()!=null) f.setAuteur(uData.getName());
                
                message="Création Questionnaire...";
                
            } else if (!bCreat && !bMaj && f_id > -1) {
                
                try {
                    f = qa.selectById(f_id);
                } catch (SQLException  e) {}
                
                if (f == null) message = "Aucune donnée sélectionnée";
                
                message="Modification Questionnaire...";
                
            } else if ( bMaj || bCreat) {
                
                f = new Questionnaire();
                if (f_id >0) f.setId(f_id);
                
                
                f.setTitre(req.getParameter("f_titre"));
                
                //f.setAuteur(req.getParameter("f_auteur"));
                
                f.setIntroduction(req.getParameter("f_introduction"));
                f.setConclusion(req.getParameter("f_conclusion")); 
                f.setF_mail_adm(req.getParameter("f_mail_adm"));
                
                
                
                try { f.setP_id_titre(Integer.parseInt(req.getParameter("p_id_titre")));
                } catch (NumberFormatException  e) {}
                try { f.setP_id_mail(Integer.parseInt(req.getParameter("p_id_mail")));
                } catch (NumberFormatException  e) {}
                try { f.setT_id_mail_on_create(Integer.parseInt(req.getParameter("t_id_mail_on_create")));
                } catch (NumberFormatException  e) {}
                try { f.setT_id_mail_on_lock(Integer.parseInt(req.getParameter("t_id_mail_on_lock")));
                } catch (NumberFormatException  e) {}
                try { f.setT_id_mail_on_delete(Integer.parseInt(req.getParameter("t_id_mail_on_delete")));
                } catch (NumberFormatException  e) {}
                try { f.setF_connected(Integer.parseInt(req.getParameter("f_connected")));
                } catch (NumberFormatException  e) {}
                try { f.setF_suppr(Integer.parseInt(req.getParameter("f_suppr")));
                } catch (NumberFormatException  e) {}
                try { f.setF_authent(Integer.parseInt(req.getParameter("f_authent")));
                } catch (NumberFormatException  e) {}
                
                f.setDate_debut(BasicType.parseDateIsoFromLocal(req.getParameter("f_date_debut"), null)); 
                f.setDate_fin(BasicType.parseDateIsoFromLocal(req.getParameter("f_date_fin"),null)); 
                f.setShowPwd((req.getParameter("f_show_pwd").equals("1")));
                
                try {
//                  update
                    if (bMaj) {
                        qa.update(f);
                        //message = "MAJ ok";
                    }
//                  create
                    if (bCreat) { 
                        qa.insert(f);
                        //message = "CREATION ok";
                        
                        // récupération du formulaire créé
                        f = qa.selectByMaxId();
                    }
                    
                    
                } catch (SQLException e) {
                    message = e.getMessage();
                }
                
            } 
            
            if (!Controls.isBlank(message))
                session.setAttribute(Questionnaire.SESSION_NAME,f);
            break;
        }
        
        /* **************************************************************** */
        case ServControl.ADMIN_MAJ_GROUPE : {
            
            Groupe g = null;
            GroupeAction ga = new GroupeAction(); 
            
            
            
            if (!bCreat && !bMaj && g_id == -1 && f_id > 0) {
                g = new Groupe();
                g.setF_id(f_id);
                g.setType(Groupe.PUBLIC);
                g.setTexte("");
                g.setTitre("");
                
                message="Création groupe...";
                
            } else if (!bCreat && !bMaj && g_id > 0) {
                
                try {
                    g = ga.selectById(g_id);
                } catch (SQLException  e) {}
                
                if (g == null) {
                    message = "Aucune donnée sélectionnée";
                } else {
                    message="Modification groupe...";
                }
                
                
            } else if ( bMaj || bCreat) {
                
                g = new Groupe();
                if (g_id >0) g.setId(g_id);
                if (f_id >0) g.setF_id(f_id);
                
                g.setTitre(req.getParameter("g_titre"));
                g.setTexte(req.getParameter("g_texte"));
                g.setType(req.getParameter("g_type"));
                
                try { g.setNum(Integer.parseInt(req.getParameter("g_num")));
                } catch (NumberFormatException  e) {}
                
                try {
//                  update
                    if (bMaj) {
                        ga.update(g);
                    }
//                  create
                    if (bCreat) { 
                        ga.insert(g);
                    }
                    
                } catch (SQLException e) {
                    message = e.getMessage();
                }
                
            } 
            
            if (!Controls.isBlank(message)) 
                session.setAttribute(Groupe.SESSION_NAME,g);
            
            break;
        }
        
        /* **************************************************************** */
        case ServControl.ADMIN_MAJ_QUESTION : {
            
            Question q = null;
            QuestionAction qa = new QuestionAction(); 
            
            if (!bCreat && !bMaj && q_id == -1 && g_id > 0) {
                q = new Question();
                q.setG_id(g_id);
                q.setType(Question.TEXTE);
                q.setTexte("");
                q.setSize(255);
                q.setMandatory(0);
                q.setNum(0);
                q.setSearch(false);
                
                message="Création question...";
                
            } else if (!bCreat && !bMaj && q_id > 0) {
                
                try {
                    q = qa.selectById(q_id);
                } catch (SQLException  e) {}
                
                if (q == null) {
                    message = "Aucune donnée sélectionnée";
                } else {
                    message="Modification question...";
                }
                
                
            } else if ( bMaj || bCreat) {
                
                q = new Question();
                if (q_id >0) q.setId(q_id);
                if (g_id >0) q.setG_id(g_id);
                
                q.setTexte(req.getParameter("q_texte"));
                q.setType(req.getParameter("q_type"));
                
                
                try { q.setNum(Integer.parseInt(req.getParameter("q_num")));
                } catch (NumberFormatException  e) {}
                try { q.setSize(Integer.parseInt(req.getParameter("q_size")));
                } catch (NumberFormatException  e) {}
                try { q.setMandatory(Integer.parseInt(req.getParameter("q_mandatory")));
                } catch (NumberFormatException  e) {}
                try { q.setSearch((Integer.parseInt(req.getParameter("q_search")))==1);
                } catch (NumberFormatException  e) {}
                
                try {
//                  update
                    if (bMaj) {
                        qa.update(q);
                    }
//                  create
                    if (bCreat) { 
                        qa.insert(q);
                    }
                    
                } catch (SQLException e) {
                    message = e.getMessage();
                }
                
            } 
            
            if (!Controls.isBlank(message)) 
                session.setAttribute(Question.SESSION_NAME,q);
            
            break;
        }
        
        /* **************************************************************** */
        case ServControl.ADMIN_MAJ_PROPOSITION :
            
            Proposition p = null;
            PropositionAction pa = new PropositionAction(); 
            
            
            
            if (!bCreat && !bMaj && p_id == -1 && q_id > 0) {
                p = new Proposition();
                p.setQid(q_id);
                p.setTexte("");
                p.setNum(0);
                p.setAlert("");
                
                message="Création proposition...";
                
            } else if (!bCreat && !bMaj && p_id > 0) {
                
                try {
                    p = pa.selectById(p_id);
                } catch (SQLException  e) {}
                
                if (p == null) {
                    message = "Aucune donnée sélectionnée";
                } else {
                    message="Modification proposition...";
                }
                
                
            } else if ( bMaj || bCreat) {
                
                p = new Proposition();
                if (p_id >0) p.setId(p_id);
                if (q_id >0) p.setQid(q_id);
                
                p.setTexte(req.getParameter("p_texte"));
                p.setAlert(req.getParameter("p_alert"));
                
                try { p.setNum(Integer.parseInt(req.getParameter("p_num")));
                } catch (NumberFormatException  e) {}
                
                try { p.setInitload(Integer.parseInt(req.getParameter("p_initload")));
                } catch (NumberFormatException  e) {}
                
                try { p.setStat(Integer.parseInt(req.getParameter("p_stat")));
                } catch (NumberFormatException  e) {}
                
                
                try {
//                  update
                    if (bMaj) pa.update(p);
                    
//                  create
                    if (bCreat) pa.insert(p);
                    
                } catch (SQLException e) {
                    message = e.getMessage();
                }
                
            } 
            
            if (!Controls.isBlank(message)) 
                session.setAttribute(Proposition.SESSION_NAME,p);
            
            break;
            
            
            
        } //switch
        
        if (Controls.isBlank(message)) { 
            QuestionnaireAction qa = new QuestionnaireAction();
            try {
                Questionnaire form =  qa.getFullQuest(f_id,null);
                uData.addOrReplaceQuest(form);
                if (form!=null) session.setAttribute("q",form);
            }catch (SQLException e) {
                message = e.getMessage();
            }
        }
        
        sessionInfos.setMessage(message);
        
    }
    
}
