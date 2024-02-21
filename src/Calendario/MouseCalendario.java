/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Calendario;

/**
 *
 * @author richard
 */
import Modelo.Calendario;
import Vistas.VentanaDiasNulos;
import Vistas.ventanaDiasxHoras;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;

public class MouseCalendario implements MouseListener{

    public Agenda ag;

    public MouseCalendario(Agenda ag) {
        this.ag = ag;
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        ag.actualizar();
    }

    @Override
    public void mousePressed(MouseEvent e) {  
        String cambioEnc = notificarCambioEnc(e.getPoint());
        if(!cambioEnc.equals("")){
            if(cambioEnc.equals("0")){//Ant
                System.out.println("ANTERIOR");
                String fecha = ag.fecha;
                if(ag.indicador==1){                    
                    String a  = fecha.split("::")[0]; 
                    String m  = fecha.split("::")[1];
                    String d  = fecha.split("::")[2];
                    String idm = ag.getIdMes(m);
                    String fecAux = "";
                    
                    if(idm.equals("1")){
                        fecAux=(Integer.parseInt(a)-1)+"::"+ag.Meses[ag.Meses.length-1]+"::1";
                    }else{
                        fecAux=a+"::"+ag.Meses[Integer.parseInt(idm)-2]+"::1";
                    }
                    ag.fecha = fecAux;                    
                }else if(ag.indicador==2){
                    String a  = fecha.split("::")[0]; 
                    String m  = fecha.split("::")[1];
                    String fecAux=(Integer.parseInt(a)-1)+"::"+m;
                    ag.fecha = fecAux; 
                }else{
                    String a  = fecha.split("::")[0]; 
                    String fecAux=""+(Integer.parseInt(a)-10);
                    ag.fecha = fecAux; 
                }
            }else if(cambioEnc.equals("1")){//ENC
                if(ag.indicador==1){
                    ag.indicador=2;
                }else if(ag.indicador==2){
                    ag.indicador=3;
                }
            }else{//Sig
                System.out.println("SIGUIENTE");
                String fecha = ag.fecha;
                if(ag.indicador==1){                    
                    String a  = fecha.split("::")[0]; 
                    String m  = fecha.split("::")[1];
                    String d  = fecha.split("::")[2];
                    String idm = ag.getIdMes(m);
                    String fecAux = "";
                    
                    if(idm.equals("12")){
                        fecAux=(Integer.parseInt(a)+1)+"::"+ag.Meses[0]+"::1";
                    }else{
                        fecAux=a+"::"+ag.Meses[Integer.parseInt(idm)]+"::1";
                    }
                    ag.fecha = fecAux;                    
                }else if(ag.indicador==2){
                    String a  = fecha.split("::")[0]; 
                    String m  = fecha.split("::")[1];
                    
                    String fecAux=(Integer.parseInt(a)+1)+"::"+m;
                    ag.fecha = fecAux; 
                }else{
                    String a  = fecha.split("::")[0]; 
                    String fecAux=""+(Integer.parseInt(a)+10);
                    ag.fecha = fecAux; 
                }
            }            
        }else{
            for (int i = 0; i < ag.c.size(); i++) {
                if (notificarCambio(e.getPoint(), ag.c.get(i))) {
                    if(ag.indicador == 1){//Abró vista de agenda por hora
                        if(ag.ventana == 1){
                            ag.Vag.dispose();
                            new ventanaDiasxHoras(ag.paciente, ag.npaciente, ag.c.get(i).anio, ag.c.get(i).mes, ag.c.get(i).dia).setVisible(true);
                        }else if(ag.ventana == 2){
                            ag.Vnl.dispose();
                            new VentanaDiasNulos(ag.c.get(i).anio, ag.c.get(i).mes, ag.c.get(i).dia).setVisible(true);
                        }
                    }else if(ag.indicador == 2){
                        ag.indicador = 1;                        
                        ag.fecha= ag.c.get(i).anio+"::"+ag.c.get(i).mes;
                    }else{
                        ag.indicador = 2;
                        ag.fecha= ag.c.get(i).anio;
                    }
                    break;
                }
            }
        }
        ag.actualizar();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        ag.actualizar();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        ag.actualizar();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        ag.actualizar();
    }

    private boolean notificarCambio(Point p, Calendario c) {
        if(p.x > c.x && p.x < c.x +c.ancho && p.y> c.y && p.y < c.y+c.alto){
            return true;
        }else{
            return false;
        }
    }

    private String notificarCambioEnc(Point p) {
        String ret = "";
        if(p.x>20 && p.x<80 && p.y >20 && p.y<80){
            ret = "0";
        }else if(p.x>80 && p.x<(722-60) && p.y >20 && p.y<80){
            ret = "1";
        }else if(p.x>(722-60) && p.x<722 && p.y >20 && p.y<80){
            ret = "2";
        }
        return ret;
    }
    
}
