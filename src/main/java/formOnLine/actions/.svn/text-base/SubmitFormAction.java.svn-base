/*
 * Créé le 18 oct. 04
 *
 * Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
package formOnLine.actions;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.triangle.lightfw.ActionBean;
import com.triangle.lightfw.BasicType;
import com.triangle.lightfw.ValueBean;
import com.triangle.lightfw.ValueBeanList;

import formOnLine.BasicTools;
import formOnLine.Controls;
import formOnLine.FormatExport;
import formOnLine.InitServlet;
import formOnLine.Mailer;
import formOnLine.msBeans.*;




/**
 * @author SeLERIDON
 *
 */
public class SubmitFormAction extends ActionBean {

    /**
     * Charge un questionnaire à partir du résultat d'une requête
     */
    /* (non-Javadoc)
     * @see com.triangle.lightfw.ActionBean#populateBean(java.sql.ResultSet)
     */
    public ValueBean populateBean(ResultSet rs) throws SQLException
    {
        SubmitForm s = new SubmitForm();

        s.setF_id(rs.getInt("f_id"));
        s.setS_id(rs.getInt("s_id"));
        s.setPwd(BasicType.trim(rs.getString("s_pwd")));
        s.setS_date(BasicType.trim(rs.getString("s_date")));
        s.setS_date_creat(BasicType.trim(rs.getString("s_date_creat")));
        s.setS_login_maj(rs.getString("s_login_maj"));
        s.setS_lock(rs.getInt("s_lock"));
        try {
            s.setTitre(BasicType.trim(rs.getString("titre")));
            s.setTitle(s.getTitre());
        } catch (SQLException e) {}
        try {
            s.setS_id_parent(rs.getInt("s_id_parent"));
        } catch (SQLException e) {}

        return s;
    }


    /**
     * @param sf
     * @return
     * @throws SQLException
     */
    public int insert(SubmitForm sf, String login) throws SQLException
    {
        StringBuffer sql =
                new StringBuffer("INSERT INTO submitform (f_id, s_date, s_pwd, s_id_parent, s_date_creat, s_login_maj, s_lock) ");
        sql.append("VALUES (").append(sf.getF_id());
        sql.append(",'").append(sf.getS_date());
        sql.append("','").append(sf.getPwd());
        sql.append("',").append(sf.getS_id_parent());
        sql.append(",'").append(BasicType.getTodaysDateIso(true)).append("'");
        sql.append(",'").append(login).append("'");
        sql.append(",").append(sf.getLock());
        sql.append(")");

        return updateData(sql.toString());



    }

    /**
     * @param sf
     * @return
     * @throws SQLException
     */
    public int insertFull(SubmitForm sf) throws SQLException
    { return insertFull(sf, false); }


    /**
     * Insertion complète d'un formulaire en base, avec ses réponses
     * 
     * @param sf
     * @return l'id du formulaire
     * @throws SQLException
     */
    public int insertFull(SubmitForm sf, boolean isRepOnly) throws SQLException
    {

        if (!isRepOnly) {


            // attention, insertion par REPLACE 
            // = (pas d'alerte si le sid existe déjà, remplacement)

            StringBuffer sql =
                    new StringBuffer("REPLACE INTO submitform ( f_id, s_date, s_pwd, s_id_parent, s_date_creat " );
            if (sf.getS_id()>0) sql.append( ",s_id ");
            if (sf.getLock()>0) sql.append( ",s_lock ");

            sql.append(") VALUES (");

            sql.append(sf.getF_id());
            sql.append(",'").append(sf.getS_date());
            sql.append("','").append(sf.getPwd());
            sql.append("',").append(sf.getS_id_parent());
            sql.append(",'").append(BasicType.getTodaysDateIso(true)).append("'");
            if (sf.getS_id()>0) sql.append( ", ").append(sf.getS_id());
            if (sf.getLock()>0) sql.append( ", ").append(sf.getLock());

            sql.append(")");

            updateData(sql.toString());

        }


        if (!sf.getReponses().isEmpty()) {

            ReponseAction ra = new ReponseAction(); 

            if (sf.getS_id()==-1) {
                // récupération de l'id généré en autoincrément par mysql
                SubmitForm sfNew = selectByDateAndPwdAndFid(sf.getS_date(),sf.getPwd(),sf.getF_id());
                if (sfNew != null) sf.setS_id(sfNew.getS_id());
            }
            for (int i=0;sf.getS_id()>0 && i<sf.getReponses().size();i++) {
                Reponse r = sf.getReponse(i);
                r.setS_id(sf.getS_id());

                // insertion réponse
                // (test s'il y a une valeur , sinon ce n'est pas la peine de faire l'insertion)
                if (!Controls.isBlank(r.getSVal())) ra.insert(r);
            }
        }
        return sf.getS_id();

    }


    /**
     * Insertion complète d'une copie d'un formulaire en base, avec ses réponses
     * 
     * @param sf
     * @return 1
     * @throws SQLException
     */
    public int copyFull(SubmitForm sf, String login, boolean locked) throws SQLException
    {

        SubmitForm newSf = sf.copy();

        newSf.setS_id(-1);
        String date = BasicType.getTodaysDateIso();
        String pwd = BasicTools.getPwd(true,true,true,8);
        newSf.setS_date(date);
        newSf.setS_date_creat(date);
        newSf.setPwd(pwd);

        newSf.setS_lock((locked?1:0)) ;
        if (login !=null) {
            newSf.setS_login_maj(login) ;
        } else {
            newSf.setS_login_maj(sf.getS_login_maj());
        }


        return insertFull(newSf, false);

    }


