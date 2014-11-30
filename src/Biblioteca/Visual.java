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
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;

public class Visual {
    
    private Visual(){ }
    
    public static void Janela(JFrame janela)
    {
        Linguagem Lingua = Linguagem.getInstance();
        try {
            // Icone barra de titulo
            janela.setIconImage(new ImageIcon(Visual.class.getClass().getResource("/Imagens/layout/binarygroup.png")).getImage());
            // Cursor
            Toolkit toolkit = Toolkit.getDefaultToolkit();  
            Point hotSpot = new Point(0,0);  
            Cursor cursor;
            cursor = toolkit.createCustomCursor(ImageIO.read(Visual.class.getClass().getResourceAsStream("/Imagens/layout/cursor.gif")), hotSpot, "Cursor");
            janela.setCursor(cursor);
            janela.setResizable(false);
        } catch (IOException ex) { 
            Janelinha.Aviso(Lingua.getMensagem("erro"), Lingua.getMensagem("excecao")+" "+ex);
        }
    }
    
    public static void JanelaModal(JDialog janela)
    {
        Linguagem Lingua = Linguagem.getInstance();
        try {
            // Icone barra de titulo
            janela.setIconImage(new ImageIcon(Visual.class.getClass().getResource("/Imagens/layout/binarygroup.png")).getImage());
            // Cursor
            Toolkit toolkit = Toolkit.getDefaultToolkit();  
            Point hotSpot = new Point(0,0);  
            Cursor cursor;
            cursor = toolkit.createCustomCursor(ImageIO.read(Visual.class.getClass().getResourceAsStream("/Imagens/layout/cursor.gif")), hotSpot, "Cursor");
            janela.setCursor(cursor);
            janela.setResizable(false);
        } catch (IOException ex) { 
            Janelinha.Aviso(Lingua.getMensagem("erro"), Lingua.getMensagem("excecao")+" "+ex);
        }
    }
    
}
