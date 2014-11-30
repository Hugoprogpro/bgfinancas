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
import Biblioteca.Numeros;
import Biblioteca.Visual;
import Principal.TelaPrincipal;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;

public final class PlanejamentoComponentes extends Janela {

    private final float ValorInicial;
    private final String id_planejamento,Mes,Ano;
    private final TelaPrincipal Principal;
    private final Planejamento Planejamento;

    public PlanejamentoComponentes(TelaPrincipal _Principal, Planejamento _Planejamento, String _id_planejamento, String _ValorInicial, String _Mes, String _Ano) {
        initComponents();
        Visual.Janela(this.getJanela());
        Mes = _Mes;
        Ano = _Ano;
        Principal = _Principal;
        ValorInicial = Float.parseFloat(_ValorInicial);
        Planejamento = _Planejamento;
        id_planejamento = _id_planejamento;
        BarraDeFerramentas.add(Botoes.Sair(this));
        valor_inicial.setText(_ValorInicial);
        PreencherFormulario();
        lista_itens.setFont(FonteFinancas);
        lista_itens.getTableHeader().setFont(FonteFinancas);
    }

    public void AdicionarItem(String id_item, String nome)
    {
        DefaultTableModel modelo = (DefaultTableModel)lista_itens.getModel();
        modelo.addRow(new Object[]{id_item,nome,"0.0"});
    }

    public void RemoverItem()
    {
        if(lista_itens.getSelectedRow()==-1)
        {
            Janelinha.Aviso(Lingua.getMensagem("atencao"),Lingua.getMensagem("restricao_remover_item"));
        }else{
            DefaultTableModel modelo = (DefaultTableModel)lista_itens.getModel();
            modelo.removeRow(lista_itens.getSelectedRow());
            CalcularValorTotal();
        }
    }

    public void CalcularValorTotal()
    {
        try{
            int quantidade_itens,i;
            Double Preco_total=0.0,Valor_Final=0.0;
            quantidade_itens = lista_itens.getRowCount();
            for(i=0;i<quantidade_itens;i++)
            {
                Preco_total+=Double.parseDouble((String)lista_itens.getValueAt(i,2));
            }
            Valor_Final = ValorInicial-Preco_total;
            valor_total.setText(Numeros.Arrendondar(Preco_total).toString());
            valor_final.setText(Numeros.Arrendondar(Valor_Final).toString());
        }catch(Exception e){
            Janelinha.Aviso(Lingua.getMensagem("erro"), Lingua.getMensagem("restricao_virgula"));
            CorrigirValores();
        }
    }
    
    public void CorrigirValores(){
        int quantidade_itens,i;
        quantidade_itens = lista_itens.getRowCount();
        for(i=0;i<quantidade_itens;i++)
        {
            String valor = lista_itens.getValueAt(i,2).toString();
            try { 
                Double.parseDouble(valor); 
                int pontos = valor.length() - valor.replace(".", "").length();
                lista_itens.setValueAt(valor.replace(",", "."), i, 2);
            } catch(NumberFormatException e) { 
                lista_itens.setValueAt("0.00", i, 2);
            }
        }
        CalcularValorTotal();
    }

    public void PreencherFormulario()
    {
        DefaultTableModel modelo = (DefaultTableModel)lista_itens.getModel();
        Banco.executeQuery("SELECT planejamento_componentes.id_item, planejamento_componentes.valor, planejamento_itens.nome FROM planejamento_componentes, planejamento_itens WHERE planejamento_componentes.id_planejamento='"+id_planejamento+"' AND planejamento_itens.id_item=planejamento_componentes.id_item ORDER BY planejamento_itens.nome ASC");
        try
        {
            while(Banco.getResultSet().next())
            {
                modelo.addRow(new Object[]{Banco.getResultSet().getString("id_item"),Banco.getResultSet().getString("nome"),Banco.getResultSet().getString("valor")});
            }
        }catch(Exception e)
        {
            Janelinha.Aviso(Lingua.getMensagem("atencao"),Lingua.getMensagem("excecao")+" "+e.getMessage());
        }
        CalcularValorTotal();
    }
    
