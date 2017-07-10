package formOnLine;

import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;






import com.triangle.lightfw.DataBase;

/**
 * Servlet d'initialisation de l'application formOnLine. Seules les méthodes
 * init() et destroy() sont définies. Cette servlet est chargé au lancement
 * du serveur d'applications (<load-on-startup>1).
 *
 * @version 12/11/02: Initialisation de Log4J et du pool de connection.
 *  
 * @author Pascal
 */
public class InitServlet extends HttpServlet
{

  /**
     * 
     */
    private static final long serialVersionUID = 1L;
  static public final String LOG4J_INIT_FILE        = "log4j.properties";
  static public final String DataSourceName         = "DataSource";
  static public final String logFileName            = "LogSource";
  static public final String MAIL_SERVER            = "MailServer";
  static public final String MAIL_FROM              = "MailFrom";
  static public final String XmlRpcAuthorizedUser  = "XmlRpcAuthorizedUser";
  static public final String XmlRpcAuthorizedPwd   = "XmlRpcAuthorizedPwd";
  static public final String FileUploadDir = "FileUploadDir";
  static public final String FileImportDir = "FileImportDir";
  static public final String FileExportDir = "FileExportDir";
  static public final String ExportCharset = "ExportCharset";
  static public final String Extensions = "Extensions";
  
  static public final String LdapSRC = "LdapSource";
  static public final String LdapPrt = "LdapPort";
  static public final String LdapBDN = "LdapBaseDN";
  static public final String LdapU = "LdapUser";
  static public final String LdapP = "LdapPwd";
  static public final String adminP = "adminPwd";
  
  static public Logger    traceLog          = null;
  
  static protected String    mailServerAddress = null;
  static protected String    mailFromAddress   = null;
  static protected String    version = null;
  static protected String    authorizedUser = null;
  static protected String    authorizedPwd = null;
  static protected String    fileUplDir = null;
  static protected String    fileImpDir = null;
  static protected String    fileExpDir = null;
  static protected String    exportCharset = null;
  static protected String    extensions = null;
  
  static protected String LdapSource = null;
  static protected String LdapPort = null;
  static protected String LdapBaseDN = null;
  static protected String LdapUser = null;
  static protected String LdapPwd = null;
  static protected String adminPwd = null;
  

  /**
   * Initialisation de l'environnement de l'application:
   *  Log4J dans /WEB-INF/classes/util/log4j.properties,
   *  formOnLine dans /WEB-INF/Init.xml.
   */
  public void init()
  {
    // Initialisation de Log4J  
    
    //traceLog = Logger.getLogger(getInitParameter(logFileName));
    traceLog = Logger.getLogger( "FOLE");
    
    String prefix = getServletContext().getRealPath("/");
    PropertyConfigurator.configure(prefix + LOG4J_INIT_FILE);

    // Initialisation de l'adresse du serveur mail
    mailServerAddress = getServletContext().getInitParameter(MAIL_SERVER);
    mailFromAddress = getServletContext().getInitParameter(MAIL_FROM);
    authorizedUser = getServletContext().getInitParameter(XmlRpcAuthorizedUser);
    authorizedPwd = getServletContext().getInitParameter(XmlRpcAuthorizedPwd);
    version = getServletContext().getInitParameter("version");
    fileUplDir = getServletContext().getInitParameter(FileUploadDir);
    fileImpDir = getServletContext().getInitParameter(FileImportDir);
    fileExpDir = getServletContext().getInitParameter(FileExportDir);
    exportCharset = getServletContext().getInitParameter(ExportCharset);

    extensions = getServletContext().getInitParameter(Extensions);
    adminPwd = getServletContext().getInitParameter(adminP);
    
    LdapSource = getServletContext().getInitParameter(LdapSRC);
    LdapPort = getServletContext().getInitParameter(LdapPrt);
    LdapBaseDN = getServletContext().getInitParameter(LdapBDN);
    LdapUser = getServletContext().getInitParameter(LdapU);
    LdapPwd = getServletContext().getInitParameter(LdapP);
    
    // Initialisation de la base de donnéees
    new DataBase(
      DataBase.POOL_MODE_AS,
      DataBase.AS_TOMCAT,
      getServletContext().getInitParameter(DataSourceName),
      0,
      null,
      null,
      traceLog);
    

    traceLog.info(getServletContext().getServletContextName()+ " **********   Portlet initialisée   **********");
  }

  /**
   * TRIANGLE 19/05/2005: Retourne l'URL du serveur de mails
   * @return Url serveur de mail
   */
  public static String getMailServerAddress()
  {
    return mailServerAddress;
  }
  
  public static String getVersion()
  {
    return version;
  }
  
  public static String getFileUploadDir()
  {
    return fileUplDir;
  }
  
  public static String getFileImportDir()
  {
    return fileImpDir;
  }
  public static String getFileExportDir()
  {
    return fileExpDir;
  }
  
  /**
   * @return Returns the extensions.
   */
  public static String getExtensions() {
      return extensions;
  }
  
  public static String getXmlRpcAuthorizedUser()
  {
    return authorizedUser;
  }
  public static String getXmlRpcAuthorizedPwd()
  {
    return authorizedPwd;
  }

  /**
   * TRIANGLE 19/05/2005: Retourne l'adresse From pour les mails envoyés
   * @return Adresse mail CR IdF
   */
  public static String getMailFromAddress()
  {
    return mailFromAddress;
  }
  
  /**
   * Permet de terminer proprement l'environnement de l'application,
   * et de libérer le pool de connexions à la base de données.
   */
  public void destroy()
  {
    DataBase.closeDataBase();
    traceLog.info( getServletContext().getServletContextName()+ " **********   Portlet terminée   **********");
  }

/**
 * @return Returns the ldapBaseDN.
 */
public static String getLdapBaseDN() {
    return LdapBaseDN;
}


/**
 * @return Returns the ldapSource.
 */
public static String getLdapSource() {
    return LdapSource;
}

/**
 * @return Returns the ldapPort.
 */
public static int getLdapPort() {
    try {
        return Integer.parseInt(LdapPort);
    } catch (NumberFormatException e) {
        return 389;
    }    
}

/**
 * @return Returns the traceLog.
 */
public static String getTraceLogName() {
    return logFileName;
}

/**
 * @return Returns the ldapPwd.
 */
public static String getLdapPwd() {
    return LdapPwd;
}

/**
 * @return Returns the adminPwd.
 */
public static String getAdminPwd() {
    return adminPwd;
}

/**
 * @return Returns the ldapUser.
 */
public static String getLdapUser() {
    return LdapUser;
}

/**
 * @return the exportCharset
 */
public static String getExportCharset() {
    return exportCharset;
}

/**
 * @param exportCharset the exportCharset to set
 */
public static void setExportCharset(String exportCharset) {
    InitServlet.exportCharset = exportCharset;
}


}
