/* **********************************
 * 
 * formonline : XmlBuilder.java
 * Created in 2010 by seleridon
 * 
 ************************************ */
package formOnLine.io;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Properties;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.xerces.dom.DocumentImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.triangle.lightfw.BasicType;

import formOnLine.Controls;
import formOnLine.InitServlet;
import formOnLine.msBeans.Groupe;
import formOnLine.msBeans.Question;
import formOnLine.msBeans.Proposition;
import formOnLine.msBeans.Questionnaire;
import formOnLine.msBeans.SubmitForm;

import com.triangle.lightfw.ValueBeanList;



public class XmlBuilder {
    
    public XmlBuilder() {}
    
    public String sendForms(Questionnaire f, ValueBeanList list, 
            OutputStream out, boolean norep) {
        String xml= null;
        
        Document doc = new DocumentImpl();
        
        
        
        Element root = doc.createElement("export");
        root.setAttribute("date", BasicType.getTodaysDateIso() + BasicType.getTimeIso());
        root.setAttribute("xml:lang", "fr");
        doc.appendChild(root);
        
        for (int i =0; list !=null && i<list.size(); i++ ) {
            SubmitForm sf = (SubmitForm)list.get(i);
            addQuestElement(f, sf, doc, root, norep);
        }
        
        doc.normalize();
        
//      génération sortie 
        TransformerFactory tf = TransformerFactory.newInstance();
        
        // sortie XML
        try {
            Transformer t = tf.newTransformer();
            Properties props = new Properties();
            props.put("method","xml");
            props.put("indent","yes");
            
            String charset = "UTF-8";
            
            // données en base toujours en latin1
            //if (!Controls.isBlank(InitServlet.getExportCharset())) charset = InitServlet.getExportCharset();
             
            props.put(OutputKeys.ENCODING, charset);
            //props.put("xalan:indent-amount","2");
            t.setOutputProperties(props);
            
            Writer wrt = new OutputStreamWriter(out, "UTF-8");
            
            Source input = new DOMSource (doc);            
            Result output = new StreamResult(wrt);
            
            t.transform(input, output);
            
        } catch (Exception e) {
            return e.getMessage();
        }
        
        return xml ;
    }
    
    

    
    public void addQuestElement(Questionnaire f, SubmitForm sf, Document doc, Element root, boolean norep) {
        
        // exemple :
        // <questionnaire  id="11" sid="1054678" parent_sid="750612" 
        // sdate="2010-06-11" titre="Demande de suppléance">
        
        
        Element elf = doc.createElement("questionnaire");
        
        f.setFullQuest(sf);
        
        f.setAttributes(elf);
        root.appendChild(elf);
        
        if (!norep && f.getGroupes() != null) {
            for (int i=0; i< f.getGroupes().size(); i++) {
                Element elg = doc.createElement("groupe");
                Groupe g = f.getGroupe(i);
                g.setAttributes(elg);
                
                elf.appendChild( elg);
                
                for (int j=0; j< g.getQuestions().size(); j++) {
                    Element elq = doc.createElement("question");
                    Question q = g.getQuestion(j);
                    q.setAttributes(elq);
                    
                    elg.appendChild( elq);
                    
                    for (int k=0; k< q.getPropositions().size(); k++) {
                        Element elp = doc.createElement("proposition");
                        Proposition p = q.getProposition(k);
                        p.setAttributes(elp);
                        String val = "";
                        if (p.getsVal()!=null) val = p.getsVal();
                        elp.appendChild(doc.createTextNode(val));
                        
                        elq.appendChild( elp);
                    }
                }
            }
        }
        
        
    }
    
     
    
}
