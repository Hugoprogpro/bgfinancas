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

package Relatorios;

import Biblioteca.Datas;

import Biblioteca.Janelinha;
import Biblioteca.Numeros;
import Biblioteca.Relatorio;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

public final class RDespesasItens extends Relatorio {

    private String DataInicial,DataFinal;
    private String DataExtenso,Ordenacao,Ordenacao2;

    public void RDespesasItens(String data_inicial, String data_final, String ordenacao, String ordenacao2) throws ParseException
    {
        String Titulo = Lingua.getMensagem("despesas")+" > "+Lingua.getMensagem("periodo_item");
        DataInicial = data_inicial;
        DataFinal = data_final;
        Ordenacao = ordenacao;
        Ordenacao2 = ordenacao2;
        DataExtenso = Datas.DataExtenso(DataInicial,DataFinal);
        JasperPrint Relatorio;
        try {
	    Relatorio = GerarRelatorio();
            List lista = new ArrayList();
            lista = Relatorio.getPages();
            if(lista.size()>0)
            {
                JasperViewer RelatorioFinal = new JasperViewer(Relatorio, false);
                RelatorioFinal.setTitle(Titulo);
                RelatorioFinal.setExtendedState(JasperViewer.MAXIMIZED_BOTH);
                RelatorioFinal.setVisible(true);
            }else{
                Janelinha.Aviso((Lingua.getMensagem("erro")), Lingua.getMensagem("relatorio_sem_dados"));
            }
	} catch (Exception e) {
	    Janelinha.Aviso((Lingua.getMensagem("erro")), Lingua.getMensagem("excecao")+" "+e.getMessage());
	}

    }

    public JasperPrint GerarRelatorio() {

		JasperPrint relatorio = null;
                Double ValorTotal=0.0;
                String SQL = "SELECT SUM(despesas.valor) AS valor_total, despesas_categorias.nome AS categoria_nome, SUM(despesas.quantidade) AS quantidade, despesas_itens.nome AS item_nome FROM despesas_categorias, despesas, despesas_itens WHERE despesas.id_item=despesas_itens.id_item AND despesas_itens.id_categoria=despesas_categorias.id_categoria AND despesas.data>='"+DataInicial+"' AND despesas.data<='"+DataFinal+"' GROUP BY categoria_nome, item_nome";
                try {
                    if(Ordenacao.equals(Lingua.getMensagem("item"))){
                        Banco.executeQuery(SQL+" ORDER BY item_nome ASC");
                    } else if (Ordenacao.equals(Lingua.getMensagem("valor"))){
                        Banco.executeQuery(SQL+" ORDER BY valor_total DESC");
                    } else if (Ordenacao.equals(Lingua.getMensagem("categoria"))){
                        if(Ordenacao2.equals(Lingua.getMensagem("valor")))
                            Banco.executeQuery(SQL+" ORDER BY categoria_nome ASC, valor_total DESC");
                        else if(Ordenacao2.equals(Lingua.getMensagem("quantidade")))
                            Banco.executeQuery(SQL+" ORDER BY categoria_nome ASC, quantidade DESC");
                        else
                            Banco.executeQuery(SQL+" ORDER BY categoria_nome ASC, item_nome ASC");
                    } else{
                        Banco.executeQuery(SQL+" ORDER BY quantidade DESC");
                    }
                    while(Banco.getResultSet().next())
                    {
                        ValorTotal+=Double.parseDouble(Banco.getResultSet().getString("valor_total"));
                    }
                    if(Ordenacao.equals(Lingua.getMensagem("item"))){
                        Banco.executeQuery(SQL+" ORDER BY item_nome ASC");
                    } else if (Ordenacao.equals(Lingua.getMensagem("valor"))){
                        Banco.executeQuery(SQL+" ORDER BY valor_total DESC");
                    } else if (Ordenacao.equals(Lingua.getMensagem("categoria"))){
                        if(Ordenacao2.equals(Lingua.getMensagem("valor")))
                            Banco.executeQuery(SQL+" ORDER BY categoria_nome ASC, valor_total DESC");
                        else if(Ordenacao2.equals(Lingua.getMensagem("quantidade")))
                            Banco.executeQuery(SQL+" ORDER BY categoria_nome ASC, quantidade DESC");
                        else
                            Banco.executeQuery(SQL+" ORDER BY categoria_nome ASC, despesas_itens.nome ASC");
                    } else{
                        Banco.executeQuery(SQL+" ORDER BY quantidade DESC");
                    }
                } catch (SQLException ex) {
                    Janelinha.Aviso((Lingua.getMensagem("erro")), Lingua.getMensagem("excecao")+" "+ex.getMessage());
                }

                try {
			HashMap parametros = new HashMap();
                        parametros.put("TITULO_RELATORIO", Lingua.getMensagem("despesas")+" - "+Lingua.getMensagem("itens"));
                        parametros.put("CATEGORIA", Lingua.getMensagem("categoria"));
                        parametros.put("ITEM", Lingua.getMensagem("item"));
                        parametros.put("VALOR", Lingua.getMensagem("valor"));
                        parametros.put("PAGINA", Lingua.getMensagem("pagina"));
                        parametros.put("DE", Lingua.getMensagem("de_pagina"));
                        parametros.put("QUANTIDADE", Lingua.getMensagem("quantidade"));
                        parametros.put("MOEDA", Lingua.getMensagem("moeda"));
                        parametros.put("DATAEXTENSO", DataExtenso);
                        parametros.put("VALORTOTAL", Lingua.getMensagem("valor_total")+": "+Lingua.getMensagem("moeda")+" "+Numeros.Arrendondar(ValorTotal).toString());
                        URL arquivo = getClass().getResource("/Formularios/DespesasItens.jasper");
                        JasperReport arquivoJasper = (JasperReport) JRLoader.loadObject( arquivo );
                        JRResultSetDataSource JRresultset = new JRResultSetDataSource(Banco.getResultSet());
			relatorio = JasperFillManager.fillReport(arquivoJasper, parametros, JRresultset);
		} catch (JRException e) {
			Janelinha.Aviso((Lingua.getMensagem("erro")), Lingua.getMensagem("excecao")+" "+e.getMessage());
		}
		return relatorio;
    }
}