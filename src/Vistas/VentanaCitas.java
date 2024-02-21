/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;

import Busquedas.ventanaBusquedaPaciente;
import Calendario.Agenda;
import Control.ControlCitas;
import Utilidades.Utilidades;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
//import sun.swing.table.DefaultTableCellHeaderRenderer;    

/**
 *
 * @author richard
 */
public class VentanaCitas extends javax.swing.JFrame {

    /**
     * Creates new form VentanaCitas
     */
    public String estadoch = "1";
    ControlCitas cita = new ControlCitas();
    public DefaultTableModel modelo;
    public VentanaPrincipal vprin;
    public String paciente;
    public String npaciente;  
    public String pkcita; 
    public VentanaConfirmacion vconf;
    public int banBQDPac = 0;
    private int x, y;
    
    public VentanaCitas(VentanaPrincipal vprin) {
        initComponents();
        Utilidades.EstablecerIcono(this);
        lblIdentificacion.setVisible(false);
        modelo = new DefaultTableModel();  
        this.vprin = vprin;
        this.setLocationRelativeTo(null);
        lblAgregar.setEnabled(false);
        //String[] Encabezado = {"Cita", "Fecha", "Hora", "Motivo", "Estado", "Acción"};
        String[] Encabezado = {"Cita".toUpperCase(), "Fecha".toUpperCase(), "Hora".toUpperCase(), "Motivo".toUpperCase(), "Estado".toUpperCase(), "Acción".toUpperCase()};
        modelo = new DefaultTableModel(Encabezado, 0);
        tblListaCitas.setModel(modelo);
        
        DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
        tcr.setHorizontalAlignment(SwingConstants.CENTER);

//        DefaultTableCellHeaderRenderer thcr = new DefaultTableCellHeaderRenderer();
//        tcr.setHorizontalAlignment(SwingConstants.CENTER);
        tblListaCitas.getColumnModel().getColumn(0).setPreferredWidth(20);
        for(int i = 0; i < modelo.getColumnCount(); i++){                
            tblListaCitas.getColumnModel().getColumn(i).setResizable(false);
            tblListaCitas.getColumnModel().getColumn(i).setCellRenderer(tcr);
//            tblListaCitas.getColumnModel().getColumn(i).setHeaderRenderer(thcr);
        }
        JTableHeader header = tblListaCitas.getTableHeader();
        ((DefaultTableCellRenderer)header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
        ((DefaultTableCellRenderer)header.getDefaultRenderer()).setFont(new Font("Tahoma", 1, 13));
        ((DefaultTableCellRenderer)header.getDefaultRenderer()).setForeground(new Color(21, 67, 96));
        
        
    }
    
    public VentanaCitas(String paciente, String npaciente, String estadoch, String pkcita, VentanaPrincipal vprin) {
        initComponents(); 
        Utilidades.EstablecerIcono(this);
        lblIdentificacion.setVisible(false);
        modelo = new DefaultTableModel();  
        this.paciente = paciente;
        this.npaciente = npaciente;
        this.estadoch = estadoch;  
        this.pkcita = pkcita; 
        this.vprin = vprin;
        this.setLocationRelativeTo(null);
        
        btnConsultarPaciente.setEnabled(false);
        
        lblAgregar.setEnabled(false);
        String[] Encabezado = {"Cita".toUpperCase(), "Fecha".toUpperCase(), "Hora".toUpperCase(), "Motivo".toUpperCase(), "Estado".toUpperCase(), "Acción".toUpperCase()};
        modelo = new DefaultTableModel(Encabezado, 0);
        tblListaCitas.setModel(modelo);
        
        JTableHeader th = tblListaCitas.getTableHeader();
        th.setFont(new Font("Tahoma", 1, 13)); 
        
        
        DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
        
//        tcr.setFont(new Font("Tahoma", 1, 13));
//        tcr.setForeground(new Color(21, 67, 96));
        tcr.setHorizontalAlignment(SwingConstants.CENTER);
       
        

//        DefaultTableCellHeaderRenderer thcr = new DefaultTableCellHeaderRenderer();
//        tcr.setHorizontalAlignment(SwingConstants.CENTER);
        tblListaCitas.getColumnModel().getColumn(0).setPreferredWidth(20);
        for(int i = 0; i < modelo.getColumnCount(); i++){                
            tblListaCitas.getColumnModel().getColumn(i).setResizable(false);
            tblListaCitas.getColumnModel().getColumn(i).setCellRenderer(tcr);
//            tblListaCitas.getColumnModel().getColumn(i).setHeaderRenderer(thcr);
        }
        lblIdentificacion.setText(paciente);
        txtNombre.setText(npaciente);
        ConsultarCitas();
    }

    public VentanaCitas(String paciente, String npaciente, String estadoch, VentanaConfirmacion vconf, VentanaPrincipal vprin) {
        initComponents(); 
        Utilidades.EstablecerIcono(this);
        lblIdentificacion.setVisible(false);
        modelo = new DefaultTableModel();  
        this.paciente = paciente;
        this.npaciente = npaciente;
        this.estadoch = estadoch;  
        this.vprin = vprin;
        this.vconf = vconf;
        this.setLocationRelativeTo(null);
        System.out.println("pac-->"+paciente+"\n npac-->"+npaciente+"\nesta-->"+estadoch);
        
        lblAgregar.setEnabled(false);
        String[] Encabezado = {"Cita", "Fecha", "Hora", "Motivo", "Estado", "Acción"};
        modelo = new DefaultTableModel(Encabezado, 0);
        tblListaCitas.setModel(modelo);
        
        DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
        tcr.setHorizontalAlignment(SwingConstants.CENTER);

//        DefaultTableCellHeaderRenderer thcr = new DefaultTableCellHeaderRenderer();
//        tcr.setHorizontalAlignment(SwingConstants.CENTER);
        tblListaCitas.getColumnModel().getColumn(0).setPreferredWidth(20);
        for(int i = 0; i < modelo.getColumnCount(); i++){                
            tblListaCitas.getColumnModel().getColumn(i).setResizable(false);
            tblListaCitas.getColumnModel().getColumn(i).setCellRenderer(tcr);
//            tblListaCitas.getColumnModel().getColumn(i).setHeaderRenderer(thcr);
        }
        lblIdentificacion.setText(paciente);
        txtNombre.setText(npaciente);
        ConsultarCitas();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jProgressBar1 = new javax.swing.JProgressBar();
        jMonthChooser1 = new com.toedter.calendar.JMonthChooser();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblListaCitas = new Tabla.CitaTable(){
            @Override
            public boolean isCellEditable(int rowindex, int colindex){
                return false;
            }

        };//new javax.swing.JTable();
        lblAgregar = new javax.swing.JLabel();
        lblIdentificacion = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        btnConsultarPaciente = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Agenda ");
        setUndecorated(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 82, 118)));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(21, 67, 96));
        jLabel1.setText("Paciente");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, -1, -1));

        txtNombre.setEditable(false);
        txtNombre.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtNombre.setForeground(new java.awt.Color(21, 67, 96));
        txtNombre.setBorder(null);
        jPanel1.add(txtNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 360, 30));

        tblListaCitas.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        tblListaCitas.setForeground(new java.awt.Color(21, 67, 96));
        tblListaCitas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblListaCitas.setGridColor(new java.awt.Color(255, 255, 255));
        tblListaCitas.setSelectionBackground(new java.awt.Color(41, 128, 185));
        tblListaCitas.getTableHeader().setReorderingAllowed(false);
        tblListaCitas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblListaCitasMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblListaCitas);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, 549, 363));

        lblAgregar.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblAgregar.setForeground(new java.awt.Color(21, 67, 96));
        lblAgregar.setText("+");
        lblAgregar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblAgregarMouseClicked(evt);
            }
        });
        jPanel1.add(lblAgregar, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 108, -1, 30));
        jPanel1.add(lblIdentificacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(445, 86, -1, -1));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(21, 67, 96));
        jLabel3.setText("Historico de Citas");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 120, -1, -1));

        btnConsultarPaciente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/lupa_1.png"))); // NOI18N
        btnConsultarPaciente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnConsultarPacienteMouseClicked(evt);
            }
        });
        jPanel1.add(btnConsultarPaciente, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 60, 30, 30));

        jPanel2.setBackground(new java.awt.Color(26, 82, 118));
        jPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel2MousePressed(evt);
            }
        });
        jPanel2.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jPanel2MouseDragged(evt);
            }
        });
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/minimizar.png"))); // NOI18N
        jLabel6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel6MouseClicked(evt);
            }
        });
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 0, 30, 30));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/cerrar_1.png"))); // NOI18N
        jLabel5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5MouseClicked(evt);
            }
        });
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 0, 30, 30));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Agenda");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 8, 152, -1));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 570, 30));
        jPanel1.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 360, 10));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 570, 520));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void LimpiarVentana(){
        lblIdentificacion.setText("");
        txtNombre.setText("");
        String[] Encabezado = {"Cita", "Fecha", "Hora", "Motivo", "Estado", "Acción"};
        modelo = new DefaultTableModel(Encabezado, 0);
        tblListaCitas.setModel(modelo);
        
        DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
        tcr.setHorizontalAlignment(SwingConstants.CENTER);

//        DefaultTableCellHeaderRenderer thcr = new DefaultTableCellHeaderRenderer();
//        tcr.setHorizontalAlignment(SwingConstants.CENTER);


        tblListaCitas.getColumnModel().getColumn(0).setPreferredWidth(20);
        for(int i = 0; i < modelo.getColumnCount(); i++){                
            tblListaCitas.getColumnModel().getColumn(i).setResizable(false);
            tblListaCitas.getColumnModel().getColumn(i).setCellRenderer(tcr);
//            tblListaCitas.getColumnModel().getColumn(i).setHeaderRenderer(thcr);
        }
    }
    private void lblAgregarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblAgregarMouseClicked
        String idPaciente = lblIdentificacion.getText();
        System.out.println("idpaciente--->"+idPaciente+"  "+txtNombre.getText());
        System.out.println("pkcita---"+pkcita);
        if(!idPaciente.equals(""))
            new ventanaDiasxHoras(idPaciente, txtNombre.getText(), pkcita, this).setVisible(true);
