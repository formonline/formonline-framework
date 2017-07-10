package formOnLine.actions;
import java.lang.Class;

public class UserActionFactory {

  public UserActionInterface getUserAction( String localClassName ) 
  throws Exception {
      UserActionInterface ua =null;

      ua = (UserActionInterface)Class.forName(localClassName).newInstance();
      
   return ua;

  }

}