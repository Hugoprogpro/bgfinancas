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

public final class RReceitas extends Relatorio {

    private String DataInicial,DataFinal;
    private String DataExtenso;

    public void RReceitas(String data_inicial, String data_final) throws ParseException
    {
        String Titulo = Lingua.getMensagem("receitas")+" > "+Lingua.getMensagem("periodo_categoria");
        DataInicial = data_inicial;
        DataFinal = data_final;
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
                String SQL = "SELECT SUM(receitas.valor) AS valor_total, receitas_itens.id_categoria, receitas_categorias.nome FROM receitas, receitas_itens, receitas_categorias WHERE receitas.id_item=receitas_itens.id_item AND receitas_itens.id_categoria=receitas_categorias.id_categoria AND receitas.data>='"+DataInicial+"' AND receitas.data<='"+DataFinal+"' GROUP BY receitas_categorias.nome, receitas_itens.id_categoria";
                try {                  
                    Banco.executeQuery(SQL);
                    while(Banco.getResultSet().next())
                    {
                        ValorTotal+=Double.parseDouble(Banco.getResultSet().getString("valor_total"));
                    }
                    Banco.executeQuery(SQL);
                } catch (SQLException ex) {
                    Janelinha.Aviso((Lingua.getMensagem("erro")), Lingua.getMensagem("excecao")+" "+ex.getMessage());
                }

                try {
			HashMap parametros = new HashMap();
                        parametros.put("TITULO", Lingua.getMensagem("receitas")+" - "+Lingua.getMensagem("categorias"));
                        parametros.put("MOEDA", Lingua.getMensagem("moeda"));
                        parametros.put("DATAEXTENSO", DataExtenso);
                        parametros.put("VALORTOTAL", Lingua.getMensagem("valor_total")+": "+Lingua.getMensagem("moeda")+" "+Numeros.Arrendondar(ValorTotal).toString());
                        URL arquivo = getClass().getResource("/Formularios/Receitas.jasper");
                        JasperReport arquivoJasper = (JasperReport) JRLoader.loadObject( arquivo );
                        JRResultSetDataSource JRresultset = new JRResultSetDataSource(Banco.getResultSet());
			relatorio = JasperFillManager.fillReport(arquivoJasper, parametros, JRresultset);
		} catch (JRException e) {
			Janelinha.Aviso((Lingua.getMensagem("erro")), Lingua.getMensagem("excecao")+" "+e.getMessage());
		}
		return relatorio;
    }
}