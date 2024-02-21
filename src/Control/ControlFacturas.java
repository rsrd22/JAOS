/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;

import BaseDeDatos.gestorMySQL;
import Utilidades.datosUsuario;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Asus
 */
public class ControlFacturas {
    private gestorMySQL gsql;
    
    public ControlFacturas(){
        
    }
    
    public ArrayList<String[]> getDatosPacientesFactura(String idpaciente, String estadoch){
        try{
            gsql = new gestorMySQL();
            System.out.println("************getDatosPacientesFactura************");
            ArrayList<String[]> Info = new ArrayList<>();
            String cons = "SELECT IFNULL(paux.pk_paciente_auxiliar, '') TID, paux.primer_nombre PNOMBRE, paux.segundo_nombre SNOMBRE,                     \n"
                        + "  paux.primer_apellido PAPELLIDO, paux.segundo_apellido SAPELLIDO, IFNULL(paux.email, '') EMAIL,\n"
                        + "	 NULL DIRECCION,\n"
                        + "  IFNULL(paux.telefono, '') TELEFONO, paux.tipo TIPO\n"
                        + "	 FROM `paciente_auxiliar` paux WHERE estado = 'ACTIVO'\n"
                        + "  AND  paux.pk_paciente_auxiliar = '"+idpaciente+"'";
            if(estadoch.equals("1")){
                cons = "SELECT CONCAT(per.pfk_tipo_documento, per.pk_persona) TID, per.primer_nombre PNOMBRE, per.segundo_nombre SNOMBRE, \n"
                    + "  per.primer_apellido PAPELLIDO, per.segundo_apellido SAPELLIDO,IFNULL(per.correo_electronico, '') EMAIL,\n"
                    + "  IFNULL(per.`direccion`,'')DIRECCION, IFNULL(tel.numero, IFNULL(cel.numero, '')) TELEFONO,'P' TIPO\n"
                    + "  FROM `pacientes` pac\n"  
                    + "  INNER JOIN `personas` per ON CONCAT(per.pfk_tipo_documento, per.pk_persona) = pac.pfk_paciente\n"
                    + "  LEFT JOIN `personas_telefonos` tel ON tel.pfk_persona= CONCAT(per.`pfk_tipo_documento`,per.pk_persona) AND tel.tipo = 'FIJO'\n"
                    + "  LEFT JOIN `personas_telefonos` cel ON cel.pfk_persona= CONCAT(per.`pfk_tipo_documento`,per.pk_persona) AND cel.tipo = 'CELULAR'\n"
                    + "  WHERE CONCAT(per.pfk_tipo_documento, per.pk_persona) = '"+idpaciente+"'";
            }
            
            Info = gsql.SELECT(cons);
            return Info;
        }catch (Exception ex) {
            ex.printStackTrace();   
            return null;
        }
    }
    
