/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tabla;

import java.awt.Component;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Asus
 */
public class NumberTableCellRenderer extends DefaultTableCellRenderer {
    private Format formatter = new DecimalFormat("###.###");
    public void setvalue(Object value){
        try{
            if (value != null)
                value = formatter.format(value);
        }
        catch(IllegalArgumentException e) {}
 
        super.setValue(value);
    }
    
//    public NumberTableCellRenderer() {
//        
//    }
//    @Override
//    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//        if (value instanceof Number) {
//            value = NumberFormat.getNumberInstance().format(value);
//        }
//        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
//    }

}
