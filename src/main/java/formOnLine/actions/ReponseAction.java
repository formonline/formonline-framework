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

import formOnLine.Controls;
import formOnLine.msBeans.*;

/**
 * @author SLE
 * 
 */
public class ReponseAction extends ActionBean
{

  /**
   * Charge un questionnaire à partir du résultat d'une requête
   */
  public ValueBean populateBean(ResultSet rs) throws SQLException
  {
    Reponse r = new Reponse();
    r.setP_id(rs.getInt("p_id"));
    r.setS_id(rs.getInt("s_id"));
    r.setSVal(rs.getString("r_text") );
    r.setVal(rs.getInt("r_val"));
    

    return r;
  }

  public int insert(Reponse r) throws SQLException
  {
    String sVal = r.getSVal();
    sVal = BasicType.quoteQuote(sVal.replaceAll("’","'"));
    
      
    StringBuffer sql = new StringBuffer(
        "REPLACE INTO reponse (s_id, p_id, r_text, r_val) ");
    sql.append("VALUES (").append(r.getS_id());
    sql.append(",").append(r.getP_id());
    sql.append(",'").append(sVal).append("',");
    sql.append(r.getVal()).append(" ) ");

    return updateData(sql.toString());
  }

  public int delete(Reponse r) throws SQLException
  {
    StringBuffer sql = new StringBuffer("DELETE FROM reponse WHERE s_id=");
    sql.append(r.getS_id()).append(" and p_id=").append(r.getP_id());

    return updateData(sql.toString());
  }

  public int deleteAll(SubmitForm sf) throws SQLException
  {
    StringBuffer sql = new StringBuffer("DELETE FROM reponse WHERE s_id=");
    sql.append(sf.getS_id());

    return updateData(sql.toString());
  }

  public Reponse selectOne(int pid, int sid) throws SQLException
  {
    StringBuffer sql = new StringBuffer("SELECT * FROM reponse WHERE p_id=")
        .append(pid);
    sql.append(" and s_id=").append(sid);

    Reponse r = (Reponse)selectOne(sql.toString());

    return r;
  }

  
  /**
   * @param sid
   * @return
   * @throws SQLException
   */
  public ValueBeanList selectAllBySId(int sid) throws SQLException     {
      
      return selectAllBySId( String.valueOf(sid), false);
    }

  /**
   * @param sid
   * @return
   * @throws SQLException
   */
  public ValueBeanList selectAllBySId(String listSid, boolean initValOnly) throws SQLException
    {
      
      if (Controls.isBlank(listSid)) return null;
      
      StringBuffer sql = new StringBuffer("SELECT reponse.* FROM reponse ");
      if (initValOnly) sql.append(" , proposition ");
      
      sql.append(" WHERE ");
      if (initValOnly) sql.append(" proposition.p_id = reponse.p_id" +
              " and proposition.p_initload = 1 and ");
      
      sql.append(" s_id in (").append(listSid).append(")");
      
      
      ValueBeanList listReps = selectMultiple(sql.toString());

      return listReps;
    }



  
  /**
   * Recherche de toutes les réponses correspondant à un PID
   *  
   * @param pid : le pid souhaité
   * @param lockedOnly : si true, filtre sur les submtiform validés uniquement
   * @return
   * @throws SQLException
   */
  public ValueBeanList selectAllByPId(int pid, boolean lockedOnly) throws SQLException
    {
      StringBuffer sql = new StringBuffer("SELECT r.* FROM reponse r, submitform sf");
      sql.append(" WHERE r.s_id = sf.s_id and p_id=" + pid);
      
      if (lockedOnly) sql.append(" and sf.s_lock = 1 ");
      sql.append(" order by r_text");
      
      return selectMultiple(sql.toString());
    }
  
  /**
   * Teste l'existance d'une réponse identique dans la base (champ ne devant pas
   * être en doublon dans la base). TRIANGLE 02/05/2005: Ajour du sid
   * @param sid Id du SubmitForm (Réponse du formulaire)
   * @param pid Id de la proposition
   * @param rtext Valeur de la réponse
   * @return true si une réponse équivanlent dans la base
   * @throws SQLException
   */
  public boolean existeReponse(int sid, int pid, String rtext)
      throws SQLException
  {
    boolean resultat = false;

    StringBuffer sql = new StringBuffer("SELECT reponse.* FROM reponse,submitform WHERE p_id=")
        .append(pid);
    sql.append(" and reponse.s_id <> ").append(sid);
    sql.append(" AND submitform.s_id = reponse.s_id ");
    sql.append(" and UPPER(r_text)='").append(BasicType.quoteQuote(rtext))
        .append("'");
    Reponse r = (Reponse)selectOne(sql.toString());
    resultat = (r != null);

    return resultat;
  }

}