package formOnLine.msBeans;
import java.io.UnsupportedEncodingException;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import formOnLine.BasicTools;

import com.triangle.lightfw.BasicType;
import com.triangle.lightfw.ValueBean;
import com.triangle.lightfw.ValueBeanList;



/**
 * @author SLE
 *
 */
public class SubmitForm extends ValueBean{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int s_id =0;
	private int s_id_parent =-1;
	private int f_id =0;
	private int s_lock=0;
	private String s_date ="" ;
	private String s_date_creat ="" ;
	private String pwd = "";
	private String titre ="";
	private String s_login_maj ="" ;

	private LinkedList<Reponse> reponses = new LinkedList<Reponse>();

	/**
	 * deep copy du submitform
	 * @return un nouveau submitform
	 */
	public SubmitForm copy () {

		SubmitForm newSf = new SubmitForm();

		// pour les types primitifs ou immuables on peut affecter directement les valeurs
		newSf.setF_id(this.getF_id());
		newSf.setPwd(BasicTools.getPwd());

		newSf.setS_id_parent(this.getS_id_parent());
		newSf.setS_lock(this.getLock());
		newSf.setTitre(this.getTitre());
		newSf.setS_login_maj(this.getS_login_maj());

		newSf.setS_date(BasicType.getTodaysDateIso(true));

		// pour les objets non immuables il faut un deep copy
		newSf.setReponses(this.getCopyOfReponses());


		return newSf;
	}



	/**
	 * deep copy de la liste des réponses 
	 * @return une nouvelle liste
	 */
	public LinkedList<Reponse>  getCopyOfReponses() {


		LinkedList<Reponse> newListRep = new LinkedList<Reponse>();

		for (int i=0;reponses!=null && i< reponses.size(); i++) {
			newListRep.add(getReponse(i).copy());
		}

		return newListRep;

	}

	/**
	 * @return Returns the titre.
	 */
	public String getTitre() {
		return titre;
	}
	/**
	 * @param titre The titre to set.
	 */
	public void setTitre(String titre) {
		this.titre = titre;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String s="";
		s += "sid_parent=" + String.valueOf(s_id_parent) + " | ";        
		s += "sid=" + String.valueOf(s_id) + " | ";
		s += "fid=" + String.valueOf(f_id) + " | ";
		s += "lock=" + String.valueOf(s_lock) + " | ";
		s += "date=" ;
		if (s_date!=null) s += s_date ;
		s += " | pwd=" ;
		if (pwd!=null) s += pwd ;
		s += " | Nb R=" + String.valueOf(reponses.size()) ;

		return s;
	}

	/**
	 * @return l'export XML du submitForm
	 * @throws UnsupportedEncodingException
	 */
	public String toXML() throws UnsupportedEncodingException {
		String s="<submitform";
		s += " id=\"" + String.valueOf(s_id)+"\"";
		s += " fid=\"" + String.valueOf(f_id) +"\"";
		s += " lock=\"" + String.valueOf(s_lock) +"\"";
		s += " date=\"" ;
		if (s_date!=null) s += s_date +"\"" ;
		s += " titre=\"" + BasicTools.xmlEncode(getTitre()) +"\"";

		s +=">";
		if (reponses!=null) {
			for (int i=0;i<reponses.size();i++) {
				Reponse r =(Reponse) reponses.get(i);
				s+= r.toXML();
			}
		}
		s += "</submitform>";

		return s;
	}

	/**
	 * sérialisatin JSON du submitform
	 * 
	 * @return chaîne json
	 */
	@SuppressWarnings("unchecked")
	public JSONObject toJSON()  {

		JSONObject json = new JSONObject();

		json.put("id",s_id);
		json.put("fid",f_id);
		json.put("lock",s_lock);
		json.put("id_parent",s_id_parent);

		if (s_date!=null) json.put("date",s_date); 

		json.put("titre",getTitre());

		JSONArray listProps = new JSONArray();

		if (reponses!=null) {
			for (int i=0;i<reponses.size();i++) {
				Reponse r =(Reponse) reponses.get(i);
				listProps.add( r.toJSON() );                
			}
		}

		json.put("props", listProps);


		return json;
	}


	/**
	 * @param r
	 */
	public void addReponse(Reponse r) {
		reponses.add(r);
	}

	/**
	 * @param pid
	 * @return
	 */
	public String getPropVal(int pid) {
		String s="" ;

		for (int i=0;reponses !=null && i< reponses.size(); i++) {
			if (getReponse(i).getP_id()==pid) {
				s=getReponse(i).getSVal();
				break;
			}

		}
		return s;
	}

	/**
	 * retour la valeur INT de la reponse (le RVAL) 
	 * @param pid
	 * @return
	 */
	public int getPropRVal(int pid) {
		int r=0 ;

		for (int i=0;reponses !=null && i< reponses.size(); i++) {
			if (getReponse(i).getP_id()==pid) {
				r =getReponse(i).getVal();
				break;
			}

		}
		return r;
	}

