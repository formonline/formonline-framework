package formOnLine.io;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcRequest;
import org.apache.xmlrpc.common.XmlRpcHttpRequestConfig;
import org.apache.xmlrpc.server.AbstractReflectiveHandlerMapping;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcHandlerMapping;
import org.apache.xmlrpc.webserver.XmlRpcServlet;

import formOnLine.InitServlet;


public class MyXmlRpcServlet extends XmlRpcServlet {
    private static final long serialVersionUID = 1L;
    
    private boolean isAuthenticated(String pUserName, String pPassword) {
        String user = InitServlet.getXmlRpcAuthorizedUser();
        String pwd  = InitServlet.getXmlRpcAuthorizedPwd();
        
        return user.equals(pUserName) && pwd.equals(pPassword);
    }
    
    protected XmlRpcHandlerMapping newXmlRpcHandlerMapping() throws XmlRpcException {
        PropertyHandlerMapping mapping = (PropertyHandlerMapping) super.newXmlRpcHandlerMapping();
        AbstractReflectiveHandlerMapping.AuthenticationHandler handler =
            new AbstractReflectiveHandlerMapping.AuthenticationHandler(){
            public boolean isAuthorized(XmlRpcRequest pRequest){
                XmlRpcHttpRequestConfig config =
                    (XmlRpcHttpRequestConfig) pRequest.getConfig();
                
                return isAuthenticated(config.getBasicUserName(),  config.getBasicPassword());
                //return true; // bypass en attendant le déploiement de GEFF 1.5.1
            };
        };
        mapping.setAuthenticationHandler(handler);
        return mapping;
    }
}