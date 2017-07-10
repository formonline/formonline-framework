package com.triangle.lightfw;

import javax.naming.*;
import java.sql.*;

import javax.sql.*;

import org.apache.log4j.Logger;

/**
 *
 * Cette classe permet de de rendre abstraite la notion de base de données
 * et d'implémenter le mécanisme de gestion de pool de connexions.
 * Cette classe comporte essentiellement des propriétés et des méthodes
 * statiques, l'initialisation se fait par la construction d'un objet,
 * le constructeur initialise toutes les données statiques, il n'est pas
 * nécéssaire de conserver une instance de DataBase pour travailler,
 * les méthodes utiles étant statiques: getConnection, releaseConnection et
 * close DataBase.
 * Nécéssite JDBC 2.0 (JDK 1.4 ou ajout jdbc2_0-stdext.jar avec JDK 1.3).
 * 
 * @see InitServlet
 * 
 * @author Pascal
 */
public final class DataBase {

  //static protected Logger  = Logger.getLogger(getInitParameter("LogSource"));

  /** Nom des paramètres et valeurs de Init.xml pour la gestion SGBD */
  static public final String JDBC_POOL_MODE_NOPOOL = "NoPool";
  static public final String JDBC_POOL_MODE_AS400 = "iSeries";
  static public final String JDBC_POOL_MODE_AS = "ApplicationServer";
  
  static public final String AS_TOMCAT = "Tomcat";
  static public final String AS_WEBSPHERE = "WebSphere";

  /** Pas de pool, accés par le driver JDBC précisé dans Init.xml */
  public static final int POOL_MODE_NOPOOL = 0;
  /** Pool géré par la Toolbox IBM, paramètres dans le Init.xml */
  public static final int POOL_MODE_AS400 = 1;
  /** Pool géré par le serveur d'applications, paramètres dans server.xml */
  public static final int POOL_MODE_AS = 2;

  /*private int initPoolMode ;
  private String initUser ;
  private String initPassword ;*/
  
    // Source des données pour Tomcat
  private static DataSource ds = null;

  
  /**
   * 
   * Constructeur de DataBase, initialise les propriétés statiques.
   * Ce constructeur ne doit être appelé qu'une seule fois, idéalement par
   * la méthode init() d'une servlet d'initialisation (InitServlet).
   * 
   * @param poolMode Indique le mode de gestion du pool de connexions.
   * POOL_MODE_NOPOOL: Pas de pool, les paramètres du driver sont définis
   * dans le fichier Init.xml.
   * POOL_MODE_AS: Le pool est géré par le serveur d'applications,
   * les paramètres du pool sont définis dans le serveur.xml. La DataSource
   * doit être définie dans le fichier sous le nom JNDI jdbc/<Instance>.
   * POOL_MODE_AS400: Le pool est géré dans l'application par la ToolBox
   * IBM, les paramètres du pool sont définis dans le fichier Init.xml.
   * 
   * @param host Nom du système où se trouve la base de données dans 
   * le mode POOL_MODE_AS400, URL JDBC dans le mode POOL_MODE_NOPOOL,
   * nom du serveur d'applications dans le mode POOL_MODE_AS.
   * 
   * @param driver Nom du driver JDBC dans le mode POOL_MODE_NOPOOL,
   * nom de la ressource JNDI dans le mode POOL_MODE_AS, ignoré dans 
   * le mode POOL_MODE_AS400.
   * 
   * @param nbConnectionInPool Nombre de connexions dans le pool.
   * Utilisé uniquement dans le mode POOL_MODE_AS400ignoré dans les
   * modes POOL_MODE_NOPOOL et POOL_MODE_AS.
   * 
   * @param user Nom du login sur la base
   * 
   * @param password Mot de passe sur la base
   * 
   */
  public DataBase(
    int poolMode,
    String host,
    String driver,
    int nbConnectionInPool,
    String user,
    String password,
    Logger traceLog)
  {

    /*setInitPoolMode(poolMode);
    setInitUser(user);
    setInitPassword( password);*/

    try
    {
      InitialContext ctx = new InitialContext();
      if (ctx != null)
      {
        ds = (DataSource)ctx.lookup("java:comp/env/" + driver);
      }
      if (ctx == null)
      {
        throw new Exception("Context impossible à créer !");
      }
      else if (ds == null)
      {
        throw new Exception(
          "DataSource " + host + " " + driver + " impossible à créer !");
      }
      traceLog.info("Datasource " + host + " " + driver + " créée !");
    }
    catch (Exception e)
    {
      traceLog.fatal(
        "Erreur dans l'initialisation de la DataSource: poolMode="
          + poolMode
          + ", host='"
          + host
          + "', driver='"
          + driver
          + "'!",
        e);
    }
  }

  /**
   * 
   * Récupère une connexion disponible dans le pool. Cette connexion
   * doit impérativement être libérée par l'appel à la méthode 
   * releaseConnection(), sous pène de bloquer le pool.
   * 
   * @return Connection
   * 
   * @throws Exception Si le pool a des problèmes,
   * exception de type SQL ou IO.
   * 
   */
  public static Connection getConnection() throws SQLException
  {
    Connection conn = null;

    if (ds != null)
    {
      conn = ds.getConnection();
    }

    return conn;
  }

  /**
   * 
   * Libère et remet à null une connection récupérée par getConnection().
   * 
   * @param conn Connexion à libérer.
   * 
   */
  public static void releaseConnection(Connection conn, Logger traceLog)
  {
    try
    {
      if (conn != null)
      {
        conn.close();
      }
    }
    catch (SQLException e)
    {
      traceLog.error("Erreur dans la libération de la connection !" + e);
    }
    finally
    {
      conn = null;
    }
  }

  /**
   * 
   * Libère le pool de connexion à la base de données. Cette méthode doit 
   * être appelée à la fin du programme, idéalement par la méthode destroy()
   * d'une servlet d'initialisation (InitServlet).
   * 
   */
  public static void closeDataBase()
  {
  }


}