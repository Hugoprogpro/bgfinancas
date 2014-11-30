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
import Biblioteca.Mascaras;
import Biblioteca.NotacaoInternacional;
import Biblioteca.Numeros;
import Biblioteca.Visual;
import Principal.TelaPrincipal;
import java.sql.SQLException;
import java.util.Calendar;

public final class Receitas extends Janela {

    private String Acao;
    private Mascaras mascaras;
    private String id_receita;
    private Float valor_antigo;
    private final TelaPrincipal Principal;
    
    public Receitas(TelaPrincipal principal) {
        initComponents();
        Principal = principal;
        Visual.Janela(this.getJanela());
        BarraDeFerramentas.add(Botoes.Cadastrar(this));
        BarraDeFerramentas.add(Botoes.Alterar(this));
        BarraDeFerramentas.add(Botoes.Excluir(this));
        BarraDeFerramentas.add(Botoes.Consultar(this));
        BarraDeFerramentas.add(Botoes.Sair(this));
        PreencherFormulario(null,null);
    }

    public void AdicionarItem(String item_nome)
    {
        item.removeAllItems();
        item.addItem(item_nome);
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

    public void PreencherItens()
    {
        try
        {
            Banco.executeQuery("SELECT nome FROM receitas_itens ORDER BY nome ASC");
            item.addItem(Lingua.getMensagem("selecione"));
            while(Banco.getResultSet().next())
            {
                item.addItem(Banco.getResultSet().getString("nome"));
            }
        }catch(SQLException e)
        {
            conta.addItem(Lingua.getMensagem("nenhum_item_encontrado"));
        }
    }

    public void PreencherFormulario(String acao, String receitas_id)
    {
        Acao = acao;
        id_receita = receitas_id;
        Formularios.LimparFormulario(JPanelProduto);
        BFinalizar.setVisible(true);
        conta.removeAllItems();
        item.removeAllItems();
        if(Acao==null)
        {
            BFinalizar.setText(Lingua.getMensagem("aguardando_acao"));
            BFinalizar.setEnabled(false);
            Formularios.DesativarFormulario(JPanelProduto);
        }else{
            BFinalizar.setText(Acao);
            BFinalizar.setEnabled(true);
            Formularios.AtivarFormulario(JPanelProduto);
            if(acao.equals(Lingua.getMensagem("cadastrar")))
            {
                PreencherContas();
                PreencherItens();
                int dia,mes,ano;
                Calendar Data = Calendar.getInstance();
                dia = Data.get(Calendar.DAY_OF_MONTH);
                mes = Data.get(Calendar.MONTH)+1;
                ano = Data.get(Calendar.YEAR);
                data.setSelectedItem(String.format("%02d", dia)+"/"+String.format("%02d", mes)+"/"+ano);
            }else{
                BItemPesquisar.setEnabled(false);
                if(acao.equals(Lingua.getMensagem("consultar")))
                {
                    BFinalizar.setVisible(false);
                }
                Banco.executeQuery("SELECT receitas.*, receitas_itens.nome AS item_nome, TO_CHAR(receitas.data,'DD/MM/YYYY') AS data_exibir, contas.nome AS conta_nome FROM receitas, receitas_itens, contas WHERE id_receita='"+id_receita+"' AND receitas.id_item=receitas_itens.id_item AND receitas.id_conta=contas.id_conta");
                try{
                    Banco.getResultSet().next();
                    item.addItem(Banco.getResultSet().getString("item_nome"));
                    descricao.setText(Banco.getResultSet().getString("descricao"));
                    valor.setText(Banco.getResultSet().getString("valor"));
                    valor_antigo = Float.parseFloat(Banco.getResultSet().getString("valor"));
                    data.setSelectedItem(Banco.getResultSet().getString("data_exibir"));
                    conta.addItem(Banco.getResultSet().getString("conta_nome"));
                }catch(SQLException e){
                    Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("excecao")+" "+e);
                } catch (NumberFormatException e) {
                    Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("excecao")+" "+e);
                }
            }
        }
    }

    public void Cadastrar()
    {
        String id_conta="0",id_item="0";

        // id_conta
        Banco.executeQuery("SELECT id_conta FROM contas WHERE nome='"+conta.getSelectedItem()+"'");
        try{
            Banco.getResultSet().next();
            id_conta = Banco.getResultSet().getString("id_conta");
        }catch(SQLException e){
            Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("excecao")+" "+e);
        }

        // id_item
        Banco.executeQuery("SELECT id_item FROM receitas_itens WHERE nome='"+item.getSelectedItem()+"'");
        try{
            Banco.getResultSet().next();
            id_item = Banco.getResultSet().getString("id_item");
        }catch(SQLException e){
            Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("excecao")+" "+e);
        }

        // cadastrando
        try{
            Banco.executeUpdate("INSERT INTO receitas (id_conta,id_item,descricao,valor,data,hora) VALUES('"+id_conta+"','"+id_item+"','"+descricao.getText()+"','"+Numeros.Arrendondar(valor.getText())+"','"+Datas.ConverterData(data.getSelectedItem().toString())+"',NOW())");
            Banco.executeUpdate("UPDATE contas SET valor=valor+"+Numeros.Arrendondar(valor.getText())+" WHERE id_conta='"+id_conta+"'");
            Janelinha.Aviso(Lingua.getMensagem("cadastrar"),Lingua.getMensagem("sucesso"));
            Principal.ContasSaldo();
            PreencherFormulario(Lingua.getMensagem("cadastrar"),null);
        }catch(Exception e){
            Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("excecao")+" "+e);
        }
    }
    
    public void Alterar()
    {
        String id_conta="0";
        // id_conta
        Banco.executeQuery("SELECT id_conta FROM contas WHERE nome='"+conta.getSelectedItem()+"'");
        try{
            Banco.getResultSet().next();
            id_conta = Banco.getResultSet().getString("id_conta");
        }catch(SQLException e){
            Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("excecao")+" "+e);
        }

        // alterar
        Double valor_atual = Double.parseDouble(valor.getText()),diferenca;
        diferenca = valor_atual-valor_antigo;
        try{
            Banco.executeUpdate("UPDATE receitas SET valor='"+Numeros.Arrendondar(valor.getText())+"', descricao='"+descricao.getText()+"', data='"+Datas.ConverterData(data.getSelectedItem().toString())+"' WHERE id_receita='"+id_receita+"'");
            // modificando conta
            Banco.executeUpdate("UPDATE contas SET valor=valor+"+Numeros.Arrendondar(diferenca)+" WHERE id_conta='"+id_conta+"'");
            Janelinha.Aviso(Lingua.getMensagem("alterar"),Lingua.getMensagem("sucesso"));
            Principal.ContasSaldo();
            PreencherFormulario(null,null);
        }catch(Exception e){
            Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("excecao")+" "+e);
        }
    }

    public void Excluir()
    {
        String id_conta="0";
        Banco.executeQuery("SELECT id_conta FROM contas WHERE nome='"+conta.getSelectedItem()+"'");
        try{
            Banco.getResultSet().next();
            id_conta = Banco.getResultSet().getString("id_conta");
        }catch(SQLException e){
            Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("excecao")+" "+e);
        }
        
        try{
            if(Banco.executeUpdate("DELETE FROM receitas WHERE id_receita='"+id_receita+"'")>0)
            {
                Banco.executeUpdate("UPDATE contas SET valor = valor - "+valor_antigo+" WHERE id_conta='"+id_conta+"'");
                Janelinha.Aviso(Lingua.getMensagem("excluir"),Lingua.getMensagem("sucesso"));
                Principal.ContasSaldo();
                PreencherFormulario(null,null);
            }else{
                PreencherFormulario(null,null);
            }
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
            new ReceitasItensPesquisar(this).setVisible(true);
        }else if(acao.equals(Lingua.getMensagem("alterar"))){
            new ReceitasPesquisar(Principal,this,Lingua.getMensagem("alterar")).setVisible(true);
        }else if(acao.equals(Lingua.getMensagem("excluir"))){
            new ReceitasPesquisar(Principal,this,Lingua.getMensagem("excluir")).setVisible(true);
        }else if(acao.equals(Lingua.getMensagem("consultar"))){
            new ReceitasPesquisar(Principal,this,Lingua.getMensagem("consultar")).setVisible(true);
        }else if(acao.equals(Lingua.getMensagem("sair"))){
            Receitas.this.dispose();
        }
    }

    public boolean ValidarFormulario()
    {
        if(item.getSelectedItem().equals(Lingua.getMensagem("selecione")))
        {
            Janelinha.Aviso(Lingua.getMensagem("erro"), Lingua.getMensagem("campo_nao_informado")+" "+Lingua.getMensagem("item"));
            item.requestFocus(true);
            return false;
        }else if (valor.getText().equals(""))
        {
            Janelinha.Aviso(Lingua.getMensagem("erro"), Lingua.getMensagem("campo_nao_informado")+" "+Lingua.getMensagem("valor"));
            valor.requestFocus(true);
            return false;
        }else if(conta.getSelectedItem().equals(Lingua.getMensagem("selecione")))
        {
            Janelinha.Aviso(Lingua.getMensagem("erro"), Lingua.getMensagem("campo_nao_informado")+" "+Lingua.getMensagem("conta"));
            conta.requestFocus(true);
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
        CONTA = new javax.swing.JLabel();
        DATA = new javax.swing.JLabel();
        DATA2 = new javax.swing.JLabel();
        DATA3 = new javax.swing.JLabel();
        DATA4 = new javax.swing.JLabel();
        valor = new javax.swing.JTextField();
        descricao = new javax.swing.JTextField();
        conta = new javax.swing.JComboBox();
        BItemPesquisar = new javax.swing.JButton();
        item = new javax.swing.JComboBox();
        DATA1 = new javax.swing.JLabel();
        BFinalizar = new javax.swing.JButton();
        data = new Calendario(false);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(Lingua.getMensagem("receitas"));
        setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        setForeground(java.awt.Color.cyan);
        getContentPane().setLayout(null);

        BarraDeFerramentas.setFloatable(false);
        BarraDeFerramentas.setRollover(true);
        getContentPane().add(BarraDeFerramentas);
        BarraDeFerramentas.setBounds(0, 0, 500, 41);

        JPanelProduto.setBorder(javax.swing.BorderFactory.createTitledBorder(null, Lingua.getMensagem("dados_receita"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, FonteFinancas, new java.awt.Color(0, 102, 204)));
        JPanelProduto.setForeground(new java.awt.Color(51, 94, 168));
        JPanelProduto.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        JPanelProduto.setLayout(null);

        CONTA.setFont(FonteFinancas);
        CONTA.setForeground(new java.awt.Color(51, 94, 168));
        CONTA.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        CONTA.setText(Lingua.getMensagem("conta")+":");
        JPanelProduto.add(CONTA);
        CONTA.setBounds(20, 160, 80, 20);

        DATA.setFont(FonteFinancas);
        DATA.setForeground(new java.awt.Color(51, 94, 168));
        DATA.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        DATA.setText(Lingua.getMensagem("moeda"));
        JPanelProduto.add(DATA);
        DATA.setBounds(110, 100, 30, 20);

        DATA2.setFont(FonteFinancas);
        DATA2.setForeground(new java.awt.Color(51, 94, 168));
        DATA2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        DATA2.setText(Lingua.getMensagem("item")+":");
        JPanelProduto.add(DATA2);
        DATA2.setBounds(20, 40, 80, 20);

        DATA3.setFont(FonteFinancas);
        DATA3.setForeground(new java.awt.Color(51, 94, 168));
        DATA3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        DATA3.setText(Lingua.getMensagem("descricao")+":");
        JPanelProduto.add(DATA3);
        DATA3.setBounds(20, 70, 80, 20);

        DATA4.setFont(FonteFinancas);
        DATA4.setForeground(new java.awt.Color(51, 94, 168));
        DATA4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        DATA4.setText(Lingua.getMensagem("data")+":");
        JPanelProduto.add(DATA4);
        DATA4.setBounds(20, 130, 80, 20);

        valor.setFont(FonteFinancas);
        JPanelProduto.add(valor);
        valor.setBounds(140, 100, 60, 20);
        valor.setDocument(new Biblioteca.NotacaoInternacional());

        descricao.setFont(FonteFinancas);
        JPanelProduto.add(descricao);
        descricao.setBounds(110, 70, 220, 20);

        conta.setFont(FonteFinancas);
        JPanelProduto.add(conta);
        conta.setBounds(110, 160, 220, 20);

        BItemPesquisar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagens/outros/pesquisar.png"))); // NOI18N
        BItemPesquisar.setBorderPainted(false);
        BItemPesquisar.setContentAreaFilled(false);
        BItemPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BItemPesquisarActionPerformed(evt);
            }
        });
        JPanelProduto.add(BItemPesquisar);
        BItemPesquisar.setBounds(330, 40, 30, 20);

        item.setFont(FonteFinancas);
        JPanelProduto.add(item);
        item.setBounds(110, 40, 220, 20);

        DATA1.setFont(FonteFinancas);
        DATA1.setForeground(new java.awt.Color(51, 94, 168));
        DATA1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        DATA1.setText(Lingua.getMensagem("valor")+":");
        JPanelProduto.add(DATA1);
        DATA1.setBounds(20, 100, 80, 20);

        BFinalizar.setFont(FonteFinancas);
        BFinalizar.setText(Lingua.getMensagem("aguardando_acao"));
        BFinalizar.setFocusPainted(false);
        BFinalizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BFinalizarActionPerformed(evt);
            }
        });
        JPanelProduto.add(BFinalizar);
        BFinalizar.setBounds(110, 190, 110, 20);

        data.setFont(FonteFinancas);
        JPanelProduto.add(data);
        data.setBounds(110, 130, 110, 20);

        getContentPane().add(JPanelProduto);
        JPanelProduto.setBounds(20, 60, 377, 230);

        setSize(new java.awt.Dimension(424, 335));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