    public String setFactura(Map<String, String> lista_datos, ArrayList<String[]> listaConcepto, ArrayList<String[]> Listatratamientos, ArrayList<String[]> ListaArticulos, ArrayList<String[]> ListaDeuda){
        try{            
            gsql = new gestorMySQL();
            System.out.println("************setFactura*************");
            String numFact = "";
            ArrayList<String> Insert = new ArrayList<String>();
            int ptarjeta = Integer.parseInt(lista_datos.get("ptarjeta"));
            int efectivo = Integer.parseInt(lista_datos.get("efectivo"));
            int total = Integer.parseInt(lista_datos.get("total"));
            
            if (ptarjeta > 0 || efectivo > 0) {
                numFact = gsql.ObtNumercionFac();
                String datos;
                //////////////////INSERTS
                //<editor-fold defaultstate="collapsed" desc="Cambio a Paciente">                
                    if (lista_datos.get("d").equals("A")) { // CAMBIO A PACIENTE
                        try {
                            Insert.add("INSERT INTO personas \n VALUES \n"
                                    + "('" + lista_datos.get("documento") + "','" + lista_datos.get("tipoDoc") + "',"
                                    + "'" + lista_datos.get("Pnombre") + "','" + lista_datos.get("Snombre") + "',"
                                    + "'" + lista_datos.get("Papellido") + "','" + lista_datos.get("Sapellido") + "',"
                                    + "NULL,NULL,NULL,NULL)");

                            Insert.add("INSERT INTO pacientes VALUES ('" + lista_datos.get("tipoDoc") + lista_datos.get("documento") + "','activo','"+datosUsuario.datos.get(0)[0]+"',NOW())");
                            Insert.add("UPDATE  paciente_auxiliar SET estado='Inactivo', tipo='P' "
                                    + "WHERE pk_paciente_auxiliar=" + lista_datos.get("TipoP"));

                            datos = "SELECT pfk_paciente FROM citas WHERE pfk_paciente =  '" + lista_datos.get("TipoP") + "'";
                            if (gsql.ExistenDatos(datos)) {
                                Insert.add("UPDATE citas SET `pfk_paciente`= '" + lista_datos.get("tipoDoc") + lista_datos.get("documento") + "' "
                                        + "WHERE `pfk_paciente`= '" + lista_datos.get("TipoP") + "'");
                            }

                            datos = "SELECT pfk_paciente FROM citasxhoras WHERE pfk_paciente =  '" + lista_datos.get("TipoP") + "'";
                            
                            if (gsql.ExistenDatos(datos)) {
                                Insert.add("UPDATE citasxhoras SET `pfk_paciente`= '" + lista_datos.get("tipoDoc") + lista_datos.get("documento") + "' "
                                        + "WHERE `pfk_paciente`= '" + lista_datos.get("TipoP") + "'");
                            }

                            datos = "SELECT pfk_paciente FROM auditoria_agenda WHERE pfk_paciente =  '" + lista_datos.get("TipoP") + "'";
                            if (gsql.ExistenDatos(datos)) {
                                Insert.add("UPDATE auditoria_agenda SET `pfk_paciente`= '" + lista_datos.get("tipoDoc") + lista_datos.get("documento") + "' "
                                        + "WHERE `pfk_paciente`= '" + lista_datos.get("TipoP") + "'");
                            }

                            String consulta = "SELECT pfk_paciente FROM pacientexcotizaciones WHERE pfk_paciente =  '" + lista_datos.get("TipoP") + "'";
                            if (gsql.ExistenDatos(consulta)) {
                                Insert.add("UPDATE pacientexcotizaciones "
                                        + "SET pfk_paciente = '" + lista_datos.get("tipoDoc") + lista_datos.get("documento") + "' "
                                        + "WHERE pfk_paciente =  '" + lista_datos.get("TipoP") + "'");
                            }
                            String in = "";
                            for (int g = 0; g < listaConcepto.size(); g++) {
                                in += "'" + listaConcepto.get(g)[0] + "'";
                            }
                            if (!in.equals("")) {
                                consulta = "SELECT pxc.`pfk_tratamiento`, pxc.`cuota_inicial`, pxc.`diferidas_en`, pxc.`cuota`, pxc.`costo`\n"
                                        + "FROM conceptos con\n"
                                        + "INNER JOIN `pacientexcotizaciones` pxc ON pxc.`pfk_tratamiento` = con.`fk_tratamiento`\n"
                                        + "WHERE con.`pk_concepto` IN (" + in + ") AND pxc.`pfk_paciente` = '" + lista_datos.get("tipoDoc") + lista_datos.get("documento") + "'\n"
                                        + "GROUP BY fk_tratamiento\n";

                                ArrayList<String[]> listac = gsql.SELECT(consulta);
                                if (listac.size() > 0) {
                                    for (int g = 0; g < listac.size(); g++) {
                                        Insert.add("INSERT INTO pacientextratamiento VALUES ("
                                                + "'1', '" + lista_datos.get("tipoDoc") + lista_datos.get("documento") + "',\n"
                                                + "" + listac.get(g)[0] + ", '" + listac.get(g)[1] + "',\n"
                                                + "" + listac.get(g)[2] + ", '" + listac.get(g)[3] + "',\n"
                                                + "'" + listac.get(g)[4] + "', 'Activo', NULL, NOW())");
                                    }
                                }
                            }
                        } catch (Exception e) {
                        }

                    }
        //</editor-fold>
                String fec_pago = gsql.SELECT("SELECT NOW()").get(0)[0];
                System.out.println("fec-->"+fec_pago);
                Insert.add("INSERT INTO facturas VALUES "
                        + "( '" + numFact + "' , '"+fec_pago.replace(".0", "")+"', '"+fec_pago.replace(".0", "")+"', 'pagado', '" + lista_datos.get("total") + "',"
                        + "'" + lista_datos.get("tipoDoc") + lista_datos.get("documento") + "',NULL,NULL)");
                System.out.println("el numero de factura a insertar es ---" + numFact);
                //System.out.println("lista final --- " + listaFinal.size());
                Insert.add("INSERT INTO pagos VALUES"
                        + "('" + lista_datos.get("tipoDoc") + lista_datos.get("documento") + "','" + numFact + "',"
                        + "'" + (efectivo + ptarjeta) + "','" + datosUsuario.datos.get(0)[0] + "',NOW(),NULL,NULL)");

                
                for (int i = 0; i < listaConcepto.size(); i++) {
                    Insert.add("INSERT INTO `pagosxconceptos`\n" +
                                "(`pfk_paciente`,`pfk_pago`,`pfk_concepto`,`cantidad`,`valorxcantidad`)\n" +
                                "VALUES \n" +
                                "('"+lista_datos.get("tipoDoc") + lista_datos.get("documento")+"','"+numFact+"', "+
                                "'"+listaConcepto.get(i)[0]+"',"+listaConcepto.get(i)[1]+", "+listaConcepto.get(i)[2]+");");
                }

                if (lista_datos.get("modopg").equals("1") || lista_datos.get("modopg").equals("2")) {
                    Insert.add("INSERT INTO modo_pago VALUES "
                            + "('" + numFact + "','Tarjeta','" + ptarjeta + "')");
                }

                if (lista_datos.get("modopg").equals("0") || lista_datos.get("modopg").equals("2")) {
                    Insert.add("INSERT INTO modo_pago "
                            + "VALUES('" + numFact + "','Efectivo','" + (efectivo > total ? total : efectivo) + "') ");                    
                }
                
                if (lista_datos.get("d").equals("P")) {
                    if(gsql.ExistenDatos("Select * from pendientes_por_pagar WHERE `fk_paciente` = '" + lista_datos.get("tipoDoc") + lista_datos.get("documento") + "' AND `estado` = 'pendiente'"))
                        Insert.add("UPDATE `pendientes_por_pagar`\n"   
                             + "SET   `estado` = 'terminado'\n"
                             + "WHERE `fk_paciente` = '" + lista_datos.get("tipoDoc") + lista_datos.get("documento") + "' \n"
                             + "AND `estado` = 'pendiente'");
                }
                
                if(ListaDeuda.size()>0){
                    for(int i = 0; i < ListaDeuda.size(); i++){
                        Insert.add("INSERT INTO `pendientes_por_pagar` VALUES "
                                + "('" + numFact + "','" + lista_datos.get("tipoDoc") + lista_datos.get("documento") + "',"
                                + "'" +ListaDeuda.get(i)[2] + "',NOW(),'pendiente','" + ListaDeuda.get(i)[0] + "')");
                    }
                }

                System.out.println("**********************************************************************************************************+");
                System.out.println("pk_cita-----" + lista_datos.get("pkcita") + "//////");
                if (!lista_datos.get("pkcita").equals("")) {
                    String Actualizar = "CALL actualizarAuditoria('" + lista_datos.get("tipoDoc") + lista_datos.get("documento") + "','" + lista_datos.get("pkcita") + "',2, '" + datosUsuario.datos.get(0)[0] + "');";
                    System.out.println("actualizarAuditoria--->" + Actualizar);
                    Insert.add(Actualizar);
                }
                
                //<editor-fold defaultstate="collapsed" desc="GUARDAAR TABLA DE AUDITORIA">
                Insert.add("INSERT INTO auditoria_cambio_estado VALUES("
                        + "'" + lista_datos.get("tipoDoc") + lista_datos.get("documento") + "',"
                        + "'" + datosUsuario.datos.get(0)[0] + "',"
                        + "'" + (lista_datos.get("pkcita").isEmpty() ? "ICONO" : "AGENDA_DIARIA") + "','FACTURA',NOW())");
                //</editor-fold>

                for (int i = 0; i < Listatratamientos.size(); i++) {
                    int abono  = Integer.parseInt(Listatratamientos.get(i)[1]) * Integer.parseInt(Listatratamientos.get(i)[2]);
                    String ActualizarSegTratamiento = "CALL actualizarSegTrat('" + lista_datos.get("tipoDoc") + lista_datos.get("documento") + "',"
                                                    + "'" + Listatratamientos.get(i)[5] + "','" + abono + "','','" + datosUsuario.datos.get(0)[0] + "','F','"+numFact+"')";
                    System.out.println("actualizarSegTrat---" + ActualizarSegTratamiento);
                    Insert.add(ActualizarSegTratamiento);
                }   
  
                if (Insert.size() > 0) {
                    for(int i = 0; i < Insert.size(); i++){
                        System.out.println(i+"--"+Insert.get(i));
                    }
                    try {
                        if (gsql.EnviarConsultas(Insert)) {
                            Insert.clear();
                        }  
                        numFact += "<::>"+fec_pago;
                    } catch (ClassNotFoundException ex) {
                        //Logger.getLogger(ventanaConsultarF.class.getName()).log(Level.SEVERE, null, ex);
                        numFact = "";
                    } catch (SQLException ex) {
                        //Logger.getLogger(ventanaConsultarF.class.getName()).log(Level.SEVERE, null, ex);
                        numFact = "";
                    }
                }
                
            } else {
                numFact = "";
            }
            return numFact;
        }catch(Exception e){
            e.printStackTrace();
            return "";
        }
    }

