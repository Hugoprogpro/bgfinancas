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

public final class DespesasCategoria extends Janela {

    private String Acao;
    private String id_categoria;

    public DespesasCategoria() {
        initComponents();
        Visual.Janela(this.getJanela());
        BarraDeFerramentas.add(Botoes.Cadastrar(this));
        BarraDeFerramentas.add(Botoes.Alterar(this));
        BarraDeFerramentas.add(Botoes.Excluir(this));
        BarraDeFerramentas.add(Botoes.Consultar(this));
        BarraDeFerramentas.add(Botoes.Sair(this));
        PreencherFormulario(null,null,null);
    }
    
    public void PreencherFormulario(String acao, String categoria_id, String nome_categoria)
    {
        Acao = acao;
        id_categoria = categoria_id;
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
                nome.setText(nome_categoria);
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
            Banco.executeUpdate("INSERT INTO despesas_categorias (nome) VALUES('"+nome.getText()+"')");
            Janelinha.Aviso(Lingua.getMensagem("cadastrar"),Lingua.getMensagem("sucesso"));
            PreencherFormulario(Lingua.getMensagem("cadastrar"),null,null);
        }catch(Exception e){
            Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("excecao")+" "+e);
        }
    }
    
    public void Alterar()
    {     
        try{
            Banco.executeUpdate("UPDATE despesas_categorias SET nome='"+nome.getText()+"' WHERE id_categoria='"+id_categoria+"'");
            Janelinha.Aviso(Lingua.getMensagem("alterar"),Lingua.getMensagem("sucesso"));
            PreencherFormulario(null,null,null);
        }catch(Exception e){
            Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("excecao")+" "+e);
        }
    }
    
    public void Excluir()
    {    
        try{
            Banco.executeQuery("SELECT * FROM despesas_itens WHERE id_categoria='"+id_categoria+"'");
            if(Banco.getResultSet().next())
            {
                Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("restricao_excluir"));
                PreencherFormulario(null,null,null);
            }else{
                if(Banco.executeUpdate("DELETE FROM despesas_categorias WHERE id_categoria='"+id_categoria+"'")>0)
                {
                    Janelinha.Aviso(Lingua.getMensagem("excluir"),Lingua.getMensagem("sucesso"));
                    PreencherFormulario(null,null,null);
                }else{
                    PreencherFormulario(null,null,null);
                }
            }
        }catch(Exception e){
            Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("excecao")+" "+e);
        }
    }
    
    @Override
    public void Botoes(String acao)
    {
        if(acao.equals(Lingua.getMensagem("cadastrar"))){
            PreencherFormulario(Lingua.getMensagem("cadastrar"),null,null);
        }else if(acao.equals(Lingua.getMensagem("alterar"))){
            new DespesasCategoriaPesquisar(this,Lingua.getMensagem("alterar")).setVisible(true);
        }else if(acao.equals(Lingua.getMensagem("excluir"))){
            new DespesasCategoriaPesquisar(this,Lingua.getMensagem("excluir")).setVisible(true);
        }else if(acao.equals(Lingua.getMensagem("consultar"))){
            new DespesasCategoriaPesquisar(this,Lingua.getMensagem("consultar")).setVisible(true);
        }else if(acao.equals(Lingua.getMensagem("sair"))){
            DespesasCategoria.this.dispose();
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
        PainelCategoria = new javax.swing.JPanel();
        NOME = new javax.swing.JLabel();
        nome = new javax.swing.JTextField();
        BFinalizar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(Lingua.getMensagem("despesas")+" > "+Lingua.getMensagem("categorias"));
        setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        setForeground(java.awt.Color.cyan);
        setResizable(false);
        getContentPane().setLayout(null);

        BarraDeFerramentas.setFloatable(false);
        BarraDeFerramentas.setRollover(true);
        getContentPane().add(BarraDeFerramentas);
        BarraDeFerramentas.setBounds(0, 0, 560, 41);

        PainelCategoria.setBorder(javax.swing.BorderFactory.createTitledBorder(null, Lingua.getMensagem("dados_categoria"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, FonteFinancas, new java.awt.Color(0, 102, 204)));
        PainelCategoria.setForeground(new java.awt.Color(51, 94, 168));
        PainelCategoria.setLayout(null);

        NOME.setFont(FonteFinancas);
        NOME.setForeground(new java.awt.Color(51, 94, 168));
        NOME.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        NOME.setText(Lingua.getMensagem("nome")+":");
        PainelCategoria.add(NOME);
        NOME.setBounds(15, 30, 100, 20);

        nome.setFont(FonteFinancas);
        PainelCategoria.add(nome);
        nome.setBounds(120, 30, 243, 20);

        BFinalizar.setFont(FonteFinancas);
        BFinalizar.setText("Selecione...");
        BFinalizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BFinalizarActionPerformed(evt);
            }
        });
        PainelCategoria.add(BFinalizar);
        BFinalizar.setBounds(120, 60, 110, 20);

        getContentPane().add(PainelCategoria);
        PainelCategoria.setBounds(20, 60, 392, 103);

        setSize(new java.awt.Dimension(442, 204));
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
    private javax.swing.JLabel NOME;
    private javax.swing.JPanel PainelCategoria;
    private javax.swing.JTextField nome;
    // End of variables declaration//GEN-END:variables

}
