/* **********************************
 * 
 * formOnLine : Messages.java
 * Created on 8 janv. 2010 by seleridon
 * 
 ************************************ */
package formOnLine;

import java.util.MissingResourceException;
import java.util.ResourceBundle;



public class Message {
    private static final String BUNDLE_NAME = "formOnLine.messages"; //$NON-NLS-1$
    //private static final String PROPS_FILE_NAME = "config.properties"; 
    
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
      .getBundle(BUNDLE_NAME);
    
    private Message() {
    }
    
    public static String getString(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
        
    }
    
    
    
    /**
     * Cette méthode stocke le fichier Properties à l'emplacement spécifié
     * 
     * @param props Le fichier à stocker
     * @param fileLocation L'emplacement où le fichier doit être stocké
     * @param comments Commentaires à insérer en tête du fichier
     * @throws FileNotFoundException
     * @throws IOException si une erreur est survenue lors de l'écriture du fichier
   
    public static void saveProperties(Properties props,  String comments) throws FileNotFoundException,
    IOException {
        OutputStream out = new FileOutputStream(PROPS_FILE_NAME);
        props.store(out, comments);
        out.flush();
        out.close();
    }
    
    
    /**
     * Cette méthode lit un fichier Properties à l'emplacement spécifié
     * 
     * @param propertiesFileLocation L'emplacemnt du fichier
     * @return Le fichier Properties chargé
     * @throws FileNotFoundException si le fichier n'a pas été trouvé
     * @throws IOException si une erreur est survenue durant la lecture
   
    public static Properties loadProperties() throws FileNotFoundException, IOException {
        
        Properties props = new Properties();
        //props.load(new FileInputStream("config.properties"));
        InputStream inputs = null;
            
        //InputStream inputs = ClassLoader.getSystemResourceAsStream("formOnLine/config.properties");
        URL u = ClassLoader.getSystemResource(PROPS_FILE_NAME);
        if (u==null) u = ClassLoader.getSystemResource("/"+PROPS_FILE_NAME);
        if (u==null) u = ClassLoader.getSystemResource("/formOnLine/"+PROPS_FILE_NAME);
        if (u==null) u = ClassLoader.getSystemResource("formOnLine/"+PROPS_FILE_NAME);
        
        
        if (u!=null)  props.load(inputs);
        

        return props;
    }
    */
    
}
