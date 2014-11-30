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
import Biblioteca.NotacaoInternacional;
import Biblioteca.Visual;
import Principal.TelaPrincipal;

public final class Grupos extends Janela {

    private String Acao;
    private String id_relatorios_grupos;
    private final TelaPrincipal Principal;

    public Grupos(TelaPrincipal _Principal) {
        initComponents();
        Principal = _Principal;
        Visual.Janela(this.getJanela());
        BarraDeFerramentas.add(Botoes.Cadastrar(this));
        BarraDeFerramentas.add(Botoes.Alterar(this));
        BarraDeFerramentas.add(Botoes.Excluir(this));
        BarraDeFerramentas.add(Botoes.Consultar(this));
        BarraDeFerramentas.add(Botoes.Sair(this));
        PreencherFormulario(null,null,null,null,null);
    }
    
    public void PreencherFormulario(String acao, String categoria_id, String nome_categoria, String Valor, String Saldo_total)
    {
        Acao = acao;
        id_relatorios_grupos = categoria_id;
        nome.requestFocus();
        nome.setText("");
        valor.setText("");
        ativado.setSelectedIndex(0);
        BFinalizar.setVisible(true);
        if(Acao==null)
        {
            BFinalizar.setText(Lingua.getMensagem("aguardando_acao"));
            nome.setEnabled(false);
            valor.setEnabled(false);
            ativado.setEnabled(false);
            BFinalizar.setEnabled(false);
        }else{
            nome.setEnabled(true);
            valor.setEnabled(true);
            ativado.setEnabled(true);
            BFinalizar.setEnabled(true);
            BFinalizar.setText(Acao);
            if(Acao.equals(Lingua.getMensagem("cadastrar")))
            {
                //
            }else{
                nome.setText(nome_categoria);
                valor.setText(Valor);
                ativado.setSelectedItem(Saldo_total);
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
        }else if(valor.getText().equals(""))
        {
            Janelinha.Aviso(Lingua.getMensagem("atencao"),Lingua.getMensagem("campo_nao_informado")+" "+Lingua.getMensagem("valor_reservado"));
            valor.requestFocus(true);
            return false;
        }else{
            return true;
        }
    }
    
    public void Cadastrar()
    {    
        try{
            int saldototal;
            if(ativado.getSelectedItem().equals(Lingua.getMensagem("sim")))
                saldototal = 0;
            else
                saldototal = 1;
            Banco.executeUpdate("INSERT INTO relatorios_grupos (nome,valor,saldo_total) VALUES('"+nome.getText()+"','"+valor.getText()+"','"+saldototal+"')");
            Janelinha.Aviso(Lingua.getMensagem("cadastrar"),Lingua.getMensagem("sucesso"));
            PreencherFormulario(Lingua.getMensagem("cadastrar"),null,null,null,null);
            Principal.RelatorioMensalGrupos();
        }catch(Exception e){
            Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("excecao")+" "+e);
        }
    }
    
