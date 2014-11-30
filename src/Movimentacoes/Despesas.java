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

package Movimentacoes;

import Biblioteca.Botoes;
import Biblioteca.Calendario;
import Biblioteca.Datas;
import Biblioteca.Formularios;
import Biblioteca.Janela;
import Biblioteca.Janelinha;
import Biblioteca.Numeros;
import Biblioteca.Visual;
import Principal.TelaPrincipal;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.Calendar;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;

public final class Despesas extends Janela {

    private String Acao;
    private String data2,id_despesas;
    private final TelaPrincipal Principal;

    public Despesas(TelaPrincipal principal) {
        initComponents();
        Principal = principal;
        Visual.Janela(this.getJanela());
        BarraDeFerramentas.add(Botoes.Cadastrar(this));
        BarraDeFerramentas.add(Botoes.Alterar(this));
        BarraDeFerramentas.add(Botoes.Excluir(this));
        BarraDeFerramentas.add(Botoes.Consultar(this));
        BarraDeFerramentas.add(Botoes.Sair(this));
        PreencherFormulario(null,null);
        lista_itens.setFont(FonteFinancas);
        lista_itens.getTableHeader().setFont(FonteFinancas);
        
    }

    public void AdicionarItem(String id_item, String Preco)
    {
            try
            {
                Banco.executeQuery("SELECT despesas_itens.*, despesas_categorias.nome AS categoria_nome FROM despesas_itens, despesas_categorias WHERE despesas_itens.id_item='"+id_item+"' AND despesas_itens.id_categoria = despesas_categorias.id_categoria");
                DefaultTableModel modelo = (DefaultTableModel)lista_itens.getModel();
                if(Banco.getResultSet().next())
                    modelo.addRow(new Object[]{Banco.getResultSet().getString("id_item"),Banco.getResultSet().getString("nome"),Banco.getResultSet().getString("categoria_nome"),"01",Preco});
            }catch(SQLException e)
            {
                Janelinha.Aviso(Lingua.getMensagem("atencao"),Lingua.getMensagem("excecao")+" "+e.getMessage());
            }
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

    public void PreencherContas()
    {
        try
        {
            Banco.executeQuery("SELECT nome FROM contas WHERE ativada='0' ORDER BY nome ASC");
            conta.addItem(Lingua.getMensagem("selecione"));
            while(Banco.getResultSet().next())
            {
                conta.addItem(Banco.getResultSet().getString("nome"));
            }
        }catch(SQLException e)
        {
            conta.addItem(Lingua.getMensagem("nenhum_item_encontrado"));
        }
    }

    public void CalcularValorTotal()
    {
        try{
            int quantidade_itens,i;
            Double Preco_total=0.0;
            quantidade_itens = lista_itens.getRowCount();
            for(i=0;i<quantidade_itens;i++)
            {
                Preco_total+=Double.parseDouble((String)lista_itens.getValueAt(i,4));
            }
            preco_total.setText(Numeros.Arrendondar(Preco_total).toString());
        }catch(NumberFormatException e){
            Janelinha.Aviso(Lingua.getMensagem("erro"), Lingua.getMensagem("restricao_virgula"));
            CorrigirValores();
        }
    }
    
    public void CorrigirValores(){
        int quantidade_itens,i;
        quantidade_itens = lista_itens.getRowCount();
        for(i=0;i<quantidade_itens;i++)
        {
            String valor = lista_itens.getValueAt(i,4).toString();
            try { 
                Double.parseDouble(valor); 
                int pontos = valor.length() - valor.replace(".", "").length();
                lista_itens.setValueAt(valor.replace(",", "."), i, 4);
            } catch(NumberFormatException e) { 
                lista_itens.setValueAt("0.00", i, 4);
            }
        }
        CalcularValorTotal();
    }

    public void PreencherFormulario(String acao, String despesas_id)
    {
        Acao = acao;
        id_despesas = despesas_id;
        Formularios.LimparFormulario(JPanelProduto);
        BFinalizar.setVisible(true);
        preco_total.setText("0.0");
        DefaultTableModel modelo = (DefaultTableModel)lista_itens.getModel();
        modelo.setNumRows(0);
        conta.removeAllItems();
        if(Acao==null)
        {
            BFinalizar.setText(Lingua.getMensagem("aguardando_acao"));
            BFinalizar.setEnabled(false);
            Formularios.DesativarFormulario(JPanelProduto);
        }else{
            PreencherContas();
            BFinalizar.setEnabled(true);
            Formularios.AtivarFormulario(JPanelProduto);
            BFinalizar.setText(Acao);
            int dia,mes,ano;
            Calendar Data = Calendar.getInstance();
            dia = Data.get(Calendar.DAY_OF_MONTH);
            mes = Data.get(Calendar.MONTH)+1;
            ano = Data.get(Calendar.YEAR);
            data.setSelectedItem(String.format("%02d", dia)+"/"+String.format("%02d", mes)+"/"+ano);
        }
    }
    
    public void Cadastrar()
    {
        data2 = Datas.ConverterData(data.getSelectedItem().toString());

        String id_conta="0";
        Double valor_final = Double.parseDouble(preco_total.getText());

        // id_conta
        Banco.executeQuery("SELECT id_conta FROM contas WHERE nome='"+conta.getSelectedItem()+"'");
        try{
            Banco.getResultSet().next();
            id_conta = Banco.getResultSet().getString("id_conta");
        }catch(SQLException e){
            Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("excecao")+" "+e);
        }

        // items
        try{
            int Quantidade_Itens,i;
            Quantidade_Itens = lista_itens.getRowCount();
            for(i=0;i<Quantidade_Itens;i++)
            {
                Banco.executeUpdate("INSERT INTO despesas (id_conta,id_item,quantidade,valor,data,hora) VALUES('"+id_conta+"','"+lista_itens.getValueAt(i,0).toString()+"','"+lista_itens.getValueAt(i,3).toString()+"','"+Numeros.Arrendondar(lista_itens.getValueAt(i,4).toString())+"','"+data2+"',NOW())");
            }
            Banco.executeUpdate("UPDATE contas SET valor = valor - "+Numeros.Arrendondar(valor_final)+" WHERE id_conta='"+id_conta+"'");
            Janelinha.Aviso(Lingua.getMensagem("cadastrar"),Lingua.getMensagem("sucesso"));
            Principal.ContasSaldo();
            PreencherFormulario(Lingua.getMensagem("cadastrar"),null);
        }catch(Exception e){
            Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("excecao")+" "+e);
        }
    }
    
    @Override
    public void Botoes(String acao)
    {
        if(acao.equals(Lingua.getMensagem("cadastrar"))){
            PreencherFormulario(Lingua.getMensagem("cadastrar"),null);
        }else if(acao.equals("ItensPesquisar")){
            new DespesasItensPesquisar(this).setVisible(true);
        }else if(acao.equals(Lingua.getMensagem("alterar"))){
            new DespesasPesquisar(Principal,this,null,Lingua.getMensagem("alterar")).setVisible(true);
        }else if(acao.equals(Lingua.getMensagem("excluir"))){
            new DespesasPesquisar(Principal,this,null,Lingua.getMensagem("excluir")).setVisible(true);
        }else if(acao.equals(Lingua.getMensagem("consultar"))){
            new DespesasPesquisar(Principal,this,null,Lingua.getMensagem("consultar")).setVisible(true);
        }else if(acao.equals(Lingua.getMensagem("sair"))){
            Despesas.this.dispose();
        }
    }

    public boolean ValidarFormulario()
    {
        if(conta.getSelectedItem().equals(Lingua.getMensagem("selecione")))
        {
            Janelinha.Aviso(Lingua.getMensagem("erro"), Lingua.getMensagem("campo_nao_informado")+" "+Lingua.getMensagem("conta"));
            conta.requestFocus(true);
            return false;
        }else if(preco_total.getText().equals("0.0"))
        {
            Janelinha.Aviso(Lingua.getMensagem("erro"), Lingua.getMensagem("restricao_valor"));
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

        BarraDeFerramentas = new javax.swing.JToolBar();
        JPanelProduto = new javax.swing.JPanel();
        Produtos = new javax.swing.JScrollPane();
        lista_itens = new javax.swing.JTable();
        BProdutoAdd = new javax.swing.JButton();
        preco_total = new javax.swing.JLabel();
        BProdutoDel = new javax.swing.JButton();
        CIFRAO = new javax.swing.JLabel();
        PRECO_TOTAL = new javax.swing.JLabel();
        CONTA = new javax.swing.JLabel();
        conta = new javax.swing.JComboBox();
        DATA = new javax.swing.JLabel();
        BFinalizar = new javax.swing.JButton();
        data = new Calendario(false);
        erro_foco = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(Lingua.getMensagem("despesas"));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        setForeground(java.awt.Color.cyan);
        getContentPane().setLayout(null);

        BarraDeFerramentas.setFloatable(false);
        BarraDeFerramentas.setRollover(true);
        getContentPane().add(BarraDeFerramentas);
        BarraDeFerramentas.setBounds(0, 0, 720, 41);

        JPanelProduto.setBorder(javax.swing.BorderFactory.createTitledBorder(null, Lingua.getMensagem("itens"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, FonteFinancas, new java.awt.Color(0, 102, 204)));
        JPanelProduto.setForeground(new java.awt.Color(51, 94, 168));
        JPanelProduto.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        JPanelProduto.setLayout(null);

        lista_itens.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "", "", "", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        lista_itens.getTableHeader().setReorderingAllowed(false);
        lista_itens.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lista_itensMouseClicked(evt);
            }
        });
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
            lista_itens.getColumnModel().getColumn(2).setHeaderValue(Lingua.getMensagem("categoria"));
            lista_itens.getColumnModel().getColumn(3).setResizable(false);
            lista_itens.getColumnModel().getColumn(3).setHeaderValue(Lingua.getMensagem("quantidade"));
            lista_itens.getColumnModel().getColumn(4).setResizable(false);
            lista_itens.getColumnModel().getColumn(4).setHeaderValue(Lingua.getMensagem("valor"));
        }

        JPanelProduto.add(Produtos);
        Produtos.setBounds(20, 30, 570, 220);

        BProdutoAdd.setFont(FonteFinancasPequena);
        BProdutoAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagens/outros/adicionar.png"))); // NOI18N
        BProdutoAdd.setText(Lingua.getMensagem("adicionar_item"));
        BProdutoAdd.setToolTipText(Lingua.getMensagem("adicionar_item"));
        BProdutoAdd.setBorder(null);
        BProdutoAdd.setBorderPainted(false);
        BProdutoAdd.setContentAreaFilled(false);
        BProdutoAdd.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        BProdutoAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BProdutoAddActionPerformed(evt);
            }
        });
        JPanelProduto.add(BProdutoAdd);
        BProdutoAdd.setBounds(40, 250, 120, 30);
        BProdutoAdd.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "A");
        BProdutoAdd.getActionMap().put("A", new AbstractAction() {
            public void actionPerformed(ActionEvent A) {
                Botoes("ItensPesquisar");
            }
        });

        preco_total.setFont(FonteFinancasNegrito);
        preco_total.setForeground(new java.awt.Color(255, 51, 51));
        preco_total.setText("0.0");
        preco_total.setFocusable(false);
        JPanelProduto.add(preco_total);
        preco_total.setBounds(520, 260, 90, 20);

        BProdutoDel.setFont(FonteFinancasPequena);
        BProdutoDel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagens/outros/remover.png"))); // NOI18N
        BProdutoDel.setText(Lingua.getMensagem("remover_item"));
        BProdutoDel.setToolTipText(Lingua.getMensagem("remover_item"));
        BProdutoDel.setBorder(null);
        BProdutoDel.setBorderPainted(false);
        BProdutoDel.setContentAreaFilled(false);
        BProdutoDel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        BProdutoDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BProdutoDelActionPerformed(evt);
            }
        });
        JPanelProduto.add(BProdutoDel);
        BProdutoDel.setBounds(40, 280, 120, 30);
        BProdutoDel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), "R");
        BProdutoDel.getActionMap().put("R", new AbstractAction() {
            public void actionPerformed(ActionEvent R) {
                RemoverItem();
            }
        });

        CIFRAO.setFont(FonteFinancasNegrito);
        CIFRAO.setForeground(new java.awt.Color(255, 0, 0));
        CIFRAO.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        CIFRAO.setText(Lingua.getMensagem("moeda")
        );
        CIFRAO.setFocusable(false);
        JPanelProduto.add(CIFRAO);
        CIFRAO.setBounds(490, 260, 30, 20);

        PRECO_TOTAL.setFont(FonteFinancas);
        PRECO_TOTAL.setForeground(new java.awt.Color(51, 94, 168));
        PRECO_TOTAL.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        PRECO_TOTAL.setText(Lingua.getMensagem("valor_total")+":");
        PRECO_TOTAL.setFocusable(false);
        JPanelProduto.add(PRECO_TOTAL);
        PRECO_TOTAL.setBounds(390, 260, 90, 20);

        CONTA.setFont(FonteFinancas);
        CONTA.setForeground(new java.awt.Color(51, 94, 168));
        CONTA.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        CONTA.setText(Lingua.getMensagem("conta")+":");
        CONTA.setFocusable(false);
        JPanelProduto.add(CONTA);
        CONTA.setBounds(160, 290, 100, 20);

        conta.setFont(FonteFinancas);
        JPanelProduto.add(conta);
        conta.setBounds(270, 290, 210, 20);

        DATA.setFont(FonteFinancas);
        DATA.setForeground(new java.awt.Color(51, 94, 168));
        DATA.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        DATA.setText(Lingua.getMensagem("data")+":");
        DATA.setFocusable(false);
        JPanelProduto.add(DATA);
        DATA.setBounds(190, 260, 70, 20);

        BFinalizar.setFont(FonteFinancas);
        BFinalizar.setText(Lingua.getMensagem("aguardando_acao"));
        BFinalizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BFinalizarActionPerformed(evt);
            }
        });
        JPanelProduto.add(BFinalizar);
        BFinalizar.setBounds(493, 290, 90, 20);

        data.setFont(FonteFinancas);
        JPanelProduto.add(data);
        data.setBounds(270, 260, 110, 20);

        getContentPane().add(JPanelProduto);
        JPanelProduto.setBounds(20, 60, 612, 335);

        erro_foco.setFont(FonteFinancas);
        erro_foco.setBorderPainted(false);
        erro_foco.setContentAreaFilled(false);
        erro_foco.setFocusPainted(false);
        erro_foco.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                erro_focoActionPerformed(evt);
            }
        });
        getContentPane().add(erro_foco);
        erro_foco.setBounds(493, 50, 90, 10);

        setSize(new java.awt.Dimension(655, 437));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