    /**
     * Suppression de tous les formulaires d'un type donné... 
     * ... A utiliser avec grande précaution...
     * 
     * @param f_id
     * @return
     * @throws SQLException
     */
    public int deleteAllByFid(int f_id) throws SQLException
    {

        StringBuffer sql =
                new StringBuffer("DELETE FROM reponse WHERE s_id in (select s_id FROM submitform WHERE f_id=" + f_id + ")");
        int r1 = updateData(sql.toString());

        sql =
                new StringBuffer("DELETE FROM submitform WHERE f_id=" + f_id);
        int r2 = updateData(sql.toString());

        return r1 + r2;
    }

    /**
     * @param sf
     * @return
     * @throws SQLException
     */
    public int delete(SubmitForm sf) throws SQLException
    {
        StringBuffer sql =
                new StringBuffer("DELETE FROM submitform WHERE s_id=");
        sql.append(sf.getS_id());

        int r1 = updateData(sql.toString());

        sql =
                new StringBuffer("DELETE FROM reponse WHERE s_id=");
        sql.append(sf.getS_id());

        int r2 = updateData(sql.toString());

        return r1 + r2;
    }

    /**
     * @param sf
     * @param date
     * @return
     * @throws SQLException
     */
    public int updateDate(SubmitForm sf, String date) throws SQLException
    {
        StringBuffer sql =
                new StringBuffer("UPDATE submitform SET s_date='");
        sql.append(date).append("' WHERE s_id=").append(sf.getS_id());

        return updateData(sql.toString());
    }

    /**
     * met à jour la somme de la longueur des valeurs des réponses dans une réponse
     * (pour contrôle % modification)
     * 
     * @param sf
     * @param pid
     * @return
     * @throws SQLException
     */
    public int updateCheckSum(SubmitForm sf, int pid) throws SQLException
    {
        StringBuffer sql =
                new StringBuffer("REPLACE INTO reponse (  s_id, p_id, r_text) ");
        sql.append("(select r.s_id, ").append(pid).append(" , sum( length(r_text) ) from  reponse r")
        .append(" WHERE r.s_id=").append(sf.getS_id()).append(" group by r.s_id ) ");


        return updateData(sql.toString());
    }

    /**
     * @param sf
     * @param sid parent
     * @return
     * @throws SQLException
     */
    public int updateSidParent(SubmitForm sf) throws SQLException
    {
        StringBuffer sql =
                new StringBuffer("UPDATE submitform SET s_id_parent=");
        sql.append(sf.getS_id_parent()).append(" WHERE s_id=").append(sf.getS_id());

        return updateData(sql.toString());
    }

    /**
     * @param sid
     * @return
     * @throws SQLException
     */
    public int lock(int sid, String login) throws SQLException
    {
        String date = BasicType.getTodaysDateIso(true);
        StringBuffer sql =
                new StringBuffer("update submitform set s_lock=1,s_date='");
        sql.append(date).append("' , s_login_maj ='").append(login).append("' where s_id=").append(sid);

        return updateData(sql.toString());
    }

    /**
     * @param sid
     * @return
     * @throws SQLException
     */
    public int unLock(int sid, String login) throws SQLException
    {
        String date = BasicType.getTodaysDateIso(true);

        StringBuffer sql =
                new StringBuffer("update submitform set s_lock=0,s_date='");
        sql.append(date).append("' , s_login_maj ='").append(login).append("' where s_id=").append(sid);

        return updateData(sql.toString());
    }


    /**
     * @param fid
     * @param s_id_parent
     * @return
     * @throws SQLException
     */
    public SubmitForm newSubForm(int fid, int s_id_parent, String login, int locked) throws SQLException {
        SubmitForm sf = new SubmitForm();
        String date = BasicType.getTodaysDateIso();
        String pwd = BasicTools.getPwd(true,true,true,8); //pass pas uniquement en chiffres 

        sf.setPwd(pwd);
        sf.setF_id(fid);
        sf.setS_date(date);
        sf.setS_id_parent(s_id_parent);
        sf.setS_lock(locked);
        
        //insertion sql
        insert(sf, login);

        //récupération de l'id généré en autoincrément par mysql
        sf = selectByDateAndPwdAndFid(date,pwd,fid);

        return sf;
    }

    /**
     * @param fid
     * @return
     * @throws SQLException
     */
    public SubmitForm newSubForm(int fid, String login) throws SQLException {
        return newSubForm(fid,-1, login, 0);
    }

    /**
     * Mise à jour des réponses du formulaire SF par les reponses contenues dans SFnew
     * 
     * @param sf
     * @param sfNew formulaire contenant les nouvelles reponses
     * @throws SQLException
     */
    public void updateSubForm(SubmitForm sf, SubmitForm sfNew ) throws SQLException {

        ReponseAction ra = new ReponseAction(); 

        /* (mise à jour de la date au lock seulement)
         String date = BasicType.getTodaysDateIso();
         sf.setS_date(date); 
         updatedate(sf,date); */

        // mise à jour s_id_parent (s'il existe)
        if (selectBySidAndPwd(sf.getS_id_parent(),null,false,false, false) != null) {
            updateSidParent(sf);
        }


        //effacement des réponses
        ra.deleteAll(sf);

        if (sfNew !=null ) sf.setReponses(sfNew.getReponses());

        for (int i=0;i<sf.getReponses().size();i++) {
            Reponse r = sf.getReponse(i);
            r.setS_id(sf.getS_id());

            // insertion réponse
            // (test s'il y a une valeur , sinon ce n'est pas la peine de faire l'insertion)
            if (!Controls.isBlank(r.getSVal())) ra.insert(r);
        }


    }

