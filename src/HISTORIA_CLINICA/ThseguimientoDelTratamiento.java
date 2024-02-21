/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HISTORIA_CLINICA;

import BaseDeDatos.gestorMySQL;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author mary
 */
public class ThseguimientoDelTratamiento implements Runnable {

    private static volatile boolean ejecutar;
    private static gestorMySQL gmsql;
    private static Thread procesoSegtrat;
    private static String tipoyDoc;
    private ArrayList<String[]> st = new ArrayList<>();
    private String tratamiento;
    private ventana ven;

    public ThseguimientoDelTratamiento(String tipoyDoc, ArrayList<String[]> segtrat, String tratamiento, ventana vent) {
        gmsql = new gestorMySQL();
        this.tipoyDoc = tipoyDoc;
        this.tratamiento = tratamiento;
        st = segtrat;
        ven = vent;
        iniciar();
    }

    public ThseguimientoDelTratamiento(ventana vent) {
        ven = vent;
        gmsql = new gestorMySQL();
        tipoyDoc = ven.cbTipoDocumento.getSelectedItem() + ven.txtDocumento.getText();

        tratamiento = ven.tratamientosxpaciente.size() > 0 ? ven.tratamientosxpaciente.get(ven.cbTratamientos.getSelectedIndex())[0] : "1";

        st = new ArrayList<>();
        iniciar();
    }

    private synchronized void iniciar() {
        ejecutar = true;
        procesoSegtrat = new Thread(this, "proceso seguimiento del tratamiento");
        procesoSegtrat.start();
    }

    public synchronized void terminar() {
        try {
            ejecutar = false;
            procesoSegtrat.join();
//            System.out.println("...::: proceso terminado :::...");
        } catch (InterruptedException ex) {
            JOptionPane.showMessageDialog(null, "terminar -> " + ex.getMessage());
        }
    }

    private void pausar() {
        try {
            Thread.sleep(650);
        } catch (InterruptedException ex) {
            JOptionPane.showMessageDialog(null, "pausar -> " + ex.getMessage());
        }
    }

    private void actualizarSeguimientoDelTratamiento() {
//        System.out.println("...::: proceso iniciado :::...");
        if (ven.detenerHilosegTrat) {
            ven.cargarTablaSegTrat(null);
        } else {
            if (ven != null) {
                if (ven.tratamientosxpaciente == null) {
                    //<editor-fold defaultstate="collapsed" desc="seguimiento del tratamiento">
                    ArrayList<String[]> segtrat = new ArrayList<>();
                    String consulta = "SELECT\n"
                            + "consecutivo,DATE_FORMAT(fecha_seguimiento,'%d/%m/%Y'),observaciones, REPLACE(FORMAT(abono, 0),',','.'), REPLACE(FORMAT(saldo, 0),',','.')\n"
                            + "FROM \n"
                            + "seguimiento_del_tratamiento\n"
                            + "WHERE\n"
                            + "pfk_paciente = '" + ven.cbTipoDocumento.getSelectedItem() + ven.txtDocumento.getText() + "'\n"
                            + "AND pfk_tratamiento = " + (ven.tratamientosxpaciente.size() > 0 ? ven.tratamientosxpaciente.get(ven.cbTratamientos.getSelectedIndex())[0] : "1") + "\n"
                            + "ORDER BY\n"
                            + "consecutivo ASC";
                    segtrat = gmsql.SELECT(consulta);
                    //</editor-fold>

                    if (segtrat.size() > 0) {
                        if (verificarModificaciones(ven.segtrat, segtrat)) {
                            copiarArray(segtrat);
//                            System.out.println("...::: proceso actualizado :::...");
                            ven.cargarTablaSegTrat(segtrat);
                        }
                    }
                }
            }
        }
    }

    private boolean verificarModificaciones(ArrayList<String[]> segtrat, ArrayList<String[]> st) {
        if (segtrat.size() != st.size()) {
            return true;
        } else {
            for (int i = 0; i < st.size(); i++) {
                for (int j = 0; j < st.get(i).length - 1; j++) {
                    if (!st.get(i)[j].equalsIgnoreCase(segtrat.get(i)[j])) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    @Override
    public void run() {
        while (ejecutar) {
            actualizarSeguimientoDelTratamiento();
            pausar();
        }
        System.out.println("proceso finalizado con exito");
    }

    private void copiarArray(ArrayList<String[]> segtratConsulta) {
        if (ven.segtrat.size() < segtratConsulta.size()) {
            ven.segtrat = new ArrayList<>();
            for (int i = 0; i < segtratConsulta.size(); i++) {
                ven.segtrat.add(new String[]{"", "", "", "", ""});
            }
        }
        for (int i = 0; i < segtratConsulta.size(); i++) {
            System.arraycopy(segtratConsulta.get(i), 0, ven.segtrat.get(i), 0, segtratConsulta.get(i).length);
        }
    }

}
