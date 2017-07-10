/* **********************************
 * 
 * formOnLine.io : XmlRpcActions.java
 * Created on 25 juil. 2012 by SeLERIDON
 * 
 ************************************ */
package formOnLine.io;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.LinkedList;





import com.triangle.lightfw.BasicType;
import com.triangle.lightfw.SessionInfos;

import formOnLine.BasicTools;
import formOnLine.Controls;
import formOnLine.FormatExport;
import formOnLine.InitServlet;
import formOnLine.actions.ReponseAction;
import formOnLine.actions.SubmitFormAction;
import formOnLine.msBeans.Reponse;
import formOnLine.msBeans.SubmitForm;
import formOnLine.msBeans.UserData;

public class XmlRpcActions {

    private static final int IMPORT = 1;
    private static final int UPDATE = 2;
    private static final int DELETE_ONE = 3; 
    private static final int DELETE_ALL = 4;
    private static final int EXECUTE = 5;
    
    
    /** contrôle des adresses IP ... A TESTER
    /* http://ws.apache.org/xmlrpc/faq.html#client_ip
     * 
     * @return boolean
     */
    

    /**
     * Methode appelée par le client distant webrpc, avec une map en paramètre
     * 
     * dans une HashMap<String,String>, on attend 
     * 
     *  -> les paires obligatoires :
     * "sid":<id>  identifiant submitform : donnée contrôlée, si le sid n'existe pas, le SF est créé (obligatoire sauf dans le cas du "delete:all")
     * "fid":<id>  identifiant questionnaire : donnée contrôlée
     * 
     * -> les paires optionnelles
     * "lock":<0|1> validé ou non : mise à jour du lock du SF
     * "defaultLock":<0|1> validé ou non : utilisé uniquement pour initialiser la valeur par défaut si le sf n'existe pas
     * "parent_sid":<id> formulaire parent : mise à jour du s_id_parent
     * "date_maj":<dateISO> date de mise à jour : mise à jour date mise à jour
     * "nocreat" si cette clé est présente, le SF ne sera pas créé s'il n'existe pas (réponses non insérées) 
     * "delete":"SF" si cette clé est présente, le SF et ses réponses seront supprimés 
     * "delete":"ALL" si cette clé est présente, tous les SF de même f_id et leurs réponses seront supprimés (!) 
     * "execute":"procstock" : lancement d'une procédure stockée distante
     *
     *  -> et les réponse(s) à la proposition PID à mettre à jour :
     * <pid1>:<val1> 
     * <pid2>:<val2> 
     * ...
     * 
     */             
    public String  setListReponse(HashMap<String,String> map) throws Exception   { 
        
        InitServlet.traceLog.debug(">>>>>> début demande xmlprc setListReponse ");
        
        String charset = "UTF-8";
        if (!Controls.isBlank(InitServlet.getExportCharset())) charset = InitServlet.getExportCharset();
            
        String startmsg = "<?xml version=\"1.0\" encoding=\""+charset+"\"?><message>";
        String msg="ok";
        String endmsg="</message>";
        
        ReponseAction ra = new ReponseAction();
        SubmitFormAction sa = new SubmitFormAction();
        int action = IMPORT;
        
        try {
            // ***************************************************    
            // Demande de mise à jour de liste de réponses de liste de submitforms
            
            if (map == null || map.size() == 0) {
                return startmsg + "Aucune donnée à mettre à jour" + endmsg;
            }
            
            
            int sid = 0;
            if (map.containsKey("sid")) sid = Integer.parseInt(map.get("sid")); 
            int fid = Integer.parseInt(map.get("fid"));
            String date_maj = null;
            boolean nocreat = false;
            
            int lock = -1;
            int defaultLock = -1;
            
            int parent_sid = -2;
            if (map.containsKey("lock")) lock = Integer.parseInt(map.get("lock"));
            if (map.containsKey("defaultLock")) defaultLock = Integer.parseInt(map.get("defaultLock"));
            if (map.containsKey("parent_sid")) parent_sid = Integer.parseInt(map.get("parent_sid"));
            if (map.containsKey("date_maj")) date_maj = map.get("date_maj");
            if (map.containsKey("nocreat")) nocreat = true;
            
            
            if (map.containsKey("delete") && map.get("delete").equals("SF")) 
                action = DELETE_ONE; // suppression SF 
            if (map.containsKey("delete") && map.get("delete").equals("ALL") && fid>0) 
                action = DELETE_ALL; // suppression tous les SF (!)
            if (map.containsKey("execute") && map.get("execute")!= null ) 
                action = EXECUTE; // execution procedure stockee
            
            
            // contrôle général
            if (sid<0 && fid <0) {
                return startmsg + "Le SID ou le FID doivent être définis" + endmsg;
            }
            
            // redirection actions
            switch (action) {
            
                // création du submitform
                case IMPORT :{
                    // on recherche le SF
                    SubmitForm sf = sa.selectBySId(sid);
                    
                    // contrôle du type du SF
                    if (sf != null && sf.getF_id() != fid) {
                        return startmsg + "Problème pour la mise à jour du SF:" +sf.getS_id() +", le formulaire existe déjà avec un type différent"+ endmsg;
                    }
                    
                    // création s'il n'existe pas
                    if (sf == null && !nocreat) {
                        //cas du sid qui n'existe pas > création
                        
                        sf = new SubmitForm();
                        sf.setF_id(fid);
                        sf.setS_id(sid);
                        
                        if (defaultLock >=0) sf.setS_lock(defaultLock);
                        
                        if (lock >=0) sf.setS_lock(lock);
                        
                        if (parent_sid >= -1) sf.setS_id_parent(parent_sid);
                        
                        sf.setS_date_creat( BasicType.getTodaysDateIso());
                        if (date_maj!=null) {
                            sf.setS_date(date_maj);
                        } else {
                            sf.setS_date(BasicType.getTodaysDateIso());
                        }
                        
                        sf.setPwd(BasicTools.getPwd(true,true,true,8)); //pass 
                        
                        // insertion du SF
                        sa.insertFull(sf);
                        
                        if (sid==0) {
                            //récupération de l'id généré en autoincrément par mysql
                            sf = sa.selectByDateAndPwdAndFid(sf.getS_date(),sf.getPwd(),fid);
                            sid = sf.getS_id();
                        }
                        
                    } else {
                        
                        // le SF existe déjà
                        
                        // contrôle du type du SF
                        if (sf.getF_id() != fid) 
                            return startmsg + "Problème pour la mise à jour du SF:" +sf.getS_id() +", le formulaire existe déjà avec un type différent"+ endmsg;
                        
                        // maj s_id_parent (si différend)
                        if (sf.getS_id_parent() != parent_sid && parent_sid >-2    ) {
                            sf.setS_id_parent(parent_sid);
                            sa.updateSidParent(sf);
                        }
                        
                        // maj lock (si différend)
                        if (sf.getLock() != lock && lock >-1) {
                            sf.setS_lock(lock);
                            if (lock==1) {
                                sa.lock(sf.getS_id(), "XMLRPC");
                            } else {
                                sa.unLock(sf.getS_id(), "XMLRPC");
                            }
                            
                        }
                        
                        // maj s_date_maj (si différend)
                        if (date_maj!=null && sf.getS_date() != date_maj   ) {
                            sf.setS_date(date_maj);
                            sa.updateDate(sf,date_maj);
                        }
                    }
                        
                    // REPLACE des réponses
                    if (sf != null ) {
                        for (String mapKey : map.keySet()) {
                            if (!mapKey.equals("sid") && !mapKey.equals("fid") 
                                    && !mapKey.equals("parent_sid") && !mapKey.equals("date_maj")
                                    && !mapKey.equals("lock") && !mapKey.equals("nocreat")
                                    && !mapKey.equals("defaultLock")) {
                                
                                int pid = -1;
                                
                                // on ne traite que les PID
                                try { pid = Integer.parseInt(mapKey);
                                } catch (NumberFormatException e) { continue; }
                                
                                String val = map.get(mapKey);
                                
                                // Traitement
                                Reponse rep = new Reponse(sid,pid,val,0);
                                
                                // replace de la réponse
                                if (Controls.isBlank(rep.getSVal())) {
                                    // si vide, on tente de supprimer la reponse au cas où elle était renseignée
                                    ra.delete(rep);
                                } else {
                                    // sinon, on lance le REPLACE
                                    ra.insert(rep);
                                }
                                
                            }
                        }
                    } 
                break;
                }
                
                
                // suppression du submitform et des réponses
                case DELETE_ONE :{
                    // on recherche le SF
                    SubmitForm sf = sa.selectBySId(sid);
    
                    if (sf != null) sa.delete(sf);
                    break;
                }
                
                // suppression de tous les submitform et des réponses d'un FID !!
                case DELETE_ALL :{
                    if (fid>0) sa.deleteAllByFid(fid);
                    break;
                }
                
                // lancement d'une procédure stockée
                case EXECUTE :{
                    String procName = map.get("execute");
                    
                    LinkedList<String> listVar = new LinkedList<String>();
                    
                    //appel de la proc
                    SubmitFormAction sfa = new SubmitFormAction();
                    sfa.callProc(procName, listVar);
                    
                break;
                }

                default:
                break;
            } 
            

            
            
            
        } catch (Exception e) { 
            return startmsg + e.getMessage() + endmsg;
        }
        
        
        return  startmsg + msg + endmsg;
    }
    

    
    
