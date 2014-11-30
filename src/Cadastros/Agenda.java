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
import Biblioteca.Calendario;
import Biblioteca.Datas;
import Biblioteca.Formularios;
import Biblioteca.Janela;
import Biblioteca.Janelinha;
import Biblioteca.NotacaoInternacional;
import Biblioteca.Visual;
import Principal.TelaPrincipal;
import java.sql.SQLException;
import java.util.Calendar;

public final class Agenda extends Janela {

    private String Acao;
    private String id_agenda;
    private final TelaPrincipal Principal;

    public Agenda(TelaPrincipal principal) {
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

    public void AdicionarItem(String tipo_nome)
    {
        tipo.removeAllItems();
        tipo.addItem(tipo_nome);
    }

    public void PreencherItens()
    {
        try
        {
            Banco.executeQuery("SELECT nome FROM agenda_tipos ORDER BY nome ASC");
            tipo.addItem(Lingua.getMensagem("selecione"));
            while(Banco.getResultSet().next())
            {
                tipo.addItem(Banco.getResultSet().getString("nome"));
            }
        }catch(SQLException e)
        {
            tipo.addItem(Lingua.getMensagem("nenhum_item_encontrado"));
        }
    }

    public void PreencherFormulario(String acao, String agenda_id)
    {
        Acao = acao;
        id_agenda = agenda_id;
        Formularios.LimparFormulario(JPanelAgenda);
        BFinalizar.setVisible(true);
        tipo.removeAllItems();
        if(Acao==null)
        {
            BFinalizar.setText(Lingua.getMensagem("aguardando_acao"));
            BFinalizar.setEnabled(false);
            Formularios.DesativarFormulario(JPanelAgenda);
        }else{
            BFinalizar.setText(Acao);
            BFinalizar.setEnabled(true);
            Formularios.AtivarFormulario(JPanelAgenda);
            if(acao.equals(Lingua.getMensagem("cadastrar")))
            {
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
                Banco.executeQuery("SELECT agenda.*, TO_CHAR(agenda.data,'DD/MM/YYYY') AS data_exibir, agenda_tipos.nome AS tipo_nome FROM agenda, agenda_tipos WHERE id_agenda='"+id_agenda+"' AND agenda.id_tipo=agenda_tipos.id_tipo");
                try{
                    Banco.getResultSet().next();
                    tipo.addItem(Banco.getResultSet().getString("tipo_nome"));
                    descricao.setText(Banco.getResultSet().getString("descricao"));
                    valor.setText(Banco.getResultSet().getString("valor"));
                    data.setSelectedItem(Banco.getResultSet().getString("data_exibir"));
                }catch(SQLException e){
                    Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("excecao")+" "+e);
                }
            }
        }
    }

