/*
 * Créé le 21 oct. 04
 */
package formOnLine.actions;

import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import formOnLine.Controls;
import formOnLine.InitServlet;
import formOnLine.LdapControl;
import formOnLine.Message;
import formOnLine.ServControl;
import formOnLine.actions.PropositionAction;
import formOnLine.actions.ReponseAction;
import formOnLine.actions.SubmitFormAction;
import formOnLine.msBeans.Proposition;
import formOnLine.msBeans.Questionnaire;
import formOnLine.msBeans.SubmitForm;
import formOnLine.msBeans.UserData;

import com.triangle.lightfw.SessionInfos;
import com.triangle.lightfw.ValueBeanList;


/**
 * Classe dédiée au chargement et à l'initialisation de formulaires en session après la connection (les formulaires fils), 
 * avant chaque passage par la page d'accueil (la mise à jour des formulaires "message" : fid=4)
 * ou avant chaque création de formulaire (aucune action)
 * 
 * Cette classe est dédiée à être surchargée par la class ContextUserAction en fonction du contexte d'utilisation 
 * 
 * @author S. LERIDON
 */
public class UserAction implements UserActionInterface {

    protected  Logger traceLog = Logger.getLogger("FOLE");
    protected  String MSG_BAD_LOGIN = //"Echec lors de la tentative d'authentification. ";
            Message.getString("msg.login.MSG_BAD_LOGIN"); 
    protected  String MSG_PB_CONF = 
            Message.getString("msg.login.MSG_BAD_CONF"); 
    protected  String MSG_CONNECTAS_DENIED = 
            Message.getString("msg.login.MSG_CONNECTAS_DENIED");
    
    public UserAction () {
    }

    /* (non-Javadoc)
     * @see formOnLine.actions.UserActionInterface#check(java.lang.String, java.lang.String, int, int, java.lang.String)
     */
    public String check( UserData ud, int loginPid, int roleQid, String ldapSource) {

               
        SubmitFormAction sfa = new SubmitFormAction();
        //PropositionAction pa = new PropositionAction();
        //ReponseAction ra = new ReponseAction(); 

        SubmitForm sf = null;
        //Properties properties = null;

        boolean ldapChecked = false;

        try {

            // controle LDAP
            if (!Controls.isBlank(ldapSource) ) {

                try {
                    LdapControl ldap = new LdapControl ();
                    String connected = ldap.checkPwdLdap(ud.getLogin(),ud.getPwd()) ;

                    if (connected != null) {
                        ldapChecked = true;
                        //ud=new UserData();
                        ud.setName(connected);
                    }

                } catch (Exception e) {
                    traceLog.error("Authentification LDAP refusée : "+ e.getMessage());
                }
            }

            // selection du formulaire par la proposition login
            
            // s'il y a un domaine du type @cridf.iledefrance.fr, on retire
            if (ud.getLogin().indexOf("@")>0) ud.setLogin(ud.getLogin().substring(0,ud.getLogin().indexOf("@")));
            
            sf = sfa.selectByPid( loginPid, ud.getLogin(), true );
            

            // si on n'est pas en mode LDAP, vérification mot de passe
            if (sf != null && !ldapChecked 
                    && !sf.getPwd().equals(ud.getPwd()) ) {
                // echec authentification
                sf = null;
            }

            // si on est en mode authentification par ID de formulaire
            if (sf == null && Controls.checkInt(ud.getLogin(),true)==null && !ldapChecked ) {
                // selection du formulaire par le Sid 
                int id= Integer.parseInt(ud.getLogin());
                sf = sfa.selectBySidAndPwd(id,ud.getPwd(),true, true, true);
            }

            // l'authentification est refusée
            if (sf==null) {
                ud=null;
                return MSG_BAD_LOGIN;
            }


            initUserData(ud, sf, roleQid);

           

        } catch (Exception e) {
            traceLog.error(MSG_PB_CONF +" : "+ e.getMessage());
            return MSG_PB_CONF;
        }
        
        return null; //ok

    }

    
    /**
     * INitialisation de l'objet  userData suite à authentification réussie
     *  
     * @param ud
     * @param sf
     * @param roleQid
     * @return
     * @throws SQLException
     */
    public String initUserData(UserData ud, SubmitForm sf, int roleQid) throws SQLException {
        
        PropositionAction pa = new PropositionAction();
        ReponseAction ra = new ReponseAction();
        
        // l'authentification est acceptée, on alimente le userdata
        ud.setName(sf.getTitre());
        ud.setPwd(sf.getPwd());
        ud.setId(sf.getS_id());
        ud.setParent_id(sf.getS_id_parent());
        ud.setF_id(sf.getF_id());
        ud.setRole(0);

        if (roleQid>0) {
            ValueBeanList listProp = pa.selectAllByQuestId(roleQid);

            for (int i=0 ; listProp != null && i < listProp.size(); i++) {
                Proposition prop = (Proposition)listProp.get(i);


                if (ra.selectOne(prop.getId(), sf.getS_id()) != null){
                    // on affecte le numéro de la proposition comme code profil
                    // le numéro doit correspondre à 0, 1, 2, 3
                    // = pas d'accès, lecture, ecriture, admin
                    ud.setRole(prop.getNum());
                    break;
                }
            }

        }

        // ajout formulaire
        ud.addRep(sf);

        traceLog.info("Connexion : "+ ud.getName());
        
        return null;
    }
    
