package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;

import com.google.gson.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class SQLUtilities {

    int column_count=0;
    int row_count=0;
    private String column_Names[]=null;
    private Connection con=null;
    private Statement sm=null;
    private ResultSet rs=null,rc=null;
    private ResultSetMetaData rsmd=null;
    public void createConnection(){
        try{
            Class.forName("org.sqlite.JDBC");
            con= DriverManager.getConnection("jdbc:sqlite:C:\\Users\\akshaypa\\Desktop\\database\\sampledb");
            System.out.println("The Connection Opened Successfully");
        }catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
    }
    public void closeConnection(){
        if(con!=null){
            try{
                con.close();
                System.out.println("The Connection Closed Successfully");
            }catch ( Exception e ) {
                System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                System.exit(0);
            }
        }
    }
    public void createJSONData(String query){
        try{
            sm= con.createStatement();
            rc=sm.executeQuery(query);
            rsmd=rc.getMetaData();
            column_count= rsmd.getColumnCount();
            while (rc.next()){
                row_count = rc.getInt(1);
            }
            column_Names=new String[column_count];
            for(int i=0;i<column_count;i++) {
                column_Names[i]=rsmd.getColumnName((i+1));
            }
            JSONObject JO=new JSONObject();
            JSONArray JA=new JSONArray();
            rs=sm.executeQuery(query);
            while(rs.next()){
                JSONObject JR=new JSONObject();
                int j=1;
                while(j<=column_count){
                    JR.put(column_Names[j-1],rs.getString(column_Names[j-1]));
                    j++;
                }
                JA.add(JR);
            }
            String Res=query+" Results";
            JO.put(Res,JA);

            FileWriter file =new FileWriter(System.getProperty("user.dir")+"\\Output\\QueryResults.json");
            file.write(JO.toJSONString());
            file.close();
            System.out.println("..JSON File Created..");

        }catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }

    }
    public void readJSON(String Key) {
        try {
            Gson gs = new Gson();
            BufferedReader BR = new BufferedReader(new FileReader(System.getProperty("user.dir") + "\\Output\\QueryResults.json"));
            Map<?, ?> map = gs.fromJson(BR, Map.class);
            System.out.println(map);
        }catch(Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
    }

}
