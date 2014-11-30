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
import Principal.TelaPrincipal;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;

public final class GruposItens extends Janela {
    
    private final TelaPrincipal Principal;
    
    public GruposItens(TelaPrincipal _Principal) {
        initComponents();
        Principal = _Principal;
        Visual.Janela(this.getJanela());
        BarraDeFerramentas.add(Botoes.Sair(this));
        PreencherCategorias();
        lista_itens.setFont(FonteFinancas);
        lista_itens.getTableHeader().setFont(FonteFinancas);
    }

    public void PreencherCategorias()
    {
        categoria.addItem(Lingua.getMensagem("selecione"));
        try
        {
            Banco.executeQuery("SELECT nome FROM relatorios_grupos ORDER BY nome ASC");
            while(Banco.getResultSet().next())
            {
                categoria.addItem(Banco.getResultSet().getString("nome"));
            }
        }catch(Exception e)
        {
            categoria.addItem(Lingua.getMensagem("nenhum_item_encontrado"));
        }
    }

    public void AdicionarItem(String id_item, String nome)
    {
        DefaultTableModel modelo = (DefaultTableModel)lista_itens.getModel();
        modelo.addRow(new Object[]{id_item,nome});
    }

    public void RemoverItem()
    {
        if(lista_itens.getSelectedRow()==-1)
        {
            Janelinha.Aviso(Lingua.getMensagem("atencao"),Lingua.getMensagem("restricao_remover_item"));
        }else{
            DefaultTableModel modelo = (DefaultTableModel)lista_itens.getModel();
            modelo.removeRow(lista_itens.getSelectedRow());
        }
    }

