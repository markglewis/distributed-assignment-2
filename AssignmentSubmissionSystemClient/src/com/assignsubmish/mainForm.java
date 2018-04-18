package com.assignsubmish;

import javax.swing.*;
import javax.xml.soap.Text;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.rmi.Naming;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class mainForm {
    private JButton LoginButton;
    private JPanel panel1;
    private JTextField TextField1;
    private JTextField TextField2;
    private JLabel loginLabel;
    private JButton downLoadbtn;
    private JButton uploadButton;
    private JButton logoutButton;
    private JTextArea resultsDisplay;
    private userClass login;


    public mainForm() {

        LoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //enter a user and password
                String userID = TextField1.getText();
                String pass = TextField2.getText();
                try{
                    AssignmentSubmissionSystem f = (AssignmentSubmissionSystem) Naming.lookup(AssignmentSubmissionSystem.SERVICENAME);


                    login = f.login(userID.toString(), pass.toString());
                    if(login.userID != "noResult"){
                        loginLabel.setText("Invalid username or password");
                    }
                    else{
                        LoginButton.setVisible(false);
                        LoginButton.setEnabled(false);
                        if(login.type.equals("student")){
                            loginLabel.setText("Welcome: " + login.type + " " + login.userID);
                            uploadButton.setVisible(true);
                            uploadButton.setEnabled(true);
                            logoutButton.setVisible(true);
                            logoutButton.setEnabled(true);
                            TextField1.setText("Upload File Name");
                            TextField2.setText("Upload File Location");


                        }
                        else if(login.type.equals("admin")){
                            loginLabel.setText("Welcome: " + login.type + " " + login.userID);
                            downLoadbtn.setVisible(true);
                            downLoadbtn.setEnabled(true);
                            String[][] accounts = f.retrieve();
                            String listings = "";
                            for(int i =0; i < accounts.length;i++){
                                for(int j =0; j < 3;j++){
                                    listings += accounts[i][0]+ ": " + accounts[i][3] + "\n";
                                }
                            }
                            resultsDisplay.setText(listings);
                            logoutButton.setVisible(true);
                            logoutButton.setEnabled(true);
                            TextField1.setText("Student ID");
                            TextField2.setText("Folder Location");
                        }
                    }





                }catch(Exception ex){
                    System.err.println("Remote exception " + ex.getMessage());
                    ex.printStackTrace();
                }

            }
        });
        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    AssignmentSubmissionSystem f = (AssignmentSubmissionSystem) Naming.lookup(AssignmentSubmissionSystem.SERVICENAME);
                    String fileName = TextField1.getText();
                    String pathName = TextField2.getText();
                    File file = new File(fileName);
                    byte buffer[] = new byte[(int)file.length()];
                    BufferedInputStream input = new BufferedInputStream(new FileInputStream(pathName+fileName));
                    input.read(buffer,0,buffer.length);
                    input.close();


                    boolean uploadS = f.upload(login.userID, fileName, buffer);
                    if(uploadS == true){
                        loginLabel.setText("Submission Success");
                    }else{
                        loginLabel.setText("File Already Submitted: "+ login.zipFile);
                    }

                }catch(Exception ex){
                    System.err.println("Remote exception " + ex.getMessage());
                    ex.printStackTrace();
                }


            }
        });
        downLoadbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    AssignmentSubmissionSystem f = (AssignmentSubmissionSystem) Naming.lookup(AssignmentSubmissionSystem.SERVICENAME);
                    String pathName = TextField1.getText();
                    String studentId = TextField2.getText();
                    String fileName ="";

                    String[][] accounts = f.retrieve();
                    for(int i =0; i < accounts.length; i++){

                        if(studentId == accounts[i][0]){
                            fileName = accounts[i][3];

                        }

                    }


                    FileOutputStream fos = new FileOutputStream(pathName+fileName);
                    fos.write(f.download(studentId));
                    fos.flush();
                    fos.close();




                    //unzip file

                    final int BUFFER = 2048;
                    BufferedOutputStream dest = null;
                    FileInputStream fis = new FileInputStream(pathName+fileName);
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

                    loginLabel.setText(studentId + " file downloaded");



                    //delete zip
                    File zipFileDelete = new File(pathName+fileName);
                    if(zipFileDelete.delete()){
                        System.out.println(zipFileDelete.getName() + " is deleted!");
                    }else{
                        System.out.println("Delete operation is failed.");
                    }



                }
                catch(Exception ex) {
                    System.err.println("FileServer exception: "+ ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logoutButton.setEnabled(false);
                uploadButton.setEnabled(false);
                LoginButton.setEnabled(true);
                logoutButton.setVisible(false);
                uploadButton.setVisible(false);
                LoginButton.setVisible(true);
                loginLabel.setText("Enter User ID and Password");

            }
        });
    }

    public static void main(String[] args){
        JFrame frame = new JFrame("App");
        frame.setContentPane(new mainForm().panel1);
        frame.setSize(1500, 1300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);


    }


}
