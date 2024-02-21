/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;

import BaseDeDatos.gestorMySQL;
import Utilidades.datosUsuario;
import Modelo.Cita;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JOptionPane;

/**
 *
 * @author richard
 */
public class ControlCitas {
    private gestorMySQL gsql;
    private Cita cita;
    
    
    public ControlCitas(Cita cita) {
        gsql = new gestorMySQL();
        this.cita = cita;
    }

    public ControlCitas() {
        gsql = new gestorMySQL();
        cita = null;
    }
    
    public ArrayList<String[]> getCitasPaciente(String idPaciente, String estadoch){        
        try{
            ArrayList<String[]> listaRetorno = new ArrayList<>();
            String SQL = "SELECT c.`pk_cita`, CONCAT_WS(' - ',c.`fecha_cita`, c.`hora`) , c.`estado`\n" +
                            "FROM `citas` c \n" +
                            "WHERE c.`pfk_paciente` = '"+idPaciente+"'\n" +
                            "ORDER BY pk_cita DESC ";
            //0--> cita,1-> fecha, 2 horas, 3 motivo, 4 estado, 5 accion
            SQL = "SELECT c.`pk_cita`, c.`fecha_cita`, REPLACE(GROUP_CONCAT(cxh.`hora`), ',', '<>') horas, ifnull(c.motivo, 'Sin motivo') motivo, c.`estado`, \n" +
                    "IF(estado = 'Activo', 'Cancelar', IF(c.`fecha_cita` <= DATE_FORMAT(SYSDATE(), \"%Y-%m-%d\"),'', 'Reactivar')) Accion \n"+
                    "FROM `citas` c \n" +
                    "INNER JOIN  `citasxhoras` cxh ON cxh.`pfk_paciente` = c.`pfk_paciente` AND cxh.`pfk_cita` = c.`pk_cita`\n" +
                    "WHERE c.`pfk_paciente` = '"+idPaciente+"'\n" +
                    "GROUP BY c.`pk_cita` \n" +
                    "ORDER BY CAST(c.pk_cita AS UNSIGNED) DESC ";
            
            listaRetorno = gsql.SELECT(SQL);
            return listaRetorno;
            
        }catch(Exception e){
            System.out.println("ERROR getCitasPaciente--> "+e.toString());
            return null;
        }
    }
    
    public ArrayList<Cita> getCitasSemanales(String fechaI, String fechaF){
        try{
            ArrayList<Cita> Lista = new ArrayList<Cita>();
            String SQL = "SELECT c.`pk_cita` IDCITA, c.`pfk_paciente` IDPACIENTE, \n" +
                            " IF(per.`pfk_tipo_documento` IS NULL AND per.`pk_persona` IS NULL,  \n" +
                            " CONCAT_WS(' ', paux.`primer_apellido`, IFNULL(paux.`segundo_apellido`, ''), paux.`primer_nombre`, IFNULL(paux.`segundo_nombre`, '')), \n" +
                            " CONCAT_WS(' ', per.`primer_apellido`, IFNULL(per.`segundo_apellido`, ''), per.`primer_nombre`, IFNULL(per.`segundo_nombre`, ''))) NOMPACIENTE,\n" +
                            " c.`fecha_cita` FEC_CITA, cxh.`hora` HOR_CITA, c.`estado` EST_CITA\n" +
                            "FROM citas c\n" +
                            "LEFT JOIN `citasxhoras` cxh ON cxh.`pfk_paciente` = c.`pfk_paciente` AND cxh.`pfk_cita` = c.`pk_cita`\n"+
                            "LEFT JOIN personas per ON CONCAT(per.`pfk_tipo_documento`, per.`pk_persona`) = c.`pfk_paciente`\n" +
                            "LEFT JOIN `paciente_auxiliar` paux ON paux.pk_paciente_auxiliar = c.`pfk_paciente` AND paux.estado = 'Activo'\n" +
                            "WHERE c.`fecha_cita` BETWEEN '"+fechaI+"' AND '"+fechaF+"' AND (c.`estado` <> 'Cancelada' AND c.`estado` <> 'Inactivo') \n" +
                            "ORDER BY c.`fecha_cita` ASC, cxh.`hora` ASC";
            System.out.println("sql-->"+SQL);
            ResultSet rs = gsql.Consultar(SQL);
            
            if(rs.next()){
                do{
                    Lista.add(new Cita(rs.getString("IDPACIENTE"),rs.getString("NOMPACIENTE"), rs.getString("IDCITA"), rs.getString("FEC_CITA"), rs.getString("HOR_CITA"), rs.getString("EST_CITA")));
                }while(rs.next());
            }
            return Lista;
        }catch(Exception e){
            System.out.println("Error getCitasSemanales-->"+e.toString());
            return null;
        }
    }
    
