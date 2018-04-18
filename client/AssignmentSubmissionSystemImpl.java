
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.io.*;
import java.util.*;
import java.util.HashMap;
import java.io.ByteArrayOutputStream;
import java.util.zip.Inflater;
import java.util.zip.*;

public class AssignmentSubmissionSystemImpl extends UnicastRemoteObject implements AssignmentSubmissionSystem{
   
   
   public AssignmentSubmissionSystemImpl() throws RemoteException {
      super();
   }
   public synchronized userClass login(String userID, String password) throws java.rmi.RemoteException{
    userClass queryResult = null;


    String accounts[][];

    try{
      accounts =  OpenFile();
    } catch (IOException e){
      accounts = new String[10000][4];
    }
    
    for(int i =0; i < accounts.length; i++){
      if(accounts[i][0].equals(userID) && accounts[i][1].equals(password)){        
        queryResult = new userClass(accounts[i][0],accounts[i][1],accounts[i][2]);   
        queryResult.assignZip(accounts[i][3]);  
        return queryResult;     

      }
    }
    return new userClass("noResult","","");
     
  }

  public synchronized boolean upload(String userID, String fileName,byte[] submission) throws java.rmi.RemoteException{
    String accounts[][] = new String[10000][4];

    try{
      accounts =  OpenFile();
    } catch (IOException e){
      return false;
    }
    for(int i =0; i < accounts.length; i++){
      if(accounts[i][0].equals(userID) && accounts[i][3].equals("noFile")){
        accounts[i][3] = fileName;

        try {
          FileOutputStream fos = new FileOutputStream(fileName);
          fos.write(submission);
          fos.close();
        }
        catch(Exception e) {
          System.err.println("FileServer exception: "+ e.getMessage());
          e.printStackTrace();
        }

        BufferedWriter bw = null;
        try{
          File file = new File("accounts.txt");

          if (!file.exists()) {
          file.createNewFile();
          }

          FileWriter fw = new FileWriter(file);
          bw = new BufferedWriter(fw);

          for(int j =0; j < accounts.length; j++){
            if(!accounts[j][0].equals("")){              
              bw.write(accounts[j][0] + " "+ accounts[j][1] + " " + accounts[j][2] + " " + accounts[j][3]);
              bw.newLine();

            }
            
          }
        }catch (IOException ioe) {
          ioe.printStackTrace();
        }finally{
          try{
            if(bw!=null)
            bw.close();
          }catch(Exception ex){
            System.out.println("Error in closing the BufferedWriter"+ex);
          }
        }

        return true;
      }
    }




    return false;




  }
  public synchronized byte[] download(String userID) throws java.rmi.RemoteException{
    String accounts[][] = new String[10000][4];

    try{
      accounts =  OpenFile();
    } catch (IOException e){

    }

    String name = null;

    
    for(int i =0; i < accounts.length; i++){
      if(accounts[i][0].equals(userID)){
        name = accounts[i][3];
        try{
          
          
          File file = new File(name);
          byte[] buffer = new byte[ (int)file.length() ];
          BufferedInputStream input = new BufferedInputStream(new FileInputStream(name));
          input.read(buffer,0,buffer.length);
          input.close();
          return(buffer);
          
        }
        catch(Exception e){
          System.out.println("FileImpl: "+e.getMessage());
          e.printStackTrace();
          return(null);
        }
        

      }
    }
    return null; 
  }

  public synchronized String[][] retrieve() throws java.rmi.RemoteException{
    String accounts[][] = new String[10000][4];

    try{
      accounts =  OpenFile();
    } catch (IOException e){

    }
    return accounts;



  }


   //reads file for id's passwords and filenames
   //format id, pass, file
  public String[][] OpenFile() throws IOException {
      
    String line = "";
    String accounts[][] = new String[10000][4];
    for(int i = 0; i < accounts.length; i++){
      for(int j = 0; j < 3; j++){
        accounts[i][j]= "";
      }
    }

    try{
      FileReader fr = new FileReader("accounts.txt");
      BufferedReader br = new BufferedReader(fr);
      int i =0;
      while((line = br.readLine()) != null) {
        //parse through data
        
        StringTokenizer st  = new StringTokenizer(line);
        int count = st.countTokens();
        if(count == 4){
        accounts[i][0] = st.nextToken();        
        accounts[i][1] = st.nextToken();
        accounts[i][2] = st.nextToken();
        accounts[i][3] = st.nextToken();
        }
        else {
        }
        i++;
      }

        
      }
      catch(FileNotFoundException ex){
        System.out.println("file not found");

      }
      catch(IOException ex) {
        System.out.println("Error reading file"); 
      }
      System.out.println(accounts[0][0] + accounts[0][1] + accounts[0][2] + accounts[0][3]);
      System.out.println(accounts[1][0] + accounts[1][1] + accounts[1][2] + accounts[1][3]);
      return accounts;
      
  }

   
}