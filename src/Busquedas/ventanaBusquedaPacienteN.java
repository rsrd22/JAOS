/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Busquedas;

import BaseDeDatos.gestorMySQL;
import Control.ControlPersonas;
import Modelo.Persona;
import Utilidades.Expresiones;
import Utilidades.Utilidades;
import Vistas.VentanaCitas;
import Vistas.VistaPaciente;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 *
 * @author User
 */
public class ventanaBusquedaPacienteN extends javax.swing.JFrame {

    private gestorMySQL gsql;
    public DefaultTableModel modelo;
    public ControlPersonas cper;
    public int opc;
    public String[] NameColumnas;
    public String DatosEntrada;
    public String DatosSalida;
    public String estadoch;
    public String SQL = "";
    public ResultSet rs = null;
    public int ini = 0;
    public int fin = 0;
    public int tot = 0;
    public int ban = 0;
    public int registrosIniciales, registrosTotales, registrosInicialPag, registrosFinalPag;
    public VistaPaciente vp;
    private int x, y;
    public int bn = 0;
    List<Map<String, String>> list_consul;

    /**
     * Creates new form ventanaBusqueda
     */
    public ventanaBusquedaPacienteN() {

    }

    public ventanaBusquedaPacienteN(int opc, String DatosSalida, String estadoch, VistaPaciente vp) {
        initComponents();
        Utilidades.EstablecerIcono(this);
        gsql = new gestorMySQL();
        this.opc = opc;
        this.DatosSalida = DatosSalida;
        this.estadoch = estadoch;
        modelo = new DefaultTableModel();
        this.setLocationRelativeTo(null);

        LlenarListaDatos();
        GenerarTabla();
        if (bn == 1) {
            return;
        }
        this.vp = vp;

        tblDatos.setModel(modelo);

        tblDatos.getColumnModel().getColumn(0).setPreferredWidth(3);
        tblDatos.getColumnModel().getColumn(1).setPreferredWidth(40);
        tblDatos.getColumnModel().getColumn(2).setPreferredWidth(100);
        tblDatos.getColumnModel().getColumn(3).setPreferredWidth(100);
        tblDatos.getColumnModel().getColumn(4).setPreferredWidth(40);

        JTableHeader header = tblDatos.getTableHeader();
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

    }

