package HISTORIA_CLINICA;

import BaseDeDatos.gestorMySQL;
import Busquedas.ventanaBusquedapacienteGralrsrd;
import Informes.PlantillasAdicionales;
import Utilidades.*;
import Vistas.VentanaPacienteAux;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.*;
import Vistas.VentanaPrincipal;
import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

public class ventana extends javax.swing.JFrame {

    public Odontograma odontograma;
    public Imagenes imagenes;
    public volatile DefaultTableModel tModelo;
    public DefaultTableModel tanaModelo;
    public VentanaPrincipal vp;
    public String extraccion;
    public String simetria;
    public String pronostico;
    public String desviacionLMS;
    public String desviacionLMI;
    public DefaultListModel modeloInterconsulta;
    public DefaultListModel modeloHabitos;
    public ArrayList<String[]> datosPaciente;
    public ArrayList<JRadioButton[]> swing;
    public String[] nombreColumnas;
    public String[] anamcolumnas;
    public static int[] indices;
    public SimpleDateFormat sdf;
    public Calendar cal;
    public boolean anamVacia;
    public String OtroAnam;
    public String[] idDatosBasicos;
    public String tipoPaciente;
    public ArrayList<String[]> tratamientosxpaciente;
    public ThseguimientoDelTratamiento psegtrat;
    public String pn = "", sn = "", pa = "", sa = "";
    public ArrayList<String[]> segtrat;
    private int x = 0, y = 0;
    public int bandbp = 0;
    public PlantillasAdicionales plantilla = new PlantillasAdicionales();

    public String pfkcita;
    public String remisiones;
    public int band;
    public ventanaBusquedapacienteGralrsrd ventbp;
    public boolean detenerHilosegTrat;

    public ventana(VentanaPrincipal ventprin) {
        initComponents();
        detenerHilosegTrat = false;
        Utilidades.EstablecerIcono(this);
        vp = ventprin;
        pfkcita = null;
        segtrat = new ArrayList<>();
        remisiones = null;

        vp.bandhc = 1;
        band = 0;
        this.setLocationRelativeTo(vp);
        tipoPaciente = "";
        //this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        odontograma = new Odontograma();
        datosPaciente = new ArrayList<>();
        tratamientosxpaciente = new ArrayList<>();
        swing = new ArrayList<>();
        cargarRB();
        cargarCBTratamientos(tratamientosxpaciente, 0);
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        cal = Calendar.getInstance();
        anamVacia = true;
        OtroAnam = "";
        idDatosBasicos = new String[]{};
        imagenes = new Imagenes(this);

        odontograma.setBounds(0, 0, this.getWidth(), this.getHeight());
        txtAnguloVertical.setText(slAnguloVertical.getValue() + "º");
        txtAnguloGoniano.setText(slAnguloGoniano.getValue() + "º");
        txtLongitudRama.setText(slLongitudRama.getValue() + " mm");
        txtAnguloII.setText(slAnguloII.getValue() + "º");
        modeloInterconsulta = new DefaultListModel();
        modeloHabitos = new DefaultListModel();
        tModelo = new DefaultTableModel();
        tanaModelo = new DefaultTableModel();
        txtInterconsulta.setModel(modeloInterconsulta);
        indices = new int[3];
        nombreColumnas = new String[]{
            "Nro",
            "Fecha",
            "Observaciones del Tratamiento",
            "Abono",
            "Saldo"
        };
        anamcolumnas = new String[]{
            "DATOS",
            "SI",
            "NO",
            "NO SABE"
        };
        int filas = 0;

        cargarTablaSegTrat(null);
        cargarTablaANAMNESIS(null);

        //<editor-fold defaultstate="collapsed" desc="CARGAR INTERUPTORES">
        btnSimetriaSi.setBackground(Color.lightGray);
        btnSimetriaSi.setForeground(new Color(60, 60, 60));
        btnSimetriaSi.setOpaque(true);
        btnSimetriaNo.setBackground(Color.white);
        btnSimetriaNo.setForeground(Color.darkGray);
        btnSimetriaNo.setOpaque(false);
        simetria = "Si";//si = 0; no = 1

        btnPronosticoB.setBackground(Color.lightGray);
        btnPronosticoB.setForeground(new Color(60, 60, 60));
        btnPronosticoB.setOpaque(true);
        btnPronosticoR.setBackground(Color.white);
        btnPronosticoR.setForeground(Color.darkGray);
        btnPronosticoR.setOpaque(false);
        pronostico = "Bueno";//bueno = 0; reservado = 1

        btnLMSDesviacionD.setBackground(new Color(27, 79, 114));
        btnLMSDesviacionD.setForeground(new Color(255, 255, 255));
//        btnLMSDesviacionD.setOpaque(true);
        btnLMSDesviacionI.setBackground(new Color(133, 193, 233));
        btnLMSDesviacionI.setForeground(new Color(27, 79, 114));
//        btnLMSDesviacionI.setOpaque(false);
        desviacionLMS = "D";//derecha = D; izquierda = I

        btnLMIDesviacionD.setBackground(Color.lightGray);
        btnLMIDesviacionD.setForeground(new Color(60, 60, 60));
        btnLMIDesviacionD.setOpaque(true);
        btnLMIDesviacionI.setBackground(Color.white);
        btnLMIDesviacionI.setForeground(Color.darkGray);
        btnLMIDesviacionI.setOpaque(false);
        desviacionLMI = "D";//derecha = D; izquierda = I
        //</editor-fold>

        verDesviacionLMS(false);
        verDesviacionLMI(false);

        creaPestanias();
        limpiarRB();

        habilitarTodosLosDatos(false);
        setEstadosBotonesDeControl(2);
        psegtrat = new ThseguimientoDelTratamiento(this);

    }

    public ventana(VentanaPrincipal ventprin, String tipydoc, String nombres, String apellidos, String PAOP, String pfk_cita) {
        initComponents();
        detenerHilosegTrat = false;
        Utilidades.EstablecerIcono(this);
        segtrat = new ArrayList<>();
        remisiones = null;

        vp = ventprin;
        this.setLocationRelativeTo(null);
        pfkcita = pfk_cita;
        band = 0;
        //this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        odontograma = new Odontograma();
        datosPaciente = new ArrayList<>();
        tratamientosxpaciente = new ArrayList<>();
        swing = new ArrayList<>();
        cargarRB();
        cargarCBTratamientos(tratamientosxpaciente, 0);
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        cal = Calendar.getInstance();
        vp.bandhc = 1;
        anamVacia = true;
        OtroAnam = "";
        idDatosBasicos = new String[]{};
        imagenes = new Imagenes(this);

        odontograma.setBounds(0, 0, this.getWidth(), this.getHeight());
        txtAnguloVertical.setText(slAnguloVertical.getValue() + "º");
        txtAnguloGoniano.setText(slAnguloGoniano.getValue() + "º");
        txtLongitudRama.setText(slLongitudRama.getValue() + " mm");
        txtAnguloII.setText(slAnguloII.getValue() + "º");
        modeloInterconsulta = new DefaultListModel();
        modeloHabitos = new DefaultListModel();
        tModelo = new DefaultTableModel();
        tanaModelo = new DefaultTableModel();
        txtInterconsulta.setModel(modeloInterconsulta);
        indices = new int[3];
        nombreColumnas = new String[]{
            "Nro",
            "Fecha",
            "Observaciones del Tratamiento",
            "Abono",
            "Saldo"
        };
        anamcolumnas = new String[]{
            "DATOS",
            "SI",
            "NO",
            "NO SABE"
        };
        int filas = 0;

        cargarTablaSegTrat(null);
        cargarTablaANAMNESIS(null);

        //<editor-fold defaultstate="collapsed" desc="CARGAR INTERUPTORES">
        btnSimetriaSi.setBackground(Color.lightGray);
        btnSimetriaSi.setForeground(new Color(60, 60, 60));
        btnSimetriaSi.setOpaque(true);
        btnSimetriaNo.setBackground(Color.white);
        btnSimetriaNo.setForeground(Color.darkGray);
        btnSimetriaNo.setOpaque(false);
        simetria = "Si";//si = 0; no = 1

        btnPronosticoB.setBackground(Color.lightGray);
        btnPronosticoB.setForeground(new Color(60, 60, 60));
        btnPronosticoB.setOpaque(true);
        btnPronosticoR.setBackground(Color.white);
        btnPronosticoR.setForeground(Color.darkGray);
        btnPronosticoR.setOpaque(false);
        pronostico = "Bueno";//bueno = 0; reservado = 1

        btnLMSDesviacionD.setBackground(Color.lightGray);
        btnLMSDesviacionD.setForeground(new Color(60, 60, 60));
        btnLMSDesviacionD.setOpaque(true);
        btnLMSDesviacionI.setBackground(Color.white);
        btnLMSDesviacionI.setForeground(Color.darkGray);
        btnLMSDesviacionI.setOpaque(false);
        desviacionLMS = "D";//derecha = D; izquierda = I

        btnLMIDesviacionD.setBackground(Color.lightGray);
        btnLMIDesviacionD.setForeground(new Color(60, 60, 60));
        btnLMIDesviacionD.setOpaque(true);
        btnLMIDesviacionI.setBackground(Color.white);
        btnLMIDesviacionI.setForeground(Color.darkGray);
        btnLMIDesviacionI.setOpaque(false);
        desviacionLMI = "D";//derecha = D; izquierda = I
        //</editor-fold>

        verDesviacionLMS(false);
        verDesviacionLMI(false);

        creaPestanias();
        limpiarRB();

        if (PAOP.equals("1")) {
            tipoPaciente = "p";
            Expresiones.setDocumentoTipoDoc(tipydoc, this);
            String tyd[] = Utilidades.obtenerDocumentoyTipoDoc(tipydoc);
            for (int i = 0; i < cbTipoDocumento.getItemCount(); i++) {
                if (cbTipoDocumento.getItemAt(i).equals(tyd[0])) {
                    cbTipoDocumento.setSelectedIndex(i);
                    break;
                }
            }
            txtDocumento.setText(tyd[1]);
            cargarHistoriaClinica(tipydoc);
            habilitarDatosPaciente();
            setEstadosBotonesDeControl(10);
            habilitarTodosLosDatos(false);
        } else {
            tipoPaciente = "a";
            txtIdPaciente.setText(tipydoc);
            imagenes.reiniciar();
            txtDocumento.setText("");
            cargarDatosPaciente("1");
            cbTipoDocumento.setSelectedIndex(0);
            cbTratamientos.setVisible(false);
//            txtNombreSegtrat.setVisible(false);
            cargarTablaSegTrat(null);
            habilitarTodosLosDatos(true);
            habilitarDatosPaciente();
            cargarTablaANAMNESIS(null);
            cargarIndices();//se busca el ultimo indice de la hc
            setEstadosBotonesDeControl(1);
        }
//        habilitarTodosLosDatos(false);
        txtNombres.setText(nombres);
        txtApellidos.setText(apellidos);

        ArrayList<String> nom = Utilidades.decodificarNombre(nombres);
        ArrayList<String> ape = Utilidades.decodificarNombre(apellidos);
        if (nom.size() >= 2) {
            pn = nom.get(0);
            sn = nom.get(1);
        } else {
            pn = nom.get(0);
            sn = "";
        }

        if (ape.size() >= 2) {
            pa = ape.get(0);
            sa = ape.get(1);
        } else {
            pa = ape.get(0);
            sa = "";
        }
        setDocumentoTipoDoc(tipydoc);

        psegtrat = new ThseguimientoDelTratamiento(this);
    }

