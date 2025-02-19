/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.edusys.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author pc
 */
public class JdbcHelper {

    private static String drive = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static String dburl = "jdbc:sqlserver://localhost:1433;databaseName=EduSys";
    private static String user = "sa";
    private static String pass = "123";
    //Nạp driver
    static{
        try {
            Class.forName(drive);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }
      public static PreparedStatement getStmt(String sql, Object...args) throws SQLException {
          Connection connection = DriverManager.getConnection(dburl, user, pass);
          PreparedStatement pstmt = null;
          if (sql.trim().startsWith("{")) {
              pstmt = connection.prepareCall(sql);
          }else{
              pstmt = connection.prepareStatement(sql);
          }
          for(int i = 0;i<args.length;i++){
              pstmt.setObject(i+1,args[i]);
          }
          return pstmt;
      }
       public static int update(String sql,Object...agrs){
           try {
               PreparedStatement stmt = getStmt(sql, agrs);
               try{
                   return stmt.executeUpdate();
               }finally{    
                   stmt.getConnection().close();
               }
           } catch (Exception e) {
               throw new RuntimeException(e);
           }
       }
        public static ResultSet query(String sql,Object...agrs){
            try {
                PreparedStatement stmt = getStmt(sql, agrs);
                return stmt.executeQuery();
            } catch (SQLException e) {
                throw new  RuntimeException(e);
            }
        }
         public static Object value(String sql, Object...agrs){
             try {
                 ResultSet rs = query(sql, agrs);
                 if (rs.next()) {
                     return rs.getObject(0);
                 }
                 rs.getStatement().getConnection().close();
             } catch (Exception e) {
                 throw new RuntimeException(e);
             }
             return null;
         }
}