    /**
     * Mise à jour des réponses du formulaire
     * 
     * @param sf
     * @throws SQLException
     */
    public void updateSubForm(SubmitForm sf ) throws SQLException {

        updateSubForm(sf, null);

    }


    /**
     * @param sid
     * @param pwd
     * @param pwdMandatory
     * @return
     * @throws SQLException
     */
    public SubmitForm selectBySidAndPwd(int sid, String pwd, boolean pwdMandatory) throws SQLException    { 
        return selectBySidAndPwd(sid, pwd, pwdMandatory, true, false);
    }

    /**
     * Contrôle pour authentification
     * 
     * @param sid
     * @param pwd
     * @param pwdMandatory true pour le controle du mot de passe
     * @param withReponses  pour alimenter les propositions du sf
     * @param checkIfFormIsAuthentForm : si oui, contrôle si le formulaire est un form  d'authentification
     * @return
     * @throws SQLException
     */
    public SubmitForm selectBySidAndPwd(int sid, String pwd, boolean pwdMandatory, boolean withReponses, boolean checkIfFormIsAuthentForm) throws SQLException
    {
        StringBuffer sql =  new StringBuffer();
        //"SELECT * FROM submitform WHERE s_id=").append(sid);

        sql.append("SELECT s.*, rt.r_text titre " +
                " FROM submitform s " +
                " LEFT OUTER JOIN formulaire f ON s.F_ID = f.F_ID " +
                " LEFT OUTER JOIN reponse rt ON rt.P_ID = f.P_ID_TITRE " +
                " AND s.S_ID = rt.S_ID " +
                " WHERE  s.s_id = " + sid);

        if (checkIfFormIsAuthentForm) sql.append(" and f.f_authent = 1 ");

        if (pwd==null) pwd="";
        if (pwdMandatory) sql.append( " and s.s_pwd='"+ BasicType.quoteQuote(pwd)+"'");

        SubmitForm s = (SubmitForm)selectOne(sql.toString());

        if (s==null) return null;

        if (withReponses) {
            
            // alimentation propositions
            ReponseAction ra = new ReponseAction();
            ValueBeanList listRep = ra.selectAllBySId(sid);

            if (listRep==null) return null;

            for (int i =0; i<listRep.size();i++) {
                Reponse r = (Reponse)listRep.get(i);
                if (r!=null) s.addReponse((Reponse)listRep.get(i));
            }
        }

        return s;
    }

    
    /**
     * Contrôle pour authentification sur login stocké en proposition principale de formulaire d'authentification
     * 
     * @param sid
     * @param pwd
     * @param pwdMandatory true pour le controle du mot de passe
     * @param withProps  pour alimenter les propositions du sf
     * @param checkIfFormIsAuthentForm : si oui, contrôle si le formulaire est un form  d'authentification
     * @return
     * @throws SQLException
     */
    public SubmitForm selectByAuthProps(String login,  boolean withProps) throws SQLException
    {
        StringBuffer sql =  new StringBuffer();
        
        sql.append("SELECT s.*, rt.r_text titre " +
                " FROM submitform s " +
                " LEFT OUTER JOIN formulaire f ON s.F_ID = f.F_ID " +
                " LEFT OUTER JOIN reponse rt ON rt.P_ID = f.P_ID_LOGIN " +
                " AND s.S_ID = rt.S_ID " +
                " WHERE rt.r_text = '" + login + "' ");

        sql.append(" and f.f_authent = 1 ");

        
        SubmitForm s = (SubmitForm)selectOne(sql.toString());

        if (s==null) return null;

        if (withProps) {
            
            // alimentation propositions
            ReponseAction ra = new ReponseAction();
            ValueBeanList listRep = ra.selectAllBySId(s.getS_id());

            if (listRep==null) return null;

            for (int i =0; i<listRep.size();i++) {
                Reponse r = (Reponse)listRep.get(i);
                if (r!=null) s.addReponse((Reponse)listRep.get(i));
            }
        }

        return s;
    }
    
    /**
     * Contrôle pour authentification sur SID de formulaire d'authentification
     * 
     * @param sid
     * @param pwd
     * @param pwdMandatory true pour le controle du mot de passe
     * @param withProps  pour alimenter les propositions du sf
     * @param checkIfFormIsAuthentForm : si oui, contrôle si le formulaire est un form  d'authentification
     * @return
     * @throws SQLException
     */
    public SubmitForm selectByAuthSid(int sid,  boolean withProps) throws SQLException
    {
        StringBuffer sql =  new StringBuffer();
        
        sql.append("SELECT s.*, rt.r_text titre " +
                " FROM submitform s " +
                " LEFT OUTER JOIN formulaire f ON s.F_ID = f.F_ID " +
                " LEFT OUTER JOIN reponse rt ON rt.P_ID = f.P_ID_LOGIN " +
                " AND s.S_ID = rt.S_ID " +
                " WHERE s.s_id = " + sid);

        sql.append(" and f.f_authent = 1 ");

        
        SubmitForm s = (SubmitForm)selectOne(sql.toString());

        if (s==null) return null;

        if (withProps) {
            
            // alimentation propositions
            ReponseAction ra = new ReponseAction();
            ValueBeanList listRep = ra.selectAllBySId(s.getS_id());

            if (listRep==null) return null;

            for (int i =0; i<listRep.size();i++) {
                Reponse r = (Reponse)listRep.get(i);
                if (r!=null) s.addReponse((Reponse)listRep.get(i));
            }
        }

        return s;
    }
    
    
    
