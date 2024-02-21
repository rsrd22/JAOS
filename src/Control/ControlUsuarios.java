package Control;

import BaseDeDatos.gestorMySQL;
import Mail.SendMail;
import Modelo.Usuario;
import Utilidades.Expresiones;
import Utilidades.Utilidades;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.JOptionPane;

public class ControlUsuarios {

    private gestorMySQL gsql;
    private Usuario usuarios;

    public ControlUsuarios(Usuario usuarios) {
        gsql = new gestorMySQL();
        this.usuarios = usuarios;
    }

    public ControlUsuarios() {
        gsql = new gestorMySQL();
        usuarios = null;
    }

    public ArrayList<String[]> getUsuarios(String usuario) {
        String consulta = "SELECT us.pk_usuario, us.`fk_perfil`,per.`pk_persona`, per.`pfk_tipo_documento`, per.`primer_nombre`, IFNULL(per.`segundo_nombre`, '') SN,\n" +
"                        per.`primer_apellido`, IFNULL(per.`segundo_apellido`, '') SA, per.`fecha_de_nacimiento` FNAC, per.`sexo`, per.`correo_electronico` MAIL,\n" +
"                        IFNULL(ptel.numero, '') TELEFONO, IFNULL(pcel.numero, '') CELULAR,\n" +
"                        usper.pfk_modulo, usper.s, usper.i, usper.u, usper.d, usper.v\n" +
"                        FROM `usuarios` us\n" +
"                        INNER JOIN `personas` per ON CONCAT(per.`pfk_tipo_documento`, per.`pk_persona`) = us.`fk_persona`\n" +
"                        LEFT JOIN `personas_telefonos` ptel ON ptel.`pfk_persona` = us.`fk_persona` AND ptel.tipo = 'FIJO'\n" +
"                        LEFT JOIN `personas_telefonos` pcel ON pcel.`pfk_persona` = us.`fk_persona` AND pcel.tipo = 'CELULAR'\n" +
"                        LEFT JOIN `usuarios_permisos` usper ON usper.pfk_usuario = us.`pk_usuario`\n" +
"                        WHERE us.`pk_usuario` = '"+usuario+"'";
        ArrayList<String[]> lista = gsql.SELECT(consulta);
        return lista;
    }

    //////////////////////////////////////////////////////77
    
    public ArrayList<String[]> getUsuario(String usuario) {
        String consulta = "SELECT pfl.pk_perfil, pfl.perfil, us.`fk_persona`, \n" +
                            "CONCAT_WS(' ', per.`primer_nombre`, IFNULL(per.`segundo_nombre`, ''), per.`primer_apellido`, IFNULL(per.`segundo_apellido`, '')) NOMBRE, \n" +
                            "us.`estado`\n" +
                            "FROM `usuarios` us\n" +
                            "INNER JOIN personas per ON CONCAT(per.`pfk_tipo_documento`, per.`pk_persona`) = us.`fk_persona`\n" +
                            "INNER JOIN `perfiles` pfl ON pfl.pk_perfil = us.`fk_perfil`\n" +
                            "WHERE us.`pk_usuario` = '"+usuario+"'";
        ArrayList<String[]> lista = gsql.SELECT(consulta);
        return lista;
    }
    
    public String setUsuario(Map<String, String> campos, ArrayList<String[]> listaPerm) {
        try{            
            ArrayList<String> consultas = new ArrayList<String>();
            System.out.println("****************setUsuario***************"+campos.get("user")+"__________________________-"+ campos.get("clve")+"//////////////7");
            String clve = gsql.getClaveEncryptada(campos.get("user").toUpperCase(), campos.get("clve"));
            
            if(gsql.ExistenDatos("SELECT * FROM `usuarios` WHERE `pk_usuario` = '"+campos.get("user")+"'")){
                consultas.add("UPDATE `usuarios`\n" +
                                "SET `fk_persona` = '"+campos.get("idPers")+"',  `fk_perfil` = '"+campos.get("idPerf")+"' "+(!campos.get("clve").equals("")?",  `clave` = '"+clve+"'":"")+",  `estado` = '"+campos.get("est")+"'\n" +
                                "WHERE `pk_usuario` = '"+campos.get("user")+"';");
            }else{
                consultas.add("INSERT INTO `usuarios`\n" +
                                "(`pk_usuario`,`fk_persona`,`fk_perfil`,\n" +
                                "`clave`,`estado`)\n" +
                                "VALUES \n" +
                                "('"+campos.get("user")+"','"+campos.get("idPers")+"','"+campos.get("idPerf")+"',\n" +
                                "'"+clve+"','"+campos.get("est")+"');");
            }
            if(gsql.ExistenDatos("SELECT * FROM `usuarios_permisos` WHERE pfk_usuario  = '"+campos.get("user")+"'")){
                consultas.add("DELETE FROM `usuarios_permisos` WHERE `pfk_usuario` = '"+campos.get("user")+"'");
            }
            for(String[] lst: listaPerm){
                consultas.add("INSERT INTO `usuarios_permisos`\n" +
                                "(`pfk_usuario`,`pfk_modulo`,`s`,\n" +
                                "`i`,`u`,`d`,`v`)\n" +
                                "VALUES \n" +
                                "('"+campos.get("user")+"','"+lst[0]+"','"+lst[1]+"',\n" +
                                "'"+lst[2]+"','"+lst[3]+"','"+lst[4]+"','"+lst[5]+"');");
            }
            
            System.out.println("***********consultas***************");
            String ret = "0";
            if(consultas.size()>0){
                if(gsql.EnviarConsultas(consultas)){
                    System.out.println("HOLAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAa");
                    JOptionPane.showMessageDialog(null,"La operación se realizó con exito\n");
                    ret = "1";
                }
            }else{
                JOptionPane.showMessageDialog(null,"No se encontraron consultas para realizar la operación\n");
            }
            return ret;
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("ERROR setUsuario-->"+e.toString());
            JOptionPane.showMessageDialog(null,
                    "Ocurrio un error al tratar de obtener los datos\n"
                    + "______________________________________________________\n"
                    + "Detalles:\n" + e.getMessage());
            return "";
        }
    }
 
