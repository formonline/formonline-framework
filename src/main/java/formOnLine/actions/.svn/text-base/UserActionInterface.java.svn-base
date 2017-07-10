package formOnLine.actions;

import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import formOnLine.msBeans.Questionnaire;
import formOnLine.msBeans.UserData;
import formOnLine.msBeans.SubmitForm;


/**
 * Interface de l'objet UserAction permettant de préciser des comportements
 * spécifiques d'initialisation ou de contrôle par injection de dépendance 
 * pour chaque utilisation de formOnLine
 * 
 * @author S LERIDON
 *
 */

/**
 * @author SeLERIDON
 *
 */
public interface UserActionInterface {
    

    /**
     * action de contrôle et d'initialisation de la connexion
     * pour initialiser des données en session
     * 
     * @param une instance de UserData
     * @param login
     * @param pwd
     * @param loginPid
     * @param roleQid
     * @param ldapSource
     * @return Message erreur authentification si la connection ne réussit pas, null sinon
     */
    public String  check( UserData ud, int loginPid, int roleQid, String ldapSource)   ;
    

    /**
     * Fonction lancée avant chaque passage ou retour à la page d'accueil
     * pour contrôler les données soumises
     *   
     * @param u
     * @param session
     * @param req
     * @return null si ok, un message d'erreur sinon
     */
    public String initBeforeIndex(UserData u, HttpSession session, HttpServletRequest req) ;

    /**
     * Fonction lancée apres initialisation de la connexion
     * 
     * @param userData
     * @return   null si ok, un message d'erreur sinon
     */
    public String initAfterCheck(UserData userData, HashMap<String, String> config) ;

    
    /**
     * Fonction lancée avant chaque création (soumission) de formulaire (submitform)
     * pour initialiser ou mettre à jour des données en session
     * 
     * @param sf
     * @return   null si ok, un message d'erreur sinon
     */
    public String testBeforeCreateSf(SubmitForm sf, UserData ud) ;
    
    /**
     * Fonction lancée avant chaque mise à jour de formulaire (submitform)
     * pour contrôler les données soumises
     * 
     * @param sf
     * @return null si ok, un message d'erreur sinon
     */
    public String testBeforeUpdateSf(SubmitForm sf, UserData ud) ;
    
    /**
     * Fonction lancée avant chaque suppression de formulaire (submitform)
     * pour contrôler les données soumises
     * 
     * @param sf
     * @return null si ok, un message d'erreur sinon
     */
    public String testBeforeDeleteSf(SubmitForm sf, UserData ud) ;
    
    /**
     * Fonction lancée après chaque création (soumission) de formulaire (submitform)
     * pour complément de traitement
     * 
     * @param sf
     * @return   null si ok, un message d'erreur sinon
     */
    public String doAfterCreateSf(SubmitForm sf, UserData ud) ;
    
    /**
     * Fonction lancée avant chaque affichage pour initialiser un nouveau formulaire (submitform)
     * pour complément de traitement
     * 
     * @param sf
     * @return   null si ok, un message d'erreur sinon
     */
    public String initBeforeNewSf(Questionnaire q, UserData ud) ;
    
    
    
    /**
     * Fonction lancée après chaque mise à jour de formulaire (submitform)
     * pour complément de traitement
     * 
     * @param sf
     * @return null si ok, un message d'erreur sinon
     */
    public String doAfterUpdateSf(SubmitForm sf, UserData ud) ;
    
    /**
     * Fonction lancée après chaque suppression de formulaire (submitform)
     * pour complément de traitement
     * 
     * @param sf
     * @return null si ok, un message d'erreur sinon
     */
    public String doAfterDeleteSf(SubmitForm sf, UserData ud) ;
    
    

    /**
     * Fonction lancée après chaque VALIDATION  de formulaire (submitform)
     * pour complément de traitement
     *
     * @param sf
     * @return null si ok, un message d'erreur sinon
     */
    public String doAfterLockSf(SubmitForm sf, UserData ud)  ;
    

    /**
     * Fonction lancée après chaque invalidation de formulaire (submitform)
     * pour complément de traitement
     * 
     * @param sf
     * @return null si ok, un message d'erreur sinon
     */
    public String doAfterUnlockSf(SubmitForm sf, UserData ud) ;
    
    /**
     * Fonction lancée avant l'envoi du password par mail (perte de mot de passe)
     * pour blocage ou complément de traitement
     * 
     * @param sf
     * @return null si ok, un message d'erreur sinon
     */
    public String doBeforeSendPwd(SubmitForm sf, UserData ud) ;
    
    /** fonction "connect AS" : 
     * permet de se connecter comme n'importe quel autre compte si on est admin, 
     * ou en fonction de conditions locales si la fonction est surchargée pour une autre instantion du framework
     * 
     * @param activeUserData
     * @param newUserData
     * @return null si OK, message d'erreur sinon
     * @throws SQLException 
     */
    public String connectAs(UserData activeUserData, UserData newUserData, int roleQid) throws SQLException ;
    
    /** fonction "doSpecificAction" : 
     * permet d'appeler une action spécifique depuis l'onglet traitement de lot 
     * 
     * @param String[] arguments d'appels
     * @return null si OK, message d'erreur sinon
     * @throws SQLException 
     */
    public String doSpecificAction(String[] args, UserData ud) throws SQLException ;
    
    
    
} 
    
    
    