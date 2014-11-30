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

package PreRelatorios;

import Biblioteca.Botoes;
import Biblioteca.Calendario;

import Biblioteca.JanelaModal;
import Biblioteca.Janelinha;
import Biblioteca.Visual;
import Relatorios.RDespesasCategoriaTempo;
import java.sql.SQLException;
import java.text.ParseException;
import javax.swing.table.DefaultTableModel;

public final class PreDespesasCategoriaTempo extends JanelaModal {

    private final RDespesasCategoriaTempo Relatorio = new RDespesasCategoriaTempo();
    private final DefaultTableModel CATEGORIAS,CATEGORIAS_FINAL;

    public PreDespesasCategoriaTempo(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        Visual.JanelaModal(this.getJanela());
        BarraDeFerramentas.add(Botoes.Sair(this));
        CATEGORIAS = (DefaultTableModel)categorias.getModel();
        CATEGORIAS_FINAL = (DefaultTableModel)categorias_final.getModel();
        PreencherCategorias();
        categorias.setFont(FonteFinancas);
        categorias.getTableHeader().setFont(FonteFinancas);
        categorias_final.setFont(FonteFinancas);
        categorias_final.getTableHeader().setFont(FonteFinancas);
    }

    public void PreencherCategorias()
    {
        try
        {
            Banco.executeQuery("SELECT nome,id_categoria FROM despesas_categorias ORDER BY nome ASC");
            CATEGORIAS.setNumRows(0);
            while(Banco.getResultSet().next())
            {
                CATEGORIAS.addRow(new Object[]{Banco.getResultSet().getString("id_categoria"),Banco.getResultSet().getString("nome")});
            }
        }catch(SQLException e)
        {
            Janelinha.Aviso(Lingua.getMensagem("erro"), Lingua.getMensagem("excecao")+" "+e.getMessage());
        }
    }

    public void Adicionar()
    {
        if(categorias.getSelectedRow()<0)
        {
            Janelinha.Aviso(Lingua.getMensagem("erro"), Lingua.getMensagem("restricao_adicionar_item"));
        }else{
            CATEGORIAS_FINAL.addRow(new Object[]{categorias.getValueAt(categorias.getSelectedRow(),0),categorias.getValueAt(categorias.getSelectedRow(),1)});
            CATEGORIAS.removeRow(categorias.getSelectedRow());
        }
    }

    public void Tudo()
    {
        if(categorias.getRowCount()>0){
            do{
                CATEGORIAS_FINAL.addRow(new Object[]{categorias.getValueAt(0,0),categorias.getValueAt(0,1)});
                CATEGORIAS.removeRow(0);
            }while(categorias.getRowCount()>0);
        }
    }

    public void Remover()
    {
        if(categorias_final.getSelectedRow()<0)
        {
            Janelinha.Aviso(Lingua.getMensagem("erro"), Lingua.getMensagem("restricao_remover_item"));
        }else{
            CATEGORIAS.addRow(new Object[]{categorias_final.getValueAt(categorias_final.getSelectedRow(),0),categorias_final.getValueAt(categorias_final.getSelectedRow(),1)});
            CATEGORIAS_FINAL.removeRow(categorias_final.getSelectedRow());
        }
    }

    @Override
    public void Botoes(String acao)
    {
        if(acao.equals(Lingua.getMensagem("sair")))
        {
            PreDespesasCategoriaTempo.this.dispose();
        }
    }

    public String Categoria() throws ParseException
    {
        int i;
        String SQL="";
        for(i=1;i<=categorias_final.getRowCount();i++)
        {
            if(i==1)
            {
                SQL = SQL.concat("'"+(String) categorias_final.getValueAt(i-1,0)+"'");
            }else{
                SQL = SQL.concat(" OR despesas_categorias.id_categoria='"+(String) categorias_final.getValueAt(i-1,0)+"'");
            }

        }
        return SQL;
    }

