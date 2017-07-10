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
import com.triangle.lightfw.SessionInfos;
import com.triangle.lightfw.ValueBean;
import com.triangle.lightfw.ValueBeanList;

import formOnLine.msBeans.*;



/**
 * @author S. Leridon
 *
 */
public class TemplateAction extends ActionBean {
    
    /**
     * Charge un questionnaire à partir du résultat d'une requête
     */
    public ValueBean populateBean(ResultSet rs) throws SQLException
    {
        Template r = new Template(
                rs.getInt("t_id"),
                "",
                rs.getInt("f_id"),
                BasicType.trim(rs.getString("t_text")),
                rs.getString("t_type") 
        );
        try {
            r.setContent(BasicType.trim(rs.getString("t_content")));
        } catch (SQLException e) {}
        
        return r;
    }
    
    /**
     * @param id
     * @return
     * @throws SQLException
     */
    public Template selectOne(int id) throws SQLException
    {
        StringBuffer sql =
            new StringBuffer("SELECT * FROM template WHERE t_id=").append(id);
        
        Template r = (Template)selectOne(sql.toString());
        
        return r;
    }
    
    /**
     * @return
     * @throws SQLException
     */
    public ValueBeanList selectAll() throws SQLException
    {
        
        return selectAllByRole( SessionInfos.ROLE_ADMIN );
        
    }
    
    /**
     * @return
     * @throws SQLException
     */
    public ValueBeanList selectAllByRole(int role) throws SQLException
    {
        int f_connected = 0;
        
        if (role == SessionInfos.ROLE_ADMIN) f_connected = Questionnaire.ACCESS_ADMIN_ONLY;
        if (role == SessionInfos.ROLE_GESTION) f_connected = Questionnaire.ACCESS_CONNECTED;
        if (role == SessionInfos.ROLE_CONSULTATION) f_connected = Questionnaire.ACCESS_CONNECTED;
        
        StringBuffer sql = new StringBuffer(
                "SELECT t_id,template.F_ID,t_text,t_type FROM template left outer join formulaire using (f_id) ");
        sql.append( " WHERE t_archive=0 and (template.f_id = 0 or formulaire.f_connected <= ");
        sql.append(f_connected);
        sql.append( ") ORDER by formulaire.F_ID, t_type, t_text ");
                
        
        
        return selectMultiple(sql.toString());
        
    }
    
    /**
     * @param t
     * @return
     * @throws SQLException
     */
    public int update (Template t) throws SQLException {
        StringBuffer sql = new StringBuffer();
        sql.append(" UPDATE template set ");
        sql.append(" f_id=" );
        sql.append(t.getFid());
        sql.append(", t_text='");
        sql.append(BasicType.quoteQuote(t.getName()));
        sql.append("', t_type='");
        sql.append(t.getType());
        sql.append("', t_content='");
        sql.append(BasicType.quoteQuote(t.getContent()));
        sql.append("', t_date_maj='");
        sql.append(BasicType.getTodaysDateIso(true));
        sql.append("' ");
        
        sql.append(" WHERE t_id = ");
        sql.append(t.getId());
        
        return updateData(sql.toString());
    }
    
    /**
     * @param t
     * @return
     * @throws SQLException
     */
    public int insert (Template t) throws SQLException {
        StringBuffer sql = new StringBuffer();
        sql.append(" INSERT INTO template ( f_id,t_text,t_type,t_content, t_date_maj) values (");
        sql.append(t.getFid());
        sql.append(", '");
        sql.append(BasicType.quoteQuote(t.getName()));
        sql.append("','");
        sql.append(t.getType());
        sql.append("','");
        sql.append(BasicType.quoteQuote(t.getContent()));
        sql.append("', '");
        sql.append(BasicType.getTodaysDateIso(true));
        sql.append("')");
        
        return updateData(sql.toString());
    }
    
}