    public void PreencherFormulario()
    {
        String id_relatorios_grupos="";
        try{
            Banco.executeQuery("SELECT id_relatorios_grupos FROM relatorios_grupos WHERE nome='"+categoria.getSelectedItem()+"'");
            Banco.getResultSet().next();
            id_relatorios_grupos = Banco.getResultSet().getString("id_relatorios_grupos");
        }catch(Exception e){
            Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("excecao")+" "+e);
        }
        DefaultTableModel modelo = (DefaultTableModel)lista_itens.getModel();
        modelo.setNumRows(0);
        Banco.executeQuery("SELECT relatorios_grupos_itens.*, despesas_categorias.nome FROM relatorios_grupos_itens, despesas_categorias WHERE relatorios_grupos_itens.id_relatorios_grupos='"+id_relatorios_grupos+"' AND relatorios_grupos_itens.id_despesas_categorias=despesas_categorias.id_categoria ORDER BY nome ASC");
        try
        {
            while(Banco.getResultSet().next())
            {
                modelo.addRow(new Object[]{Banco.getResultSet().getString("id_despesas_categorias"),Banco.getResultSet().getString("nome")});
            }
        }catch(Exception e)
        {
            Janelinha.Aviso(Lingua.getMensagem("atencao"),Lingua.getMensagem("excecao")+" "+e.getMessage());
        }
    }
    
    public void CadastrarAlterar()
    {
        String id_relatorios_grupos="";
        try{
            Banco.executeQuery("SELECT id_relatorios_grupos FROM relatorios_grupos WHERE nome='"+categoria.getSelectedItem()+"'");
            Banco.getResultSet().next();
            id_relatorios_grupos = Banco.getResultSet().getString("id_relatorios_grupos");
        }catch(Exception e){
            Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("excecao")+" "+e);
        }
        // items
        try{
            Banco.executeUpdate("DELETE FROM relatorios_grupos_itens WHERE id_relatorios_grupos='"+id_relatorios_grupos+"'");
            int Quantidade_Itens,i;
            Quantidade_Itens = lista_itens.getRowCount();
            for(i=0;i<Quantidade_Itens;i++)
            {
                Banco.executeUpdate("INSERT INTO relatorios_grupos_itens (id_relatorios_grupos,id_despesas_categorias) VALUES('"+id_relatorios_grupos+"','"+(String)lista_itens.getValueAt(i,0)+"')");
            }
            Janelinha.Aviso(Lingua.getMensagem("cadastrar"),Lingua.getMensagem("sucesso"));
            Principal.RelatorioMensalGrupos();
            categoria.setSelectedIndex(0);
            DefaultTableModel modelo = (DefaultTableModel)lista_itens.getModel();
            modelo.setNumRows(0);
        }catch(Exception e){
            Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("excecao")+" "+e.getMessage());
        }
    }
    
    @Override
    public void Botoes(String acao)
    {
        if(acao.equals(Lingua.getMensagem("sair"))){
            GruposItens.this.dispose();
        }else if(acao.equals("ItensPesquisar")){
            new GruposItensPesquisar(this).setVisible(true);
        }
    }
    
    public boolean ValidarFormulario()
    {
        if(categoria.getSelectedItem().equals(Lingua.getMensagem("selecione")))
        {
            Janelinha.Aviso(Lingua.getMensagem("atencao"),Lingua.getMensagem("campo_nao_informado")+" "+Lingua.getMensagem("grupo"));
            categoria.requestFocus(true);
            return false;
        }else{
            return true;
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

        jRadioButton1 = new javax.swing.JRadioButton();
        BarraDeFerramentas = new javax.swing.JToolBar();
        JPanelProduto = new javax.swing.JPanel();
        Produtos = new javax.swing.JScrollPane();
        lista_itens = new javax.swing.JTable();
        BProdutoAdd = new javax.swing.JButton();
        BProdutoDel = new javax.swing.JButton();
        CIDADE = new javax.swing.JLabel();
        categoria = new javax.swing.JComboBox();
        BFinalizar = new javax.swing.JButton();

        jRadioButton1.setText("jRadioButton1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(Lingua.getMensagem("grupos")+" > "+Lingua.getMensagem("itens"));
        setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        setForeground(java.awt.Color.cyan);
        getContentPane().setLayout(null);

        BarraDeFerramentas.setFloatable(false);
        BarraDeFerramentas.setRollover(true);
        getContentPane().add(BarraDeFerramentas);
        BarraDeFerramentas.setBounds(0, 0, 730, 41);

        JPanelProduto.setBorder(javax.swing.BorderFactory.createTitledBorder(null, Lingua.getMensagem("lista_categorias_despesa"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, FonteFinancas, new java.awt.Color(0, 102, 204)));
        JPanelProduto.setForeground(new java.awt.Color(51, 94, 168));
        JPanelProduto.setLayout(null);

        lista_itens.setModel(new javax.swing.table.DefaultTableModel(
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
        lista_itens.getTableHeader().setReorderingAllowed(false);
        Produtos.setViewportView(lista_itens);
        if (lista_itens.getColumnModel().getColumnCount() > 0) {
            lista_itens.getColumnModel().getColumn(0).setMaxWidth(50);
            lista_itens.getColumnModel().getColumn(1).setResizable(false);
            lista_itens.getColumnModel().getColumn(1).setHeaderValue(Lingua.getMensagem("nome"));
        }

        JPanelProduto.add(Produtos);
        Produtos.setBounds(20, 50, 570, 270);

        BProdutoAdd.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        BProdutoAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagens/outros/adicionar.png"))); // NOI18N
        BProdutoAdd.setText(Lingua.getMensagem("adicionar_item"));
        BProdutoAdd.setToolTipText(Lingua.getMensagem("adicionar_item"));
        BProdutoAdd.setBorder(null);
        BProdutoAdd.setBorderPainted(false);
        BProdutoAdd.setContentAreaFilled(false);
        BProdutoAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BProdutoAddActionPerformed(evt);
            }
        });
        JPanelProduto.add(BProdutoAdd);
        BProdutoAdd.setBounds(10, 320, 140, 30);
        BProdutoAdd.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "A");
        BProdutoAdd.getActionMap().put("A", new AbstractAction() {
            public void actionPerformed(ActionEvent A) {
                Botoes("ItensPesquisar");
            }
        });

        BProdutoDel.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        BProdutoDel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagens/outros/remover.png"))); // NOI18N
        BProdutoDel.setText(Lingua.getMensagem("remover_item"));
        BProdutoDel.setToolTipText(Lingua.getMensagem("remover_item"));
        BProdutoDel.setBorder(null);
        BProdutoDel.setBorderPainted(false);
        BProdutoDel.setContentAreaFilled(false);
        BProdutoDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BProdutoDelActionPerformed(evt);
            }
        });
        JPanelProduto.add(BProdutoDel);
        BProdutoDel.setBounds(150, 320, 130, 30);
        BProdutoDel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), "R");
        BProdutoDel.getActionMap().put("R", new AbstractAction() {
            public void actionPerformed(ActionEvent R) {
                RemoverItem();
            }
        });

        CIDADE.setFont(FonteFinancas);
        CIDADE.setForeground(new java.awt.Color(51, 94, 168));
        CIDADE.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        CIDADE.setText(Lingua.getMensagem("grupo")+":");
        JPanelProduto.add(CIDADE);
        CIDADE.setBounds(10, 20, 50, 20);

        categoria.setFont(FonteFinancas);
        categoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                categoriaActionPerformed(evt);
            }
        });
        JPanelProduto.add(categoria);
        categoria.setBounds(70, 20, 400, 20);

        BFinalizar.setFont(FonteFinancas);
        BFinalizar.setText(Lingua.getMensagem("atualizar"));
        BFinalizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BFinalizarActionPerformed(evt);
            }
        });
        JPanelProduto.add(BFinalizar);
        BFinalizar.setBounds(480, 20, 110, 20);

        getContentPane().add(JPanelProduto);
        JPanelProduto.setBounds(20, 60, 612, 365);

        setSize(new java.awt.Dimension(658, 466));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

private void BFinalizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BFinalizarActionPerformed
        if(ValidarFormulario() && Janelinha.Pergunta(Lingua.getMensagem("atualizar"),Lingua.getMensagem("tem_certeza")))
        {
            CadastrarAlterar();
        }
}//GEN-LAST:event_BFinalizarActionPerformed

private void BProdutoAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BProdutoAddActionPerformed
    Botoes("ItensPesquisar");
}//GEN-LAST:event_BProdutoAddActionPerformed

private void BProdutoDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BProdutoDelActionPerformed
    RemoverItem();
}//GEN-LAST:event_BProdutoDelActionPerformed

    private void categoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_categoriaActionPerformed
        if(!categoria.getSelectedItem().equals(Lingua.getMensagem("selecione"))){
            PreencherFormulario();
        }
    }//GEN-LAST:event_categoriaActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BFinalizar;
    private javax.swing.JButton BProdutoAdd;
    private javax.swing.JButton BProdutoDel;
    private javax.swing.JToolBar BarraDeFerramentas;
    private javax.swing.JLabel CIDADE;
    private javax.swing.JPanel JPanelProduto;
    private javax.swing.JScrollPane Produtos;
    private javax.swing.JComboBox categoria;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JTable lista_itens;
    // End of variables declaration//GEN-END:variables

}
