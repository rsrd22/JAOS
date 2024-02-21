/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Control;

import BaseDeDatos.gestorMySQL;
import Utilidades.Utilidades;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 *
 * @author AT-DESARROLLO
 */
public class ControlPacAux {
    private gestorMySQL gsql;
    
    public ControlPacAux() {
        gsql = new gestorMySQL();
    }
    
    public String setPacAuxiliar(Map<String, String> campos, ArrayList<String[]> listaCot) {
        try{            
            System.out.println("campos.get(\"noms\")-->"+campos.get("noms"));
            System.out.println("campos.get(\"apes\")-->"+campos.get("apes"));
            ArrayList<String> nombres = Utilidades.decodificarNombre(campos.get("noms").toUpperCase());
            ArrayList<String> apellidos = Utilidades.decodificarNombre(campos.get("apes").toUpperCase());
            String pn = "", sn =  "", pa = "", sa = "";
            
            if (nombres.size() >= 2) { 
                pn = nombres.get(0);
                sn = nombres.get(1);  
            } else {
                pn = nombres.get(0);
                sn = "";
            }
              
            if (apellidos.size() >= 2) { 
                pa = apellidos.get(0);
                sa = apellidos.get(1);  
            } else {
                pa = apellidos.get(0);
                sa = "";
            }
            ArrayList<String> consultas = new ArrayList<String>();
            String id = "";
            if(campos.get("id").equals("")){//NUEVO
                id = gsql.SELECT("SELECT IFNULL(MAX(CAST(`pk_paciente_auxiliar` AS UNSIGNED)), 0)+1 FROM `paciente_auxiliar`").get(0)[0];
                consultas.add("INSERT INTO `paciente_auxiliar`\n" +
                                "(`pk_paciente_auxiliar`,`primer_nombre`,`segundo_nombre`,`primer_apellido`,\n" +
                                "`segundo_apellido`,`telefono`,`email`,`estado`,`tipo`)\n" +
                                "VALUES \n" +
                                "('"+id+"','"+pn+"','"+sn+"','"+pa+"',\n" +
                                "'"+sa+"','"+campos.get("tel")+"','"+campos.get("cor")+"','"+campos.get("est")+"','"+campos.get("tpo").charAt(0)+"');");
            }else{//UPDATE
                id = campos.get("id");
                consultas.add("UPDATE `paciente_auxiliar`\n" +
                                "SET `primer_nombre` = '"+pn+"', `segundo_nombre` = '"+sn+"', `primer_apellido` = '"+pa+"',\n" +
                                "`segundo_apellido` = '"+sa+"', `telefono` = '"+campos.get("tel")+"', `email` = '"+campos.get("cor")+"',\n" +
                                "`estado` = '"+campos.get("est")+"', `tipo` = '"+campos.get("tpo").charAt(0)+"'\n" +
                                "WHERE `pk_paciente_auxiliar` = '"+id+"';");
            }
            System.out.println("listaa---<"+listaCot.size());
            for(String[] lst: listaCot){
                System.out.println("******************lst[4]*****************"+lst[4]);
                if(lst[4].equals("0")){//INSERT
                    String cons = gsql.SELECT("SELECT IFNULL(MAX(CAST(p.`pk_consecutivo` AS UNSIGNED)), 0)+1 FROM `pacientexcotizaciones` p WHERE p.pfk_paciente = '"+id+"' AND p.`pfk_tratamiento` = '"+lst[0]+"'").get(0)[0];
                    consultas.add("INSERT INTO `pacientexcotizaciones`\n" +
                                    "(`pk_consecutivo`,`pfk_paciente`,`pfk_tratamiento`,\n" +
                                    "`cuota_inicial`, diferidas_en,`cuota`,`costo`)\n" +  
                                    "VALUES \n" +
                                    "('"+cons+"','"+id+"','"+lst[0]+"',\n" +
                                    "'"+lst[2]+"', '"+lst[6]+"','"+lst[3]+"','"+lst[1]+"');");
                }else{//UPDATE
                    consultas.add("UPDATE `pacientexcotizaciones`\n" +
                                    "SET `cuota_inicial` = '"+lst[2]+"', `cuota` = '"+lst[3]+"', `costo` = '"+lst[1]+"', diferidas_en = '"+lst[6]+"' \n" +
                                    "WHERE `pk_consecutivo` = '"+lst[5]+"' \n" +
                                    "AND `pfk_paciente` = '"+id+"' AND `pfk_tratamiento` = '"+lst[0]+"';");
                }                    
            }
            
            System.out.println("***********consultas***************");
            for(int i = 0; i< consultas.size(); i++){
                System.out.println(i+"---"+consultas.get(i));
            }
            String ret  = "";
            if(consultas.size()>0){
                if(gsql.EnviarConsultas(consultas)){
                    System.out.println("HOLAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAa");
                    JOptionPane.showMessageDialog(null,"La operación se realizó con exito\n");
                    ret = "1";
                }
            }else{
                JOptionPane.showMessageDialog(null,"No se encontraron consultas para realizar la operación\n");
                ret = "0";
            }
            return ret;
        }catch(Exception e){
            System.out.println("ERROR setPacAuxiliar-->"+e.toString());
            JOptionPane.showMessageDialog(null,
                    "Ocurrio un error al tratar de obtener los datos\n"
                    + "______________________________________________________\n"
                    + "Detalles:\n" + e.getMessage());
            return "0";
        }
    }
    
