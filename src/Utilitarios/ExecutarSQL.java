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

import Biblioteca.Janela;
import Biblioteca.Janelinha;
import Biblioteca.Visual;
import Principal.TelaPrincipal;
import java.sql.SQLException;

public final class ExecutarSQL extends Janela {

    private final TelaPrincipal Principal;
    private final String Logado;
    
    public ExecutarSQL(TelaPrincipal principal) {
        initComponents();
        Visual.Janela(this.getJanela());
        BarraDeFerramentas.add(Botoes.Sair(this));
        Principal = principal;
        Logado = Principal.getLogado();
    }

    public boolean ValidarFormulario()
    {
        String[] linhas = comandos_sql.getText().split("\\n");
        for(int i=0; i<linhas.length; i++){
            if(!(linhas[i].contains("INSERT") || linhas[i].contains("UPDATE") || linhas[i].contains("DELETE") || linhas[i].contains("insert") || linhas[i].contains("update") || linhas[i].contains("delete"))){
                Janelinha.Aviso(Lingua.getMensagem("erro"), Lingua.getMensagem("comando_invalido")+": "+linhas[i]);
                return false;
            }
        }
        return true;        
    }
    
    public void Executar()
    {   
            String[] linhas = comandos_sql.getText().split("\\n");
            for(int i=0; i<linhas.length; i++){
                if(Banco.executeUpdate(linhas[i])==0){
                    Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("comando_invalido")+": "+linhas[i]);
                }
            }            
            Janelinha.Aviso(Lingua.getMensagem("alterar"),Lingua.getMensagem("sucesso"));
            ExecutarSQL.this.dispose();
    }
    
    @Override
    public void Botoes(String acao)
    {
        if(acao.equals(Lingua.getMensagem("sair"))){
            ExecutarSQL.this.dispose();
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
        PanelSenha = new javax.swing.JPanel();
        SENHA = new javax.swing.JLabel();
        BFinalizar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        comandos_sql = new javax.swing.JTextArea();
        SENHA1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(Lingua.getMensagem("executar_sql"));
        setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        setForeground(java.awt.Color.cyan);
        getContentPane().setLayout(null);

        BarraDeFerramentas.setFloatable(false);
        BarraDeFerramentas.setRollover(true);
        getContentPane().add(BarraDeFerramentas);
        BarraDeFerramentas.setBounds(0, 0, 580, 41);

        PanelSenha.setBorder(javax.swing.BorderFactory.createTitledBorder(null, Lingua.getMensagem("comando_sql")
            , javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, FonteFinancas, new java.awt.Color(0, 102, 204)));
    PanelSenha.setForeground(new java.awt.Color(51, 94, 168));
    PanelSenha.setLayout(null);

    SENHA.setFont(FonteFinancas);
    SENHA.setForeground(new java.awt.Color(51, 94, 168));
    SENHA.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    SENHA.setText(Lingua.getMensagem("ben_parker"));
    SENHA.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    PanelSenha.add(SENHA);
    SENHA.setBounds(20, 170, 480, 20);

    BFinalizar.setFont(FonteFinancas);
    BFinalizar.setText(Lingua.getMensagem("executar"));
    BFinalizar.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            BFinalizarActionPerformed(evt);
        }
    });
    PanelSenha.add(BFinalizar);
    BFinalizar.setBounds(380, 200, 120, 20);

    comandos_sql.setColumns(20);
    comandos_sql.setFont(FonteFinancas);
    comandos_sql.setLineWrap(true);
    comandos_sql.setRows(5);
    jScrollPane1.setViewportView(comandos_sql);

    PanelSenha.add(jScrollPane1);
    jScrollPane1.setBounds(20, 60, 480, 100);

    SENHA1.setFont(FonteFinancas);
    SENHA1.setForeground(new java.awt.Color(51, 94, 168));
    SENHA1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    SENHA1.setText(Lingua.getMensagem("instrucao_sql"));
    SENHA1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    PanelSenha.add(SENHA1);
    SENHA1.setBounds(20, 30, 480, 20);

    getContentPane().add(PanelSenha);
    PanelSenha.setBounds(20, 60, 520, 240);

    setSize(new java.awt.Dimension(565, 345));
    setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

private void BFinalizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BFinalizarActionPerformed
        if(ValidarFormulario() && Janelinha.Pergunta(Lingua.getMensagem("executar"),Lingua.getMensagem("tem_certeza")))
        {
            Executar();
        }
}//GEN-LAST:event_BFinalizarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BFinalizar;
    private javax.swing.JToolBar BarraDeFerramentas;
    private javax.swing.JPanel PanelSenha;
    private javax.swing.JLabel SENHA;
    private javax.swing.JLabel SENHA1;
    private javax.swing.JTextArea comandos_sql;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

}
