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
import com.triangle.lightfw.ValueBean;
import com.triangle.lightfw.ValueBeanList;

import formOnLine.Controls;
import formOnLine.msBeans.*;



/**
 * @author S. Leridon
 *
 */
public class StatAction extends ActionBean {

    /**
     * Charge un questionnaire à partir du résultat d'une requête
     */
    public ValueBean populateBean(ResultSet rs) throws SQLException
    {
      Stat s = new Stat();
      
      
      s.setVal(rs.getInt("val"));
      s.setTotal(rs.getInt("total"));
      
      try {
        s.setTitre(rs.getString("titre"));
      } catch (Exception e) {}
      
      s.setNum(s.getVal());
      
      //s.setComment(BasicType.trim(rs.getString("comment")));
      
      
      return s;
    }
  
    /**
     * @param pid
     * @param locked
     * @return
     * @throws SQLException
     */
    public Stat getStat(int pid, int locked) throws SQLException
    {
        return getStat(pid, locked, -1, null, true);
    }
    
    /**
     * @param pid
     * @param locked
     * @param filterPid
     * @param filterVal
     * @return
     * @throws SQLException
     */
    public Stat getStat(int pid, int locked, int filterPid, String filterVal) throws SQLException
    {
        return getStat(pid, locked, filterPid,  filterVal, true);
    }
    
    
    /**
     * @param pid
     * @param locked
     * @param filterPid
     * @param filterVal
     * @param isSum
     * @return
     * @throws SQLException
     */
    public Stat getStat(int pid, int locked, int filterPid, String filterVal, boolean isSum) throws SQLException {
        return getStat(pid, locked, -1, -1, filterPid, filterVal, -1, null,  isSum);
    }
    
    
    public Stat getStat(int pid, int locked, int s_id_parent, int forbiddenSid, int filterPid1, String filterVal1, int filterPid2, String filterVal2, boolean isSum) throws SQLException {  
        return getStat(pid, locked, s_id_parent, forbiddenSid, filterPid1, null, filterVal1, filterPid2, null, filterVal2,  isSum);
    
    }

    /**
     * Retourne un objet Stat contenant les données :
     *  - si isSum est vrai : une somme des valeurs des réponses à la propositon Pid...
     *    sinon : le décompte des valeurs des réponses à la propositon Pid...
     *    
     *  - si isLock = 1 : filtre sur les données des formulaires validés uniquement
     *    si isLock = 0 : filtre sur les données des formulaires NON validés uniquement
     *    si isLock = -1 : tous les formulaires
     *    
     *  - si sid_parent >0 : filtre sur les données de formulaires ayant le sid_parent
     *     
     *  - si forbiddenSid >0 : filtre sur les SID différents du forbiddenSid (pour filtrer le formulaire courant )
     *  
     *  - si filterPid1 >0 : filtre sur les données des formulaire ayant une réponse au Pid1 = filterVal1
     *  - si filterPid2 >0 : filtre sur les données des formulaire ayant une réponse au Pid2 = filterVal2
     *
     * @param pid
     * @param locked
     * @param s_id_parent
     * @param forbiddenSid
     * @param filterPid1
     * @param filterVal1
     * @param filterPid2
     * @param filterVal2
     * @param isSum
     * @return
     * @throws SQLException
     */
    public Stat getStat(int pid, int locked, int s_id_parent, int forbiddenSid, int filterPid1, String filterOperator1, String filterVal1, int filterPid2, String filterOperator2, String filterVal2, boolean isSum) throws SQLException
    {
      StringBuffer sql =
        new StringBuffer("SELECT ");
      if (isSum) {
          sql.append(" sum(r_text) as val, ");
      } else {
          sql.append(" count(r_text) as val, ");
      }
      
      sql.append(" count(submitform.s_id) as total FROM  submitform LEFT OUTER JOIN reponse USING (s_id) " +
                " WHERE p_id=").append(pid);
      
      if (locked >=0) sql.append(" and s_lock=").append(locked);
      
      if (s_id_parent >=0) sql.append(" and s_id_parent =").append(s_id_parent);
      
      if (forbiddenSid >=0) sql.append(" and s_id !=").append(forbiddenSid);
      
      
      if (filterPid1 >0) {
          if (filterOperator1==null) {
              if (filterVal1.contains("%")) {
                  filterOperator1= " LIKE  ";
              } else {
                  filterOperator1= " = ";
              }
          }
          
          sql.append(" and exists (select r_text from reponse  r2 " +
                  " WHERE r2.s_id = submitform.s_id " +
                  " AND r2.p_id ="+filterPid1 +
                  " AND r2.r_text " + filterOperator1 + " '"+filterVal1+"') " 
                  );
      }
      
      if (filterPid2 >0) {
          if (filterOperator2==null) {
              if (filterVal2.contains("%")) {
                  filterOperator2= " LIKE  ";
              } else {
                  filterOperator2= " = ";
              }
          }
          
          sql.append(" and exists (select r_text from reponse  r3 " +
                  " WHERE r3.s_id = submitform.s_id " +
                  " AND r3.p_id = "+filterPid2 +
                  " AND r3.r_text " + filterOperator2 + " '" + filterVal2 + "') " 
                  );
      }
      
      Stat s = (Stat)selectOne(sql.toString());
      if (isSum) {
          s.setType(Stat.TYPE_SUM);
      } else {
          s.setType(Stat.TYPE_COUNT);
      }
      

      return s;
    }
    