    /* (non-Javadoc)
     * @see formOnLine.actions.UserActionInterface#initAfterCheck(formOnLine.msBeans.UserData)
     */
    public String initAfterCheck(UserData ud, HashMap<String, String> config)    {

        String msg = null;
        if (ud == null) return null;

        SubmitFormAction sfa = new SubmitFormAction();

        try {

            //      ajout formulaires fils
            ValueBeanList vbl = sfa.selectAllChildrenByFid(ud.getId() ,-1," ORDER BY TITRE ",true, -1);

            // chargement des champs à initialiser pour les fils
            sfa.setInitVals(vbl);

            if (vbl!=null) ud.addListRep(vbl.getList());

        } catch (SQLException e) {
            msg = "Problème initialisation après login : "+ e.getMessage();
            traceLog.error(msg);
        }

        return msg;


    }

    /* (non-Javadoc)
     * @see formOnLine.actions.UserActionInterface#testBeforeCreateSf(formOnLine.msBeans.SubmitForm)
     */
    public String testBeforeCreateSf(SubmitForm sf, UserData ud)   { 
        return null;
    }
    
    /* (non-Javadoc)
     * @see formOnLine.actions.UserActionInterface#initBeforeNewSf(formOnLine.msBeans.SubmitForm)
     */
    public String initBeforeNewSf(Questionnaire q, UserData ud)   { 
        return null;
    }

    /* (non-Javadoc)
     * @see formOnLine.actions.UserActionInterface#testBeforeCreateSf(formOnLine.msBeans.SubmitForm)
     */
    public String doAfterCreateSf(SubmitForm sf, UserData ud)   { 
        return null;
    }

    /* (non-Javadoc)
     * @see formOnLine.actions.UserActionInterface#testBeforeCreateSf(formOnLine.msBeans.SubmitForm)
     */
    public String doAfterUpdateSf(SubmitForm sf, UserData ud)    { 
        return null;
    }

    /* (non-Javadoc)
     * @see formOnLine.actions.UserActionInterface#doAfterLockSf(formOnLine.msBeans.SubmitForm)
     */
    public String doAfterLockSf(SubmitForm sf, UserData ud)  {
        return null;
    }

    /* (non-Javadoc)
     * @see formOnLine.actions.UserActionInterface#doAfterUnlockSf(formOnLine.msBeans.SubmitForm)
     */
    public String doAfterUnlockSf(SubmitForm sf, UserData ud)  {
        return null;
    }


    /* (non-Javadoc)
     * @see formOnLine.actions.UserActionInterface#testBeforeUpdateSf(formOnLine.msBeans.SubmitForm)
     */
    public String testBeforeUpdateSf(SubmitForm sf, UserData ud)   { 
        return null;
    }



    public String testBeforeDeleteSf(SubmitForm sf, UserData ud)  { 
        return null;
    }

    public String doAfterDeleteSf(SubmitForm sf, UserData ud)  { 
        return null;
    }

    /* (non-Javadoc)
     * @see formOnLine.actions.UserActionInterface#initBeforeIndex(formOnLine.msBeans.UserData, javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest)
     */
    public String initBeforeIndex(UserData u, HttpSession session, HttpServletRequest req)   {

        int msgFid=4;
        String msg = null;

        //       init des messages d'information (ici, issus du formulaire 4 par convention)
        SubmitFormAction sfa = new SubmitFormAction();

        try {
            ValueBeanList listMsg = sfa.selectAllByFormId(msgFid, 1);
            for (int i=0; listMsg!=null && i< listMsg.size(); i++){
                SubmitForm rep = (SubmitForm)listMsg.get(i);
                u.addOrReplaceRep(rep);
            }
            
            
        } catch (SQLException e) {
            msg = "Problème initialisation après login : "+ e.getMessage();
            traceLog.error(msg);
        }

        return msg;


    }
    
    
    /** fonction "connect AS" : 
     * permet de se connecter comme n'importe quel autre compte si on est admin, 
     * ou en fonction de conditions locales si la fonction est surchargée pour une autre instantion du framework
     * 
     * @param activeUserData
     * @param newUserData
     * @return null si OK, message d'erreur sinon
     * @throws SQLException 
     */
    public String connectAs(UserData activeUserData, UserData newUserData, int roleQid) throws SQLException {
        String msg = null;
        
        
        if (activeUserData!=null && activeUserData.getRole()>=SessionInfos.ROLE_ADMIN && newUserData!=null) {
            
            SubmitFormAction sfa = new SubmitFormAction();
            
            int id= Integer.parseInt(newUserData.getLogin());
            SubmitForm sf = sfa.selectBySId(id); 
            
            if (sf==null) return MSG_CONNECTAS_DENIED;
            
            initUserData(newUserData, sf, roleQid);
            
        } else {
            msg = MSG_CONNECTAS_DENIED;
        }
        
        return msg;
    }

    /* (non-Javadoc)
     * @see formOnLine.actions.UserActionInterface#doBeforeSendPwd(...)
     */
    public String doBeforeSendPwd(SubmitForm sf, UserData ud) {
        
        return null;
    }
    
    /* (non-Javadoc)
     * @see formOnLine.actions.UserActionInterface#doSpecificAction(java.lang.String[])
     */
    public String doSpecificAction(String[] args, UserData ud) throws SQLException {
        return null;
    };
}
