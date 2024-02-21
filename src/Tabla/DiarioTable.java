/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Tabla;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author AT-DESARROLLO
 */
public class DiarioTable extends JTable{
    
    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int col){
        Component component = super.prepareRenderer(renderer, row, col);
        String dato = String.valueOf(getValueAt(row, 3));
        if(col >-1){
            if(dato.toUpperCase().equals("AUSENTE")){
                component.setBackground(Color.RED); 
                component.setForeground(Color.BLACK); 
                //component.setForeground(Color.BLUE); 
            }else if(dato.toUpperCase().equals("ESPERA")){
                component.setBackground(new Color(255, 239, 39)); 
                    component.setForeground(Color.BLACK); 
            }else if(dato.toUpperCase().equals("ATENDIDO")){
                component.setBackground(new Color(147, 213, 53)); 
                component.setForeground(Color.BLACK); 
            }else if(dato.toUpperCase().equals("ATENDIENDO")){
                //component.setBackground(Color.BLUE); 
                //[26,82,118]
                component.setBackground(new Color(127, 213, 205));
                component.setForeground(Color.black); 
            }else{
                component.setBackground(Color.white); 
            }
        }else{
            
        }
        return component;
    }
    
}
