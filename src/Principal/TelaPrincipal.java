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

package Principal;

import Ajuda.Links;
import Ajuda.Tutorial;
import Backup.GerarBackup;
import Backup.ImportarBackup;
import Biblioteca.Conexao;
import Biblioteca.Configuracoes;
import Biblioteca.Database;
import Biblioteca.Fonte;
import Biblioteca.Janela;
import Biblioteca.Janelinha;
import Biblioteca.Numeros;
import Biblioteca.Visual;
import Cadastros.*;
import Linguagens.Linguagem;
import Movimentacoes.Despesas;
import Movimentacoes.Receitas;
import Movimentacoes.Transferencias;
import PreRelatorios.*;
import Utilitarios.AlterarSenha;
import Utilitarios.Configuracao;
import Utilitarios.ExecutarSQL;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;


public final class TelaPrincipal extends Janela {
    
    private final Calendar Calendario;
    private final String DataAtual,MesAtual,AnoAtual;
    private String IDPlano,PlanoMes,PlanoAno,PlanoValorInicial;
    private String Logado="nao";
    private Boolean CadastrarNovoUsuario = false;
    
    public TelaPrincipal() {
        
        VerificarCadastroUsuario();
        
        if(CadastrarNovoUsuario){
            Configuracao config = new Configuracao(true,this,true);
            config.PreencherFormulario();
            config.setVisible(true);
        }else{
            new Login(this,true,this).setVisible(true);
        }
        
        initComponents();      
        Visual.Janela(this);
        
        if(Logado.equals("nao"))
        {
            System.exit(0);
        }else{
            usuario_logado.setText("  "+Lingua.getMensagem("usuario_logado")+": "+Logado);
        }                

        Calendario = Calendar.getInstance();
        int dia,mes,ano;
        dia = Calendario.get(Calendar.DAY_OF_MONTH);
        mes = Calendario.get(Calendar.MONTH)+1;
        ano = Calendario.get(Calendar.YEAR);
        MesAtual = String.format("%02d", mes);
        AnoAtual = Integer.toString(ano);
        DataAtual = String.format("%02d", dia)+"/"+String.format("%02d", mes)+"/"+ano;
        DataPrincipal.setText(DataAtual+" - "+Lingua.getMensagem("sistema")+"  ");

        AtualizarAgenda();
        ContasSaldo();
        PlanoMes = Integer.toString(mes);
        PlanoAno = Integer.toString(ano);
        Planejamento(PlanoMes,PlanoAno); 
        
        contas.setRowHeight(32);
        planejamento.setRowHeight(23);
        agenda.setRowHeight(27);
        grupos.setRowHeight(27);
        relatorio.setRowHeight(24);
        
    }
    
    public void VerificarCadastroUsuario(){
        try {
            
            Banco.executeQuery("SELECT COUNT(TABLE_NAME) AS quantidade FROM INFORMATION_SCHEMA.SYSTEM_TABLES where TABLE_TYPE='TABLE'");
            Banco.getResultSet().next();
            if(Banco.getResultSet().getInt("quantidade")<19){            
                Database.GerarBanco();
            }
            Banco.executeQuery("SELECT COUNT(*) AS quantidade FROM usuarios");
            Banco.getResultSet().next();
            if(Banco.getResultSet().getInt("quantidade")<1){
                CadastrarNovoUsuario = true;
            }
        } catch (SQLException ex) {
            Janelinha.Aviso(Lingua.getMensagem("erro"),Lingua.getMensagem("excecao")+" "+ex);
        }
    }

    public void setLogado(String status){ Logado = status; }
    public String getLogado(){ return usuario_logado.getText(); }

    public void ContasSaldo()
    {
        try {
            Double valor_total=0.0;
            Banco.executeQuery("SELECT * FROM contas WHERE ativada='0' ORDER BY nome ASC");
            DefaultTableModel modelo = (DefaultTableModel)contas.getModel();
            modelo.setNumRows(0);
            while(Banco.getResultSet().next())
            {
                if(Banco.getResultSet().getString("saldo_total").equals("0"))
                {
                    valor_total+= Double.parseDouble(Banco.getResultSet().getString("valor"));
                }
                modelo.addRow(new Object[]{"   "+Banco.getResultSet().getString("nome"),Lingua.getMensagem("moeda")+" "+Banco.getResultSet().getString("valor")});
            }
            saldo_total.setText(Lingua.getMensagem("contas_saldo_total")+": "+Lingua.getMensagem("moeda")+" "+Numeros.Arrendondar(Double.toString(valor_total)));
        } catch (SQLException ex) {
            Janelinha.Aviso(Lingua.getMensagem("erro"), Lingua.getMensagem("excecao")+" "+ex);
        }
        RelatorioMensalDespesas();
        RelatorioMensalGrupos();
    }
    
    public void RelatorioMensalGrupos()
    {
        Double valor_grupo=0.0;
        Double saldo=0.0;
        DefaultTableModel modelo = (DefaultTableModel)grupos.getModel();
        modelo.setNumRows(0);
        try {
            Banco.executeQuery("SELECT * FROM relatorios_grupos WHERE saldo_total='0' ORDER BY nome ASC");
            ResultSet resultset_grupos = Banco.getResultSet();
            ResultSet resultset_categorias = null;
            // para cada grupo
            while(resultset_grupos.next())
            {
                valor_grupo=0.0;
                saldo=0.0;
                Banco.executeQuery("SELECT id_despesas_categorias FROM relatorios_grupos_itens WHERE id_relatorios_grupos='"+resultset_grupos.getString("id_relatorios_grupos")+"'");
                resultset_categorias = Banco.getResultSet();
                // para cada categoria do grupo
                while(resultset_categorias.next())
                {
                    Banco.executeQuery("SELECT SUM(despesas.valor) AS valor_total FROM despesas, despesas_itens, despesas_categorias WHERE despesas.id_item=despesas_itens.id_item AND despesas_itens.id_categoria=despesas_categorias.id_categoria AND MONTH(despesas.data)='"+MesAtual+"' AND YEAR(despesas.data)='"+AnoAtual+"' AND despesas_categorias.id_categoria='"+resultset_categorias.getString("id_despesas_categorias")+"'");
                    Banco.getResultSet().next();
                    valor_grupo+=Banco.getResultSet().getDouble("valor_total");
                }
                saldo=resultset_grupos.getDouble("valor");
                saldo=saldo-valor_grupo;
                modelo.addRow(new Object[]{" "+resultset_grupos.getString("nome"),Numeros.Arrendondar(valor_grupo),Numeros.Arrendondar(saldo)});
            }
        } catch (SQLException ex) {
            Janelinha.Aviso(Lingua.getMensagem("erro"), Lingua.getMensagem("excecao")+" "+ex);
        }
    }

    public void AtualizarAgenda()
    {
        try {
            Banco.executeQuery("SELECT agenda.*, agenda_tipos.nome AS tipo_nome, TO_CHAR(data,'DD/MM/YYYY') AS data_exibir FROM agenda, agenda_tipos WHERE agenda.id_tipo=agenda_tipos.id_tipo ORDER BY data ASC");
            DefaultTableModel modelo = (DefaultTableModel)agenda.getModel();
            modelo.setNumRows(0);
            while(Banco.getResultSet().next())
            {
                String valor_exibir;
                if(Banco.getResultSet().getString("valor").equals("0.00")) {
                    valor_exibir = ".........................";
                }
                else {
                    valor_exibir = Lingua.getMensagem("moeda")+" "+Banco.getResultSet().getString("valor");
                }
                modelo.addRow(new Object[]{"   "+Banco.getResultSet().getString("tipo_nome"),Banco.getResultSet().getString("descricao"),valor_exibir,Banco.getResultSet().getString("data_exibir")});
            }
        } catch (SQLException ex) {
            Janelinha.Aviso(Lingua.getMensagem("erro"), Lingua.getMensagem("excecao")+" "+ex);
        }
    }