     public ArrayList<String[]> getPacAux(String id) {
        try{
            ArrayList<String[]> Info = new ArrayList<>();
            
            String sql = "";
            
            sql = "SELECT IFNULL(paux.pk_paciente_auxiliar, '') IDENTIFICACION, \n" +
                "CONCAT_WS( ' ', paux.primer_nombre, IFNULL(paux.segundo_nombre, '')) NOM, \n" +
                "CONCAT_WS( ' ', paux.primer_apellido, IFNULL(paux.segundo_apellido, '')) APE, IFNULL(paux.email, '') EMAIL,\n" +
                "REPLACE(IFNULL(paux.telefono, ''), '-', '<>') TELEFONO, IFNULL(paux.tipo, 'A') TIPO, paux.estado EST\n" +
                "FROM  `paciente_auxiliar` paux \n" +
                "WHERE paux.`pk_paciente_auxiliar` = '"+id+"'";
            
            Info = gsql.SELECT(sql);
            return Info;
        }catch(Exception e){
            return null;
        }
    }
     
     public ArrayList<String[]> getPacienteCotizaciones(String id) {
        try{
            ArrayList<String[]> Info = new ArrayList<>();
            
            String sql = "";
            
            sql = "SELECT ctz.`pfk_tratamiento`, trat.`descripcion`, ctz.`costo`, ctz.`cuota_inicial`, ctz.diferidas_en, ctz.`cuota`, '1', ctz.`pk_consecutivo` \n" +
                    "FROM `pacientexcotizaciones` ctz \n" +
                    "INNER JOIN `tratamientos` trat ON trat.`pk_tratamiento` = ctz.`pfk_tratamiento`\n" +
                    "WHERE ctz.`pfk_paciente` = '"+id+"'";
            
            Info = gsql.SELECT(sql);
            return Info;
        }catch(Exception e){
            return null;
        }
    }

    public String delPacienteAux(String id){
        try{
            ArrayList<String> consultas = new ArrayList<String>();
            
//            if(gsql.ExistenDatos("")){  
//                JOptionPane.showMessageDialog(null,"El paciente\n");
//                return "";
//            }
            consultas.add("DELETE FROM `paciente_auxiliar` WHERE `pk_paciente_auxiliar`= '"+id+"'");
            //consultas.add("DELETE FROM `paciente` WHERE `pfk_paciente` = '"+pkPaciente+"';");
                
            if(consultas.size()>0){  
                if(gsql.EnviarConsultas(consultas)){
                    JOptionPane.showMessageDialog(null,"La operación se realizó con exito\n");
                }
            }else{
                JOptionPane.showMessageDialog(null,"No se encontraron consultas para realizar la operación\n");
            }
            return "";
        }catch(Exception e){
            System.out.println("ERROR delPacienteAux-->"+e.toString());
            JOptionPane.showMessageDialog(null,
                    "Ocurrio un error al tratar de obtener los datos\n"
                    + "______________________________________________________\n"
                    + "Detalles:\n" + e.getMessage());
            return "";
        }
    }
}
