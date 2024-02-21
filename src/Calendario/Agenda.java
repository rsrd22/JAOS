package Calendario;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JFrame;
import javax.swing.JPanel;
import Modelo.Calendario;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author richard
 */
public class Agenda extends JPanel {

    public int ancho = 794, alto = 598;
    public float escala = 1;
    public int tletra = 20;
    public String paciente;
    public String npaciente;
    public GenerarCalendario cp = new GenerarCalendario();
    public int indicador = 1;
    public int anchoEnc = 60;
    public int anchoDia = 104;
    public int altoDia = 60;
    public int anchoMes = 182 ;
    public int altoMes = 153;
    public int ventana = -1;
    public String fecha = "";
    VentanaAgenda Vag;
    VentanaNulos Vnl;
    public ArrayList<Calendario> c = new ArrayList<>();
    JFrame vdxh;
    String[] Meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};

    public Agenda() {
    }

    public Agenda(String paciente, String npaciente) {
        this.paciente = paciente;
        this.npaciente = npaciente;
        this.addMouseListener(new MouseCalendario(this));
        indicador = 1;
    }
    
    public Agenda(String paciente, String npaciente, String fecha, VentanaAgenda Vag) {
        this.paciente = paciente;
        this.npaciente = npaciente;
        this.Vag = Vag;
        this.fecha = fecha;
        this.addMouseListener(new MouseCalendario(this));
        indicador = 1;
        ventana = 1;
    }
    public Agenda(String fecha, VentanaNulos Vnl) {
        this.Vnl = Vnl;
        this.fecha = fecha;
        this.addMouseListener(new MouseCalendario(this));
        indicador = 1;
        ventana = 2;
    }

    public void actualizar() {
        this.repaint();
    }

    private static void pausar() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException ex) {

        }
    }

    @Override
    public void paint(Graphics g) {
        System.out.println("PPPPPPPPP");
        super.paint(g);
        System.out.println("PAINT>");
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, ancho, alto);

        g2d.setColor(Color.darkGray);

        if (indicador == 1) {
            pintarDiasDelCalendario(g2d);
        } else if (indicador == 2) {
            pintarMesesDelCalendario(g2d);
        } else if (indicador == 3) {
            pintarAniosDelCalendario(g2d);
        }
        pintarEncabezado(g2d);

    }

//    public static void main(String[] args) {
//        JFrame ventanaAgenda = new JFrame("Calendario");
//        Agenda ap = new Agenda();
//        ventanaAgenda.setSize(ap.ancho, ap.alto);
//        ventanaAgenda.add(ap);
//        ventanaAgenda.setVisible(true);
//        ventanaAgenda.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        ventanaAgenda.setLocationRelativeTo(null);
//        ap.addMouseListener(new MouseCalendario(ap, ventanaAgenda));
//        while (true) {
//            ap.repaint();
//            pausar();
//        }
//    }
    private void pintarDiasDelCalendario(Graphics2D g) {
        g.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, tletra));
        System.out.println("***pintarDiasDelCalendario***"+fecha);
        ArrayList<String[]> dias = cp.getDiasCalendario(fecha, 0);
        System.out.println("dias--->"+dias.size());
        int x = 20, y = 80;
        c.clear();
        g.setColor(new Color(21, 67, 96));

        x = 20;
        for (int i = 0; i < cp.diasSemana.length; i++) {
            g.drawRect(x, y, anchoDia, altoDia);
            g.drawString(cp.diasSemana[i], x + anchoDia / 2 - 11, y + altoDia / 2 + 11);
            x += anchoDia;
        }
