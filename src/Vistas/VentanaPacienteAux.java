/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;

import BaseDeDatos.gestorMySQL;
import Busquedas.ventanaBusquedaPacAuxs;
import Busquedas.ventanaBusquedaTratamientos;
import Control.*; 
import HISTORIA_CLINICA.ventana;
import Informes.PlantillasAdicionales;
import Utilidades.Expresiones;
import Utilidades.Utilidades;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 *
 * @author AT-DESARROLLO
 */
public class VentanaPacienteAux extends javax.swing.JFrame {

    public DefaultTableModel modeloCotizacion;
    public DefaultListModel modlistTelefonos;
    public ArrayList<String[]> listaCotizaciones = new ArrayList<String[]>();
    public String[] datos;
    public String[] EncabezadoCotizaciones;
    boolean[] editc = {false, false, false, false, false, false};
    ControlPacAux pacaux = new ControlPacAux();
    public VentanaPrincipal vprin;
    public PlantillasAdicionales plantilla = new PlantillasAdicionales();
    public gestorMySQL con;
    private int x, y;
    public ventana venHC;

    /**
     * Creates new form VentanaPacienteAux
     */
    public VentanaPacienteAux(VentanaPrincipal vprin) {
        initComponents();
        Utilidades.EstablecerIcono(this);
        this.vprin = vprin;
        this.setLocationRelativeTo(vprin);
        con = new gestorMySQL();

        EncabezadoCotizaciones = new String[]{"Tratamiento", "Costo Total", "Cuota Inicial", "Cuota", ""};
        EncabezadoCotizaciones = new String[]{"Tratamiento".toUpperCase(), "Costo Total".toUpperCase(), "Cuota Inicial".toUpperCase(),
            "Diferidas en".toUpperCase(), "Cuota".toUpperCase(), "Acción".toUpperCase()};
        modlistTelefonos = new DefaultListModel();
        InicializarTblCotizaciones();
        lblIdentificacion.setVisible(false);
        PacienteAuxEstadoFormulario(0);
        PacienteAuxEstadoFormulario(1);
    }

    public VentanaPacienteAux(ventana venHC) {
        initComponents();
        Utilidades.EstablecerIcono(this);
        this.venHC = venHC;
        this.setLocationRelativeTo(venHC);
        con = new gestorMySQL();

        EncabezadoCotizaciones = new String[]{"Tratamiento", "Costo Total", "Cuota Inicial", "Cuota", ""};
        EncabezadoCotizaciones = new String[]{"Tratamiento".toUpperCase(), "Costo Total".toUpperCase(), "Cuota Inicial".toUpperCase(),
            "Diferidas en".toUpperCase(), "Cuota".toUpperCase(), "Acción".toUpperCase()};
        modlistTelefonos = new DefaultListModel();
        InicializarTblCotizaciones();
        lblIdentificacion.setVisible(false);
        PacienteAuxEstadoFormulario(2);

        lblIdentificacion.setText(venHC.txtIdPaciente.getText());
        ConsultarPacienteAux();
        
        jTabbedPane1.setSelectedIndex(1);
        
        
//        VentanaCotizaciones vc = new VentanaCotizaciones(this, -1);
//        vc.setVisible(true);
//        
//        new ventanaBusquedaTratamientos(1, "ID:-:TRATAMIENTO", "", vc, 2);
    }

    public VentanaPacienteAux() {

    }

