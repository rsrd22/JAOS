/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;

import Calendario.VentanaAgenda;
import Control.ControlCitas;
import Modelo.Cita;
import Utilidades.Utilidades;
import java.awt.Color;
import java.lang.invoke.MethodHandles;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Map;
import javax.swing.JFrame;
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
public class ventanaDiasxHoras extends javax.swing.JFrame {
    public String anio;
    public String mes;
    public String id_mes;
    public String dia;
    
    public String anio_act;
    public String mes_act;
    public String id_mes_act;
    public String dia_act;
    
    public String SemInianio;
    public String SemInimes;
    public String SemIniid_mes;
    public String SemInidia;
    
    public String SemFinanio;
    public String SemFinid_mes;
    public String SemFinmes;
    public String SemFindia;
    
    public VentanaAgenda v;
    public VentanaCitas vcts;
    public VentanaConfirmacion  vconf;
    public String paciente;
    public String npaciente;
    public String pkcita = "";
    Utilidades utl = new Utilidades();
    DefaultTableModel modelo = new DefaultTableModel();
    ControlCitas cita = new ControlCitas();
    private int x, y;
    
    String[] dias = {"Lunes","Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};    
    String[] Meses = {"Enero","Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
    
    String[] nombreColumnasAux = new String[]{"Hora", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado"};
    String[] nombreColumnas = new String[]{"Hora", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado"};
    
    String[] horasTabla = new String[41];
    
    String[] infDiasS = new String[6];
    String fechaSeleccionada = "";
    ArrayList<String> horasSeleccionada = new ArrayList<String>();
    int numSel = 4;
    ArrayList<String[]> ListaCitaAux = new ArrayList<>();
    ArrayList<String[]> ListaNuloAux = new ArrayList<>();
    ArrayList<Cita> Lista_Calendario = new ArrayList<>();
    
    /**
     * Creates new form ventanaDiasxHoras
     */
    public ventanaDiasxHoras(String paciente, String npaciente){
        initComponents();
        Utilidades.EstablecerIcono(this);
        SimpleDateFormat sdfa = new SimpleDateFormat("yyyy");
        SimpleDateFormat sdfm = new SimpleDateFormat("MMMM");
        SimpleDateFormat sdfd = new SimpleDateFormat("d");
        
        
        Calendar cal =Calendar.getInstance();
        
        this.anio_act = sdfa.format(cal.getTime());
        this.mes_act = sdfm.format(cal.getTime());
        this.dia_act = sdfd.format(cal.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY/MMMM/d");
        SimpleDateFormat sdfas = new SimpleDateFormat("dd/MM/yyyy");        
        System.out.println("prueba  --- sdf.format(cal.getTime())-->"+sdf.format(cal.getTime()));
        System.out.println("sdfas.format(cal.getTime())-->"+sdfas.format(cal.getTime()));
        System.out.println("sdfa.format(cal.getTime())-->"+sdfa.format(cal.getTime()));
        System.out.println("sdfm.format(cal.getTime())-->"+sdfm.format(cal.getTime()));
        System.out.println("sdfd.format(cal.getTime())-->"+sdfd.format(cal.getTime()));
        cal.add(Calendar.WEEK_OF_MONTH, 3);
        
        
        this.paciente = paciente;
        this.npaciente = npaciente;
        
        lblNombrePaciente.setText(this.npaciente);
        this.setLocationRelativeTo(null);
        pkcita = "";
        this.anio = sdfa.format(cal.getTime());
        this.mes = sdfm.format(cal.getTime());
        this.dia = sdfd.format(cal.getTime());
        id_mes = getIdMes(mes);        
        id_mes_act = getIdMes(mes_act); 
        nombreColumnas = new String[]{"Hora", "Lu", "Ma", "Mi", "Ju", "Vi", "Sa"};
        GenerarCalendarioxHoras();
        
    }
    
    public ventanaDiasxHoras(String paciente, String npaciente, String pkcita, VentanaCitas vcts){
        initComponents();
        Utilidades.EstablecerIcono(this);
        Calendar cal =Calendar.getInstance();
        SimpleDateFormat sdfa = new SimpleDateFormat("yyyy");
        SimpleDateFormat sdfm = new SimpleDateFormat("MMMM");
        SimpleDateFormat sdfd = new SimpleDateFormat("d");
        System.out.println("sdfa.format(cal.getTime())-->"+sdfa.format(cal.getTime()));
        System.out.println("sdfm.format(cal.getTime())-->"+sdfm.format(cal.getTime()));
        System.out.println("sdfd.format(cal.getTime())-->"+sdfd.format(cal.getTime()));
        this.anio_act = sdfa.format(cal.getTime());
        this.mes_act = sdfm.format(cal.getTime());
        this.dia_act = sdfd.format(cal.getTime());
        this.pkcita = pkcita;
        //borrar
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");        
        SimpleDateFormat sdfas = new SimpleDateFormat("YYYY/MMMM/d");
        System.out.println("prueba 2 sdf.format(cal.getTime())-->"+sdf.format(cal.getTime()));
        System.out.println("sdfas.format(cal.getTime())-->"+sdfas.format(cal.getTime()));
        //borrar
        System.out.println("1-"+dia_act+"/"+mes_act+"/"+dia_act);
        
        cal.add(Calendar.WEEK_OF_MONTH, 1);
        this.paciente = paciente;
        this.npaciente = npaciente;
        lblNombrePaciente.setText(this.npaciente);
        this.vcts = vcts;
        //borrar
        System.out.println("2-"+sdf.format(cal.getTime()));
        this.anio = sdfa.format(cal.getTime());
        this.mes = sdfm.format(cal.getTime());
        this.dia = sdfd.format(cal.getTime());
        this.setLocationRelativeTo(null);
        
        
        id_mes = getIdMes(mes); 
        id_mes_act = getIdMes(mes_act); 
        nombreColumnas = new String[]{"Hora", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado"};
        GenerarCalendarioxHoras();
        
    }
    
    public ventanaDiasxHoras(Map<String, String> datos, VentanaConfirmacion  vconf){
        initComponents();
        Utilidades.EstablecerIcono(this);
        Calendar cal =Calendar.getInstance();
        SimpleDateFormat sdfa = new SimpleDateFormat("yyyy");
        SimpleDateFormat sdfm = new SimpleDateFormat("MMMM");
        SimpleDateFormat sdfd = new SimpleDateFormat("d");
        System.out.println("sdfa.format(cal.getTime())-->"+sdfa.format(cal.getTime()));
        System.out.println("sdfm.format(cal.getTime())-->"+sdfm.format(cal.getTime()));
        System.out.println("sdfd.format(cal.getTime())-->"+sdfd.format(cal.getTime()));
        
        this.anio_act = sdfa.format(cal.getTime());
        this.mes_act = sdfm.format(cal.getTime());
        this.dia_act = sdfd.format(cal.getTime());
        
        //borrar
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");        
        SimpleDateFormat sdfas = new SimpleDateFormat("YYYY/MMMM/d");
        System.out.println("prueba 3 sdf.format(cal.getTime())-->"+sdf.format(cal.getTime()));
        System.out.println("sdfas.format(cal.getTime())-->"+sdfas.format(cal.getTime()));
        //borrar
        System.out.println("1-"+dia_act+"/"+mes_act+"/"+dia_act);
        pkcita = "";
        cal.add(Calendar.WEEK_OF_MONTH, 1);
        this.paciente = datos.get("idPaciente");
        this.npaciente = datos.get("Paciente");
        EstadoMotivo(datos.get("motivo"));
        cbMotivo.setEnabled(false);
        
        lblNombrePaciente.setText(npaciente);   
        this.vconf = vconf;
        //borrar
        System.out.println("2-"+sdf.format(cal.getTime()));
        this.anio = sdfa.format(cal.getTime());
        this.mes = sdfm.format(cal.getTime());
        this.dia = sdfd.format(cal.getTime());
        this.setLocationRelativeTo(null);        
        
        id_mes = getIdMes(mes); 
        id_mes_act = getIdMes(mes_act); 
        nombreColumnas = new String[]{"Hora", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado"};
        GenerarCalendarioxHoras();
    }
    
    public ventanaDiasxHoras(String paciente, String npaciente, VentanaAgenda v){
        
        initComponents();
        Utilidades.EstablecerIcono(this);
        this.v = v;
        SimpleDateFormat sdfa = new SimpleDateFormat("yyyy");
        SimpleDateFormat sdfm = new SimpleDateFormat("MMMM");  
        SimpleDateFormat sdfd = new SimpleDateFormat("d");
        Calendar cal =Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY/MMMM/d");
        SimpleDateFormat sdfas = new SimpleDateFormat("dd/MM/yyyy");        
        System.out.println("prueba 4sdf.format(cal.getTime())-->"+sdf.format(cal.getTime()));
        System.out.println("sdfas.format(cal.getTime())-->"+sdfas.format(cal.getTime()));
        System.out.println("sdfa.format(cal.getTime())-->"+sdfa.format(cal.getTime()));
        System.out.println("sdfm.format(cal.getTime())-->"+sdfm.format(cal.getTime()));
        System.out.println("sdfd.format(cal.getTime())-->"+sdfd.format(cal.getTime()));
        this.anio_act = sdfa.format(cal.getTime());
        this.mes_act = sdfm.format(cal.getTime());
        this.dia_act = sdfd.format(cal.getTime());
        pkcita = "";
        cal.add(Calendar.WEEK_OF_MONTH, 3);
        this.paciente = paciente;
        this.npaciente = npaciente;
        lblNombrePaciente.setText(this.npaciente);
        this.anio = sdfa.format(cal.getTime());
        this.mes = sdfm.format(cal.getTime());
        this.dia = sdfd.format(cal.getTime());
        this.setLocationRelativeTo(null);
        id_mes = getIdMes(mes);        
        id_mes_act = getIdMes(mes_act);      
        nombreColumnas = new String[]{"Hora", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado"};
        GenerarCalendarioxHoras();
        
    }
    
    public ventanaDiasxHoras(String paciente, String npaciente, String anio, String mes, String dia) {
        initComponents();
        Utilidades.EstablecerIcono(this);
        
        
        SimpleDateFormat sdfa = new SimpleDateFormat("yyyy");
        SimpleDateFormat sdfm = new SimpleDateFormat("MMMM");  
        SimpleDateFormat sdfd = new SimpleDateFormat("d");
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY/MMMM/d");
        SimpleDateFormat sdfas = new SimpleDateFormat("dd/MM/yyyy");        
        System.out.println("prueba 5sdf.format(cal.getTime())-->"+sdf.format(cal.getTime()));
        System.out.println("sdfas.format(cal.getTime())-->"+sdfas.format(cal.getTime()));
        System.out.println("sdfa.format(cal.getTime())-->"+sdfa.format(cal.getTime()));
        System.out.println("sdfm.format(cal.getTime())-->"+sdfm.format(cal.getTime()));
        System.out.println("sdfd.format(cal.getTime())-->"+sdfd.format(cal.getTime()));
        this.anio_act = sdfa.format(cal.getTime());
        this.mes_act = sdfm.format(cal.getTime());
        this.dia_act = sdfd.format(cal.getTime());
        pkcita = "";
        this.setLocationRelativeTo(null);
        this.paciente = paciente;
        this.npaciente = npaciente;        
        lblNombrePaciente.setText(this.npaciente);
        this.anio = anio;
        this.mes = mes;
        this.dia = dia;
        id_mes = getIdMes(mes);
        id_mes_act = getIdMes(mes_act);
        this.setLocationRelativeTo(null);
        nombreColumnas = new String[]{"Hora", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado"};
        GenerarCalendarioxHoras();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jPanel1 = new javax.swing.JPanel();
        btnAnterior = new javax.swing.JButton();
        btnSiguiente = new javax.swing.JButton();
        btnEncabezado = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblHorarioSemanalR = new Tabla.AgendaTable(){
            @Override
            public boolean isCellEditable(int rowindex, int colindex){
                return false;
            }

        };//javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        lblFechaSel = new javax.swing.JLabel();
        btnGuardar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        cbMotivo = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        lblNombrePaciente = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();

        jLabel2.setText("jLabel2");

        jScrollPane2.setViewportView(jTree1);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 82, 118)));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnAnterior.setBackground(new java.awt.Color(255, 255, 255));
        btnAnterior.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/previous2.png"))); // NOI18N
        btnAnterior.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, new java.awt.Color(0, 0, 0), null));
        btnAnterior.setBorderPainted(false);
        btnAnterior.setContentAreaFilled(false);
        btnAnterior.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAnterior.setMaximumSize(new java.awt.Dimension(60, 60));
        btnAnterior.setMinimumSize(new java.awt.Dimension(60, 60));
        btnAnterior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnteriorActionPerformed(evt);
            }
        });
        jPanel1.add(btnAnterior, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 152, 60, 60));

        btnSiguiente.setBackground(new java.awt.Color(255, 255, 255));
        btnSiguiente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/next2.png"))); // NOI18N
        btnSiguiente.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, new java.awt.Color(0, 0, 0), null));
        btnSiguiente.setBorderPainted(false);
        btnSiguiente.setContentAreaFilled(false);
        btnSiguiente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSiguiente.setMaximumSize(new java.awt.Dimension(60, 60));
        btnSiguiente.setMinimumSize(new java.awt.Dimension(60, 60));
        btnSiguiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSiguienteActionPerformed(evt);
            }
        });
        jPanel1.add(btnSiguiente, new org.netbeans.lib.awtextra.AbsoluteConstraints(769, 152, 60, 60));

        btnEncabezado.setBackground(new java.awt.Color(255, 255, 255));
        btnEncabezado.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, java.awt.Color.black, null));
        btnEncabezado.setContentAreaFilled(false);
        btnEncabezado.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEncabezado.setMaximumSize(new java.awt.Dimension(60, 60));
        btnEncabezado.setMinimumSize(new java.awt.Dimension(60, 60));
        btnEncabezado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEncabezadoActionPerformed(evt);
            }
        });
        jPanel1.add(btnEncabezado, new org.netbeans.lib.awtextra.AbsoluteConstraints(76, 152, 683, 60));

        tblHorarioSemanalR.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tblHorarioSemanalR.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7"
            }
        ));
        tblHorarioSemanalR.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tblHorarioSemanalR.setEnabled(true);
        tblHorarioSemanalR.setGridColor(new java.awt.Color(21, 67, 96));
        tblHorarioSemanalR.setSelectionBackground(new java.awt.Color(41, 128, 185));
        tblHorarioSemanalR.getTableHeader().setReorderingAllowed(false);
        tblHorarioSemanalR.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                tblHorarioSemanalRMouseWheelMoved(evt);
            }
        });
        tblHorarioSemanalR.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblHorarioSemanalRMouseClicked(evt);
            }
        });
        tblHorarioSemanalR.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                tblHorarioSemanalRMouseMoved(evt);
            }
        });
        jScrollPane1.setViewportView(tblHorarioSemanalR);
        if (tblHorarioSemanalR.getColumnModel().getColumnCount() > 0) {
            tblHorarioSemanalR.getColumnModel().getColumn(0).setResizable(false);
            tblHorarioSemanalR.getColumnModel().getColumn(0).setPreferredWidth(50);
            tblHorarioSemanalR.getColumnModel().getColumn(1).setResizable(false);
            tblHorarioSemanalR.getColumnModel().getColumn(2).setResizable(false);
            tblHorarioSemanalR.getColumnModel().getColumn(3).setResizable(false);
            tblHorarioSemanalR.getColumnModel().getColumn(4).setResizable(false);
            tblHorarioSemanalR.getColumnModel().getColumn(5).setResizable(false);
            tblHorarioSemanalR.getColumnModel().getColumn(6).setResizable(false);
        }

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 218, 819, 439));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(21, 67, 96));
        jLabel1.setText("Fecha:");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 40, -1, -1));

        lblFechaSel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblFechaSel.setForeground(new java.awt.Color(21, 67, 96));
        jPanel1.add(lblFechaSel, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 60, 340, 30));

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/guardar_2.png"))); // NOI18N
        btnGuardar.setToolTipText("Guardar");
        btnGuardar.setContentAreaFilled(false);
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });
        jPanel1.add(btnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 40, 30, 30));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(21, 67, 96));
        jLabel3.setText("Motivo:");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, -1, -1));

        cbMotivo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Consulta", "Control", "Inicio Tratamiento(TTO)", "Limpieza", "Obturación", "Exodoncia" }));
        cbMotivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbMotivoActionPerformed(evt);
            }
        });
        jPanel1.add(cbMotivo, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 100, 150, 30));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(21, 67, 96));
        jLabel4.setText("Paciente:");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        lblNombrePaciente.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblNombrePaciente.setForeground(new java.awt.Color(21, 67, 96));
        lblNombrePaciente.setText("Richard ramos duran");
        jPanel1.add(lblNombrePaciente, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 60, 340, 30));

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
        jPanel5.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 0, 30, 30));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/cerrar_1.png"))); // NOI18N
        jLabel5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5MouseClicked(evt);
            }
        });
        jPanel5.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 0, 30, 30));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Asignacion de Cita");
        jPanel5.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 8, 152, -1));

        jPanel1.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 840, 30));
        jPanel1.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 90, 220, 10));
        jPanel1.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 90, 340, 10));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 840, 668));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEncabezadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEncabezadoActionPerformed
        this.dispose();
        System.out.println("btnEncabezadoActionPerformed --- FECHA ---->"+anio+"::"+mes+"::"+dia);
        new VentanaAgenda(paciente, npaciente, this).setVisible(true);
    }//GEN-LAST:event_btnEncabezadoActionPerformed

    private void tblHorarioSemanalRMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHorarioSemanalRMouseClicked
        
        int filaSeleccionada = tblHorarioSemanalR.getSelectedRow();
        int colaSeleccionada = tblHorarioSemanalR.getSelectedColumn();
        String fecha = lblFechaSel.getText();
        if(colaSeleccionada > 0){ 
            
            System.out.println("*******************tblHorarioSemanalRMouseClicked***********************");
            //String estado = getDiponibleCelda(paciente,filaSeleccionada, colaSeleccionada);//0--> disp, 1 --> ocu, 2-->con el paciente
            String est = getCeldaDisponible(filaSeleccionada, colaSeleccionada);
            System.out.println("estado-->"+est);
            System.out.println("richard");
            if(!est.equals("-1")){                
                String[] dat = est.split("<>");
                boolean fecAnt= VerificarFechas(dat[1]);
                if(fecAnt){
                    System.out.println("est------->"+est);
                    System.out.println("paciente------->"+paciente);
                    if(dat[0].equals("1")){///CUANDO SELECCIONO Y ESTA EN BLANCO
                        if(!paciente.equals(dat[3])){
                            fechaSeleccionada = (fechaSeleccionada.equals("")?dat[1]:fechaSeleccionada);
                            System.out.println("fechaSeleccionada----->"+fechaSeleccionada+"    dat[1]-->"+dat[1]);
                            //tblHorarioSemanalR.setValueAt("Sele", filaSeleccionada, colaSeleccionada);
                            if(!fechaSeleccionada.equals(dat[1])){///FECHA DIFERENTE
                                CambiarEstadoCelda();
                                fechaSeleccionada = dat[1];                        
                                horasSeleccionada.clear();  
                                //horasSeleccionada.add(dat[2]);
                            }
                            System.out.println("fecha--->"+fechaSeleccionada);
                            for(String hos:horasSeleccionada){
                                System.out.println("hor--->"+hos);
                            }
                            System.out.println("*************ANTES VALIDAR HORA TABLA***************");
                            ValidarHoraTablaN(dat[2], colaSeleccionada, filaSeleccionada);

                            OrdenarHoras();
                        }else{
                            JOptionPane.showMessageDialog(null, "El paciente ya se encuentra asignado para la hora seleccionada.", "Mensaje", JOptionPane.OK_OPTION);
                        }
                    }else if(dat[0].equals("3")){//CUANDO ESTA SELECCIONADO Y NO ESTA EN BLANCO
                        tblHorarioSemanalR.setValueAt("", filaSeleccionada, colaSeleccionada);
                        QuitarHoraLista(filaSeleccionada, colaSeleccionada);  
                        if(!dat[3].equals("")){
                            tblHorarioSemanalR.setValueAt("OCUPADO", filaSeleccionada, colaSeleccionada);
                        }
                    }
                }
            }
            
            MostarFecha();
            
        }
    }//GEN-LAST:event_tblHorarioSemanalRMouseClicked
     
   
    private void btnSiguienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSiguienteActionPerformed
        SimpleDateFormat sdf = new SimpleDateFormat("d/MMMM/yyyy");
        // TODO add your handling code here:
        Calendar cal = Calendar.getInstance();
        cal.set(Integer.parseInt(SemInianio), Integer.parseInt(SemIniid_mes)-1, Integer.parseInt(SemInidia));
        
        
        cal.add(Calendar.DAY_OF_YEAR, 7);
        
        SimpleDateFormat sdfa = new SimpleDateFormat("yyyy");
        SimpleDateFormat sdfm = new SimpleDateFormat("MMMM");
        SimpleDateFormat sdfd = new SimpleDateFormat("d");
        System.out.println("AÑO------>"+sdfa.format(cal.getTime()));
        anio = sdfa.format(cal.getTime());
        mes = sdfm.format(cal.getTime());
        dia = sdfd.format(cal.getTime());
        id_mes = getIdMes(mes);        
        nombreColumnas = new String[]{"Hora", "Lu", "Ma", "Mi", "Ju", "Vi", "Sa"};
        GenerarCalendarioxHoras();
        
    }//GEN-LAST:event_btnSiguienteActionPerformed

    private void btnAnteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnteriorActionPerformed
        // TODO add your handling code here:
        SimpleDateFormat sdf = new SimpleDateFormat("d/MMMM/yyyy");
        String fecha = SemInianio+"/"+SemIniid_mes+"/"+SemInidia;
        Calendar cal =Calendar.getInstance();
        cal.set(Integer.parseInt(SemInianio), Integer.parseInt(SemIniid_mes)-1, Integer.parseInt(SemInidia));
        
        cal.add(Calendar.DAY_OF_YEAR, -7);
        SimpleDateFormat sdfa = new SimpleDateFormat("yyyy");
        SimpleDateFormat sdfm = new SimpleDateFormat("MMMM");
        SimpleDateFormat sdfd = new SimpleDateFormat("d");
        
        
        anio = sdfa.format(cal.getTime());
        mes = sdfm.format(cal.getTime());
        dia = sdfd.format(cal.getTime());
        id_mes = getIdMes(mes);      
        
        nombreColumnas = new String[]{"Hora", "Lu", "Ma", "Mi", "Ju", "Vi", "Sa"};
        GenerarCalendarioxHoras();
    }//GEN-LAST:event_btnAnteriorActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        try{
            Calendar cal =Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdfa = new SimpleDateFormat("YYYY");
            SimpleDateFormat sdfm = new SimpleDateFormat("MMMM");
            SimpleDateFormat sdfd = new SimpleDateFormat("d");

            String a = sdfa.format(cal.getTime());
            String m = sdfm.format(cal.getTime());
            String d = sdfd.format(cal.getTime());
            if(!fechaSeleccionada.equals("")){
//                System.out.println("fecha--->"+fechaSeleccionada);
//                System.out.println("horasSeleccionada--->"+horasSeleccionada);
                m = getIdMes(m); 
                String fec = a+"-"+m+"-"+d;
                Date fechAct = sdf.parse(fec);
                Date fechEsc = sdf.parse(fechaSeleccionada);
                int comp = fechAct.compareTo(fechEsc);

                String mot = cbMotivo.getSelectedItem().toString();

                boolean ingr = cita.CrearCita(fechaSeleccionada, horasSeleccionada, paciente, mot, pkcita,  "");
                System.out.println("ingr--"+ingr);
                if(ingr){
                    this.dispose();
                    if(vcts != null){
                        vcts.ConsultarCitas(); 
                    }else if(vconf != null){
                        
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Ocurrio un error al momento de realizar la operación.");
                    //this.dispose();
                }
            }else{
                JOptionPane.showMessageDialog(null, "Por favor seleccione una fecha valida para realizar la operación.");
            }
            
           
        }catch(Exception e){
            
            System.out.println("ERROR GUARDAR-->"+e.toString());
        }
        
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void cbMotivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbMotivoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbMotivoActionPerformed

    private void jLabel7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseClicked
        this.setState(ventanaDiasxHoras.ICONIFIED);
    }//GEN-LAST:event_jLabel7MouseClicked

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
        this.dispose();
    }//GEN-LAST:event_jLabel5MouseClicked

    private void jPanel5MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel5MousePressed
        x = evt.getX();
        y = evt.getY();
    }//GEN-LAST:event_jPanel5MousePressed

    private void jPanel5MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel5MouseDragged
        this.setLocation(this.getLocation().x + evt.getX()-x, this.getLocation().y + evt.getY()-y);
    }//GEN-LAST:event_jPanel5MouseDragged

    private void tblHorarioSemanalRMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHorarioSemanalRMouseMoved
        int fila = tblHorarioSemanalR.rowAtPoint(evt.getPoint());
        int filaa = -1;
        int columna = tblHorarioSemanalR.columnAtPoint(evt.getPoint());
        //System.out.println("tblHorarioSemanalRMouseMoved---->"+fila);
        
        if (fila > -1) {
            //se quitan todas las selecciones
            tblHorarioSemanalR.clearSelection();
            
            tblHorarioSemanalR.setSelectionBackground(Color.green);
            tblHorarioSemanalR.setRowSelectionInterval(fila, fila);
            String dia =  ""+nombreColumnas[columna];
            String hora = ""+tblHorarioSemanalR.getModel().getValueAt(fila, 0);
            String html = "<html><head></head><body style='background:#ffffff; border:none; margin:0; padding: 0 5px;'><p>" + dia + "<br>"+hora+"</p></body></html>";
            tblHorarioSemanalR.setToolTipText(""+html);
            
        } else {
            tblHorarioSemanalR.setSelectionBackground(Color.white); 
        }
        
        if(fila != filaa){
            filaa = fila;
//            
            
            
        }
        
        
        //tblHorarioSemanalR.setToolTipText("hola");
    }//GEN-LAST:event_tblHorarioSemanalRMouseMoved

    private void tblHorarioSemanalRMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_tblHorarioSemanalRMouseWheelMoved
        
    }//GEN-LAST:event_tblHorarioSemanalRMouseWheelMoved

    
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
            java.util.logging.Logger.getLogger(ventanaDiasxHoras.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ventanaDiasxHoras.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ventanaDiasxHoras.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ventanaDiasxHoras.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ventanaDiasxHoras(null, null, null, null, null).setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAnterior;
    private javax.swing.JButton btnEncabezado;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnSiguiente;
    private javax.swing.JComboBox cbMotivo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    public javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTree jTree1;
    private javax.swing.JLabel lblFechaSel;
    private javax.swing.JLabel lblNombrePaciente;
    private javax.swing.JTable tblHorarioSemanalR;
    // End of variables declaration//GEN-END:variables

    private void CargarCalendario() {
        try{
            int numFilas = 7;
            int num = 7;
        }catch(Exception e){
            
        }
    }
    
    public String getDatosSemana(){
        
        return "";
    }
    
    public void GenerarCalendarioxHorasO(){
        try{
            
            String Enc = getEncabezado();
            
            btnEncabezado.setText(Enc);
            
            Calendar cal =Calendar.getInstance();            
            cal.set(Integer.parseInt(SemInianio), Integer.parseInt(SemIniid_mes)-1, Integer.parseInt(SemInidia));
            
            SimpleDateFormat sdfd = new SimpleDateFormat("d");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String[] infDiasS = new String[6];
            for(int i = 1; i <= 6; i++){
                String dia  = sdfd.format(cal.getTime());
                infDiasS[i-1] = sdf.format(cal.getTime());
                nombreColumnas[i] += " "+dia;
                cal.add(Calendar.DAY_OF_MONTH, 1);
            }
            modelo = new DefaultTableModel(nombreColumnas, 0);
            
            tblHorarioSemanalR.setModel(modelo);
            DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
            tcr.setHorizontalAlignment(SwingConstants.CENTER);
            
//            DefaultTableCellHeaderRenderer thcr = new DefaultTableCellHeaderRenderer();
//            tcr.setHorizontalAlignment(SwingConstants.CENTER);

            
            tblHorarioSemanalR.getColumnModel().getColumn(0).setPreferredWidth(20);
            for(int i = 0; i < modelo.getColumnCount(); i++){                
                tblHorarioSemanalR.getColumnModel().getColumn(i).setResizable(false);
                tblHorarioSemanalR.getColumnModel().getColumn(i).setCellRenderer(tcr);
//                tblHorarioSemanalR.getColumnModel().getColumn(i).setHeaderRenderer(thcr);
            }
            
            ArrayList<Cita> Lista_Calendario = cita.getCitasSemanales(SemInianio+"-"+SemIniid_mes+"-"+SemInidia, SemFinanio+"-"+SemFinid_mes+"-"+SemFindia);
            ArrayList<String[]> Lista_DNulo = cita.getDiasNulos(SemInianio+"-"+SemIniid_mes+"-"+SemInidia, SemFinanio+"-"+SemFinid_mes+"-"+SemFindia);
            
            //CONTENIDO
            int minAu = 20;
            int hora = 6;
            int min = 40;
            
            //for(int i = 0; i < 13; i ++){
            String HoraDig = "";
            int i = 0, fil = -1;
            String[] datos;
            while(i < 13){
                datos = new String[] {"", "", "", "", "", "", ""};
                fil++;
                if(min==60){
                    i++;
                    hora +=1; 
                    min = 0;
                }
                HoraDig = (String.valueOf(hora).length() == 1?"0"+hora:hora)+":"+(String .valueOf(min).length()==1?"0"+min:min);
                datos[0] = HoraDig;
                for(int j = 0; j < infDiasS.length; j++){
//                    String[] pac = getPaciente(Lista_Calendario,HoraDig, infDiasS[j]);
//                    if(pac!= null){
//                        datos[j+1] = pac[1];
                        //ListaCitaAux.add(new String[]{pac[0], ""+fil, ""+(j+1), pac[2], infDiasS[j], HoraDig});
//                    }else{
//                        datos[j+1] = ""; 
//                    }                    
                }
                
                //agregarFila(new String[]{HoraDig, "", "", "", "", "", ""});                
                agregarFila(datos);                
                min += minAu;
            }
            
        }catch(Exception e){
            System.out.println("ERROR GenerarCalendarioxHoras--> "+e.toString());
        }
    }
    
    public void GenerarCalendarioxHoras(){
        try{
            ListaNuloAux.clear();
            ListaCitaAux.clear();
            String Enc = getEncabezado();
            
            btnEncabezado.setText(Enc);
            
            Calendar cal =Calendar.getInstance();            
            cal.set(Integer.parseInt(SemInianio), Integer.parseInt(SemIniid_mes)-1, Integer.parseInt(SemInidia));
            
            SimpleDateFormat sdfd = new SimpleDateFormat("d");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            
            for(int i = 1; i <= 6; i++){
                String dia  = sdfd.format(cal.getTime());
                infDiasS[i-1] = sdf.format(cal.getTime());
                nombreColumnas[i] += " "+dia;
                cal.add(Calendar.DAY_OF_MONTH, 1);
            }
            modelo = new DefaultTableModel(nombreColumnas, 0);
            
            tblHorarioSemanalR.setModel(modelo);
            DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
            DefaultTableCellRenderer tcc = new DefaultTableCellRenderer();
            tcr.setHorizontalAlignment(SwingConstants.RIGHT);
            tcc.setHorizontalAlignment(SwingConstants.CENTER);
            
//            DefaultTableCellHeaderRenderer thcr = new DefaultTableCellHeaderRenderer();
//            tcr.setHorizontalAlignment(SwingConstants.CENTER);

            
            tblHorarioSemanalR.getColumnModel().getColumn(0).setPreferredWidth(25);
            for(int i = 0; i < modelo.getColumnCount(); i++){                
                tblHorarioSemanalR.getColumnModel().getColumn(i).setResizable(false);
                if(i == 0)
                    tblHorarioSemanalR.getColumnModel().getColumn(i).setCellRenderer(tcr);
                else
                    tblHorarioSemanalR.getColumnModel().getColumn(i).setCellRenderer(tcc);
//                tblHorarioSemanalR.getColumnModel().getColumn(i).setHeaderRenderer(thcr);
            }
            JTableHeader header = tblHorarioSemanalR.getTableHeader();
            ((DefaultTableCellRenderer)header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
            Lista_Calendario = cita.getCitasSemanales(SemInianio+"-"+SemIniid_mes+"-"+SemInidia, SemFinanio+"-"+SemFinid_mes+"-"+SemFindia);
            ArrayList<String[]> Lista_DNulo = cita.getDiasNulos(SemInianio+"-"+SemIniid_mes+"-"+SemInidia, SemFinanio+"-"+SemFinid_mes+"-"+SemFindia);
            
            //CONTENIDO
            int minAu = 20;
            int hora = 6;
            int min = 40;
            
            //for(int i = 0; i < 13; i ++){
            String HoraDig = "";
            int i = 0, fil = -1;
            String[] datos;
            while(i < 13){
                datos = new String[] {"", "", "", "", "", "", ""};
                fil++;
                if(min==60){
                    i++;
                    hora +=1; 
                    min = 0;
                }
                HoraDig = (String.valueOf(hora).length() == 1?"0"+hora:hora)+":"+(String .valueOf(min).length()==1?"0"+min:min);
                datos[0] = getHora(HoraDig);
               
                horasTabla[fil] = HoraDig;
                String idPac = "";
                for(int j = 0; j < infDiasS.length; j++){
                    String nulo = getDiaNulo(Lista_DNulo, HoraDig, infDiasS[j]);
                    if(nulo.equals("")){
                        String ocupado = getPaciente(Lista_Calendario,HoraDig, infDiasS[j]);
                        //String[] pac = getPaciente(Lista_Calendario,HoraDig, infDiasS[j]);
                        if(!ocupado.equals("")){
                            datos[j+1] = ocupado.split("<>")[0];    
                            idPac = ocupado.split("<>")[1];    
                        }else{
                            datos[j+1] = "";
                            idPac = "";
                        }
                        ListaCitaAux.add(new String[]{""+fil, ""+(j+1), infDiasS[j], HoraDig, idPac});
                    }else{
                        datos[j+1] = nulo;                    
                        ListaNuloAux.add(new String[]{""+fil, ""+(j+1), infDiasS[j], HoraDig});
                    }                 
                }
                             
                agregarFila(datos);                
                min += minAu;
            }
            System.out.println("Filas-->"+(fil+1));
            
        }catch(Exception e){
            System.out.println("ERROR GenerarCalendarioxHoras--> "+e.toString());
        }
    }
    
     

    private String getIdMes(String mes) {
        String ret = "";
        for(int  i = 0; i < Meses.length; i++){
            if(Meses[i].equalsIgnoreCase(mes)){
                ret = ""+(i+1);
                break;
            }
        }
        return ret;
    }

    private int getDiaSemana(String dato) {
        int ret = 0;
        for(int  i = 0; i < dias.length; i++){
            if(dias[i].equalsIgnoreCase(dato)){
                ret = (i+1);
                break;
            }
        }
        return ret;
    }
    
    
    public String getDiponibleCelda(String pac, int fila, int col){
        String ret="0";
        try{
            for(int i = 0; i < ListaCitaAux.size(); i++){
                String[] info = ListaCitaAux.get(i); // 0-->pac, 1-->fila, 2-->col, 3--> IdCita             
                if(info[1].equals(""+fila) && info[2].equals(""+col)){
                    if(info[0].equals(pac)){
                        ret = "2::"+info[3]+"::"+info[4]+"::"+info[5];
                    }else if(info[0].equals("")){
                        ret = "0::"+info[3]+"::"+info[4]+"::"+info[5];
                    }else{
                        ret = "1::";
                    }                    
                }
            }            
        }catch(Exception e){
            System.out.println("Error getDiponibleCelda-->"+e.toString());
        }
        return ret;
    }

    private String getPaciente(ArrayList<Cita> Lista_Calendario, String hora, String fecha) {
        try{
            String ret = "";
            //System.out.println("*******************getPaciente******"+Lista_Calendario.size()+"******"+hora+"*****"+fecha+"**");
            for(int i = 0; i < Lista_Calendario.size(); i++){
                if(Lista_Calendario.get(i).getFecha().equals(fecha) && Lista_Calendario.get(i).getHora().equals(hora)){
                    ret = "OCUPADO<>"+Lista_Calendario.get(i).getId()+"<>END";
                    break;
                }
            }
            //System.out.println("****************END   //"+ret+"// ********************");
            return ret;
        }catch(Exception e){
            return "";
        }
    }

    private String getDiaNulo(ArrayList<String[]> Lista_DNulo, String hora, String fecha) {
        try{
            String ret = "";
            for(int i = 0; i < Lista_DNulo.size(); i++){
                if(Lista_DNulo.get(i)[0].equals(fecha) && Lista_DNulo.get(i)[1].equals(hora)){
                    ret = "NO DISPONIBLE";
                    break;
                }
            }
            return ret;
        }catch(Exception e){
            return "";
        }
    }
    
    private String getCeldaDisponible(int fila, int col) {
        try{
            System.out.println("**************getCeldaDisponible***************");
            String ret = "", dat ="";
            String[] info = null;
            System.out.println("fila---->"+fila);
            System.out.println("col---->"+col);  
            System.out.println("ListaNuloAux---->"+ListaNuloAux.size());
//            System.out.println("ListaNuloAux.get(0)0-->"+ListaNuloAux.get(0)[0]);
//            System.out.println("ListaNuloAux.get(0)1-->"+ListaNuloAux.get(0)[1]);
            for(int i = 0; i < ListaNuloAux.size(); i++){
                info = ListaNuloAux.get(i);
                System.out.println("****************");
                if(ListaNuloAux.get(i)[0].equals(""+fila) && ListaNuloAux.get(i)[1].equals(""+col)){
                    ret = "-1";
                    break;
                }
            }
            System.out.println("ret-->"+ret);
            if(ret.equals("")){
                
                for(int i = 0; i < ListaCitaAux.size(); i++){
                    info = ListaCitaAux.get(i); // 0-->pac, 1-->fila, 2-->col, 3--> IdCita   
                    
                    dat = ""+tblHorarioSemanalR.getValueAt(fila, col);
                    if(info[0].equals(""+fila) && info[1].equals(""+col) && !dat.equals("SELECCIONADO")){
                        ret = "1<>"+info[2]+"<>"+info[3]+"<>"+info[4]+"<>END";
                    }else if(info[0].equals(""+fila) && info[1].equals(""+col) && dat.equals("SELECCIONADO")){
                        ret = "3<>"+info[2]+"<>"+info[3]+"<>"+info[4]+"<>END";
                    }
                }    
            }
            System.out.println("ret-->"+ret);
            return ret;
        }catch(Exception e){
            System.out.println("ERROR --getCeldaDisponible-- "+e.toString());
            return "";
        }
    }

    private void ValidarHoraTabla(String hora, int col, int fil) {
        try{
            int tam = horasSeleccionada.size();
            System.out.println("validarHORATABLA------- "+hora+"  col  "+ col+"   tam--> "+tam);
            if(horasSeleccionada.size()>0){
                int pos = -1, paux = -1;
                int posH = getposicionHora(hora);
                System.out.println("posH-->"+posH);
                int min = -1, max = -1;
                for(int i = 0; i < horasSeleccionada.size(); i++){
                    pos = getposicionHora(horasSeleccionada.get(i));
                    min = min == -1?pos:min;
                    max = max == -1?pos:max;
                    
                    if(min < pos){
                        min = pos;
                    }
                    if(pos > max){
                        max = pos;
                    }                    
                }
                
                if((min-1==posH || max+1 == posH) && tam <numSel){
                    tblHorarioSemanalR.setValueAt("Sele", fil, col);
                    horasSeleccionada.add(hora);
                }else{
                    int difm = min - posH;
                    int difM = posH - max;
                    
                    if(difm <= numSel-tam && difm > 0){//ANTES
                        for(int i = posH; i < min; i++){
                            horasSeleccionada.add(horasTabla[i]);
                            tblHorarioSemanalR.setValueAt("Sele", i, col);
                        }
                    } 
                    
                    if(difM <= numSel-tam && difM > 0){//DESPUES
                        
                        for(int i = max+1; i <= posH; i++){
                            horasSeleccionada.add(horasTabla[i]);
                            tblHorarioSemanalR.setValueAt("Sele", i, col);
                        }
                    }
                }
                
            }else{
                tblHorarioSemanalR.setValueAt("Sele", fil, col);
                horasSeleccionada.add(hora);
                        
            }
        }catch(Exception e){
            System.out.println("ERROR ValidarHoraTabla    "+e.toString());
        }
    }
    private void ValidarHoraTablaN(String hora, int col, int fil) {
        try{
            int tam = horasSeleccionada.size();
            System.out.println("validarHORATABLA------- "+hora+"  col  "+ col+"   tam--> "+tam);
            if(horasSeleccionada.size()>0){
                int pos = -1, paux = -1;
                int posH = getposicionHora(hora);
                System.out.println("posH-->"+posH);
                int min = -1, max = -1;
                for(int i = 0; i < horasSeleccionada.size(); i++){
                    pos = getposicionHora(horasSeleccionada.get(i));
                    min = min == -1?pos:min;
                    max = max == -1?pos:max;
                    
                    if(min < pos){
                        min = pos;
                    }
                    if(pos > max){
                        max = pos;
                    }                    
                }
                
                if((min-1==posH || max+1 == posH) && tam <numSel){
                    tblHorarioSemanalR.setValueAt("SELECCIONADO", fil, col);
                    horasSeleccionada.add(hora);
                }else{
                    int difm = min - posH;
                    int difM = posH - max;
                    
                    if(difm <= numSel-tam && difm > 0){//ANTES
                        for(int i = posH; i < min; i++){
                            horasSeleccionada.add(horasTabla[i]);
                            tblHorarioSemanalR.setValueAt("SELECCIONADO", i, col);
                        }
                    } 
                    
                    if(difM <= numSel-tam && difM > 0){//DESPUES
                        
                        for(int i = max+1; i <= posH; i++){
                            horasSeleccionada.add(horasTabla[i]);
                            tblHorarioSemanalR.setValueAt("SELECCIONADO", i, col);
                        }
                    }
                }
                
            }else{
                tblHorarioSemanalR.setValueAt("SELECCIONADO", fil, col);
                horasSeleccionada.add(hora);
                        
            }
        }catch(Exception e){
            System.out.println("ERROR ValidarHoraTabla    "+e.toString());
        }
    }
    
    public int getposicionHora(String hora){
        int  pos = -1;
        for(int i = 0; i < horasTabla.length; i++){
            if(horasTabla[i].equals(hora)){
                pos = i;
                break;
            }
        }
        return pos;
    }
    
    private void OrdenarHoras() {
        try{
            String aux = "";
            int posi = -1, posj = -1;
            if(horasSeleccionada.size()>0){
                for(int i = 0; i < horasSeleccionada.size(); i++){
                    
                    for(int j = i+1; j < horasSeleccionada.size(); j++){
                        posi = getposicionHora(horasSeleccionada.get(i));
                        posj = getposicionHora(horasSeleccionada.get(j));
                        if( posj < posi){ 
                           aux = horasSeleccionada.get(i);
                           horasSeleccionada.set(i, horasSeleccionada.get(j));
                           horasSeleccionada.set(j, aux);
                        }
                    }
                }
            }
        }catch(Exception e){
            System.out.println("ERROR ----OrdenarHoras--- "+e.toString());
        }
    }

    private void CambiarEstadoCelda() {
        try{
            String fec = fechaSeleccionada;
            String col = "-1", fil = "-1";
            int i = 0;
            for(String d:infDiasS){
                i++;
                if(d.equals(fec)){
                    col = ""+i;
                    break;
                }
            }
            
            System.out.println("col---->"+col);
            
            for(String hrs: horasSeleccionada){
                fil = "-1";
                String ocupado = getPaciente(Lista_Calendario,hrs, fec);
                for(int j = 0; j < horasTabla.length; j++){
                    if(hrs.equals(horasTabla[j])){
                        fil = ""+j;
                        break;
                    }
                }
                if(!fil.equals("-1")){
                    tblHorarioSemanalR.setValueAt(""+(ocupado.indexOf("<>")>-1?ocupado.split("<>")[0]:""), Integer.parseInt(fil), Integer.parseInt(col));
                }
               
            }
        }catch(Exception e){
            System.out.println("ERROR CambiarEstadoCelda--->"+e.toString());
        }
    }

    private void QuitarHoraLista(int fila, int col) {
        try{
            System.out.println("*****************************************QuitarHoraLista*************"+fila+"**************"+col+"*************************");
            String hora = horasTabla[fila];
            System.out.println("hora-->"+hora);
            System.out.println("horasSeleccionada.size()---->"+horasSeleccionada.size());
            for(int i = 0; i < horasSeleccionada.size(); i++){
                System.out.println("horasSeleccionada.get(i)-->"+horasSeleccionada.get(i));
                if(horasSeleccionada.get(i).equals(hora)){
                    horasSeleccionada.remove(i);
                }
            }
            if(horasSeleccionada.size()==0){
                fechaSeleccionada = "";
            }
        }catch(Exception e){
            System.out.println("ERROR QuitarHoraLista---"+e.toString());
        }
    }

    private void MostarFecha() {
        try{
            String fecha = (fechaSeleccionada.equals("")?"":fechaSeleccionada+"   ");
            
            
            fecha+= (horasSeleccionada.size()==0?"":horasSeleccionada.size()==1?horasSeleccionada.get(0):horasSeleccionada.get(0)+" - "+horasSeleccionada.get(horasSeleccionada.size()-1));
            
            lblFechaSel.setText(fecha);
        }catch(Exception e){
            System.out.println("ERROR --MostarFecha-- "+e.toString());
        }
    }

     private void agregarFila(String[] fila) {
        modelo.addRow(fila);
        
    }

    private String getEncabezado() {
        String retorno = "";
        try{
            System.out.println("*******************getEncabezado***************");
            SimpleDateFormat sdfnd = new SimpleDateFormat("EEEE");
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");
            
            SimpleDateFormat sdfai = new SimpleDateFormat("yyyy");
            SimpleDateFormat sdfidmi = new SimpleDateFormat("M");
            SimpleDateFormat sdfmi = new SimpleDateFormat("MMMM");
            SimpleDateFormat sdfdi = new SimpleDateFormat("d");
            
            System.out.println("anio--->"+anio);
            System.out.println("mes--->"+mes);
            System.out.println("idmes--->"+id_mes);
            System.out.println("dia--->"+dia);
            
            Calendar cal =Calendar.getInstance();          
            
            cal.set(Integer.parseInt(anio), Integer.parseInt(id_mes)-1, Integer.parseInt(dia));
            
            Calendar calIni = Calendar.getInstance();
            calIni.set(Integer.parseInt(anio), Integer.parseInt(id_mes)-1, Integer.parseInt(dia));
            Calendar calFin = Calendar.getInstance();
            calFin.set(Integer.parseInt(anio), Integer.parseInt(id_mes)-1, Integer.parseInt(dia));
            
            int diaenSem = getDiaSemana(sdfnd.format(cal.getTime()));
            
             System.out.println("SemInianio--->"+SemInianio);
            System.out.println("SemInimes--->"+SemInimes);
            System.out.println("SemIniid_mes--->"+SemIniid_mes);
            System.out.println("SemInidia--->"+SemInidia);
            
            System.out.println("SemFinanio--->"+SemFinanio);
            System.out.println("SemFinmes--->"+SemFinmes);
            System.out.println("SemFinid_mes--->"+SemFinid_mes);
            System.out.println("SemFindia--->"+SemFindia);
            
            System.out.println("*****************************+");
            
            System.out.println("dia-->"+diaenSem);
            int ini = 0;
            int fin = 0;
            
            ini = diaenSem - 1;                 
            fin = 7 - diaenSem;
                                  
            calIni.add(Calendar.DAY_OF_MONTH, -ini);
            calFin.add(Calendar.DAY_OF_MONTH, fin);
            
            SemInianio = sdfai.format(calIni.getTime());
            SemIniid_mes = sdfidmi.format(calIni.getTime());
            SemInimes = sdfmi.format(calIni.getTime());
            SemInidia = sdfdi.format(calIni.getTime());
            
            SemFinanio = sdfai.format(calFin.getTime());
            SemFinid_mes = sdfidmi.format(calFin.getTime());
            SemFinmes = sdfmi.format(calFin.getTime());
            SemFindia = sdfdi.format(calFin.getTime());
            
            System.out.println("SemInianio--->"+SemInianio);
            System.out.println("SemInimes--->"+SemInimes);
            System.out.println("SemIniid_mes--->"+SemIniid_mes);
            System.out.println("SemInidia--->"+SemInidia);
            
            System.out.println("SemFinanio--->"+SemFinanio);
            System.out.println("SemFinmes--->"+SemFinmes);
            System.out.println("SemFinid_mes--->"+SemFinid_mes);
            System.out.println("SemFindia--->"+SemFindia);
            
            retorno = "Semana del ";
            if(SemIniid_mes.equals(SemFinid_mes)){
                retorno = SemInidia+" al "+SemFindia+" de "+SemInimes+" del "+SemInianio;
            }else if(!SemIniid_mes.equals(SemFinid_mes) && SemInianio.equals(SemFinanio)){
                retorno = SemInidia+" de "+SemInimes+" al "+SemFindia+" de "+SemFinmes+" del "+SemInianio;
            }else if(!SemIniid_mes.equals(SemFinid_mes) && !SemInianio.equals(SemFinanio)){
                retorno = SemInidia+" de "+SemInimes+" del "+SemInianio+" al "+SemFindia+" de "+SemFinmes+" del "+SemFinanio;
            }         
            
            System.out.println("*******************FIN ENCABEZADO********************++");
            
        }catch(Exception e){
            System.out.println("ERROR-getEncabezado->"+e.toString());
            
        }
        return retorno.toUpperCase();
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

    private boolean VerificarFechas(String fechaSeleccionada){
        try{
            boolean ret = true;
            System.out.println("fechaSeleccionada--->"+fechaSeleccionada);
            System.out.println("anio_act--->"+anio_act);
            System.out.println("fechaSeleccionada.split(\"-\")[0]-->"+fechaSeleccionada.split("-")[0]);
            System.out.println("condicion-->"+(Integer.parseInt(fechaSeleccionada.split("-")[0]) < Integer.parseInt(anio_act)));
            if(Integer.parseInt(fechaSeleccionada.split("-")[0]) < Integer.parseInt(anio_act)){
                ret = false;
            }else{
                System.out.println("id_mes_act--->"+id_mes_act);
                System.out.println("fechaSeleccionada.split(\"-\")[1]-->"+fechaSeleccionada.split("-")[1]);
                System.out.println("CONDICION 2"+(Integer.parseInt(fechaSeleccionada.split("-")[0]) == Integer.parseInt(anio_act) && 
                    Integer.parseInt(fechaSeleccionada.split("-")[1]) < Integer.parseInt(id_mes_act)));
                if(Integer.parseInt(fechaSeleccionada.split("-")[0]) == Integer.parseInt(anio_act) && 
                    Integer.parseInt(fechaSeleccionada.split("-")[1]) < Integer.parseInt(id_mes_act)){
                        ret = false;
                    
                }else{
                    System.out.println("dia_act--->"+dia_act);
                    System.out.println("fechaSeleccionada.split(\"-\")[2]-->"+fechaSeleccionada.split("-")[2]);
                    System.out.println("CONDICION 3"+(Integer.parseInt(fechaSeleccionada.split("-")[1]) == Integer.parseInt(id_mes_act) && 
                        Integer.parseInt(fechaSeleccionada.split("-")[2]) < Integer.parseInt(dia_act)));
                    if( Integer.parseInt(fechaSeleccionada.split("-")[0]) == Integer.parseInt(anio_act) &&
                        Integer.parseInt(fechaSeleccionada.split("-")[1]) == Integer.parseInt(id_mes_act) && 
                        Integer.parseInt(fechaSeleccionada.split("-")[2]) < Integer.parseInt(dia_act)){
                                ret = false;
                    }
                }
            }
            
            System.out.println("ret -->"+ret);
             
            return ret;
        }catch(Exception e){
            return false;
        }
    }

    private void EstadoMotivo(String motivo) {
        int index = -1;
        String mot = "";
        System.out.println("total--->"+cbMotivo.getItemCount());
        for(int i = 0; i < cbMotivo.getItemCount(); i++){
            mot = ""+cbMotivo.getItemAt(i);
            if(mot.equals(motivo)){
                index = i;
                break;
            }
        }
        if(index > -1){
            cbMotivo.setSelectedIndex(index);
        }else{
            cbMotivo.setSelectedIndex(0);
        }
    }

}

