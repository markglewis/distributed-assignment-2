
import java.rmi.*;

public class  AssignmentSubmissionSystemServer {
   public static void main(String[] args) {
      System.setSecurityManager(new RMISecurityManager());
      try {
        AssignmentSubmissionSystemImpl fi = new AssignmentSubmissionSystemImpl();
        Naming.rebind(AssignmentSubmissionSystem.SERVICENAME, fi);
        System.out.println("Published in RMI registery, ready...");
      } catch (Exception e) {
        System.err.println(e.getMessage());
      }
   }
}
