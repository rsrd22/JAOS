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
import Vistas.VentanaAnularFactura;

import Vistas.ventanaConsultarF;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 *
 * @author User
 */
public class ventanaBusquedaPacFact extends javax.swing.JFrame {

    private gestorMySQL gsql;
    public DefaultTableModel modelo;
    public ControlPersonas cper;
    public int opc;
    public String[] NameColumnas;
    public String DatosEntrada;
    public String estadoch;
    public String SQL = "";
    public ResultSet rs = null;
    public int ini = 0;
    public int fin = 0;
    public int tot = 0;
    public int registrosIniciales, registrosTotales, registrosInicialPag, registrosFinalPag;
//    public ventanaPrincipalFacturacion vpf;
    public ventanaConsultarF vcf;
    public VentanaAnularFactura vaf;
    public String d = "";
    private int x, y;
    List<Map<String, String>> list_consul;
    public int ban = 0;

    /**
     * Creates new form ventanaBusqueda
     */
    public ventanaBusquedaPacFact() {
        initComponents();
        Utilidades.EstablecerIcono(this);
        modelo = new DefaultTableModel();
        this.setLocationRelativeTo(null);
        cper = new ControlPersonas();

        String[] nombreColumnas = new String[8];
        nombreColumnas = new String[]{"Nombre", "Dirección", "Fecha de Nacimiento", "Sexo", "E-mail"};
        int filas = 0;

        cargarTabla(nombreColumnas, filas);

    }

