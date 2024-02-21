/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Tabla;

import Control.ControlCitas;
import Vistas.VentanaConfirmacion;
import java.awt.Cursor;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel; 
import javax.swing.table.JTableHeader;

/**
 *
 * @author AT-DESARROLLO
 */
public class TablaConfirmacion {
    
    ControlCitas cita = new ControlCitas();
    ConfirmacionRender render = new ConfirmacionRender();
    String[] columnas = new String[]{"HORA","PACIENTES", "TELEFONOS", "CONFIRMAR", "MENSAJE", "CANCELAR", "R CITA"};
    ArrayList<String[]> listaDatos = new ArrayList<String[]>();
       
    public void Vizualizar(final VentanaConfirmacion frame, ArrayList<String[]> listaDatos){
        this.listaDatos = listaDatos;
        frame.tblListaConfirmacion.setDefaultRenderer(Object.class, new ConfirmacionRender());
        
        frame.modelo = new DefaultTableModel(new String[]{"HORA","PACIENTES", "TELEFONOS", "CONFIRMAR", "MENSAJE", "CANCELAR", "REPROGRAMAR"}, 0){
            Class[] types = new Class[]{
                java.lang.Object.class,java.lang.Object.class,java.lang.Object.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class,java.lang.Object.class
            };
            
            public Class getColumnClass(int col){
                return types[col];
            } 
            
            public boolean isCellEditable(int row,int col){
                //System.out.println("row-->"+row+" -- col-->"+col+"::  ::: "+edit[col]);
                return  frame.edit[col];
            }
            
            
        };  
          
        
        JButton btn = new JButton();
        btn.setName("v");
        btn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/repro22.png")));
        btn.setToolTipText("Reprogramar");
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new java.awt.Dimension(16, 16));
        btn.setContentAreaFilled(false);
        
        
        
//        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/guardar32.png"))); // NOI18N
//        btnGuardar.setToolTipText("Guardar");
//        btnGuardar.setContentAreaFilled(false);
//        btnGuardar.setMaximumSize(new java.awt.Dimension(81, 57));
//        btnGuardar.setMinimumSize(new java.awt.Dimension(81, 57));
//        btnGuardar.setPreferredSize(new java.awt.Dimension(32, 32));
        
        if(listaDatos == null){
            listaDatos = new ArrayList<String[]>();
        }
        if(listaDatos.size()>0){
            for(int i = 0; i < listaDatos.size(); i++){  
                Object[] fila = new Object[7];
                fila[0] = listaDatos.get(i)[0];
                fila[1] = listaDatos.get(i)[4];
                fila[2] = listaDatos.get(i)[5];
                fila[3] = listaDatos.get(i)[6].equals("Confirmar");
                fila[4] = listaDatos.get(i)[6].equals("Mensaje");
                fila[5] = listaDatos.get(i)[6].equals("Cancelar");
                fila[6] = btn;
                
                frame.modelo.addRow(fila);
            }  
            frame.lblMensaje.setText("");
            
        }else{
            frame.lblMensaje.setText("No se encontraron pacientes para la fecha seleccionada.");
        }
        frame.tblListaConfirmacion.setModel(frame.modelo);
            

        frame.tblListaConfirmacion.getColumnModel().getColumn(0).setPreferredWidth(95);
        frame.tblListaConfirmacion.getColumnModel().getColumn(1).setPreferredWidth(445);
        frame.tblListaConfirmacion.getColumnModel().getColumn(2).setPreferredWidth(280);
        frame.tblListaConfirmacion.getColumnModel().getColumn(3).setPreferredWidth(87);
        frame.tblListaConfirmacion.getColumnModel().getColumn(4).setPreferredWidth(80);
        frame.tblListaConfirmacion.getColumnModel().getColumn(5).setPreferredWidth(80);
        frame.tblListaConfirmacion.getColumnModel().getColumn(6).setPreferredWidth(115);
        frame.tblListaConfirmacion.setRowHeight(23);
        for(int i = 0; i < frame.modelo.getColumnCount(); i++){                
            
            frame.tblListaConfirmacion.getColumnModel().getColumn(i).setResizable(false);
        }
        JTableHeader header = frame.tblListaConfirmacion.getTableHeader();
        ((DefaultTableCellRenderer)header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
        
    }
    
    public String[] getDatos(int row) {
        try{
            ///RETORNA   
            // 0---->id paciento
            // 1---->NOmbre paciente
            // 2---->ID CITA
            // 3---->Motivo
            String[] ret = new String[]{"", "", "", ""}; 
            for(int i = 0; i < listaDatos.size(); i++){
                if(i == row){
                    ret[0]=listaDatos.get(i)[3];
                    ret[1]=listaDatos.get(i)[4];
                    ret[2]=listaDatos.get(i)[2];
                    ret[3]=listaDatos.get(i)[listaDatos.get(i).length-1];
                }
            }
            return ret;
        }catch(Exception e){
            System.out.println("ERROR --getDatos->"+e.toString());
            return null;
        }
    }
}
