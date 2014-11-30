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
import Biblioteca.JanelaModal;
import Biblioteca.Janelinha;
import Biblioteca.Secure;
import Biblioteca.Visual;
import Principal.TelaPrincipal;
import java.sql.SQLException;


public final class Usuarios extends JanelaModal {

    private String Acao;
    private String id_usuarios;
    private Boolean PrimeiroCadastro;
    private TelaPrincipal Principal;
    
    public Usuarios(boolean modal, TelaPrincipal _Principal, Boolean _PrimeiroCadastro) {
        super(_Principal, modal);
        initComponents();
        Visual.JanelaModal(this.getJanela());
        PrimeiroCadastro = _PrimeiroCadastro;
        Principal = _Principal;
        if(!PrimeiroCadastro){
            BarraDeFerramentas.add(Botoes.Cadastrar(this));
            BarraDeFerramentas.add(Botoes.Alterar(this));
            BarraDeFerramentas.add(Botoes.Excluir(this));
            BarraDeFerramentas.add(Botoes.Consultar(this));
        }
        BarraDeFerramentas.add(Botoes.Sair(this));
        PreencherFormulario(null,null);
    }

    public void PreencherFormulario(String acao, String _id_usuarios)
    {
        Acao = acao;
        id_usuarios = _id_usuarios;
        Formularios.LimparFormulario(PainelUsuarios);
        BFinalizar.setVisible(true);
        if(Acao==null)
        {
            BFinalizar.setText(Lingua.getMensagem("aguardando_acao"));
            Formularios.DesativarFormulario(PainelUsuarios);
        }else{
            nome.requestFocus();
            Formularios.AtivarFormulario(PainelUsuarios);
            BFinalizar.setText(Acao);
            if(!Acao.equals(Lingua.getMensagem("cadastrar"))){
                try
                {
                    Banco.executeQuery("SELECT nome,usuario FROM usuarios WHERE id_usuarios='"+id_usuarios+"'");
                    Banco.getResultSet().next();
                    nome.setText(Banco.getResultSet().getString("nome"));
                    usuario.setText(Banco.getResultSet().getString("usuario"));
                    senha.setEnabled(false);
                    senha2.setEnabled(false);
                }catch(SQLException e){
                    Janelinha.Aviso(Lingua.getMensagem("erro"), Lingua.getMensagem("excecao")+" "+e);
                }
                if(Acao.equals(Lingua.getMensagem("consultar")))
                {
                    BFinalizar.setVisible(false);
                }
            }
        }
    }
    
    public boolean ValidarFormulario()
    {
        if(usuario.getText().equals(""))
        {
            Janelinha.Aviso(Lingua.getMensagem("atencao"),Lingua.getMensagem("campo_nao_informado")+" "+Lingua.getMensagem("usuario"));
            usuario.requestFocus(true);
            return false;
        }else if(nome.getText().equals("")){
            Janelinha.Aviso(Lingua.getMensagem("atencao"),Lingua.getMensagem("campo_nao_informado")+" "+Lingua.getMensagem("nome"));
            nome.requestFocus(true);
            return false;
        }else if(String.valueOf(senha.getPassword()).equals("")){
            if(Acao.equals(Lingua.getMensagem("cadastrar")))
            {
                Janelinha.Aviso(Lingua.getMensagem("atencao"),Lingua.getMensagem("campo_nao_informado")+" "+Lingua.getMensagem("senha"));
                senha.requestFocus(true);
                return false;
            }else{
                return true;
            }
        }else{
            return true;
        }
    }
    
