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
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class Fonte
{
    private static Fonte instance = null;  
    private static Font FonteFinancas = null;
    private static Font FonteFinancasNegrito = null;
    private static Font FonteFinancasPequena = null;
    private final Linguagem Lingua = Linguagem.getInstance();
    
    private Fonte(){ 
        try {
            URL fonteUrl = null;
            InputStream fonteArquivo1 = getClass().getResourceAsStream("/fonte.ttf");
            InputStream fonteArquivo2 = getClass().getResourceAsStream("/fonte.ttf");
            InputStream fonteArquivo3 = getClass().getResourceAsStream("/fonte.ttf");
            FonteFinancas = Font.createFont(Font.TRUETYPE_FONT, fonteArquivo1).deriveFont(Font.PLAIN, 12f);
            FonteFinancasNegrito = Font.createFont(Font.TRUETYPE_FONT, fonteArquivo2).deriveFont(Font.BOLD, 12f);
            FonteFinancasPequena = Font.createFont(Font.TRUETYPE_FONT, fonteArquivo3).deriveFont(Font.PLAIN, 11f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(FonteFinancas);
            ge.registerFont(FonteFinancasPequena);
        } catch (FontFormatException ex) {
            Janelinha.Aviso(Lingua.getMensagem("erro"), Lingua.getMensagem("excecao")+" "+ex);
        } catch (IOException ex) {
            Janelinha.Aviso(Lingua.getMensagem("erro"), Lingua.getMensagem("excecao")+" "+ex);
        }
    }
    
    public Font getFonte(){
        return FonteFinancas;
    }
    
    public Font getFonteNegrito(){
        return FonteFinancasNegrito;
    }
    
    public Font getFontePequena(){
        return FonteFinancasPequena;
    }
    
    public static Fonte getInstance(){  
        if(instance == null){  
            instance = new Fonte();  
        }  
        return instance;  
   } 
    
}