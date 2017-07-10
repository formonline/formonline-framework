/*
 * Créé le 21 oct. 04
 *
 * Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
package formOnLine.msBeans;

import java.util.HashSet;
import java.util.LinkedList;

import com.triangle.lightfw.SessionInfos;
import com.triangle.lightfw.ValueBean;
import com.triangle.lightfw.ValueBeanList;

/**
 * @author SLE
 *
 */
public class UserData extends ValueBean {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String login;
    private String name;
    private String pwd;
    private int id;
    private int parent_id;
    private int f_id;
    private int role;
    private String baseUrl = "";
    private LinkedList<Questionnaire> listQuest;
    private LinkedList<SubmitForm> listRep;
    private LinkedList<Template> listTemplate;
    
    // liste d'IDs de SF délégués
    private HashSet<Integer> listDelegIds = new HashSet<Integer>() ;
    
    // liste d'IDs de SF devant être en lecture seule (quelque soit leur état de lock)
    private HashSet<Integer> listReadOnlyIds = new HashSet<Integer>();
    
    
    public UserData (String login, String pwd, int id, int parent_id) {
        setLogin(login);
        setName(login);
        setPwd(pwd);
        setId(id);
        setParent_id(parent_id);
        listQuest = new LinkedList<Questionnaire>();
        listRep = new LinkedList<SubmitForm>();
        listTemplate = new LinkedList<Template>();
    }
    
    public UserData () {
        listQuest = new LinkedList<Questionnaire>();
        listRep = new LinkedList<SubmitForm>();
        listTemplate = new LinkedList<Template>();
    }
    
    public String toString() {
        String s="";
        s += "Name=" + name + " | ";
        s += "Id=" + String.valueOf(id) + " | ";
        s += "Role=" + String.valueOf(role) + " | ";
        //s += "Pwd=" + pwd + " | ";
        s += "Nb Q=" + String.valueOf(listQuest.size()) + " | ";
        s += "Nb R=" + String.valueOf(listRep.size()) ;
        
        return s;
    }
    /**
     * @return
     */
    public int getId() {
        return id;
    }
    
    /**
     * @return
     */
    public String getName() {
        return name;
    }
    
    /**
     * @return
     */
    public String getPwd() {
        return pwd;
    }
    
    /**
     * @param i
     */
    public void setId(int i) {
        id = i;
    }
    
    /**
     * @param string
     */
    public void setName(String string) {
        name = string;
    }
    
    /**
     * @param string
     */
    public void setPwd(String string) {
        pwd = string;
        
    }
    
    /**
     * @return Returns the listRep.
     */
    public ValueBeanList getListRep() {
        return ValueBeanList.getFromList(listRep);
    }
    
    /**
     * Extraction de la liste des Submitform de la session utilisateur
     * connecté correspondant au type de formulaire demandé
     * 
     * @return Returns the listRep.
     */
    public ValueBeanList getListRepByFid(int f_id) {
        return getListRepByFidAndSidParentAndSlock( f_id,  -1,  -1);
    }
    
    /**
     * Extraction de la liste des Submitform de la session utilisateur
     * connecté correspondant au type de formulaire demandé et à un s_id_parent donné
     * 
     * @return Returns the listRep.
     */
    public ValueBeanList getListRepByFidAndSidParent(int f_id, int s_id_parent) {
        
        return getListRepByFidAndSidParentAndSlock( f_id,  s_id_parent, -1);
    }
    
    /**
     * Extraction de la liste des Submitform de la session utilisateur
     * connecté correspondant au type de formulaire demandé et à un s_id_parent donné
     * 
     * @return Returns the listRep.
     */
    public ValueBeanList getListRepByFidAndSidParentAndSlock(int f_id, int s_id_parent, int s_lock) {
        return getListRepByFidAndSidParentAndSlockAndPid( f_id,  s_id_parent,  s_lock, -1, null);
    }
    