    public void Cadastrar()
    {
        if(String.valueOf(senha.getPassword()).equals(String.valueOf(senha2.getPassword())))
        {
            try{
                Banco.executeUpdate("INSERT INTO usuarios (nome,usuario,senha) VALUES('"+nome.getText()+"','"+usuario.getText()+"','"+Secure.md5(String.valueOf(senha.getPassword()))+"')");
                Janelinha.Aviso(Lingua.getMensagem("cadastrar"),Lingua.getMensagem("sucesso"));
                if(PrimeiroCadastro){
                    Principal.setLogado(nome.getText());
                    Usuarios.this.dispose();
                }
                PreencherFormulario(Lingua.getMensagem("cadastrar"),null);
            }catch(Exception e){
                Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("excecao")+" "+e);
            }
        }else{
            Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("senhas_diferentes"));
            senha.requestFocus(true);
        }
        
    }
    
    public void Alterar()
    {   
        try{
            Banco.executeUpdate("UPDATE usuarios SET nome='"+nome.getText()+"', usuario='"+usuario.getText()+"' WHERE id_usuarios='"+id_usuarios+"'");
            Janelinha.Aviso(Lingua.getMensagem("alterar"),Lingua.getMensagem("sucesso"));
            PreencherFormulario(null,null);
        }catch(Exception e){
            Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("excecao")+" "+e);
        }
    }
    
    public void Excluir()
    {    
        try{
            Banco.executeUpdate("DELETE FROM usuarios WHERE id_usuarios='"+id_usuarios+"'");
            Janelinha.Aviso(Lingua.getMensagem("excluir"),Lingua.getMensagem("sucesso"));
            PreencherFormulario(null,null);
        }catch(Exception e){
            Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("excecao")+" "+e);
        }
    }
    
    @Override
    public void Botoes(String acao)
    {
        if(acao.equals(Lingua.getMensagem("cadastrar"))){
            PreencherFormulario(Lingua.getMensagem("cadastrar"),null);
        }else if(acao.equals(Lingua.getMensagem("alterar"))){
            new UsuariosPesquisar(this,Lingua.getMensagem("alterar")).setVisible(true);
        }else if(acao.equals(Lingua.getMensagem("excluir"))){
            new UsuariosPesquisar(this,Lingua.getMensagem("excluir")).setVisible(true);
        }else if(acao.equals(Lingua.getMensagem("consultar"))){
            new UsuariosPesquisar(this,Lingua.getMensagem("consultar")).setVisible(true);
        }else if(acao.equals(Lingua.getMensagem("sair"))){
            Usuarios.this.dispose();
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
        PainelUsuarios = new javax.swing.JPanel();
        usuario = new javax.swing.JTextField();
        SENHA = new javax.swing.JLabel();
        BFinalizar = new javax.swing.JButton();
        nome = new javax.swing.JTextField();
        NOME = new javax.swing.JLabel();
        USUARIO = new javax.swing.JLabel();
        senha = new javax.swing.JPasswordField();
        senha2 = new javax.swing.JPasswordField();
        SENHA1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(Lingua.getMensagem("usuarios"));
        setFont(FonteFinancas);
        setForeground(java.awt.Color.cyan);
        setResizable(false);
        getContentPane().setLayout(null);

        BarraDeFerramentas.setFloatable(false);
        BarraDeFerramentas.setRollover(true);
        getContentPane().add(BarraDeFerramentas);
        BarraDeFerramentas.setBounds(0, 0, 490, 45);

        PainelUsuarios.setBorder(javax.swing.BorderFactory.createTitledBorder(null, Lingua.getMensagem("dados_usuario"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, FonteFinancas, new java.awt.Color(0, 102, 204)));
        PainelUsuarios.setForeground(new java.awt.Color(51, 94, 168));
        PainelUsuarios.setLayout(null);

        usuario.setFont(FonteFinancas);
        PainelUsuarios.add(usuario);
        usuario.setBounds(150, 80, 220, 20);

        SENHA.setFont(FonteFinancas);
        SENHA.setForeground(new java.awt.Color(51, 94, 168));
        SENHA.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        SENHA.setText(Lingua.getMensagem("senha")+":");
        PainelUsuarios.add(SENHA);
        SENHA.setBounds(20, 110, 120, 20);

        BFinalizar.setFont(FonteFinancas);
        BFinalizar.setText(Lingua.getMensagem("aguardando_acao"));
        BFinalizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BFinalizarActionPerformed(evt);
            }
        });
        PainelUsuarios.add(BFinalizar);
        BFinalizar.setBounds(150, 170, 120, 20);

        nome.setFont(FonteFinancas);
        PainelUsuarios.add(nome);
        nome.setBounds(150, 50, 220, 20);

        NOME.setFont(FonteFinancas);
        NOME.setForeground(new java.awt.Color(51, 94, 168));
        NOME.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        NOME.setText(Lingua.getMensagem("nome")+":");
        PainelUsuarios.add(NOME);
        NOME.setBounds(20, 50, 120, 20);

        USUARIO.setFont(FonteFinancas);
        USUARIO.setForeground(new java.awt.Color(51, 94, 168));
        USUARIO.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        USUARIO.setText(Lingua.getMensagem("usuario")+":");
        PainelUsuarios.add(USUARIO);
        USUARIO.setBounds(20, 80, 120, 20);

        senha.setFont(FonteFinancas);
        PainelUsuarios.add(senha);
        senha.setBounds(150, 110, 220, 20);

        senha2.setFont(FonteFinancas);
        PainelUsuarios.add(senha2);
        senha2.setBounds(150, 140, 220, 20);

        SENHA1.setFont(FonteFinancas);
        SENHA1.setForeground(new java.awt.Color(51, 94, 168));
        SENHA1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        SENHA1.setText(Lingua.getMensagem("senha_confirmar")+":");
        PainelUsuarios.add(SENHA1);
        SENHA1.setBounds(20, 140, 120, 20);

        getContentPane().add(PainelUsuarios);
        PainelUsuarios.setBounds(20, 60, 427, 214);

        setSize(new java.awt.Dimension(472, 315));
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
    private javax.swing.JPanel PainelUsuarios;
    private javax.swing.JLabel SENHA;
    private javax.swing.JLabel SENHA1;
    private javax.swing.JLabel USUARIO;
    private javax.swing.JTextField nome;
    private javax.swing.JPasswordField senha;
    private javax.swing.JPasswordField senha2;
    private javax.swing.JTextField usuario;
    // End of variables declaration//GEN-END:variables

}
