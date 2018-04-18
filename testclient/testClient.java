import java.io.*;
import java.rmi.Naming;
import java.util.*;
import java.io.ByteArrayOutputStream;
import java.util.zip.Inflater;
import java.util.zip.*;
import java.util.HashMap;

public class testClient {
   public static void main(String[] args) {
   	

   	try{
   		AssignmentSubmissionSystem f = (AssignmentSubmissionSystem) Naming.lookup(AssignmentSubmissionSystem.SERVICENAME);
   		userClass accessTest = new userClass("mark", "pass", "   ");

      userClass loginTest = f.login("p", "test");
      userClass loginTest2 = f.login("mark", "pass");

   		String[][] accounts = f.retrieve();
      System.out.println(loginTest.userID+loginTest.psswrd+loginTest.type+loginTest.zipFile);
      System.out.println(loginTest2.userID+loginTest2.psswrd+loginTest2.type+loginTest2.zipFile);

      	
   	}catch(Exception e){
   		System.err.println("Remote exception " + e.getMessage());
   		e.printStackTrace();
   	}
   	//test upload 
      
   	try{
   		 AssignmentSubmissionSystem f = (AssignmentSubmissionSystem) Naming.lookup(AssignmentSubmissionSystem.SERVICENAME);
   		 File file = new File("lolz.zip");
		    byte buffer[] = new byte[(int)file.length()];
		    BufferedInputStream input = new	BufferedInputStream(new FileInputStream("lolz.zip"));
		    input.read(buffer,0,buffer.length);
		    input.close();
   		

          boolean uploadS = f.upload("p", "lolz.zip", buffer);
      	 if(uploadS == true){
      	 System.out.println("upload success did not exist before");
          }
          boolean uploadS2 = f.upload("p", "lol.zip", buffer);
      	 if(uploadS2 == false){
      		System.out.println("repeat upload success");
      	 }
     	
   	}catch(Exception e){
   		System.err.println("Remote exception " + e.getMessage());
   		e.printStackTrace();
   	}
      //test download
      try {
          AssignmentSubmissionSystem f = (AssignmentSubmissionSystem) Naming.lookup(AssignmentSubmissionSystem.SERVICENAME);
          
          FileOutputStream fos = new FileOutputStream("lol.zip");
          fos.write(f.download("mark"));
          fos.flush();
          fos.close();

          
          

          //unzip file
          
          final int BUFFER = 2048;
          BufferedOutputStream dest = null;
          FileInputStream fis = new 
          FileInputStream("lol.zip");
          CheckedInputStream checksum = new CheckedInputStream(fis, new Adler32());
          ZipInputStream zis = new ZipInputStream(new BufferedInputStream(checksum));
          ZipEntry entry;
          while((entry = zis.getNextEntry()) != null) {
            System.out.println("Extracting: " +entry);
            int count;
            byte data[] = new byte[BUFFER];
            // write the files to the disk
            FileOutputStream fos2 = new FileOutputStream(entry.getName());
            dest = new BufferedOutputStream(fos2, BUFFER);
            while ((count = zis.read(data, 0, BUFFER)) != -1) {
               dest.write(data, 0, count);
            }
            dest.flush();
            dest.close();
          }
          zis.close();
              
          
          
          //delete zip
          File here = new File(".");
          String herepath = here.getAbsolutePath();
          System.out.println(herepath.substring(0, herepath.length() - 1) + "lol.zip");
          File zipFileDelete = new File(herepath.substring(0, herepath.length() - 1) + "lol.zip");
          if(zipFileDelete.delete()){
              System.out.println(zipFileDelete.getName() + " is deleted!");
          }else{
            System.out.println("Delete operation is failed.");
          }



       }
      catch(Exception e) {
          System.err.println("FileServer exception: "+ e.getMessage());
          e.printStackTrace();
      }
      


      
   }
}