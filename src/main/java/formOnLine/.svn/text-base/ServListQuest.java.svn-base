package formOnLine;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import formOnLine.actions.QuestionnaireAction;

import com.triangle.lightfw.ValueBeanList;

/**
 * @version 	1.0
 * @author
 */
public class ServListQuest extends HttpServlet implements Servlet {


    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	* @see javax.servlet.http.HttpServlet#void (javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	*/
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException {
        doPost(req,resp);
    }
    
    
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException {	
		QuestionnaireAction qa = new QuestionnaireAction();
		try {
			ValueBeanList v = qa.selectAll();
			HttpSession session = req.getSession(true);
			if (v!=null) session.setAttribute("qlist",v);
			
		} 
		catch (SQLException e ) {
			String message = e.getMessage()+"<br/>"+e.toString(); //erreur SQL
			HttpSession session = req.getSession(true);
			session.setAttribute(ServControl.MESSAGE,message);
		}
			
		
		
		

	}

}
