/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Tabla;

import Control.ControlUsuarios;
import Vistas.VentanaUsuarios;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 *
 * @author AT-DESARROLLO
 */
public class TablaPermisos {
    ControlUsuarios cita = new ControlUsuarios();
    ConfirmacionRender render = new ConfirmacionRender();
    String[] columnas = new String[]{"MODULO","CONSULTAR", "GUARDAR", "MODIFICAR", "ELIMINAR", "VER", "TODO"};
    ArrayList<String[]> listaDatos = new ArrayList<String[]>(); 
    public int bandera = 0;
    
    public void Vizualizar(final VentanaUsuarios frame, ArrayList<String[]> listaDatos){
        this.listaDatos = listaDatos; 
        frame.tblPermisos.setDefaultRenderer(Object.class, new ConfirmacionRender());
        
        DefaultTableModel dt = new DefaultTableModel(columnas, 0){
            Class[] types = new Class[]{
                java.lang.Object.class,
                java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class
                , java.lang.Boolean.class
            };
            
            public Class getColumnClass(int col){
                return types[col];
            }
            
            public boolean isCellEditable(int row,int col){
                //System.out.println("row-->"+row+" -- col-->"+col+"::::: "+edit[col]);
                return frame.edit[col]; 
            }
        };  
          
        
        
        if(listaDatos == null){
            listaDatos = new ArrayList<String[]>();
        }
        if(listaDatos.size()>0){
            for(int i = 0; i < listaDatos.size(); i++){
                Object[] fila = new Object[7];
                fila[0] = listaDatos.get(i)[1];
                fila[1] = listaDatos.get(i)[2].equals("1");
                fila[2] = listaDatos.get(i)[3].equals("1");
                fila[3] = listaDatos.get(i)[4].equals("1");  
                fila[4] = listaDatos.get(i)[5].equals("1");
                fila[5] = listaDatos.get(i)[6].equals("1");
                fila[6] = listaDatos.get(i)[2].equals("1") && listaDatos.get(i)[3].equals("1")
                        && listaDatos.get(i)[4].equals("1") && listaDatos.get(i)[5].equals("1") && listaDatos.get(i)[6].equals("1");
                
                dt.addRow(fila);
            }  
            
            
        }
        frame.tblPermisos.setModel(dt);
        

        frame.tblPermisos.getColumnModel().getColumn(0).setPreferredWidth(450);
        frame.tblPermisos.getColumnModel().getColumn(1).setPreferredWidth(100);
        frame.tblPermisos.getColumnModel().getColumn(2).setPreferredWidth(100);
        frame.tblPermisos.getColumnModel().getColumn(3).setPreferredWidth(100);
        frame.tblPermisos.getColumnModel().getColumn(4).setPreferredWidth(100);
        frame.tblPermisos.getColumnModel().getColumn(5).setPreferredWidth(100);
        frame.tblPermisos.getColumnModel().getColumn(5).setPreferredWidth(70);
        
        for(int i = 0; i < dt.getColumnCount(); i++){                
            frame.tblPermisos.getColumnModel().getColumn(i).setResizable(false);
        }
        
        JTableHeader header = frame.tblPermisos.getTableHeader();
        ((DefaultTableCellRenderer)header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
        
        frame.tblPermisos.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                int v = 0;
                if(bandera == 0){
                    bandera = 1;
                    v = frame.tblPermisos.columnAtPoint(e.getPoint());
                    System.out.println("****************mouseClicked***********************----"+v);
                    if(v == 6){
                        int n = frame.tblPermisos.getRowCount();
                        System.out.println("n--->"+n);
                        Object value = frame.tblPermisos.getValueAt(0, 6);
                        for(int i = 0; i < n; i++){
                            System.out.println("VALOR-------"+(Boolean)value);
                            if(!(Boolean)value){
                                frame.tblPermisos.setValueAt(true, i, 1);
                                frame.tblPermisos.setValueAt(true, i, 2);
                                frame.tblPermisos.setValueAt(true, i, 3);
                                frame.tblPermisos.setValueAt(true, i, 4);
                                frame.tblPermisos.setValueAt(true, i, 5);
                                frame.tblPermisos.setValueAt(true, i, 6);
                            }else{
                                frame.tblPermisos.setValueAt(false, i, 1);
                                frame.tblPermisos.setValueAt(false, i, 2);
                                frame.tblPermisos.setValueAt(false, i, 3);
                                frame.tblPermisos.setValueAt(false, i, 4);
                                frame.tblPermisos.setValueAt(false, i, 5);
                                frame.tblPermisos.setValueAt(false, i, 6);
                            }
                        }
                    }
                }else{
                        bandera = 0;
                }
            }
        });
        
        
    }
    
    
    
}