    public void cargarHCDesdeAgendaDiaria(String tipydoc, String nombres, String apellidos, String PAOP, String pfk_cita) {
        Utilidades.EstablecerIcono(this);
        segtrat = new ArrayList<>();
        remisiones = null;

        this.setLocationRelativeTo(null);
        pfkcita = pfk_cita;
        //band = 0;
        //this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
//        odontograma = new Odontograma();
        datosPaciente = new ArrayList<>();
        tratamientosxpaciente = new ArrayList<>();
        swing = new ArrayList<>();
        cargarRB();
        cargarCBTratamientos(tratamientosxpaciente, 0);
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        cal = Calendar.getInstance();
        vp.bandhc = 1;
        anamVacia = true;
        OtroAnam = "";
        idDatosBasicos = new String[]{};
        imagenes = new Imagenes(this);

        limpiarComponentesHC();
        odontograma.setBounds(0, 0, this.getWidth(), this.getHeight());
        txtAnguloVertical.setText(slAnguloVertical.getValue() + "º");
        txtAnguloGoniano.setText(slAnguloGoniano.getValue() + "º");
        txtLongitudRama.setText(slLongitudRama.getValue() + " mm");
        txtAnguloII.setText(slAnguloII.getValue() + "º");
        modeloInterconsulta = new DefaultListModel();
        modeloHabitos = new DefaultListModel();
        tModelo = new DefaultTableModel();
        tanaModelo = new DefaultTableModel();
        txtInterconsulta.setModel(modeloInterconsulta);
        indices = new int[3];
        nombreColumnas = new String[]{
            "Nro",
            "Fecha",
            "Observaciones del Tratamiento",
            "Abono",
            "Saldo"
        };
        anamcolumnas = new String[]{
            "DATOS",
            "SI",
            "NO",
            "NO SABE"
        };
        int filas = 0;

        cargarTablaSegTrat(null);
        cargarTablaANAMNESIS(null);

        //<editor-fold defaultstate="collapsed" desc="CARGAR INTERUPTORES">
        btnSimetriaSi.setBackground(Color.lightGray);
        btnSimetriaSi.setForeground(new Color(60, 60, 60));
        btnSimetriaSi.setOpaque(true);
        btnSimetriaNo.setBackground(Color.white);
        btnSimetriaNo.setForeground(Color.darkGray);
        btnSimetriaNo.setOpaque(false);
        simetria = "Si";//si = 0; no = 1

        btnPronosticoB.setBackground(Color.lightGray);
        btnPronosticoB.setForeground(new Color(60, 60, 60));
        btnPronosticoB.setOpaque(true);
        btnPronosticoR.setBackground(Color.white);
        btnPronosticoR.setForeground(Color.darkGray);
        btnPronosticoR.setOpaque(false);
        pronostico = "Bueno";//bueno = 0; reservado = 1

        btnLMSDesviacionD.setBackground(Color.lightGray);
        btnLMSDesviacionD.setForeground(new Color(60, 60, 60));
        btnLMSDesviacionD.setOpaque(true);
        btnLMSDesviacionI.setBackground(Color.white);
        btnLMSDesviacionI.setForeground(Color.darkGray);
        btnLMSDesviacionI.setOpaque(false);
        desviacionLMS = "D";//derecha = D; izquierda = I

        btnLMIDesviacionD.setBackground(Color.lightGray);
        btnLMIDesviacionD.setForeground(new Color(60, 60, 60));
        btnLMIDesviacionD.setOpaque(true);
        btnLMIDesviacionI.setBackground(Color.white);
        btnLMIDesviacionI.setForeground(Color.darkGray);
        btnLMIDesviacionI.setOpaque(false);
        desviacionLMI = "D";//derecha = D; izquierda = I
        //</editor-fold>

        verDesviacionLMS(false);
        verDesviacionLMI(false);

//        creaPestanias();
        limpiarRB();
        System.out.println("PAOP--------" + PAOP + "--");
        if (PAOP.equals("1")) {
            tipoPaciente = "p";
            Expresiones.setDocumentoTipoDoc(tipydoc, this);
            String tyd[] = Utilidades.obtenerDocumentoyTipoDoc(tipydoc);
            for (int i = 0; i < cbTipoDocumento.getItemCount(); i++) {
                if (cbTipoDocumento.getItemAt(i).equals(tyd[0])) {
                    cbTipoDocumento.setSelectedIndex(i);
                    break;
                }
            }
            txtDocumento.setText(tyd[1]);
            cargarHistoriaClinica(tipydoc);
            habilitarDatosPaciente();
            setEstadosBotonesDeControl(10);
            habilitarTodosLosDatos(false);
        } else {
            tipoPaciente = "a";
            txtIdPaciente.setText(tipydoc);
            imagenes.reiniciar();
            txtDocumento.setText("");
            cargarDatosPaciente("1");
            cbTipoDocumento.setSelectedIndex(0);
            cbTratamientos.setVisible(false);
//            txtNombreSegtrat.setVisible(false);
            cargarTablaSegTrat(null);
            habilitarTodosLosDatos(true);
            habilitarDatosPaciente();
            cargarTablaANAMNESIS(null);
            cargarIndices();//se busca el ultimo indice de la hc
            setEstadosBotonesDeControl(1);
        }
//        habilitarTodosLosDatos(false);
        txtNombres.setText(nombres);
        txtApellidos.setText(apellidos);

        ArrayList<String> nom = Utilidades.decodificarNombre(nombres);
        ArrayList<String> ape = Utilidades.decodificarNombre(apellidos);
        if (nom.size() >= 2) {
            pn = nom.get(0);
            sn = nom.get(1);
        } else {
            pn = nom.get(0);
            sn = "";
        }

        if (ape.size() >= 2) {
            pa = ape.get(0);
            sa = ape.get(1);
        } else {
            pa = ape.get(0);
            sa = "";
        }
        setDocumentoTipoDoc(tipydoc);

//        psegtrat = new ThseguimientoDelTratamiento(this);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        grupoMolarDerecho = new javax.swing.ButtonGroup();
        grupoPremolarDerecho = new javax.swing.ButtonGroup();
        grupoCaninoDerecho = new javax.swing.ButtonGroup();
        grupoMolarIzquierdo = new javax.swing.ButtonGroup();
        grupoPremolarIzquierdo = new javax.swing.ButtonGroup();
        grupoCaninoIzquierdo = new javax.swing.ButtonGroup();
        txtSApellido = new javax.swing.JLabel();
        txtPApellido = new javax.swing.JLabel();
        txtSNombre = new javax.swing.JLabel();
        txtEstado = new javax.swing.JLabel();
        txtEmailPac = new javax.swing.JLabel();
        txtIdPaciente = new javax.swing.JLabel();
        txtPNombre = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        btnGuardar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnDescartar = new javax.swing.JButton();
        btnConsultar = new javax.swing.JButton();
        chkCerrarHC = new javax.swing.JCheckBox();
        jPanel11 = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        contenedorPaneles = new javax.swing.JTabbedPane();
        pananamnesis = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtMotivoConsulta = new javax.swing.JTextArea();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        tblAnamnesis = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                if(rowIndex == (this.getRowCount()-1) && colIndex == 0)
                return true;
                else
                return false;
            }
        };
        panPadre = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtNombres = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtApellidos = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        cbTipoDocumento = new javax.swing.JComboBox<String>();
        jLabel38 = new javax.swing.JLabel();
        txtDocumento = new javax.swing.JTextField();
        sep6 = new javax.swing.JSeparator();
        sep7 = new javax.swing.JSeparator();
        sep8 = new javax.swing.JSeparator();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        slAnguloVertical = new javax.swing.JSlider();
        slAnguloGoniano = new javax.swing.JSlider();
        jLabel6 = new javax.swing.JLabel();
        txtAnguloVertical = new javax.swing.JLabel();
        txtAnguloGoniano = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtLongitudRama = new javax.swing.JLabel();
        slLongitudRama = new javax.swing.JSlider();
        jPanel5 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        txtAnguloII = new javax.swing.JLabel();
        slAnguloII = new javax.swing.JSlider();
        jPanel1 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        molderclasei = new javax.swing.JRadioButton();
        molderclaseii = new javax.swing.JRadioButton();
        molderclaseiii = new javax.swing.JRadioButton();
        premolderclasei = new javax.swing.JRadioButton();
        premolderclaseii = new javax.swing.JRadioButton();
        premolderclaseiii = new javax.swing.JRadioButton();
        canderclasei = new javax.swing.JRadioButton();
        canderclaseii = new javax.swing.JRadioButton();
        canderclaseiii = new javax.swing.JRadioButton();
        canizqclaseiii = new javax.swing.JRadioButton();
        canizqclaseii = new javax.swing.JRadioButton();
        canizqclasei = new javax.swing.JRadioButton();
        premolizqclaseiii = new javax.swing.JRadioButton();
        premolizqclaseii = new javax.swing.JRadioButton();
        premolizqclasei = new javax.swing.JRadioButton();
        molizqclaseiii = new javax.swing.JRadioButton();
        molizqclaseii = new javax.swing.JRadioButton();
        molizqclasei = new javax.swing.JRadioButton();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        cbSuperior = new javax.swing.JComboBox();
        jLabel11 = new javax.swing.JLabel();
        cbInferior = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        txtSpeed = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtOverBite = new javax.swing.JTextField();
        txtOverJet = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        sep3 = new javax.swing.JSeparator();
        sep4 = new javax.swing.JSeparator();
        sep5 = new javax.swing.JSeparator();
        jPanel7 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        cbLineaMS = new javax.swing.JComboBox();
        panLMS = new javax.swing.JPanel();
        btnLMSDesviacionD = new javax.swing.JLabel();
        btnLMSDesviacionI = new javax.swing.JLabel();
        lblLMSDesviacion = new javax.swing.JLabel();
        lblLMSPor = new javax.swing.JLabel();
        txtLMSMilimetros = new javax.swing.JTextField();
        lblLMSMilimetros = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        cbLineaMI = new javax.swing.JComboBox();
        panLMI = new javax.swing.JPanel();
        btnLMIDesviacionD = new javax.swing.JLabel();
        btnLMIDesviacionI = new javax.swing.JLabel();
        lblLMIDesviacion = new javax.swing.JLabel();
        lblLMIPor = new javax.swing.JLabel();
        txtLMIMilimetros = new javax.swing.JTextField();
        lblLMIMilimetros = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtObservaciones = new javax.swing.JTextArea();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtExamenes = new javax.swing.JTextArea();
        jLabel20 = new javax.swing.JLabel();
        panLMI1 = new javax.swing.JPanel();
        btnSimetriaSi = new javax.swing.JLabel();
        btnSimetriaNo = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        panLMI2 = new javax.swing.JPanel();
        btnPronosticoB = new javax.swing.JLabel();
        btnPronosticoR = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtInterconsulta = new javax.swing.JList<String>();
        jScrollPane8 = new javax.swing.JScrollPane();
        txtHabitos = new javax.swing.JList<String>();
        jScrollPane7 = new javax.swing.JScrollPane();
        listPreHabitos = new javax.swing.JList<String>();
        jScrollPane6 = new javax.swing.JScrollPane();
        listPreInterconsulta = new javax.swing.JList<String>();
        habnext = new javax.swing.JLabel();
        internext = new javax.swing.JLabel();
        sep2 = new javax.swing.JSeparator();
        sep1 = new javax.swing.JSeparator();
        pansegtratamiento = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblSegTrat = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int vColIndex) {
                return false;
            }
        };
        jLabel39 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        cbTratamientos = new javax.swing.JComboBox<String>();
        btnAgregarFila = new javax.swing.JButton();
        panelremform = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        cbformulacionyremisiones = new javax.swing.JComboBox<String>();
        jScrollPane10 = new javax.swing.JScrollPane();
        txtformulacionyremisiones = new javax.swing.JTextArea();
        btnImprimir = new javax.swing.JButton();
        txtMensajeHC = new javax.swing.JLabel();

        txtEstado.setText("jLabel10");

        txtIdPaciente.setText("jLabel10");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 82, 118)));
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/guardar.png"))); // NOI18N
        btnGuardar.setToolTipText("Guardar");
        btnGuardar.setBorderPainted(false);
        btnGuardar.setContentAreaFilled(false);
        btnGuardar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGuardar.setMargin(new java.awt.Insets(2, 10, 2, 8));
        btnGuardar.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/guardar_over.png"))); // NOI18N
        btnGuardar.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/guardar_over.png"))); // NOI18N
        btnGuardar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnGuardarMousePressed(evt);
            }
        });
        jPanel8.add(btnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 64, 64));

        btnModificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/modificar.png"))); // NOI18N
        btnModificar.setToolTipText("Modificar");
        btnModificar.setBorderPainted(false);
        btnModificar.setContentAreaFilled(false);
        btnModificar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnModificar.setMargin(new java.awt.Insets(2, 14, 2, 5));
        btnModificar.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/modificar_over.png"))); // NOI18N
        btnModificar.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/modificar_over.png"))); // NOI18N
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });
        jPanel8.add(btnModificar, new org.netbeans.lib.awtextra.AbsoluteConstraints(64, 0, 64, 64));

        btnDescartar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/descartar.png"))); // NOI18N
        btnDescartar.setToolTipText("Descartar");
        btnDescartar.setBorderPainted(false);
        btnDescartar.setContentAreaFilled(false);
        btnDescartar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDescartar.setMargin(new java.awt.Insets(2, 10, 2, 8));
        btnDescartar.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/descartar_over.png"))); // NOI18N
        btnDescartar.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/descartar_over.png"))); // NOI18N
        btnDescartar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDescartarActionPerformed(evt);
            }
        });
        jPanel8.add(btnDescartar, new org.netbeans.lib.awtextra.AbsoluteConstraints(128, 0, 64, 64));

        btnConsultar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/consultar.png"))); // NOI18N
        btnConsultar.setToolTipText("Consutar");
        btnConsultar.setBorderPainted(false);
        btnConsultar.setContentAreaFilled(false);
        btnConsultar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnConsultar.setMargin(new java.awt.Insets(2, 10, 2, 10));
        btnConsultar.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/consultar_over.png"))); // NOI18N
        btnConsultar.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/consultar_over.png"))); // NOI18N
        btnConsultar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnConsultarMousePressed(evt);
            }
        });
        jPanel8.add(btnConsultar, new org.netbeans.lib.awtextra.AbsoluteConstraints(192, 0, 64, 64));

        jPanel10.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 740, 256, -1));

        chkCerrarHC.setBackground(new java.awt.Color(255, 255, 255));
        chkCerrarHC.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        chkCerrarHC.setForeground(new java.awt.Color(26, 82, 118));
        chkCerrarHC.setText("Terminar Historia Clinica");
        chkCerrarHC.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                chkCerrarHCMousePressed(evt);
            }
        });
        jPanel10.add(chkCerrarHC, new org.netbeans.lib.awtextra.AbsoluteConstraints(1180, 40, -1, -1));

        jPanel11.setBackground(new java.awt.Color(26, 82, 118));
        jPanel11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel11MousePressed(evt);
            }
        });
        jPanel11.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jPanel11MouseDragged(evt);
            }
        });
        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/minimizar.png"))); // NOI18N
        jLabel30.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel30.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel30MouseClicked(evt);
            }
        });
        jPanel11.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(1310, 0, 30, 30));

        jLabel31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/cerrar_1.png"))); // NOI18N
        jLabel31.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel31.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel31MouseClicked(evt);
            }
        });
        jPanel11.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(1340, 0, 30, 30));

        jLabel32.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(255, 255, 255));
        jLabel32.setText("Historia Clinica");
        jPanel11.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 8, 152, -1));

        jPanel10.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1380, 30));

        contenedorPaneles.setForeground(new java.awt.Color(21, 67, 96));
        contenedorPaneles.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        contenedorPaneles.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                contenedorPanelesStateChanged(evt);
            }
        });

        pananamnesis.setBackground(new java.awt.Color(255, 255, 255));

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(26, 82, 118));
        jLabel10.setText("Motivo de la consulta");

        txtMotivoConsulta.setColumns(20);
        txtMotivoConsulta.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtMotivoConsulta.setForeground(new java.awt.Color(40, 116, 166));
        txtMotivoConsulta.setRows(5);
        txtMotivoConsulta.setCaretColor(new java.awt.Color(26, 82, 118));
        txtMotivoConsulta.setSelectionColor(new java.awt.Color(26, 82, 118));
        jScrollPane1.setViewportView(txtMotivoConsulta);

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(null, " Datos Basicos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12), new java.awt.Color(26, 82, 118))); // NOI18N

        tblAnamnesis.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tblAnamnesis.setForeground(new java.awt.Color(40, 116, 166));
        tblAnamnesis.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblAnamnesis.setGridColor(new java.awt.Color(26, 82, 118));
        tblAnamnesis.setSelectionBackground(new java.awt.Color(26, 82, 118));
        tblAnamnesis.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblAnamnesisMousePressed(evt);
            }
        });
        jScrollPane9.setViewportView(tblAnamnesis);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 512, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout pananamnesisLayout = new javax.swing.GroupLayout(pananamnesis);
        pananamnesis.setLayout(pananamnesisLayout);
        pananamnesisLayout.setHorizontalGroup(
            pananamnesisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pananamnesisLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pananamnesisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 527, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(761, Short.MAX_VALUE))
        );
        pananamnesisLayout.setVerticalGroup(
            pananamnesisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pananamnesisLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        contenedorPaneles.addTab("Anamnesis", pananamnesis);

        panPadre.setBackground(new java.awt.Color(255, 255, 255));
        panPadre.setAutoscrolls(true);
        panPadre.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos Básicos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12), new java.awt.Color(21, 67, 96))); // NOI18N
        jPanel2.setForeground(new java.awt.Color(21, 67, 96));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(21, 67, 96));
        jLabel1.setText("Nombres");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 30, -1, -1));

        txtNombres.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtNombres.setForeground(new java.awt.Color(31, 97, 141));
        txtNombres.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        txtNombres.setSelectionColor(new java.awt.Color(21, 67, 96));
        jPanel2.add(txtNombres, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 50, 260, 30));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(21, 67, 96));
        jLabel2.setText("Apellidos");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 90, -1, -1));

        txtApellidos.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtApellidos.setForeground(new java.awt.Color(31, 97, 141));
        txtApellidos.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        txtApellidos.setSelectionColor(new java.awt.Color(21, 67, 96));
        jPanel2.add(txtApellidos, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 110, 260, 30));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(21, 67, 96));
        jLabel3.setText("Tipo Documento");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, -1, -1));

        cbTipoDocumento.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cbTipoDocumento.setForeground(new java.awt.Color(31, 97, 141));
        cbTipoDocumento.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "seleccionar", "CC", "TI", "NIT", "CE", "PAS", "NUI", "RC" }));
        cbTipoDocumento.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbTipoDocumentoItemStateChanged(evt);
            }
        });
        jPanel2.add(cbTipoDocumento, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, -1, 30));

        jLabel38.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(21, 67, 96));
        jLabel38.setText("Documento");
        jPanel2.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 90, -1, -1));

        txtDocumento.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtDocumento.setForeground(new java.awt.Color(31, 97, 141));
        txtDocumento.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        txtDocumento.setSelectionColor(new java.awt.Color(21, 67, 96));
        txtDocumento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDocumentoKeyReleased(evt);
            }
        });
        jPanel2.add(txtDocumento, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, 124, 30));

        sep6.setBackground(new java.awt.Color(21, 67, 96));
        jPanel2.add(sep6, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 80, 260, 10));

        sep7.setBackground(new java.awt.Color(21, 67, 96));
        jPanel2.add(sep7, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, 124, 10));

        sep8.setBackground(new java.awt.Color(21, 67, 96));
        jPanel2.add(sep8, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 140, 260, 10));

        panPadre.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(705, 11, 530, 157));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, " Analisis Vertical ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12), new java.awt.Color(21, 67, 96))); // NOI18N
        jPanel3.setForeground(new java.awt.Color(21, 67, 96));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(21, 67, 96));
        jLabel5.setText("Angulo Articular");
        jPanel3.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(27, 25, -1, -1));

        slAnguloVertical.setBackground(new java.awt.Color(255, 255, 255));
        slAnguloVertical.setForeground(new java.awt.Color(31, 97, 141));
        slAnguloVertical.setMajorTickSpacing(90);
        slAnguloVertical.setMaximum(360);
        slAnguloVertical.setPaintLabels(true);
        slAnguloVertical.setPaintTicks(true);
        slAnguloVertical.setValue(0);
        slAnguloVertical.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                slAnguloVerticalStateChanged(evt);
            }
        });
        jPanel3.add(slAnguloVertical, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 53, -1, -1));

        slAnguloGoniano.setBackground(new java.awt.Color(255, 255, 255));
        slAnguloGoniano.setForeground(new java.awt.Color(31, 97, 141));
        slAnguloGoniano.setMajorTickSpacing(90);
        slAnguloGoniano.setMaximum(360);
        slAnguloGoniano.setPaintLabels(true);
        slAnguloGoniano.setPaintTicks(true);
        slAnguloGoniano.setValue(0);
        slAnguloGoniano.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                slAnguloGonianoStateChanged(evt);
            }
        });
        jPanel3.add(slAnguloGoniano, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 50, -1, -1));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(21, 67, 96));
        jLabel6.setText("Angulo Goniano");
        jPanel3.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(244, 25, -1, -1));

        txtAnguloVertical.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtAnguloVertical.setForeground(new java.awt.Color(26, 82, 118));
        txtAnguloVertical.setText(" ");
        jPanel3.add(txtAnguloVertical, new org.netbeans.lib.awtextra.AbsoluteConstraints(138, 25, 34, -1));

        txtAnguloGoniano.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtAnguloGoniano.setForeground(new java.awt.Color(26, 82, 118));
        txtAnguloGoniano.setText(" ");
        jPanel3.add(txtAnguloGoniano, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 25, 34, -1));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(21, 67, 96));
        jLabel7.setText("Longitud de Rama");
        jPanel3.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 25, -1, -1));

        txtLongitudRama.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtLongitudRama.setForeground(new java.awt.Color(26, 82, 118));
        txtLongitudRama.setText(" ");
        jPanel3.add(txtLongitudRama, new org.netbeans.lib.awtextra.AbsoluteConstraints(607, 25, 61, -1));

        slLongitudRama.setBackground(new java.awt.Color(255, 255, 255));
        slLongitudRama.setForeground(new java.awt.Color(31, 97, 141));
        slLongitudRama.setMajorTickSpacing(20);
        slLongitudRama.setPaintLabels(true);
        slLongitudRama.setPaintTicks(true);
        slLongitudRama.setValue(0);
        slLongitudRama.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                slLongitudRamaStateChanged(evt);
            }
        });
        jPanel3.add(slLongitudRama, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 50, -1, -1));

        panPadre.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, 689, 123));

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, " Analisis Sagital ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12), new java.awt.Color(21, 67, 96))); // NOI18N
        jPanel5.setForeground(new java.awt.Color(21, 67, 96));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(21, 67, 96));
        jLabel8.setText("Angulo Incisivo Inferior");
        jPanel5.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 27, -1, -1));

        txtAnguloII.setForeground(new java.awt.Color(153, 153, 153));
        txtAnguloII.setText(" ");
        jPanel5.add(txtAnguloII, new org.netbeans.lib.awtextra.AbsoluteConstraints(178, 28, 34, -1));

        slAnguloII.setBackground(new java.awt.Color(255, 255, 255));
        slAnguloII.setForeground(new java.awt.Color(31, 97, 141));
        slAnguloII.setMajorTickSpacing(20);
        slAnguloII.setMaximum(130);
        slAnguloII.setMinimum(50);
        slAnguloII.setPaintLabels(true);
        slAnguloII.setPaintTicks(true);
        slAnguloII.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                slAnguloIIStateChanged(evt);
            }
        });
        jPanel5.add(slAnguloII, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 53, 226, -1));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel26.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(21, 67, 96));
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel26.setText("DERECHO");
        jLabel26.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel26.setName(""); // NOI18N
        jLabel26.setPreferredSize(new java.awt.Dimension(200, 16));
        jPanel1.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 0, 198, 20));

        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(21, 67, 96));
        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel27.setText("MOLAR");
        jLabel27.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel27.setPreferredSize(new java.awt.Dimension(59, 16));
        jPanel1.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 19, 60, 20));

        jLabel28.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(21, 67, 96));
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel28.setText("PREMOLAR");
        jLabel28.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(129, 19, 80, 20));

        jLabel29.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(21, 67, 96));
        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel29.setText("CANINO");
        jLabel29.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(208, 19, 60, 20));

        molderclasei.setBackground(new java.awt.Color(255, 255, 255));
        grupoMolarDerecho.add(molderclasei);
        molderclasei.setForeground(java.awt.Color.lightGray);
        molderclasei.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        molderclasei.setBorderPainted(true);
        molderclasei.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        molderclasei.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        molderclasei.setMargin(new java.awt.Insets(10, 10, 10, 10));
        jPanel1.add(molderclasei, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 38, 60, 23));

        molderclaseii.setBackground(new java.awt.Color(255, 255, 255));
        grupoMolarDerecho.add(molderclaseii);
        molderclaseii.setForeground(java.awt.Color.lightGray);
        molderclaseii.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        molderclaseii.setBorderPainted(true);
        molderclaseii.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        molderclaseii.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        molderclaseii.setMargin(new java.awt.Insets(10, 10, 10, 10));
        jPanel1.add(molderclaseii, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 59, 60, 23));

        molderclaseiii.setBackground(new java.awt.Color(255, 255, 255));
        grupoMolarDerecho.add(molderclaseiii);
        molderclaseiii.setForeground(java.awt.Color.lightGray);
        molderclaseiii.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        molderclaseiii.setBorderPainted(true);
        molderclaseiii.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        molderclaseiii.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        molderclaseiii.setMargin(new java.awt.Insets(10, 10, 10, 10));
        jPanel1.add(molderclaseiii, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 80, 60, 23));

        premolderclasei.setBackground(new java.awt.Color(255, 255, 255));
        grupoPremolarDerecho.add(premolderclasei);
        premolderclasei.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        premolderclasei.setBorderPainted(true);
        premolderclasei.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        premolderclasei.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        premolderclasei.setMargin(new java.awt.Insets(10, 10, 10, 10));
        jPanel1.add(premolderclasei, new org.netbeans.lib.awtextra.AbsoluteConstraints(129, 38, 80, 23));

        premolderclaseii.setBackground(new java.awt.Color(255, 255, 255));
        grupoPremolarDerecho.add(premolderclaseii);
        premolderclaseii.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        premolderclaseii.setBorderPainted(true);
        premolderclaseii.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        premolderclaseii.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        premolderclaseii.setMargin(new java.awt.Insets(10, 10, 10, 10));
        jPanel1.add(premolderclaseii, new org.netbeans.lib.awtextra.AbsoluteConstraints(129, 60, 80, 22));

        premolderclaseiii.setBackground(new java.awt.Color(255, 255, 255));
        grupoPremolarDerecho.add(premolderclaseiii);
        premolderclaseiii.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        premolderclaseiii.setBorderPainted(true);
        premolderclaseiii.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        premolderclaseiii.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        premolderclaseiii.setMargin(new java.awt.Insets(10, 10, 10, 10));
        jPanel1.add(premolderclaseiii, new org.netbeans.lib.awtextra.AbsoluteConstraints(129, 80, 80, 23));

        canderclasei.setBackground(new java.awt.Color(255, 255, 255));
        grupoCaninoDerecho.add(canderclasei);
        canderclasei.setToolTipText("");
        canderclasei.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        canderclasei.setBorderPainted(true);
        canderclasei.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        canderclasei.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        canderclasei.setMargin(new java.awt.Insets(10, 10, 10, 10));
        jPanel1.add(canderclasei, new org.netbeans.lib.awtextra.AbsoluteConstraints(208, 38, 60, 23));

        canderclaseii.setBackground(new java.awt.Color(255, 255, 255));
        grupoCaninoDerecho.add(canderclaseii);
        canderclaseii.setToolTipText("");
        canderclaseii.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        canderclaseii.setBorderPainted(true);
        canderclaseii.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        canderclaseii.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        canderclaseii.setMargin(new java.awt.Insets(10, 10, 10, 10));
        jPanel1.add(canderclaseii, new org.netbeans.lib.awtextra.AbsoluteConstraints(208, 60, 60, 22));

        canderclaseiii.setBackground(new java.awt.Color(255, 255, 255));
        grupoCaninoDerecho.add(canderclaseiii);
        canderclaseiii.setToolTipText("");
        canderclaseiii.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        canderclaseiii.setBorderPainted(true);
        canderclaseiii.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        canderclaseiii.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        canderclaseiii.setMargin(new java.awt.Insets(10, 10, 10, 10));
        jPanel1.add(canderclaseiii, new org.netbeans.lib.awtextra.AbsoluteConstraints(208, 80, 60, 23));

        canizqclaseiii.setBackground(new java.awt.Color(255, 255, 255));
        grupoCaninoIzquierdo.add(canizqclaseiii);
        canizqclaseiii.setToolTipText("");
        canizqclaseiii.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        canizqclaseiii.setBorderPainted(true);
        canizqclaseiii.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        canizqclaseiii.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        canizqclaseiii.setMargin(new java.awt.Insets(10, 10, 10, 10));
        jPanel1.add(canizqclaseiii, new org.netbeans.lib.awtextra.AbsoluteConstraints(405, 81, 60, 22));

        canizqclaseii.setBackground(new java.awt.Color(255, 255, 255));
        grupoCaninoIzquierdo.add(canizqclaseii);
        canizqclaseii.setToolTipText("");
        canizqclaseii.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        canizqclaseii.setBorderPainted(true);
        canizqclaseii.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        canizqclaseii.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        canizqclaseii.setMargin(new java.awt.Insets(10, 10, 10, 10));
        jPanel1.add(canizqclaseii, new org.netbeans.lib.awtextra.AbsoluteConstraints(405, 60, 60, 22));

        canizqclasei.setBackground(new java.awt.Color(255, 255, 255));
        grupoCaninoIzquierdo.add(canizqclasei);
        canizqclasei.setToolTipText("");
        canizqclasei.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        canizqclasei.setBorderPainted(true);
        canizqclasei.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        canizqclasei.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        canizqclasei.setMargin(new java.awt.Insets(10, 10, 10, 10));
        jPanel1.add(canizqclasei, new org.netbeans.lib.awtextra.AbsoluteConstraints(405, 38, 60, 23));

        premolizqclaseiii.setBackground(new java.awt.Color(255, 255, 255));
        grupoPremolarIzquierdo.add(premolizqclaseiii);
        premolizqclaseiii.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        premolizqclaseiii.setBorderPainted(true);
        premolizqclaseiii.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        premolizqclaseiii.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        premolizqclaseiii.setMargin(new java.awt.Insets(10, 10, 10, 10));
        jPanel1.add(premolizqclaseiii, new org.netbeans.lib.awtextra.AbsoluteConstraints(326, 81, 80, 22));

        premolizqclaseii.setBackground(new java.awt.Color(255, 255, 255));
        grupoPremolarIzquierdo.add(premolizqclaseii);
        premolizqclaseii.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        premolizqclaseii.setBorderPainted(true);
        premolizqclaseii.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        premolizqclaseii.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        premolizqclaseii.setMargin(new java.awt.Insets(10, 10, 10, 10));
        jPanel1.add(premolizqclaseii, new org.netbeans.lib.awtextra.AbsoluteConstraints(326, 60, 80, 22));

        premolizqclasei.setBackground(new java.awt.Color(255, 255, 255));
        grupoPremolarIzquierdo.add(premolizqclasei);
        premolizqclasei.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        premolizqclasei.setBorderPainted(true);
        premolizqclasei.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        premolizqclasei.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        premolizqclasei.setMargin(new java.awt.Insets(10, 10, 10, 10));
        jPanel1.add(premolizqclasei, new org.netbeans.lib.awtextra.AbsoluteConstraints(326, 38, 80, 23));

        molizqclaseiii.setBackground(new java.awt.Color(255, 255, 255));
        grupoMolarIzquierdo.add(molizqclaseiii);
        molizqclaseiii.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        molizqclaseiii.setBorderPainted(true);
        molizqclaseiii.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        molizqclaseiii.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        molizqclaseiii.setMargin(new java.awt.Insets(10, 10, 10, 10));
        molizqclaseiii.setName(""); // NOI18N
        jPanel1.add(molizqclaseiii, new org.netbeans.lib.awtextra.AbsoluteConstraints(267, 81, 60, 22));

        molizqclaseii.setBackground(new java.awt.Color(255, 255, 255));
        grupoMolarIzquierdo.add(molizqclaseii);
        molizqclaseii.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        molizqclaseii.setBorderPainted(true);
        molizqclaseii.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        molizqclaseii.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        molizqclaseii.setMargin(new java.awt.Insets(10, 10, 10, 10));
        jPanel1.add(molizqclaseii, new org.netbeans.lib.awtextra.AbsoluteConstraints(267, 60, 60, 22));

        molizqclasei.setBackground(new java.awt.Color(255, 255, 255));
        grupoMolarIzquierdo.add(molizqclasei);
        molizqclasei.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        molizqclasei.setBorderPainted(true);
        molizqclasei.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        molizqclasei.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        molizqclasei.setMargin(new java.awt.Insets(10, 10, 10, 10));
        jPanel1.add(molizqclasei, new org.netbeans.lib.awtextra.AbsoluteConstraints(267, 38, 60, 23));

        jLabel33.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(21, 67, 96));
        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel33.setText("CLASE I  ");
        jLabel33.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 39, 70, 22));

        jLabel34.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(21, 67, 96));
        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel34.setText("CLASE II  ");
        jLabel34.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 60, 70, 22));

        jLabel35.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(21, 67, 96));
        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel35.setText("CLASE III  ");
        jLabel35.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 81, 70, 22));

        jLabel40.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(21, 67, 96));
        jLabel40.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel40.setText("IZQUIERDO");
        jLabel40.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel40.setName(""); // NOI18N
        jLabel40.setPreferredSize(new java.awt.Dimension(200, 16));
        jPanel1.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(267, 0, 198, 20));

        jLabel41.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(21, 67, 96));
        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel41.setText("MOLAR");
        jLabel41.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel41.setPreferredSize(new java.awt.Dimension(59, 16));
        jPanel1.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(267, 19, 60, 20));

        jLabel42.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(21, 67, 96));
        jLabel42.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel42.setText("PREMOLAR");
        jLabel42.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(326, 19, 80, 20));

        jLabel43.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(21, 67, 96));
        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel43.setText("CANINO");
        jLabel43.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(405, 19, 60, 20));

        jPanel5.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 140, 470, 110));

        jLabel36.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(21, 67, 96));
        jLabel36.setText("Solo si hay paralelismo Maxilar clasificación de Angle");
        jPanel5.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 114, -1, -1));

        panPadre.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(705, 337, 530, 260));

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, " Forma de Arco ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12), new java.awt.Color(21, 67, 96))); // NOI18N
        jPanel6.setForeground(new java.awt.Color(21, 67, 96));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(21, 67, 96));
        jLabel9.setText("Superior");
        jPanel6.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 29, -1, -1));

        cbSuperior.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cbSuperior.setForeground(new java.awt.Color(31, 97, 141));
        cbSuperior.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Seleccionar", "Ovoide", "Triangular", "Cuadrada" }));
        jPanel6.add(cbSuperior, new org.netbeans.lib.awtextra.AbsoluteConstraints(95, 27, -1, 30));

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(21, 67, 96));
        jLabel11.setText("Inferior");
        jPanel6.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(206, 29, -1, -1));

        cbInferior.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cbInferior.setForeground(new java.awt.Color(31, 97, 141));
        cbInferior.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Seleccionar", "Ovoide", "Triangular", "Cuadrada" }));
        jPanel6.add(cbInferior, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 27, -1, 30));

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(21, 67, 96));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("Curva de Speed");
        jPanel6.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, -1, -1));

        txtSpeed.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtSpeed.setForeground(new java.awt.Color(31, 97, 141));
        txtSpeed.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        txtSpeed.setSelectionColor(new java.awt.Color(21, 67, 96));
        txtSpeed.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSpeedKeyReleased(evt);
            }
        });
        jPanel6.add(txtSpeed, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, 51, 30));

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(21, 67, 96));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel13.setText("Overbite");
        jPanel6.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 80, 60, -1));

        txtOverBite.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtOverBite.setForeground(new java.awt.Color(31, 97, 141));
        txtOverBite.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        txtOverBite.setSelectionColor(new java.awt.Color(21, 67, 96));
        txtOverBite.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtOverBiteKeyReleased(evt);
            }
        });
        jPanel6.add(txtOverBite, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 100, 51, 30));

        txtOverJet.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtOverJet.setForeground(new java.awt.Color(31, 97, 141));
        txtOverJet.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        txtOverJet.setSelectionColor(new java.awt.Color(21, 67, 96));
        txtOverJet.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtOverJetKeyReleased(evt);
            }
        });
        jPanel6.add(txtOverJet, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 100, 51, 30));

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(21, 67, 96));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel14.setText("Overjet");
        jPanel6.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 80, 50, -1));

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(21, 67, 96));
        jLabel23.setText("mm");
        jPanel6.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 100, -1, 30));

        jLabel24.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(21, 67, 96));
        jLabel24.setText("%");
        jPanel6.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 100, -1, 30));

        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(21, 67, 96));
        jLabel25.setText("mm");
        jPanel6.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 100, -1, 30));

        sep3.setBackground(new java.awt.Color(21, 67, 96));
        jPanel6.add(sep3, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 130, 51, 10));

        sep4.setBackground(new java.awt.Color(21, 67, 96));
        jPanel6.add(sep4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 130, 51, 10));

        sep5.setBackground(new java.awt.Color(21, 67, 96));
        jPanel6.add(sep5, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 130, 51, 10));

        panPadre.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(705, 174, 530, 152));

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(null, " Analisis Transversal ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12), new java.awt.Color(21, 67, 96))); // NOI18N
        jPanel7.setForeground(new java.awt.Color(21, 67, 96));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(21, 67, 96));
        jLabel15.setText("Linea Media Superior");
        jPanel7.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, -1, 30));

        cbLineaMS.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cbLineaMS.setForeground(new java.awt.Color(31, 97, 141));
        cbLineaMS.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Seleccionar", "Coincidente", "Desviada" }));
        cbLineaMS.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbLineaMSItemStateChanged(evt);
            }
        });
        jPanel7.add(cbLineaMS, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 30, -1, 30));

        panLMS.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        panLMS.setAlignmentX(0.0F);
        panLMS.setAlignmentY(0.0F);
        panLMS.setFocusable(false);
        panLMS.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnLMSDesviacionD.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnLMSDesviacionD.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnLMSDesviacionD.setText("Derecha");
        btnLMSDesviacionD.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLMSDesviacionD.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnLMSDesviacionDMousePressed(evt);
            }
        });
        panLMS.add(btnLMSDesviacionD, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 70, 30));

        btnLMSDesviacionI.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnLMSDesviacionI.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnLMSDesviacionI.setText("Izquierda");
        btnLMSDesviacionI.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLMSDesviacionI.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnLMSDesviacionIMousePressed(evt);
            }
        });
        panLMS.add(btnLMSDesviacionI, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 0, 70, 30));

        jPanel7.add(panLMS, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 30, 140, 30));

        lblLMSDesviacion.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblLMSDesviacion.setForeground(new java.awt.Color(21, 67, 96));
        lblLMSDesviacion.setText("Hacia la ");
        jPanel7.add(lblLMSDesviacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 30, -1, 30));

        lblLMSPor.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblLMSPor.setForeground(new java.awt.Color(21, 67, 96));
        lblLMSPor.setText("x");
        jPanel7.add(lblLMSPor, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 30, -1, 30));

        txtLMSMilimetros.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtLMSMilimetros.setForeground(new java.awt.Color(31, 97, 141));
        txtLMSMilimetros.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        txtLMSMilimetros.setSelectionColor(new java.awt.Color(21, 67, 96));
        txtLMSMilimetros.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtLMSMilimetrosKeyReleased(evt);
            }
        });
        jPanel7.add(txtLMSMilimetros, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 30, 90, 30));

        lblLMSMilimetros.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblLMSMilimetros.setForeground(new java.awt.Color(21, 67, 96));
        lblLMSMilimetros.setText("mm");
        jPanel7.add(lblLMSMilimetros, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 30, -1, 30));

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(21, 67, 96));
        jLabel16.setText("Linea Media Inferior");
        jPanel7.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, -1, 30));

        cbLineaMI.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cbLineaMI.setForeground(new java.awt.Color(31, 97, 141));
        cbLineaMI.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Seleccionar", "Coincidente", "Desviada" }));
        cbLineaMI.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbLineaMIItemStateChanged(evt);
            }
        });
        jPanel7.add(cbLineaMI, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 80, -1, 30));

        panLMI.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        panLMI.setAlignmentX(0.0F);
        panLMI.setAlignmentY(0.0F);
        panLMI.setFocusable(false);
        panLMI.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnLMIDesviacionD.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnLMIDesviacionD.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnLMIDesviacionD.setText("Derecha");
        btnLMIDesviacionD.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLMIDesviacionD.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnLMIDesviacionDMousePressed(evt);
            }
        });
        panLMI.add(btnLMIDesviacionD, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 70, 30));

        btnLMIDesviacionI.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnLMIDesviacionI.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnLMIDesviacionI.setText("Izquierda");
        btnLMIDesviacionI.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLMIDesviacionI.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnLMIDesviacionIMousePressed(evt);
            }
        });
        panLMI.add(btnLMIDesviacionI, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 0, 70, 30));

        jPanel7.add(panLMI, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 80, 140, 30));

        lblLMIDesviacion.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblLMIDesviacion.setForeground(new java.awt.Color(21, 67, 96));
        lblLMIDesviacion.setText("Hacia la ");
        jPanel7.add(lblLMIDesviacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 80, -1, 30));

        lblLMIPor.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblLMIPor.setForeground(new java.awt.Color(21, 67, 96));
        lblLMIPor.setText("x");
        jPanel7.add(lblLMIPor, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 80, -1, 30));

        txtLMIMilimetros.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtLMIMilimetros.setForeground(new java.awt.Color(31, 97, 141));
        txtLMIMilimetros.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        txtLMIMilimetros.setSelectionColor(new java.awt.Color(21, 67, 96));
        txtLMIMilimetros.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtLMIMilimetrosKeyReleased(evt);
            }
        });
        jPanel7.add(txtLMIMilimetros, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 80, 90, 30));

        lblLMIMilimetros.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblLMIMilimetros.setForeground(new java.awt.Color(21, 67, 96));
        lblLMIMilimetros.setText("mm");
        jPanel7.add(lblLMIMilimetros, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 80, -1, 30));

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(21, 67, 96));
        jLabel17.setText("Habitos");
        jPanel7.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, -1, -1));

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(21, 67, 96));
        jLabel18.setText("Interconsulta");
        jPanel7.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 270, -1, -1));

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(21, 67, 96));
        jLabel19.setText("Observaciones");
        jPanel7.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 130, -1, -1));

        txtObservaciones.setColumns(20);
        txtObservaciones.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtObservaciones.setForeground(new java.awt.Color(31, 97, 141));
        txtObservaciones.setRows(2);
        txtObservaciones.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        jScrollPane3.setViewportView(txtObservaciones);

        jPanel7.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 150, 179, 110));

        txtExamenes.setColumns(20);
        txtExamenes.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtExamenes.setForeground(new java.awt.Color(31, 97, 141));
        txtExamenes.setRows(2);
        txtExamenes.setTabSize(5);
        jScrollPane4.setViewportView(txtExamenes);

        jPanel7.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 290, 190, 110));

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(21, 67, 96));
        jLabel20.setText("Examenes Complementarios");
        jPanel7.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 270, -1, -1));

        panLMI1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        panLMI1.setAlignmentX(0.0F);
        panLMI1.setAlignmentY(0.0F);
        panLMI1.setFocusable(false);
        panLMI1.setLayout(null);

        btnSimetriaSi.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnSimetriaSi.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnSimetriaSi.setText("SI");
        btnSimetriaSi.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSimetriaSi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnSimetriaSiMousePressed(evt);
            }
        });
        panLMI1.add(btnSimetriaSi);
        btnSimetriaSi.setBounds(0, 0, 35, 30);

        btnSimetriaNo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnSimetriaNo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnSimetriaNo.setText("NO");
        btnSimetriaNo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSimetriaNo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnSimetriaNoMousePressed(evt);
            }
        });
        panLMI1.add(btnSimetriaNo);
        btnSimetriaNo.setBounds(35, 0, 35, 30);

        jPanel7.add(panLMI1, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 170, 70, 30));

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(21, 67, 96));
        jLabel21.setText("Simetria");
        jPanel7.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 150, -1, -1));

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(21, 67, 96));
        jLabel22.setText("Pronostico");
        jPanel7.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 220, -1, -1));

        panLMI2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        panLMI2.setAlignmentX(0.0F);
        panLMI2.setAlignmentY(0.0F);
        panLMI2.setFocusable(false);
        panLMI2.setLayout(null);

        btnPronosticoB.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnPronosticoB.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnPronosticoB.setText("Bueno");
        btnPronosticoB.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPronosticoB.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnPronosticoBMousePressed(evt);
            }
        });
        panLMI2.add(btnPronosticoB);
        btnPronosticoB.setBounds(0, 0, 70, 30);

        btnPronosticoR.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnPronosticoR.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnPronosticoR.setText("Reservado");
        btnPronosticoR.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPronosticoR.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnPronosticoRMousePressed(evt);
            }
        });
        panLMI2.add(btnPronosticoR);
        btnPronosticoR.setBounds(70, 0, 70, 30);

        jPanel7.add(panLMI2, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 240, 140, 30));

        txtInterconsulta.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtInterconsulta.setForeground(new java.awt.Color(31, 97, 141));
        txtInterconsulta.setSelectionBackground(new java.awt.Color(26, 82, 118));
        txtInterconsulta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtInterconsultaMousePressed(evt);
            }
        });
        jScrollPane5.setViewportView(txtInterconsulta);

        jPanel7.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 290, 120, 130));

        txtHabitos.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtHabitos.setForeground(new java.awt.Color(31, 97, 141));
        txtHabitos.setSelectionBackground(new java.awt.Color(26, 82, 118));
        txtHabitos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtHabitosMousePressed(evt);
            }
        });
        jScrollPane8.setViewportView(txtHabitos);

        jPanel7.add(jScrollPane8, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 150, 123, 110));

        jScrollPane7.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        listPreHabitos.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        listPreHabitos.setForeground(new java.awt.Color(31, 97, 141));
        listPreHabitos.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Deglucion Atipica", "Deglucion Atipica Anterior", "Empuje Lingual", "Succion Digital", "Omnicofagia", "Respirado Oral" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        listPreHabitos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        listPreHabitos.setSelectionBackground(new java.awt.Color(26, 82, 118));
        listPreHabitos.setValueIsAdjusting(true);
        listPreHabitos.setVisibleRowCount(5);
        listPreHabitos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                listPreHabitosKeyReleased(evt);
            }
        });
        jScrollPane7.setViewportView(listPreHabitos);

        jPanel7.add(jScrollPane7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, 150, 110));

        jScrollPane6.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        listPreInterconsulta.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        listPreInterconsulta.setForeground(new java.awt.Color(31, 97, 141));
        listPreInterconsulta.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Periodoncista", "Endodoncista", "Cirujano Maxilofacial", "Odontopediatra", "Estomatologo", "Odontologo General", "Rehabilitador Oral" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        listPreInterconsulta.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        listPreInterconsulta.setMaximumSize(new java.awt.Dimension(130, 94));
        listPreInterconsulta.setMinimumSize(new java.awt.Dimension(130, 94));
        listPreInterconsulta.setPreferredSize(new java.awt.Dimension(130, 94));
        listPreInterconsulta.setSelectionBackground(new java.awt.Color(26, 82, 118));
        listPreInterconsulta.setVerifyInputWhenFocusTarget(false);
        listPreInterconsulta.setVisibleRowCount(5);
        listPreInterconsulta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                listPreInterconsultaKeyReleased(evt);
            }
        });
        jScrollPane6.setViewportView(listPreInterconsulta);

        jPanel7.add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, 154, 130));

        habnext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/der_1.png"))); // NOI18N
        habnext.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                habnextMousePressed(evt);
            }
        });
        jPanel7.add(habnext, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 190, 30, 30));

        internext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/der_1.png"))); // NOI18N
        internext.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                internextMousePressed(evt);
            }
        });
        jPanel7.add(internext, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 350, 30, 30));

        sep2.setBackground(new java.awt.Color(21, 67, 96));
        jPanel7.add(sep2, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 110, 90, 10));

        sep1.setBackground(new java.awt.Color(21, 67, 96));
        jPanel7.add(sep1, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 60, 90, 10));

        panPadre.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 152, 689, 430));

        contenedorPaneles.addTab("Historia Clinica", panPadre);

        pansegtratamiento.setBackground(new java.awt.Color(255, 255, 255));
        pansegtratamiento.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblSegTrat.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tblSegTrat.setForeground(new java.awt.Color(26, 82, 118));
        tblSegTrat.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblSegTrat.setGridColor(new java.awt.Color(26, 82, 118));
        tblSegTrat.setSelectionBackground(new java.awt.Color(26, 82, 118));
        tblSegTrat.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblSegTrat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblSegTratMousePressed(evt);
            }
        });
        jScrollPane2.setViewportView(tblSegTrat);

        pansegtratamiento.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, 680, 410));

        jLabel39.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(26, 82, 118));
        jLabel39.setText("EVOLUCIÓN DEL TRATAMIENTO");
        pansegtratamiento.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, 452, -1));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(26, 82, 118));
        jLabel4.setText("Seleccione el tratamiento");
        pansegtratamiento.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, -1, -1));

        cbTratamientos.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cbTratamientos.setForeground(new java.awt.Color(26, 82, 118));
        cbTratamientos.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbTratamientosItemStateChanged(evt);
            }
        });
        pansegtratamiento.add(cbTratamientos, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, 314, 30));

        btnAgregarFila.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/add.png"))); // NOI18N
        btnAgregarFila.setContentAreaFilled(false);
        btnAgregarFila.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAgregarFila.setDefaultCapable(false);
        btnAgregarFila.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarFilaActionPerformed(evt);
            }
        });
        pansegtratamiento.add(btnAgregarFila, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 70, 30, 30));

        contenedorPaneles.addTab("Seguimiento del Tratamiento", pansegtratamiento);

        panelremform.setBackground(new java.awt.Color(255, 255, 255));
        panelremform.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel37.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(26, 82, 118));
        jLabel37.setText("Seleccione una opción");
        panelremform.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(23, 38, -1, -1));

        cbformulacionyremisiones.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cbformulacionyremisiones.setForeground(new java.awt.Color(26, 82, 118));
        cbformulacionyremisiones.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Seleccione", "Formulación", "Remisión" }));
        cbformulacionyremisiones.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbformulacionyremisionesItemStateChanged(evt);
            }
        });
        panelremform.add(cbformulacionyremisiones, new org.netbeans.lib.awtextra.AbsoluteConstraints(23, 69, 160, -1));

        txtformulacionyremisiones.setColumns(20);
        txtformulacionyremisiones.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txtformulacionyremisiones.setForeground(new java.awt.Color(26, 82, 118));
        txtformulacionyremisiones.setRows(5);
        txtformulacionyremisiones.setTabSize(3);
        txtformulacionyremisiones.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtformulacionyremisionesKeyReleased(evt);
            }
        });
        jScrollPane10.setViewportView(txtformulacionyremisiones);

        panelremform.add(jScrollPane10, new org.netbeans.lib.awtextra.AbsoluteConstraints(23, 107, 778, 434));

        btnImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/impresora_2.png"))); // NOI18N
        btnImprimir.setBorderPainted(false);
        btnImprimir.setContentAreaFilled(false);
        btnImprimir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimirActionPerformed(evt);
            }
        });
        panelremform.add(btnImprimir, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 30, 64, 64));

        contenedorPaneles.addTab("Remisiones y Formulaciones", panelremform);

        jPanel10.add(contenedorPaneles, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, -1, 590));

        txtMensajeHC.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtMensajeHC.setForeground(new java.awt.Color(26, 82, 118));
        jPanel10.add(txtMensajeHC, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, 1080, 60));

        getContentPane().add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1380, 830));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        vp.bandhc = 0;
        psegtrat.terminar();
    }//GEN-LAST:event_formWindowClosed

    private void slAnguloVerticalStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_slAnguloVerticalStateChanged
        txtAnguloVertical.setText(slAnguloVertical.getValue() + "º");
    }//GEN-LAST:event_slAnguloVerticalStateChanged

    private void slAnguloGonianoStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_slAnguloGonianoStateChanged
        txtAnguloGoniano.setText(slAnguloGoniano.getValue() + "º");
    }//GEN-LAST:event_slAnguloGonianoStateChanged

    private void slLongitudRamaStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_slLongitudRamaStateChanged
        txtLongitudRama.setText(slLongitudRama.getValue() + " mm");
    }//GEN-LAST:event_slLongitudRamaStateChanged

    private void cbLineaMSItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbLineaMSItemStateChanged
        if (cbLineaMS.getSelectedIndex() == 2) {
            verDesviacionLMS(true);
        } else {
            verDesviacionLMS(false);
        }
    }//GEN-LAST:event_cbLineaMSItemStateChanged

    private void cbLineaMIItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbLineaMIItemStateChanged
        if (cbLineaMI.getSelectedIndex() == 2) {
            verDesviacionLMI(true);
        } else {
            verDesviacionLMI(false);
        }
    }//GEN-LAST:event_cbLineaMIItemStateChanged

    private void btnLMIDesviacionDMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLMIDesviacionDMousePressed
        if (btnLMIDesviacionD.isEnabled()) {
            selectBotonInteruptor(btnLMIDesviacionD, btnLMIDesviacionI, true);
            desviacionLMI = "D";//derecha = D; izquierda = I
        }
    }//GEN-LAST:event_btnLMIDesviacionDMousePressed

    private void btnLMIDesviacionIMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLMIDesviacionIMousePressed
        if (btnLMIDesviacionI.isEnabled()) {
            selectBotonInteruptor(btnLMIDesviacionI, btnLMIDesviacionD, true);
            desviacionLMI = "I";//derecha = D; izquierda = I
        }
    }//GEN-LAST:event_btnLMIDesviacionIMousePressed

    private void btnSimetriaSiMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSimetriaSiMousePressed
        if (btnSimetriaSi.isEnabled()) {
            selectBotonInteruptor(btnSimetriaSi, btnSimetriaNo, true);
            simetria = "Si";//si = 0; no = 1
        }
    }//GEN-LAST:event_btnSimetriaSiMousePressed

    private void btnSimetriaNoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSimetriaNoMousePressed
        if (btnSimetriaNo.isEnabled()) {
            selectBotonInteruptor(btnSimetriaNo, btnSimetriaSi, true);
            simetria = "No";//si = 0; no = 1
        }
    }//GEN-LAST:event_btnSimetriaNoMousePressed

    private void btnPronosticoBMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPronosticoBMousePressed
        if (btnPronosticoB.isEnabled()) {
            selectBotonInteruptor(btnPronosticoB, btnPronosticoR, true);
            pronostico = "Bueno";//bueno = 0; reservado = 1
        }
    }//GEN-LAST:event_btnPronosticoBMousePressed

    private void btnPronosticoRMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPronosticoRMousePressed
        if (btnPronosticoR.isEnabled()) {
            selectBotonInteruptor(btnPronosticoR, btnPronosticoB, true);
            pronostico = "Reservado";//bueno = 0; reservado = 1
        }
    }//GEN-LAST:event_btnPronosticoRMousePressed

    private void slAnguloIIStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_slAnguloIIStateChanged
        txtAnguloII.setText(slAnguloII.getValue() + "°");
    }//GEN-LAST:event_slAnguloIIStateChanged

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
//        if (chkCerrarHC.isSelected()) {
//            editarSeguimientoDeImgenes(true);
//            JOptionPane.showMessageDialog(this, "La historia clinica se encuentra TERMINADA,\nno es posible efectuar modificaciones sobre la misma.");
//        } else {
        editarHistoriaClinica(true);
        setEstadosBotonesDeControl(1);
