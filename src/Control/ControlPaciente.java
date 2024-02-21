/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;

import BaseDeDatos.gestorMySQL;
import Modelo.Paciente;
import Modelo.Persona;
import Utilidades.Utilidades;
import Utilidades.datosUsuario;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 *
 * @author AT-DESARROLLO
 */
public class ControlPaciente {

    private gestorMySQL gsql;
    private Persona persona;
    private Paciente paciente;
    private ControlPersonas controlPersona = new ControlPersonas();

    public ControlPaciente(Paciente paciente) {
        gsql = new gestorMySQL();
        this.paciente = paciente;
    }

    public ControlPaciente() {
        gsql = new gestorMySQL();
        paciente = null;
    }

    public ArrayList<String[]> getPaciente(String id, String tid) {
        try {
            ArrayList<String[]> Info = new ArrayList<>();

            String sql = "SELECT CONCAT_WS(' ', per.primer_nombre, IFNULL(per.segundo_nombre, '')) NOM, \n"
                    + "CONCAT_WS(' ', per.primer_apellido, IFNULL(per.segundo_apellido, '')) APE, IFNULL(per.`direccion`, '') DIR,\n"
                    + "DATE_FORMAT(per.fecha_de_nacimiento,'%d/%m/%Y') FNAC, IFNULL(per.`sexo`, '') SEXO, \n"
                    + "IFNULL(tel.numero, '') TEL, IFNULL(cel.numero, '') CEL, IFNULL(per.correo_electronico, '') COR, IFNULL(pac.estado, 'Activo') EST\n"
                    + "FROM `pacientes` pac\n"
                    + "INNER JOIN `personas` per ON CONCAT(per.pfk_tipo_documento, per.pk_persona) = pac.pfk_paciente\n"
                    + "LEFT JOIN `personas_telefonos` tel ON tel.pfk_persona=CONCAT(per.pk_persona) AND tel.tipo = 'FIJO'\n"
                    + "LEFT JOIN `personas_telefonos` cel ON cel.pfk_persona=CONCAT(per.pk_persona) AND cel.tipo = 'CELULAR'\n"
                    + "WHERE CONCAT(per.`pfk_tipo_documento`, per.`pk_persona`) = '" + tid + id + "'";

            sql = "SELECT CONCAT_WS(' ', per.primer_nombre, IFNULL(per.segundo_nombre, '')) NOM, \n"
                    + "CONCAT_WS(' ', per.primer_apellido, IFNULL(per.segundo_apellido, '')) APE, IFNULL(per.`direccion`, '') DIR,\n"
                    + "DATE_FORMAT(per.fecha_de_nacimiento,'%d/%m/%Y') FNAC, IF(IFNULL(per.`sexo`, 'M') = 'M', 'Masculino', 'Femenino') SEXO, \n"
                    + "REPLACE(GROUP_CONCAT(IFNULL(tel.numero, '')), ',', '<>') TEL, IFNULL(per.correo_electronico, '') COR, IFNULL(pac.estado, 'Activo') EST\n"
                    + "FROM `pacientes` pac\n"
                    + "INNER JOIN `personas` per ON CONCAT(per.pfk_tipo_documento, per.pk_persona) = pac.pfk_paciente\n"
                    + "LEFT JOIN `personas_telefonos` tel ON tel.pfk_persona=CONCAT(per.`pfk_tipo_documento`, per.`pk_persona`)  \n"
                    + "WHERE CONCAT(per.`pfk_tipo_documento`, per.`pk_persona`) = '" + tid + id + "'\n"
                    + "GROUP BY pac.pfk_paciente";

            Info = gsql.SELECT(sql);
            return Info;
        } catch (Exception e) {
            return null;
        }
    }