    public ArrayList<String[]> getDiasNulos(String fechaI, String fechaF){        
        try{
            System.out.println("************getDiasNulos*************");
            ArrayList<String[]> listaRetorno = new ArrayList<>();
            String SQL = "SELECT d.`fecha`, d.`hora`\n" +
                            "FROM dias_nulos d WHERE d.`fecha` BETWEEN '"+fechaI+"' AND '"+fechaF+"'\n" +
                            "ORDER BY d.`fecha` ASC";
            listaRetorno = gsql.SELECT(SQL);
            System.out.println("***************END getDiasNulos*******************");
            return listaRetorno;
            
        }catch(Exception e){
            System.out.println("ERROR getCitasPaciente--> "+e.toString());
            return null;
        }
    }
    
    public boolean ReactivarCita(String paciente, String idCita, String user){
        try{
            ArrayList<String> consultas = new ArrayList<String>();
            
            if(gsql.ExistenDatos("SELECT * FROM `pacientexconfirmacion` WHERE pfk_paciente = '"+paciente+"' AND pfk_cita = '"+idCita+"' AND estado = 'Cancelar'")){
                consultas.add("UPDATE `pacientexconfirmacion`\n" +
                            "SET `estado` = 'Confirmar' \n" +
                            "WHERE `pfk_paciente` = '"+paciente+"' AND `pfk_cita` = '"+idCita+"';");
            }
            
            consultas.add("UPDATE `citas`\n" +
                            "SET `estado` = 'Activo', `_usuario` = '"+user+"', _fecha = NOW()\n" +
                            "WHERE `pfk_paciente` = '"+paciente+"' AND `pk_cita` = '"+idCita+"';");
                
            if(consultas.size()>0){
                if(gsql.EnviarConsultas(consultas))
                    return true;
                else 
                    return false;
            }
            else 
                return false;
        }catch(Exception e){
            System.out.println("ERROR CancelarCita--> "+e.toString());
            return false;
        }
    }
    
    public boolean CancelarCita(String paciente, String idCita, String user){
        try{
            ArrayList<String> consultas = new ArrayList<String>();
            consultas.add("UPDATE `citas`\n" +
                            "SET `estado` = 'Cancelada', `_usuario` = '"+user+"', _fecha = NOW()\n" +
                            "WHERE `pfk_paciente` = '"+paciente+"' AND `pk_cita` = '"+idCita+"';");
                
            if(consultas.size()>0){
                if(gsql.EnviarConsultas(consultas))
                    return true;
                else 
                    return false;
            }
            else 
                return false;
        }catch(Exception e){
            System.out.println("ERROR CancelarCita--> "+e.toString());
            return false;
        }
    }
    
    public boolean CrearCita(String paciente, String fecha, String hora, String user){
        try{
            ArrayList<String> consultas = new ArrayList<String>();
            if(gsql.ExistenDatos("SELECT * FROM citas WHERE pfk_paciente = '"+paciente+"' AND estado = 'Activo'")){
                consultas.add("UPDATE `citas`\n" +
                            "SET `estado` = 'Cancelada', `_usuario` = '"+user+"', _fecha = NOW()\n" +
                            "WHERE `pfk_paciente` = '"+paciente+"' AND `estado` = 'Activo';");
            }
            String[] cita = gsql.SELECT("SELECT IFNULL(MAX(CAST(pk_cita AS UNSIGNED)), 0)+1 FROM `citas` WHERE pfk_paciente = '"+paciente+"'").get(0);
            consultas.add("INSERT INTO citas\n" +
                            "(`pfk_paciente`,`pk_cita`,`fecha_cita`,`hora`,\n" +
                            "`estado`,`usuario`,`fecha`)\n" +
                            "VALUES \n" +
                            "('"+paciente+"','"+cita[0]+"','"+fecha+"','"+hora+"',\n" +
                            "'Activo','"+user+"',NOW());");
                
            if(consultas.size()>0){
                if(gsql.EnviarConsultas(consultas))
                    return true;
                else 
                    return false;
            }
            else 
                return false;            
        }catch(Exception e){
            System.out.println("ERROR CrearCita-->"+e.toString());
            return true;
        }
    }
    