    public void InicializarTblCotizaciones() {
        modeloCotizacion = new DefaultTableModel(EncabezadoCotizaciones, 0) {
            Class[] types = new Class[]{
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int col) {
                return types[col];
            }

            public boolean isCellEditable(int row, int col) {
                //System.out.println("row-->"+row+" -- col-->"+col+"::::: "+edit[col]);
                return editc[col];
            }
        };

        tbl_cotizaciones.setModel(modeloCotizacion);

        tbl_cotizaciones.getColumnModel().getColumn(0).setPreferredWidth(150);
        tbl_cotizaciones.getColumnModel().getColumn(1).setPreferredWidth(70);
        tbl_cotizaciones.getColumnModel().getColumn(2).setPreferredWidth(70);
        tbl_cotizaciones.getColumnModel().getColumn(3).setPreferredWidth(60);
        tbl_cotizaciones.getColumnModel().getColumn(4).setPreferredWidth(60);
        tbl_cotizaciones.getColumnModel().getColumn(4).setPreferredWidth(60);

        for (int i = 0; i < modeloCotizacion.getColumnCount(); i++) {
            tbl_cotizaciones.getColumnModel().getColumn(i).setResizable(false);
            DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
            if (i == 0) {
                tcr.setHorizontalAlignment(SwingConstants.LEFT);
            } else if (i > 0 && i < modeloCotizacion.getColumnCount() - 2) {
                tcr.setHorizontalAlignment(SwingConstants.RIGHT);
            } else {
                tcr.setHorizontalAlignment(SwingConstants.CENTER);
            }

            tbl_cotizaciones.getColumnModel().getColumn(i).setCellRenderer(tcr);
//            tblListaCitas.getColumnModel().getColumn(i).setHeaderRenderer(thcr);
        }
        JTableHeader header = tbl_cotizaciones.getTableHeader();
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtApellidos = new javax.swing.JTextField();
        txtNombres = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        txtTelefono = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        lstTelefonos = new javax.swing.JList();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        cbEstado = new javax.swing.JComboBox();
        lblIdentificacion = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        cbTipo = new javax.swing.JComboBox();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel8 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbl_cotizaciones = new Tabla.cotPacTable(){

        };
        btnAgregar = new javax.swing.JButton();
        btnImprimirCotizaciones = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btnConsultar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnDescartar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Pacientes Auxiliares");
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(26, 82, 118), 1, true));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTabbedPane1.setForeground(new java.awt.Color(21, 67, 96));
        jTabbedPane1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(21, 67, 96));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Nombres");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, -1, -1));

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(21, 67, 96));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("Apellidos");
        jPanel2.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, -1, -1));

        txtApellidos.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtApellidos.setForeground(new java.awt.Color(21, 67, 96));
        txtApellidos.setBorder(null);
        txtApellidos.setCaretColor(new java.awt.Color(21, 67, 96));
        txtApellidos.setSelectionColor(new java.awt.Color(21, 67, 96));
        txtApellidos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtApellidosActionPerformed(evt);
            }
        });
        jPanel2.add(txtApellidos, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 610, 30));

        txtNombres.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtNombres.setForeground(new java.awt.Color(21, 67, 96));
        txtNombres.setBorder(null);
        txtNombres.setCaretColor(new java.awt.Color(21, 67, 96));
        txtNombres.setSelectionColor(new java.awt.Color(21, 67, 96));
        jPanel2.add(txtNombres, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 610, 30));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(21, 67, 96));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel3.setText("Correo Electronico");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 120, -1));

        txtEmail.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtEmail.setForeground(new java.awt.Color(21, 67, 96));
        txtEmail.setBorder(null);
        txtEmail.setCaretColor(new java.awt.Color(21, 67, 96));
        txtEmail.setSelectionColor(new java.awt.Color(21, 67, 96));
        txtEmail.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtEmailFocusLost(evt);
            }
        });
        jPanel2.add(txtEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 260, 30));

        txtTelefono.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtTelefono.setForeground(new java.awt.Color(21, 67, 96));
        txtTelefono.setBorder(null);
        txtTelefono.setCaretColor(new java.awt.Color(21, 67, 96));
        txtTelefono.setSelectionColor(new java.awt.Color(21, 67, 96));
        txtTelefono.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTelefonoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTelefonoKeyReleased(evt);
            }
        });
        jPanel2.add(txtTelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 160, 130, 30));

        lstTelefonos.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lstTelefonos.setForeground(new java.awt.Color(21, 67, 96));
        lstTelefonos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lstTelefonosMousePressed(evt);
            }
        });
        jScrollPane4.setViewportView(lstTelefonos);

        jPanel2.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 140, 123, 80));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(21, 67, 96));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Telefono");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 140, -1, -1));

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(21, 67, 96));
        jLabel9.setText("Estado");
        jPanel2.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 270, -1, -1));

        cbEstado.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cbEstado.setForeground(new java.awt.Color(21, 67, 96));
        cbEstado.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Activo", "Inactivo" }));
        cbEstado.setMinimumSize(new java.awt.Dimension(84, 20));
        cbEstado.setPreferredSize(new java.awt.Dimension(84, 20));
        cbEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbEstadoActionPerformed(evt);
            }
        });
        jPanel2.add(cbEstado, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 260, 100, 30));
        jPanel2.add(lblIdentificacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(427, 197, -1, -1));

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(21, 67, 96));
        jLabel10.setText("Tipo");
        jPanel2.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 220, -1, -1));

        cbTipo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cbTipo.setForeground(new java.awt.Color(21, 67, 96));
        cbTipo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Seleccionar", "Auxiliar", "Ocasional" }));
        cbTipo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbTipoItemStateChanged(evt);
            }
        });
        jPanel2.add(cbTipo, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 210, 120, 30));
        jPanel2.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 610, 10));
        jPanel2.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 610, 10));

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/der_1.png"))); // NOI18N
        jLabel8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel8MouseClicked(evt);
            }
        });
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 160, 30, 30));
        jPanel2.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, 260, 10));
        jPanel2.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 190, 130, 10));

        jTabbedPane1.addTab("Datos Basicos", jPanel2);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        tbl_cotizaciones.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tbl_cotizaciones.setModel(new javax.swing.table.DefaultTableModel(
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
        tbl_cotizaciones.setGridColor(new java.awt.Color(255, 255, 255));
        tbl_cotizaciones.setSelectionBackground(new java.awt.Color(41, 128, 185));
        tbl_cotizaciones.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_cotizacionesMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tbl_cotizacionesMousePressed(evt);
            }
        });
        tbl_cotizaciones.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbl_cotizacionesKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbl_cotizacionesKeyReleased(evt);
            }
        });
        jScrollPane3.setViewportView(tbl_cotizaciones);

        btnAgregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/add.png"))); // NOI18N
        btnAgregar.setToolTipText("Agregar Cotización");
        btnAgregar.setContentAreaFilled(false);
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });

        btnImprimirCotizaciones.setBackground(new java.awt.Color(0, 0, 204));
        btnImprimirCotizaciones.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/impresora_1.png"))); // NOI18N
        btnImprimirCotizaciones.setToolTipText("Imprimir Cotizaciones");
        btnImprimirCotizaciones.setBorder(null);
        btnImprimirCotizaciones.setBorderPainted(false);
        btnImprimirCotizaciones.setContentAreaFilled(false);
        btnImprimirCotizaciones.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnImprimirCotizaciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimirCotizacionesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnImprimirCotizaciones, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnAgregar))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 635, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAgregar)
                    .addComponent(btnImprimirCotizaciones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Cotizaciones", jPanel3);

        jPanel4.add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 660, 350));

        jPanel5.setBackground(new java.awt.Color(26, 82, 118));
        jPanel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel5MousePressed(evt);
            }
        });
        jPanel5.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jPanel5MouseDragged(evt);
            }
        });
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/minimizar.png"))); // NOI18N
        jLabel7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel7MouseClicked(evt);
            }
        });
        jPanel5.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 0, 30, 30));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/cerrar_1.png"))); // NOI18N
        jLabel5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5MouseClicked(evt);
            }
        });
        jPanel5.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 0, 30, 30));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Pacientes auxiliares ");
        jPanel5.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 8, 152, -1));

        jPanel4.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 700, 30));

        btnConsultar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/consultar.png"))); // NOI18N
        btnConsultar.setToolTipText("Consutar");
        btnConsultar.setBorderPainted(false);
        btnConsultar.setContentAreaFilled(false);
        btnConsultar.setMargin(new java.awt.Insets(2, 10, 2, 10));
        btnConsultar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConsultarActionPerformed(evt);
            }
        });
        jPanel4.add(btnConsultar, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 420, -1, -1));

        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/eliminar.png"))); // NOI18N
        btnEliminar.setToolTipText("Eliminar");
        btnEliminar.setBorderPainted(false);
        btnEliminar.setContentAreaFilled(false);
        btnEliminar.setMargin(new java.awt.Insets(2, 10, 2, 8));
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });
        jPanel4.add(btnEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 420, -1, -1));

        btnDescartar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/descartar.png"))); // NOI18N
        btnDescartar.setToolTipText("Descartar");
        btnDescartar.setBorderPainted(false);
        btnDescartar.setContentAreaFilled(false);
        btnDescartar.setMargin(new java.awt.Insets(2, 10, 2, 8));
        btnDescartar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDescartarActionPerformed(evt);
            }
        });
        jPanel4.add(btnDescartar, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 420, -1, -1));

        btnModificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/modificar.png"))); // NOI18N
        btnModificar.setToolTipText("Modificar");
        btnModificar.setBorderPainted(false);
        btnModificar.setContentAreaFilled(false);
        btnModificar.setMargin(new java.awt.Insets(2, 14, 2, 5));
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });
        jPanel4.add(btnModificar, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 420, -1, -1));

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/guardar.png"))); // NOI18N
        btnGuardar.setToolTipText("Guardar");
        btnGuardar.setBorderPainted(false);
        btnGuardar.setContentAreaFilled(false);
        btnGuardar.setMargin(new java.awt.Insets(2, 10, 2, 8));
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });
        jPanel4.add(btnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 420, -1, -1));

        getContentPane().add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 700, 510));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tbl_cotizacionesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_cotizacionesMouseClicked
        int fila = tbl_cotizaciones.getSelectedRow();
        int cola = tbl_cotizaciones.getSelectedColumn();
        String dato = "" + tbl_cotizaciones.getValueAt(fila, cola);
        if (cola == 5) {//QUITAR
            if (dato.toUpperCase().equals("QUITAR")) {
                modeloCotizacion.removeRow(fila);
                tbl_cotizaciones.setModel(modeloCotizacion);
                System.out.println("fila-->" + fila);
                listaCotizaciones.remove(fila);
            }
        }