    public ArrayList<String[]> getPersonal(String id, String tid) {
        try {
            ArrayList<String[]> Info = new ArrayList<>();

            String sql = "SELECT IFNULL(per.primer_nombre, '') N1,IFNULL(per.segundo_nombre, '') N2, \n"
                    + "IFNULL(per.primer_apellido, '') A1,IFNULL(per.segundo_apellido, '') A2, IFNULL(per.direccion, '') DIR, IF(IFNULL(per.sexo, 'M')='M', 'Masculino', 'Femenino') SXO,\n"
                    + "DATE_FORMAT(per.fecha_de_nacimiento,'%d/%m/%Y') FNAC, IFNULL(per.correo_electronico, '') COR, IFNULL(tel.numero, '') TEL,IFNULL(cel.numero, '') CEL,\n"
                    + "IFNULL(emp.titulo, '') TITL, IFNULL(emp.cargo, 'Seleccionar') CARG, IFNULL(emp.salario, '') SLRIO\n"
                    + "FROM `personal` emp\n"
                    + "INNER JOIN `personas` per ON CONCAT(per.pfk_tipo_documento, per.pk_persona) = emp.pk_personal\n"
                    + "LEFT JOIN `personas_telefonos` tel ON tel.pfk_persona = emp.pk_personal AND tel.tipo = 'FIJO'\n"
                    + "LEFT JOIN `personas_telefonos` cel ON cel.pfk_persona = emp.pk_personal AND cel.tipo = 'CELULAR'\n"
                    + "WHERE emp.pk_personal = '" + tid + id + "'";
            Info = gsql.SELECT(sql);
            return Info;
        } catch (Exception e) {
            return null;
        }
    }