    /**
     * @param pid
     * @param locked
     * @param filterPid
     * @param filterVal
     * @param isSum
     * @return
     * @throws SQLException
     */
    public ValueBeanList getStatByOccurence(int pid, int locked, int filterPid, String filterVal) throws SQLException
    {
      StringBuffer sql =
        new StringBuffer("SELECT r_text as titre, sum(s_lock) as val, ");
      
      sql.append(" count(submitform.s_id) as total FROM  submitform LEFT OUTER JOIN reponse USING (s_id) " +
                " WHERE p_id=").append(pid);
      
      if (locked >=0) {
          sql.append(" and s_lock=");
          sql.append(locked);
      }
      
      
      if (filterPid >0) {
          sql.append(" and exists (select r_text from reponse  r2 " +
                  " WHERE r2.s_id = submitform.s_id " +
                  " AND r2.p_id ="+filterPid +
                  " AND r2.r_text = '"+filterVal+"') " 
                  );
      }
      
      sql.append(" group by r_text ");
      
      
      return selectMultiple(sql.toString());
    }
    
    
    /**
     * @param fid
     * @return
     * @throws SQLException
     */
    public Stat getNbForm(int fid) throws SQLException
    {
      StringBuffer sql =
        new StringBuffer("SELECT ");
          
      sql.append(" count(*) as total, sum(s_lock) as val ");
      sql.append("  FROM  submitform  WHERE f_id=").append(fid);
      
      Stat s = (Stat)selectOne(sql.toString());
      
      s.setType(Stat.TYPE_COUNT);
      
      
      return s;
    }

