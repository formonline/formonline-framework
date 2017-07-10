package formOnLine;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;





/**
 * @version   1.0
 * @author
 */
public class ListCommunes extends HttpServlet implements Servlet {
    
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException {
        
        /*
         HttpSession session = req.getSession(true);
        
         CommuneAction ca = new CommuneAction();
        
        
        Commune c = new Commune();
        
        // parse paramètres
        String cp = (String)req.getParameter("cp");
        if (cp != null) c.setCodePostal(cp);
        
        String ci = (String)req.getParameter("ci");
        if (ci != null) c.setCodeInsee(ci);
        
        
        
        // get xml 
        ValueBeanList listCommunes = null;
        
        try {
            
            
            if (c.getCodeInsee().length()>0) listCommunes = ca.selectLikeCodeInsee(c.getCodeInsee());
            if (c.getCodePostal().length()>0) listCommunes = ca.selectLikeCodePostal(c.getCodePostal());
            
        } catch (Exception e) {
            //...
        }
        
        // formatage XML
        StringBuffer xml = ca.getXml(listCommunes);        
        
        // renvoi flux xml
        resp.setContentType("application/xml charset=ISO-8859-1");
        resp.addHeader("Content-Disposition", "attachment;filename=communes.xml;");
        
        try {
            OutputStream out = resp.getOutputStream();
            PrintStream fichier = new PrintStream(out);
            fichier.println(xml);
            out.flush();
        } catch (Exception e) {
            InitServlet.traceLog.error("Erreur lors de l'exportation XML : " + e);
        }
        */ 
        
        
    }
    
    
    
    
}
