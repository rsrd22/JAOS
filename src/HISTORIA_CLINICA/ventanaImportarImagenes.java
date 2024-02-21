package HISTORIA_CLINICA;

import ARCHIVOS.ControlArchivos;
import BaseDeDatos.gestorMySQL;
import Utilidades.*;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class ventanaImportarImagenes extends javax.swing.JFrame {

    public ventana ven;
    public ControlArchivos ca;
    private int x, y;
    private Imagenes img;

    public ventanaImportarImagenes(ventana venRef) {
        initComponents();
        Utilidades.EstablecerIcono(this);
        ca = new ControlArchivos();
        x = 0;
        y = 0;
        this.setLocationRelativeTo(null);
        ven = venRef;
        img = new Imagenes(ven);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bgTipodeimagen = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel();
        txtURL = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        btnSelarch = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        btnCargar = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel1 = new javax.swing.JPanel();
        rbPanoramica = new javax.swing.JRadioButton();
        rbFotografia = new javax.swing.JRadioButton();
        rbRadiografia = new javax.swing.JRadioButton();
        rbOdontograma = new javax.swing.JRadioButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 82, 118)));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtURL.setEditable(false);
        txtURL.setBackground(new java.awt.Color(255, 255, 255));
        txtURL.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        txtURL.setCaretColor(new java.awt.Color(26, 82, 118));
        txtURL.setSelectionColor(new java.awt.Color(26, 82, 118));
        jPanel2.add(txtURL, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 170, 190, 30));

        jPanel4.setBackground(new java.awt.Color(26, 82, 118));

        btnSelarch.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSelarch.setForeground(new java.awt.Color(255, 255, 255));
        btnSelarch.setText("Seleccionar Archivo");
        btnSelarch.setBorderPainted(false);
        btnSelarch.setContentAreaFilled(false);
        btnSelarch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSelarchMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSelarchMouseExited(evt);
            }
        });
        btnSelarch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelarchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 151, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(btnSelarch)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(btnSelarch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jPanel2.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, -1, 30));

        jPanel5.setBackground(new java.awt.Color(26, 82, 118));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnCargar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCargar.setForeground(new java.awt.Color(255, 255, 255));
        btnCargar.setText("Cargar Imagenes");
        btnCargar.setBorderPainted(false);
        btnCargar.setContentAreaFilled(false);
        btnCargar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCargarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCargarMouseExited(evt);
            }
        });
        btnCargar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCargarActionPerformed(evt);
            }
        });
        jPanel5.add(btnCargar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 140, 30));

        jPanel2.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 220, 140, 30));
        jPanel2.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 200, 190, -1));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(" Tipos de Imagenes"));

        rbPanoramica.setBackground(new java.awt.Color(255, 255, 255));
        bgTipodeimagen.add(rbPanoramica);
        rbPanoramica.setSelected(true);
        rbPanoramica.setText("Panoramica");

        rbFotografia.setBackground(new java.awt.Color(255, 255, 255));
        bgTipodeimagen.add(rbFotografia);
        rbFotografia.setText("Fotografía");

        rbRadiografia.setBackground(new java.awt.Color(255, 255, 255));
        bgTipodeimagen.add(rbRadiografia);
        rbRadiografia.setText("Perfil");

        rbOdontograma.setBackground(new java.awt.Color(255, 255, 255));
        bgTipodeimagen.add(rbOdontograma);
        rbOdontograma.setText("Análisis Cefalométrico");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(rbPanoramica)
                        .addGap(18, 18, 18)
                        .addComponent(rbRadiografia)
                        .addGap(18, 18, 18)
                        .addComponent(rbOdontograma))
                    .addComponent(rbFotografia))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbPanoramica)
                    .addComponent(rbRadiografia)
                    .addComponent(rbOdontograma))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rbFotografia)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 350, -1));

        jPanel6.setBackground(new java.awt.Color(26, 82, 118));
        jPanel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel6MousePressed(evt);
            }
        });
        jPanel6.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jPanel6MouseDragged(evt);
            }
        });
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/minimizar.png"))); // NOI18N
        jLabel6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel6MouseClicked(evt);
            }
        });
        jPanel6.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 0, 30, 30));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/cerrar_1.png"))); // NOI18N
        jLabel5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5MouseClicked(evt);
            }
        });
        jPanel6.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 0, 30, 30));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Importar Imagenes");
        jPanel6.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 9, 220, -1));

        jPanel2.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 380, 31));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 370, 270));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSelarchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelarchActionPerformed
        String url = "Z:\\Escaneo\\";

        if (rbPanoramica.isSelected()) {
            url += "Panoramica";
        } else if (rbRadiografia.isSelected()) {
            url += "Perfil";
        } else if (rbFotografia.isSelected()) {
            url += "Fotografias";
        } else if (rbOdontograma.isSelected()) {
            url += "Trazo cefalometrico";
        }
        
        txtURL.setText(Expresiones.seleccionarArchivo(true, url));
        btnCargar.requestFocusInWindow();
    }//GEN-LAST:event_btnSelarchActionPerformed

    private void btnCargarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCargarActionPerformed
        if (!txtURL.getText().isEmpty()) {
            cargarDatos();
            ven.imagenes.actualizar();
            this.dispose();
        } else {
            btnSelarch.requestFocusInWindow();
            JOptionPane.showMessageDialog(this, "Debe seleccionar al menos una imagen a cargar.");
        }
    }//GEN-LAST:event_btnCargarActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        this.dispose();
    }//GEN-LAST:event_formWindowClosing

    private void btnSelarchMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSelarchMouseEntered
        jPanel4.setBackground(new Color(31, 97, 141));
    }//GEN-LAST:event_btnSelarchMouseEntered

    private void btnSelarchMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSelarchMouseExited
        jPanel4.setBackground(new Color(26, 82, 118));
    }//GEN-LAST:event_btnSelarchMouseExited

    private void btnCargarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCargarMouseEntered
        jPanel5.setBackground(new Color(31, 97, 141));
    }//GEN-LAST:event_btnCargarMouseEntered

    private void btnCargarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCargarMouseExited
        jPanel5.setBackground(new Color(26, 82, 118));
    }//GEN-LAST:event_btnCargarMouseExited

    private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseClicked
        this.setState(ventanaImportarImagenes.ICONIFIED);
    }//GEN-LAST:event_jLabel6MouseClicked

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
        this.dispose();
    }//GEN-LAST:event_jLabel5MouseClicked

    private void jPanel6MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel6MousePressed
        x = evt.getX();
        y = evt.getY();
    }//GEN-LAST:event_jPanel6MousePressed

    private void jPanel6MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel6MouseDragged
        this.setLocation(this.getLocation().x + evt.getX() - x, this.getLocation().y + evt.getY() - y);
    }//GEN-LAST:event_jPanel6MouseDragged

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
            java.util.logging.Logger.getLogger(ventanaImportarImagenes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ventanaImportarImagenes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ventanaImportarImagenes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ventanaImportarImagenes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ventanaImportarImagenes(null).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgTipodeimagen;
    public javax.swing.JButton btnCargar;
    public javax.swing.JButton btnSelarch;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JRadioButton rbFotografia;
    private javax.swing.JRadioButton rbOdontograma;
    private javax.swing.JRadioButton rbPanoramica;
    private javax.swing.JRadioButton rbRadiografia;
    public javax.swing.JTextField txtURL;
    // End of variables declaration//GEN-END:variables

    private void cargarDatos() {
        gestorMySQL SQL = new gestorMySQL();
        ArrayList<String> INSERT = new ArrayList<>();
        String imagenes[] = txtURL.getText().split("#-#");//se obtienen las URL's de las imagenes
        String nombres = "";

        int tipoIMG = 3;

        if (rbPanoramica.isSelected()) {
            tipoIMG = 0;
        } else if (rbRadiografia.isSelected()) {
            tipoIMG = 1;
        } else if (rbOdontograma.isSelected()) {
            tipoIMG = 4;
        } else if (rbFotografia.isSelected()) {
            tipoIMG = 3;
        }

        int consecutivo = Expresiones.getConsecutivoXHCYTI(ven.indices[0], tipoIMG);

        String consulta = "INSERT INTO hc_imagenes VALUES";
        for (int i = 0; i < imagenes.length; i++) {
            imagenes[i] = imagenes[i].replace('\\', '/');
            String nombreIMG = imagenes[i].substring(imagenes[i].lastIndexOf("/") + 1);

            String conidhc = "SELECT\n"
                    + "pk_historia_clinica\n"
                    + "FROM\n"
                    + "historias_clinicas hc\n"
                    + "WHERE\n"
                    + "hc.pfk_paciente = '" + ven.cbTipoDocumento.getSelectedItem() + ven.txtDocumento.getText() + "'";
            String idhc = SQL.unicoDato(conidhc);

            String conup = "SELECT\n"
                    + "'IMGDUP'\n"
                    + "FROM\n"
                    + "hc_imagenes\n"
                    + "WHERE\n"
                    + "pfk_historia_clinica = " + idhc + "\n"
                    + "AND nombre = '" + nombreIMG + "'";

            if (SQL.unicoDato(conup) == null) {
                if (i == imagenes.length - 1) {
                    consulta += "(" + idhc + ",'" + tipoIMG + "','" + Parametros.dirRaiz + ven.cbTipoDocumento.getSelectedItem() + ven.txtDocumento.getText() + "','" + nombreIMG + "'," + consecutivo + ",'" + datosUsuario.datos.get(0)[0] + "',NOW(),NULL,NULL);";
                } else {
                    consulta += "(" + idhc + ",'" + tipoIMG + "','" + Parametros.dirRaiz + ven.cbTipoDocumento.getSelectedItem() + ven.txtDocumento.getText() + "','" + nombreIMG + "'," + consecutivo + ",'" + datosUsuario.datos.get(0)[0] + "',NOW(),NULL,NULL),";
                }
                consecutivo++;
            } else {
                nombres += nombreIMG + "\n";
            }
        }
        INSERT.add(consulta);

        try {
            if (nombres.length() == 0) {
                if (INSERT.size() > 0) {
                    if (SQL.EnviarConsultas(INSERT)) {
                        System.out.println("se cargaron las imagenes con exito");
                        JOptionPane.showMessageDialog(this, "Imagnes cargadas exitosamente.");
                        //se cargan las nuevas imagenes al panel de visualizacion
                        ven.imagenes.reiniciar();
                        moverADirectorioPersonal(imagenes, Parametros.dirRaiz + ven.cbTipoDocumento.getSelectedItem() + ven.txtDocumento.getText() + "/");
                        redimensionarImagenes(imagenes);
                        ven.cargarImagenes(ven.cbTipoDocumento.getSelectedItem() + ven.txtDocumento.getText());
                    } else {
                        System.out.println("no se cargaron las imagenes");
                        JOptionPane.showMessageDialog(this, "No fue posible cargar las Imagenes.");
                    }
                } else {
                    System.out.println("no hay nada que cargar");
                    JOptionPane.showMessageDialog(this, "No hay imagenes para cargar a la Historia Clinica.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "las siguientes imagenes ya se encuentran cargadas:\n" + nombres);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void moverADirectorioPersonal(String[] imagen, String urldestino) {
        ca.crearDirectorio(urldestino);
        ca.moverArchivos(imagen, urldestino);
    }

    private void redimensionarImagenes(String[] imagen) {
        for (int i = 0; i < imagen.length; i++) {
            String nombreIMG = imagen[i].substring(imagen[i].lastIndexOf("/") + 1);
            String imag = Parametros.dirRaiz + img.vent.cbTipoDocumento.getSelectedItem() + ven.txtDocumento.getText() + "/" + nombreIMG;
            Utilidades.guardarImagen(img.redimensionarImagen(imag), imag);
        }
    }

}
