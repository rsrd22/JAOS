package HISTORIA_CLINICA;

import BaseDeDatos.gestorMySQL;
import Utilidades.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Odontograma extends JPanel {

    public ArrayList<Diente> dientes;
    public ArrayList<String> ids;
    public ArrayList<Convencion> convenciones;
    public int idConvencionSeleccionada = -1;
    public int idDientePress = -1;
    public int idOpcionSeleccionada = -1;
    public int btnMousePress[] = new int[]{0, 0};
    public int band = 0;

    public Odontograma() {
        dientes = new ArrayList<Diente>();
        convenciones = new ArrayList<Convencion>();
        ids = new ArrayList<>();
        this.addMouseListener(new Mouse(this));
        this.addMouseMotionListener(new movimientoDelMouse(this));

        cargarConvenciones();
        cargarDientes();
    }

    public void actualizar() {
        this.repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, this.getWidth(), this.getHeight());

        pintarBarraDeHerramientas(g2d);

        g2d.setColor(Color.black);

        //<editor-fold defaultstate="collapsed" desc="DIENTE Y SUS CONVENCIONES">
        for (int i = 0; i < dientes.size(); i++) {
            if (i == 0) {
                pintarLineasDivisorias(g2d, dientes.get(i));
            }

            pintarDiente(dientes.get(i), g2d);

            if (!dientes.get(i).convenciones.isEmpty()) {
                for (int j = 0; j < dientes.get(i).convenciones.size(); j++) {
                    pintarConvDiente(g2d, dientes.get(i), dientes.get(i).convenciones.get(j));
                }
            }
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="MENSAJE EMERGENTE">
        for (int i = 0; i < convenciones.size(); i++) {
            if (convenciones.get(i).over) {
                pintarMensajeEmergente(g2d, convenciones.get(i));
            }
        }
        //</editor-fold>
        if (idDientePress != -1) {
            pintarMenuDeOpciones(g2d, dientes.get(idDientePress));
        }
        g2d.setColor(Color.black);

    }

    private void pintarBarraDeHerramientas(Graphics2D g) {
        g.setColor(new Color(240, 240, 240));//color de fondo
        g.fillRect(0, 0, 100, this.getHeight() - 1);//se pinta el rectangulo relleno de la izq
        g.setColor(new Color(30, 30, 30));
        g.drawRect(0, -1, 101, this.getHeight());
        g.setColor(Color.darkGray);
        g.drawString("Herramientas", 11, 20);
        g.setColor(new Color(200, 200, 200));
        g.drawLine(5, 25, 95, 25);
        int x = 6, y = 45, ancho = 40;

        if (convenciones != null) {
            for (int i = 0; i < convenciones.size(); i++) {
                if (i % 2 == 0 && i != 0) {
                    y += ancho + 6;
                    x = 6;
                } else if (i == 0) {
                    x = 6;
                } else {
                    x += ancho + 6;
                }

                g.setColor(convenciones.get(i).fondo);//fondo
                g.fillRect(x, y, ancho, ancho);
                g.setColor(convenciones.get(i).borde);//borde
                g.drawRect(x, y, ancho, ancho);
                convenciones.get(i).x = x;
                convenciones.get(i).y = y;
                convenciones.get(i).ANCHO = ancho;
                convenciones.get(i).ALTO = ancho;
                g.setColor(convenciones.get(i).color);

                //<editor-fold defaultstate="collapsed" desc="SE PINTAN LOS BOTONES DE LA BARRA DE HERRAMIENTAS">
                switch (convenciones.get(i).tipoDeConvencion) {
                    case tiposDeConvencion.CV_CUADRADA:
                        PintarConvencionCuadrada(g, convenciones.get(i), 10);
                        break;
                    case tiposDeConvencion.CV_TRIANGULAR:
                        PintarConvencionTriangular(g, convenciones.get(i), 10);
                        break;
                    case tiposDeConvencion.CV_CIRCULAR:
                        PintarConvencionCircular(g, convenciones.get(i), 10);
                        break;
                    case tiposDeConvencion.CV_LHORIZONTAL:
                        PintarConvencionLinealH(g, convenciones.get(i), 10);
                        break;
                    case tiposDeConvencion.CV_LVERTICAL:
                        PintarConvencionLinealV(g, convenciones.get(i), 10);
                        break;
                    case tiposDeConvencion.CV_CRUZADA:
                        PintarConvencionCruzada(g, convenciones.get(i), 10);
                        break;
                    case tiposDeConvencion.CV_LHORIZONTAL_PARALELA:
                        PintarConvencionLinealHP(g, convenciones.get(i), 10);
                        break;
                    case tiposDeConvencion.CV_LVERTICAL_PARALELA:
                        PintarConvencionLinealVP(g, convenciones.get(i), 10);
                        break;
                }
                //</editor-fold>
            }
        }
    }

    private void pintarDiente(Diente d, Graphics2D g) {
        g.setColor(Color.black);
        g.drawString(d.id, d.x + (Math.round(d.ancho / 2) - d.id.length() * 3), d.y - 10);//se pinta el id del diente
        g.setColor(Color.white);
        g.fillRect(d.x, d.y, d.ancho, d.alto);//se pinta el cuadrado que representa el diente
        g.setColor(Color.black);
        g.drawRect(d.x - 1, d.y - 1, d.ancho + 1, d.alto + 1);//se pinta el borde del cuadrado que representa el diente
        g.drawLine(d.x, d.y, d.x + d.ei, d.y + d.ei);//se pinta la diagonal sup izq
        g.drawLine(d.x + d.ei + d.aac, d.y + d.ei, d.x + d.ancho, d.y);//se pinta la diagonal sup der
        g.drawLine(d.x, d.y + d.alto, d.x + d.ei, d.y + d.ei + d.aac);//se pinta la diagonal inf izq
        g.drawLine(d.x + d.ei + d.aac, d.y + d.ei + d.aac, d.x + d.ancho, d.y + d.alto);//se pinta la diagonal inf der
        g.drawRect(d.x + d.ei, d.y + d.ei, d.aac, d.aac);//se pinta el cuadrado interior
    }

    private void cargarDientes() {
        int posfija = 130, posinix = 130, posiniy = 100, anchodiente = 60, seph = 8, sepv = 30, indice = 0;
        cargarID();
        //<editor-fold defaultstate="collapsed" desc="PRIMERA SECCION DE DIENTES">
        for (int i = 1; i <= 16; i++) {
            dientes.add(new Diente("" + ids.get(indice++), posinix, posiniy, anchodiente));

            if (i % 8 == 0 && i != 0) {
                seph = 16;
            } else {
                seph = 8;
            }
            posinix += anchodiente + seph;
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="SEGUNDA SECCION DE DIENTES">
        seph = 8;
        posinix = anchodiente * 3 + seph * 3 + posfija;
        posiniy += anchodiente + sepv;
        for (int i = 1; i <= 10; i++) {
            dientes.add(new Diente("" + ids.get(indice++), posinix, posiniy, anchodiente));

            if (i % 5 == 0 && i != 0) {
                seph = 16;
            } else {
                seph = 8;
            }
            posinix += anchodiente + seph;
        }
        //</editor-fold>

        seph = 8;//se reinicia la separacion horizontal

        //<editor-fold defaultstate="collapsed" desc="SEPEARACION HORIZONTAL">
        dientes.get(0).lineaH = new Point[]{new Point(posfija, posiniy + anchodiente + sepv),
            new Point(posfija + (anchodiente + seph) * 16, posiniy + anchodiente + sepv)
        };
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="TERCERA SECCION DE DIENTES">
        seph = 8;
        sepv = 60;
        posinix = anchodiente * 3 + seph * 3 + posfija;
        posiniy += anchodiente + sepv;
        for (int i = 1; i <= 10; i++) {
            dientes.add(new Diente("" + ids.get(indice++), posinix, posiniy, anchodiente));

            if (i % 5 == 0 && i != 0) {
                seph = 16;
            } else {
                seph = 8;
            }
            posinix += anchodiente + seph;
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="CUARTA SECCION DE DIENTES">
        seph = 8;
        sepv = 30;
        posinix = posfija;
        posiniy += anchodiente + sepv;
        for (int i = 1; i <= 16; i++) {
            dientes.add(new Diente("" + ids.get(indice++), posinix, posiniy, anchodiente));

            if (i % 8 == 0 && i != 0) {
                seph = 16;
            } else {
                seph = 8;
            }
            posinix += anchodiente + seph;
        }
        //</editor-fold>

        seph = 8;//se reinicia la separacion horizontal

        //<editor-fold defaultstate="collapsed" desc="SEPEARACION VERTICAL">
        dientes.get(0).lineaV = new Point[]{new Point(posfija + (anchodiente + seph) * 8, 70),
            new Point(posfija + (anchodiente + seph) * 8, posiniy + anchodiente + sepv)
        };
        //</editor-fold>
    }

    private void cargarConvenciones() {
        int x = 0, y = 0, ancho = 0;
        //<editor-fold defaultstate="collapsed" desc="NUEVA DISTRIBUCION DE LAS CONVENCIONES">
        //primeras dos colunmas
        convenciones.add(new Convencion(0, "Caries", tiposDeConvencion.CV_CUADRADA, x, y, ancho, Color.RED));
        convenciones.add(new Convencion(1, "Amalgma", tiposDeConvencion.CV_CUADRADA, x, y, ancho, Color.BLUE));
        //segundas columnas
        convenciones.add(new Convencion(2, "Incrustación", tiposDeConvencion.CV_CUADRADA, x, y, ancho, Color.GREEN));
        convenciones.add(new Convencion(3, "Resina", tiposDeConvencion.CV_CUADRADA, x, y, ancho, Color.ORANGE));
        //terceras columnas
        convenciones.add(new Convencion(4, "Puente Removible", tiposDeConvencion.CV_LHORIZONTAL_PARALELA, x, y, ancho, new Color(10, 85, 45)));
        convenciones.add(new Convencion(5, "Implante", tiposDeConvencion.CV_TRIANGULAR, x, y, ancho, Color.BLUE));
        //caurta columnas
        convenciones.add(new Convencion(6, "Diente Perdido", tiposDeConvencion.CV_LVERTICAL, x, y, ancho, Color.MAGENTA));
        convenciones.add(new Convencion(7, "Diente no Erupcionado", tiposDeConvencion.CV_LHORIZONTAL, x, y, ancho, Color.ORANGE));
        //quintas columnas
        convenciones.add(new Convencion(8, "Exodoncia Indicada", tiposDeConvencion.CV_CRUZADA, x, y, ancho, new Color(170, 0, 0)));
        convenciones.add(new Convencion(9, "Exodoncia Por Apiñamiento o Mal Oclusión", tiposDeConvencion.CV_CRUZADA, x, y, ancho, Color.BLUE));
        //sexta columnas
        convenciones.add(new Convencion(10, "Tratamiento de conducto", tiposDeConvencion.CV_LVERTICAL_PARALELA, x, y, ancho, Color.BLUE));
//        convenciones.add(new Convencion(9, "Exodoncia Por Apiñamiento o Mal Oclusión", tiposDeConvencion.CV_CRUZADA, x, y, ancho, Color.BLUE));
        //</editor-fold>
    }

    /**
     * este metodo pinta una convencion de tipo cuadrada.
     *
     * @param g objeto grafico necesario para pintar sobte el lienzo
     * @param cv es el objeto que contiene las caracteristicas de la convencion
     * @param ajuste esta es la variable que se encarga de ajustara el grafico
     * del interior. Para que no quede justo con el grafico exterior (boton
     * cuadrado).
     */
    private void PintarConvencionCuadrada(Graphics2D g, Convencion cv, int ajuste) {
        g.fillRect(cv.x + ajuste, cv.y + ajuste, cv.ANCHO - ajuste * 2, cv.ALTO - ajuste * 2);
        g.setColor(Color.gray);
        g.drawRect(cv.x + ajuste, cv.y + ajuste, cv.ANCHO - ajuste * 2, cv.ALTO - ajuste * 2);
    }

    private void PintarConvencionTriangular(Graphics2D g, Convencion cv, int ajuste) {
        Polygon p = new Polygon(
                new int[]{cv.x + ajuste + (cv.ANCHO - ajuste * 2) / 2, cv.x + ajuste + cv.ANCHO - ajuste * 2, cv.x + ajuste},
                new int[]{cv.y + ajuste, cv.y + ajuste + cv.ALTO - ajuste * 2, cv.y + ajuste + cv.ALTO - ajuste * 2},
                3
        );

        g.fillPolygon(p);
        g.setColor(Color.gray);
        g.drawPolygon(p);
    }

    private void PintarConvencionCircular(Graphics2D g, Convencion cv, int ajuste) {
        g.fillOval(cv.x + ajuste, cv.y + ajuste, cv.ANCHO - ajuste * 2, cv.ALTO - ajuste * 2);
        g.setColor(Color.gray);
        g.drawOval(cv.x + ajuste, cv.y + ajuste, cv.ANCHO - ajuste * 2, cv.ALTO - ajuste * 2);

        g.setColor(cv.fondo);
        g.fillOval(cv.x + ajuste + 5, cv.y + ajuste + 5, cv.ANCHO - 10 - ajuste * 2, cv.ALTO - 10 - ajuste * 2);
        g.setColor(Color.gray);
        g.drawOval(cv.x + ajuste + 5, cv.y + ajuste + 5, cv.ANCHO - 10 - ajuste * 2, cv.ALTO - 10 - ajuste * 2);
    }

    private void PintarConvencionCruzada(Graphics2D g, Convencion cv, int ajuste) {
        pintarlinea(g, new Point(cv.x + ajuste, cv.y + ajuste), new Point(cv.x + cv.ANCHO - ajuste, cv.y + cv.ALTO - ajuste), 2);
        pintarlinea(g, new Point(cv.x + cv.ANCHO - ajuste, cv.y + ajuste), new Point(cv.x + ajuste, cv.y + cv.ALTO - ajuste), 2);
    }

    private void cargarID() {
        int id = 19, pot = 2;
        for (int i = 0; i < 16; i++) {
            if (i % 8 == 0 && i != 0) {
                id += 10;
                pot = 1;
            } else {
                id = (int) (id - (Math.pow(-1, pot)));
            }
            ids.add("" + id);
        }

        id = 56;
        pot = 2;
        for (int i = 0; i < 10; i++) {
            if (i % 5 == 0 && i != 0) {
                id += 10;
                pot = 1;
            } else {
                id = (int) (id - (Math.pow(-1, pot)));
            }
            ids.add("" + id);
        }

        id = 86;
        pot = 2;
        for (int i = 0; i < 10; i++) {
            if (i % 5 == 0 && i != 0) {
                id -= 10;
                pot = 1;
            } else {
                id = (int) (id - (Math.pow(-1, pot)));
            }
            ids.add("" + id);
        }

        id = 49;
        pot = 2;
        for (int i = 0; i < 16; i++) {
            if (i % 8 == 0 && i != 0) {
                id -= 10;
                pot = 1;
            } else {
                id = (int) (id - (Math.pow(-1, pot)));
            }
            ids.add("" + id);
        }
    }

    private void pintarlinea(Graphics2D g, Point ini, Point fin, int ancho) {
        Point pini = ini;
        Point pfin = fin;
        int xfijaini = 0;
        int yfijaini = 0;
        int xfijafin = 0;
        int inc = 1;
        ancho *= 2;

        g.drawLine(ini.x, ini.y, fin.x, fin.y);

        if (ini.x == fin.x) {
            xfijaini = ini.x;
            for (int i = 0; i < ancho; i++) {
                if (i % 2 == 0 && i != 0) {
                    inc++;
                }
                g.drawLine(xfijaini + (int) (inc * Math.pow(-1, i)), ini.y, xfijaini + (int) (inc * Math.pow(-1, i)), fin.y);
            }
        } else if (ini.y == fin.y) {
            yfijaini = ini.y;
            for (int i = 0; i < ancho; i++) {
                if (i % 2 == 0 && i != 0) {
                    inc++;
                }
                g.drawLine(ini.x, yfijaini + (int) (inc * Math.pow(-1, i)), fin.x, yfijaini + (int) (inc * Math.pow(-1, i)));
            }
        } else {
            xfijaini = ini.x;
            xfijafin = fin.x;
            for (int i = 0; i < ancho; i++) {
                if (i % 2 == 0 && i != 0) {
                    inc++;
                }
                g.drawLine(xfijaini + (int) (inc * Math.pow(-1, i)), ini.y, xfijafin + (int) (inc * Math.pow(-1, i)), fin.y);
            }
        }
    }

    private void PintarConvencionLinealH(Graphics2D g, Convencion cv, int ajuste) {
        pintarlinea(g, new Point(cv.x + ajuste, cv.y + ajuste + (cv.ANCHO - ajuste * 2) / 2), new Point(cv.x + ajuste + cv.ANCHO - ajuste * 2, cv.y + ajuste + (cv.ANCHO - ajuste * 2) / 2), 2);
    }

    private void PintarConvencionLinealV(Graphics2D g, Convencion cv, int ajuste) {
        pintarlinea(g, new Point(cv.x + ajuste + (cv.ANCHO - ajuste * 2) / 2, cv.y + ajuste), new Point(cv.x + ajuste + (cv.ANCHO - ajuste * 2) / 2, cv.y + ajuste + cv.ALTO - ajuste * 2), 2);
    }
    
    private void PintarConvencionLinealVP(Graphics2D g, Convencion cv, int ajuste) {
        pintarlinea(g, new Point(cv.x + ajuste + (cv.ANCHO - ajuste * 2) / 2-3, cv.y + ajuste), new Point(cv.x + ajuste + (cv.ANCHO - ajuste * 2) / 2-3, cv.y + ajuste + cv.ALTO - ajuste * 2), 2);
        pintarlinea(g, new Point(cv.x + ajuste + (cv.ANCHO - ajuste * 2) / 2+3, cv.y + ajuste), new Point(cv.x + ajuste + (cv.ANCHO - ajuste * 2) / 2+3, cv.y + ajuste + cv.ALTO - ajuste * 2), 2);
    }

    private void PintarConvencionLinealHP(Graphics2D g, Convencion cv, int ajuste) {
        pintarlinea(g, new Point(cv.x + ajuste, cv.y + cv.ALTO / 2 - 3), new Point(cv.x + cv.ANCHO - ajuste, cv.y + cv.ALTO / 2 - 3), 2);
        pintarlinea(g, new Point(cv.x + ajuste, cv.y + cv.ALTO / 2 + 3), new Point(cv.x + cv.ANCHO - ajuste, cv.y + cv.ALTO / 2 + 3), 2);
    }

    private void pintarLineasDivisorias(Graphics2D g, Diente d) {
        g.drawLine(d.lineaV[0].x, d.lineaV[0].y, d.lineaV[1].x, d.lineaV[1].y);
        g.drawLine(d.lineaH[0].x, d.lineaH[0].y, d.lineaH[1].x, d.lineaH[1].y);
    }

    private void pintarConvDiente(Graphics2D g, Diente d, Convencion c) {
        g.setColor(c.color);

        //<editor-fold defaultstate="collapsed" desc="SE PINTAN LAS CONVENCIONES EN LOS DIENTES">
        switch (c.tipoDeConvencion) {
            case tiposDeConvencion.CV_CUADRADA:
                pintarSeccion(g, d, c);
                break;
            case tiposDeConvencion.CV_TRIANGULAR:
                PintarConvencionTriangular(g, c, 1);
                break;
            case tiposDeConvencion.CV_CIRCULAR:
                PintarConvencionCircular(g, c, 1);
                break;
            case tiposDeConvencion.CV_LHORIZONTAL:
                PintarConvencionLinealH(g, c, 0);
                break;
            case tiposDeConvencion.CV_LVERTICAL:
                PintarConvencionLinealV(g, c, 0);
                break;
            case tiposDeConvencion.CV_CRUZADA:
                PintarConvencionCruzada(g, c, 1);
                break;
            case tiposDeConvencion.CV_LHORIZONTAL_PARALELA:
                PintarConvencionLinealHP(g, c, 1);
                break;
            case tiposDeConvencion.CV_LVERTICAL_PARALELA:
                PintarConvencionLinealVP(g, c, 1);
                break;
        }
        //</editor-fold>
    }

    private void pintarSeccion(Graphics2D g, Diente d, Convencion c) {
        for (int j = 0; j < d.secciones.length; j++) {//se recorren las secciones para pintar la que halla sido seleccionada
            if (d.secciones[j] != 0) {//si el vector en la posicion j es diferente de 0;
                //<editor-fold defaultstate="collapsed" desc="PINTAR SECCION DEL DIENTE">
                switch (j) {
                    case 0: {
                        g.setColor(d.convencionXSecciones[j].color);//se establece el color dependiendo de la convencion seleccioada
                        Polygon p = new Polygon(d.getPolXUP(), d.getPolYUP(), 4);
                        g.fillPolygon(p);
                        g.setColor(Color.black);
                        g.drawPolygon(p);
                        break;
                    }
                    case 1: {
                        g.setColor(d.convencionXSecciones[j].color);
                        Polygon p = new Polygon(d.getPolXLEFT(), d.getPolYLEFT(), 4);
                        g.fillPolygon(p);
                        g.setColor(Color.black);
                        g.drawPolygon(p);
                        break;
                    }
                    case 2: {
                        g.setColor(d.convencionXSecciones[j].color);
                        Polygon p = new Polygon(d.getPolXDOWN(), d.getPolYDOWN(), 4);
                        g.fillPolygon(p);
                        g.setColor(Color.black);
                        g.drawPolygon(p);
                        break;
                    }
                    case 3: {
                        g.setColor(d.convencionXSecciones[j].color);
                        Polygon p = new Polygon(d.getPolXRIGHT(), d.getPolYRIGHT(), 4);
                        g.fillPolygon(p);
                        g.setColor(Color.black);
                        g.drawPolygon(p);
                        break;
                    }
                    case 4: {
                        g.setColor(d.convencionXSecciones[j].color);
                        Polygon p = new Polygon(d.getPolXCENTER(), d.getPolYCENTER(), 4);
                        g.fillPolygon(p);
                        g.setColor(Color.black);
                        g.drawPolygon(p);
                        break;
                    }
                    default:
                        break;
                }
                //</editor-fold>
            }
        }
    }

    private void pintarMensajeEmergente(Graphics2D g, Convencion c) {
        g.setColor(new Color(240, 240, 240));//color de fondo
        g.fillRect(c.pMenEmer.x + 20, c.pMenEmer.y - 15, c.desc.length() * 6 + 20, 20);
        g.setColor(new Color(90, 90, 90));//color de fondo
        g.drawRect(c.pMenEmer.x + 20, c.pMenEmer.y - 15, c.desc.length() * 6 + 20, 20);
        g.setColor(Color.BLACK);
        g.drawString(c.desc, c.pMenEmer.x + 25, c.pMenEmer.y);
    }

    private void pintarMenuDeOpciones(Graphics2D g, Diente d) {
        int sigi = 0;
        if (band == 0) {
            for (int i = 0; i < d.convenciones.size(); i++) {
                d.mopc.opciones.add(new Opcion(d.convenciones.get(i).desc, i, d.mopc.x, d.mopc.y + i * 20, 250, 20));
                band = 1;
                sigi = i;
            }
            if (band != 0) {
                sigi++;
                d.mopc.opciones.add(new Opcion("Borrar Todo", sigi, d.mopc.x, d.mopc.y + (sigi * 20), 250, 20));
            }
        }

        for (int i = 0; i < d.mopc.opciones.size(); i++) {
            g.setColor(d.mopc.opciones.get(i).fondo);
            g.fillRect(d.mopc.x, d.mopc.y + i * 20, d.mopc.opciones.get(i).ancho, d.mopc.opciones.get(i).alto);
            g.setColor(Color.BLACK);
            g.drawRect(d.mopc.x, d.mopc.y + i * 20, d.mopc.opciones.get(i).ancho, d.mopc.opciones.get(i).alto);
            g.setColor(Color.red);
            g.drawString("x  ", d.mopc.x + 5, d.mopc.y + i * 20 + 14);
            g.setColor(Color.DARK_GRAY);
            g.drawString(d.mopc.opciones.get(i).descripcion, d.mopc.x + 15, d.mopc.y + i * 20 + 16);
        }
    }

    public void limpiar() {
        for (int i = 0; i < dientes.size(); i++) {
            dientes.get(i).convenciones.removeAll(dientes.get(i).convenciones);
            dientes.get(i).convencionXSecciones = new Convencion[]{null, null, null, null, null};
            dientes.get(i).secciones = new int[]{0, 0, 0, 0, 0};
        }
    }

    public boolean guardarImagenOdontograma(String consecOdonto, String dir, ventana ven) {
        BufferedImage bi = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
        BufferedImage rec = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics();
        rec = bi.getSubimage(102, dientes.get(0).lineaV[0].y - 23, dientes.get(0).lineaH[1].x - 50, dientes.get(0).lineaV[1].y - 20);
        paint(g);
        File directorio = new File(Parametros.dirRaiz + dir);
        try {
            if (!directorio.exists()) {
                if (directorio.mkdir()) {
                    ImageIO.write(rec, "png", new File(directorio.getAbsolutePath() + "\\odontograma_" + consecOdonto + ".png"));
                } 
                else {
                    System.out.println("Error al crear el directorio");
                }
            } else {
                ImageIO.write(rec, "png", new File(directorio.getAbsolutePath() + "\\odontograma_" + consecOdonto + ".png"));
            }

        } catch (IOException ex) {
            System.out.println("Error de escritura");
        }

        //<editor-fold defaultstate="collapsed" desc="guardar imagen">
        gestorMySQL SQL = new gestorMySQL();
        ArrayList<String> INSERT = new ArrayList<>();
        String imagen = directorio.getAbsolutePath() + "\\odontograma_" + consecOdonto + ".png";//se obtienen las URL's de las imagenes
        String nombres = "";

        final int tipoIMG = 2;

        final int consecutivo = Integer.parseInt(consecOdonto);

        String consulta = "INSERT INTO hc_imagenes VALUES";
        imagen = imagen.replace('\\', '/');
        String nombreIMG = imagen.substring(imagen.lastIndexOf("/") + 1);

        //es la misma variable consecutivo  *************
        String idhc = ""+ven.indices[0];//SQL.unicoDato(conidhc);

        String conup = "SELECT\n"
                + "'IMGDUP'\n"
                + "FROM\n"
                + "hc_imagenes\n"
                + "WHERE\n"
                + "pfk_historia_clinica = " + idhc + "\n"
                + "AND nombre = '" + nombreIMG + "'";

        if (SQL.unicoDato(conup) == null) {//si la imagen no ha sido cargada carguela
            consulta += "(" + idhc + ",'" + tipoIMG + "','" + Parametros.dirRaiz + ven.cbTipoDocumento.getSelectedItem() + ven.txtDocumento.getText() + "','" + nombreIMG + "'," + consecutivo + ",'" + datosUsuario.datos.get(0)[0] + "',NOW(),NULL,NULL);";
        } else {//si ya existe acumulela en el string nombres para mostrarla en un mensaje de advertencia
            nombres += nombreIMG + "\n";
        }
        INSERT.add(consulta);

        try {
            //es lo mismo que .isEmpty() ************
            if (nombres.isEmpty()) {
                if (!INSERT.isEmpty()) {
                    if (SQL.EnviarConsultas(INSERT)) {
                        System.out.println("se cargaron las imagenes con exito");
                        ven.imagenes.reiniciar();
                        ven.cargarImagenes(ven.cbTipoDocumento.getSelectedItem() + ven.txtDocumento.getText());
                        return true;
                    } else {
                        System.out.println("no se cargaron las imagenes");
                        return false;
                    }
                } else {
                    System.out.println("no hay imagen de odontograma para cargar");
                    return false;
                }
            } else {
                JOptionPane.showMessageDialog(this, "las siguientes imagenes ya se encuentran cargadas:\n" + nombres);
                return false;
            }
        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
            return false;
        }
        //</editor-fold>
    }

    public String guardarImagenOdontograma(String consecOdonto, String dir, ventana ven, int a) {
        BufferedImage bi = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
        BufferedImage rec = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics();
        rec = bi.getSubimage(102, dientes.get(0).lineaV[0].y - 23, dientes.get(0).lineaH[1].x - 50, dientes.get(0).lineaV[1].y - 20);
        paint(g);
        File directorio = new File(Parametros.dirRaiz + dir);
        String urli = "";
        try {
            if (!directorio.exists()) {
                if (directorio.mkdir()) {
                    urli = directorio.getAbsolutePath() + "\\odontograma_" + consecOdonto + ".png";
//                    ImageIO.write(rec, "png", new File(urli));
                } 
                else {
                    System.out.println("clase: Odontograma\n"
                            + "funcion: guardarImagenOdontograma(String consecOdonto, String dir, ventana ven, int a)\n"
                            + "Detalle: Error al crear el directorio donde se guardara la imagen.");
                }
            } else {
                urli = directorio.getAbsolutePath() + "\\odontograma_" + consecOdonto + ".png";
//                ImageIO.write(rec, "png", new File(urli));
            }
            ImageIO.write(rec, "png", new File(urli));
            Utilidades.guardarImagen(redimensionarImagen(urli), urli);

        } catch (IOException ex) {
            System.out.println("Error de escritura");
        }

        //<editor-fold defaultstate="collapsed" desc="guardar imagen en la base de datos">
        gestorMySQL SQL = new gestorMySQL();
        ArrayList<String> INSERT = new ArrayList<>();
        String imagen = directorio.getAbsolutePath() + "\\odontograma_" + consecOdonto + ".png";//se obtienen las URL's de las imagenes
        String nombres = "";

        final int tipoIMG = 2;

        final int consecutivo = Integer.parseInt(consecOdonto);

        String consulta = "INSERT INTO hc_imagenes VALUES";
        imagen = imagen.replace('\\', '/');
        String nombreIMG = imagen.substring(imagen.lastIndexOf("/") + 1);

        //es la misma variable consecutivo  *************
        String idhc = ""+ven.indices[0];//SQL.unicoDato(conidhc);

        String conup = "SELECT\n"
                + "'IMGDUP'\n"
                + "FROM\n"
                + "hc_imagenes\n"
                + "WHERE\n"
                + "pfk_historia_clinica = " + idhc + "\n"
                + "AND nombre = '" + nombreIMG + "'";

        if (SQL.unicoDato(conup) == null) {//si la imagen no ha sido cargada carguela
            consulta += "(" + idhc + ",'" + tipoIMG + "','" + Parametros.dirRaiz + ven.cbTipoDocumento.getSelectedItem() + ven.txtDocumento.getText() + "','" + nombreIMG + "'," + consecutivo + ",'" + datosUsuario.datos.get(0)[0] + "',NOW(),NULL,NULL);";
        } else {//si ya existe acumulela en el string nombres para mostrarla en un mensaje de advertencia
            nombres += nombreIMG + "\n";
        }
        
        return consulta;
        //</editor-fold>
    }
    
    public BufferedImage redimensionarImagen(String archivo) {
        BufferedImage bi = null;
//        BufferedImage bi = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
        try {
            File arch = new File(archivo);
            while (!arch.exists());
                
            bi = ImageIO.read(new File(archivo));

            int ancho = bi.getWidth();
            int alto = bi.getHeight();
            int anchoe = this.getWidth() - 200 * 2;
            int altoe = this.getHeight()-200;
            BufferedImage bifinal = new BufferedImage(anchoe, altoe, bi.getType());
            Graphics2D g = bifinal.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(bi, 0, 0, anchoe, altoe, 0, 0, ancho, alto, null);
//            g.rotate(Math.toRadians(90));//quitar sino funciona
            g.dispose();
            return bifinal;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "clase: Imagenes\n"
                    + "metodo: redimensionarImagen()\n"
                    + "detalle: ocurrio un error al tratar de crear el buffer de la imagen '" + archivo + "'\n"
                    + "" + ex.getMessage());
            return null;
        }
    }
}
