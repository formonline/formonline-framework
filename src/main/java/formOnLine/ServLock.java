package formOnLine;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.triangle.lightfw.AbstractServlet;
import com.triangle.lightfw.BasicType;
import com.triangle.lightfw.SessionInfos;
import com.triangle.lightfw.ValueBeanList;

import formOnLine.actions.QuestionnaireAction;
import formOnLine.actions.SubmitFormAction;
import formOnLine.actions.UserActionFactory;
import formOnLine.msBeans.Questionnaire;
import formOnLine.msBeans.SubmitForm;
import formOnLine.msBeans.UserData;

/**
 * @version     1.0
 * @author
 */
public class ServLock extends HttpServlet implements Servlet {

    private static final long serialVersionUID = 1L;
    private static final boolean lockSubmitformParent = false; // configuration par défaut : si TRUE, au lock du fils, on locke le père...

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req,resp);
    }


    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(true);
        SubmitFormAction sfa = new SubmitFormAction();
        String message= ""; 

        int sid =-1;
        int action = -1;
        boolean lockParent = lockSubmitformParent;
        
        if (req.getParameter("lockParent") != null) lockParent = true;

        SessionInfos sessionInfos =  (SessionInfos)session.getAttribute( AbstractServlet.ID_SESSION_INFOS);
        action = sessionInfos.getActionToDo();

        UserData uData = (UserData)session.getAttribute(ServControl.USERDATA);
        SubmitForm sf = null;
        SubmitForm sfp = null;

        try {

            switch (action) {

            case ServControl.LOCK: // lock d'un formulaire (et de son père si besoin)

                if (!Controls.isBlank(req.getParameter("sid"))) {

                    sid=Integer.parseInt( req.getParameter("sid"));
                    sf= uData.getRep(sid);

                    String msgMandatory = null;
                    String msgValues = null;
                    String diffAdr = null;

                    if (sf!=null && Controls.isBlank(message)) {


                        /* lock du formulaire parent si besoin 
                         * (... et si on est dans le mode ou la demande de lock 
                         * fils entraîne une demande de lock du parent)
                         * (lockSubmitformParent = true) */



                        int sid_parent = sf.getS_id_parent();
                        sfp=uData.getRep(sid_parent);

                        if ( lockParent 
                                && sid_parent>0 
                                && Controls.isBlank(message)) { 

                            // réinit complet du formulaire pour contrôle
                            // (il peut n'avoir que les valeurs de reponses de type "à initialiser")

                            sfp=sfa.selectBySidAndPwd(sfp.getS_id(),null,false);

                            if (sfp!= null && !sfp.isLocked()) {
                                //  si formulaire parent  incomplet, on bloque
                                Questionnaire qp = uData.getQuest(sfp.getF_id());
                                if (qp==null) {
                                    qp= new QuestionnaireAction().getFullQuest(sfp.getF_id());
                                } 

                                qp.setFullQuest(sfp); 

                                msgMandatory = qp.checkMandatoryAlert();
                                if (!Controls.isBlank(msgMandatory)) message = msgMandatory;

                                msgValues = qp.checkValues();
                                if (!Controls.isBlank(msgValues)) message = msgValues;
                            }



                        }

                        if (Controls.isBlank(message)) {

                            // lock formulaire fils
                            Questionnaire q = uData.getQuest(sf.getF_id());
                            if (q==null) q= new QuestionnaireAction().getFullQuest(sf.getF_id());

                            // réinit du formulaire si vide 
                            // => ne fonctionne plus avec les valeurs de reponses de type "à initialiser"
                            // => à remplacer par un "selectBySIdandPwd" de uniquement les champs obligatoires
                            // if (sf.getReponses().size()==0)

                            sf=sfa.selectBySidAndPwd(sf.getS_id(),null,false);

                            q.resetFullQuest();
                            q.setFullQuest(sf); 

                            // si formulaire incomplet, on bloque
                            msgMandatory = q.checkMandatoryAlert();
                            if (!Controls.isBlank(msgMandatory)) message = msgMandatory;

                            msgValues = q.checkValues();
                            if (!Controls.isBlank(msgValues)) message = msgValues;

                            if (Controls.isBlank(message)) {
                                // lock parent
                                if (sfp!=null &&  !sfp.isLocked() && lockParent) {
                                    sfa.lock(sfp.getS_id(), uData.getLogin());
                                    sfp.setS_lock(1);
                                    sfp.setS_date(BasicType.getTodaysDateIso(true));
                                    uData.addOrReplaceRep(sfp);

                                    Questionnaire qp = uData.getQuest(sfp.getF_id()) ;
                                    if (qp!=null) {
                                        qp.setS_lock(1);
                                        uData.addOrReplaceQuest(qp);
                                    }

                                    message = new UserActionFactory().getUserAction("ContextUserAction").doAfterLockSf(sfp,uData);
                                }

                                // lock du formulaire fils
                                sfa.lock(sid, uData.getLogin());

                                sf.setS_lock(1);
                                sf.setS_date(BasicType.getTodaysDateIso(true));
                                uData.addOrReplaceRep(sf);

                                session.setAttribute("q",q) ;
                                session.setAttribute("sf",sf) ;

                                //mail
                                if (q.getT_id_mail_on_lock()>0) {

                                    int mailPid = q.getP_id_mail();
                                    if (Controls.isBlank(sf.getPropVal(mailPid)) && sfp!= null) {
                                        diffAdr = sfp.getPropVal(mailPid); // on va chercher l'adr mail du parent 
                                    }

                                    try {
                                        sfa.sendMailOnLock(sf,q,diffAdr, uData);
                                    } catch (Exception e) { message = e.getMessage(); }
                                }

                                message = new UserActionFactory().getUserAction("ContextUserAction").doAfterLockSf(sf,uData);

                            } 
                        }
                    }

                }

                if (!Controls.isBlank(message)) {
                    session.setAttribute(ServControl.MESSAGE,message);

                }
                break;

                
            case ServControl.UNLOCK: 
                
                /***********************
                 * CONTROLE DES DROITS *
                 ***********************/
                
                boolean access = false;
                
                if (!Controls.isBlank(req.getParameter("sid"))) {
                    sid=Integer.parseInt( req.getParameter("sid"));
                    sf= uData.getRep(sid);
                }
                
                Questionnaire quest = uData.getQuest(sf.getF_id());
                if (quest==null) quest= new QuestionnaireAction().getFullQuest(sf.getF_id());

                message = quest.checkAccess(sessionInfos.getActionToDo(), uData, sf, sid, true, true);
                if (message == null) access = true;
                
                
                if ( access ) {
                    

                    sfa.unLock(sid, uData.getLogin());
                    
                    if (sf!=null) {
                        sf.setS_lock(0);
                        sf.setS_date(BasicType.getTodaysDateIso(true));
                        uData.addOrReplaceRep(sf);
                    }

                    message = new UserActionFactory().getUserAction("ContextUserAction").doAfterUnlockSf(sf,uData);
                }
                break;
            
                
            case ServControl.LOCKALL: // LOCK de tous les submitform de la session (avec filtre sur F_id et parent)

                ValueBeanList vbl = null;
                int s_id_parent = -1;
                int f_id = -1;

                if (!Controls.isBlank(req.getParameter("s_id_parent"))) 
                    s_id_parent = Integer.parseInt(req.getParameter("s_id_parent"));

                if (!Controls.isBlank(req.getParameter("f_id"))) 
                    f_id = Integer.parseInt(req.getParameter("f_id"));

                //if (f_id < 0 && s_id_parent < 0) vbl = uData.getListRep();
                if (f_id > 0 && s_id_parent < 0) vbl = uData.getListRepByFid(f_id);
                if (f_id < 0 && s_id_parent > 0) vbl = uData.getListRepByFidAndSidParentAndSlock(f_id, s_id_parent, 0);
                if (f_id > 0 && s_id_parent > 0) vbl = uData.getListRepByFidAndSidParentAndSlock(f_id, s_id_parent, 0);

                // si le formulaire parent n'st pas encore locké on l'ajoute dans la liste 
                sfp = uData.getRep(s_id_parent);
                if (!sfp.isLocked()) vbl.add(sfp);

                // on ajoute aussi le grand père si nécessaire
                if (lockParent) {
                    SubmitForm sfgp = uData.getRep(sfp.getS_id_parent());
                    if (sfgp != null && !sfgp.isLocked()) vbl.add(sfgp);
                }

                // première boucle de contrôle
                for (int i=0; vbl!=null && i<vbl.size()  && Controls.isBlank(message);i++) {
                    sf = (SubmitForm)vbl.get(i);
                    if (!sf.isLocked()  && Controls.isBlank(message)) {

                        Questionnaire q = uData.getQuest(sf.getF_id());
                        if (q==null) q= new QuestionnaireAction().getFullQuest(sf.getF_id());

                        // réinit cpmplet du formulaire pour contrôles
                        sf=sfa.selectBySidAndPwd(sf.getS_id(),null,false);

                        q.setFullQuest(sf);

                        // si formulaire incomplet, on bloque
                        String msgMandatory = q.checkMandatoryAlert(true);
                        if ( !Controls.isBlank(msgMandatory)) {
                            message = msgMandatory;
                            break;
                        }
                        String msgValues = q.checkValues();
                        if (!Controls.isBlank(msgValues)) {
                            message = msgValues;
                            break;
                        }


                    }
                }

                // seconde boucle pour le  lock
                for (int i=0; vbl!=null && i<vbl.size()  && Controls.isBlank(message);i++) {
                    sf = (SubmitForm)vbl.get(i);
                    if (!sf.isLocked()  && Controls.isBlank(message)) {

                        Questionnaire q = uData.getQuest(sf.getF_id());
                        if (q==null) q= new QuestionnaireAction().getFullQuest(sf.getF_id());

                        sfa.lock(sf.getS_id(), uData.getLogin());
                        sf.setS_lock(1);
                        sf.setS_date(BasicType.getTodaysDateIso(true));
                        uData.addOrReplaceRep(sf);

                        try {
                            if (q.getT_id_mail_on_lock()>0) {
                                String diffAdr = null;
                                int mailPid = q.getP_id_mail();
                                if (Controls.isBlank(sf.getPropVal(mailPid))  && sfp!= null ) {
                                    SubmitForm sfparent = uData.getRep(sf.getS_id_parent());
                                 // on va chercher l'adr mail du parent (il faut que la proposition soit en mode InitVal
                                    if (sfparent !=null) diffAdr = sfparent.getPropVal(mailPid); 
                                }
                                sfa.sendMailOnLock(sf,q, diffAdr, uData);
                            }
                        } catch (Exception e) { message = e.getMessage(); }

                        message = new UserActionFactory().getUserAction("ContextUserAction").doAfterLockSf(sf,uData);

                    }
                }

                if (!Controls.isBlank(message)) 
                    session.setAttribute(ServControl.MESSAGE,message);

                session.setAttribute(ServControl.USERDATA,uData);
                break;

            } //switch

        } catch (Exception e ) { 
            message = e.getMessage(); //erreur SQL
            if (Controls.isBlank(message)) session.setAttribute(ServControl.MESSAGE,message);
        }




    }

}