    public void RelatorioMensalDespesas()
    {
        Double valor_total = 0.0;
        try {
            Banco.executeQuery("SELECT sum(despesas.valor) AS valor_total, despesas_categorias.nome FROM despesas, despesas_itens, despesas_categorias WHERE despesas.id_item=despesas_itens.id_item AND despesas_itens.id_categoria=despesas_categorias.id_categoria AND MONTH(despesas.data)='"+MesAtual+"' AND YEAR(despesas.data)='"+AnoAtual+"' GROUP BY despesas_categorias.nome ORDER BY despesas_categorias.nome");
            DefaultTableModel modelo = (DefaultTableModel)relatorio.getModel();
            modelo.setNumRows(0);
            while(Banco.getResultSet().next())
            {
                modelo.addRow(new Object[]{"   "+Banco.getResultSet().getString("nome"),Lingua.getMensagem("moeda")+" "+Banco.getResultSet().getString("valor_total")});
                valor_total += Double.parseDouble(Banco.getResultSet().getString("valor_total"));
            }
            relatorio_mensal.setText(Lingua.getMensagem("mini_relatorio_mensal_despesas")+" - "+Lingua.getMensagem("moeda")+" "+Numeros.Arrendondar(valor_total));
        } catch (SQLException ex) {
            Janelinha.Aviso(Lingua.getMensagem("erro"), Lingua.getMensagem("excecao")+" "+ex);
        }
    }

    public void RelatorioMensalReceitas()
    {
        Double valor_total = 0.0;
        try {
            Banco.executeQuery("select sum(receitas.valor) AS valor_total, receitas_categorias.nome from receitas, receitas_itens, receitas_categorias WHERE receitas.id_item=receitas_itens.id_item AND receitas_itens.id_categoria=receitas_categorias.id_categoria AND MONTH(receitas.data)='"+MesAtual+"' AND YEAR(receitas.data)='"+AnoAtual+"' GROUP BY receitas_categorias.nome ORDER BY receitas_categorias.nome");
            DefaultTableModel modelo = (DefaultTableModel)relatorio.getModel();
            modelo.setNumRows(0);
            while(Banco.getResultSet().next())
            {
                modelo.addRow(new Object[]{"   "+Banco.getResultSet().getString("nome"),Lingua.getMensagem("moeda")+" "+Banco.getResultSet().getString("valor_total")});
                valor_total += Double.parseDouble(Banco.getResultSet().getString("valor_total"));
            }
            relatorio_mensal.setText(Lingua.getMensagem("mini_relatorio_mensal_receitas")+" - "+Lingua.getMensagem("moeda")+" "+Numeros.Arrendondar(valor_total));
        } catch (SQLException ex) {
            Janelinha.Aviso(Lingua.getMensagem("erro"), Lingua.getMensagem("excecao")+" "+ex);
        }
    }

    public void Planejamento(String pmes, String pano)
    {
        PlanoMes = pmes;
        PlanoAno = pano;
        try {
            Double Valor_total=0.0,Valor_final=0.0,Valor_inicial=0.0;
            DefaultTableModel modelo = (DefaultTableModel)planejamento.getModel();
            modelo.setNumRows(0);
            Banco.executeQuery("SELECT * FROM planejamento WHERE mes='"+pmes+"' AND ano='"+pano+"'");
            if(Banco.getResultSet().next()){
                IDPlano = Banco.getResultSet().getString("id_planejamento");
                PlanoValorInicial = Banco.getResultSet().getString("valor");
                Banco.executeQuery("SELECT planejamento_componentes.id_item, planejamento_componentes.valor, planejamento_itens.nome FROM planejamento_componentes, planejamento_itens WHERE planejamento_componentes.id_planejamento='"+IDPlano+"' AND planejamento_itens.id_item=planejamento_componentes.id_item ORDER BY planejamento_itens.nome ASC");
                while(Banco.getResultSet().next())
                {
                    modelo.addRow(new Object[]{"   "+Banco.getResultSet().getString("nome"),Lingua.getMensagem("moeda")+" "+Banco.getResultSet().getString("valor")});
                    Valor_total += Double.parseDouble(Banco.getResultSet().getString("valor"));
                }
                Valor_inicial = Double.parseDouble(PlanoValorInicial);
                Valor_final = Valor_inicial-Valor_total;
                planejamento_inicial.setText(Lingua.getMensagem("saldo_inicial")+": "+Lingua.getMensagem("moeda")+" "+Numeros.Arrendondar(Valor_inicial));
                planejamento_final.setText(Lingua.getMensagem("saldo_final")+": "+Lingua.getMensagem("moeda")+" "+Numeros.Arrendondar(Valor_final));
            }else{
                IDPlano = "-1";
                planejamento_inicial.setText(Lingua.getMensagem("saldo_inicial")+": "+Lingua.getMensagem("moeda")+" 0.00");
                planejamento_final.setText(Lingua.getMensagem("saldo_final")+": "+Lingua.getMensagem("moeda")+" 0.00");
            }
            planejamento_titulo.setText(Lingua.getMensagem("planejamento")+" - "+getNomeMes(pmes));
            planejamento_titulo.repaint();
            planejamento_inicial.repaint();
            planejamento.repaint();
            planejamento_final.repaint();
            jsplanejamento.repaint();
        } catch (SQLException ex) {
            Janelinha.Aviso(Lingua.getMensagem("erro"), Lingua.getMensagem("excecao")+" "+ex);
        }
    }

    public void SetarData(String smes, String sano)
    {
        Calendario.set(Integer.parseInt(sano), Integer.parseInt(smes)-1, Calendario.get(Calendar.DAY_OF_MONTH));
    }
    
    public void PlanejamentoAnterior()
    {
        Calendario.add(Calendar.MONTH, -1);
        PlanoMes = Integer.toString(Calendario.get(Calendar.MONTH)+1);
        PlanoAno = Integer.toString(Calendario.get(Calendar.YEAR));
        Planejamento(PlanoMes,PlanoAno);
    }

    public void PlanejamentoProximo()
    {
        Calendario.add(Calendar.MONTH, +1);
        PlanoMes = Integer.toString(Calendario.get(Calendar.MONTH)+1);
        PlanoAno = Integer.toString(Calendario.get(Calendar.YEAR));
        Planejamento(PlanoMes,PlanoAno);
    }

    public String getNomeMes(String mes)
    {
        if(mes.equals("1") || mes.equals("01")){
            return Lingua.getMensagem("janeiro");
        }else if(mes.equals("2") || mes.equals("02")){
            return Lingua.getMensagem("fevereiro");
        }else if(mes.equals("3") || mes.equals("03")){
            return Lingua.getMensagem("marco");
        }else if(mes.equals("4") || mes.equals("04")){
            return Lingua.getMensagem("abril");
        }else if(mes.equals("5") || mes.equals("05")){
            return Lingua.getMensagem("maio");
        }else if(mes.equals("6") || mes.equals("06")){
            return Lingua.getMensagem("junho");
        }else if(mes.equals("7") || mes.equals("07")){
            return Lingua.getMensagem("julho");
        }else if(mes.equals("8") || mes.equals("08")){
            return Lingua.getMensagem("agosto");
        }else if(mes.equals("9") || mes.equals("09")){
            return Lingua.getMensagem("setembro");
        }else if(mes.equals("10")){
            return Lingua.getMensagem("outubro");
        }else if(mes.equals("11")){
            return Lingua.getMensagem("novembro");
        }else if(mes.equals("12")){
            return Lingua.getMensagem("dezembro");
        }else{
            return Lingua.getMensagem("inexistente");
        }
    }

