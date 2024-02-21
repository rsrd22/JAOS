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
import Vistas.VentanaPersonal;
import Vistas.VentanaUsuarios;
import java.awt.Color;
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
public class ventanaBusqueda extends javax.swing.JFrame {

    private gestorMySQL gsql;
    public DefaultTableModel modelo;
    public ControlPersonas cper;
    public int opc;
    public String[] NameColumnas;
    public String DatosEntrada;
    public String DatosSalida;
    public String SQL = "";
    public ResultSet rs = null;
    public int ini = 0;
    public int fin = 0;
    public int tot = 0;
    public int registrosIniciales, registrosTotales, registrosInicialPag, registrosFinalPag;
    public VentanaPersonal vp;
    public VentanaUsuarios vu;
    public int banest = 0;
    public int ban = 0;
    private int x, y;
    List<Map<String, String>> list_consul;
    
    /**
     * Creates new form ventanaBusqueda
     */
    public ventanaBusqueda() {
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

    public ventanaBusqueda(int opc, String DatosEntrada, String DatosSalida, VentanaPersonal vp) {

        initComponents();
        Utilidades.EstablecerIcono(this);
        gsql = new gestorMySQL();
        this.opc = opc;
        this.DatosEntrada = DatosEntrada;
        this.DatosSalida = DatosSalida;
        modelo = new DefaultTableModel();
        this.setLocationRelativeTo(null);

        this.vp = vp;                
        LlenarListaDatos();
        GenerarTabla();
        
//        tblDatos.setModel(modelo);
//
//        tblDatos.getColumnModel().getColumn(0).setPreferredWidth(10);
//        tblDatos.getColumnModel().getColumn(1).setPreferredWidth(130);
//        tblDatos.getColumnModel().getColumn(2).setPreferredWidth(200);
//        tblDatos.getColumnModel().getColumn(3).setPreferredWidth(90);
//        tblDatos.getColumnModel().getColumn(4).setPreferredWidth(0);
//
//        JTableHeader header = tblDatos.getTableHeader();
//        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
    }

    public ventanaBusqueda(int opc, String DatosEntrada, String DatosSalida, VentanaUsuarios vu) {

        initComponents();
        Utilidades.EstablecerIcono(this);
        gsql = new gestorMySQL();
        this.opc = opc;
        this.DatosEntrada = DatosEntrada;
        this.DatosSalida = DatosSalida;
        modelo = new DefaultTableModel();
        this.setLocationRelativeTo(null);
        
        LlenarListaDatos();
        GenerarTabla();

        this.vu = vu;
        banest = 1;

        tblDatos.setModel(modelo);

        tblDatos.getColumnModel().getColumn(0).setPreferredWidth(10);
        tblDatos.getColumnModel().getColumn(1).setPreferredWidth(130);
        tblDatos.getColumnModel().getColumn(2).setPreferredWidth(200);
        tblDatos.getColumnModel().getColumn(3).setPreferredWidth(90);
        tblDatos.getColumnModel().getColumn(4).setPreferredWidth(0);

        JTableHeader header = tblDatos.getTableHeader();
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
    }

    private void LlenarListaDatos(){
        switch (opc) {//PERSONAL
            case 1: {
                SQL = "SELECT IFNULL(per.pfk_tipo_documento, '') TID, IFNULL(per.pk_persona, '') IDENTIFICACION, \n"
                        + "CONCAT_WS(' ', per.primer_nombre, IFNULL(per.segundo_nombre, ''), per.primer_apellido, IFNULL(per.segundo_apellido, '')) NOMBRE, \n"
                        + "IFNULL(per.direccion, '') DIRECCION, IF(IFNULL(per.sexo, 'M')='M', 'Masculino', 'Femenino') SEXO \n"
                        + "FROM `personal` emp\n"
                        + "INNER JOIN `personas` per ON CONCAT(per.pfk_tipo_documento, per.pk_persona) = emp.pk_personal \n ORDER BY TID ASC";
                break;
            }
        }
        list_consul = gsql.ListSQL(SQL);
        if(list_consul.size()>0){
            Iterator it = list_consul.get(0).keySet().iterator();
            String col = "";
            while(it.hasNext()){
              String key = (String) it.next();
              col += (col.equals("")?"":"<::>")+key;
            }   
            System.out.println("col--"+col);
            String[] Columnas = col.split("<::>");
            //
            NameColumnas = Columnas;
        }else{
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
        setTitle("Busqueda Personal");
        setBackground(new java.awt.Color(255, 255, 255));
        setUndecorated(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 82, 118)));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(21, 67, 96));
        jLabel1.setText("Filtro");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, -1, -1));

        txtFiltro.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtFiltro.setForeground(new java.awt.Color(31, 97, 141));
        txtFiltro.setBorder(null);
        txtFiltro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtFiltroKeyPressed(evt);
            }
        });
        jPanel1.add(txtFiltro, new org.netbeans.lib.awtextra.AbsoluteConstraints(26, 60, 220, 30));

        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/lupa_1.png"))); // NOI18N
        btnBuscar.setBorderPainted(false);
        btnBuscar.setContentAreaFilled(false);
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });
        jPanel1.add(btnBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 60, 30, 30));

        btnRegistroInicial.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/inicio_1.png"))); // NOI18N
        btnRegistroInicial.setBorderPainted(false);
        btnRegistroInicial.setContentAreaFilled(false);
        btnRegistroInicial.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRegistroInicial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistroInicialActionPerformed(evt);
            }
        });
        jPanel1.add(btnRegistroInicial, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 30, 30));

        btnRegistroAnterior.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/izq_1.png"))); // NOI18N
        btnRegistroAnterior.setBorderPainted(false);
        btnRegistroAnterior.setContentAreaFilled(false);
        btnRegistroAnterior.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRegistroAnterior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistroAnteriorActionPerformed(evt);
            }
        });
        jPanel1.add(btnRegistroAnterior, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 110, 30, 30));

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
        jPanel1.add(btnRegistroSiguiente, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 110, 30, 30));

        btnRegistroFinal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/fin_1.png"))); // NOI18N
        btnRegistroFinal.setBorderPainted(false);
        btnRegistroFinal.setContentAreaFilled(false);
        btnRegistroFinal.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRegistroFinal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistroFinalActionPerformed(evt);
            }
        });
        jPanel1.add(btnRegistroFinal, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 110, 30, 30));

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

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, 540, 200));

        lblIni.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblIni.setForeground(new java.awt.Color(21, 67, 96));
        lblIni.setText("1");
        jPanel1.add(lblIni, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 120, 20, -1));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(21, 67, 96));
        jLabel2.setText("a");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 120, -1, -1));

        lblFin.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblFin.setForeground(new java.awt.Color(21, 67, 96));
        lblFin.setText("10");
        jPanel1.add(lblFin, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 120, 30, -1));

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(21, 67, 96));
        jLabel3.setText("de");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 120, -1, -1));

        lblTotal.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblTotal.setForeground(new java.awt.Color(21, 67, 96));
        lblTotal.setText("10");
        jPanel1.add(lblTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 120, 50, -1));

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
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 0, 30, 30));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/cerrar_1.png"))); // NOI18N
        jLabel5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5MouseClicked(evt);
            }
        });
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 0, 30, 30));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Consulta de Empleados");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 8, 152, -1));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 580, 30));
        jPanel1.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 230, 10));

        jPanel3.setBackground(new java.awt.Color(26, 82, 118));

        btnSeleccionar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSeleccionar.setForeground(new java.awt.Color(255, 255, 255));
        btnSeleccionar.setText("Seleccionar");
        btnSeleccionar.setBorderPainted(false);
        btnSeleccionar.setContentAreaFilled(false);
        btnSeleccionar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSeleccionarMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSeleccionarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSeleccionarMouseExited(evt);
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
            .addComponent(btnSeleccionar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnSeleccionar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 90, -1, 40));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 580, 370));

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
                    switch (opc) {
                        case 1:
                            //vp.cbTidentificacion.setSelectedItem(fila.get(0));
                            vp.tipoId = fila.get(0);
                            vp.txtIdentificacion.setText(fila.get(1));
                            vp.ConsultarPersonal();
                            this.dispose();
                            break;
                        default:
                            break;
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No ha seleccionado ninguna fila");
                }
            }
        } catch (HeadlessException e) {

        }
    }

    public void SeleccionarFilaUsuario() {
        int filaSeleccionada;
        ArrayList<String> fila = new ArrayList<>();
        try {
            filaSeleccionada = tblDatos.getSelectedRow();
            if (DatosSalida != null) {
                System.out.println("DatosSalida--->" + DatosSalida + "////");
                String[] Salida = DatosSalida.split(":-:");
                for (int i = 0; i < Salida.length; i++) {
                    for (int j = 0; j < NameColumnas.length; j++) {
                        if (Salida[i].equals(NameColumnas[j])) {
                            Salida[i] = j + "::" + Salida[i];
                            break;
                        }
                    }
                }
                for (int i = 0; i < Salida.length; i++) {
                    System.out.println("i--" + i + "->" + Salida[i]);
                }
                if (filaSeleccionada > -1) {
                    DefaultTableModel modAux = (DefaultTableModel) tblDatos.getModel();
                    for (int j = 0; j < Salida.length; j++) {
                        fila.add((String) modAux.getValueAt(filaSeleccionada, Integer.parseInt(Salida[j].split("::")[0])));
                    }
                    switch (opc) {
                        case 1:
                            vu.lblPersona.setText(fila.get(0) + fila.get(1));
                            vu.txtPersona.setText(fila.get(2));
                            vu.txtClave.requestFocusInWindow();
                            this.dispose();
                            break;
                        default:
                            break;
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No ha seleccionado ninguna fila");
                }
            }
        } catch (HeadlessException e) {
            e.printStackTrace();
        }
    }

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        this.ini = 0;
        GenerarTabla();
    }//GEN-LAST:event_btnBuscarActionPerformed

    public void Close() {
        this.dispose();
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

    private void tblDatosMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDatosMousePressed
        if (evt.getClickCount() == 2) {
            if (banest == 0) {
                SeleccionarFila();
            } else {
                SeleccionarFilaUsuario();
            }
        }
    }//GEN-LAST:event_tblDatosMousePressed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        GenerarTabla();
    }//GEN-LAST:event_formWindowOpened

    private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseClicked
        this.setState(ventanaBusqueda.ICONIFIED);
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

    private void btnSeleccionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSeleccionarActionPerformed
        if (banest == 0) {
            SeleccionarFila();
        } else if (banest == 1) {
            SeleccionarFilaUsuario();
        }
    }//GEN-LAST:event_btnSeleccionarActionPerformed

    private void btnSeleccionarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSeleccionarMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSeleccionarMouseClicked

    private void btnSeleccionarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSeleccionarMouseEntered
        jPanel3.setBackground(new Color(31, 97, 141));
    }//GEN-LAST:event_btnSeleccionarMouseEntered

    private void btnSeleccionarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSeleccionarMouseExited
        jPanel3.setBackground(new Color(26, 82, 118));
    }//GEN-LAST:event_btnSeleccionarMouseExited

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
                new ventanaBusqueda();
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

        //modelo = new DefaultTableModel(nombreColumnas, 0);
        modelo = new DefaultTableModel(nombreColumnas, 0) {
            boolean[] edit = {false, false, false, false, false};

            public boolean isCellEditable(int row, int col) {
                //System.out.println("row-->"+row+" -- col-->"+col+"::::: "+edit[col]);

                return edit[col];
            }
        };
        tblDatos.setModel(modelo);
        tblDatos.getColumnModel().getColumn(0).setPreferredWidth(50);
        tblDatos.getColumnModel().getColumn(1).setPreferredWidth(100);
        tblDatos.getColumnModel().getColumn(1).setPreferredWidth(300);
        tblDatos.getColumnModel().getColumn(1).setPreferredWidth(200);
        tblDatos.getColumnModel().getColumn(1).setPreferredWidth(70);
