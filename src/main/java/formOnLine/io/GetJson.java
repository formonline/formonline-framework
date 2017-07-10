package formOnLine.io;

import java.io.IOException;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.triangle.lightfw.AbstractServlet;
import com.triangle.lightfw.SessionInfos;
import com.triangle.lightfw.ValueBeanList;

import formOnLine.Controls;
import formOnLine.ServControl;
import formOnLine.actions.ConfigAction;
import formOnLine.actions.PropositionAction;
import formOnLine.actions.QuestionnaireAction;
import formOnLine.actions.StatAction;
import formOnLine.actions.SubmitFormAction;
import formOnLine.actions.UserAction;
import formOnLine.actions.UserActionFactory;
import formOnLine.msBeans.Config;
import formOnLine.msBeans.Questionnaire;
import formOnLine.msBeans.Reponse;
import formOnLine.msBeans.Stat;
import formOnLine.msBeans.SubmitForm;
import formOnLine.msBeans.UserData;

/**
 * @version 1.0
 * @author SLE
 */
public class GetJson extends HttpServlet implements Servlet {

	private static final long serialVersionUID = 1L;

	/**
	 * @see javax.servlet.http.HttpServlet#void
	 *	(javax.servlet.http.HttpServletRequest,
	 *	javax.servlet.http.HttpServletResponse)
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	/**
	 * @see javax.servlet.http.HttpServlet#void
	 *	(javax.servlet.http.HttpServletRequest,
	 *	javax.servlet.http.HttpServletResponse)
	 *	  
	 *	  Liste des codes retournés ( synthaxe identique au http, exemple : https://fr.wikipedia.org/wiki/Liste_des_codes_HTTP)
	 *	  200 : Requête traitée avec succès
	 *	  400 : La syntaxe de la requête est erronée
	 *	  401 : Une authentification est nécessaire pour accéder à la ressource
	 *    403 : (Forbidden) Le serveur a compris la requête, mais refuse de l'exécuter.
	 *    412 : Préconditions envoyées par la requête non vérifiées.
	 *	  500 : Erreur interne du serveur
	 *    506 : Erreur de négociation (problème sDate)	
	 *    520 : Le serveur renvoie une erreur inconnue
	 */
	@SuppressWarnings("unchecked")
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		HttpSession session = req.getSession(true);
		SessionInfos sessionInfos = (SessionInfos)session.getAttribute(AbstractServlet.ID_SESSION_INFOS);
		String msg = null;
		JSONObject json = null;

		resp.setCharacterEncoding("UTF-8");

		UserData user = (UserData) session.getAttribute(ServControl.USERDATA);

		String action = req.getParameter("action");
		if (action == null){
			json = new JSONObject();
			json.put("message", "No Action");
			json.put("code", "400");
		}

		/*************************************************
		 * déconnexion
		 */
		if (json == null && action.equals("DISCONNECT")) {
			session.invalidate();
			json = new JSONObject();
			json.put("code", "200");
			json.put("message", "Deconnected");
		}

		/*************************************************
		 * login
		 */
		if (json == null && action.equals("LOGIN")) {
			String login = req.getParameter("login");
			String pwd = req.getParameter("pwd");
			int loginPid = -1;
			int roleQid = -1;
			String ldapSource = req.getParameter("ldapSource");
			try {
				loginPid = Integer.parseInt(req.getParameter("loginPid"));
				roleQid = Integer.parseInt(req.getParameter("roleQid"));
			} catch (NumberFormatException e) { }

			UserAction ua = new UserAction();
			user = new UserData(login, pwd, -1,-1);
			msg = ua.check(user, loginPid, roleQid, ldapSource);

			if (msg != null) {
				json = new JSONObject();
				json.put("code", "401");
				json.put("message", msg);
			} else {
				session.setAttribute(ServControl.USERDATA, user);
				json = new JSONObject();
				json.put("code", "200");
				json.put("id", user.getId());
				json.put("message", "Access Granted");

				// sessioninfo
				if (sessionInfos == null) sessionInfos = new SessionInfos();
				sessionInfos.setRole(user.getRole());
				session.setAttribute( AbstractServlet.ID_SESSION_INFOS,sessionInfos);

				// init config
				HashMap<String, String> conf = null;
				try {
					if (sessionInfos.getRole() > SessionInfos.ROLE_PUBLIC) {
						conf = new ConfigAction().getParams();
					} else {
						conf = new ConfigAction().getParams(true, user.getId() );
					}
				} catch (Exception e) { }
				session.setAttribute(Config.SESSION_NAME ,conf);

				// init complémentaires
				if (!"admin".equals(login)) msg = ua.initAfterCheck(user, conf);

			}
		}

		// absence de connexion
		if (json == null) {
			if (user == null || !user.isConnected()){
				json = new JSONObject();
				json.put("code", "401");
				json.put("message", "Droits insuffisants (absence de connexion)");
			}
		}


