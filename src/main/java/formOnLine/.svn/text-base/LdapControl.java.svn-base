/* **********************************
 * 
 * formOnLine : LdapControl.java
 * Created on 25 janv. 2008 by seleridon
 * 
 ************************************ */
package formOnLine;

import org.apache.directory.api.ldap.model.cursor.EntryCursor;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.message.BindRequest;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.ldap.client.api.LdapConnectionConfig;
import org.apache.directory.ldap.client.api.LdapNetworkConnection;
import org.apache.directory.ldap.client.api.NoVerificationTrustManager;
import org.apache.log4j.Logger;



public class LdapControl {
    
    
    public static final String MESSAGE_MAUVAIS_LOGIN = "Erreur > identifiants incorrects";
    
//  Param�tres LDAP
    private  int ldapPort = InitServlet.getLdapPort(); 
    private  String ldapHost = InitServlet.getLdapSource(); 
    private  String ldapBaseDN = InitServlet.getLdapBaseDN();
    private  String ldapUser = InitServlet.getLdapUser();
    private  String ldapPwd = InitServlet.getLdapPwd();
    private  String loginAttr = "cn";
    
    static protected Logger traceLog = Logger.getLogger(InitServlet.getTraceLogName());
    
    public LdapControl() {}
    
    
    /**
     * @param login
     * @param pwd
     * @return
     */
    public String checkPwdLdap(String login, String pwd) {
        
        String userConnected = null;
        
        //  login/mdp obligatoire
        if (login ==null || login.trim().equals("") || pwd ==null || pwd.trim().equals("") ) return null;
        
        traceLog.debug("Tentative de connexion au LDAP -------------------------");
        
        try {
            LdapNetworkConnection connection = null;
            
            if (ldapPort == 636) {
            
                // SSL  (nécessite certificats AD ldapS dans le keystore du SDK 1.7)
                LdapConnectionConfig sslConfig = new LdapConnectionConfig();
                sslConfig.setLdapHost( ldapHost );
                sslConfig.setUseSsl( true );
                sslConfig.setLdapPort( 636 );
                sslConfig.setTrustManagers( new NoVerificationTrustManager() );
                sslConfig.setSslProtocol("SSLv3");
                connection = new LdapNetworkConnection( sslConfig );
            
            } else {
                // normal
                connection = new LdapNetworkConnection( ldapHost, ldapPort );
            }
            
            // connect to the server
            connection.connect();
            traceLog.debug("> connect ok");
            
            if (Controls.isBlank(ldapUser)) {
                // authenticate anonymous to the server 
                connection.bind(  "", "" );
                traceLog.debug("> bind anonymous ok");
            } else {
                //authenticate to the server 
                connection.bind( ldapUser, ldapPwd);
                traceLog.debug("> bind " + ldapUser  + " : ok");
            }
            
            // par défaut
            loginAttr = "sAMAccountName";
            // cas de l'authent avec l'UPN avec domaine
            if (login.contains("@")) loginAttr = "UserPrincipalName"; 
            
            String[] attrAuth = {"sn","givenName"};
            
            
            // recherche DN user
            EntryCursor cursor = connection.search( 
                    ldapBaseDN,
                    "("+loginAttr+"="+ login +")",
                    SearchScope.SUBTREE, 
                    attrAuth );
            traceLog.debug("> search user ok");
            
            if ( cursor.next() ) {
                Entry e = cursor.get();
                
                String fullName = e.get("sn").getString()
                + " " + e.get("givenName").getString();
                
                // authenticate user to the server 
                connection.unBind();
                connection.bind( e.getDn() , pwd );
                
                if (connection.isAuthenticated()) {
                    traceLog.debug("> bind user ok");
                    userConnected = fullName;
                }
            }
            
            //          disconnect with the server AUTH
            connection.unBind();
            connection.close();

            traceLog.debug("Déconnexion LDAP -----------------------------");
            
        } catch ( Exception e) {
            traceLog.debug(e.getMessage());
        }
        
        return userConnected;
    }
    
}