    /**
     * Extraction de la liste des Submitform de la session utilisateur
     * connecté correspondant au type de formulaire demandé et à un s_id_parent donné
     * 
     * @return Returns the listRep.
     */
    public ValueBeanList getListRepByFidAndSidParentAndSlockAndPid(int f_id, int s_id_parent, int s_lock, 
            int propId, String propVal) {
        ValueBeanList res = new ValueBeanList();
        
        for (int i=0; listRep !=null && i< listRep.size();i++) {
            SubmitForm sf = (SubmitForm)listRep.get(i);
            if (  (f_id<0 || sf.getF_id()==f_id) 
                    && (s_id_parent<0 || sf.getS_id_parent() == s_id_parent)
                    && (s_lock<0 || sf.getLock() == s_lock)
                    && (propId<0 || sf.getPropVal(propId).equals(propVal))) 
                res.add(sf);
        }
        
        return res;
    }

    /**
     * test l'existence de SF dans la session utilisateur
     * connecté correspondant au type de formulaire demandé
     * (fils du SF parent identifié ou  délégué) 
     * 
     * @return Returns true if exists.
     */
    public boolean existsSubmitformByFidAndSidParent(int f_id, int s_id_parent) {
        
        return existsSubmitformByFidAndSidParent(f_id, s_id_parent, true); 
    }    
    
    /**
     * test l'existence de SF dans la session utilisateur
     * connecté correspondant au type de formulaire demandé
     * (fils du SF parent identifié ou  délégué si demandé)
     *  
     * @param f_id
     * @param s_id_parent
     * @param withDelegation (si oui on teste aussi les délégations comme pères possibles)
     * @return Returns true if exists.
     */
    public boolean existsSubmitformByFidAndSidParent(int f_id, int s_id_parent, boolean withDelegation) {
        
        for (int i=0; f_id>0 && listRep !=null && i< listRep.size();i++) {
            SubmitForm sf = (SubmitForm)listRep.get(i);
            if ( sf.getF_id()==f_id && sf.getS_id_parent()==s_id_parent) return true;
            // test avec parent délégué
            if ( withDelegation && sf.getF_id()==f_id && isDelegId(sf.getS_id_parent()) ) return true;
        }
        
        return false;
    } 
    
    /**
     * test l'existence de SF dans la session utilisateur
     * au moins deux item du même f_id 
     * 
     * @return Returns true if exists.
     */
    public boolean existsMoreThanOneSubmitformByFid(int f_id) {
        int count =0;
        for (int i=0; f_id>0 && listRep !=null && i< listRep.size();i++) {
            SubmitForm sf = (SubmitForm)listRep.get(i);
            if ( sf.getF_id()==f_id ) count++;
            // test 
            if (count>1) return true;
        }
        
        return false;
    }    
    
    /**
     * Test si existe un Submitform de la session utilisateur
     * connecté correspondant au type de formulaire demandé et à un s_id_parent donné
     * 
     * @return vrai s'il en existe au moins un 
     */
    public boolean existRepByFidAndSidParentAndSlockAndPid(int f_id, int s_id_parent, int s_lock, 
            int propId, String propVal) {
        
        for (int i=0; listRep !=null && i< listRep.size();i++) {
            SubmitForm sf = (SubmitForm)listRep.get(i);
            if (  (f_id<0 || sf.getF_id()==f_id) 
                    && (s_id_parent<0 || sf.getS_id_parent() == s_id_parent)
                    && (s_lock<0 || sf.getLock() == s_lock)
                    && (propId<0 || sf.getPropVal(propId).equals(propVal))) 
                return true;
        }
        
        return false;
    }
    
    /**
     * @param listRep The listRep to set.
     */
   
    public void setListRep(LinkedList<SubmitForm> listRep) {
        this.listRep = listRep;
    }
    
    /**
     * @param listRep The listRep to add.
     */   
    public void addListRep(LinkedList<SubmitForm> listRep) {
        this.listRep.addAll( listRep);
    }
    