    /**
     * Execute une exportation en fonction des paramètres reçus. 
     * 
     * Les paramètres de la methode getFormulaires sont: 
     * "file:<Chemin fichier>" fichier sur disque, defaut : retour XML 
     * "dMin:<AAAA-MM-JJ>" date minimum de màj, defaut : toutes les données quelque soit la date
     * "dMax:<AAAA-MM-JJ>" date max de màj, defaut : toutes les données quelque
     * soit la date 
     * "all" : ne pas filtrer les données non validées, defaut :
     * uniquement les données verrouillées 
     * "norep" : ne pas charger les reponses (pour comptage), defaut : renvoie les réponses 
     * "fid:<N° formulaire>" Numéro du formulaire interne, defaut : 1 
     * "sid:<N° submitform>" Numéro du submitform interne, defaut : -1 
     * "pid:<N° proposition>" Numéro de la proposition pour le filtre, defaut : -1 
     * "val:<valeur reponse>" valeur nécessaire de la réponse à la proposition pour le
     * filtre, defaut : null
     * 
     * 
     * @param arguments
     *            HashMap des paramètres de la méthode appelée
     * @return String XML indiquant le résultat de l'exportation ou
     *         l'exportation elle même
     */
    public String getFormulaires(HashMap<String,String> map) throws Exception {
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileOutputStream fos = null;
        int fid = 1;
        int sid = -1, parentSid = -1;
        boolean isFichier = false;
        int lockedOnly = 1;
        boolean norep = false;
        String nomFichier = null;
        String dateIsoMin = null;
        String dateIsoMax = null;
        int pid = -1;
        String val = null;
        
        
        
        if (map.containsKey("sid")) sid = Integer.parseInt(map.get("sid"));
        if (map.containsKey("parentSid")) parentSid = Integer.parseInt(map.get("parentSid"));
        if (map.containsKey("fid")) fid = Integer.parseInt(map.get("fid"));
        
        
        if (map.containsKey("file")) {
            isFichier = true;
            nomFichier = map.get("file");
            fos = new FileOutputStream(nomFichier);
        }
        
        if (map.containsKey("dmin")) dateIsoMin = map.get("dmin");
        if (map.containsKey("dmax")) dateIsoMax = map.get("dmax");
        if (map.containsKey("pid")) pid = Integer.parseInt(map.get("pid"));
        if (map.containsKey("val")) val = map.get("val");
        if (map.containsKey("all")) lockedOnly = -1;
        if (map.containsKey("norep")) norep = true;
        
        // init userdata pour le contrôle des droits
        UserData ud = new UserData();
        ud.setLogin("xmlrpc");
        ud.setRole(SessionInfos.ROLE_ADMIN);
        
        // TRaitement
        if (isFichier) {
            int result = 0;
            
            result = FormatExport.exportXML(fid, lockedOnly,
                    dateIsoMin, dateIsoMax, fos, sid, norep, ud);
            
            fos.close();
            
            String charset = "UTF-8";
            if (!Controls.isBlank(InitServlet.getExportCharset())) charset = InitServlet.getExportCharset(); //(la base est toujours en iso-latin...)
                
            
            baos.write(("<?xml version=\"1.0\" encoding=\""+charset+"\"?><export>OK "
                    + result + " formulaires(s) exporté(s)</export>")
                    .getBytes("UTF-8"));
        } else {
            
            FormatExport.exportXML(fid, lockedOnly, dateIsoMin,
                    dateIsoMax, baos, sid, parentSid, norep, pid, val, ud);
            
        }
        
        
        
        
        return baos.toString();
    }
        
}