    public boolean CrearCita(String fecha, ArrayList<String> horas, String paciente, String Motivo, String pkcita, String user){
        try{
            System.out.println("***********************CrearCita***************************");
            boolean ret = false;
            ArrayList<String> consultas = new ArrayList<String>();
//            if(gsql.ExistenDatos("SELECT * FROM citas WHERE pfk_paciente = '"+paciente+"' AND estado = 'Activo'")){
//                consultas.add("UPDATE `citas`\n" +
//                            "SET `estado` = 'Cancelada', `_usuario` = '"+user+"', _fecha = NOW()\n" +
//                            "WHERE `pfk_paciente` = '"+paciente+"' AND `estado` = 'Activo';");
//            }
            String[] cita = gsql.SELECT("SELECT IFNULL(MAX(CAST(pk_cita AS UNSIGNED)), 0)+1 FROM `citas` WHERE pfk_paciente = '"+paciente+"'").get(0);
            
            consultas.add("INSERT INTO `citas`\n" +
                        "(`pfk_paciente`,`pk_cita`,`fecha_cita`,`estado`, motivo,`usuario`,`fecha`)\n" +
                        "VALUES \n" +
                        "('"+paciente+"','"+cita[0]+"','"+fecha+"','Activo','"+Motivo+"','"+user+"',NOW());");
            for(int i = 0; i < horas.size(); i++){
                consultas.add("INSERT INTO `citasxhoras`\n" +
                            "(`pfk_paciente`,`pfk_cita`,`hora`)\n" +
                            "VALUES \n" +
                            "('"+paciente+"','"+cita[0]+"','"+horas.get(i)+"');");
            }
            
            
            System.out.println("pkcita--->"+pkcita);
            
            
            
            if(pkcita == null){
                pkcita = "";
            }        
            
            if(!pkcita.isEmpty()){
                consultas.add("CALL actualizarAuditoria('"+paciente+"','"+pkcita+"',0, '" + datosUsuario.datos.get(0)[0] + "');");
            }
            
            //<editor-fold defaultstate="collapsed" desc="GUARDAAR TABLA DE AUDITORIA">
                consultas.add("INSERT INTO auditoria_cambio_estado VALUES("
                        + "'" +paciente+ "',"
                        + "'" + datosUsuario.datos.get(0)[0] + "',"
                        + "'" + (pkcita.isEmpty() ? "ICONO" : "AGENDA_DIARIA") + "','AGENDA',NOW())");
                //</editor-fold>
            
            for(String con: consultas){
                System.out.println("c -- "+con);
            }
            
            if(consultas.size()>0){
                if(gsql.EnviarConsultas(consultas))
                    return true;
                else 
                    return false;
            }
            else 
                return false; 
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("ERROR --CREAR CITA-- "+e.toString());
            return false;
        }
    }
    
    
    /// DEL DIA
    public ArrayList<String[]> getCitasDelDia(){        
        try{            
            ArrayList<String[]> listaRetorno = new ArrayList<>();
            Calendar cal =Calendar.getInstance();            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String fecha =   sdf.format(cal.getTime());
            String SQL = "SELECT c.`pk_cita`, IF(paux.pk_paciente_auxiliar IS NULL,'0', '1') PAUX, \n" +
                            "IF(paux.pk_paciente_auxiliar IS NULL,CONCAT_WS(' ', per.`primer_apellido`, IFNULL(per.`segundo_apellido`, ''), per.`primer_nombre`, IFNULL(per.`segundo_nombre`, '')),\n" +
                            "CONCAT_WS(' ', paux.`primer_apellido`, IFNULL(paux.`segundo_apellido`, ''), paux.`primer_nombre`, IFNULL(paux.`segundo_nombre`, ''))) PACIENTE, \n" +
                            "c.`hora` , c.`estado`\n" +
                            "FROM `citas` c\n" +
                            "LEFT JOIN `personas` per ON CONCAT(per.`pfk_tipo_documento`, per.`pk_persona`) = c.`pfk_paciente`\n" +
                            "LEFT JOIN `paciente_auxiliar` paux  ON paux.pk_paciente_auxiliar = c.`pfk_paciente`\n" +
                            "WHERE c.`fecha_cita` = '"+fecha+"' AND c.estado <> 'Cancelada' AND c.`estado` <> 'Inactivo'\n" +
                            "ORDER BY c.`hora` ASC";   
            //PK_CITA, PAUX, ID, PACIENTE, HORA, ESTADO
//            SQL = "SELECT c.`pk_cita`, IF(paux.pk_paciente_auxiliar IS NULL,'1', '2') PAUX, IF(paux.pk_paciente_auxiliar IS NULL,c.`pfk_paciente`, paux.pk_paciente_auxiliar) ID, \n" +
//                    "IF(paux.pk_paciente_auxiliar IS NULL,CONCAT_WS(' ', per.`primer_apellido`, IFNULL(per.`segundo_apellido`, ''), per.`primer_nombre`, IFNULL(per.`segundo_nombre`, '')),\n" +
//                    "CONCAT_WS(' ', paux.`primer_apellido`, IFNULL(paux.`segundo_apellido`, ''), paux.`primer_nombre`, IFNULL(paux.`segundo_nombre`, ''))) PACIENTE, \n" +
//                    "GROUP_CONCAT(cxh.`hora`) hora , c.`estado`\n" +
//                    "FROM `citas` c\n" +
//                    "INNER JOIN `citasxhoras` cxh ON cxh.`pfk_paciente` = c.`pfk_paciente` AND cxh.`pfk_cita` = c.`pk_cita`\n" +
//                    "LEFT JOIN `personas` per ON CONCAT(per.`pfk_tipo_documento`, per.`pk_persona`) = c.`pfk_paciente`\n" +
//                    "LEFT JOIN `paciente_auxiliar` paux  ON paux.pk_paciente_auxiliar = c.`pfk_paciente`\n" +
//                    "WHERE c.`fecha_cita` = '"+fecha+"' AND c.estado <> 'Cancelada' AND c.`estado` <> 'Inactivo'\n" +
//                    "GROUP BY c.`pfk_paciente`\n" +
//                    "ORDER BY cxh.`hora` ASC";
//     
//            SQL = "SELECT c.`pk_cita`, IF(paux.pk_paciente_auxiliar IS NULL,'1', '2') PAUX, IF(paux.pk_paciente_auxiliar IS NULL,c.`pfk_paciente`, paux.pk_paciente_auxiliar) ID, \n" +
//                    "IF(paux.pk_paciente_auxiliar IS NULL,CONCAT_WS(' ', per.`primer_apellido`, IFNULL(per.`segundo_apellido`, ''), per.`primer_nombre`, IFNULL(per.`segundo_nombre`, '')),\n" +
//                    "CONCAT_WS(' ', paux.`primer_apellido`, IFNULL(paux.`segundo_apellido`, ''), paux.`primer_nombre`, IFNULL(paux.`segundo_nombre`, ''))) PACIENTE,\n" +
//                    "GROUP_CONCAT(cxh.`hora`) hora , c.`estado`, IF(paux.pk_paciente_auxiliar IS NULL,CONCAT_WS(' ', per.`primer_nombre`, IFNULL(per.`segundo_nombre`, '')),\n" +
//                    "CONCAT_WS(' ', paux.`primer_nombre`, IFNULL(paux.`segundo_nombre`, ''))) NOMBRES, \n" +
//                    "IF(paux.pk_paciente_auxiliar IS NULL,CONCAT_WS(' ', per.`primer_apellido`, IFNULL(per.`segundo_apellido`, '')),\n" +
//                    "CONCAT_WS(' ', paux.`primer_apellido`, IFNULL(paux.`segundo_apellido`, ''))) APELLIDOS\n" +
//                    "FROM `citas` c\n" +
//                    "INNER JOIN `citasxhoras` cxh ON cxh.`pfk_paciente` = c.`pfk_paciente` AND cxh.`pfk_cita` = c.`pk_cita`\n" +
//                    "LEFT JOIN `personas` per ON CONCAT(per.`pfk_tipo_documento`, per.`pk_persona`) = c.`pfk_paciente`\n" +
//                    "LEFT JOIN `paciente_auxiliar` paux  ON paux.pk_paciente_auxiliar = c.`pfk_paciente`\n" +
//                    "WHERE c.`fecha_cita` = '"+fecha+"' AND c.estado <> 'Cancelada' AND c.`estado` <> 'Inactivo'\n" +
//                    "GROUP BY c.`pfk_paciente`\n" +
//                    "ORDER BY cxh.`hora` ASC";
            
            SQL = "SELECT c.`pk_cita`, IF(paux.pk_paciente_auxiliar IS NULL,'1', '2') PAUX, IF(paux.pk_paciente_auxiliar IS NULL,c.`pfk_paciente`, paux.pk_paciente_auxiliar) ID, \n" +
                    "IF(paux.pk_paciente_auxiliar IS NULL,CONCAT_WS(' ', per.`primer_apellido`, IFNULL(per.`segundo_apellido`, ''), per.`primer_nombre`, IFNULL(per.`segundo_nombre`, '')),\n" +
                    "CONCAT_WS(' ', paux.`primer_apellido`, IFNULL(paux.`segundo_apellido`, ''), paux.`primer_nombre`, IFNULL(paux.`segundo_nombre`, ''))) PACIENTE,\n" +
                    "GROUP_CONCAT(cxh.`hora`) hora , IF(c.`estado` = 'Terminado', 'Atendido', c.estado) estado, IF(paux.pk_paciente_auxiliar IS NULL,CONCAT_WS(' ', per.`primer_nombre`, IFNULL(per.`segundo_nombre`, '')),\n" +
                    "CONCAT_WS(' ', paux.`primer_nombre`, IFNULL(paux.`segundo_nombre`, ''))) NOMBRES, \n" +
                    "IF(paux.pk_paciente_auxiliar IS NULL,CONCAT_WS(' ', per.`primer_apellido`, IFNULL(per.`segundo_apellido`, '')),\n" +
                    "CONCAT_WS(' ', paux.`primer_apellido`, IFNULL(paux.`segundo_apellido`, ''))) APELLIDOS,\n" +
                    "(IFNULL(estado_agenda, 0) + IFNULL(estado_historia, 0) + IFNULL(estado_factura, 0)) suma,\n" +
                    "/*IF((IFNULL(estado_agenda, 0) + IFNULL(estado_historia, 0) + IFNULL(estado_factura, 0)) = 3, 1, 0) est,*/ '0' p, IFNULL(estado_historia, 0) esthts,  IFNULL(estado_factura, 0) estfect , c.`motivo` , IF(paux.tipo IS NULL,'', paux.tipo) TIPO \n" +
                    "FROM `citas` c\n" +
                    "INNER JOIN `citasxhoras` cxh ON cxh.`pfk_paciente` = c.`pfk_paciente` AND cxh.`pfk_cita` = c.`pk_cita`\n" +
                    "LEFT JOIN `personas` per ON CONCAT(per.`pfk_tipo_documento`, per.`pk_persona`) = c.`pfk_paciente`\n" +
                    "LEFT JOIN `paciente_auxiliar` paux  ON paux.pk_paciente_auxiliar = c.`pfk_paciente`  AND paux.estado = 'Activo' \n" +
                    "LEFT JOIN auditoria_agenda audi ON audi.pfk_paciente = c.`pfk_paciente` AND audi.pfk_cita = c.`pk_cita`\n" +
                    "WHERE c.`fecha_cita` = '"+fecha+"' AND c.estado <> 'Cancelada' AND c.`estado` <> 'Inactivo'\n" +
                    "GROUP BY c.`pfk_paciente`, c.`pk_cita`\n" +
                    "ORDER BY cxh.`hora` ASC";
            
//            System.out.println("SQL--->"+SQL);
            
            listaRetorno = gsql.SELECT(SQL);
            return listaRetorno;
            
        }catch(Exception e){
            System.out.println("ERROR getCitasPaciente--> "+e.toString());
            return null;
        }
    }
    
