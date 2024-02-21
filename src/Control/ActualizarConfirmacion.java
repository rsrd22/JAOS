/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;

import Vistas.VentanaConfirmacion;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Acer
 */
public class ActualizarConfirmacion extends Thread implements Runnable{
    public boolean terminar=true;
    public VentanaConfirmacion vconf;
    public ActualizarConfirmacion(VentanaConfirmacion vconf){
        this.vconf = vconf;
    }
    
    @Override
    public void run(){
        while(terminar){
            try {
                vconf.CargarTabla();
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ActualizarCitas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void terminar(){
        terminar = false;
    }
}