		/*************************************************
		 * Récupération d'une liste de formulaires avec filtre par une valeur de Propositoin
		 */
		if (json == null && action.equals("LIST_FORMS")) {
			int fid = -1;
			int pid = -1;
			int sid_parent = -1;
			String val = "";
			int pid2 = -1;
			String val2 = "";
			int r_val = -1;
			int lockedOnly = -1;

			try {
				if (req.getParameter("sid_parent") != null)
					sid_parent = Integer.parseInt(req.getParameter("sid_parent"));
			} catch (NumberFormatException e) { }
			try {
				if (req.getParameter("f_id") != null)
					fid = Integer.parseInt(req.getParameter("f_id"));
			} catch (NumberFormatException e) { }
			try {
				if (req.getParameter("p_id") != null)
					pid = Integer.parseInt(req.getParameter("p_id"));
				if (req.getParameter("val") != null)
					val = req.getParameter("val");
				if (req.getParameter("r_val") != null)
					r_val = Integer.parseInt(req.getParameter("r_val"));
			} catch (NumberFormatException e) { }

			try {
				if (req.getParameter("p_id2") != null)
					pid2 = Integer.parseInt(req.getParameter("p_id2"));
				if (req.getParameter("val2") != null)
					val2 = req.getParameter("val2");
			} catch (NumberFormatException e) {	}

			if (req.getParameter("lockedOnly") != null)
				lockedOnly = (req.getParameter("lockedOnly").equals("true")?1:-1);

			if (fid < 0){
				json = (JSONObject) (new JSONObject());
				json.put("code", "400");
				json.put("message", "Le FId doit être renseigné");
			}

			if (json == null) {
				try {
					QuestionnaireAction qa = new QuestionnaireAction();
					Questionnaire f = qa.selectById(fid);

					// user pas connecté
					if (user==null || 
							(json == null && f!=null & user !=null && f.getF_connected() > 0 && !user.isConnected())){
						json = new JSONObject();
						json.put("code","401");
						json.put("message", "Droits insuffisants");
					}

					// user connecté mais role insuffisant par rapport aux droits formulaire
					if (json == null && f!=null && user!=null && f.getF_connected() > 0
							&& user.getRole() < f.getF_connected()){
						json = new JSONObject();
						json.put("code","401");
						json.put("message", "Droits insuffisants");
					}

					// OK
					if (json == null) {
						SubmitFormAction sfa = new SubmitFormAction();
						ValueBeanList listsf = null;
						if(sid_parent > 0){
							listsf = sfa.selectAllChildrenByFid(sid_parent, fid, null, false, -1);
						} else if (pid > 0) {
							if(r_val<0)
								listsf = sfa.selectAllByPropIdAndVal(pid, val, pid2, val2, lockedOnly, " order by R.r_text ");
							else
								listsf = sfa.selectByPidAndValAndRval(pid,"1",r_val,false);
						} else {
							listsf = sfa.selectAllByFormId(fid,-1, lockedOnly, null, null, " order by reponse.r_text");
						}

						// contrôle droits de la liste retour 
						for (int i = 0; listsf != null && i < listsf.size(); i++) {
							SubmitForm sf = (SubmitForm) listsf.get(i);
							msg = f.checkAccess(ServControl.RDET, user, sf, sf.getS_id(), false, true);

							if (msg != null) {
								json = new JSONObject();
								json.put("code", "403");
								json.put("message", msg);
								break;
							}
						}

						JSONArray listForms = new JSONArray();

						for (int i = 0; json == null && listsf != null && i < listsf.size(); i++) {
							SubmitForm sf = (SubmitForm) listsf.get(i);
							sfa.getInitVals(sf);
							listForms.add(sf.toJSON());
						}

						if (json == null) {
							json = new JSONObject();
							json.put("code", "200");
							json.put("listforms", listForms);
						}

					}

				} catch (SQLException e) {
					json = (JSONObject) (new JSONObject());
					json.put("code", "500");
					json.put("erreur",e.getMessage());
				}
			}
		}

