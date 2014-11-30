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
import javax.swing.JDialog;

public class JanelaModal extends javax.swing.JDialog {
    
    public Linguagem Lingua = Linguagem.getInstance();
    public Font FonteFinancas = Fonte.getInstance().getFonte();
    public Font FonteFinancasNegrito = Fonte.getInstance().getFonteNegrito();
    public Font FonteFinancasPequena = Fonte.getInstance().getFontePequena();
    public Conexao Banco = Conexao.getInstance();
    
    public JanelaModal(java.awt.Frame parent, boolean modal){
        super(parent, modal);
    }
       
    public JDialog getJanela(){
        return this;
    }
    
    public void Botoes(String acao){ }
    
}