//        }
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnDescartarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDescartarActionPerformed
        bandbp = 0;
//        editarHistoriaClinica(false);
//        setEstadosBotonesDeControl(0);
        limpiarComponentesHC();
        reiniciarHistoriaClinica();
        detenerHilosegTrat = true;
    }//GEN-LAST:event_btnDescartarActionPerformed

    private void listPreInterconsultaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_listPreInterconsultaKeyReleased
        if (KeyEvent.VK_ENTER == evt.getKeyCode() || KeyEvent.VK_SPACE == evt.getKeyCode()) {
            if (!estaSeleccionado(modeloInterconsulta, listPreInterconsulta.getSelectedValue())) {
                modeloInterconsulta.addElement(listPreInterconsulta.getSelectedValue());
                txtInterconsulta.setModel(modeloInterconsulta);
                listPreInterconsulta.requestFocusInWindow();
            }
        }
    }//GEN-LAST:event_listPreInterconsultaKeyReleased

    private void txtInterconsultaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtInterconsultaMousePressed
        if (evt.getClickCount() == 2) {
            //colocar un jconfirmdialog
            modeloInterconsulta.remove(txtInterconsulta.getSelectedIndex());
            txtInterconsulta.setModel(modeloInterconsulta);
        }
    }//GEN-LAST:event_txtInterconsultaMousePressed

    private void listPreHabitosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_listPreHabitosKeyReleased
        if (KeyEvent.VK_ENTER == evt.getKeyCode() || KeyEvent.VK_SPACE == evt.getKeyCode()) {
            if (!estaSeleccionado(modeloHabitos, listPreHabitos.getSelectedValue())) {
                modeloHabitos.addElement(listPreHabitos.getSelectedValue());
                txtHabitos.setModel(modeloHabitos);
                listPreHabitos.requestFocusInWindow();
            }
        }
    }//GEN-LAST:event_listPreHabitosKeyReleased

    private void txtHabitosMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtHabitosMousePressed
        if (evt.getClickCount() == 2) {
            //colocar un jconfirmdialog
            modeloHabitos.removeElement(txtHabitos.getSelectedValue());
            txtHabitos.setModel(modeloHabitos);
        }
    }//GEN-LAST:event_txtHabitosMousePressed

    private void tblAnamnesisMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAnamnesisMousePressed
        int f = tblAnamnesis.getSelectedRow();
        int c = tblAnamnesis.getSelectedColumn();
        int fc = tblAnamnesis.getRowCount() - 1;

        switch (c) {
            case 0:
                if (f == fc) {
                    tanaModelo.isCellEditable(f, c);
                    tanaModelo.setValueAt("x", f, 1);
                    tanaModelo.setValueAt("", f, 2);
                    tanaModelo.setValueAt("", f, 3);
                }
                break;
            case 1:
                if (f > Parametros.cantDatosBasicos) {
                    tanaModelo.setValueAt("x", f, 1);
                    tanaModelo.setValueAt("", f, 2);
                    tanaModelo.setValueAt("", f, 3);
                    break;
                }
                tanaModelo.setValueAt("", f, 2);
                tanaModelo.setValueAt("", f, 3);
                tanaModelo.setValueAt("x", f, c);
                break;
            case 2:
                if (f > Parametros.cantDatosBasicos) {
                    tanaModelo.setValueAt("x", f, 1);
                    tanaModelo.setValueAt("", f, 2);
                    tanaModelo.setValueAt("", f, 3);
                    break;
                }
                tanaModelo.setValueAt("", f, 1);
                tanaModelo.setValueAt("", f, 3);
                tanaModelo.setValueAt("x", f, c);
                break;
            case 3:
                if (f > Parametros.cantDatosBasicos) {
                    tanaModelo.setValueAt("x", f, 1);
                    tanaModelo.setValueAt("", f, 2);
                    tanaModelo.setValueAt("", f, 3);
                    break;
                }
                tanaModelo.setValueAt("", f, 1);
                tanaModelo.setValueAt("", f, 2);
                tanaModelo.setValueAt("x", f, c);
                break;
            default:
                break;
        }
    }//GEN-LAST:event_tblAnamnesisMousePressed

    private void tblSegTratMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSegTratMousePressed
        int ultimafila = tblSegTrat.getRowCount() - 1;

        if (evt.getClickCount() == 2 && tblSegTrat.isEnabled()) {
            int f = tblSegTrat.getSelectedRow();
            int c = tblSegTrat.getSelectedColumn();

            if (c == 2 && f == ultimafila) {
                new ventanaObservacionesTrat(this, f, c).setVisible(true);
            } else {
                new ventanaObservacionesTrat(this, f, c, false).setVisible(true);
            }
        }
    }//GEN-LAST:event_tblSegTratMousePressed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        cargarIndices();
    }//GEN-LAST:event_formWindowOpened

    private void txtLMSMilimetrosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLMSMilimetrosKeyReleased
        Expresiones.procesarSoloNumeros(txtLMSMilimetros);
    }//GEN-LAST:event_txtLMSMilimetrosKeyReleased

    private void txtLMIMilimetrosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLMIMilimetrosKeyReleased
        Expresiones.procesarSoloNumeros(txtLMIMilimetros);
    }//GEN-LAST:event_txtLMIMilimetrosKeyReleased

    private void txtSpeedKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSpeedKeyReleased
        Expresiones.procesarSoloNumeros(txtSpeed);
    }//GEN-LAST:event_txtSpeedKeyReleased

    private void txtOverBiteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtOverBiteKeyReleased
        Expresiones.procesarSoloNumeros(txtOverBite);
    }//GEN-LAST:event_txtOverBiteKeyReleased

    private void txtOverJetKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtOverJetKeyReleased
        Expresiones.procesarSoloNumeros(txtOverJet);
    }//GEN-LAST:event_txtOverJetKeyReleased

    private void txtDocumentoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDocumentoKeyReleased
        Expresiones.procesarSoloNumeros(txtDocumento);
    }//GEN-LAST:event_txtDocumentoKeyReleased

    private void cbTipoDocumentoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbTipoDocumentoItemStateChanged
        System.out.println("###evento cbTipoDocumentoItemStateChanged");
        mostrarDatosDelPaciente();
    }//GEN-LAST:event_cbTipoDocumentoItemStateChanged

    private void habnextMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_habnextMousePressed
        if (!estaSeleccionado(modeloHabitos, listPreHabitos.getSelectedValue()) && habnext.isEnabled()) {
            modeloHabitos.addElement(listPreHabitos.getSelectedValue());
            txtHabitos.setModel(modeloHabitos);
            listPreHabitos.requestFocusInWindow();
        }
    }//GEN-LAST:event_habnextMousePressed

    private void internextMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_internextMousePressed
        if (!estaSeleccionado(modeloInterconsulta, listPreInterconsulta.getSelectedValue()) && internext.isEnabled()) {
            modeloInterconsulta.addElement(listPreInterconsulta.getSelectedValue());
            txtInterconsulta.setModel(modeloInterconsulta);
            listPreInterconsulta.requestFocusInWindow();
        }
    }//GEN-LAST:event_internextMousePressed

    private void contenedorPanelesStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_contenedorPanelesStateChanged
        if (imagenes != null) {
            imagenes.actualizar();
        }
    }//GEN-LAST:event_contenedorPanelesStateChanged

    private void cbTratamientosItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbTratamientosItemStateChanged
        if (tratamientosxpaciente.size() > 0 && cbTratamientos.getSelectedIndex() >= 0) {
            if (!txtDocumento.getText().isEmpty() && !txtDocumento.isEnabled()) {
                for(int i = 0; i <tratamientosxpaciente.size(); i++){
                    for(int j = 0; j < tratamientosxpaciente.get(i).length; j++){
                        System.out.println("tratamientosxpaciente.get("+i+")["+j+"]---->"+tratamientosxpaciente.get(i)[j]);
                    }
                    System.out.println("*****************************************************");
                }
                
                
                cargarSeguimientoDelTratamiento(
                        cbTipoDocumento.getSelectedItem() + txtDocumento.getText(),
                        tratamientosxpaciente.get(cbTratamientos.getSelectedIndex())[0]
                );
            }
        }
    }//GEN-LAST:event_cbTratamientosItemStateChanged

    private void btnAgregarFilaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarFilaActionPerformed
        if (band == 0) {
            agregarFilaSegTrat(new String[tblSegTrat.getColumnCount()]);
            band = 1;
        }
    }//GEN-LAST:event_btnAgregarFilaActionPerformed

    private void btnLMSDesviacionIMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLMSDesviacionIMousePressed
        if (btnLMSDesviacionI.isEnabled()) {
            selectBotonInteruptor(btnLMSDesviacionI, btnLMSDesviacionD, true);
            desviacionLMS = "I";//derecha = 0; izquierda = 1
        }
    }//GEN-LAST:event_btnLMSDesviacionIMousePressed

    private void btnLMSDesviacionDMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLMSDesviacionDMousePressed
        if (btnLMSDesviacionD.isEnabled()) {
            selectBotonInteruptor(btnLMSDesviacionD, btnLMSDesviacionI, true);
            desviacionLMS = "D";//derecha = 0; izquierda = 1
        }
    }//GEN-LAST:event_btnLMSDesviacionDMousePressed

    private void jLabel30MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel30MouseClicked
        this.setState(ventana.ICONIFIED);
    }//GEN-LAST:event_jLabel30MouseClicked

    private void jLabel31MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel31MouseClicked
        vp.bandhc = 0;
        this.dispose();
    }//GEN-LAST:event_jLabel31MouseClicked

    private void jPanel11MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel11MousePressed
        x = evt.getX();
        y = evt.getY();
    }//GEN-LAST:event_jPanel11MousePressed

    private void jPanel11MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel11MouseDragged
        this.setLocation(this.getLocation().x + evt.getX() - x, this.getLocation().y + evt.getY() - y);
    }//GEN-LAST:event_jPanel11MouseDragged

    private void txtformulacionyremisionesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtformulacionyremisionesKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            establecerNumeracionFormulacion(txtformulacionyremisiones.getText());
        }
    }//GEN-LAST:event_txtformulacionyremisionesKeyReleased

    private void cbformulacionyremisionesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbformulacionyremisionesItemStateChanged
        if (cbformulacionyremisiones.getSelectedIndex() == 1) {
            if (txtformulacionyremisiones.getText().isEmpty()) {
                txtformulacionyremisiones.setText("1.\t");
            }
        } //        else if(cbformulacionyremisiones.getSelectedIndex() == 2){
        //            if(remisiones != null && !remisiones.isEmpty()){
        //               txtformulacionyremisiones.setText(Utilidades.decodificarElemento(remisiones));
        //            }
        //        }
        else {
            txtformulacionyremisiones.setText("");
        }
    }//GEN-LAST:event_cbformulacionyremisionesItemStateChanged

    private void btnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirActionPerformed
        if (btnImprimir.isEnabled()) {
            imprimirRemisiones(cbformulacionyremisiones.getSelectedIndex());
        }
    }//GEN-LAST:event_btnImprimirActionPerformed

    private void btnGuardarMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGuardarMousePressed
        if (evt.getClickCount() == 1 && btnGuardar.isEnabled()) {
            cargarIndices();
            guardarHistoriaClinica(1);
            bandbp = 0;
            detenerHilosegTrat = true;
        }
    }//GEN-LAST:event_btnGuardarMousePressed

    private void btnConsultarMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConsultarMousePressed
        if (evt.getClickCount() == 1 && btnConsultar.isEnabled()) {
            if (bandbp == 0) {
                detenerHilosegTrat = false;
                ventbp = new ventanaBusquedapacienteGralrsrd(this, "");
                ventbp.setVisible(true);
            } else {
                ventbp.setState(ventanaBusquedapacienteGralrsrd.NORMAL);
            }
        }
    }//GEN-LAST:event_btnConsultarMousePressed

    private void chkCerrarHCMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_chkCerrarHCMousePressed
        if (chkCerrarHC.isEnabled()) {
            if (!chkCerrarHC.isSelected()) {
                int opc = JOptionPane.showConfirmDialog(this, "¿Esta a punto de cerrar la historia clinica?\n"
                        + "Si es lo que desea presiones SI, "
                        + "de lo contrario presione NO.", "Confirmar", JOptionPane.YES_NO_OPTION);
                chkCerrarHC.setSelected(opc == JOptionPane.YES_OPTION);
            } else {
                int opc = JOptionPane.showConfirmDialog(this, "¿La historia clinica se encuentra cerrada, desea reabrirla?\n"
                        + "Si es lo que desea presiones SI, "
                        + "de lo contrario presione NO.", "Confirmar", JOptionPane.YES_NO_OPTION);
                chkCerrarHC.setSelected(!(opc == JOptionPane.YES_OPTION));
            }
        }
    }//GEN-LAST:event_chkCerrarHCMousePressed

    public ArrayList<String> getAnamnesis() {
        ArrayList<String> datos = new ArrayList<>();

        for (int i = 0; i < tanaModelo.getRowCount(); i++) {
            String cols = "";
            for (int j = 1; j < tanaModelo.getColumnCount(); j++) {
                if (((String) tanaModelo.getValueAt(i, j)).equalsIgnoreCase("x")) {
                    switch (j) {
                        case 1:
                            cols = "SI";
                            break;
                        case 2:
                            cols = "NO";
                            break;
                        case 3:
                            cols = "NO SABE";
                            break;
                    }
                    break;
                }
            }
            datos.add(cols);
        }
        datos.add(txtMotivoConsulta.getText().trim());

        if (!((String) tanaModelo.getValueAt(tanaModelo.getRowCount() - 1, 0)).trim().isEmpty()) {
            OtroAnam = (String) tanaModelo.getValueAt(tanaModelo.getRowCount() - 1, 0);
            OtroAnam = OtroAnam.substring(0, 1).toUpperCase() + OtroAnam.substring(1);
        }

        for (int i = 0; i < datos.size() - (!OtroAnam.isEmpty() ? 1 : 0); i++) {
            if (datos.get(i).isEmpty()) {
                anamVacia &= true;
                datos.set(i, "NO");
            } else {
                anamVacia &= false;
            }
        }

        return datos;
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
            java.util.logging.Logger.getLogger(ventana.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ventana.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ventana.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ventana.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ventana(null).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarFila;
    private javax.swing.JButton btnConsultar;
    private javax.swing.JButton btnDescartar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnImprimir;
    public javax.swing.JLabel btnLMIDesviacionD;
    public javax.swing.JLabel btnLMIDesviacionI;
    public javax.swing.JLabel btnLMSDesviacionD;
    public javax.swing.JLabel btnLMSDesviacionI;
    private javax.swing.JButton btnModificar;
    public javax.swing.JLabel btnPronosticoB;
    public javax.swing.JLabel btnPronosticoR;
    public javax.swing.JLabel btnSimetriaNo;
    public javax.swing.JLabel btnSimetriaSi;
    public javax.swing.JRadioButton canderclasei;
    public javax.swing.JRadioButton canderclaseii;
    public javax.swing.JRadioButton canderclaseiii;
    public javax.swing.JRadioButton canizqclasei;
    public javax.swing.JRadioButton canizqclaseii;
    public javax.swing.JRadioButton canizqclaseiii;
    public javax.swing.JComboBox cbInferior;
    public javax.swing.JComboBox cbLineaMI;
    public javax.swing.JComboBox cbLineaMS;
    public javax.swing.JComboBox cbSuperior;
    public javax.swing.JComboBox<String> cbTipoDocumento;
    public javax.swing.JComboBox<String> cbTratamientos;
    private javax.swing.JComboBox<String> cbformulacionyremisiones;
    public javax.swing.JCheckBox chkCerrarHC;
    private javax.swing.JTabbedPane contenedorPaneles;
    private javax.swing.ButtonGroup grupoCaninoDerecho;
    private javax.swing.ButtonGroup grupoCaninoIzquierdo;
    private javax.swing.ButtonGroup grupoMolarDerecho;
    private javax.swing.ButtonGroup grupoMolarIzquierdo;
    private javax.swing.ButtonGroup grupoPremolarDerecho;
    private javax.swing.ButtonGroup grupoPremolarIzquierdo;
    private javax.swing.JLabel habnext;
    private javax.swing.JLabel internext;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    public javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JLabel lblLMIDesviacion;
    private javax.swing.JLabel lblLMIMilimetros;
    private javax.swing.JLabel lblLMIPor;
    private javax.swing.JLabel lblLMSDesviacion;
    private javax.swing.JLabel lblLMSMilimetros;
    private javax.swing.JLabel lblLMSPor;
    private javax.swing.JList<String> listPreHabitos;
    private javax.swing.JList<String> listPreInterconsulta;
    public javax.swing.JRadioButton molderclasei;
    public javax.swing.JRadioButton molderclaseii;
    public javax.swing.JRadioButton molderclaseiii;
    public javax.swing.JRadioButton molizqclasei;
    public javax.swing.JRadioButton molizqclaseii;
    public javax.swing.JRadioButton molizqclaseiii;
    private javax.swing.JPanel panLMI;
    private javax.swing.JPanel panLMI1;
    private javax.swing.JPanel panLMI2;
    private javax.swing.JPanel panLMS;
    private javax.swing.JPanel panPadre;
    private javax.swing.JPanel pananamnesis;
    private javax.swing.JPanel panelremform;
    private javax.swing.JPanel pansegtratamiento;
    public javax.swing.JRadioButton premolderclasei;
    public javax.swing.JRadioButton premolderclaseii;
    public javax.swing.JRadioButton premolderclaseiii;
    public javax.swing.JRadioButton premolizqclasei;
    public javax.swing.JRadioButton premolizqclaseii;
    public javax.swing.JRadioButton premolizqclaseiii;
    private javax.swing.JSeparator sep1;
    private javax.swing.JSeparator sep2;
    private javax.swing.JSeparator sep3;
    private javax.swing.JSeparator sep4;
    private javax.swing.JSeparator sep5;
    private javax.swing.JSeparator sep6;
    private javax.swing.JSeparator sep7;
    private javax.swing.JSeparator sep8;
    public javax.swing.JSlider slAnguloGoniano;
    public javax.swing.JSlider slAnguloII;
    public javax.swing.JSlider slAnguloVertical;
    public javax.swing.JSlider slLongitudRama;
    public javax.swing.JTable tblAnamnesis;
    public volatile javax.swing.JTable tblSegTrat;
    private javax.swing.JLabel txtAnguloGoniano;
    private javax.swing.JLabel txtAnguloII;
    private javax.swing.JLabel txtAnguloVertical;
    public javax.swing.JTextField txtApellidos;
    public javax.swing.JTextField txtDocumento;
    public javax.swing.JLabel txtEmailPac;
    public javax.swing.JLabel txtEstado;
    public javax.swing.JTextArea txtExamenes;
    public javax.swing.JList<String> txtHabitos;
    public javax.swing.JLabel txtIdPaciente;
    public javax.swing.JList<String> txtInterconsulta;
    public javax.swing.JTextField txtLMIMilimetros;
    public javax.swing.JTextField txtLMSMilimetros;
    private javax.swing.JLabel txtLongitudRama;
    public javax.swing.JLabel txtMensajeHC;
    public javax.swing.JTextArea txtMotivoConsulta;
    public javax.swing.JTextField txtNombres;
    public javax.swing.JTextArea txtObservaciones;
    public javax.swing.JTextField txtOverBite;
    public javax.swing.JTextField txtOverJet;
    public javax.swing.JLabel txtPApellido;
    public javax.swing.JLabel txtPNombre;
    public javax.swing.JLabel txtSApellido;
    public javax.swing.JLabel txtSNombre;
    public javax.swing.JTextField txtSpeed;
    private javax.swing.JTextArea txtformulacionyremisiones;
    // End of variables declaration//GEN-END:variables

    private void creaPestanias() {
        odontograma.setMinimumSize(new Dimension(1200, 750));
        odontograma.setMaximumSize(new Dimension(1200, 750));
        odontograma.setPreferredSize(new Dimension(1100, 750));

        JScrollPane scroll = new JScrollPane(odontograma, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        JScrollPane scrollPadre = new JScrollPane(panPadre, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        JScrollPane scrollSegTrat = new JScrollPane(pansegtratamiento, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        JScrollPane scrollImagenes = new JScrollPane(imagenes, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        contenedorPaneles.add(scrollPadre, "Historia Clinica");
        contenedorPaneles.add(scrollImagenes, "Imagenes");
        contenedorPaneles.add(scrollSegTrat, "Seguimiento del Tratamiento");
        contenedorPaneles.add(scroll, "Odontograma");

        contenedorPaneles.setSelectedIndex(2);
    }

    private void verDesviacionLMS(boolean mostrar) {
        lblLMSDesviacion.setVisible(mostrar);
        lblLMSMilimetros.setVisible(mostrar);
        lblLMSPor.setVisible(mostrar);
        panLMS.setVisible(mostrar);
        txtLMSMilimetros.setVisible(mostrar);
        sep1.setVisible(mostrar);
    }

    private void verDesviacionLMI(boolean mostrar) {
        lblLMIDesviacion.setVisible(mostrar);
        lblLMIMilimetros.setVisible(mostrar);
        lblLMIPor.setVisible(mostrar);
        panLMI.setVisible(mostrar);
        txtLMIMilimetros.setVisible(mostrar);
        sep2.setVisible(mostrar);
    }

    public void cargarDatosPaciente(String tipoyDoc) {
        System.out.println("tipoyDoc----"+tipoyDoc);
        gestorMySQL SQL = new gestorMySQL();
//------------------------------------------------------------------------------
        //<editor-fold defaultstate="collapsed" desc="se busca el pk de la historia clinica">
        String consulta = "SELECT pk_historia_clinica FROM historias_clinicas WHERE pfk_paciente = '" + tipoyDoc + "'";
        String idhc = SQL.unicoDato(consulta);
        //</editor-fold>
        if (idhc != null) {
            indices[0] = Integer.parseInt(idhc);
            //<editor-fold defaultstate="collapsed" desc="se busca el ultimo odontograma por HC">
            consulta = "SELECT max(pfk_odontograma) FROM hc_odontograma WHERE pfk_historia_clinica = " + indices[0];
            String ultOdontograma = SQL.unicoDato(consulta);
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="ODONTOGRAMA">
            consulta = "SELECT\n"
                    + "pfk_diente,\n"
                    + "posicion,\n"
                    + "estado,\n"
                    + "ausente\n"
                    + "FROM\n"
                    + "hc_odontograma\n"
                    + "WHERE\n"
                    + "pfk_historia_clinica = " + idhc + "\n"
                    + "AND pfk_odontograma = " + ultOdontograma;
            //</editor-fold>

            datosPaciente = SQL.SELECT(consulta);

            //<editor-fold defaultstate="collapsed" desc="SE CARGAN LOS DATOS DEL ODONTOGRAMA">
            if (datosPaciente.size() > 0) {
                for (int i = 0; i < datosPaciente.size(); i++) {
                    for (int j = 0; j < odontograma.dientes.size(); j++) {
                        if (odontograma.dientes.get(j).id.equals(datosPaciente.get(i)[0])) {//si el id del diente es el mismo que viene de la BD
                            addConvAlDiente(odontograma.dientes.get(j), datosPaciente.get(i)[1], datosPaciente.get(i)[2]);
                        }
                    }
                }
            } else {
                odontograma.limpiar();
            }
            //</editor-fold>
        }

//------------------------------------------------------------------------------
        //<editor-fold defaultstate="collapsed" desc="ANALISIS SAGITAL">
        consulta = "SELECT\n"
                + "(SELECT angulo_incisivo_inferior FROM hc_analisis_sagital WHERE swing.`pfk_historia_clinica` = pfk_historia_clinica) AS ANGULO_INC_INF,\n"
                + "lado AS LADO,\n"
                + "tipo_diente AS TIPO_DIENTE,\n"
                + "clase AS CLASE\n"
                + "FROM\n"
                + "hc_swing swing\n"
                + "WHERE\n"
                + "swing.`pfk_historia_clinica` = (SELECT pk_historia_clinica FROM historias_clinicas WHERE pfk_paciente = '" + tipoyDoc + "')";
        //</editor-fold>

        datosPaciente = SQL.SELECT(consulta);

        //<editor-fold defaultstate="collapsed" desc="SE CARGAN LOS DATOS DEL ANALISIS SAGITAL">
        if (datosPaciente.size() > 0) {
            slAnguloII.setValue(Integer.parseInt(datosPaciente.get(0)[0]));//angulo incisivo inferior
            for (int i = 0; i < datosPaciente.size(); i++) {
                int indClase = Integer.parseInt(datosPaciente.get(i)[3]) - 1;
                int indTDiente = -1;
                if (datosPaciente.get(i)[1].equals("D")) {
                    switch (datosPaciente.get(i)[2]) {
                        case "M":
                            indTDiente = 0;
                            break;
                        case "P":
                            indTDiente = 1;
                            break;
                        case "C":
                            indTDiente = 2;
                            break;
                        default:
                            indTDiente = -1;
                    }
                } else if (datosPaciente.get(i)[1].equals("I")) {
                    switch (datosPaciente.get(i)[2]) {
                        case "M":
                            indTDiente = 3;
                            break;
                        case "P":
                            indTDiente = 4;
                            break;
                        case "C":
                            indTDiente = 5;
                            break;
                        default:
                            indTDiente = -1;
                    }
                }

                if (indTDiente != -1) {
                    swing.get(indTDiente)[indClase].setSelected(true);
                }
            }

            editarANALISIS_SAGITAL(false);
        } else {
            slAnguloII.setValue(0);
            limpiarRB();
            limpiarANALISIS_SAGITAL(true);
        }
        //</editor-fold>

//------------------------------------------------------------------------------
        //<editor-fold defaultstate="collapsed" desc="ANALISIS VERTICAL">
        consulta = "SELECT\n"
                + "angulo_vertical AS ANG_VERT,\n"
                + "angulo_goniano AS ANG_GONI,\n"
                + "longitud_de_rama AS LONG_RAMA\n"
                + "FROM\n"
                + "historias_clinicas hicl,\n"
                + "hc_analisis_vertical hcav\n"
                + "WHERE\n"
                + "hicl.`pk_historia_clinica` =  hcav.`pfk_historia_clinica`\n"
                + "AND hicl.`pfk_paciente` = '" + tipoyDoc + "'";
        //</editor-fold>

        datosPaciente = SQL.SELECT(consulta);

        //<editor-fold defaultstate="collapsed" desc="SE CARGAN LOS DATOS DEL ANALISIS VERTICAL">
        if (datosPaciente.size() > 0) {
            for (int i = 0; i < datosPaciente.size(); i++) {
                slAnguloVertical.setValue(Integer.parseInt(datosPaciente.get(i)[0]));
                slAnguloGoniano.setValue(Integer.parseInt(datosPaciente.get(i)[1]));
                slLongitudRama.setValue(Integer.parseInt(datosPaciente.get(i)[2]));
            }
            editarANALISIS_VERTICAL(false);
        } else {
            limpiarANALISIS_VERTICAL(true);
        }
        //</editor-fold>

//------------------------------------------------------------------------------
        //<editor-fold defaultstate="collapsed" desc="ANALISIS TRANSVERSAL">
        consulta = "SELECT\n"
                + "linea_m_superior AS LINEA_MSUP,\n"
                + "CASE WHEN linea_m_superior =  'C' THEN 'A' ELSE direccion_m_superior END AS DIR_MSUP,\n"
                + "CASE WHEN linea_m_superior =  'C' THEN 'A' ELSE IF(longitud_m_superior IS NULL OR `longitud_m_superior` = 'null', '', `longitud_m_superior`) END AS LONG_MSUP,\n"
                + "linea_m_inferior AS LINEA_MINF,\n"
                + "CASE WHEN linea_m_inferior =  'C' THEN 'A' ELSE direccion_m_inferior END AS DIR_MINF,\n"
                + "CASE WHEN linea_m_inferior =  'C' THEN 'A' ELSE IF(longitud_m_inferior IS NULL OR `longitud_m_inferior` = 'null', '', `longitud_m_inferior`) END AS LONG_MINF,\n"
                + "IFNULL(hab,'') AS HAB,\n"
                + "IFNULL(interconsulta,'') AS INTCON,\n"
                + "observacion AS OBS,\n"
                + "examenes AS EXAM,\n"
                + "simetria AS SIM,\n"
                + "pronostico AS PRONOS\n"
                + "FROM\n"
                + "historias_clinicas hicl,\n"
                + "hc_ant_tra hcat\n"
                + "WHERE\n"
                + "hicl.`pk_historia_clinica` =  hcat.`pfk_historia_clinica`\n"
                + "AND hicl.`pfk_paciente` = '" + tipoyDoc + "'";
        //</editor-fold>

//        System.out.println("consulta--TRANSVERSAL->"+consulta);
        datosPaciente = SQL.SELECT(consulta);

        //<editor-fold defaultstate="collapsed" desc="SE CARGAN LOS DATOS DEL ANALISIS TRANSVERSAL">
        if (datosPaciente.size() > 0) {
            for (int i = 0; i < datosPaciente.size(); i++) {
                if (datosPaciente.get(i)[0].equalsIgnoreCase("C")) {
                    cbLineaMS.setSelectedIndex(1);
                } else {
                    cbLineaMS.setSelectedIndex(2);
                    String mm = "";
                    //<editor-fold defaultstate="collapsed" desc="DESVIACION LMS">
                    if (datosPaciente.get(i)[1].equalsIgnoreCase("D")) {
                        selectBotonInteruptor(btnLMSDesviacionD, btnLMSDesviacionI, true);
                        mm = datosPaciente.get(i)[2];
                    } else if (datosPaciente.get(i)[1].equalsIgnoreCase("I")) {
                        selectBotonInteruptor(btnLMSDesviacionI, btnLMSDesviacionD, true);
                        mm = datosPaciente.get(i)[2];
                    } else {
                        mm = "";
                    }
                    //</editor-fold>
                    txtLMSMilimetros.setText(mm);
                }

                if (datosPaciente.get(i)[3].equalsIgnoreCase("C")) {
                    cbLineaMI.setSelectedIndex(1);
                } else {
                    cbLineaMI.setSelectedIndex(2);
                    String mm = "";
                    //<editor-fold defaultstate="collapsed" desc="DESVIACION LMI">
                    if (datosPaciente.get(i)[4].equalsIgnoreCase("D")) {
                        selectBotonInteruptor(btnLMSDesviacionD, btnLMSDesviacionI, true);
                        mm = datosPaciente.get(i)[5];
                    } else if (datosPaciente.get(i)[4].equalsIgnoreCase("I")) {
                        selectBotonInteruptor(btnLMSDesviacionI, btnLMSDesviacionD, true);
                        mm = datosPaciente.get(i)[5];
                    } else {
                        mm = "";
                    }
                    //</editor-fold>
                    txtLMIMilimetros.setText(mm);
                }

                if (datosPaciente.get(i)[6] != null) {
                    String[] habitos = datosPaciente.get(i)[6].split("#");
                    for (int j = 0; j < habitos.length; j++) {
                        if (!habitos[j].equalsIgnoreCase("null")) {
                            modeloHabitos.addElement(habitos[j]);
                        }
                    }
                }

                if (datosPaciente.get(i)[7] != null) {
                    String[] interConsulta = datosPaciente.get(i)[7].split("#");
                    for (int j = 0; j < interConsulta.length; j++) {
                        if (!interConsulta[j].equalsIgnoreCase("null")) {
                            modeloInterconsulta.addElement(interConsulta[j]);
                        }
                    }
                }

                int a = modeloInterconsulta.size();
                if (modeloInterconsulta.size() > 0) {
                    txtInterconsulta.setModel(modeloInterconsulta);
                } else {
                    modeloInterconsulta.removeAllElements();
                    txtInterconsulta.setModel(modeloInterconsulta);
                    listPreInterconsulta.clearSelection();
                }

                if (modeloHabitos.size() > 0) {
                    txtHabitos.setModel(modeloHabitos);
                } else {
                    modeloHabitos.removeAllElements();
                    txtHabitos.setModel(modeloHabitos);
                    listPreHabitos.clearSelection();
                }

                txtObservaciones.setText(datosPaciente.get(i)[8].equalsIgnoreCase("null") ? "" : datosPaciente.get(i)[8]);
                txtExamenes.setText(datosPaciente.get(i)[9].equalsIgnoreCase("null") ? "" : datosPaciente.get(i)[9]);

                //<editor-fold defaultstate="collapsed" desc="SIMETRIA">
                if (datosPaciente.get(i)[10].equalsIgnoreCase("si")) {
                    selectBotonInteruptor(btnSimetriaSi, btnSimetriaNo, true);
                    simetria = "Si";//si = 0; no = 1
                } else {
                    selectBotonInteruptor(btnSimetriaNo, btnSimetriaSi, true);
                    simetria = "No";//si = 0; no = 1
                }
                //</editor-fold>

                //<editor-fold defaultstate="collapsed" desc="PRONOSTICO">
                if (datosPaciente.get(i)[11].equalsIgnoreCase("BUENO")) {
                    selectBotonInteruptor(btnPronosticoB, btnPronosticoR, true);
                    pronostico = "Bueno";//si = 0; no = 1
                } else {
                    selectBotonInteruptor(btnPronosticoR, btnPronosticoB, true);
                    pronostico = "Reservado";//si = 0; no = 1
                }
                //</editor-fold>
            }
            editarANALISIS_TRANSVERSAL(false);
        } else {
            limpiarANALISIS_TRANSVERSAL(true);
        }
        //</editor-fold>

//------------------------------------------------------------------------------
        //<editor-fold defaultstate="collapsed" desc="FORMA DE ARCO">
        consulta = "SELECT\n"
                + "superior AS SUP,\n"
                + "inferior AS INF,\n"
                + "curva AS CURVA,\n"
                + "overbite AS OVERB,\n"
                + "overjet AS OVERJ\n"
                + "FROM\n"
                + "historias_clinicas hicl,\n"
                + "hc_forma_arco hcfa\n"
                + "WHERE\n"
                + "hicl.`pk_historia_clinica` =  hcfa.`pfk_historia_clinica`\n"
                + "AND hicl.`pfk_paciente` = '" + tipoyDoc + "'";
        //</editor-fold>

        datosPaciente = SQL.SELECT(consulta);

        //<editor-fold defaultstate="collapsed" desc="SE CARGAN LOS DATOS DE FORMA DE ARCO">
        if (datosPaciente.size() > 0) {
            for (int i = 0; i < datosPaciente.size(); i++) {
                for (int j = 0; j < cbSuperior.getItemCount(); j++) {
                    if (("" + cbSuperior.getItemAt(j)).indexOf(datosPaciente.get(i)[0]) == 0) {
                        cbSuperior.setSelectedIndex(j);
                    }

                    if (("" + cbInferior.getItemAt(j)).indexOf(datosPaciente.get(i)[1]) == 0) {
                        cbInferior.setSelectedIndex(j);
                    }
                }

                txtSpeed.setText(datosPaciente.get(i)[2]);
                txtOverBite.setText(datosPaciente.get(i)[3]);
                txtOverJet.setText(datosPaciente.get(i)[4]);
            }
            editarFORMA_DE_ARCO(false);
        } else {
            limpiarFORMA_DE_ARCO(true);
        }
        //</editor-fold>

//------------------------------------------------------------------------------
        //<editor-fold defaultstate="collapsed" desc="CIERRE DE LA HC">
        consulta = "SELECT\n"
                + "estado\n"
                + "FROM\n"
                + "historias_clinicas\n"
                + "WHERE pfk_paciente = '" + tipoyDoc + "'";
        //</editor-fold>

        datosPaciente = SQL.SELECT(consulta);

        //<editor-fold defaultstate="collapsed" desc="SE CARGAN LOS DATOS DEl CIERRE DE HC">
        if (datosPaciente.size() > 0) {
            chkCerrarHC.setSelected(datosPaciente.get(0)[0].equalsIgnoreCase("Terminada"));
        } else {
            chkCerrarHC.setSelected(false);
        }
        //</editor-fold>

//------------------------------------------------------------------------------
        
//        
        
        String tratamiento = "1";
        cargarTratamientos(tipoyDoc);
        
//        System.out.println("tratamientosxpaciente---"+tratamientosxpaciente.size());
//        System.out.println("cbTratamientos getSelectedIndex--->"+cbTratamientos.getSelectedIndex());
        //System.out.println("tratamientosxpaciente.get(cbTratamientos.getSelectedIndex())[0]"+);
        if(tratamientosxpaciente.size()>0){
            tratamiento = tratamientosxpaciente.get(cbTratamientos.getSelectedIndex())[0];
        }
        cargarSeguimientoDelTratamiento(tipoyDoc, tratamiento);
        cargarTablaANAMNESIS(tipoyDoc);
        cargarImagenes(tipoyDoc);
//        cargarRemisiones(tipoyDoc);
    }

    private void addConvAlDiente(Diente d, String seccion, String idConv) {
        Convencion conv = new Convencion();

        for (int i = 0; i < odontograma.convenciones.size(); i++) {
            if (idConv.equals("" + (odontograma.convenciones.get(i).id + 1))) {
                conv = new Convencion(odontograma.convenciones.get(i));
                break;
            }
        }

        //verificar las convenciones cuadradar
        conv.x = d.x;
        conv.y = d.y;
        conv.ANCHO = d.ancho;
        conv.ALTO = d.alto;
        if (!(seccion.equals("0") && idConv.equals("0"))) {//esto es para saber si el diente esta sano
            if (conv.tipoDeConvencion == tiposDeConvencion.CV_CUADRADA) {
                conv.desc += " " + Expresiones.getNombreSeccion(d, Integer.parseInt(seccion) - 1);
                d.secciones[Integer.parseInt(seccion) - 1] = 1;//se activa la seccion
                d.convencionXSecciones[Integer.parseInt(seccion) - 1] = conv;//se coloca la convencion por la seccion
                d.convenciones.add(0, conv);
            } else {
                d.convenciones.add(conv);
            }
        }
    }

    public void limpiarRB() {
        grupoMolarDerecho.clearSelection();
        grupoPremolarDerecho.clearSelection();
        grupoCaninoDerecho.clearSelection();
        grupoMolarIzquierdo.clearSelection();
        grupoPremolarIzquierdo.clearSelection();
        grupoCaninoIzquierdo.clearSelection();
    }

    private void cargarRB() {
        swing.add(new JRadioButton[]{molderclasei, molderclaseii, molderclaseiii});
        swing.add(new JRadioButton[]{premolderclasei, premolderclaseii, premolderclaseiii});
        swing.add(new JRadioButton[]{canderclasei, canderclaseii, canderclaseiii});
        swing.add(new JRadioButton[]{molizqclasei, molizqclaseii, molizqclaseiii});
        swing.add(new JRadioButton[]{premolizqclasei, premolizqclaseii, premolizqclaseiii});
        swing.add(new JRadioButton[]{canizqclasei, canizqclaseii, canizqclaseiii});
    }

    public void limpiarComponentesHC() {
        chkCerrarHC.setSelected(false);
        odontograma.limpiar();
        limpiarANALISIS_SAGITAL(true);
        limpiarANALISIS_VERTICAL(true);
        limpiarANALISIS_TRANSVERSAL(true);
        limpiarFORMA_DE_ARCO(true);
        limpiarDatosPaciente(true);
        limpiarFormulacionYRemisiones(true);
        cargarTablaSegTrat(null);
        txtMensajeHC.setText("");
        limpiarCBTratamientos();

    }

    public synchronized void cargarDatosTabla(ArrayList<String[]> datos) {
        tModelo = new DefaultTableModel(nombreColumnas, 0);

        if (datos != null) {
            for (int i = 0; i < datos.size(); i++) {
                agregarFilaSegTrat(datos.get(i));
            }
        }
        tblSegTrat.setModel(tModelo);
    }

    public synchronized void cargarTablaSegTrat(ArrayList<String[]> datos) {
        tModelo = new DefaultTableModel(nombreColumnas, 0);

        if (datos != null) {
            for (int i = 0; i < datos.size(); i++) {
                agregarFilaSegTrat(datos.get(i));
            }
        }
        tblSegTrat.setModel(tModelo);

        //<editor-fold defaultstate="collapsed" desc="AJUSTAR TAMAÑO CELDAS">
        int anchos[] = {6, 45, 183, 33, 33};
        for (int i = 0; i < tblSegTrat.getColumnCount(); i++) {
            tblSegTrat.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
        }
        //</editor-fold>
    }

    public void cargarTablaANAMNESIS(String idpac) {
        gestorMySQL SQL = new gestorMySQL();
        //<editor-fold defaultstate="collapsed" desc="CARGAR MOTIVO DE LA CONSULTA">
        String motivoConsulta = "select motivo FROM (\n"
                + "select\n"
                + "pk_anamnesis pk,\n"
                + "motivo_consulta motivo\n"
                + "from\n"
                + "anamnesis\n"
                + "where\n"
                + "pfk_paciente = '" + idpac + "'\n"
                + "order by pk_anamnesis DESC) TABLA LIMIT 0,1";
        String motivo = SQL.unicoDato(motivoConsulta);
        txtMotivoConsulta.setText(motivo != null ? motivo : "");
        //</editor-fold>

        tanaModelo = new DefaultTableModel(anamcolumnas, 0);
        ArrayList<String[]> datos = new ArrayList<>();
        //<editor-fold defaultstate="collapsed" desc="CONSULTA">
        String consulta = "";
        if (idpac != null) {
            consulta = "SELECT \n"
                    + "daba.`pk_datos_basicos` ID,\n"
                    + "daba.`descripcion` DESCRIPCION,\n"
                    + "CASE WHEN anpa.`estado` = 'SI' THEN 'x' ELSE ' ' END SI,\n"
                    + "CASE WHEN anpa.`estado` = 'NO' THEN 'x' ELSE ' ' END N_O,\n"
                    + "CASE WHEN anpa.`estado` = 'NO SABE' THEN 'x' ELSE ' ' END NO_SABE\n"
                    + "FROM\n"
                    + "anamnesis anam,\n"
                    + "anamnesisxpaciente anpa,\n"
                    + "datos_basicos daba\n"
                    + "WHERE\n"
                    + "anam.`pfk_paciente` = anpa.`pfk_paciente`\n"
                    + "AND anam.`pk_anamnesis` = anpa.`pfk_anamnesis`\n"
                    + "AND daba.`pk_datos_basicos` = anpa.`pfk_datos_basicos`\n"
                    + "AND anam.`pfk_paciente` = '" + idpac + "'\n"
                    + "AND anam.`pk_anamnesis` = (SELECT MAX(pk_anamnesis) FROM anamnesis WHERE pfk_paciente = '" + idpac + "')\n"
                    + "UNION\n"
                    + "SELECT \n"
                    + "daba.`pk_datos_basicos` ID,\n"
                    + "daba.`descripcion` DESCRIPCION,\n"
                    + "' ' SI,\n"
                    + "' ' N_O,\n"
                    + "' ' NO_SABE\n"
                    + "FROM\n"
                    + "datos_basicos daba\n"
                    + "WHERE\n"
                    + "daba.`pk_datos_basicos` = 13";
        } else {
            consulta = "SELECT pk_datos_basicos,descripcion,' ',' ',' ' FROM datos_basicos WHERE pk_datos_basicos <= 13 ORDER BY pk_datos_basicos ASC";
        }

        datos = SQL.SELECT(consulta);

        if (datos.size() <= 1) {
            consulta = "SELECT pk_datos_basicos,descripcion,' ',' ',' ' FROM datos_basicos WHERE pk_datos_basicos <= 13 ORDER BY pk_datos_basicos ASC";
            datos = SQL.SELECT(consulta);
        }
        //</editor-fold>
        idDatosBasicos = new String[datos.size()];
        for (int i = 0; i < datos.size(); i++) {
            idDatosBasicos[i] = datos.get(i)[0];
            String fila[] = new String[datos.get(i).length - 1];
            for (int j = 1; j < datos.get(i).length; j++) {
                fila[j - 1] = datos.get(i)[j];
            }
            agregarFilaANAM(fila);
            tblAnamnesis.isCellEditable(i, 0);
        }

        tblAnamnesis.setModel(tanaModelo);

        //<editor-fold defaultstate="collapsed" desc="AJUSTAR TAMAÑO CELDAS">
        int anchos[] = {288, 3, 3, 6};
        for (int i = 0; i < tblAnamnesis.getColumnCount(); i++) {
            tblAnamnesis.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
        }
        //</editor-fold>
        //EVENTOS SOBRE EL ENCABEZADO DE LA TABLA
        tblAnamnesis.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int c = tblAnamnesis.columnAtPoint(e.getPoint());//se obtiene el indice del encabezado
                if (tblAnamnesis.isEnabled()) {
                    if (c != 0) {
                        if (!(txtNombres.getText().isEmpty() || txtNombres.getText() == null)) {
                            for (int i = 0; i < tblAnamnesis.getModel().getRowCount() - 1; i++) {
                                switch (c) {
                                    case 1:
                                        tblAnamnesis.getModel().setValueAt("x", i, c);
                                        tblAnamnesis.getModel().setValueAt("", i, 2);
                                        tblAnamnesis.getModel().setValueAt("", i, 3);
                                        break;
                                    case 2:
                                        tblAnamnesis.getModel().setValueAt("x", i, c);
                                        tblAnamnesis.getModel().setValueAt("", i, 1);
                                        tblAnamnesis.getModel().setValueAt("", i, 3);
                                        break;
                                    case 3:
                                        tblAnamnesis.getModel().setValueAt("x", i, c);
                                        tblAnamnesis.getModel().setValueAt("", i, 1);
                                        tblAnamnesis.getModel().setValueAt("", i, 2);
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    private synchronized void agregarFilaSegTrat(String[] fila) {
        tModelo.addRow(fila);
    }

    private void agregarFilaANAM(String[] fila) {
        tanaModelo.addRow(fila);
    }

    private ArrayList<String[]> getDatosOdontograma() {
        int band = 0;
        ArrayList<String[]> dao = new ArrayList<>();

        for (int i = 0; i < odontograma.dientes.size(); i++) {
            String idDiente = odontograma.dientes.get(i).id;
            if (odontograma.dientes.get(i).convenciones.size() > 0) {
                for (int j = odontograma.dientes.get(i).convenciones.size() - 1; j >= 0; j--) {

                    if (odontograma.dientes.get(i).convenciones.get(j).tipoDeConvencion == tiposDeConvencion.CV_CUADRADA) {
                        for (int k = 0; k < odontograma.dientes.get(i).secciones.length; k++) {
                            if (odontograma.dientes.get(i).secciones[k] != 0) {
                                dao.add(new String[]{
                                    idDiente,
                                    "" + (k + 1),
                                    "" + (odontograma.dientes.get(i).convencionXSecciones[k].id + 1),
                                    "No"
                                });
                            }
                        }
                        break;
                    } else {
                        dao.add(new String[]{
                            idDiente,
                            "0",
                            "" + (odontograma.dientes.get(i).convenciones.get(j).id + 1),
                            "No"
                        });
                    }
                }
            } else {
                dao.add(new String[]{
                    idDiente,
                    "0",
                    "0",
                    "No"
                });
            }
        }
        return dao;
    }

    private void guardarHistoriaClinica() {
        int opc = -2;
        boolean imagenCargadayGuardada = false;
        //<editor-fold defaultstate="collapsed" desc="VALLIDACIONES">
        boolean adelante = true;
        ArrayList<String> anam = new ArrayList<>();
        anam = getAnamnesis();
        String mensaje = "";
        if (txtDocumento.getText().isEmpty()) {
            mensaje += "-Es necesario que especifique el documento de identidad del paciente.\n";
            txtDocumento.requestFocusInWindow();
            adelante &= false;
        } else {
            adelante &= true;
        }

        if (cbTipoDocumento.getSelectedIndex() == 0) {
            mensaje += "-Es necesario que especifique el tipo de documento del paciente.\n";
            cbTipoDocumento.requestFocusInWindow();
            adelante &= false;
        } else {
            adelante &= true;
        }

        if (anamVacia) {
            mensaje += "-Es necesario que ingrese los datos de la anamnesis del paciente.\n";
            contenedorPaneles.setSelectedIndex(0);
            txtMotivoConsulta.requestFocusInWindow();
            adelante &= false;
        } else {
            adelante &= true;
        }
        //</editor-fold>

        if (!adelante) {
            JOptionPane.showMessageDialog(this, mensaje);
            habilitarDatosPaciente();
        } else {
            gestorMySQL SQL = new gestorMySQL();
            ArrayList<String> INSERTS = new ArrayList<>();

            //<editor-fold defaultstate="collapsed" desc="VALIDAR SI EL PACIENTE YA EXISTE">
            String consulta = "SELECT\n"
                    + "pfk_paciente\n"
                    + "FROM\n"
                    + "pacientes\n"
                    + "WHERE\n"
                    + "pfk_paciente = '" + cbTipoDocumento.getSelectedItem() + txtDocumento.getText() + "' AND estado = 'Activo'";
            boolean existePaciente = SQL.ExistenDatos(consulta);
            //</editor-fold>

            if (existePaciente) {
                //<editor-fold defaultstate="collapsed" desc="VALIDAR SI EXISTE HC PARA EL PACIENTE">
                consulta = "SELECT\n"
                        + "hc.pk_historia_clinica\n"
                        + "FROM\n"
                        + "historias_clinicas hc\n"
                        + "WHERE\n"
                        + "hc.pfk_paciente = '" + cbTipoDocumento.getSelectedItem() + txtDocumento.getText() + "'";

                if (SQL.ExistenDatos(consulta) && !tipoPaciente.equalsIgnoreCase("p")) {
                    opc = JOptionPane.showConfirmDialog(this, "El paciente identificado con "
                            + cbTipoDocumento.getSelectedItem() + " número "
                            + txtDocumento.getText() + " ya tiene una historia clinica activa.\n"
                            + "¿Desea ver la historia clinica del paciente?", null, JOptionPane.YES_NO_OPTION);

                    if (opc == JOptionPane.YES_OPTION) {
                        cargarHistoriaClinica(cbTipoDocumento.getSelectedItem() + txtDocumento.getText());
                        tipoPaciente = "p";
                    } else {
                        habilitarDatosPaciente();
                        tipoPaciente = "a";
                    }
                } else {//si no existe una historia clinica
                    if (SQL.ExistenDatos(consulta)) {
                        indices[0] = Integer.parseInt(SQL.unicoDato(consulta));
                    } else {
                        //<editor-fold defaultstate="collapsed" desc="SE CREA LA HISTORIA CLINICA PARA EL PACIENTE">
                        INSERTS.add("INSERT INTO historias_clinicas VALUES(" + indices[0] + ",'" + cbTipoDocumento.getSelectedItem() + txtDocumento.getText() + "',NULL,'" + datosUsuario.datos.get(0)[0] + "',NOW(),NULL,NULL,'" + (chkCerrarHC.isSelected() ? "Terminada" : "Activa") + "');");

                        consulta = "SELECT CASE WHEN pfk_odontograma IS NOT NULL THEN MAX(pfk_odontograma+1) ELSE 1 END FROM hc_odontograma WHERE pfk_historia_clinica = " + indices[0];
                        String indexOdontograma = SQL.unicoDato(consulta);
                        imagenCargadayGuardada = odontograma.guardarImagenOdontograma(indexOdontograma, cbTipoDocumento.getSelectedItem() + txtDocumento.getText(), this);
                        //</editor-fold>
                    }
                }
                //</editor-fold>
            } else {//si no existe el paciente

                //<editor-fold defaultstate="collapsed" desc="GUARDAR DATOS DEL PACIENTE">
                INSERTS.add("INSERT INTO historias_clinicas VALUES(" + indices[0] + ",'" + cbTipoDocumento.getSelectedItem() + txtDocumento.getText() + "',NULL,'" + datosUsuario.datos.get(0)[0] + "',NOW(),NULL,NULL,'" + (chkCerrarHC.isSelected() ? "Terminada" : "Activa") + "');");
                INSERTS.add("INSERT INTO pacientes VALUES('" + cbTipoDocumento.getSelectedItem() + txtDocumento.getText() + "','Activo','" + datosUsuario.datos.get(0)[0] + "',NOW());");
                INSERTS.add("INSERT INTO personas VALUES('" + txtDocumento.getText() + "','" + cbTipoDocumento.getSelectedItem() + "'," + Utilidades.CodificarElemento(("'" + pn + "'," + (sn.isEmpty() ? "NULL" : "'" + sn + "'") + ",'" + pa + "'," + (sa.isEmpty() ? "NULL" : "'" + sa + "'")).toUpperCase()) + ",NULL,NULL,NULL,NULL);");
                INSERTS.add("UPDATE paciente_auxiliar SET estado = 'INACTIVO' WHERE pk_paciente_auxiliar = '" + txtIdPaciente.getText() + "'");

                consulta = "SELECT pfk_paciente FROM pacientexconfirmacion WHERE pfk_paciente =  '" + txtIdPaciente.getText() + "'";
                if (SQL.ExistenDatos(consulta) && !txtEstado.getText().equals("P")) {
                    INSERTS.add("UPDATE pacientexconfirmacion SET pfk_paciente = '" + cbTipoDocumento.getSelectedItem() + txtDocumento.getText() + "', usuario = '" + datosUsuario.datos.get(0)[0] + "', fecha = NOW() WHERE pfk_paciente =  '" + txtIdPaciente.getText() + "'");
                }

                consulta = "SELECT pfk_paciente FROM pacientexcotizaciones WHERE pfk_paciente =  '" + txtIdPaciente.getText() + "'";
                if (SQL.ExistenDatos(consulta) && !txtEstado.getText().equals("P")) {
                    INSERTS.add("UPDATE pacientexcotizaciones SET pfk_paciente = '" + cbTipoDocumento.getSelectedItem() + txtDocumento.getText() + "' WHERE pfk_paciente =  '" + txtIdPaciente.getText() + "'");
                }

                consulta = "SELECT pfk_paciente FROM pacientextratamiento WHERE pfk_paciente =  '" + txtIdPaciente.getText() + "'";
                if (SQL.ExistenDatos(consulta) && !txtEstado.getText().equals("P")) {
                    INSERTS.add("UPDATE pacientextratamiento SET pfk_paciente = '" + cbTipoDocumento.getSelectedItem() + txtDocumento.getText() + "' WHERE pfk_paciente =  '" + txtIdPaciente.getText() + "'");
                }

                consulta = "SELECT pfk_paciente FROM citas WHERE pfk_paciente =  '" + txtIdPaciente.getText() + "'";
                if (SQL.ExistenDatos(consulta) && !txtEstado.getText().equals("P")) {
                    INSERTS.add("UPDATE citas SET pfk_paciente = '" + cbTipoDocumento.getSelectedItem() + txtDocumento.getText() + "' WHERE pfk_paciente = '" + txtIdPaciente.getText() + "'");
                }

                consulta = "SELECT pfk_paciente FROM citasxhoras WHERE pfk_paciente =  '" + txtIdPaciente.getText() + "'";
                if (SQL.ExistenDatos(consulta) && !txtEstado.getText().equals("P")) {
                    INSERTS.add("UPDATE citasxhoras SET pfk_paciente = '" + cbTipoDocumento.getSelectedItem() + txtDocumento.getText() + "' WHERE pfk_paciente = '" + txtIdPaciente.getText() + "'");
                }

                consulta = "SELECT pfk_paciente FROM auditoria_agenda WHERE pfk_paciente =  '" + txtIdPaciente.getText() + "'";
                if (SQL.ExistenDatos(consulta) && !txtEstado.getText().equals("P")) {
                    INSERTS.add("UPDATE auditoria_agenda SET pfk_paciente = '" + cbTipoDocumento.getSelectedItem() + txtDocumento.getText() + "' WHERE pfk_paciente = '" + txtIdPaciente.getText() + "'");
                }
                //</editor-fold>
            }

            if (opc == -2) {
                //<editor-fold defaultstate="collapsed" desc="PROCESO DE GUARDADO">
                //<editor-fold defaultstate="collapsed" desc="GUARDAR ANAMNESIS">
                consulta = "SELECT CASE WHEN pk_anamnesis IS NOT NULL THEN MAX(pk_anamnesis+1) ELSE 1 END FROM anamnesis WHERE pfk_paciente = '" + cbTipoDocumento.getSelectedItem() + txtDocumento.getText() + "'";
                String indexANAM = SQL.unicoDato(consulta);
                String idRemOtro = "";
                if (!existeAnamnesis(anam)) {
                    anam.remove(anam.size() - 1);
                    if (!OtroAnam.equalsIgnoreCase("otro") && anam.get(anam.size() - 1).equalsIgnoreCase("SI")) {
                        //<editor-fold defaultstate="collapsed" desc="CREAR DATO BASICO">
                        String crearDatoBasico = "INSERT INTO datos_basicos(descripcion) VALUE('" + OtroAnam.toUpperCase() + "');";

                        ArrayList<String> cdb = new ArrayList<>();
                        cdb.add(crearDatoBasico);
                        try {
                            if (SQL.EnviarConsultas(cdb)) {
                                System.out.println("cargo el nuevo dato basico en reemplazo de Otro.");
                                idRemOtro = SQL.unicoDato("SELECT pk_datos_basicos FROM datos_basicos WHERE descripcion = '" + OtroAnam.toUpperCase() + "'");
                            } else {
                                System.out.println("no cargo el nuevo dato basico");
                            }
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(this, ex.getMessage());
                        } catch (ClassNotFoundException ex) {
                            JOptionPane.showMessageDialog(this, ex.getMessage());
                        }
                        //</editor-fold>
                    }

                    //<editor-fold defaultstate="collapsed" desc="INSERTAR EN LA TABLA ANAMNESIS Y ANAMNESISXPACIENTE">
                    INSERTS.add("INSERT INTO anamnesis VALUE(" + (txtMotivoConsulta.getText() != null ? "'" + txtMotivoConsulta.getText() + "'" : "NULL").toUpperCase() + "," + indexANAM + ",'" + cbTipoDocumento.getSelectedItem() + txtDocumento.getText() + "','" + datosUsuario.datos.get(0)[0] + "',NOW())");

                    String insertAnamnesis = "";
                    for (int i = 0; i < anam.size(); i++) {
                        if (i == 0) {
                            insertAnamnesis = "INSERT INTO anamnesisxpaciente VALUES('" + cbTipoDocumento.getSelectedItem() + txtDocumento.getText() + "'," + idDatosBasicos[i] + "," + indexANAM + ",'" + anam.get(i) + "'),\n";
                        } else if (i == anam.size() - 1) {
                            if (!OtroAnam.equalsIgnoreCase("otro") && anam.get(anam.size() - 1).equalsIgnoreCase("SI")) {
                                insertAnamnesis += "('" + cbTipoDocumento.getSelectedItem() + txtDocumento.getText() + "'," + idRemOtro + "," + indexANAM + ",'" + anam.get(i) + "');";
                                break;
                            }
                            insertAnamnesis = insertAnamnesis.substring(0, insertAnamnesis.length() - 2) + ";";
                        } else {
                            insertAnamnesis += "('" + cbTipoDocumento.getSelectedItem() + txtDocumento.getText() + "'," + idDatosBasicos[i] + "," + indexANAM + ",'" + anam.get(i) + "'),\n";
                        }
                    }
                    INSERTS.add(insertAnamnesis);
                    //</editor-fold>
                }
                //</editor-fold>

                //<editor-fold defaultstate="collapsed" desc="GUARDAR ANALISIS VERTICAL">
                consulta = "SELECT pfk_historia_clinica FROM hc_analisis_vertical WHERE pfk_historia_clinica = " + indices[0];
                if (!SQL.ExistenDatos(consulta) && validarAnaVert()) {
                    editarANALISIS_VERTICAL(false);
                    INSERTS.add("INSERT INTO hc_analisis_vertical VALUES(" + indices[0] + "," + slAnguloVertical.getValue() + "," + slAnguloGoniano.getValue() + "," + slLongitudRama.getValue() + ",'" + datosUsuario.datos.get(0)[0] + "',NOW(),NULL,NULL);");
                } else {
                    editarANALISIS_VERTICAL(false);
                }
                //</editor-fold>

                //<editor-fold defaultstate="collapsed" desc="GUARDAR ANALISIS TRANSVERSAL">
                consulta = "SELECT pfk_historia_clinica FROM hc_ant_tra WHERE pfk_historia_clinica = " + indices[0];
//                boolean b = validarAnalisisTransversal();//crear un metodo que devuelva un array de los campos que se pueden llenar
                if (!SQL.ExistenDatos(consulta) && validarAntTra()) {
                    editarANALISIS_TRANSVERSAL(false);
                    String dirlonLMS = "";
                    if (cbLineaMS.getSelectedIndex() == 2) {
                        dirlonLMS = Utilidades.CodificarElemento("'" + desviacionLMS + "','" + txtLMSMilimetros.getText() + "'");
                    } else {
                        dirlonLMS = "NULL,NULL";
                    }

                    String dirlonLMI = "";
                    if (cbLineaMI.getSelectedIndex() == 2) {
                        dirlonLMI = Utilidades.CodificarElemento("'" + desviacionLMI + "','" + txtLMIMilimetros.getText() + "'");
                    } else {
                        dirlonLMI = "NULL,NULL";
                    }

                    String habitos = "";
                    for (int i = 0; i < modeloHabitos.size(); i++) {
                        habitos += "#" + modeloHabitos.getElementAt(i);
                    }
                    if (habitos.isEmpty()) {
                        habitos = "NULL";
                    }

                    String intercon = "";
                    for (int i = 0; i < modeloInterconsulta.size(); i++) {
                        intercon += "#" + modeloInterconsulta.getElementAt(i);
                    }
                    if (intercon.isEmpty()) {
                        intercon = "NULL";
                    }
//                    System.out.println("INSERT INTO hc_ant_tra VALUES(" + indices[0] + ",'" + (cbLineaMS.getSelectedIndex() != 0 ? cbLineaMS.getSelectedItem().toString().charAt(0) : "NULL") + "'," + dirlonLMS + ",'" + (cbLineaMI.getSelectedIndex() != 0 ? cbLineaMI.getSelectedItem().toString().charAt(0) : "NULL") + "'," + dirlonLMI + "," + (habitos.equals("NULL") ? "NULL" : "'" + habitos.substring(1) + "'") + "," + (intercon.equals("NULL") ? "NULL" : "'" + intercon.substring(1) + "'") + ",'" + Utilidades.CodificarElemento(txtObservaciones.getText()) + "','" + Utilidades.CodificarElemento(txtExamenes.getText()) + "','" + simetria + "','" + pronostico + "');");
                    INSERTS.add("INSERT INTO hc_ant_tra VALUES(" + indices[0] + ",'" + (cbLineaMS.getSelectedIndex() != 0 ? cbLineaMS.getSelectedItem().toString().charAt(0) : "NULL") + "'," + dirlonLMS + ",'" + (cbLineaMI.getSelectedIndex() != 0 ? cbLineaMI.getSelectedItem().toString().charAt(0) : "NULL") + "'," + dirlonLMI + "," + (habitos.equals("NULL") ? "NULL" : "'" + habitos.substring(1) + "'") + "," + (intercon.equals("NULL") ? "NULL" : "'" + intercon.substring(1) + "'") + ",'" + Utilidades.CodificarElemento(txtObservaciones.getText()) + "','" + Utilidades.CodificarElemento(txtExamenes.getText()) + "','" + simetria + "','" + pronostico + "');");
                } else {
                    editarANALISIS_TRANSVERSAL(false);
                }
                //</editor-fold>

                //<editor-fold defaultstate="collapsed" desc="GUARDAR ANALISIS SAGITAL">
                consulta = "SELECT CASE WHEN pfk_swing IS NOT NULL THEN MAX(pfk_swing+1) ELSE 1 END FROM hc_analisis_sagital WHERE pfk_historia_clinica = " + indices[0];
                String indexSwing = SQL.unicoDato(consulta);
                consulta = "SELECT pfk_swing FROM hc_analisis_sagital WHERE pfk_historia_clinica = " + indices[0];
                if (!SQL.ExistenDatos(consulta) && validarAnaSag()) {
                    editarANALISIS_SAGITAL(false);
                    INSERTS.add("INSERT INTO hc_analisis_sagital VALUES(" + indices[0] + "," + indexSwing + "," + slAnguloII.getValue() + ",'" + datosUsuario.datos.get(0)[0] + "',NOW(),NULL,NULL);");

                    String con = "INSERT INTO hc_swing VALUES";
                    //<editor-fold defaultstate="collapsed" desc="getDatosSwing">
                    String ltc = "";
                    for (int i = 0; i < swing.size(); i++) {
                        for (int j = 0; j < 3; j++) {
                            if (swing.get(i)[j].isSelected()) {
                                if (i < 3) {
                                    ltc += "'D',";//lado
                                    switch (i) {//tipo
                                        case 0:
                                            ltc += "'M',";
                                            break;
                                        case 1:
                                            ltc += "'P',";
                                            break;
                                        case 2:
                                            ltc += "'C',";
                                            break;
                                        case 3:
                                            ltc += "'M',";
                                            break;
                                        case 4:
                                            ltc += "'P',";
                                            break;
                                        case 5:
                                            ltc += "'C',";
                                            break;
                                        default:
                                            break;
                                    }
                                    switch (j) {//clase
                                        case 0:
                                            ltc += "'1'";
                                            break;
                                        case 1:
                                            ltc += "'2'";
                                            break;
                                        case 2:
                                            ltc += "'3'";
                                            break;
                                        default:
                                            break;
                                    }

                                } else {
                                    ltc += "'I',";
                                    switch (i) {//tipo
                                        case 0:
                                            ltc += "'M',";
                                            break;
                                        case 1:
                                            ltc += "'P',";
                                            break;
                                        case 2:
                                            ltc += "'C',";
                                            break;
                                        case 3:
                                            ltc += "'M',";
                                            break;
                                        case 4:
                                            ltc += "'P',";
                                            break;
                                        case 5:
                                            ltc += "'C',";
                                            break;
                                        default:
                                            break;
                                    }
                                    switch (j) {//clase
                                        case 0:
                                            ltc += "'1'";
                                            break;
                                        case 1:
                                            ltc += "'2'";
                                            break;
                                        case 2:
                                            ltc += "'3'";
                                            break;
                                        default:
                                            break;
                                    }
                                }
                                break;
                            }
                        }
                        if (i != swing.size() - 1) {
                            con += "(" + indices[0] + "," + indexSwing + "," + ltc + ",'" + datosUsuario.datos.get(0)[0] + "',NOW(),NULL,NULL),";
                            ltc = "";
                        } else {
                            con += "(" + indices[0] + "," + indexSwing + "," + ltc + ",'" + datosUsuario.datos.get(0)[0] + "',NOW(),NULL,NULL);";
                            ltc = "";
                        }
                    }
                    //</editor-fold>

                    INSERTS.add(con);
                } else {
                    editarANALISIS_SAGITAL(false);
                }
                //</editor-fold>

                //<editor-fold defaultstate="collapsed" desc="GUARDAR FORMA DE ARCO">
                consulta = "SELECT pfk_historia_clinica FROM hc_forma_arco WHERE pfk_historia_clinica = " + indices[0];
                if (!SQL.ExistenDatos(consulta) && validarFormaArco()) {
                    editarFORMA_DE_ARCO(false);
                    INSERTS.add("INSERT INTO hc_forma_arco VALUES(" + indices[0] + ",'" + cbSuperior.getSelectedItem().toString().charAt(0) + "','" + cbInferior.getSelectedItem().toString().charAt(0) + "','" + txtSpeed.getText() + "','" + txtOverBite.getText() + "','" + txtOverJet.getText() + "','" + datosUsuario.datos.get(0)[0] + "',NOW(),NULL,NULL);");
                } else {
                    editarFORMA_DE_ARCO(false);
                }
                //</editor-fold>

                //<editor-fold defaultstate="collapsed" desc="GARDAR ODONTOGRAMA">
                ArrayList<String[]> od = new ArrayList<>();
                od = getDatosOdontograma();
                String indexOdontograma = "";
                String insertOdontograma = "";
                if (!existeOdontograma(od)) {
                    consulta = "SELECT CASE WHEN pfk_odontograma IS NOT NULL THEN MAX(pfk_odontograma+1) ELSE 1 END FROM hc_odontograma WHERE pfk_historia_clinica = " + indices[0];
                    indexOdontograma = SQL.unicoDato(consulta);

                    insertOdontograma = "";
                    for (int i = 0; i < od.size(); i++) {
                        String dat = od.get(i)[0] + ",'" + od.get(i)[1] + "','" + od.get(i)[2] + "','" + od.get(i)[3] + "'";
                        if (i == 0) {
                            insertOdontograma = "INSERT INTO hc_odontograma VALUES(" + indices[0] + "," + indexOdontograma + "," + dat + ",'" + datosUsuario.datos.get(0)[0] + "',NOW(),NULL,NULL),\n";
                        } else if (i == od.size() - 1) {
                            insertOdontograma += "(" + indices[0] + "," + indexOdontograma + "," + dat + ",'" + datosUsuario.datos.get(0)[0] + "',NOW(),NULL,NULL);";
                        } else {
                            insertOdontograma += "(" + indices[0] + "," + indexOdontograma + "," + dat + ",'" + datosUsuario.datos.get(0)[0] + "',NOW(),NULL,NULL),\n";
                        }
                    }
                    INSERTS.add(insertOdontograma);
                }
                //</editor-fold>

                //<editor-fold defaultstate="collapsed" desc="GUARDAR SEGUIMIENTO DEL TRATAMIENTO">
                if (tipoPaciente.equalsIgnoreCase("p")) {
                    DefaultTableModel modAux = (DefaultTableModel) tblSegTrat.getModel();
                    int indice = tblSegTrat.getRowCount() - 1;
                    if (indice != -1) {
                        INSERTS.add("CALL actualizarSegTrat('" + cbTipoDocumento.getSelectedItem() + txtDocumento.getText() + "',"
                                + "'" + tratamientosxpaciente.get(cbTratamientos.getSelectedIndex())[0] + "','',"
                                + "'" + ((String) modAux.getValueAt(indice, 2)) + "','" + datosUsuario.datos.get(0)[0] + "','H')");
                    }
                }
                //</editor-fold>
                if (pfkcita != null) {
                    INSERTS.add("CALL actualizarAuditoria('" + cbTipoDocumento.getSelectedItem() + txtDocumento.getText() + "','" + pfkcita + "',1)");
                }
                try {
                    if (INSERTS.size() > 0) {
                        if (SQL.EnviarConsultas(INSERTS)) {
                            System.out.println("se cargaron los datos");
                            JOptionPane.showMessageDialog(this, "Historia clinica guardada exitosamente.");
                            tipoPaciente = "p";
                            setEstadosBotonesDeControl(0);
                            if (!insertOdontograma.isEmpty()) {
                                if (!imagenCargadayGuardada) {
                                    odontograma.guardarImagenOdontograma(indexOdontograma, cbTipoDocumento.getSelectedItem() + txtDocumento.getText(), this);
                                }
                            }
                        } else {
                            System.out.println("no se carga ningun dato");
                            JOptionPane.showMessageDialog(this, "No fue posible guardar los datos de la historia clinica.");
                            setEstadosBotonesDeControl(2);
                            limpiarDatosPaciente(true);
                            cargarTratamientos("0");
                            cargarTablaSegTrat(null);
                        }
                    } else {
                        System.out.println("no hay nada que cargar");
                        JOptionPane.showMessageDialog(this, "No existen datos a cargar para la historia clinica.");
                        setEstadosBotonesDeControl(2);
                        cargarTratamientos("0");
                        cargarTablaSegTrat(null);
                    }
                    actualizarHC();
                } catch (SQLException ex) {
                    setEstadosBotonesDeControl(2);
                    limpiarDatosPaciente(true);
                    cargarTratamientos("0");
                    cargarTablaSegTrat(null);
                    JOptionPane.showMessageDialog(this, ex.getMessage());
                } catch (ClassNotFoundException ex) {
                    setEstadosBotonesDeControl(2);
                    cargarTratamientos("0");
                    cargarTablaSegTrat(null);
                    limpiarDatosPaciente(true);
                    JOptionPane.showMessageDialog(this, ex.getMessage());
                }
                //</editor-fold>
            }
        }
    }

    private synchronized void guardarHistoriaClinica(int a) {
        int opc = -2;
        boolean imagenCargadayGuardada = false;
        //<editor-fold defaultstate="collapsed" desc="VALLIDACIONES">
        boolean adelante = true;
        ArrayList<String> anam = new ArrayList<>();
        anam = getAnamnesis();
        String mensaje = "";
        if (txtDocumento.getText().isEmpty()) {
            mensaje += "-Es necesario que especifique el documento de identidad del paciente.\n";
            txtDocumento.requestFocusInWindow();
            adelante &= false;
        } else {
            adelante &= true;
        }

        if (cbTipoDocumento.getSelectedIndex() == 0) {
            mensaje += "-Es necesario que especifique el tipo de documento del paciente.\n";
            cbTipoDocumento.requestFocusInWindow();
            adelante &= false;
        } else {
            adelante &= true;
        }

        if (anamVacia) {
            mensaje += "-Es necesario que ingrese los datos de la anamnesis del paciente.\n";
            contenedorPaneles.setSelectedIndex(0);
            txtMotivoConsulta.requestFocusInWindow();
            adelante &= false;
        } else {
            adelante &= true;
        }
        //</editor-fold>

        if (!adelante) {
            JOptionPane.showMessageDialog(this, mensaje);
            habilitarDatosPaciente();
        } else {
            gestorMySQL SQL = new gestorMySQL();
            ArrayList<String> INSERTS = new ArrayList<>();

            //<editor-fold defaultstate="collapsed" desc="VALIDAR SI EL PACIENTE YA EXISTE">
            String consulta = "SELECT\n"
                    + "pfk_paciente\n"
                    + "FROM\n"
                    + "pacientes\n"
                    + "WHERE\n"
                    + "pfk_paciente = '" + cbTipoDocumento.getSelectedItem() + txtDocumento.getText() + "' AND estado = 'Activo'";
            boolean existePaciente = SQL.ExistenDatos(consulta);
            //</editor-fold>

            if (existePaciente) {
                //<editor-fold defaultstate="collapsed" desc="VALIDAR SI EXISTE HC PARA EL PACIENTE">
                consulta = "SELECT\n"
                        + "hc.pk_historia_clinica\n"
                        + "FROM\n"
                        + "historias_clinicas hc\n"
                        + "WHERE\n"
                        + "hc.pfk_paciente = '" + cbTipoDocumento.getSelectedItem() + txtDocumento.getText() + "'";

                if (SQL.ExistenDatos(consulta) && !tipoPaciente.equalsIgnoreCase("p")) {
                    opc = JOptionPane.showConfirmDialog(this, "El paciente identificado con "
                            + cbTipoDocumento.getSelectedItem() + " número "
                            + txtDocumento.getText() + " ya tiene una historia clinica activa.\n"
                            + "¿Desea ver la historia clinica del paciente?", null, JOptionPane.YES_NO_OPTION);

                    if (opc == JOptionPane.YES_OPTION) {
                        cargarHistoriaClinica(cbTipoDocumento.getSelectedItem() + txtDocumento.getText());
                        tipoPaciente = "p";
                    } else {
                        habilitarDatosPaciente();
                        tipoPaciente = "a";
                    }
                } else {//si no existe una historia clinica
                    if (SQL.ExistenDatos(consulta)) {
                        indices[0] = Integer.parseInt(SQL.unicoDato(consulta));
                    } else {
                        //<editor-fold defaultstate="collapsed" desc="SE CREA LA HISTORIA CLINICA PARA EL PACIENTE">
                        INSERTS.add("INSERT INTO historias_clinicas VALUES(" + indices[0] + ",'" + cbTipoDocumento.getSelectedItem() + txtDocumento.getText() + "',NULL,'" + datosUsuario.datos.get(0)[0] + "',NOW(),NULL,NULL,'" + (chkCerrarHC.isSelected() ? "Terminada" : "Activa") + "');");

                        if (chkCerrarHC.isSelected()) {
                            INSERTS.add("UPDATE\n"
                                    + "pacientextratamiento\n"
                                    + "SET\n"
                                    + "estado = '" + (chkCerrarHC.isSelected() ? "Terminada" : "Activa") + "'\n"
                                    + "WHERE\n"
                                    + "pfk_paciente = '" + cbTipoDocumento.getSelectedItem() + txtDocumento.getText() + "'\n"
                                    + "AND fk_tratamiento = " + tratamientosxpaciente.get(cbTratamientos.getSelectedIndex())[0] + ";");
                        }

                        consulta = "SELECT CASE WHEN pfk_odontograma IS NOT NULL THEN MAX(pfk_odontograma+1) ELSE 1 END FROM hc_odontograma WHERE pfk_historia_clinica = " + indices[0];
                        String indexOdontograma = SQL.unicoDato(consulta);
                        imagenCargadayGuardada = true;
                        INSERTS.add(odontograma.guardarImagenOdontograma(indexOdontograma, cbTipoDocumento.getSelectedItem() + txtDocumento.getText(), this, 0));
                        //</editor-fold>
                    }
                }
                //</editor-fold>
            } else {//si no existe el paciente
                //<editor-fold defaultstate="collapsed" desc="GUARDAR DATOS DEL PACIENTE">
                if (chkCerrarHC.isSelected()) {
                    INSERTS.add("UPDATE\n"
                            + "pacientextratamiento\n"
                            + "SET\n"
                            + "estado = 'Terminado'\n"
                            + "WHERE\n"
                            + "pfk_paciente = '" + cbTipoDocumento.getSelectedItem() + txtDocumento.getText() + "'\n"
                            + "AND fk_tratamiento = " + tratamientosxpaciente.get(cbTratamientos.getSelectedIndex())[0] + ";");
                }
                String consul = "SELECT\n"
                        + "email,\n"
                        + "telefono\n"
                        + "FROM\n"
                        + "paciente_auxiliar\n"
                        + "WHERE\n"
                        + "pk_paciente_auxiliar = " + txtIdPaciente.getText();
                ArrayList<String[]> dat = new ArrayList<>();
                String correo = "";
                dat = SQL.SELECT(consul);

                if (dat.size() > 0) {
                    String[] tels = dat.get(0)[1].split("<>");
                    correo = dat.get(0)[0];

                    for (int i = 0; i < tels.length; i++) {
                        INSERTS.add("INSERT INTO personas_telefonos VALUES('" + cbTipoDocumento.getSelectedItem() + txtDocumento.getText() + "',"
                                + "'" + (tels[i].length() > 7 ? "CELULAR" : "FIJO") + "','0','" + tels[i] + "');");
                    }
                }

                INSERTS.add("INSERT INTO historias_clinicas VALUES(" + indices[0] + ",'" + cbTipoDocumento.getSelectedItem() + txtDocumento.getText() + "',NULL,'" + datosUsuario.datos.get(0)[0] + "',NOW(),NULL,NULL,'" + (chkCerrarHC.isSelected() ? "Terminada" : "Activa") + "');");
                INSERTS.add("INSERT INTO pacientes VALUES('" + cbTipoDocumento.getSelectedItem() + txtDocumento.getText() + "','Activo','" + datosUsuario.datos.get(0)[0] + "',NOW());");
                INSERTS.add("INSERT INTO personas VALUES('" + txtDocumento.getText() + "','" + cbTipoDocumento.getSelectedItem() + "'," + Utilidades.CodificarElemento(("'" + pn + "'," + (sn.isEmpty() ? "NULL" : "'" + sn + "'") + ",'" + pa + "'," + (sa.isEmpty() ? "NULL" : "'" + sa + "'")).toUpperCase()) + ",NULL,NULL,NULL,'" + correo + "');");
                INSERTS.add("UPDATE paciente_auxiliar SET estado = 'INACTIVO' WHERE pk_paciente_auxiliar = '" + txtIdPaciente.getText() + "'");

                consulta = "SELECT pfk_paciente FROM pacientexconfirmacion WHERE pfk_paciente =  '" + txtIdPaciente.getText() + "'";
                if (SQL.ExistenDatos(consulta) && !txtEstado.getText().equals("P")) {
                    INSERTS.add("UPDATE pacientexconfirmacion SET pfk_paciente = '" + cbTipoDocumento.getSelectedItem() + txtDocumento.getText() + "', usuario = '" + datosUsuario.datos.get(0)[0] + "', fecha = NOW() WHERE pfk_paciente =  '" + txtIdPaciente.getText() + "'");
                }

                consulta = "SELECT pfk_paciente FROM pacientexcotizaciones WHERE pfk_paciente =  '" + txtIdPaciente.getText() + "'";
                if (SQL.ExistenDatos(consulta) && !txtEstado.getText().equals("P")) {
                    INSERTS.add("UPDATE "
                            + "pacientexcotizaciones "
                            + "SET "
                            + "pfk_paciente = '" + cbTipoDocumento.getSelectedItem() + txtDocumento.getText() + "' "
                            + "WHERE pfk_paciente =  '" + txtIdPaciente.getText() + "'"
                            + "AND pfk_tratamiento = " + tratamientosxpaciente.get(cbTratamientos.getSelectedIndex())[0]);
                }

                if (tratamientosxpaciente.size() > 0) {
                    consulta = "SELECT pk_consecutivo FROM pacientextratamiento WHERE pfk_paciente =  '" + cbTipoDocumento.getSelectedItem() + txtDocumento.getText() + "' AND fk_tratamiento = '" + tratamientosxpaciente.get(cbTratamientos.getSelectedIndex())[0] + "'";
                    if (!SQL.ExistenDatos(consulta)) {
                        ArrayList<String[]> pxc = new ArrayList<>();
                        pxc = obtenerDatosPacientesxCotizacion(txtIdPaciente.getText());
                        if (pxc.size() > 0) {
                            INSERTS.add("INSERT INTO pacientextratamiento VALUES ("
                                    + "'1',\n"
                                    + "'" + cbTipoDocumento.getSelectedItem() + txtDocumento.getText() + "',\n"
                                    + "" + tratamientosxpaciente.get(cbTratamientos.getSelectedIndex())[0] + ",\n"
                                    + "'" + pxc.get(0)[3] + "',\n"
                                    + "" + pxc.get(0)[4] + ",\n"
                                    + "'" + pxc.get(0)[5] + "',\n"
                                    + "'" + pxc.get(0)[6] + "',\n"
                                    + "'Activo',\n"
                                    + "NULL,\n"
                                    + "NOW())");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "El paciente no tiene tratamientos asociados, debe asignarle uno.");
                    setEstadosBotonesDeControl(4);
                    new VentanaPacienteAux(this).setVisible(true);
                    return;
                }

                consulta = "SELECT pfk_paciente FROM citas WHERE pfk_paciente =  '" + txtIdPaciente.getText() + "'";
                if (SQL.ExistenDatos(consulta) && !txtEstado.getText().equals("P")) {
                    INSERTS.add("UPDATE citas SET pfk_paciente = '" + cbTipoDocumento.getSelectedItem() + txtDocumento.getText() + "' WHERE pfk_paciente = '" + txtIdPaciente.getText() + "'");
                }

                consulta = "SELECT pfk_paciente FROM citasxhoras WHERE pfk_paciente =  '" + txtIdPaciente.getText() + "'";
                if (SQL.ExistenDatos(consulta) && !txtEstado.getText().equals("P")) {
                    INSERTS.add("UPDATE citasxhoras SET pfk_paciente = '" + cbTipoDocumento.getSelectedItem() + txtDocumento.getText() + "' WHERE pfk_paciente = '" + txtIdPaciente.getText() + "'");
                }

                consulta = "SELECT pfk_paciente FROM auditoria_agenda WHERE pfk_paciente =  '" + txtIdPaciente.getText() + "'";
                if (SQL.ExistenDatos(consulta) && !txtEstado.getText().equals("P")) {
                    INSERTS.add("UPDATE auditoria_agenda SET pfk_paciente = '" + cbTipoDocumento.getSelectedItem() + txtDocumento.getText() + "' WHERE pfk_paciente = '" + txtIdPaciente.getText() + "'");
                }
                //</editor-fold>
            }

            if (opc == -2) {
                //<editor-fold defaultstate="collapsed" desc="PROCESO DE GUARDADO">
                //<editor-fold defaultstate="collapsed" desc="GUARDAR ANAMNESIS">
                consulta = "SELECT CASE WHEN pk_anamnesis IS NOT NULL THEN MAX(pk_anamnesis+1) ELSE 1 END FROM anamnesis WHERE pfk_paciente = '" + cbTipoDocumento.getSelectedItem() + txtDocumento.getText() + "'";
                String indexANAM = SQL.unicoDato(consulta);
                String idRemOtro = "";
                if (!existeAnamnesis(anam)) {
                    anam.remove(anam.size() - 1);
                    if (!OtroAnam.equalsIgnoreCase("otro") && anam.get(anam.size() - 1).equalsIgnoreCase("SI")) {
                        //<editor-fold defaultstate="collapsed" desc="CREAR DATO BASICO">
                        String crearDatoBasico = "INSERT INTO datos_basicos(descripcion) VALUE('" + OtroAnam.toUpperCase() + "');";

                        ArrayList<String> cdb = new ArrayList<>();
                        cdb.add(crearDatoBasico);
                        try {
                            if (SQL.EnviarConsultas(cdb)) {
                                System.out.println("cargo el nuevo dato basico en reemplazo de Otro.");
                                idRemOtro = SQL.unicoDato("SELECT pk_datos_basicos FROM datos_basicos WHERE descripcion = '" + OtroAnam.toUpperCase() + "'");
                            } else {
                                System.out.println("no cargo el nuevo dato basico");
                            }
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(this, ex.getMessage());
                        } catch (ClassNotFoundException ex) {
                            JOptionPane.showMessageDialog(this, ex.getMessage());
                        }
                        //</editor-fold>
                    }

                    //<editor-fold defaultstate="collapsed" desc="INSERTAR EN LA TABLA ANAMNESIS Y ANAMNESISXPACIENTE">
                    INSERTS.add("INSERT INTO anamnesis VALUE(" + (txtMotivoConsulta.getText() != null ? "'" + txtMotivoConsulta.getText() + "'" : "NULL").toUpperCase() + "," + indexANAM + ",'" + cbTipoDocumento.getSelectedItem() + txtDocumento.getText() + "','" + datosUsuario.datos.get(0)[0] + "',NOW())");

                    String insertAnamnesis = "";
                    for (int i = 0; i < anam.size(); i++) {
                        if (i == 0) {
                            insertAnamnesis = "INSERT INTO anamnesisxpaciente VALUES('" + cbTipoDocumento.getSelectedItem() + txtDocumento.getText() + "'," + idDatosBasicos[i] + "," + indexANAM + ",'" + anam.get(i) + "'),\n";
                        } else if (i == anam.size() - 1) {
                            if (!OtroAnam.equalsIgnoreCase("otro") && anam.get(anam.size() - 1).equalsIgnoreCase("SI")) {
                                insertAnamnesis += "('" + cbTipoDocumento.getSelectedItem() + txtDocumento.getText() + "'," + idRemOtro + "," + indexANAM + ",'" + anam.get(i) + "');";
                                break;
                            }
                            insertAnamnesis = insertAnamnesis.substring(0, insertAnamnesis.length() - 2) + ";";
                        } else {
                            insertAnamnesis += "('" + cbTipoDocumento.getSelectedItem() + txtDocumento.getText() + "'," + idDatosBasicos[i] + "," + indexANAM + ",'" + anam.get(i) + "'),\n";
                        }
                    }
                    INSERTS.add(insertAnamnesis);
                    //</editor-fold>
                }
                //</editor-fold>

                //<editor-fold defaultstate="collapsed" desc="GUARDAR ANALISIS VERTICAL">
                consulta = "SELECT pfk_historia_clinica FROM hc_analisis_vertical WHERE pfk_historia_clinica = " + indices[0];
                if (!SQL.ExistenDatos(consulta)) {
                    editarANALISIS_VERTICAL(false);
                    INSERTS.add("INSERT INTO hc_analisis_vertical VALUES(" + indices[0] + "," + slAnguloVertical.getValue() + "," + slAnguloGoniano.getValue() + "," + slLongitudRama.getValue() + ",'" + datosUsuario.datos.get(0)[0] + "',NOW(),NULL,NULL);");
                } else {
                    editarANALISIS_VERTICAL(false);
                    INSERTS.add("UPDATE hc_analisis_vertical SET \n"
                            + "angulo_vertical = " + slAnguloVertical.getValue() + ",\n"
                            + "angulo_goniano = " + slAnguloGoniano.getValue() + ",\n"
                            + "longitud_de_rama = " + slLongitudRama.getValue() + ",\n"
                            + "_usuario = '" + datosUsuario.datos.get(0)[0] + "',\n"
                            + "_fecha = NOW()\n"
                            + " WHERE pfk_historia_clinica = " + indices[0]);
                }
                //</editor-fold>

                //<editor-fold defaultstate="collapsed" desc="GUARDAR ANALISIS TRANSVERSAL">
                consulta = "SELECT pfk_historia_clinica FROM hc_ant_tra WHERE pfk_historia_clinica = " + indices[0];
//                boolean b = validarAnalisisTransversal();//crear un metodo que devuelva un array de los campos que se pueden llenar
                if (validarAntTra()) {
                    if (!SQL.ExistenDatos(consulta)) {

                        editarANALISIS_TRANSVERSAL(false);
                        String dirlonLMS = "";
                        if (cbLineaMS.getSelectedIndex() == 2) {
                            dirlonLMS = Utilidades.CodificarElemento("'" + desviacionLMS + "','" + txtLMSMilimetros.getText() + "'");
                        } else {
                            dirlonLMS = "NULL,NULL";
                        }

                        String dirlonLMI = "";
                        if (cbLineaMI.getSelectedIndex() == 2) {
                            dirlonLMI = Utilidades.CodificarElemento("'" + desviacionLMI + "','" + txtLMIMilimetros.getText() + "'");
                        } else {
                            dirlonLMI = "NULL,NULL";
                        }

                        String habitos = "";
                        for (int i = 0; i < modeloHabitos.size(); i++) {
                            habitos += "#" + modeloHabitos.getElementAt(i);
                        }
                        if (habitos.isEmpty()) {
                            habitos = "NULL";
                        }

                        String intercon = "";
                        for (int i = 0; i < modeloInterconsulta.size(); i++) {
                            intercon += "#" + modeloInterconsulta.getElementAt(i);
                        }
                        if (intercon.isEmpty()) {
                            intercon = "NULL";
                        }
                        
//                        System.out.println("indice -> "+cbLineaMI.getSelectedIndex());
//                        System.out.println("cbLineaMI.getSelectedItem() -> "+cbLineaMI.getSelectedItem());
                        
                        INSERTS.add("INSERT INTO hc_ant_tra VALUES(" + indices[0] + ","
                                + "'" + (cbLineaMS.getSelectedIndex() != 0 ? cbLineaMS.getSelectedItem().toString().charAt(0) : "NULL") + "',"
                                + "" + dirlonLMS + ","
                                + "'" + (cbLineaMI.getSelectedIndex() != 0 ? cbLineaMI.getSelectedItem().toString().charAt(0) : "NULL") + "',"
                                + "" + dirlonLMI + ","
                                + "" + (habitos.equals("NULL") ? "NULL" : "'" + habitos.substring(1) + "'") + ","
                                + "" + (intercon.equals("NULL") ? "NULL" : "'" + intercon.substring(1) + "'") + ","
                                + "'" + Utilidades.CodificarElemento(txtObservaciones.getText()) + "',"
                                + "'" + Utilidades.CodificarElemento(txtExamenes.getText()) + "',"
                                + "'" + simetria + "',"
                                + "'" + Utilidades.CodificarElemento(pronostico) + "');");

                    } else {
                        editarANALISIS_TRANSVERSAL(false);
                        String dirlonLMS = "";
                        if (cbLineaMS.getSelectedIndex() == 2) {
                            dirlonLMS = "'" + desviacionLMS + "'";
                        } else {
                            dirlonLMS = "NULL";
                        }

                        String dirlonLMI = "";
                        if (cbLineaMI.getSelectedIndex() == 2) {
                            dirlonLMI = "'" + desviacionLMI + "'";
                        } else {
                            dirlonLMI = "NULL";
                        }

                        String habitos = "";
                        for (int i = 0; i < modeloHabitos.size(); i++) {
                            habitos += "#" + modeloHabitos.getElementAt(i);
                        }
                        if (habitos.isEmpty()) {
                            habitos = "NULL";
                        }

                        String intercon = "";
                        for (int i = 0; i < modeloInterconsulta.size(); i++) {
                            intercon += "#" + modeloInterconsulta.getElementAt(i);
                        }
                        if (intercon.isEmpty()) {
                            intercon = "NULL";
                        }

                        INSERTS.add("UPDATE hc_ant_tra SET\n"
                                + "linea_m_superior = '" + (cbLineaMS.getSelectedIndex() != 0 ? cbLineaMS.getSelectedItem().toString().charAt(0) : "NULL") + "',\n"
                                + "direccion_m_superior = " + dirlonLMS + ",\n"
                                + "longitud_m_superior = " + (txtLMSMilimetros.getText().isEmpty() ? "NULL" : "'" + txtLMSMilimetros.getText() + "'") + ",\n"
                                + "linea_m_inferior = '" + (cbLineaMI.getSelectedIndex() != 0 ? cbLineaMI.getSelectedItem().toString().charAt(0) : "NULL") + "',\n"
                                + "direccion_m_inferior = " + dirlonLMI + ",\n"
                                + "longitud_m_inferior = " + (txtLMIMilimetros.getText().isEmpty() ? "NULL" : "'" + txtLMIMilimetros.getText() + "'") + ",\n"
                                + "hab = " + (habitos.equals("NULL") ? "NULL" : "'" + habitos.substring(1) + "'") + ",\n"
                                + "interconsulta = " + (intercon.equals("NULL") ? "NULL" : "'" + intercon.substring(1) + "'") + ",\n"
                                + "observacion = '" + Utilidades.CodificarElemento(txtObservaciones.getText()) + "',\n"
                                + "examenes = '" + Utilidades.CodificarElemento(txtExamenes.getText()) + "',\n"
                                + "simetria = '" + simetria + "',\n"
                                + "pronostico = '" + Utilidades.CodificarElemento(pronostico) + "'\n"
                                + "WHERE pfk_historia_clinica = " + indices[0]);
                    }
                }
                //</editor-fold>

                //<editor-fold defaultstate="collapsed" desc="GUARDAR ANALISIS SAGITAL">
                consulta = "SELECT CASE WHEN pfk_swing IS NOT NULL THEN pfk_swing ELSE 1 END FROM hc_analisis_sagital WHERE pfk_historia_clinica = " + indices[0];

                String indexSwing = "";
                if (!SQL.ExistenDatos(consulta)) {
                    indexSwing = "1";
                } else {
                    indexSwing = SQL.unicoDato(consulta);
                }

//                consulta = "SELECT pfk_swing FROM hc_analisis_sagital WHERE pfk_historia_clinica = " + indices[0];
//                if (!SQL.ExistenDatos(consulta)) {
                editarANALISIS_SAGITAL(false);
                INSERTS.add("INSERT INTO hc_analisis_sagital VALUES(" + indices[0] + "," + indexSwing + "," + slAnguloII.getValue() + ",'" + datosUsuario.datos.get(0)[0] + "',NOW(),NULL,NULL)"
                        + " ON DUPLICATE KEY UPDATE "
                        + "angulo_incisivo_inferior = " + slAnguloII.getValue() + ","
                        + "_usuario = '" + datosUsuario.datos.get(0)[0] + "',"
                        + "_fecha = NOW();");

                if (indexSwing != null /*&& hayParalelismoMaxilar()*/) {

                    //<editor-fold defaultstate="collapsed" desc="getDatosSwing">
                    String lado = "";
                    String tipo = "";
                    String clase = "";
                    for (int i = 0; i < swing.size(); i++) {
                        for (int j = 0; j < 3; j++) {
                            if (swing.get(i)[j].isSelected()) {
                                if (i < 3) {
                                    lado = "'D'";//lado
                                    switch (i) {//tipo
                                        case 0:
                                            tipo = "'M'";
                                            break;
                                        case 1:
                                            tipo = "'P'";
                                            break;
                                        case 2:
                                            tipo = "'C'";
                                            break;
                                        case 3:
                                            tipo = "'M'";
                                            break;
                                        case 4:
                                            tipo = "'P'";
                                            break;
                                        case 5:
                                            tipo = "'C'";
                                            break;
                                        default:
                                            break;
                                    }
                                    switch (j) {//clase
                                        case 0:
                                            clase = "'1'";
                                            break;
                                        case 1:
                                            clase = "'2'";
                                            break;
                                        case 2:
                                            clase = "'3'";
                                            break;
                                        default:
                                            break;
                                    }

                                } else {
                                    lado = "'I'";
                                    switch (i) {//tipo
                                        case 0:
                                            tipo = "'M'";
                                            break;
                                        case 1:
                                            tipo = "'P'";
                                            break;
                                        case 2:
                                            tipo = "'C'";
                                            break;
                                        case 3:
                                            tipo = "'M'";
                                            break;
                                        case 4:
                                            tipo = "'P'";
                                            break;
                                        case 5:
                                            tipo = "'C'";
                                            break;
                                        default:
                                            break;
                                    }
                                    switch (j) {//clase
                                        case 0:
                                            clase = "'1'";
                                            break;
                                        case 1:
                                            clase = "'2'";
                                            break;
                                        case 2:
                                            clase = "'3'";
                                            break;
                                        default:
                                            break;
                                    }
                                }
                                break;
                            }
                        }
//                            if (i != swing.size() - 1) {
                        if (!(lado.isEmpty() && tipo.isEmpty() && clase.isEmpty())) {
                            String con
                                    = "CALL actualizarSWING(" + indices[0] + "," + indexSwing + "," + lado + "," + tipo + "," + clase + ",'" + datosUsuario.datos.get(0)[0] + "');";
//                                    "INSERT INTO hc_swing VALUES (" + indices[0] + "," + indexSwing + "," + lado + "," + tipo + "," + clase + ",'" + datosUsuario.datos.get(0)[0] + "',NOW(),NULL,NULL)"
//                                    + " ON DUPLICATE KEY UPDATE "
//                                    + "lado = " + lado + ","
//                                    + "tipo_diente = " + tipo + ","
//                                    + "clase = " + clase + ","
//                                    + "_usuario = '" + datosUsuario.datos.get(0)[0] + "',"
//                                    + "_fecha = NOW();";
                            INSERTS.add(con);
                        }
                        lado = "";
                        tipo = "";
                        clase = "";
                    }
                    //</editor-fold>
                }
                //</editor-fold>

                //<editor-fold defaultstate="collapsed" desc="GUARDAR FORMA DE ARCO">
                consulta = "SELECT pfk_historia_clinica FROM hc_forma_arco WHERE pfk_historia_clinica = " + indices[0];
                if (!SQL.ExistenDatos(consulta)) {
                    if (validarFormaArco()) {
                        editarFORMA_DE_ARCO(false);
                        INSERTS.add("INSERT INTO hc_forma_arco VALUES(" + indices[0] + ","
                                + "'" + cbSuperior.getSelectedItem().toString().charAt(0) + "',"
                                + "'" + cbInferior.getSelectedItem().toString().charAt(0) + "',"
                                + "'" + txtSpeed.getText() + "','" + txtOverBite.getText() + "',"
                                + "'" + txtOverJet.getText() + "',"
                                + "'" + datosUsuario.datos.get(0)[0] + "',"
                                + "NOW(),NULL,NULL);");
                    }
                } else {
                    if (validarFormaArco()) {
                        editarFORMA_DE_ARCO(false);
                        INSERTS.add("UPDATE hc_forma_arco SET\n"
                                + "superior = '" + cbSuperior.getSelectedItem().toString().charAt(0) + "',\n"
                                + "inferior = '" + cbInferior.getSelectedItem().toString().charAt(0) + "',\n"
                                + "curva = '" + txtSpeed.getText() + "',\n"
                                + "overbite = '" + txtOverBite.getText() + "',\n"
                                + "overjet = '" + txtOverJet.getText() + "',\n"
                                + "_usuario = '" + datosUsuario.datos.get(0)[0] + "',"
                                + "_fecha = NOW()\n"
                                + "WHERE pfk_historia_clinica = " + indices[0]);
                    }
                }
                //</editor-fold>

                //<editor-fold defaultstate="collapsed" desc="GARDAR ODONTOGRAMA">
                ArrayList<String[]> od = new ArrayList<>();
                od = getDatosOdontograma();
                String indexOdontograma = "";
                String insertOdontograma = "";
                if (!existeOdontograma(od)) {
                    consulta = "SELECT CASE WHEN pfk_odontograma IS NOT NULL THEN MAX(pfk_odontograma+1) ELSE 1 END FROM hc_odontograma WHERE pfk_historia_clinica = " + indices[0];
                    indexOdontograma = SQL.unicoDato(consulta);

                    insertOdontograma = "";
                    for (int i = 0; i < od.size(); i++) {
                        String dat = od.get(i)[0] + ",'" + od.get(i)[1] + "','" + od.get(i)[2] + "','" + od.get(i)[3] + "'";
                        if (i == 0) {
                            insertOdontograma = "INSERT INTO hc_odontograma VALUES(" + indices[0] + "," + indexOdontograma + "," + dat + ",'" + datosUsuario.datos.get(0)[0] + "',NOW(),NULL,NULL),\n";
                        } else if (i == od.size() - 1) {
                            insertOdontograma += "(" + indices[0] + "," + indexOdontograma + "," + dat + ",'" + datosUsuario.datos.get(0)[0] + "',NOW(),NULL,NULL);";
                        } else {
                            insertOdontograma += "(" + indices[0] + "," + indexOdontograma + "," + dat + ",'" + datosUsuario.datos.get(0)[0] + "',NOW(),NULL,NULL),\n";
                        }
                    }
                    INSERTS.add(insertOdontograma);
                }

                if (!insertOdontograma.isEmpty()) {
                    if (!imagenCargadayGuardada) {
                        INSERTS.add(odontograma.guardarImagenOdontograma(indexOdontograma, cbTipoDocumento.getSelectedItem() + txtDocumento.getText(), this, 0));
                    }
                }
                //</editor-fold>

                //<editor-fold defaultstate="collapsed" desc="GUARDAR SEGUIMIENTO DEL TRATAMIENTO">
                if (tipoPaciente.equalsIgnoreCase("p")) {
                    DefaultTableModel modAux = (DefaultTableModel) tblSegTrat.getModel();
                    int indice = tblSegTrat.getRowCount() - 1;
                    
                    if (indice != -1) {
//                        if (((String) modAux.getValueAt(indice, 1)) == null) {
                        String cons = ((String) modAux.getValueAt(indice, 0));
                        if(cons == null){
                            cons = "";
                        }
                        System.out.println("valor tabla>"+cons+"<");
                        if (!((String) modAux.getValueAt(indice, 2)).isEmpty() && cons.isEmpty()) {
                            INSERTS.add("CALL actualizarSegTrat('" + cbTipoDocumento.getSelectedItem() + txtDocumento.getText() + "',"
                                    + "'" + tratamientosxpaciente.get(cbTratamientos.getSelectedIndex())[0] + "','',"
                                    + "'" + ((String) modAux.getValueAt(indice, 2)) + "','" + datosUsuario.datos.get(0)[0] + "','H','000000000')");
                        }
                    }
                } else {
                    DefaultTableModel modAux = (DefaultTableModel) tblSegTrat.getModel();
                    int indice = tblSegTrat.getRowCount() - 1;
                    if (indice != -1) {
                        String cons = ((String) modAux.getValueAt(indice, 0));
                        if(cons == null){
                            cons = "";
                        }
                        System.out.println("valor tabla>"+cons+"<");
                        if (!((String) modAux.getValueAt(indice, 2)).isEmpty() && cons.isEmpty()) {
                            INSERTS.add("CALL actualizarSegTrat('" + cbTipoDocumento.getSelectedItem() + txtDocumento.getText() + "',"
                                    + "'" + tratamientosxpaciente.get(cbTratamientos.getSelectedIndex())[0] + "','',"
                                    + "'" + ((String) modAux.getValueAt(indice, 2)) + "','" + datosUsuario.datos.get(0)[0] + "','H','000000000')");
                        }
                    }
                }
                //</editor-fold>

                //<editor-fold defaultstate="collapsed" desc="GUARDAR REMISIONES">
                if (cbformulacionyremisiones.getSelectedIndex() == 2 && !txtformulacionyremisiones.getText().isEmpty()) {
                    consulta = "SELECT DISTINCT pfk_paciente FROM remisiones WHERE pfk_paciente = '" + cbTipoDocumento.getSelectedItem() + txtDocumento.getText() + "'";
                    String consecutivo = "1";
                    if (SQL.ExistenDatos(consulta)) {
                        consecutivo = obtenerConsecutivo(cbTipoDocumento.getSelectedItem() + txtDocumento.getText());
                    }
                    INSERTS.add("INSERT INTO remisiones VALUES("
                            + "'" + cbTipoDocumento.getSelectedItem() + txtDocumento.getText() + "',"
                            + "" + consecutivo + ","
                            + "'" + Utilidades.CodificarElemento(txtformulacionyremisiones.getText().trim()) + "')");
                }
                //</editor-fold>

                //<editor-fold defaultstate="collapsed" desc="GUARDAAR CIERRE HC">
                consulta = "SELECT\n"
                        + "hc.pk_historia_clinica\n"
                        + "FROM\n"
                        + "historias_clinicas hc\n"
                        + "WHERE\n"
                        + "hc.pfk_paciente = '" + cbTipoDocumento.getSelectedItem() + txtDocumento.getText() + "'";

                if (SQL.ExistenDatos(consulta)) {
                    INSERTS.add("UPDATE\n"
                            + "historias_clinicas\n"
                            + "SET\n"
                            + "estado = '" + (chkCerrarHC.isSelected() ? "Terminada" : "Activa") + "',\n"
                            + "_usuario = '" + datosUsuario.datos.get(0)[0] + "',\n"
                            + "_fecha = NOW()\n"
                            + "WHERE\n"
                            + "pfk_paciente = '" + cbTipoDocumento.getSelectedItem() + txtDocumento.getText() + "'");
                }
                //</editor-fold>

                if (pfkcita != null) {
                    INSERTS.add("CALL actualizarAuditoria('" + cbTipoDocumento.getSelectedItem() + txtDocumento.getText() + "','" + pfkcita + "',1,'" + datosUsuario.datos.get(0)[0] + "')");
                } else {
                    pfkcita = "";
                }

                //<editor-fold defaultstate="collapsed" desc="GUARDAAR TABLA DE AUDITORIA">
                INSERTS.add("INSERT INTO auditoria_cambio_estado VALUES("
                        + "'" + cbTipoDocumento.getSelectedItem() + txtDocumento.getText() + "',"
                        + "'" + datosUsuario.datos.get(0)[0] + "',"
                        + "'" + (pfkcita.isEmpty() ? "ICONO" : "AGENDA_DIARIA") + "','HISTORIA CLINICA',NOW())");
                //</editor-fold>

                try {
                    if (INSERTS.size() > 0) {
                        if (SQL.EnviarConsultas(INSERTS)) {
                            System.out.println("se cargaron los datos");
                            JOptionPane.showMessageDialog(this, "Historia clinica guardada exitosamente.");
                            tipoPaciente = "p";
                            setEstadosBotonesDeControl(0);
                            reiniciarHistoriaClinica();
                        } else {
                            System.out.println("no se carga ningun dato");
                            JOptionPane.showMessageDialog(this, "No fue posible guardar los datos de la historia clinica.");
                        }
                    } else {
                        System.out.println("no hay nada que cargar");
                        JOptionPane.showMessageDialog(this, "No existen datos a cargar para la historia clinica.");
                        setEstadosBotonesDeControl(2);
                        cargarTratamientos("0");
                        cargarTablaSegTrat(null);
                    }
                    actualizarHC();
                } catch (SQLException ex) {
                    setEstadosBotonesDeControl(2);
                    limpiarDatosPaciente(true);
                    cargarTratamientos("0");
                    cargarTablaSegTrat(null);
                    JOptionPane.showMessageDialog(this, ex.getMessage());
                } catch (ClassNotFoundException ex) {
                    setEstadosBotonesDeControl(2);
                    cargarTratamientos("0");
                    cargarTablaSegTrat(null);
                    limpiarDatosPaciente(true);
                    JOptionPane.showMessageDialog(this, ex.getMessage());
                }
                //</editor-fold>
            }
        }
    }

    public void cargarIndices() {
        String consulta = "SELECT MAX(pk_historia_clinica)+1 FROM historias_clinicas";

        gestorMySQL SQL = new gestorMySQL();
        consulta = SQL.unicoDato(consulta);
        if (consulta != null) {
            indices[0] = Integer.parseInt(consulta);
        }
    }

    private void editarANALISIS_SAGITAL(boolean editar) {
        slAnguloII.setEnabled(editar);

        molderclasei.setEnabled(editar);
        molderclaseii.setEnabled(editar);
        molderclaseiii.setEnabled(editar);

        premolderclasei.setEnabled(editar);
        premolderclaseii.setEnabled(editar);
        premolderclaseiii.setEnabled(editar);

        canderclasei.setEnabled(editar);
        canderclaseii.setEnabled(editar);
        canderclaseiii.setEnabled(editar);

        molizqclasei.setEnabled(editar);
        molizqclaseii.setEnabled(editar);
        molizqclaseiii.setEnabled(editar);

        premolizqclasei.setEnabled(editar);
        premolizqclaseii.setEnabled(editar);
        premolizqclaseiii.setEnabled(editar);

        canizqclasei.setEnabled(editar);
        canizqclaseii.setEnabled(editar);
        canizqclaseiii.setEnabled(editar);
    }

    private void editarANALISIS_VERTICAL(boolean editar) {
        slAnguloVertical.setEnabled(editar);
        slAnguloGoniano.setEnabled(editar);
        slLongitudRama.setEnabled(editar);
    }

    private void editarANALISIS_TRANSVERSAL(boolean editar) {
        cbLineaMS.setEnabled(editar);
        //<editor-fold defaultstate="collapsed" desc="DESVIACION LMS">
        btnLMSDesviacionD.setEnabled(editar);
        btnLMSDesviacionI.setEnabled(editar);
        //</editor-fold>
        txtLMSMilimetros.setEnabled(editar);
        cbLineaMI.setEnabled(editar);

        //<editor-fold defaultstate="collapsed" desc="DESVIACION LMI">
        btnLMIDesviacionD.setEnabled(editar);
        btnLMIDesviacionI.setEnabled(editar);
        //</editor-fold>
        txtLMIMilimetros.setEnabled(editar);

        txtHabitos.setEnabled(editar);
        //<editor-fold defaultstate="collapsed" desc="BLOQUEAR BOTONES DE DESPLAZAMIENTO">
        habnext.setEnabled(editar);
        internext.setEnabled(editar);
        //</editor-fold>
        listPreHabitos.setEnabled(editar);

        txtInterconsulta.setEnabled(editar);
        listPreInterconsulta.setEnabled(editar);
        txtObservaciones.setEnabled(editar);
        txtExamenes.setEnabled(editar);

        //<editor-fold defaultstate="collapsed" desc="SIMETRIA">
        btnSimetriaSi.setEnabled(editar);
        btnSimetriaNo.setEnabled(editar);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="PRONOSTICO">
        btnPronosticoB.setEnabled(editar);
        btnPronosticoR.setEnabled(editar);
        //</editor-fold>
    }

    private void editarFORMA_DE_ARCO(boolean editar) {
        cbSuperior.setEnabled(editar);
        cbInferior.setEnabled(editar);
        txtSpeed.setEnabled(editar);
        txtOverBite.setEnabled(editar);
        txtOverJet.setEnabled(editar);
    }

    private void editarANAMNESIS(boolean editar) {
        tblAnamnesis.setEnabled(editar);
        txtMotivoConsulta.setEnabled(editar);
    }

    private void selectBotonInteruptor(JLabel btn1, JLabel btn2, boolean b) {
        btn1.setBackground(new Color(33, 97, 140));
        btn1.setForeground(new Color(255, 255, 255));
        btn1.setOpaque(b);
        btn2.setBackground(new Color(214, 234, 248));
        btn2.setForeground(new Color(33, 97, 140));
        btn2.setOpaque(!b);
    }

    public void editarHistoriaClinica(boolean b) {
        System.out.println("--->" + tipoPaciente);
        if (tipoPaciente.equalsIgnoreCase("p")) {
            editarANALISIS_SAGITAL(b);
            editarANALISIS_VERTICAL(b);
            editarANALISIS_TRANSVERSAL(b);
            editarFORMA_DE_ARCO(b);
            editarANAMNESIS(b);
            editarSeguimientoDelTratamiento(b);
            editarOdontograma(b);
            editarSeguimientoDeImgenes(b);
            editarFormulacionYRemisiones(b);
        } else {

            editarANALISIS_SAGITAL(b);
            editarANALISIS_VERTICAL(b);
            editarANALISIS_TRANSVERSAL(b);
            editarFORMA_DE_ARCO(b);
            editarANAMNESIS(b);
            editarSeguimientoDelTratamiento(b);
            editarOdontograma(b);
            b = !b;
            editarSeguimientoDeImgenes(b);
            editarFormulacionYRemisiones(b);

//            b = !b;
//            editarANALISIS_SAGITAL(b);
//            editarANALISIS_VERTICAL(b);
//            editarANALISIS_TRANSVERSAL(b);
//            editarFORMA_DE_ARCO(b);
//            editarANAMNESIS(b);
//            editarSeguimientoDelTratamiento(b);
//            editarOdontograma(b);
//            editarSeguimientoDeImgenes(!b);
//            editarFormulacionYRemisiones(!b);
        }
    }

    private boolean validarAntTra() {
        return (cbLineaMS.getSelectedIndex() > 0 && cbLineaMI.getSelectedIndex() > 0);
    }

    private boolean validarAnaVert() {
        return slAnguloVertical.getValue() != 0
                && slAnguloGoniano.getValue() != 0
                && slLongitudRama.getValue() != 0;
    }

    private boolean validarAnaSag() {
        return slAnguloII.getValue() != 50;//verificar
    }

    private boolean validarFormaArco() {
        return cbSuperior.getSelectedIndex() != 0
                && cbInferior.getSelectedIndex() != 0
                && !txtOverJet.getText().isEmpty()
                && !txtOverBite.getText().isEmpty()
                && !txtSpeed.getText().isEmpty();
    }

    /**
     * se utiliza para las interconsultas y demas listas seleccionables
     *
     * @param modelo
     * @param valor
     * @return
     */
    private boolean estaSeleccionado(DefaultListModel modelo, String valor) {
        for (int i = 0; i < modelo.size(); i++) {
            if (modelo.getElementAt(i).equals(valor)) {
                return true;
            }
        }
        return false;
    }

    public void habilitarDatosPaciente() {
        if (txtDocumento.getText().isEmpty() && cbTipoDocumento.getSelectedIndex() == 0) {
            txtDocumento.setEnabled(true);
            cbTipoDocumento.setEnabled(true);
        } else {
            txtDocumento.setEnabled(false);
            cbTipoDocumento.setEnabled(false);
        }
        txtNombres.setEnabled(false);
        txtApellidos.setEnabled(false);
    }

    public void setEstadosBotonesDeControl(int estado) {
        switch (estado) {
            case 0://press boton guardar
                btnGuardar.setEnabled(false);
                btnModificar.setEnabled(false);
                btnDescartar.setEnabled(false);
                btnConsultar.setEnabled(true);
                break;
            case 1://press boton editar
                btnGuardar.setEnabled(true);
                btnModificar.setEnabled(false);
                if (pfkcita != null) {
                    if (pfkcita.isEmpty()) {
                        btnDescartar.setEnabled(false);
                    }
                } else {
                    btnDescartar.setEnabled(true);
                }

                btnConsultar.setEnabled(false);
                break;
            case 2://press boton descartar
                btnGuardar.setEnabled(false);
                btnModificar.setEnabled(false);
                btnDescartar.setEnabled(false);
                btnConsultar.setEnabled(true);
                break;
            case 3://press boton consultar
                btnGuardar.setEnabled(false);
                btnModificar.setEnabled(false);
                btnDescartar.setEnabled(false);
                btnConsultar.setEnabled(false);
                break;
            case 4://press boton consultar
                btnGuardar.setEnabled(true);
                btnModificar.setEnabled(false);
                btnDescartar.setEnabled(false);
                btnConsultar.setEnabled(true);
                break;
            case 5://ocurre cuando se selecciona un paciente
                btnGuardar.setEnabled(false);
                btnModificar.setEnabled(true);
                btnDescartar.setEnabled(true);
                btnConsultar.setEnabled(false);
                break;
            case 10://ocurre cuando se selecciona un paciente
                btnGuardar.setEnabled(false);
                btnModificar.setEnabled(true);
                btnDescartar.setEnabled(false);
                btnConsultar.setEnabled(false);
                break;
            default:
                break;
        }
    }

    public void habilitarTodosLosDatos(boolean b) {
        editarHistoriaClinica(b);
        habilitarDatosPaciente();
    }

    private boolean existeAnamnesis(ArrayList<String> anam) {
        gestorMySQL SQL = new gestorMySQL();
        String consulta = "SELECT\n"
                + "estado ESTADO,\n"
                + "(SELECT motivo_consulta FROM anamnesis WHERE pfk_paciente = axp.pfk_paciente AND pk_anamnesis = axp.pfk_anamnesis)\n"
                + "FROM\n"
                + "anamnesisxpaciente axp\n"
                + "WHERE \n"
                + "axp.pfk_paciente = '" + cbTipoDocumento.getSelectedItem() + txtDocumento.getText() + "'\n"
                + "AND axp.pfk_anamnesis = (SELECT MAX(pfk_anamnesis) FROM anamnesisxpaciente WHERE pfk_paciente = '" + cbTipoDocumento.getSelectedItem() + txtDocumento.getText() + "')\n"
                + "ORDER BY\n"
                + "axp.pfk_anamnesis, axp.pfk_datos_basicos ASC";
        ArrayList<String[]> dbanam = new ArrayList<>();
        dbanam = SQL.SELECT(consulta);

        if (dbanam.size() > 0) {
            for (int i = 0; i < dbanam.size(); i++) {
                if (!dbanam.get(i)[0].equalsIgnoreCase(anam.get(i))) {
                    return false;
                }
            }
            if (!OtroAnam.equalsIgnoreCase("otro") && !OtroAnam.trim().isEmpty()) {
                return false;
            }
            if (!dbanam.get(0)[1].equalsIgnoreCase(anam.get(anam.size() - 1))) {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    private void eliminarFilaSegTrat(int fila) {
        tModelo.removeRow(fila);
    }

    private boolean existeOdontograma(ArrayList<String[]> od) {
        //se ordena el array del odontograma
        Collections.sort(od, new Comparator<String[]>() {
            @Override
            public int compare(String[] o1, String[] o2) {
                if (Integer.parseInt(o1[0]) > Integer.parseInt(o2[0])) {
                    return 1;
                } else if (Integer.parseInt(o1[0]) < Integer.parseInt(o2[0])) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
        gestorMySQL SQL = new gestorMySQL();
        String consulta = "SELECT\n"
                + "hcod.`pfk_diente` diente,\n"
                + "hcod.`posicion` seccion,\n"
                + "hcod.`estado` estado\n"
                + "FROM \n"
                + "hc_odontograma hcod\n"
                + "WHERE \n"
                + "hcod.`pfk_historia_clinica` = (SELECT MAX(hc.`pk_historia_clinica`) FROM historias_clinicas hc WHERE hc.`pfk_paciente` = '" + cbTipoDocumento.getSelectedItem() + txtDocumento.getText() + "')\n"
                + "AND hcod.`pfk_odontograma` = (SELECT MAX(odon.`pfk_odontograma`) FROM hc_odontograma odon WHERE odon.`pfk_historia_clinica` = hcod.`pfk_historia_clinica`)";
        ArrayList<String[]> dbOdontograma = new ArrayList<>();
        dbOdontograma = SQL.SELECT(consulta);

        if (dbOdontograma.size() > 0) {
            for (int i = 0; i < dbOdontograma.size(); i++) {
                for (int j = 0; j < dbOdontograma.get(i).length; j++) {
                    if (!dbOdontograma.get(i)[j].equalsIgnoreCase(od.get(i)[j])) {
                        return false;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public void cargarImagenes(String tid) {
        String consulta = "SELECT\n"
                + "pfk_tipo_imagen,\n"
                + "CONCAT(url_imagen,'/',nombre)\n"
                + "FROM\n"
                + "hc_imagenes hcim,\n"
                + "historias_clinicas hc\n"
                + "WHERE\n"
                + "hcim.pfk_historia_clinica = hc.pk_historia_clinica\n"
                + "AND hc.pfk_paciente = '" + tid + "'\n"
                + "ORDER BY\n"
                + "pfk_tipo_imagen, consecutivo DESC";

        gestorMySQL SQL = new gestorMySQL();
        ArrayList<String[]> imgs = new ArrayList<>();
        imagenes.reiniciar();
        imgs = SQL.SELECT(consulta);

        int tiposDeImagenes = 0;
        if (imgs.size() > 0) {
            for (int i = 0; i < imgs.size(); i++) {
                imagenes.img.add(imgs.get(i)[1]);//se agrega la url absoluta de la imagen
                imagenes.tipo.add(imgs.get(i)[0]);//se agrega el tipo de imagen
            }
            while (!(imagenes.tipo.contains("0") && imagenes.tipo.contains("1") && imagenes.tipo.contains("2") && imagenes.tipo.contains("3") && imagenes.tipo.contains("4"))) {
                int i = 0, j = 0;
                if (!imagenes.tipo.contains("0")) {
                    i = 0;
                    j = 0;
                } else if (!imagenes.tipo.contains("1")) {
                    if (imagenes.tipo.contains("0")) {
                        i = imagenes.tipo.lastIndexOf("0") + 1;
                    } else if (imagenes.tipo.contains("2")) {
                        i = imagenes.tipo.indexOf("2");
                    } else if (imagenes.tipo.contains("3")) {
                        i = imagenes.tipo.indexOf("3");
                    } else if (imagenes.tipo.contains("4")) {
                        i = imagenes.tipo.indexOf("4");
                    }
                    j = 1;
                } else if (!imagenes.tipo.contains("2")) {
                    if (imagenes.tipo.contains("1")) {
                        i = imagenes.tipo.lastIndexOf("1") + 1;
                    } else if (imagenes.tipo.contains("3")) {
                        i = imagenes.tipo.indexOf("3");
                    } else if (imagenes.tipo.contains("4")) {
                        i = imagenes.tipo.indexOf("4");
                    } else if (imagenes.tipo.contains("0")) {
//                        i = imagenes.tipo.lastIndexOf("0") + 1;
                        i = imagenes.tipo.indexOf("0");
                    }
                    j = 2;
                } else if (!imagenes.tipo.contains("3")) {
                    if (imagenes.tipo.contains("2")) {
                        i = imagenes.tipo.lastIndexOf("2") + 1;
                    } else if (imagenes.tipo.contains("3")) {
                        i = imagenes.tipo.indexOf("3");
                    } else if (imagenes.tipo.contains("1")) {
                        i = imagenes.tipo.indexOf("1");
                    } else if (imagenes.tipo.contains("0")) {
                        i = imagenes.tipo.indexOf("0");
                    }
                    j = 3;
                } else {
                    i = imagenes.tipo.size();
                    j = 4;
                }

                imagenes.img.add(i, "nada");//se agrega la url absoluta de la imagen
                imagenes.tipo.add(i, "" + j);//se agrega el tipo de imagen
            }

            imagenes.tipimg[Integer.parseInt(imagenes.tipo.get(0))].sel = true;
        } else {
            imagenes.reiniciar();
        }
        imagenes.actualizar();
    }

    public void limpiarHC(boolean b) {
        if (b) {
            limpiarComponentesHC();
            limpiarDatosPaciente(b);
        }
    }

    private void limpiarANALISIS_SAGITAL(boolean limpiar) {
        if (limpiar) {
            slAnguloII.setValue(0);
            limpiarRB();
        }
    }

    private void limpiarANALISIS_VERTICAL(boolean limpiar) {
        if (limpiar) {
            slAnguloVertical.setValue(0);
            slAnguloGoniano.setValue(0);
            slLongitudRama.setValue(0);
        }
    }

    private void limpiarANALISIS_TRANSVERSAL(boolean limpiar) {
        if (limpiar) {
            limpiar = !limpiar;
            cbLineaMS.setSelectedIndex(0);
            //<editor-fold defaultstate="collapsed" desc="DESVIACION LMS">
            selectBotonInteruptor(btnLMSDesviacionD, btnLMSDesviacionI, true);
            desviacionLMS = "D";
            //</editor-fold>
            txtLMSMilimetros.setText("");
            cbLineaMI.setSelectedIndex(0);

            //<editor-fold defaultstate="collapsed" desc="DESVIACION LMI">
            selectBotonInteruptor(btnLMIDesviacionD, btnLMIDesviacionI, true);
            desviacionLMI = "D";
            //</editor-fold>
            txtLMIMilimetros.setText("");

            modeloHabitos.removeAllElements();
            txtHabitos.setModel(modeloHabitos);
            listPreHabitos.clearSelection();

            modeloInterconsulta.removeAllElements();
            txtInterconsulta.setModel(modeloInterconsulta);
            listPreInterconsulta.clearSelection();
            txtObservaciones.setText("");
            txtExamenes.setText("");

            //<editor-fold defaultstate="collapsed" desc="SIMETRIA">
            selectBotonInteruptor(btnSimetriaSi, btnSimetriaNo, true);
            simetria = "SI";
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="PRONOSTICO">
            selectBotonInteruptor(btnPronosticoB, btnPronosticoR, true);
            pronostico = "BUENO";
            //</editor-fold>
        }
    }

    private void limpiarFORMA_DE_ARCO(boolean limpiar) {
        if (limpiar) {
            limpiar = !limpiar;
            cbSuperior.setSelectedIndex(0);
            cbInferior.setSelectedIndex(0);
            txtSpeed.setText("");
            txtOverBite.setText("");
            txtOverJet.setText("");
        }
    }

    private void limpiarDatosPaciente(boolean limpiar) {
        if (limpiar) {
            cbTipoDocumento.setSelectedIndex(0);
            txtDocumento.setText("");
            txtNombres.setText("");
            txtApellidos.setText("");
        }
    }

    public void cargarHistoriaClinica(String tipydoc) {
        cargarDatosPaciente(tipydoc);
    }

    private void actualizarHC() {
        odontograma.limpiar();
        limpiarComponentesHC();
        cargarHistoriaClinica(cbTipoDocumento.getSelectedItem() + txtDocumento.getText());
    }

    boolean existeHC(String tid) {
        gestorMySQL SQL = new gestorMySQL();
        String consulta = "SELECT pk_historia_clinica FROM historias_clinicas WHERE pfk_paciente = '" + tid + "'";
        return SQL.ExistenDatos(consulta);
    }

    public void reiniciarHistoriaClinica() {
        imagenes.reiniciar();
        setEstadosBotonesDeControl(2);
        cargarDatosPaciente("1");
        habilitarTodosLosDatos(false);
        limpiarHC(true);
        cargarTablaANAMNESIS(null);
        tratamientosxpaciente = new ArrayList<>();
//        odontograma.limpiar();
    }

    /**
     * Este metodo carga los datos del seguimiento del tramtamiento teniendo en
     * cuenta el tratamiento pasado por parametro y el paciente.
     *
     * @param tipydoc = concatenacion del tipo de documento y el documento de la
     * persona.
     * @param tratamiento = codigo del tratamiento.
     */
    public void cargarSeguimientoDelTratamiento(String tipydoc, String tratamiento) {
        gestorMySQL sql = new gestorMySQL();
        String consulta = "SELECT\n"
                + "consecutivo,DATE_FORMAT(fecha_seguimiento,'%d/%m/%Y'),observaciones, REPLACE(FORMAT(abono, 0),',','.'), REPLACE(FORMAT(saldo, 0),',','.')\n"
                + "FROM \n"
                + "seguimiento_del_tratamiento\n"
                + "WHERE\n"
                + "pfk_paciente = '" + tipydoc + "'\n"
                + "AND pfk_tratamiento = '" + tratamiento + "'\n"
                + "ORDER BY\n"
                + "consecutivo ASC";
        ///System.out.println("consulta--cargarSeguimientoDelTratamiento-->"+consulta);
        segtrat = sql.SELECT(consulta);

        if (segtrat.size() > 0) {
            cargarTablaSegTrat(segtrat);
        }else{
            cargarTablaSegTrat(null);
        }
    }

    public void cargarTratamientos(String tipydoc) {
        gestorMySQL sql = new gestorMySQL();
        String consulta = "";

        if (tipoPaciente.equalsIgnoreCase("p")) {
            consulta = "SELECT\n"
                    + "fk_tratamiento, descripcion\n"
                    + "FROM\n"
                    + "pacientextratamiento,\n"
                    + "tratamientos\n"
                    + "WHERE\n"
                    + "pk_tratamiento = fk_tratamiento\n"
                    + "AND pfk_paciente = '" + tipydoc + "'\n"
                    + "ORDER BY\n"
                    + "fk_tratamiento ASC";
        } else {
            consulta = "SELECT\n"
                    + "pfk_tratamiento, descripcion\n"
                    + "FROM\n"
                    + "pacientexcotizaciones,\n"
                    + "tratamientos\n"
                    + "WHERE\n"
                    + "pk_tratamiento = pfk_tratamiento\n"
                    + "AND pfk_paciente = '" + tipydoc + "'\n"
                    + "ORDER BY\n"
                    + "pfk_tratamiento ASC";
        }

        tratamientosxpaciente = sql.SELECT(consulta);
        if (tratamientosxpaciente.size() > 0) {
            cargarCBTratamientos(tratamientosxpaciente, 1);
        }
    }

    public void cargarCBTratamientos(ArrayList<String[]> txp, int columna) {
        cbTratamientos.removeAllItems();
        if (txp != null) {
            for (int i = 0; i < txp.size(); i++) {
                cbTratamientos.addItem(txp.get(i)[columna]);
            }
        }
    }

    private void mostrarDatosDelPaciente() {
//        System.out.println("### if (!txtDocumento.isEnabled() = " + txtDocumento.isEnabled() + " && !txtDocumento.getText().isEmpty() = " + txtDocumento.getText().isEmpty() + ") = " + (!txtDocumento.isEnabled() && !txtDocumento.getText().isEmpty()));
//        if (!txtDocumento.isEnabled() && !txtDocumento.getText().isEmpty()) {
//
//            txtmenpac.setText("<html><p>Se encuentra viendo la <b>HISTORIA CLINICA</b>"
//                    + " del paciente <b>" + (txtNombres.getText() + " " + txtApellidos.getText()).trim() + "</b>"
//                    + " identificado con <b>" + cbTipoDocumento.getSelectedItem() + "</b> Número"
//                    + " <b>" + txtDocumento.getText() + "</b>");
//        } else {
//            txtmenpac.setText("Seleccione el paciente");
//        }
    }

    private boolean existeSegTrat(String tipydoc, String tratamiento) {
        gestorMySQL SQL = new gestorMySQL();
        String consulta = "SELECT\n"
                + "pfk_historia_clinica\n"
                + "FROM\n"
                + "seguimiento_del_tratamiento\n"
                + "WHERE\n"
                + "pfk_historia_clinica = " + indices[0] + ""
                + "AND pfk_tratamiento = " + tratamientosxpaciente.get(cbTratamientos.getSelectedIndex())[0];
        return SQL.ExistenDatos(consulta);
    }

    private void formatearSegTrat(ArrayList<String[]> datos) {
        if (datos != null) {
            if (datos.get(0)[4] != null) {
                int total = Integer.parseInt(datos.get(0)[4]);
                int saldo = 0;
                for (int i = 0; i < datos.size(); i++) {
                    saldo = total - Integer.parseInt(datos.get(i)[3]);
                    datos.get(i)[4] = "" + saldo;
                    total = saldo;
                }
            }
        }
    }

    private void establecerSelector(JLabel btnuno, JLabel btndos, String id) {

        if (id.equalsIgnoreCase("d")) {
            btnuno.setBackground(new Color(27, 79, 114));
            btnuno.setForeground(new Color(255, 255, 255));
//            btnLMSDesviacionD.setOpaque(true);
            btndos.setBackground(new Color(133, 193, 233));
            btndos.setForeground(new Color(27, 79, 114));
//            btnLMSDesviacionI.setOpaque(false);
            desviacionLMS = "D";
        } else {
            btnuno.setBackground(new Color(133, 193, 233));
            btnuno.setForeground(new Color(27, 79, 114));
//            btnLMSDesviacionD.setOpaque(true);
            btndos.setBackground(new Color(27, 79, 114));
            btndos.setForeground(new Color(255, 255, 255));
//            btnLMSDesviacionI.setOpaque(false);
            desviacionLMS = "I";
        }

    }

    private boolean hayParalelismoMaxilar() {
        boolean retorno = true;
        for (int i = 0; i < swing.size(); i++) {
            boolean hayunoSelccionado = false;
            for (int j = 0; j < 3; j++) {
                if (!swing.get(i)[j].isSelected()) {
                    hayunoSelccionado |= false;
                } else {
                    hayunoSelccionado |= true;
                    break;
                }
            }
            retorno &= hayunoSelccionado;
        }
        return retorno;
    }

    private void editarSeguimientoDelTratamiento(boolean b) {
        cbTratamientos.setEnabled(b);

//        b = tipoPaciente.equalsIgnoreCase("p");
        tblSegTrat.setEnabled(b);
        btnAgregarFila.setEnabled(b);
    }

    private void editarSeguimientoDeImgenes(boolean b) {
        imagenes.setEnabled(b);
    }

    private void editarOdontograma(boolean b) {
        odontograma.setEnabled(b);
        chkCerrarHC.setEnabled(b);
    }

    private ArrayList<String[]> obtenerDatosPacientesxCotizacion(String idpacaux) {
        gestorMySQL gm = new gestorMySQL();
        return gm.SELECT("SELECT * FROM pacientexcotizaciones WHERE pfk_paciente =  '" + idpacaux + "' AND pfk_tratamiento = " + tratamientosxpaciente.get(cbTratamientos.getSelectedIndex())[0] + "");
    }

    private void establecerNumeracionFormulacion(String texto) {
        if (cbformulacionyremisiones.getSelectedIndex() == 1) {
            String[] form = texto.split("\n");
            txtformulacionyremisiones.setText(texto + (form.length + 1) + ".\t");
        }
    }

    private void editarFormulacionYRemisiones(boolean editar) {
        cbformulacionyremisiones.setEnabled(editar);
        txtformulacionyremisiones.setEnabled(editar);
        btnImprimir.setEnabled(editar);
    }

    private String obtenerConsecutivo(String tipoydoc) {
        String con = "SELECT IFNULL(consecutivo,0)+1 FROM remisiones WHERE pfk_paciente = '" + tipoydoc + "'";
        gestorMySQL gm = new gestorMySQL();
        return gm.unicoDato(con);
    }

    private void cargarRemisiones(String tipoydoc) {
        String con = "SELECT consecutivo, descripcion FROM remisiones WHERE pfk_paciente = '" + tipoydoc + "' ORDER BY consecutivo DESC\n"
                + "LIMIT 1;";
        gestorMySQL gm = new gestorMySQL();
        remisiones = gm.unicoDato(con);
    }

    private void limpiarFormulacionYRemisiones(boolean limpiar) {
        if (limpiar) {
            cbformulacionyremisiones.setSelectedIndex(0);
            txtformulacionyremisiones.setText("");
        }
    }

    private void imprimirRemisiones(int tipo) {
        String noms = txtNombres.getText().trim();
        String apes = txtApellidos.getText().trim();
        Map<String, String> list = new HashMap<String, String>();
        list.put("nombre", noms + " " + apes);
        list.put("tipo", "" + tipo);
        list.put("descripcion", "" + txtformulacionyremisiones.getText());

        String archivo = plantilla.Generarinformes(list);
    }

    public String setDocumentoTipoDoc(String texto) {
        String edad = calcularEdadPaciente(texto);
        String tipoDoc = "", doc = "";
        for (int i = 0; i < texto.length(); i++) {
            if (Expresiones.validarSoloNumeros("" + texto.charAt(i))) {
                tipoDoc = texto.substring(0, i);
                txtDocumento.setText(texto.substring(i));
                doc = texto.substring(i);
                break;
            }
        }
        for (int i = 0; i < cbTipoDocumento.getItemCount(); i++) {
            if (cbTipoDocumento.getItemAt(i).equals(tipoDoc)) {
                cbTipoDocumento.setSelectedIndex(i);
                break;
            }
        }
        txtMensajeHC.setText("<html>"
                + "<p>Se encuentra viendo la Historia Clinica del paciente "
                + "<b>"
                + "" + txtNombres.getText() + " " + txtApellidos.getText() + " "
                + "</b>"
                + "identificado con <b>" + tipoDoc + "</b> Nro. <b>" + doc + "</b><br>"
                + "Edad del paciente: <b>" + (edad.isEmpty() ? "" : edad + " años") + "</b> "
                + "</p>"
                + "</html>");

        return doc;
    }

    public String calcularEdadPaciente(String tipoydoc) {
        gestorMySQL gsql = new gestorMySQL();
        String consulta = "SELECT\n"
                + "CASE WHEN DATEDIFF(NOW(),fecha_de_nacimiento) > 0 THEN\n"
                + "(CASE WHEN (DATEDIFF(NOW(),fecha_de_nacimiento) >= \n"
                + "ROUND(365.242 * (CAST(DATE_FORMAT(NOW(), '%Y') AS UNSIGNED) - CAST(DATE_FORMAT(fecha_de_nacimiento, '%Y') AS UNSIGNED)),0)) THEN\n"
                + "(CAST(DATE_FORMAT(NOW(), '%Y') AS UNSIGNED) - CAST(DATE_FORMAT(fecha_de_nacimiento, '%Y') AS UNSIGNED)) ELSE\n"
                + "(CAST(DATE_FORMAT(NOW(), '%Y') AS UNSIGNED) - CAST(DATE_FORMAT(fecha_de_nacimiento, '%Y') AS UNSIGNED))-1 END)\n"
                + "ELSE 0 END AS anio\n"
                + "FROM\n"
                + "personas\n"
                + "WHERE\n"
                + "CONCAT(pfk_tipo_documento,pk_persona) = '" + tipoydoc + "'";
        String edad = gsql.unicoDato(consulta);
        if (edad != null) {
            return edad;
        } else {
            return "";
        }
    }

    private void limpiarCBTratamientos() {
        cbTratamientos.removeAllItems();
    }

    public void eliminarImagen(String urlimg, String tipoimg, Imagenes img, int indice) {
        //<editor-fold defaultstate="collapsed" desc="ELIMINACION DE LA BASE DE DATOS">
        Imagenes imgaux = img;
        String consulta = "DELETE FROM hc_imagenes \n"
                + "WHERE \n"
                + "pfk_historia_clinica = " + indices[0] + " \n"
                + "AND pfk_tipo_imagen = " + tipoimg + " \n"
                + "AND CONCAT(url_imagen,'/',nombre) = '" + urlimg + "'";

        gestorMySQL SQL = new gestorMySQL();
        ArrayList<String> INSERTS = new ArrayList<>();
        INSERTS.add(consulta);

        if (INSERTS.size() > 0) {
            try {
                if (SQL.EnviarConsultas(INSERTS)) {
                    imgaux.img.remove(indice);
                    JOptionPane.showMessageDialog(this, "La imagen fue borrada con exito.");
                    //<editor-fold defaultstate="collapsed" desc="ELIMINACION DEL EQUIPO">
                    File fichero = new File(urlimg);

                    while (!fichero.delete());
                    //</editor-fold>
                    img.actualizar();
                }
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ventana.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(ventana.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //</editor-fold>
    }
}
