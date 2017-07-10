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
public class QuestionAction extends ActionBean {

    /**
     * Charge un bean question à partir du résultat d'une requête
     */
    public ValueBean populateBean(ResultSet rs) throws SQLException
    {
        Question q = new Question();

        q.setId(rs.getInt("q_id"));
        q.setTexte(BasicType.trim(rs.getString("q_texte")));
        q.setNum(rs.getInt("q_num"));
        q.setSize(rs.getInt("q_size"));
        q.setType(BasicType.trim(rs.getString("q_type")));
        q.setMandatory(rs.getInt("q_mandatory"));
        q.setSearch( (rs.getInt("q_search")==1));
        q.setG_id(rs.getInt("g_id"));




        return q;
    }

    /**
     * @param id
     * @return
     * @throws SQLException
     */
    public Question selectById(int id) throws SQLException
    {
        StringBuffer sql =
                new StringBuffer("SELECT * FROM question WHERE q_id=").append(id);
        Question q = (Question)selectOne(sql.toString());

        return q;
    }

    /** sélection de questions par l'ID du groupe
     * 
     * @param g_id
     * @return
     * @throws SQLException
     */
    public ValueBeanList selectAllByGroupId(int g_id) throws SQLException
    {
        StringBuffer sql = new StringBuffer("SELECT * FROM question WHERE g_id="+String.valueOf(g_id)+ " ORDER BY q_num,q_id");
        ValueBeanList listGroups = selectMultiple(sql.toString());

        return listGroups;
    }
    
    /** sélection de questions par l'ID du formulaire
     * 
     * @param g_id
     * @return
     * @throws SQLException
     */
    public ValueBeanList selectAllByFid(int f_id) throws SQLException
    {
        //StringBuffer sql = new StringBuffer("SELECT * FROM question WHERE g_id="+String.valueOf(g_id)+ " ORDER BY q_num,q_id");
        StringBuffer sql = new StringBuffer("SELECT * FROM  question WHERE  g_id in " );
        sql.append("    (select g_id from groupe where f_id="+ f_id + ") ORDER BY q_num,q_id " );
        
        ValueBeanList listGroups = selectMultiple(sql.toString());

        return listGroups;
    }
    

    /**
     * @return
     * @throws SQLException
     */
    public ValueBeanList selectSearchQuest() throws SQLException
    {
        StringBuffer sql = new StringBuffer(
                "SELECT * FROM question WHERE q_search='1' order by q_texte");
        ValueBeanList listGroups = selectMultiple(sql.toString());

        return listGroups;
    }

    /**
     * @param qid
     * @return
     * @throws SQLException
     */
    public ValueBeanList selectSearchQuestByQid(int qid) throws SQLException
    {
        StringBuffer sql = new StringBuffer(
                "SELECT q.* FROM question q, question q2, groupe g, groupe g2 ");
        sql.append(" WHERE q.q_search='1' and g.g_id = q.g_id and g.f_id = g2.f_id and g2.g_id = q2.g_id and q2.q_id <> q.q_id and q2.q_id = ");
        sql.append(qid);
        sql.append(" order by q_texte");

        ValueBeanList listGroups = selectMultiple(sql.toString());

        return listGroups;
    }





    /**
     * @param q
     * @return
     * @throws SQLException
     */
    public int update (Question q) throws SQLException {

        StringBuffer sql = new StringBuffer();

        sql.append(" UPDATE question SET " );

        sql.append("  q_texte ='"+BasicType.quoteQuote(q.getTexte()));
        sql.append("' ,q_type='"+q.getType());
        sql.append("' ,q_num ="+q.getNum());
        sql.append(" ,g_id ="+ q.getG_id());
        sql.append(" ,q_mandatory ="+ q.getMandatory());
        sql.append(" ,q_size ="+ q.getSize());
        sql.append(" ,q_search ="+ (q.isSearch()?"1":"0"));



        sql.append(" WHERE q_id = "+q.getId());

        return updateData(sql.toString());


    }

    /**
     * @param q
     * @return
     * @throws SQLException
     */
    public int insert (Question q) throws SQLException {

        StringBuffer sql = new StringBuffer();

        sql.append(" INSERT INTO question (" );
        sql.append("q_texte ,q_type , q_num, g_id, q_mandatory, q_size, q_search");

        sql.append(") VALUES (");

        sql.append("'"+BasicType.quoteQuote(q.getTexte())+"', ");
        sql.append("'"+BasicType.quoteQuote(q.getType())+"', ");
        sql.append(q.getNum()+", ");
        sql.append(q.getG_id()+", ");
        sql.append(q.getMandatory()+", ");
        sql.append(q.getSize()+", ");
        sql.append((q.isSearch()?"1":"0"));


        sql.append(") ");

        return updateData(sql.toString());


    }

}
