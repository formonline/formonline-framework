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
public class GroupeAction extends ActionBean {

	 	/**
	   * Charge un questionnaire à partir du résultat d'une requête
	   */
	  public ValueBean populateBean(ResultSet rs) throws SQLException
	  {
	    Groupe gr = new Groupe();
	    
	    gr.setId(rs.getInt("g_id"));
	    gr.setTexte(BasicType.trim(rs.getString("g_texte")));
	    gr.setTitre(BasicType.trim(rs.getString("g_titre")));
	    gr.setNum(rs.getInt("g_num"));
	    gr.setType(BasicType.trim(rs.getString("g_type")));
        gr.setF_id(rs.getInt("f_id"));
	    
	    return gr;
	  }
	
	  public Groupe selectById(int id) throws SQLException
	  {
	    StringBuffer sql =
	      new StringBuffer("SELECT * FROM groupe WHERE g_id=").append(id);
	    Groupe result = (Groupe)selectOne(sql.toString());

	    return result;
	  }
	  
	  public ValueBeanList selectAllByFormId(int f_id) throws SQLException
	  {
	    StringBuffer sql = new StringBuffer("SELECT * FROM groupe WHERE f_id="+String.valueOf(f_id)+ " ORDER BY g_num,g_id");
	    ValueBeanList listGroups = selectMultiple(sql.toString());

	    return listGroups;
	  }

      public int update (Groupe g) throws SQLException {
          
          StringBuffer sql = new StringBuffer();
          
          sql.append(" UPDATE groupe SET " );
          
          sql.append(" g_TITRE ='"+BasicType.quoteQuote(g.getTitre()));
          sql.append("' ,g_texte ='"+BasicType.quoteQuote(g.getTexte()));
          sql.append("' ,g_type='"+g.getType());
          sql.append("' ,g_num ="+g.getNum());
          sql.append(" ,f_id ="+ g.getF_id());
          
          sql.append(" WHERE g_id = "+g.getId());
          
          return updateData(sql.toString());
          
          
      }

    public int insert (Groupe g) throws SQLException {
          
          StringBuffer sql = new StringBuffer();
          
          sql.append(" INSERT INTO groupe (" );
          
          sql.append(" g_TITRE, g_texte, g_type, g_num, f_id  ");
          
          sql.append(") VALUES (");
          
          sql.append("'"+BasicType.quoteQuote(g.getTitre())+"', ");
          sql.append("'"+BasicType.quoteQuote(g.getTexte())+"', ");
          sql.append("'"+BasicType.quoteQuote(g.getType())+"', ");
          sql.append(g.getNum()+", ");
          sql.append(g.getF_id());
          
          sql.append(") ");
          
          return updateData(sql.toString());
          
          
      }
	  
}
