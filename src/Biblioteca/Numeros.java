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

package Biblioteca;

import java.math.BigDecimal;

public class Numeros {
    
    private Numeros(){ }

    public static BigDecimal Arrendondar(Double valor) {
        BigDecimal valor_retorno = new BigDecimal(valor);
        valor_retorno = valor_retorno.setScale(2, BigDecimal.ROUND_HALF_UP);
        return valor_retorno;
    }
    
    public static BigDecimal Arrendondar(String valor) {
        Double Valor = Double.parseDouble(valor);
        BigDecimal valor_retorno = new BigDecimal(Valor);
        valor_retorno = valor_retorno.setScale(2, BigDecimal.ROUND_HALF_UP);
        return valor_retorno;
    }

}