    @Override
    public void Botoes(String acao)
    {
        if(acao.equals("Contas")){
             new Contas(this).setVisible(true);
        }else if (acao.equals("Receitas")){
             new Receitas(this).setVisible(true);
        }else if (acao.equals("Transferencias")){
             new Transferencias(this).setVisible(true);
        }else if(acao.equals("DespesasCategoria")) {
             new DespesasCategoria().setVisible(true);
        }else if(acao.equals("DespesasItem")){
             new DespesasItem().setVisible(true);
        }else if(acao.equals("PlanejamentoItem")){
             new PlanejamentoItem().setVisible(true);
        }else if(acao.equals("Planejamento")){
             new Planejamento(this).setVisible(true);
        }else if (acao.equals("ReceitasCategoria")) {
             new ReceitasCategoria().setVisible(true);
        }else if(acao.equals("ReceitasItem")){
             new ReceitasItem().setVisible(true);
        }else if (acao.equals("Agenda")) {
             new Agenda(this).setVisible(true);
        }else if (acao.equals("AgendaTipo")) {
             new AgendaTipo(this).setVisible(true);
        }else if (acao.equals("TransferenciasCategoria")) {
             new TransferenciasCategoria().setVisible(true);
        }else if(acao.equals("TransferenciasItem")){
             new TransferenciasItem().setVisible(true);
        }else if(acao.equals("RelatorioDespesas")){
             new PreDespesas(this,false).setVisible(true);
        }else if(acao.equals("RelatorioDespesasItens")){
             new PreDespesasItens(this,false).setVisible(true);
        }else if(acao.equals("RelatorioDespesasCategoriaTempo")){    
             new PreDespesasCategoriaTempo(this,false).setVisible(true);
        }else if(acao.equals("RelatorioReceitas")){
             new PreReceitas(this,false).setVisible(true);
        }else if(acao.equals("RelatorioReceitasItens")){
             new PreReceitasItens(this,false).setVisible(true);
        }else if(acao.equals("Despesas")){
              new Despesas(this).setVisible(true);
        }else if(acao.equals("Usuarios")){
              new Usuarios(false,this,false).setVisible(true);
        }else if(acao.equals("Configuracao")){
              new Configuracao(false,this,false).setVisible(true);
        }else if(acao.equals("UsuariosSenha")){
              new AlterarSenha(this).setVisible(true);
        }else if(acao.equals(Lingua.getMensagem("sair"))){
            System.exit(0);
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

        BarraFerramentas = new javax.swing.JToolBar();
        BDespesas = new javax.swing.JButton();
        BReceitas = new javax.swing.JButton();
        BTransferencias = new javax.swing.JButton();
        BPlanejamento = new javax.swing.JButton();
        BAgenda = new javax.swing.JButton();
        Conteudo = new javax.swing.JPanel();
        BotaoPlanoItens = new javax.swing.JButton();
        BotaoPlano = new javax.swing.JButton();
        BotaoPlanoDuplicar = new javax.swing.JButton();
        jscontas = new javax.swing.JScrollPane();
        contas = new javax.swing.JTable();
        jsagenda = new javax.swing.JScrollPane();
        agenda = new javax.swing.JTable();
        jsplanejamento = new javax.swing.JScrollPane();
        planejamento = new javax.swing.JTable();
        Jresumo_despesas = new javax.swing.JScrollPane();
        relatorio = new javax.swing.JTable();
        relatorio_mensal = new javax.swing.JLabel();
        saldo_total = new javax.swing.JLabel();
        BRReceitas = new javax.swing.JButton();
        BRDespesas = new javax.swing.JButton();
        planejamento_inicial = new javax.swing.JLabel();
        planejamento_final = new javax.swing.JLabel();
        BSairCentro = new javax.swing.JButton();
        BPlanejamentoAnterior = new javax.swing.JButton();
        BPlanejamentoProximo = new javax.swing.JButton();
        planejamento_titulo = new javax.swing.JLabel();
        lblRelat = new javax.swing.JLabel();
        jsgrupos = new javax.swing.JScrollPane();
        grupos = new javax.swing.JTable();
        grupos_saldo = new javax.swing.JLabel();
        plan_botoes = new javax.swing.JLabel();
        plan_navegacao1 = new javax.swing.JLabel();
        lblAgenda1 = new javax.swing.JLabel();
        lblRelat1 = new javax.swing.JLabel();
        PainelRodape = new javax.swing.JPanel();
        DataPrincipal = new javax.swing.JLabel();
        usuario_logado = new javax.swing.JLabel();
        MenuPrincipal = new javax.swing.JMenuBar();
        MCadastros = new javax.swing.JMenu();
        MCContas = new javax.swing.JMenuItem();
        MCDespesas = new javax.swing.JMenu();
        MCDItem = new javax.swing.JMenuItem();
        MCDCategoria = new javax.swing.JMenuItem();
        MCReceitas = new javax.swing.JMenu();
        MCRItem = new javax.swing.JMenuItem();
        MCRCategoria = new javax.swing.JMenuItem();
        MCTransferencias = new javax.swing.JMenu();
        MCTItem = new javax.swing.JMenuItem();
        MCTCategoria = new javax.swing.JMenuItem();
        MCGrupos = new javax.swing.JMenu();
        MCGGrupo = new javax.swing.JMenuItem();
        MCGGrupoItens = new javax.swing.JMenuItem();
        MCAgenda = new javax.swing.JMenu();
        MCALembrete = new javax.swing.JMenuItem();
        MCALembreteTipo = new javax.swing.JMenuItem();
        MCPlanejamento = new javax.swing.JMenu();
        MCPItem = new javax.swing.JMenuItem();
        MCPPlano = new javax.swing.JMenuItem();
        MCUsuarios = new javax.swing.JMenuItem();
        MMovimentacoes = new javax.swing.JMenu();
        MMDespesas = new javax.swing.JMenuItem();
        MMReceitas = new javax.swing.JMenuItem();
        MMTransferencias = new javax.swing.JMenuItem();
        MRelatorios = new javax.swing.JMenu();
        MRDespesas = new javax.swing.JMenu();
        MRDCategoria = new javax.swing.JMenuItem();
        MRDItem = new javax.swing.JMenuItem();
        MRDDespesaTempo = new javax.swing.JMenuItem();
        MRReceitas = new javax.swing.JMenu();
        MRRCategorias = new javax.swing.JMenuItem();
        MRRItens = new javax.swing.JMenuItem();
        MUtilitarios = new javax.swing.JMenu();
        MUSenha = new javax.swing.JMenuItem();
        MUConfiguracao = new javax.swing.JMenuItem();
        MUExecutarSQL = new javax.swing.JMenuItem();
        MBackup = new javax.swing.JMenu();
        MBGerar = new javax.swing.JMenuItem();
        MBImportar = new javax.swing.JMenuItem();
        MAjuda = new javax.swing.JMenu();
        MALinks = new javax.swing.JMenuItem();
        MATutorial = new javax.swing.JMenuItem();
        MSair = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(Lingua.getMensagem("sistema")+" - "+Lingua.getMensagem("versao"));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setResizable(false);

        BarraFerramentas.setBackground(new java.awt.Color(238, 238, 238));
        BarraFerramentas.setFloatable(false);
        BarraFerramentas.setRollover(true);

        BDespesas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagens/barra/despesas.png"))); // NOI18N
        BDespesas.setToolTipText(Lingua.getMensagem("despesas"));
        BDespesas.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        BDespesas.setFocusable(false);
        BDespesas.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BDespesas.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        BDespesas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BDespesasActionPerformed(evt);
            }
        });
        BarraFerramentas.add(BDespesas);
        BDespesas.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), "D");
        BDespesas.getActionMap().put("D", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                Botoes("Despesas");
            }
        });

        BReceitas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagens/barra/receitas.png"))); // NOI18N
        BReceitas.setToolTipText(Lingua.getMensagem("receitas"));
        BReceitas.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        BReceitas.setFocusable(false);
        BReceitas.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BReceitas.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        BReceitas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BReceitasActionPerformed(evt);
            }
        });
        BarraFerramentas.add(BReceitas);
        BReceitas.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), "R");
        BReceitas.getActionMap().put("R", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                Botoes("Receitas");
            }
        });

        BTransferencias.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagens/barra/transferencias.png"))); // NOI18N
        BTransferencias.setToolTipText(Lingua.getMensagem("transferencias"));
        BTransferencias.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        BTransferencias.setFocusable(false);
        BTransferencias.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BTransferencias.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        BTransferencias.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTransferenciasActionPerformed(evt);
            }
        });
        BarraFerramentas.add(BTransferencias);
        BTransferencias.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_T, 0), "T");
        BTransferencias.getActionMap().put("T", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                Botoes("Transferencias");
            }
        });

        BPlanejamento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagens/barra/planejamento.png"))); // NOI18N
        BPlanejamento.setToolTipText(Lingua.getMensagem("planejamento"));
        BPlanejamento.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        BPlanejamento.setFocusable(false);
        BPlanejamento.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BPlanejamento.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        BPlanejamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BPlanejamentoActionPerformed(evt);
            }
        });
        BarraFerramentas.add(BPlanejamento);
        BPlanejamento.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_P, 0), "P");
        BPlanejamento.getActionMap().put("P", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                Botoes("Planejamento");
            }
        });

        BAgenda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagens/barra/agenda.png"))); // NOI18N
        BAgenda.setToolTipText(Lingua.getMensagem("agenda"));
        BAgenda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        BAgenda.setFocusable(false);
        BAgenda.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BAgenda.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        BAgenda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BAgendaActionPerformed(evt);
            }
        });
        BarraFerramentas.add(BAgenda);
        BAgenda.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "A");
        BAgenda.getActionMap().put("A", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                Botoes("Agenda");
            }
        });

        getContentPane().add(BarraFerramentas, java.awt.BorderLayout.PAGE_START);

        Conteudo.setLayout(null);

        BotaoPlanoItens.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagens/outros/plano_item.gif"))); // NOI18N
        BotaoPlanoItens.setToolTipText(Lingua.getMensagem("itens"));
        BotaoPlanoItens.setBorderPainted(false);
        BotaoPlanoItens.setContentAreaFilled(false);
        BotaoPlanoItens.setFocusPainted(false);
        BotaoPlanoItens.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        BotaoPlanoItens.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotaoPlanoItensActionPerformed(evt);
            }
        });
        Conteudo.add(BotaoPlanoItens);
        BotaoPlanoItens.setBounds(50, 210, 30, 30);

        BotaoPlano.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagens/outros/plano.png"))); // NOI18N
        BotaoPlano.setToolTipText(Lingua.getMensagem("plano"));
        BotaoPlano.setBorderPainted(false);
        BotaoPlano.setContentAreaFilled(false);
        BotaoPlano.setFocusPainted(false);
        BotaoPlano.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        BotaoPlano.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotaoPlanoActionPerformed(evt);
            }
        });
        Conteudo.add(BotaoPlano);
        BotaoPlano.setBounds(20, 210, 30, 30);

        BotaoPlanoDuplicar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagens/outros/plano_duplicar.png"))); // NOI18N
        BotaoPlanoDuplicar.setToolTipText(Lingua.getMensagem("duplicar"));
        BotaoPlanoDuplicar.setBorderPainted(false);
        BotaoPlanoDuplicar.setContentAreaFilled(false);
        BotaoPlanoDuplicar.setFocusPainted(false);
        BotaoPlanoDuplicar.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        BotaoPlanoDuplicar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotaoPlanoDuplicarActionPerformed(evt);
            }
        });
        Conteudo.add(BotaoPlanoDuplicar);
        BotaoPlanoDuplicar.setBounds(80, 210, 30, 30);

        jscontas.setBorder(null);

        contas.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        contas.setFont(FonteFinancas);
        contas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        contas.setFocusable(false);
        contas.setRequestFocusEnabled(false);
        contas.setRowHeight(31);
        contas.setRowMargin(0);
        contas.setRowSelectionAllowed(false);
        contas.setShowHorizontalLines(false);
        contas.setShowVerticalLines(false);
        contas.getTableHeader().setReorderingAllowed(false);
        jscontas.setViewportView(contas);
        if (contas.getColumnModel().getColumnCount() > 0) {
            contas.getColumnModel().getColumn(0).setHeaderValue(Lingua.getMensagem("nome"));
            contas.getColumnModel().getColumn(1).setHeaderValue(Lingua.getMensagem("saldo"));
        }
        contas.getColumnModel().getColumn(1).setMinWidth(100);
        contas.getColumnModel().getColumn(1).setMaxWidth(100);
        contas.setTableHeader(null);

        Conteudo.add(jscontas);
        jscontas.setBounds(20, 40, 260, 130);

        jsagenda.setBorder(null);

        agenda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        agenda.setFont(FonteFinancas);
        agenda.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "", "", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        agenda.setFocusable(false);
        agenda.setRequestFocusEnabled(false);
        agenda.setRowHeight(32);
        agenda.setRowSelectionAllowed(false);
        agenda.setShowHorizontalLines(false);
        agenda.setShowVerticalLines(false);
        jsagenda.setViewportView(agenda);
        if (agenda.getColumnModel().getColumnCount() > 0) {
            agenda.getColumnModel().getColumn(0).setHeaderValue(Lingua.getMensagem("tipo"));
            agenda.getColumnModel().getColumn(1).setHeaderValue(Lingua.getMensagem("descricao"));
            agenda.getColumnModel().getColumn(2).setHeaderValue(Lingua.getMensagem("valor"));
            agenda.getColumnModel().getColumn(3).setHeaderValue(Lingua.getMensagem("data"));
        }
        agenda.getColumnModel().getColumn(2).setMinWidth(90);
        agenda.getColumnModel().getColumn(2).setMaxWidth(90);
        agenda.getColumnModel().getColumn(3).setMinWidth(100);
        agenda.getColumnModel().getColumn(3).setMaxWidth(100);
        agenda.getTableHeader().setFont(FonteFinancas);

        Conteudo.add(jsagenda);
        jsagenda.setBounds(380, 250, 500, 210);

        jsplanejamento.setBorder(null);

        planejamento.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        planejamento.setFont(FonteFinancas);
        planejamento.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        planejamento.setFocusable(false);
        planejamento.setRequestFocusEnabled(false);
        planejamento.setRowHeight(21);
        planejamento.setRowSelectionAllowed(false);
        planejamento.setShowHorizontalLines(false);
        planejamento.setShowVerticalLines(false);
        jsplanejamento.setViewportView(planejamento);
        if (planejamento.getColumnModel().getColumnCount() > 0) {
            planejamento.getColumnModel().getColumn(0).setHeaderValue(Lingua.getMensagem("nome"));
            planejamento.getColumnModel().getColumn(1).setHeaderValue(Lingua.getMensagem("valor"));
        }
        planejamento.getColumnModel().getColumn(1).setMinWidth(100);
        planejamento.getColumnModel().getColumn(1).setMaxWidth(100);
        planejamento.setTableHeader(null);

        Conteudo.add(jsplanejamento);
        jsplanejamento.setBounds(20, 240, 340, 190);

        Jresumo_despesas.setBackground(new java.awt.Color(255, 255, 255));
        Jresumo_despesas.setBorder(null);
        Jresumo_despesas.setOpaque(false);

        relatorio.setBackground(new java.awt.Color(240, 240, 240));
        relatorio.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        relatorio.setFont(FonteFinancas);
        relatorio.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nome", "Saldo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        relatorio.setFocusable(false);
        relatorio.setRequestFocusEnabled(false);
        relatorio.setRowHeight(21);
        relatorio.setRowSelectionAllowed(false);
        relatorio.setShowHorizontalLines(false);
        relatorio.setShowVerticalLines(false);
        Jresumo_despesas.setViewportView(relatorio);
        if (relatorio.getColumnModel().getColumnCount() > 0) {
            relatorio.getColumnModel().getColumn(0).setResizable(false);
            relatorio.getColumnModel().getColumn(1).setResizable(false);
        }
        relatorio.getColumnModel().getColumn(1).setMinWidth(100);
        relatorio.getColumnModel().getColumn(1).setMaxWidth(100);
        relatorio.setTableHeader(null);

        Conteudo.add(Jresumo_despesas);
        Jresumo_despesas.setBounds(570, 40, 310, 170);

        relatorio_mensal.setBackground(new java.awt.Color(255, 255, 255));
        relatorio_mensal.setFont(FonteFinancas);
        relatorio_mensal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        relatorio_mensal.setText(Lingua.getMensagem("mini_relatorio_mensal_despesas"));
        relatorio_mensal.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        relatorio_mensal.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        relatorio_mensal.setOpaque(true);
        Conteudo.add(relatorio_mensal);
        relatorio_mensal.setBounds(570, 20, 310, 20);

        saldo_total.setBackground(new java.awt.Color(255, 255, 255));
        saldo_total.setFont(FonteFinancas);
        saldo_total.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        saldo_total.setText(Lingua.getMensagem("contas_saldo_total"));
        saldo_total.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        saldo_total.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        saldo_total.setOpaque(true);
        Conteudo.add(saldo_total);
        saldo_total.setBounds(20, 20, 260, 20);

        BRReceitas.setBackground(new java.awt.Color(153, 153, 153));
        BRReceitas.setFont(FonteFinancas);
        BRReceitas.setText(Lingua.getMensagem("receitas"));
        BRReceitas.setToolTipText(Lingua.getMensagem("receitas"));
        BRReceitas.setBorder(null);
        BRReceitas.setBorderPainted(false);
        BRReceitas.setContentAreaFilled(false);
        BRReceitas.setFocusPainted(false);
        BRReceitas.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        BRReceitas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BRReceitasActionPerformed(evt);
            }
        });
        Conteudo.add(BRReceitas);
        BRReceitas.setBounds(650, 210, 80, 20);

        BRDespesas.setBackground(new java.awt.Color(153, 153, 153));
        BRDespesas.setFont(FonteFinancas);
        BRDespesas.setText(Lingua.getMensagem("despesas"));
        BRDespesas.setToolTipText(Lingua.getMensagem("despesas"));
        BRDespesas.setActionCommand(Lingua.getMensagem("despesas"));
        BRDespesas.setBorder(null);
        BRDespesas.setBorderPainted(false);
        BRDespesas.setContentAreaFilled(false);
        BRDespesas.setFocusPainted(false);
        BRDespesas.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        BRDespesas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BRDespesasActionPerformed(evt);
            }
        });
        Conteudo.add(BRDespesas);
        BRDespesas.setBounds(570, 210, 80, 20);

        planejamento_inicial.setBackground(new java.awt.Color(255, 255, 255));
        planejamento_inicial.setFont(FonteFinancas);
        planejamento_inicial.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        planejamento_inicial.setText(Lingua.getMensagem("saldo_inicial")+": "+Lingua.getMensagem("moeda")+" 0.00");
        planejamento_inicial.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        planejamento_inicial.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        planejamento_inicial.setOpaque(true);
        planejamento_inicial.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        Conteudo.add(planejamento_inicial);
        planejamento_inicial.setBounds(110, 210, 250, 30);

        planejamento_final.setBackground(new java.awt.Color(255, 255, 255));
        planejamento_final.setFont(FonteFinancas);
        planejamento_final.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        planejamento_final.setText(Lingua.getMensagem("saldo_final")+": "+Lingua.getMensagem("moeda")+" 0.00");
        planejamento_final.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        planejamento_final.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        planejamento_final.setOpaque(true);
        planejamento_final.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        Conteudo.add(planejamento_final);
        planejamento_final.setBounds(20, 430, 340, 30);

        BSairCentro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagens/layout/sair.png"))); // NOI18N
        BSairCentro.setToolTipText(Lingua.getMensagem("sair"));
        BSairCentro.setBorderPainted(false);
        BSairCentro.setContentAreaFilled(false);
        BSairCentro.setFocusPainted(false);
        BSairCentro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BSairCentroActionPerformed(evt);
            }
        });
        Conteudo.add(BSairCentro);
        BSairCentro.setBounds(370, 170, 190, 60);

        BPlanejamentoAnterior.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagens/outros/seta_esquerda.png"))); // NOI18N
        BPlanejamentoAnterior.setToolTipText(Lingua.getMensagem("anterior"));
        BPlanejamentoAnterior.setBorder(null);
        BPlanejamentoAnterior.setBorderPainted(false);
        BPlanejamentoAnterior.setContentAreaFilled(false);
        BPlanejamentoAnterior.setFocusPainted(false);
        BPlanejamentoAnterior.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        BPlanejamentoAnterior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BPlanejamentoAnteriorActionPerformed(evt);
            }
        });
        Conteudo.add(BPlanejamentoAnterior);
        BPlanejamentoAnterior.setBounds(210, 190, 20, 20);

        BPlanejamentoProximo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagens/outros/seta_direita.png"))); // NOI18N
        BPlanejamentoProximo.setToolTipText(Lingua.getMensagem("proximo"));
        BPlanejamentoProximo.setBorder(null);
        BPlanejamentoProximo.setBorderPainted(false);
        BPlanejamentoProximo.setContentAreaFilled(false);
        BPlanejamentoProximo.setFocusPainted(false);
        BPlanejamentoProximo.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        BPlanejamentoProximo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BPlanejamentoProximoActionPerformed(evt);
            }
        });
        Conteudo.add(BPlanejamentoProximo);
        BPlanejamentoProximo.setBounds(340, 190, 20, 20);

        planejamento_titulo.setBackground(new java.awt.Color(255, 255, 255));
        planejamento_titulo.setFont(FonteFinancas);
        planejamento_titulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        planejamento_titulo.setText(Lingua.getMensagem("planejamento_inexistente"));
        planejamento_titulo.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        planejamento_titulo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        planejamento_titulo.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        planejamento_titulo.setOpaque(true);
        planejamento_titulo.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        Conteudo.add(planejamento_titulo);
        planejamento_titulo.setBounds(20, 190, 180, 20);

        lblRelat.setBackground(new java.awt.Color(255, 255, 255));
        lblRelat.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblRelat.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblRelat.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblRelat.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblRelat.setOpaque(true);
        Conteudo.add(lblRelat);
        lblRelat.setBounds(570, 210, 80, 20);

        jsgrupos.setBorder(null);

        grupos.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        grupos.setFont(FonteFinancas);
        grupos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        grupos.setFocusable(false);
        grupos.setRequestFocusEnabled(false);
        grupos.setRowHeight(31);
        grupos.setRowSelectionAllowed(false);
        grupos.setShowHorizontalLines(false);
        grupos.setShowVerticalLines(false);
        grupos.getTableHeader().setReorderingAllowed(false);
        jsgrupos.setViewportView(grupos);
        if (grupos.getColumnModel().getColumnCount() > 0) {
            grupos.getColumnModel().getColumn(0).setHeaderValue(Lingua.getMensagem("nome"));
            grupos.getColumnModel().getColumn(1).setHeaderValue(Lingua.getMensagem("gastos"));
            grupos.getColumnModel().getColumn(2).setHeaderValue(Lingua.getMensagem("saldo"));
        }
        grupos.getTableHeader().setFont(FonteFinancas);

        Conteudo.add(jsgrupos);
        jsgrupos.setBounds(290, 40, 270, 130);

        grupos_saldo.setBackground(new java.awt.Color(255, 255, 255));
        grupos_saldo.setFont(FonteFinancas);
        grupos_saldo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        grupos_saldo.setText(Lingua.getMensagem("grupos_relatorio_mensal"));
        grupos_saldo.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        grupos_saldo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        grupos_saldo.setOpaque(true);
        Conteudo.add(grupos_saldo);
        grupos_saldo.setBounds(290, 20, 270, 20);

        plan_botoes.setBackground(new java.awt.Color(255, 255, 255));
        plan_botoes.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        plan_botoes.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        plan_botoes.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        plan_botoes.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        plan_botoes.setOpaque(true);
        plan_botoes.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        Conteudo.add(plan_botoes);
        plan_botoes.setBounds(20, 210, 90, 30);

        plan_navegacao1.setBackground(new java.awt.Color(255, 255, 255));
        plan_navegacao1.setFont(FonteFinancasPequena);
        plan_navegacao1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        plan_navegacao1.setText(Lingua.getMensagem("anterior")+" - "+Lingua.getMensagem("proximo"));
        Conteudo.add(plan_navegacao1);
        plan_navegacao1.setBounds(230, 190, 110, 20);

        lblAgenda1.setBackground(new java.awt.Color(255, 255, 255));
        lblAgenda1.setFont(FonteFinancas);
        lblAgenda1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAgenda1.setText(Lingua.getMensagem("agenda")+" - "+Lingua.getMensagem("lembretes"));
        lblAgenda1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblAgenda1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblAgenda1.setOpaque(true);
        Conteudo.add(lblAgenda1);
        lblAgenda1.setBounds(380, 230, 170, 20);

        lblRelat1.setBackground(new java.awt.Color(255, 255, 255));
        lblRelat1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblRelat1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblRelat1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblRelat1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblRelat1.setOpaque(true);
        Conteudo.add(lblRelat1);
        lblRelat1.setBounds(650, 210, 80, 20);

        getContentPane().add(Conteudo, java.awt.BorderLayout.CENTER);

        PainelRodape.setBackground(new java.awt.Color(202, 192, 192));
        PainelRodape.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(204, 204, 204)));
        PainelRodape.setPreferredSize(new java.awt.Dimension(900, 20));
        PainelRodape.setLayout(null);

        DataPrincipal.setFont(FonteFinancasPequena);
        DataPrincipal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        DataPrincipal.setText("Data");
        PainelRodape.add(DataPrincipal);
        DataPrincipal.setBounds(390, 0, 510, 20);

        usuario_logado.setFont(FonteFinancasPequena);
        usuario_logado.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        usuario_logado.setText(Lingua.getMensagem("excecao")+": "+Lingua.getMensagem("usuario_nao_identificado"));
        PainelRodape.add(usuario_logado);
        usuario_logado.setBounds(0, 0, 360, 17);

        getContentPane().add(PainelRodape, java.awt.BorderLayout.PAGE_END);

        MenuPrincipal.setBackground(new java.awt.Color(230, 230, 230));
        MenuPrincipal.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(204, 204, 204)));
        MenuPrincipal.setForeground(new java.awt.Color(102, 102, 102));
        MenuPrincipal.setFont(FonteFinancas);

        MCadastros.setText(Lingua.getMensagem("cadastros"));
        MCadastros.setFont(FonteFinancas);

        MCContas.setFont(FonteFinancas);
        MCContas.setText(Lingua.getMensagem("contas"));
        MCContas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MCContasActionPerformed(evt);
            }
        });
        MCadastros.add(MCContas);

        MCDespesas.setText(Lingua.getMensagem("despesas"));
        MCDespesas.setFont(FonteFinancas);

        MCDItem.setFont(FonteFinancas);
        MCDItem.setText(Lingua.getMensagem("item"));
        MCDItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MCDItemActionPerformed(evt);
            }
        });
        MCDespesas.add(MCDItem);

        MCDCategoria.setFont(FonteFinancas);
        MCDCategoria.setText(Lingua.getMensagem("categoria"));
        MCDCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MCDCategoriaActionPerformed(evt);
            }
        });
        MCDespesas.add(MCDCategoria);

        MCadastros.add(MCDespesas);

        MCReceitas.setText(Lingua.getMensagem("receitas"));
        MCReceitas.setFont(FonteFinancas);

        MCRItem.setFont(FonteFinancas);
        MCRItem.setText(Lingua.getMensagem("item"));
        MCRItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MCRItemActionPerformed(evt);
            }
        });
        MCReceitas.add(MCRItem);

        MCRCategoria.setFont(FonteFinancas);
        MCRCategoria.setText(Lingua.getMensagem("categoria"));
        MCRCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MCRCategoriaActionPerformed(evt);
            }
        });
        MCReceitas.add(MCRCategoria);

        MCadastros.add(MCReceitas);

        MCTransferencias.setText(Lingua.getMensagem("transferencias"));
        MCTransferencias.setFont(FonteFinancas);

        MCTItem.setFont(FonteFinancas);
        MCTItem.setText(Lingua.getMensagem("item"));
        MCTItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MCTItemActionPerformed(evt);
            }
        });
        MCTransferencias.add(MCTItem);

        MCTCategoria.setFont(FonteFinancas);
        MCTCategoria.setText(Lingua.getMensagem("categoria"));
        MCTCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MCTCategoriaActionPerformed(evt);
            }
        });
        MCTransferencias.add(MCTCategoria);

        MCadastros.add(MCTransferencias);

        MCGrupos.setText(Lingua.getMensagem("grupos"));
        MCGrupos.setFont(FonteFinancas);

        MCGGrupo.setFont(FonteFinancas);
        MCGGrupo.setText(Lingua.getMensagem("grupo"));
        MCGGrupo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MCGGrupoActionPerformed(evt);
            }
        });
        MCGrupos.add(MCGGrupo);

        MCGGrupoItens.setFont(FonteFinancas);
        MCGGrupoItens.setText(Lingua.getMensagem("grupo_itens"));
        MCGGrupoItens.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MCGGrupoItensActionPerformed(evt);
            }
        });
        MCGrupos.add(MCGGrupoItens);

        MCadastros.add(MCGrupos);

        MCAgenda.setText(Lingua.getMensagem("agenda"));
        MCAgenda.setFont(FonteFinancas);

        MCALembrete.setFont(FonteFinancas);
        MCALembrete.setText(Lingua.getMensagem("lembrete"));
        MCALembrete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MCALembreteActionPerformed(evt);
            }
        });
        MCAgenda.add(MCALembrete);

        MCALembreteTipo.setFont(FonteFinancas);
        MCALembreteTipo.setText(Lingua.getMensagem("tipo_lembrete"));
        MCALembreteTipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MCALembreteTipoActionPerformed(evt);
            }
        });
        MCAgenda.add(MCALembreteTipo);

        MCadastros.add(MCAgenda);

        MCPlanejamento.setText(Lingua.getMensagem("planejamento"));
        MCPlanejamento.setFont(FonteFinancas);

        MCPItem.setFont(FonteFinancas);
        MCPItem.setText(Lingua.getMensagem("item"));
        MCPItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MCPItemActionPerformed(evt);
            }
        });
        MCPlanejamento.add(MCPItem);

        MCPPlano.setFont(FonteFinancas);
        MCPPlano.setText(Lingua.getMensagem("plano"));
        MCPPlano.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MCPPlanoActionPerformed(evt);
            }
        });
        MCPlanejamento.add(MCPPlano);

        MCadastros.add(MCPlanejamento);

        MCUsuarios.setFont(FonteFinancas);
        MCUsuarios.setText(Lingua.getMensagem("usuarios"));
        MCUsuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MCUsuariosActionPerformed(evt);
            }
        });
        MCadastros.add(MCUsuarios);

        MenuPrincipal.add(MCadastros);

        MMovimentacoes.setText(Lingua.getMensagem("movimentacoes"));
        MMovimentacoes.setFont(FonteFinancas);

        MMDespesas.setFont(FonteFinancas);
        MMDespesas.setText(Lingua.getMensagem("despesas"));
        MMDespesas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MMDespesasActionPerformed(evt);
            }
        });
        MMovimentacoes.add(MMDespesas);

        MMReceitas.setFont(FonteFinancas);
        MMReceitas.setText(Lingua.getMensagem("receitas"));
        MMReceitas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MMReceitasActionPerformed(evt);
            }
        });
        MMovimentacoes.add(MMReceitas);

        MMTransferencias.setFont(FonteFinancas);
        MMTransferencias.setText(Lingua.getMensagem("transferencias"));
        MMTransferencias.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MMTransferenciasActionPerformed(evt);
            }
        });
        MMovimentacoes.add(MMTransferencias);

        MenuPrincipal.add(MMovimentacoes);

        MRelatorios.setText(Lingua.getMensagem("relatorios"));
        MRelatorios.setFont(FonteFinancas);

        MRDespesas.setText(Lingua.getMensagem("despesas"));
        MRDespesas.setFont(FonteFinancas);

        MRDCategoria.setFont(FonteFinancas);
        MRDCategoria.setText(Lingua.getMensagem("periodo_categoria"));
        MRDCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MRDCategoriaActionPerformed(evt);
            }
        });
        MRDespesas.add(MRDCategoria);

        MRDItem.setFont(FonteFinancas);
        MRDItem.setText(Lingua.getMensagem("periodo_item"));
        MRDItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MRDItemActionPerformed(evt);
            }
        });
        MRDespesas.add(MRDItem);

        MRDDespesaTempo.setFont(FonteFinancas);
        MRDDespesaTempo.setText(Lingua.getMensagem("periodo_categoria_tempo"));
        MRDDespesaTempo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MRDDespesaTempoActionPerformed(evt);
            }
        });
        MRDespesas.add(MRDDespesaTempo);

        MRelatorios.add(MRDespesas);

        MRReceitas.setText(Lingua.getMensagem("receitas"));
        MRReceitas.setFont(FonteFinancas);

        MRRCategorias.setFont(FonteFinancas);
        MRRCategorias.setText(Lingua.getMensagem("periodo_categoria"));
        MRRCategorias.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MRRCategoriasActionPerformed(evt);
            }
        });
        MRReceitas.add(MRRCategorias);

        MRRItens.setFont(FonteFinancas);
        MRRItens.setText(Lingua.getMensagem("periodo_item"));
        MRRItens.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MRRItensActionPerformed(evt);
            }
        });
        MRReceitas.add(MRRItens);

        MRelatorios.add(MRReceitas);

        MenuPrincipal.add(MRelatorios);

        MUtilitarios.setText(Lingua.getMensagem("utilitarios"));
        MUtilitarios.setFont(FonteFinancas);

        MUSenha.setFont(FonteFinancas);
        MUSenha.setText(Lingua.getMensagem("alterar_senha"));
        MUSenha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MUSenhaActionPerformed(evt);
            }
        });
        MUtilitarios.add(MUSenha);

        MUConfiguracao.setFont(FonteFinancas);
        MUConfiguracao.setText(Lingua.getMensagem("configuracao"));
        MUConfiguracao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MUConfiguracaoActionPerformed(evt);
            }
        });
        MUtilitarios.add(MUConfiguracao);

        MUExecutarSQL.setFont(FonteFinancas);
        MUExecutarSQL.setText(Lingua.getMensagem("executar_sql"));
        MUExecutarSQL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MUExecutarSQLActionPerformed(evt);
            }
        });
        MUtilitarios.add(MUExecutarSQL);

        MenuPrincipal.add(MUtilitarios);

        MBackup.setText(Lingua.getMensagem("backup"));
        MBackup.setFont(FonteFinancas);

        MBGerar.setFont(FonteFinancas);
        MBGerar.setText(Lingua.getMensagem("gerar"));
        MBGerar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MBGerarActionPerformed(evt);
            }
        });
        MBackup.add(MBGerar);

        MBImportar.setFont(FonteFinancas);
        MBImportar.setText(Lingua.getMensagem("importar"));
        MBImportar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MBImportarActionPerformed(evt);
            }
        });
        MBackup.add(MBImportar);

        MenuPrincipal.add(MBackup);

        MAjuda.setText(Lingua.getMensagem("ajuda"));
        MAjuda.setFont(FonteFinancas);

        MALinks.setFont(FonteFinancas);
        MALinks.setText(Lingua.getMensagem("links"));
        MALinks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MALinksActionPerformed(evt);
            }
        });
        MAjuda.add(MALinks);

        MATutorial.setFont(FonteFinancas);
        MATutorial.setText(Lingua.getMensagem("tutorial_rapido"));
        MATutorial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MATutorialActionPerformed(evt);
            }
        });
        MAjuda.add(MATutorial);

        MenuPrincipal.add(MAjuda);

        MSair.setText(Lingua.getMensagem("sair"));
        MSair.setFont(FonteFinancas);
        MSair.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                MSairMouseClicked(evt);
            }
        });
        MSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MSairActionPerformed(evt);
            }
        });
        MenuPrincipal.add(MSair);

        setJMenuBar(MenuPrincipal);

        setSize(new java.awt.Dimension(910, 598));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