    public void CadastrarAlterar()
    {
        // items
        try{
            Banco.executeUpdate("DELETE FROM planejamento_componentes WHERE id_planejamento='"+id_planejamento+"'");
            int Quantidade_Itens,i;
            Quantidade_Itens = lista_itens.getRowCount();
            for(i=0;i<Quantidade_Itens;i++)
            {
                Banco.executeUpdate("INSERT INTO planejamento_componentes VALUES('"+id_planejamento+"','"+(String)lista_itens.getValueAt(i,0)+"','"+(String)lista_itens.getValueAt(i,2)+"')");
            }
            Janelinha.Aviso(Lingua.getMensagem("cadastrar"),Lingua.getMensagem("sucesso"));
            Principal.SetarData(Mes, Ano);
            Principal.Planejamento(Mes,Ano);
            if(Planejamento != null)
            {
                Planejamento.PreencherFormulario(null, null, 0, null, null);
            }
            PlanejamentoComponentes.this.dispose();
        }catch(Exception e){
            Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("excecao")+" "+e.getMessage());
        }
    }
    
    @Override
    public void Botoes(String acao)
    {
        if(acao.equals(Lingua.getMensagem("sair"))){
            PlanejamentoComponentes.this.dispose();
        }else if(acao.equals("ItensPesquisar")){
            new PlanejamentoItensPesquisar(this).setVisible(true);
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
        JPanelProduto = new javax.swing.JPanel();
        Produtos = new javax.swing.JScrollPane();
        lista_itens = new javax.swing.JTable();
        BProdutoAdd = new javax.swing.JButton();
        valor_final = new javax.swing.JLabel();
        BProdutoDel = new javax.swing.JButton();
        CIFRAO = new javax.swing.JLabel();
        CIFRAO1 = new javax.swing.JLabel();
        valor_inicial = new javax.swing.JLabel();
        CIFRAO2 = new javax.swing.JLabel();
        valor_total = new javax.swing.JLabel();
        PRECO_TOTAL3 = new javax.swing.JLabel();
        PRECO_TOTAL4 = new javax.swing.JLabel();
        PRECO_TOTAL5 = new javax.swing.JLabel();
        BFinalizar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(Lingua.getMensagem("planejamento")+" > "+Lingua.getMensagem("plano")+" > "+Lingua.getMensagem("itens"));
        setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        setForeground(java.awt.Color.cyan);
        getContentPane().setLayout(null);

        BarraDeFerramentas.setFloatable(false);
        BarraDeFerramentas.setRollover(true);
        getContentPane().add(BarraDeFerramentas);
        BarraDeFerramentas.setBounds(0, 0, 790, 41);

        JPanelProduto.setBorder(javax.swing.BorderFactory.createTitledBorder(null, Lingua.getMensagem("lista_itens"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, FonteFinancas, new java.awt.Color(0, 102, 204)));
        JPanelProduto.setForeground(new java.awt.Color(51, 94, 168));
        JPanelProduto.setLayout(null);

        lista_itens.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        lista_itens.getTableHeader().setReorderingAllowed(false);
        lista_itens.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                lista_itensKeyReleased(evt);
            }
        });
        Produtos.setViewportView(lista_itens);
        if (lista_itens.getColumnModel().getColumnCount() > 0) {
            lista_itens.getColumnModel().getColumn(0).setMaxWidth(50);
            lista_itens.getColumnModel().getColumn(1).setResizable(false);
            lista_itens.getColumnModel().getColumn(1).setHeaderValue(Lingua.getMensagem("nome"));
            lista_itens.getColumnModel().getColumn(2).setResizable(false);
            lista_itens.getColumnModel().getColumn(2).setHeaderValue(Lingua.getMensagem("valor"));
        }

        JPanelProduto.add(Produtos);
        Produtos.setBounds(20, 30, 560, 220);

        BProdutoAdd.setFont(FonteFinancasPequena);
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
        BProdutoAdd.setBounds(20, 250, 120, 30);
        BProdutoAdd.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "A");
        BProdutoAdd.getActionMap().put("A", new AbstractAction() {
            public void actionPerformed(ActionEvent A) {
                Botoes("ItensPesquisar");
            }
        });

        valor_final.setFont(FonteFinancasNegrito);
        valor_final.setForeground(new java.awt.Color(255, 51, 51));
        valor_final.setText("0.0");
        JPanelProduto.add(valor_final);
        valor_final.setBounds(510, 320, 90, 20);

        BProdutoDel.setFont(FonteFinancasPequena);
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
        BProdutoDel.setBounds(140, 250, 120, 30);
        BProdutoDel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), "R");
        BProdutoDel.getActionMap().put("R", new AbstractAction() {
            public void actionPerformed(ActionEvent R) {
                RemoverItem();
            }
        });

        CIFRAO.setFont(FonteFinancasNegrito);
        CIFRAO.setForeground(new java.awt.Color(255, 0, 0));
        CIFRAO.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        CIFRAO.setText(Lingua.getMensagem("moeda"));
        JPanelProduto.add(CIFRAO);
        CIFRAO.setBounds(480, 290, 30, 20);

        CIFRAO1.setFont(FonteFinancasNegrito);
        CIFRAO1.setForeground(new java.awt.Color(255, 0, 0));
        CIFRAO1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        CIFRAO1.setText(Lingua.getMensagem("moeda"));
        JPanelProduto.add(CIFRAO1);
        CIFRAO1.setBounds(480, 320, 30, 20);

        valor_inicial.setFont(FonteFinancasNegrito);
        valor_inicial.setForeground(new java.awt.Color(255, 51, 51));
        valor_inicial.setText("0.0");
        JPanelProduto.add(valor_inicial);
        valor_inicial.setBounds(510, 290, 90, 20);

        CIFRAO2.setFont(FonteFinancasNegrito);
        CIFRAO2.setForeground(new java.awt.Color(255, 0, 0));
        CIFRAO2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        CIFRAO2.setText(Lingua.getMensagem("moeda"));
        JPanelProduto.add(CIFRAO2);
        CIFRAO2.setBounds(480, 260, 30, 20);

        valor_total.setFont(FonteFinancasNegrito);
        valor_total.setForeground(new java.awt.Color(255, 51, 51));
        valor_total.setText("0.0");
        JPanelProduto.add(valor_total);
        valor_total.setBounds(510, 260, 90, 20);

        PRECO_TOTAL3.setFont(FonteFinancas);
        PRECO_TOTAL3.setForeground(new java.awt.Color(51, 51, 51));
        PRECO_TOTAL3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        PRECO_TOTAL3.setText(Lingua.getMensagem("valor_final")+":");
        JPanelProduto.add(PRECO_TOTAL3);
        PRECO_TOTAL3.setBounds(300, 320, 180, 20);

        PRECO_TOTAL4.setFont(FonteFinancas);
        PRECO_TOTAL4.setForeground(new java.awt.Color(51, 51, 51));
        PRECO_TOTAL4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        PRECO_TOTAL4.setText(Lingua.getMensagem("valor_total_itens")+":");
        JPanelProduto.add(PRECO_TOTAL4);
        PRECO_TOTAL4.setBounds(300, 260, 180, 20);

        PRECO_TOTAL5.setFont(FonteFinancas);
        PRECO_TOTAL5.setForeground(new java.awt.Color(51, 51, 51));
        PRECO_TOTAL5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        PRECO_TOTAL5.setText(Lingua.getMensagem("valor_inicial")+":");
        JPanelProduto.add(PRECO_TOTAL5);
        PRECO_TOTAL5.setBounds(300, 290, 180, 20);

        getContentPane().add(JPanelProduto);
        JPanelProduto.setBounds(20, 90, 604, 365);

        BFinalizar.setFont(FonteFinancas);
        BFinalizar.setText(Lingua.getMensagem("atualizar"));
        BFinalizar.setFocusPainted(false);
        BFinalizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BFinalizarActionPerformed(evt);
            }
        });
        getContentPane().add(BFinalizar);
        BFinalizar.setBounds(503, 60, 110, 20);

        setSize(new java.awt.Dimension(648, 500));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