    public ArrayList<String[]> getPermisosUsuario(String us){
        String consulta = "SELECT pk_modulo, nombre, IFNULL(s, '0'), IFNULL(i, '0'), IFNULL(u, '0'), IFNULL(d, '0'), IFNULL(v, '0')\n" +
                            "FROM `menu_modulos` modu \n" + 
                            "LEFT JOIN `usuarios_permisos` perm ON perm.`pfk_modulo` = modu.`pk_modulo` AND perm.`pfk_usuario` = '"+us+"'\n" +
                            "ORDER BY CAST(modu.`pk_modulo` AS UNSIGNED) ASC";
        ArrayList<String[]> lista = gsql.SELECT(consulta);
        return lista;
    }

    public String getClave(String usuario) {
        String consulta = "SELECT clave FROM `usuarios` WHERE pk_usuario = '"+usuario+"'";
        ArrayList<String[]> lista = gsql.SELECT(consulta);
        return lista.get(0)[0];
    }

    public boolean CambiarClave(String usuario, String clveAnt, String clveNew) {
        try{  
            ArrayList<String> consultas = new ArrayList<String>();
            String clve = gsql.getClaveEncryptada(usuario.toUpperCase(), clveNew);
            consultas.add("UPDATE `usuarios`\n" +
                            "SET `clave` = '"+clve+"', pass_dinam = '0' \n" +
                            "WHERE `pk_usuario` = '"+usuario+"';");

            if(consultas.size()>0){
                if(gsql.EnviarConsultas(consultas)){
                    System.out.println("HOLAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAa");
                    JOptionPane.showMessageDialog(null,"La operación se realizó con exito\n");
                    return true;
                }   
                return false;
            }else{
                JOptionPane.showMessageDialog(null,"No se encontraron consultas para realizar la operación\n");
                return false;
            }
        }catch(Exception e){
            System.out.println("ERROR setPaciente-->"+e.toString());
            JOptionPane.showMessageDialog(null,
                    "Ocurrio un error al tratar de obtener los datos\n"
                    + "______________________________________________________\n"
                    + "Detalles:\n" + e.getMessage());
            return false;
        }
    }

    public String delUsuario(String user) {
        try{
            ArrayList<String> consultas = new ArrayList<String>();
            consultas.add("DELETE FROM `usuarios` WHERE pk_usuario = '"+user+"'");
                
            if(gsql.ExistenDatos("SELECT * FROM `usuarios_permisos` WHERE pfk_usuario = '"+user+"'")){
                consultas.add("DELETE FROM `usuarios_permisos` WHERE pfk_usuario = '"+user+"'");
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
            System.out.println("ERROR delUsuario-->"+e.toString());
            JOptionPane.showMessageDialog(null,
                    "Ocurrio un error al tratar de obtener los datos\n"
                    + "______________________________________________________\n"
                    + "Detalles:\n" + e.getMessage());
            return "";
        }
    }

    public String setRestablecerContrasenia(Map<String, String> campos) {
        try{
            SendMail mail = new SendMail();
            String passDin = Expresiones.GenerarPassword();
            ArrayList<String> consultas = new ArrayList<String>();
            System.out.println("***********************"+passDin+"****************"+campos.get("user")+"******");
            String clve = gsql.getClaveEncryptada(campos.get("user").toUpperCase(), passDin);
            consultas.add("UPDATE `usuarios`\n" +
                            "SET `clave` = '"+clve+"', pass_dinam = '1' \n" +
                            "WHERE `pk_usuario` = '"+campos.get("user")+"';");
            
            consultas.add("UPDATE `personas`\n" +
                            "SET `correo_electronico` = '"+campos.get("correo")+"'\n" +
                            "WHERE CONCAT(pfk_tipo_documento, pk_persona) = (\n" +
                            "SELECT fk_persona FROM `usuarios` WHERE pk_usuario = '"+campos.get("user")+"'\n" +
                            ")");
            
            String nombre = gsql.SELECT("SELECT CONCAT_WS(' ', per.`primer_nombre`, IFNULL(per.`segundo_nombre`, ''), per.`primer_apellido`, IFNULL(per.`segundo_apellido`, '')) nombre\n" +
                            "FROM `usuarios` us\n" +
                            "LEFT JOIN `personas` per ON CONCAT(per.`pfk_tipo_documento`, per.`pk_persona`) = us.`fk_persona`\n" +
                            "WHERE us.`pk_usuario` = '"+campos.get("user")+"'\n" +
                            "").get(0)[0];
            System.out.println("nombre--._>"+nombre);
            System.out.println("consultas-->"+consultas.size());
            if(consultas.size()>0){
                if(gsql.EnviarConsultas(consultas)){
                    System.out.println("HOLAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAa");
                    mail.EnviarEmail(""+nombre, campos.get("correo"), "Contraseña nueva", "Su nueva contraseña es :"+passDin);
                    return "1"; 
                }   
                return "0";
            }else{
                JOptionPane.showMessageDialog(null,"No se encontraron consultas para realizar la operación\n");
                return "0";
            }
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("ERROR setRestablecerContrasenia-->"+e.toString());
            JOptionPane.showMessageDialog(null,
                    "Ocurrio un error al tratar de obtener los datos\n"
                    + "______________________________________________________\n"
                    + "Detalles:\n" + e.getMessage());
            return "0";
        }
    }
}