    public ventanaBusquedaPacFact(ventanaConsultarF vcf, String estadoch) {

        initComponents();
        Utilidades.EstablecerIcono(this);
        gsql = new gestorMySQL();
        this.estadoch = estadoch;
        modelo = new DefaultTableModel();
        this.setLocationRelativeTo(null);

        LlenarListaDatos();
        GenerarTabla();

        tblDatos.setModel(modelo);

        tblDatos.getColumnModel().getColumn(0).setPreferredWidth(80);
        tblDatos.getColumnModel().getColumn(1).setPreferredWidth(60);
        tblDatos.getColumnModel().getColumn(2).setPreferredWidth(60);
        tblDatos.getColumnModel().getColumn(3).setPreferredWidth(70);
        tblDatos.getColumnModel().getColumn(4).setPreferredWidth(60);
        tblDatos.getColumnModel().getColumn(5).setPreferredWidth(160);
        tblDatos.getColumnModel().getColumn(6).setPreferredWidth(150);
        tblDatos.getColumnModel().getColumn(7).setPreferredWidth(70);
        tblDatos.getColumnModel().getColumn(8).setPreferredWidth(40);

        JTableHeader header = tblDatos.getTableHeader();
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < modelo.getColumnCount(); i++) {
            tblDatos.getColumnModel().getColumn(i).setResizable(false);
            DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
            if (i == 7) {
                tcr.setHorizontalAlignment(SwingConstants.CENTER);
            } else {
                tcr.setHorizontalAlignment(SwingConstants.LEFT);
            }
            tblDatos.getColumnModel().getColumn(i).setCellRenderer(tcr);
        }
        this.vcf = vcf;

    }

    public ventanaBusquedaPacFact(VentanaAnularFactura vaf) {

        initComponents();
        Utilidades.EstablecerIcono(this);
        gsql = new gestorMySQL();
        modelo = new DefaultTableModel();
        this.setLocationRelativeTo(null);

        LlenarListaDatos();
        GenerarTabla();

        tblDatos.setModel(modelo);

        tblDatos.getColumnModel().getColumn(0).setPreferredWidth(80);
        tblDatos.getColumnModel().getColumn(1).setPreferredWidth(60);
        tblDatos.getColumnModel().getColumn(2).setPreferredWidth(60);
        tblDatos.getColumnModel().getColumn(3).setPreferredWidth(70);
        tblDatos.getColumnModel().getColumn(4).setPreferredWidth(60);
        tblDatos.getColumnModel().getColumn(5).setPreferredWidth(160);
        tblDatos.getColumnModel().getColumn(6).setPreferredWidth(150);
        tblDatos.getColumnModel().getColumn(7).setPreferredWidth(70);
        tblDatos.getColumnModel().getColumn(8).setPreferredWidth(40);

        JTableHeader header = tblDatos.getTableHeader();
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < modelo.getColumnCount(); i++) {
            tblDatos.getColumnModel().getColumn(i).setResizable(false);
            DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
            if (i == 7) {
                tcr.setHorizontalAlignment(SwingConstants.CENTER);
            } else {
                tcr.setHorizontalAlignment(SwingConstants.LEFT);
            }
            tblDatos.getColumnModel().getColumn(i).setCellRenderer(tcr);
        }
        this.vaf = vaf;

    }

    private void LlenarListaDatos() {

        SQL = "SELECT CONCAT(per.pfk_tipo_documento, per.pk_persona) IDENTIFICACION , per.primer_nombre NOMBRE, IFNULL(per.segundo_nombre, '') SEGNOMBRE, \n"
                + "  per.primer_apellido APELLIDO, IFNULL(per.segundo_apellido, '') SEGAPELLIDO ,IFNULL(per.correo_electronico, '') EMAIL,\n"
                + "  IFNULL(per.`direccion`, '') DIRECCION, REPLACE(GROUP_CONCAT(IFNULL(tel.numero, IFNULL(cel.numero, ''))), ',', ', ') TELEFONO,'P' TIPO\n"
                + "  FROM `pacientes` pac\n"
                + "  INNER JOIN `personas` per ON CONCAT(per.pfk_tipo_documento, per.pk_persona) = pac.pfk_paciente\n"
                + "  LEFT JOIN `personas_telefonos` tel ON tel.pfk_persona= CONCAT(per.`pfk_tipo_documento`,per.pk_persona) AND tel.tipo = 'FIJO'\n"
                + "  LEFT JOIN `personas_telefonos` cel ON cel.pfk_persona= CONCAT(per.`pfk_tipo_documento`,per.pk_persona) AND cel.tipo = 'CELULAR'\n"
                + "  GROUP BY pac.pfk_paciente\n UNION\n"
                + "  SELECT IFNULL(paux.pk_paciente_auxiliar, '') IDENTIFICACION, paux.primer_nombre NOMBRE, paux.segundo_nombre SEGNOMBRE ,                     \n"
                + "  paux.primer_apellido APELLIDO, paux.segundo_apellido SEGAPELLIDO , IFNULL(paux.email, '') EMAIL,\n"
                + "	 '' DIRECCION,\n"
                + "  IFNULL(paux.telefono, '') TELEFONO, paux.tipo TIPO\n"
                + "	 FROM `paciente_auxiliar` paux WHERE estado = 'ACTIVO'\nORDER BY APELLIDO ASC, SEGAPELLIDO ASC, NOMBRE ASC, SEGNOMBRE ASC";

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
        jPanel3 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel4 = new javax.swing.JPanel();
        btnSeleccionar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Busqueda Pacientes ");
        setBackground(new java.awt.Color(255, 255, 255));
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 82, 118)));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(21, 67, 96));
        jLabel1.setText("Filtro");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, -1, -1));

        txtFiltro.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtFiltro.setForeground(new java.awt.Color(31, 97, 141));
        txtFiltro.setBorder(null);
        txtFiltro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtFiltroKeyPressed(evt);
            }
        });
        jPanel1.add(txtFiltro, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 370, 30));

        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/lupa_1.png"))); // NOI18N
        btnBuscar.setBorderPainted(false);
        btnBuscar.setContentAreaFilled(false);
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });
        jPanel1.add(btnBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 70, 30, 30));

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
        tblDatos.setGridColor(new java.awt.Color(21, 67, 96));
        tblDatos.setSelectionBackground(new java.awt.Color(41, 128, 185));
        tblDatos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDatosMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblDatosMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblDatos);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 159, 1000, 230));

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
        jPanel1.add(lblFin, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 130, 30, -1));

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(21, 67, 96));
        jLabel3.setText("de");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 130, -1, -1));

        lblTotal.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblTotal.setForeground(new java.awt.Color(21, 67, 96));
        lblTotal.setText("10");
        jPanel1.add(lblTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 130, 40, -1));

        jPanel3.setBackground(new java.awt.Color(26, 82, 118));
        jPanel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel3MousePressed(evt);
            }
        });
        jPanel3.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jPanel3MouseDragged(evt);
            }
        });
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/minimizar.png"))); // NOI18N
        jLabel7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel7MouseClicked(evt);
            }
        });
        jPanel3.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 0, 30, 30));

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/cerrar_1.png"))); // NOI18N
        jLabel8.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel8MouseClicked(evt);
            }
        });
        jPanel3.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 0, 30, 30));

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Consulta de Pacientes");
        jPanel3.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 220, -1));

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1030, 31));
        jPanel1.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 370, 10));

        jPanel4.setBackground(new java.awt.Color(26, 82, 118));

        btnSeleccionar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSeleccionar.setForeground(new java.awt.Color(255, 255, 255));
        btnSeleccionar.setText("Seleccionar");
        btnSeleccionar.setBorderPainted(false);
        btnSeleccionar.setContentAreaFilled(false);
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

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnSeleccionar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnSeleccionar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 110, 130, 40));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1030, 410));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        this.ini = 0;
        GenerarTabla();
    }//GEN-LAST:event_btnBuscarActionPerformed

    public String[] setDocumentoTipoDoc(String texto) {
        String tipoDoc = "", doc = "";
        for (int i = 0; i < texto.length(); i++) {
            if (Expresiones.validarSoloNumeros("" + texto.charAt(i))) {
                tipoDoc = texto.substring(0, i);
                System.out.println(texto.substring(i));
                doc = texto.substring(i);
                break;
            }
        }
        return new String[]{tipoDoc, doc};

    }

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

    private void tblDatosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDatosMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblDatosMouseClicked

    private void tblDatosMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDatosMousePressed
        if (evt.getClickCount() == 2) {
            SeleccionarFila();
        }
    }//GEN-LAST:event_tblDatosMousePressed

    private void jLabel7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseClicked
        this.setState(ventanaBusquedaPacFact.ICONIFIED);
    }//GEN-LAST:event_jLabel7MouseClicked

    private void jLabel8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel8MouseClicked
        this.dispose();
    }//GEN-LAST:event_jLabel8MouseClicked

    private void jPanel3MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel3MousePressed
        x = evt.getX();
        y = evt.getY();
    }//GEN-LAST:event_jPanel3MousePressed

    private void jPanel3MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel3MouseDragged
        this.setLocation(this.getLocation().x + evt.getX() - x, this.getLocation().y + evt.getY() - y);
    }//GEN-LAST:event_jPanel3MouseDragged

    private void btnSeleccionarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSeleccionarMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSeleccionarMouseClicked

    private void btnSeleccionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSeleccionarActionPerformed
        SeleccionarFila();
    }//GEN-LAST:event_btnSeleccionarActionPerformed

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
            java.util.logging.Logger.getLogger(ventanaBusqueda.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ventanaBusqueda.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ventanaBusqueda.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ventanaBusqueda.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ventanaBusquedaPacFact(null, null).setVisible(true);
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
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
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
            for (int i = 0; i < datos.size(); i++) {
                agregarFila(datos.get(i));
            }
        }
        tblDatos.setModel(modelo);
    }

    private void cargarTabla(String[] nombreColumnas, int filas) {
        modelo = new DefaultTableModel(nombreColumnas, filas);
        ArrayList<String[]> personas = new ArrayList<>();

        personas = cper.getPersonas();
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

                tblDatos.getColumnModel().getColumn(0).setPreferredWidth(80);
                tblDatos.getColumnModel().getColumn(1).setPreferredWidth(60);
                tblDatos.getColumnModel().getColumn(2).setPreferredWidth(60);
                tblDatos.getColumnModel().getColumn(3).setPreferredWidth(70);
                tblDatos.getColumnModel().getColumn(4).setPreferredWidth(60);
                tblDatos.getColumnModel().getColumn(5).setPreferredWidth(160);
                tblDatos.getColumnModel().getColumn(6).setPreferredWidth(150);
                tblDatos.getColumnModel().getColumn(7).setPreferredWidth(70);
                tblDatos.getColumnModel().getColumn(8).setPreferredWidth(40);

                JTableHeader header = tblDatos.getTableHeader();
                ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
                for (int i = 0; i < modelo.getColumnCount(); i++) {
                    tblDatos.getColumnModel().getColumn(i).setResizable(false);
                    DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
                    if (i == 7) {
                        tcr.setHorizontalAlignment(SwingConstants.CENTER);
                    } else {
                        tcr.setHorizontalAlignment(SwingConstants.LEFT);
                    }
                    tblDatos.getColumnModel().getColumn(i).setCellRenderer(tcr);
                }
                
                

            } else {
                JOptionPane.showMessageDialog(null, "No se encontraron datos");

            }

        } catch (Exception w) {
            w.printStackTrace();
        }
    }

