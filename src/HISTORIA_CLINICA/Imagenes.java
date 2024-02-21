package HISTORIA_CLINICA;

import Utilidades.Expresiones;
import java.awt.*;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.Style;

public class Imagenes extends JPanel {

    public ArrayList<String> img;
    public ArrayList<String> imgEnBlanco;
    public ArrayList<String> tipo;
    public int indice;
    public int tipoimgsel;
    public tiposDeImagenes[] tipimg;
    public botonesDeDesplazamiento[] btnDesp;
    public ImageIcon[] imgdesp;
    public int band = 0;
    public ventana vent;

    public Imagenes(ventana vr) {
        img = new ArrayList<>();
        imgEnBlanco = new ArrayList<>();
        tipo = new ArrayList<>();
        indice = 0;
        tipoimgsel = -1;
        imgdesp = new ImageIcon[]{
            new ImageIcon("Z:/Recursos/img/iconos/previous.png"),
            new ImageIcon("Z:/Recursos/img/iconos/next.png")
        };
        vent = vr;
        cargarPanelDeOpciones();
        this.addMouseMotionListener(new movimientoDelMouseIMG(this));
        this.addMouseListener(new mouseIMG(this));
    }

    public void reiniciar() {
        img = new ArrayList<>();
        tipo = new ArrayList<>();
        indice = 0;
        tipoimgsel = -1;
        reiniciarSeleccionados();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        pintarPanelLateralIzquierdo(g2d);
        if (img.size() > 0) {
            if (img.get(indice).equalsIgnoreCase("nada")) {
                g2d.setColor(new Color(255, 255, 255));
                g2d.fillRect(201, 0, this.getWidth() - 200 * 2, this.getHeight());
            } else {
//                ImageIcon Img = new ImageIcon(img.get(indice));
////                System.out.println(""+img.get(indice));
//                Img = new ImageIcon(Img.getImage().getScaledInstance(this.getWidth() - 200 * 2, this.getHeight(), Image.SCALE_DEFAULT));

//                BufferedImage bi = redimensionarImagen(img.get(indice));
//////////////////////                try {
//////////////////////                    ImageIO.write(bi, Expresiones.obtenerExtension(img.get(indice)), new File(img.get(indice)));
//////////////////////                } catch (IOException ex) {
//////////////////////                    JOptionPane.showMessageDialog(vent, "clase: Imagenes\n"
//////////////////////                            + "metodo: ImageIO.write()\n"
//////////////////////                            + "detalle: ocurrio un error al tratar de crear la imagen redimensionada\n"
//////////////////////                            + "" + ex.getMessage());
//////////////////////                }
//
                ImageIcon Img = new ImageIcon(img.get(indice));
//                System.out.println(""+img.get(indice));
//                Img = new ImageIcon(Img.getImage());
//                g2d.drawImage(bi, null, 200, 0);
                g2d.drawImage(Img.getImage(), 200, 0, Img.getIconWidth(), Img.getIconHeight(), this);
//////////////////////                g2d.drawImage(bi, null, 200, 0);

            }
            pintarBotonesDeDesplazamiento(g2d);
        } else {
            g2d.setColor(new Color(255, 255, 255));
            g2d.fillRect(201, 0, this.getWidth() - 200 * 2, this.getHeight());
            actualizar();
        }
        g2d.dispose();
    }

    public void actualizar() {
        repaint();
    }

