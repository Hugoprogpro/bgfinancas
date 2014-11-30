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

package Backup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ZipUnzip {

    public static void compactar(File[] arquivos_entrada, File arquivo_saida) throws IOException {
        if (arquivos_entrada != null && arquivos_entrada.length > 0) {
            FileOutputStream saida = new FileOutputStream(arquivo_saida);
            ZipOutputStream saida_zip = new ZipOutputStream(saida);
            compactarArquivos(arquivos_entrada, saida_zip);
            saida_zip.close();
        }
    }

    private static void compactarArquivos(File[] arquivos_entrada, ZipOutputStream saida_zip) throws IOException {
        byte[] buffer = new byte[1024];
        for (int i = 0; i < arquivos_entrada.length; i++) {
            FileInputStream entrada = new FileInputStream(arquivos_entrada[i]);
            saida_zip.putNextEntry(new ZipEntry(arquivos_entrada[i].getName()));
            int tamanho;
            while ((tamanho = entrada.read(buffer)) > 0) {
                saida_zip.write(buffer,0,tamanho);
            }
            saida_zip.closeEntry();
            entrada.close();
        }
    }

    public static void descompactar(File arquivo_zip, File diretorio_saida) throws IOException {
        ZipFile zip = null;
        File arquivo = null;
        InputStream entrada = null;
        OutputStream saida = null;
        byte[] buffer = new byte[1024];
        try {
            if (!diretorio_saida.exists()) {
                diretorio_saida.mkdirs();
            }
            if (!diretorio_saida.exists() || !diretorio_saida.isDirectory()) {
                throw new IOException("Directory "+diretorio_saida.getName()+" invalid.");
            }
            zip = new ZipFile(arquivo_zip);
            Enumeration conteudo_zip = zip.entries();
            while (conteudo_zip.hasMoreElements()) {
                ZipEntry arquivo_zipado = (ZipEntry) conteudo_zip.nextElement();
                arquivo = new File(diretorio_saida, arquivo_zipado.getName());
                if (!arquivo.getParentFile().exists()) {
                    arquivo.getParentFile().mkdirs();
                }
                try {
                    entrada = zip.getInputStream(arquivo_zipado);
                    saida = new FileOutputStream(arquivo);
                    int bytesLidos = 0;
                    if (arquivo_zipado == null) {
                        throw new ZipException("Erro ao ler a entrada do zip: "+arquivo_zipado.getName());
                    }
                    while ((bytesLidos = entrada.read(buffer)) > 0) {
                        saida.write(buffer, 0, bytesLidos);
                    }
                } finally {
                    if (entrada != null) {
                        try {
                            entrada.close();
                        } catch (IOException ex) { }
                    }
                    if (saida != null) {
                        try {
                            saida.close();
                        } catch (IOException ex) { }
                    }
                }
            }
        } finally {
            if (zip != null) {
                try {
                    zip.close();
                } catch (IOException e) { }
            }
        }
    }
}