    public String setFacturaOcacional(Map<String, String> lista_datos, ArrayList<String[]> listaConcepto, ArrayList<String[]> Listatratamientos, ArrayList<String[]> ListaArticulos, ArrayList<String[]> ListaDeuda){
        try{            
            gsql = new gestorMySQL();
            System.out.println("************setFactura*************");
            String numFact = "";
            ArrayList<String> Insert = new ArrayList<String>();
            int ptarjeta = Integer.parseInt(lista_datos.get("ptarjeta"));
            int efectivo = Integer.parseInt(lista_datos.get("efectivo"));
            int total = Integer.parseInt(lista_datos.get("total"));
            
            if (ptarjeta > 0 || efectivo > 0) {
                numFact = gsql.ObtNumercionFac();
                String datos;
                //////////////////INSERTS
                //<editor-fold defaultstate="collapsed" desc="Cambio a Paciente">                
                    if (lista_datos.get("d").equals("A")) { // CAMBIO A PACIENTE
                        try {
                            Insert.add("INSERT INTO personas \n VALUES \n"
                                    + "('" + lista_datos.get("documento") + "','" + lista_datos.get("tipoDoc") + "',"
                                    + "'" + lista_datos.get("Pnombre") + "','" + lista_datos.get("Snombre") + "',"
                                    + "'" + lista_datos.get("Papellido") + "','" + lista_datos.get("Sapellido") + "',"
                                    + "NULL,NULL,NULL,NULL)");

                            Insert.add("INSERT INTO pacientes VALUES ('" + lista_datos.get("tipoDoc") + lista_datos.get("documento") + "','activo','"+datosUsuario.datos.get(0)[0]+"',NOW())");
                            Insert.add("UPDATE  paciente_auxiliar SET estado='Inactivo', tipo='P' "
                                    + "WHERE pk_paciente_auxiliar=" + lista_datos.get("TipoP"));

                            datos = "SELECT pfk_paciente FROM citas WHERE pfk_paciente =  '" + lista_datos.get("TipoP") + "'";
                            if (gsql.ExistenDatos(datos)) {
                                Insert.add("UPDATE citas SET `pfk_paciente`= '" + lista_datos.get("tipoDoc") + lista_datos.get("documento") + "' "
                                        + "WHERE `pfk_paciente`= '" + lista_datos.get("TipoP") + "'");
                            }

                            datos = "SELECT pfk_paciente FROM citasxhoras WHERE pfk_paciente =  '" + lista_datos.get("TipoP") + "'";
                            
                            if (gsql.ExistenDatos(datos)) {
                                Insert.add("UPDATE citasxhoras SET `pfk_paciente`= '" + lista_datos.get("tipoDoc") + lista_datos.get("documento") + "' "
                                        + "WHERE `pfk_paciente`= '" + lista_datos.get("TipoP") + "'");
                            }

                            datos = "SELECT pfk_paciente FROM auditoria_agenda WHERE pfk_paciente =  '" + lista_datos.get("TipoP") + "'";
                            if (gsql.ExistenDatos(datos)) {
                                Insert.add("UPDATE auditoria_agenda SET `pfk_paciente`= '" + lista_datos.get("tipoDoc") + lista_datos.get("documento") + "' "
                                        + "WHERE `pfk_paciente`= '" + lista_datos.get("TipoP") + "'");
                            }

                            String consulta = "SELECT pfk_paciente FROM pacientexcotizaciones WHERE pfk_paciente =  '" + lista_datos.get("TipoP") + "'";
                            if (gsql.ExistenDatos(consulta)) {
                                Insert.add("UPDATE pacientexcotizaciones "
                                        + "SET pfk_paciente = '" + lista_datos.get("tipoDoc") + lista_datos.get("documento") + "' "
                                        + "WHERE pfk_paciente =  '" + lista_datos.get("TipoP") + "'");
                            }
                            String in = "";
                            for (int g = 0; g < listaConcepto.size(); g++) {
                                in += "'" + listaConcepto.get(g)[0] + "'";
                            }
                            if (!in.equals("")) {
                                consulta = "SELECT pxc.`pfk_tratamiento`, pxc.`cuota_inicial`, pxc.`diferidas_en`, pxc.`cuota`, pxc.`costo`\n"
                                        + "FROM conceptos con\n"
                                        + "INNER JOIN `pacientexcotizaciones` pxc ON pxc.`pfk_tratamiento` = con.`fk_tratamiento`\n"
                                        + "WHERE con.`pk_concepto` IN (" + in + ") AND pxc.`pfk_paciente` = '" + lista_datos.get("tipoDoc") + lista_datos.get("documento") + "'\n"
                                        + "GROUP BY fk_tratamiento\n";

                                ArrayList<String[]> listac = gsql.SELECT(consulta);
                                if (listac.size() > 0) {
                                    for (int g = 0; g < listac.size(); g++) {
                                        Insert.add("INSERT INTO pacientextratamiento VALUES ("
                                                + "'1', '" + lista_datos.get("tipoDoc") + lista_datos.get("documento") + "',\n"
                                                + "" + listac.get(g)[0] + ", '" + listac.get(g)[1] + "',\n"
                                                + "" + listac.get(g)[2] + ", '" + listac.get(g)[3] + "',\n"
                                                + "'" + listac.get(g)[4] + "', 'Activo', NULL, NOW())");
                                    }
                                }
                            }
                        } catch (Exception e) {
                        }

                    }
        //</editor-fold>
                String fec_pago = gsql.SELECT("SELECT NOW()").get(0)[0];
                System.out.println("fec-->"+fec_pago);
                Insert.add("INSERT INTO facturas VALUES "
                        + "( '" + numFact + "' , '"+fec_pago.replace(".0", "")+"', '"+fec_pago.replace(".0", "")+"', 'pagado', '" + lista_datos.get("total") + "',"
                        + "'" + lista_datos.get("documentoAux")+ "',NULL,NULL)");
                System.out.println("el numero de factura a insertar es ---" + numFact);
                //System.out.println("lista final --- " + listaFinal.size());
                Insert.add("INSERT INTO pagos VALUES"
                        + "('" + lista_datos.get("documentoAux") + "','" + numFact + "',"
                        + "'" + (efectivo + ptarjeta) + "','" + datosUsuario.datos.get(0)[0] + "',NOW(),NULL,NULL)");

                
                for (int i = 0; i < listaConcepto.size(); i++) {
                    Insert.add("INSERT INTO `pagosxconceptos`\n" +
                                "(`pfk_paciente`,`pfk_pago`,`pfk_concepto`,`cantidad`,`valorxcantidad`)\n" +
                                "VALUES \n" +
                                "('"+lista_datos.get("documentoAux") +"','"+numFact+"', "+
                                "'"+listaConcepto.get(i)[0]+"',"+listaConcepto.get(i)[1]+", "+listaConcepto.get(i)[2]+");");
                }

                if (lista_datos.get("modopg").equals("1") || lista_datos.get("modopg").equals("2")) {
                    Insert.add("INSERT INTO modo_pago VALUES "
                            + "('" + numFact + "','Tarjeta','" + ptarjeta + "')");
                }

                if (lista_datos.get("modopg").equals("0") || lista_datos.get("modopg").equals("2")) {
                    Insert.add("INSERT INTO modo_pago "
                            + "VALUES('" + numFact + "','Efectivo','" + (efectivo > total ? total : efectivo) + "') ");                    
                }
                
                

                System.out.println("**********************************************************************************************************+");
                System.out.println("pk_cita-----" + lista_datos.get("pkcita") + "//////");
                if (!lista_datos.get("pkcita").equals("")) {
                    String Actualizar = "CALL actualizarAuditoria('" + lista_datos.get("documentoAux")+ "','" + lista_datos.get("pkcita") + "',2, '" + datosUsuario.datos.get(0)[0] + "');";
                    System.out.println("actualizarAuditoria--->" + Actualizar);
                    Insert.add(Actualizar);
                }

//                for (int i = 0; i < Listatratamientos.size(); i++) {
//                    int abono  = Integer.parseInt(Listatratamientos.get(i)[1]) * Integer.parseInt(Listatratamientos.get(i)[2]);
//                    String ActualizarSegTratamiento = "CALL actualizarSegTrat('" + lista_datos.get("tipoDoc") + lista_datos.get("documento") + "',"
//                                                    + "'" + Listatratamientos.get(i)[5] + "','" + abono + "','','" + datosUsuario.datos.get(0)[0] + "','F','"+numFact+"')";
//                    System.out.println("actualizarSegTrat---" + ActualizarSegTratamiento);
//                    Insert.add(ActualizarSegTratamiento);
//                }   
  
                if (Insert.size() > 0) {
                    for(int i = 0; i < Insert.size(); i++){
                        System.out.println(i+"--"+Insert.get(i));
                    }
                    try {
                        if (gsql.EnviarConsultas(Insert)) {
                            Insert.clear();
                        }  
                        numFact += "<::>"+fec_pago;
                    } catch (ClassNotFoundException ex) {
                        //Logger.getLogger(ventanaConsultarF.class.getName()).log(Level.SEVERE, null, ex);
                        numFact = "";
                    } catch (SQLException ex) {
                        //Logger.getLogger(ventanaConsultarF.class.getName()).log(Level.SEVERE, null, ex);
                        numFact = "";
                    }
                }
                
            } else {
                numFact = "";
            }
            return numFact;
        }catch(Exception e){
            e.printStackTrace();
            return "";
        }
    }
    
