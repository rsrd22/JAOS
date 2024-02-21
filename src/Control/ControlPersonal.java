/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;

import BaseDeDatos.gestorMySQL;
import Modelo.Persona;
import Modelo.Personal;
import Utilidades.Utilidades;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 *
 * @author richard
 */
public class ControlPersonal {
    private gestorMySQL gsql;
    private Persona persona;
    private ControlPersonas controlPersona = new ControlPersonas();
    private Personal personal; 

    public ControlPersonal(Personal persona) {
        gsql = new gestorMySQL();
        this.personal = personal;
    }

    public ControlPersonal() {
        gsql = new gestorMySQL();
        personal = null;
    }
    
    public String setPersonal(String id, String tid, String pn, String sn, String pa, String sa, String fnac, String sxo, String dir, String tel, String cel, String cor, String tit, String car, String sal){
        try{
            
            boolean persona = controlPersona.setPersona(id, tid, pn, sn, pa, sa, fnac, sxo, dir, tel, cel, cor);
            ArrayList<String> consultas = new ArrayList<String>();
            String pkPersonal = tid+id;
            if(persona){
                if(gsql.ExistenDatos("SELECT * FROM `personal` WHERE `pk_personal` = '"+pkPersonal+"'")){
                    consultas.add("UPDATE `personal`\n" +
                                    "SET `titulo` = '"+tit+"',`cargo` = '"+car+"',`salario` = '"+sal+"'\n" +
                                    "WHERE `pk_personal` = '"+pkPersonal+"';");
                }else{
                    consultas.add("INSERT INTO `personal`\n" +
                                    "(`pk_personal`,`titulo`,`cargo`,`salario`,`fecha_de_ingreso`)\n" +
                                    "VALUES \n" +
                                    "('"+pkPersonal+"','"+tit+"','"+car+"','"+sal+"',NOW());");
                }
            }
            
            if(consultas.size()>0){
                if(gsql.EnviarConsultas(consultas)){
                    JOptionPane.showMessageDialog(null,"La operación se realizó con exito\n");
                }
            }else{
                JOptionPane.showMessageDialog(null,"No se encontraron consultas para realizar la operación\n");
            }
            return "";
        }catch(Exception e){
            System.out.println("ERROR setPersonal-->"+e.toString());
            JOptionPane.showMessageDialog(null,
                    "Ocurrio un error al tratar de obtener los datos\n"
                    + "______________________________________________________\n"
                    + "Detalles:\n" + e.getMessage());
            return "";
        }
    }
       
    public String setPersonal(Map<String, String> campos){
        try{
            ArrayList<String> nombres = Utilidades.decodificarNombre(campos.get("noms"));
            ArrayList<String> apellidos = Utilidades.decodificarNombre(campos.get("apes"));
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
            
            boolean persona = controlPersona.setPersonaN(campos.get("id"), campos.get("tid"), pn, sn, pa, sa, campos.get("fnac"), campos.get("sxo"), campos.get("dir"), campos.get("tel"), campos.get("cor"));
            ArrayList<String> consultas = new ArrayList<String>();
            String pkPersonal = campos.get("tid")+campos.get("id");
            if(persona){
                if(gsql.ExistenDatos("SELECT * FROM `personal` WHERE `pk_personal` = '"+pkPersonal+"'")){
                    consultas.add("UPDATE `personal`\n" +
                                    "SET `titulo` = '"+campos.get("tit")+"',`cargo` = '"+campos.get("car")+"', \n"+
                                    "`salario` = '"+campos.get("sal")+"', `fecha_de_ingreso` = '"+campos.get("fvin")+"'\n" +  
                                    "WHERE `pk_personal` = '"+pkPersonal+"';");
                }else{
                    consultas.add("INSERT INTO `personal`\n" +
                                    "(`pk_personal`,`titulo`,`cargo`,`salario`,`fecha_de_ingreso`)\n" +
                                    "VALUES \n" +
                                    "('"+pkPersonal+"','"+campos.get("tit")+"','"+campos.get("car")+"','"+campos.get("sal")+"','"+campos.get("fvin")+"');");
                }
            }
            
            if(consultas.size()>0){
                if(gsql.EnviarConsultas(consultas)){
                    JOptionPane.showMessageDialog(null,"La operación se realizó con exito\n");
                }
            }else{
                JOptionPane.showMessageDialog(null,"No se encontraron consultas para realizar la operación\n");
            }
            return "";
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("ERROR setPersonal-->"+e.toString());
            JOptionPane.showMessageDialog(null,
                    "Ocurrio un error al tratar de obtener los datos\n"
                    + "______________________________________________________\n"
                    + "Detalles:\n" + e.getMessage());
            return "";
        }
    }

    public String delPersonal(String id, String tid){
        try{
            String pkPersonal = tid+id;
            ArrayList<String> consultas = new ArrayList<String>();
            consultas.add("DELETE FROM `personal` WHERE `pk_personal` = '"+pkPersonal+"';");
                
            if(consultas.size()>0){
                if(gsql.EnviarConsultas(consultas)){
                    JOptionPane.showMessageDialog(null,"La operación se realizó con exito\n");
                }
            }else{
                JOptionPane.showMessageDialog(null,"No se encontraron consultas para realizar la operación\n");
            }
            return "";
        }catch(Exception e){
            System.out.println("ERROR setPersonal-->"+e.toString());
            JOptionPane.showMessageDialog(null,
                    "Ocurrio un error al tratar de obtener los datos\n"
                    + "______________________________________________________\n"
                    + "Detalles:\n" + e.getMessage());
            return "";
        }
    }
    
    public ArrayList<String[]> getPersonal(String id, String tid) {
        try {
            ArrayList<String[]> Info = new ArrayList<>();
            
            String sql = "SELECT CONCAT_WS(' ',IFNULL(per.primer_nombre, ''), IFNULL(per.segundo_nombre, '')) N2, \n" +
                            "CONCAT_WS(' ',IFNULL(per.primer_apellido, ''),IFNULL(per.segundo_apellido, '') )A2, IFNULL(per.direccion, '') DIR, IF(IFNULL(per.sexo, 'M')='M', 'Masculino', 'Femenino') SXO,\n" +
                            "IF(per.fecha_de_nacimiento IS NULL, '', DATE_FORMAT(per.fecha_de_nacimiento,'%d/%m/%Y')) FNAC, IFNULL(per.correo_electronico, '') COR, REPLACE(GROUP_CONCAT(IFNULL(tel.numero, '')), ',', '<>') TELEFONO,\n" +
                            "IFNULL(emp.titulo, '') TITL, IFNULL(emp.cargo, 'Seleccionar') CARG, IFNULL(emp.salario, '') SLRIO, DATE_FORMAT(emp.fecha_de_ingreso,'%d/%m/%Y')\n" +
                            "FROM `personal` emp\n" +
                            "INNER JOIN `personas` per ON CONCAT(per.pfk_tipo_documento, per.pk_persona) = emp.pk_personal\n" +
                            "LEFT JOIN `personas_telefonos` tel ON tel.pfk_persona = emp.pk_personal \n" +
                            "WHERE emp.pk_personal = '"+tid+id+"'\n"+
                            "GROUP BY emp.pk_personal";
            Info = gsql.SELECT(sql);
            return Info;
        } catch (Exception e) {
            return null;
        }
    }

    
}
