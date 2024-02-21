/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;

import BaseDeDatos.gestorMySQL;
import Busquedas.ventanaBusquedaPacFact;
import Busquedas.ventanaBusquedacConceptoFactura;
import Control.ControlConcepto;
import Control.ControlFacturas;
import Informes.DescripcionFactura;
import Informes.Descripcioninformespdf;
import Modelo.Usuario;
import Utilidades.Expresiones;
import Utilidades.Utilidades;
import Utilidades.datosUsuario;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

/**
 *
 * @author 10
 */
public class ventanaConsultarF extends javax.swing.JFrame {

    private gestorMySQL gsql;
    private String estadoch;
    public Control.ControlConcepto concfact;
    public Control.ControlFacturas cntlcfact;
    public DefaultTableModel modelo;
    public int filaseleccionada = 0;
    public ArrayList<String[]> datosConcepto = new ArrayList<>();
    public String[] nombreColumnas = {"Código", "Concepto", "Cantidad", "Valor Unitario", "Valor Total", "Acción"};
    public String modopg;
    public Date fecha;
    public ArrayList<String> Insert = new ArrayList<String>();
    public String d = "";
    public String TipoP = "";
    public String pk_c = "";
    public String pkcita = "";
    public String[] datos;
    Utilidades utls = new Utilidades();
    public ArrayList<String[]> listafact = new ArrayList<>();
    public Descripcioninformespdf informe;
    private int x, y;
    public ArrayList<int[]> posiciones = new ArrayList<>();
    public static ArrayList<String[]> ListaConceptos = new ArrayList<>();

    /**
     * Creates new form ventanaConsultarF
     */
    public void IniciarTabla() {
        modelo = new DefaultTableModel(nombreColumnas, 0) {
            Class[] types = new Class[]{
                java.lang.Object.class,
                java.lang.Object.class,
                java.lang.Object.class,
                java.lang.Object.class,
                java.lang.Object.class,
                java.lang.Object.class
            };

            public Class getColumnClass(int col) {
                return types[col];
            }
            boolean[] edit = {false, false, false, false, false, false};

            public boolean isCellEditable(int row, int col) { 
                //System.out.println("row-->"+row+" -- col-->"+col+"::::: "+edit[col]);
                if (getFiladeuda(row)) {
                    return false;
                } else {
                    return edit[col];
                }
            }

        };

        tblConcepto.setModel(modelo);

        tblConcepto.getColumnModel().getColumn(0).setPreferredWidth(3);
        tblConcepto.getColumnModel().getColumn(1).setPreferredWidth(200);
        tblConcepto.getColumnModel().getColumn(2).setPreferredWidth(10);
        tblConcepto.getColumnModel().getColumn(3).setPreferredWidth(20);
        tblConcepto.getColumnModel().getColumn(4).setPreferredWidth(20);
        tblConcepto.getColumnModel().getColumn(5).setPreferredWidth(20);
        JTableHeader header = tblConcepto.getTableHeader();
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < modelo.getColumnCount(); i++) {
            tblConcepto.getColumnModel().getColumn(i).setResizable(false);
            DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
            if (i == 0 || i == 2 || i == 5) {
                tcr.setHorizontalAlignment(SwingConstants.CENTER);
            } else if (i == 1) {
                tcr.setHorizontalAlignment(SwingConstants.LEFT);
            } else {
                tcr.setHorizontalAlignment(SwingConstants.RIGHT);
            }

            tblConcepto.getColumnModel().getColumn(i).setCellRenderer(tcr);
//            tblListaCitas.getColumnModel().getColumn(i).setHeaderRenderer(thcr);
        }
    }

    public ventanaConsultarF() {
        initComponents();
        Utilidades.EstablecerIcono(this);
        this.setLocationRelativeTo(null); // centrar la ventana 
        concfact = new ControlConcepto();

        posiciones.add(new int[]{270, 410});
        posiciones.add(new int[]{270, 450});
        posiciones.add(new int[]{190, 418});
        posiciones.add(new int[]{190, 460});
        ListaConceptos = new ArrayList<>();
        IniciarTabla();
        lblDireccion.setText("");
        lblTelefono.setText("");
        gsql = new gestorMySQL();
        this.modopg = modopg;
        rbEfectivo.setSelected(true);
        mostrarCampoEfec(true);
        txtnombre.setEnabled(false);
        txtapellidos.setEnabled(false);
        txtdocumento.setEnabled(false);
        combotipoDoc.setEnabled(false);

    }
    
    public void EstadoPaciente(){
        txtdocumento.setEnabled(false);
        combotipoDoc.setEnabled(false);
    }

    public ventanaConsultarF(String idpaciente, String nombres, String apellidos, String estadoch, String pkcita) {
        initComponents();
        Utilidades.EstablecerIcono(this);
        this.setLocationRelativeTo(null); // centrar la ventana 
        concfact = new ControlConcepto();
        modelo = new DefaultTableModel(nombreColumnas, 0);
        ListaConceptos = new ArrayList<>();
        posiciones.add(new int[]{270, 410});
        posiciones.add(new int[]{270, 450});
        posiciones.add(new int[]{190, 418});
        posiciones.add(new int[]{190, 460});
        IniciarTabla();
        btnDescartat.setEnabled(false);
        btnbuscarPaciente.setEnabled(false);
        cntlcfact = new ControlFacturas();
        gsql = new gestorMySQL();
        this.modopg = modopg;
        this.pkcita = pkcita;
        rbEfectivo.setSelected(true);
        mostrarCampoEfec(true);
        lblDireccion.setText("");
        lblTelefono.setText("");
        CargarDatosPaciente(idpaciente, estadoch);
        listaFact(idpaciente);    

        System.out.println("*****estadoch******+"+estadoch);
        System.out.println("*****d******+"+d);
        if(d.equals("O")){
            EstadoPaciente();
        }
        
        txtnombre.setEnabled(false);
    }

    public void listaFact(String id) {
        System.out.println("*************listaFact*************");
        ArrayList<String[]> con = new ArrayList<>();
        String sql = "SELECT numero,`fecha_pago` FROM facturas WHERE `pfk_paciente`='" + id + "' AND `estado`='pagado'";
        con = gsql.SELECT(sql);
        System.out.println("sql --"+sql);
        listafact = new ArrayList<>();
        for (int i = 0; i < con.size(); i++) { // recorre el array
            listafact.add(new String[]{con.get(i)[0], con.get(i)[1]});
        }

    }
    
    private boolean getFiladeuda(int row) {

        if (ListaConceptos.get(row)[7].equals("")) {
            return false;
        } else {
            return true;
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

        grupoModoPago = new javax.swing.ButtonGroup();
        lblPnombre = new javax.swing.JLabel();
        lblSnombre = new javax.swing.JLabel();
        lblSapellido = new javax.swing.JLabel();
        lblPapellido = new javax.swing.JLabel();
        lblDocumento = new javax.swing.JLabel();
        lblidConcepto = new javax.swing.JLabel();
        lblTelefono = new javax.swing.JLabel();
        lblDireccion = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        lbltipoDocumento = new javax.swing.JLabel();
        combotipoDoc = new javax.swing.JComboBox();
        lblnombre = new javax.swing.JLabel();
        txtnombre = new javax.swing.JTextField();
        lbldocumento = new javax.swing.JLabel();
        txtdocumento = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtapellidos = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblConcepto = new javax.swing.JTable();
        rbTarjeta = new javax.swing.JRadioButton();
        rbEfectivo = new javax.swing.JRadioButton();
        rbTaryefec = new javax.swing.JRadioButton();
        lblCambio = new javax.swing.JLabel();
        lblEfectivo = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();
        txtEfectivo = new javax.swing.JTextField();
        txtCambio = new javax.swing.JTextField();
        lblTarjeta = new javax.swing.JLabel();
        txtPtarjeta = new javax.swing.JTextField();
        btnbuscarConcepto = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        btnGuardarfact = new javax.swing.JButton();
        btnDescartat = new javax.swing.JButton();
        btnbuscarPaciente = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        btnvisualizarFactura = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        btnRecalcularTratamientos = new javax.swing.JButton();

        lblPnombre.setText("jLabel1");

        lblSnombre.setText("jLabel1");

        lblSapellido.setText("jLabel1");

        lblPapellido.setText("jLabel1");

        lblDocumento.setText("jLabel1");

        lblTelefono.setText("jLabel1");

        lblDireccion.setText("jLabel1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Facturación");
        setBackground(new java.awt.Color(255, 255, 255));
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 82, 118)));
        jPanel1.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        jPanel1.setPreferredSize(new java.awt.Dimension(858, 507));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbltipoDocumento.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbltipoDocumento.setForeground(new java.awt.Color(21, 67, 96));
        lbltipoDocumento.setText("Tipo de Documento");
        jPanel1.add(lbltipoDocumento, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, -1, -1));

        combotipoDoc.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Seleccionar", "CC", "CE", "TI", "RC", "NIT", "NUI", "PAS" }));
        jPanel1.add(combotipoDoc, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 150, 30));

        lblnombre.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblnombre.setForeground(new java.awt.Color(21, 67, 96));
        lblnombre.setText("Nombres");
        jPanel1.add(lblnombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 40, -1, -1));

        txtnombre.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtnombre.setBorder(null);
        txtnombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtnombreActionPerformed(evt);
            }
        });
        txtnombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtnombreKeyReleased(evt);
            }
        });
        jPanel1.add(txtnombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 60, 320, 30));

        lbldocumento.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbldocumento.setForeground(new java.awt.Color(21, 67, 96));
        lbldocumento.setText("Documento");
        jPanel1.add(lbldocumento, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, -1, -1));

        txtdocumento.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtdocumento.setBorder(null);
        txtdocumento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtdocumentoKeyReleased(evt);
            }
        });
        jPanel1.add(txtdocumento, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 180, 30));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(21, 67, 96));
        jLabel3.setText("Apellidos ");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 100, -1, -1));

        txtapellidos.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtapellidos.setBorder(null);
        txtapellidos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtapellidosActionPerformed(evt);
            }
        });
        txtapellidos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtapellidosKeyReleased(evt);
            }
        });
        jPanel1.add(txtapellidos, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 120, 320, 30));

        tblConcepto.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tblConcepto.setForeground(new java.awt.Color(21, 67, 96));
        tblConcepto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Concepto", "Cantidad", "Valor Unitario", "Valor Total", "Elinimar Celda"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblConcepto.setGridColor(new java.awt.Color(21, 67, 96));
        tblConcepto.setSelectionBackground(new java.awt.Color(41, 128, 185));
        tblConcepto.getTableHeader().setReorderingAllowed(false);
        tblConcepto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblConceptoMouseClicked(evt);
            }
        });
        tblConcepto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblConceptoKeyReleased(evt);
            }
        });
        tblConcepto.addVetoableChangeListener(new java.beans.VetoableChangeListener() {
            public void vetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {
                tblConceptoVetoableChange(evt);
            }
        });
        jScrollPane2.setViewportView(tblConcepto);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, 860, 250));

        rbTarjeta.setBackground(new java.awt.Color(255, 255, 255));
        grupoModoPago.add(rbTarjeta);
        rbTarjeta.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        rbTarjeta.setForeground(new java.awt.Color(21, 67, 96));
        rbTarjeta.setText("Tarjeta ");
        rbTarjeta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rbTarjetaMouseClicked(evt);
            }
        });
        rbTarjeta.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rbTarjetaStateChanged(evt);
            }
        });
        rbTarjeta.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rbTarjetaItemStateChanged(evt);
            }
        });
        rbTarjeta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbTarjetaActionPerformed(evt);
            }
        });
        rbTarjeta.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                rbTarjetaFocusLost(evt);
            }
        });
        jPanel1.add(rbTarjeta, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 460, -1, -1));

        rbEfectivo.setBackground(new java.awt.Color(255, 255, 255));
        grupoModoPago.add(rbEfectivo);
        rbEfectivo.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        rbEfectivo.setForeground(new java.awt.Color(21, 67, 96));
        rbEfectivo.setText("Efectivo");
        rbEfectivo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rbEfectivoMouseClicked(evt);
            }
        });
        rbEfectivo.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rbEfectivoStateChanged(evt);
            }
        });
        rbEfectivo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rbEfectivoItemStateChanged(evt);
            }
        });
        rbEfectivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbEfectivoActionPerformed(evt);
            }
        });
        jPanel1.add(rbEfectivo, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 430, -1, 20));

        rbTaryefec.setBackground(new java.awt.Color(255, 255, 255));
        grupoModoPago.add(rbTaryefec);
        rbTaryefec.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        rbTaryefec.setForeground(new java.awt.Color(21, 67, 96));
        rbTaryefec.setText("Tarjeta y Efectivo");
        rbTaryefec.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rbTaryefecMouseClicked(evt);
            }
        });
        rbTaryefec.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rbTaryefecStateChanged(evt);
            }
        });
        rbTaryefec.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rbTaryefecItemStateChanged(evt);
            }
        });
        rbTaryefec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbTaryefecActionPerformed(evt);
            }
        });
        rbTaryefec.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                rbTaryefecKeyReleased(evt);
            }
        });
        jPanel1.add(rbTaryefec, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 490, -1, -1));

        lblCambio.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblCambio.setForeground(new java.awt.Color(21, 67, 96));
        lblCambio.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCambio.setText("CAMBIO $");
        jPanel1.add(lblCambio, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 480, 70, 20));

        lblEfectivo.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblEfectivo.setForeground(new java.awt.Color(21, 67, 96));
        lblEfectivo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblEfectivo.setText("EFECTIVO $");
        jPanel1.add(lblEfectivo, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 480, -1, -1));

        lblTotal.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblTotal.setForeground(new java.awt.Color(21, 67, 96));
        lblTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotal.setText("TOTAL $");
        jPanel1.add(lblTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 440, 60, -1));

        txtTotal.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtTotal.setForeground(new java.awt.Color(21, 67, 96));
        txtTotal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTotalKeyReleased(evt);
            }
        });
        jPanel1.add(txtTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 430, 150, 30));

        txtEfectivo.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtEfectivo.setForeground(new java.awt.Color(21, 67, 96));
        txtEfectivo.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtEfectivoCaretUpdate(evt);
            }
        });
        txtEfectivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEfectivoActionPerformed(evt);
            }
        });
        txtEfectivo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtEfectivoFocusLost(evt);
            }
        });
        txtEfectivo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtEfectivoKeyReleased(evt);
            }
        });
        jPanel1.add(txtEfectivo, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 470, 150, 30));

        txtCambio.setEditable(false);
        txtCambio.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtCambio.setForeground(new java.awt.Color(21, 67, 96));
        jPanel1.add(txtCambio, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 470, 150, 30));

        lblTarjeta.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblTarjeta.setForeground(new java.awt.Color(21, 67, 96));
        lblTarjeta.setText("TARJETA $");
        jPanel1.add(lblTarjeta, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 440, -1, -1));

        txtPtarjeta.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtPtarjeta.setForeground(new java.awt.Color(21, 67, 96));
        txtPtarjeta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPtarjetaActionPerformed(evt);
            }
        });
        txtPtarjeta.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPtarjetaFocusLost(evt);
            }
        });
        txtPtarjeta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPtarjetaKeyReleased(evt);
            }
        });
        jPanel1.add(txtPtarjeta, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 430, 150, 30));

        btnbuscarConcepto.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        btnbuscarConcepto.setForeground(new java.awt.Color(21, 67, 96));
        btnbuscarConcepto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/agregar_1.png"))); // NOI18N
        btnbuscarConcepto.setToolTipText("Agregar Concepto");
        btnbuscarConcepto.setBorder(null);
        btnbuscarConcepto.setBorderPainted(false);
        btnbuscarConcepto.setContentAreaFilled(false);
        btnbuscarConcepto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbuscarConceptoActionPerformed(evt);
            }
        });
        jPanel1.add(btnbuscarConcepto, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 140, 30, 30));

        jPanel7.setBackground(new java.awt.Color(26, 82, 118));
        jPanel7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel7MousePressed(evt);
            }
        });
        jPanel7.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jPanel7MouseDragged(evt);
            }
        });
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/minimizar.png"))); // NOI18N
        jLabel12.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel12MouseClicked(evt);
            }
        });
        jPanel7.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 0, 30, 30));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/cerrar_1.png"))); // NOI18N
        jLabel6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel6MouseClicked(evt);
            }
        });
        jPanel7.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 0, 30, 30));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Gestionar Factura");
        jPanel7.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 8, 152, -1));

        jPanel1.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 890, 30));
        jPanel1.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, 180, 10));
        jPanel1.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 90, 320, 10));
        jPanel1.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 150, 320, 10));

        btnGuardarfact.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/guardar.png"))); // NOI18N
        btnGuardarfact.setToolTipText("Guardar Factura");
        btnGuardarfact.setBorderPainted(false);
        btnGuardarfact.setContentAreaFilled(false);
        btnGuardarfact.setPreferredSize(new java.awt.Dimension(80, 60));
        btnGuardarfact.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnGuardarfactMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnGuardarfactMousePressed(evt);
            }
        });
        btnGuardarfact.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarfactActionPerformed(evt);
            }
        });
        jPanel1.add(btnGuardarfact, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 530, 70, -1));

        btnDescartat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/descartar.png"))); // NOI18N
        btnDescartat.setToolTipText("Atras");
        btnDescartat.setBorderPainted(false);
        btnDescartat.setContentAreaFilled(false);
        btnDescartat.setPreferredSize(new java.awt.Dimension(80, 60));
        btnDescartat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDescartatActionPerformed(evt);
            }
        });
        jPanel1.add(btnDescartat, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 530, 70, -1));

        btnbuscarPaciente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/consultar.png"))); // NOI18N
        btnbuscarPaciente.setToolTipText("Agregar Paciente");
        btnbuscarPaciente.setBorderPainted(false);
        btnbuscarPaciente.setContentAreaFilled(false);
        btnbuscarPaciente.setPreferredSize(new java.awt.Dimension(80, 60));
        btnbuscarPaciente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbuscarPacienteActionPerformed(evt);
            }
        });
        jPanel1.add(btnbuscarPaciente, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 530, 63, -1));

        jPanel2.setBackground(new java.awt.Color(26, 82, 118));

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Relación de pagos ");
        jButton1.setToolTipText("Relación de pagos ");
        jButton1.setBorderPainted(false);
        jButton1.setContentAreaFilled(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 40, 90, 90));

        jPanel3.setBackground(new java.awt.Color(26, 82, 118));

        btnvisualizarFactura.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnvisualizarFactura.setForeground(new java.awt.Color(255, 255, 255));
        btnvisualizarFactura.setText("Visualizar Facturas");
        btnvisualizarFactura.setToolTipText("Visualizar Facturas");
        btnvisualizarFactura.setBorderPainted(false);
        btnvisualizarFactura.setContentAreaFilled(false);
        btnvisualizarFactura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnvisualizarFacturaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnvisualizarFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 90, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnvisualizarFactura, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 40, 90, 90));

        jPanel4.setBackground(new java.awt.Color(26, 82, 118));

        btnRecalcularTratamientos.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnRecalcularTratamientos.setForeground(new java.awt.Color(255, 255, 255));
        btnRecalcularTratamientos.setText("Recalcular");
        btnRecalcularTratamientos.setToolTipText("Recalcular Tratamientos");
        btnRecalcularTratamientos.setBorderPainted(false);
        btnRecalcularTratamientos.setContentAreaFilled(false);
        btnRecalcularTratamientos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRecalcularTratamientosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnRecalcularTratamientos, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnRecalcularTratamientos, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 40, 90, 90));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 890, 600));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnbuscarPacienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbuscarPacienteActionPerformed
//        if (chkPacAux.isSelected()) {
//            estadoch = "1";
//            //combotipoDoc.enable(false);
//        } else {
//            estadoch = "0";
//        }

        limpiarVentanaFactura();
        new ventanaBusquedaPacFact(this, estadoch).setVisible(true);

    }//GEN-LAST:event_btnbuscarPacienteActionPerformed

    private void txtapellidosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtapellidosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtapellidosActionPerformed

    private void btnbuscarConceptoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbuscarConceptoActionPerformed
//        new ventanaBusquedacConceptoFactura(this).setVisible(true);
//        new Busquedas.ventanaBusquedacConceptoFactura(this).setVisible(true);
        System.out.println("tipo--->"+TipoP);
        if(!TipoP.equals("")){
            ArrayList<String> listac = getConceptosTabla();
            new VentanaAdicionarConceptos(this, -1, listac).setVisible(true);
        }
    }//GEN-LAST:event_btnbuscarConceptoActionPerformed

    private void tblConceptoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblConceptoKeyReleased
//        valorxcantidad();

        totalPago();
//        Eliminarfila(tblConcepto);
//        valorTratamiento();

    }//GEN-LAST:event_tblConceptoKeyReleased

    private void tblConceptoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblConceptoMouseClicked
        filaseleccionada = tblConcepto.getSelectedRow();
        String dato = "";
        int fila = tblConcepto.getSelectedRow();
        int cola = tblConcepto.getSelectedColumn();
        dato = (String) modelo.getValueAt(fila, 5);
        int clicks = evt.getClickCount();

        if (clicks == 1) {

            if (cola == 5 && !getFiladeuda(fila)) {

                if (dato.toUpperCase().equals("QUITAR")) {
                    ListaConceptos.remove(fila);
                    modelo.removeRow(fila);
                    tblConcepto.setModel(modelo);
                    System.out.println("fila-->" + fila);
                    totalPago();
                }
            }
        } else {
            if (!getFiladeuda(fila)) {
                new VentanaAdicionarConceptos(this, fila, null).setVisible(true);
            }
        }