//        if(cola == 5){//QUITAR  
//
//            if(dato.toUpperCase().equals("QUITAR")){
//                modeloCotizacion.removeRow(fila);
//                tbl_cotizaciones.setModel(modeloCotizacion);
//                System.out.println("fila-->"+fila);
//                listaCotizaciones.remove(fila);
//            }
//        }
//        if(cola == 4){//ACTIVAR
//            if(dato.toUpperCase().equals("ACTIVAR")){
//                System.out.println("**********************ACTIVAR***********************");
//                String[] info = listaCotizaciones.get(fila);
//                for(int i =0; i < info.length; i++){
//                    System.out.println("info["+i+"]-->"+info[i]);
//                }
//                String ctot = ""+tbl_cotizaciones.getValueAt(fila, 1);
//                String cini = ""+tbl_cotizaciones.getValueAt(fila, 2);
//                String cuota = ""+tbl_cotizaciones.getValueAt(fila, 3);
//                boolean val = ValidarTratamiento(info[0]);  
//                System.out.println("****************val***"+val+"***********ctot--->"+ctot+"**cini--->"+cini+"***cuota--->"+cuota+"");
//
//                if(!ctot.equals("") && !cini.equals("") && !cuota.equals("") && !val){
//                    //datos ---> idtra 0, tra 1, costot 2, cuotaI 3, cuota 4,
//                    System.out.println("ENTRE");
//                    AgregarTratamiento(new String[]{info[0], info[1], ctot, cini, cuota, "0", "", "",""});
//                }
//            }
//        }
    }//GEN-LAST:event_tbl_cotizacionesMouseClicked

    private void tbl_cotizacionesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbl_cotizacionesKeyPressed
        System.out.println("*************************************tbl_cotizacionesKeyPressed********************************************");