private void BFinalizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BFinalizarActionPerformed
        if(Janelinha.Pergunta(Lingua.getMensagem("atualizar"),Lingua.getMensagem("tem_certeza")))
        {
            CadastrarAlterar();
        }
}//GEN-LAST:event_BFinalizarActionPerformed

private void BProdutoAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BProdutoAddActionPerformed
    Botoes("ItensPesquisar");
}//GEN-LAST:event_BProdutoAddActionPerformed

private void lista_itensKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lista_itensKeyReleased
    CalcularValorTotal();
}//GEN-LAST:event_lista_itensKeyReleased

private void BProdutoDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BProdutoDelActionPerformed
    RemoverItem();
}//GEN-LAST:event_BProdutoDelActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BFinalizar;
    private javax.swing.JButton BProdutoAdd;
    private javax.swing.JButton BProdutoDel;
    private javax.swing.JToolBar BarraDeFerramentas;
    private javax.swing.JLabel CIFRAO;
    private javax.swing.JLabel CIFRAO1;
    private javax.swing.JLabel CIFRAO2;
    private javax.swing.JPanel JPanelProduto;
    private javax.swing.JLabel PRECO_TOTAL3;
    private javax.swing.JLabel PRECO_TOTAL4;
    private javax.swing.JLabel PRECO_TOTAL5;
    private javax.swing.JScrollPane Produtos;
    private javax.swing.JTable lista_itens;
    private javax.swing.JLabel valor_final;
    private javax.swing.JLabel valor_inicial;
    private javax.swing.JLabel valor_total;
    // End of variables declaration//GEN-END:variables

}
