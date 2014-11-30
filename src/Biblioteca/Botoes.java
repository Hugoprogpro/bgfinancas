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
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

public class Botoes {
    
    private Botoes(){ }
    
    public static JButton Cadastrar(final Janela janela){
        final Linguagem Lingua = Linguagem.getInstance();
        JButton BCadastrar;
        BCadastrar = new javax.swing.JButton();
        BCadastrar.setIcon(new javax.swing.ImageIcon(Botoes.class.getClass().getResource("/Imagens/acoes/cadastrar.png")));
        BCadastrar.setToolTipText(Lingua.getMensagem("cadastrar"));
        BCadastrar.setFocusable(false);
        BCadastrar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BCadastrar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        BCadastrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                janela.Botoes(Lingua.getMensagem("cadastrar"));
            }
        });
        BCadastrar.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0), "F1");  
        BCadastrar.getActionMap().put("F1", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                janela.Botoes(Lingua.getMensagem("cadastrar"));
            }
        });
        return BCadastrar;
    }
    
    public static JButton Cadastrar(final JanelaModal janela){
        final Linguagem Lingua = Linguagem.getInstance();
        JButton BCadastrar;
        BCadastrar = new javax.swing.JButton();
        BCadastrar.setIcon(new javax.swing.ImageIcon(Botoes.class.getClass().getResource("/Imagens/acoes/cadastrar.png")));
        BCadastrar.setToolTipText(Lingua.getMensagem("cadastrar"));
        BCadastrar.setFocusable(false);
        BCadastrar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BCadastrar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        BCadastrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                janela.Botoes(Lingua.getMensagem("cadastrar"));
            }
        });
        BCadastrar.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0), "F1");  
        BCadastrar.getActionMap().put("F1", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                janela.Botoes(Lingua.getMensagem("cadastrar"));
            }
        });
        return BCadastrar;
    }
    
    public static JButton Alterar(final Janela janela){
        final Linguagem Lingua = Linguagem.getInstance();
        JButton BAlterar;
        BAlterar = new javax.swing.JButton();
        BAlterar.setIcon(new javax.swing.ImageIcon(Botoes.class.getClass().getResource("/Imagens/acoes/alterar.png")));
        BAlterar.setToolTipText(Lingua.getMensagem("alterar"));
        BAlterar.setFocusable(false);
        BAlterar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BAlterar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        BAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                janela.Botoes(Lingua.getMensagem("alterar"));
            }
        });
        BAlterar.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0), "F2");  
        BAlterar.getActionMap().put("F2", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                janela.Botoes(Lingua.getMensagem("alterar"));
            }
        });
        return BAlterar;
    }
    
    public static JButton Alterar(final JanelaModal janela){
        final Linguagem Lingua = Linguagem.getInstance();
        JButton BAlterar;
        BAlterar = new javax.swing.JButton();
        BAlterar.setIcon(new javax.swing.ImageIcon(Botoes.class.getClass().getResource("/Imagens/acoes/alterar.png")));
        BAlterar.setToolTipText(Lingua.getMensagem("alterar"));
        BAlterar.setFocusable(false);
        BAlterar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BAlterar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        BAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                janela.Botoes(Lingua.getMensagem("alterar"));
            }
        });
        BAlterar.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0), "F2");  
        BAlterar.getActionMap().put("F2", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                janela.Botoes(Lingua.getMensagem("alterar"));
            }
        });
        return BAlterar;
    }
    
    public static JButton Excluir(final Janela janela){
        final Linguagem Lingua = Linguagem.getInstance();
        JButton BExcluir;
        BExcluir = new javax.swing.JButton();
        BExcluir.setIcon(new javax.swing.ImageIcon(Botoes.class.getClass().getResource("/Imagens/acoes/excluir.png")));
        BExcluir.setToolTipText(Lingua.getMensagem("excluir"));
        BExcluir.setFocusable(false);
        BExcluir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BExcluir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        BExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                janela.Botoes(Lingua.getMensagem("excluir"));
            }
        });
        BExcluir.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0), "F3");  
        BExcluir.getActionMap().put("F3", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                janela.Botoes(Lingua.getMensagem("excluir"));
            }
        });
        return BExcluir;
    }
    
    public static JButton Excluir(final JanelaModal janela){
        final Linguagem Lingua = Linguagem.getInstance();
        JButton BExcluir;
        BExcluir = new javax.swing.JButton();
        BExcluir.setIcon(new javax.swing.ImageIcon(Botoes.class.getClass().getResource("/Imagens/acoes/excluir.png")));
        BExcluir.setToolTipText(Lingua.getMensagem("excluir"));
        BExcluir.setFocusable(false);
        BExcluir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BExcluir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        BExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                janela.Botoes(Lingua.getMensagem("excluir"));
            }
        });
        BExcluir.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0), "F3");  
        BExcluir.getActionMap().put("F3", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                janela.Botoes(Lingua.getMensagem("excluir"));
            }
        });
        return BExcluir;
    }
    
    public static JButton Consultar(final Janela janela){
        final Linguagem Lingua = Linguagem.getInstance();
        JButton BConsultar;
        BConsultar = new javax.swing.JButton();
        BConsultar.setIcon(new javax.swing.ImageIcon(Botoes.class.getClass().getResource("/Imagens/acoes/consultar.png")));
        BConsultar.setToolTipText(Lingua.getMensagem("consultar"));
        BConsultar.setFocusable(false);
        BConsultar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BConsultar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        BConsultar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                janela.Botoes(Lingua.getMensagem("consultar"));
            }
        });
        BConsultar.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0), "F4");  
        BConsultar.getActionMap().put("F4", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                janela.Botoes(Lingua.getMensagem("consultar"));
            }
        });
        return BConsultar;
    }
    
    public static JButton Consultar(final JanelaModal janela){
        final Linguagem Lingua = Linguagem.getInstance();
        JButton BConsultar;
        BConsultar = new javax.swing.JButton();
        BConsultar.setIcon(new javax.swing.ImageIcon(Botoes.class.getClass().getResource("/Imagens/acoes/consultar.png")));
        BConsultar.setToolTipText(Lingua.getMensagem("consultar"));
        BConsultar.setFocusable(false);
        BConsultar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BConsultar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        BConsultar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                janela.Botoes(Lingua.getMensagem("consultar"));
            }
        });
        BConsultar.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0), "F4");  
        BConsultar.getActionMap().put("F4", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                janela.Botoes(Lingua.getMensagem("consultar"));
            }
        });
        return BConsultar;
    }
    
    public static JButton Sair(final Janela janela){
        final Linguagem Lingua = Linguagem.getInstance();
        JButton BSair;
        BSair = new javax.swing.JButton();
        BSair.setIcon(new javax.swing.ImageIcon(Botoes.class.getClass().getResource("/Imagens/acoes/sair.png")));
        BSair.setToolTipText(Lingua.getMensagem("sair"));
        BSair.setFocusable(false);
        BSair.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BSair.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        BSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                janela.Botoes(Lingua.getMensagem("sair"));
            }
        });
        BSair.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0), "F12");  
        BSair.getActionMap().put("F12", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                janela.Botoes(Lingua.getMensagem("sair"));
            }
        });
        return BSair;
    }
    
    public static JButton Sair(final JanelaModal janela){
        final Linguagem Lingua = Linguagem.getInstance();
        JButton BSair;
        BSair = new javax.swing.JButton();
        BSair.setIcon(new javax.swing.ImageIcon(Botoes.class.getClass().getResource("/Imagens/acoes/sair.png")));
        BSair.setToolTipText(Lingua.getMensagem("sair"));
        BSair.setFocusable(false);
        BSair.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BSair.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        BSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                janela.Botoes(Lingua.getMensagem("sair"));
            }
        });
        BSair.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0), "F12");  
        BSair.getActionMap().put("F12", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                janela.Botoes(Lingua.getMensagem("sair"));
            }
        });
        return BSair;
    }
}