private void MSairMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MSairMouseClicked
    Botoes(Lingua.getMensagem("sair"));
}//GEN-LAST:event_MSairMouseClicked

private void BDespesasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BDespesasActionPerformed
    Botoes("Despesas");
}//GEN-LAST:event_BDespesasActionPerformed

private void BReceitasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BReceitasActionPerformed
    Botoes("Receitas");
}//GEN-LAST:event_BReceitasActionPerformed

private void BTransferenciasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTransferenciasActionPerformed
    Botoes("Transferencias");
}//GEN-LAST:event_BTransferenciasActionPerformed

private void MSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MSairActionPerformed
    Botoes(Lingua.getMensagem("sair"));
}//GEN-LAST:event_MSairActionPerformed

private void MMDespesasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MMDespesasActionPerformed
    Botoes("Despesas");
}//GEN-LAST:event_MMDespesasActionPerformed

private void MCUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MCUsuariosActionPerformed
    Botoes("Usuarios");
}//GEN-LAST:event_MCUsuariosActionPerformed

private void MUSenhaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MUSenhaActionPerformed
    Botoes("UsuariosSenha");
}//GEN-LAST:event_MUSenhaActionPerformed

private void MRDCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MRDCategoriaActionPerformed
    Botoes("RelatorioDespesas");
}//GEN-LAST:event_MRDCategoriaActionPerformed

