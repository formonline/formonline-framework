package com.triangle.lightfw;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import org.apache.log4j.Logger;


/**
 * 
 * ActionBean est la classe dans laquelle on trouve toutes les opérations
 * d'accès à la base de données. Tous les Beans de traitement héritent de 
 * cette classe.
 * 
 * @version 12 nov. 02: Intégration de la DataBase et de la servlet
 * d'initialisation
 *  
 * @author Pascal
 *
 */
public abstract class ActionBean
{

  static protected Logger traceLog = Logger.getLogger("FOLE");
  // Connection à la base
  private Connection cnx = null;

  /**
   * Ouvre une connexion à la base de données, via le pool de connexions.
   * @return
   * @throws SQLException
   */
  public Connection openConnection() throws SQLException
  {
    return DataBase.getConnection();
  }

  /**
   * Libère la connexion à la base de données.
   * @param cnx Connexion à libérer.
   */
  public void closeConnection(Connection cnx)
  {

      
    try
    {
      if (cnx.getAutoCommit() == false)
      {
        cnx.commit();
      }
    }
    catch (SQLException e)
    {
      traceLog.error("Erreur en fermeture de connexion " + e);
    }
    DataBase.releaseConnection(cnx,traceLog);
  }

  /**
   * Méthode utilisée pour exécuter une requête de selection devant
   * ramener un résultat unique.
   * @param sql Requête Select...
   * @return ValueBean
   */
  public ValueBean selectOne(String sql) throws SQLException
  {
    ValueBean returnBean = null;
    cnx = openConnection();
    Statement requete = cnx.createStatement();

    try
    {
      if (traceLog.isDebugEnabled())
      {
        traceLog.debug("Exécution de " + sql);
      }
      ResultSet resultat = requete.executeQuery(sql);
      if (resultat.next())
      {
        returnBean = populateBean(resultat);
      }
      resultat.close();
      requete.close();
      closeConnection(cnx);
    }
    catch (SQLException e)
    {
      closeConnection(cnx);
      traceLog.fatal("Erreur SGBD !", e);
      throw (e);
    }

    return returnBean;
  }

  /**
   * Méthode utilisée pour exécuter une requête de selection devant
   * ramener une liste de résultats. 
   * @param sql Requête Select...
   * @return Vector Vecteur des résultats ou null si la reqête n'a
   * pas aboutie.
   */
  public ValueBeanList selectMultiple(String sql)
    throws SQLException
  {
    LinkedList<ValueBean> v = new LinkedList<ValueBean>();
    cnx = openConnection();
    Statement requete = cnx.createStatement();

    try
    {
      if (traceLog.isDebugEnabled())
      {
        traceLog.debug("Exécution de " + sql);
      }
      ResultSet resultat = requete.executeQuery(sql);
      ValueBean b = null;
      while (resultat.next())
      {
        b = populateBean(resultat);
        v.add(b);
      }
      if (v.size() == 0)
      {
        v = null;
      }
      resultat.close();
      requete.close();
      closeConnection(cnx);
    }
    catch (SQLException e)
    {
      closeConnection(cnx);
      traceLog.fatal("Erreur SGBD !", e);
      throw (e);
    }

    return ValueBeanList.getFromList(v);
  }

  /**
   * Méthode utilisée pour exécuter une requête de modification de données
   * dans la base (Create, Update, Delete).
   * @param sql Requête Insert...
   * @return Le nombre de lignes mises à jour
   */
  public int updateData(String sql) throws SQLException
  {
    int nbLignesMaj = 0;

    try
    {
      cnx = openConnection();
      Statement requete = cnx.createStatement();

      if (traceLog.isDebugEnabled())
      {
        traceLog.debug("Exécution de " + sql);
      }
      nbLignesMaj = requete.executeUpdate(sql);

      requete.close();
      closeConnection(cnx);
    }
    catch (SQLException e)
    {
      closeConnection(cnx);
      traceLog.fatal("Erreur SGBD !", e);
      throw (e);
    }

    return nbLignesMaj;
  }

  /**
   * Méthode utilisée pour exécuter une procédure stockée
   * 
   * @param ps nom de la procédure stockée
   * @param params liste des paramètres
   * @return Le nombre de lignes mises à jour
   */
  public String callProc(String procstock, LinkedList<String> params) 
  {
    String msg=null;
      
    boolean result=false;
    CallableStatement cstm;
    
    try
    {
      cnx = openConnection();
      
      if (traceLog.isDebugEnabled()) {
          traceLog.debug("Lancement de la procédure : " + procstock);
          if (params!=null) traceLog.debug("Paramètres : "+params);
      }
      
      cstm=cnx.prepareCall("{ CALL " + procstock + "}");
      
      for (int i=0; params!=null && i<params.size(); i++) {
          
          cstm.setString(i+1, (String)params.get(i));
      }
      
      result = cstm.execute();
      
      /*if (!result) {
          msg = "Echec execution de :" + procstock;
          traceLog.error("Echec execution de :" + procstock);
          return msg;
      }*/

      cstm.close();
      closeConnection(cnx);
    }
    catch (SQLException e)
    {
      closeConnection(cnx);
      msg = "Erreur SGBD ! "+ e.getMessage();
      traceLog.fatal(msg);
      return msg;
    }

    return msg;
  }
  
  
  /**
   * Méthode abstraite, à redéfinir dans les beans dérivés, utilisée pour
   * remplir les ValueBeans de résultats suite à l'exécution des méthodes
   * selectOne() ou selectMultiple().
   * @param rs ResultSet pointant sur une ligne de résultat
   * @return ValueBean ValueBean du bon type chargé avec les
   * données du ResultSet.
   */
  public abstract ValueBean populateBean(ResultSet rs)
    throws SQLException;

  /**
   * Méthode qui exécute une transaction et qui assure le commit et 
   * le rolback de celle-ci.
   * @param t Objet de type dérivé de AbstractTransaction dont la 
   * méthode doIt contient le code de la transaction à effectuer.
   * @return ValueBean résultat de la transaction
   */
  /*
  public ValueBean doTransaction(
    AbstractTransaction t,
    Hashtable table)
    throws SQLException
  {
    ValueBean result = null;
  
    try
    {
      if (traceLog.isDebugEnabled())
      {
        traceLog.debug("Début de transaction");
      }
      cnx = openConnection();
      cnx.setAutoCommit(false);
      result = t.doIt(this, cnx, table);
      cnx.commit();
      cnx.setAutoCommit(true);
      closeConnection(cnx);
      if (traceLog.isDebugEnabled())
      {
        traceLog.debug("Fin de transaction OK");
      }
    }
    catch (SQLException e)
    {
      traceLog.error(
        "Fin de transaction en erreur, Rollback : " + e.getLocalizedMessage(),
        e);
      try
      {
        if (cnx.getMetaData().supportsTransactions())
        {
          // Bug sous MySQL
          cnx.rollback();
        }
        cnx.setAutoCommit(true);
        closeConnection(cnx);
      }
      catch (SQLException e2)
      {
        closeConnection(cnx);
        throw (e2);
      }
    }
  
    return result;
  }
  */
}