    public String AnularFactura(String idpaciente, String nroFact, String observacion) {
        try{
            gsql = new gestorMySQL();
            String ret = "";
            ArrayList<String> Insert = new ArrayList<String>();
            
            Insert.add("UPDATE `facturas`\n" +
                        "SET   `estado` = 'anulada',\n"
                    + "observacion = '"+observacion+"',\n"
                    + "fecha_anulacion = NOW()\n" +
                        "WHERE `numero` = '"+nroFact+"';");
            String cons = "SELECT con.`fk_tratamiento` IDTRATAMIENTO , ptra.`costo` COSTO\n" +
                            "FROM `pagosxconceptos` pag\n" +
                            "INNER JOIN `conceptos` con ON con.`pk_concepto` = pag.`pfk_concepto` AND `fk_tipo_concepto` = '1'\n" +
                            "INNER JOIN `pacientextratamiento` ptra ON ptra.`pfk_paciente` = pag.`pfk_paciente` AND ptra.`fk_tratamiento` = con.`fk_tratamiento` AND ptra.`estado` = 'Activo'\n" +
                            "WHERE pfk_pago = '"+nroFact+"'";
            
            List<Map<String,String>> lista = gsql.ListSQL(cons);
            
            if(lista.size() > 0){
                for(int v = 0; v < lista.size(); v++){
                    Insert.add("UPDATE `seguimiento_del_tratamiento`\n" +
                                "SET `abono` = '0' ,  `_usuario` = '"+datosUsuario.datos.get(0)[0]+"', `_fecha` = NOW()\n" +
                                "WHERE `pfk_paciente` = '"+idpaciente+"' AND `pfk_tratamiento` = '"+lista.get(v).get("IDTRATAMIENTO")+"' AND pfk_pago = '"+nroFact+"'");
                    
                    Insert.add("CALL actualizarCostoTratSeg('"+idpaciente+"', '"+lista.get(v).get("IDTRATAMIENTO")+"', "+lista.get(v).get("COSTO")+", '"+datosUsuario.datos.get(0)[0]+"');");
                }
            }
            
            
            
            if (Insert.size() > 0) {
                for(int i = 0; i < Insert.size(); i++){
                    System.out.println(i+"--"+Insert.get(i));
                }
                try {
                    if (gsql.EnviarConsultas(Insert)) {
                        Insert.clear();
                    }  
                    ret = "1";
                } catch (ClassNotFoundException ex) {
                    //Logger.getLogger(ventanaConsultarF.class.getName()).log(Level.SEVERE, null, ex);
                    ret = "";
                } catch (SQLException ex) {
                    //Logger.getLogger(ventanaConsultarF.class.getName()).log(Level.SEVERE, null, ex);
                    ret = "";
                }
            }
        
            
            
            return ret;
        }catch(Exception e){
            e.printStackTrace();
            return "";
        }
    }
    