private void MCDItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MCDItemActionPerformed
    Botoes("DespesasItem");
}//GEN-LAST:event_MCDItemActionPerformed

private void MCDCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MCDCategoriaActionPerformed
    Botoes("DespesasCategoria");
}//GEN-LAST:event_MCDCategoriaActionPerformed

private void MCRItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MCRItemActionPerformed
    Botoes("ReceitasItem");
}//GEN-LAST:event_MCRItemActionPerformed

private void MCRCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MCRCategoriaActionPerformed
    Botoes("ReceitasCategoria");
}//GEN-LAST:event_MCRCategoriaActionPerformed

private void MCTItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MCTItemActionPerformed
    Botoes("TransferenciasItem");
}//GEN-LAST:event_MCTItemActionPerformed

private void MCTCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MCTCategoriaActionPerformed
    Botoes("TransferenciasCategoria");
}//GEN-LAST:event_MCTCategoriaActionPerformed

private void MCContasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MCContasActionPerformed
    Botoes("Contas");
}//GEN-LAST:event_MCContasActionPerformed

private void MMReceitasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MMReceitasActionPerformed
    Botoes("Receitas");
}//GEN-LAST:event_MMReceitasActionPerformed

private void MMTransferenciasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MMTransferenciasActionPerformed
    Botoes("Transferencias");
}//GEN-LAST:event_MMTransferenciasActionPerformed

