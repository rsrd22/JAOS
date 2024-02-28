package BaseDeDatos;

import Utilidades.Utilidades;
import java.io.IOException;
import java.net.InetAddress;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class gestorMySQL implements IBaseDeDatos {

    private Connection con;
    private String BD;
    private String usuario;
    private String contrasena;
    public String mensaje;
    private String hostName;

    public gestorMySQL() {
        con = null;
        mensaje = "";

//        BD = baseDeDatos.JAOSORTODONCIA; 
//        usuario = baseDeDatos.JAOSUSUARIO;
//        hostName = baseDeDatos.JAOSERVIDOR;
//        contrasena = baseDeDatos.JAOSPASSWORD;  
        
//        BD = baseDeDatos.BD_RASB; 
//        usuario = baseDeDatos.RASB_US;
//        hostName = baseDeDatos.HOST_RASB;
//        contrasena = baseDeDatos.RASB_PASS; 
        
        BD = baseDeDatos.ORTODONCIA;
        usuario = baseDeDatos.JAOSUSUARIO;
        hostName = baseDeDatos.HOSTLOCAL;
        contrasena = baseDeDatos.PASSWORD_DB_KENNYS;  
//
//        BD = baseDeDatos.ORTODONCIA;
//        usuario = baseDeDatos.JAOSUSUARIO;
//        hostName = baseDeDatos.JAOSERVIDOR;
//        contrasena = baseDeDatos.JAOSPASSWORD;
        
    }

    public ResultSet Consultar(String sentenciaSQL) {
        System.out.println("Conexión consultas ----- " + sentenciaSQL);
        try {
            ResultSet contenedorQUERY = null;
            if (!Conectar()) {
                JOptionPane.showMessageDialog(null, mensaje);
                return null;
            }
            Statement s = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            contenedorQUERY = s.executeQuery(sentenciaSQL);
            //s.close();
            //Desconectar();   
            return contenedorQUERY;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,
                    "Ocurrio un error al tratar de obtener los datos: Consultar\n"
                    + "______________________________________________________\n"
                    + "Detalles:\n" + ex.getMessage());
            Desconectar();
            return null;
        }
    }

    @Override
    public ArrayList<String[]> SELECT(String sentenciaSQL) {
        try {
//            System.out.println("SELECT -->" + sentenciaSQL);
            ResultSet contenedorQUERY = null;
            ArrayList<String[]> datosQUERY = new ArrayList<>();
            boolean existenDatos = false;

            if (!Conectar()) {
                JOptionPane.showMessageDialog(null, "ERROR EN EL SELECT "+mensaje);
            } else {
                Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                contenedorQUERY = st.executeQuery(sentenciaSQL);

                int numeroDeColumnas = contenedorQUERY.getMetaData().getColumnCount();

                while (contenedorQUERY.next()) {
                    existenDatos = true;
                    String filaConsulta[] = new String[numeroDeColumnas];
                    for (int i = 0; i < numeroDeColumnas; i++) {
                        filaConsulta[i] = Utilidades.decodificarElemento(contenedorQUERY.getString(i + 1));
                    }

                    datosQUERY.add(filaConsulta);
                }
                st.close();
                contenedorQUERY.close();
                Desconectar();
            }

            if (existenDatos) {
                return datosQUERY;
            } else {
                return new ArrayList<>();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,
                    "Ocurrio un error al tratar de obtener los datos\n"
                    + "______________________________________________________\n"
                    + "Detalles:\n" + ex.getMessage());
            Desconectar();
            return new ArrayList<>();
        }
    }

    public boolean ExistenDatos(String sentenciaSQL) {
        try {
            ResultSet contenedorQUERY = null;
            ArrayList<String[]> datosQUERY = new ArrayList<>();
            boolean existenDatos = false;
            //System.out.println("Existen Datos-->" + sentenciaSQL);
            if (!Conectar()) {
                JOptionPane.showMessageDialog(null, mensaje);
            } else {
                Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                contenedorQUERY = st.executeQuery(sentenciaSQL);

                if (contenedorQUERY.next()) {
                    existenDatos = true;
                }
                st.close();
                contenedorQUERY.close();
                Desconectar();
            }

            return existenDatos;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,
                    "Ocurrio un error al tratar de obtener los datos\n"
                    + "______________________________________________________\n"
                    + "Detalles:\n" + ex.getMessage());
            Desconectar();
            return false;
        }
    }

    public boolean ExistenDatos(Connection con, String sentenciaSQL) throws ClassNotFoundException {
        try {
            if (con == null) {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection("jdbc:mysql://" + hostName + ":3306" + "/" + BD, usuario, contrasena);
            }
            ResultSet contenedorQUERY = null;
            ArrayList<String[]> datosQUERY = new ArrayList<>();
            boolean existenDatos = false;
            //System.out.println("Existen Datos-->" + sentenciaSQL);

            Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            contenedorQUERY = st.executeQuery(sentenciaSQL);

            if (contenedorQUERY.next()) {
                existenDatos = true;
            }
            st.close();
            contenedorQUERY.close();
            //Desconectar();

            return existenDatos;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,
                    "Ocurrio un error al tratar de obtener los datos\n"
                    + "______________________________________________________\n"
                    + "Detalles:\n" + ex.getMessage());
            //Desconectar();
            return false;
        }
    }

    @Override
    public int UPDATE(String sentenciaSQL) {
        int ret = 0;
        try {
            if (!Conectar()) {
                JOptionPane.showMessageDialog(null, mensaje);
            } else {
                Statement estamento = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ret = estamento.executeUpdate(sentenciaSQL);

                estamento.close();
                Desconectar();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,
                    "Ocurrio un error al tratar de actualizar\n"
                    + "______________________________________________________\n"
                    + "Detalles:\n" + ex.getMessage());
            Desconectar();
            return 0;
        }

        return ret;
    }

    @Override
    public boolean INSERT(String sentenciaSQL, String[] datosAinsertar) {
        boolean ret = false;
        try {
            if (!Conectar()) {
                JOptionPane.showMessageDialog(null, mensaje);
            } else {
                PreparedStatement pst = con.prepareStatement(sentenciaSQL);
                for (int i = 0; i < datosAinsertar.length; i++) {
                    pst.setString(i + 1, datosAinsertar[i]);
                }
                pst.execute();
                ret = true;

                pst.close();
                Desconectar();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,
                    "Ocurrio un error al tratar de insertar en la base de tatos\n"
                    + "______________________________________________________\n"
                    + "Detalles:\n" + ex.getMessage());
            Desconectar();
            return false;
        }

        return ret;
    }

    private boolean Conectar() {
        try {
//            System.out.println("***************Conectar*****************");
//            System.out.println("hostName-->"+hostName);
//            System.out.println("BD-->"+BD);
//            System.out.println("usuario-->"+usuario);
//            System.out.println("contrasena-->"+contrasena);
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://" + hostName + ":3306" + "/" + BD, usuario, contrasena);
//            System.out.println("*****************END Conectar******************");
            return true;
        } catch (ClassNotFoundException e) {

            mensaje = "Ocurrio un error al tratar de establecer conexión con el servidor.\n"
                    + "Verifique que el servidor se encuentra encendido y funcionando bien."
                    + "_____________________________________________________________________\n" + e.getMessage();

            return false;
        } catch (SQLException ex) {
            System.out.println("ERROR CONECTAR -- "+ex.getMessage());
            
            ex.printStackTrace();
//            boolean ret = PING(""+hostName);
//            if(ret){
//                return Conectar();
//            }else{
//                ////NO SE QUE HACER
////                
//                try {
//                Class.forName("com.mysql.jdbc.Driver");
//                con = DriverManager.getConnection("jdbc:mysql://localhost:3306"+ "/" + BD, usuario, "root");
//                return true;
//                }catch (ClassNotFoundException e) {            
//                    mensaje = "Ocurrio un error al tratar de establecer conexión con el servidor.\n"
//                            + "Verifique que el servidor se encuentra encendido y funcionando bien."
//                            + "_____________________________________________________________________\n" + e.getMessage(); 
//
//                    return false;
//                }catch (SQLException e) {
//                    mensaje = "Error de conexión con la BD (Base de Datos).\n"
//                    + "__________________________________PRUEBA RSRD___________________________________\n" + ex.getMessage();
//                return false;
//                }
//            }
            return false;
        }
    }

    private boolean PING(String ip) {
        InetAddress ping;
        //String ip = "192.168.1.10"; // Ip de la máquina remota 
        try {
            System.out.println("ip--->" + ip);
            ping = InetAddress.getByName(ip);
            if (ping.isReachable(5000)) {
                System.out.println(ip + " - responde!");
                return true;
            } else {
                System.out.println(ip + " - no responde!");
                return false;
            }

        } catch (IOException ex) {
            System.out.println(ex);
            return false;
        }

    }

    public Connection ConectarConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://" + hostName + ":3306" + "/" + BD, usuario, contrasena);
            return con;
        } catch (ClassNotFoundException e) {
            mensaje = "Ocurrio un error al tratar de establecer conexión con el servidor.\n"
                    + "Verifique que el servidor se encuentra encendido y funcionando bien."
                    + "_____________________________________________________________________\n" + e.getMessage();
            return null;
        } catch (SQLException ex) {
            mensaje = "Error de conexión con la BD (Base de Datos).\n"
                    + "_____________________________________________________________________\n" + ex.getMessage();
            return null;
        }
    }

    private boolean Desconectar() {
        try {
            if (!con.isClosed()) {
                con.close();
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            String mensaje = "Error al tratar de cerrar la conexión.\n"
                    + "___________________________________________________________________\n" + ex.getMessage();
            return false;

        }
    }

    @Override
    public boolean EnviarConsultas(ArrayList consultas) throws ClassNotFoundException, SQLException {
        String QuerySQL = null;

        try {
            if (!Conectar()) {
                JOptionPane.showMessageDialog(null, mensaje);
                return false;
            } else {
                if (con == null) {
                    return false;
                }

                if (consultas.size() < 1) {
                    mensaje = "No hay consultas en la cadena enviada.";
                    return false;
                }
                //System.out.println("con--->"+con);
                con.setAutoCommit(false);
                Statement st = con.createStatement();
                for (int x = 0; x < consultas.size(); x++) {
                    System.out.println("consultas.get(" + x + ")-->" + consultas.get(x) + "//////////////////////");
                    if (!consultas.get(x).equals("")) {
                        QuerySQL = consultas.get(x).toString();
                        //System.out.println("EnviarConsultas-("+x+")->"+QuerySQL);
                        if (st.executeUpdate(QuerySQL) <= 0) {
//                        if (st.execute(QuerySQL)) {
                            con.rollback();
                            con.setAutoCommit(true);
                            con.close();
                            return false;
                        }
                    }
                    //System.out.println("HOLA");
                }
                con.commit();
                con.setAutoCommit(true);
                Desconectar();
                return true;
            }

        } catch (Exception e) {
            con.rollback();
            con.setAutoCommit(true);
            Desconectar();
            if (mensaje != null && QuerySQL.toUpperCase().indexOf("WHERE") > 0) {
                mensaje += QuerySQL.toUpperCase().split("WHERE")[1];
            }
            e.printStackTrace();
            return false;
        }
    }

    public List<Map<String, String>> ListSQL(String sql) {
        List<Map<String, String>> lista = new ArrayList<Map<String, String>>();
        try {
            System.out.println("ListSQL -->" + sql);
            ResultSet contenedorQUERY = null;

            boolean existenDatos = false;

            if (!Conectar()) {
                JOptionPane.showMessageDialog(null, mensaje);
            } else {
                Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                contenedorQUERY = st.executeQuery(sql);

                int numeroDeColumnas = contenedorQUERY.getMetaData().getColumnCount();

                while (contenedorQUERY.next()) {
                    ResultSetMetaData rsmd = (ResultSetMetaData) contenedorQUERY.getMetaData();
                    Map<String, String> obj = new LinkedHashMap<String, String>();

                    for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                        obj.put(rsmd.getColumnLabel(i), Utilidades.decodificarElemento(contenedorQUERY.getString(rsmd.getColumnLabel(i))));
                    }
                    lista.add(obj);

                }
                st.close();
                contenedorQUERY.close();
                Desconectar();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR ListSQL--->" + e.getMessage());
        }
        return lista;
    }

    public String getClaveEncryptada(String user, String clave) {
        Encryptar e = new Encryptar();
        System.out.println("user--->" + user + "-----clve-->" + clave);
        String r = "";

        r = user.substring(1, (user.length() < 5 ? user.length() : 5));
        while (r.length() < 5) {
            r += "0";
        }
        r = e.EncryptarClave(r + clave);
        return r;
    }

    public String unicoDato(String sentenciaSQL) {
        try {
            //System.out.println("SELECT -->"+sentenciaSQL);
            ResultSet contenedorQUERY = null;
            String resultConsulta = "";
            boolean existenDatos = false;

            if (!Conectar()) {
                JOptionPane.showMessageDialog(null, mensaje);
            } else {
                Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                contenedorQUERY = st.executeQuery(sentenciaSQL);

                int numeroDeColumnas = contenedorQUERY.getMetaData().getColumnCount();

                if (contenedorQUERY.next()) {
                    existenDatos = true;

                    resultConsulta = contenedorQUERY.getString(1);

                }
                st.close();
                contenedorQUERY.close();
                Desconectar();
            }

            if (existenDatos) {
                return resultConsulta;
            } else {
                return null;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,
                    "Ocurrio un error al tratar de obtener los datos\n"
                    + "______________________________________________________\n"
                    + "Detalles:\n" + ex.getMessage());
            Desconectar();
            return null;
        }
    }

    public boolean EnviarConsultas(Connection con, ArrayList consultas) throws ClassNotFoundException, SQLException {
        String QuerySQL = null;

        try {
            if (con == null) {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection("jdbc:mysql://" + hostName + ":3306" + "/" + BD, usuario, contrasena);
            }

            if (consultas.size() < 1) {
                mensaje = "No hay consultas en la cadena enviada.";
                return false;
            }
            con.setAutoCommit(false);
            Statement st = con.createStatement();
            for (int x = 0; x < consultas.size(); x++) {
                System.out.println("consultas.get(" + x + ")-->" + consultas.get(x) + "//////////////////////");
                if (!consultas.get(x).equals("")) {
                    QuerySQL = consultas.get(x).toString();
                    System.out.println("EnviarConsultasCon-(" + x + ")->" + QuerySQL);
                    if (st.executeUpdate(QuerySQL) <= 0) {
                        con.rollback();
                        con.setAutoCommit(true);
                        con.close();
                        return false;
                    }
                }
                System.out.println("HOLA");
            }
            con.commit();
            con.setAutoCommit(true);
            return true;

        } catch (Exception e) {
            con.rollback();
            con.setAutoCommit(true);
            if (mensaje != null && QuerySQL.toUpperCase().indexOf("WHERE") > 0) {
                mensaje += QuerySQL.toUpperCase().split("WHERE")[1];
            }

            return false;
        }
    }

    public String ObtNumercionFac() {

        try {
            Conectar();
            CallableStatement sp = con.prepareCall("{CALL obtenerNumero(?)}");
//se cargan los parametros de entrada

// parametros de salida
            sp.registerOutParameter(1, java.sql.Types.VARCHAR);//Tipo String
// Se ejecuta el procedimiento almacenado
            sp.execute();
            // devuelve el valor del parametro de salida del procedimiento
            String numeracion = sp.getString("nuevoConsecutivo");
            Desconectar();
            return numeracion;

        } catch (SQLException ex) {
            Logger.getLogger(gestorMySQL.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

}