    private void pintarPanelLateralIzquierdo(Graphics2D g) {
        g.setColor(new Color(26, 82, 118));//color de fondo
        g.fillRect(0, 0, 200, this.getHeight());//se pinta el rectangulo relleno de la izq
        g.setColor(new Color(30, 30, 30));
        g.drawRect(0, 0, 200, this.getHeight() - 1);

        for (int i = 0; i < tipimg.length; i++) {
            if (tipimg[i].sel) {
                if (tipimg[i].desc.equals("BORRAR IMAGEN")) {
                    g.setColor(new Color(146, 43, 33));//color rojo oscuro
                    g.setFont(new Font("Tahoma", Font.BOLD, 12));
                } else {
                    g.setColor(tipimg[i].colorsel);
                    g.setFont(new Font("Tahoma", Font.BOLD, 12));
                }
            } else {
                if (tipimg[i].desc.equals("BORRAR IMAGEN")) {
                    g.setColor(new Color(192, 57, 43));//color rojo claro
                    g.setFont(new Font("Tahoma", Font.BOLD, 12));
                } else {
                    g.setColor(tipimg[i].fondo);
                    g.setFont(new Font("Tahoma", Font.PLAIN, 12));
                }
            }
            g.fillRect(tipimg[i].x, tipimg[i].y, tipimg[i].ancho, tipimg[i].alto);
//            g.setColor(new Color(15,48,69));
//            g.drawRect(tipimg[i].x, tipimg[i].y, tipimg[i].ancho, tipimg[i].alto);
            g.setColor(new Color(255, 255, 255));
            g.drawString(tipimg[i].desc, 20, i * 50 + 30);
        }
    }

    private void cargarPanelDeOpciones() {
        String tiposIMG[] = new String[]{
            "PANORAMICAS",
            "RADIOGRAFIAS",
            "ODONTOGRAMAS",
            "FOTOGRAFIAS",
            "ANALISIS CEFALOMETRICO",
            "BORRAR IMAGEN",
            "CARGAR IMAGENES"
        };
        tipimg = new tiposDeImagenes[tiposIMG.length];
        for (int i = 0; i < tipimg.length; i++) {
            tipimg[i] = new tiposDeImagenes(0, 50 * i, 200, 50, new Color(26, 82, 118), tiposIMG[i]);
        }
    }

    private void pintarBotonesDeDesplazamiento(Graphics2D g) {
        int sep = 20;
        if (band == 0) {
            btnDesp = new botonesDeDesplazamiento[2];
            btnDesp[0] = new botonesDeDesplazamiento(200 + sep, this.getHeight() / 2 - imgdesp[0].getImage().getHeight(null) / 2, 48, 48, imgdesp[0].getImage());
            btnDesp[1] = new botonesDeDesplazamiento((this.getWidth() - 200) - sep - imgdesp[1].getImage().getWidth(null), this.getHeight() / 2 - imgdesp[1].getImage().getHeight(null) / 2, 48, 48, imgdesp[1].getImage());
            band = 1;
//        } else {
//            for (int i = 0; i < btnDesp.length; i++) {
//                g.drawImage(btnDesp[i].imagen, btnDesp[i].x, btnDesp[i].y, btnDesp[i].ancho, btnDesp[i].alto, null);
//            }
        }

        for (int i = 0; i < btnDesp.length; i++) {
            g.drawImage(btnDesp[i].imagen, btnDesp[i].x, btnDesp[i].y, btnDesp[i].ancho, btnDesp[i].alto, null);
        }
    }

    public void reiniciarSeleccionados() {
        if (tipimg != null) {
            for (int i = 0; i < tipimg.length; i++) {
                tipimg[i].sel = false;
            }
        }
    }

    public BufferedImage redimensionarImagen(String archivo) {
        BufferedImage bi = null;
//        BufferedImage bi = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
        try {
            bi = ImageIO.read(new File(archivo));

            int ancho = bi.getWidth();
            int alto = bi.getHeight();
            int anchoe = vent.odontograma.getWidth() - 200 * 2;
            int altoe = vent.odontograma.getHeight() - 210;
            BufferedImage bifinal = new BufferedImage(anchoe, altoe, bi.getType());
            Graphics2D g = bifinal.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(bi, 0, 0, anchoe, altoe, 0, 0, ancho, alto, null);
//            g.rotate(Math.toRadians(90));//quitar sino funciona
            g.dispose();
            return bifinal;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(vent, "clase: Imagenes\n"
                    + "metodo: redimensionarImagen()\n"
                    + "detalle: ocurrio un error al tratar de crear el buffer de la imagen '" + archivo + "'\n"
                    + "" + ex.getMessage());
            return null;
        }
    }

}