//
        y += altoDia;
        x = 20;

        for (int i = 0; i < dias.size(); i++) {
            if (i % 7 == 0 && i != 0) {
                y += altoDia;
                x = 20;
            }
            c.add(new Calendario(dias.get(i)[0], dias.get(i)[1], dias.get(i)[2], anchoDia, altoDia, x, y));
            g.drawRect(x, y, anchoDia, altoDia);
            if (dias.get(i)[3].equals("S")) {
                fecha = dias.get(i)[0] + "::" + dias.get(i)[1] + "::" + dias.get(i)[2];
                g.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, tletra));
                g.setColor(Color.red);
            } else {
                g.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, tletra));
                g.setColor(new Color(21, 67, 96));
            }

            g.drawString(dias.get(i)[2], x + anchoDia / 2 - 11, y + altoDia / 2 + 11);
            x += anchoDia;
            g.setColor(Color.black);

        }
    }

    private void pintarMesesDelCalendario(Graphics2D g) {
        g.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, tletra));
        ArrayList<String[]> meses = cp.getMesesCalendario(fecha, 0);

        int x = 20, y = 80;
        g.setColor(new Color(21, 67, 96));
        c.clear();
        x = 20;

        for (int i = 0; i < meses.size(); i++) {
            if (i % 4 == 0 && i != 0) {
                y += altoMes;
                x = 20;
            }
            c.add(new Calendario(meses.get(i)[0], meses.get(i)[1], meses.get(i)[2], anchoMes, altoMes, x, y));
            g.drawRect(x, y, anchoMes, altoMes);
            if (meses.get(i)[3].equals("S")) {
                fecha = meses.get(i)[0] + "::" + meses.get(i)[1];
                g.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, tletra));
                g.setColor(Color.red);
            } else {
                g.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, tletra));
                g.setColor(new Color(21, 67, 96));
            }
            g.drawString(meses.get(i)[1].substring(0, 3).toUpperCase() + ".", x + anchoMes / 2 - 15, y + altoMes / 2 + 11);
            x += anchoMes;
            g.setColor(Color.black);

        }
    }

    private void pintarAniosDelCalendario(Graphics2D g) {
        g.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, tletra));
        ArrayList<String[]> anios = cp.getAniosCalendario(fecha, 0);

        int x = 20, y = 80;
        c.clear();
        g.setColor(new Color(21, 67, 96));

        x = 20;

        for (int i = 0; i < anios.size(); i++) {
            if (i % 4 == 0 && i != 0) {
                y += altoMes;
                x = 20;
            }
            c.add(new Calendario(anios.get(i)[0], anios.get(i)[1], anios.get(i)[2], anchoMes, altoMes, x, y));
            g.drawRect(x, y, anchoMes, altoMes);
            if (anios.get(i)[3].equals("S")) {
                fecha = anios.get(i)[0];
                g.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, tletra));
                g.setColor(Color.red);
            } else {
                g.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, tletra));
                g.setColor(new Color(21, 67, 96));
            }

            g.drawString(anios.get(i)[0], x + anchoMes / 2 - 15, y + altoMes / 2 + 11);
            x += anchoMes;
            g.setColor(Color.black);

        }
    }

    private void pintarEncabezado(Graphics2D g) {
        g.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, tletra));
        //       ArrayList<String[]> dias = cp.getDiasDelCalendario(Calendar.getInstance(), 0);

        int x = 20, y = 20;
        int h = 0, b = 0;
        g.setColor(new Color(21, 67, 96));
        g.fillPolygon(new int[]{x + 20, x + anchoEnc - 20, x + anchoEnc - 20}, new int[]{y + anchoEnc / 2, y + 20, y + anchoEnc - 20}, 3);
        g.drawRect(x, 20, anchoEnc, anchoEnc);
        x += anchoEnc;
        String Enc = getEncabezado();
        System.out.println("Enc-->" + Enc.length());
        int EncLen = Enc.length() * 11;
        int cal1 = (608 / 2), cal2 = (EncLen) / 2;
        g.drawString(Enc, x + cal1 - cal2, y + 40);

        g.drawRect(x, 20, 608, anchoEnc);
        
        x += 608;
        g.fillPolygon(new int[]{x + 20, x + anchoEnc - 20, x + 20}, new int[]{y + 20, y + anchoEnc / 2, y + anchoEnc - 20}, 3);
        g.drawRect(x, 20, anchoEnc, anchoEnc);

    }

    private String getEncabezado() {
        String ret = "";
        System.out.println("ENCABEZADO AGENDA");
        System.out.println("fecha-->"+fecha);
        if (indicador == 1) {
            ret = fecha.split("::")[1].toUpperCase() + " DEL " + fecha.split("::")[0];
        } else if (indicador == 2) {
            ret = fecha.split("::")[0];
        } else if (indicador == 3) {
            System.out.println("fecha ENc--" + fecha);
            int Anio = Integer.parseInt(fecha);
            int modulo = Integer.parseInt(fecha) % 10;
            int anioIni = Anio - modulo;
            int anioFin = anioIni + 9;
            ret = anioIni + " - " + anioFin;
        }
        return ret;
    }

    public String getIdMes(String mes) {
        String ret = "";
        for (int i = 0; i < Meses.length; i++) {
            if (Meses[i].equalsIgnoreCase(mes)) {
                ret = "" + (i + 1);
                break;
            }
        }
        return ret;
    }
}

