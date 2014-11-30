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

import Linguagens.Linguagem;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Datas
{
    
    private Datas(){ }

    public static String ConverterData(String DATA) {
        Linguagem Lingua = Linguagem.getInstance();
        if(DATA.equals("  /  /    "))
        {
            return "0000-00-00";
        }else{
            try {
                SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
                Date data = formatador.parse(DATA);
                SimpleDateFormat novo_formatador = new SimpleDateFormat("yyyy-MM-dd");
                String novoFormato = novo_formatador.format(data);
                return novoFormato;
            } catch (ParseException ex) {
                Logger.getLogger(Datas.class.getName()).log(Level.SEVERE, null, ex);
                return Lingua.getMensagem("erro");
            }
        }
    }
    
    public static String DataExtenso(String DataInicial, String DataFinal) throws ParseException {
        try{
            Linguagem Lingua = Linguagem.getInstance();
            String DataExtenso;
            String Mes,Dia,Ano;
            String[] idioma = Configuracoes.getPropriedade("idioma").split("_");
            Locale.setDefault (new Locale (idioma[0], idioma[1]));
            DateFormat df = new SimpleDateFormat ("yyyy-MM-dd");
            Date dt = df.parse (DataInicial);
            DateFormat df3 = new SimpleDateFormat ("MMMM");
            Mes = df3.format (dt);
            DateFormat df4 = new SimpleDateFormat ("dd");
            Dia = df4.format (dt);
            DateFormat df5 = new SimpleDateFormat ("yyyy");
            Ano = df5.format (dt);
            String de_data = Lingua.getMensagem("de_data");
            if(!de_data.equals("")){
                de_data = " "+de_data;
            }
            DataExtenso = Dia+de_data+" "+Mes+de_data+" "+Ano;
            dt = df.parse (DataFinal);
            Mes = df3.format (dt);
            Dia = df4.format (dt);
            Ano = df5.format (dt);
            DataExtenso = DataExtenso+" / "+Dia+de_data+" "+Mes+de_data+" "+Ano;
            return DataExtenso;
        } catch (SQLException ex) {
            Janelinha.Aviso("",ex.getMessage());
            return "???????";
        }
    }

}