    public ArrayList<String[]> getDiasNulosDelDia(){        
        try{
            Calendar cal =Calendar.getInstance();            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String fecha =   sdf.format(cal.getTime());
            ArrayList<String[]> listaRetorno = new ArrayList<>();
            String SQL = "SELECT d.`fecha`, d.`hora`\n" +
                            "FROM dias_nulos d WHERE d.`fecha` = '"+fecha+"' \n" +
                            "ORDER BY d.`hora` ASC";
            listaRetorno = gsql.SELECT(SQL);
            return listaRetorno;
            
        }catch(Exception e){
        
            System.out.println("ERROR getCitasPaciente--> "+e.toString());
            return null;
        }
    }
    
    public void CitasEnEspera(){
        try{
            Calendar cal =Calendar.getInstance();            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String fecha =   sdf.format(cal.getTime());
            ArrayList<String> consultas = new ArrayList<String>();
            
            if(gsql.SELECT("SELECT * FROM citas WHERE `fecha_cita` = '"+fecha+"' AND `estado` = 'Activo'").size()>0){
                consultas.add("UPDATE `citas`\n" +
                                "SET `estado` = 'Espera'\n" +
                                "WHERE `fecha_cita` = '"+fecha+"' AND `estado` = 'Activo';");
            }
            
            if(consultas.size()>0){
                boolean f = gsql.EnviarConsultas(consultas);
            }
        }catch(Exception e){
            System.out.println("ERROR AusentarCitas--->"+e.toString());
        }
    }
    
