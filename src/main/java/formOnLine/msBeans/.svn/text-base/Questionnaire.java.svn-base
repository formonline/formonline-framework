/*
 * Créé le 18 oct. 04
 * 
 * Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
package formOnLine.msBeans;


import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.w3c.dom.Element;

import com.triangle.lightfw.BasicType;
import com.triangle.lightfw.SessionInfos;
import com.triangle.lightfw.ValueBean;
import com.triangle.lightfw.ValueBeanList;

import formOnLine.BasicTools;
import formOnLine.Controls;
import formOnLine.ServControl;
/**
 * @author S. Leridon
 * 
 */
/**
 * @author seleridon
 *
 */
public class Questionnaire extends ValueBean
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String SESSION_NAME = "questionnaire";
    public static final String SESSION_LIST = "liste_questionnaire";

    public static final int ACCESS_PUBLIC = 0;
    public static final int ACCESS_CONNECTED = 1;
    public static final int ACCESS_RESTRICTED = 2;
    public static final int ACCESS_ADMIN_ONLY = 3;


    public static final String MSG_MANDATORY= "ATTENTION ! Des champs obligatoires n'ont pas été remplis :<br/>";
    public static final String MSG_INCORRECT= "ATTENTION ! Certaines valeurs sont incorrectes :<br/>";

    private static final String MSG_DROITS_INSUFFISANTS = "Droits insuffisants";
    private static final String MSG_HORS_PERIODE = "Ce formulaire est accessible à partir du &1 jusqu'au &2";


    private String      titre        = "";
    private String      introduction = "";
    private int         id           = 0;
    private String      conclusion   = "";
    private String      auteur       = "";
    private int         p_id_titre, p_id_login;
    private boolean     showPwd;
    private LinkedList<Groupe>      groupes;
    private int         t_id_mail_on_create = 0;
    private int         t_id_mail_on_lock = 0;
    private int         t_id_mail_on_delete = 0;
    private int         p_id_mail = 0;
    private String      f_mail_adm = "";

    private String      date_debut = "";
    private String      date_fin = "";
    private String      date_creation = "";

    private int         f_connected = 0;
    private int         f_suppr = 1;
    private int         f_authent = 0;

    private int         s_id         = 0;
    private int         s_id_parent  = -1;
    private int         s_lock = 0;
    private String      s_date = "";


    /**
     * @return Returns the s_id.
     */
    public int getS_id()
    {
        return s_id;
    }

    /**
     * @param s_id The s_id to set.
     */
    public void setS_id(int s_id)
    {
        this.s_id = s_id;
    }

    public Questionnaire()
    {
        groupes = new LinkedList<Groupe>();
    }

    public String toString()
    {
        String s = "";

        s += "qid=" + String.valueOf(id) + " | ";
        s += "sid=" + s_id + " | ";
        s += "titre=" + titre + " | ";
        s += "Nb G=" + String.valueOf(groupes.size());

        return s;

    }

    /**
     * @param e
     */
    public void setAttributes(Element e) {

        e.setAttribute( "id",String.valueOf(getId()));
        e.setAttribute( "sid",String.valueOf(getS_id()));
        e.setAttribute( "parent_sid",String.valueOf(getS_id_parent()));        
        e.setAttribute( "sdate",getS_date());
        e.setAttribute( "titre",getTitre());
        e.setAttribute( "lock",String.valueOf(getS_lock()));
    }

    public String toXML() throws UnsupportedEncodingException
    {
        String s = "<questionnaire ";
        s += " id=\"" + id + "\"";
        s += " sid=\"" + s_id + "\"";
        s += " parent_sid=\"" + s_id_parent + "\"";
        //s += " titre=\"" + URLEncoder.encode(titre,FormatExport.XML_ENCODE)
        // +"\"";
        s += " sdate=\"" + date_creation+ "\"";
        s += " titre=\"" + BasicTools.xmlEncode(titre) + "\"";
        s += ">";
        if (groupes != null)
        {
            for (int i = 0; i < groupes.size(); i++)
            {
                Groupe g = (Groupe)groupes.get(i);
                s += g.toXML();
            }
        }
        s += "</questionnaire>";

        return s;
    }

    public String toBalises() 
    {
        String s = "";
        String fid =  BasicTools.formatWith0(id,5) ;

        if (groupes != null)
        {
            for (int i = 0; i < groupes.size(); i++)
            {
                Groupe g = (Groupe)groupes.get(i);
                s += g.toBalises( fid );
            }
        }

        return s;
    }

    /**
     * sérialisation HTML du questionnaire
     * 
     * @return chaîne HTML
     */
    public StringBuffer toFullHtml(boolean publicOnly, boolean isComplete)     {
        StringBuffer s = new StringBuffer();
        s.append("<div class='titreFormulaire'>").append(titre).append("</div>") ;

        for (Groupe g:groupes) {

            if (g.getType().equals("PU") || !publicOnly) 
                s.append(g.toFullHtml(isComplete));
        }

        return s;
    }


    /**
     * récupération ligne de titres des colonnes de la sérialisation CSV du questionnaire
     * 
     * @return chaîne CSV
     */
    public StringBuffer getFullCsvTitles(boolean publicOnly)     {
        StringBuffer s = new StringBuffer();

        for (Groupe g:groupes) {

            if (g.getType().equals("PU") || !publicOnly) 
                s.append(g.getFullCsvTitles());
        }

        return s;
    }


    /**
     * sérialisation CSV du questionnaire
     * 
     * @return chaîne CSV
     */
    public StringBuffer toFullCsv(boolean publicOnly)     {
        StringBuffer s = new StringBuffer();



        for (Groupe g:groupes) {

            if (g.getType().equals("PU") || !publicOnly) 
                s.append(g.toFullCsv());
        }

        return s;
    }

    /**
     * sérialisation JSON du submitform
     * 
     * @return chaîne json
     */
    @SuppressWarnings("unchecked")
    public JSONObject toJSON(boolean publicOnly)     {

        JSONObject json = new JSONObject();

        json.put("fid", id);
        json.put("id",s_id);
        json.put("id_parent",s_id_parent);
        json.put("lock", s_lock);
        if (s_date!=null) json.put("date", s_date) ;
        json.put("titre",getTitre() );

        JSONArray listg = new JSONArray();

        if (groupes!=null) {
            for (int i=0;i<groupes.size();i++) {
                Groupe g =(Groupe) groupes.get(i);
                if (g.getType().equals("PU") || !publicOnly) {
                    listg.add( g.toJSON() );
                } 

            }
        }

        json.put("groupes", listg);

        return json;
    }


    /**
     * Mise à jour d'une réponse dans un questionnaire
     * 
     * @param gid
     * @param qid
     * @param pid
     * @param sVal
     */
    public void setQuestValById(int gid, int qid, int pid, String sVal)    {
        setQuestValById(gid,qid,pid,sVal, 0);
    }

    /**
     * Mise à jour d'une réponse dans un questionnaire
     * 
     * @param gid
     * @param qid
     * @param pid
     * @param sVal
     * @param val
     */
    public void setQuestValById(int gid, int qid, int pid, String sVal, int val)
    {
        Groupe g = getGroupeById(gid);

        if (g==null) return ;

        Question q = g.getQuestionById(qid);

        if (q==null) return ;

        // cas du choix exclusif : une seule proposition possible
        if (q.getType().equals(Question.CHOIX_EXCLUSIF)) q.resetPropositions();

        Proposition p = q.getPropositionByIdAndVal(pid, val);

        if (p==null) return ;

        p.setsVal(sVal);
        p.setVal(val);

    }

    public String getPropValById(int gid, int qid, int pid)
    {
        Groupe g = getGroupeById(gid);
        Question q = g.getQuestionById(qid);
        Proposition p = q.getPropositionById(pid);
        return p.getsVal();
    }

    public String getPropValBySfAndQuest(int gid, int qid, SubmitForm sf)
    {
        Groupe g = getGroupeById(gid);
        Question q =null;
        if (g!=null) q = g.getQuestionById(qid);
        String val = "";

        for (int i=0; q!=null && q.getPropositions()!=null && i<q.getPropositions().size(); i++) {
            Proposition p = (Proposition)q.getPropositions().get(i);
            if (sf!=null && "1".equals(sf.getPropVal(p.getId())) ) {
                val = p.getTexte();
            }


        }
        return val;
    }

    public String printPropValById(int gid, int qid, int pid)
            throws UnsupportedEncodingException
            {
        Groupe g = getGroupeById(gid);
        Question q = g.getQuestionById(qid);
        Proposition p = q.getPropositionById(pid);
        return p.printVal();
            }

    public void addQuestVal(int gid, int qid, int pid)
    {
        Groupe g = getGroupeById(gid);
        Question q = g.getQuestion(qid);
        Proposition p = q.getProposition(pid);
        p.setVal(p.getVal() + 1);
    }

    public void addGroupe(Groupe g)
    {
        groupes.add(g);
    }

    public LinkedList<Groupe> getGroupes()
    {
        return groupes;
    }

    /**
     * @param groupes
     */
    public void setGroupes(LinkedList<Groupe> groupes)
    {
        this.groupes = groupes;
    }

    /**
     * @param i
     * @return
     */
    public Groupe getGroupe(int i)
    {
        return (Groupe)groupes.get(i);
    }

    /**
     * @param id
     * @return
     */
    public Groupe getGroupeById(int id)
    {
        Groupe res = null;
        for (int i = 0; i < groupes.size(); i++)
        {
            Groupe g = (Groupe)groupes.get(i);
            if (g.getId() == id)
            {
                res = g;
                break;
            }
        }
        return res;
    }

    /**
     * Recherche d'une question par son id 
     * 
     * @param id de la quest
     * @return la question 
     */
    public Question getQuestByQid(int qid)
    {

        for (int i = 0; i < groupes.size(); i++) {
            Groupe g = (Groupe)groupes.get(i);
            for (int j=0; j < g.getQuestions().size(); j++) {
                Question q = (Question)g.getQuestions().get(j);

                // si on trouve le PId, on retourne la question
                if (q.getId() == qid) return q;

            }

        }

        //par défaut
        return null;
    }

    /**
     * Recherche de la liste de propositions par l'id de la question 
     * 
     * @param id de la quest
     * @return la liste de propositions 
     */
    public ValueBeanList getListProsByQid(int qid)
    {

        for (int i = 0; i < groupes.size(); i++) {
            Groupe g = (Groupe)groupes.get(i);
            for (int j=0; j < g.getQuestions().size(); j++) {
                Question q = (Question)g.getQuestions().get(j);

                // si on trouve le PId, on retourne la question
                if (q.getId() == qid) return q.getPropositions();

            }

        }

        //par défaut
        return null;
    }

    /**
     * Recherche d'une question par l'id d'une proposition
     * 
     * @param id de la proposition
     * @return la question 
     */
    public Question getQuestByPid(int pid)
    {

        for (int i = 0; i < groupes.size(); i++) {
            Groupe g = (Groupe)groupes.get(i);
            for (int j=0; j < g.getQuestions().size(); j++) {
                Question q = (Question)g.getQuestions().get(j);

                for (int k=0; k < q.getPropositions().size(); k++) {
                    Proposition p = (Proposition)q.getPropositions().get(k);

                    // si on trouve le PId, on retourne la question
                    if (p.getId() == pid) return q;
                }       
            }

        }

        //par défaut
        return null;
    }

    /**
     * Recherche d'une proposition par l'id d'une proposition
     * 
     * @param id de la proposition
     * @return la proposition 
     */
    public Proposition getPropByPid(int pid)
    {

        for (int i = 0; i < groupes.size(); i++) {
            Groupe g = (Groupe)groupes.get(i);
            for (int j=0; j < g.getQuestions().size(); j++) {
                Question q = (Question)g.getQuestions().get(j);

                for (int k=0; k < q.getPropositions().size(); k++) {
                    Proposition p = (Proposition)q.getPropositions().get(k);

                    // si on trouve le PId, on retourne la prop.
                    if (p.getId() == pid) return p;
                }       
            }

        }

        //par défaut
        return null;
    }

    /**
     * @return
     */
    public int getNbGroupes()
    {
        return groupes.size();
    }

    /**
     * @return
     */
    public String getTitre()
    {
        return titre;
    }

    /**
     * @param string
     */
    public void setTitre(String string)
    {
        titre = string;
    }

    /**
     * @return
     */
    public String getAuteur()
    {
        return auteur;
    }

    /**
     * @return
     */
    public String getConclusion()
    {
        return conclusion;
    }

    /**
     * @return
     */
    public int getId()
    {
        return id;
    }

    /**
     * @return
     */
    public String getIntroduction()
    {
        return introduction;
    }

    /**
     * @param string
     */
    public void setAuteur(String string)
    {
        auteur = string;
    }

    /**
     * @param string
     */
    public void setConclusion(String string)
    {
        conclusion = string;
    }

    /**
     * @param i
     */
    public void setId(int i)
    {
        id = i;
    }

    /**
     * @param string
     */
    public void setIntroduction(String string)
    {
        introduction = string;
    }

    /**
     * @return Returns the showPwd.
     */
    public boolean showPwd()
    {
        return showPwd;
    }

    /**
     * @param showPwd The showPwd to set.
     */
    public void setShowPwd(boolean showPwd)
    {
        this.showPwd = showPwd;
    }

    /**
     * @return Returns the p_id_titre.
     */
    public int getP_id_titre()
    {
        return p_id_titre;
    }

    /**
     * @param p_id_titre The p_id_titre to set.
     */
    public void setP_id_titre(int p_id_titre)
    {
        this.p_id_titre = p_id_titre;
    }

    /**
     * Alimentation du questionnaire par les données d'un Submitform
     * 
     * @param sf
     */
    public void setFullQuest(SubmitForm sf)
    {


        setS_id(sf.getS_id());
        setS_id_parent(sf.getS_id_parent());
        setS_lock(sf.getLock());
        setS_date(sf.getS_date());


        // init pour l'export XML
        setDate_creation(sf.getS_date());

        for (int k = 0; groupes!= null && k < groupes.size(); k++)
        {
            Groupe gk = getGroupe(k);
            for (int i = 0; i < gk.getNbQuestions(); i++)
            {
                Question qi = gk.getQuestion(i);
                for (int j = 0; j < qi.getNbPropositions(); j++)
                {
                    Proposition pj = qi.getProposition(j);

                    String sval = sf.getPropVal(pj.getId(), pj.getVal()) ;  
                    pj.setsVal(sval);

                    /*
                    // gestion alternative pour permettre le stockage de rval=-9 pour tagger les modifs  + modids du resetFullQuest
                    // impacte le process de mise à jour mais aussi l'export XML ... id : initialiser le form avec -9 ?
                    if (qi.getType()==Question.LIST_FORM) {
                        pj.setsVal(sf.getPropVal( pj.getId(), pj.getVal())) ;
                    } else {
                        if (sf.getPropRVal(pj.getId()) >=0) {
                            pj.setsVal(sf.getPropVal( pj.getId(), sf.getPropRVal(pj.getId()) ));
                        } else {
                            // si la donnée  rval est négatif, on ne cherche que sur le PID
                            pj.setsVal(sf.getPropVal(pj.getId()));
                        }
                        pj.setVal(sf.getPropRVal(pj.getId()));
                    }*/
                }
            }
        }
    }

    /**
     * réinitialisation des réponses associées aux propositions du questionnaire
     * 
     */
    public void resetFullQuest()
    {
        for (int k = 0; groupes !=null && k < groupes.size(); k++)
        {
            Groupe gk = getGroupe(k);
            for (int i = 0; i < gk.getNbQuestions(); i++)
            {
                Question qi = gk.getQuestion(i);
                for (int j = 0; j < qi.getNbPropositions(); j++)
                {
                    Proposition pj = qi.getProposition(j);
                    pj.setsVal(null);

                    //if (qi.getType()!=Question.LIST_FORM) pj.setVal(0);
                }
            }
        }
    }

    /**
     * TRIANGLE 20/04/2005:
     * Test si des champs du questionnaire sont de type obligatoire avec message
     * (MANDATORY_ALERT) et sans réponse associée
     * @return String La liste des champs concernés
     */
    public String checkMandatoryAlert() { 
        return checkMandatoryAlert(false);
    }

    /**
     * Test si des champs du questionnaire sont de type obligatoire avec message
     * (MANDATORY_ALERT) et sans réponse associée
     *
     * @param withTitle affiche dans le message le titre du SF s'il existe
     * @return String La liste des champs concernés
     */
    public String checkMandatoryAlert(boolean withTitle)
    {
        String msg = "";
        String title = null;
        boolean existRep = false;


        for (int k = 0; k < groupes.size(); k++) {
            Groupe gk = getGroupe(k);
            for (int i = 0; i < gk.getNbQuestions(); i++) {
                Question qi = gk.getQuestion(i);
                existRep = false;

                if (qi.getMandatory() < Question.MANDATORY_ALERT) continue;

                for (int j = 0; j < qi.getNbPropositions(); j++) {
                    Proposition pj = qi.getProposition(j);
                    // Mandatory == NO, YES et UNIQUE gérés avant
                    // ALERT -> On affiche

                    if (qi.getMandatory() >= Question.MANDATORY_ALERT
                            && (qi.getType().equals(Question.CHOIX_EXCLUSIF) || qi.getType().equals(Question.CHOIX_MULTIPLE))) {
                        if (pj.getsVal()!=null && pj.getsVal().equals("1") && !Controls.isBlank(pj.getTexte())) 
                            existRep=true;
                    } else if ((qi.getMandatory() >= Question.MANDATORY_ALERT)
                            && Controls.isBlank(pj.getsVal())) {

                        if (msg.equals("")) msg = MSG_MANDATORY ;
                        msg += " - " + qi.getTexte() + " " + pj.getTexte() + "<br/>";
                    }

                    if (withTitle && pj.getId() == getP_id_titre()) {
                        if (pj.getsVal()!=null) title = pj.getsVal();
                    }
                }


                if (qi.getMandatory() >= Question.MANDATORY_ALERT
                        && (qi.getType().equals(Question.CHOIX_EXCLUSIF) || qi.getType().equals(Question.CHOIX_MULTIPLE))
                        && !existRep) {
                    if (msg.equals("")) msg = MSG_MANDATORY ;
                    msg += " - " + qi.getTexte() + "<br/>";
                }

                if (!Controls.isBlank(msg) && title !=null && withTitle) msg = "[Formulaire : " + title + "] " + msg;

            }

        }

        return msg;

    }

    /**
     * @return
     */
    public String checkValues()
    {
        String msg = "";


        for (int k = 0; k < groupes.size(); k++)
        {
            Groupe gk = getGroupe(k);
            for (int i = 0; i < gk.getNbQuestions(); i++)
            {
                Question qi = gk.getQuestion(i);

                for (int j = 0; j < qi.getNbPropositions(); j++)
                {
                    Proposition pj = qi.getProposition(j);

                    String errMsg = null;

                    if (qi.getType().equals(Question.NOMBRE)) errMsg = Controls.checkDouble(pj.getsVal());
                    //if (qi.getType().equals(Question.NOMBRE)) errMsg = Controls.checkInt(pj.getsVal());
                    if (qi.getType().equals(Question.EMAIL)) errMsg = Controls.checkEmail(pj.getsVal());
                    if (qi.getType().equals(Question.DATE)) errMsg = Controls.checkDate(pj.getsVal());
                    if (qi.getType().equals(Question.EURO)) errMsg = Controls.checkEuro(pj.getsVal());
                    if (qi.getType().equals(Question.SIRET)) errMsg = Controls.checkSiret(pj.getsVal());


                    if (msg.equals("") && errMsg != null) msg = MSG_INCORRECT;
                    if (errMsg != null) msg += " - " + qi.getTexte() + " " + pj.getTexte() + " [" + errMsg + "]<br/>";

                }


            }
        }

        return msg;
    }

    /**
     * @return
     */
    public String checkSumsNotSup( 
            int[] tabGid,
            int[] tabQid,
            int[] tabPid,
            int maxGid, 
            int maxQid, 
            int maxPid, 
            int minusGid,
            int minusQid,
            int minusPid)
    {
        String msg = "";
        int sum = 0;
        int max = 0;
        int minus = 0;

        try {
            for (int i=0; i< tabPid.length; i++) {
                sum += Integer.parseInt(getPropValById(tabGid[i],tabQid[i],tabPid[i]));
            }
            max = Integer.parseInt(getPropValById(maxGid,maxQid,maxPid));
            minus = Integer.parseInt(getPropValById(minusGid,minusQid,minusPid));


            if (sum > (max - minus)) 
                msg = "Attention, le total des votes est supérieur au nb d'inscrits !";
        } catch (Exception e) {}

        return msg;
    }



    public String getEmails()
    {
        String emails = "";

        for (int k = 0; k < groupes.size(); k++)
        {
            Groupe gk = getGroupe(k);
            for (int i = 0; i < gk.getNbQuestions(); i++)
            {
                Question qi = gk.getQuestion(i);
                if (qi.getType().equals(Question.EMAIL)) {
                    for (int j = 0; j < qi.getNbPropositions(); j++) {
                        Proposition pj = qi.getProposition(j);
                        if (!Controls.isBlank(pj.getsVal()))
                            emails += pj.getsVal()+";";
                    }
                }
            }
        }
        return emails;
    }


    /**
     * @return Returns the t_id_mail_on_lock.
     */
    public int getT_id_mail_on_lock() {
        return t_id_mail_on_lock;
    }
    /**
     * @param t_id_mail_on_lock The t_id_mail_on_lock to set.
     */
    public void setT_id_mail_on_lock(int t_id_mail_on_lock) {
        this.t_id_mail_on_lock = t_id_mail_on_lock;
    }
    /**
     * @return Returns the t_id_mail_pwd.
     */
    public int getT_id_mail_on_create() {
        return t_id_mail_on_create;
    }
    /**
     * @param t_id_mail_pwd The t_id_mail_pwd to set.
     */
    public void setT_id_mail_on_create(int t_id_mail_on_create) {
        this.t_id_mail_on_create = t_id_mail_on_create;
    }
    /**
     * @return Returns the f_mail_adm.
     */
    public String getF_mail_adm() {
        return f_mail_adm;
    }
    /**
     * @param f_mail_adm The f_mail_adm to set.
     */
    public void setF_mail_adm(String f_mail_adm) {
        this.f_mail_adm = f_mail_adm;
    }
    /**
     * @return Returns the p_id_mail.
     */
    public int getP_id_mail() {
        return p_id_mail;
    }
    /**
     * @param p_id_mail The p_id_mail to set.
     */
    public void setP_id_mail(int p_id_mail) {
        this.p_id_mail = p_id_mail;
    }

    /**
     * @return Returns the date_creation.
     */
    public String getDate_creation() {
        return date_creation;
    }

    /**
     * @param date_creation The date_creation to set.
     */
    public void setDate_creation(String date_creation) {
        this.date_creation = date_creation;
    }

    /**
     * @return Returns the date_debut.
     */
    public String getDate_debut() {
        return date_debut;
    }

    /**
     * @param date_debut The date_debut to set.
     */
    public void setDate_debut(String date_debut) {
        this.date_debut = date_debut;
    }

    /**
     * @return Returns the date_fin.
     */
    public String getDate_fin() {
        return date_fin;
    }

    /**
     * @param date_fin The date_fin to set.
     */
    public void setDate_fin(String date_fin) {
        this.date_fin = date_fin;
    }

    public boolean isActive() {

        if (BasicType.isDateBetween(BasicType.getTodaysDateIso(), 
                date_debut, 
                date_fin)) return true;

        // else
        return false;
    }

    public String getSqlView() {
        StringBuffer sql = new StringBuffer();
        StringBuffer subSql = new StringBuffer();

        subSql.append(" , (select IF(STRCMP(r_text, '1'),  r_text, p_texte) "); 
        subSql.append(" from reponse, proposition, question where "); 
        subSql.append(" reponse.S_ID = submitform.s_id  and ");
        subSql.append(" proposition.p_id = reponse.p_id and ");
        subSql.append(" question.Q_ID = proposition.Q_ID and ");


        sql.append( "select submitform.* ");

        for (int i=0; groupes != null && i < groupes.size(); i++) {
            Groupe g = (Groupe)groupes.get(i);

            for (int j=0; j < g.getNbQuestions(); j++) {
                Question q = g.getQuestion(j);

                if (q.getType().equals(Question.CHOIX_EXCLUSIF)) {

                    sql.append(subSql);
                    sql.append(" proposition.Q_ID = ");
                    sql.append(q.getId());
                    sql.append(" limit 1 ) as q_");
                    sql.append(q.getId());
                } else {

                    for (int k=0; k < q.getNbPropositions(); k++) {
                        Proposition p = q.getProposition(k);

                        sql.append(subSql);
                        sql.append(" proposition.P_ID = ");
                        sql.append(p.getId());
                        sql.append(" limit 1 ) as p_");
                        sql.append(p.getId());

                    }
                }
            }  

        }
        sql.append(" from submitform where f_id =");
        sql.append(id);

        return sql.toString();
    }

    /**
     * @return Returns the f_connected.
     */
    public int getF_connected() {
        return f_connected;
    }

    /**
     * @param f_connected The f_connected to set.
     */
    public void setF_connected(int f_connected) {
        this.f_connected = f_connected;
    }

    /**
     * @return Returns the f_suppr.
     */
    public int getF_suppr() {
        return f_suppr;
    }

    /**
     * @param f_suppr The f_suppr to set.
     */
    public void setF_suppr(int f_suppr) {
        this.f_suppr = f_suppr;
    }

    /**
     * @return Returns the s_id_parent.
     */
    public int getS_id_parent() {
        return s_id_parent;
    }

    /**
     * @param s_id_parent The s_id_parent to set.
     */
    public void setS_id_parent(int s_id_parent) {
        this.s_id_parent = s_id_parent;
    }



    /**
     * @return Returns the s_date.
     */
    public String getS_date() {
        return s_date;
    }

    /**
     * @param s_date The s_date to set.
     */
    public void setS_date(String s_date) {
        this.s_date = s_date;
    }

    /**
     * @return Returns the s_lock.
     */
    public int getS_lock() {
        return s_lock;
    }

    /**
     * @param s_lock The s_lock to set.
     */
    public void setS_lock(int s_lock) {
        this.s_lock = s_lock;
    }

    /**
     * @return Returns the t_id_mail_on_delete.
     */
    public int getT_id_mail_on_delete() {
        return t_id_mail_on_delete;
    }

    /**
     * @param t_id_mail_on_delete The t_id_mail_on_delete to set.
     */
    public void setT_id_mail_on_delete(int t_id_mail_on_delete) {
        this.t_id_mail_on_delete = t_id_mail_on_delete;
    }

    /**
     * @return the f_authent
     */
    public int getF_authent() {
        return f_authent;
    }

    /**
     * @param f_authent the f_authent to set
     */
    public void setF_authent(int f_authent) {
        this.f_authent = f_authent;
    }

    /**
     * @return the p_id_login
     */
    public int getP_id_login() {
        return p_id_login;
    }

    /**
     * @param p_id_login the p_id_login to set
     */
    public void setP_id_login(int p_id_login) {
        this.p_id_login = p_id_login;
    }


    /** CONTROLE DES DROITS 
     * 
     * CREATION / MODIF
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
     *   
     * CONSULTATION
     *  - si le role PUBLIC non connecté : pas d'accès en lecture
     *  - si le role connecté : accès aux submitform fils ou père
     *  (f_connected <= 1)
     *  - si le role CONSULT : accès en lecture à tous les submitform, hors admin & restricted
     *  (f_connected <= 1)
     *  - si le role GESTION : accès en modif à tous les submitform, hors admin & restricted
     *  (f_connected <= 1)
     *  - si le role ADMIN : accès en lecture à tous les submitform
     *  (f_connected 0,1,2,3)
     * 
     * 
     * @param action
     * @param userData
     * @param sf
     * @param sid 
     * @param checkDates : contrôle des dates de validité du formulaire
     * @param extended : contrôle du isItsOwndata en mode etendu (pères, fils & petits fils autorisés)
     * @return null si OK, un message d'erreur sinon
     */
    public String checkAccess(int action, UserData userData, SubmitForm sf, int sid, boolean checkDates, boolean extended ) {
        String message = null;
        boolean access=false;

        /* ADMIN */
        if (userData != null && userData.getRole() == SessionInfos.ROLE_ADMIN ) return null;
        
        /* non Admin - Modif, suppr */
        if    ( (action== ServControl.RMAJ
                || action== ServControl.RDEL 
                || action== ServControl.QUEST)        

                && (
                        // creation mode public (non connecté)
                        (userData != null && !userData.isConnected() 
                        && action== ServControl.QUEST
                        && getF_connected()==Questionnaire.ACCESS_PUBLIC
                        && userData.getRole() >= SessionInfos.ROLE_PUBLIC)

                        || // suppression
                        (userData != null && userData.isConnected() 
                        && action== ServControl.RDEL
                        && sf!=null && !sf.isLocked()
                        && userData.isItsOwnData(sid,extended)
                        && userData.getId() != sid
                        //&& q!=null && q.getF_connected()<=1 
                        && userData.getRole() >= SessionInfos.ROLE_PUBLIC)

                        || // creation + mise à jour (si pas locké & pas en ReadOnly) en mode connecté
                        (userData != null && userData.isConnected()
                        && action != ServControl.RDEL
                        && getF_connected()<= Questionnaire.ACCESS_RESTRICTED       
                        && (sf==null || sf.getS_id()<=0 || (userData.isItsOwnData(sf.getS_id(),extended) && !sf.isLocked() && !userData.isReadOnlyId(sf.getS_id()))  )
                        && userData.getRole() >= SessionInfos.ROLE_PUBLIC ) 

                        || // acces en mode gestion
                        (getF_connected()<=Questionnaire.ACCESS_CONNECTED      
                        && userData != null 
                        && userData.getRole() >= SessionInfos.ROLE_GESTION) 
                        ))
            access = true;


        // consultation
        if ( action==ServControl.RDET && ( 

                // acces public
                (getF_connected()== Questionnaire.ACCESS_PUBLIC)
                || // accès connecté front office (pour users public ou avec droits sur backoffice)
                (userData != null && userData.isConnected()
                && getF_connected() <= Questionnaire.ACCESS_RESTRICTED      
                && userData.isItsOwnData(sid,extended)
                && userData.getRole() <= SessionInfos.ROLE_GESTION ) 
                || // accès connecté backoffice
                (getF_connected() < Questionnaire.ACCESS_RESTRICTED      
                        && userData != null 
                        && userData.getRole() >= SessionInfos.ROLE_CONSULTATION) 
                        || // admin
                        (userData != null && userData.getRole() == SessionInfos.ROLE_ADMIN ) 
                )
                )
            access = true;
        
        // déverrouillage ( = invalider)
        // TODO : prévoir une nouvelle propriété de formulaire de type : "invalidation possible" ? 
        if ( action==ServControl.UNLOCK && ( 

                // acces public
                (getF_connected()== Questionnaire.ACCESS_PUBLIC)
                || // accès connecté front office (pour users public ou avec droits sur backoffice)
                (userData != null && userData.isConnected()
                && getF_connected() <= Questionnaire.ACCESS_RESTRICTED      
                && userData.isItsOwnData(sid,extended)
                && userData.getRole() <= SessionInfos.ROLE_GESTION ) 
                || // accès connecté backoffice
                (getF_connected() < Questionnaire.ACCESS_RESTRICTED      
                        && userData != null 
                        && userData.getRole() >= SessionInfos.ROLE_CONSULTATION) 
                        || // admin
                        (userData != null && userData.getRole() == SessionInfos.ROLE_ADMIN ) 
                )
                )
            access = true;

        // contrôle dates questionnaire
        String now = null;
        if (checkDates) now = BasicType.getTodaysDateIso();

        if (    checkDates && access
                
                &&
                
                (userData == null 
                || (userData != null && userData.getRole() < SessionInfos.ROLE_CONSULTATION )) 

                && (    
                        (!Controls.isBlank(getDate_debut()) 
                                && !getDate_debut().equals("0000-00-00")
                                && BasicType.compareDateIso(getDate_debut(),now)>0)
                                || ( !Controls.isBlank(getDate_fin()) 
                                        && getDate_fin().equals("0000-00-00")
                                        &&  BasicType.compareDateIso(now,getDate_fin())>0))) {

            message = MSG_HORS_PERIODE;
            access = false;

            if (!Controls.isBlank(getDate_debut())) 
                message = message.replaceAll("&1", BasicType.formatDateIsoToLocal(getDate_debut(),null)); 
            if (!Controls.isBlank(getDate_fin()) &&  !getDate_fin().equals("0000-00-00")) 
                message = message.replaceAll("&2", BasicType.formatDateIsoToLocal(getDate_fin(),null));  

        }
        
        

        if (access) {
            // accès OK
            return null; 
        } else if (message !=null) {
            return message;
        } else {
            return MSG_DROITS_INSUFFISANTS;
        }
    }


}