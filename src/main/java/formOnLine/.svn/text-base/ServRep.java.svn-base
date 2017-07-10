package formOnLine;


import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import formOnLine.actions.QuestionnaireAction;
import formOnLine.actions.ReponseAction;
import formOnLine.actions.SubmitFormAction;
import formOnLine.actions.UserActionFactory;
import formOnLine.msBeans.Questionnaire;
import formOnLine.msBeans.Reponse;
import formOnLine.msBeans.SubmitForm;
import formOnLine.msBeans.UserData;

/**
 * @version 1.0
 * @author
 */
public class ServRep extends HttpServlet implements Servlet
{
    
    
    private static final long serialVersionUID = 1L;
    //private static final String MSG_CAPTCHA =         "Les caractères saisis ne correspondent pas à l'image" ;
    
    private static final String MSG_BADEXT = 
        "Extention non autorisée" ;
    private static final String MSG_PB_UPLOAD = 
        "Un problème est survenu lors de l'envoi du fichier sur le serveur, veuillez réessayer." ;
    static protected Logger traceLog = Logger.getLogger("FOLE");
    static protected String dirUploadedFiles = "/files/upload";
    
    
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException {
        doPost(req,resp);
    }
    
    
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException {
        
        HttpSession session = req.getSession(true);
        
        //SessionInfos sessionInfos = (SessionInfos)session.getAttribute(AbstractServlet.ID_SESSION_INFOS);
        QuestionnaireAction qa = new QuestionnaireAction(); 
        SubmitFormAction sfa = new SubmitFormAction();
        
        UserData userData = (UserData)session.getAttribute(ServControl.USERDATA);
        
        
        SubmitForm sf = null;
        String message = null;
        boolean isUpload = false;
        
        //récup questionnaire
        Questionnaire form = (Questionnaire)session.getAttribute("q");
        
        
        
        try {
            
//          Create a factory for disk-based file items
            FileItemFactory factory = new DiskFileItemFactory();
//          Create a new file upload handler
            ServletFileUpload upload = new ServletFileUpload(factory);
//          Parse the request : list of FileItems
            List items = upload.parseRequest(req);
            
            
            // DEBUT TRAITEMENT REPONSE     
            
            
            
            // recherche des  SID pour commencer
            int sid = -1;
            int sid_parent = -2;
            int fid = form.getId(); 
                    
            Iterator iter = items.iterator();
            while (iter.hasNext()) {
                FileItem item = (FileItem) iter.next();
                
                if (item.isFormField() && item.getFieldName().equals("sid")) {
                    try {
                        sid = Integer.parseInt(item.getString());
                    } catch (NumberFormatException e) {}
                }
                    
                
                if (item.isFormField() && item.getFieldName().equals("s_id_parent")) {
                    try {
                        sid_parent = Integer.parseInt(item.getString());
                    } catch (NumberFormatException e) {}
                }
                
                if (item.isFormField() && item.getFieldName().equals("f_id")) {
                    try {
                        fid = Integer.parseInt(item.getString());
                    } catch (NumberFormatException e) {}
                }
                
            }
            
            
          //reinit du Q courant de la session si différent
            if (session.getAttribute("q")!=null) {
                form = (Questionnaire)session.getAttribute("q");
                if (form.getId()!= fid ) form=null;
            }
            
            //recherche dans le userdata
            if (form==null && userData.getQuest(fid)!=null) {
                form=userData.getQuest(fid);
            }
            
            // si le questionnaire n'a pas été chargé avec ses questions, il y a un pb ?
            if (form!=null && form.getGroupes().size()==0 ) {
                message = "erreur init questionnaire !";
                session.setAttribute(ServControl.MESSAGE, message);
            }
            
            
            // cas ou le f_id n'est plus le même (navigation multi-onglets)
            if (fid != form.getId()) {
                form = qa.getFullQuest(fid);
            }
            
            // préparation du nom du répertoire en cas d'upload
            String uDir = String.valueOf(sid);
            
            //      nouveau formulaire ou modif existant ?
            boolean newForm = true;  
            if (sid > -1) {
                newForm = false;
                
                sf = userData.getRep(sid);
                if (sf == null) sf = sfa.selectBySId(sid);
                
                if (sid_parent >-2) sf.setS_id_parent(sid_parent);
                
            } else {
                newForm = true; 
                //sid_parent =  Integer.parseInt(req.getParameter("s_id_parent"));
                
                sf = new SubmitForm();
                sf.setF_id(form.getId());
                sf.setS_id_parent(sid_parent);
                
            }
            
            
            /* CONTROLE DES DROITS DE CREATION / MODIF
             *  - si le role PUBLIC : accès aux submitform publiques hors connexion
             *  en création (f_connected = 0)
             *  - si le role PUBLIC + connecté : accès aux submitform fils ou père
             *  en modif (f_connected <= 1)
             *  - si le role CONSULT : pas d'accès en modif
             *  - si le role GESTION : accès en modif aux submitform, hors admin
             *  (f_connected <= 1), hors création
             *  - si le role ADMIN : accès en lecture à tous les submitform
             *  (f_connected <= 3)
             * 
             */            
            if ( sf != null) /* &&
            (
            (sessionInfos.getActionToDo()== ServControl.RMAJ
            || sessionInfos.getActionToDo()== ServControl.RDEL 
            || sessionInfos.getActionToDo()== ServControl.QUEST)        
            
            && (
            (userData != null && !userData.isConnected() 
            && sessionInfos.getActionToDo()== ServControl.QUEST
            && form!=null && form.getF_connected()==0 
            && userData.getRole() == SessionInfos.ROLE_PUBLIC)
            ||
            (userData != null && userData.isConnected()
            && form!=null && form.getF_connected()<=1      
            && (sf==null || userData.isItsOwnData(sf.getS_id()))
            && userData.getRole() == SessionInfos.ROLE_PUBLIC ) 
            ||
            (form!=null && form.getF_connected()<=1      
            && userData != null 
            && userData.getRole() == SessionInfos.ROLE_GESTION) 
            ))
            
            */ 
            {
//              nouvelle instance de questionnaire pour récupérer les réponses
                SubmitForm sfNew = sf.copy();
                sfNew.setReponses(new LinkedList<Reponse>());
                
                iter = items.iterator();
                if (sid == -1) uDir = BasicTools.getPwd(true,true,true,30);
                
                
                while (iter.hasNext()) {
                    FileItem item = (FileItem) iter.next();
                    
                    String sParam = item.getFieldName();
                    
                    String sVal = "";
                    int rVal = 0;
                    
                    if (item.isFormField()) {
                        sVal = item.getString("UTF-8");
                    } else {
                        // appel de FileNameUtils pour nettoyer le chemin client 
                        // envoyé par certains navigateurs
                        sVal = FilenameUtils.getName(item.getName());
                    }
                    
                    
                    
                    if (sParam.length() > 3 && sParam.startsWith("fol_")) {
                        
                        String sType = sParam.substring(4, 7);
                        int gid = Integer.parseInt(sParam.substring(7, 12));
                        int qid = Integer.parseInt(sParam.substring(13, 18));
                        int pid = 0;
                        
                        if (sType.equals("rag"))  {
                            pid = Integer.parseInt(sVal);
                        } else {
                            pid = Integer.parseInt(sParam.substring(19,24));
                        }
                        
                        // si listes (choix exclusif/multiple, liste forms) les propositions sélectionnées prennent la valeur 1
                        if (sType.equals("rag") || sType.equals("chg") || sType.equals("lfg")) sVal = "1";
                        
                        if (sType.equals("lfg")) {
                            try {
                                rVal = Integer.parseInt(sParam.substring(25));
                            } catch (Exception e) {}
                        }
                        
                        
                        if (!item.isFormField()) {
                            
                            String oldSVal = sf.getPropVal(pid);
                            
                            
                            if (Controls.isBlank(sVal)) {
                                // pas de nouveau fichier
                                sVal = oldSVal ;
                            } else if (!BasicTools.checkFileExtention(sVal, 
                                    InitServlet.getExtensions())){
                                sVal = "";
                                message = MSG_BADEXT;
                            } else {
                                
                                // upload
                                String newFilePath =  
                                    //session.getServletContext().getRealPath("/") +
                                    //dirUploadedFiles + "/" +
                                    InitServlet.getFileUploadDir() + File.separator +
                                    uDir ;
                                
                                File dirUploadedFile = new File(newFilePath);
                                if (!dirUploadedFile.exists()) dirUploadedFile.mkdir();
                                
                                // remplacement du caractère "’" en "'" (pour pb compatibilité stockage sql et chemins linux)
                                sVal = sVal.replaceAll("’", "'");
                                
                                File uploadedFile = new File(newFilePath + File.separator + sVal);
                                
                                // tentative de création
                                traceLog.debug("Tentative Upload : " + newFilePath + File.separator + sVal);
                                uploadedFile.createNewFile();
                                
                                item.write(uploadedFile);
                                isUpload = true;
                                
                                // contrôle taille du fichier final
                                //if ( !uploadedFile.exists() || new FileInputStream(uploadedFile).read()==-1)   
                                //    message = MSG_PB_UPLOAD;
                                
                                
                                // suppr old
                                if (!Controls.isBlank(oldSVal) && message==null && !oldSVal.equals(sVal)) {
                                    String oldFilePath = newFilePath + File.separator + oldSVal ;
                                    File oldFile = new File (oldFilePath);
                                    if (oldFile.exists()) oldFile.delete();
                                }
                            }
                            
                        }
                        

                        
                        // maj réponse
                        if (sVal != null )//&& !sVal.equals(""))
                        {
                            sVal = sVal.replaceAll("\"","'");
                            sVal = BasicTools.cleanWordChars(sVal);
                            
                            form.setQuestValById(gid, qid, pid, sVal, rVal);
                            
                            Reponse r = new Reponse(sf.getS_id(), pid, sVal, rVal);
                            
                            //ra.insert(r);
                            sfNew.addReponse(r);
                        }
                    }
                }
                
                // contrôle unicité
                if (Controls.isBlank(message)) message = qa.checkMandatoryUnique(form, sf.getS_id());
                
                // TESTS SPECIFIQUES
                if (Controls.isBlank(message)) {
                    if (newForm) {
                        message = new UserActionFactory().getUserAction("ContextUserAction").testBeforeCreateSf(sfNew, userData);
                    } else {
                        sfNew.setS_id(sf.getS_id());
                        sfNew.setS_id_parent(sf.getS_id_parent());
                        message = new UserActionFactory().getUserAction("ContextUserAction").testBeforeUpdateSf(sfNew, userData);
                    }
                }
                
                
                
                
                // captcha
                if (newForm) {
                    /*
                     String userCaptcha = req.getParameter("captcha");
                     Captcha captcha = (Captcha) session.getAttribute(Captcha.NAME);
                     if ( userCaptcha != null ) {
                     if (captcha == null || !captcha.isCorrect(userCaptcha)) message = MSG_CAPTCHA;
                     }
                     if (captcha != null) session.removeAttribute(Captcha.NAME);
                     */
                    /*
                     String sessionCaptcha = (String)session.getAttribute(nl.captcha.servlet.Constants.SIMPLE_CAPCHA_SESSION_KEY);
                     String userCaptcha = req.getParameter("captcha");
                     if (sessionCaptcha != null
                     && userCaptcha != null
                     && !sessionCaptcha.equals(userCaptcha)) 
                     message = MSG_CAPTCHA;*/
                }
                
                // Update slmt si on n'a pas de doublons ni de pb de captcha !
                if (Controls.isBlank(message)) {
                    if (newForm) {
                        SubmitForm insertSf = sfa.newSubForm(sf.getF_id(),sf.getS_id_parent(), userData.getLogin(),0);
                        sf.setS_id(insertSf.getS_id());
                        sf.setPwd(insertSf.getPwd());
                        sf.setS_date(insertSf.getS_date());
                        sf.setS_lock(insertSf.getLock());
                        sf.setTitre(insertSf.getTitre());
                    }    
                    
                    if (isUpload) {
                        // renommage répertoire upload
                        File oldUplDir = new File(InitServlet.getFileUploadDir() + File.separator + uDir);
                        File newUplDir = new File(InitServlet.getFileUploadDir() + File.separator + String.valueOf(sf.getS_id()));
                        
                        if (oldUplDir.exists()) {
                            boolean success = BasicTools.moveDirectory(oldUplDir,newUplDir);
                            if (!success)  message = "Probleme renommage repertoire";
                        }
                    }
                    
                    // mise à jour
                    sfa.updateSubForm(sf, sfNew);
                    
                    // envoi du mail  si le t_id_mail_on_create (= n° du template du mail à envoyer) est renseigné
                    if (newForm && form.getT_id_mail_on_create()>0) {
                        String diffAdr = null;
                        if (Controls.isBlank(sf.getPropVal(form.getP_id_mail()) )
                                && sf.getS_id_parent()>0) {
                            try {
//                              on recherche l'adresse email dans le submitform parent
                                diffAdr = new ReponseAction().selectOne(form.getP_id_mail(), sf.getS_id_parent()).getSVal();
                            } catch (Exception e2) {}
                        }
                        
                        message = sfa.sendMailOnCreate(sf,form,diffAdr, userData);
                        if (message !=null) req.getSession().setAttribute(ServControl.MESSAGE, message);
                    }
                    
                    // actions complémentaires
                    if (newForm) {
                        message = new UserActionFactory().getUserAction("ContextUserAction").doAfterCreateSf(sf, userData);
                        
                        // on envoie juste le message pour affichage, pas de blocage sur ces actions en post
                        session.setAttribute(ServControl.MESSAGE, message);
                        message=null;
                        
                    } else {
                        message = new UserActionFactory().getUserAction("ContextUserAction").doAfterUpdateSf(sf, userData);
                        
                        // on envoie juste le message pour affichage, pas de blocage sur ces actions en post
                        session.setAttribute(ServControl.MESSAGE, message);
                        message=null;
                    }
                    
                    
                } else {
                    // On retourne en modif si doublons avec un message
                
                    req.setAttribute(ServControl.ACTION, "RMAJ");
                    req.setAttribute(ServControl.MESSAGE, message);
                    sf = sfNew;
                }
                
                // récup du titre
                sf.setTitre(sf.getPropVal(form.getP_id_titre()));
                
                if (form != null)
                    form.resetFullQuest();
                form.setFullQuest(sf);
                session.setAttribute("q", form);
                if (sfNew != null)
                    session.setAttribute("sf", sf);
                
                if (Controls.isBlank(message)) {
                    userData.addOrReplaceRep(sf);
                    userData.addOrReplaceQuest(form);
                    
                    session.setAttribute(ServControl.USERDATA, userData);
                }
                
            } else {
                // pas d'autorisation
                session.setAttribute(ServControl.MESSAGE, "Droits insuffisants");
                session.removeAttribute("q");
                session.removeAttribute("sf");
                sf=null;
                newForm=false;
            }
            
        }
        catch (Exception e)
        {
            message = e.getMessage(); //erreur SQL
            session.setAttribute(ServControl.MESSAGE, message);
        }
        
    }
    
}