    public void GeralES() throws ParseException
    {
        String DataInicial = data_inicial.getSelectedItem().toString();
        String tmp[] = DataInicial.split("/");
        DataInicial = tmp[2]+"-"+tmp[1]+"-"+tmp[0];
        String DataFinal = data_final.getSelectedItem().toString();
        String tmp2[] = DataFinal.split("/");
        DataFinal = tmp2[2]+"-"+tmp2[1]+"-"+tmp2[0];
        Relatorio.RDespesasCategoriaTempo(DataInicial,DataFinal,Categoria(),tipo.getSelectedItem().toString(),tipo.getSelectedIndex());
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
        INICIO = new javax.swing.JLabel();
        BFiltrar = new javax.swing.JButton();
        FINAL = new javax.swing.JLabel();
        TEXTO = new javax.swing.JLabel();
        TEXTO2 = new javax.swing.JLabel();
        TEXTO1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        categorias_final = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        categorias = new javax.swing.JTable();
        adicionar = new javax.swing.JButton();
        remover = new javax.swing.JButton();
        data_inicial = new Calendario(true);
        data_final = new Calendario(true);
        FINAL1 = new javax.swing.JLabel();
        tipo = new javax.swing.JComboBox();
        tudo = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(Lingua.getMensagem("despesas")+" > "+Lingua.getMensagem("periodo_categoria_tempo"));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        getContentPane().setLayout(null);

        BarraDeFerramentas.setFloatable(false);
        BarraDeFerramentas.setRollover(true);
        getContentPane().add(BarraDeFerramentas);
        BarraDeFerramentas.setBounds(0, 0, 570, 41);

        INICIO.setFont(FonteFinancas);
        INICIO.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        INICIO.setText(Lingua.getMensagem("inicio")+":");
        getContentPane().add(INICIO);
        INICIO.setBounds(40, 270, 70, 20);

        BFiltrar.setFont(FonteFinancas);
        BFiltrar.setText(Lingua.getMensagem("gerar"));
        BFiltrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BFiltrarActionPerformed(evt);
            }
        });
        getContentPane().add(BFiltrar);
        BFiltrar.setBounds(340, 300, 120, 20);

        FINAL.setFont(FonteFinancas);
        FINAL.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        FINAL.setText(Lingua.getMensagem("fim")+":");
        getContentPane().add(FINAL);
        FINAL.setBounds(40, 300, 70, 20);

        TEXTO.setFont(FonteFinancasPequena);
        TEXTO.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TEXTO.setText(Lingua.getMensagem("mensagem_relatorio_categorias")+":");
        getContentPane().add(TEXTO);
        TEXTO.setBounds(0, 56, 530, 20);

        TEXTO2.setFont(FonteFinancas);
        TEXTO2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TEXTO2.setText(Lingua.getMensagem("categorias_disponiveis"));
        getContentPane().add(TEXTO2);
        TEXTO2.setBounds(40, 90, 190, 20);

        TEXTO1.setFont(FonteFinancas);
        TEXTO1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TEXTO1.setText(Lingua.getMensagem("categorias_selecionadas"));
        getContentPane().add(TEXTO1);
        TEXTO1.setBounds(290, 90, 190, 20);

        categorias_final.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(categorias_final);
        if (categorias_final.getColumnModel().getColumnCount() > 0) {
            categorias_final.getColumnModel().getColumn(1).setHeaderValue(Lingua.getMensagem("nome"));
        }
        categorias_final.getColumnModel().getColumn(0).setMinWidth(0);
        categorias_final.getColumnModel().getColumn(0).setMaxWidth(0);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(290, 110, 190, 140);

        categorias.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(categorias);
        if (categorias.getColumnModel().getColumnCount() > 0) {
            categorias.getColumnModel().getColumn(1).setHeaderValue(Lingua.getMensagem("nome"));
        }
        categorias.getColumnModel().getColumn(0).setMinWidth(0);
        categorias.getColumnModel().getColumn(0).setMaxWidth(0);

        getContentPane().add(jScrollPane2);
        jScrollPane2.setBounds(40, 110, 190, 140);

        adicionar.setFont(FonteFinancas);
        adicionar.setText(">>");
        adicionar.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        adicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adicionarActionPerformed(evt);
            }
        });
        getContentPane().add(adicionar);
        adicionar.setBounds(240, 130, 40, 30);

        remover.setFont(FonteFinancas);
        remover.setText("<<");
        remover.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        remover.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removerActionPerformed(evt);
            }
        });
        getContentPane().add(remover);
        remover.setBounds(240, 170, 40, 30);

        data_inicial.setFont(FonteFinancas);
        getContentPane().add(data_inicial);
        data_inicial.setBounds(120, 270, 110, 20);

        data_final.setFont(FonteFinancas);
        getContentPane().add(data_final);
        data_final.setBounds(120, 300, 110, 20);

        FINAL1.setFont(FonteFinancas);
        FINAL1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        FINAL1.setText(Lingua.getMensagem("tipo")+":");
        getContentPane().add(FINAL1);
        FINAL1.setBounds(240, 270, 90, 20);

        tipo.setFont(FonteFinancas);
        tipo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { Lingua.getMensagem("barras"), Lingua.getMensagem("linhas") }));
        getContentPane().add(tipo);
        tipo.setBounds(340, 270, 120, 20);

        tudo.setFont(FonteFinancas);
        tudo.setText(Lingua.getMensagem("todas"));
        tudo.setToolTipText(Lingua.getMensagem("todas"));
        tudo.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        tudo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tudoActionPerformed(evt);
            }
        });
        getContentPane().add(tudo);
        tudo.setBounds(240, 210, 40, 30);

        setSize(new java.awt.Dimension(535, 378));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void BFiltrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BFiltrarActionPerformed
        if(categorias_final.getRowCount()>0)
        {
            try {
                GeralES();
            } catch (ParseException ex) {
                Janelinha.Aviso((Lingua.getMensagem("erro")), Lingua.getMensagem("excecao")+" "+ex.getMessage());
            }
        }else{
            Janelinha.Aviso(Lingua.getMensagem("erro"), Lingua.getMensagem("restricao_relatorio"));
        }
}//GEN-LAST:event_BFiltrarActionPerformed

    private void adicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adicionarActionPerformed
        Adicionar();
}//GEN-LAST:event_adicionarActionPerformed

    private void removerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removerActionPerformed
        Remover();
}//GEN-LAST:event_removerActionPerformed

    private void tudoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tudoActionPerformed
        Tudo();
    }//GEN-LAST:event_tudoActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BFiltrar;
    private javax.swing.JToolBar BarraDeFerramentas;
    private javax.swing.JLabel FINAL;
    private javax.swing.JLabel FINAL1;
    private javax.swing.JLabel INICIO;
    private javax.swing.JLabel TEXTO;
    private javax.swing.JLabel TEXTO1;
    private javax.swing.JLabel TEXTO2;
    private javax.swing.JButton adicionar;
    private javax.swing.JTable categorias;
    private javax.swing.JTable categorias_final;
    private javax.swing.JComboBox data_final;
    private javax.swing.JComboBox data_inicial;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton remover;
    private javax.swing.JComboBox tipo;
    private javax.swing.JButton tudo;
    // End of variables declaration//GEN-END:variables

}