    public String setPaciente(Map<String, String> campos, ArrayList<String[]> listaCot, ArrayList<String[]> listaTrat) {
        try {
            System.out.println("campos.get(\"noms\")-->" + campos.get("noms"));
            System.out.println("campos.get(\"apes\")-->" + campos.get("apes"));
            ArrayList<String> nombres = Utilidades.decodificarNombre(campos.get("noms"));
            ArrayList<String> apellidos = Utilidades.decodificarNombre(campos.get("apes"));
            String pn = "", sn = "", pa = "", sa = "";

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

            boolean persona = controlPersona.setPersonaN(campos.get("id"), campos.get("tid"), pn, sn, pa, sa,
                    campos.get("fnac"), campos.get("sex"), campos.get("dir"), campos.get("tel"), campos.get("cor"));

            ArrayList<String> consultas = new ArrayList<String>();
            String pkPaciente = campos.get("tid") + campos.get("id");
            System.out.println("persona--->" + persona);
            if (persona) {
                if (gsql.ExistenDatos("SELECT * FROM `pacientes` WHERE `pfk_paciente` = '" + pkPaciente + "'")) {
                    consultas.add("UPDATE `pacientes`\n"
                            + "SET `estado` = '" + campos.get("est") + "'\n"
                            + "WHERE `pfk_paciente` = '" + pkPaciente + "';");
                } else {
                    consultas.add("INSERT INTO `pacientes`\n"
                            + "(`pfk_paciente`,`estado`,`usuario`,`fecha`)\n"
                            + "VALUES \n"
                            + "('" + pkPaciente + "','" + campos.get("est") + "','usuario',NOW());");
                }
                System.out.println("***********COTIZACION*********");

                for (String[] lst : listaCot) {
                    System.out.println("***********************************");
                    if (lst[4].equals("0")) {//INSERT
                        String cons = gsql.SELECT("SELECT IFNULL(MAX(CAST(p.`pk_consecutivo` AS UNSIGNED)), 0)+1 FROM `pacientexcotizaciones` p WHERE p.pfk_paciente = '" + pkPaciente + "' AND p.`pfk_tratamiento` = '" + lst[0] + "'").get(0)[0];
                        consultas.add("INSERT INTO `pacientexcotizaciones`\n"
                                + "(`pk_consecutivo`,`pfk_paciente`,`pfk_tratamiento`,\n"
                                + "`cuota_inicial`, diferidas_en,`cuota`,`costo`)\n"
                                + "VALUES \n"
                                + "('" + cons + "','" + pkPaciente + "','" + lst[0] + "',\n"
                                + "'" + lst[2] + "','" + lst[6] + "','" + lst[3] + "','" + lst[1] + "');");
                    } else {//UPDATE
                        consultas.add("UPDATE `pacientexcotizaciones`\n"
                                + "SET `cuota_inicial` = '" + lst[2] + "', `cuota` = '" + lst[3] + "', `costo` = '" + lst[1] + "', diferidas_en = '" + lst[6] + "'\n"
                                + "WHERE `pk_consecutivo` = '" + lst[5] + "' \n"
                                + "AND `pfk_paciente` = '" + pkPaciente + "' AND `pfk_tratamiento` = '" + lst[0] + "';");
                    }
                }
                System.out.println("*********TRATAMIENTO**********" + listaTrat.size());
                for (String[] lst : listaTrat) {
                    if (lst[4].equals("0")) {//INSERT
                        String cons = gsql.SELECT("SELECT IFNULL(MAX(CAST(p.`pk_consecutivo` AS UNSIGNED)), 0)+1 FROM `pacientextratamiento` p WHERE p.pfk_paciente = '" + pkPaciente + "' AND p.`fk_tratamiento` = '" + lst[0] + "'").get(0)[0];
                        consultas.add("INSERT INTO `pacientextratamiento`\n"
                                + "(`pk_consecutivo`,`pfk_paciente`,`fk_tratamiento`,\n"
                                + "`cuota_inicial`, diferidas_en,`cuota`,`costo`,\n"
                                + "`estado`,`fecha_inicio`)\n"
                                + "VALUES \n"
                                + "('" + cons + "','" + pkPaciente + "','" + lst[0] + "',\n"
                                + "'" + lst[2] + "', '" + lst[8] + "','" + lst[3] + "','" + lst[1] + "',\n"
                                + "'" + lst[5] + "','" + lst[6] + "');");
                    } else {
                        consultas.add("UPDATE `pacientextratamiento`\n"
                                + "SET `cuota_inicial` = '" + lst[2] + "', `cuota` = '" + lst[3] + "', `costo` = '" + lst[1] + "',\n"
                                + "  `estado` = '" + lst[5] + "', `fecha_inicio` = '" + lst[6] + "', diferidas_en='" + lst[8] + "'\n"
                                + "WHERE `pk_consecutivo` = '" + lst[7] + "'  AND `pfk_paciente` = '" + pkPaciente + "'\n"
                                + "AND `fk_tratamiento` = '" + lst[0] + "';");
                    }
                    if (lst[9].equals("1")) {
                        consultas.add("CALL actualizarCostoTratSeg('" + pkPaciente + "', '" + lst[0] + "', " + lst[1] + ", '" + datosUsuario.datos.get(0)[0] + "');");
                    }
                }
            }
            System.out.println("***********consultas***************");
            if (consultas.size() > 0) {
                if (gsql.EnviarConsultas(consultas)) {
                    System.out.println("TRUEEEEEEEEEEE");
                    JOptionPane.showMessageDialog(null, "La operación se realizó con exito\n");
                } else {
                    System.out.println("FALSEEEEEEEEEE");
                }
            } else {
                JOptionPane.showMessageDialog(null, "No se encontraron consultas para realizar la operación\n");
            }
            return "";
        } catch (Exception e) {
            System.out.println("ERROR setPaciente-->" + e.toString());
            JOptionPane.showMessageDialog(null,
                    "Ocurrio un error al tratar de obtener los datos\n"
                    + "______________________________________________________\n"
                    + "Detalles:\n" + e.getMessage());
            return "";
        }
    }

    public String delPaciente(String id, String tid) {
        try {
            String pkPaciente = tid + id;
            ArrayList<String> consultas = new ArrayList<String>();
            consultas.add("DELETE FROM `pacientes` WHERE `pfk_paciente` = '"+pkPaciente+"';");

            if (consultas.size() > 0) {
                if (gsql.EnviarConsultas(consultas)) {
                    JOptionPane.showMessageDialog(null, "La operación se realizó con exito\n");
                }
            } else {
                JOptionPane.showMessageDialog(null, "No se encontraron consultas para realizar la operación\n");
            }
            return "";
        } catch (Exception e) {
            System.out.println("ERROR delPaciente-->" + e.toString());
            JOptionPane.showMessageDialog(null,
                    "Ocurrio un error al tratar de obtener los datos\n"
                    + "______________________________________________________\n"
                    + "Detalles:\n" + e.getMessage());
            return "";
        }
    }