    public void Alterar()
    {     
        try{
            int saldototal;
            if(ativado.getSelectedItem().equals(Lingua.getMensagem("sim")))
                saldototal = 0;
            else
                saldototal = 1;
            Banco.executeUpdate("UPDATE relatorios_grupos SET nome='"+nome.getText()+"', valor='"+valor.getText()+"', saldo_total='"+saldototal+"' WHERE id_relatorios_grupos='"+id_relatorios_grupos+"'");
            Janelinha.Aviso(Lingua.getMensagem("alterar"),Lingua.getMensagem("sucesso"));
            PreencherFormulario(null,null,null,null,null);
            Principal.RelatorioMensalGrupos();
        }catch(Exception e){
            Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("excecao")+" "+e);
        }
    }
    
    public void Excluir()
    {    
        try{
            Banco.executeQuery("SELECT * FROM relatorios_grupos_itens WHERE id_relatorios_grupos='"+id_relatorios_grupos+"'");
            if(Banco.getResultSet().next())
            {
                Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("restricao_excluir"));
                PreencherFormulario(null,null,null,null,null);
            }else{
                if(Banco.executeUpdate("DELETE FROM relatorios_grupos WHERE id_relatorios_grupos='"+id_relatorios_grupos+"'")>0)
                {
                    Janelinha.Aviso(Lingua.getMensagem("excluir"),Lingua.getMensagem("sucesso"));
                    PreencherFormulario(null,null,null,null,null);
                    Principal.RelatorioMensalGrupos();
                }else{
                    PreencherFormulario(null,null,null,null,null);
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
            PreencherFormulario(Lingua.getMensagem("cadastrar"),null,null,null,null);
        }else if(acao.equals(Lingua.getMensagem("alterar"))){
            new GruposPesquisar(this,Lingua.getMensagem("alterar")).setVisible(true);
        }else if(acao.equals(Lingua.getMensagem("excluir"))){
            new GruposPesquisar(this,Lingua.getMensagem("excluir")).setVisible(true);
        }else if(acao.equals(Lingua.getMensagem("consultar"))){
            new GruposPesquisar(this,Lingua.getMensagem("consultar")).setVisible(true);
        }else if(acao.equals(Lingua.getMensagem("sair"))){
            Grupos.this.dispose();
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
        JPanelGrupo = new javax.swing.JPanel();
        NOME = new javax.swing.JLabel();
        nome = new javax.swing.JTextField();
        BFinalizar = new javax.swing.JButton();
        NOME4 = new javax.swing.JLabel();
        NOME2 = new javax.swing.JLabel();
        valor = new javax.swing.JTextField();
        ativado = new javax.swing.JComboBox();
        NOME5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(Lingua.getMensagem("grupos")+" > "+Lingua.getMensagem("grupo"));
        setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        setForeground(java.awt.Color.cyan);
        getContentPane().setLayout(null);

        BarraDeFerramentas.setFloatable(false);
        BarraDeFerramentas.setRollover(true);
        getContentPane().add(BarraDeFerramentas);
        BarraDeFerramentas.setBounds(0, 0, 520, 41);

        JPanelGrupo.setBorder(javax.swing.BorderFactory.createTitledBorder(null, Lingua.getMensagem("dados_grupo"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, FonteFinancas, new java.awt.Color(0, 102, 204)));
        JPanelGrupo.setForeground(new java.awt.Color(51, 94, 168));
        JPanelGrupo.setLayout(null);

        NOME.setFont(FonteFinancas);
        NOME.setForeground(new java.awt.Color(51, 94, 168));
        NOME.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        NOME.setText(Lingua.getMensagem("nome")+":");
        JPanelGrupo.add(NOME);
        NOME.setBounds(10, 30, 150, 20);

        nome.setFont(FonteFinancas);
        JPanelGrupo.add(nome);
        nome.setBounds(170, 30, 243, 20);

        BFinalizar.setFont(FonteFinancas);
        BFinalizar.setText("Selecione...");
        BFinalizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BFinalizarActionPerformed(evt);
            }
        });
        JPanelGrupo.add(BFinalizar);
        BFinalizar.setBounds(170, 120, 110, 20);

        NOME4.setFont(FonteFinancas);
        NOME4.setForeground(new java.awt.Color(51, 94, 168));
        NOME4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        NOME4.setText(Lingua.getMensagem("valor_reservado")+":");
        JPanelGrupo.add(NOME4);
        NOME4.setBounds(10, 60, 150, 20);

        NOME2.setFont(FonteFinancas);
        NOME2.setForeground(new java.awt.Color(51, 94, 168));
        NOME2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        NOME2.setText(Lingua.getMensagem("moeda"));
        JPanelGrupo.add(NOME2);
        NOME2.setBounds(170, 60, 30, 20);

        valor.setFont(FonteFinancas);
        JPanelGrupo.add(valor);
        valor.setBounds(200, 60, 85, 20);
        valor.setDocument(new Biblioteca.NotacaoInternacional());

        ativado.setFont(FonteFinancas);
        ativado.setModel(new javax.swing.DefaultComboBoxModel(new String[] { Lingua.getMensagem("sim"), Lingua.getMensagem("nao") }));
        ativado.setSelectedItem(Lingua.getMensagem("sim"));
        JPanelGrupo.add(ativado);
        ativado.setBounds(170, 90, 110, 20);

        NOME5.setFont(FonteFinancas);
        NOME5.setForeground(new java.awt.Color(51, 94, 168));
        NOME5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        NOME5.setText(Lingua.getMensagem("ativado")+":");
        JPanelGrupo.add(NOME5);
        NOME5.setBounds(10, 90, 150, 20);

        getContentPane().add(JPanelGrupo);
        JPanelGrupo.setBounds(20, 60, 460, 165);

        setSize(new java.awt.Dimension(510, 266));
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
    private javax.swing.JPanel JPanelGrupo;
    private javax.swing.JLabel NOME;
    private javax.swing.JLabel NOME2;
    private javax.swing.JLabel NOME4;
    private javax.swing.JLabel NOME5;
    private javax.swing.JComboBox ativado;
    private javax.swing.JTextField nome;
    private javax.swing.JTextField valor;
    // End of variables declaration//GEN-END:variables

}
