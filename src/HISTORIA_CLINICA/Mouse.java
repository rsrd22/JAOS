package HISTORIA_CLINICA;

import Utilidades.Expresiones;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Mouse implements MouseListener {

    public Odontograma o;
    public boolean habConvCuad;
    public int esConvCuadrada = 0;

    public Mouse(Odontograma o) {
        this.o = o;
        habConvCuad = false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        o.actualizar();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        String var = "" + o.isEnabled();
        if (o.isEnabled()) {
            switch (e.getButton()) {
                case MouseEvent.BUTTON1:
                    boolean band = true;

                    //<editor-fold defaultstate="collapsed" desc="ELIMINACION CONVENCION DEL DIENTE">
                    if (o.idDientePress != -1) {
                        if (!o.dientes.get(o.idDientePress).mopc.opciones.isEmpty()) {
                            for (int i = 0; i < o.dientes.get(o.idDientePress).mopc.opciones.size(); i++) {
                                if (notificarClickSobreOpcion(e.getPoint(), o.dientes.get(o.idDientePress).mopc.opciones.get(i))) {
                                    if (i == o.dientes.get(o.idDientePress).mopc.opciones.size() - 1) {
                                        //<editor-fold defaultstate="collapsed" desc="PROCEDIMIENTO PARA ELIMINAR LAS SECCIONES UNA A UNA">
                                        o.dientes.get(o.idDientePress).convencionXSecciones = new Convencion[]{null, null, null, null, null};
                                        o.dientes.get(o.idDientePress).nombreXSeccion = new String[]{null, null, null, null, null};
                                        o.dientes.get(o.idDientePress).secciones = new int[]{0, 0, 0, 0, 0};
                                        o.dientes.get(o.idDientePress).parteSeleccionada = -1;
                                        //</editor-fold>

                                        o.dientes.get(o.idDientePress).convenciones.removeAll(o.dientes.get(o.idDientePress).convenciones);
                                    } else {
                                        //<editor-fold defaultstate="collapsed" desc="PROCEDIMIENTO PARA ELIMINAR LAS SECCIONES UNA A UNA">
                                        if (o.dientes.get(o.idDientePress).mopc.opciones.get(i).descripcion.contains("Vestibular")
                                                || o.dientes.get(o.idDientePress).mopc.opciones.get(i).descripcion.contains("Lingual")
                                                || o.dientes.get(o.idDientePress).mopc.opciones.get(i).descripcion.contains("Mesial")
                                                || o.dientes.get(o.idDientePress).mopc.opciones.get(i).descripcion.contains("Distal")
                                                || o.dientes.get(o.idDientePress).mopc.opciones.get(i).descripcion.contains("Palatina")
                                                || o.dientes.get(o.idDientePress).mopc.opciones.get(i).descripcion.contains("Oclusal")) {

                                            System.out.println("diente = " + Integer.parseInt(o.dientes.get(o.idDientePress).id));
                                            System.out.println("seccion = " + o.dientes.get(o.idDientePress).mopc.opciones.get(i).descripcion.split(" ")[1]);
                                            int indice = obtenerSeccionAEliminar(Integer.parseInt(o.dientes.get(o.idDientePress).id), o.dientes.get(o.idDientePress).mopc.opciones.get(i).descripcion.split(" ")[1]);
                                            if (indice >= 0) {
                                                o.dientes.get(o.idDientePress).convencionXSecciones[indice] = null;
                                                o.dientes.get(o.idDientePress).nombreXSeccion[indice] = null;
                                                o.dientes.get(o.idDientePress).secciones[indice] = 0;
                                                System.out.println("indice = " + indice + "\n");
                                            }
                                        }
                                        //</editor-fold>
                                        o.dientes.get(o.idDientePress).convenciones.remove(i);
                                    }
                                    band = false;
                                    break;
                                } else {
                                    band = true;
                                }
                            }
                        }
                    }
                    //</editor-fold>

                    if (band) {
                        for (int i = 0; i < o.convenciones.size(); i++) {
                            if (seleccionarConvenvion(e.getPoint(), o.convenciones.get(i))) {
                                o.convenciones.get(i).press = true;
                                o.idConvencionSeleccionada = i;
                                o.convenciones.get(i).fondo = new Color(210, 210, 210);
                                o.convenciones.get(i).borde = new Color(180, 180, 180);
                                break;
                            }
                        }

                        for (int i = 0; i < o.dientes.size(); i++) {
                            if (o.idConvencionSeleccionada != -1) {

                                if (notificarCambioOC(e.getPoint(), o.dientes.get(i))) {
                                    validacionConvencionSobreDiente(o.dientes.get(i), o.convenciones.get(o.idConvencionSeleccionada));
                                }

                                if (habConvCuad) {
                                    notificarCambioCC(e.getPoint(), o.dientes.get(i), o.convenciones.get(o.idConvencionSeleccionada));
                                }
                            }
                        }
                    }
                    o.idDientePress = -1;
                    break;

                case MouseEvent.BUTTON3:
                    for (int i = 0; i < o.dientes.size(); i++) {
                        if (notificarCambioClickDerecho(e.getPoint(), o.dientes.get(i))) {
                            o.band = 0;
                            o.idDientePress = i;
                            break;
                        }
                    }
                    break;
                default:
                    for (int i = 0; i < o.dientes.size(); i++) {
                        o.dientes.get(i).presionadoCD = false;
                        o.idDientePress = -1;
                    }
                    o.btnMousePress = new int[]{0, 0};
                    break;
            }
        }
        o.actualizar();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        o.actualizar();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        o.actualizar();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        o.actualizar();
    }

    private boolean notificarCambioCC(Point p, Diente d, Convencion c) {
        int indice = 0;
        if (c.tipoDeConvencion == tiposDeConvencion.CV_CUADRADA) {
            if (p.x > d.x + d.ei
                    && p.x < d.x + d.ancho - d.ei
                    && p.y > d.y + d.ei
                    && p.y < d.y + d.alto - d.ei) {
                indice = 4;
            } else if (p.x > d.x
                    && p.x < (d.x + d.ei)
                    && p.y > (d.mp * (p.x - d.x) + d.y)
                    && p.y < (d.mn * (p.x - (d.x + d.ancho)) + d.y)) {
                indice = 1;
            } else if (p.y > (d.y + d.ei + d.aac)
                    && p.y < (d.y + d.alto)
                    && p.y > (d.mp * (p.x - d.x) + d.y)
                    && p.y > (d.mn * (p.x - (d.x + d.ancho)) + d.y)) {
                indice = 2;
            } else if (p.x > (d.x + d.ei + d.aac)
                    && p.x < (d.x + d.ancho)
                    && p.y < (d.mp * (p.x - d.x) + d.y)
                    && p.y > (d.mn * (p.x - (d.x + d.ancho)) + d.y)) {
                indice = 3;
            } else if (p.y > d.y
                    && p.y < (d.y + d.ei)
                    && p.y < (d.mp * (p.x - d.x) + d.y)
                    && p.y < (d.mn * (p.x - (d.x + d.ancho)) + d.y)) {
                indice = 0;
            } else {
                if (d.convenciones.size() > 0) {
                    if (d.convenciones.get(0).desc.split(" ").length <= 1
                            && d.convenciones.get(0).tipoDeConvencion == tiposDeConvencion.CV_CUADRADA) {
                        d.convenciones.remove(0);
                    }
                }
                return false;
            }

            d.parteSeleccionada = indice;
            d.secciones[indice] = 1;
            if (d.convencionXSecciones[indice] != null) {
                boolean verif = verificarConvCuad(d.convencionXSecciones, indice);
                if (verif) {
                    d.convencionXSecciones[indice] = c;
                    String nombre = Expresiones.getNombreSeccion(d, indice);
                    for (int i = 1; i < d.convenciones.size(); i++) {
                        /**
                         * se busca en las convenciones la que se va a remplazae
                         * para eliminarla.
                         */
                        if (d.convenciones.get(i).desc.split(" ")[1].equals(nombre)) {
                            d.convenciones.remove(i);
                            break;
                        }
                    }
                    if (!nombre.equals("")) {
                        d.convenciones.get(0).desc += " " + nombre;
                    }
                } else {
                    d.convenciones.remove(0);
                }
            } else {
                d.convenciones.get(0).desc += " " + Expresiones.getNombreSeccion(d, indice);
                d.convencionXSecciones[indice] = c;
            }

            return true;
        } else {
            return false;
        }
    }

    /**
     * Este metodo verifica si se esta haciendo Click sobre alguna de las
     * convenciones establecidas en el menu de opciones
     *
     * @param p la coordenada (x,y) donde se esta haciendo Click
     * @param c es la referencia a la convencion del menu de opciones a la
     * izquierda de la pantalla
     * @return boolean
     */
    private boolean seleccionarConvenvion(Point p, Convencion c) {
        if (p.x > c.x
                && p.x < c.x + c.ANCHO
                && p.y > c.y
                && p.y < c.y + c.ALTO) {
            reiniciarConcvencionPresionada();
            return true;
        } else {
            return false;
        }
    }

    /**
     * ESTE METODO HACE QUE EN EL MENU DE OPCIONES SOLO SE VEA DE COLOR GRIS LA
     * SECCION SELECCIONADA.
     */
    private void reiniciarConcvencionPresionada() {
        for (int i = 0; i < this.o.convenciones.size(); i++) {
            this.o.convenciones.get(i).press = false;
            this.o.idConvencionSeleccionada = -1;
            this.o.convenciones.get(i).fondo = new Color(240, 240, 240);
            this.o.convenciones.get(i).borde = new Color(200, 200, 200);
        }
    }

    private boolean notificarCambioOC(Point p, Diente d) {
        if (p.x > d.x
                && p.x < d.x + d.ancho
                && p.y > d.y
                && p.y < d.y + d.alto) {
            d.presionadoCI = true;
            return true;
        } else {
            d.presionadoCI = false;
            return false;
        }
    }

    private boolean notificarCambioClickDerecho(Point p, Diente d) {
        if (p.x > d.x
                && p.x < d.x + d.ancho
                && p.y > d.y
                && p.y < d.y + d.alto) {
            d.mopc = new menuDeOpciones();
            d.mopc.x = p.x;
            d.mopc.y = p.y;
            d.presionadoCD = true;
            return true;
        } else {
            d.presionadoCD = false;
            return false;
        }
    }

    private boolean notificarClickSobreOpcion(Point p, Opcion op) {
        return p.x > op.x
                && p.x < op.x + op.ancho
                && p.y > op.y
                && p.y < op.y + op.alto;
    }

    private void validacionConvencionSobreDiente(Diente d, Convencion c) {
        boolean agregarConvencion = false;
        int lastIndex = d.convenciones.size() - 1;
        if (!d.convenciones.isEmpty()) {
            agregarConvencion = verificarRestricciones(d.convenciones.get(lastIndex), c);
        } else {
            agregarConvencion = true;
        }

        if (agregarConvencion) {
            habConvCuad = true;
            Convencion conv = new Convencion(c);
            conv.x = d.x;
            conv.y = d.y;
            conv.ANCHO = d.ancho;
            conv.ALTO = d.alto;
            if (conv.tipoDeConvencion == tiposDeConvencion.CV_CUADRADA) {
                d.convenciones.add(0, conv);
            } else {
                if (!d.convenciones.isEmpty()) {
                    d.convenciones = new ArrayList<>();
                    d.convencionXSecciones = new Convencion[5];
                }

                //esto es para eliminar las secciones cuando sobre ellas hay otra convencion
                if (esConvCuadrada == 1) {
                    int tam = d.convenciones.size();
                    for (int i = 0; i < tam; i++) {
                        d.convenciones.remove(0);
                    }
                    esConvCuadrada = 0;
                }
                //se agrega la nueva convencion
                d.convenciones.add(conv);
            }
        } else {
            habConvCuad = false;
        }
    }

    private boolean verificarRestricciones(Convencion uc, Convencion nc) {
        //<editor-fold defaultstate="collapsed" desc="NUEVAS CONDICIONES">
        if (nc.tipoDeConvencion == tiposDeConvencion.CV_CRUZADA) {
            if (uc.tipoDeConvencion == tiposDeConvencion.CV_TRIANGULAR
                    || uc.tipoDeConvencion == tiposDeConvencion.CV_LHORIZONTAL_PARALELA
                    || uc.tipoDeConvencion == tiposDeConvencion.CV_LVERTICAL_PARALELA
                    || uc.tipoDeConvencion == tiposDeConvencion.CV_LVERTICAL
                    || uc.tipoDeConvencion == tiposDeConvencion.CV_CRUZADA) {
                return false;
            } else {
                return true;
            }
        } else if (nc.tipoDeConvencion == tiposDeConvencion.CV_TRIANGULAR) {
            if (uc.tipoDeConvencion == tiposDeConvencion.CV_LHORIZONTAL
                    || uc.tipoDeConvencion == tiposDeConvencion.CV_LVERTICAL_PARALELA) {
                return false;
            } else {
                return true;
            }
        } else if (nc.tipoDeConvencion == tiposDeConvencion.CV_LVERTICAL_PARALELA) {//agregado 
            if (uc.tipoDeConvencion == tiposDeConvencion.CV_CUADRADA
                    || uc.tipoDeConvencion == tiposDeConvencion.CV_LVERTICAL_PARALELA) {
                esConvCuadrada = 0;
                return false;
            } else {
                esConvCuadrada = 1;//es cuadrada
                System.out.println("desc -> " + nc.desc);
                return true;
            }
        } else if (nc.tipoDeConvencion == tiposDeConvencion.CV_CUADRADA) {
            if (uc.tipoDeConvencion == tiposDeConvencion.CV_TRIANGULAR
                    || uc.tipoDeConvencion == tiposDeConvencion.CV_LHORIZONTAL_PARALELA
                    || uc.tipoDeConvencion == tiposDeConvencion.CV_LVERTICAL
                    || uc.tipoDeConvencion == tiposDeConvencion.CV_LHORIZONTAL
                    || uc.tipoDeConvencion == tiposDeConvencion.CV_CRUZADA) {
                esConvCuadrada = 0;
                return false;
            } else {
                esConvCuadrada = 1;//es cuadrada
                System.out.println("desc -> " + nc.desc);
                return true;
            }
        } else if (nc.tipoDeConvencion == tiposDeConvencion.CV_LHORIZONTAL_PARALELA) {
            if (uc.tipoDeConvencion == tiposDeConvencion.CV_LVERTICAL
                    || uc.tipoDeConvencion == tiposDeConvencion.CV_CRUZADA) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
        //</editor-fold>
    }

    private String guardarEn() {
        String ret = "";
        JFileChooser selectorDeArchivos = new JFileChooser();

        selectorDeArchivos.setDialogTitle("Guardar En...");
        selectorDeArchivos.setCurrentDirectory(new File("C:\\"));
        javax.swing.filechooser.FileFilter filtro = new FileNameExtensionFilter("Directorio", "dir");
        selectorDeArchivos.setAcceptAllFileFilterUsed(false);
        selectorDeArchivos.addChoosableFileFilter(filtro);
        selectorDeArchivos.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int result = selectorDeArchivos.showOpenDialog(o);
        switch (result) {
            case JFileChooser.APPROVE_OPTION:
                File archivo = selectorDeArchivos.getSelectedFile();
                ret = String.valueOf(archivo);
                break;
            case JFileChooser.CANCEL_OPTION:
                ret = "";
                break;
            case JFileChooser.ABORT:
                ret = "";
                break;
            default:
                JOptionPane.showMessageDialog(o, "Ocurrio un error al tratar de seleccionar la ubicación, vuelve a intentarlo.");
                break;
        }
        return ret;
    }

    private int obtenerSeccionAEliminar(int idDiente, String tipo) {
        if (idDiente > 10 && idDiente < 19
                || idDiente > 50 && idDiente < 56) {
            switch (tipo) {
                case "Vestibular":
                    return 0;
                case "Distal":
                    return 1;
                case "Palatina":
                    return 2;
                case "Mesial":
                    return 3;
                case "Oclusal":
                    return 4;
                default:
                    return -1;
            }
        } else if (idDiente > 20 && idDiente < 29
                || idDiente > 60 && idDiente < 66) {
            switch (tipo) {
                case "Vestibular":
                    return 0;
                case "Mesial":
                    return 1;
                case "Palatina":
                    return 2;
                case "Distal":
                    return 3;
                case "Oclusal":
                    return 4;
                default:
                    return -1;
            }
        } else if (idDiente > 80 && idDiente < 86
                || idDiente > 40 && idDiente < 49) {
            switch (tipo) {
                case "Lingual":
                    return 0;
                case "Distal":
                    return 1;
                case "Vestibular":
                    return 2;
                case "Mesial":
                    return 3;
                case "Oclusal":
                    return 4;
                default:
                    return -1;
            }
        } else if (idDiente > 70 && idDiente < 76
                || idDiente > 30 && idDiente < 39) {
            switch (tipo) {
                case "Lingual":
                    return 0;
                case "Mesial":
                    return 1;
                case "Vestibular":
                    return 2;
                case "Distal":
                    return 3;
                case "Oclusal":
                    return 4;
                default:
                    return -1;
            }
        } else {
            return -1;
        }
    }

    /**
     * Este metodo verifica si sobre la seccion que se presiona hay o no caries
     * Si hay caris permite colocar cualquier otro tipo de convencion cuadrada,
     * sino solo no la tiene en cuenta para pintarla en el odontograma
     *
     * @param convXSec -> son las convenciones por secciones que se tienen
     * @param indice -> es el indice sobre el que se desea colocar la nueva
     * seccion en el diente.
     * @return boolean
     */
    private boolean verificarConvCuad(Convencion[] convXSec, int indice) {
        if (convXSec[indice] == null) {
            return true;
        } else {
            if (!convXSec[indice].desc.split(" ")[0].equalsIgnoreCase("caries")) {
                return false;
            } else {
                return true;
            }
        }
    }

}