    /**
     * @param listRep The listRep to add.
     */  
    public void addListRep(ValueBeanList listSf) {
        if (listSf != null) {
            for (ValueBean sf : listSf) {
                this.listRep.add((SubmitForm)sf);
            }
        }
        
        
    }
    
    
    /**
     * Ajout  d'un questionnaire dans le userdata
     * @param q
     */
    public void addQuest (Questionnaire q) {
        listQuest.add(q);
    }
    
    /**
     * Ajout d'un submitform  dans le userdata
     * @param sf
     */
    public void addRep (SubmitForm sf) {
        listRep.add(sf);
    }
    

    
    /**
     * Retrait d'un submitform dans le userdata
     * @param sfNew
     */
    public void removeRep (SubmitForm sfNew) {
        if (sfNew!=null) {
            int sid = sfNew.getS_id();
            
            for (int i=0; i<listRep.size(); i++) {
                SubmitForm sf  = (SubmitForm)listRep.get(i); 
                if (sf!=null && sf.getS_id() == sid) {
                    listRep.remove(i) ;
                    break;
                } 
            }
        }
    }
    
    
    /**
     * Ajout ou remplacement d'un submitform dans le userdata
     * @param sfNew
     */
    public void addOrReplaceRep (SubmitForm sfNew) {
        
        int pos = -1;
        for (int i=0; listRep !=null && i< listRep.size(); i++) {
            SubmitForm sf = (SubmitForm)listRep.get(i);
            if (sf.getS_id() == sfNew.getS_id()) {
                pos = i;
                break;
            }
        }
        
        if (pos>=0) {
            listRep.set(pos, sfNew);
        } else {
            addRep(sfNew);
        }

    }
    
    /**
     * Ajout ou remplacement d'une liste de submitform dans le userdata
     * @param liste des sf
     */
    public void addOrReplaceListRep (LinkedList<SubmitForm> listSf) {
        
        for (int i=0; listSf!=null && i< listSf.size(); i++){
            SubmitForm rep = (SubmitForm)listSf.get(i);
            addOrReplaceRep(rep);
        }
    }
    
    /**
     * Ajout ou remplacement d'un questionnaire dans le userdata
     * @param qNew
     */
    public void addOrReplaceQuest (Questionnaire qNew) {
        
        
        int pos = -1;
        for (int i=0; listQuest !=null && i< listQuest.size(); i++) {
            Questionnaire q = (Questionnaire)listQuest.get(i);
            if (qNew!=null && q!=null && q.getId() == qNew.getId()) {
                pos = i;
                break;
            }
        }
        
        if (pos>=0) {
            listQuest.set(pos, qNew);
        } else {
            addQuest(qNew);
        }
        
        /*if (qNew == null) return;
        int qid = qNew.getId();
        
        for (int i=0; listQuest!=null && i<listQuest.size(); i++) {
            Questionnaire q  = (Questionnaire)listQuest.elementAt(i); 
            if (q.getId() == qid) {
                listQuest.remove(i) ;
                break;
            } 
        }
        addQuest(qNew);*/
    }
    
    
    /**
     * @param id
     * @return
     */
    public Questionnaire getQuest(int id) {
        Questionnaire res = null;
        for (int i=0; i<listQuest.size(); i++) {
            Questionnaire q  = (Questionnaire) listQuest.get(i); 
            if (q!=null && q.getId() == id) {
                res = q ;
                break;
            } 
        }
        return res;
    }
    

    /**
     *  retourne le SubmitForm du userData correspondant à l'id demandé
     *  
     * @param id
     * @return le SubmitForm
     */
    public SubmitForm getRep(int id) {
        SubmitForm res = null;
        for (int i=0; i<listRep.size(); i++) {
            SubmitForm sf  = (SubmitForm)listRep.get(i); 
            if (sf!=null && sf.getS_id() == id) {
                res = sf ;
                break;
            } 
        }
        return res;
    }
    
