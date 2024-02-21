/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;

import BaseDeDatos.gestorMySQL;
import Busquedas.ventanaBusquedaPaciente;
import Busquedas.ventanaBusquedaPacienteN;
import Busquedas.ventanaBusquedaTratamientos;
import Control.ControlPaciente;
import Informes.PlantillasAdicionales;
import Tabla.NumberTableCellRenderer;
import Utilidades.Expresiones;
import Utilidades.Utilidades;
import Utilidades.datosUsuario;
import com.sun.org.apache.bcel.internal.generic.AALOAD;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 *
 * @author mary
 */
public class VistaPaciente extends javax.swing.JFrame {

    public VentanaPrincipal vprin;
    public gestorMySQL con;
    ControlPaciente paciente = new ControlPaciente();
    //DATOS 0-->idTrat , 1--> TRAT, 2--> CostoTotal, 3-->CuotaIni, 4-->diferidas, 5 --> cuota, 6-->(0 si es nuevo, 1 si ya esta en la tabla), 7-->(VACIO) 
    public String[] datos;
    public DefaultTableModel modeloCotizacion;
    public DefaultListModel modlistTelefonos;
    public DefaultTableModel modeloTratamientos;
    public ArrayList<String[]> listaCotizaciones = new ArrayList<String[]>();
    public ArrayList<String[]> listaTratamiento = new ArrayList<String[]>();
    public ArrayList<String[]> listaTratamientoAux = new ArrayList<String[]>();
    public String[] Encabezadotratamientos;
    public String[] EncabezadoCotizaciones;
    public PlantillasAdicionales plantilla = new PlantillasAdicionales();
    ArrayList<String> lista_tpoID;
    public String tipoId = "";

    boolean[] editc = {false, true, true, true, true, false, false}; 
    boolean[] editt = {false, true, true, true, true, false, false, false};
    String obs = "";
    public int enterID = 0;
    public int lostID = 0;
    Expresiones exp = new Expresiones();
    private int x, y;
    public String idPacAux = "";

    /**
     * Creates new form VistaPaciente
     */
    public VistaPaciente(VentanaPrincipal vprin) {
        initComponents();
        Utilidades.EstablecerIcono(this);
        this.vprin = vprin;
        this.setLocationRelativeTo(vprin);
        con = new gestorMySQL();

        Encabezadotratamientos = new String[]{"Tratamiento".toUpperCase(), "Costo Total".toUpperCase(), "Cuota Inicial".toUpperCase(),
            "Diferidas en".toUpperCase(), "Cuota".toUpperCase(), "Fecha".toUpperCase(), "Estado".toUpperCase(), "Acción".toUpperCase()};
        EncabezadoCotizaciones = new String[]{"Tratamiento".toUpperCase(), "Costo Total".toUpperCase(), "Cuota Inicial".toUpperCase(),
            "Diferidas en".toUpperCase(), "Cuota".toUpperCase(), "Estado".toUpperCase(), "Acción".toUpperCase()};
        modlistTelefonos = new DefaultListModel();
        InicializarTblCotizaciones();
        InicializarTblTratamientos();
        PacienteEstadoFormulario(0);

        lista_tpoID = new ArrayList<>();
        //Seleccionar, CC, CE, TI, RC, NIT, NUI, PAS
        lista_tpoID.add("-1");
        lista_tpoID.add("CC");
        lista_tpoID.add("CE");
        lista_tpoID.add("TI");
        lista_tpoID.add("RC");
        lista_tpoID.add("NIT");
        lista_tpoID.add("NUI");
        lista_tpoID.add("PAS");
//        btnCambioID.setVisible(false);
//        btnImprimirCotizaciones.setVisible(false);

    }

    public void InicializarTblCotizaciones() {
        modeloCotizacion = new DefaultTableModel(EncabezadoCotizaciones, 0) {
            Class[] types = new Class[]{
                java.lang.Object.class,
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int col) {
                return types[col];
            }

            public boolean isCellEditable(int row, int col) {
                //System.out.println("row-->"+row+" -- col-->"+col+"::::: "+edit[col]);                
                return false;

            }
        };

        tbl_cotizaciones.setModel(modeloCotizacion);
        tbl_cotizaciones.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "selectNextColumnCell");

        tbl_cotizaciones.getColumnModel().getColumn(0).setPreferredWidth(130);
        tbl_cotizaciones.getColumnModel().getColumn(1).setPreferredWidth(70);
        tbl_cotizaciones.getColumnModel().getColumn(2).setPreferredWidth(80);
        tbl_cotizaciones.getColumnModel().getColumn(3).setPreferredWidth(70);
        tbl_cotizaciones.getColumnModel().getColumn(4).setPreferredWidth(60);
        tbl_cotizaciones.getColumnModel().getColumn(5).setPreferredWidth(60);
        tbl_cotizaciones.getColumnModel().getColumn(5).setPreferredWidth(60);

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