    /**
     * @param sid
     * @return
     * @throws SQLException
     */
    public SubmitForm selectBySId(int sid) throws SQLException  {
        return selectBySidAndPwd(sid,"",false);
    }

    /**
     * @param pid
     * @param val
     * @return
     * @throws SQLException
     */
    public SubmitForm selectByPid(int pid, String val) throws SQLException
    {
        return selectByPid(pid,val,false);
    }


    /**
     * Recherche d'un submitform par la réponse à une proposition
     *  
     * @param pid
     * @param val
     * @param full si vrai, renvoie les réponses avec le submitform
     * @return le submitform
     * @throws SQLException
     */
    public SubmitForm selectByPid(int pid, String val, boolean full) throws SQLException
    {
        StringBuffer sql =
                new StringBuffer("SELECT s.*, r.r_text titre FROM submitform s, reponse r WHERE p_id=").append(pid);
        sql.append(" and s.s_id = r.s_id ");
        sql.append(" and r_text='").append(val).append("'");

        SubmitForm s = (SubmitForm)selectOne(sql.toString());

        if (s==null) return null;

        if (!full) { 
            return s ;
        } else {
            // alimentation propositions
            ReponseAction ra = new ReponseAction();
            ValueBeanList listRep = ra.selectAllBySId(s.getS_id());

            if (listRep==null) return null;

            for (int i =0; i<listRep.size();i++) {
                Reponse r = (Reponse)listRep.get(i);
                if (r!=null) s.addReponse((Reponse)listRep.get(i));
            }

            return s;
        }
    }



    /**
     * Sélection de formulaires correspondant à un champ de type FORM_ID
     * 
     * @param pid
     * @param val
     * @param r_val
     * @param full si vrai, renvoi des formulaires chargés de leurs réponses
     * @return liste de submitforms
     * @throws SQLException
     */
    public ValueBeanList selectAllRefairsBySidAndPidAndVal(int sid, int pid, String val, boolean full) throws SQLException
    {
        ValueBeanList vbl = null;


        /*SELECT s.*, r.r_text titre 
        FROM submitform s, formulaire f, reponse r 
        WHERE s.s_id = r.s_id and f.f_id = s.f_id and f.p_id_titre = r.p_id
        and  s.s_id in (select r_val from reponse where p_id =1958 and r_text=1  and s_id=200083)
         order by r.r_text ;*/

        StringBuffer sql =
                new StringBuffer("SELECT s.*, r.r_text titre FROM submitform s, formulaire f, reponse r WHERE ");
        sql.append(" s.s_id = r.s_id and f.f_id = s.f_id and f.p_id_titre = r.p_id  ");

        sql.append(" and  s.s_id in (select r_val from reponse where p_id =").append(pid);
        if (!Controls.isBlank(val)) sql.append(" and r_text='").append(val).append("'");
        if (sid>0) sql.append(" and s_id =").append(sid).append(") ");



        sql.append(" order by r.r_text ");

        vbl = selectMultiple(sql.toString());

        if (vbl==null) return null;

        if (!full) { 
            return vbl ;
        } else {
            // alimentation propositions
            ReponseAction ra = new ReponseAction();

            for (int i =0; i<vbl.size(); i++) {

                SubmitForm s = (SubmitForm)vbl.get(i);
                ValueBeanList listRep = ra.selectAllBySId(s.getS_id());

                if (listRep==null) return null;

                for (int j =0; j<listRep.size();j++) {
                    Reponse r = (Reponse)listRep.get(j);
                    if (r!=null) s.addReponse(r);
                }
            }

            return vbl;
        }
    }   


    /**
     * Sélection de formulaires par l'existence d'une reponse 
     * correspondant aux critères pid, val, r_val
     * 
     * @param pid
     * @param val
     * @param r_val
     * @param full si vrai, renvoi des formulaires chargés de leurs réponses
     * @return liste de submitforms
     * @throws SQLException
     */
    public ValueBeanList selectByPidAndValAndRval(int pid, String val, int r_val, boolean full) throws SQLException
    {
        ValueBeanList vbl = null;

        StringBuffer sql =
                new StringBuffer("SELECT s.*, r.r_text titre FROM submitform s, reponse r WHERE p_id=").append(pid);
        sql.append(" and s.s_id = r.s_id ");
        sql.append(" and r_text='").append(val).append("'");

        if (r_val>0) sql.append(" and r_val=").append(r_val);

        sql.append(" order by r.r_text ");

        vbl = selectMultiple(sql.toString());

        if (vbl==null) return null;

        if (!full) { 
            return vbl ;
        } else {
            // alimentation propositions
            ReponseAction ra = new ReponseAction();

            for (int i =0; i<vbl.size(); i++) {

                SubmitForm s = (SubmitForm)vbl.get(i);
                ValueBeanList listRep = ra.selectAllBySId(s.getS_id());

                if (listRep==null) return null;

                for (int j =0; j<listRep.size();j++) {
                    Reponse r = (Reponse)listRep.get(j);
                    if (r!=null) s.addReponse(r);
                }
            }

            return vbl;
        }
    }   


