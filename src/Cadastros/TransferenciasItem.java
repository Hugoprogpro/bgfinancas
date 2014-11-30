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
import javax.swing.*;

public final class TransferenciasItem extends Janela {

    private String Acao;
    private String id_item;
    
    public TransferenciasItem() {
        initComponents();
        Visual.Janela(this.getJanela());
        BarraDeFerramentas.add(Botoes.Cadastrar(this));
        BarraDeFerramentas.add(Botoes.Alterar(this));
        BarraDeFerramentas.add(Botoes.Excluir(this));
        BarraDeFerramentas.add(Botoes.Consultar(this));
        BarraDeFerramentas.add(Botoes.Sair(this));
        PreencherFormulario(null,null,null,null);
    }
    
    public void PreencherCategorias()
    {
        try
        {
            Banco.executeQuery("SELECT nome FROM transferencias_categorias ORDER BY nome ASC");
            categoria.addItem(Lingua.getMensagem("selecione"));
            while(Banco.getResultSet().next())
            {
                categoria.addItem(Banco.getResultSet().getString("nome"));
            }
        }catch(Exception e)
        {
            categoria.addItem(Lingua.getMensagem("nenhum_item_encontrado"));
        }
    }
    
    public void PreencherFormulario(String acao, String item_id, String Item, String nome_categoria)
    {
        Acao = acao;
        categoria.setModel( new DefaultComboBoxModel());
        id_item = item_id;
        item.requestFocus();
        item.setText("");
        BFinalizar.setVisible(true);
        if(Acao==null)
        {
            BFinalizar.setText(Lingua.getMensagem("aguardando_acao"));
            item.setEnabled(false);
            categoria.setEnabled(false);
            BFinalizar.setEnabled(false);
        }else{
            item.setEnabled(true);
            categoria.setEnabled(true);
            BFinalizar.setEnabled(true);
            BFinalizar.setText(Acao);
            if(Acao.equals(Lingua.getMensagem("cadastrar")))
            {
                PreencherCategorias();
            }else{
                try
                {
                    Banco.executeQuery("SELECT nome FROM transferencias_itens WHERE id_item='"+id_item+"'");
                    Banco.getResultSet().next();
                    item.setText(Banco.getResultSet().getString("nome"));
                }catch(SQLException e){
                  item.setText(Lingua.getMensagem("erro")+e);
                }
                categoria.addItem(nome_categoria);
                if(Acao.equals(Lingua.getMensagem("consultar")))
                {
                    BFinalizar.setVisible(false);
                }else{
                    PreencherCategorias();
                }
            }
        }
    }
    
    public boolean ValidarFormulario()
    {
        if(item.getText().equals(""))
        {
            Janelinha.Aviso(Lingua.getMensagem("atencao"),Lingua.getMensagem("campo_nao_informado")+" "+Lingua.getMensagem("item"));
            item.requestFocus(true);
            return false;
        }else if(categoria.getSelectedItem().equals(Lingua.getMensagem("selecione"))){
            Janelinha.Aviso(Lingua.getMensagem("atencao"),Lingua.getMensagem("campo_nao_informado")+" "+Lingua.getMensagem("categoria"));
            categoria.requestFocus(true);
            return false;
        }else{
            return true;
        }
    }
    
