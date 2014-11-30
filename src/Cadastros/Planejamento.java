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
import Biblioteca.Formularios;
import Biblioteca.Janela;
import Biblioteca.Janelinha;
import Biblioteca.NotacaoInternacional;
import Biblioteca.Visual;
import Principal.TelaPrincipal;
import java.sql.SQLException;
import java.util.Calendar;

public final class Planejamento extends Janela {

    private String Acao;
    private String id_planejamento,Valor;
    private final TelaPrincipal Principal;

    public Planejamento(TelaPrincipal principal) {
        initComponents();
        Principal = principal;
        Visual.Janela(this.getJanela());
        BarraDeFerramentas.add(Botoes.Cadastrar(this));
        BarraDeFerramentas.add(Botoes.Alterar(this));
        BarraDeFerramentas.add(Botoes.Excluir(this));
        BarraDeFerramentas.add(Botoes.Consultar(this));
        BarraDeFerramentas.add(Botoes.Sair(this));
        PreencherFormulario(null,null,0,null,null);
    }

    public void PreencherFormulario(String acao, String planejamento_id, Integer mes, String ano, String valor)
    {
        Acao = acao;
        id_planejamento = planejamento_id;
        Valor = valor;
        pmes.setSelectedIndex(0);
        Formularios.LimparFormulario(JPanelProduto);
        BFinalizar.setVisible(true);
        BItens.setEnabled(false);
        if(Acao==null)
        {
            BFinalizar.setText(Lingua.getMensagem("aguardando_acao"));
            BFinalizar.setEnabled(false);
            Formularios.DesativarFormulario(JPanelProduto);
        }else{
            BFinalizar.setText(Acao);
            BFinalizar.setEnabled(true);
            Formularios.AtivarFormulario(JPanelProduto);
            if(acao.equals(Lingua.getMensagem("duplicar")))
            {
                Calendar Data = Calendar.getInstance();
                Data.add(Calendar.MONTH, 1);
                int MES = Data.get(Calendar.MONTH)+1;
                pmes.setSelectedIndex(MES);
                pano.setText(Integer.toString(Data.get(Calendar.YEAR)));
                pmes.setEnabled(true);
                pano.setEditable(true);
                pvalor.setText(valor);
                BItens.setEnabled(false);
            }else if(acao.equals(Lingua.getMensagem("cadastrar")))
            {
                if(mes>0){
                    pmes.setSelectedIndex(mes);
                    pano.setText(ano);
                }else{
                    Calendar Data = Calendar.getInstance();
                    Data.add(Calendar.MONTH, 1);
                    int MES = Data.get(Calendar.MONTH)+1;
                    pmes.setSelectedIndex(MES);
                    pano.setText(Integer.toString(Data.get(Calendar.YEAR)));
                }
                pmes.setEnabled(true);
                pano.setEditable(true);
                BItens.setEnabled(false);
            }else{
                if(acao.equals(Lingua.getMensagem("consultar")))
                {
                    BFinalizar.setVisible(false);
                }
                if(acao.equals(Lingua.getMensagem("alterar")))
                {
                    pmes.setEnabled(false);
                    pano.setEditable(false);
                    BItens.setEnabled(true);
                }
                pmes.setSelectedIndex(mes);
                pano.setText(ano);
                pvalor.setText(valor);
            }
        }
    }

