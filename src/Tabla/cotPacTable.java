/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Tabla;

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author AT-DESARROLLO
 */
public class cotPacTable extends JTable{
    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int col){
        Component component = super.prepareRenderer(renderer, row, col);
//        System.out.println("row-->"+row);
        int mod = row % 2;
//        System.out.println("mod -->"+mod);
        if(mod==0){
            component.setBackground(Color.WHITE);             
        }else {
            component.setBackground(new Color(242, 242, 242)); 
        }  
        component.setForeground(Color.black);
        if(col == 6){
            String dato = String.valueOf(getValueAt(row, col));
            if(dato.equals("Quitar"))
                component.setForeground(Color.BLUE); 
            else
                component.setForeground(Color.black); 
        }if(col == 5){
            String dato = String.valueOf(getValueAt(row, col));
            if(dato.equals("Activar"))
                component.setForeground(Color.BLUE); 
            else
                component.setForeground(Color.black); 
        }
        
        return component;
    }
}