//    private void GenerarTabla() {
//        try {
//            ArrayList<String[]> datos = new ArrayList<>();
//            String filtro = txtFiltro.getText();
//            String add  ="WHERE CONCAT_WS(' ',LOWER(TID), LOWER(NOMBRE), LOWER(EMAIL), LOWER(TELEFONO)) LIKE '%"+filtro+"%' ";
//            switch (estadoch) {//PERSONAL
//                case "0": {
//                    SQL = "SELECT  CONCAT (per.pfk_tipo_documento, per.pk_persona) TID, CONCAT_WS(' ', per.primer_nombre, per.segundo_nombre)NOMBRE, CONCAT_WS (' ',per.primer_apellido,per.segundo_apellido) APELLIDO,\n" +
//                        "IFNULL(per.correo_electronico, '') EMAIL,IFNULL(tel.numero, IFNULL(cel.numero, ''))TELEFONO\n" +
//                        "FROM `pacientes` pac\n" +
//                        "INNER JOIN `personas` per ON CONCAT(per.pfk_tipo_documento, per.pk_persona) = pac.pfk_paciente\n" +
//                        "LEFT JOIN `personas_telefonos` tel ON tel.pfk_persona=CONCAT(per.pk_persona) AND tel.tipo = 'FIJO'\n" +
//                        "LEFT JOIN `personas_telefonos` cel ON cel.pfk_persona=CONCAT(per.pk_persona) AND cel.tipo = 'CELULAR'";
//
//                    break;
//                }
//                case "1": {
////                    SQL = "SELECT IFNULL(paux.pk_paciente_auxiliar, '') TID, \n" +
////                        "CONCAT_WS( ' ', paux.primer_nombre, paux.segundo_nombre)NOMBRE, CONCAT_WS(' ', paux.primer_apellido, paux.segundo_apellido) APELLIDOS,\n" +
////                        "IFNULL(paux.telefono, '') TELEFONO, IFNULL(paux.email, '') EMAIL  \n" +
////                        "FROM  `paciente_auxiliar` paux ";
//                        SQL="SELECT IFNULL(paux.pk_paciente_auxiliar, '') TID,\n" +
//                        "CONCAT_WS( ' ', paux.primer_nombre, paux.segundo_nombre)NOMBRE, CONCAT_WS(' ', paux.primer_apellido, paux.segundo_apellido) APELLIDOS,\n" +
//                        "IFNULL(paux.telefono, '') TELEFONO, IFNULL(paux.email, '') EMAIL ,IFNULL (`costo_tratamiento`,'') COSTO_TRATAMIENTO\n" +
//                        "FROM  `paciente_auxiliar` paux ";
//                    
////                    break;
//                }
//            }
//            System.out.println("SQL-->"+SQL);
//            if (!SQL.equals("")) {
//                String SQLTot = "SELECT COUNT(*) FROM (" + SQL + ") TABLA";
//                int tot = 0, fil = 0;
//                rs = gsql.Consultar(SQLTot);
//                if (rs.next()) {
//                    tot = Integer.parseInt(rs.getString(1));
//                }
//                if (tot < 100) {
//                    EstadoBotonesSig(false);
//                }
//                this.tot = tot;
//                SQL = "SELECT * FROM ("+SQL+") TABLA "+add+" LIMIT " + ini + ", 100";
//                rs = gsql.Consultar(SQL);
//                fin = (tot < 100 ? tot : (fin + 100 > tot ? fin + (tot - fin) : fin + 100));
//                lblIni.setText("" + ini);
//                lblFin.setText("" + fin);
//                lblTotal.setText("" + tot);
//                if (rs.next()) {
//                    ResultSetMetaData rsmd = (ResultSetMetaData) rs.getMetaData();
//                    String[] Columnas = new String[rsmd.getColumnCount()];
//
//                    for (int j = 1; j <= rsmd.getColumnCount(); j++) {
//                        Columnas[j - 1] = rsmd.getColumnName(j);
//                    }
//                    NameColumnas = Columnas;
//                    do {
//                        tot++;
//                        String[] ColDatos = new String[rsmd.getColumnCount()];
//                        for (int i = 0; i < ColDatos.length; i++) {
//                            ColDatos[i] = rs.getString(i + 1);
//                        }
//                        datos.add(ColDatos);
//
//                    } while (rs.next());
//
//                    cargarDatosTabla(Columnas, datos);
//
//                } else {
//                    JOptionPane.showMessageDialog(null, "No se encontraron datos");
//                }
//            }
//        } catch (SQLException w) {
//
//        }
//    }
    private void GenerarTablaOLD() {
        try {
            ArrayList<String[]> datos = new ArrayList<>();
            String filtro = Utilidades.CodificarElemento(txtFiltro.getText());
            String add = "WHERE CONCAT_WS(' ',LOWER(IDENTIFICACION), LOWER(NOMBRE), LOWER(SEGNOMBRE ), LOWER(APELLIDO), LOWER(SEGAPELLIDO), LOWER(EMAIL), LOWER(TELEFONO)) LIKE '%" + filtro + "%' ";

            SQL = "SELECT CONCAT(per.pfk_tipo_documento, per.pk_persona) IDENTIFICACION , per.primer_nombre NOMBRE, IFNULL(per.segundo_nombre, '') SEGNOMBRE, \n"
                    + "  per.primer_apellido APELLIDO, IFNULL(per.segundo_apellido, '') SEGAPELLIDO ,IFNULL(per.correo_electronico, '') EMAIL,\n"
                    + "  IFNULL(per.`direccion`, '') DIRECCION, REPLACE(GROUP_CONCAT(IFNULL(tel.numero, IFNULL(cel.numero, ''))), ',', ', ') TELEFONO,'P' TIPO\n"
                    + "  FROM `pacientes` pac\n"
                    + "  INNER JOIN `personas` per ON CONCAT(per.pfk_tipo_documento, per.pk_persona) = pac.pfk_paciente\n"
                    + "  LEFT JOIN `personas_telefonos` tel ON tel.pfk_persona= CONCAT(per.`pfk_tipo_documento`,per.pk_persona) AND tel.tipo = 'FIJO'\n"
                    + "  LEFT JOIN `personas_telefonos` cel ON cel.pfk_persona= CONCAT(per.`pfk_tipo_documento`,per.pk_persona) AND cel.tipo = 'CELULAR'\n"
                    + "  GROUP BY pac.pfk_paciente\n UNION\n"
                    + "  SELECT IFNULL(paux.pk_paciente_auxiliar, '') IDENTIFICACION, paux.primer_nombre NOMBRE, paux.segundo_nombre SEGNOMBRE ,                     \n"
                    + "  paux.primer_apellido APELLIDO, paux.segundo_apellido SEGAPELLIDO , IFNULL(paux.email, '') EMAIL,\n"
                    + "	 '' DIRECCION,\n"
                    + "  IFNULL(paux.telefono, '') TELEFONO, paux.tipo TIPO\n"
                    + "	 FROM `paciente_auxiliar` paux WHERE estado = 'ACTIVO'\nORDER BY APELLIDO ASC, SEGAPELLIDO ASC, NOMBRE ASC, SEGNOMBRE ASC";

            System.out.println("SQL-->" + SQL);

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

    private void SeleccionarFila() {
        int filaSeleccionada;
        ArrayList<String> fila = new ArrayList<>();
        try {
            filaSeleccionada = tblDatos.getSelectedRow();

            if (filaSeleccionada > -1) {
                DefaultTableModel modAux = (DefaultTableModel) tblDatos.getModel();
                //for (int j = 0; j < Salida.length; j++) {
//                    fila.add((String)modAux.getValueAt(filaSeleccionada, 0));
//                    fila.add((String)modAux.getValueAt(filaSeleccionada, 1));
//             vcf=  new ventanaConsultarFactura_();
//                if (estadoch == "0") {

                d = Expresiones.CodificarTipoPaciente((String) modAux.getValueAt(filaSeleccionada, 8));
                vcf.d = d;
                vcf.TipoP = (String) modAux.getValueAt(filaSeleccionada, 0);

                System.out.println(" ---------" + (String) modAux.getValueAt(filaSeleccionada, 8));

                if (d.equals("A") || d.equals("O")) {

                    System.out.println("entre en A // O");

                    vcf.combotipoDoc.setEnabled(true);
                    vcf.txtnombre.setText((String) modAux.getValueAt(filaSeleccionada, 1) + " " + (String) modAux.getValueAt(filaSeleccionada, 2));
                    vcf.txtapellidos.setText((String) modAux.getValueAt(filaSeleccionada, 3) + " " + (String) modAux.getValueAt(filaSeleccionada, 4));
//                      vcf.lblValorApagar.setText((String) modAux.getValueAt(filaSeleccionada, 5));
                    vcf.txtdocumento.setText("");
                    vcf.combotipoDoc.setSelectedItem("Seleccionar");
                    vcf.lblDocumento.setText((String) modAux.getValueAt(filaSeleccionada, 0));
                    vcf.lblDireccion.setText("");
                    vcf.lblTelefono.setText("");
                    if(d.equals("O")){
                        vcf.EstadoPaciente();
                    }

                } else {

                    System.out.println("entre en P");
                    vcf.txtdocumento.setText(setDocumentoTipoDoc((String) modAux.getValueAt(filaSeleccionada, 0))[1]);
                    vcf.combotipoDoc.setSelectedItem(setDocumentoTipoDoc((String) modAux.getValueAt(filaSeleccionada, 0))[0]);
                    vcf.txtnombre.setText((String) modAux.getValueAt(filaSeleccionada, 1) + " " + (String) modAux.getValueAt(filaSeleccionada, 2));
                    vcf.txtapellidos.setText((String) modAux.getValueAt(filaSeleccionada, 3) + " " + (String) modAux.getValueAt(filaSeleccionada, 4));
                    vcf.lblDireccion.setText((String) modAux.getValueAt(filaSeleccionada, 6));
                    vcf.lblTelefono.setText((String) modAux.getValueAt(filaSeleccionada, 7));
                    vcf.combotipoDoc.setEnabled(false);
                    vcf.txtnombre.setEnabled(false);
                    vcf.txtapellidos.setEnabled(false);
                    vcf.txtdocumento.setEnabled(false);

                }

                vcf.lblPnombre.setText((String) modAux.getValueAt(filaSeleccionada, 1));
                vcf.lblSnombre.setText((String) modAux.getValueAt(filaSeleccionada, 2));
                vcf.lblPapellido.setText((String) modAux.getValueAt(filaSeleccionada, 3));
                vcf.lblSapellido.setText((String) modAux.getValueAt(filaSeleccionada, 4));
                vcf.lblDocumento.setText((String) modAux.getValueAt(filaSeleccionada, 0));

                vcf.CargarTratamientoPaciente();
                vcf.totalPago();
                vcf.listaFact((String) modAux.getValueAt(filaSeleccionada, 0));

//                } else {
//                    vcf.combotipoDoc.setEnabled(true);
//                    vcf.txtnombre.setText((String) modAux.getValueAt(filaSeleccionada, 1));
//                    vcf.txtapellidos.setText((String) modAux.getValueAt(filaSeleccionada, 2));
//                    vcf.lblValorApagar.setText((String) modAux.getValueAt(filaSeleccionada, 5));
////                }
//                    vcf.txtapellido.setText((String)modAux.getValueAt(filaSeleccionada, 2));
//                    vcf.mostrarcampos(true);
//                    
                //}
//                switch (opc) {
//                    case 1:
//                        vpf.lblidentificacion.setText(fila.get(0));
//                        vpf.lblretNombre.setText(fila.get(1));
//                        this.dispose();
//                        break;
//                    default:
//                        break;
//                }
            } else {
                JOptionPane.showMessageDialog(null, "No ha seleccionado ninguna fila");
            }

        } catch (HeadlessException e) {

        }
        this.dispose();
    }

    

    public void EstadoVentana() {
        if (ban == 1) {
            this.setVisible(true);
        }
    }

    private List<Map<String, String>> getFiltroLista(String filtro) {
        List<Map<String, String>> retorno = new ArrayList<>();
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
//                    System.out.println("NAme-"+j+"->"+NameColumnas[j]);
                    String value = list_consul.get(i).get(NameColumnas[j]);
                    valores += ""+value; 
//                    System.out.println("value--->"+value);
//                    System.out.println("NAme-" + j + "->" + NameColumnas[j]);
//                    String value = list_consul.get(i).get(NameColumnas[j]);
//                    System.out.println("value--->" + value);
//                    int con = value.toUpperCase().indexOf(filtro.toUpperCase());
//                    System.out.println("con--->" + con);
//                    if (con >= 0) {
//                        b = 0;
//                        break;
//                    }
                }
                boolean encontro = Expresiones.filtrobusqueda(filtros,valores);
                System.out.println("IIIIIIIIIIIIII-"+valores);
                System.out.println("IIIIIIIIIIIIII-"+encontro);
                if(encontro){
                    retorno.add(list_consul.get(i));
                }
            }
        }
        System.out.println("********************retorno --> " + retorno.size() + "***********************");
        return retorno;
    }
    
    private List<Map<String, String>> getFiltroLista(String filtro, int a) {
        List<Map<String, String>> retorno = new ArrayList<>();
        System.out.println("***************getFiltroLista*****************" + filtro);
        int b = -1;
        String[] filtros = filtro.isEmpty()?null:filtro.replace(" ", "<::>").split("<::>");
        String valores = "";
        for (int i = 0; i < list_consul.size(); i++) {
            b = 1;
            if (filtro.isEmpty()) {
                retorno.add(list_consul.get(i));
            } else {
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