		/*************************************************
		 * Récupération d'un formulaire
		 */
		if (json == null && (action.equals("GET_QUEST"))) {
			int fid = -1;
			int sid = -1;

			QuestionnaireAction qa = new QuestionnaireAction();
			SubmitFormAction sfa = new SubmitFormAction();

			Questionnaire f = null;
			SubmitForm sf = null;

			try {
				// gestion récupération SubmitForm
				if (req.getParameter("f_id") != null) 
					try { fid = Integer.parseInt(req.getParameter("f_id"));
					} catch (NumberFormatException e) {}

				if (fid < 0) {
					json = new JSONObject();
					json.put("code", "500");
					json.put("message", "Le Fid doit être renseigné");
				} else {
					if (user.getQuest(fid) != null) {
						f = user.getQuest(fid) ;
					} else {
						f = qa.getFullQuest(fid);
						user.addOrReplaceQuest(f);
					}
				}

				// controle droits 
				if (json == null ) {
					msg = f.checkAccess(ServControl.RDET, user, sf, sid, true, true);

					if (msg != null) {
						json = new JSONObject();
						json.put("code", "401");
						json.put("message", msg);
					}
				} 


				// récup Submitform
				if (json == null && req.getParameter("s_id") != null) 
					try {  sid = Integer.parseInt(req.getParameter("s_id"));
					} catch (NumberFormatException e) {}


				if (json == null && sid < 0){
					json = new JSONObject();
					json.put("code", "500");
					json.put("message", "Le Sid doit être renseigné");
				} else {
					sf = sfa.selectBySId(sid);
					f = qa.getFullQuest(fid, sf);
				}

				if (json == null) { // OK
					json = new JSONObject();

					boolean isPublicOnly = (user.getRole() < SessionInfos.ROLE_GESTION);
					json.put("code", "200");
					json.put("quest", f.toJSON(isPublicOnly));				

				}

			} catch (SQLException e) {
				json = new JSONObject();
				json.put("code", "520");
				json.put("erreur", e.getMessage());
			}

		}


		/*************************************************
		 * Récupération des stats
		 */
		if (json == null && action.equals("LIST_STATS")) {
			try {
				// contrôle droits
				if (!user.isConnected()){
					json = new JSONObject();
					json.put("code", "401");
					json.put("message", "Droits insuffisants");
				}

				if (user.getRole() < SessionInfos.ROLE_ADMIN) {
					json = new JSONObject();
					json.put("code", "401");
					json.put("message", "Droits insuffisants");
				}

				if (json == null) {
					PropositionAction pa = new PropositionAction();
					StatAction sa = new StatAction();

					ValueBeanList listProps = pa.selectAllByStat();
					ValueBeanList listStats = sa.getAllStats(listProps);

					JSONArray listst = new JSONArray();

					for (int i = 0; listStats != null && i < listStats.size(); i++) {
						Stat st = (Stat) listStats.get(i);
						listst.add(st.toJSON());
					}

					json = new JSONObject();
					json.put("code", "200");
					json.put("liststats", listst);
				}

			} catch (SQLException e)  {
				json = new JSONObject();
				json.put("code", "520");
				json.put("erreur", e.getMessage());
			}
		}