//        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
//            int fila = tbl_cotizaciones.getSelectedRow();
//            int cola = tbl_cotizaciones.getSelectedColumn();
//            String dato =""+tbl_cotizaciones.getValueAt(fila, cola);
//            if(cola >= 1 && cola <= 3){
//                boolean cond = exp.validarSoloNumeros(dato);
//                if(!cond){
//                    tbl_cotizaciones.setValueAt("", fila, cola);
//                    //                    tbl_cotizaciones.requestFocus();
//                    //                    tbl_cotizaciones.editCellAt(fila, cola);
//                }
//            }
//
//        }
    }//GEN-LAST:event_tbl_cotizacionesKeyPressed

    private void txtEmailFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEmailFocusLost
        validarCampoEm(txtEmail);
    }//GEN-LAST:event_txtEmailFocusLost

    private void txtTelefonoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelefonoKeyPressed
        if (KeyEvent.VK_ENTER == evt.getKeyCode() || KeyEvent.VK_SPACE == evt.getKeyCode()) {
            String dat = txtTelefono.getText();
            System.out.println("*************telefono****************" + dat);
            if (!estaSeleccionado(modlistTelefonos, dat) && !dat.equals("") && (dat.length() == 7 || dat.length() == 10)) {
                modlistTelefonos.addElement(dat);
                lstTelefonos.setModel(modlistTelefonos);
                txtTelefono.setText("");
                txtTelefono.requestFocusInWindow();
            } else if (dat.length() < 7) {
                JOptionPane.showMessageDialog(rootPane, "Por favor Digite correctamente el número de telefono.");
            } else if (dat.length() < 9) {
                JOptionPane.showMessageDialog(rootPane, "Por favor Digite correctamente el número de celular.");
            }
        }
    }//GEN-LAST:event_txtTelefonoKeyPressed

    private void lstTelefonosMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lstTelefonosMousePressed
        if (evt.getClickCount() == 2) {
            modlistTelefonos.remove(lstTelefonos.getSelectedIndex());
            lstTelefonos.setModel(modlistTelefonos);
        }
    }//GEN-LAST:event_lstTelefonosMousePressed

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        //new ventanaBusquedaTratamientos(1, "ID:-:TRATAMIENTO", "", this, 1);
        new VentanaCotizaciones(this, -1).setVisible(true);
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        System.out.println("*****************************************GUARDAR PACIENTE AUXILIAR------" + venHC);
//        if (venHC == null) {
        Map<String, String> campos = new HashMap<String, String>();
        String id = lblIdentificacion.getText();
        String noms = txtNombres.getText();
        String apes = txtApellidos.getText();
        String cor = txtEmail.getText();

        String tel = "";
        for (int i = 0; i < modlistTelefonos.size(); i++) {
            tel += (tel.equals("") ? "" : "<>") + modlistTelefonos.getElementAt(i);
        }

        String est = cbEstado.getSelectedItem().toString();
        String tpo = cbTipo.getSelectedItem().toString();

        campos.put("id", id);
        campos.put("noms", Utilidades.CodificarElemento(noms.toUpperCase()));
        campos.put("apes", Utilidades.CodificarElemento(apes.toUpperCase()));
        campos.put("cor", cor);
        campos.put("tel", tel);
        campos.put("est", est);
        campos.put("tpo", tpo);
        System.out.println("tpo-->" + tpo);
        if (tpo.equals("Seleccionar")) {
            JOptionPane.showMessageDialog(this, "Por favor seleccione el tipo de paciente auxiliar, para completar la operación.");
            return;
        }
        ArrayList<String[]> listaCot = new ArrayList<>();
        if (!tpo.equals("Ocasional")) {
            listaCot = getDatosCotizaciones();

            if (listaCot == null) {
                JOptionPane.showMessageDialog(rootPane, "No esta permitido el ingreso de texto en la tabla de cotizaciones. Por favor verificar la información e intentelo nuevamente.");
                return;
            }
        }
        if (noms.equals("")) {
            JOptionPane.showConfirmDialog(this, "Por favor coloque el nombre para realizar la operación");
            return;
        }
        if (apes.equals("")) {
            JOptionPane.showConfirmDialog(this, "Por favor coloque el apellido para realizar la operación");
            return;
        }

        pacaux.setPacAuxiliar(campos, listaCot);
        System.out.println("tpo--->"+tpo);
        if(tpo.equals("Auxiliar")){
            int opc = JOptionPane.showConfirmDialog(this, "¿Desea Imprimir la Cotización?", "Confirmación", JOptionPane.YES_NO_OPTION);
            if (opc == JOptionPane.YES_OPTION) {
                ImprimirCotizacion();
            }else{
                PacienteAuxEstadoFormulario(0);
                PacienteAuxEstadoFormulario(1);
                if (venHC != null) {
        //        } else {
                    venHC.cargarTratamientos(campos.get("id"));
                    this.dispose();
                }
            }
        }else{
            PacienteAuxEstadoFormulario(0);
            PacienteAuxEstadoFormulario(1);
            if (venHC != null) {
    //        } else {
                venHC.cargarTratamientos(campos.get("id"));
                this.dispose();
            }
        }
        
        

    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        PacienteAuxEstadoFormulario(2);
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnDescartarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDescartarActionPerformed
        PacienteAuxEstadoFormulario(0);
        PacienteAuxEstadoFormulario(1);
    }//GEN-LAST:event_btnDescartarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        String id = lblIdentificacion.getText();
        if (!id.equals("")) {
            pacaux.delPacienteAux(id);
            PacienteAuxEstadoFormulario(0);
            PacienteAuxEstadoFormulario(1);
        }

    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnConsultarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConsultarActionPerformed
        ////TRAER EL BQD CITAS
        new ventanaBusquedaPacAuxs(1, "IDENTIFICACION", "", this);
    }//GEN-LAST:event_btnConsultarActionPerformed

    private void cbTipoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbTipoItemStateChanged
        CambioEstado();
    }//GEN-LAST:event_cbTipoItemStateChanged
    public void CambioEstado() {
        if (cbTipo.getSelectedItem().equals("Auxiliar")) {
            jTabbedPane1.setEnabledAt(1, true);
        } else {
            jTabbedPane1.setEnabledAt(1, false);
        }
    }

    private void cbEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbEstadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbEstadoActionPerformed

    private void tbl_cotizacionesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbl_cotizacionesKeyReleased
        // TODO add your handling code here:
        System.out.println("**********************tbl_cotizacionesKeyReleased***********************");
        int fila = tbl_cotizaciones.getSelectedRow();
        int cola = tbl_cotizaciones.getSelectedColumn();

        System.out.println("fila---->" + fila);
        System.out.println("cola---->" + cola);
        String valor = "" + tbl_cotizaciones.getValueAt(fila, cola);
        System.out.println("valor---->" + valor);
        String dato = Expresiones.procesarSoloNum(valor);
        System.out.println("dato---->" + dato);
        tbl_cotizaciones.setValueAt("" + dato, fila, cola);
        //Expresiones.procesarSoloNumeros();
    }//GEN-LAST:event_tbl_cotizacionesKeyReleased

    private void txtTelefonoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelefonoKeyReleased
        Expresiones.procesarSoloNumeros(txtTelefono);
    }//GEN-LAST:event_txtTelefonoKeyReleased

    private void jLabel7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseClicked
        this.setState(VentanaPacienteAux.ICONIFIED);
    }//GEN-LAST:event_jLabel7MouseClicked

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
        this.dispose();
    }//GEN-LAST:event_jLabel5MouseClicked

    private void jPanel5MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel5MousePressed
        x = evt.getX();
        y = evt.getY();
    }//GEN-LAST:event_jPanel5MousePressed

    private void jPanel5MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel5MouseDragged
        this.setLocation(this.getLocation().x + evt.getX() - x, this.getLocation().y + evt.getY() - y);
    }//GEN-LAST:event_jPanel5MouseDragged

    private void txtApellidosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtApellidosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtApellidosActionPerformed

    private void jLabel8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel8MouseClicked
        String dat = txtTelefono.getText();
        System.out.println("*************telefono****************" + dat);
        if (!estaSeleccionado(modlistTelefonos, dat) && !dat.equals("") && (dat.length() == 7 || dat.length() == 10)) {
            modlistTelefonos.addElement(dat);
            lstTelefonos.setModel(modlistTelefonos);
            txtTelefono.setText("");
            txtTelefono.requestFocusInWindow();
        } else if (dat.length() < 7) {
            JOptionPane.showMessageDialog(rootPane, "Por favor Digite correctamente el número de telefono.");
        } else if (dat.length() < 9) {
            JOptionPane.showMessageDialog(rootPane, "Por favor Digite correctamente el número de celular.");
        }
    }//GEN-LAST:event_jLabel8MouseClicked

    private void tbl_cotizacionesMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_cotizacionesMousePressed
        if (evt.getClickCount() == 2) {
            int fila = tbl_cotizaciones.getSelectedRow();
            String estado = "" + tbl_cotizaciones.getModel().getValueAt(fila, 5);
            if (!estado.equals("ACTIVADO")) {
                new VentanaCotizaciones(this, fila).setVisible(true);
            }
        }
    }//GEN-LAST:event_tbl_cotizacionesMousePressed

    private void btnImprimirCotizacionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirCotizacionesActionPerformed
        // TODO add your handling code here:
        //JOptionPane.showMessageDialog(this, "NO HAS HECHO ESTO");        
        ImprimirCotizacion();

    }//GEN-LAST:event_btnImprimirCotizacionesActionPerformed

    public void ImprimirCotizacion(){
        String noms = txtNombres.getText().trim();
        String apes = txtApellidos.getText().trim();
        Map<String, String> list = new HashMap <String, String>();        
        list.put("nombre", ""+noms+" "+apes);
        list.put("tipo", "10");
        ArrayList<String[]> tbl_cotizacion = getTblCotizacion();
        String archivo = plantilla.GenerarinformesAux(list, tbl_cotizacion);
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
            java.util.logging.Logger.getLogger(VentanaPacienteAux.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaPacienteAux.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaPacienteAux.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaPacienteAux.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VentanaPacienteAux().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnConsultar;
    private javax.swing.JButton btnDescartar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnImprimirCotizaciones;
    private javax.swing.JButton btnModificar;
    private javax.swing.JComboBox cbEstado;
    private javax.swing.JComboBox cbTipo;
    public javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    public javax.swing.JLabel jLabel13;
    public javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    public javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    public javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane3;
    public javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JTabbedPane jTabbedPane1;
    public javax.swing.JLabel lblIdentificacion;
    private javax.swing.JList lstTelefonos;
    private javax.swing.JTable tbl_cotizaciones;
    public javax.swing.JTextField txtApellidos;
    public javax.swing.JTextField txtEmail;
    public javax.swing.JTextField txtNombres;
    public javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables

    public void ConsultarPacienteAux() {
        String id = lblIdentificacion.getText();

        if (id.equals("")) {
            JOptionPane.showMessageDialog(rootPane, "Por favor digite una identificación");
        }
        ArrayList<String[]> Info = new ArrayList<>();
        ArrayList<String[]> lst_cot = new ArrayList<>();

        Info = pacaux.getPacAux(id);

        if (Info.size() > 0) {
            String[] datos = Info.get(0);

            txtNombres.setText(datos[1]);
            txtApellidos.setText(datos[2]);
            txtEmail.setText(datos[3]);
            String[] tels = (datos[4].equals("") ? null : datos[4].split("<>"));
            if (tels != null) {
                System.out.println("tels--->" + tels.length);
                for (int i = 0; i < tels.length; i++) {
                    modlistTelefonos.addElement(tels[i]);
                }
                lstTelefonos.setModel(modlistTelefonos);
            }
            System.out.println("*******datos[5]*******" + datos[5] + "****");
            if (Expresiones.CodificarTipoPaciente(datos[5]).equals("A")) {
                cbTipo.setSelectedIndex(1);
            } else if (Expresiones.CodificarTipoPaciente(datos[5]).equals("O")) {
                cbTipo.setSelectedIndex(2);
            }

            cbEstado.setSelectedItem(datos[6]);
            if (Expresiones.CodificarTipoPaciente(datos[5]).equals("A")) {
                lst_cot = pacaux.getPacienteCotizaciones(id);
                if (lst_cot.size() > 0) {
                    AgregarCotizacion(lst_cot);
                }
            }
            PacienteAuxEstadoFormulario(3);
        } else {
            PacienteAuxEstadoFormulario(1);
        }
    }

    private void validarCampoEm(JTextField txtEmail) {
        if (!txtEmail.getText().equals("")) {
            Pattern pat = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,4})$");
            Matcher mat = pat.matcher(txtEmail.getText());
            if (mat.matches()) {
                System.out.println("SI");
            } else {
                txtEmail.setText("");
                System.out.println("NO");
                JOptionPane.showMessageDialog(rootPane, "Escriba un correo electronico valido");
            }
        }
    }

    public void AgregarCotizacion() {
        listaCotizaciones.add(datos);
        //datos ---> idtra 0, tra 1    
        /////////////////////////////////  
        agregarFilaCotizacion(new String[]{datos[1], datos[2], datos[3], datos[4], datos[5], "Quitar"});
        LimpiarDatos();
    }

    public void AgregarCotizacion(ArrayList<String[]> lista) {
        for (String[] lst : lista) {
            listaCotizaciones.add(lst);
            agregarFilaCotizacion(new String[]{lst[1], Utilidades.MascaraMoneda(lst[2]), Utilidades.MascaraMoneda(lst[3]), lst[4], Utilidades.MascaraMoneda(lst[5]), ""});
            //LimpiarDatos();        
        }
    }

    public void ActualizarCotizacion(int fila) {
        listaCotizaciones.set(fila, datos);
        tbl_cotizaciones.setValueAt("" + datos[1], fila, 0);//Tratamiento
        tbl_cotizaciones.setValueAt("" + datos[2], fila, 1);//Costo
        tbl_cotizaciones.setValueAt("" + datos[3], fila, 2);//Cuota inicial
        tbl_cotizaciones.setValueAt("" + datos[4], fila, 3);//diferidas en 
        tbl_cotizaciones.setValueAt("" + datos[5], fila, 4);//Cuotas

        LimpiarDatos();
    }

    private void agregarFilaCotizacion(String[] fila) {
        modeloCotizacion.addRow(fila);
    }

    private void LimpiarDatos() {
        datos = null;
    }

    private boolean estaSeleccionado(DefaultListModel modelo, String valor) {
        for (int i = 0; i < modelo.size(); i++) {
            if (modelo.getElementAt(i).equals(valor)) {
                return true;
            }
        }
        return false;
    }

    private ArrayList<String[]> getDatosCotizaciones() {
        ArrayList<String[]> ret = new ArrayList<>();
        try {
            int tam = tbl_cotizaciones.getRowCount();
//            System.out.println("***************getDatosCotizaciones**************+"+tam);
            for (int i = 0; i < tam; i++) {
                String ctot = "" + tbl_cotizaciones.getValueAt(i, 1);
                String cini = "" + tbl_cotizaciones.getValueAt(i, 2);
                String cuota = "" + tbl_cotizaciones.getValueAt(i, 4);
                String dif = "" + tbl_cotizaciones.getValueAt(i, 3);
                ret.add(new String[]{listaCotizaciones.get(i)[0], ctot.replace(".", ""), cini.replace(".", ""),
                    cuota.replace(".", ""), listaCotizaciones.get(i)[6], listaCotizaciones.get(i)[7], dif});

            }
//            System.out.println("**********END ****************"+ret.size());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("");
        }
        return ret;
    }

    private void PacienteAuxEstadoFormulario(int opc) {
        PacienteAuxEstadoBotones(opc);
        CambioEstado();
        switch (opc) {
            case 0: {//DESCARTAR
                lblIdentificacion.setText("");
                txtNombres.setText("");
                txtApellidos.setText("");
                txtTelefono.setText("");
                lstTelefonos.removeAll();
                txtEmail.setText("");
                cbEstado.setSelectedIndex(0);
                cbTipo.setSelectedIndex(0);
                ////
                txtNombres.setEnabled(false);
                txtApellidos.setEnabled(false);
                txtTelefono.setEnabled(false);
                txtEmail.setEnabled(false);
                cbEstado.setEnabled(false);
                cbTipo.setEnabled(false);
                LimpiartblCot();
                LimpiarLstTel();
                jTabbedPane1.setEnabledAt(1, false);
                btnAgregar.setEnabled(false);
                jTabbedPane1.setSelectedIndex(0);
                break;
            }
            case 1: {
                txtNombres.setEnabled(true);
                txtApellidos.setEnabled(true);
                txtTelefono.setEnabled(true);
                txtEmail.setEnabled(true);
                cbEstado.setEnabled(true);
                cbTipo.setEnabled(true);
                btnAgregar.setEnabled(true);
                //jTabbedPane1.setEnabledAt(1,true);
                txtNombres.requestFocus();
                break;
            }
            case 2: {
                txtNombres.setEnabled(true);
                txtApellidos.setEnabled(true);
                txtTelefono.setEnabled(true);
                txtEmail.setEnabled(true);
                cbEstado.setEnabled(true);
                cbTipo.setEnabled(true);
                btnAgregar.setEnabled(true);
                //jTabbedPane1.setEnabledAt(1,true);
                break;
            }
            case 3: {
                txtNombres.setEnabled(false);
                txtApellidos.setEnabled(false);
                txtTelefono.setEnabled(false);
                txtEmail.setEnabled(false);
                cbEstado.setEnabled(false);
                cbTipo.setEnabled(false);
                btnAgregar.setEnabled(false);
                //jTabbedPane1.setEnabledAt(1,false);
                break;
            }
        }
    }

    public void PacienteAuxEstadoBotones(int opc) {
        switch (opc) {
            case 0: {//INICIO         
                btnGuardar.setEnabled(false);
                btnModificar.setEnabled(false);
                btnDescartar.setEnabled(false);
                btnEliminar.setEnabled(false);
                btnConsultar.setEnabled(true);
                break;
            }
            case 1: {//NUEVO
                btnGuardar.setEnabled(true);
                btnModificar.setEnabled(false);
                btnDescartar.setEnabled(true);
                btnEliminar.setEnabled(false);
                btnConsultar.setEnabled(true);
                break;
            }
            case 2: {//MODIFICAR
                btnGuardar.setEnabled(true);
                btnModificar.setEnabled(false);
                btnDescartar.setEnabled(true);
                btnEliminar.setEnabled(true);
                btnConsultar.setEnabled(false);
                break;
            }
            case 3: {//CONSULTADO
                btnGuardar.setEnabled(false);
                btnModificar.setEnabled(true);
                btnDescartar.setEnabled(true);
                btnEliminar.setEnabled(true);
                btnConsultar.setEnabled(false);
                break;
            }
        }
    }

    private void LimpiarLstTel() {
        int tam = modlistTelefonos.size();
        System.out.println("tam-LST-TELEFONOS->" + tam);
        //for(int i = 0; i < tam; i++){
        modlistTelefonos.removeAllElements();
        //}
        lstTelefonos.setModel(modlistTelefonos);
    }

    private void LimpiartblCot() {
        int tam = tbl_cotizaciones.getRowCount();
        for (int i = 0; i < tam; i++) {
            modeloCotizacion.removeRow(0);
            tbl_cotizaciones.setModel(modeloCotizacion);
        }
        listaCotizaciones.clear();
    }

    private ArrayList<String[]> getTblCotizacion() {
        try{
            ArrayList<String[]> ret = new ArrayList<>();
            
            int tam = tbl_cotizaciones.getRowCount();
            
            for (int i = 0; i < tam; i++) {
                String trat = "" + tbl_cotizaciones.getValueAt(i, 0);
                String ctot = "" + tbl_cotizaciones.getValueAt(i, 1);
                String cini = "" + tbl_cotizaciones.getValueAt(i, 2);
                String dif = "" + tbl_cotizaciones.getValueAt(i, 3);
                String cuota = "" + tbl_cotizaciones.getValueAt(i, 4);
                
                ret.add(new String[]{trat, ctot, cini, cuota, dif});

            }
            
            return ret;
        }catch(Exception e){
            return null;
        }
    }
}
