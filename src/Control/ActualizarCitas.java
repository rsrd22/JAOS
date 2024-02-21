/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Control;

import Vistas.VentanaPrincipal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;

/**
 *
 * @author AT-DESARROLLO
 */
public class ActualizarCitas extends Thread implements Runnable{
    public VentanaPrincipal vprin;
    public boolean terminar=true;
    public ActualizarCitas(VentanaPrincipal vprin){
        this.vprin = vprin;
    }
    
    @Override
    public void run(){
        System.out.println("antes del wh terminar = "+terminar);
        while(terminar){
            try {
                vprin.cita.CitasEnEspera();
                vprin.cargarAgendaDiaria();
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(ActualizarCitas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("termine");
    }
    
    public void terminar(){
        System.out.println("terminar = "+terminar);
        terminar = false;
    }
    
    
    
}