    /**
     * @param d
     * @param pwd
     * @param fid
     * @return
     * @throws SQLException
     */
    public SubmitForm selectByDateAndPwdAndFid(String d,String pwd,int fid) throws SQLException
    {
        StringBuffer sql =
                new StringBuffer("SELECT * FROM submitform WHERE f_id=").append(fid);
        sql.append(" and s_date='").append(d);
        sql.append("' and s_pwd='").append(pwd).append("'");

        SubmitForm s = (SubmitForm)selectOne(sql.toString());

        return s;
    }   



    /**
     * @param fid
     * @param lockedOnly
     * @param datedeb
     * @param datefin
     * @param orderBy
     * @return
     * @throws SQLException
     */
    public ValueBeanList selectAllByFormId(int fid, int s_id_parent, int lockedOnly, String datedeb, String datefin, String orderBy) throws SQLException
    {
        StringBuffer sql = new StringBuffer("SELECT submitform.*, reponse.r_text as titre " +
                " FROM submitform, reponse, formulaire ");

        sql.append(" WHERE formulaire.f_id="+String.valueOf(fid));
        sql.append(" and reponse.p_id = formulaire.p_id_titre ");
        sql.append(" and formulaire.f_id = submitform.f_id ");
        sql.append(" and submitform.s_id = reponse.s_id ");

        if (lockedOnly>=0) sql.append(" and submitform.s_lock= " + lockedOnly +" ");
        if (!Controls.isBlank(datedeb)) sql.append(" and submitform.s_date>='" + datedeb + "' ");
        if (!Controls.isBlank(datefin)) sql.append(" and submitform.s_date<='" + datefin + "' ");
        if (s_id_parent>0) sql.append(" and submitform.s_id_parent= " + s_id_parent +" ");
        
        
        if (orderBy == null) orderBy =" ORDER BY submitform.s_date DESC, submitform.s_id DESC ";
        sql.append(orderBy);

        ValueBeanList listForms = selectMultiple(sql.toString());

        return listForms;
    }

    /**
     * @param fid
     * @param lockedOnly
     * @param datedeb
     * @param datefin
     * @return
     * @throws SQLException
     */
    public ValueBeanList selectAllByFormId(int fid, int lockedOnly, String datedeb, String datefin) throws SQLException
    {
        return selectAllByFormId( fid, -1,  lockedOnly,  datedeb,  datefin,null);
    }

    /**
     * @param fid
     * @return
     * @throws SQLException
     */
    public ValueBeanList selectAllByFormId(int fid) throws SQLException { 
        return selectAllByFormId(fid,-1,-1,null,null, null);
    }

    /**
     * @param fid
     * @param lockedOnly
     * @return
     * @throws SQLException
     */
    public ValueBeanList selectAllByFormId(int fid, int lockedOnly) throws SQLException {
        return selectAllByFormId(fid,-1, lockedOnly,null,null, null);
    }

    /**
     * @param fid
     * @param sid_parent
     * @return
     * @throws SQLException
     */
    public ValueBeanList selectAllByFidAndSidParent(int fid, int sidParent) throws SQLException    {
        return selectAllByFormId(fid,sidParent, -1,null,null, null);   
    }
    /**
     * Recherche de tous les formulaires fils
     * 
     * @param sid
     * @return
     * @throws SQLException
     */
    public ValueBeanList selectAllChildren(int sid) throws SQLException    {
        return selectAllChildrenByFid( sid,  -1, " order by f_id, rt.r_text , s_id ", false, -1);
    }

    /**
     * Recherche de tous les formulaires fils par type
     * 
     * @param sid
     * @param fid
     * @param orderBy
     * @param controlFormDates
     * @param lockedOnly : 0 pour les non verrouillés, 1 pour les verrouillés(validés), -1 pour tous
     * @return
     * @throws SQLException
     */
    public ValueBeanList selectAllChildrenByFid(
            int sid_parent, 
            int fid, 
            String orderBy,
            boolean controlFormDates, int  lockedOnly) throws SQLException
            {
        StringBuffer sql = new StringBuffer();


        // version avec la récupération du titre du submitform
        sql.append( "SELECT distinct s.*, rt.r_text titre  " +
                " FROM submitform s " +
                " LEFT OUTER JOIN formulaire f ON s.F_ID = f.F_ID " +
                " LEFT OUTER JOIN reponse rt ON rt.P_ID = f.P_ID_TITRE " +
                " AND s.S_ID = rt.S_ID " +
                " WHERE  s.s_id_parent = "+sid_parent );
        if (fid >0) sql.append(" and s.f_id ="+fid);

        if (lockedOnly>=0) sql.append(" and s.s_lock =  " + lockedOnly + " ");

        if (controlFormDates) {
            String today = BasicType.getTodaysDateIso();
            sql.append(" and f.f_datedeb <='");
            sql.append(today);
            sql.append("' and f.f_datefin >='");

            sql.append(today);
            sql.append("' ");

        }

        if (orderBy == null) orderBy =" ORDER BY s_date DESC, s_id DESC";
        sql.append(orderBy);



        return selectMultiple(sql.toString());
            }