//        valorTratamiento();
    }//GEN-LAST:event_tblConceptoMouseClicked

    private void rbEfectivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbEfectivoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbEfectivoActionPerformed

    private void rbTarjetaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rbTarjetaMouseClicked
        estadosradiobtn(1);

    }//GEN-LAST:event_rbTarjetaMouseClicked

    /**
     * Estados de los Radiobtn<BR>
     * 0= Efectivo<BR>
     * 1= Tarjeta<BR>
     * 2= TarYefectivo<BR>
     *
     * @param est
     */
    public void estadosradiobtn(int est) {
        if (est == 0) {///EFECTIVO
            txtTotal.setVisible(true);
            txtPtarjeta.setVisible(false);
            txtCambio.setVisible(true);
            txtEfectivo.setVisible(true);
            lblTotal.setVisible(true);
            lblCambio.setVisible(true);
            lblTarjeta.setVisible(false);
            lblEfectivo.setVisible(true);

        } else if (est == 1) {///TARJETA
            txtTotal.setVisible(true);
            txtPtarjeta.setVisible(true);
            txtCambio.setVisible(false);
            txtEfectivo.setVisible(false);
            lblTotal.setVisible(true);
            lblCambio.setVisible(false);
            lblTarjeta.setVisible(true);
            lblEfectivo.setVisible(false);

        } else if (est == 2) {///TAR Y EFECTIVO
            txtTotal.setVisible(true);
            txtPtarjeta.setVisible(true);
            txtCambio.setVisible(true);
            txtEfectivo.setVisible(true);
            lblTotal.setVisible(true);
            lblCambio.setVisible(true);
            lblTarjeta.setVisible(true);
            lblEfectivo.setVisible(true);

        }
    }

    private void rbEfectivoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rbEfectivoMouseClicked
//        mostrarCampoEfec(true);
        estadosradiobtn(0);
    }//GEN-LAST:event_rbEfectivoMouseClicked

    private void rbTaryefecMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rbTaryefecMouseClicked
        estadosradiobtn(2);
    }//GEN-LAST:event_rbTaryefecMouseClicked

    private void txtEfectivoCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtEfectivoCaretUpdate
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEfectivoCaretUpdate

    private void txtEfectivoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEfectivoKeyReleased
        String dato = txtEfectivo.getText();
        String pefect = dato.indexOf(".") > -1 ? dato.replace(".", "") : dato;
        String dat = Expresiones.procesarSoloNum(pefect);
        dat = Utilidades.MascaraMoneda(dat);
        txtEfectivo.setText(dat);
        //Expresiones.procesarSoloNumeros(txtEfectivo);
//        Utilidades.validarNumeroEncampodeTexto(txtEfectivo);
        calcularCambio(evt);

    }//GEN-LAST:event_txtEfectivoKeyReleased

    private void txtEfectivoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEfectivoFocusLost
        String dato = txtEfectivo.getText();
        String pefect = dato.indexOf(".") > -1 ? dato.replace(".", "") : dato;
        String dat = Expresiones.procesarSoloNum(pefect);
//        dat = Utilidades.MascaraMoneda(dat);
//        txtEfectivo.setText(dat);
        //Expresiones.procesarSoloNumeros(txtEfectivo);
        String tot = txtTotal.getText().replace(".", "");
        int efectivo = Integer.parseInt(pefect.equals("") ? "0" : pefect);
        int total = Integer.parseInt(tot.equals("") ? "0" : tot);
        int cambio = 0;

        if (modopg.equals("0")) {
            cambio = (efectivo - total);
            if (cambio >= 0) {
                txtCambio.setText("" + Utilidades.MascaraMoneda("" + cambio));
                txtCambio.setVisible(true);
                lblCambio.setVisible(true);
            } else {
                lblCambio.setVisible(false);
                txtCambio.setVisible(false);
            }
        } else if (modopg.equals("2")) {
            String ptarj = txtPtarjeta.getText().replace(".", "");
            int t = efectivo + Integer.parseInt(ptarj.equals("") ? "0" : ptarj);
            cambio = (t - total);
            System.out.println("totalfoc----- " + total);
            System.out.println("t-----  " + t);
            System.out.println("cambio focl-- " + cambio);
            if (cambio >= 0) {
                txtCambio.setText("" + Utilidades.MascaraMoneda("" + cambio));
                txtCambio.setVisible(true);
                lblCambio.setVisible(true);
            } else {
                txtCambio.setVisible(false);
                lblCambio.setVisible(false);
            }

        }
    }//GEN-LAST:event_txtEfectivoFocusLost

    private void rbTarjetaStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rbTarjetaStateChanged
        Establecermodopago();
    }//GEN-LAST:event_rbTarjetaStateChanged

    private void rbEfectivoStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rbEfectivoStateChanged
        Establecermodopago();
    }//GEN-LAST:event_rbEfectivoStateChanged

    private void rbTaryefecActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbTaryefecActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbTaryefecActionPerformed

    private void rbTaryefecStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rbTaryefecStateChanged
        Establecermodopago();
    }//GEN-LAST:event_rbTaryefecStateChanged

    private void rbTaryefecItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rbTaryefecItemStateChanged
        txtCambio.setText("");
        txtEfectivo.setText("");
    }//GEN-LAST:event_rbTaryefecItemStateChanged

    private void rbTarjetaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rbTarjetaItemStateChanged
        txtCambio.setText("");
        txtEfectivo.setText("");
    }//GEN-LAST:event_rbTarjetaItemStateChanged

    private void rbEfectivoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rbEfectivoItemStateChanged
        txtCambio.setText("");
        txtEfectivo.setText("");
    }//GEN-LAST:event_rbEfectivoItemStateChanged

    private void txtPtarjetaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPtarjetaKeyReleased
        String dato = txtPtarjeta.getText();
        String ptarj = dato.indexOf(".") > -1 ? dato.replace(".", "") : dato;
        String dat = Expresiones.procesarSoloNum(ptarj);
        dat = Utilidades.MascaraMoneda(dat);
        txtPtarjeta.setText(dat);
        String tot = txtTotal.getText().indexOf(".") > -1 ? txtTotal.getText().replace(".", "") : txtTotal.getText();
        //Expresiones.procesarSoloNumeros(txtPtarjeta);
//        Utilidades.validarNumeroEncampodeTexto(txtPtarjeta);
        int ptarjeta = Integer.parseInt(ptarj.equals("") ? "0" : ptarj);
        int total = Integer.parseInt(tot.equals("") ? "0" : tot);

        try {
            if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                if (modopg.equals("1")) {
                    if (ptarjeta < total) {
                        JOptionPane.showMessageDialog(null, "El valor de la tarjeta debe ser igual al total");
//                    txtPtarjeta.setText("" + total);
                        rbTaryefec.setSelected(true);
                        estadosradiobtn(2);
                    } else if (ptarjeta > total) {
                        JOptionPane.showMessageDialog(null, "El valor de la tarjeta debe ser igual al total");
                        txtPtarjeta.setText("" + Utilidades.MascaraMoneda("" + total));
                    } else {
                        rbTarjeta.setSelected(true);
                        estadosradiobtn(1);
                        System.out.println("estoy en el enter");
                        btnGuardarfact.requestFocusInWindow();
                    }
                } else {
                    if (ptarjeta > total) {
                        JOptionPane.showMessageDialog(null, "El valor de la tarjeta debe ser menor al total");
                        txtPtarjeta.setText("");
                    } else if (ptarjeta == total) {
                        rbTarjeta.setSelected(true);
                        estadosradiobtn(1);
                        btnGuardarfact.requestFocusInWindow();
                    } else {
                        txtEfectivo.setText("" + Utilidades.MascaraMoneda("" + (total - ptarjeta)));
                        txtEfectivo.requestFocusInWindow();

                    }
                }
            }
        } catch (Exception e) {

        }

    }//GEN-LAST:event_txtPtarjetaKeyReleased

    private void txtPtarjetaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPtarjetaFocusLost
        //Expresiones.procesarSoloNumeros(txtPtarjeta); // CAMBIAR POR EL QUE MANDO RICHAR Y VERIFICAR EL ERROR
        String ptarj = txtPtarjeta.getText().replace(".", "");
        String tot = txtTotal.getText().replace(".", "");

        int ptarjeta = Integer.parseInt(ptarj.equals("") ? "0" : ptarj);
        int total = Integer.parseInt(tot.equals("") ? "0" : tot);

        try {
            if (modopg.equals("1")) {
                if (ptarjeta < total) {
                    JOptionPane.showMessageDialog(null, "El valor de la tarjeta debe ser igual al total");
//                    txtPtarjeta.setText("" + total);
                    rbTaryefec.setSelected(true);
                    estadosradiobtn(2);
                } else if (ptarjeta > total) {
                    JOptionPane.showMessageDialog(null, "El valor de la tarjeta debe ser igual al total");
                    txtPtarjeta.setText("" + Utilidades.MascaraMoneda("" + total));
                } else {
                    rbTarjeta.setSelected(true);
                    estadosradiobtn(1);
                    System.out.println("estoy en el del foco");
                    btnGuardarfact.requestFocusInWindow();
                }
            } else {
                if (ptarjeta > total) {
                    JOptionPane.showMessageDialog(null, "El valor de la tarjeta debe ser menor al total");
                    txtPtarjeta.setText("");
                } else if (ptarjeta == total) {
                    rbTarjeta.setSelected(true);
                    estadosradiobtn(1);
                } else {
                    txtEfectivo.setText("" + (total - ptarjeta));
                    txtEfectivo.requestFocusInWindow();

                }
            }
        } catch (Exception e) {
        }


    }//GEN-LAST:event_txtPtarjetaFocusLost

    private void btnDescartatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDescartatActionPerformed
        limpiarVentanaFactura();
        txtnombre.setEnabled(false);
        txtapellidos.setEnabled(false);
        txtdocumento.setEnabled(false);
        combotipoDoc.setEnabled(false);
    }//GEN-LAST:event_btnDescartatActionPerformed

    private void rbTarjetaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rbTarjetaFocusLost
//        int ptarjeta = Integer.parseInt(txtPtarjeta.getText().equals("") ? "0" : txtPtarjeta.getText());
//        int total = Integer.parseInt(txtTotal.getText().equals("") ? "0" : txtTotal.getText());

