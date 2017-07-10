/*
 * Créé le 23/12/10
 *
 */
package formOnLine.actions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import com.triangle.lightfw.ActionBean;
import com.triangle.lightfw.BasicType;
import com.triangle.lightfw.ValueBean;
import com.triangle.lightfw.ValueBeanList;

import formOnLine.msBeans.*;



/**
 * @author SLE
 *
 */
public class ConfigAction extends ActionBean {

    /**
     * Charge un questionnaire à partir du résultat d'une requête
     */
    public ValueBean populateBean(ResultSet rs) throws SQLException   {
        Config conf = new Config();

        conf.setId(rs.getInt("c_id"));
        conf.setType(BasicType.trim(rs.getString("c_type")));
        conf.setParam(BasicType.trim(rs.getString("c_param")));
        conf.setVal(BasicType.trim(rs.getString("c_val")));
        conf.setTexte(BasicType.trim(rs.getString("c_texte")));

        return conf;
    }

    /**
     * @param id
     * @return
     * @throws SQLException
     */
    public Config selectById(int id) throws SQLException     {
        StringBuffer sql =
                new StringBuffer("SELECT * FROM config WHERE c_id=").append(id);
        Config result = (Config)selectOne(sql.toString());

        return result;
    }

    /**
     * @return liste de vqlueBeqn Config
     * @throws SQLException
     */
    public ValueBeanList selectAll() throws SQLException    {
        
        StringBuffer sql = new StringBuffer("SELECT * FROM config order by c_type, c_param");
        ValueBeanList listGroups = selectMultiple(sql.toString());

        return listGroups;
    }

    /**
     * @return HashMap des para;etres
     * @throws SQLException
     */
    public HashMap<String,String> getParams() throws SQLException     {
        return getParams(false,-1);
        
    }
    /**
     * @return HashMap des parametres en prenant en compte la conf de test si le SID courant correspond
     * @throws SQLException
     */
    public HashMap<String,String> getParams(boolean withTestConfig, int testId) throws SQLException     {
        ValueBeanList list = selectAll();
        HashMap<String,String> tab = new HashMap<String,String>();

        for (int i=0; list !=null && i< list.size(); i++) {
            Config param = (Config)list.get(i);
            tab.put(param.getParam(), param.getVal());
        }

        
        // si le paramètre "isTest" est positionné à "true", et que le SID connecté est dans la liste des Id de "listeTest" ,
        // on corrige la config et on prend en compte la liste de config donné dans "confTest" 
        if (withTestConfig && tab.containsKey("isTest") && tab.containsKey("listeTest") && tab.containsKey("confTest")) {
            
            String listeTest = tab.get("listeTest");
            if ( (new java.lang.Boolean((String)tab.get("isTest")).booleanValue())
                    && listeTest.contains(","+testId+",")) {
                
                String[] confTest = tab.get("confTest").split(",");
                for (int i=0; i<confTest.length; i++) {
                    String[] testConf = confTest[i].split("=");
                    if (tab.containsKey(testConf[0].trim())) tab.put(testConf[0], testConf[1].trim());
                }
            }
            
        }

        return tab;
    }

    /**
     * @param g
     * @return
     * @throws SQLException
     */
    public int updateValByParam (Config g) throws SQLException {

        StringBuffer sql = new StringBuffer();

        sql.append(" UPDATE config SET " );

        sql.append(" c_val='"+g.getVal());
        sql.append("' ");

        sql.append(" WHERE c_param = '"+g.getParam()+"'");

        return updateData(sql.toString());


    }

    /**
     * @param g
     * @return
     * @throws SQLException
     */
    public int update (Config g) throws SQLException {

        StringBuffer sql = new StringBuffer();

        sql.append(" UPDATE config SET " );

        sql.append(" c_type ='"+BasicType.quoteQuote(g.getType()));
        sql.append("' ,c_param ='"+BasicType.quoteQuote(g.getParam()));
        sql.append("' ,c_val='"+g.getVal());
        sql.append("' ");

        sql.append(" WHERE c_id = "+g.getId());

        return updateData(sql.toString());


    }

    /**
     * @param g
     * @return
     * @throws SQLException
     */
    public int insert (Config g) throws SQLException {

        StringBuffer sql = new StringBuffer();

        sql.append(" INSERT INTO config (" );

        sql.append(" c_type, c_param, c_val  ");

        sql.append(") VALUES (");

        sql.append("'"+BasicType.quoteQuote(g.getType())+"', ");
        sql.append("'"+BasicType.quoteQuote(g.getParam())+"', ");
        sql.append("'"+BasicType.quoteQuote(g.getVal())+"' ");

        sql.append(") ");

        return updateData(sql.toString());


    }

}
