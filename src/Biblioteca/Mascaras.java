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

import java.text.ParseException;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;

public class Mascaras {
    
    private Mascaras(){ }
    
    public static DefaultFormatterFactory Cep()   
    {   
        MaskFormatter cep = null;   
        try    
        {    
            cep = new MaskFormatter("##.###-###");   
        }    
        catch (ParseException e) { }   
        DefaultFormatterFactory factory = new DefaultFormatterFactory(cep, cep);   
        return factory;   
    }
    
    public static DefaultFormatterFactory Data()   
    {   
        MaskFormatter data = null;   
        try    
        {    
            data = new MaskFormatter("##/##/####");   
        }    
        catch (ParseException e) { }   
        DefaultFormatterFactory factory = new DefaultFormatterFactory(data, data);   
        return factory;   
    }
    
    public static DefaultFormatterFactory CPF()   
    {   
        MaskFormatter cpf = null;   
        try    
        {    
            cpf = new MaskFormatter("###.###.###-##");   
        }    
        catch (ParseException e) { }   
        DefaultFormatterFactory factory = new DefaultFormatterFactory(cpf, cpf);   
        return factory;   
    }
    
    public static DefaultFormatterFactory CNPJ()   
    {   
        MaskFormatter cnpj = null;   
        try    
        {    
            cnpj = new MaskFormatter("##.###.###/####-##");   
        }    
        catch (ParseException e) { }   
        DefaultFormatterFactory factory = new DefaultFormatterFactory(cnpj, cnpj);   
        return factory;   
    }
    
}
