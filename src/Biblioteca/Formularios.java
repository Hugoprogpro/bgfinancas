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

import java.awt.Component;
import java.awt.Container;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Formularios {
    
    private Formularios(){ }
    
    public static void LimparFormulario(Container container)
    {
        Component components[] = container.getComponents();
        for (Component component : components) {
            if (component instanceof JFormattedTextField) {
               JFormattedTextField field = (JFormattedTextField) component;
               field.setValue(null);
            } else if (component instanceof JTextField) {
               JTextField field = (JTextField) component;
               field.setText("");
            } else if (component instanceof JTextArea) {
               JTextArea area = (JTextArea) component;
               area.setText("");
            } else if (component instanceof Container) {
               LimparFormulario((Container) component);
            }
        }
    }
    
    public static void AtivarFormulario(Container container)
    {
        Component components[] = container.getComponents();
        for (Component component : components) {
            if (component instanceof JFormattedTextField) {
               JFormattedTextField field = (JFormattedTextField) component;
               field.setEnabled(true);
            } else if (component instanceof JTextField) {
               JTextField field = (JTextField) component;
               field.setEnabled(true);
            } else if (component instanceof JTextArea) {
               JTextArea area = (JTextArea) component;
               area.setEnabled(true);
            } else if (component instanceof JButton) {
               JButton area = (JButton) component;
               area.setEnabled(true);
            } else if (component instanceof JLabel) {
               JLabel area = (JLabel) component;
               area.setEnabled(true);
            }else if (component instanceof JComboBox) {
               JComboBox area = (JComboBox) component;
               area.setEnabled(true);
            } else if (component instanceof Container) {
               AtivarFormulario((Container) component);
            }
        }
    }
    
    public static void DesativarFormulario(Container container)
    {
        Component components[] = container.getComponents();
        for (Component component : components) {
            if (component instanceof JFormattedTextField) {
               JFormattedTextField field = (JFormattedTextField) component;
               field.setEnabled(false);
            } else if (component instanceof JTextField) {
               JTextField field = (JTextField) component;
               field.setEnabled(false);
            } else if (component instanceof JTextArea) {
               JTextArea area = (JTextArea) component;
               area.setEnabled(false);
            } else if (component instanceof JButton) {
               JButton area = (JButton) component;
               area.setEnabled(false);
            } else if (component instanceof JLabel) {
               JLabel area = (JLabel) component;
               area.setEnabled(false);
            }else if (component instanceof JComboBox) {
               JComboBox area = (JComboBox) component;
               area.setEnabled(false);
            }
            else if (component instanceof Container) {
               DesativarFormulario((Container) component);
            }
        }
    }
    
}
