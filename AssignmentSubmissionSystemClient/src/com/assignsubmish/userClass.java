package com.assignsubmish;

public class userClass implements java.io.Serializable {

    public String userID;
    public String psswrd;
    public String type;
    public String zipFile;

    public userClass(String uID, String pass, String userType) {
        userID = uID;
        psswrd = pass;
        type = userType;
        System.out.println(userID + psswrd + type);
    }

    public void assignZip(String tFile){
        zipFile =  tFile;

    }


}