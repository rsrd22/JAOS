package Busquedas;

import BaseDeDatos.gestorMySQL;
import HISTORIA_CLINICA.ventana;
import Utilidades.Expresiones;
import Utilidades.Utilidades;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class ventanaBusquedapacienteGralrsrd extends javax.swing.JFrame {

    private gestorMySQL gsql;
    public DefaultTableModel modelo;
    public String[] NameColumnas;
    public String DatosEntrada;
    public String estado;
    public String SQL = "";
    public ResultSet rs = null;
    public int ini = 0;
    public int fin = 0;
    public int tot = 0;
//    public int registrosIniciales, registrosTotales, registrosInicialPag, registrosFinalPag;
    public ventana venHC;
    private int x, y;
    List<Map<String, String>> list_consul;
    public int ban = 0;

    public ventanaBusquedapacienteGralrsrd() {
        initComponents();
        Utilidades.EstablecerIcono(this);
        modelo = new DefaultTableModel();
        this.setLocationRelativeTo(null);

        String[] nombreColumnas = new String[8];
        nombreColumnas = new String[]{"Nombre", "Dirección", "Fecha de Nacimiento", "Sexo", "E-mail"};
        int filas = 0;
        LlenarListaDatos();
        GenerarTabla();
        venHC.bandbp = 1;

    }

    public ventanaBusquedapacienteGralrsrd(ventana venRef, String estado) {
        initComponents();
        Utilidades.EstablecerIcono(this);
        gsql = new gestorMySQL();
        this.estado = estado;
        modelo = new DefaultTableModel();
        this.setLocationRelativeTo(null);
        LlenarListaDatos();
        GenerarTabla();
        //this.setVisible(band == 1);
        this.venHC = venRef;
        venHC.bandbp = 1;
    }

    private void LlenarListaDatos() {
        SQL = "SELECT CONCAT(per.pfk_tipo_documento, per.pk_persona) TID, per.primer_nombre PNOMBRE, IFNULL(per.segundo_nombre, '') SNOMBRE, \n"
                + "per.primer_apellido PAPELLIDO, IFNULL(per.segundo_apellido, '') SAPELLIDO,\n"
                + "'P' TIPO\n"
                + "FROM `pacientes` pac\n"
                + "INNER JOIN `personas` per ON CONCAT(per.pfk_tipo_documento, per.pk_persona) = pac.pfk_paciente\n"
                + "UNION\n"
                + "SELECT IFNULL(paux.pk_paciente_auxiliar, '') TID, paux.primer_nombre PNOMBRE, paux.segundo_nombre SNOMBRE,\n"
                + "paux.primer_apellido PAPELLIDO, paux.segundo_apellido SAPELLIDO, paux.tipo TIPO\n"
                + "FROM  `paciente_auxiliar` paux WHERE estado = 'ACTIVO'\n ORDER BY PAPELLIDO ASC, SAPELLIDO ASC, PNOMBRE ASC, SNOMBRE ASC";

        list_consul = gsql.ListSQL(SQL);
        if (list_consul.size() > 0) {
            Iterator it = list_consul.get(0).keySet().iterator();
            String col = "";
            while (it.hasNext()) {
                String key = (String) it.next();
                col += (col.equals("") ? "" : "<::>") + key;
            }
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
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDatos = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btnRegistroSiguiente = new javax.swing.JButton();
        lblFin = new javax.swing.JLabel();
        btnBuscar = new javax.swing.JButton();
        btnRegistroAnterior = new javax.swing.JButton();
        txtFiltro = new javax.swing.JTextField();
        btnRegistroFinal = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        btnRegistroInicial = new javax.swing.JButton();
        lblTotal = new javax.swing.JLabel();
        lblIni = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btnSeleccionar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Busqueda Pacientes");
        setBackground(new java.awt.Color(255, 255, 255));
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
        tblDatos.setGridColor(new java.awt.Color(21, 67, 96));
        tblDatos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblDatosMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblDatos);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 159, 501, 220));

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
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 0, 30, 30));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/cerrar_1.png"))); // NOI18N
        jLabel5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5MouseClicked(evt);
            }
        });
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 0, 30, 30));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Consulta de Pacientes");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 220, -1));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 540, 31));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(21, 67, 96));
        jLabel1.setText("Filtro");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, -1, -1));

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(21, 67, 96));
        jLabel3.setText("de");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 130, -1, -1));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(21, 67, 96));
        jLabel2.setText("a");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 130, -1, -1));

        btnRegistroSiguiente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/der_1.png"))); // NOI18N
        btnRegistroSiguiente.setBorderPainted(false);
        btnRegistroSiguiente.setContentAreaFilled(false);
        btnRegistroSiguiente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
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

        lblFin.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblFin.setForeground(new java.awt.Color(21, 67, 96));
        lblFin.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFin.setText("10");
        jPanel1.add(lblFin, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 130, 30, -1));

        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/lupa_1.png"))); // NOI18N
        btnBuscar.setBorderPainted(false);
        btnBuscar.setContentAreaFilled(false);
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });
        jPanel1.add(btnBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 70, 30, 30));

        btnRegistroAnterior.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/izq_1.png"))); // NOI18N
        btnRegistroAnterior.setBorderPainted(false);
        btnRegistroAnterior.setContentAreaFilled(false);
        btnRegistroAnterior.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRegistroAnterior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistroAnteriorActionPerformed(evt);
            }
        });
        jPanel1.add(btnRegistroAnterior, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 120, 30, 30));

        txtFiltro.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtFiltro.setForeground(new java.awt.Color(31, 97, 141));
        txtFiltro.setBorder(null);
        txtFiltro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtFiltroKeyPressed(evt);
            }
        });
        jPanel1.add(txtFiltro, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 250, 30));

        btnRegistroFinal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/fin_1.png"))); // NOI18N
        btnRegistroFinal.setBorderPainted(false);
        btnRegistroFinal.setContentAreaFilled(false);
        btnRegistroFinal.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRegistroFinal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistroFinalActionPerformed(evt);
            }
        });
        jPanel1.add(btnRegistroFinal, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 120, 30, 30));
        jPanel1.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 250, 10));

        btnRegistroInicial.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/inicio_1.png"))); // NOI18N
        btnRegistroInicial.setBorderPainted(false);
        btnRegistroInicial.setContentAreaFilled(false);
        btnRegistroInicial.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRegistroInicial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistroInicialActionPerformed(evt);
            }
        });
        jPanel1.add(btnRegistroInicial, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 30, 30));

        lblTotal.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblTotal.setForeground(new java.awt.Color(21, 67, 96));
        lblTotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTotal.setText("10");
        jPanel1.add(lblTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 130, 40, -1));

        lblIni.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblIni.setForeground(new java.awt.Color(21, 67, 96));
        lblIni.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblIni.setText("1");
        jPanel1.add(lblIni, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 130, 30, -1));

        jPanel3.setBackground(new java.awt.Color(26, 82, 118));

        btnSeleccionar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSeleccionar.setForeground(new java.awt.Color(255, 255, 255));
        btnSeleccionar.setText("Seleccionar");
        btnSeleccionar.setBorderPainted(false);
        btnSeleccionar.setContentAreaFilled(false);
        btnSeleccionar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnSeleccionarMousePressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnSeleccionar, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnSeleccionar, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 110, 130, 40));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 540, 390));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblDatosMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDatosMousePressed
        if (evt.getClickCount() == 2) {
            selectRow();
        }
    }//GEN-LAST:event_tblDatosMousePressed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        this.dispose();
        venHC.bandbp = 0;
        venHC.reiniciarHistoriaClinica();
    }//GEN-LAST:event_formWindowClosing

    private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseClicked
        this.setState(ventanaBusquedaPacAuxs.ICONIFIED);
    }//GEN-LAST:event_jLabel6MouseClicked

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
        venHC.setEstadosBotonesDeControl(2);
        venHC.bandbp = 0;
        this.dispose();
    }//GEN-LAST:event_jLabel5MouseClicked

    private void jPanel2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MousePressed
        x = evt.getX();
        y = evt.getY();
    }//GEN-LAST:event_jPanel2MousePressed

    private void jPanel2MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MouseDragged
        this.setLocation(this.getLocation().x + evt.getX() - x, this.getLocation().y + evt.getY() - y);
    }//GEN-LAST:event_jPanel2MouseDragged

    private void btnRegistroSiguienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRegistroSiguienteMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnRegistroSiguienteMouseClicked

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

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        this.ini = 0;
        GenerarTabla();
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnRegistroAnteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistroAnteriorActionPerformed
        int ini = Integer.parseInt(lblIni.getText());
        if (ini - 100 >= 0) {
            ini -= 100;
            this.ini = ini;
            GenerarTabla();
        }
    }//GEN-LAST:event_btnRegistroAnteriorActionPerformed

    private void txtFiltroKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFiltroKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            this.ini = 0;
            GenerarTabla();
        }
    }//GEN-LAST:event_txtFiltroKeyPressed

    private void btnRegistroFinalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistroFinalActionPerformed
        this.ini = this.tot - (tot % 100) + 1;

        GenerarTabla();
    }//GEN-LAST:event_btnRegistroFinalActionPerformed

    private void btnRegistroInicialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistroInicialActionPerformed
        this.ini = 0;
        GenerarTabla();
    }//GEN-LAST:event_btnRegistroInicialActionPerformed

    private void btnSeleccionarMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSeleccionarMousePressed
        if (evt.getClickCount() == 1) {
            selectRow();
        }
    }//GEN-LAST:event_btnSeleccionarMousePressed

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
            java.util.logging.Logger.getLogger(ventanaBusquedapacienteGralrsrd.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ventanaBusquedapacienteGralrsrd.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ventanaBusquedapacienteGralrsrd.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ventanaBusquedapacienteGralrsrd.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ventanaBusquedapacienteGralrsrd(null, null).setVisible(true);
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
        modelo = new DefaultTableModel(nombreColumnas, 0);

        if (datos != null) {
            for (String[] dato : datos) {
                agregarFila(dato);
            }
        }
        JTableHeader header = tblDatos.getTableHeader();
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
        tblDatos.setModel(modelo);
    }

    private void cargarTabla(String[] nombreColumnas, int filas) {
        modelo = new DefaultTableModel(nombreColumnas, filas);
        ArrayList<String[]> personas = new ArrayList<>();

        if (personas != null) {
            for (int i = 0; i < personas.size(); i++) {
                agregarFila(personas.get(i));
            }
        }
        tblDatos.setModel(modelo);
    }

    private void agregarFila(String[] fila) {
        fila[fila.length-1] = Expresiones.DecodificarTipoPaciente(fila[fila.length-1]); //se coloca paciente en vez de P, auxiliar en vez de A
        modelo.addRow(fila);
    }

    private void GenerarTabla() {
        try {
            ArrayList<String[]> datos = new ArrayList<>();
            String filtro = txtFiltro.getText();
            java.util.List<Map<String, String>> list_consul_aux = new ArrayList<>();

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
            String add = "WHERE CONCAT_WS(' ',LOWER(TID), LOWER(PNOMBRE), LOWER(SNOMBRE), LOWER(PAPELLIDO), LOWER(SAPELLIDO)) LIKE '%" + filtro + "%' ";

            SQL = "SELECT CONCAT(per.pfk_tipo_documento, per.pk_persona) TID, per.primer_nombre PNOMBRE, IFNULL(per.segundo_nombre, '') SNOMBRE, \n"
                    + "per.primer_apellido PAPELLIDO, IFNULL(per.segundo_apellido, '') SAPELLIDO,\n"
                    + "'P' TIPO\n"
                    + "FROM `pacientes` pac\n"
                    + "INNER JOIN `personas` per ON CONCAT(per.pfk_tipo_documento, per.pk_persona) = pac.pfk_paciente\n"
                    + "UNION\n"
                    + "SELECT IFNULL(paux.pk_paciente_auxiliar, '') TID, paux.primer_nombre PNOMBRE, paux.segundo_nombre SNOMBRE,\n"
                    + "paux.primer_apellido PAPELLIDO, paux.segundo_apellido SAPELLIDO, paux.tipo TIPO\n"
                    + "FROM  `paciente_auxiliar` paux WHERE estado = 'ACTIVO'\n ORDER BY PAPELLIDO ASC, SAPELLIDO ASC, PNOMBRE ASC, SNOMBRE ASC";

            System.out.println("SQL-->" + SQL);

            if (!SQL.equals("")) {
                String SQLTot = "SELECT COUNT(*) FROM (" + SQL + ") TAB";
                int tot = 0, fil = 0;
                rs = gsql.Consultar(SQLTot);
                if (rs.next()) {
                    tot = Integer.parseInt(rs.getString(1));
                }
                if (tot < 100) {
                    EstadoBotonesSig(false);
                }
                this.tot = tot;
                SQL = "SELECT * FROM (" + SQL + ") TAB " + add + " LIMIT " + ini + ", 100";
                rs = gsql.Consultar(SQL);
                fin = (tot < 100 ? tot : (fin + 100 > tot ? fin + (tot - fin) : fin + 100));
                lblIni.setText("" + ini);
                lblFin.setText("" + fin);
                lblTotal.setText("" + tot);
                if (rs.next()) {
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
                }
            }
        } catch (SQLException w) {
        }
    }

    private void EstadoBotonesSig(boolean b) {
        btnRegistroAnterior.setEnabled(b);
        btnRegistroFinal.setEnabled(b);
        btnRegistroInicial.setEnabled(b);
        btnRegistroSiguiente.setEnabled(b);
    }

    private void selectRow() {
        int filaSeleccionada;
        ArrayList<String> fila = new ArrayList<>();
        try {
            filaSeleccionada = tblDatos.getSelectedRow();

            if (filaSeleccionada > -1) {
                DefaultTableModel modAux = (DefaultTableModel) tblDatos.getModel();

                venHC.limpiarComponentesHC();
                venHC.txtIdPaciente.setText((String) modAux.getValueAt(filaSeleccionada, 0));
                venHC.txtPNombre.setText((String) modAux.getValueAt(filaSeleccionada, 1));
                venHC.pn = (String) modAux.getValueAt(filaSeleccionada, 1);
                if ((String) modAux.getValueAt(filaSeleccionada, 2) == null) {
                    venHC.txtSNombre.setText("");
                    venHC.sn = "";
                } else {
                    venHC.txtSNombre.setText((String) modAux.getValueAt(filaSeleccionada, 2));
                    venHC.sn = (String) modAux.getValueAt(filaSeleccionada, 2);
                }
                venHC.txtPApellido.setText((String) modAux.getValueAt(filaSeleccionada, 3));
                venHC.pa = (String) modAux.getValueAt(filaSeleccionada, 3);
                if ((String) modAux.getValueAt(filaSeleccionada, 4) == null) {
                    venHC.txtSApellido.setText("");
                    venHC.sa = "";
                } else {
                    venHC.txtSApellido.setText((String) modAux.getValueAt(filaSeleccionada, 4));
                    venHC.sa = (String) modAux.getValueAt(filaSeleccionada, 4);
                }

                venHC.txtNombres.setText((venHC.pn + " " + venHC.sn).trim());
                venHC.txtApellidos.setText((venHC.pa + " " + venHC.sa).trim());
                venHC.txtEstado.setText((String) modAux.getValueAt(filaSeleccionada, 5));
                venHC.band = 0;
                venHC.tipoPaciente = Expresiones.CodificarTipoPaciente((String) modAux.getValueAt(filaSeleccionada, 5));
                System.out.println("venHC.tipoPaciente--->"+venHC.tipoPaciente);
                System.out.println("Expresiones.ValidarTipoPacienteAO(venHC.tipoPaciente)--->"+Expresiones.ValidarTipoPacienteAO(venHC.tipoPaciente));
                if (!Expresiones.ValidarTipoPacienteAO(venHC.tipoPaciente)) {

                    venHC.setDocumentoTipoDoc((String) modAux.getValueAt(filaSeleccionada, 0));
                    venHC.cargarHistoriaClinica((String) modAux.getValueAt(filaSeleccionada, 0));
                } else {
                    venHC.imagenes.reiniciar();
                    venHC.txtDocumento.setText("");
                    venHC.cargarHistoriaClinica((String) modAux.getValueAt(filaSeleccionada, 0));
                    venHC.cbTipoDocumento.setSelectedIndex(0);
                    venHC.cargarIndices();//se busca el ultimo indice de la hc
                    venHC.editarHistoriaClinica(false);
                }

            } else {
                venHC.slAnguloII.setValue(0);
                venHC.limpiarComponentesHC();
                venHC.editarHistoriaClinica(true);
                venHC.cargarTablaANAMNESIS(null);
                venHC.cbTratamientos.setVisible(false);
                venHC.setEstadosBotonesDeControl(0);
                JOptionPane.showMessageDialog(null, "No ha seleccionado ninguna paciente");
                return;
            }
        } catch (HeadlessException e) {
            venHC.odontograma.limpiar();
            venHC.slAnguloII.setValue(50);
            venHC.limpiarComponentesHC();
            venHC.setEstadosBotonesDeControl(0);
            venHC.editarHistoriaClinica(true);
            venHC.cargarTablaANAMNESIS(null);
            venHC.cbTratamientos.setVisible(false);
        }

        if (venHC.tipoPaciente.equalsIgnoreCase("p")) {
            venHC.setEstadosBotonesDeControl(5);
        } else {
            venHC.setEstadosBotonesDeControl(5);
        }
        venHC.editarHistoriaClinica(false);
        venHC.habilitarDatosPaciente();
        this.dispose();

    }

    private void GenerarTablaTR() {
        try {
            ArrayList<String[]> datos = new ArrayList<>();
            String filtro = txtFiltro.getText();
            String add = "WHERE CONCAT_WS(' ',LOWER(TID), LOWER(PNOMBRE), LOWER(SNOMBRE), LOWER(PAPELLIDO), LOWER(SAPELLIDO), LOWER(EMAIL), LOWER(TELEFONO)) LIKE '%" + filtro + "%' ";

            SQL = "SELECT CONCAT(per.pfk_tipo_documento, per.pk_persona) TID, per.primer_nombre PNOMBRE, per.segundo_nombre SNOMBRE, \n"
                    + "per.primer_apellido PAPELLIDO, per.segundo_apellido SAPELLIDO,IFNULL(per.correo_electronico, '') EMAIL,\n"
                    + "IFNULL(tel.numero, IFNULL(cel.numero, '')) TELEFONO,'P' TIPO\n"
                    + "FROM `pacientes` pac\n"
                    + "INNER JOIN `personas` per ON CONCAT(per.pfk_tipo_documento, per.pk_persona) = pac.pfk_paciente\n"
                    + "LEFT JOIN `personas_telefonos` tel ON tel.pfk_persona=CONCAT(per.pk_persona) AND tel.tipo = 'FIJO'\n"
                    + "LEFT JOIN `personas_telefonos` cel ON cel.pfk_persona=CONCAT(per.pk_persona) AND cel.tipo = 'CELULAR'\n"
                    + "UNION\n"
                    + "SELECT IFNULL(paux.pk_paciente_auxiliar, '') TID, paux.primer_nombre PNOMBRE, paux.segundo_nombre SNOMBRE,\n"
                    + "paux.primer_apellido PAPELLIDO, paux.segundo_apellido SAPELLIDO, IFNULL(paux.email, '') EMAIL,\n"
                    + "IFNULL(paux.telefono, '') TELEFONO, paux.tipo TIPO\n"
                    + "FROM  `paciente_auxiliar` paux WHERE estado = 'ACTIVO'";

            System.out.println("SQL-->" + SQL);

            if (!SQL.equals("")) {
                String SQLTot = "SELECT COUNT(*) FROM (" + SQL + ") TAB";
                int tot = 0, fil = 0;
                rs = gsql.Consultar(SQLTot);
                if (rs.next()) {
                    tot = Integer.parseInt(rs.getString(1));
                }
                if (tot < 100) {
                    EstadoBotonesSig(false);
                }
                this.tot = tot;
                SQL = "SELECT * FROM (" + SQL + ") TAB " + add + " LIMIT " + ini + ", 100";
                rs = gsql.Consultar(SQL);
                fin = (tot < 100 ? tot : (fin + 100 > tot ? fin + (tot - fin) : fin + 100));
                lblIni.setText("" + ini);
                lblFin.setText("" + fin);
                lblTotal.setText("" + tot);
                if (rs.next()) {
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
                            ColDatos[i] = rs.getString(i + 1);
                        }
                        datos.add(ColDatos);

                    } while (rs.next());

                    cargarDatosTabla(Columnas, datos);

                }
            }
        } catch (SQLException w) {

        }
    }

    public void EstadoVentana() {
        if (ban == 1) {
            this.setVisible(true);
        }
    }

    private List<Map<String, String>> getFiltroLista(String filtro) {
        java.util.List<Map<String, String>> retorno = new ArrayList<>();
        System.out.println("***************getFiltroLista*****************" + filtro);
        int b = -1;
        String[] filtros = filtro.isEmpty()?null:filtro.replace(" ", "<::>").split("<::>");
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
                    valores += ""+value;
//                    System.out.println("value--->" + value);
//                    int con = value.toUpperCase().indexOf(filtro.toUpperCase());
//                    System.out.println("con--->" + con);
//                    if (con >= 0) {
//                        b = 0;
//                        break;
//                    }
                }
                boolean encontro = Expresiones.filtrobusqueda(filtros,valores);
                System.out.println("i-"+i+"-b-"+b);
                if(encontro){
                    retorno.add(list_consul.get(i));
                }
            }
        }
        System.out.println("********************retorno --> " + retorno.size() + "***********************");
        return retorno;
    }
}
