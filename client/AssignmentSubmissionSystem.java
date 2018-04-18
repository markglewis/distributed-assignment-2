
public interface AssignmentSubmissionSystem extends java.rmi.Remote {

	//interface for the client and server callback
   public final static String SERVICENAME="AssignmentService";
   //hashmap login-success, user-type
   public userClass login(String userID, String password) throws java.rmi.RemoteException;
   public boolean upload(String userID, String fileName,byte[] submission) throws java.rmi.RemoteException;
   public byte[] download(String userID) throws java.rmi.RemoteException;
   public String[][] retrieve() throws java.rmi.RemoteException;
}