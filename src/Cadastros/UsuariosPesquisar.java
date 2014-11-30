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

package Cadastros;

import Biblioteca.Botoes;
import Biblioteca.Janela;
import Biblioteca.Janelinha;
import Biblioteca.Visual;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

public class UsuariosPesquisar extends Janela {

    private final String Acao;
    private final Usuarios Usuario;
    
    public UsuariosPesquisar(Usuarios usuario, String acao) {
        initComponents();
        Visual.Janela(this.getJanela());
        BarraDeFerramentas.add(Botoes.Sair(this));
        Acao = acao;
        Usuario = usuario;
        resultado_pesquisa.setFont(FonteFinancas);
        resultado_pesquisa.getTableHeader().setFont(FonteFinancas);
    }
      
    public void Filtrar()
    {
        try
        {
            Banco.executeQuery("SELECT id_usuarios, nome, usuario FROM usuarios WHERE nome LIKE '%"+nome.getText()+"%' OR usuario LIKE '%"+nome.getText()+"%' ORDER BY nome ASC");
            DefaultTableModel modelo = (DefaultTableModel)resultado_pesquisa.getModel();
            modelo.setNumRows(0);
            while(Banco.getResultSet().next())
            {
                modelo.addRow(new Object[]{Banco.getResultSet().getString("id_usuarios"),Banco.getResultSet().getString("nome"),Banco.getResultSet().getString("usuario")});
            }
        }catch(SQLException e)
        {
            Janelinha.Aviso(Lingua.getMensagem("atencao"),Lingua.getMensagem("excecao")+" "+e.getMessage());
        }
    }
    
    @Override
    public void Botoes(String acao)
    {
        if(acao.equals(Lingua.getMensagem("sair"))){
            UsuariosPesquisar.this.dispose();
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        BarraDeFerramentas = new javax.swing.JToolBar();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        resultado_pesquisa = new javax.swing.JTable();
        CEP = new javax.swing.JLabel();
        nome = new javax.swing.JTextField();
        BFiltrar = new javax.swing.JButton();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(Lingua.getMensagem("pesquisar"));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        BarraDeFerramentas.setFloatable(false);
        BarraDeFerramentas.setRollover(true);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, Lingua.getMensagem("usuarios"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, FonteFinancas, new java.awt.Color(0, 102, 204)));
        jPanel1.setForeground(new java.awt.Color(51, 94, 168));

        resultado_pesquisa.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        resultado_pesquisa.getTableHeader().setReorderingAllowed(false);
        resultado_pesquisa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                resultado_pesquisaMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(resultado_pesquisa);
        if (resultado_pesquisa.getColumnModel().getColumnCount() > 0) {
            resultado_pesquisa.getColumnModel().getColumn(0).setResizable(false);
            resultado_pesquisa.getColumnModel().getColumn(1).setHeaderValue(Lingua.getMensagem("nome"));
            resultado_pesquisa.getColumnModel().getColumn(2).setHeaderValue(Lingua.getMensagem("usuario"));
        }

        CEP.setFont(FonteFinancas);
        CEP.setText(Lingua.getMensagem("nome")+":");

        nome.setFont(FonteFinancas);
        nome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nomeActionPerformed(evt);
            }
        });

        BFiltrar.setFont(FonteFinancas);
        BFiltrar.setText(Lingua.getMensagem("pesquisar"));
        BFiltrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BFiltrarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 572, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(CEP)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nome, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(BFiltrar)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nome, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(CEP)
                        .addComponent(BFiltrar, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BarraDeFerramentas, javax.swing.GroupLayout.DEFAULT_SIZE, 624, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(BarraDeFerramentas, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        setSize(new java.awt.Dimension(640, 480));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

private void nomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nomeActionPerformed
    Filtrar();
}//GEN-LAST:event_nomeActionPerformed

private void BFiltrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BFiltrarActionPerformed
    Filtrar();
}//GEN-LAST:event_BFiltrarActionPerformed

private void resultado_pesquisaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_resultado_pesquisaMouseClicked
    String codSelecionado0;
    String codSelecionado1;
    String codSelecionado2;
    codSelecionado0 = (String)resultado_pesquisa.getValueAt(resultado_pesquisa.getSelectedRow(), 0);
    codSelecionado1 = (String)resultado_pesquisa.getValueAt(resultado_pesquisa.getSelectedRow(), 1);
    codSelecionado2 = (String)resultado_pesquisa.getValueAt(resultado_pesquisa.getSelectedRow(), 2);
    if(Janelinha.Pergunta(codSelecionado1, Lingua.getMensagem("tem_certeza"))){
        Usuario.PreencherFormulario(Acao,codSelecionado0);
        UsuariosPesquisar.this.dispose();
    }
}//GEN-LAST:event_resultado_pesquisaMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BFiltrar;
    private javax.swing.JToolBar BarraDeFerramentas;
    private javax.swing.JLabel CEP;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField nome;
    private javax.swing.JTable resultado_pesquisa;
    // End of variables declaration//GEN-END:variables

}