    /**
     * @param sid
     * @param fid
     * @return
     * @throws SQLException
     */
    public ValueBeanList selectAllChildrenByFid(int sid, int fid) throws SQLException
    {
        return selectAllChildrenByFid( sid,  fid, null, false, -1);
    }

    /**
     * Retourne la liste des submitform dont l'ID correspond à la valeur de la réponse à la proposition PID
     * 
     * @param pid
     * @param sid
     * @param lockedOnly
     * @return
     * @throws SQLException
     */
    public ValueBeanList selectAllByPropId(int pid, int sid, boolean lockedOnly) throws SQLException
    {
        StringBuffer sql = new StringBuffer(
                "SELECT distinct S.* FROM submitform S, reponse R "+
                        " WHERE S.s_id=R.s_id and R.p_id="+String.valueOf(pid)+
                        " and R.r_text ='"+ String.valueOf(sid) +"'");
        if (lockedOnly) sql.append(" and S.s_lock=1 ");

        sql.append(" ORDER BY s_date DESC, s_id DESC");
        ValueBeanList listForms = selectMultiple(sql.toString());

        return listForms;
    }

    /**
     * @param pid1
     * @param val1
     * @param lockedOnly
     * @return
     * @throws SQLException
     */
    public ValueBeanList selectAllByPropIdAndVal(
            int pid1, String val1, 
            int lockedOnly) throws SQLException    { 
        return selectAllByPropIdAndVal(pid1, val1, -1, null, lockedOnly, null);
    }

    /**
     * @param pid1
     * @param val1
     * @param pid2
     * @param val2
     * @param lockedOnly
     * @return une valuebeanlist avec les submitform correspondant aux criteres
     * @throws SQLException
     */
    public ValueBeanList selectAllByPropIdAndVal(
            int pid1, String val1, 
            int pid2, String val2, 
            int lockedOnly, String orderBy) throws SQLException             {
        
        
        StringBuffer sql = new StringBuffer(
                "SELECT distinct S.*, R.r_text titre FROM submitform S ");
        sql.append( " LEFT OUTER JOIN formulaire F ON S.F_ID = F.F_ID ");
        sql.append( " LEFT OUTER JOIN reponse R ON R.P_ID = F.P_ID_TITRE " );
        sql.append( " AND S.S_ID = R.S_ID " );

        if(pid1>0) sql.append(" , reponse R1 ");
        if(pid2>0) sql.append(" , reponse R2 ");

        sql.append(" WHERE 1 ");        

        if(pid1>0) {
            sql.append(" and S.s_id=R1.s_id and R1.p_id="+pid1);
            sql.append(" and R1.r_text like '"+ val1 +"' ");            
        }

        if(pid2>0) {
            sql.append(" and S.s_id=R2.s_id and R2.p_id="+pid2);
            if (val2.indexOf(',')>0) {
                // petit ajout pour l'appel ajax (à revoir)
                sql.append(" and R2.r_text in ("+ val2 +") ");
            } else {
                sql.append(" and R2.r_text like '"+ val2 +"' ");
            }
        }


        if (lockedOnly>=0) sql.append(" and S.s_lock= " + lockedOnly + " ");

        if (orderBy != null) {
            sql.append(" "+ orderBy);
        } else {
            sql.append(" ORDER BY s_date DESC, s_id DESC");
        }

        ValueBeanList listForms = selectMultiple(sql.toString());

        return listForms;
            }

    public ValueBeanList selectAllByPropId(int pid, int sid) throws SQLException
    {
        return selectAllByPropId( pid,  sid, false);
    }



    public ValueBeanList selectByFormIdSidDate(int sid, String datedeb, String datefin, int afficheLocked) throws SQLException
    {
        StringBuffer sql = new StringBuffer();


        // ajout pour afficher le titre 
        sql.append("SELECT s.*, rt.r_text titre " +
                " FROM submitform s " +
                " LEFT OUTER JOIN formulaire f ON s.F_ID = f.F_ID " +
                " LEFT OUTER JOIN reponse rt ON rt.P_ID = f.P_ID_TITRE " +
                " AND s.S_ID = rt.S_ID " +
                " WHERE 1 ") ;

        if (sid>=0) {
            sql.append(" and s.s_id=").append(sid); 
        } else {
            if (!Controls.isBlank(datedeb)) sql.append(" and s.s_date>='" + datedeb + "'");
            if (!Controls.isBlank(datefin)) sql.append(" and s.s_date<='" + datefin + "'");
            if (afficheLocked>=0) sql.append(" and s.s_lock="+afficheLocked);

        }


        sql.append(" ORDER BY s.s_date DESC, s.s_id DESC");
        ValueBeanList listForms = selectMultiple(sql.toString());

        return listForms;
    }