//        if (modopg.equals("1")) {
////            if (ptarjeta < total || ptarjeta > total) {
//            if(txtPtarjeta.getText().equals("")){
//                JOptionPane.showMessageDialog(null, "ingrese valor en tarjeta");
//                txtPtarjeta.setText("" + total);
//            }
//        }

    }//GEN-LAST:event_rbTarjetaFocusLost

    private void rbTaryefecKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_rbTaryefecKeyReleased


    }//GEN-LAST:event_rbTaryefecKeyReleased

    private void txtdocumentoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtdocumentoKeyReleased
        Utilidades.validarNumeroEncampodeTexto(txtdocumento);

    }//GEN-LAST:event_txtdocumentoKeyReleased

    private void txtTotalKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTotalKeyReleased
        Utilidades.validarNumeroEncampodeTexto(txtTotal);
    }//GEN-LAST:event_txtTotalKeyReleased

    private void txtEfectivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEfectivoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEfectivoActionPerformed

    private void tblConceptoVetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {//GEN-FIRST:event_tblConceptoVetoableChange
        totalPago();
    }//GEN-LAST:event_tblConceptoVetoableChange

    private void rbTarjetaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbTarjetaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbTarjetaActionPerformed

    private void txtPtarjetaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPtarjetaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPtarjetaActionPerformed

    private void txtnombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtnombreActionPerformed

    }//GEN-LAST:event_txtnombreActionPerformed

    private void txtnombreKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtnombreKeyReleased
        Expresiones.procesarTextoSinNumeros(txtnombre);
    }//GEN-LAST:event_txtnombreKeyReleased

    private void txtapellidosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtapellidosKeyReleased
        Expresiones.procesarTextoSinNumeros(txtapellidos);
    }//GEN-LAST:event_txtapellidosKeyReleased

    private void btnvisualizarFacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnvisualizarFacturaActionPerformed
        if(d.equals("A")){
            JOptionPane.showMessageDialog(this, "El paciente Auxiliar no posee facturas generadas.");
            return ;
        }
        if (listafact.size() > 0) {
            String paciente = combotipoDoc.getSelectedItem() + txtdocumento.getText();
            new VentanavFacturas(listafact, paciente).setVisible(true);
        } else {
        }
    }//GEN-LAST:event_btnvisualizarFacturaActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        informe = new Descripcioninformespdf();
        String paciente = combotipoDoc.getSelectedItem() + txtdocumento.getText();
        if(d.equals("A")){
            JOptionPane.showMessageDialog(this, "No se puede generar una relación de pagos a un paciente auxiliar.");
        }

        if (!combotipoDoc.getSelectedItem().equals("Seleccionar") && !txtdocumento.getText().equals("")) {
            Map<String, String> list = new HashMap<String, String>();
            list.put("cat", "2");
            list.put("inf", "5");
            list.put("idpac", "" + paciente);
            System.out.println("----" + list.get("cat") + "&&&&&&" + list.get("inf") + "---" + list.get("idpac"));

            String archivo = informe.Generarinformes(list);

        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jLabel12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel12MouseClicked
        this.setState(ventanaConsultarF.ICONIFIED);
    }//GEN-LAST:event_jLabel12MouseClicked

    private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseClicked
        this.dispose();
    }//GEN-LAST:event_jLabel6MouseClicked

    private void jPanel7MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel7MousePressed
        x = evt.getX();
        y = evt.getY();
    }//GEN-LAST:event_jPanel7MousePressed

    private void jPanel7MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel7MouseDragged
        this.setLocation(this.getLocation().x + evt.getX() - x, this.getLocation().y + evt.getY() - y);
    }//GEN-LAST:event_jPanel7MouseDragged

    private void btnGuardarfactMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGuardarfactMousePressed
        if (evt.getClickCount() == 1) {
            if(getDatosConceptos().size()>0){
                int opc = JOptionPane.showConfirmDialog(this, "¿Esta seguro que desea generar la factura?", "Confirmación", JOptionPane.YES_NO_OPTION);
                if (opc == JOptionPane.YES_OPTION) {
                    GuardarNew();
                }
            }else{
                JOptionPane.showMessageDialog(this, "Por favor ingresar al menos un concepto para realizar la operación");
            }
        }
    }//GEN-LAST:event_btnGuardarfactMousePressed

    private void btnGuardarfactActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarfactActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnGuardarfactActionPerformed

    private void btnRecalcularTratamientosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRecalcularTratamientosActionPerformed
        String paciente = combotipoDoc.getSelectedItem() + txtdocumento.getText();
        if(d.equals("A")){
            JOptionPane.showMessageDialog(this, "No se puede generar una relación de pagos a un paciente auxiliar.");
        }

        if (!combotipoDoc.getSelectedItem().equals("Seleccionar") && !txtdocumento.getText().equals("")) {
            cntlcfact = new ControlFacturas();
            
            String ret = cntlcfact.SetRecalcularTratamientos(paciente);
            
            if(!ret.equals("")){
                JOptionPane.showMessageDialog(null, "La operación se Realizo Exitosamente.");
            }else{
                JOptionPane.showMessageDialog(null, "Ocurrio un error al momento de realizar la operación.");
            }

        }
    }//GEN-LAST:event_btnRecalcularTratamientosActionPerformed

    private void btnGuardarfactMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGuardarfactMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnGuardarfactMouseClicked

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
            java.util.logging.Logger.getLogger(ventanaConsultarF.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ventanaConsultarF.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ventanaConsultarF.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ventanaConsultarF.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ventanaConsultarF().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDescartat;
    private javax.swing.JButton btnGuardarfact;
    private javax.swing.JButton btnRecalcularTratamientos;
    private javax.swing.JButton btnbuscarConcepto;
    private javax.swing.JButton btnbuscarPaciente;
    private javax.swing.JButton btnvisualizarFactura;
    public javax.swing.JComboBox combotipoDoc;
    private javax.swing.ButtonGroup grupoModoPago;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    public javax.swing.JLabel lblCambio;
    public javax.swing.JLabel lblDireccion;
    public javax.swing.JLabel lblDocumento;
    public javax.swing.JLabel lblEfectivo;
    public javax.swing.JLabel lblPapellido;
    public javax.swing.JLabel lblPnombre;
    public javax.swing.JLabel lblSapellido;
    public javax.swing.JLabel lblSnombre;
    public javax.swing.JLabel lblTarjeta;
    public javax.swing.JLabel lblTelefono;
    public javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lbldocumento;
    public javax.swing.JLabel lblidConcepto;
    private javax.swing.JLabel lblnombre;
    private javax.swing.JLabel lbltipoDocumento;
    public javax.swing.JRadioButton rbEfectivo;
    public javax.swing.JRadioButton rbTarjeta;
    public javax.swing.JRadioButton rbTaryefec;
    public javax.swing.JTable tblConcepto;
    public javax.swing.JTextField txtCambio;
    public javax.swing.JTextField txtEfectivo;
    public javax.swing.JTextField txtPtarjeta;
    public javax.swing.JTextField txtTotal;
    public javax.swing.JTextField txtapellidos;
    public javax.swing.JTextField txtdocumento;
    public javax.swing.JTextField txtnombre;
    // End of variables declaration//GEN-END:variables

    public void conceptosxfactura(String Codigo, String nombre, String descripcion, String Valor, String Nfactura) {
        //                                                cant              tipo_concepto                 
        datos = new String[]{Codigo, nombre, descripcion, "1", Valor, Valor, "1", Nfactura};
        AgregarTablaConceptos();
    }

    private void cargarDatosTabla(String[] nombreColumnas, ArrayList<String[]> datos) {
        // modelo = new DefaultTableModel(nombreColumnas, 0);

        if (datos != null) {
            for (int i = 0; i < datos.size(); i++) {
                agregarFila(datos.get(i));
            }
        }

        //tblConcepto.setModel(modelo);
        totalPago();

//        modoPago();
    }

    private void agregarFila(String[] fila) {
        modelo.addRow(fila);
    }

    public void totalPago() {
        System.out.println("######### ");
        int Nfilas = tblConcepto.getRowCount();
        int total = 0;
        int valorxcant = 0;
        int cambio = 0;
        String dato = "";
        int valor = 0, cant = 0;
        String dt = "";
        Integer dtt = 0;  

        for (int i = 0; i < Nfilas; i++) {
            dt = (String) tblConcepto.getValueAt(i, 2);
            System.out.println("cant-->" + dt);

            cant = (dt.equals("") ? 0 : Integer.parseInt(dt));
            String val = (String) tblConcepto.getValueAt(i, 3);
//            System.out.println("valor-->"+tblConcepto.getValueAt(i, 3));
            System.out.println("valor-->" + val);
//            dt = (String) tblConcepto.getValueAt(i, 3);
            dt = val;
            System.out.println("valor-->" + dt);
            valor = (dt.isEmpty() ? 0 : Integer.parseInt(dt.replace(".", "")));
//            valor = dtt;

            total += cant * valor;
        }

        txtTotal.setText("" + Utilidades.MascaraMoneda("" + total));
        System.out.println("total-----" + total);
        txtTotal.setEditable(false);

    }

//    public void valorxcantidad() {
//        int filas = tblConcepto.getRowCount();
//        int valxcant = 0;
//        int valor = 2;
//        int nuevo = 0;
//        String dato1 = "";
//        String dato2 = "";
//        for (int i = 0; i < filas; i++) {
//            System.out.println("} " + i);
//            System.out.println("______> " + filaseleccionada);
//            if (filaseleccionada == i) {
//
//                dato1 = (String) modelo.getValueAt(i, 1);
//                System.out.println("dato que trae"+ dato1);
//                System.out.println("DATO QUE TRAE 2"+ dato2);
//                dato2 = (String) modelo.getValueAt(i, 2);
//                valxcant = (dato1.equals("") ? 0 : Integer.parseInt(dato1)) * (dato2.equals("") ? 0 : Integer.parseInt(dato2));
//                System.out.println("el resultado " + valxcant);
//                modelo.setValueAt(("" + valxcant), i, 2);
//
//            }
//        }
//    }   
    private void mostrarCampoEfec(boolean b) {
        txtTotal.setVisible(b);
        txtPtarjeta.setVisible(!b);
        txtCambio.setVisible(b);
        txtEfectivo.setVisible(b);
        lblTotal.setVisible(b);
        lblCambio.setVisible(b);
        lblTarjeta.setVisible(!b);
        lblEfectivo.setVisible(b);

    }

    public void eliminar() {
        DefaultTableModel tb = (DefaultTableModel) tblConcepto.getModel();
        int a = tblConcepto.getRowCount() - 1;
        for (int i = a; i >= 0; i--) {
            tb.removeRow(tb.getRowCount() - 1);
        }
        //cargaTicket();
    }

    public void insertar_pagosxconcepto() throws ClassNotFoundException {

        int Nfilas = tblConcepto.getRowCount();
        String documento = txtdocumento.getText();
        String tipodoc = combotipoDoc.getSelectedItem().toString();

        Insert = new ArrayList<>();

        String consulta = "SELECT MAX(`numero`) FROM `facturas`\n"
                + "WHERE `pfk_paciente`='" + tipodoc + documento + "'";

        String num_p = gsql.unicoDato(consulta);

        try {
            System.out.println("numero de filas en la tabla+++" + Nfilas);

            for (int i = 0; i < Nfilas; i++) {
//             
                Insert.add("INSERT INTO pagosxconceptos VALUES('" + tipodoc + documento + "','" + num_p + "','" + modelo.getValueAt(i, 0) + "'," + modelo.getValueAt(i, 2) + "," + modelo.getValueAt(i, 3) + ")");

            }
            gsql.EnviarConsultas(Insert);

        } catch (SQLException ex) {
            //Logger.getLogger(ventanaConsultarF.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void GuardarNew() {

        String documento = txtdocumento.getText();
        String tipoDoc = combotipoDoc.getSelectedItem().toString();
        String Pnombre = lblPnombre.getText();
        String Snombre = lblSnombre.getText();
        String Papellido = lblPapellido.getText();
        String Sapellido = lblSapellido.getText();
        String sql;
        String nombre = txtnombre.getText();
        String apellido = txtapellidos.getText();
        String documentoAux = lblDocumento.getText();
        int total = txtTotal.getText().equals("") ? 0 : Integer.parseInt(txtTotal.getText().replace(".", ""));
        int efectivo = txtEfectivo.getText().equals("") ? 0 : Integer.parseInt(txtEfectivo.getText().replace(".", ""));
        int ptarjeta = txtPtarjeta.getText().equals("") ? 0 : Integer.parseInt(txtPtarjeta.getText().replace(".", ""));
        int pagototal = efectivo + ptarjeta;
        
        System.out.println("TipoP->"+TipoP);
        
        
        
        
        int valor_deuda = 0;
        int cambio = 0;
        int cant = 0, sum = 0;
        Calendar now = Calendar.getInstance();
        //<editor-fold defaultstate="collapsed" desc="VALIDACIONES">
        if(!d.equals("O")){
            if (tipoDoc.equals("Seleccionar")) {
                JOptionPane.showMessageDialog(null, "Por favor ingrese el tipo de documento");
                return;
            }
            if (documento.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Por favor ingrese el documento");
                return;
            }
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Por favor ingrese el nombre ");
                return;
            }

            if (apellido.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Por favor ingrese el apellido ");
                return;
            }
            if (this.tblConcepto.getRowCount() == 0 && this.tblConcepto.getSelectedRow() == -1) {
                JOptionPane.showMessageDialog(null, "Por favor ingrse conceptos a la factura");
                return;
            }
        }
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="RETORNOS">
            if (modopg == "0") {
                if (txtEfectivo.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Por favor ingresar un valor para Efectivo");
                    txtEfectivo.requestFocusInWindow();
                    return;
                }
            } else if (modopg == "1") {
                if (txtPtarjeta.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Por favor ingrese un valor para Tarjeta");
                    txtPtarjeta.requestFocusInWindow();
                    return;
                }
            } else if (modopg == "2") {
                if (txtPtarjeta.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Por favor ingrese un valor para tarjeta");
                    txtPtarjeta.requestFocusInWindow();
                    return;
                }
                if (txtEfectivo.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Por favor ingrese un valor para Efectivo");
                    txtEfectivo.requestFocusInWindow();
                    return;
                }
            }
        //</editor-fold>
            
            
        Map<String, String> lista_datos = new HashMap <String, String>();
        
        lista_datos.put("documento", documento);
        lista_datos.put("tipoDoc", tipoDoc);
        lista_datos.put("Pnombre", Pnombre);
        lista_datos.put("Snombre", Snombre);
        lista_datos.put("Papellido", Papellido);
        lista_datos.put("Sapellido", Sapellido);
        lista_datos.put("nombre", nombre);
        lista_datos.put("apellido", apellido);        
        lista_datos.put("modopg", ""+modopg);
        lista_datos.put("total", ""+total);        
        lista_datos.put("efectivo", ""+efectivo);
        lista_datos.put("ptarjeta", ""+ptarjeta);
        lista_datos.put("d", ""+d);    
        lista_datos.put("TipoP", ""+TipoP);    
        lista_datos.put("pkcita", ""+pkcita);    
        lista_datos.put("documentoAux", ""+documentoAux);   
        
    
        ArrayList<String[]> listaConcepto = getDatosConceptos();        
        ArrayList<String[]> listaTramientos = new ArrayList<>();
        ArrayList<String[]> listaArticulos = new ArrayList<>();
        ArrayList<String[]> listaTramientosAux = new ArrayList<>();
        ArrayList<String[]> listaArtiAux = new ArrayList<>();
        ArrayList<String[]> listaDeudaN = new ArrayList<>();
        
        System.out.println("**********listaConcepto***********");
        for(int i = 0; i < listaConcepto.size(); i++){
            System.out.println("listaConcepto--"+i+"->");
            String sou = "";
            for(int j = 0; j < listaConcepto.get(i).length; j++){                
                sou += (sou.equals("")?"":", ")+listaConcepto.get(i)[j];
            }
            System.out.println("["+sou+"]"); 
        }
        System.out.println("**********END listaConcepto***********");
        
        for (int i = 0; i < listaConcepto.size(); i++) {
            boolean encontro = false;
            if (!listaConcepto.get(i)[5].equals("")) {
                for (int j = 0; j < listaTramientos.size(); j++) {
                    if (listaTramientos.get(j)[5].equals(listaConcepto.get(i)[5])) {
                        encontro = true;
                        break;
                    }
                }
                if (!encontro) {
                    listaTramientos.add(listaConcepto.get(i));
                }
            }else{
               listaArticulos.add(listaConcepto.get(i));                
            }
        }
        System.out.println("**********listaTramientos***********");
        for(int i = 0; i < listaTramientos.size(); i++){
            System.out.println("listaTramientos--"+i+"->");
            String sou = "";
            for(int j = 0; j < listaTramientos.get(i).length; j++){
                sou += (sou.equals("")?"":", ")+listaTramientos.get(i)[j];
            }
            System.out.println("["+sou+"]");
        }
        System.out.println("**********END listaTramientos***********");
        System.out.println("**********listaArticulos***********");
        for(int i = 0; i < listaArticulos.size(); i++){
            System.out.println("listaArticulos--"+i+"->");
            String sou = "";
            for(int j = 0; j < listaArticulos.get(i).length; j++){
                sou += (sou.equals("")?"":", ")+listaArticulos.get(i)[j];
            }
            System.out.println("["+sou+"]");
        }
        System.out.println("**********END listaArticulos***********");
        
        int total_trata = 0, total_arti = 0;
        
        for(int i = 0; i < listaTramientos.size(); i++){
            total_trata += Integer.parseInt(listaTramientos.get(i)[1]) * Integer.parseInt(listaTramientos.get(i)[2]);                    
            listaTramientosAux.add(new String[]{listaTramientos.get(i)[0], "" + listaTramientos.get(i)[1], "" + listaTramientos.get(i)[2], "", listaTramientos.get(i)[5]});
        }
        
        if(pagototal < total_trata){
            JOptionPane.showMessageDialog(null, "El valor pagado debe ser igual o superior al costo de la cuota del tratamiento");            
            return;
        }
        
        int disponible = pagototal - total_trata;
        System.out.println("disponible---->"+disponible);
        System.out.println("pagototal---->"+pagototal);
        System.out.println("total_trata---->"+total_trata);
        
        for (int i = 0; i < listaArticulos.size(); i++) {
            boolean encontro = false;
            for (int j = 0; j < listaArtiAux.size(); j++) {
                if (listaArticulos.get(i)[0].equals(listaArtiAux.get(j)[0])) {
                    encontro = true;
                    break;
                }
            }
            if (!encontro) {
                listaArtiAux.add(listaArticulos.get(i));
            }
        }
        System.out.println("**********listaArtiAux***********");
        for(int i = 0; i < listaArtiAux.size(); i++){
            System.out.println("listaArtiAux--"+i+"->");
            String sou = "";
            for(int j = 0; j < listaArtiAux.get(i).length; j++){
                sou += (sou.equals("")?"":", ")+listaArtiAux.get(i)[j];
            }
            System.out.println("["+sou+"]");
        }
        System.out.println("**********END listaArtiAux***********");
        
       
        for (int i = 0; i < listaArtiAux.size(); i++) {
            sum = 0;            
            for(int j = 0; j < listaArticulos.size(); j++){
                if(listaArtiAux.get(i)[0].equals(listaArticulos.get(j)[0])){ 
                    sum += Integer.parseInt(listaArticulos.get(j)[1]) * Integer.parseInt(listaArticulos.get(j)[2]);
                }
            }
            listaArtiAux.set(i, new String[]{listaArtiAux.get(i)[0], "1", ""+sum, "", listaArtiAux.get(i)[4], ""});
        }
        System.out.println("**********listaArtiAux*****BEFORE******");
        for(int i = 0; i < listaArtiAux.size(); i++){
            System.out.println("listaArtiAux--"+i+"->");
            String sou = "";
            for(int j = 0; j < listaArtiAux.get(i).length; j++){
                sou += (sou.equals("")?"":", ")+listaArtiAux.get(i)[j];
            }
            System.out.println("["+sou+"]");
        }
        System.out.println("**********END listaArtiAux***********");
        
        
        for(int i = 0; i < listaArtiAux.size(); i++){            
            total_arti = Integer.parseInt(listaArtiAux.get(i)[1]) * Integer.parseInt(listaArtiAux.get(i)[2]);       
        }
        
        cambio = disponible - total_arti;
        System.out.println("total_arti->"+total_arti);    
        if(cambio < 0){        
            Collections.sort(listaArtiAux, new Comparator<String[]>(){
                @Override
                public int compare(String[] o1, String[] o2) {
                    if(Integer.parseInt(o1[2]) >  Integer.parseInt(o2[2])){
                        return -1;
                    }else if(Integer.parseInt(o1[2]) <  Integer.parseInt(o2[2])){
                        return 1; 
                    }else{
                        return 0;
                    }
                }
            });
            listaDeudaN = getDeudaArticulo(listaArtiAux, disponible);
        }
        System.out.println("lista_datos--->"+lista_datos);
        System.out.println("listaConcepto--->"+listaConcepto.size());
        System.out.println("listaTramientos--->"+listaTramientos.size());
        System.out.println("listaArticulos--->"+listaArticulos.size());
        System.out.println("listaDeudaN--->"+listaDeudaN.size());
        cntlcfact = new ControlFacturas();
        String numeroFac = "";
        
        if(!d.equals("O")){
            numeroFac = cntlcfact.setFactura(lista_datos, listaConcepto, listaTramientos, listaArticulos, listaDeudaN);
        }else{
            numeroFac = cntlcfact.setFacturaOcacional(lista_datos, listaConcepto, listaTramientos, listaArticulos, listaDeudaN);
        }
        
        if(!numeroFac.equals("")){
            int confirmar = JOptionPane.showConfirmDialog(null, "¿Crear y Guardar Factura?", "", JOptionPane.YES_NO_OPTION);
            String[] nums = numeroFac.split("<::>");
            listafact.add(new String[]{nums[0], nums[1]});
            if (confirmar == JOptionPane.YES_OPTION) {                
                DescripcionFactura df = new DescripcionFactura();
                df.GenerarFactura(this, (efectivo + ptarjeta), (efectivo + ptarjeta) - total, nums[0]);
                LimpiarTabla();
            }
        }else{
            JOptionPane.showMessageDialog(null, "Ocurrio un Error al momento de crear la factura.");
        }
            
    }
     
    public ArrayList<String[]> getDeudaArticulo(ArrayList<String[]> Lista, int disponible){
        ArrayList<String[]> retorno= new ArrayList<>();
        try{
            System.out.println("disponible-->"+disponible);
            System.out.println("Lista.size()-->"+Lista.size());
            int diferencia = 0;
            for(int i = 0; i < Lista.size();  i++){
                diferencia = disponible -Integer.parseInt(Lista.get(i)[2]);
                Lista.get(i)[5] = ""+diferencia;
            }
            Collections.sort(Lista, new Comparator<String[]>(){
                @Override
                public int compare(String[] o1, String[] o2) {
                    if(Integer.parseInt(o1[5]) >  Integer.parseInt(o2[5])){
                        return 1;
                    }else if(Integer.parseInt(o1[5]) <  Integer.parseInt(o2[5])){
                        return -1;
                    }else{
                        return 0;
                    }
                } 
             });
            ArrayList<String[]> nega = new ArrayList<>();
            
            for(int i = 0; i < Lista.size();  i++){
                if(Integer.parseInt(Lista.get(i)[5]) < 0){
                    nega.add(Lista.get(i));
                    Lista.remove(i);
                }
            }
            
            for(int i = 0; i < nega.size();  i++){
                Lista.add(nega.get(i));
            }
            
            int resta  = disponible - Integer.parseInt(Lista.get(0)[2]);
            if(resta >= 0){//
                disponible = resta;
                Lista.remove(0);
                Lista = getDeudaArticulo(Lista, disponible);
            }else{
                resta = Integer.parseInt(Lista.get(0)[2])- disponible;
                Lista.get(0)[2] = ""+ resta;
            }
            return Lista;
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("e.getMessage();-->"+e.getMessage());
            
        }
        return retorno;
    }

    private void GuardarOLD() {

        String documento = txtdocumento.getText();
        String tipoDoc = combotipoDoc.getSelectedItem().toString();
        String Pnombre = lblPnombre.getText();
        String Snombre = lblSnombre.getText();
        String Papellido = lblPapellido.getText();
        String Sapellido = lblSapellido.getText();
        String sql;
        String nombre = txtnombre.getText();
        String apellido = txtapellidos.getText();
        int total = txtTotal.getText().equals("") ? 0 : Integer.parseInt(txtTotal.getText().replace(".", ""));
        int efectivo = txtEfectivo.getText().equals("") ? 0 : Integer.parseInt(txtEfectivo.getText().replace(".", ""));
        int ptarjeta = txtPtarjeta.getText().equals("") ? 0 : Integer.parseInt(txtPtarjeta.getText().replace(".", ""));
        int valor_deuda = 0;
        int cambio = 0;
        Calendar now = Calendar.getInstance();

        if (tipoDoc.equals("Seleccionar")) {
            JOptionPane.showMessageDialog(null, "Por favor ingrese el tipo de documento");
            return;
        }
        if (documento.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor ingrese el documento");
            return;
        }
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor ingrese el nombre ");
            return;
        }

        if (apellido.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor ingrese el apellido ");
            return;
        }
        if (this.tblConcepto.getRowCount() == 0 && this.tblConcepto.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "Por favor ingrse conceptos a la factura");
            return;
        }

        if (modopg == "0") {
            if (txtEfectivo.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Por favor ingresar un valor para Efectivo");
                txtEfectivo.requestFocusInWindow();
                return;
            }
        } else if (modopg == "1") {
            if (txtPtarjeta.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Por favor ingrese un valor para Tarjeta");
                txtPtarjeta.requestFocusInWindow();
                return;
            }
        } else if (modopg == "2") {
            if (txtPtarjeta.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Por favor ingrese un valor para tarjeta");
                txtPtarjeta.requestFocusInWindow();
                return;
            }
            if (txtEfectivo.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Por favor ingrese un valor para Efectivo");
                txtEfectivo.requestFocusInWindow();
                return;
            }
        }

        ArrayList<String[]> listaConcepto = getDatosConceptos();
        ArrayList<String[]> listaPago = new ArrayList<>();
        ArrayList<String[]> listaDeuda = new ArrayList<>();
        ArrayList<String[]> listaAux = new ArrayList<>();
        ArrayList<String[]> listaTramientos = new ArrayList<>();
        ArrayList<String[]> listaTramientosAux = new ArrayList<>();
        ArrayList<String[]> listaFinal = new ArrayList<>();

        for (int i = 0; i < listaConcepto.size(); i++) {
            System.out.println("listaConcepto.get(i)[0]--- " + listaConcepto.get(i)[0]);
            System.out.println("listaConcepto.get(i)[1]--- " + listaConcepto.get(i)[1]);
            System.out.println("listaConcepto.get(i)[2]--- " + listaConcepto.get(i)[2]);
            System.out.println("listaConcepto.get(i)[3]--- " + listaConcepto.get(i)[3]);
            System.out.println("listaConcepto.get(i)[4]--- " + listaConcepto.get(i)[4]);
            //System.out.println("listaConcepto.get(i)[4]--- "+ listaConcepto.get(i)[5] );
            if (listaConcepto.get(i)[3].equals("")) {
                listaPago.add(listaConcepto.get(i));
            } else {
                listaDeuda.add(listaConcepto.get(i));
            }
        }
        System.out.println("lista concepto  " + listaConcepto.size());
        System.out.println("lista PAGO --- " + listaPago.size());
        for (int i = 0; i < listaPago.size(); i++) {
            boolean encontro = false;
            for (int j = 0; j < listaAux.size(); j++) {
                if (listaPago.get(i)[0].equals(listaAux.get(j)[0])) {
                    encontro = true;
                    break;
                }
            }
            if (!encontro) {
                listaAux.add(listaPago.get(i));
            }
        }
        int cant = 0, sum = 0;
        System.out.println("listaAUX --- " + listaAux.size());
        for (int i = 0; i < listaAux.size(); i++) {
            cant = 0;
            sum = 0;
            for (int j = 0; j < listaConcepto.size(); j++) {
                if (listaAux.get(i)[0].equals(listaConcepto.get(j)[0])) {
                    if (listaConcepto.get(i)[3].equals("")) {
                        cant += Integer.parseInt(listaConcepto.get(j)[1]);
                    }
                    sum += Integer.parseInt(listaConcepto.get(j)[2]);

                }
            }
            listaFinal.add(new String[]{listaAux.get(i)[0], "" + cant, "" + sum, ""});

        }

        for (int i = 0; i < listaConcepto.size(); i++) {
            boolean encontro = false;
            if (!listaConcepto.get(i)[5].equals("")) {
                for (int j = 0; j < listaTramientos.size(); j++) {
                    if (listaTramientos.get(j)[5].equals(listaConcepto.get(i)[5])) {
                        encontro = true;
                        break;
                    }
                }
                if (!encontro) {
                    listaTramientos.add(listaConcepto.get(i));
                }
            }
        }

        String numFact = "";

        if (ptarjeta > 0 || efectivo > 0) {
            numFact = gsql.ObtNumercionFac();

            String datos;
            //////////////////INSERTS
            if (d.equals("A")) { // CAMBIO A PACIENTE

                try {

                    Insert.add("INSERT INTO personas "
                            + "VALUES "
                            + "('" + documento + "','" + tipoDoc + "','" + Pnombre + "','" + Snombre + "','" + Papellido + "','" + Sapellido + "',"
                            + "NULL,NULL,NULL,NULL)");

                    Insert.add("INSERT INTO pacientes VALUES ('" + tipoDoc + documento + "','activo','dorozco',NOW())");
                    Insert.add("UPDATE  paciente_auxiliar SET estado='Inactivo', tipo='P' WHERE pk_paciente_auxiliar=" + TipoP);

                    datos = "SELECT pfk_paciente FROM citas WHERE pfk_paciente =  '" + TipoP + "'";
                    if (gsql.ExistenDatos(datos)) {
                        Insert.add("UPDATE citas SET `pfk_paciente`= '" + tipoDoc + documento + "' WHERE `pfk_paciente`= '" + TipoP + "'");
                    }

                    datos = "SELECT pfk_paciente FROM citasxhoras WHERE pfk_paciente =  '" + TipoP + "'";
                    if (gsql.ExistenDatos(datos)) {
                        Insert.add("UPDATE citasxhoras SET `pfk_paciente`= '" + tipoDoc + documento + "' WHERE `pfk_paciente`= '" + TipoP + "'");
                    }

                    datos = "SELECT pfk_paciente FROM auditoria_agenda WHERE pfk_paciente =  '" + TipoP + "'";
                    if (gsql.ExistenDatos(datos)) {
                        Insert.add("UPDATE auditoria_agenda SET `pfk_paciente`= '" + tipoDoc + documento + "' WHERE `pfk_paciente`= '" + TipoP + "'");
                    }

                    String consulta = "SELECT pfk_paciente FROM pacientexcotizaciones WHERE pfk_paciente =  '" + TipoP + "'";
                    if (gsql.ExistenDatos(consulta)) {
                        Insert.add("UPDATE "
                                + "pacientexcotizaciones "
                                + "SET "
                                + "pfk_paciente = '" + tipoDoc + documento + "' "
                                + "WHERE pfk_paciente =  '" + TipoP + "'");
                    }
                    String in = "";
                    for (int g = 0; g < listaConcepto.size(); g++) {
                        in += "'" + listaConcepto.get(g)[0] + "'";
                    }
                    if (!in.equals("")) {
                        consulta = "SELECT pxc.`pfk_tratamiento`, pxc.`cuota_inicial`, pxc.`diferidas_en`, pxc.`cuota`, pxc.`costo`\n"
                                + "FROM conceptos con\n"
                                + "INNER JOIN `pacientexcotizaciones` pxc ON pxc.`pfk_tratamiento` = con.`fk_tratamiento`\n"
                                + "WHERE con.`pk_concepto` IN (" + in + ") AND pxc.`pfk_paciente` = '" + tipoDoc + documento + "'\n"
                                + "GROUP BY fk_tratamiento\n";

                        ArrayList<String[]> listac = gsql.SELECT(consulta);
                        if (listac.size() > 0) {
                            for (int g = 0; g < listac.size(); g++) {
                                Insert.add("INSERT INTO pacientextratamiento VALUES ("
                                        + "'1',\n"
                                        + "'" + tipoDoc + documento + "',\n"
                                        + "" + listac.get(g)[0] + ",\n"
                                        + "'" + listac.get(g)[1] + "',\n"
                                        + "" + listac.get(g)[2] + ",\n"
                                        + "'" + listac.get(g)[3] + "',\n"
                                        + "'" + listac.get(g)[4] + "',\n"
                                        + "'Activo',\n"
                                        + "NULL,\n"
                                        + "NOW())");
                            }

                        }
                    }

                    //gsql.EnviarConsultas
                } catch (Exception e) {
                }

            }

            Insert.add("INSERT INTO facturas VALUES ( '" + numFact + "' , NOW(), NOW(), 'pagado', '" + total + "','" + tipoDoc + documento + "')");
            System.out.println("el numero de factura a insertar es ---" + numFact);
            System.out.println("lista final --- " + listaFinal.size());
            for (int i = 0; i < listaFinal.size(); i++) {
                Insert.add("INSERT INTO pagosxconceptos "
                        + "VALUES('" + tipoDoc + documento + "','" + numFact + "','" + listaFinal.get(i)[0] + "',"
                        + "" + listaFinal.get(i)[1] + "," + listaFinal.get(i)[2] + ")");
            }

            if (modopg.equals("1") || modopg.equals("2")) {
                System.out.println("inserte valor de tarjeta");
                Insert.add("INSERT INTO modo_pago VALUES ('" + numFact + "','Tarjeta','" + ptarjeta + "')");

//            if (modopg.equals("1")) {
////                JOptionPane.showMessageDialog(null, "GRACIAS POR SU PAGO");
//            }
            }

            if (modopg.equals("0") || modopg.equals("2")) {

                Insert.add("INSERT INTO modo_pago VALUES('" + numFact + "','Efectivo','" + (efectivo > total ? total : efectivo) + "') ");

                valor_deuda = (total - (modopg.equals("2") ? ptarjeta + efectivo : efectivo));
//                cambio = (efectivo - total);

                if (valor_deuda > 0) {//// insert deuda
                    String idcd = "";
                    System.out.println("lista pago--" + listaPago.size());
//                
//                System.out.println("lista deuda--" + listaDeuda.get(0).length);
//
//                System.out.println("lista d[0]--" + listaDeuda.get(0)[0]);
//                System.out.println("lista d[1]--" + listaDeuda.get(0)[1]);
//                System.out.println("lista d[2]--" + listaDeuda.get(0)[2]);
//                System.out.println("lista d[3]--" + listaDeuda.get(0)[3]);
//                System.out.println("lista d[4]--" + listaDeuda.get(0)[4]);
                    for (int i = 0; i < listaPago.size(); i++) {
                        if (listaPago.get(i)[4].equals("1")) {
                            idcd = listaPago.get(i)[0];
                            break;
                        }
                    }
                    if (idcd.equals("")) {
                        int valor = 0, ind = 0;
                        for (int i = 0; i < listaPago.size(); i++) {
                            if (Integer.parseInt(listaPago.get(i)[2]) > valor) {
                                ind = i;
                                valor = Integer.parseInt(listaPago.get(i)[2]);
                            }
                        }
                        idcd = listaPago.get(ind)[0];

                    }

                    Insert.add("INSERT INTO `pendientes_por_pagar` "
                            + " VALUES ('" + numFact + "','" + tipoDoc + documento + "','" + valor_deuda + "',NOW(),'pendiente','" + idcd + "')");
                }
                if (cambio >= 0) {
                    lblCambio.setVisible(true);
                    txtCambio.setVisible(true);
                    txtCambio.setText("" + cambio);
                }

//            JOptionPane.showMessageDialog(null, "GRACIAS POR SU PAGO");
            }

            //if (efectivo > 0) {
            Insert.add("INSERT INTO pagos VALUES('" + tipoDoc + documento + "','" + numFact + "','" + (efectivo + ptarjeta) + "','" + datosUsuario.datos.get(0)[0] + "',NOW(),NULL,NULL)");
            //}

//        if (ptarjeta > 0) {
//            Insert.add("INSERT INTO pagos VALUES('" + tipoDoc + documento + "','" + num + "','" + ptarjeta + "','dorozco',NOW(),'dorozco',NOW())");
//        }
            int sumadeuda = 0;
            if (d.equals("P")) {
                for (int i = 0; i < listaDeuda.size(); i++) {
                    Insert.add("UPDATE `pendientes_por_pagar`\n"
                            + "SET   `estado` = 'terminado'\n"
                            + "WHERE `fk_factura` = '" + listaDeuda.get(i)[3] + "' AND `fk_paciente` = '" + tipoDoc + documento + "' \n"
                            + "AND `fk_concepto` = '" + listaDeuda.get(i)[0] + "'");
                    sumadeuda += Integer.parseInt(listaDeuda.get(i)[1]) * Integer.parseInt(listaDeuda.get(i)[2]);
                }

            }
            int pagototal = efectivo + ptarjeta;
            int disponible = pagototal;//-sumadeuda;
            int resta = 0, val = 0;
            for (int i = 0; i < listaTramientos.size(); i++) {
                sum = 0;
                cant = 0;
                resta = 0;
                val = 0;
                for (int j = 0; j < listaConcepto.size(); j++) {
                    if (listaTramientos.get(i)[5].equals(listaConcepto.get(j)[5])) {
                        if (listaConcepto.get(i)[3].equals("")) {
                            cant += Integer.parseInt(listaConcepto.get(j)[1]);
                        }
                        sum += Integer.parseInt(listaConcepto.get(j)[2]);

                    }
                }
                resta = disponible - sum;
                if (resta < 0) {
                    if (disponible > 0) {
                        val = disponible;
                    } else {
                        val = 0;
                    }
                } else {
                    val = sum;
                }
                disponible = resta;

                if (val > 0) {
                    listaTramientosAux.add(new String[]{listaTramientos.get(i)[0], "" + cant, "" + val, "", listaTramientos.get(i)[5]});
                }

            }

            System.out.println("Lista FINAL");
            for (int i = 0; i < listaFinal.size(); i++) {
                System.out.println("indice ------ " + i);
                for (int j = 0; j < listaFinal.get(i).length; j++) {
                    System.out.println("j: " + j + "--v: " + listaFinal.get(i)[j]);
                }
            }
            System.out.println("Lista TRATAMEINTO");
            for (int i = 0; i < listaTramientos.size(); i++) {
                System.out.println("indice ------ " + i);
                for (int j = 0; j < listaTramientos.get(i).length; j++) {
                    System.out.println("j: " + j + "--v: " + listaTramientos.get(i)[j]);
                }
                System.out.println("***********************************");
            }
            System.out.println("Lista TRATAMEINTO__AUX");
            for (int i = 0; i < listaTramientosAux.size(); i++) {
                System.out.println("indice ------ " + i);
                for (int j = 0; j < listaTramientosAux.get(i).length; j++) {
                    System.out.println("j: " + j + "--v: " + listaTramientosAux.get(i)[j]);
                }
            }
            System.out.println("Lista DEUDAS");
            for (int i = 0; i < listaDeuda.size(); i++) {
                System.out.println("indice ------ " + i);
                for (int j = 0; j < listaDeuda.get(i).length; j++) {
                    System.out.println("j: " + j + "--v: " + listaDeuda.get(i)[j]);
                }
            }

            System.out.println("**********************************************************************************************************+");
            System.out.println("pk_cita-----" + pkcita + "//////");
            if (!pkcita.equals("")) {
                String Actualizar = "CALL actualizarAuditoria('" + tipoDoc + documento + "','" + pkcita + "',2)";
                System.out.println("actualizarAuditoria--->" + Actualizar);
                Insert.add(Actualizar);
            }

            for (int i = 0; i < listaTramientosAux.size(); i++) {
                String ActualizarSegTratamiento = "CALL actualizarSegTrat('" + tipoDoc + documento + "','" + listaTramientosAux.get(i)[4] + "','" + listaTramientosAux.get(i)[2] + "','','" + datosUsuario.datos.get(0)[0] + "','F')";
                System.out.println("actualizarSegTrat---" + ActualizarSegTratamiento);
                Insert.add(ActualizarSegTratamiento);
            }

            if (Insert.size() > 0) {
                try {
                    if (gsql.EnviarConsultas(Insert)) {
                        Insert.clear();
                    }
                } catch (ClassNotFoundException ex) {
                    //Logger.getLogger(ventanaConsultarF.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    //Logger.getLogger(ventanaConsultarF.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            int confirmar = JOptionPane.showConfirmDialog(null, "¿Crear y Guardar Factura?", "", JOptionPane.YES_NO_OPTION);
            if (confirmar == JOptionPane.YES_OPTION) {

                DescripcionFactura df = new DescripcionFactura();
                df.GenerarFactura(this, (efectivo + ptarjeta), (efectivo + ptarjeta) - total, numFact);
                //df.conceptosFactura(this, (efectivo + ptarjeta), (efectivo + ptarjeta) - total, numFact);
//                limpiarVentanaFactura();
                LimpiarTabla();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Debe ingresar valor a pagar");
        }

//        System.out.println("fecha" + now.get(Calendar.YEAR));
//        imprimirFactura();
    }

    private ArrayList<String[]> getDatosConceptos() {
        int Nfilas = tblConcepto.getRowCount();
        ArrayList<String[]> listaret = new ArrayList<>();
        String idc = "", cant = "", val = "", nfac = "", C = "";

        for (int i = 0; i < Nfilas; i++) {
            idc = "" + modelo.getValueAt(i, 0);
            cant = "" + modelo.getValueAt(i, 2);
            val = "" + modelo.getValueAt(i, 3);
            nfac = ListaConceptos.get(i)[7];
            C = ListaConceptos.get(i)[6];
            System.out.println("ListaConceptos.get(i)[8]-->"+ListaConceptos.get(i)[8]+"//");
            listaret.add(new String[]{idc, cant, val.replace(".", ""), nfac, C, ListaConceptos.get(i)[8]});

        }

        return listaret;
    }

    private void Establecermodopago() {

        if (rbEfectivo.isSelected()) {//pago efectivo
            modopg = "0";
        } else if (rbTarjeta.isSelected()) {
            modopg = "1";

        } else {
            modopg = "2";
        }
    }

    private void calcularCambio(KeyEvent evt) {
        String efect = txtEfectivo.getText().replace(".", "");
        String tot = txtTotal.getText().replace(".", "");

        int efectivo = Integer.parseInt(efect.equals("") ? "0" : efect);
        int total = Integer.parseInt(tot.equals("") ? "0" : tot);
        int cambio = 0;

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (modopg.equals("0")) {
                cambio = (efectivo - total);
                if (cambio >= 0) {
                    txtCambio.setText("" + Utilidades.MascaraMoneda("" + cambio));
                    txtCambio.setVisible(true);
                    lblCambio.setVisible(true);
                } else {
                    lblCambio.setVisible(false);
                    txtCambio.setVisible(false);
                }
            } else if (modopg.equals("2")) {
                String ptarj = txtPtarjeta.getText().replace(".", "");
                int t = efectivo + Integer.parseInt(ptarj.equals("") ? "0" : ptarj);
                System.out.println("t-----  " + t);
                cambio = (t - total);
                System.out.println("totalkeyr----- " + total);
                System.out.println("cambio keyr-- " + cambio);
                if (cambio >= 0) {
                    txtCambio.setText("" + Utilidades.MascaraMoneda("" + cambio));
                    txtCambio.setVisible(true);
                    lblCambio.setVisible(true);
                } else {
                    lblCambio.setVisible(false);
                    txtCambio.setVisible(false);
                }

            }

        }
    }

    private void limpiarVentanaFactura() {
        txtdocumento.setText("");
        txtdocumento.setEnabled(true);
        txtnombre.setText("");
        txtnombre.setEnabled(true);
        txtapellidos.setText("");
        txtapellidos.setEnabled(true);
        combotipoDoc.setSelectedItem("Seleccionar");
        combotipoDoc.setEnabled(true);
        eliminar();
        txtTotal.setText("");
        txtEfectivo.setText("");
        txtCambio.setText("");
        txtPtarjeta.setText("");
        Insert.clear();
        ListaConceptos = new ArrayList<>();
        IniciarTabla();        

    }

    private void CargarDatosPaciente(String idpaciente, String estadoch) {
        System.out.println("************CargarDatosPaciente*********" + idpaciente + "*+");
        System.out.println("estado--_>" + estadoch);
        ArrayList<String[]> lbl = cntlcfact.getDatosPacientesFactura(idpaciente, estadoch);

        if (lbl != null) {
            String[] datos = lbl.get(0);

            d = datos[8];
            TipoP = datos[0];

            if (d.equals("A") || d.equals("O")) {
                combotipoDoc.setEnabled(true);
                txtnombre.setText(datos[1] + " " + datos[2]);
                txtapellidos.setText(datos[3] + " " + datos[4]);
//                      vcf.lblValorApagar.setText((String) modAux.getValueAt(filaSeleccionada, 5));
                txtdocumento.setText("");
                combotipoDoc.setSelectedItem("Seleccionar");
                lblDocumento.setText(datos[0]);

            } else {

                txtdocumento.setText(Utilidades.obtenerDocumentoyTipoDoc(datos[0])[1]);
                combotipoDoc.setSelectedItem(Utilidades.obtenerDocumentoyTipoDoc(datos[0])[0]);
                txtnombre.setText(datos[1] + " " + datos[2]);
                txtapellidos.setText(datos[3] + " " + datos[4]);
                lblDireccion.setText(datos[6]);
                lblTelefono.setText(datos[7]);
                combotipoDoc.setEnabled(false);
                txtnombre.setEnabled(false);
                txtapellidos.setEnabled(false);
                txtdocumento.setEnabled(false);

            }

            lblPnombre.setText(datos[1]);
            lblSnombre.setText(datos[2]);
            lblPapellido.setText(datos[3]);
            lblSapellido.setText(datos[4]);
            lblDocumento.setText(datos[0]);

            CargarTratamientoPaciente();
            totalPago();

        }

    }

    public void CargarTratamientoPaciente() {

        String doc = lblDocumento.getText();

        ArrayList<String[]> consultas = new ArrayList<>();
        consultas = new ArrayList<>();  

        String sql = "SELECT c.`pk_concepto` AS conc, c.`descripcion` AS CONCEPTO , c.`descripcion` DESCRP, \n"
                + "IF((ptra.`cuota_inicial` - SUM(IFNULL(`valorxcantidad`, 0))) <= 0, \n"
                + "ptra.cuota, \n"
                + "IF(ptra.diferidas_en = 1, (ptra.`cuota_inicial` - SUM(IFNULL(`valorxcantidad`, 0))), (ptra.`cuota_inicial` / ptra.diferidas_en))) valor, '' AS NFAC , IFNULL(c.fk_tratamiento, '') TRATAMIENTO \n"
                + "FROM `pacientextratamiento`  ptra\n"
                + "INNER JOIN `conceptos` c ON ptra.`fk_tratamiento` = c.`fk_tratamiento`\n"
                + "LEFT JOIN `pagosxconceptos` pag ON ptra.`pfk_paciente` = pag.`pfk_paciente` AND c.`pk_concepto`=pag.`pfk_concepto` \n"
                + "WHERE ptra.`pfk_paciente` ='" + doc + "' AND c.`fk_tipo_concepto`='1'\n"
                + "GROUP BY c.`pk_concepto`\n"
                + "UNION\n"
                + "SELECT pp.`fk_concepto` AS conc, c.`descripcion` AS CONCEPTO, c.`descripcion` AS DESCRP, pp.`valor_deuda` AS valor, pp.fk_factura  AS NFAC, IFNULL(c.fk_tratamiento, '') TRATAMIENTO\n"
                + "FROM `pendientes_por_pagar` pp\n"
                + "INNER JOIN `conceptos` c ON pp.`fk_concepto`= c.pk_concepto\n"
                + "WHERE pp.`fk_paciente`='" + doc + "' AND pp.`estado`='pendiente'\n"
                + "GROUP BY c.`pk_concepto`";

        sql = "SELECT c.`pk_concepto` AS conc, c.`descripcion` AS CONCEPTO , c.`descripcion` DESCRP, \n"
                + "IF((ptra.`cuota_inicial` - SUM(IFNULL(`valorxcantidad`, 0))) <= 0, \n"
                + "ptra.cuota, \n"
                + "IF(ptra.diferidas_en = 1, (ptra.`cuota_inicial` - SUM(IFNULL(`valorxcantidad`, 0))), ROUND(ptra.`cuota_inicial` / ptra.diferidas_en, 0))) valor, '' AS NFAC , IFNULL(c.fk_tratamiento, '') TRATAMIENTO \n"
                + "FROM `pacientextratamiento`  ptra\n"
                + "INNER JOIN (\n"
                + "	SELECT pxt.`fk_tratamiento`, pxt.`pk_consecutivo`, pxt.`pfk_paciente`, IFNULL(SUM(abono), 0) abonos, pxt.`costo`, IF(pxt.costo = '0', '1',IF(IFNULL(SUM(abono), 0) <  pxt.`costo`, '1', '0'))  cond\n"
                + "	FROM `pacientextratamiento` pxt \n"
                + "	LEFT JOIN `seguimiento_del_tratamiento` seg ON seg.`pfk_paciente` = pxt.`pfk_paciente` AND seg.`pfk_tratamiento` = pxt.`fk_tratamiento`\n"
                + "	WHERE pxt.`pfk_paciente` = '" + doc + "' AND pxt.`estado` = 'Activo' \n"
                + "	GROUP BY pxt.`pfk_paciente`, pxt.`fk_tratamiento`\n"
                + ") tbl ON tbl.fk_tratamiento = ptra.`fk_tratamiento` AND tbl.pk_consecutivo = ptra.`pk_consecutivo` AND ptra.`pfk_paciente` = tbl.pfk_paciente AND tbl.cond = '1'\n"
                + "INNER JOIN `conceptos` c ON tbl.`fk_tratamiento` = c.`fk_tratamiento`\n"
                + "LEFT JOIN `pagosxconceptos` pag ON ptra.`pfk_paciente` = pag.`pfk_paciente` AND c.`pk_concepto`=pag.`pfk_concepto` \n"
                + "LEFT JOIN facturas fac ON fac.`numero` = pag.`pfk_pago` AND fac.`estado` = 'pagado' \n"
                + "WHERE ptra.`pfk_paciente` = '" + doc + "' AND c.`fk_tipo_concepto` = '1' \n"
                + "GROUP BY c.`pk_concepto`\n"
                + "UNION\n"
                + "SELECT pp.`fk_concepto` AS conc, c.`descripcion` AS CONCEPTO, c.`descripcion` AS DESCRP, pp.`valor_deuda` AS valor, pp.fk_factura  AS NFAC, IFNULL(c.fk_tratamiento, '') TRATAMIENTO\n"
                + "FROM `pendientes_por_pagar` pp\n"
                + "INNER JOIN `facturas` fact ON fact.`pfk_paciente` = pp.`fk_paciente` AND fact.`numero` = pp.`fk_factura` AND fact.`estado` = 'pagado'"
                + "INNER JOIN `conceptos` c ON pp.`fk_concepto`= c.pk_concepto\n"
                + "WHERE pp.`fk_paciente`='" + doc + "' AND pp.`estado`='pendiente'\n"
                + "GROUP BY c.`pk_concepto`";

        System.out.println("sql--CargarTratamientoPaciente----" + sql);

        consultas = gsql.SELECT(sql);
//        System.out.println("datos del tratatiemto de este paciente " + Integer.parseInt(consultas.get(0)[1]) );
        System.out.println("tamaño----" + consultas.size());

        if (consultas.size() > 0) {
            for (int i = 0; i < consultas.size(); i++) {
                datos = new String[]{consultas.get(i)[0], consultas.get(i)[1], consultas.get(i)[2], "1",
                    Utilidades.MascaraMoneda(consultas.get(i)[3]), Utilidades.MascaraMoneda(consultas.get(i)[3]), "1",
                    consultas.get(i)[4], consultas.get(i)[5]};
                AgregarTablaConceptos();
            }
        }
    }

    ///
    private void LimpiarDatos() {
        datos = null;
    }

    public void ActualizarTablaConceptos(int fila){
        ListaConceptos.set(fila, datos);
        tblConcepto.setValueAt("" + datos[0], fila, 0);//idConcepto
        tblConcepto.setValueAt("" + datos[1], fila, 1);//Concepto
        tblConcepto.setValueAt("" + datos[3], fila, 2);//Cantidad
        tblConcepto.setValueAt("" + datos[4], fila, 3);//Valor unitario
        tblConcepto.setValueAt("" + datos[5], fila, 4);//Valor Total 
        totalPago();
        LimpiarDatos();
    }

    public void AgregarTablaConceptos() {
        ListaConceptos.add(datos);
        //DATOS 0-->idTrat , 1--> TRAT, 2--> CostoTotal, 3-->CuotaIni, 4-->diferidas, 5 --> cuota, 6-->(TIPO COONCEPTO), 7-->(FACTURA), 8--> fk_tratamiento  
        /////////////////////////////////  
        agregarFilaConcepto(new String[]{datos[0], datos[1], datos[3], datos[4], datos[5], "Quitar"});
        totalPago();
        LimpiarDatos();
    }

    private void agregarFilaConcepto(String[] fila) {
        modelo.addRow(fila);
    }

    public void LimpiarTabla() {
        txtTotal.setText("");
        txtEfectivo.setText("");
        txtCambio.setText("");
        txtPtarjeta.setText("");
        Insert.clear();
        ListaConceptos = new ArrayList<>();
        modelo = new DefaultTableModel(nombreColumnas, 0);
        tblConcepto.setModel(modelo);
    }

    private ArrayList<String> getConceptosTabla() {
        ArrayList<String> listaret = new ArrayList<>();
        String idc = "";
        System.out.println("*************getConceptosTabla()************"+ListaConceptos.size());
        for(int i = 0; i < ListaConceptos.size(); i++){
            if(ListaConceptos.get(i)[7].equals("")){
                System.out.println("*****"+ListaConceptos.get(i)[0]);
                listaret.add(ListaConceptos.get(i)[0]);
            }
        }
        return listaret;
    }

}
