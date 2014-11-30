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

import java.sql.SQLException;

public class Configuracoes
{
    public static Conexao Banco = Conexao.getInstance();
    
    private Configuracoes(){ }

    public static void Verificar(){
        try {
            // Tabela de configuracao            
            Banco.executeQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.SYSTEM_TABLES where TABLE_TYPE='TABLE' AND (TABLE_NAME='configuracoes' OR TABLE_NAME='CONFIGURACOES')");
            if(!Banco.getResultSet().next()){
                Banco.executeUpdate("CREATE TABLE configuracoes(nome VARCHAR(20) PRIMARY KEY, valor VARCHAR(50))");
            }
            // Configuracao: idioma
            Banco.executeQuery("SELECT valor FROM configuracoes WHERE nome='idioma'");
            if(!Banco.getResultSet().next()){
                Banco.executeUpdate("INSERT INTO configuracoes VALUES('idioma','en_US')");
            }
            // Configuracao: moeda
            Banco.executeQuery("SELECT valor FROM configuracoes WHERE nome='moeda'");
            if(!Banco.getResultSet().next()){
                Banco.executeUpdate("INSERT INTO configuracoes VALUES('moeda','$')");
            }
            // Configuracao: tema
            Banco.executeQuery("SELECT valor FROM configuracoes WHERE nome='tema'");
            if(!Banco.getResultSet().next()){
                Banco.executeUpdate("INSERT INTO configuracoes VALUES('tema','OfficeSilver2007')");
            }
        } catch (SQLException ex) {
            Janelinha.Aviso("",ex.getMessage());
        }
    }
    
    public static String getPropriedade(String Propriedade) throws SQLException{
        Banco.executeQuery("SELECT valor FROM configuracoes WHERE nome='"+Propriedade+"'");
        Banco.getResultSet().next();
        return Banco.getResultSet().getString("valor");
    }
    
    public static String[] getListaTemas(){
        String[] lista_temas = {"Autumn","Business","BusinessBlackSteel","BusinessBlueSteel","Creme","CremeCoffee","Dust","DustCoffee","MistAqua","MistSilver","Moderate","Nebula","NebulaBrickWall","OfficeBlue2007","OfficeSilver2007","Sahara"};  
        return lista_temas;
   }

}
