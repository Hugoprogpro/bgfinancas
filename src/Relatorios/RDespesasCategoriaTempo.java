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

public final class RDespesasCategoriaTempo extends Relatorio {

    private String DataInicial,DataFinal;
    private String DataExtenso,Categoria,Tipo;
    private Integer TipoIndex; 

    public void RDespesasCategoriaTempo(String data_inicial, String data_final, String categoria, String tipo, Integer tipo_index) throws ParseException
    {
        String Titulo = Lingua.getMensagem("despesas")+" > "+Lingua.getMensagem("periodo_categoria_tempo");
        DataInicial = data_inicial;
        Tipo = tipo;
        DataFinal = data_final;
        TipoIndex = tipo_index;
        Categoria = categoria;
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
                int Quantidade=0;
                Double ValorTotal=0.0;

                try {
                    String id_categoria="";
                    String SQL = "SELECT SUM(despesas.valor) AS valor_total, MONTH(despesas.data) AS despesas_mes, YEAR(despesas.data) AS despesas_ano, despesas_categorias.nome FROM despesas,despesas_itens,despesas_categorias WHERE despesas.data>='"+DataInicial+"' AND despesas.data<='"+DataFinal+"' AND despesas.id_item=despesas_itens.id_item AND despesas_itens.id_categoria=despesas_categorias.id_categoria AND (despesas_categorias.id_categoria="+Categoria+") GROUP BY despesas_categorias.nome, despesas_mes, despesas_ano ORDER BY despesas_mes, despesas_ano";
                    Banco.executeQuery(SQL);
                    while(Banco.getResultSet().next())
                    {
                        ValorTotal+=Banco.getResultSet().getDouble("valor_total");
                        Quantidade++;
                    }
                    Banco.executeQuery(SQL);
                } catch (SQLException ex) {
                    Janelinha.Aviso((Lingua.getMensagem("erro")), Lingua.getMensagem("excecao")+" "+ex.getMessage());
                }

                try {
			HashMap parametros = new HashMap();
                        parametros.put("TITULO", Lingua.getMensagem("despesas")+" - "+Lingua.getMensagem("categoria_tempo"));
                        parametros.put("DATAEXTENSO", DataExtenso);
                        parametros.put("VALORTOTAL", Lingua.getMensagem("valor_total")+": "+Lingua.getMensagem("moeda")+" "+Numeros.Arrendondar(ValorTotal).toString());
                        parametros.put("CATEGORIA", Categoria);
                        parametros.put("MEDIA", Lingua.getMensagem("media")+": "+Lingua.getMensagem("moeda")+" "+Numeros.Arrendondar(ValorTotal/Quantidade).toString());
                        URL arquivo = null;
                        if(TipoIndex==0){
                            arquivo = getClass().getResource("/Formularios/DespesasCategoriaTempoBarras.jasper");
                        }else{
                            arquivo = getClass().getResource("/Formularios/DespesasCategoriaTempoLinhas.jasper");
                        }
                        JasperReport arquivoJasper = (JasperReport) JRLoader.loadObject( arquivo );
                        JRResultSetDataSource JRresultset = new JRResultSetDataSource(Banco.getResultSet());
			relatorio = JasperFillManager.fillReport(arquivoJasper, parametros, JRresultset);
		} catch (JRException e) {
			Janelinha.Aviso((Lingua.getMensagem("erro")), Lingua.getMensagem("excecao")+" "+e.getMessage());
		}
		return relatorio;
    }
}