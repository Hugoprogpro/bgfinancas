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
import javax.swing.JOptionPane;

public class Janelinha {
    
    private Janelinha(){ }
    
    public static boolean Pergunta(String Titulo, String Mensagem)
    {
        final Linguagem Lingua = Linguagem.getInstance();
        Object[] options = {Lingua.getMensagem("sim"),Lingua.getMensagem("nao")};   
        int i = JOptionPane.showOptionDialog(null, Mensagem, Titulo, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if(i == JOptionPane.YES_OPTION)
        {
            return true;
        }else{
            return false;
        }
    }
    
    public static void Aviso(String Titulo, String Mensagem)
    {
        if(Titulo.equals("Erro")){
            JOptionPane.showMessageDialog(null, Mensagem, Titulo, JOptionPane.ERROR_MESSAGE, null);
        }else{
            final Linguagem Lingua = Linguagem.getInstance();
            if(Titulo.equals(Lingua.getMensagem("erro"))){
                JOptionPane.showMessageDialog(null, Mensagem, Titulo, JOptionPane.ERROR_MESSAGE, null);
            }else{
                JOptionPane.showMessageDialog(null, Mensagem, Titulo, JOptionPane.INFORMATION_MESSAGE, null);
            }  
        }
    }
    
    public static String Entrada(String titulo, String mensagem){
        final Linguagem Lingua = Linguagem.getInstance();
        Object resposta = JOptionPane.showInputDialog(null, mensagem, titulo, JOptionPane.QUESTION_MESSAGE);
        if(resposta==null){
            return "cancelado";
        }else{
            return resposta.toString();
        }
    }
}