    /** retourne le SubmitForm principal (ayant servi à l'authentification)
     * 
     * @return le SubmitForm principal
     */
    public SubmitForm getMasterSf() {
        return getRep(getId());
    }
    

    
    /** retourne le template correspondant à l'id
     * 
     * @param id
     * @return
     */
    public Template getTemplate(int id) {
        Template res = null;
        for (int i=0; i<listTemplate.size(); i++) {
            Template t  = (Template)listTemplate.get(i); 
            if (t.getId() == id) {
                res = t ;
                break;
            } 
        }
        return res;
    }
    
    
    /**
     * @return Returns the listQuest.
     */
    public ValueBeanList getListQuest() {
        return ValueBeanList.getFromList(listQuest);
    }
    /**
     * @param listQuest The listQuest to set.
     */
    public void setListQuest(LinkedList<Questionnaire> listQuest) {
        this.listQuest = listQuest;
    }
    
    /**
     * @return Returns the listQuest.
     */
    public ValueBeanList getListTemplate() {
        return ValueBeanList.getFromList(listTemplate);
    }
    /**
     * @param listQuest The listQuest to set.
     */
    public void setListTemplate(LinkedList<Template> list) {
        this.listTemplate = list;
    }
    
    /**
     * @return Returns the role.
     */
    public int getRole() {
        return role;
    }
    
    /**
     * @param role The role to set.
     */
    public void setRole(int role) {
        this.role = role;
    }
    
    /**
     * contrôle si l'objet userData contient le submitform 
     * qui a servi pour le login 
     */
    public boolean isItsOwnData() {
        return (getRep(id) != null);
    }

    
    /**
     * contrôle si l'objet userData contient le submitform 
     * dont l'id est donné en paramètre
     */
    public boolean isItsOwnData(int sid) {
        return isItsOwnData(sid,false, false);
    } 
    /**
     * contrôle si l'objet userData contient le submitform 
     * dont l'id est donné en paramètre
     */
    public boolean isItsOwnData(int sid, boolean extended) {
        return isItsOwnData(sid,extended, false);
    }
        
    /**
     * contrôle si l'objet userData contient le submitform 
     * dont l'id est donné en paramètre, et que le submitform
     * est bien le fils du père connecté ou le père
     * OU que le submitform est fils d'un submitform pere 
     * dans le userData (cas des délégations) 
     * OU le sid est stocké dans une proposition 
     * 
     */
    public boolean isItsOwnData(int sid, boolean extended, boolean forExport) {
        SubmitForm sf = getRep(sid);
        
        // test pour la délégation
        if ((extended &&  listDelegIds!=null && isDelegId(sid))
                && (sf!=null || forExport)) return true; //ok, cas d'une délégation 
        
        if (sf==null) return false; // pas trouvé
        
        if (sf.getS_id() == id) return true; // OK c'est le père
        
        if (sf.getS_id_parent() == id) return true; // OK c'est un fils reconnu
        
        if (extended && getRep(sf.getS_id_parent()) != null) return true; // OK c'est un petit-fils reconnu
        
        
//      formulaire fils d'un formulaire délégé
        //SubmitForm sff = getRep(sf.getS_id_parent());
        //if (sff!=null && listDelegIds!=null && isDelegId(sff.getS_id_parent())) return true; //ok, cas d'un fils d'une délégation 
        if (extended &&  listDelegIds!=null && isDelegId(sf.getS_id_parent())) return true; //ok, cas d'un fils d'une délégation 

        // formulaire petit-fils d'un formulaire délégé
        SubmitForm sff = getRep(sf.getS_id_parent());
        if (extended && sff!=null && listDelegIds!=null && isDelegId(sff.getS_id_parent())) return true; //ok, cas d'un petit-fils d'une délégation 
         

        // le lien a été fait par une proposition stockant le pid
        Questionnaire f = getQuest(sf.getF_id());
        for (int i=0; extended && f!=null && f.getGroupes()!=null && i< f.getGroupes().size(); i++) {
            Groupe g = (Groupe)f.getGroupes().get(i);
            for (int j=0; g.getQuestions()!= null && j< g.getQuestions().size(); j++) {
                Question q = (Question)g.getQuestions().get(j);
                if (q.getType().equals(Question.FORM_ID) && q.getPropositions()!=null &&  q.getPropositions().size()>0) {
                    Proposition p = (Proposition)q.getPropositions().get(0);
                    if (sf.getPropVal(p.getId()).equals( String.valueOf(getId()) )) return true;
                }
            }
        }
        
        
        return false; // sinon
        
    }
    
  
    
