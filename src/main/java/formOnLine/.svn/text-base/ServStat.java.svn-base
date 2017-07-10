package formOnLine;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import formOnLine.actions.PropositionAction;
import formOnLine.actions.StatAction;
import formOnLine.msBeans.Stat;



import com.triangle.lightfw.ValueBeanList;

/**
 * @version 	1.0
 * @author
 */
public class ServStat extends HttpServlet implements Servlet {

    private static final long serialVersionUID = 1L;

    
    private Stat getVotants(StatAction sa, String scrut) 
    throws SQLException {
        
        Stat s = new Stat();
        Stat s_votants = sa.getStat(80, -1, 79, scrut);
        Stat s_inscrits = sa.getStat(86, -1, 79, scrut);
        Stat s_vpc = sa.getStat(87, -1, 79, scrut);
        
        
        s.setTitre("Nb Votants "+scrut);
        s.setVal(s_votants.getVal());
        s.setTotal(s_inscrits.getVal() - s_vpc.getVal());
        s.setComment(BasicTools.getPourcent(s.getVal(),s.getTotal(),2));
        
        return s;
        
    }
    
    private ValueBeanList getQuorum(StatAction sa) 
    throws SQLException {
        
        ValueBeanList res = new ValueBeanList();
        
        String scrut = "CTP";
        Stat s = new Stat();
        Stat s_votants = sa.getStat(192, -1, 191, scrut);
        Stat s_inscrits = sa.getStat(49, -1, 42, scrut);
        Stat s_vpc      = sa.getStat(50, -1, 42, scrut);
        
        s.setTitre("Nb Votants "+scrut);
        s.setVal(s_votants.getVal());
        s.setTotal(s_inscrits.getVal() - s_vpc.getVal());
        s.setComment(BasicTools.getPourcent(s.getVal(),s.getTotal(),2));
        res.add(s);
        
        scrut = "CHS";
        s = new Stat();
        s_votants = sa.getStat(192, -1, 191, scrut);
        s_inscrits = sa.getStat(161, -1, 158, scrut);
        s_vpc      = sa.getStat(162, -1, 158, scrut);
        s.setTitre("Nb Votants "+scrut);
        s.setVal(s_votants.getVal());
        s.setTotal(s_inscrits.getVal() - s_vpc.getVal());
        s.setComment(BasicTools.getPourcent(s.getVal(),s.getTotal(),2));
        res.add(s);
        
        scrut = "CAP-A";
        s = new Stat();
        s_votants = sa.getStat(192, -1, 191, scrut);
        s_inscrits = sa.getStat(171, -1, 168, scrut);
        s_vpc      = sa.getStat(172, -1, 168, scrut);
        s.setTitre("Nb Votants "+scrut);
        s.setVal(s_votants.getVal());
        s.setTotal(s_inscrits.getVal() - s_vpc.getVal());
        s.setComment(BasicTools.getPourcent(s.getVal(),s.getTotal(),2));
        res.add(s);
        
        scrut = "CAP-B";
        s = new Stat();
        s_votants = sa.getStat(192, -1, 191, scrut);
        s_inscrits = sa.getStat(166, -1, 163, scrut);
        s_vpc      = sa.getStat(167, -1, 163, scrut);
        s.setTitre("Nb Votants "+scrut);
        s.setVal(s_votants.getVal());
        s.setTotal(s_inscrits.getVal() - s_vpc.getVal());
        s.setComment(BasicTools.getPourcent(s.getVal(),s.getTotal(),2));
        res.add(s);
        
        scrut = "CAP-C";
        s = new Stat();
        s_votants = sa.getStat(192, -1, 191, scrut);
        s_inscrits = sa.getStat(155, -1, 152, scrut);
        s_vpc      = sa.getStat(156, -1, 152, scrut);
        s.setTitre("Nb Votants "+scrut);
        s.setVal(s_votants.getVal());
        s.setTotal(s_inscrits.getVal() - s_vpc.getVal());
        s.setComment(BasicTools.getPourcent(s.getVal(),s.getTotal(),2));
        res.add(s);
        
        
        
        return res;
        
    }
    