    public void InicializarTblTratamientos() {
        //{"Tratamiento", "Costo Total", "Cuota Inicial", "Cuota", "Fecha", "Estado"};
        modeloTratamientos = new DefaultTableModel(Encabezadotratamientos, 0) {
            Class[] types = new Class[]{
                java.lang.Object.class,
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int col) {
                return types[col];
            }

            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        tbl_tratamientos.setModel(modeloTratamientos);
        tbl_tratamientos.getColumnModel().getColumn(0).setCellRenderer(new NumberTableCellRenderer());
        tbl_tratamientos.getColumnModel().getColumn(2).setCellRenderer(new NumberTableCellRenderer());
        tbl_tratamientos.getColumnModel().getColumn(4).setCellRenderer(new NumberTableCellRenderer());

        tbl_tratamientos.getColumnModel().getColumn(0).setPreferredWidth(130);
        tbl_tratamientos.getColumnModel().getColumn(1).setPreferredWidth(70);
        tbl_tratamientos.getColumnModel().getColumn(2).setPreferredWidth(80);
        tbl_tratamientos.getColumnModel().getColumn(3).setPreferredWidth(70);
        tbl_tratamientos.getColumnModel().getColumn(4).setPreferredWidth(50);
        tbl_tratamientos.getColumnModel().getColumn(5).setPreferredWidth(60);
        tbl_tratamientos.getColumnModel().getColumn(6).setPreferredWidth(60);
        tbl_tratamientos.getColumnModel().getColumn(7).setPreferredWidth(50);

        for (int i = 0; i < modeloTratamientos.getColumnCount(); i++) {
            tbl_tratamientos.getColumnModel().getColumn(i).setResizable(false);
            DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
            if (i == 0) {
                tcr.setHorizontalAlignment(SwingConstants.LEFT);
            } else if (i > 0 && i < modeloTratamientos.getColumnCount() - 2) {
                tcr.setHorizontalAlignment(SwingConstants.RIGHT);
            } else {
                tcr.setHorizontalAlignment(SwingConstants.CENTER);
            }

            tbl_tratamientos.getColumnModel().getColumn(i).setCellRenderer(tcr);
//            tblListaCitas.getColumnModel().getColumn(i).setHeaderRenderer(thcr);
        }
        JTableHeader header = tbl_tratamientos.getTableHeader();
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

        jPanel1 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        lblId = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        lblTid = new javax.swing.JLabel();
        cbTid = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtNombres = new javax.swing.JTextField();
        txtDireccion = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtTelefono = new javax.swing.JTextField();
        txtCorreo = new javax.swing.JTextField();
        jdFnac = new com.toedter.calendar.JDateChooser();
        cbSexo = new javax.swing.JComboBox();
        jLabel13 = new javax.swing.JLabel();
        txtApellidos = new javax.swing.JTextField();
        lblTipo = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        cbEstado = new javax.swing.JComboBox();
        jScrollPane4 = new javax.swing.JScrollPane();
        lstTelefonos = new javax.swing.JList();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        jSeparator6 = new javax.swing.JSeparator();
        jLabel5 = new javax.swing.JLabel();
        btnCambioID = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbl_cotizaciones = new Tabla.cotPacTable(){

        };
        btnAgregarCotizaciones = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        btnImprimirCotizaciones = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_tratamientos = new javax.swing.JTable();
        jLabel15 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        btnGuardar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnDescartar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnConsultar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Pacientes");
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 82, 118)));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/cerrar_1.png"))); // NOI18N
        jLabel10.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel10MouseClicked(evt);
            }
        });
        jPanel5.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 5, -1, -1));

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/minimizar.png"))); // NOI18N
        jLabel11.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel11MouseClicked(evt);
            }
        });
        jPanel5.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 5, -1, 30));

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Pacientes");
        jPanel5.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 12, -1, -1));

        jPanel1.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 700, 40));

        jTabbedPane1.setForeground(new java.awt.Color(26, 82, 118));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblId.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblId.setForeground(new java.awt.Color(26, 82, 118));
        lblId.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblId.setText("Identificación");
        jPanel2.add(lblId, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 20, 100, 20));

        txtId.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtId.setForeground(new java.awt.Color(31, 97, 141));
        txtId.setBorder(null);
        txtId.setCaretColor(new java.awt.Color(31, 97, 141));
        txtId.setFocusCycleRoot(true);
        txtId.setSelectionColor(new java.awt.Color(36, 113, 163));
        txtId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtIdFocusLost(evt);
            }
        });
        txtId.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtIdKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtIdKeyReleased(evt);
            }
        });
        jPanel2.add(txtId, new org.netbeans.lib.awtextra.AbsoluteConstraints(354, 40, 130, 30));

        lblTid.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblTid.setForeground(new java.awt.Color(26, 82, 118));
        lblTid.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblTid.setText("Tipo Identificación ");
        jPanel2.add(lblTid, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 150, -1));

        cbTid.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cbTid.setForeground(new java.awt.Color(31, 97, 141));
        cbTid.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Seleccionar", "Cédula de Ciudadania", "Cédula de Extrangeria", "Tarjeta de Identidad", "Registro Civil", "Número de Identificación Tributario", "Número Unico de Identificación", "Pasaporte" }));
        cbTid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbTidActionPerformed(evt);
            }
        });
        jPanel2.add(cbTid, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 142, 30));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(26, 82, 118));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel1.setText("Nombres");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 56, -1));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(26, 82, 118));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setText("Dirección");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, 99, -1));

        txtNombres.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtNombres.setForeground(new java.awt.Color(31, 97, 141));
        txtNombres.setBorder(null);
        txtNombres.setCaretColor(new java.awt.Color(31, 97, 141));
        txtNombres.setSelectionColor(new java.awt.Color(36, 113, 163));
        jPanel2.add(txtNombres, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, 280, 30));

        txtDireccion.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtDireccion.setForeground(new java.awt.Color(31, 97, 141));
        txtDireccion.setBorder(null);
        txtDireccion.setCaretColor(new java.awt.Color(31, 97, 141));
        txtDireccion.setSelectionColor(new java.awt.Color(36, 113, 163));
        txtDireccion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDireccionActionPerformed(evt);
            }
        });
        jPanel2.add(txtDireccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 170, 280, 30));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(26, 82, 118));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel3.setText("Fecha de Nacimiento");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 220, -1, -1));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(26, 82, 118));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel4.setText("Sexo");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 150, -1, -1));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(26, 82, 118));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel6.setText("Telefono");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 220, 99, -1));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(26, 82, 118));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel7.setText("Correo");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, 99, -1));
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 308, -1, -1));

        txtTelefono.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtTelefono.setForeground(new java.awt.Color(31, 97, 141));
        txtTelefono.setBorder(null);
        txtTelefono.setCaretColor(new java.awt.Color(31, 97, 141));
        txtTelefono.setSelectionColor(new java.awt.Color(36, 113, 163));
        txtTelefono.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTelefonoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTelefonoKeyReleased(evt);
            }
        });
        jPanel2.add(txtTelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 240, 123, 30));

        txtCorreo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtCorreo.setForeground(new java.awt.Color(31, 97, 141));
        txtCorreo.setBorder(null);
        txtCorreo.setCaretColor(new java.awt.Color(31, 97, 141));
        txtCorreo.setSelectionColor(new java.awt.Color(36, 113, 163));
        txtCorreo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCorreoFocusLost(evt);
            }
        });
        jPanel2.add(txtCorreo, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 310, 280, 30));

        jdFnac.setBackground(new java.awt.Color(255, 255, 255));
        jdFnac.setForeground(new java.awt.Color(31, 97, 141));
        jdFnac.setDateFormatString("dd/MM/yyyy");
        jPanel2.add(jdFnac, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 240, 123, 30));

        cbSexo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cbSexo.setForeground(new java.awt.Color(31, 97, 141));
        cbSexo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Seleccionar", "Femenino", "Masculino" }));
        cbSexo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbSexoActionPerformed(evt);
            }
        });
        jPanel2.add(cbSexo, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 170, 126, 30));

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(26, 82, 118));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel13.setText("Apellidos");
        jPanel2.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 80, 70, -1));

        txtApellidos.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtApellidos.setForeground(new java.awt.Color(31, 97, 141));
        txtApellidos.setBorder(null);
        txtApellidos.setCaretColor(new java.awt.Color(31, 97, 141));
        txtApellidos.setSelectionColor(new java.awt.Color(36, 113, 163));
        jPanel2.add(txtApellidos, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 100, 288, 30));
        jPanel2.add(lblTipo, new org.netbeans.lib.awtextra.AbsoluteConstraints(412, 11, -1, -1));

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(26, 82, 118));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel9.setText("Estado");
        jPanel2.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 290, -1, -1));

        cbEstado.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cbEstado.setForeground(new java.awt.Color(31, 97, 141));
        cbEstado.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Activo", "Inactivo" }));
        cbEstado.setMinimumSize(new java.awt.Dimension(84, 20));
        cbEstado.setPreferredSize(new java.awt.Dimension(84, 20));
        jPanel2.add(cbEstado, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 310, -1, 30));

        lstTelefonos.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lstTelefonos.setForeground(new java.awt.Color(31, 97, 141));
        lstTelefonos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lstTelefonosMousePressed(evt);
            }
        });
        jScrollPane4.setViewportView(lstTelefonos);

        jPanel2.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 210, 123, 85));

        jSeparator1.setBackground(new java.awt.Color(31, 97, 141));
        jPanel2.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 340, 288, 10));

        jSeparator2.setBackground(new java.awt.Color(31, 97, 141));
        jPanel2.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 270, 130, 10));

        jSeparator3.setBackground(new java.awt.Color(31, 97, 141));
        jPanel2.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, 288, 10));

        jSeparator4.setBackground(new java.awt.Color(31, 97, 141));
        jPanel2.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 288, 10));

        jSeparator5.setBackground(new java.awt.Color(31, 97, 141));
        jPanel2.add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 130, 295, 10));

        jSeparator6.setBackground(new java.awt.Color(31, 97, 141));
        jPanel2.add(jSeparator6, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 70, 140, 10));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/der_1.png"))); // NOI18N
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5MouseClicked(evt);
            }
        });
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 240, 30, 30));

        btnCambioID.setText("Cambio");
        btnCambioID.setToolTipText("Cambio de Identificación");
        btnCambioID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCambioIDActionPerformed(evt);
            }
        });
        jPanel2.add(btnCambioID, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 20, -1, 50));

        jTabbedPane1.addTab("Datos Basicos", jPanel2);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tbl_cotizaciones.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tbl_cotizaciones.setForeground(new java.awt.Color(26, 82, 118));
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
        if (tbl_cotizaciones.getColumnModel().getColumnCount() > 0) {
            tbl_cotizaciones.getColumnModel().getColumn(0).setResizable(false);
            tbl_cotizaciones.getColumnModel().getColumn(1).setResizable(false);
            tbl_cotizaciones.getColumnModel().getColumn(2).setResizable(false);
            tbl_cotizaciones.getColumnModel().getColumn(3).setResizable(false);
        }

        jPanel4.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 52, 651, 350));

        btnAgregarCotizaciones.setBackground(new java.awt.Color(0, 0, 204));
        btnAgregarCotizaciones.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/add.png"))); // NOI18N
        btnAgregarCotizaciones.setToolTipText("Agregar Cotización");
        btnAgregarCotizaciones.setBorder(null);
        btnAgregarCotizaciones.setBorderPainted(false);
        btnAgregarCotizaciones.setContentAreaFilled(false);
        btnAgregarCotizaciones.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAgregarCotizaciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarCotizacionesActionPerformed(evt);
            }
        });
        jPanel4.add(btnAgregarCotizaciones, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 10, 30, 30));

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(26, 82, 118));
        jLabel14.setText("El tratamiento no incluye aparatologia adicional");
        jPanel4.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 350, -1));

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
        jPanel4.add(btnImprimirCotizaciones, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 10, 30, 30));

        jTabbedPane1.addTab("Cotizaciones", jPanel4);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tbl_tratamientos.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tbl_tratamientos.setForeground(new java.awt.Color(26, 82, 118));
        tbl_tratamientos.setModel(new javax.swing.table.DefaultTableModel(
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
        tbl_tratamientos.setGridColor(new java.awt.Color(255, 255, 255));
        tbl_tratamientos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tbl_tratamientosMousePressed(evt);
            }
        });
        tbl_tratamientos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbl_tratamientosKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbl_tratamientosKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_tratamientos);

        jPanel3.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 41, 654, 321));

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(26, 82, 118));
        jLabel15.setText("El tratamiento no incluye aparatologia adicional");
        jPanel3.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 350, -1));

        jTabbedPane1.addTab("Tratamientos", jPanel3);

        jPanel1.add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 680, 401));

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/guardar.png"))); // NOI18N
        btnGuardar.setToolTipText("Guardar");
        btnGuardar.setBorderPainted(false);
        btnGuardar.setContentAreaFilled(false);
        btnGuardar.setMargin(new java.awt.Insets(2, 10, 2, 8));
        btnGuardar.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/guardar_over.png"))); // NOI18N
        btnGuardar.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/guardar_over.png"))); // NOI18N
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });
        jPanel6.add(btnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 64, 64));

        btnModificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/modificar.png"))); // NOI18N
        btnModificar.setToolTipText("Modificar");
        btnModificar.setBorderPainted(false);
        btnModificar.setContentAreaFilled(false);
        btnModificar.setMargin(new java.awt.Insets(2, 14, 2, 5));
        btnModificar.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/modificar_over.png"))); // NOI18N
        btnModificar.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/modificar_over.png"))); // NOI18N
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });
        jPanel6.add(btnModificar, new org.netbeans.lib.awtextra.AbsoluteConstraints(64, 0, 64, 64));

        btnDescartar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/descartar.png"))); // NOI18N
        btnDescartar.setToolTipText("Descartar");
        btnDescartar.setBorderPainted(false);
        btnDescartar.setContentAreaFilled(false);
        btnDescartar.setMargin(new java.awt.Insets(2, 10, 2, 8));
        btnDescartar.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/descartar_over.png"))); // NOI18N
        btnDescartar.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/descartar_over.png"))); // NOI18N
        btnDescartar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDescartarActionPerformed(evt);
            }
        });
        jPanel6.add(btnDescartar, new org.netbeans.lib.awtextra.AbsoluteConstraints(128, 0, 64, 64));

        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/eliminar.png"))); // NOI18N
        btnEliminar.setToolTipText("Eliminar");
        btnEliminar.setBorderPainted(false);
        btnEliminar.setContentAreaFilled(false);
        btnEliminar.setMargin(new java.awt.Insets(2, 10, 2, 8));
        btnEliminar.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/eliminar_over.png"))); // NOI18N
        btnEliminar.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/eliminar_over.png"))); // NOI18N
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });
        jPanel6.add(btnEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(192, 0, 64, 64));

        btnConsultar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/consultar.png"))); // NOI18N
        btnConsultar.setToolTipText("Consutar");
        btnConsultar.setBorderPainted(false);
        btnConsultar.setContentAreaFilled(false);
        btnConsultar.setMargin(new java.awt.Insets(2, 10, 2, 10));
        btnConsultar.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/consultar_over.png"))); // NOI18N
        btnConsultar.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/consultar_over.png"))); // NOI18N
        btnConsultar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConsultarActionPerformed(evt);
            }
        });
        jPanel6.add(btnConsultar, new org.netbeans.lib.awtextra.AbsoluteConstraints(256, 0, 64, 64));

        jPanel1.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 470, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 560, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnConsultarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConsultarActionPerformed
        ////TRAER EL BQD CITAS
        new ventanaBusquedaPacienteN(1, "TID:-:ID", "", this);
    }//GEN-LAST:event_btnConsultarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        int resp = JOptionPane.showConfirmDialog(this, "¿Esta seguro de eliminar este registro?");
        if(resp == JOptionPane.YES_OPTION){
            String id = txtId.getText();
            String tid = lista_tpoID.get(cbTid.getSelectedIndex());
            cbTid.getSelectedItem().toString();
            paciente.delPaciente(id, tid);
            PacienteEstadoFormulario(0);
        }
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnDescartarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDescartarActionPerformed
        PacienteEstadoFormulario(0);
    }//GEN-LAST:event_btnDescartarActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        PacienteEstadoFormulario(2);
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        if (!tipoId.equalsIgnoreCase("aux")) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Map<String, String> campos = new HashMap<String, String>();
            String id = txtId.getText();
            String tid = lista_tpoID.get(cbTid.getSelectedIndex());
           
            String noms = txtNombres.getText().trim();
            String apes = txtApellidos.getText().trim();
            String dir = txtDireccion.getText().trim();
            String sex = cbSexo.getSelectedItem().toString();
            if (jdFnac.getCalendar() == null) {
                JOptionPane.showMessageDialog(rootPane, "Por favor llenar el campo de fecha de nacimiento");
                jdFnac.requestFocusInWindow();
                return;
            }
            String fnac = sdf.format(jdFnac.getCalendar().getTime());
            String cor = txtCorreo.getText();
            String tel = "";
            for (int i = 0; i < modlistTelefonos.size(); i++) {
                tel += (tel.equals("") ? "" : "<>") + modlistTelefonos.getElementAt(i);
            }

            String est = cbEstado.getSelectedItem().toString();
            if (noms.equals("")) {
                JOptionPane.showMessageDialog(rootPane, "Por favor digite el nombre del empleado");
                return;
            }
            if (apes.equals("")) {
                JOptionPane.showMessageDialog(rootPane, "Por favor digite el apellido del empleado");
                return;
            }

            campos.put("id", id);
            campos.put("tid", tid);
            campos.put("noms", Utilidades.CodificarElemento(noms.toUpperCase()));
            campos.put("apes", Utilidades.CodificarElemento(apes.toUpperCase()));
            campos.put("dir", Utilidades.CodificarElemento(dir.toUpperCase()));
            campos.put("sex", (sex.charAt(0) == 'S' ? "M" : sex));
            campos.put("fnac", fnac);
            campos.put("cor", cor);
            campos.put("tel", tel);
            campos.put("est", est);

            ArrayList<String[]> listaCot = getDatosCotizaciones();
            ArrayList<String[]> listaTrat = getDatosTratamiento();

            if (listaTrat.size() == 0) {
                JOptionPane.showMessageDialog(rootPane, "Por favor ingrese por lo menos un tratamiento al paciente.");
                return;
            }
            if (listaCot == null) {
                JOptionPane.showMessageDialog(rootPane, "No esta permitido el ingreso de texto en la tabla de cotizaciones. Por favor verificar la información e intentelo nuevamente.");
                return;
            }
            if (listaTrat == null) {
                JOptionPane.showMessageDialog(rootPane, "No esta permitido el ingreso de texto en la tabla de tratamientos. Por favor verificar la información e intentelo nuevamente.");
                return;
            }
            paciente.setPaciente(campos, listaCot, listaTrat);
            PacienteEstadoFormulario(0);
        } else {
            guardarPacDePacAux();
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void cbSexoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbSexoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbSexoActionPerformed

    private void txtDireccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDireccionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDireccionActionPerformed

    private void cbTidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbTidActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbTidActionPerformed

    private void txtIdKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIdKeyPressed
        if (evt.getKeyCode() == evt.VK_ENTER) {
            enterID = 1;
//            ConsultarPaciente();
            if (!tipoId.equalsIgnoreCase("aux")) {
                ConsultarPaciente(0);
            }
        }
    }//GEN-LAST:event_txtIdKeyPressed

    private void txtIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtIdFocusLost
        System.out.println("enterID----->" + enterID);
        if (!txtId.getText().equals("")) {
            if (enterID == 0) {
                lostID = 1;
//                ConsultarPaciente();
                if (!tipoId.equalsIgnoreCase("aux")) {
                    ConsultarPaciente(0);
                }
            } else {
                enterID = 0;
            }
        }
    }//GEN-LAST:event_txtIdFocusLost

    private void btnAgregarCotizacionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarCotizacionesActionPerformed
        //////Agregar Cotizacion 
        //new ventanaBusquedaTratamientos(1, "ID:-:TRATAMIENTO", "", this);
        new VentanaCotizaciones(this, -1).setVisible(true);
    }//GEN-LAST:event_btnAgregarCotizacionesActionPerformed

    private void tbl_cotizacionesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_cotizacionesMouseClicked
        int fila = tbl_cotizaciones.getSelectedRow();
        int cola = tbl_cotizaciones.getSelectedColumn();
        String dato = "" + tbl_cotizaciones.getValueAt(fila, cola);
        if (cola == 6) {//QUITAR

            if (dato.toUpperCase().equals("QUITAR")) {
                modeloCotizacion.removeRow(fila);
                tbl_cotizaciones.setModel(modeloCotizacion);
                //System.out.println("fila-->"+fila);
                listaCotizaciones.remove(fila);
            }
        }
        if (cola == 5) {//ACTIVAR
            if (dato.toUpperCase().equals("ACTIVAR")) {
                System.out.println("**********************ACTIVAR***********************");
                String[] info = listaCotizaciones.get(fila);
//                for(int i =0; i < info.length; i++){
//                    System.out.println("info["+i+"]-->"+info[i]);
//                }
                tbl_cotizaciones.setValueAt("ACTIVADO", fila, cola);//.getValueAt(fila, cola)
                tbl_cotizaciones.setValueAt("", fila, 6);
                String ctot = "" + tbl_cotizaciones.getValueAt(fila, 1);
                String cini = "" + tbl_cotizaciones.getValueAt(fila, 2);
                String cuota = "" + tbl_cotizaciones.getValueAt(fila, 4);
                String dif = "" + tbl_cotizaciones.getValueAt(fila, 3);
                boolean val = ValidarTratamiento(info[0]);
//                System.out.println("****************val***"+val+"***********ctot--->"+ctot+"**cini--->"+cini+"***cuota--->"+cuota+"");

                if (!ctot.equals("") && !cini.equals("") && !cuota.equals("") && !val) {
                    //datos ---> idtra 0, tra 1, costot 2, cuotaI 3, cuota 4,  
////                    System.out.println("ENTRE");
                    AgregarTratamiento(new String[]{info[0], info[1], ctot, cini, dif, cuota, "0", "" + fila, "", "", "", ""});
                }
            }
        }
    }//GEN-LAST:event_tbl_cotizacionesMouseClicked

    public boolean ValidarTratamiento(String id) {
        boolean ret = false;
        int rows = tbl_tratamientos.getRowCount();
        for (int i = 0; i < rows; i++) {
            String estado = "" + tbl_tratamientos.getValueAt(i, 5);
            if (listaTratamiento.get(i)[0].equals(id) && estado.equals("Activo")) {
                ret = true;
            }
        }
        return ret;
    }

    private void tbl_cotizacionesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbl_cotizacionesKeyPressed
        System.out.println("*********************tbl_cotizacionesKeyPressed********************");
        int fila = tbl_cotizaciones.getSelectedRow();
        int cola = tbl_cotizaciones.getSelectedColumn();
        String dato = "" + tbl_cotizaciones.getValueAt(fila, cola);
        System.out.println("dato--->" + dato);
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            System.out.println("cola--Cotizaciones-->" + dato);
            System.out.println("fila-->" + fila);
            System.out.println("cola-->" + cola);
            if (cola >= 1 && cola <= 3) {
                String valor = dato.replaceAll(".", "");
                boolean cond = exp.validarSoloNumeros(valor);
                if (!cond) {
                    tbl_cotizaciones.setValueAt("", fila, cola);
//                    tbl_cotizaciones.requestFocus();  
//                    tbl_cotizaciones.editCellAt(fila, cola);
                }
            }
            System.out.println("*********************END tbl_cotizacionesKeyPressed**********" + dato + "**********");
        }
    }//GEN-LAST:event_tbl_cotizacionesKeyPressed

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

    private void tbl_tratamientosMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_tratamientosMousePressed
        if (evt.getClickCount() == 2) {
            int fila = tbl_tratamientos.getSelectedRow();
            int cola = tbl_tratamientos.getSelectedColumn();
            String dato = "" + tbl_tratamientos.getValueAt(fila, cola);
            String tid = lista_tpoID.get(cbTid.getSelectedIndex());
            cbTid.getSelectedItem().toString();
            String id = tid + txtId.getText();
            String idTra = "", idcons = "", est = "";
            Map<String, String> campos = new HashMap<String, String>();

            if (cola == 6 && listaTratamiento.get(fila)[6].equals("1")) {
                System.out.println("HOLAAAAAAAAAAAAAAAAAA");
                obs = listaTratamiento.get(fila)[11];
                idTra = listaTratamiento.get(fila)[0];
                idcons = listaTratamiento.get(fila)[10];
                est = listaTratamiento.get(fila)[8];
                campos.put("id", id);
                campos.put("idTra", idTra);
                campos.put("idcons", idcons);
                campos.put("est", est);
                campos.put("obs", obs);

                new ventanaObsPac(this, campos);

            }
            
            

            if (cola == 1) {
                System.out.println("estoy modificando ");
//                    if (!datosUsuario.datos.get(0)[1].equals("2")) {
                new VentanaModificarTrata(this, fila).setVisible(true);
//                    }

            }

        } else if (evt.getClickCount() == 1) {
            int fila = tbl_tratamientos.getSelectedRow();
            int cola = tbl_tratamientos.getSelectedColumn();
            System.out.println("f-->" + fila + "---c-->" + cola);
            System.out.print("usuario activo" + datosUsuario.datos.get(0)[1]);
            String dato = "" + tbl_tratamientos.getValueAt(fila, cola);

            if (cola == 7 && dato.toUpperCase().equals("QUITAR")) {
                System.out.println("datos-->" + listaTratamiento.get(fila).length);
                System.out.println("datos-FILA ->" + listaTratamiento.get(fila)[7]);
                tbl_cotizaciones.setValueAt("Activar", Integer.parseInt(listaTratamiento.get(fila)[7]), 5);
                tbl_cotizaciones.setValueAt("Quitar", Integer.parseInt(listaTratamiento.get(fila)[7]), 6);
                modeloTratamientos.removeRow(fila);
                tbl_tratamientos.setModel(modeloTratamientos);
//                System.out.println("fila-->"+fila);
                listaTratamiento.remove(fila);

            }
            //     0             1         2       3         4        5    6   7    8         9       10        11
            //TRATAMIENTO, DESCRIP_TRAT, COSTO, CUOTA_I, DIFERIDAS, CUOTA, 1, '', ESTADO, FECHA_I, CONSECUTIVO, OBS
            if(cola == 7 && dato.equals("ELIMINAR")){
                int r = JOptionPane.showConfirmDialog(this, "¿Esta seguro de Eliminar este registro?");
                if(r == JOptionPane.YES_OPTION){
                    String id = txtId.getText();
                    String tid = lista_tpoID.get(cbTid.getSelectedIndex());
                    String resp = paciente.delTratamiento(listaTratamiento.get(fila)[10], listaTratamiento.get(fila)[0], tid+id);
                    if(resp.equals("1")){
                        modeloTratamientos.removeRow(fila);
                        tbl_tratamientos.setModel(modeloTratamientos);
                        listaTratamiento.remove(fila); 
                    }
                }
            }
        }


    }//GEN-LAST:event_tbl_tratamientosMousePressed

    private void txtTelefonoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelefonoKeyReleased
        Expresiones.procesarSoloNumeros(txtTelefono);
    }//GEN-LAST:event_txtTelefonoKeyReleased

    private void txtCorreoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCorreoFocusLost
        if (!txtCorreo.getText().equals("")) {
            if (Expresiones.validarCorreo(txtCorreo)) {
                txtTelefono.requestFocus();
            }
        }
    }//GEN-LAST:event_txtCorreoFocusLost

    private void tbl_tratamientosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbl_tratamientosKeyReleased
        int fila = tbl_tratamientos.getSelectedRow();
        int cola = tbl_tratamientos.getSelectedColumn();

        String valor = "" + tbl_tratamientos.getValueAt(fila, cola);
        String dato = Expresiones.procesarSoloNum(valor);
        tbl_tratamientos.setValueAt("" + dato, fila, cola);
    }//GEN-LAST:event_tbl_tratamientosKeyReleased

    private void tbl_cotizacionesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbl_cotizacionesKeyReleased
        System.out.println("*********************tbl_cotizacionesKeyReleased********************");
        int fila = tbl_cotizaciones.getSelectedRow();
        int cola = tbl_cotizaciones.getSelectedColumn();

        String valor = "" + tbl_cotizaciones.getModel().getValueAt(fila, cola);
        System.out.println("valor--->" + valor);
        String valorsin = valor.indexOf(".") > -1 ? valor.replace(".", "") : valor;
        System.out.println("valorsin--->" + valorsin);
        String dato = Expresiones.procesarSoloNum(valorsin);
        System.out.println("dato---->" + dato);
        System.out.println("fila--->" + fila);
        System.out.println("cola--->" + cola);
        dato = Utilidades.MascaraMoneda(dato);

        if (cola > 0 && cola <= 3) {
            tbl_cotizaciones.setValueAt("" + dato, fila, cola);
        }
        System.out.println("*********************END tbl_cotizacionesKeyReleased********************");

    }//GEN-LAST:event_tbl_cotizacionesKeyReleased

    private void txtIdKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIdKeyReleased
        Expresiones.ProcesarSoloNumerosSinPUNTOS(txtId);
    }//GEN-LAST:event_txtIdKeyReleased

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
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
    }//GEN-LAST:event_jLabel5MouseClicked

    private void jLabel10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10MouseClicked
        this.dispose();
    }//GEN-LAST:event_jLabel10MouseClicked

    private void jLabel11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel11MouseClicked
        this.setState(VistaPaciente.ICONIFIED);
    }//GEN-LAST:event_jLabel11MouseClicked

    private void jPanel5MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel5MousePressed
        x = evt.getX();
        y = evt.getY();
    }//GEN-LAST:event_jPanel5MousePressed

    private void jPanel5MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel5MouseDragged
        this.setLocation(this.getLocation().x + evt.getX() - x, this.getLocation().y + evt.getY() - y);
    }//GEN-LAST:event_jPanel5MouseDragged

    private void tbl_tratamientosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbl_tratamientosKeyPressed
        int fila = tbl_tratamientos.getSelectedRow();
        int cola = tbl_tratamientos.getSelectedColumn();

        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            evt.setKeyCode(java.awt.event.KeyEvent.VK_TAB);
            {
                //este codigo lo que hace es convertir el enter en tab
            }
        }
    }//GEN-LAST:event_tbl_tratamientosKeyPressed

    private void tbl_cotizacionesMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_cotizacionesMousePressed
        int fila = tbl_cotizaciones.getSelectedRow();
        int cola = tbl_cotizaciones.getSelectedColumn();
        String estado = "" + tbl_cotizaciones.getModel().getValueAt(fila, 5);
        String accion = "" + tbl_cotizaciones.getModel().getValueAt(fila, 6);
        if (evt.getClickCount() == 2) {
            
            //----------listaCotizaciones
            //    0             1         2       3         4        5      6         7
            //TRATAMIENTO, DESCRP_TRAT, COSTO, COUTA_I, DIFERIDAS, COUTA, ESTADO, CONSECUTIVO
            if (!estado.equals("ACTIVADO")) {
                new VentanaCotizaciones(this, fila).setVisible(true);
            }else if(cola == 1){
                if (!datosUsuario.datos.get(0)[1].equals("2")) {
                    System.out.println("listaCotizaciones.get(fila)[7]--->"+listaCotizaciones.get(fila)[7]);
                    new VentanaCotizaciones(this, fila, 1, listaCotizaciones.get(fila)[7]).setVisible(true);
                }
            }
            
//            if (cola == 1) {
//                System.out.println("estoy modificando ");
////                    if (!datosUsuario.datos.get(0)[1].equals("2")) {
//                new VentanaModificarTrata(this, fila).setVisible(true);
////                    }
//
//            }
        } else if (evt.getClickCount() == 1) {
            if(cola == 6 && accion.equals("ELIMINAR")){
                    int r = JOptionPane.showConfirmDialog(this, "¿Esta seguro de Eliminar este registro?");
                    if(r == JOptionPane.YES_OPTION){
                        String id = txtId.getText();
                        String tid = lista_tpoID.get(cbTid.getSelectedIndex());
                        paciente.delCotizacion(listaCotizaciones.get(fila)[7], listaCotizaciones.get(fila)[0], tid+id);
                        modeloCotizacion.removeRow(fila);
                        tbl_cotizaciones.setModel(modeloCotizacion);
                        listaCotizaciones.remove(fila);
                    }
               }   
        }
    }//GEN-LAST:event_tbl_cotizacionesMousePressed

    private void btnImprimirCotizacionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirCotizacionesActionPerformed
        // TODO add your handling code here:
        //JOptionPane.showMessageDialog(this, "NO HAS HECHO ESTO");
        String id = txtId.getText();
        String tid = lista_tpoID.get(cbTid.getSelectedIndex());
        String noms = txtNombres.getText().trim();
        String apes = txtApellidos.getText().trim();
        Map<String, String> list = new HashMap<String, String>();
        list.put("id", "" + tid + id);
        list.put("nombre", "" + noms + " " + apes);
        list.put("tipo", "0");

        String archivo = plantilla.Generarinformes(list);

    }//GEN-LAST:event_btnImprimirCotizacionesActionPerformed

    private void btnCambioIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCambioIDActionPerformed
        // TODO add your handling code here:
        new VentanaCambioID(this).setVisible(true);
    }//GEN-LAST:event_btnCambioIDActionPerformed

    private boolean estaSeleccionado(DefaultListModel modelo, String valor) {
        for (int i = 0; i < modelo.size(); i++) {
            if (modelo.getElementAt(i).equals(valor)) {
                return true;
            }
        }
        return false;
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
            java.util.logging.Logger.getLogger(VistaPaciente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VistaPaciente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VistaPaciente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VistaPaciente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VistaPaciente(null).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarCotizaciones;
    private javax.swing.JButton btnCambioID;
    private javax.swing.JButton btnConsultar;
    private javax.swing.JButton btnDescartar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnImprimirCotizaciones;
    private javax.swing.JButton btnModificar;
    private javax.swing.JComboBox cbEstado;
    private javax.swing.JComboBox cbSexo;
    public javax.swing.JComboBox cbTid;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JTabbedPane jTabbedPane1;
    public com.toedter.calendar.JDateChooser jdFnac;
    public javax.swing.JLabel lblId;
    private javax.swing.JLabel lblTid;
    public javax.swing.JLabel lblTipo;
    private javax.swing.JList lstTelefonos;
    private javax.swing.JTable tbl_cotizaciones;
    private javax.swing.JTable tbl_tratamientos;
    private javax.swing.JTextField txtApellidos;
    private javax.swing.JTextField txtCorreo;
    private javax.swing.JTextField txtDireccion;
    public javax.swing.JTextField txtId;
    private javax.swing.JTextField txtNombres;
    private javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables

    private void PacienteEstadoFormulario(int opc) {
        PacienteEstadoBotones(opc);
        switch (opc) {
            case 0: {//DESCARTAR
                txtId.setText("");
                cbTid.setSelectedIndex(0);
                txtNombres.setText("");
                txtApellidos.setText("");
                txtDireccion.setText("");
                jdFnac.setCalendar(null);
                cbSexo.setSelectedIndex(0);
                txtTelefono.setText("");
                //lstTelefonos.removeAll();
                txtCorreo.setText("");
                cbEstado.setSelectedIndex(0);
                ////
                txtId.setEnabled(true);
                cbTid.setEnabled(true);
                txtNombres.setEnabled(false);
                txtApellidos.setEnabled(false);
                txtDireccion.setEnabled(false);
                txtTelefono.setEnabled(false);
                txtCorreo.setEnabled(false);
                cbSexo.setEnabled(false);
                jdFnac.setEnabled(false);
                cbEstado.setEnabled(false);
                LimpiarTblTrat();
                LimpiartblCot();
                LimpiarLstTel();
                btnCambioID.setVisible(false);
                btnAgregarCotizaciones.setEnabled(false);
                cbTid.requestFocus();
                jTabbedPane1.setEnabledAt(1, false);
                jTabbedPane1.setEnabledAt(2, false);
                jTabbedPane1.setSelectedIndex(0);
                break;
            }
            case 1: {
                txtId.setEnabled(false);
                cbTid.setEnabled(false);
                txtNombres.setEnabled(true);
                txtApellidos.setEnabled(true);
                txtDireccion.setEnabled(true);
                txtTelefono.setEnabled(true);
                txtCorreo.setEnabled(true);
                cbSexo.setEnabled(true);
                jdFnac.setEnabled(true);
                cbEstado.setEnabled(true);
                btnAgregarCotizaciones.setEnabled(true);
                jTabbedPane1.setEnabledAt(1, true);
                jTabbedPane1.setEnabledAt(2, true);
                txtNombres.requestFocus();
                break;
            }
            case 2: {
                txtId.setEnabled(false);
                cbTid.setEnabled(false);
                txtNombres.setEnabled(true);
                txtApellidos.setEnabled(true);
                txtDireccion.setEnabled(true);
                txtTelefono.setEnabled(true);
                txtCorreo.setEnabled(true);
                cbSexo.setEnabled(true);
                jdFnac.setEnabled(true);
                cbEstado.setEnabled(true);
                jTabbedPane1.setEnabledAt(1, true);
                jTabbedPane1.setEnabledAt(2, true);
                btnAgregarCotizaciones.setEnabled(true);
                break;
            }
            case 3: {
                txtId.setEnabled(false);
                cbTid.setEnabled(false);
                txtNombres.setEnabled(false);
                txtApellidos.setEnabled(false);
                txtDireccion.setEnabled(false);
                txtTelefono.setEnabled(false);
                txtCorreo.setEnabled(false);
                cbSexo.setEnabled(false);
                jdFnac.setEnabled(false);
                cbEstado.setEnabled(false);
                btnCambioID.setVisible(true);
                btnAgregarCotizaciones.setEnabled(false);
                jTabbedPane1.setEnabledAt(1, false);
                jTabbedPane1.setEnabledAt(2, false);
                break;
            }
            case 4: {//para pacientes auxiliares
                txtId.setEnabled(true);
                cbTid.setEnabled(true);
                txtNombres.setEnabled(true);
                txtApellidos.setEnabled(true);
                txtDireccion.setEnabled(true);
                txtTelefono.setEnabled(true);
                txtCorreo.setEnabled(true);
                cbSexo.setEnabled(true);
                jdFnac.setEnabled(true);
                cbEstado.setEnabled(true);
                btnCambioID.setVisible(false);
                btnAgregarCotizaciones.setEnabled(false);
                jTabbedPane1.setEnabledAt(1, true);
                jTabbedPane1.setEnabledAt(2, true);
                break;
            }
        }
    }

    public void PacienteEstadoBotones(int opc) {
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
                btnConsultar.setEnabled(false);
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
            case 4: {//PACIENTE AUX
                btnGuardar.setEnabled(true);
                btnModificar.setEnabled(false);
                btnDescartar.setEnabled(true);
                btnEliminar.setEnabled(false);
                btnConsultar.setEnabled(false);
                break;
            }
        }
    }

    //metodo anterior
    public void ConsultarPaciente() {
        String id = txtId.getText();
        String tid = "";

        if (!tipoId.equals("")) {
            EstadoTIdentificacion();
            tid = tipoId;
            tipoId = "";
        } else {
            tid = lista_tpoID.get(cbTid.getSelectedIndex());
        }

        if (id.equals("")) {
            JOptionPane.showMessageDialog(rootPane, "Por favor digite una identificación");
            //txtId.requestFocusInWindow();                       
            return;
        }

        if (tid.equals("Seleccionar")) {
            JOptionPane.showMessageDialog(rootPane, "Por favor escoja un tipo de identificación");
            cbTid.requestFocusInWindow();
            return;
        }
        boolean emp = (paciente.getPersonal(id, tid).size() > 0);
        if (emp) {
            JOptionPane.showMessageDialog(rootPane, "La identificación digitada ya se encuentra registrada para un empleado. \nPor favor verifique la información e intentelo nuevamente.");
            txtId.setText("");
            //txtId.requestFocusInWindow();
            return;
        }
        ArrayList<String[]> Info = new ArrayList<>();
        ArrayList<String[]> lst_cot = new ArrayList<>();

        Info = paciente.getPaciente(id, tid);
        System.out.println("info-->" + Info.size());
        //RECORRO LISTA CON INFO
        if (Info.size() > 0) {
            String[] datos = Info.get(0);
            txtNombres.setText(datos[0]);
            txtApellidos.setText(datos[1]);
            txtDireccion.setText(datos[2]);
            System.out.println("dato-->" + datos[3]);
            boolean bol = datos[3] != null && !datos[3].equals("") && !datos[3].equals("null");
            boolean bol2 = datos[3].equals("null");
            System.out.println("bol-->" + bol);
            System.out.println("bol2-->" + bol2);
            if (bol) {
                System.out.println("ENTRO DATO 3--->" + datos[3]);
                jdFnac.setCalendar(formatString(datos[3]));
            }

            cbSexo.setSelectedItem(datos[4]);
            System.out.println("datos[5]------>" + datos[5] + "%%%%%");
            String[] tels = (datos[5].equals("") ? null : datos[5].split("<>"));
            if (tels != null) {
                System.out.println("tels--->" + tels.length);
                for (int i = 0; i < tels.length; i++) {
                    modlistTelefonos.addElement(tels[i]);
                }
                lstTelefonos.setModel(modlistTelefonos);
            }
            //txtTelefono.setText(datos[5]);
            ///txtCelular.setText(datos[6]);
            txtCorreo.setText(datos[6]);
            cbEstado.setSelectedItem(datos[7]);

            lst_cot = paciente.getPacienteCotizaciones(id, tid);
            if (lst_cot.size() > 0) {
                AgregarCotizacion(lst_cot);
            }
            LlenarTablaTratamiento();

            PacienteEstadoFormulario(3);
        } else {
            PacienteEstadoFormulario(1);
        }
    }

    //metodo a modificar
    public void ConsultarPaciente(int a) {
        String id = txtId.getText();
        String tid = "";

        if (!tipoId.equals("")) {
            EstadoTIdentificacion();
            tid = tipoId;
            tipoId = "";
        } else {
            tid = lista_tpoID.get(cbTid.getSelectedIndex());
        }

        if (id.equals("")) {
            JOptionPane.showMessageDialog(rootPane, "Por favor digite una identificación");
            //txtId.requestFocusInWindow();                       
            return;
        } else {

            if (tid.equals("Seleccionar")) {
                JOptionPane.showMessageDialog(rootPane, "Por favor escoja un tipo de identificación");
                cbTid.requestFocusInWindow();
                return;
            }
            boolean emp = (paciente.getPersonal(id, tid).size() > 0);
            if (emp) {
                JOptionPane.showMessageDialog(rootPane, "La identificación digitada ya se encuentra registrada para un empleado. \nPor favor verifique la información e intentelo nuevamente.");
                txtId.setText("");
                //txtId.requestFocusInWindow();
                return;
            }
            ArrayList<String[]> Info = new ArrayList<>();
            ArrayList<String[]> lst_cot = new ArrayList<>();

            Info = paciente.getPaciente(id, tid);
            System.out.println("info-->" + Info.size());
            //RECORRO LISTA CON INFO
            if (Info.size() > 0) {
                String[] datos = Info.get(0);
                txtNombres.setText(datos[0]);
                txtApellidos.setText(datos[1]);
                txtDireccion.setText(datos[2]);
                System.out.println("dato-->" + datos[3]);
                boolean bol = datos[3] != null && !datos[3].equals("") && !datos[3].equals("null");
                boolean bol2 = datos[3].equals("null");
                System.out.println("bol-->" + bol);
                System.out.println("bol2-->" + bol2);
                if (bol) {
                    System.out.println("ENTRO DATO 3--->" + datos[3]);
                    jdFnac.setCalendar(formatString(datos[3]));
                }

                cbSexo.setSelectedItem(datos[4]);
                System.out.println("datos[5]------>" + datos[5] + "%%%%%");
                String[] tels = (datos[5].equals("") ? null : datos[5].split("<>"));
                if (tels != null) {
                    System.out.println("tels--->" + tels.length);
                    for (int i = 0; i < tels.length; i++) {
                        modlistTelefonos.addElement(tels[i]);
                    }
                    lstTelefonos.setModel(modlistTelefonos);
                }
                //txtTelefono.setText(datos[5]);
                ///txtCelular.setText(datos[6]);
                txtCorreo.setText(datos[6]);
                cbEstado.setSelectedItem(datos[7]);

                lst_cot = paciente.getPacienteCotizaciones(id, tid);
                if (lst_cot.size() > 0) {
                    AgregarCotizacion(lst_cot);
                }
                LlenarTablaTratamiento();

                PacienteEstadoFormulario(3);
            } else {
                PacienteEstadoFormulario(1);
            }
        }
    }

    public void LlenarTablaTratamiento() {
        String id = txtId.getText();
        String tid = lista_tpoID.get(cbTid.getSelectedIndex());
        ArrayList<String[]> lst_trat = new ArrayList<>();

        lst_trat = paciente.getPacienteTratamientos(id, tid);

        if (lst_trat.size() > 0) {

            AgregarTratamiento(lst_trat);
        }
    }

    private Calendar formatString(String f) {
        Date fecha = new Date(Integer.parseInt(f.split("/")[2]) - 1900, Integer.parseInt(f.split("/")[1]) - 1, Integer.parseInt(f.split("/")[0]));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        return cal;
    }

    ///////////////////7
    public void AgregarCotizacion() {

        listaCotizaciones.add(datos);
        //DATOS 0-->idTrat , 1--> TRAT, 2--> CostoTotal, 3-->CuotaIni, 4-->diferidas, 5 --> cuota, 6-->(0 si es nuevo, 1 si ya esta en la tabla), 7-->(VACIO)   
        /////////////////////////////////  
        agregarFilaCotizacion(new String[]{datos[1], datos[2], datos[3], datos[4], datos[5], "Activar", "Quitar"});
        LimpiarDatos();
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

    public void AgregarCotizacion(ArrayList<String[]> lista) {
        System.out.println("***************AgregarCotizacion CON*****************");
        for (String[] lst : lista) {
            
            listaCotizaciones.add(lst);
            boolean b = true;
            if (b) {
                for (int i = 0; i < lst.length; i++) {
                    System.out.println("i-->" + i + " -- " + lst[i]);
                }
                b = false;
            }
            if (!tipoId.equalsIgnoreCase("aux")) {
                agregarFilaCotizacion(new String[]{lst[1], Utilidades.MascaraMoneda(lst[2]),
                    Utilidades.MascaraMoneda(lst[3]), lst[4],
                    Utilidades.MascaraMoneda(lst[5]), "ACTIVADO", "ELIMINAR"});
            } else {
                agregarFilaCotizacion(new String[]{lst[1], Utilidades.MascaraMoneda(lst[2]),
                    Utilidades.MascaraMoneda(lst[3]), lst[4],
                    Utilidades.MascaraMoneda(lst[5]), "Activar", "Quitar"});
            }
            //LimpiarDatos();        
        }
        
        System.out.println("***************END AgregarCotizacion CON*****************");
    }

    private void cargarDatosTablaCotizacion(String[] nombreColumnas, ArrayList<String[]> datos) {
        modeloCotizacion = new DefaultTableModel(nombreColumnas, 0) {
            Class[] types = new Class[]{
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int col) {
                return types[col];
            }
            boolean[] edit = {false, true, true, true, false};

            public boolean isCellEditable(int row, int col) {
                //System.out.println("row-->"+row+" -- col-->"+col+"::::: "+edit[col]);
                return edit[col];
            }
        };

        if (datos != null) {
            for (int i = 0; i < datos.size(); i++) {
                agregarFilaCotizacion(datos.get(i));
            }
        }
        tbl_cotizaciones.setModel(modeloCotizacion);
    }

    private void agregarFilaCotizacion(String[] fila) {
        modeloCotizacion.addRow(fila);
    }

    private void LimpiarDatos() {
        datos = null;
    }

    ///////////////   
    public void ActualizarTratamiento(int fila) {
        listaTratamiento.set(fila, datos);

        tbl_tratamientos.setValueAt("" + datos[1], fila, 0);//Tratamiento
        tbl_tratamientos.setValueAt("" + datos[2], fila, 1);//Costo
        tbl_tratamientos.setValueAt("" + datos[3], fila, 2);//Cuota inicial
        tbl_tratamientos.setValueAt("" + datos[4], fila, 3);//diferidas en 
        tbl_tratamientos.setValueAt("" + datos[5], fila, 4);//Cuotas

        LimpiarDatos();
    }

    public void AgregarTratamiento(String[] info) {
        System.out.println("*********************AgregarTratamiento* STRING[]*************************+");
        for (int i = 0; i < info.length; i++) {
            System.out.println("i-->" + i + " -- " + info[i]);
        }
        listaTratamiento.add(info);
        //datos ---> idtra 0, tra 1, costot 2, cuotaI 3, cuota 4,    
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String fecha = sdf.format(cal.getTime());
        String Estado = "Activo";
        info[8] = Estado;
        info[9] = fecha;
        /////////////////////////////////    
        agregarFilaTratamiento(new String[]{info[1], info[2], info[3], info[4], info[5], "" + fecha, "" + Estado, "Quitar"});
    }

    public void AgregarTratamiento(ArrayList<String[]> lista) {

        LimpiarTblTrat();
        System.out.println("*********************AgregarTratamiento***ARRAY***********************+");
        for (String[] lst : lista) {
            boolean b = true;
            if (b) {
                for (int i = 0; i < lst.length; i++) {
                    System.out.println("i-->" + i + " -- " + lst[i]);
                }
                b = false;
            }
            listaTratamiento.add(lst);
            listaTratamientoAux.add(lst);

            //datos ---> idtra 0, tra 1, costot 2, cuotaI 3, cuota 4,    
            String fecha = lst[9];
            String Estado = lst[8];
            /////////////////////////////////    
            //     0             1         2       3         4        5    6   7    8         9       10        11
            //TRATAMIENTO, DESCRIP_TRAT, COSTO, CUOTA_I, DIFERIDAS, CUOTA, 1, '', ESTADO, FECHA_I, CONSECUTIVO, OBS
            agregarFilaTratamiento(new String[]{lst[1], Utilidades.MascaraMoneda(lst[2]), Utilidades.MascaraMoneda(lst[3]), lst[4], Utilidades.MascaraMoneda(lst[5]), "" + fecha, "" + Estado, "ELIMINAR"});

        }
    }

    private void agregarFilaTratamiento(String[] fila) {
        modeloTratamientos.addRow(fila);
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
                System.out.println("listaCotizaciones.get("+i+")[7]----"+listaCotizaciones.get(i)[7]+"//");
//                System.out.println("**"+listaCotizaciones.get(i)[0]+"---"+ctot+"____"+ cini+"****"+cuota+"---"+listaCotizaciones.get(i)[2]+"_________");
                ret.add(new String[]{listaCotizaciones.get(i)[0], ctot.replace(".", ""), cini.replace(".", ""), cuota.replace(".", ""),
                    listaCotizaciones.get(i)[6], listaCotizaciones.get(i)[7], dif});

            }
//            System.out.println("**********END ****************"+ret.size());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("");
        }
        return ret;
    }

    private ArrayList<String[]> getDatosTratamiento() {
        ArrayList<String[]> ret = new ArrayList<>();
        try {
            int tam = tbl_tratamientos.getRowCount();
            System.out.println("*****************getDatosTratamiento*****************" + tam);
            for (int i = 0; i < tam; i++) {
                String ctot = "" + tbl_tratamientos.getValueAt(i, 1);
                String cini = "" + tbl_tratamientos.getValueAt(i, 2);
                String cuota = "" + tbl_tratamientos.getValueAt(i, 4);
                String est = "" + tbl_tratamientos.getValueAt(i, 6);
                String fec = "" + tbl_tratamientos.getValueAt(i, 5);
                String dif = "" + tbl_tratamientos.getValueAt(i, 3);
                System.out.println("fec-->" + fec);
                String fech = fec.split("/")[2] + "-" + fec.split("/")[1] + "-" + fec.split("/")[0];
                System.out.println("fech-->" + fech);
                String opc = "0";
                //sdf.format(jdFnac.getCalendar().getTime());                
//                System.out.println("**"+listaTratamiento.get(i)[0]+"---"+ctot+"____"+ cini+"****"+cuota+"---"+listaTratamiento.get(i)[5]+"_________");
                System.out.println("ctot.replace(\".\", \"\")-->" + ctot.replace(".", ""));
                //System.out.println("listaTratamientoAux.get(i)[2]-->"+listaTratamientoAux.get(i)[2]);
                if (listaTratamientoAux.size() > 0 && i < listaTratamientoAux.size()) {
                    if (!ctot.replace(".", "").equals(listaTratamientoAux.get(i)[2])) {
                        opc = "1";
                    }
                }

                ret.add(new String[]{listaTratamiento.get(i)[0], ctot.replace(".", ""), cini.replace(".", ""), cuota.replace(".", ""),
                    listaTratamiento.get(i)[6], est, fech, listaTratamiento.get(i)[10], dif, "" + opc});

            }
            System.out.println("**********END ****************" + ret.size());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("eror--getDatosTratamiento->" + e.toString());
        }
        return ret;
    }

    private void LimpiartblCot() {
        int tam = tbl_cotizaciones.getRowCount();
        listaCotizaciones.clear();
        System.out.println("tam-COTIZACION->" + tam);
        for (int i = 0; i < tam; i++) {
            modeloCotizacion.removeRow(0);
            tbl_cotizaciones.setModel(modeloCotizacion);
        }
    }

    private void LimpiarTblTrat() {
        int tam = tbl_tratamientos.getRowCount();
        listaTratamiento.clear();
        listaTratamientoAux.clear();
        for (int i = 0; i < tam; i++) {
            modeloTratamientos.removeRow(0);

        }
        tbl_tratamientos.setModel(modeloTratamientos);
    }

    private void LimpiarLstTel() {
        int tam = modlistTelefonos.size();

        modlistTelefonos.removeAllElements();
        lstTelefonos.setModel(modlistTelefonos);
    }

    private void EstadoTIdentificacion() {
        System.out.println("******************EstadoTIdentificacion***************" + tipoId + "**********");
        for (int i = 0; i < lista_tpoID.size(); i++) {
            System.out.println("lista_tpoID.get(" + i + ")-->" + lista_tpoID.get(i));
            if (lista_tpoID.get(i).equals(tipoId)) {
                cbTid.setSelectedIndex(i);
                break;
            }
        }
        System.out.println("**********************END EstadoTIdentificacion*************************");
    }

    private void cargarDatosPacienteAux(String id) {
        gestorMySQL gsql = new gestorMySQL();
        ArrayList<String[]> datosPacAux = new ArrayList<>();
        String consulta = "SELECT\n"
                + "TRIM(CONCAT(TRIM(pa.primer_nombre),' ',TRIM(pa.segundo_nombre))) nombres,\n"
                + "TRIM(CONCAT(TRIM(pa.primer_apellido),' ',TRIM(pa.segundo_apellido))) apellidos,\n"
                + "pa.email correo,\n"
                + "REPLACE(pa.telefono,'<>','-') telefono\n"
                + "FROM\n"
                + "paciente_auxiliar pa\n"
                + "WHERE\n"
                + "pa.estado IN('Activo')\n"
                + "AND pa.tipo IN('A')\n"
                + "AND pa.pk_paciente_auxiliar = " + id;
        datosPacAux = gsql.SELECT(consulta);

        if (datosPacAux.size() > 0) {
            for (int i = 0; i < datosPacAux.size(); i++) {
                System.out.println("DATO 1: " + datosPacAux.get(0)[0]);
                System.out.println("DATO 2: " + datosPacAux.get(0)[1]);
                System.out.println("DATO 3: " + datosPacAux.get(0)[2]);
            }

            txtNombres.setText(datosPacAux.get(0)[0]);
            txtApellidos.setText(datosPacAux.get(0)[1]);
            txtCorreo.setText(datosPacAux.get(0)[2]);
            txtDireccion.setText("");
            cbEstado.setSelectedItem(0);
            cbSexo.setSelectedItem(0);

            String[] tels = datosPacAux.get(0)[3].split("-");
            if (tels != null) {
                System.out.println("tels >>> " + tels.length);
                for (int i = 0; i < tels.length; i++) {
                    modlistTelefonos.addElement(tels[i]);
                }
                lstTelefonos.setModel(modlistTelefonos);
            }

            jdFnac.setCalendar(Calendar.getInstance());
        } else {
            System.out.println("no existen datos del paciente auxiliar " + id);
        }

    }

    public void ConsultarPacienteAuxiliar() {
        cargarDatosPacienteAux(idPacAux);

        ArrayList<String[]> lst_cot = new ArrayList<>();
        lst_cot = paciente.getCotizacionesPacienteAuxiliar(idPacAux);

        if (lst_cot.size() > 0) {
            AgregarCotizacion(lst_cot);
        }
        PacienteEstadoFormulario(4);
    }

    private void guardarPacDePacAux() {
        if (cbTid.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un tipo de documento.");
            cbTid.requestFocusInWindow();
            return;
        }

        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe escribir el número de identificación del paciente.");
            txtId.requestFocusInWindow();
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, String> campos = new HashMap<String, String>();
        String id = txtId.getText();
        String tid = lista_tpoID.get(cbTid.getSelectedIndex());
        cbTid.getSelectedItem().toString();
        String noms = txtNombres.getText().trim();
        String apes = txtApellidos.getText().trim();
        String dir = txtDireccion.getText().trim();
        String sex = cbSexo.getSelectedItem().toString();
        if (jdFnac.getCalendar() == null) {
            JOptionPane.showMessageDialog(rootPane, "Por favor llenar el campo de fecha de nacimiento");
            jdFnac.requestFocusInWindow();
            return;
        }
        String fnac = sdf.format(jdFnac.getCalendar().getTime());
        String cor = txtCorreo.getText();
        String tel = "";
        for (int i = 0; i < modlistTelefonos.size(); i++) {
            tel += (tel.equals("") ? "" : "<>") + modlistTelefonos.getElementAt(i);
        }

        String est = cbEstado.getSelectedItem().toString();
        if (noms.equals("")) {
            JOptionPane.showMessageDialog(rootPane, "Por favor digite el nombre del empleado");
            return;
        }
        if (apes.equals("")) {
            JOptionPane.showMessageDialog(rootPane, "Por favor digite el apellido del empleado");
            return;
        }

        campos.put("id", id);
        campos.put("tid", tid);
        campos.put("noms", Utilidades.CodificarElemento(noms.toUpperCase()));
        campos.put("apes", Utilidades.CodificarElemento(apes.toUpperCase()));
        campos.put("dir", Utilidades.CodificarElemento(dir.toUpperCase()));
        campos.put("sex", (sex.charAt(0) == 'S' ? "M" : sex));
        campos.put("fnac", fnac);
        campos.put("cor", cor);
        campos.put("tel", tel);
        campos.put("est", est);

        ArrayList<String[]> listaCot = getDatosCotizaciones();
        ArrayList<String[]> listaTrat = getDatosTratamiento();

        if (listaTrat.size() == 0) {
            JOptionPane.showMessageDialog(rootPane, "Por favor ingrese por lo menos un tratamiento al paciente.");
            return;
        }
        if (listaCot == null) {
            JOptionPane.showMessageDialog(rootPane, "No esta permitido el ingreso de texto en la tabla de cotizaciones. Por favor verificar la información e intentelo nuevamente.");
            return;
        }
        if (listaTrat == null) {
            JOptionPane.showMessageDialog(rootPane, "No esta permitido el ingreso de texto en la tabla de tratamientos. Por favor verificar la información e intentelo nuevamente.");
            return;
        }
        if (tipoId.equalsIgnoreCase("aux")) {
            campos.put("idaux", idPacAux);
            paciente.setPacienteDePacienteAux(campos, listaCot, listaTrat);
        } else {
            paciente.setPaciente(campos, listaCot, listaTrat);
        }

        PacienteEstadoFormulario(0);
    }

}