    /**
     * @param searchP1
     * @param searchP2
     * @param findP1
     * @param findP2
     * @param typQ1
     * @param typQ2
     * @param datedeb
     * @param datefin
     * @param s_id_parent
     * @param afficheLockedOnly
     * @return
     * @throws SQLException
     */
    public ValueBeanList selectBySearchInfos(
            int searchP1, int searchP2, 
            int searchRVal1, int searchRVal2, 
            String findP1, String findP2,
            String typQ1, String typQ2, 
            String datedeb, String datefin, 
            int sid_parent,
            int afficheLocked, String orderBy) 
                    throws SQLException
                    {
        //      Nouvelle version  OUTER JOIN (SLE 9 sept 08)
        StringBuffer sql = 
                new StringBuffer(" SELECT distinct s.*, rt.r_text titre  " +
                        " FROM submitform s " +
                        " LEFT OUTER JOIN formulaire f ON s.F_ID = f.F_ID " +
                        " LEFT OUTER JOIN reponse rt ON rt.P_ID = f.P_ID_TITRE " +
                        " AND s.S_ID = rt.S_ID " );

        if (searchP1>=0 && !Controls.isBlank(findP1)) sql.append( " ,reponse r ");
        if (searchP2>=0 && !Controls.isBlank(findP2)) sql.append( " ,reponse r2 ");     

        sql.append(" WHERE 1 ");

        if (searchP1>=0 && !Controls.isBlank(findP1)) {
            sql.append(" AND r.s_id= s.s_id  ");
            sql.append(" AND r.p_id=").append(searchP1);
            sql.append(" AND r.r_val=").append(searchRVal1);
            if (typQ1.equals(Question.READ_ONLY) 
                    || typQ1.equals(Question.NOMBRE)
                    || typQ1.equals(Question.FORM_ID)) {
                sql.append(" AND upper(r.r_text) like upper('").append(BasicType.quoteQuote(findP1)).append("') ");
            } else {
                sql.append(" AND upper(r.r_text) LIKE upper('").append(BasicType.quoteQuote(findP1)).append("%') ");
            }
        }

        if (searchP2>=0 && !Controls.isBlank(findP2)) {
            sql.append(" AND r2.s_id= s.s_id  ");
            sql.append(" AND r2.p_id=").append(searchP2);
            sql.append(" AND r2.r_val=").append(searchRVal2);
            if (typQ2.equals(Question.READ_ONLY)
            		|| typQ2.equals(Question.NOMBRE)
                    || typQ2.equals(Question.FORM_ID)) {
                sql.append(" AND upper(r2.r_text) like upper('").append(BasicType.quoteQuote(findP2)).append("') ");
            } else {
                sql.append(" AND upper(r2.r_text) LIKE upper('").append(BasicType.quoteQuote(findP2)).append("%') ");
            }

        }

        if (!Controls.isBlank(datedeb)) sql.append("  AND s.s_date>='" + datedeb + "' ");
        if (!Controls.isBlank(datefin)) sql.append("  AND s.s_date<='" + datefin + "' ");
        if (afficheLocked>=0) sql.append("  AND s.s_lock="+afficheLocked);
        if (sid_parent>0) sql.append("  AND s.s_id_parent="+sid_parent);

        if (orderBy == null) 
            orderBy = " order by rt.r_text "; //" ORDER BY s_date DESC, s_id DESC";
        sql.append(orderBy);
        ValueBeanList listForms = selectMultiple(sql.toString());

        return listForms;
                    }

    /**
     * @param textAlert
     * @param searchP1
     * @param alert
     * @param typQ
     * @param datedeb
     * @param datefin
     * @param afficheLockedOnly
     * @return liste
     * @throws SQLException
     */
    public ValueBeanList selectByAlert(String textAlert,
            int searchP1, String alert,  String typQ,
            String datedeb, String datefin, boolean afficheLockedOnly) 
                    throws SQLException                     {
        
        
        StringBuffer sql = 
                new StringBuffer("SELECT distinct s.*, concat('");
        
        if (textAlert != null) sql.append(textAlert);
        
        sql.append("',rt.r_text) titre  " +
                " FROM submitform s " +
                " LEFT OUTER JOIN formulaire f ON s.F_ID = f.F_ID " +
                " LEFT OUTER JOIN reponse rt ON rt.P_ID = f.P_ID_TITRE " +
                " AND s.S_ID = rt.S_ID " );

        if (searchP1>=0 && !Controls.isBlank(alert)) sql.append( " ,reponse r ");

        sql.append(" WHERE 1 ");

        if (searchP1>=0 && !Controls.isBlank(alert)) {
            sql.append(" AND r.s_id= s.s_id  ");
            sql.append(" AND r.p_id= ").append(searchP1);

            if (typQ.equals(Question.DATE)) {
                sql.append(" AND STR_TO_DATE(r.r_text,'%d/%m/%Y  %H:%i:%s') ").append(alert);
            } else {
                sql.append(" AND r.r_text ").append(alert);
            }
        }

        if (!Controls.isBlank(datedeb)) sql.append("  AND s.s_date>='" + datedeb + "' ");
        if (!Controls.isBlank(datefin)) sql.append("  AND s.s_date<='" + datefin + "' ");
        if (afficheLockedOnly) sql.append("  AND s.s_lock=1");


        sql.append(" ORDER BY s_date DESC, s_id DESC");
        ValueBeanList listForms = selectMultiple(sql.toString());

        return listForms;
                    }


