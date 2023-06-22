package org.example;

public class Runner {
    static SQLUtilities sql=new SQLUtilities();
    public static void main(String args[]){
        sql.createConnection();
        sql.createJSONData("SELECT * FROM user");
        sql.readJSON("id");
        //sql.closeConnection();
    }
}
