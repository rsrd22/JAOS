package BaseDeDatos;

import java.sql.SQLException;
import java.util.ArrayList;

public interface IBaseDeDatos {

    /**
     * Este devuelve un listado de un venctor de la clase Object con los datos
     * de la consulta.<br>
     * Si la consulta se encuentra vacia el valor retornado es <b>null</b>
     *
     * @param sentenciaSQL Es una sentencia de tipo SELECT de SQL.
     * @return ArrayList
     */
    public ArrayList<String[]> SELECT(String sentenciaSQL);

    /**
     * Este metodo devuelve un valor <b>entero</b> que hace referencia a la
     * cantidad de veces que fue actualizado el registro que se envia en la
     * sentencia SQL.<br>
     * <table border="1">
     * <tr>
     * <td>valor devuelto</td>
     * <td>Significado</td>
     * </tr>
     * <tr>
     * <td>0</td>
     * <td>No se actualizo ningun registro</td>
     * </tr>
     * <tr>
     * <td>1</td>
     * <td>El registro fue actualizado una vez</td>
     * </tr>
     * <tr>
     * <td>2</td>
     * <td>El registro fue actualizado dos vez</td>
     * </tr>
     * <tr>
     * <td>...</td>
     * <td></td>
     * </tr>
     * <tr>
     * <td>n</td>
     * <td>El registro fue actualizado (n) veces</td>
     * </tr>
     * </table>
     *
     * @param sentenciaSQL Es una sentencia de tipo UPDATE en SQL.
     * @return int
     */
    public int UPDATE(String sentenciaSQL);

    /**
     * Este metodo inserta un registro en la base de datos.<br>
     * Si la inserción fue satisfactoria este retorna <b>true</b> de lo
     * contrario este devuelve <b>false</b>.
     *
     * @param sentenciaSQL Es una sentencia de tipo INSERT de SQL.
     * @param datosAInsertar Es un vector de String's que contiene los campos a
     * insertar.
     * @return boolean
     */
    public boolean INSERT(String sentenciaSQL, String[] datosAInsertar);

    public boolean EnviarConsultas(ArrayList<String> consultas) throws ClassNotFoundException, SQLException;
}
