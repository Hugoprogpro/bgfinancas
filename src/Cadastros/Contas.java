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
import Biblioteca.Numeros;
import Biblioteca.Validar;
import Biblioteca.Visual;
import Principal.TelaPrincipal;

public class Contas extends Janela {

    private String Acao;
    private String id_conta;
    private TelaPrincipal Principal;

    public Contas(TelaPrincipal principal) {
        initComponents();
        Visual.Janela(this.getJanela());
        BarraDeFerramentas.add(Botoes.Cadastrar(this));
        BarraDeFerramentas.add(Botoes.Alterar(this));
        BarraDeFerramentas.add(Botoes.Excluir(this));
        BarraDeFerramentas.add(Botoes.Consultar(this));
        BarraDeFerramentas.add(Botoes.Sair(this));
        Principal = principal;
        PreencherFormulario(null,null,null,null,null,null);
    }
    
    public void PreencherFormulario(String acao, String conta_id, String nome_conta, String valor, String Ativado, String Saldo_total)
    {
        Acao = acao;
        id_conta = conta_id;
        Formularios.LimparFormulario(JPanelConta);
        conta_nome.requestFocus();
        BFinalizar.setVisible(true);
        if(Acao==null)
        {
            BFinalizar.setText(Lingua.getMensagem("aguardando_acao"));
            Formularios.DesativarFormulario(JPanelConta);
        }else{
            Formularios.AtivarFormulario(JPanelConta);
            BFinalizar.setText(Acao);
            if(Acao.equals(Lingua.getMensagem("cadastrar")))
            {
                //
            }else{
                ativar.setSelectedItem(Ativado);
                saldo_total.setSelectedItem(Saldo_total);
                conta_nome.setText(nome_conta);
                saldo_atual.setText(valor);
                if(Acao.equals(Lingua.getMensagem("consultar")))
                {
                    BFinalizar.setVisible(false);
                }
            }
        }
    }
    
    public boolean ValidarFormulario()
    {
        if(conta_nome.getText().equals("")) {
            Janelinha.Aviso(Lingua.getMensagem("atencao"),Lingua.getMensagem("campo_nao_informado")+" "+Lingua.getMensagem("conta"));
            conta_nome.requestFocus(true);
            return false;
        }else if(Validar.Numero(saldo_atual.getText())) {
            Janelinha.Aviso(Lingua.getMensagem("atencao"),Lingua.getMensagem("saldo_inicial_invalido"));
            saldo_atual.requestFocus(true);
            return false;
        }else if(Acao.equals(Lingua.getMensagem("cadastrar")) && saldo_atual.getText().equals(""))
        {
            Janelinha.Aviso(Lingua.getMensagem("atencao"),Lingua.getMensagem("campo_nao_informado")+" "+Lingua.getMensagem("saldo_atual"));
            saldo_atual.requestFocus(true);
            return false;
        }else{
            return true;
        }
    }
    