    public void AusentarCitas(){
        try{
            Calendar cal =Calendar.getInstance(); 
            SimpleDateFormat sl = new SimpleDateFormat("EEEE");
            String dia = sl.format(cal.getTime());
            
            if(dia.toLowerCase().equals("lunes")){
                cal.add(Calendar.DAY_OF_YEAR, -2);
            }else{
                cal.add(Calendar.DAY_OF_YEAR, -1);
            }
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String fecha =   sdf.format(cal.getTime());
            
            
            
            ArrayList<String> consultas = new ArrayList<String>();

            consultas.add("UPDATE `citas`\n" +
                            "SET `estado` = 'Ausente'\n" +
                            "WHERE `fecha_cita` = '"+fecha+"' AND `estado` = 'Espera';");
            if(consultas.size()>0){
                boolean f = gsql.EnviarConsultas(consultas);
            }
        }catch(Exception e){
            System.out.println("ERROR AusentarCitas--->"+e.toString());
        }
    }
///////////////NUEVO/////////////////////
    
    public boolean CrearCita(String paciente, String fechas, String servicio, String observacion, String user){
         try{
            System.out.println("**********************CrearCita*******************");
            ArrayList<String> consultas = new ArrayList<String>();
            String[] fecha = fechas.split("<:-:>");
            String f = fecha[0].split("::")[0];
            String[] cita = gsql.SELECT("SELECT IFNULL(MAX(CAST(pk_cita AS UNSIGNED)), 0)+1 FROM `citas` WHERE pfk_paciente = '"+paciente+"'").get(0);
            consultas.add("INSERT INTO `citas`\n" +
                        "(`pfk_paciente`,`pk_cita`,`fecha_cita`,`servicio`,`estado`,`usuario`,`fecha`)\n" +
                        "VALUES \n" +
                        "('"+paciente+"','"+cita[0]+"','"+f+"','"+servicio+"','Activo','"+user+"',NOW());");
            for(int i = 0; i < fecha.length; i++){
                consultas.add("INSERT INTO `citasxhoras`\n" +
                            "(`pfk_paciente`,`pfk_cita`,`hora`)\n" +
                            "VALUES \n" +
                            "('"+paciente+"','"+cita[0]+"','"+fecha[i].split("::")[0]+"');");
            }
            
            
            System.out.println("*****************Consultas**************");
            
            for(String dato: consultas){
                System.out.println("con-->"+dato);
            }
            if(consultas.size()>0){
                if(gsql.EnviarConsultas(consultas))
                    return true;
                else 
                    return false;
            }
            else 
                return false; 
        }catch(Exception e){
            System.out.println("ERROR CrearCita  --> "+e.toString());
            return false;
        }
    }
    