//        if (datos != null) {
//            for (int i = 0; i < datos.size(); i++) {
//                agregarFila(datos.get(i));
//            }
//        }

        JTableHeader header = tblDatos.getTableHeader();
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
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
        modelo.addRow(fila);
    }

    private void GenerarTabla() {
        try {
            ArrayList<String[]> datos = new ArrayList<>();
            String filtro = Utilidades.CodificarElemento(txtFiltro.getText());
            List<Map<String, String>> list_consul_aux =  new ArrayList<>();
            
            list_consul_aux = getFiltroLista(filtro);
            
            //
            if (list_consul_aux.size()>0) {
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

                for(int i = ini ; i < fin; i++){
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
            String add = "";
            if (!filtro.equals("")) {
                add = "WHERE CONCAT_WS(' ',LOWER(CONCAT(TID, IDENTIFICACION)), LOWER(NOMBRE), LOWER(DIRECCION), LOWER(SEXO)) LIKE '%" + filtro + "%' ";
            }
            switch (opc) {//PERSONAL
                case 1: {
                    SQL = "SELECT IFNULL(per.pfk_tipo_documento, '') TID, IFNULL(per.pk_persona, '') IDENTIFICACION, \n"
                            + "CONCAT_WS(' ', per.primer_nombre, IFNULL(per.segundo_nombre, ''), per.primer_apellido, IFNULL(per.segundo_apellido, '')) NOMBRE, \n"
                            + "IFNULL(per.direccion, '') DIRECCION, IF(IFNULL(per.sexo, 'M')='M', 'Masculino', 'Femenino') SEXO \n"
                            + "FROM `personal` emp\n"
                            + "INNER JOIN `personas` per ON CONCAT(per.pfk_tipo_documento, per.pk_persona) = emp.pk_personal \n ORDER BY TID ASC";

                    break;
                }
            }

            if (!SQL.equals("")) {
                
                int tot = 0, fil = 0;
                List<Map<String, String>> list_consul = gsql.ListSQL(SQL);
                
                //rs = gsql.Consultar(SQLTot);
                if (list_consul.size()>0) {
                    tot = list_consul.size();
                }
                if (tot < 100) {
                    EstadoBotonesSig(false);
                }
                this.tot = tot;
                SQL = "SELECT TID, IDENTIFICACION, NOMBRE,DIRECCION, SEXO  FROM (" + SQL + ") TABLA " + add + " LIMIT " + ini + ", 100";
                //rs = gsql.Consultar(SQL);
                list_consul = gsql.ListSQL(SQL);
                
                fin = (tot < 100 ? tot : (fin + 100 > tot ? fin + (tot - fin) : fin + 100));
                lblIni.setText("" + ini);
                lblFin.setText("" + fin);
                lblTotal.setText("" + tot);
                if (list_consul.size()>0) {
                    if (ban == 0) {
                        ban = 1;
                        EstadoVentana();
                    }
                    
                    Iterator it = list_consul.get(0).keySet().iterator();
                    String col = "";
                    while(it.hasNext()){
                      String key = (String) it.next();
                      col += (col.equals("")?"":"<::>")+key;
                    }   
                    System.out.println("col--"+col);
                    String[] Columnas = col.split("<::>");
                    //
                    NameColumnas = Columnas;
                    for(int i = 0; i < list_consul.size(); i++){
                        tot++;
                        String[] ColDatos = new String[Columnas.length];
                        for (int j = 0; j < ColDatos.length; j++) {
                            ColDatos[j] = list_consul.get(i).get(Columnas[j]);
                        }
                        datos.add(ColDatos);
                    }

                    cargarDatosTabla(Columnas, datos);

                } else {
                    JOptionPane.showMessageDialog(null, "No se encontraron datos");
                }
            }
        } catch (Exception w) {

        }
    }
    
    private void GenerarTablaResultset() {
        try {
            ArrayList<String[]> datos = new ArrayList<>();
            String filtro = Utilidades.CodificarElemento(txtFiltro.getText());
            String add = "";
            if (!filtro.equals("")) {
                add = "WHERE CONCAT_WS(' ',LOWER(CONCAT(TID, IDENTIFICACION)), LOWER(NOMBRE), LOWER(DIRECCION), LOWER(SEXO)) LIKE '%" + filtro + "%' ";
            }
            switch (opc) {//PERSONAL
                case 1: {
                    SQL = "SELECT IFNULL(per.pfk_tipo_documento, '') TID, IFNULL(per.pk_persona, '') IDENTIFICACION, \n"
                            + "CONCAT_WS(' ', per.primer_nombre, IFNULL(per.segundo_nombre, ''), per.primer_apellido, IFNULL(per.segundo_apellido, '')) NOMBRE, \n"
                            + "IFNULL(per.direccion, '') DIRECCION, IF(IFNULL(per.sexo, 'M')='M', 'Masculino', 'Femenino') SEXO \n"
                            + "FROM `personal` emp\n"
                            + "INNER JOIN `personas` per ON CONCAT(per.pfk_tipo_documento, per.pk_persona) = emp.pk_personal \n ORDER BY TID ASC";

                    break;
                }
            }

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

    private void EstadoBotonesSig(boolean b) {
        btnRegistroAnterior.setEnabled(b);
        btnRegistroFinal.setEnabled(b);
        btnRegistroInicial.setEnabled(b);
        btnRegistroSiguiente.setEnabled(b);
    }

    private List<Map<String, String>> getFiltroLista(String filtro) {
        List<Map<String, String>> retorno = new ArrayList<>();
        System.out.println("***************getFiltroLista*****************"+filtro);
        int b = -1;
        String[] filtros = filtro.isEmpty()?null:filtro.replace(" ", "<::>").split("<::>");
        String valores = "";
        for(int i = 0; i < list_consul.size(); i++){
            b = 1;
            if(filtro.isEmpty()){
                retorno.add(list_consul.get(i));
            }else{    
                valores = "";
                for(int j = 0; j < NameColumnas.length; j++){
                    String value = list_consul.get(i).get(NameColumnas[j]);
                    valores += ""+value;                
//                    System.out.println("NAme-"+j+"->"+NameColumnas[j]);
//                    String value = list_consul.get(i).get(NameColumnas[j]);
//                    System.out.println("value--->"+value);
//                    int con  = value.toUpperCase().indexOf(filtro.toUpperCase());
//                    System.out.println("con--->"+con);
//                    if(con >= 0){
//                        b = 0; 
//                        break;
//                    }
//                }
//                System.out.println("i-"+i+"-b-"+b);
//                if(b == 0){
//                    retorno.add(list_consul.get(i));
                }
                boolean encontro = Expresiones.filtrobusqueda(filtros,valores);
                System.out.println("i-"+i+"-b-"+b);
                if(encontro){
                    retorno.add(list_consul.get(i));
                }
                
            }
        }
        System.out.println("********************retorno --> "+retorno.size()+"***********************");
        return retorno;
    }

    private List<Map<String, String>> getFiltroListaOLD(String filtro) {
        List<Map<String, String>> retorno = new ArrayList<>();
        System.out.println("***************getFiltroLista*****************"+filtro);
        int b = -1;
        for(int i = 0; i < list_consul.size(); i++){
            b = 1;
            if(filtro.isEmpty()){
                retorno.add(list_consul.get(i));
            }else{                
                for(int j = 0; j < NameColumnas.length; j++){
                    System.out.println("NAme-"+j+"->"+NameColumnas[j]);
                    String value = list_consul.get(i).get(NameColumnas[j]);
                    System.out.println("value--->"+value);
                    int con  = value.toUpperCase().indexOf(filtro.toUpperCase());
                    System.out.println("con--->"+con);
                    if(con >= 0){
                        b = 0; 
                        break;
                    }
                }
                System.out.println("i-"+i+"-b-"+b);
                if(b == 0){
                    retorno.add(list_consul.get(i));
                }
            }
        }
        System.out.println("********************retorno --> "+retorno.size()+"***********************");
        return retorno;
    }
}