    public void Cadastrar()
    {
        int ativada;
        if(ativar.getSelectedItem().equals(Lingua.getMensagem("sim")))
            ativada = 0;
        else
            ativada = 1;
        int saldototal;
        if(saldo_total.getSelectedItem().equals(Lingua.getMensagem("sim")))
            saldototal = 0;
        else
            saldototal = 1;
        try{
            Banco.executeUpdate("INSERT INTO contas (nome,valor,ativada,saldo_total) VALUES ('"+conta_nome.getText()+"','"+Numeros.Arrendondar(saldo_atual.getText())+"','"+ativada+"','"+saldototal+"')");
            Janelinha.Aviso(Lingua.getMensagem("cadastrar"),Lingua.getMensagem("sucesso"));
            Principal.ContasSaldo();
            PreencherFormulario(Lingua.getMensagem("cadastrar"),null,null,null,null,null);
        }catch(Exception e){
            Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("excecao")+" "+e);
        }
    }
    
    public void Alterar()
    {
        int ativada;
        if(ativar.getSelectedItem().equals(Lingua.getMensagem("sim")))
            ativada = 0;
        else
            ativada = 1;
        int saldototal;
        if(saldo_total.getSelectedItem().equals(Lingua.getMensagem("sim")))
            saldototal = 0;
        else
            saldototal = 1;
        try{
            Banco.executeUpdate("UPDATE contas SET nome='"+conta_nome.getText()+"', valor='"+saldo_atual.getText()+"', ativada='"+ativada+"', saldo_total='"+saldototal+"' WHERE id_conta='"+id_conta+"'");
            Janelinha.Aviso(Lingua.getMensagem("alterar"),Lingua.getMensagem("sucesso"));
            Principal.ContasSaldo();
            PreencherFormulario(null,null,null,null,null,null);
        }catch(Exception e){
            Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("excecao")+" "+e);
        }
    }
    
    public void Excluir()
    {    
        try{
            Banco.executeQuery("SELECT * FROM despesas WHERE id_conta='"+id_conta+"'");
            if(Banco.getResultSet().next())
            {
                Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("restricao_excluir"));
                PreencherFormulario(null,null,null,null,null,null);
            }else{
                Banco.executeQuery("SELECT * FROM receitas WHERE id_conta='"+id_conta+"'");
                if(Banco.getResultSet().next())
                {
                    Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("restricao_excluir"));
                    PreencherFormulario(null,null,null,null,null,null);
                }else{
                    Banco.executeQuery("SELECT * FROM transferencias WHERE id_conta1='"+id_conta+"' OR id_conta2='"+id_conta+"'");
                    if(Banco.getResultSet().next())
                    {
                        Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("restricao_excluir"));
                        PreencherFormulario(null,null,null,null,null,null);
                    }else{
                        if(Banco.executeUpdate("DELETE FROM contas WHERE id_conta='"+id_conta+"'")>0)
                        {
                            Janelinha.Aviso(Lingua.getMensagem("excluir"),Lingua.getMensagem("sucesso"));
                            PreencherFormulario(null,null,null,null,null,null);
                            Principal.ContasSaldo();
                        }else{
                            PreencherFormulario(null,null,null,null,null,null);
                        }
                    }
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
            PreencherFormulario(Lingua.getMensagem("cadastrar"),null,null,null,null,null);
        }else if(acao.equals(Lingua.getMensagem("alterar"))){
            new ContasPesquisar(this,Lingua.getMensagem("alterar")).setVisible(true);
        }else if(acao.equals(Lingua.getMensagem("excluir"))){
            new ContasPesquisar(this,Lingua.getMensagem("excluir")).setVisible(true);
        }else if(acao.equals(Lingua.getMensagem("consultar"))){
            new ContasPesquisar(this,Lingua.getMensagem("consultar")).setVisible(true);
        }else if(acao.equals(Lingua.getMensagem("sair"))){
            Contas.this.dispose();
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
        JPanelConta = new javax.swing.JPanel();
        NOME = new javax.swing.JLabel();
        conta_nome = new javax.swing.JTextField();
        BFinalizar = new javax.swing.JButton();
        saldo_atual = new javax.swing.JTextField();
        NOME2 = new javax.swing.JLabel();
        ativar = new javax.swing.JComboBox();
        NOME3 = new javax.swing.JLabel();
        NOME4 = new javax.swing.JLabel();
        NOME5 = new javax.swing.JLabel();
        saldo_total = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(Lingua.getMensagem("contas"));
        setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        setForeground(java.awt.Color.cyan);
        getContentPane().setLayout(null);

        BarraDeFerramentas.setFloatable(false);
        BarraDeFerramentas.setRollover(true);
        getContentPane().add(BarraDeFerramentas);
        BarraDeFerramentas.setBounds(0, 0, 490, 41);

        JPanelConta.setBorder(javax.swing.BorderFactory.createTitledBorder(null, Lingua.getMensagem("dados_conta"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, FonteFinancas, new java.awt.Color(0, 102, 204)));
        JPanelConta.setForeground(new java.awt.Color(51, 94, 168));
        JPanelConta.setLayout(null);

        NOME.setFont(FonteFinancas);
        NOME.setForeground(new java.awt.Color(51, 94, 168));
        NOME.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        NOME.setText(Lingua.getMensagem("nome")+":");
        JPanelConta.add(NOME);
        NOME.setBounds(8, 30, 130, 20);

        conta_nome.setFont(FonteFinancas);
        JPanelConta.add(conta_nome);
        conta_nome.setBounds(150, 30, 200, 20);

        BFinalizar.setFont(FonteFinancas);
        BFinalizar.setText(Lingua.getMensagem("aguardando_acao"));
        BFinalizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BFinalizarActionPerformed(evt);
            }
        });
        JPanelConta.add(BFinalizar);
        BFinalizar.setBounds(150, 150, 100, 20);

        saldo_atual.setFont(FonteFinancas);
        JPanelConta.add(saldo_atual);
        saldo_atual.setBounds(170, 60, 80, 20);
        saldo_atual.setDocument(new Biblioteca.NotacaoInternacional());

        NOME2.setFont(FonteFinancas);
        NOME2.setForeground(new java.awt.Color(51, 94, 168));
        NOME2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        NOME2.setText(Lingua.getMensagem("moeda"));
        JPanelConta.add(NOME2);
        NOME2.setBounds(150, 60, 20, 20);

        ativar.setFont(FonteFinancas);
        ativar.setModel(new javax.swing.DefaultComboBoxModel(new String[] { Lingua.getMensagem("sim"), Lingua.getMensagem("nao") }));
        JPanelConta.add(ativar);
        ativar.setBounds(150, 120, 100, 20);

        NOME3.setFont(FonteFinancas);
        NOME3.setForeground(new java.awt.Color(51, 94, 168));
        NOME3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        NOME3.setText(Lingua.getMensagem("ativada")+":");
        JPanelConta.add(NOME3);
        NOME3.setBounds(10, 120, 130, 20);

        NOME4.setFont(FonteFinancas);
        NOME4.setForeground(new java.awt.Color(51, 94, 168));
        NOME4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        NOME4.setText(Lingua.getMensagem("saldo_atual")+":");
        JPanelConta.add(NOME4);
        NOME4.setBounds(8, 60, 130, 20);

        NOME5.setFont(FonteFinancas);
        NOME5.setForeground(new java.awt.Color(51, 94, 168));
        NOME5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        NOME5.setText(Lingua.getMensagem("saldo_total")+":");
        JPanelConta.add(NOME5);
        NOME5.setBounds(10, 90, 130, 20);

        saldo_total.setFont(FonteFinancas);
        saldo_total.setModel(new javax.swing.DefaultComboBoxModel(new String[] { Lingua.getMensagem("sim"), Lingua.getMensagem("nao") }));
        JPanelConta.add(saldo_total);
        saldo_total.setBounds(150, 90, 100, 20);

        getContentPane().add(JPanelConta);
        JPanelConta.setBounds(20, 60, 390, 200);

        setSize(new java.awt.Dimension(441, 302));
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
    private javax.swing.JPanel JPanelConta;
    private javax.swing.JLabel NOME;
    private javax.swing.JLabel NOME2;
    private javax.swing.JLabel NOME3;
    private javax.swing.JLabel NOME4;
    private javax.swing.JLabel NOME5;
    private javax.swing.JComboBox ativar;
    private javax.swing.JTextField conta_nome;
    private javax.swing.JTextField saldo_atual;
    private javax.swing.JComboBox saldo_total;
    // End of variables declaration//GEN-END:variables

}
