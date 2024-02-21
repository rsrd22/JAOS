/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Control;

import BaseDeDatos.gestorMySQL;
import Modelo.Concepto;
import java.util.ArrayList;

/**
 *
 * @author 10
 */
public class ControlConcepto {
    public gestorMySQL gsql;
    public Concepto concepto;
    
public ControlConcepto (Concepto concepto){
    gsql = new gestorMySQL();
    this.concepto = concepto;
}

public ControlConcepto (){
    gsql = new gestorMySQL();
    this.concepto = concepto;
}

public ArrayList<String[]> getConcepto(String concepto){
       String consulta  = "";
       
       ArrayList<String[]>lista =gsql.SELECT(consulta);
       return lista;
}

}
