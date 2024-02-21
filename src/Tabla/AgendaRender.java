/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tabla;

import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author richard
 */
public class AgendaRender extends DefaultTableCellRenderer {
    
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int fil, int col){
        JLabel cell = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, fil, col);
        if(col == 0){
            cell.setBackground(Color.GRAY);
        }
        if(col>0 && !value.equals("")){
            cell.setBackground(Color.BLUE);
        }
        
        return cell;
    }
}