    public void Cadastrar()
    {
        // id_tipo
        String id_tipo="0";
        Banco.executeQuery("SELECT id_tipo FROM agenda_tipos WHERE nome='"+tipo.getSelectedItem()+"'");
        try{
            Banco.getResultSet().next();
            id_tipo = Banco.getResultSet().getString("id_tipo");
        }catch(SQLException e){
            Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("excecao")+" "+e);
        }
        // cadastrando
        try{
            Banco.executeUpdate("INSERT INTO agenda (id_tipo, descricao, valor, data) VALUES ('"+id_tipo+"','"+descricao.getText()+"','"+valor.getText()+"','"+Datas.ConverterData(data.getSelectedItem().toString())+"')");
            Janelinha.Aviso(Lingua.getMensagem("cadastrar"),Lingua.getMensagem("sucesso"));
            Principal.AtualizarAgenda();
            PreencherFormulario(Lingua.getMensagem("cadastrar"),null);
        }catch(Exception e){
            Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("excecao")+" "+e);
        }
    }
    
    public void Alterar()
    {
        try{
            Banco.executeUpdate("UPDATE agenda SET valor='"+valor.getText()+"', descricao='"+descricao.getText()+"', data='"+Datas.ConverterData(data.getSelectedItem().toString())+"' WHERE id_agenda='"+id_agenda+"'");
            Janelinha.Aviso(Lingua.getMensagem("alterar"),Lingua.getMensagem("sucesso"));
            Principal.AtualizarAgenda();
            PreencherFormulario(null,null);
        }catch(Exception e){
            Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("excecao")+" "+e);
        }
    }

    public void Excluir()
    {
        try{
            if(Banco.executeUpdate("DELETE FROM agenda WHERE id_agenda='"+id_agenda+"'")>0)
            {
                Janelinha.Aviso(Lingua.getMensagem("excluir"),Lingua.getMensagem("sucesso"));
                Principal.AtualizarAgenda();
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
            new AgendaTipoPesquisar(null,this,"").setVisible(true);
        }else if(acao.equals(Lingua.getMensagem("alterar"))){
            new AgendaPesquisar(Principal,this,Lingua.getMensagem("alterar")).setVisible(true);
        }else if(acao.equals(Lingua.getMensagem("excluir"))){
            new AgendaPesquisar(Principal,this,Lingua.getMensagem("excluir")).setVisible(true);
        }else if(acao.equals(Lingua.getMensagem("consultar"))){
            new AgendaPesquisar(Principal,this,Lingua.getMensagem("consultar")).setVisible(true);
        }else if(acao.equals(Lingua.getMensagem("sair"))){
            Agenda.this.dispose();
        }
    }

    public boolean ValidarFormulario()
    {
        if(tipo.getSelectedItem().equals(Lingua.getMensagem("selecione")))
        {
            Janelinha.Aviso(Lingua.getMensagem("erro"), Lingua.getMensagem("campo_nao_informado")+" "+Lingua.getMensagem("tipo"));
            tipo.requestFocus(true);
            return false;
        }else if (valor.getText().equals(""))
        {
            Janelinha.Aviso(Lingua.getMensagem("erro"), Lingua.getMensagem("campo_nao_informado")+" "+Lingua.getMensagem("valor"));
            valor.requestFocus(true);
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
        JPanelAgenda = new javax.swing.JPanel();
        DATA = new javax.swing.JLabel();
        DATA2 = new javax.swing.JLabel();
        DATA3 = new javax.swing.JLabel();
        valor = new javax.swing.JTextField();
        descricao = new javax.swing.JTextField();
        BItemPesquisar = new javax.swing.JButton();
        tipo = new javax.swing.JComboBox();
        DATA1 = new javax.swing.JLabel();
        DATA5 = new javax.swing.JLabel();
        BFinalizar = new javax.swing.JButton();
        data = new Calendario(false);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(Lingua.getMensagem("agenda")+" > "+Lingua.getMensagem("lembrete"));
        setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        setForeground(java.awt.Color.cyan);
        getContentPane().setLayout(null);

        BarraDeFerramentas.setFloatable(false);
        BarraDeFerramentas.setRollover(true);
        getContentPane().add(BarraDeFerramentas);
        BarraDeFerramentas.setBounds(0, 0, 470, 41);

        JPanelAgenda.setBorder(javax.swing.BorderFactory.createTitledBorder(null, Lingua.getMensagem("dados_lembrete"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, FonteFinancas, new java.awt.Color(0, 102, 204)));
        JPanelAgenda.setForeground(new java.awt.Color(51, 94, 168));
        JPanelAgenda.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        JPanelAgenda.setLayout(null);

        DATA.setFont(FonteFinancas);
        DATA.setForeground(new java.awt.Color(51, 94, 168));
        DATA.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        DATA.setText(Lingua.getMensagem("moeda"));
        JPanelAgenda.add(DATA);
        DATA.setBounds(110, 100, 20, 20);

        DATA2.setFont(FonteFinancas);
        DATA2.setForeground(new java.awt.Color(51, 94, 168));
        DATA2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        DATA2.setText(Lingua.getMensagem("tipo")+":");
        JPanelAgenda.add(DATA2);
        DATA2.setBounds(20, 40, 80, 20);

        DATA3.setFont(FonteFinancas);
        DATA3.setForeground(new java.awt.Color(51, 94, 168));
        DATA3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        DATA3.setText(Lingua.getMensagem("descricao")+":");
        JPanelAgenda.add(DATA3);
        DATA3.setBounds(20, 70, 80, 20);

        valor.setFont(FonteFinancas);
        JPanelAgenda.add(valor);
        valor.setBounds(130, 100, 70, 20);
        valor.setDocument(new Biblioteca.NotacaoInternacional());

        descricao.setFont(FonteFinancas);
        JPanelAgenda.add(descricao);
        descricao.setBounds(110, 70, 220, 20);

        BItemPesquisar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagens/outros/pesquisar.png"))); // NOI18N
        BItemPesquisar.setBorderPainted(false);
        BItemPesquisar.setContentAreaFilled(false);
        BItemPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BItemPesquisarActionPerformed(evt);
            }
        });
        JPanelAgenda.add(BItemPesquisar);
        BItemPesquisar.setBounds(330, 40, 30, 20);

        tipo.setFont(FonteFinancas);
        JPanelAgenda.add(tipo);
        tipo.setBounds(110, 40, 220, 20);

        DATA1.setFont(FonteFinancas);
        DATA1.setForeground(new java.awt.Color(51, 94, 168));
        DATA1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        DATA1.setText(Lingua.getMensagem("valor")+":");
        JPanelAgenda.add(DATA1);
        DATA1.setBounds(20, 100, 80, 20);

        DATA5.setFont(FonteFinancas);
        DATA5.setForeground(new java.awt.Color(51, 94, 168));
        DATA5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        DATA5.setText(Lingua.getMensagem("data")+":");
        JPanelAgenda.add(DATA5);
        DATA5.setBounds(20, 130, 80, 20);

        BFinalizar.setFont(FonteFinancas);
        BFinalizar.setText(Lingua.getMensagem("aguardando_acao"));
        BFinalizar.setFocusPainted(false);
        BFinalizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BFinalizarActionPerformed(evt);
            }
        });
        JPanelAgenda.add(BFinalizar);
        BFinalizar.setBounds(110, 160, 120, 20);

        data.setFont(FonteFinancas);
        JPanelAgenda.add(data);
        data.setBounds(110, 130, 120, 20);

        getContentPane().add(JPanelAgenda);
        JPanelAgenda.setBounds(20, 60, 377, 210);

        setSize(new java.awt.Dimension(425, 313));
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
    private javax.swing.JLabel DATA;
    private javax.swing.JLabel DATA1;
    private javax.swing.JLabel DATA2;
    private javax.swing.JLabel DATA3;
    private javax.swing.JLabel DATA5;
    private javax.swing.JPanel JPanelAgenda;
    private javax.swing.JComboBox data;
    private javax.swing.JTextField descricao;
    private javax.swing.JComboBox tipo;
    private javax.swing.JTextField valor;
    // End of variables declaration//GEN-END:variables

}