private void MRDItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MRDItemActionPerformed
    Botoes("RelatorioDespesasItens");
}//GEN-LAST:event_MRDItemActionPerformed

private void MCALembreteTipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MCALembreteTipoActionPerformed
    Botoes("AgendaTipo");
}//GEN-LAST:event_MCALembreteTipoActionPerformed

private void BAgendaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BAgendaActionPerformed
    Botoes("Agenda");
}//GEN-LAST:event_BAgendaActionPerformed

private void BRDespesasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BRDespesasActionPerformed
    RelatorioMensalDespesas();
}//GEN-LAST:event_BRDespesasActionPerformed

private void BRReceitasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BRReceitasActionPerformed
    RelatorioMensalReceitas();
}//GEN-LAST:event_BRReceitasActionPerformed

private void BPlanejamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BPlanejamentoActionPerformed
    Botoes("Planejamento");
}//GEN-LAST:event_BPlanejamentoActionPerformed

private void MCPItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MCPItemActionPerformed
    Botoes("PlanejamentoItem");
}//GEN-LAST:event_MCPItemActionPerformed

private void MCPPlanoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MCPPlanoActionPerformed
    Botoes("Planejamento");
}//GEN-LAST:event_MCPPlanoActionPerformed

private void BSairCentroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BSairCentroActionPerformed
    Botoes(Lingua.getMensagem("sair"));
}//GEN-LAST:event_BSairCentroActionPerformed