    public ArrayList<String[]> getServicios(){        
        try{
            ArrayList<String[]> listaServ = new ArrayList<>();
            String SQL = "SELECT pk_servicio, descripcion FROM `servicios`";
            listaServ = gsql.SELECT(SQL);
            return listaServ;
            
        }catch(Exception e){
            System.out.println("ERROR getServicios--> "+e.toString());
            return null;
        }
    }

    public boolean QuitarDiaNulo(String fecha, String hora) {
        try{
            System.out.println("**********************QuitarDiaNulo*******************");
            ArrayList<String> consultas = new ArrayList<String>();
            
            consultas.add("DELETE FROM `dias_nulos` WHERE `fecha` = '"+fecha+"' AND `hora` = '"+hora+"';");
            
            System.out.println("*****************Consultas**************");
            for(String dato: consultas){
                System.out.println("con-->"+dato);
            }
            if(consultas.size()>0){
                if(gsql.EnviarConsultas(consultas))
                    return true;
                else 
                    return false;
            }
            else 
                return false; 
        }catch(Exception e){
            System.out.println("ERROR CrearCita  --> "+e.toString());
            return false;
        }
    }

    public boolean AgregarDiaNulo(String fecha, String hora) {
        try{
            System.out.println("**********************QuitarDiaNulo*******************");
            ArrayList<String> consultas = new ArrayList<String>();
            
            String sql = "SELECT IFNULL(c.`pfk_paciente`, '') pac, IFNULL(c.`pk_cita`, '') cit \n" +
                            "FROM `citas` c\n" +
                            "INNER JOIN `citasxhoras` cxh ON cxh.`pfk_paciente` = c.`pfk_paciente` AND cxh.`pfk_cita` = c.`pk_cita`\n" +
                            "WHERE c.`fecha_cita` = '"+fecha+"' AND cxh.`hora` = '"+hora+"'";
            ArrayList<String[]> lista = gsql.SELECT(sql);
            System.out.println("lista-->"+lista.size());
            if(lista.size() > 0){
                for(String[] lst : lista){
                    consultas.add("UPDATE `citas`\n" +
                                "SET `estado` = 'Cancelar'\n" +
                                "WHERE `pfk_paciente` = '"+lst[0]+"' AND `pk_cita` = '"+lst[1]+"';");
                }
            }
            
            
            consultas.add("INSERT INTO `dias_nulos`\n" +
                            "(`fecha`,`hora`,`usuario`,`fecha_sys`)\n" +
                            "VALUES \n" +
                            "('"+fecha+"','"+hora+"','dorozco',NOW());");
            
            System.out.println("*****************Consultas**************");
            for(String dato: consultas){
                System.out.println("con-->"+dato);
            }
            if(consultas.size()>0){
                if(gsql.EnviarConsultas(consultas))
                    return true;
                else 
                    return false;
            }
            else 
                return false; 
        }catch(Exception e){
            System.out.println("ERROR CrearCita  --> "+e.toString());
            return false;
        }
    }
    
    
    //
    public boolean QuitarDiaNulo(Connection con, String fecha, String hora) {
        try{
            System.out.println("**********************QuitarDiaNulo*******************");
            ArrayList<String> consultas = new ArrayList<String>();
            
            consultas.add("DELETE FROM `dias_nulos` WHERE `fecha` = '"+fecha+"' AND `hora` = '"+hora+"';");
            
            System.out.println("*****************Consultas**************");
            for(String dato: consultas){
                System.out.println("con-->"+dato);
            }
            if(consultas.size()>0){
                if(gsql.EnviarConsultas(con, consultas))
                    return true;
                else 
                    return false;
            }
            else 
                return false; 
        }catch(Exception e){
            System.out.println("ERROR CrearCita  --> "+e.toString());
            return false;
        }
    }