    public ArrayList<String[]> getPacienteCotizaciones(String id, String tid) {
        try {
            ArrayList<String[]> Info = new ArrayList<>();

            String sql = "";
            //    0             1         2       3         4        5      6         7
            //TRATAMIENTO, DESCRP_TRAT, COSTO, COUTA_I, DIFERIDAS, COUTA, ESTADO, CONSECUTIVO
            sql = "SELECT ctz.`pfk_tratamiento`, trat.`descripcion`, ctz.`costo`, ctz.`cuota_inicial`, ctz.diferidas_en, ctz.`cuota`, '1', ctz.`pk_consecutivo`  \n"
                    + "FROM `pacientexcotizaciones` ctz \n"
                    + "INNER JOIN `tratamientos` trat ON trat.`pk_tratamiento` = ctz.`pfk_tratamiento`\n"
                    + "WHERE ctz.`pfk_paciente` = '" + tid + id + "'";
            System.out.println("sql-->"+sql);
            Info = gsql.SELECT(sql);
            return Info;
        } catch (Exception e) {
            return null;
        }
    }

    public ArrayList<String[]> getPacienteTratamientos(String id, String tid) {
        try {
            ArrayList<String[]> Info = new ArrayList<>();

            String sql = "";
            //     0             1         2       3         4        5    6   7    8         9       10        11
            //TRATAMIENTO, DESCRIP_TRAT, COSTO, CUOTA_I, DIFERIDAS, CUOTA, 1, '', ESTADO, FECHA_I, CONSECUTIVO, OBS
            sql = "SELECT ctz.`fk_tratamiento`, trat.`descripcion`, ctz.`costo`, ctz.`cuota_inicial`,  ctz.diferidas_en, ctz.`cuota`, \n"
                    + " '1', '', ctz.`estado`, DATE_FORMAT(ctz.`fecha_inicio`,'%d/%m/%Y'), ctz.`pk_consecutivo`,  IFNULL(ctz.observacion, '') OBS \n"
                    + "FROM `pacientextratamiento` ctz \n"
                    + "INNER JOIN `tratamientos` trat ON trat.`pk_tratamiento` = ctz.`fk_tratamiento`\n"
                    + "WHERE ctz.`pfk_paciente` = '" + tid + id + "'";

            Info = gsql.SELECT(sql);
            return Info;
        } catch (Exception e) {
            return null;
        }
    }

