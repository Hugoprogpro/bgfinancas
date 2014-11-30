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

package Ajuda;

import Biblioteca.Botoes;
import Biblioteca.Janela;
import Biblioteca.Janelinha;
import Biblioteca.Visual;
import Principal.TelaPrincipal;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URISyntaxException;

public class Links extends Janela {

    private final TelaPrincipal Principal;

    public Links(TelaPrincipal Tela) {
        Principal = Tela;
        initComponents();
        Visual.Janela(this);
        BarraDeFerramentas.add(Botoes.Sair(this));
    }
    
    @Override
    public void Botoes(String acao)
    {
        if(acao.equals(Lingua.getMensagem("sair"))){
            Links.this.dispose();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JLabel imageLabel = new javax.swing.JLabel();
        javax.swing.JLabel imageLabel2 = new javax.swing.JLabel();
        javax.swing.JLabel imageLabel4 = new javax.swing.JLabel();
        javax.swing.JLabel site = new javax.swing.JLabel();
        javax.swing.JLabel imageLabel6 = new javax.swing.JLabel();
        javax.swing.JLabel imageLabel8 = new javax.swing.JLabel();
        javax.swing.JLabel forum = new javax.swing.JLabel();
        javax.swing.JLabel imageLabel10 = new javax.swing.JLabel();
        javax.swing.JLabel manual = new javax.swing.JLabel();
        javax.swing.JLabel imageLabel12 = new javax.swing.JLabel();
        BarraDeFerramentas = new javax.swing.JToolBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(Lingua.getMensagem("links"));
        setResizable(false);
        getContentPane().setLayout(null);

        imageLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        imageLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagens/layout/binarygroup.png"))); // NOI18N
        getContentPane().add(imageLabel);
        imageLabel.setBounds(10, 40, 160, 80);

        imageLabel2.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        imageLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        imageLabel2.setText("BinaryGroup");
        getContentPane().add(imageLabel2);
        imageLabel2.setBounds(10, 110, 160, 50);

        imageLabel4.setFont(FonteFinancas);
        imageLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        imageLabel4.setText(Lingua.getMensagem("website_oficial")+":");
        getContentPane().add(imageLabel4);
        imageLabel4.setBounds(160, 80, 120, 20);

        site.setFont(FonteFinancas);
        site.setForeground(new java.awt.Color(0, 0, 255));
        site.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        site.setText("http://bgfinancas.sourceforge.net");
        site.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                siteMouseClicked(evt);
            }
        });
        getContentPane().add(site);
        site.setBounds(290, 80, 280, 20);

        imageLabel6.setFont(FonteFinancas);
        imageLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        imageLabel6.setText(Lingua.getMensagem("desenvolvedor")+":");
        getContentPane().add(imageLabel6);
        imageLabel6.setBounds(160, 50, 120, 30);

        imageLabel8.setFont(FonteFinancas);
        imageLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        imageLabel8.setText(Lingua.getMensagem("forum")+":");
        getContentPane().add(imageLabel8);
        imageLabel8.setBounds(160, 130, 120, 20);

        forum.setFont(FonteFinancas);
        forum.setForeground(new java.awt.Color(0, 0, 255));
        forum.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        forum.setText("http://sourceforge.net/p/bgfinancas/forum");
        forum.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                forumMouseClicked(evt);
            }
        });
        getContentPane().add(forum);
        forum.setBounds(290, 130, 310, 20);

        imageLabel10.setFont(FonteFinancas);
        imageLabel10.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        imageLabel10.setText("Jose Robson Mariano Alves");
        getContentPane().add(imageLabel10);
        imageLabel10.setBounds(290, 50, 280, 30);

        manual.setFont(FonteFinancas);
        manual.setForeground(new java.awt.Color(0, 0, 255));
        manual.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        manual.setText("http://bgfinancas.sourceforge.net#manual");
        manual.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                manualMouseClicked(evt);
            }
        });
        getContentPane().add(manual);
        manual.setBounds(290, 100, 280, 30);

        imageLabel12.setFont(FonteFinancas);
        imageLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        imageLabel12.setText(Lingua.getMensagem("manual")+":");
        getContentPane().add(imageLabel12);
        imageLabel12.setBounds(160, 100, 120, 30);

        BarraDeFerramentas.setFloatable(false);
        BarraDeFerramentas.setRollover(true);
        getContentPane().add(BarraDeFerramentas);
        BarraDeFerramentas.setBounds(0, 0, 620, 40);

        setSize(new java.awt.Dimension(620, 203));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void siteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_siteMouseClicked
        Desktop desk = java.awt.Desktop.getDesktop();     
        try {    
             desk.browse(new java.net.URI("http://bgfinancas.sourceforge.net/"));    
        } catch (URISyntaxException e) {    
            Janelinha.Aviso(Lingua.getMensagem("erro"), Lingua.getMensagem("excecao")+" "+e);
        } catch (IOException e) {
            Janelinha.Aviso(Lingua.getMensagem("erro"), Lingua.getMensagem("excecao")+" "+e);
        }
    }//GEN-LAST:event_siteMouseClicked

    private void manualMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_manualMouseClicked
        Desktop desk = java.awt.Desktop.getDesktop();     
        try {    
             desk.browse(new java.net.URI("http://bgfinancas.sourceforge.net#manual"));    
        } catch (URISyntaxException e) {    
             Janelinha.Aviso(Lingua.getMensagem("erro"), Lingua.getMensagem("excecao")+" "+e);
        } catch (IOException e) {
            Janelinha.Aviso(Lingua.getMensagem("erro"), Lingua.getMensagem("excecao")+" "+e);
        }
    }//GEN-LAST:event_manualMouseClicked

    private void forumMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_forumMouseClicked
        Desktop desk = java.awt.Desktop.getDesktop();     
        try {    
            desk.browse(new java.net.URI("http://sourceforge.net/p/bgfinancas/forum"));    
        } catch (IOException e) {    
            Janelinha.Aviso(Lingua.getMensagem("erro"), Lingua.getMensagem("excecao")+" "+e);
        } catch (URISyntaxException e) {
            Janelinha.Aviso(Lingua.getMensagem("erro"), Lingua.getMensagem("excecao")+" "+e);
        }
    }//GEN-LAST:event_forumMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToolBar BarraDeFerramentas;
    // End of variables declaration//GEN-END:variables

}
