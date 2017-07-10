/*
 * Créé le 18 oct. 04
 * 
 * Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
package formOnLine.actions;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

import com.triangle.lightfw.ActionBean;
import com.triangle.lightfw.BasicType;
import com.triangle.lightfw.SessionInfos;
import com.triangle.lightfw.ValueBean;
import com.triangle.lightfw.ValueBeanList;

import formOnLine.BasicTools;
import formOnLine.Controls;
import formOnLine.ServControl;
import formOnLine.msBeans.*;

/**
 * @author 
 * 
 * Pour changer le modèle de ce commentaire de type généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class QuestionnaireAction extends ActionBean
{

    public static String MSG_CHAMP_OBLIGATOIRE = 
            "Attention, la valeur suivante existe déjà dans la base : ";

    /**
     * Charge un questionnaire à partir du résultat d'une requête
     */
    public ValueBean populateBean(ResultSet rs) throws SQLException
    {
        Questionnaire quest = new Questionnaire();

        quest.setId(rs.getInt("f_id"));
        //quest.setAuteur(BasicType.trim(rs.getString("f_auteur")));
        quest.setConclusion(BasicType.trim(rs.getString("f_conclusion")));
        quest.setIntroduction(BasicType.trim(rs.getString("f_introduction")));
        quest.setTitre(BasicType.trim(rs.getString("f_titre")));
        quest.setShowPwd((rs.getInt("f_show_pwd") == 1));
        quest.setP_id_titre(rs.getInt("p_id_titre"));
        quest.setT_id_mail_on_create(rs.getInt("t_id_mail_on_create"));
        quest.setT_id_mail_on_lock(rs.getInt("t_id_mail_on_lock"));
        quest.setT_id_mail_on_delete(rs.getInt("t_id_mail_on_delete"));
        quest.setP_id_mail(rs.getInt("p_id_mail"));
        quest.setP_id_login(rs.getInt("p_id_login"));
        quest.setF_mail_adm(BasicType.trim(rs.getString("f_mail_adm")));
        quest.setDate_creation(rs.getString("f_datecreation"));
        quest.setDate_debut(rs.getString("f_datedeb"));
        quest.setDate_fin(rs.getString("f_datefin"));

        quest.setF_connected(rs.getInt("f_connected"));
        quest.setF_suppr(rs.getInt("f_suppr"));

        quest.setF_authent(rs.getInt("f_authent"));

        // pour compatibilité avec Mysql 5
        if (quest.getDate_debut()!=null && 
                (quest.getDate_debut().equals("null") ||  quest.getDate_debut().equals("0000-00-00"))) 
            quest.setDate_debut("");
        if (quest.getDate_fin()!=null && 
                (quest.getDate_fin().equals("null") || quest.getDate_fin().equals("0000-00-00"))) 
            quest.setDate_fin("");


        return quest;
    }

    /** selection par ID
     * 
     * @param id
     * @return
     * @throws SQLException
     */
    public Questionnaire selectById(int id) throws SQLException
    {
        StringBuffer sql = new StringBuffer("SELECT * FROM formulaire WHERE f_id=")
        .append(id);
        Questionnaire result = (Questionnaire)selectOne(sql.toString());

        return result;
    }
    
    
    /** selection par le dernier ID créé
     * 
     * @param id
     * @return
     * @throws SQLException
     */
    public Questionnaire selectByMaxId() throws SQLException
    {
        StringBuffer sql = new StringBuffer("SELECT * FROM formulaire  order by f_id desc limit 1 ");
        Questionnaire result = (Questionnaire)selectOne(sql.toString());

        return result;
    }
    


    /** selectionne tous les formulaires
     * 
     * @return
     * @throws SQLException
     */
    public ValueBeanList selectAll() throws SQLException
    {
        StringBuffer sql = new StringBuffer(
                "SELECT * FROM formulaire ORDER BY f_id");
        ValueBeanList listeQuests = selectMultiple(sql.toString());

        return listeQuests;
    }

    /**
     * Chargement complet du formulaire avec ses groupes de questions, questions et propositions
     * avec initialisation des réponses aux propositions en fonction du SF en paramètre
     * 
     * @param id
     * @param sf
     * @return
     * @throws SQLException
     */
    public Questionnaire getFullQuest(int fid, SubmitForm sf) throws SQLException
    {
        GroupeAction ga = new GroupeAction();
        QuestionAction qa = new QuestionAction();
        PropositionAction pa = new PropositionAction();

        if (fid<0) return null;

        Questionnaire fullQ = selectById(fid);
        ValueBeanList groups = ga.selectAllByFormId(fid);
        ValueBeanList quests = qa.selectAllByFid(fid);
        ValueBeanList props =  pa.selectAllByFid(fid, null);

        if (groups != null) {
            for (Iterator<ValueBean> iterG = groups.iterator(); iterG.hasNext();) {
                Groupe g = (Groupe)iterG.next();

                fullQ.addGroupe(g);

                if (quests != null) {
                    for (Iterator<ValueBean> iterQ = quests.iterator(); iterQ.hasNext();) {
                        Question q = (Question)iterQ.next();

                        if (q.getG_id() == g.getId()) {
                            g.addQuestion(q);

                            // Cas d'une liste de valeurs à partir d'une liste dynamique de formulaires
                            // (ce sont les titres des submitform qui vont remplacer les propositions, 
                            // et les Ids des SF seront stockés dans le champ numérique "val" de la proposition)
                            if (q.getType().equals(Question.LIST_FORM)  ) {

                                // le numéro de la proposition du titre des formulaires fils est stocké dans
                                // le champ "num" de la proposition de la question
                                int pid = -1;
                                Proposition propIdTitre = null;

                                for (Iterator<ValueBean> iterP = props.iterator(); iterP.hasNext();) {
                                    Proposition prop = (Proposition)iterP.next();
                                    if (prop.getQid() == q.getId()) {
                                        propIdTitre = prop;
                                        pid =propIdTitre.getNum(); 
                                        break;
                                    }
                                }
                                if (pid>0) {
                                    ValueBeanList listRep = new ReponseAction().selectAllByPId(pid,true);

                                    if (listRep !=null) {
                                        for (ValueBean rep : listRep) {
                                            Proposition p = new Proposition( propIdTitre.getId(), ((Reponse)rep).getSVal(), 0, 0);
                                            p.setQid(propIdTitre.getQid());
                                            p.setVal(((Reponse)rep).getS_id());

                                            // recherche d'une réponse sur le "pid" ET le "r_val"
                                            if (sf!=null) p.setsVal(sf.getPropVal(p.getId(),p.getVal()));

                                            q.addProposition(p);
                                        }
                                    }

                                }

                            } else {

                                // question de type "standard"
                                // >> Ajout des propositions à la question, 
                                // avec alimentation des valeurs des réponses correspondant aux propositions
                                if (props != null) {
                                    for (Iterator<ValueBean> iterP = props.iterator(); iterP.hasNext();) {
                                        Proposition p = (Proposition)iterP.next();
                                        if (p.getQid() == q.getId()) {
                                            if (sf != null) {
                                                p.setsVal(sf.getPropVal(p.getId()));
                                                p.setVal(sf.getPropRVal(p.getId()));
                                            }
                                            q.addProposition(p);
                                        }
                                    }
                                }
                            }

                        }

                    }
                }

            }
        }

        // finalisation initialisation
        if (sf!=null) {
            fullQ.setS_id(sf.getS_id());
            fullQ.setS_id_parent(sf.getS_id_parent());
            fullQ.setS_lock(sf.getLock());
            fullQ.setS_date(sf.getS_date());
        }

        return fullQ;
    }


    /**
     * Chargement complet du formulaire avec ses groupes de questions, questions et propositions
     * avec initialisation des réponses aux propositions en fonction du SF en paramètre
     * 
     * @param id
     * @param sf
     * @return
     * @throws SQLException

    public Questionnaire getFullQuestOld(int id, SubmitForm sf) throws SQLException
    {
        GroupeAction ga = new GroupeAction();
        QuestionAction qa = new QuestionAction();
        PropositionAction pa = new PropositionAction();

        if (id<0) return null;

        Questionnaire fullQ = selectById(id);
        ValueBeanList groups = ga.selectAllByFormId(id);
        if (groups != null)
        {
            for (Iterator iterG = groups.iterator(); iterG.hasNext();)
            {
                Groupe gr = (Groupe)iterG.next();
                ValueBeanList quests = qa.selectAllByGroupId(gr.getId());
                if (quests != null)
                {
                    for (Iterator iterQ = quests.iterator(); iterQ.hasNext();)
                    {
                        Question qu = (Question)iterQ.next();

                        ValueBeanList props = null;

                        // alimentation des propositions 
                        props = pa.selectAllByQuestId(qu.getId());

                        // Cas d'une liste de valeurs d'un autre form
                        if (qu.getType().equals(Question.LIST_FORM) && props != null) {
                            Proposition prop = (Proposition)props.get(0);
                            int pid = prop.getNum();
                            if (pid>0) {
                                ValueBeanList listRep = new ReponseAction().selectAllByPId(pid,true);
                                // réinit pour remplacer par les valeurs trouvées dans la base
                                props = new ValueBeanList();

                                if (listRep !=null) {
                                    for (ValueBean rep : listRep) {
                                        Proposition p = new Proposition( prop.getId(), ((Reponse)rep).getSVal(), 0, 0);
                                        p.setQid(prop.getQid());
                                        p.setVal(((Reponse)rep).getS_id());
                                        props.add(p);
                                    }
                                }
                            }

                        }


                        // Ajout des propostions à la question, 
                        // avec alimentation des valeurs des réponses correspondant aux propositions
                        if (props != null) {
                            for (Iterator iterP = props.iterator(); iterP.hasNext();) {

                                Proposition pr = (Proposition)iterP.next();
                                if (sf != null) {
                                    //Cas d'une liste de valeurs d'un autre form 
                                    if (qu.getType().equals(Question.LIST_FORM) ) {
                                        // recherche d'une réponse sur le "pid" ET le "r_val"
                                        pr.setsVal(sf.getPropVal(pr.getId(),pr.getVal()));
                                    } else {
                                        //cas normal
                                        //pr.setsVal(sf.getPropVal(pr.getId());
                                        pr.setsVal(sf.getPropVal(pr.getId(),pr.getVal()));
                                    }
                                }

                                qu.addProposition(pr);
                            }
                        }
                        gr.addQuestion(qu);
                    }
                }
                fullQ.addGroupe(gr);
            }
        }

        // finalisation initialisation
        if (sf!=null) {
            fullQ.setS_id(sf.getS_id());
            fullQ.setS_id_parent(sf.getS_id_parent());
            fullQ.setS_lock(sf.getLock());
            fullQ.setS_date(sf.getS_date());
        }

        return fullQ;
    }*/

    public Questionnaire getFullQuest(int id) throws SQLException
    {
        return getFullQuest(id, null);
    }



    /**
     * TRIANGLE 20/04/2005: Vérifie l'existance de doublons pour les champs qui
     * doivent être unique (mandatory = 3). 
     * 
     * @param form formulaire à tester
     * @param sid Id du SubmitForm (Réponse du formulaire)
     * @return String Message "" si pas de dopublons, ou message à afficher
     * @throws SQLException
     */
    public String checkMandatoryUnique(Questionnaire form, int sid) throws SQLException {
        String msg = "";

        for (int k = 0; k < form.getGroupes().size(); k++) {            
            Groupe gk = form.getGroupe(k);

            for (int i = 0; i < gk.getNbQuestions(); i++) {                
                Question qi = gk.getQuestion(i);

                for (int j = 0; j < qi.getNbPropositions(); j++) {                    
                    Proposition pj = qi.getProposition(j);
                    // Mandatory == UNIQUE -> Vérification sur la base
                    if ((qi.getMandatory() == Question.MANDATORY_UNIQUE)
                            && ((new ReponseAction()).existeReponse(sid, pj.getId(), pj
                                    .getsVal()))) {
                        msg = BasicTools.htmlConvert(MSG_CHAMP_OBLIGATOIRE + qi.getTexte());

                        break;
                    }
                }
            }
        }

        return msg;
    }

    

    /** mise à jour 
     * 
     * @param f
     * @return
     * @throws SQLException
     */
    public int update (Questionnaire f) throws SQLException {

        StringBuffer sql = new StringBuffer();

        sql.append(" UPDATE formulaire SET " );

        sql.append(" F_TITRE ='"+BasicType.quoteQuote(f.getTitre()));
        sql.append("' ,F_INTRODUCTION ='"+BasicType.quoteQuote(f.getIntroduction()));
        sql.append("' ,F_CONCLUSION ='"+BasicType.quoteQuote(f.getConclusion()));
        sql.append("' ,F_DATEDEB ='"+f.getDate_debut());
        sql.append("' ,F_DATEFIN ='"+f.getDate_fin());
        sql.append("' ,F_SHOW_PWD ="+(f.showPwd()?"1":"0"));
        sql.append(" ,P_ID_TITRE ="+f.getP_id_titre());
        sql.append(" ,t_id_mail_on_lock ="+f.getT_id_mail_on_lock());
        sql.append(" ,t_id_mail_on_create ="+f.getT_id_mail_on_create());
        sql.append(" ,t_id_mail_on_delete ="+f.getT_id_mail_on_delete());
        sql.append(" ,F_MAIL_ADM ='"+f.getF_mail_adm());
        sql.append("' ,P_ID_MAIL ="+f.getP_id_mail());
        sql.append(" ,f_connected ="+f.getF_connected());
        sql.append(" ,f_suppr ="+f.getF_suppr());
        sql.append(" ,f_authent ="+f.getF_authent());

        sql.append(" WHERE f_id = "+f.getId());

        return updateData(sql.toString());


    }

    /** insertion 
     * 
     * @param f
     * @return
     * @throws SQLException
     */
    public int insert (Questionnaire f) throws SQLException {

        StringBuffer sql = new StringBuffer();

        sql.append(" INSERT INTO formulaire (" );

        sql.append(" F_TITRE,  F_INTRODUCTION, F_CONCLUSION,  ");
        sql.append(" F_DATEDEB , F_DATEFIN , F_SHOW_PWD, P_ID_TITRE, ");
        sql.append(" t_id_mail_on_lock ,t_id_mail_on_create,t_id_mail_on_delete, ");
        sql.append(" F_MAIL_ADM, P_ID_MAIL, F_DATECREATION, F_CONNECTED, F_SUPPR, F_AUTHENT ");

        sql.append(") VALUES (");

        sql.append("'"+BasicType.quoteQuote(f.getTitre())+"', ");
        sql.append("'"+BasicType.quoteQuote(f.getIntroduction())+"', ");
        sql.append("'"+BasicType.quoteQuote(f.getConclusion())+"', ");
        sql.append("'"+f.getDate_debut()+"', ");
        sql.append("'"+f.getDate_fin()+"', ");
        sql.append((f.showPwd()?"1, ":"0, "));
        sql.append(f.getP_id_titre()+", ");
        sql.append(f.getT_id_mail_on_lock()+", ");
        sql.append(f.getT_id_mail_on_create()+", ");
        sql.append(f.getT_id_mail_on_delete()+", ");
        sql.append("'"+f.getF_mail_adm()+"', ");
        sql.append(f.getP_id_mail());
        sql.append(",'" + BasicType.getTodaysDateIso()+"',");
        sql.append(f.getF_connected()+ ",");
        sql.append(f.getF_suppr()+ ",");
        sql.append(f.getF_authent());

        sql.append(") ");

        return updateData(sql.toString());


    }


}