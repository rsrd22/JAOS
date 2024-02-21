/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HISTORIA_CLINICA;

import BaseDeDatos.gestorMySQL;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JOptionPane;

/**
 *
 * @author User
 */
public class mouseIMG implements MouseListener {

    public Imagenes img;
    public ventana vent;
//    public int indiceimg;

    public mouseIMG(Imagenes img) {
        this.img = img;
        vent = img.vent;
//        indiceimg = 0;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        img.actualizar();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (img.isEnabled()) {
            if (img.img.size() > 0) {
                for (int i = 0; i < img.tipimg.length; i++) {
                    if (detectarClicksobrelasOpciones(img.tipimg[i], e.getPoint())) {
                        img.tipimg[i].sel = true;
                        img.tipoimgsel = i;
                        reiniciarSeleccionado(i);

                        if (img.tipo.indexOf("" + i) != -1) {//si el valor retornado es diferente de -1
                            img.indice = img.tipo.indexOf("" + i);
                        }

                        if (!vent.chkCerrarHC.isSelected()) {
                            //<editor-fold defaultstate="collapsed" desc="FUNCIONALIDAD CARGAR Y BORRAR IMAGEN">
                            if (img.tipimg[i].desc.equalsIgnoreCase("CARGAR IMAGENES")) {

                                if (!vent.txtDocumento.getText().isEmpty() && !vent.txtDocumento.isEnabled()) {//verificar si el paciente tiene HC activa
                                    if (historiaClinicaCreada(vent.cbTipoDocumento.getSelectedItem() + vent.txtDocumento.getText())) {
                                        if (vent.existeHC(vent.cbTipoDocumento.getSelectedItem() + vent.txtDocumento.getText())) {
                                            new ventanaImportarImagenes(vent).setVisible(true);
                                        } else {
                                            JOptionPane.showMessageDialog(vent, "No hay una Historia Clinica activa.");
                                        }
                                    } else {
                                        JOptionPane.showMessageDialog(vent, "No hay una Historia Clinica activa o aun no se ha guardado.");
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(vent, "No hay una Historia Clinica activa.");
                                }
                            } else if (img.tipimg[i].desc.equalsIgnoreCase("BORRAR IMAGEN")) {
                                int opc = JOptionPane.showConfirmDialog(vent, "¿Esta seguro que desea eliminar la imagen?", "Eliminar Imagen", JOptionPane.YES_NO_OPTION);

                                if (opc == JOptionPane.YES_OPTION) {
                                    vent.eliminarImagen(img.img.get(img.indice), img.tipo.get(img.indice), img, img.indice);
                                }
                            }
                            //</editor-fold>
                        } else {
                            if (img.tipimg[i].desc.equalsIgnoreCase("CARGAR IMAGENES") || img.tipimg[i].desc.equalsIgnoreCase("BORRAR IMAGEN")) {
                                JOptionPane.showMessageDialog(vent, "Funcion no disponible. Historia clinica Terminada.");
                            }
                        }
                        break;
                    }
                }

                for (int i = 0; i < img.btnDesp.length; i++) {
                    if (detectarClickSobreDesp(img.btnDesp[i], e.getPoint())) {
                        if (i == 0) {
                            img.indice -= img.indice > 0 ? 1 : 0;
                        } else {
                            img.indice += img.indice < img.img.size() - 1 ? 1 : 0;
                        }
                        int ind = Integer.parseInt(img.tipo.get(img.indice));
                        System.out.println("-> " + ind);
                        img.tipimg[ind].sel = true;
                        img.tipoimgsel = ind;
                        reiniciarSeleccionado(ind);
                        break;
                    }
                }
            } else {
                for (int i = 0; i < img.tipimg.length; i++) {
                    if (detectarClicksobrelasOpciones(img.tipimg[i], e.getPoint())) {
                        if (!vent.chkCerrarHC.isSelected()) {
                            //<editor-fold defaultstate="collapsed" desc="FUNCIONALIDAD CARGAR Y BORRAR IMAGEN">
                            if (img.tipimg[i].desc.equalsIgnoreCase("CARGAR IMAGENES")) {
                                img.tipimg[i].sel = true;
                                img.tipoimgsel = i;
                                reiniciarSeleccionado(i);
                                if (!vent.txtDocumento.getText().isEmpty() && !vent.txtDocumento.isEnabled()) {
                                    if (historiaClinicaCreada(vent.cbTipoDocumento.getSelectedItem() + vent.txtDocumento.getText())) {
                                        new ventanaImportarImagenes(vent).setVisible(true);
                                    } else {
                                        JOptionPane.showMessageDialog(vent, "No hay una Historia Clinica activa o aun no se ha guardado.");
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(vent, "No hay una Historia Clinica activa,\npor lo tanto no se puede usar esta opcion aqui.");
                                }
                            } else if (img.tipimg[i].desc.equalsIgnoreCase("BORRAR IMAGEN")) {
                                int opc = JOptionPane.showConfirmDialog(vent, "¿Esta seguro que desea eliminar la imagen?", "Eliminar Imagen", JOptionPane.YES_NO_OPTION);

                                if (opc == JOptionPane.YES_OPTION) {
                                    img.actualizar();
                                    vent.eliminarImagen(img.img.get(img.indice), img.tipo.get(img.indice), img, img.indice);
                                }
                            }
                            //</editor-fold>
                        } else {
                            if (img.tipimg[i].desc.equalsIgnoreCase("CARGAR IMAGENES") || img.tipimg[i].desc.equalsIgnoreCase("BORRAR IMAGEN")) {
                                JOptionPane.showMessageDialog(vent, "Funcion no disponible. Historia clinica Terminada.");
                            }
                        }
                        break;
                    }
                }
            }
        }
        img.actualizar();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        img.actualizar();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        img.actualizar();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        img.actualizar();
    }

    private boolean detectarClicksobrelasOpciones(tiposDeImagenes t, Point p) {
        if (p.x > t.x
                && p.x < t.x + t.ancho
                && p.y > t.y
                && p.y < t.y + t.alto) {
            return true;
        } else {
            return false;
        }
    }

    private void reiniciarSeleccionado(int sel) {
        for (int i = 0; i < img.tipimg.length; i++) {
            if (i != sel) {
                img.tipimg[i].sel = false;
            }
        }
    }

    private boolean detectarClickSobreDesp(botonesDeDesplazamiento b, Point p) {
        if (p.x > b.x
                && p.x < b.x + b.ancho
                && p.y > b.y
                && p.y < b.y + b.alto) {
            return true;
        } else {
            return false;
        }
    }

    private boolean historiaClinicaCreada(String tipoydoc) {
        gestorMySQL SQL = new gestorMySQL();
        String conidhc = "SELECT\n"
                + "pk_historia_clinica\n"
                + "FROM\n"
                + "historias_clinicas hc\n"
                + "WHERE\n"
                + "hc.pfk_paciente = '" + tipoydoc + "'";
        String idhc = SQL.unicoDato(conidhc);
        return idhc != null;
    }

}