    public void Cadastrar()
    {
        String id_categoria="";
        try{
            Banco.executeQuery("SELECT id_categoria FROM transferencias_categorias WHERE nome='"+categoria.getSelectedItem()+"'");
            Banco.getResultSet().next();
            id_categoria = Banco.getResultSet().getString("id_categoria");
        }catch(Exception e){
            Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("excecao")+" "+e);
        }       
        try{
            Banco.executeUpdate("INSERT INTO transferencias_itens (id_categoria,nome) VALUES('"+id_categoria+"','"+item.getText()+"')");
            Janelinha.Aviso(Lingua.getMensagem("cadastrar"),Lingua.getMensagem("sucesso"));
            PreencherFormulario(Lingua.getMensagem("cadastrar"),null,null,null);
        }catch(Exception e){
            Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("excecao")+" "+e);
        }
    }
    
    public void Alterar()
    {
        String id_categoria="";
        try{
            Banco.executeQuery("SELECT id_categoria FROM transferencias_categorias WHERE nome='"+categoria.getSelectedItem()+"'");
            Banco.getResultSet().next();
            id_categoria = Banco.getResultSet().getString("id_categoria");
        }catch(Exception e){
            Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("excecao")+" "+e);
        }
        try{
            Banco.executeUpdate("UPDATE transferencias_itens SET id_categoria='"+id_categoria+"', nome='"+item.getText()+"' WHERE id_item='"+id_item+"'");
            Janelinha.Aviso(Lingua.getMensagem("alterar"),Lingua.getMensagem("sucesso"));
            PreencherFormulario(null,null,null,null);
        }catch(Exception e){
            Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("excecao")+" "+e);
        }
    }
    
    public void Excluir()
    {    
        try{
            Banco.executeQuery("SELECT * FROM transferencias WHERE id_item='"+id_item+"'");
            if(Banco.getResultSet().next())
            {
                Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("restricao_excluir"));
                PreencherFormulario(null,null,null,null);
            }else{
                Banco.executeUpdate("DELETE FROM transferencias_itens WHERE id_item='"+id_item+"'");
                Janelinha.Aviso(Lingua.getMensagem("excluir"),Lingua.getMensagem("sucesso"));
                PreencherFormulario(null,null,null,null);
            }
        }catch(Exception e){
            Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("excecao")+" "+e);
        }
    }
    
    @Override
    public void Botoes(String acao)
    {
        if(acao.equals(Lingua.getMensagem("cadastrar"))){
            PreencherFormulario(Lingua.getMensagem("cadastrar"),null,null,null);
        }else if(acao.equals(Lingua.getMensagem("alterar"))){
            new TransferenciasItemPesquisar(this,Lingua.getMensagem("alterar")).setVisible(true);
        }else if(acao.equals(Lingua.getMensagem("excluir"))){
            new TransferenciasItemPesquisar(this,Lingua.getMensagem("excluir")).setVisible(true);
        }else if(acao.equals(Lingua.getMensagem("consultar"))){
            new TransferenciasItemPesquisar(this,Lingua.getMensagem("consultar")).setVisible(true);
        }else if(acao.equals(Lingua.getMensagem("sair"))){
            TransferenciasItem.this.dispose();
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
        JPanelItem = new javax.swing.JPanel();
        CIDADE = new javax.swing.JLabel();
        categoria = new javax.swing.JComboBox();
        BFinalizar = new javax.swing.JButton();
        CEP1 = new javax.swing.JLabel();
        item = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(Lingua.getMensagem("transferencias")+" > "+Lingua.getMensagem("item"));
        setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        setForeground(java.awt.Color.cyan);
        getContentPane().setLayout(null);

        BarraDeFerramentas.setFloatable(false);
        BarraDeFerramentas.setRollover(true);
        getContentPane().add(BarraDeFerramentas);
        BarraDeFerramentas.setBounds(0, 0, 600, 41);

        JPanelItem.setBorder(javax.swing.BorderFactory.createTitledBorder(null, Lingua.getMensagem("dados_item"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, FonteFinancas, new java.awt.Color(0, 102, 204)));
        JPanelItem.setForeground(new java.awt.Color(51, 94, 168));
        JPanelItem.setLayout(null);

        CIDADE.setFont(FonteFinancas);
        CIDADE.setForeground(new java.awt.Color(51, 94, 168));
        CIDADE.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        CIDADE.setText(Lingua.getMensagem("categoria")+":");
        JPanelItem.add(CIDADE);
        CIDADE.setBounds(17, 70, 80, 20);

        categoria.setFont(FonteFinancas);
        JPanelItem.add(categoria);
        categoria.setBounds(110, 70, 323, 20);

        BFinalizar.setFont(FonteFinancas);
        BFinalizar.setText(Lingua.getMensagem("aguardando_acao"));
        BFinalizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BFinalizarActionPerformed(evt);
            }
        });
        JPanelItem.add(BFinalizar);
        BFinalizar.setBounds(110, 100, 130, 20);

        CEP1.setFont(FonteFinancas);
        CEP1.setForeground(new java.awt.Color(51, 94, 168));
        CEP1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        CEP1.setText(Lingua.getMensagem("nome")+":");
        JPanelItem.add(CEP1);
        CEP1.setBounds(17, 40, 80, 20);

        item.setFont(FonteFinancas);
        JPanelItem.add(item);
        item.setBounds(110, 40, 322, 20);

        getContentPane().add(JPanelItem);
        JPanelItem.setBounds(20, 60, 458, 145);

        setSize(new java.awt.Dimension(504, 248));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

private void BFinalizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BFinalizarActionPerformed
    if(Acao.equals(Lingua.getMensagem("cadastrar")))
    {
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BFinalizar;
    private javax.swing.JToolBar BarraDeFerramentas;
    private javax.swing.JLabel CEP1;
    private javax.swing.JLabel CIDADE;
    private javax.swing.JPanel JPanelItem;
    private javax.swing.JComboBox categoria;
    private javax.swing.JTextField item;
    // End of variables declaration//GEN-END:variables

}