    private ValueBeanList getResults(StatAction sa, int[] tabPid, String scrut) 
    throws SQLException {
        
        ValueBeanList listStat = new ValueBeanList();
        PropositionAction pa = new PropositionAction();
        int total = 0;
        
        for (int i=0; i<tabPid.length; i++) {
            Stat si = sa.getStat(tabPid[i], -1);
            total = total + si.getVal()  ;
            si.setTitre(scrut +" " +pa.selectById(tabPid[i]).getTexte());
            
            listStat.add(si);
            
        }
        
        for (int i=0; i<listStat.size(); i++) {
            Stat si = (Stat)listStat.get(i);
            si.setTotal(total);
            si.setComment(BasicTools.getPourcent(si.getVal(),si.getTotal(),2));
            
        }
        
        
        return listStat;
        
    }
    
    public ValueBeanList getEstimationSieges(ValueBeanList list, int nbTotalSieges) {
        
        ValueBeanList listSieges = new ValueBeanList();
        int nbTotalVotes = 0;
        
        if (list == null) return null;
        
        
        // calcul nb de sièges scrutin proportionnel à plus forte moyenne
        for (int i=0; i<list.size();i++) {
            Stat tot = (Stat)list.get(i);
            nbTotalVotes += tot.getVal();
        }
        
        double quotient = nbTotalVotes / nbTotalSieges;
        int reste = nbTotalSieges;
        
        for (int i=0; i<list.size();i++) {
            Stat nbVotes = (Stat)list.get(i);
            Stat nbSieges = new Stat();
            nbSieges.setTitre(nbVotes.getTitre());
            nbSieges.setTotal(nbTotalSieges);
            double d = Math.floor(nbVotes.getVal() / quotient);
            
            nbSieges.setVal( new Double(d).intValue() );
            reste = reste - nbSieges.getVal();
            
            listSieges.add(nbSieges);            
        }
        
        
        while (reste != 0 ) {
            int iMax = 0;
            double moyMax = 0;
            
            for (int i=0; i<list.size();i++) {
                
                Stat nbVotes  = (Stat)list.get(i);
                Stat nbSieges = (Stat)listSieges.get(i);
                
                double moyenne = nbVotes.getVal() / (nbSieges.getVal()+1);
                if (i>0 && moyenne > moyMax) {
                    iMax = i;
                    moyMax = moyenne;
                } 
            }
            
            Stat nbSieges = (Stat)listSieges.get(iMax);
            nbSieges.setVal( nbSieges.getVal() +1);
            reste = reste -1;
            
            //listSieges.replace(i,nbSieges) ??
            
        }
        
        return listSieges;
    }
    
    /**
    * @see javax.servlet.http.HttpServlet#void (javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
    */
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        doPost(req, resp);
    }
    /**
	* @see javax.servlet.http.HttpServlet#void (javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	*/
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		
        HttpSession session = req.getSession(true);

        PropositionAction pa = new PropositionAction();
        StatAction sa = new StatAction();
        
        String refresh = req.getParameter("refresh");
        if (refresh == null) refresh= "Forms";
        
		try {
            ValueBeanList stats = new ValueBeanList();
            
            if (refresh.equals("Reps")) {
                // stats automatiques
                ValueBeanList v = pa.selectAllByStat();
                stats = sa.getAllStats(v);
            }
            
            if (refresh.equals("Forms")) {
                // stats automatiques
                
                stats= sa.getAllForms() ;
            }
            if (refresh != null && refresh.equals("ouverture")) {
                // nb votants scrutins
                stats = getQuorum(sa);
            }
            
            if (refresh != null && refresh.equals("quorum")) {
                // nb votants scrutins
                stats = getQuorum(sa);
            }
            
            if (refresh != null && refresh.equals("votes")) {
                // nb votants scrutins
                stats.add( getVotants(sa,"CTP"));
                // ...
                
            }
            
            if (refresh != null && refresh.equals("sieges")) {
                // résultats nb de voix
                ValueBeanList resCTP = new ValueBeanList();
                int[] tabCTP = {46,47,46,45,136,137};
                resCTP = getResults(sa, tabCTP, "CTP");
                ValueBeanList resultSieges = getEstimationSieges(resCTP,15);
                stats = resultSieges;
                
                // chs : 10 sièges
                //CAP A : 6 sièges
                //CAP B : 5 sièges
                //CAP C : 8 sièges

            }
            
            
            
            // mise en session des stats
			if (stats!=null) session.setAttribute(Stat.SESSION_LIST ,stats);
		} 
		catch (SQLException e ) {
			String message = e.getMessage()+"<br/>"+e.toString(); //erreur SQL
			session.setAttribute(ServControl.MESSAGE,message);
		}
			
		
		
		

	}

}