private void BPlanejamentoAnteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BPlanejamentoAnteriorActionPerformed
    PlanejamentoAnterior();
}//GEN-LAST:event_BPlanejamentoAnteriorActionPerformed

private void BPlanejamentoProximoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BPlanejamentoProximoActionPerformed
    PlanejamentoProximo();
}//GEN-LAST:event_BPlanejamentoProximoActionPerformed

private void MRRCategoriasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MRRCategoriasActionPerformed
    Botoes("RelatorioReceitas");
}//GEN-LAST:event_MRRCategoriasActionPerformed

private void MRRItensActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MRRItensActionPerformed
    Botoes("RelatorioReceitasItens");
}//GEN-LAST:event_MRRItensActionPerformed

private void MRDDespesaTempoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MRDDespesaTempoActionPerformed
    Botoes("RelatorioDespesasCategoriaTempo");
}//GEN-LAST:event_MRDDespesaTempoActionPerformed

private void MCGGrupoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MCGGrupoActionPerformed
    new Grupos(this).setVisible(true);
}//GEN-LAST:event_MCGGrupoActionPerformed

private void MCGGrupoItensActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MCGGrupoItensActionPerformed
    new GruposItens(this).setVisible(true);
}//GEN-LAST:event_MCGGrupoItensActionPerformed

    private void MCALembreteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MCALembreteActionPerformed
        Botoes("Agenda");
    }//GEN-LAST:event_MCALembreteActionPerformed

    private void BotaoPlanoItensActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotaoPlanoItensActionPerformed
        if(!IDPlano.equals("-1"))
        {
            new PlanejamentoComponentes(this,null,IDPlano,PlanoValorInicial,PlanoMes,PlanoAno).setVisible(true);
        }else{
            Janelinha.Aviso(Lingua.getMensagem("erro"), Lingua.getMensagem("plano_nao_cadastrado"));
            if(Janelinha.Pergunta(Lingua.getMensagem("cadastrar"), Lingua.getMensagem("deseja_cadastrar")))
            {
                Planejamento plano = new Planejamento(this);
                plano.PreencherFormulario(Lingua.getMensagem("cadastrar"),null,Integer.parseInt(PlanoMes),PlanoAno,null);
                plano.setVisible(true);
            }
        }
    }//GEN-LAST:event_BotaoPlanoItensActionPerformed

    private void BotaoPlanoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotaoPlanoActionPerformed
        if(!IDPlano.equals("-1"))
        {
            Planejamento plano = new Planejamento(this);
            plano.PreencherFormulario(Lingua.getMensagem("alterar"), IDPlano, Integer.parseInt(PlanoMes), PlanoAno, PlanoValorInicial);
            plano.setVisible(true);
        }else{
            Janelinha.Aviso(Lingua.getMensagem("erro"), Lingua.getMensagem("plano_nao_cadastrado"));
            if(Janelinha.Pergunta(Lingua.getMensagem("cadastrar"), Lingua.getMensagem("deseja_cadastrar")))
            {
                Planejamento plano = new Planejamento(this);
                plano.PreencherFormulario(Lingua.getMensagem("cadastrar"),null,Integer.parseInt(PlanoMes),PlanoAno,null);
                plano.setVisible(true);
            }
        }
    }//GEN-LAST:event_BotaoPlanoActionPerformed

    private void BotaoPlanoDuplicarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotaoPlanoDuplicarActionPerformed
        if(!IDPlano.equals("-1"))
        {
            Planejamento plano = new Planejamento(this);
            plano.PreencherFormulario(Lingua.getMensagem("duplicar"), IDPlano, Integer.parseInt(PlanoMes), PlanoAno, PlanoValorInicial);
            plano.setVisible(true);
        }else{
            Janelinha.Aviso(Lingua.getMensagem("erro"), Lingua.getMensagem("plano_nao_cadastrado"));
            if(Janelinha.Pergunta(Lingua.getMensagem("cadastrar"), Lingua.getMensagem("deseja_cadastrar")))
            {
                Planejamento plano = new Planejamento(this);
                plano.PreencherFormulario(Lingua.getMensagem("cadastrar"),null,Integer.parseInt(PlanoMes),PlanoAno,null);
                plano.setVisible(true);
            }
        }
    }//GEN-LAST:event_BotaoPlanoDuplicarActionPerformed

    private void MBGerarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MBGerarActionPerformed
        new GerarBackup().setVisible(true);
    }//GEN-LAST:event_MBGerarActionPerformed

    private void MBImportarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MBImportarActionPerformed
        new ImportarBackup().setVisible(true);
    }//GEN-LAST:event_MBImportarActionPerformed

    private void MUConfiguracaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MUConfiguracaoActionPerformed
        Botoes("Configuracao");
    }//GEN-LAST:event_MUConfiguracaoActionPerformed

    private void MALinksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MALinksActionPerformed
        new Links(this).setVisible(true);
    }//GEN-LAST:event_MALinksActionPerformed

    private void MATutorialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MATutorialActionPerformed
        new Tutorial(true,this,true).setVisible(true);
    }//GEN-LAST:event_MATutorialActionPerformed

    private void MUExecutarSQLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MUExecutarSQLActionPerformed
        new ExecutarSQL(this).setVisible(true);
    }//GEN-LAST:event_MUExecutarSQLActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Linguagem Lingua = Linguagem.getInstance();
                try {
                    Font FontFinancas = Fonte.getInstance().getFonte();                   
                    UIManager.setLookAndFeel("org.jvnet.substance.skin.Substance"+Configuracoes.getPropriedade("tema")+"LookAndFeel");
                    UIManager.put("OptionPane.messageFont", FontFinancas);
                } catch (ClassNotFoundException ex) {
                    Janelinha.Aviso(Lingua.getMensagem("erro"), Lingua.getMensagem("excecao")+" "+ex);
                } catch (InstantiationException ex) {
                    Janelinha.Aviso(Lingua.getMensagem("erro"), Lingua.getMensagem("excecao")+" "+ex);
                } catch (IllegalAccessException ex) {
                    Janelinha.Aviso(Lingua.getMensagem("erro"), Lingua.getMensagem("excecao")+" "+ex);
                } catch (UnsupportedLookAndFeelException ex) {
                    Janelinha.Aviso(Lingua.getMensagem("erro"), Lingua.getMensagem("excecao")+" "+ex);
                } catch (SQLException ex) {
                    Janelinha.Aviso(Lingua.getMensagem("erro"), Lingua.getMensagem("excecao")+" "+ex);
                }
                TelaPrincipal Principal = new TelaPrincipal();
                Principal.setVisible(true);
                try {
                    Conexao.getInstance().executeQuery("SELECT nome FROM contas WHERE ativada='0'");
                    if(!Conexao.getInstance().getResultSet().next()){
                        new Tutorial(true,Principal,true).setVisible(true);
                    }
                } catch (SQLException ex) {
                    Janelinha.Aviso(Lingua.getMensagem("erro"), Lingua.getMensagem("excecao")+" "+ex);
                }
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BAgenda;
    private javax.swing.JButton BDespesas;
    private javax.swing.JButton BPlanejamento;
    private javax.swing.JButton BPlanejamentoAnterior;
    private javax.swing.JButton BPlanejamentoProximo;
    private javax.swing.JButton BRDespesas;
    private javax.swing.JButton BRReceitas;
    private javax.swing.JButton BReceitas;
    private javax.swing.JButton BSairCentro;
    private javax.swing.JButton BTransferencias;
    private javax.swing.JToolBar BarraFerramentas;
    private javax.swing.JButton BotaoPlano;
    private javax.swing.JButton BotaoPlanoDuplicar;
    private javax.swing.JButton BotaoPlanoItens;
    private javax.swing.JPanel Conteudo;
    private javax.swing.JLabel DataPrincipal;
    private javax.swing.JScrollPane Jresumo_despesas;
    private javax.swing.JMenuItem MALinks;
    private javax.swing.JMenuItem MATutorial;
    private javax.swing.JMenu MAjuda;
    private javax.swing.JMenuItem MBGerar;
    private javax.swing.JMenuItem MBImportar;
    private javax.swing.JMenu MBackup;
    private javax.swing.JMenuItem MCALembrete;
    private javax.swing.JMenuItem MCALembreteTipo;
    private javax.swing.JMenu MCAgenda;
    private javax.swing.JMenuItem MCContas;
    private javax.swing.JMenuItem MCDCategoria;
    private javax.swing.JMenuItem MCDItem;
    private javax.swing.JMenu MCDespesas;
    private javax.swing.JMenuItem MCGGrupo;
    private javax.swing.JMenuItem MCGGrupoItens;
    private javax.swing.JMenu MCGrupos;
    private javax.swing.JMenuItem MCPItem;
    private javax.swing.JMenuItem MCPPlano;
    private javax.swing.JMenu MCPlanejamento;
    private javax.swing.JMenuItem MCRCategoria;
    private javax.swing.JMenuItem MCRItem;
    private javax.swing.JMenu MCReceitas;
    private javax.swing.JMenuItem MCTCategoria;
    private javax.swing.JMenuItem MCTItem;
    private javax.swing.JMenu MCTransferencias;
    private javax.swing.JMenuItem MCUsuarios;
    private javax.swing.JMenu MCadastros;
    private javax.swing.JMenuItem MMDespesas;
    private javax.swing.JMenuItem MMReceitas;
    private javax.swing.JMenuItem MMTransferencias;
    private javax.swing.JMenu MMovimentacoes;
    private javax.swing.JMenuItem MRDCategoria;
    private javax.swing.JMenuItem MRDDespesaTempo;
    private javax.swing.JMenuItem MRDItem;
    private javax.swing.JMenu MRDespesas;
    private javax.swing.JMenuItem MRRCategorias;
    private javax.swing.JMenuItem MRRItens;
    private javax.swing.JMenu MRReceitas;
    private javax.swing.JMenu MRelatorios;
    private javax.swing.JMenu MSair;
    private javax.swing.JMenuItem MUConfiguracao;
    private javax.swing.JMenuItem MUExecutarSQL;
    private javax.swing.JMenuItem MUSenha;
    private javax.swing.JMenu MUtilitarios;
    private javax.swing.JMenuBar MenuPrincipal;
    private javax.swing.JPanel PainelRodape;
    protected javax.swing.JTable agenda;
    private javax.swing.JTable contas;
    private javax.swing.JTable grupos;
    private javax.swing.JLabel grupos_saldo;
    private javax.swing.JScrollPane jsagenda;
    private javax.swing.JScrollPane jscontas;
    private javax.swing.JScrollPane jsgrupos;
    private javax.swing.JScrollPane jsplanejamento;
    private javax.swing.JLabel lblAgenda1;
    private javax.swing.JLabel lblRelat;
    private javax.swing.JLabel lblRelat1;
    private javax.swing.JLabel plan_botoes;
    private javax.swing.JLabel plan_navegacao1;
    private javax.swing.JTable planejamento;
    private javax.swing.JLabel planejamento_final;
    private javax.swing.JLabel planejamento_inicial;
    private javax.swing.JLabel planejamento_titulo;
    private javax.swing.JTable relatorio;
    private javax.swing.JLabel relatorio_mensal;
    private javax.swing.JLabel saldo_total;
    private javax.swing.JLabel usuario_logado;
    // End of variables declaration//GEN-END:variables
    
}
