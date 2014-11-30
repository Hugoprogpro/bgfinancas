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

package Utilitarios;

import Biblioteca.Botoes;
import Biblioteca.Configuracoes;
import Biblioteca.JanelaModal;
import Biblioteca.Janelinha;
import Biblioteca.Visual;
import Cadastros.Usuarios;
import Linguagens.Linguagem;
import Principal.TelaPrincipal;
import java.sql.SQLException;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public final class Configuracao extends JanelaModal {

    private final TelaPrincipal Principal;
    private final Boolean PrimeiroCadastro;
    private final String[][] Idiomas;
    private final String[] lista_idiomas;
    private final String[] lista_temas;
    private String botao;
    
    public Configuracao(boolean modal, TelaPrincipal _Principal, Boolean _PrimeiroCadastro) {
        super(_Principal, modal);
        Principal = _Principal;
        PrimeiroCadastro = _PrimeiroCadastro;
        Idiomas = Linguagem.getIdiomas();
        lista_idiomas = Linguagem.getListaIdiomas();
        lista_temas = Configuracoes.getListaTemas(); 
        if(PrimeiroCadastro){
            botao = "Prosseguir / Next";
        }else{
            botao = "Alterar / Edit";
        }
        initComponents();
        Visual.JanelaModal(this.getJanela());
        BarraDeFerramentas.add(Botoes.Sair(this));
        PreencherFormulario();
    }

    public void PreencherFormulario()
    {
        try{
            Boolean achou = false;
            String Idioma = Configuracoes.getPropriedade("idioma");
            for(int i=0; i<Idiomas.length; i++){
                if(Idioma.equals(Idiomas[i][1])){
                    idioma.setSelectedItem(Idiomas[i][0]);
                    achou = true;
                }
            }
            moeda.setText(Configuracoes.getPropriedade("moeda"));
            tema.setSelectedItem(Configuracoes.getPropriedade("tema"));
            if(!achou){
                idioma.setSelectedItem(Idiomas[0][0]);
            }
        }catch(SQLException e){
            Janelinha.Aviso(Lingua.getMensagem("erro"), Lingua.getMensagem("excecao")+" "+e);
        }
    }
    
    public void Alterar()
    {   
        try{
            String Idioma = Idiomas[0][1];
            for(int i=0; i<Idiomas.length; i++){
                if(idioma.getSelectedItem().toString().equals(Idiomas[i][0])){
                    Idioma = Idiomas[i][1];
                }
            }
            Banco.executeUpdate("UPDATE configuracoes SET valor='"+Idioma+"' WHERE nome='idioma'");
            Banco.executeUpdate("UPDATE configuracoes SET valor='"+moeda.getText()+"' WHERE nome='moeda'");
            Banco.executeUpdate("UPDATE configuracoes SET valor='"+tema.getSelectedItem().toString()+"' WHERE nome='tema'");
            if(PrimeiroCadastro){
                Linguagem.getInstance().CarregarIdioma();
                Usuarios cadastro = new Usuarios(true,Principal,true);
                cadastro.PreencherFormulario(Lingua.getMensagem("cadastrar"), null);
                cadastro.setVisible(true);
                Configuracao.this.dispose();
            }else{
                Janelinha.Aviso(Lingua.getMensagem("alterar"),Lingua.getMensagem("sucesso"));
                Janelinha.Aviso(Lingua.getMensagem("atencao"), Lingua.getMensagem("sistema_sera_fechado"));
                System.exit(0);
            }
            PreencherFormulario();
        }catch(SQLException e){
            Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("excecao")+" "+e);
        }
    }
    
    @Override
    public void Botoes(String acao)
    {
        if(acao.equals(Lingua.getMensagem("sair"))){
            try {
                UIManager.setLookAndFeel("org.jvnet.substance.skin.Substance"+Configuracoes.getPropriedade("tema")+"LookAndFeel");
                SwingUtilities.updateComponentTreeUI( this );
            } catch (ClassNotFoundException ex) {
                Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("excecao")+" "+ex);
            } catch (InstantiationException ex) {
                Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("excecao")+" "+ex);
            } catch (IllegalAccessException ex) {
                Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("excecao")+" "+ex);
            } catch (UnsupportedLookAndFeelException ex) {
                Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("excecao")+" "+ex);
            } catch (SQLException ex) {
                Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("excecao")+" "+ex);
            }
            Configuracao.this.dispose();
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        BarraDeFerramentas = new javax.swing.JToolBar();
        PainelUsuarios = new javax.swing.JPanel();
        BFinalizar = new javax.swing.JButton();
        NOME = new javax.swing.JLabel();
        idioma = new javax.swing.JComboBox();
        moeda = new javax.swing.JTextField();
        NOME1 = new javax.swing.JLabel();
        NOME3 = new javax.swing.JLabel();
        NOME4 = new javax.swing.JLabel();
        tema = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Configuracao / Configuration");
        setFont(FonteFinancas);
        setForeground(java.awt.Color.cyan);
        setResizable(false);
        getContentPane().setLayout(null);

        BarraDeFerramentas.setFloatable(false);
        BarraDeFerramentas.setRollover(true);
        getContentPane().add(BarraDeFerramentas);
        BarraDeFerramentas.setBounds(0, 0, 580, 45);

        PainelUsuarios.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Preferencias / Preferences", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, FonteFinancas, new java.awt.Color(0, 102, 204)));
        PainelUsuarios.setForeground(new java.awt.Color(51, 94, 168));
        PainelUsuarios.setLayout(null);

        BFinalizar.setFont(FonteFinancas);
        BFinalizar.setText(botao);
        BFinalizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BFinalizarActionPerformed(evt);
            }
        });
        PainelUsuarios.add(BFinalizar);
        BFinalizar.setBounds(160, 140, 150, 20);

        NOME.setFont(FonteFinancas);
        NOME.setForeground(new java.awt.Color(51, 94, 168));
        NOME.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        NOME.setText("R$ / $");
        PainelUsuarios.add(NOME);
        NOME.setBounds(210, 80, 80, 20);

        idioma.setFont(FonteFinancas);
        idioma.setModel(new javax.swing.DefaultComboBoxModel(lista_idiomas));
        PainelUsuarios.add(idioma);
        idioma.setBounds(160, 50, 230, 20);

        moeda.setFont(FonteFinancas);
        PainelUsuarios.add(moeda);
        moeda.setBounds(160, 80, 40, 20);

        NOME1.setFont(FonteFinancas);
        NOME1.setForeground(new java.awt.Color(51, 94, 168));
        NOME1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        NOME1.setText("Idioma / Language:");
        PainelUsuarios.add(NOME1);
        NOME1.setBounds(10, 50, 140, 20);

        NOME3.setFont(FonteFinancas);
        NOME3.setForeground(new java.awt.Color(51, 94, 168));
        NOME3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        NOME3.setText("Tema / Theme:");
        PainelUsuarios.add(NOME3);
        NOME3.setBounds(10, 110, 140, 20);

        NOME4.setFont(FonteFinancas);
        NOME4.setForeground(new java.awt.Color(51, 94, 168));
        NOME4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        NOME4.setText("Moeda / Currency:");
        PainelUsuarios.add(NOME4);
        NOME4.setBounds(10, 80, 140, 20);

        tema.setFont(FonteFinancas);
        tema.setModel(new javax.swing.DefaultComboBoxModel(lista_temas));
        tema.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                temaActionPerformed(evt);
            }
        });
        PainelUsuarios.add(tema);
        tema.setBounds(160, 110, 230, 20);

        getContentPane().add(PainelUsuarios);
        PainelUsuarios.setBounds(20, 60, 410, 190);

        setSize(new java.awt.Dimension(453, 290));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

