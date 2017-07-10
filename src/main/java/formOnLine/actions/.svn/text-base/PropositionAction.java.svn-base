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

import formOnLine.msBeans.*;



/**
 * @author S. Leridon
 *
 */
public class PropositionAction extends ActionBean {

    /**
     * Charge un bean propositon  à partir du résultat d'une requête
     */
    public ValueBean populateBean(ResultSet rs) throws SQLException
    {
        Proposition p = new Proposition();

        p.setId(rs.getInt("p_id"));
        p.setTexte(BasicType.trim(rs.getString("p_texte")));
        p.setNum(rs.getInt("p_num"));
        p.setQid(rs.getInt("q_id"));
        p.setAlert(rs.getString("p_alert"));
        p.setStat(rs.getInt("p_stat"));
        p.setInitload(rs.getInt("p_initload"));

        p.setVal(0);

        return p;
    }

    /** selection proposition par id
     * 
     * @param id
     * @return
     * @throws SQLException
     */
    public Proposition selectById(int id) throws SQLException
    {
        StringBuffer sql =
                new StringBuffer("SELECT * FROM proposition WHERE p_id=").append(id);
        Proposition p = (Proposition)selectOne(sql.toString());

        return p;
    }

    /** selection des propositions avec une condition d'alerte
     * 
     * @return
     * @throws SQLException
     */
    public ValueBeanList selectAllByAlert() throws SQLException
    {
        StringBuffer sql = new StringBuffer("SELECT * FROM proposition WHERE p_alert <> '' and p_alert is not null ");
        ValueBeanList listProps = selectMultiple(sql.toString());

        return listProps;
    }

    /** selection des propositions d'une question
     * 
     * @param q_id
     * @return
     * @throws SQLException
     */
    public ValueBeanList selectAllByQuestId(int q_id) throws SQLException
    {
        StringBuffer sql = new StringBuffer("SELECT * FROM proposition WHERE q_id="+String.valueOf(q_id)+ " ORDER BY p_num,p_texte");
        ValueBeanList listProps = selectMultiple(sql.toString());

        return listProps;
    }

    /** selection des propositions par Id de formulaire
     *
     * @param f_id
     * @return
     * @throws SQLException
     */
    public ValueBeanList selectAllByFid(int f_id, String orderBy) throws SQLException
    {
        StringBuffer sql = new StringBuffer("SELECT * FROM proposition WHERE q_id in " );
        if (orderBy == null) orderBy = " ORDER BY p_num,p_texte "; 

        sql.append(" (select q_id from question where g_id in " );
        sql.append("    (select g_id from groupe where f_id="+ f_id + ")) " + orderBy );

        ValueBeanList listProps = selectMultiple(sql.toString());

        return listProps;
    }

    /** selection des propositions sélectionnées pour les stats
     * 
     * @return
     * @throws SQLException
     */
    public ValueBeanList selectAllByStat() throws SQLException
    {
        StringBuffer sql = new StringBuffer("SELECT p_id, concat(q_texte, ' = ', p_texte) as p_texte, p_num, proposition.q_id, p_alert, p_stat, p_initload ");
        sql.append(" FROM proposition, question ");
        sql.append(" WHERE p_stat>0  and question.q_id = proposition.q_id ORDER BY p_texte");
        ValueBeanList listProps = selectMultiple(sql.toString());


        return listProps;
    }

    /**
     * @param q_id
     * @param p_id
     * @return
     * @throws SQLException
     */
    public ValueBeanList selectExternalValuesByFormId(int q_id, int p_id ) throws SQLException
    {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT s_id AS p_id, r_text AS p_texte, 0 as p_num, " );
        sql.append(q_id);
        sql.append(" AS q_id ");
        sql.append(" FROM reponse WHERE p_id=");
        sql.append(p_id);
        sql.append(" ORDER BY p_texte"); 
        ValueBeanList listProps = selectMultiple(sql.toString());

        return listProps;
    }

    /**
     * @param f_id
     * @return
     * @throws SQLException
     */
    public ValueBeanList selectAllByFormId(int f_id) throws SQLException
    {

        return selectAllByFid(f_id, " ORDER BY p_id");
    }


    /** mise à jour d'une proposition
     * 
     * @param p
     * @return
     * @throws SQLException
     */
    public int update (Proposition p) throws SQLException {

        StringBuffer sql = new StringBuffer();

        sql.append(" UPDATE proposition SET " );

        sql.append("  p_texte ='"+BasicType.quoteQuote(p.getTexte()));
        sql.append("' ,p_num ="+p.getNum());
        sql.append(" ,q_id ="+ p.getQid());
        sql.append(" ,p_alert ='"+BasicType.quoteQuote(p.getAlert()));
        sql.append("' ,p_stat ="+ p.getStat());
        sql.append(" ,p_initload = "+p.getInitload());
        sql.append(" ,p_date_maj = '"+BasicType.getTodaysDateIso(true)+"'");

        sql.append(" WHERE p_id = "+p.getId());

        return updateData(sql.toString());


    }

    /** insertion d'une proposition 
     * 
     * @param p
     * @return
     * @throws SQLException
     */
    public int insert (Proposition p) throws SQLException {

        StringBuffer sql = new StringBuffer();

        sql.append(" INSERT INTO proposition (" );
        sql.append("p_texte ,p_num, q_id, p_alert, p_stat, p_initload, p_date_maj");

        sql.append(") VALUES (");

        sql.append("'"+BasicType.quoteQuote(p.getTexte())+"', ");
        sql.append(p.getNum()+", ");
        sql.append(p.getQid());
        sql.append(",'"+BasicType.quoteQuote(p.getAlert())+"', ");
        sql.append(p.getStat()+", ");
        sql.append(p.getInitload()+", '");
        sql.append(BasicType.getTodaysDateIso(true)+"'");

        sql.append(") ");

        return updateData(sql.toString());


    }

}