    public void setObservacion(Map<String, String> datos) {
        try {
            ArrayList<String> consultas = new ArrayList<String>();
            consultas.add("UPDATE `pacientextratamiento`\n"
                    + "SET `observacion` = '" + datos.get("obs") + "', estado = 'Cancelado'\n"
                    + "WHERE `pk_consecutivo` = '" + datos.get("idcons") + "' AND `pfk_paciente` = '" + datos.get("id") + "'  \n"
                    + "AND `fk_tratamiento` = '" + datos.get("idTra") + "';");

            if (consultas.size() > 0) {
                if (gsql.EnviarConsultas(consultas)) {
                }
            } else {
                JOptionPane.showMessageDialog(null, "No se encontraron consultas para realizar la operación\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean CambioIdentificacion(Connection con, Map<String, String> campos) {
        try {
            ////////

            ArrayList<String> consultas = new ArrayList<String>();
            String pkPaciente = campos.get("tid") + campos.get("id");
            String pkPacienteO = campos.get("tidO") + campos.get("idO");

            ///ANAMNESIS
            if (gsql.ExistenDatos(con, "SELECT * FROM `anamnesis`\n"
                    + "WHERE pfk_paciente = '" + pkPacienteO + "';")) {//UPDATE
                consultas.add("UPDATE `anamnesis`\n"
                        + "SET  `pfk_paciente` = '" + pkPaciente + "'\n"
                        + "WHERE `pfk_paciente` = '" + pkPacienteO + "';");
            }
            if (gsql.ExistenDatos(con, "SELECT * FROM `anamnesisxpaciente`\n"
                    + "WHERE pfk_paciente = '" + pkPacienteO + "'")) {
                consultas.add("UPDATE `anamnesisxpaciente`\n"
                        + "SET `pfk_paciente` = '" + pkPaciente + "'\n"
                        + "WHERE `pfk_paciente` = '" + pkPacienteO + "';");
            }
            if (gsql.ExistenDatos(con, "SELECT * FROM `auditoria_agenda`\n"
                    + "WHERE pfk_paciente = '" + pkPacienteO + "'")) {
                consultas.add("UPDATE `auditoria_agenda`\n"
                        + "SET `pfk_paciente` = '" + pkPaciente + "'\n"
                        + "WHERE `pfk_paciente` = '" + pkPacienteO + "';");
            }
            if (gsql.ExistenDatos(con, "SELECT * FROM `citas`\n"
                    + "WHERE pfk_paciente = '" + pkPacienteO + "'")) {
                consultas.add("UPDATE `citas`\n"
                        + "SET `pfk_paciente` = '" + pkPaciente + "'\n"
                        + "WHERE `pfk_paciente` = '" + pkPacienteO + "';");
            }
            if (gsql.ExistenDatos(con, "SELECT * FROM `citasxhoras`\n"
                    + "WHERE pfk_paciente = '" + pkPacienteO + "'")) {
                consultas.add("UPDATE `citasxhoras`\n"
                        + "SET `pfk_paciente` = '" + pkPaciente + "'\n"
                        + "WHERE `pfk_paciente` = '" + pkPacienteO + "';");
            }
            if (gsql.ExistenDatos(con, "SELECT * FROM `historias_clinicas`\n"
                    + "WHERE pfk_paciente = '" + pkPacienteO + "'")) {
                consultas.add("UPDATE `historias_clinicas`\n"
                        + "SET `pfk_paciente` = '" + pkPaciente + "'\n"
                        + "WHERE `pfk_paciente` = '" + pkPacienteO + "';");
            }
            if (gsql.ExistenDatos(con, "SELECT * FROM `pacientes`\n"
                    + "WHERE pfk_paciente = '" + pkPacienteO + "'")) {
                consultas.add("UPDATE `pacientes`\n"
                        + "SET `pfk_paciente` = '" + pkPaciente + "'\n"
                        + "WHERE `pfk_paciente` = '" + pkPacienteO + "';");
            }
            if (gsql.ExistenDatos(con, "SELECT * FROM `pacientexconfirmacion`\n"
                    + "WHERE pfk_paciente = '" + pkPacienteO + "'")) {
                consultas.add("UPDATE `pacientexconfirmacion`\n"
                        + "SET `pfk_paciente` = '" + pkPaciente + "'\n"
                        + "WHERE `pfk_paciente` = '" + pkPacienteO + "';");
            }
            if (gsql.ExistenDatos(con, "SELECT * FROM `pacientexcotizaciones`\n"
                    + "WHERE pfk_paciente = '" + pkPacienteO + "'")) {
                consultas.add("UPDATE `pacientexcotizaciones`\n"
                        + "SET `pfk_paciente` = '" + pkPaciente + "'\n"
                        + "WHERE `pfk_paciente` = '" + pkPacienteO + "';");
            }
            if (gsql.ExistenDatos(con, "SELECT * FROM `pacientextratamiento`\n"
                    + "WHERE pfk_paciente = '" + pkPacienteO + "'")) {
                consultas.add("UPDATE `pacientextratamiento`\n"
                        + "SET `pfk_paciente` = '" + pkPaciente + "'\n"
                        + "WHERE `pfk_paciente` = '" + pkPacienteO + "';");
            }
            if (gsql.ExistenDatos(con, "SELECT * FROM `pagos`\n"
                    + "WHERE pfk_paciente = '" + pkPacienteO + "'")) {
                consultas.add("UPDATE `pagos`\n"
                        + "SET `pfk_paciente` = '" + pkPaciente + "'\n"
                        + "WHERE `pfk_paciente` = '" + pkPacienteO + "';");
            }
            if (gsql.ExistenDatos(con, "SELECT * FROM `pagosxconceptos`\n"
                    + "WHERE pfk_paciente = '" + pkPacienteO + "'")) {
                consultas.add("UPDATE `pagosxconceptos`\n"
                        + "SET `pfk_paciente` = '" + pkPaciente + "'\n"
                        + "WHERE `pfk_paciente` = '" + pkPacienteO + "';");
            }
            if (gsql.ExistenDatos(con, "SELECT * FROM `pendientes_por_pagar`\n"
                    + "WHERE fk_paciente = '" + pkPacienteO + "'")) {
                consultas.add("UPDATE `pendientes_por_pagar`\n"
                        + "SET `fk_paciente` = '" + pkPaciente + "'\n"
                        + "WHERE `fk_paciente` = '" + pkPacienteO + "';");
            }
            if (gsql.ExistenDatos(con, "SELECT * FROM `personas`\n"
                    + "WHERE pfk_tipo_documento = '" + campos.get("tidO") + "' AND  pk_persona = '" + campos.get("idO") + "'")) {
                consultas.add("UPDATE `personas`\n"
                        + "SET `pk_persona` = '" + campos.get("id") + "', `pfk_tipo_documento` = '" + campos.get("tid") + "'\n"
                        + "WHERE `pk_persona` = '" + campos.get("idO") + "'   AND `pfk_tipo_documento` = '" + campos.get("tidO") + "';");
            }
            if (gsql.ExistenDatos(con, "SELECT * FROM `personas_telefonos`\n"
                    + "WHERE pfk_persona = '" + pkPacienteO + "'")) {
                consultas.add("UPDATE `personas_telefonos`\n"
                        + "SET `pfk_persona` = '" + pkPaciente + "'\n"
                        + "WHERE `pfk_persona` = '" + pkPacienteO + "';");
            }
            if (gsql.ExistenDatos(con, "SELECT * FROM `seguimiento_del_tratamiento`\n"
                    + "WHERE pfk_paciente = '" + pkPacienteO + "'")) {
                consultas.add("UPDATE `seguimiento_del_tratamiento`\n"
                        + "SET `pfk_paciente` = '" + pkPaciente + "'\n"
                        + "WHERE `pfk_paciente` = '" + pkPacienteO + "';");
            }
            if (gsql.ExistenDatos(con, "SELECT * FROM `usuarios`\n"
                    + "WHERE fk_persona = '" + pkPacienteO + "'")) {
                consultas.add("UPDATE `usuarios`\n"
                        + "SET `fk_persona` = '" + pkPaciente + "'\n"
                        + "WHERE `fk_persona` = '" + pkPacienteO + "';");
            }
            if (consultas.size() > 0) {
                if (gsql.EnviarConsultas(con, consultas)) {
                    System.out.println("HOLAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAa");
                    JOptionPane.showMessageDialog(null, "La operación se realizó con exito\n");
                    return true;
                }
            } else {
                JOptionPane.showMessageDialog(null, "No se encontraron consultas para realizar la operación\n");

            }
            return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<String[]> getCotizacionesPacienteAuxiliar(String idPacAux) {
        try {
            ArrayList<String[]> Info = new ArrayList<>();

            String sql = "";

            sql = "SELECT ctz.`pfk_tratamiento`, trat.`descripcion`, ctz.`costo`, ctz.`cuota_inicial`, ctz.diferidas_en, ctz.`cuota`, '1', ctz.`pk_consecutivo`  \n"
                    + "FROM `pacientexcotizaciones` ctz \n"
                    + "INNER JOIN `tratamientos` trat ON trat.`pk_tratamiento` = ctz.`pfk_tratamiento`\n"
                    + "WHERE ctz.`pfk_paciente` = '" + idPacAux + "'";

            Info = gsql.SELECT(sql);
            return Info;
        } catch (Exception e) {
            return null;
        }
    }

    public String setPacienteDePacienteAux(Map<String, String> campos, ArrayList<String[]> listaCot, ArrayList<String[]> listaTrat) {
        try {
            System.out.println("campos.get(\"noms\")-->" + campos.get("noms"));
            System.out.println("campos.get(\"apes\")-->" + campos.get("apes"));
            ArrayList<String> nombres = Utilidades.decodificarNombre(campos.get("noms"));
            ArrayList<String> apellidos = Utilidades.decodificarNombre(campos.get("apes"));
            String pn = "", sn = "", pa = "", sa = "";

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

            boolean persona = controlPersona.setPersonaN(campos.get("id"), campos.get("tid"), pn, sn, pa, sa,
                    campos.get("fnac"), campos.get("sex"), campos.get("dir"), campos.get("tel"), campos.get("cor"));

            ArrayList<String> consultas = new ArrayList<String>();
            String pkPaciente = campos.get("tid") + campos.get("id");
            System.out.println("persona--->" + persona);
            if (persona) {

                consultas.add("INSERT INTO `pacientes`\n"
                        + "(`pfk_paciente`,`estado`,`usuario`,`fecha`)\n"
                        + "VALUES \n"
                        + "('" + pkPaciente + "','" + campos.get("est") + "','usuario',NOW());");

                consultas.add("UPDATE paciente_auxiliar SET estado = 'INACTIVO' WHERE pk_paciente_auxiliar = '" + campos.get("idaux") + "'");

                System.out.println("***********COTIZACION*********");

                for (String[] lst : listaCot) {
                    System.out.println("***********************************");
                    if (lst[4].equals("0")) {//INSERT
                        String cons = gsql.SELECT("SELECT IFNULL(MAX(CAST(p.`pk_consecutivo` AS UNSIGNED)), 0)+1 FROM `pacientexcotizaciones` p WHERE p.pfk_paciente = '" + pkPaciente + "' AND p.`pfk_tratamiento` = '" + lst[0] + "'").get(0)[0];
                        consultas.add("INSERT INTO `pacientexcotizaciones`\n"
                                + "(`pk_consecutivo`,`pfk_paciente`,`pfk_tratamiento`,\n"
                                + "`cuota_inicial`, diferidas_en,`cuota`,`costo`)\n"
                                + "VALUES \n"
                                + "('" + cons + "','" + pkPaciente + "','" + lst[0] + "',\n"
                                + "'" + lst[2] + "','" + lst[6] + "','" + lst[3] + "','" + lst[1] + "');");
                    } else {//UPDATE
                        consultas.add("UPDATE `pacientexcotizaciones`\n"
                                + "SET `pfk_paciente` = '" + pkPaciente + "' , `cuota_inicial` = '" + lst[2] + "', `cuota` = '" + lst[3] + "', `costo` = '" + lst[1] + "', diferidas_en = '" + lst[6] + "'\n"
                                + "WHERE `pk_consecutivo` = '" + lst[5] + "' \n"
                                + "AND `pfk_paciente` = '" + campos.get("idaux") + "' AND `pfk_tratamiento` = '" + lst[0] + "';");
                    }
                }
                System.out.println("*********TRATAMIENTO**********" + listaTrat.size());
                for (String[] lst : listaTrat) {
                    if (lst[4].equals("0")) {//INSERT
                        String cons = gsql.SELECT("SELECT IFNULL(MAX(CAST(p.`pk_consecutivo` AS UNSIGNED)), 0)+1 FROM `pacientextratamiento` p WHERE p.pfk_paciente = '" + pkPaciente + "' AND p.`fk_tratamiento` = '" + lst[0] + "'").get(0)[0];
                        consultas.add("INSERT INTO `pacientextratamiento`\n"
                                + "(`pk_consecutivo`,`pfk_paciente`,`fk_tratamiento`,\n"
                                + "`cuota_inicial`, diferidas_en,`cuota`,`costo`,\n"
                                + "`estado`,`fecha_inicio`)\n"
                                + "VALUES \n"
                                + "('" + cons + "','" + pkPaciente + "','" + lst[0] + "',\n"
                                + "'" + lst[2] + "', '" + lst[8] + "','" + lst[3] + "','" + lst[1] + "',\n"
                                + "'" + lst[5] + "','" + lst[6] + "');");
                    } else {
                        consultas.add("UPDATE `pacientextratamiento`\n"
                                + "SET `cuota_inicial` = '" + lst[2] + "', `cuota` = '" + lst[3] + "', `costo` = '" + lst[1] + "',\n"
                                + "  `estado` = '" + lst[5] + "', `fecha_inicio` = '" + lst[6] + "', diferidas_en='" + lst[8] + "'\n"
                                + "WHERE `pk_consecutivo` = '" + lst[7] + "'  AND `pfk_paciente` = '" + pkPaciente + "'\n"
                                + "AND `fk_tratamiento` = '" + lst[0] + "';");
                    }
//                    if (lst[9].equals("1")) {
//                        consultas.add("CALL actualizarCostoTratSeg('" + pkPaciente + "', '" + lst[0] + "', " + lst[1] + ", '" + datosUsuario.datos.get(0)[0] + "');");
//                    }
                }
            }
            System.out.println("***********consultas***************");
            if (consultas.size() > 0) {
                if (gsql.EnviarConsultas(consultas)) {
                    System.out.println("TRUEEEEEEEEEEE");
                    JOptionPane.showMessageDialog(null, "La operación se realizó con exito\n");
                } else {
                    System.out.println("FALSEEEEEEEEEE");
                }
            } else {
                JOptionPane.showMessageDialog(null, "No se encontraron consultas para realizar la operación\n");
            }
            return "";
        } catch (Exception e) {
            System.out.println("ERROR setPaciente-->" + e.toString());
            JOptionPane.showMessageDialog(null,
                    "Ocurrio un error al tratar de obtener los datos\n"
                    + "______________________________________________________\n"
                    + "Detalles:\n" + e.getMessage());
            return "";
        }
    }

    public String delCotizacion(String consecutivo, String tratamiento, String id) {
        try {
            ArrayList<String> consultas = new ArrayList<String>();
            consultas.add("DELETE FROM `pacientexcotizaciones` WHERE `pk_consecutivo` = '"+consecutivo+"' AND `pfk_paciente` = '"+id+"' AND `pfk_tratamiento` = '"+tratamiento+"';");

            if (consultas.size() > 0) {
                if (gsql.EnviarConsultas(consultas)) {
                    JOptionPane.showMessageDialog(null, "La operación se realizó con exito\n");
                }
            } else {
                JOptionPane.showMessageDialog(null, "No se encontraron consultas para realizar la operación\n");
            }
            return "";
        } catch (Exception e) {
            System.out.println("ERROR delPaciente-->" + e.toString());
            JOptionPane.showMessageDialog(null,
                    "Ocurrio un error al tratar de obtener los datos\n"
                    + "______________________________________________________\n"
                    + "Detalles:\n" + e.getMessage());
            return "";
        }
    }

    public String delTratamiento(String consecutivo, String tratamiento, String id) {
        try {
            ArrayList<String> consultas = new ArrayList<String>();
            
            
            if(gsql.ExistenDatos("SELECT * FROM seguimiento_del_tratamiento WHERE pfk_tratamiento = '"+tratamiento+"' AND pfk_paciente = '"+id+"'")){
                JOptionPane.showMessageDialog(null, "El tratamiento ya esta relacionado con una historia clinica, por lo que no se puede realizar la operación\n");
                return "";
            }
            
            consultas.add("DELETE FROM `pacientextratamiento` WHERE `pk_consecutivo` = '"+consecutivo+"' AND `pfk_paciente` = '"+id+"' AND `fk_tratamiento` = '"+tratamiento+"';");

            if (consultas.size() > 0) {
                if (gsql.EnviarConsultas(consultas)) {
                    JOptionPane.showMessageDialog(null, "La operación se realizó con exito\n");
                    return "1";
                }
            } else {
                JOptionPane.showMessageDialog(null, "No se encontraron consultas para realizar la operación\n");
            }
            return "";
        } catch (Exception e) {
            System.out.println("ERROR delPaciente-->" + e.toString());
            JOptionPane.showMessageDialog(null,
                    "Ocurrio un error al tratar de obtener los datos\n"
                    + "______________________________________________________\n"
                    + "Detalles:\n" + e.getMessage());
            return "";
        }
    }

}