    public boolean AgregarDiaNulo(Connection con, String fecha, String hora) {
        try{
            System.out.println("**********************QuitarDiaNulo*******************"); 
            ArrayList<String> consultas = new ArrayList<String>();
            
            String sql = "SELECT IFNULL(c.`pfk_paciente`, '') pac, IFNULL(c.`pk_cita`, '') cit \n" +
                            "FROM `citas` c\n" +
                            "INNER JOIN `citasxhoras` cxh ON cxh.`pfk_paciente` = c.`pfk_paciente` AND cxh.`pfk_cita` = c.`pk_cita`\n" +
                            "WHERE c.`fecha_cita` = '"+fecha+"' AND cxh.`hora` = '"+hora+"'";
            ArrayList<String[]> lista = gsql.SELECT(sql);
            System.out.println("lista-->"+lista.size());
            if(lista.size() > 0){
                for(String[] lst : lista){  
                    consultas.add("UPDATE `citas`\n" +
                                "SET `estado` = 'Cancelar'\n" +
                                "WHERE `pfk_paciente` = '"+lst[0]+"' AND `pk_cita` = '"+lst[1]+"';");
                }
            }
            
            
            consultas.add("INSERT INTO `dias_nulos`\n" +
                            "(`fecha`,`hora`,`usuario`,`fecha_sys`)\n" +
                            "VALUES \n" +
                            "('"+fecha+"','"+hora+"','dorozco',NOW());");
            
            System.out.println("*****************Consultas**************");
            for(String dato: consultas){
                System.out.println("con-->"+dato);
            }
            if(consultas.size()>0){
                if(gsql.EnviarConsultas(con, consultas))
                    return true;
                else 
                    return false;
            }
            else 
                return false; 
        }catch(Exception e){
            System.out.println("ERROR CrearCita  --> "+e.toString());
            return false;
        }
    }
    
    //////CONFIRMACION
    public ArrayList<String[]> getPacientesxFecha(String fecha){        
        try{
            ArrayList<String[]> listaRetorno = new ArrayList<>();
            String SQL = "SELECT cxh.`hora`, c.`pk_cita`, IF(paux.pk_paciente_auxiliar IS NULL,'1', '2') PAUX, IF(paux.pk_paciente_auxiliar IS NULL,c.`pfk_paciente`, paux.pk_paciente_auxiliar) ID, \n" +
                            "IF(paux.pk_paciente_auxiliar IS NULL,CONCAT_WS(' ', per.`primer_apellido`, IFNULL(per.`segundo_apellido`, ''), per.`primer_nombre`, IFNULL(per.`segundo_nombre`, '')),\n" +
                            "CONCAT_WS(' ', paux.`primer_apellido`, IFNULL(paux.`segundo_apellido`, ''), paux.`primer_nombre`, IFNULL(paux.`segundo_nombre`, ''))) PACIENTE, \n" +
                            "IFNULL(IF(paux.pk_paciente_auxiliar IS NULL, CONCAT(IF(telp.numero IS NULL, '', CONCAT(telp.numero, ' - ')), REPLACE(GROUP_CONCAT(telc.numero), ',', ' - ')), IFNULL(paux.telefono, '')), '') telefonos, IFNULL(conf.estado, '') estado, c.`motivo` \n" +
                            "FROM `citas` c\n" +
                            "INNER JOIN `citasxhoras` cxh ON cxh.`pfk_paciente` = c.`pfk_paciente` AND cxh.`pfk_cita` = c.`pk_cita`\n" +
                            "LEFT JOIN `personas` per ON CONCAT(per.`pfk_tipo_documento`, per.`pk_persona`) = c.`pfk_paciente`\n" +
                            "LEFT JOIN `paciente_auxiliar` paux  ON paux.pk_paciente_auxiliar = c.`pfk_paciente` and paux.estado = 'Activo'\n" +
                            "LEFT JOIN `personas_telefonos` telp ON telp.pfk_persona = c.`pfk_paciente` AND telp.tipo = 'FIJO'\n" +
                            "LEFT JOIN `personas_telefonos` telc ON telc.pfk_persona = c.`pfk_paciente` AND telc.tipo = 'CELULAR'\n" +
                            "LEFT JOIN `pacientexconfirmacion` conf ON conf.pfk_paciente = c.`pfk_paciente` AND conf.pfk_cita = c.`pk_cita`  \n"+
                            "WHERE c.`fecha_cita` = '"+fecha+"' AND c.estado <> 'Cancelada' AND c.`estado` <> 'Inactivo'\n" +
                            "GROUP BY c.`pfk_paciente`\n" +
                            "ORDER BY cxh.`hora` ASC";
            
            listaRetorno = gsql.SELECT(SQL);
            
            return listaRetorno;
            
        }catch(Exception e){
            System.out.println("ERROR getCitasPaciente--> "+e.toString()); 
            return new ArrayList<>();
        }
    }    