private void BFinalizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BFinalizarActionPerformed
    if(Acao.equals(Lingua.getMensagem("cadastrar"))){
        if(ValidarFormulario() && Janelinha.Pergunta(Lingua.getMensagem("cadastrar"),Lingua.getMensagem("tem_certeza")))
        {
            Cadastrar();
        }
    }else if(Acao.equals(Lingua.getMensagem("alterar"))){
        if(ValidarFormulario() && Janelinha.Pergunta(Lingua.getMensagem("alterar"),Lingua.getMensagem("tem_certeza")))
        {
            Alterar();
        }
    }else if(Acao.equals(Lingua.getMensagem("excluir")))
    {
        if(Janelinha.Pergunta(Lingua.getMensagem("excluir"),Lingua.getMensagem("tem_certeza")))
        {
            Excluir();
        }
    }
}//GEN-LAST:event_BFinalizarActionPerformed

private void BItemPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BItemPesquisarActionPerformed
    Botoes("ItensPesquisar");
}//GEN-LAST:event_BItemPesquisarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BFinalizar;
    private javax.swing.JButton BItemPesquisar;
    private javax.swing.JToolBar BarraDeFerramentas;
    private javax.swing.JLabel CONTA;
    private javax.swing.JLabel DATA;
    private javax.swing.JLabel DATA1;
    private javax.swing.JLabel DATA2;
    private javax.swing.JLabel DATA3;
    private javax.swing.JLabel DATA4;
    private javax.swing.JPanel JPanelProduto;
    private javax.swing.JComboBox conta;
    private javax.swing.JComboBox data;
    private javax.swing.JTextField descricao;
    private javax.swing.JComboBox item;
    private javax.swing.JTextField valor;
    // End of variables declaration//GEN-END:variables

}
