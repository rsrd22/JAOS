/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;

import BaseDeDatos.gestorMySQL;
import Modelo.Persona;
import java.util.ArrayList;

/**
 *
 * @author richard
 */
public class ControlPersonas {
    private gestorMySQL gsql;
    private Persona persona;

    public ControlPersonas(Persona persona) {
        gsql = new gestorMySQL();
        this.persona = persona;
    }

    public ControlPersonas() {
        gsql = new gestorMySQL();
        persona = null;
    }
    
    public boolean setPersona(String id, String tid, String pn, String sn, String pa, String sa, String fnac, String sxo, String dir, String tel, String cel, String cor){
        try{
            ArrayList<String> consultas = new ArrayList<String>();
            
            if(gsql.ExistenDatos("SELECT * FROM `personas` p WHERE p.`pk_persona` = '"+id+"' AND p.`pfk_tipo_documento` = '"+tid+"'")){//UPDATE
                consultas.add("UPDATE `personas`\n" +
                                "SET `primer_nombre` = '"+pn+"', `segundo_nombre` = '"+sn+"', `primer_apellido` = '"+pa+"', `segundo_apellido` = '"+pa+"',\n" +
                                "`direccion` = '"+dir+"', `fecha_de_nacimiento` = '"+fnac+"', `sexo` = '"+sxo.charAt(0)+"', `correo_electronico` = '"+cor+"'\n" +
                                "WHERE `pk_persona` = '"+id+"' AND `pfk_tipo_documento` = '"+tid+"';");
            }else{//INSERT
                consultas.add("INSERT INTO `personas`\n" +
                                "(`pk_persona`,`pfk_tipo_documento`,`primer_nombre`,`segundo_nombre`,`primer_apellido`,\n" +
                                "`segundo_apellido`,`direccion`,`fecha_de_nacimiento`,`sexo`,`correo_electronico`)\n" +
                                "VALUES \n" +
                                "('"+id+"','"+tid+"','"+pn+"','"+sn+"','"+pa+"',\n" +
                                "'"+sa+"','"+dir+"','"+fnac+"','"+sxo.charAt(0)+"','"+cor+"');");
            }
            if(!tel.equals("")){
                if(gsql.ExistenDatos("SELECT * FROM `personas_telefonos` WHERE pfk_persona = '"+tid+id+"' AND tipo = 'FIJO'")){
                    consultas.add("UPDATE `personas_telefonos`\n" +
                                    "SET `indicativo` = '0', `numero` = '"+tel+"'\n" +
                                    "WHERE `pfk_persona` = '"+tid+id+"' AND `tipo` = 'FIJO';");
                }else{
                    consultas.add("INSERT INTO `personas_telefonos`\n" +
                                    "(`pfk_persona`,`tipo`,`indicativo`,`numero`)\n" +
                                    "VALUES \n" +
                                    "('"+tid+id+"','FIJO','0','"+tel+"');");
                }
            }
            if(!cel.equals("")){
                if(gsql.ExistenDatos("SELECT * FROM `personas_telefonos` WHERE pfk_persona = '"+tid+id+"' AND tipo = 'CELULAR'")){
                    consultas.add("UPDATE `personas_telefonos`\n" +
                                    "SET `indicativo` = '0', `numero` = '"+cel+"'\n" +
                                    "WHERE `pfk_persona` = '"+tid+id+"' AND `tipo` = 'CELULAR';");
                }else{
                    consultas.add("INSERT INTO `personas_telefonos`\n" +
                                    "(`pfk_persona`,`tipo`,`indicativo`,`numero`)\n" +
                                    "VALUES \n" +
                                    "('"+tid+id+"','CELULAR','0','"+cel+"');");
                }
            }
            
            
            
            
            if(gsql.EnviarConsultas(consultas)){
                return true;
            }
            return false;
        }catch(Exception e){
            return false;
        }
    }

    public boolean setPersonaN(String id, String tid, String pn, String sn, String pa, String sa, String fnac, String sxo, String dir, String tels, String cor){
        try{            
            ArrayList<String> consultas = new ArrayList<String>();
            
            if(gsql.ExistenDatos("SELECT * FROM `personas` p WHERE p.`pk_persona` = '"+id+"' AND p.`pfk_tipo_documento` = '"+tid+"'")){//UPDATE
                consultas.add("UPDATE `personas`\n" +
                                "SET `primer_nombre` = '"+pn+"', `segundo_nombre` = '"+sn+"', `primer_apellido` = '"+pa+"', `segundo_apellido` = '"+sa+"',\n" +
                                "`direccion` = '"+dir+"', `fecha_de_nacimiento` = '"+fnac+"', `sexo` = "+((""+sxo.charAt(0)).equals("S")?"NULL":"'"+sxo.charAt(0)+"'")+", `correo_electronico` = '"+cor+"'\n" +
                                "WHERE `pk_persona` = '"+id+"' AND `pfk_tipo_documento` = '"+tid+"';");
            }else{//INSERT
                consultas.add("INSERT INTO `personas`\n" +
                                "(`pk_persona`,`pfk_tipo_documento`,`primer_nombre`,`segundo_nombre`,`primer_apellido`,\n" +
                                "`segundo_apellido`,`direccion`,`fecha_de_nacimiento`,`sexo`,`correo_electronico`)\n" +
                                "VALUES \n" +
                                "('"+id+"','"+tid+"','"+pn+"','"+sn+"','"+pa+"',\n" +
                                "'"+sa+"','"+dir+"','"+fnac+"', "+((""+sxo.charAt(0)).equals("S")?"NULL":"'"+sxo.charAt(0)+"'")+",'"+cor+"');");
            } 
            
            if(gsql.ExistenDatos("SELECT * FROM `personas_telefonos` p WHERE p.pfk_persona = '"+tid+id+"'")){//UPDATE
                consultas.add("DELETE FROM `personas_telefonos` WHERE pfk_persona = '"+tid+id+"'");
            }            
            if(!tels.equals("")){ 
                String[] tel = tels.split("<>");
                String tpo = "";
                for(int i = 0; i < tel.length; i++){
                    if(tel[i].length()==7){
                        tpo = "FIJO";
                    }else if(tel[i].length()==10){
                        tpo = "CELULAR";
                    }
                    consultas.add("INSERT INTO `personas_telefonos`\n" +
                                    "(`pfk_persona`,`tipo`,`indicativo`,`numero`)\n" +
                                    "VALUES \n" +
                                    "('"+tid+id+"','"+tpo+"','0','"+tel[i]+"');");
                }
            }
            if(gsql.EnviarConsultas(consultas)){
                return true;
            }
            return false;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
    
    public ArrayList<String[]> getPersonas() {
        return gsql.SELECT("SELECT CONCAT(TRIM(primer_nombre),' ',TRIM(segundo_nombre),' ',TRIM(primer_apellido),' ',TRIM(segundo_apellido)),"
                + "direccion,DATE_FORMAT(fecha_de_nacimiento, '%d/%m/%Y'),CASE WHEN sexo = 'F' THEN 'FEMENINO' ELSE 'MASCULINO' END,"
                + "correo_electronico FROM personas");
    }
}
