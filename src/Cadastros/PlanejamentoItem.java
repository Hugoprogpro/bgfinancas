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

public final class PlanejamentoItem extends Janela {

    private String Acao;
    private String id_item;

    public PlanejamentoItem() {
        initComponents();
        Visual.Janela(this.getJanela());
        BarraDeFerramentas.add(Botoes.Cadastrar(this));
        BarraDeFerramentas.add(Botoes.Alterar(this));
        BarraDeFerramentas.add(Botoes.Excluir(this));
        BarraDeFerramentas.add(Botoes.Consultar(this));
        BarraDeFerramentas.add(Botoes.Sair(this));
        PreencherFormulario(null,null,null);
    }
    
    public void PreencherFormulario(String acao, String item_id, String nome_item)
    {
        Acao = acao;
        id_item = item_id;
        nome.requestFocus();
        nome.setText("");
        BFinalizar.setVisible(true);
        if(Acao==null)
        {
            BFinalizar.setText(Lingua.getMensagem("aguardando_acao"));
            nome.setEnabled(false);
            BFinalizar.setEnabled(false);
        }else{
            nome.setEnabled(true);
            BFinalizar.setEnabled(true);
            BFinalizar.setText(Acao);
            if(Acao.equals(Lingua.getMensagem("cadastrar")))
            {
                //
            }else{
                nome.setText(nome_item);
                if(Acao.equals(Lingua.getMensagem("consultar")))
                {
                    BFinalizar.setVisible(false);
                }
            }
        }
    }
    
    public boolean ValidarFormulario()
    {
        if(nome.getText().equals(""))
        {
            Janelinha.Aviso(Lingua.getMensagem("atencao"),Lingua.getMensagem("campo_nao_informado")+" "+Lingua.getMensagem("nome"));
            nome.requestFocus(true);
            return false;
        }else{
            return true;
        }
    }
    
    public void Cadastrar()
    {    
        try{
            Banco.executeUpdate("INSERT INTO planejamento_itens (nome) VALUES('"+nome.getText()+"')");
            Janelinha.Aviso(Lingua.getMensagem("cadastrar"),Lingua.getMensagem("sucesso"));
            PreencherFormulario(Lingua.getMensagem("cadastrar"),null,null);
        }catch(Exception e){
            Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("excecao")+" "+e);
        }
    }
    
    public void Alterar()
    {     
        try{
            Banco.executeUpdate("UPDATE planejamento_itens SET nome='"+nome.getText()+"' WHERE id_item='"+id_item+"'");
            Janelinha.Aviso(Lingua.getMensagem("alterar"),Lingua.getMensagem("sucesso"));
            PreencherFormulario(null,null,null);
        }catch(Exception e){
            Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("excecao")+" "+e);
        }
    }
    
    public void Excluir()
    {    
        try{
            Banco.executeQuery("SELECT * FROM planejamento_componentes WHERE id_item='"+id_item+"'");
            if(Banco.getResultSet().next())
            {
                Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("restricao_excluir"));
                PreencherFormulario(null,null,null);
            }else{
                if(Banco.executeUpdate("DELETE FROM planejamento_itens WHERE id_item='"+id_item+"'")>0)
                {
                    Janelinha.Aviso(Lingua.getMensagem("excluir"),Lingua.getMensagem("sucesso"));
                    PreencherFormulario(null,null,null);
                }else{
                    PreencherFormulario(null,null,null);
                }
            }
        }catch(SQLException e){
            Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("excecao")+" "+e);
        }
    }
    
    @Override
    public void Botoes(String acao)
    {
        if(acao.equals(Lingua.getMensagem("cadastrar"))){
            PreencherFormulario(Lingua.getMensagem("cadastrar"),null,null);
        }else if(acao.equals(Lingua.getMensagem("alterar"))){
            new PlanejamentoItemPesquisar(this,Lingua.getMensagem("alterar")).setVisible(true);
        }else if(acao.equals(Lingua.getMensagem("excluir"))){
            new PlanejamentoItemPesquisar(this,Lingua.getMensagem("excluir")).setVisible(true);
        }else if(acao.equals(Lingua.getMensagem("consultar"))){
            new PlanejamentoItemPesquisar(this,Lingua.getMensagem("consultar")).setVisible(true);
        }else if(acao.equals(Lingua.getMensagem("sair"))){
            PlanejamentoItem.this.dispose();
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
        NOME = new javax.swing.JLabel();
        nome = new javax.swing.JTextField();
        BFinalizar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(Lingua.getMensagem("planejamento")+" > "+Lingua.getMensagem("item"));
        setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        setForeground(java.awt.Color.cyan);
        getContentPane().setLayout(null);

        BarraDeFerramentas.setFloatable(false);
        BarraDeFerramentas.setRollover(true);
        getContentPane().add(BarraDeFerramentas);
        BarraDeFerramentas.setBounds(0, 0, 520, 41);

        JPanelItem.setBorder(javax.swing.BorderFactory.createTitledBorder(null, Lingua.getMensagem("dados_item"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, FonteFinancas, new java.awt.Color(0, 102, 204)));
        JPanelItem.setForeground(new java.awt.Color(51, 94, 168));
        JPanelItem.setLayout(null);

        NOME.setFont(FonteFinancas);
        NOME.setForeground(new java.awt.Color(51, 94, 168));
        NOME.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        NOME.setText(Lingua.getMensagem("nome")+":");
        JPanelItem.add(NOME);
        NOME.setBounds(20, 40, 80, 20);

        nome.setFont(FonteFinancas);
        JPanelItem.add(nome);
        nome.setBounds(110, 40, 243, 20);

        BFinalizar.setFont(FonteFinancas);
        BFinalizar.setText(Lingua.getMensagem("aguardando_acao"));
        BFinalizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BFinalizarActionPerformed(evt);
            }
        });
        JPanelItem.add(BFinalizar);
        BFinalizar.setBounds(110, 70, 110, 20);

        getContentPane().add(JPanelItem);
        JPanelItem.setBounds(20, 60, 400, 121);

        setSize(new java.awt.Dimension(445, 222));
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
    private javax.swing.JPanel JPanelItem;
    private javax.swing.JLabel NOME;
    private javax.swing.JTextField nome;
    // End of variables declaration//GEN-END:variables

}