//        new VentanaNuevaCita(this).setVisible(true);
    }//GEN-LAST:event_lblAgregarMouseClicked

    private void tblListaCitasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblListaCitasMouseClicked
        int fila = tblListaCitas.getSelectedRow();
        int cola = tblListaCitas.getSelectedColumn();
        String idPaciente = lblIdentificacion.getText();
        if(cola == 5){
             String dato =""+tblListaCitas.getValueAt(fila, cola);
              String pkcita =""+tblListaCitas.getValueAt(fila, 0);
              System.out.println("dato-->"+dato);
             if(dato.equals("Cancelar")){                
                 boolean can = cita.CancelarCita(idPaciente, pkcita, "");                 
                 if(can){
                     ConsultarCitas();
                 }else{
                     JOptionPane.showMessageDialog(null, "Ocurrio un Error al momento de realizar la operación.");
                 }
             }
             else if(dato.equals("Reactivar")){
                 boolean rea = cita.ReactivarCita(idPaciente, pkcita, "");    
                 if(rea){
                     ConsultarCitas();
                 }else{
                     JOptionPane.showMessageDialog(null, "Ocurrio un Error al momento de realizar la operación.");
                 }
             }
        }
    }//GEN-LAST:event_tblListaCitasMouseClicked

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if(vconf!=null)
            vconf.b = 0;
    }//GEN-LAST:event_formWindowClosing

    private void btnConsultarPacienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConsultarPacienteMouseClicked
        if(btnConsultarPaciente.isEnabled()){
            LimpiarVentana();
            String id = lblIdentificacion.getText();
            if(id.equals("") && banBQDPac == 0)
                new ventanaBusquedaPaciente(1, "IDENTIFICACION:-:NOMBRE", estadoch, this);
        }
    }//GEN-LAST:event_btnConsultarPacienteMouseClicked

    private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseClicked
        this.setState(VentanaCitas.ICONIFIED);
    }//GEN-LAST:event_jLabel6MouseClicked

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
        this.dispose();
    }//GEN-LAST:event_jLabel5MouseClicked

    private void jPanel2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MousePressed
        x = evt.getX();
        y = evt.getY();
    }//GEN-LAST:event_jPanel2MousePressed

    private void jPanel2MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MouseDragged
        this.setLocation(this.getLocation().x + evt.getX()-x, this.getLocation().y + evt.getY()-y);
    }//GEN-LAST:event_jPanel2MouseDragged

    
    public void ConsultarCitas(){
        try{
            ArrayList<String[]> Lista = cita.getCitasPaciente(lblIdentificacion.getText(), estadoch);
            //0--> cita,1-> fecha, 2 horas, 3 motivo, 4 estado, 5 accion
            ArrayList<String[]> Listar = AcomodarLista(Lista);
            
            //String[] Encabezado = {"Cita", "Fecha", "Hora", "Motivo", "Estado", "Acción"};
            String[] Encabezado = {"Cita".toUpperCase(), "Fecha".toUpperCase(), "Hora".toUpperCase(), "Motivo".toUpperCase(), "Estado".toUpperCase(), "Acción".toUpperCase()};
            cargarDatosTabla(Encabezado, Listar);     
            lblAgregar.setEnabled(true);
        }catch(Exception e){
            System.out.println("ERROR Consultas Citaas -->"+e.toString());
        }
    }
    
    private void cargarDatosTabla(String[] nombreColumnas, ArrayList<String[]> datos) {
        modelo = new DefaultTableModel(nombreColumnas, 0);
                
        if (datos != null) {
            for (int i = 0; i < datos.size(); i++) {
                agregarFila(datos.get(i));
            }
        }
        tblListaCitas.setModel(modelo);
        
        DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
        tcr.setHorizontalAlignment(SwingConstants.CENTER);
        

//        DefaultTableCellHeaderRenderer thcr = new DefaultTableCellHeaderRenderer();
//        tcr.setHorizontalAlignment(SwingConstants.CENTER);


        tblListaCitas.getColumnModel().getColumn(0).setPreferredWidth(20);
        for(int i = 0; i < modelo.getColumnCount(); i++){                
            tblListaCitas.getColumnModel().getColumn(i).setResizable(false);
            tblListaCitas.getColumnModel().getColumn(i).setCellRenderer(tcr);
//            tblListaCitas.getCoilumnModel().getColumn(i).setHeaderRenderer(thcr);
        }
    }

    private void agregarFila(String[] fila) {
        modelo.addRow(fila);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VentanaCitas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaCitas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaCitas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaCitas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {  
            public void run() {
                new VentanaCitas(null).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel btnConsultarPaciente;
    public javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private com.toedter.calendar.JMonthChooser jMonthChooser1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    public javax.swing.JLabel lblAgregar;
    public javax.swing.JLabel lblIdentificacion;
    public javax.swing.JTable tblListaCitas;
    public javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables

    private ArrayList<String[]> AcomodarLista(ArrayList<String[]> Lista) { 
        try{
            ArrayList<String[]> lis = new ArrayList<>();
            String horas = "", hora = "";
            
            for(int i = 0; i < Lista.size(); i++){
                hora = "";
                horas = Lista.get(i)[2];    
                String[] hrs = horas.split("<>");
                for(int j = 0; j < hrs.length;j++){
                    hora += (hora.equals("")?"":", ")+getHora(hrs[j]);
                }
                lis.add(new String[]{Lista.get(i)[0], Lista.get(i)[1], hora, Lista.get(i)[3], Lista.get(i)[4], Lista.get(i)[5]});
            }
            return lis;
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("");
            return null;
        }
    }
    
    public String getHora(String hora){
        try{
            String[] dat = hora.split(":");
            int h = Integer.parseInt(dat[0]);
            int  b= 0;
            
            if(h >= 12){
                b = 1;
                if(h>12)
                    h = h-12;
            }
            
            return h+":"+dat[1]+" "+(b==0?"a.":"p.")+" m.";
            
        }catch(Exception e){
            System.out.println("ERROR--getHora--"+e.toString());
            return "";
        }
    }

   
}