    public ValueBeanList getAllStats(ValueBeanList listProp) throws SQLException {
        ValueBeanList listStat = new ValueBeanList();
        
        for (int i=0; listProp!=null && i<listProp.size(); i++) {
            Proposition p = (Proposition)listProp.get(i);
            
            String titre = "";
            
            Stat s = new Stat();
            Stat s1 = new Stat();
            Stat s2 = new Stat();
            
            s.setPid(p.getId()) ;
            s.setQid(p.getQid()) ;
            
            
            
            if (Controls.isBlank(p.getTexte())) {
                titre = "Q" + s.getQid() + "-P" + s.getPid() ; 
            } else {
                titre = p.getTexte(); 
            }
                

            s.setTitre(titre);
            
            /*   si p_stat=1 on compte les réponses à la proposition, 
            si p_stat=2 on somme les réponses à la proposition, 
            si p_stat=3 on compte chaque occurence de réponse à la proposition */
            
            if (p.getStat()==1) {
                s1 = getStat(p.getId(), -1, -1, null, false); // all
                s2 = getStat(p.getId(),  1, -1, null, false); // locked only
                
                s.setVal(s2.getVal());
                s.setTotal(s1.getVal());
                
                s.setType(Stat.TYPE_COUNT);
                listStat.add(s);
            }
            
            if (p.getStat()==2) {
                s1 = getStat(p.getId(),-1, -1, null, true); // all
                s2 = getStat(p.getId(),1, -1, null, true); // locked only
                
                s.setVal(s2.getVal());
                s.setTotal(s1.getVal());
                s.setNum(s1.getTotal());
                
                s.setType(Stat.TYPE_SUM);
                listStat.add(s);
            }
            
            if (p.getStat()==3) {
                ValueBeanList list = getStatByOccurence(p.getId(),-1, -1, null);
                for (int j=0; list != null && j<list.size(); j++) {
                    s = (Stat)list.get(j);
                    s.setTitre(titre + " " + s.getTitre());
                    s.setType(Stat.TYPE_OCCURENCE);
                    listStat.add(s);
                }
                
            }
            
            
        }
        
        return listStat;
        
    }
    
    /**
     * 
     * @return liste des places disponibles pour les formations
     * @throws SQLException
     */
    public ValueBeanList selectAllStatsRefairsBy(int pid1, int pid2, int pid3) throws SQLException {
        String sql = 
            " SELECT s.s_id AS val, (rp.r_text - ifnull(nbinsr,0)) AS total, rt.r_text AS titre " + 
            " FROM submitform s LEFT JOIN  " + 
            " (SELECT r_text as numsession, count( reponse.s_id ) as nbinsr  " + 
            " FROM reponse " + 
            " WHERE reponse.p_id = " + pid1 + // formation sélectionnée 
            " GROUP BY r_text)  inscriptions ON s.s_id = numsession " + 
            " , reponse rp, reponse rt " + 
            
            " WHERE s.s_id = rt.s_id " + 
            " AND s.s_id = rp.s_id " + 
            " AND rp.p_id = " + pid2 + //nb de places 
            " AND rt.p_id = " + pid3 + //intitulé formation
            " AND s.f_id =2 " + 
            " AND s.s_lock =1 " + 
            " ORDER BY titre " ;
            
            /* *** 30 fois plus long !!!
            " SELECT s.s_id AS val, " +
            " (rp.r_text - count( r.s_id )) AS total, " +
            " rt.r_text as titre " +
            " FROM submitform s " +
            " LEFT JOIN reponse r ON r.p_id = " + pid1 +
            " AND r.r_text = s.s_id " +
            " INNER JOIN reponse rp ON rp.s_id = s.s_id " +
            " AND rp.p_id = " + pid2 +
            " INNER JOIN reponse rt ON rt.s_id = s.s_id " +
            " AND rt.p_id = " + pid3 +
            " WHERE s.f_id =2  AND s.s_lock =1 " +
            " GROUP BY s.s_id ORDER BY s.s_id " ;*/
        
        
        return selectMultiple(sql);
    }
    
    public ValueBeanList getAllForms() throws SQLException {
        ValueBeanList listStat = new ValueBeanList();
        ValueBeanList listForms = null;
        
        QuestionnaireAction qa = new QuestionnaireAction();
        listForms = qa.selectAll();
        
        for (int i=0; listForms!=null && i<listForms.size(); i++) {
            Questionnaire q = (Questionnaire)listForms.get(i);
            
            Stat s = getNbForm(q.getId());
            s.setTitre("F"+q.getId()+":  "+ q.getTitre()+" ");
            s.setType(Stat.TYPE_COUNT);
            listStat.add(s);
        }
        
        return listStat;
        
    }
    
}
