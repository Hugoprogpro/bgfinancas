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

package Linguagens;

import Biblioteca.Configuracoes;
import Biblioteca.Janelinha;
import java.sql.SQLException;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Linguagem {
    
   private static Linguagem instance = null; 
   private static final String[][] Idiomas = {{"Portugues Brasileiro","pt_BR"},{"English","en_US"}};
   private ResourceBundle RB;
   private Locale L;
   private String Moeda;
  
   private Linguagem(){
       try {
           CarregarIdioma();
       } catch (SQLException ex) {
           Janelinha.Aviso("Erro", ex.getMessage());
       }
   }
   
   public final void CarregarIdioma() throws SQLException{
       Configuracoes.Verificar();
        try {
            String[] idioma = Configuracoes.getPropriedade("idioma").split("_");
            L = new Locale(idioma[0],idioma[1]);
        }catch (SQLException ex) {
            Janelinha.Aviso("Erro", ex.getMessage());
            L = new Locale("en","US");
        }
        RB = ResourceBundle.getBundle("Linguagens/Linguagem", L);
        Moeda = Configuracoes.getPropriedade("moeda");
   }
   
   public static String[][] getIdiomas(){
        return Idiomas;
   }
   
   public static String[] getListaIdiomas(){
        String[] lista_idiomas = new String[Idiomas.length];
        for(int i=0; i<Idiomas.length; i++){
            lista_idiomas[i] = Idiomas[i][0];
        }
        return lista_idiomas;
   }
  
   public String getMensagem(String mensagem){  
        if(mensagem.equals("moeda")){
            return Moeda;
        }else{
            try{
                return RB.getString(mensagem);
            }catch(MissingResourceException ex){
                Janelinha.Aviso(this.getMensagem("erro"), this.getMensagem("excecao")+": "+ex.getMessage());
                return "?????";
            }
        }
   }
   
   public static Linguagem getInstance(){  
        if(instance == null){  
            instance = new Linguagem();  
        }  
        return instance;  
   }  
}