    public boolean GuardarConfirmacion(ArrayList<String[]> listaGuardar) {
        try{
            ArrayList<String> consultas = new ArrayList<String>();

            for(int i = 0; i < listaGuardar.size(); i++){
                if(listaGuardar.get(i)[2].equals("Cancelar")){
                    consultas.add("UPDATE `citas`\n" + 
                                    "SET `estado` = 'Cancelada'\n" +
                                    "WHERE `pfk_paciente` = '"+listaGuardar.get(i)[0]+"' AND `pk_cita` = '"+listaGuardar.get(i)[1]+"';");
                } 
                if(listaGuardar.get(i)[3].equals("0")){
                    consultas.add("INSERT INTO `pacientexconfirmacion`\n" +
                                    "(`pfk_paciente`,`pfk_cita`,`estado`,`usuario`,`fecha`)\n" +
                                    "VALUES \n" +
                                    "('"+listaGuardar.get(i)[0]+"','"+listaGuardar.get(i)[1]+"','"+listaGuardar.get(i)[2]+"','usuario',NOW());");
                }else{//UPDATE 
                    consultas.add("UPDATE `pacientexconfirmacion`\n" +
                                    "SET `estado` = '"+listaGuardar.get(i)[2]+"', `usuario` = 'usuario',  `fecha` = NOW()\n" +
                                    "WHERE `pfk_paciente` = '"+listaGuardar.get(i)[0]+"'  AND `pfk_cita` = '"+listaGuardar.get(i)[1]+"';");
                }
            }
            
           
            
            System.out.println("*****************Consultas**************");
            for(String dato: consultas){
                System.out.println("con-->"+dato);
            }
            if(consultas.size()>0){
                if(gsql.EnviarConsultas(consultas))
                    return true;
                else 
                    return false;
            }
            else 
                return false; 
        }catch(Exception e){
            System.out.println("ERROR --GuardarConfirmacion-- "+e.toString());
            return false;
        }
    }

    public boolean InsertarDiasNulos(String dia, String[] horasTabla, String Usuario) {
        try{
            ArrayList<String> consultas = new ArrayList<String>();
            if(gsql.ExistenDatos("SELECT * FROM dias_nulos d WHERE d.fecha = '"+dia+"'")){
                consultas.add("DELETE FROM dias_nulos  WHERE fecha = '"+dia+"'");
            }
            String insert = "INSERT INTO `dias_nulos`\n" +
                            "(`fecha`,`hora`,`usuario`,`fecha_sys`) VALUES ";
            String insert2 = "";
            for(int i = 0; i < horasTabla.length; i++){
                insert2 += (insert2.equals("")?"":", ")
                            +"('"+dia+"','"+horasTabla[i]+"','"+Usuario+"',NOW())";
            }
            if(!insert2.equals("")){
                insert2+=";";
                consultas.add(insert+insert2);
            }
            if(consultas.size()>0){
                if(gsql.EnviarConsultas(consultas)){
                    System.out.println("");  
                    return true;
                }else {
                    JOptionPane.showMessageDialog(null, "Hubo un error al momento de agregar Dias no laborables");
                    return false;
                }
            }
            else {
                JOptionPane.showMessageDialog(null, "No se encontraron consultas para realizar la operación.");
                return false;
            }
                
            
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean QuitarDiasNulos(String dia, String[] horasTabla) {
        try{
            ArrayList<String> consultas = new ArrayList<String>();
            if(gsql.ExistenDatos("SELECT * FROM dias_nulos d WHERE d.fecha = '"+dia+"'")){
                consultas.add("DELETE FROM dias_nulos  WHERE fecha = '"+dia+"'");
            }
            if(consultas.size()>0){
                if(gsql.EnviarConsultas(consultas)){
                    System.out.println("");
                    return true;
                }else {
                    JOptionPane.showMessageDialog(null, "Hubo un error al momento de agregar Dias no laborables");
                    return false;
                }
            }
            else {
                JOptionPane.showMessageDialog(null, "No se encontraron consultas para realizar la operación.");
                return false;
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
    
    
    
    
}