	/**
	 * retourne les valeurs correspond au Pid (et à l'indice rval)
	 * 
	 * @param pid
	 * @param rval
	 * @return liste des valeurs de la réponse
	 */
	public ValueBeanList getListPropVal(int pid, boolean selectedOnly) {
		ValueBeanList l= new ValueBeanList() ;

		for (int i=0;reponses !=null && i< reponses.size(); i++) {
			if (getReponse(i).getP_id()==pid)
				if(selectedOnly && "1".equals(getReponse(i).getSVal()) || !selectedOnly)
					l.add(getReponse(i));
		}
		return l;
	}


	/**
	 * retourne la valeur correspond au Pid (et à l'indice rval)
	 * 
	 * @param pid
	 * @param rval
	 * @return valeur de la réponse
	 */
	public String getPropVal(int pid, int rval) {
		String s="" ;

		for (int i=0;reponses !=null && i< reponses.size(); i++) {
			if (getReponse(i).getP_id()==pid && getReponse(i).getVal()==rval) {
				s=getReponse(i).getSVal();
				break;
			}
		}
		return s;
	}

	/**
	 * maj de la valeur correspondant au Pid (et à l'indice rval)
	 * (sans création automatique de la réponse si elle n'existe pas)
	 * 
	 * @param pid
	 * @param rval
	 * @param valeur de la réponse
	 */
	public void setPropVal(int pid, int rval, String val) {

		setPropVal(pid, rval, val, false);        
	}

	/**
	 * maj de la valeur correspondant au Pid (et à l'indice rval)
	 * 
	 * @param pid
	 * @param rval
	 * @param valeur de la réponse
	 * @param booléen indiquant s'il faut créer la réponse si elle n'existe pas
	 */
	public void setPropVal(int pid, int rval, String val, boolean createIfNotExists) {

		boolean repFound= false;

		for (int i=0;reponses !=null && i< reponses.size(); i++) {
			if (getReponse(i).getP_id()==pid && getReponse(i).getVal()==rval) {
				getReponse(i).setSVal(val);
				repFound=true;
				break;
			}
		}

		if (createIfNotExists && !repFound) {
			Reponse r = new Reponse(this.s_id, pid, val, rval);
			addReponse(r);
		}
	}




	/**
	 * @param pid
	 * @return
	 */
	public int getPropInt(int pid) {
		int ret=0 ;
		try {
			ret= Integer.parseInt(getPropVal(pid));
		} catch (NumberFormatException e) {
			return 0;
		}

		return ret;
	}

	/**
	 * @param pid
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String printPropVal(int pid) throws UnsupportedEncodingException {
		String s="" ;

		for (int i=0;i< reponses.size(); i++) {
			if (getReponse(i).getP_id()==pid) s=getReponse(i).printVal();
		}
		return s;
	}


	/**
	 * @param i
	 * @return
	 */
	public Reponse getReponse(int i) {
		return (Reponse)reponses.get(i);
	}

	/**
	 * @return Returns the f_id.
	 */
	public int getF_id() {
		return f_id;
	}
	/**
	 * @param f_id The f_id to set.
	 */
	public void setF_id(int f_id) {
		this.f_id = f_id;
	}
	/**
	 * @return Returns the pwd.
	 */
	public String getPwd() {
		return pwd;
	}
	/**
	 * @param pwd The pwd to set.
	 */
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	/**
	 * @return Returns the reponses.
	 */
	public LinkedList<Reponse> getReponses() {
		return reponses;
	}
	/**
	 * @param reponses The reponses to set.
	 */
	public void setReponses(LinkedList<Reponse> reponses) {
		this.reponses = reponses;
	}
	/**
	 * @return Returns the s_date.
	 */
	public String getS_date() {
		return s_date;
	}
	/**
	 * @param s_date The s_date to set.
	 */
	public void setS_date(String s_date) {
		this.s_date = s_date;
	}
	/**
	 * @return Returns the s_id.
	 */
	public int getS_id() {
		return s_id;
	}
	/**
	 * @param s_id The s_id to set.
	 */
	public void setS_id(int s_id) {
		this.s_id = s_id;
	}
	/**
	 * @return Returns the s_lock.
	 */
	public int getLock() {
		return s_lock;
	}

	/**
	 * @return true if locked
	 */
	public boolean isLocked() {
		return s_lock==1;
	}

	/**
	 * @param s_lock The s_lock to set.
	 */
	public void lock() {
		this.s_lock = 1;
	}
	/**
	 * @param i
	 */
	public void setS_lock(int i) {
		this.s_lock = i;
	}
	/**
	 * 
	 */
	public void unlock() {
		this.s_lock = 0;
	}

	/**
	 * @return Returns the s_id_parent.
	 */
	public int getS_id_parent() {
		return s_id_parent;
	}
	/**
	 * @param s_id_parent The s_id_parent to set.
	 */
	public void setS_id_parent(int s_id_parent) {
		this.s_id_parent = s_id_parent;
	}

	/**
	 * @return Returns the s_date_creat.
	 */
	public String getS_date_creat() {
		return s_date_creat;
	}

	/**
	 * @param s_date_creat The s_date_creat to set.
	 */
	public void setS_date_creat(String s_date_creat) {
		this.s_date_creat = s_date_creat;
	}

	/**
	 * @return the s_login_maj
	 */
	public String getS_login_maj() {
		return s_login_maj;
	}

	/**
	 * @param s_login_maj the s_login_maj to set
	 */
	public void setS_login_maj(String s_login_maj) {
		this.s_login_maj = s_login_maj;
	}
}