    private void LlenarListaDatos() {

        SQL = "SELECT  per.pfk_tipo_documento TID, per.pk_persona ID, \n"
                + "CONCAT_WS(' ', per.primer_apellido, IFNULL(per.segundo_apellido, ''), per.primer_nombre, IFNULL(per.segundo_nombre, '')) NOMBRE, \n"
                + "IFNULL(per.correo_electronico, '') EMAIL, REPLACE(GROUP_CONCAT(IFNULL(tel.numero, '')), ',', '-') TELEFONO \n"
                + "FROM `pacientes` pac\n"
                + "INNER JOIN `personas` per ON CONCAT(per.pfk_tipo_documento, per.pk_persona) = pac.pfk_paciente\n"
                + "LEFT JOIN `personas_telefonos` tel ON tel.pfk_persona=CONCAT(per.pk_persona) \n"
                + "WHERE pac.`estado` = 'Activo' \n"
                + "GROUP BY pac.pfk_paciente \n"
                + "UNION\n"
                + "SELECT  'aux' TID, CONCAT('',pk_paciente_auxiliar) ID, \n"
                + "CONCAT_WS(' ', pa.primer_apellido, IFNULL(pa.segundo_apellido, ''), pa.primer_nombre, IFNULL(pa.segundo_nombre, '')) NOMBRE, \n"
                + "IFNULL(pa.email, '') EMAIL, REPLACE(IFNULL(pa.telefono, ''), '<>', '-') TELEFONO \n"
                + "FROM paciente_auxiliar pa\n"
                + "WHERE pa.`estado` IN('Activo')\n"
                + "AND pa.tipo IN('A')";

//        SQL = "SELECT  per.pfk_tipo_documento TID, per.pk_persona ID, \n"
//                + "CONCAT_WS(' ', per.primer_apellido, IFNULL(per.segundo_apellido, ''), per.primer_nombre, IFNULL(per.segundo_nombre, '')) NOMBRE, \n"
//                + "IFNULL(per.correo_electronico, '') EMAIL, REPLACE(GROUP_CONCAT(IFNULL(tel.numero, '')), ',', '-') TELEFONO \n"
//                + "FROM `pacientes` pac\n"
//                + "INNER JOIN `personas` per ON CONCAT(per.pfk_tipo_documento, per.pk_persona) = pac.pfk_paciente\n"
//                + "LEFT JOIN `personas_telefonos` tel ON tel.pfk_persona=CONCAT(per.pk_persona) \n"
//                + "WHERE pac.`estado` = 'Activo' \n"
//                + "GROUP BY pac.pfk_paciente \n";
        list_consul = gsql.ListSQL(SQL);
        if (list_consul.size() > 0) {
            Iterator it = list_consul.get(0).keySet().iterator();
            String col = "";
            while (it.hasNext()) {
                String key = (String) it.next();
                col += (col.equals("") ? "" : "<::>") + key;
            }
            System.out.println("col--" + col);
            String[] Columnas = col.split("<::>");
            //
            NameColumnas = Columnas;
        } else {
            //CERRAR VENTAANA
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtFiltro = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        btnRegistroInicial = new javax.swing.JButton();
        btnRegistroAnterior = new javax.swing.JButton();
        btnRegistroSiguiente = new javax.swing.JButton();
        btnRegistroFinal = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDatos = new javax.swing.JTable();
        lblIni = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblFin = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel3 = new javax.swing.JPanel();
        btnSeleccionar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Busqueda Pacientes");
        setBackground(new java.awt.Color(255, 255, 255));
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 82, 118)));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(21, 67, 96));
        jLabel1.setText("Filtro");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, -1, -1));

        txtFiltro.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtFiltro.setForeground(new java.awt.Color(31, 97, 141));
        txtFiltro.setBorder(null);
        txtFiltro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtFiltroKeyPressed(evt);
            }
        });
        jPanel1.add(txtFiltro, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 290, 30));

        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/lupa_1.png"))); // NOI18N
        btnBuscar.setBorderPainted(false);
        btnBuscar.setContentAreaFilled(false);
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });
        jPanel1.add(btnBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 70, 30, 30));

        btnRegistroInicial.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/inicio_1.png"))); // NOI18N
        btnRegistroInicial.setBorderPainted(false);
        btnRegistroInicial.setContentAreaFilled(false);
        btnRegistroInicial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistroInicialActionPerformed(evt);
            }
        });
        jPanel1.add(btnRegistroInicial, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 30, 30));

        btnRegistroAnterior.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/izq_1.png"))); // NOI18N
        btnRegistroAnterior.setBorderPainted(false);
        btnRegistroAnterior.setContentAreaFilled(false);
        btnRegistroAnterior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistroAnteriorActionPerformed(evt);
            }
        });
        jPanel1.add(btnRegistroAnterior, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 120, 30, 30));

        btnRegistroSiguiente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/der_1.png"))); // NOI18N
        btnRegistroSiguiente.setBorderPainted(false);
        btnRegistroSiguiente.setContentAreaFilled(false);
        btnRegistroSiguiente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRegistroSiguienteMouseClicked(evt);
            }
        });
        btnRegistroSiguiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistroSiguienteActionPerformed(evt);
            }
        });
        btnRegistroSiguiente.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                btnRegistroSiguienteAncestorAdded(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        jPanel1.add(btnRegistroSiguiente, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 120, 30, 30));

        btnRegistroFinal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/fin_1.png"))); // NOI18N
        btnRegistroFinal.setBorderPainted(false);
        btnRegistroFinal.setContentAreaFilled(false);
        btnRegistroFinal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistroFinalActionPerformed(evt);
            }
        });
        jPanel1.add(btnRegistroFinal, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 120, 30, 30));

        tblDatos.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tblDatos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblDatos.setGridColor(new java.awt.Color(255, 255, 255));
        tblDatos.setSelectionBackground(new java.awt.Color(41, 128, 185));
        tblDatos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblDatosMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblDatos);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 159, 550, 240));

        lblIni.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblIni.setForeground(new java.awt.Color(21, 67, 96));
        lblIni.setText("1");
        jPanel1.add(lblIni, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 130, 30, -1));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(21, 67, 96));
        jLabel2.setText("a");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 130, -1, -1));

        lblFin.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblFin.setForeground(new java.awt.Color(21, 67, 96));
        lblFin.setText("10");
        jPanel1.add(lblFin, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 130, 40, -1));

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(21, 67, 96));
        jLabel3.setText("de");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 130, -1, -1));

        lblTotal.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblTotal.setForeground(new java.awt.Color(21, 67, 96));
        lblTotal.setText("10");
        jPanel1.add(lblTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 130, 40, -1));

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
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 0, 30, 30));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/cerrar_1.png"))); // NOI18N
        jLabel5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5MouseClicked(evt);
            }
        });
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 0, 30, 30));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Consulta de Pacientes");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 220, -1));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 590, 31));
        jPanel1.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 290, 10));

        jPanel3.setBackground(new java.awt.Color(26, 82, 118));
        jPanel3.setForeground(new java.awt.Color(26, 82, 118));

        btnSeleccionar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSeleccionar.setForeground(new java.awt.Color(255, 255, 255));
        btnSeleccionar.setText("Seleccionar");
        btnSeleccionar.setBorderPainted(false);
        btnSeleccionar.setContentAreaFilled(false);
        btnSeleccionar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnSeleccionar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSeleccionarMouseClicked(evt);
            }
        });
        btnSeleccionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSeleccionarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnSeleccionar, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnSeleccionar, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 110, 140, 40));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 590, 440));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        this.ini = 0;
        GenerarTabla();
    }//GEN-LAST:event_btnBuscarActionPerformed

    public void SeleccionarFila() {
        int filaSeleccionada;
        ArrayList<String> fila = new ArrayList<>();
        try {
            filaSeleccionada = tblDatos.getSelectedRow();

            if (DatosSalida != null) {
                String[] Salida = DatosSalida.split(":-:");
                for (int i = 0; i < Salida.length; i++) {
                    for (int j = 0; j < NameColumnas.length; j++) {
                        if (Salida[i].equals(NameColumnas[j])) {
                            Salida[i] = j + "::" + Salida[i];
                            break;
                        }
                    }
                }
                if (filaSeleccionada > -1) {
                    DefaultTableModel modAux = (DefaultTableModel) tblDatos.getModel();
                    for (int j = 0; j < Salida.length; j++) {
                        fila.add((String) modAux.getValueAt(filaSeleccionada, Integer.parseInt(Salida[j].split("::")[0])));
                    }

                    if (opc == 1) {////////////CITAS
                        vp.tipoId = fila.get(0);
                        if (!vp.tipoId.equalsIgnoreCase("aux")) {
                            vp.txtId.setText(fila.get(1));
                            vp.ConsultarPaciente(0);
                        } else {
                            vp.idPacAux = fila.get(1);
                            vp.ConsultarPacienteAuxiliar();
                        }
                        this.dispose();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No ha seleccionado ninguna fila");
                }
            }
        } catch (HeadlessException e) {

        }
    }

    private void btnSeleccionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSeleccionarActionPerformed
        SeleccionarFila();
    }//GEN-LAST:event_btnSeleccionarActionPerformed

    private void btnSeleccionarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSeleccionarMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSeleccionarMouseClicked

    private void btnRegistroSiguienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRegistroSiguienteMouseClicked
        // TODO add your handling code here:


    }//GEN-LAST:event_btnRegistroSiguienteMouseClicked

    private void btnRegistroAnteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistroAnteriorActionPerformed
        int ini = Integer.parseInt(lblIni.getText());
        if (ini - 100 >= 0) {
            ini -= 100;
            this.ini = ini;
            GenerarTabla();
        }
    }//GEN-LAST:event_btnRegistroAnteriorActionPerformed

    private void btnRegistroInicialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistroInicialActionPerformed
        this.ini = 0;
        GenerarTabla();
    }//GEN-LAST:event_btnRegistroInicialActionPerformed

    private void btnRegistroFinalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistroFinalActionPerformed
        this.ini = this.tot - (tot % 100) + 1;

        GenerarTabla();
    }//GEN-LAST:event_btnRegistroFinalActionPerformed

    private void btnRegistroSiguienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistroSiguienteActionPerformed
        int ini = Integer.parseInt(lblIni.getText());
        if (ini + 100 < tot) {
            ini += 100;
            this.ini = ini;
            GenerarTabla();
        }
    }//GEN-LAST:event_btnRegistroSiguienteActionPerformed

    private void btnRegistroSiguienteAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_btnRegistroSiguienteAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_btnRegistroSiguienteAncestorAdded

    private void txtFiltroKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFiltroKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            this.ini = 0;
            GenerarTabla();
        }
    }//GEN-LAST:event_txtFiltroKeyPressed

    private void tblDatosMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDatosMousePressed
        if (evt.getClickCount() == 2) {
            SeleccionarFila();
        }
    }//GEN-LAST:event_tblDatosMousePressed

    private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseClicked
        this.setState(ventanaBusquedaPacAuxs.ICONIFIED);
    }//GEN-LAST:event_jLabel6MouseClicked

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
        this.dispose();
    }//GEN-LAST:event_jLabel5MouseClicked

    private void jPanel2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MousePressed
        x = evt.getX();
        y = evt.getY();
    }//GEN-LAST:event_jPanel2MousePressed

    private void jPanel2MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MouseDragged
        this.setLocation(this.getLocation().x + evt.getX() - x, this.getLocation().y + evt.getY() - y);
    }//GEN-LAST:event_jPanel2MouseDragged

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
            java.util.logging.Logger.getLogger(ventanaBusquedaPacienteN.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ventanaBusquedaPacienteN.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ventanaBusquedaPacienteN.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ventanaBusquedaPacienteN.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ventanaBusquedaPacienteN();
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnRegistroAnterior;
    private javax.swing.JButton btnRegistroFinal;
    private javax.swing.JButton btnRegistroInicial;
    private javax.swing.JButton btnRegistroSiguiente;
    private javax.swing.JButton btnSeleccionar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblFin;
    private javax.swing.JLabel lblIni;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JTable tblDatos;
    private javax.swing.JTextField txtFiltro;
    // End of variables declaration//GEN-END:variables

    private void cargarDatosTabla(String[] nombreColumnas, ArrayList<String[]> datos) {
        modelo = new DefaultTableModel(nombreColumnas, 0) {
            boolean[] edit = {false, false, false, false, false};

            public boolean isCellEditable(int row, int col) {
                //System.out.println("row-->"+row+" -- col-->"+col+"::::: "+edit[col]);

                return edit[col];
            }
        };

        if (datos != null) {
            for (int i = 0; i < datos.size(); i++) {
                agregarFila(datos.get(i));
            }
        }
        JTableHeader header = tblDatos.getTableHeader();
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
        tblDatos.setModel(modelo);
    }

    private void agregarFila(String[] fila) {
        modelo.addRow(fila);
    }

    private void GenerarTabla() {
        try {
            ArrayList<String[]> datos = new ArrayList<>();
            String filtro = txtFiltro.getText();
            List<Map<String, String>> list_consul_aux = new ArrayList<>();

            list_consul_aux = getFiltroLista(filtro);

            //
            if (list_consul_aux.size() > 0) {
                if (ban == 0) {
                    ban = 1;
                    EstadoVentana();
                }

                tot = list_consul_aux.size();

                if (tot < 100) {
                    EstadoBotonesSig(false);
                }

                fin = (tot < 100 ? tot : (fin + 100 > tot ? fin + (tot - fin) : fin + 100));
                lblIni.setText("" + ini);
                lblFin.setText("" + fin);
                lblTotal.setText("" + tot);

                for (int i = ini; i < fin; i++) {
                    String[] ColDatos = new String[NameColumnas.length];
                    for (int j = 0; j < ColDatos.length; j++) {
                        ColDatos[j] = list_consul_aux.get(i).get(NameColumnas[j]);
                    }
                    datos.add(ColDatos);
                }

                cargarDatosTabla(NameColumnas, datos);

                tblDatos.setModel(modelo);

                tblDatos.getColumnModel().getColumn(0).setPreferredWidth(3);
                tblDatos.getColumnModel().getColumn(1).setPreferredWidth(40);
                tblDatos.getColumnModel().getColumn(2).setPreferredWidth(100);
                tblDatos.getColumnModel().getColumn(3).setPreferredWidth(100);
                tblDatos.getColumnModel().getColumn(4).setPreferredWidth(40);

                JTableHeader header = tblDatos.getTableHeader();
                ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

            } else {
                JOptionPane.showMessageDialog(null, "No se encontraron datos");

            }

        } catch (Exception w) {
            w.printStackTrace();
        }
    }

    private void GenerarTablaOLD() {
        try {
            ArrayList<String[]> datos = new ArrayList<>();
            String filtro = Utilidades.CodificarElemento(txtFiltro.getText());

            String add = "WHERE CONCAT_WS(' ',LOWER(TID),LOWER(ID), LOWER(NOMBRE), LOWER(EMAIL), LOWER(TELEFONO)) LIKE '%" + filtro + "%' ";

            SQL = "SELECT  per.pfk_tipo_documento TID, per.pk_persona ID, \n"
                    + "CONCAT_WS(' ', per.primer_apellido, IFNULL(per.segundo_apellido, ''), per.primer_nombre, IFNULL(per.segundo_nombre, '')) NOMBRE, \n"
                    + "IFNULL(per.correo_electronico, '') EMAIL, REPLACE(GROUP_CONCAT(IFNULL(tel.numero, '')), ',', '-') TELEFONO \n"
                    + "FROM `pacientes` pac\n"
                    + "INNER JOIN `personas` per ON CONCAT(per.pfk_tipo_documento, per.pk_persona) = pac.pfk_paciente\n"
                    + "LEFT JOIN `personas_telefonos` tel ON tel.pfk_persona=CONCAT(per.pk_persona) \n"
                    + "WHERE pac.`estado` = 'Activo' \nGROUP BY pac.pfk_paciente \nORDER BY NOMBRE ASC";

            if (!SQL.equals("")) {

                String SQLTot = "SELECT COUNT(*) FROM (" + SQL + ") TABLA";
                int tot = 0, fil = 0;
                rs = gsql.Consultar(SQLTot);
                if (rs.next()) {
                    tot = Integer.parseInt(rs.getString(1));
                }
                if (tot < 100) {
                    EstadoBotonesSig(false);
                }
                this.tot = tot;
                SQL = "SELECT * FROM (" + SQL + ") TABLA " + add + " LIMIT " + ini + ", 100";
                rs = gsql.Consultar(SQL);
                fin = (tot < 100 ? tot : (fin + 100 > tot ? fin + (tot - fin) : fin + 100));
                lblIni.setText("" + ini);
                lblFin.setText("" + fin);
                lblTotal.setText("" + tot);
                if (rs.next()) {
                    if (ban == 0) {
                        ban = 1;
                        EstadoVentana();
                    }
                    ResultSetMetaData rsmd = (ResultSetMetaData) rs.getMetaData();
                    String[] Columnas = new String[rsmd.getColumnCount()];

                    for (int j = 1; j <= rsmd.getColumnCount(); j++) {
                        Columnas[j - 1] = rsmd.getColumnName(j);
                    }
                    NameColumnas = Columnas;
                    do {
                        tot++;
                        String[] ColDatos = new String[rsmd.getColumnCount()];
                        for (int i = 0; i < ColDatos.length; i++) {
                            ColDatos[i] = Utilidades.decodificarElemento(rs.getString(i + 1));
                        }
                        datos.add(ColDatos);

                    } while (rs.next());

                    cargarDatosTabla(Columnas, datos);

                } else {
                    JOptionPane.showMessageDialog(null, "No se encontraron datos");
                    //this.dispose();
                    bn = 1;
                }
            }
        } catch (SQLException w) {
            JOptionPane.showMessageDialog(null, "Se encontro un error con la consulta " + w.getMessage());
            bn = 1;

        }
    }

    public void EstadoVentana() {
        if (ban == 1) {
            this.setVisible(true);

        }
    }

    private void EstadoBotonesSig(boolean b) {
        btnRegistroAnterior.setEnabled(b);
        btnRegistroFinal.setEnabled(b);
        btnRegistroInicial.setEnabled(b);
        btnRegistroSiguiente.setEnabled(b);
    }

    private List<Map<String, String>> getFiltroLista(String filtro) {
        List<Map<String, String>> retorno = new ArrayList<>();
        System.out.println("***************getFiltroLista*****************" + filtro);
        int b = -1, c = 0;
        String[] filtros = filtro.isEmpty() ? null : filtro.replace(" ", "<::>").split("<::>");
        String valores = "";
        for (int i = 0; i < list_consul.size(); i++) {
            b = 1;
            if (filtro.isEmpty()) {
                retorno.add(list_consul.get(i));
            } else {
                valores = "";
                for (int j = 0; j < NameColumnas.length; j++) {
                    System.out.println("NAme-" + j + "->" + NameColumnas[j]);
                    String value = list_consul.get(i).get(NameColumnas[j]);
                    valores += "" + value;
                    System.out.println("value--->" + value);
//                    for(int k = 0; k < filtros.length; k++){
//                        int con  = value.toUpperCase().indexOf(filtros[k].toUpperCase());
//                        if(con < 0){
//                            c = 1; 
//                            break;
//                        }
//                    }
//                    
//                    if(c == 0){
//                        b = 0; 
//                        break;
//                    }
                }
                boolean encontro = Expresiones.filtrobusqueda(filtros, valores);
                System.out.println("i-" + i + "-b-" + b);
                if (encontro) {
                    retorno.add(list_consul.get(i));
                }
            }
        }
        System.out.println("********************retorno --> " + retorno.size() + "***********************");
        return retorno;
    }

    private List<Map<String, String>> getFiltroListaold(String filtro) {
        List<Map<String, String>> retorno = new ArrayList<>();
        System.out.println("***************getFiltroLista*****************" + filtro);
        int b = -1;
        for (int i = 0; i < list_consul.size(); i++) {
            b = 1;
            if (filtro.isEmpty()) {
                retorno.add(list_consul.get(i));
            } else {
                for (int j = 0; j < NameColumnas.length; j++) {
                    System.out.println("NAme-" + j + "->" + NameColumnas[j]);
                    String value = list_consul.get(i).get(NameColumnas[j]);
                    System.out.println("value--->" + value);
                    int con = value.toUpperCase().indexOf(filtro.toUpperCase());
                    System.out.println("con--->" + con);
                    if (con >= 0) {
                        b = 0;
                        break;
                    }
                }
                System.out.println("i-" + i + "-b-" + b);
                if (b == 0) {
                    retorno.add(list_consul.get(i));
                }
            }
        }
        System.out.println("********************retorno --> " + retorno.size() + "***********************");
        return retorno;
    }
}