    public String sendMailOnCreate(SubmitForm sf, Questionnaire form, String diffAdr, UserData ud) {

        String adr = "";
        String from = InitServlet.getMailFromAddress();

        if (form !=null && !Controls.isBlank(form.getF_mail_adm()) )
            from = form.getF_mail_adm();

        if (diffAdr != null ) {
            adr = diffAdr; 
        } else {
            adr = sf.getPropVal(form.getP_id_mail());
        }

        TemplateAction ta = new TemplateAction();
        Template t = null;
        try {
            t=ta.selectOne(form.getT_id_mail_on_create());
        } catch (SQLException e) {}


        String res = null;
        if (!Controls.isBlank(adr)) {
            StringBuffer txt = FormatExport.exportMailFromTemplate(form.getT_id_mail_on_create(),sf.getS_id(), ud);

            if (txt!=null) {
                String objet = t.getName(); // TODO : pouvoir fusionner le titre avec des balises...
                res = Mailer.sendHtmlMail(from,adr,null, objet, txt.toString(),txt.toString());
            }
        }
        if (res!=null) traceLog.info(res);
        return res;
    }

    /**
     * @param sf
     * @param form
     * @param diffAdr
     */
    public void sendMailOnLock(SubmitForm sf, Questionnaire form, String diffAdr, UserData ud) {

        String adr = "";
        String from = InitServlet.getMailFromAddress();
        String copy = form.getF_mail_adm();

        if (form !=null && !Controls.isBlank(form.getF_mail_adm()) )
            from = form.getF_mail_adm();

        if (!Controls.isBlank(diffAdr)) {
            adr = diffAdr; 
        } else {
            adr = sf.getPropVal(form.getP_id_mail());
        }

        TemplateAction ta = new TemplateAction();
        Template t = null;
        try {
            t=ta.selectOne(form.getT_id_mail_on_lock());
        } catch (SQLException e) {}

        String res = null;
        if (!Controls.isBlank(adr)) {
            StringBuffer txt = FormatExport.exportMailFromTemplate(form.getT_id_mail_on_lock(),sf.getS_id(), ud);
            if (txt!=null)
                res = Mailer.sendHtmlMail(from, adr, copy, 
                        t.getName(), txt.toString(), txt.toString());
        }
        if (res!=null) traceLog.info(res);
    }


    /**
     * @param sf
     * @param form
     * @param diffAdr
     */
    public void sendMailOnDelete(SubmitForm sf, Questionnaire form, String diffAdr,UserData ud) {

        String adr = "";
        String from = InitServlet.getMailFromAddress();
        String copy = form.getF_mail_adm();

        if (form !=null && !Controls.isBlank(form.getF_mail_adm()) )
            from = form.getF_mail_adm();
        if (!Controls.isBlank(diffAdr)) {
            adr = diffAdr; 
        } else {
            adr = sf.getPropVal(form.getP_id_mail());
        }

        TemplateAction ta = new TemplateAction();
        Template t = null;
        try {
            t=ta.selectOne(form.getT_id_mail_on_delete());
        } catch (SQLException e) {}

        String res = null;
        if (!Controls.isBlank(adr)) {
            StringBuffer txt = FormatExport.exportMailFromTemplate(form.getT_id_mail_on_delete(),sf.getS_id(), ud);
            if (txt!=null)
                res = Mailer.sendHtmlMail(from, adr, copy, 
                        t.getName(), txt.toString(), txt.toString());
        }
        if (res!=null) traceLog.info(res);
    }


    /**
     * @param sf
     * @param form
     */
    public void sendMailOnLock(SubmitForm sf, Questionnaire form, UserData ud) {
        sendMailOnLock( sf, form, null, ud);
    }

    /**
     * @param sf
     * @param form
     */
    public void sendMailOnDelete(SubmitForm sf, Questionnaire form, UserData ud) {
        sendMailOnDelete( sf, form, null, ud);
    }

    /**
     * alimentation d'un formulaire avec les réponses 
     * dont la proposition a un indicateur d'initialisation
     * 
     * @param sf
     * @return le nb de réponses initialisées
     * @throws SQLException
     */
    public int getInitVals(SubmitForm sf) throws SQLException {
        //      alimentation propositions
        ReponseAction ra = new ReponseAction();
        ValueBeanList listRep = ra.selectAllBySId(String.valueOf(sf.getS_id()), true);

        if (listRep==null) return 0;

        for (int i =0; i<listRep.size();i++) {
            Reponse r = (Reponse)listRep.get(i);
            if (r!=null) sf.addReponse((Reponse)listRep.get(i));
        }

        return listRep.size();

    }
    
    
    /**
     * alimentation d'une liste formulaire avec les réponses 
     * dont la proposition a un indicateur d'initialisation
     * 
     * @param sf
     * @return le nb de réponses initialisées
     * @throws SQLException
     */
    public int setInitVals(ValueBeanList listSf) throws SQLException {
        //      alimentation propositions
        ReponseAction ra = new ReponseAction();
        StringBuffer listIds = new StringBuffer("");

        for (int i=0; listSf != null && i<listSf.size(); i++) {
            SubmitForm sf = (SubmitForm)listSf.get(i);
            if (i>0) listIds.append(", ");
            listIds.append(sf.getS_id());
        }

        ValueBeanList listRep = ra.selectAllBySId(listIds.toString(), true);

        for (int i=0; listSf != null && i<listSf.size(); i++) {
            SubmitForm sf = (SubmitForm)listSf.get(i);

            for (int j=0; listRep != null && j<listRep.size(); j++) {
                Reponse rep = (Reponse)listRep.get(j);
                if (rep.getS_id() == sf.getS_id()) sf.addReponse(rep);                
            }
        }
        return (listRep!=null?listRep.size():0);

    }



}