		/* ***********************
		 * SET QUEST
		 * 
		 *************************/
		if (json == null && action.equals("SET_QUEST")) {

			int fid = -1;
			int sid = -1;
			int sid_parent = -1;
			int locked = 0;
			String sdate = "", last_sdate = "", login = "";
			boolean newForm = true; 

			QuestionnaireAction qa = new QuestionnaireAction();
			SubmitFormAction sfa = new SubmitFormAction();

			Questionnaire f = null;
			SubmitForm sf = null;

			// contrôle 
			if (!user.isConnected()){
				json = new JSONObject();
				json.put("code","401");
				json.put("message", "Droits insuffisants (contrôle)");
			}

			// controles champs
			if (req.getParameter("s_id_parent") == null
					|| req.getParameter("s_id") == null
					|| req.getParameter("f_id") == null
					|| req.getParameter("locked") == null
					|| req.getParameter("s_date") == null
					|| req.getParameter("last_s_date") == null) {
				json = new JSONObject();				
				json.put("code", "500");
				json.put("message", "données de mise à jour incorrectes");
			}

			if (json == null) {
				try { 
					sid = Integer.parseInt(req.getParameter("s_id"));
					sid_parent = Integer.parseInt(req.getParameter("s_id_parent"));
					fid = Integer.parseInt(req.getParameter("f_id"));
					locked = Integer.parseInt(req.getParameter("locked"));
					sdate = req.getParameter("s_date");
					last_sdate = req.getParameter("last_s_date");
					login = req.getParameter("login");
				} catch (Exception e) {
					json = new JSONObject();					
					json.put("code", "500");
					json.put("message", e.getLocalizedMessage()); }
			}

			try {
				if (fid < 0 && json == null) {
					json = (JSONObject) (new JSONObject());
					json.put("code", "400");
					json.put("message", "Les champs f_id, s_id, s_id_parent, locked, s_date, last_s_date, login doivent être renseignés");
				} 

				if (fid > 0 && json == null) {
					if (user.getQuest(fid) != null) {
						f = user.getQuest(fid) ;
					} else {
						f = qa.getFullQuest(fid);
						user.addOrReplaceQuest(f);
					}
				}

				if (json == null && sid > 0) {
					newForm = false;
					sf = sfa.selectBySId(sid);
				}

				if (json == null && sf == null  && sid > 0) {
					json = new JSONObject();
					json.put("code", "500");
					json.put("message", "Le Sid n'existe pas");
				}

				// CONTROLE DROITS
				if (json == null) {
					if (newForm) {
						msg = f.checkAccess(ServControl.REP, user, sf, sid, true, true);
					} else {
						msg = f.checkAccess(ServControl.RMAJ, user, sf, sid, true, true);
					}
					if (msg != null){
						json = new JSONObject();
						json.put("code","401");
						json.put("message", "Droits insuffisants (CONTROLE DROITS)");
					}
				}

				// contrôle accès concurrents (il faut utiliser des date/heure)
				if (json == null && sf != null && 
						(last_sdate ==null || (last_sdate!=null && !last_sdate.equals(sf.getS_date())))) {
					json = new JSONObject();
					json.put("code", "506");
					json.put("message", "Conflit de mise à jour: le formulaire a déjà été modifié par un autre utilisateur");
				}


				// contrôle présence réponses
				String listeReponses = req.getParameter("reponses");
				if (Controls.isBlank(listeReponses) ){
					json = (JSONObject) (new JSONObject());
					json.put("code", "400");
					json.put("message", "Le(s) réponse(s) doi(ven)t être renseignée(s), de type [{\"id\":\"106\",\"sVal\":\"45\"}, ... ]");
				}



				if (json == null) {
					// création submitform
					if (sf==null) {
						sf = sfa.newSubForm(fid, sid_parent, login, locked);						
					}

					SubmitForm newSf = new SubmitForm();

					newSf.setS_id(sid);
					newSf.setF_id(fid);
					newSf.setS_id_parent(sid_parent);
					newSf.setS_lock(locked);
					newSf.setS_login_maj(login);
					newSf.setS_date_creat(sdate);

					// parse des réponses
					JSONParser parser = new JSONParser();



					JSONArray jsonArray = null; 
					if (listeReponses!= null) jsonArray = (JSONArray ) parser.parse(listeReponses);

					for (int i=0; json==null &&  jsonArray!=null && i<jsonArray.size();  i++) {
						JSONObject jsrep = (JSONObject)jsonArray.get(i);
						String sval = (String)jsrep.get("sVal");
						String rval = (String)jsrep.get("val");
						String sp_id = (String)jsrep.get("id");
						int p_id = Integer.parseInt(sp_id);

						Reponse rep = new Reponse();
						rep.setS_id(sid);
						rep.setP_id(p_id);
						rep.setSVal(sval);
						if (rval != null) rep.setVal(Integer.parseInt(rval));
						newSf.addReponse(rep);
					}

					// mise à jour des réponses
					sf.setS_id_parent(newSf.getS_id_parent());
					
					// contrôle unicité
					if (Controls.isBlank(msg)) msg = qa.checkMandatoryUnique(f, sf.getS_id());

					// TESTS SPECIFIQUES
					if (Controls.isBlank(msg)) {
						if (newForm) {
							msg = new UserActionFactory().getUserAction("ContextUserAction").testBeforeCreateSf(newSf,user);
						} else {
							msg = new UserActionFactory().getUserAction("ContextUserAction").testBeforeUpdateSf(newSf,user);
						}
					}

					if (!Controls.isBlank(msg)) {
						// mise à jour refusée
						json = (JSONObject) (new JSONObject());
						json.put("code", "403");
						json.put("message", msg);
					} else {
						// on peut mettre à jour !
						sfa.updateSubForm(sf, newSf);
						json = new JSONObject();
						json.put("code", "200");
						json.put("message", "Insert:OK");
						json.put("sDate", sf.getS_date());
						if (newForm){
							json.put("new_sid", sf.getS_id());
						}
					}
				}
			} catch (Exception e) { 
				json = new JSONObject();
				json.put("code", "520");
				json.put("message", e.getLocalizedMessage());
			} 
		}
		/* ***********************
		 * Action inconnu
		 * 
		 *************************/
		if (json == null) {
			json = new JSONObject();
			json.put("code", "412");
			json.put("id", user.getId());
			json.put("message", "Préconditions envoyées par la requête non vérifiées.");
		}

		// Préparation flux sortie
		resp.setContentType("application/json;charset=UTF-8");

		// Préparation envoi du contenu
		PrintStream fichier = new PrintStream(resp.getOutputStream(), false, "UTF-8");

		// flux
		if (req.getParameter("callback") != null) {
			/* Si flux jsonp */
			String callbackFunction = (String) req.getParameter("callback");
			fichier.write((callbackFunction + "(" + json.toJSONString() + ");").getBytes("UTF-8"));
		} else {
			/* Si flux post (pour le set) */
			fichier.write(json.toJSONString().getBytes("UTF-8"));
		}
		resp.getOutputStream().flush();
	}
}