    public void Cadastrar()
    {
        // cadastrando
        try{
            Banco.executeQuery("SELECT id_planejamento FROM planejamento WHERE mes='"+pmes.getSelectedItem()+"' AND ano='"+pano.getText()+"'");
            if(Banco.getResultSet().next())
            {
                Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("restricao_cadastrar"));
            }else{
                Banco.executeUpdate("INSERT INTO planejamento (mes,ano,valor) VALUES('"+pmes.getSelectedItem()+"','"+pano.getText()+"','"+pvalor.getText()+"')");
                Janelinha.Aviso(Lingua.getMensagem("cadastrar"),Lingua.getMensagem("sucesso"));
                Principal.SetarData((String)pmes.getSelectedItem(), pano.getText());
                Principal.Planejamento((String)pmes.getSelectedItem(),pano.getText());
                PreencherFormulario(null,null,0,null,null);
            }
        }catch(Exception e){
            Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("excecao")+" "+e);
        }
    }
    
    public void Alterar()
    {
        try{
            Banco.executeUpdate("UPDATE planejamento SET valor='"+pvalor.getText()+"' WHERE id_planejamento='"+id_planejamento+"'");
            Janelinha.Aviso(Lingua.getMensagem("alterar"),Lingua.getMensagem("sucesso"));
            Principal.SetarData((String)pmes.getSelectedItem(), pano.getText());
            Principal.Planejamento((String)pmes.getSelectedItem(),pano.getText());
            PreencherFormulario(null,null,0,null,null);
        }catch(Exception e){
            Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("excecao")+" "+e);
        }
    }

    public void Excluir()
    {
        try{
            if(Banco.executeUpdate("DELETE FROM planejamento WHERE id_planejamento='"+id_planejamento+"'")>0)
            {
                Banco.executeUpdate("DELETE FROM planejamento_componentes WHERE id_planejamento='"+id_planejamento+"'");
                Janelinha.Aviso(Lingua.getMensagem("excluir"),Lingua.getMensagem("sucesso"));
                Principal.SetarData((String)pmes.getSelectedItem(), pano.getText());
                Principal.Planejamento((String)pmes.getSelectedItem(),pano.getText());
                PreencherFormulario(null,null,0,null,null);
            }else{
                PreencherFormulario(null,null,0,null,null);
            }
        }catch(Exception e){
            Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("excecao")+" "+e);
        }
    }
    
    public void Duplicar()
    {
        // cadastrando
        try{
            Banco.executeQuery("SELECT id_planejamento FROM planejamento WHERE mes='"+pmes.getSelectedItem()+"' AND ano='"+pano.getText()+"'");
            if(Banco.getResultSet().next())
            {
                Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("restricao_cadastrar"));
            }else{
                Banco.executeUpdate("INSERT INTO planejamento (mes,ano,valor) VALUES('"+pmes.getSelectedItem()+"','"+pano.getText()+"','"+pvalor.getText()+"')");
                Banco.executeQuery("SELECT id_planejamento FROM planejamento WHERE mes='"+pmes.getSelectedItem()+"' AND ano='"+pano.getText()+"'");
                Banco.getResultSet().next();
                String id_novo_plano = Banco.getResultSet().getString("id_planejamento");
                Banco.executeQuery("SELECT planejamento_componentes.id_item, planejamento_componentes.valor, planejamento_itens.nome FROM planejamento_componentes, planejamento_itens WHERE planejamento_componentes.id_planejamento='"+id_planejamento+"' AND planejamento_itens.id_item=planejamento_componentes.id_item ORDER BY planejamento_itens.nome ASC");
                while(Banco.getResultSet().next()){
                    Banco.executeUpdate("INSERT INTO planejamento_componentes VALUES('"+id_novo_plano+"','"+Banco.getResultSet().getString("id_item")+"','"+Banco.getResultSet().getString("valor")+"')");
                }
                Janelinha.Aviso(Lingua.getMensagem("duplicar"),Lingua.getMensagem("sucesso"));
                Principal.SetarData((String)pmes.getSelectedItem(), pano.getText());
                Principal.Planejamento((String)pmes.getSelectedItem(),pano.getText());
                PreencherFormulario(null,null,0,null,null);
            }
        }catch(SQLException e){
            Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("excecao")+" "+e);
        }
    }
    
    @Override
    public void Botoes(String acao)
    {
        if(acao.equals(Lingua.getMensagem("cadastrar"))){
            PreencherFormulario(Lingua.getMensagem("cadastrar"),null,0,null,null);
        }else if(acao.equals(Lingua.getMensagem("alterar"))){
            new PlanejamentoPesquisar(Principal,this,Lingua.getMensagem("alterar")).setVisible(true);
        }else if(acao.equals(Lingua.getMensagem("excluir"))){
            new PlanejamentoPesquisar(Principal,this,Lingua.getMensagem("excluir")).setVisible(true);
        }else if(acao.equals(Lingua.getMensagem("consultar"))){
            new PlanejamentoPesquisar(Principal,this,Lingua.getMensagem("consultar")).setVisible(true);
        }else if(acao.equals(Lingua.getMensagem("sair"))){
            Planejamento.this.dispose();
        }
    }

    public boolean ValidarFormulario()
    {
        if(pmes.getSelectedItem().equals(Lingua.getMensagem("selecione")))
        {
            Janelinha.Aviso(Lingua.getMensagem("erro"), Lingua.getMensagem("campo_nao_informado")+" "+Lingua.getMensagem("mes"));
            pmes.requestFocus(true);
            return false;
        }else if (pvalor.getText().equals(""))
        {
            Janelinha.Aviso(Lingua.getMensagem("erro"), Lingua.getMensagem("campo_nao_informado")+" "+Lingua.getMensagem("valor"));
            pvalor.requestFocus(true);
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
        DATA = new javax.swing.JLabel();
        DATA2 = new javax.swing.JLabel();
        DATA3 = new javax.swing.JLabel();
        pvalor = new javax.swing.JTextField();
        pano = new javax.swing.JTextField();
        pmes = new javax.swing.JComboBox();
        DATA1 = new javax.swing.JLabel();
        BItens = new javax.swing.JButton();
        BFinalizar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(Lingua.getMensagem("planejamento")+" > "+Lingua.getMensagem("plano"));
        setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        setForeground(java.awt.Color.cyan);
        getContentPane().setLayout(null);

        BarraDeFerramentas.setFloatable(false);
        BarraDeFerramentas.setRollover(true);
        getContentPane().add(BarraDeFerramentas);
        BarraDeFerramentas.setBounds(0, 0, 430, 41);

        JPanelProduto.setBorder(javax.swing.BorderFactory.createTitledBorder(null, Lingua.getMensagem("dados_plano"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, FonteFinancas, new java.awt.Color(0, 102, 204)));
        JPanelProduto.setForeground(new java.awt.Color(51, 94, 168));
        JPanelProduto.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        JPanelProduto.setLayout(null);

        DATA.setFont(FonteFinancas);
        DATA.setForeground(new java.awt.Color(51, 94, 168));
        DATA.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        DATA.setText(Lingua.getMensagem("moeda"));
        JPanelProduto.add(DATA);
        DATA.setBounds(110, 100, 30, 20);

        DATA2.setFont(FonteFinancas);
        DATA2.setForeground(new java.awt.Color(51, 94, 168));
        DATA2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        DATA2.setText(Lingua.getMensagem("mes")+":");
        JPanelProduto.add(DATA2);
        DATA2.setBounds(10, 40, 90, 20);

        DATA3.setFont(FonteFinancas);
        DATA3.setForeground(new java.awt.Color(51, 94, 168));
        DATA3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        DATA3.setText(Lingua.getMensagem("ano")+":");
        JPanelProduto.add(DATA3);
        DATA3.setBounds(10, 70, 90, 20);

        pvalor.setFont(FonteFinancas);
        JPanelProduto.add(pvalor);
        pvalor.setBounds(140, 100, 60, 20);
        pvalor.setDocument(new Biblioteca.NotacaoInternacional());

        pano.setFont(FonteFinancas);
        JPanelProduto.add(pano);
        pano.setBounds(110, 70, 90, 20);

        pmes.setFont(FonteFinancas);
        pmes.setModel(new javax.swing.DefaultComboBoxModel(new String[] { Lingua.getMensagem("selecione"), "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" }));
        JPanelProduto.add(pmes);
        pmes.setBounds(110, 40, 90, 20);

        DATA1.setFont(FonteFinancas);
        DATA1.setForeground(new java.awt.Color(51, 94, 168));
        DATA1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        DATA1.setText(Lingua.getMensagem("valor")+":");
        JPanelProduto.add(DATA1);
        DATA1.setBounds(10, 100, 90, 20);

        BItens.setFont(FonteFinancas);
        BItens.setText(Lingua.getMensagem("itens"));
        BItens.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BItensActionPerformed(evt);
            }
        });
        JPanelProduto.add(BItens);
        BItens.setBounds(110, 130, 90, 20);

        BFinalizar.setFont(FonteFinancas);
        BFinalizar.setText(Lingua.getMensagem("aguardando_acao"));
        BFinalizar.setFocusPainted(false);
        BFinalizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BFinalizarActionPerformed(evt);
            }
        });
        JPanelProduto.add(BFinalizar);
        BFinalizar.setBounds(110, 160, 90, 20);

        getContentPane().add(JPanelProduto);
        JPanelProduto.setBounds(20, 60, 280, 210);

        setSize(new java.awt.Dimension(327, 314));
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
    }else if(Acao.equals(Lingua.getMensagem("duplicar"))){
        if(ValidarFormulario() && Janelinha.Pergunta(Lingua.getMensagem("duplicar"),Lingua.getMensagem("tem_certeza")))
        {
            Duplicar();
        }
    }
}//GEN-LAST:event_BFinalizarActionPerformed

private void BItensActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BItensActionPerformed
    new PlanejamentoComponentes(Principal,this,id_planejamento,Valor,(String)pmes.getSelectedItem(),pano.getText()).setVisible(true);
}//GEN-LAST:event_BItensActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BFinalizar;
    private javax.swing.JButton BItens;
    private javax.swing.JToolBar BarraDeFerramentas;
    private javax.swing.JLabel DATA;
    private javax.swing.JLabel DATA1;
    private javax.swing.JLabel DATA2;
    private javax.swing.JLabel DATA3;
    private javax.swing.JPanel JPanelProduto;
    private javax.swing.JTextField pano;
    private javax.swing.JComboBox pmes;
    private javax.swing.JTextField pvalor;
    // End of variables declaration//GEN-END:variables

}