private void BFinalizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BFinalizarActionPerformed
    if(PrimeiroCadastro){
        Alterar();
    }else if(Janelinha.Pergunta(Lingua.getMensagem("alterar"),Lingua.getMensagem("tem_certeza")))
    {
        Alterar();
    }
}//GEN-LAST:event_BFinalizarActionPerformed

    private void temaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_temaActionPerformed
        try {
            UIManager.setLookAndFeel("org.jvnet.substance.skin.Substance"+tema.getSelectedItem().toString()+"LookAndFeel");
            SwingUtilities.updateComponentTreeUI( this );
        } catch (ClassNotFoundException ex) {
            Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("excecao")+" "+ex);
        } catch (InstantiationException ex) {
            Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("excecao")+" "+ex);
        } catch (IllegalAccessException ex) {
            Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("excecao")+" "+ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("excecao")+" "+ex);
        }
    }//GEN-LAST:event_temaActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BFinalizar;
    private javax.swing.JToolBar BarraDeFerramentas;
    private javax.swing.JLabel NOME;
    private javax.swing.JLabel NOME1;
    private javax.swing.JLabel NOME3;
    private javax.swing.JLabel NOME4;
    private javax.swing.JPanel PainelUsuarios;
    private javax.swing.JComboBox idioma;
    private javax.swing.JTextField moeda;
    private javax.swing.JComboBox tema;
    // End of variables declaration//GEN-END:variables

}
