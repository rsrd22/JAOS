package Utilidades;

import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Utilidades {

    public static String TEXTO_SIN_NUMEROS = "\\d";
    public static String SOLO_NUMEROS = "^[0-9]+$";
    public static String PACIENTES_AUXILIARES_OCASIONALES = "^[A|O]$";
    
    public static void EstablecerIcono(JFrame vent){
        vent.setIconImage(Toolkit.getDefaultToolkit().getImage(vent.getClass().getResource("/img/Logo.png")));
    }
    
    
    public static void guardarImagen(BufferedImage bi, String imagen) {
        try {
            ImageIO.write(bi, Expresiones.obtenerExtension(imagen), new File(imagen));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "clase: Imagenes\n"
                    + "metodo: ImageIO.write()\n"
                    + "detalle: ocurrio un error al tratar de crear la imagen redimensionada\n"
                    + "" + ex.getMessage());
        }
    }
    
    public static ArrayList<String> decodificarNombre(String nombre) {
        do {
            nombre = nombre.replace("  ", " ");
        } while (nombre.contains("  "));

        ArrayList<String> resultado = new ArrayList<>();
        String vn[] = nombre.split(" ");

        for (int i = 0; i < vn.length; i++) {
            if (vn[i].equalsIgnoreCase("de")) {
                if (vn[i + 1].equalsIgnoreCase("la") || vn[i + 1].equalsIgnoreCase("los")) {
                    resultado.add(vn[i] + " " + vn[i + 1] + " " + vn[i + 2]);
                    i += 2;
                } else {
                    resultado.add(vn[i] + " " + vn[i + 1]);
                    i += 1;
                }
            } else if (vn[i].equalsIgnoreCase("del")) {
                resultado.add(vn[i] + " " + vn[i + 1]);
                i += 1;
            } else {
                resultado.add(vn[i]);
            }
        }

        vn = null;
        return resultado;
    }

    public static String decodificarElemento(String dato) {
        try {

            dato = "" + dato + "";

            dato = dato.replace("_INTEa_", "¿");
            dato = dato.replace("_INTEc_", "?");

            dato = dato.replace("_at_", "á");
            dato = dato.replace("_et_", "é");
            dato = dato.replace("_it_", "í");
            dato = dato.replace("_ot_", "ó");
            dato = dato.replace("_ut_", "ú");
            dato = dato.replace("_At_", "Á");
            dato = dato.replace("_Et_", "É");
            dato = dato.replace("_It_", "Í");
            dato = dato.replace("_Ot_", "Ó");
            dato = dato.replace("_Ut_", "Ú");

            dato = dato.replace("Ã¡", "á");
            dato = dato.replace("Ã©", "é");
            dato = dato.replace("Ã­", "í");
            dato = dato.replace("Ã³", "ó");
            dato = dato.replace("Ãº", "ú");
            dato = dato.replace("Ã±", "ñ");
            dato = dato.replace("Ã?", "Ñ");

            dato = dato.replace("_enie_", "ñ");
            dato = dato.replace("_ENIE_", "Ñ");

            dato = dato.replace("&ntilde;", "ñ");
            dato = dato.replace("&Ntilde;", "Ñ");

            dato = dato.replace("Â", "");//espacios en blanco
            dato = dato.replace("_CD_", "\"");//COMILLAS DOBLES.."&quot;");//COMILLAS DOBLES..
            dato = dato.replace("_dx_", "<");//menorque..
            dato = dato.replace("_bx_", ">");//menorque..
            dato = dato.replace("_PT_", ":");//2 PUNTOS..
            dato = dato.replace("_M_", "+");//COMILLAS DOBLES..
            dato = dato.replace("_I_", "=");//IGUAL......
            dato = dato.replace("_BS_", "/");// BASESLAS
            dato = dato.replace("_CS_", "\"");//COMILLA SIMPLE...
            dato = dato.replace("_P_", "%");//PORCENTA.....
            //HTML = HTML.replace("_L_n", "<br/>");//ESLAS.....
            dato = dato.replace("_L_", "\\");//ESLAS.....
            dato = dato.replace("_A_", "&");//AMPERSAN.....Â
            dato = dato.replace("_Ord_", "°");//°    
            //System.out.println("html-->"+HTML);
//            dato = dato.replace("\\n", "<br/>");//° 
            //System.out.println("html2-->"+HTML);
            //HTML = HTML.replace("<br>", "\n");//°
            //HTML = HTML.replace("<br/>", "\n");//°
//            dato = dato.replace("<br>", "\n");//°
//            dato = dato.replace("<br/>", "\n");//° 
            //  alert(HTML);

        } catch (Exception e) {
            System.out.println("ERROR decodificarElemento()-->" + e.toString());
        }

        return dato;
    }

    public static String CodificarElemento(String dato) {
        try {  

            dato = "" + dato + "";
            dato = dato.replace("Â", "");//espacios en blanco
            dato = dato.replace("Ã¡", "á");
            dato = dato.replace("Ã©", "é");
            dato = dato.replace("Ã­", "í");
            dato = dato.replace("Ã³", "ó");
            dato = dato.replace("Ãº", "ú");
            dato = dato.replace("Ã±", "ñ");
            dato = dato.replace("Ã?", "Ñ");
            
            dato = dato.replace("á", "_at_");
            dato = dato.replace("é", "_et_");
            dato = dato.replace("í", "_it_");
            dato = dato.replace("ó", "_ot_");
            dato = dato.replace("ú", "_ut_");
            
            dato = dato.replace("Á", "_At_");
            dato = dato.replace("É", "_Et_");
            dato = dato.replace("Í", "_It_");
            dato = dato.replace("Ó", "_Ot_");
            dato = dato.replace("Ú", "_Ut_");
            
            dato = dato.replace("¿", "_INTEa_");
            dato = dato.replace("?", "_INTEc_");
            
            dato = dato.replace("ñ", "_enie_");
            dato = dato.replace("Ñ", "_ENIE_");
            
            dato = dato.replace("\"", "_CD_");//COMILLAS DOBLES.."&quot;");//COMILLAS DOBLES..
            dato = dato.replace("<", "_dx_");//menorque..
            dato = dato.replace(">", "_bx_");//menorque..
            dato = dato.replace(":", "_PT_");//2 PUNTOS..
            dato = dato.replace("+", "_M_");//COMILLAS DOBLES..
            dato = dato.replace("=", "_I_");//IGUAL......
            dato = dato.replace("/", "_BS_");// BASESLAS
            dato = dato.replace("\"", "_CS_");//COMILLA SIMPLE...
            dato = dato.replace("%", "_P_");//PORCENTA.....
            //HTML = HTML.replace("_L_n", "<br/>");//ESLAS.....
            dato = dato.replace("\\", "_L_");//ESLAS.....
            dato = dato.replace("&","_A_");//AMPERSAN.....Â
            dato = dato.replace("°", "_Ord_");//°   
            
            //  alert(HTML);

        } catch (Exception e) {
            System.out.println("ERROR decodificarElemento()-->" + e.toString());
        }

        return dato;
    }
    
    public static boolean validarSoloNumeros(String texto) {
       Pattern p = Pattern.compile(SOLO_NUMEROS);
       Matcher m = p.matcher(texto);
       return m.find();
   }
    
    public static void validarNumeroEncampodeTexto(JTextField campo){
       if (validarSoloNumeros(campo.getText())){
            campo.setText(campo.getText());
        }else{
             campo.setText(campo.getText().substring(0,campo.getText().length()-1));
        }
   }

    public static String MascaraMoneda(String dato) {
        String ret = "";
        int con = 0;
        for(int i = dato.length()-1; i >= 0; i--){            
            
            if(con%3 == 0 && con > 0){
                ret = "."+ret;
                con = 0;
            }
            ret = ""+dato.charAt(i)+ret;
            con++;
            
        }
        return ret;
    }

    
    public String convertirNumeroEnLetras(int numero) {
        String moneda = "";

        if (numero == 1) {
            moneda = " Peso";
        } else {
            moneda = " Pesos";
        }

        return numeroEnLetras(numero) + moneda;

    }
    
    public static String formatomoneda(String costo) {
          String caux = "";
          String cfin = "";
          int control = 0;
          boolean entero = validarSoloNumeros(costo);

          if (!costo.equals("") && entero) {
              Pattern pat = Pattern.compile("^[0-9]+$");
              Matcher mat = pat.matcher(costo);
              if (mat.matches()) {
                  System.out.println("SI");
                  for (int i = costo.length() - 1; i >= 0; i--) {
                      control++;
                      caux += costo.charAt(i);
                      if (control % 3 == 0 && i != 0) {
                          caux += ".";
                      }
                  }
                  for (int j = caux.length() - 1; j >= 0; j--) {
                      cfin += caux.charAt(j);
                  }
                  return ("$ " + cfin);
              } else {
                  return "";
              }
          }
          return "";
      }

    public static String numeroEnLetras(int numero) {
        String[] Unidades, Decenas, Centenas;
        String Resultado = "";

        /**
         * ************************************************
         * Nombre de los números
        *************************************************
         */
        Unidades = new String[]{"", "Un", "Dos", "Tres", "Cuatro", "Cinco", "Seis", "Siete", "Ocho", "Nueve", "Diez", "Once", "Doce", "Trece", "Catorce", "Quince", "Dieciséis", "Diecisiete", "Dieciocho", "Diecinueve", "Veinte", "Veintiuno", "Veintidos", "Veintitres", "Veinticuatro", "Veinticinco", "Veintiseis", "Veintisiete", "Veintiocho", "Veintinueve"};
        Decenas = new String[]{"", "Diez", "Veinte", "Treinta", "Cuarenta", "Cincuenta", "Sesenta", "Setenta", "Ochenta", "Noventa", "Cien"};
        Centenas = new String[]{"", "Ciento", "Doscientos", "Trescientos", "Cuatrocientos", "Quinientos", "Seiscientos", "Setecientos", "Ochocientos", "Novecientos"};

        if (numero == 0) {
            Resultado = "Cero";
        } else if (numero >= 1 && numero <= 29) {
            Resultado = Unidades[numero];
        } else if (numero >= 30 && numero <= 100) {
            String agregado = "";
            if (numero % 10 != 0) {
                agregado = " y " + numeroEnLetras(numero % 10);
            } else {
                agregado = "";
            }
            Resultado = Decenas[numero / 10] + agregado;
        } else if (numero >= 101 && numero <= 999) {
            String agregado = "";
            if (numero % 100 != 0) {
                agregado = " " + numeroEnLetras(numero % 100);
            } else {
                agregado = "";
            }
            Resultado = Centenas[numero / 100] + agregado;
        } else if (numero >= 1000 && numero <= 1999) {
            String agregado = "";
            if (numero % 1000 != 0) {
                agregado = " " + numeroEnLetras(numero % 1000);
            } else {
                agregado = "";
            }
            Resultado = "Mil" + agregado;
        } else if (numero >= 2000 && numero <= 999999) {
            String agregado = "";
            if (numero % 1000 != 0) {
                agregado = " " + numeroEnLetras(numero % 1000);
            } else {
                agregado = "";
            }
            Resultado = numeroEnLetras(numero / 1000) + " Mil" + agregado;
        } else if (numero >= 1000000 && numero <= 1999999) {
            String agregado = "";
            if (numero % 1000000 != 0) {
                agregado = " " + numeroEnLetras(numero % 1000000);
            } else {
                agregado = "";
            }
            Resultado = "Un Millón" + agregado;
        } else if (numero >= 2000000 && numero <= 1999999999) {
            String agregado = "";
            if (numero % 1000000 != 0) {
                agregado = " " + numeroEnLetras(numero % 1000000);
            } else {
                agregado = "";
            }
            Resultado = numeroEnLetras(numero / 1000000) + " Millones" + agregado;
        }
        return Resultado;
    }
    
    public static String[] obtenerDocumentoyTipoDoc(String TID) {
        String tipoydoc[] = new String[]{"",""};
        for (int i = 0; i < TID.length(); i++) { 
            if (Expresiones.validarSoloNumeros("" + TID.charAt(i))) {
                tipoydoc[0] = TID.substring(0, i);
                tipoydoc[1] = TID.substring(i);
                break;
            }
        }
        return tipoydoc;
    }
    
    public static String CapitaliceTexto(String texto){
        try{
            String[] info = texto.trim().toLowerCase().replace(" ", ":").split(":");
            String ret = "";
            System.out.println("*********************CapitaliceTexto********************"+texto+"**********");
            String inf ="";
            for (int i = 0; i < info.length; i++) { 
                inf =info[i];
                if(!inf.trim().equals("")){
                    String ini = ""+inf.charAt(0);
                    String fin = inf.substring(1);
                    System.out.println("ini-->"+ini);
                    System.out.println("fin-->"+fin);
                    ret += (ret.equals("")?"":" ") +  ini.toUpperCase() + fin;
                }
            }
            System.out.println("***END***");
            return ret;
        }catch(Exception e){
            e.printStackTrace();
            return "";
        }
    }
    
}