    public String SetRecalcularTratamientos(String pkPaciente){
        try{
            gsql = new gestorMySQL();
            ArrayList<String> consultas = new ArrayList<String>();
            ArrayList<String[]> Info = new ArrayList<>();
            String ret = "";
            String consulta = "SELECT fk_tratamiento, costo \n" +
                                "FROM  `pacientextratamiento` \n" +
                                "WHERE `pfk_paciente` = '"+pkPaciente+"' AND estado = 'Activo'";
            
            
            //consultas.add("CALL actualizarCostoTratSeg('"+pkPaciente+"', '"+lst[0]+"', "+lst[1]+", '"+datosUsuario.datos.get(0)[0]+"');");
            Info = gsql.SELECT(consulta);
            if(Info.size()>0){
                for(int i = 0; i < Info.size(); i++){
                    consultas.add("CALL actualizarCostoTratSeg('"+pkPaciente+"', '"+Info.get(i)[0]+"', "+Info.get(i)[1]+", '"+datosUsuario.datos.get(0)[0]+"');");
                }
            }
            if (consultas.size() > 0) {
                for(int i = 0; i < consultas.size(); i++){
                    System.out.println(i+"--"+consultas.get(i));
                }
                try {
                    if (gsql.EnviarConsultas(consultas)) {
                        consultas.clear();
                    }  
                    ret = "1";
                } catch (ClassNotFoundException ex) {
                    //Logger.getLogger(ventanaConsultarF.class.getName()).log(Level.SEVERE, null, ex);
                    ret = "";
                } catch (SQLException ex) {
                    //Logger.getLogger(ventanaConsultarF.class.getName()).log(Level.SEVERE, null, ex);
                    ret = "";
                }
            }
            
            return ret;
        }catch(Exception e){
            e.printStackTrace();
            return "";
        }
    }
    
}