private void BFinalizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BFinalizarActionPerformed
    if(Acao.equals(Lingua.getMensagem("cadastrar")))
    {
        if(lista_itens.isEditing()){
            Janelinha.Aviso(Lingua.getMensagem("erro"), Lingua.getMensagem("restricao_edicao_celula_tabela"));
            CalcularValorTotal();
        }else{
            if(ValidarFormulario() && Janelinha.Pergunta(Lingua.getMensagem("cadastrar"),Lingua.getMensagem("tem_certeza")))
            {
                Cadastrar();
            }
        }
    }
}//GEN-LAST:event_BFinalizarActionPerformed

    private void BProdutoDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BProdutoDelActionPerformed
        RemoverItem();
    }//GEN-LAST:event_BProdutoDelActionPerformed

    private void BProdutoAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BProdutoAddActionPerformed
        Botoes("ItensPesquisar");
    }//GEN-LAST:event_BProdutoAddActionPerformed

    private void lista_itensKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lista_itensKeyReleased
        CalcularValorTotal();
    }//GEN-LAST:event_lista_itensKeyReleased

    private void lista_itensMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lista_itensMouseClicked
        CalcularValorTotal();
    }//GEN-LAST:event_lista_itensMouseClicked

    private void erro_focoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_erro_focoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_erro_focoActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BFinalizar;
    private javax.swing.JButton BProdutoAdd;
    private javax.swing.JButton BProdutoDel;
    private javax.swing.JToolBar BarraDeFerramentas;
    private javax.swing.JLabel CIFRAO;
    private javax.swing.JLabel CONTA;
    private javax.swing.JLabel DATA;
    private javax.swing.JPanel JPanelProduto;
    private javax.swing.JLabel PRECO_TOTAL;
    private javax.swing.JScrollPane Produtos;
    private javax.swing.JComboBox conta;
    private javax.swing.JComboBox data;
    private javax.swing.JButton erro_foco;
    private javax.swing.JTable lista_itens;
    private javax.swing.JLabel preco_total;
    // End of variables declaration//GEN-END:variables

}