    /**
     * contrôle si l'objet userData contient au moins un submitform 
     */
    public boolean isConnected() {
        
        if (listRep != null && listRep.size()>0 && isItsOwnData(id,true)) return true;
        
        if (getRole()> SessionInfos.ROLE_PUBLIC) return true;
        
        return false;
    }

    /**
     * @return Returns the listDelegIds.
     */
    public boolean isDelegId(int id) {
        
        return (listDelegIds.contains(new Integer(id) ));
    }
    
    
    /**
     * @return Returns the listDelegIds.
     */
    public HashSet<Integer>  getListDelegIds() {
        
        return listDelegIds;
    }

    /**
     * @param listDelegIds The listDelegIds to set.
     */
    public void setListDelegIds(HashSet<Integer> listDelegIds) {
        this.listDelegIds = listDelegIds;
    }
    
    /**
     * @param addDelegId The listDelegIds to set.
     */
    public void addDelegId(int id) {
        this.listDelegIds.add(new Integer(id)) ;
    }
    
    

    /**
     * @param listDelegIds The listDelegIds to add.
     */
    public void addListDelegIds(ValueBeanList listDelegIds) {
        if (listDelegIds != null) {
            for (ValueBean sf : listDelegIds) {
                this.listDelegIds.add( ((SubmitForm)sf).getS_id());
            }
        }
        
        
    }
    
    /**
     * @return Returns true if the ID should be  a READONLY form
     */
    public boolean isReadOnlyId(int id) {
        
        return (listReadOnlyIds.contains(new Integer(id) ));
    }
    
    
    /**
     * @return Returns the listReadOnlyIds.
     */
    public HashSet<Integer>  getListReadOnlyIds() {
        
        return listReadOnlyIds;
    }

    /**
     * @param  The listReadOnlyIds to set.
     */
    public void setListReadOnlyIds(HashSet<Integer> listReadOnlyIds) {
        this.listReadOnlyIds = listReadOnlyIds;
    }
    
    /**
     * @param int The ReadOnly Ids to add
     */
    public void addReadOnlyId(int id) {
        this.listReadOnlyIds.add(new Integer(id)) ;
    }

    /**
     * @param  The listReadOnlyIds to add
     */
    public void addListReadOnlyIds(ValueBeanList listReadOnlyIds) {
        if (listReadOnlyIds != null) {
            for (ValueBean sf : listReadOnlyIds) {
                this.listReadOnlyIds.add( ((SubmitForm)sf).getS_id());
            }
        }
        
        
    }
    
    /**
     * @param  The listReadOnlyIds to remove
     */
    public void removeListReadOnlyIds(ValueBeanList listReadOnlyIds) {
        if (listReadOnlyIds != null) {
            for (ValueBean sf : listReadOnlyIds) {
                this.listReadOnlyIds.remove( ((SubmitForm)sf).getS_id());
            }
        }
        
        
    }
    
    

    /**
     * @return the login
     */
    public String getLogin() {
        return login;
    }

    /**
     * @param login the login to set
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * @return the parent_id
     */
    public int getParent_id() {
        return parent_id;
    }

    /**
     * @param parent_id the parent_id to set
     */
    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    /**
     * @return the f_id
     */
    public int getF_id() {
        return f_id;
    }

    /**
     * @param f_id the f_id to set
     */
    public void setF_id(int f_id) {
        this.f_id = f_id;
    }

    /**
     * @return the baseUrl
     */
    public String getBaseUrl() {
        return baseUrl;
    }

    /**
     * @param baseUrl the baseUrl to set
     */
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }


}
