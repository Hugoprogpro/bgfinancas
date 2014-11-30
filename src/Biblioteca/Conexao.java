/*
Copyright 2010, 2012, 2014 Jose Robson Mariano Alves

This file is part of bgfinancas.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This package is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program. If not, see <http://www.gnu.org/licenses/>.

*/

package Biblioteca;

import java.io.File;
import java.sql.*;

public class Conexao
{
    private static Conexao instance = null;  
    final private String driver = "org.hsqldb.jdbcDriver";
    private String url;
    private Connection conexao = null;
    private Statement statement;
    private ResultSet resultset;
    
    private Conexao(){  }
    
    public boolean Conectar()
    {
        try{
            String sistema_operacional = System.getProperty("os.name");
            if(sistema_operacional.equals("Linux")){
                String home = System.getProperty("user.home")+"/.bgfinancas/";
                new File(home).mkdirs();
                url = home+"banco";
            }else{
                url = "banco";
            }
            Class.forName(driver);
            conexao = DriverManager.getConnection("jdbc:hsqldb:file:"+url, "sa", "");
            return true;
        }catch(ClassNotFoundException Driver){
            Janelinha.Aviso("Erro",Driver.getMessage());
            return false;
        }catch(SQLException Fonte){
            Janelinha.Aviso("Erro",Fonte.getMessage());
            return false;
        }
    }
    
    public boolean Desconectar()
    {
        try {
            conexao.close();
            return true;
        }catch(SQLException fechar) {
            Janelinha.Aviso("Erro",fechar.getMessage());
            return false;
        }
    }
    
    public boolean isConectado()
    {
        if(conexao != null){
            return true;
        }else{
            return false;
        }
    } 
    
    public ResultSet getResultSet()
    {
        return resultset;
    }
    
    public boolean executeQuery(String sql)
    {
        try {
            if(!isConectado()) {
                Conectar();
            }
            statement = conexao.createStatement();
            resultset = statement.executeQuery(sql);
            return true;
        }catch(SQLException sqlex){
            Janelinha.Aviso("Erro",sqlex.getMessage());
            return false;
        }
    }
    
    public int executeUpdate(String sql)
    {
        try
        {
            if(!isConectado()) {
                Conectar();
            }
            statement = conexao.createStatement();
            int linhasAfetadas;
            linhasAfetadas = statement.executeUpdate(sql);
            return linhasAfetadas;
        }catch(SQLException sqlex)
        {
            Janelinha.Aviso("Erro",sqlex.getMessage());
            return 0;
        }
    }
    
    public void execute(String sql) throws SQLException
    {
        if(!isConectado()) {
            Conectar();
        }
        statement = conexao.createStatement();
        Boolean resultado = statement.execute(sql);
        statement.close();
    }
    
    public static Conexao getInstance(){  
        if(instance == null){  
            instance = new Conexao();  
        }  
        return instance;  
   } 
    
}