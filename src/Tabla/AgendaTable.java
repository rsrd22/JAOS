/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tabla;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author richard
 */
public class AgendaTable extends JTable{
    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int col){
        Component component = super.prepareRenderer(renderer, row, col);
        
        
        component.setForeground(Color.BLACK);
        if(col == 0){
            component.setBackground(Color.LIGHT_GRAY);
        }
        if(col>0){
            String dato = String.valueOf(getValueAt(row, col));     
            if(dato.equals("")){
                component.setBackground(Color.WHITE);
                
            }else if(dato.equals("OCUPADO")){
                component.setBackground(Color.GREEN);
            }else if(dato.equals("SELECCIONADO")){
                component.setBackground(Color.BLUE);                
            }else if(dato.equals("NO DISPONIBLE")){
                component.setBackground(Color.RED);
            }
            
        }
        
        
        
        return component;
    }
}
