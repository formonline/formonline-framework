/* **********************************
 * 
 * formOnLine : XmlReader.java
 * Created on 15 févr. 2008 by seleridon
 * 
 ************************************ */
package formOnLine.io;

import java.sql.SQLException;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import formOnLine.actions.SubmitFormAction;
import formOnLine.msBeans.Reponse;
import formOnLine.msBeans.SubmitForm;

public class XmlReader {
    public XmlReader () {};
    
    private String readAttribute (Node n, String name) {
        String val="";
        NamedNodeMap al = n.getAttributes();
        
        if (al != null && al.getLength()>0) {
            for (int i=0; i< al.getLength(); i++) {
                if (al.item(i).getNodeName().equals(name)) 
                    val=al.item(i).getNodeValue();
            }
        }
        return val;
    }
    private int readIntAttribute (Node n, String name) {
        String val = readAttribute(n,name);
        int num = -1;
        try {
            num = Integer.parseInt(val);
        } catch (Exception e) {}
        
        return num;
        
    }
    
    public String parse(Node n, SubmitForm sf, boolean insert) {
        String message = null;
        short nodeTyp = n.getNodeType();
        
        
        
        if (nodeTyp == Node.DOCUMENT_NODE ){
            parse(n.getFirstChild(),null,insert);
        }
        
        if (nodeTyp == Node.ELEMENT_NODE 
                && n.getNodeName().equals("export") ) {
            
            // init du mode insert / modif
            if (readAttribute(n,"action").equals("insert")) insert = true;
            
            // parcours des éléments fils (questionnaires)
            NodeList list  = n.getChildNodes();
            if (list != null && list.getLength() > 0) {
                for (int i =0; i < list.getLength(); i++) {
                    parse(list.item(i), null, insert);
                }
            }
        }
        
        if (nodeTyp == Node.ELEMENT_NODE 
                && n.getNodeName().equals("questionnaire") ) {
            
            // init formulaire
            SubmitForm newSf = new SubmitForm();
            newSf.setF_id( readIntAttribute(n,"id") );
            newSf.setS_id( readIntAttribute(n,"sid"));
            newSf.setS_id_parent( readIntAttribute(n,"sid_parent"));
            
            // parcours des elements fils (groupes)
            NodeList list  = n.getChildNodes();
            if (list != null && list.getLength() > 0) {
                for (int i =0; i < list.getLength(); i++) {
                    parse(list.item(i), newSf, insert);
                }
            }
            
            // insert du formulaire
            //insert = false;
            if (insert) {
                SubmitFormAction sfa = new SubmitFormAction();
                try {
                    SubmitForm insertSf = sfa.newSubForm(newSf.getF_id(),newSf.getS_id_parent(), "XMLRPC", 0);
                    sfa.updateSubForm(insertSf, newSf);
                    
                } catch (SQLException e) {
                    message = e.getMessage();
                }
            }
            
        } 
        
        if (nodeTyp == Node.ELEMENT_NODE 
                && n.getNodeName().equals("groupe") ) {
            
            // parcours des elements fils (questions)
            NodeList list  = n.getChildNodes();
            if (list != null && list.getLength() > 0) {
                for (int i =0; i < list.getLength(); i++) {
                    parse(list.item(i), sf, insert);
                }
            }
            
        } 
        
        if (nodeTyp == Node.ELEMENT_NODE 
                && n.getNodeName().equals("question") ) {
            
            // parcours des elements fils (propositions)
            NodeList list  = n.getChildNodes();
            if (list != null && list.getLength() > 0) {
                for (int i =0; i < list.getLength(); i++) {
                    parse(list.item(i), sf, insert);
                }
            }
            
        } 
        
        if (nodeTyp == Node.ELEMENT_NODE 
                && n.getNodeName().equals("proposition") ) {
            
            Node ntext = n.getFirstChild();
            String val = ntext.getNodeValue();
            int pid = readIntAttribute(n, "id");
            
            Reponse r = new Reponse(sf.getS_id(),pid,val,0);
            
            sf.addReponse(r);
            
            
        } 
        
      
    
    return message;
}
}
