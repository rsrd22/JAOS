/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tabla;

import Control.ControlCitas;
import Vistas.VentanaPrincipal;
import java.awt.Cursor;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 *
 * @author Asus
 */
public class TablaAgendaDiaria {
    ControlCitas cita = new ControlCitas();
    //ConfirmacionRender render = new ConfirmacionRender();
    //String[] columnas = new String[]{"HORA","PACIENTES", "TELEFONOS", "CONFIRMAR", "MENSAJE", "CANCELAR", "R CITA"};
    ArrayList<String[]> listaDatos = new ArrayList<String[]>();
       
    public void Vizualizar(final VentanaPrincipal frame, ArrayList<String[]> listaDatos){
        try{
            
//            System.out.println("************Vizualizar*************");
        this.listaDatos = listaDatos;
        frame.tblAgendaDiaria.setDefaultRenderer(Object.class, new ConfirmacionRender());
        
        frame.modelo = new DefaultTableModel(new String[]{"Hora","Paciente", "Motivo", "Estado", "", "", ""}, 0){
            Class[] types = new Class[]{
                java.lang.Object.class,java.lang.Object.class,java.lang.Object.class,
                java.lang.Object.class,
                java.lang.Object.class,java.lang.Object.class,java.lang.Object.class
            };
            
            public Class getColumnClass(int col){
                return types[col];
            } 
            
            public boolean isCellEditable(int row,int col){
                //System.out.println("row-->"+row+" -- col-->"+col+"::  ::: "+edit[col]);
                return false;
            }
            
            
        };  
          
        
        JButton ag = new JButton();
        ag.setName("v");
        ag.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/agenda_mini_2.png")));
        ag.setToolTipText("Agenda");
        ag.setPreferredSize(new java.awt.Dimension(16, 16));
        ag.setContentAreaFilled(false);
        JButton hc = new JButton();
        hc.setName("v");
        hc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/historiaclinica_mini_1.png")));
        hc.setToolTipText("Historia Clinica");
        hc.setPreferredSize(new java.awt.Dimension(16, 16));
        hc.setContentAreaFilled(false);
        JButton fact = new JButton();
        fact.setName("v");
        fact.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/factura_mini_2.png")));        
        fact.setToolTipText("Facturación");
        fact.setPreferredSize(new java.awt.Dimension(16, 16));
        fact.setCursor(new Cursor(Cursor.HAND_CURSOR));
        fact.setContentAreaFilled(false);
        
//        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/guardar32.png"))); // NOI18N
//        btnGuardar.setToolTipText("Guardar");
//        btnGuardar.setContentAreaFilled(false);
//        btnGuardar.setMaximumSize(new java.awt.Dimension(81, 57));
//        btnGuardar.setMinimumSize(new java.awt.Dimension(81, 57));
//        btnGuardar.setPreferredSize(new java.awt.Dimension(32, 32));
        
        if(listaDatos == null){
            listaDatos = new ArrayList<String[]>();
        }
        //if(listaDatos.size()>0){
            String HoraDig = "";
            int i = 0, fil = -1, min = 40, hora =6 ;
            int minAu = 20;
            String[] datos;
            Object[] objetos;
            String aux = "";
            
            while(i < 13){
                
                objetos = new Object[7];
                if(min==60){
                    i++;  
                    hora +=1; 
                    min = 0;
                }
                HoraDig = (String.valueOf(hora).length() == 1?"0"+hora:hora)+":"+(String .valueOf(min).length()==1?"0"+min:min);     
                aux = frame.getHoraNula(HoraDig); 
                
                if(aux.equals("")){
                    ArrayList<String[]> listad = frame.getDatosxHora(HoraDig); 
                    if(listad!=null && listad.size()>0){
                        for(int j = 0; j < listad.size(); j++){
                            fil++;                         ///FILA  //PK_CITA, PAUX, ID, PACIENTE, HORA, ESTADO

                            frame.Lista_auxiliar.add(new String[]{""+fil, ""+listad.get(j)[0], ""+listad.get(j)[1], ""+listad.get(j)[2], ""+listad.get(j)[3], ""+listad.get(j)[6], ""+listad.get(j)[7], ""+listad.get(j)[8]});
                            objetos[0] = ""+frame.getHora(HoraDig);
                            objetos[1] = ""+listad.get(j)[3];
                            objetos[2] = ""+listad.get(j)[12];
                            
                            objetos[3] = ""+listad.get(j)[5];
                            objetos[4] = ag;
                            objetos[5] = hc;
                            objetos[6] = fact;
                            frame.modelo.addRow(objetos);
                            //agregarFila(objetos);  
                        }
                    }else{
                        fil++;
                        objetos[0] = frame.getHora(HoraDig);
                        objetos[1] = "";
                        objetos[2] = "";
                        objetos[3] = "";
                        objetos[4] = "";
                        objetos[5] = "";
                        objetos[6] = "";
                        frame.modelo.addRow(objetos);
                        //agregarFila(objetos);  
                    }
                }
                frame.tblAgendaDiaria.setModel(frame.modelo);  
                min += minAu;
            }            
            
//        }else{
////            frame.lblMensaje.setText("No se encontraron pacientes para la fecha seleccionada.");
//        }
        frame.tblAgendaDiaria.setModel(frame.modelo);
            

        frame.tblAgendaDiaria.getColumnModel().getColumn(0).setPreferredWidth(50);
        frame.tblAgendaDiaria.getColumnModel().getColumn(1).setPreferredWidth(180);
        frame.tblAgendaDiaria.getColumnModel().getColumn(2).setPreferredWidth(100);
        frame.tblAgendaDiaria.getColumnModel().getColumn(3).setPreferredWidth(50);
        frame.tblAgendaDiaria.getColumnModel().getColumn(4).setPreferredWidth(20);
        frame.tblAgendaDiaria.getColumnModel().getColumn(5).setPreferredWidth(20);
        frame.tblAgendaDiaria.getColumnModel().getColumn(6).setPreferredWidth(20);
        frame.tblAgendaDiaria.setRowHeight(23);
        for(i = 0; i < frame.modelo.getColumnCount(); i++){                
            frame.tblAgendaDiaria.getColumnModel().getColumn(i).setResizable(false);
        }
        JTableHeader header = frame.tblAgendaDiaria.getTableHeader();
        ((DefaultTableCellRenderer)header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
        }catch(Exception e){
            System.out.println("ERROR VIZUALIZAR");
            e.printStackTrace();
        }
    }
    
    public String[] getDatos(int row) {
        try{
            String[] ret = new String[]{"", "", ""}; 
            for(int i = 0; i < listaDatos.size(); i++){
                if(i == row){
                    ret[0]=listaDatos.get(i)[3];
                    ret[1]=listaDatos.get(i)[4];
                    ret[2]=listaDatos.get(i)[2];
                }
            }
            return ret;
        }catch(Exception e){
            System.out.println("ERROR --getDatos->"+e.toString());
            return null;
        }
    }
}
