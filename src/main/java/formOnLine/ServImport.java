package formOnLine;



import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.apache.xerces.parsers.DOMParser;

import au.com.bytecode.opencsv.CSVReader;

import com.triangle.lightfw.AbstractServlet;
import com.triangle.lightfw.BasicType;
import com.triangle.lightfw.SessionInfos;
import com.triangle.lightfw.ValueBeanList;

import formOnLine.actions.QuestionnaireAction;
import formOnLine.actions.ReponseAction;
import formOnLine.actions.SubmitFormAction;
import formOnLine.actions.TemplateAction;
import formOnLine.actions.UserActionFactory;
import formOnLine.io.XmlReader;
import formOnLine.msBeans.Questionnaire;
import formOnLine.msBeans.Reponse;
import formOnLine.msBeans.SubmitForm;
import formOnLine.msBeans.Template;
import formOnLine.msBeans.UserData;

public class ServImport extends HttpServlet implements Servlet
{
    
    private static final long serialVersionUID = 1L;
    
    private static final int ERREUR = 0;
    private static final int XML   = 1;  
    private static final int CSV   = 2;  
    private static final int LOT   = 3;
    
    public static final String startTag = "<%";
    public static final String endTag = "%>";
    
    // répertoire source de l'import
    private static final String fileDir= InitServlet.getFileImportDir();
    
    
    private static Logger traceLog = Logger.getLogger("FOLE");
    
    
    /**
     * init du code action 
     *  
     */
    private int init(String s)
    {
        int i = ERREUR;
        if (s != null) {
            if (s.equals("XML")) i = XML;
            if (s.equals("CSV")) i = CSV;
            if (s.equals("LOT")) i = LOT;
        }
        return i;
    }
    
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException {
        doPost(req,resp);
    }
    
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException
    {
        
        SubmitFormAction sfa = new SubmitFormAction();
        TemplateAction ta = new TemplateAction();
        
        
        /***********************
         * CONTROLE DES DROITS *
         ***********************/
        
        UserData userData = (UserData)req.getSession().getAttribute(ServControl.USERDATA);
        
        
        /* contrôle droits ADMIN ONLY */
        if (userData == null 
                || userData.getRole() < SessionInfos.ROLE_ADMIN )
            return;
        
        SessionInfos sessionInfos = (SessionInfos)req.getSession().getAttribute(AbstractServlet.ID_SESSION_INFOS);
        
        
        String message = null;
        Template t = null;
        int action = 0;
        int nbImp = 0;
        int idTestImport = 999999999;
        boolean isTestImport = false;
        boolean isRepOnly = false;
        
        String templateType = null;
        String file = null;
        String templateId = null;
        
        String testImport = null;
        String repOnly    = null;
        
        String currentStartTag = startTag;
        String currentEndTag = endTag;
       
        String fileName = null;
        String uploadDirPath = null;
        
        String datDeb = null;
        String datFin = null;
        int lockedOnly = -1;
        int sqlFilterId = -1;
        String sqlValId = null;
        
       
        
        try {
            
//          Create a factory for disk-based file items
            FileItemFactory factory = new DiskFileItemFactory();
//          Create a new file upload handler
            ServletFileUpload upload = new ServletFileUpload(factory);
//          Parse the request : list of FileItems
            List items = upload.parseRequest(req);
            
            Iterator iter = items.iterator();
            while (iter.hasNext()) {
                FileItem item = (FileItem) iter.next();
                
                if (item.isFormField() && item.getFieldName().equals("templateType")) 
                    templateType = item.getString("UTF-8");
        
                if (item.isFormField() && item.getFieldName().equals("file")) 
                    file = item.getString("UTF-8");
                
                if (item.isFormField() && item.getFieldName().equals("templateId")) 
                    templateId = item.getString("UTF-8");
                
                if (item.isFormField() && item.getFieldName().equals("test")) 
                    testImport = item.getString("UTF-8");
                
                if (item.isFormField() && item.getFieldName().equals("repOnly")) 
                    repOnly    = item.getString("UTF-8");
                    
                if (testImport != null) isTestImport = testImport.equals("on");
                if (repOnly != null) isRepOnly = repOnly.equals("on");
                
                if (item.isFormField() && item.getFieldName().equals("lockedOnly")) 
                    lockedOnly    = 1;
                
                
                if (item.isFormField() && item.getFieldName().equals("sqlFilterId")) {
                    try {
                        sqlFilterId = Integer.parseInt(item.getString("UTF-8"));
                    } catch (NumberFormatException e) {
                        sqlFilterId = -1;
                    }
                }
                
                if (item.isFormField() && item.getFieldName().equals("sqlValId")) 
                    sqlValId = item.getString("UTF-8");

                if (item.isFormField() && item.getFieldName().equals("date_debut")) 
                    datDeb = item.getString("UTF-8");
                
                if (item.isFormField() && item.getFieldName().equals("date_fin"))
                    datFin = item.getString("UTF-8");
                
                
                
                if (templateType != null) action = init(templateType); 
                
                
                // traitement de l'upload
                if (!item.isFormField()) {
                    
                    // appel de FileNameUtils pour nettoyer le chemin client 
                    // envoyé par certains navigateurs
                    fileName = FilenameUtils.getName(item.getName());
                    
                    // test extension
                    // ...
                    
                    // repertoire pour l'upload
                    uploadDirPath =  InitServlet.getFileUploadDir() + File.separator + "TMP" ;
                    
                    File dirUploadedFile = new File(uploadDirPath);
                    if (!dirUploadedFile.exists()) dirUploadedFile.mkdir();
                    
                    // remplacement du caractère "’" en "'" (pour pb compatibilité stockage sql et chemins linux)
                    fileName = fileName.replaceAll("’", "'");
                    
                    File uploadedFile = new File(uploadDirPath + File.separator + fileName);
                    
                    // tentative de création
                    traceLog.debug("Tentative Upload : " + uploadDirPath + File.separator + fileName);
                    uploadedFile.createNewFile();
                    
                    item.write(uploadedFile);
                    
                    
                }
                
            }
        
        // Traitement  
        switch (action)
        {
        case XML:
//          parse
            
            /* contrôle droits : admin only */
            if (userData == null 
                    || userData.getRole() < SessionInfos.ROLE_ADMIN )
                return;
            
            
                DOMParser p = new DOMParser();
                p.parse(file);
                
                XmlReader xmlr = new XmlReader();
                xmlr.parse(p.getDocument(), null, false);
            
            
            break;
            
            
        // imports CSV   
        case CSV:
            
            
            traceLog.info("DEBUT TRAITEMENT ........ " + BasicType.getCurentTime(null));
           
                // template
                t = ta.selectOne(Integer.parseInt(templateId));
                
                if (t.getContent().indexOf("{%")>=0) currentStartTag = "{%";
                
                
                String tab[] = t.getContent().split(";");
                int pidTab[] = new int[tab.length];
                int qidTab[] = new int[tab.length];
                int numColSid = -1;
                int numColSidParent = -1;
                int numColPwd = -1;
                int numColLocked = -1;
                int numColDate = -1;
                
                
                
                for (int i=0; i< tab.length ;i++) {
                    pidTab[i]= -1;
                    qidTab[i]= -1;
                    if (BasicTools.getIntFrom5DigitFormatedInputName(tab[i],"F")==t.getFid()) {
                        pidTab[i] =  BasicTools.getIntFrom5DigitFormatedInputName(tab[i],"P");
                        qidTab[i] =  BasicTools.getIntFrom5DigitFormatedInputName(tab[i],"Q");
                    }
                    if (tab[i].startsWith(currentStartTag+"SID")) numColSid=i;
                    if (tab[i].startsWith(currentStartTag+"PARENT")) numColSidParent=i;
                    if (tab[i].startsWith(currentStartTag+"PWD")) numColPwd=i;
                    if (tab[i].startsWith(currentStartTag+"LOCKED")) numColLocked=i;
                    if (tab[i].startsWith(currentStartTag+"SDATE")) numColDate= i;
                }
                
                
                // parcours CSV
                CSVReader reader = new CSVReader(new FileReader(uploadDirPath + File.separator + fileName),';');
                
                String [] nextLine ;
                
                
                // premiere ligne de titres ?
                reader.readNext();
                String oldPwd="";
                
                while ((nextLine = reader.readNext()) != null) {
                    
                    // test ligne vide
                    String collapse="";
                    for (int itab=0; itab<nextLine.length; itab++) collapse +=nextLine[itab].trim();
                    if ("".equals(collapse)) continue;
                    
                    
                    int sid = -1;
                    int sid_parent = -1;
                    String pwd = "";
                    String sDate = null;
                    int locked = 0;
                    
                    // init du submitform
                    SubmitForm sf = new SubmitForm();
                    
                    if (numColSid > -1 ) {
                        try {sid = Integer.parseInt(nextLine[numColSid]); } 
                        catch (Exception e) {}
                    }
                    
                    if (numColSidParent > -1) {
                        try {sid_parent = Integer.parseInt(nextLine[numColSidParent]);
                        } catch (Exception e) {}
                    }
                    if (numColLocked > -1) {
                        try {locked = Integer.parseInt(nextLine[numColLocked]);
                        } catch (Exception e) {}
                    }
                    if (numColDate > -1) {
                        try {sDate = nextLine[numColDate];
                        } catch (Exception e) {}
                    }
                    if (numColPwd > -1) {
                        try {pwd = nextLine[numColPwd];
                        } catch (Exception e) {}
                    } else {
                        pwd = BasicTools.getPwd(true,false,true,6);    
                        while (pwd.equals(oldPwd)) {
                            pwd = BasicTools.getPwd(true,false,true,6); 
                        }
                        oldPwd = pwd;
                    }
                    
                    
                    sf.setS_id(sid);
                    sf.setF_id(t.getFid());
                    sf.setPwd(pwd);
                    sf.setS_id_parent(sid_parent);
                    sf.setS_lock(locked);
                    sf.setS_login_maj(userData.getLogin());
                    
                    if (sDate != null ) {
                        sf.setS_date(sDate);
                    } else {
                        sf.setS_date(BasicType.getTodaysDateIso());
                    }
                    
                    // insert des réponses
                    for (int i=0; i< pidTab.length ;i++) {
                        if (pidTab[i]>-1 && i<nextLine.length) {
                            Reponse r = new Reponse(sid,pidTab[i],nextLine[i],0);
                            
                            // traitement des retours ligne (exportés sous la forme / /)
                            if (r.getSVal().indexOf("//")>0)
                                r.setSVal( r.getSVal().replaceAll("//", "\r\n"));
                            if (r.getSVal().indexOf("/ /")>0)
                                r.setSVal( r.getSVal().replaceAll("/ /", "\r\n"));
                            if (r.getSVal().indexOf("/  /")>0)
                                r.setSVal( r.getSVal().replaceAll("/  /", "\r\n"));
                            
                            
                            sf.addReponse(r);
                            
                        } //if
                        
                    }   // for
                    
                    
                    if (!isTestImport) {
                        // insertion en base
                        sfa.insertFull(sf, isRepOnly);
                        nbImp ++;
                    } else {
                        // TEST : on met le formulaire de test en mémoire uniquement
                        sf.setS_id(idTestImport);
                        req.getSession().setAttribute("sf", sf);
                        
                        QuestionnaireAction qa = new QuestionnaireAction();
                        Questionnaire q = qa.getFullQuest(sf.getF_id(), sf);
                        
                        req.getSession().setAttribute("q", q);
                        
                        /*userData.addOrReplaceQuest(q);
                        userData.addOrReplaceRep(sf);*/
                        
                        break;
                    }
                    
                }// while     
                
                
                traceLog.info("........... FIN TRAITEMENT");
                
                // suppression fichier d'import
                File uploadedFile = new File(uploadDirPath + File.separator + fileName);
                if (uploadedFile.exists()) uploadedFile.delete();
                
                // import reussi
                if (message==null) {
                    if (isTestImport) {
                        message = "Import test id : <a href='rdetail.jsp'>" + idTestImport+"</a>" ;
                    } else {
                        message = "Import OK : " + nbImp + " ligne(s) importée(s)."; 
                    }
                } 
                
           
            
            break;
        
            
        case LOT:  // traitement de lot : mise à jour d'une liste de formulaires, ou lancement de procédures stockées..
            
            traceLog.info("DEBUT TRAITEMENT LOT ........ "+BasicType.getCurentTime(null));
    
                 
         // template
            t = ta.selectOne(Integer.parseInt(templateId));
            
//          test dates saisies
            if (datDeb !=null  && datFin!=null)
                message=Controls.checkDates(datDeb,datFin);
            if (message!=null) break;
      
                
                datDeb = BasicType.parseDateIsoFromLocal(datDeb,null);
                datFin = BasicType.parseDateIsoFromLocal(datFin,null);
                
                // récupération du filtre SQL
                Template tmplSqlFilter = null;
                String sqlFilter = null;
                
                if (sqlFilterId>=0) {
                    tmplSqlFilter = ta.selectOne(sqlFilterId);
                    sqlFilter = tmplSqlFilter.getContent();
                    
                    // recherche et remplacement de la chaîne {%id%}
                    if (sqlValId != null && sqlFilter!=null) 
                        sqlFilter= sqlFilter.replaceAll("\\{%id%\\}", sqlValId);
                }

                
                // ajout de filtre : la donnée ne doit pas avoir déjà été mise à jour pour cette liste
                // ** retiré , compléter directement le filtre sql...
                /* String testFilter = (sqlFilter!=null?sqlFilter:"") +
                    " and exists (select * from reponse where reponse.s_id = submitform.s_id "+
                    " and reponse.p_id=" + pidToSet + " and (r_text is null or length(trim(r_text)) =0) )";*/
                
                
                // récupération de la liste des submitform pour le lot
                ValueBeanList list = sfa.selectAllByFormId(t.getFid(), -1, lockedOnly,
                        datDeb, datFin, sqlFilter);
                
                if (list == null && t.getFid()>0) {
                    message = "Erreur : aucun enregistrement à mettre à jour";
                    break;
                }
            
                
                
                /************** traitement du lot *****************/
                
                if (t.getContent().indexOf("{%")>=0) { 
                    currentStartTag = "{%";
                    currentEndTag = "%}";
                }
                
                // parcours des instructions du template ligne par ligne
                LinkedList<String> script = BasicTools.parseLineToList(BasicTools.replaceCrLf(t.getContent(),""), ";");
                
                message="";
                
                for (int i=0; script!=null && i<script.size(); i++) {
                    String cmd = ((String)script.get(i)).trim();
                    
                    if (cmd.indexOf("LOCKALL")>=0) {
                        // lock de la sélection de submitform
                        
                        // mise à jour de chaque submitform
                        for (int j=0; j<list.size(); j++) {
                            SubmitForm sf = (SubmitForm)list.get(j);
                            sfa.lock(sf.getS_id(), userData.getLogin());
                        }
                    }
                    
                    if (cmd.indexOf("UNLOCKALL")>=0) {
                        // unlock
                        
                        // mise à jour de chaque submitform
                        for (int j=0; j<list.size(); i++) {
                            SubmitForm sf = (SubmitForm)list.get(j);
                            sfa.unLock(sf.getS_id(), userData.getLogin());
                        }
                    }
                    
                    
                    if (cmd.indexOf(currentStartTag+"F0")>=0 && cmd.indexOf("=")>=0) {
                        
                        /**  MISE A JOUR d'un champ de formulaire pour tous les SFs concernés **/
                        int pid =  BasicTools.getIntFrom5DigitFormatedInputName(cmd.substring(0,cmd.indexOf("=")),"P");
                        
                        String val = cmd.substring(cmd.indexOf('=')+1 ) ;  //, cmd.length()-cmd.indexOf('=')
                        int pidVal = -1;
                        
                        if (val != null && val.indexOf(currentStartTag+"DATE")>=0)  
                            val = BasicType.getTodaysDateLocal();
                        if (val != null && val.indexOf(currentStartTag+"F0")>=0)
                            pidVal =  BasicTools.getIntFrom5DigitFormatedInputName(val,"P");
                        
                        
                        
                        // mise à jour de la valeur pour chaque submitform
                        for (int j=0; pid >0 && j<list.size(); j++) {
                            SubmitForm sf = (SubmitForm)list.get(j);
                            ReponseAction ra = new ReponseAction();
                            Reponse r = new Reponse();
                            
                            if (pidVal>0) {
                                // aller chercher valeur (mais la proposition du questionnaire doit être en mode "initOnLoad")
                                val=sf.getPropVal(pidVal);
                            } 
                            
                            if (val == null) val = "";
                            
                            r.setS_id(sf.getS_id());
                            r.setP_id(pid);
                            r.setSVal(val.trim());
                            
                            ra.insert(r);
                        }
                    }
                    
                    
                    
                    if (cmd.startsWith("EXECUTE ") ) {
//                      executer une procédure stockée une seule fois
                        cmd = BasicTools.cleanHTML(cmd);
                        String procStock = cmd.substring(cmd.indexOf(" ")+1 ,cmd.indexOf('(')  );
                        String vars = null;
                        if (cmd.indexOf(')') > cmd.indexOf('(')+1) vars= cmd.substring( cmd.indexOf('(')+1 , cmd.indexOf(')') );  
                        
                        LinkedList<String> listVar = null;
                        
                        if (vars != null && vars.length()>0) {
                            listVar = BasicTools.parseLineToList(vars, ",");
                        } else {
                            listVar = new LinkedList<String>();
                        }
                        
                        // formatage pour l'appel de la procstock avec variables
                        procStock += "(";
                        for (int v=0; listVar!=null && v<listVar.size(); v++) {
                            
                            if (v==0) {
                                procStock += "?";
                            } else {
                                procStock += ",?";
                            }
                        }
                        procStock += ")";
                                
                        //appel de la proc
                        sfa.callProc(procStock.trim(), listVar);
                        
                        
                    }
                    
                    
                    
                    if (cmd.startsWith("EXECUTE_BY_ID ") ) {
                        // executer une procédure stockée pour chaque SF du f_id du template 
                        cmd = BasicTools.cleanHTML(cmd);
                        String procStock = cmd.substring(cmd.indexOf(" ")+1 ,cmd.indexOf('(') );
                        String vars = null;
                        if (cmd.indexOf(')') > cmd.indexOf('(')+1) vars= cmd.substring( cmd.indexOf('(')+1 , cmd.indexOf(')') );  
                        
                        LinkedList<String> listVar = null;
                        
                        if (vars != null && vars.length()>0) listVar = BasicTools.parseLineToList(vars, ",");
                        
                        // formatage pour l'appel de la procstock avec variables
                        procStock += "(";
                        for (int v=0; listVar!=null && v<listVar.size(); v++) {
                            
                            if (v==0) {
                                procStock += "?";
                            } else {
                                procStock += ",?";
                            }
                        }
                        procStock += ")";
                        
//                      mise à jour de la valeur pour chaque submitform
                        for (int j=0; j<list.size(); j++) {
                            SubmitForm sf = (SubmitForm)list.get(j);
                            
                            LinkedList<String> listVarForId = new LinkedList<String>();
                            
                            for (int v=0; listVar!=null && v<listVar.size(); v++) {
                                if (listVar.get(v).trim().equals(currentStartTag+"SID........."+currentEndTag)) {
                                    listVarForId.add(String.valueOf(sf.getS_id()));
                                } else {
                                    listVarForId.add(listVar.get(v).trim());
                                }
                            } 
                                
                            //appel de la proc
                            sfa.callProc(procStock.trim(), listVarForId);
                        
                        }
                    }
                    
                    
                    if (cmd.startsWith("SPECIFIC ") ) {
                        String s_args = cmd.substring(cmd.indexOf(" ")+1  );
                        String[] args = s_args.split(" ");
                        message += "Lancement de : " + cmd + "<br/>";
                        String ret = new UserActionFactory().getUserAction("ContextUserAction").doSpecificAction(args,userData);
                        if (ret !=null) message += ret+ "<br/>";
                    }
                    
                    traceLog.info(cmd);  
                    
                    message += "Exécution de : " + cmd + "<br/>";
                    if (t.getFid()>0 && list!=null) message += list.size() + " enregistrement(s) du lot mis à jour.<br/>";
                     
                    
                }
                
                
                traceLog.info("........... FIN TRAITEMENT LOT");
                    
                
                /**********************************/
               
                              
            
            break;
            
            
            
        case ERREUR:
            message = "Erreur : ligne "+nbImp;
            
            break;
        }
        
        
            } catch (Exception e) {
                traceLog.error(e.getMessage());
                if (Controls.isBlank(message)) message = e.toString();
                req.getSession().setAttribute(ServControl.MESSAGE,message);
            }
        
        traceLog.info(message);
            req.getSession().setAttribute(ServControl.MESSAGE,message);
            
               
    }
}