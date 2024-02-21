/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Calendario;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 *
 * @author richard
 */
public class GenerarCalendario {
    public String diasSemana[];
    public Calendar cal;
    public SimpleDateFormat sdf;
    public String diaActual;
    public String mesActual;
    public String anioActual;
    public String[] Nombremeses = {"enero", "febrero", "marzo", "abril", "mayo", "junio", "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre"};
    
    
    public GenerarCalendario() {
        diasSemana = new String[]{"lu", "ma", "mi", "ju", "vi", "sa", "do"};
        cal = Calendar.getInstance();
        sdf = new SimpleDateFormat("d");
        diaActual =  sdf.format(cal.getTime()).toUpperCase();
        sdf = new SimpleDateFormat("MMMM");
        mesActual =  sdf.format(cal.getTime()).toUpperCase();
        sdf = new SimpleDateFormat("yyyy");
        anioActual =  sdf.format(cal.getTime()).toUpperCase();
    }

    public String getMesAnio() {
        sdf = new SimpleDateFormat("MMMM 'de' yyyy");
        return sdf.format(cal.getTime()).toUpperCase();
    }

    public Calendar getPrimerDiaDelCalendario(Calendar cal) {
        SimpleDateFormat s1 = new SimpleDateFormat("EEEE");//muestra los dias en forma de lunes, martes, miercoles, etc
        SimpleDateFormat s2 = new SimpleDateFormat("dd/MM/yyyy");

        String fecha = s2.format(cal.getTime());//se captura la fecha en formato dia/mes/año
        // independientemente en el dia en el que se este, se pasa al primer dia de cada mes
        System.out.println("fecha-->"+fecha);
        cal.set(Integer.parseInt(fecha.split("/")[2]), Integer.parseInt(fecha.split("/")[1]) - 1, 1);

        //se pregunta. Mientras la fecha no sea un lunes -> se resta un dia calendario
        while (!s1.format(cal.getTime()).equalsIgnoreCase("lunes")) {
            cal.add(Calendar.DAY_OF_YEAR, -1);
        }

        return cal;
    }

    public ArrayList<String> getDiasDelCalendario(Calendar cal) {
        ArrayList<String> dias = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("d");//formato dia en numeros

        cal = getPrimerDiaDelCalendario(cal);

        dias.add(sdf.format(cal.getTime()));
        for (int i = 0; i < 41; i++) {
            cal.add(Calendar.DAY_OF_YEAR, 1);
            dias.add(sdf.format(cal.getTime()));
        }

        return dias;
    }

    public ArrayList<String[]> getDiasDelCalendario(Calendar cal, int a) {
        ArrayList<String[]> dias = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("d");//formato dia en numeros
//        SimpleDateFormat sdf1 = new SimpleDateFormat("dMMMM");//formato dia en numeros

        Calendar c = Calendar.getInstance();
        String diaMesActual = sdf.format(cal.getTime());
        String indicador = "";
        int band = 0;

        cal = getPrimerDiaDelCalendario(cal);

        for (int i = 0; i < 42; i++) {
            if (i != 0) {
                cal.add(Calendar.DAY_OF_YEAR, 1);
            }
            
            if (diaMesActual.equals(sdf.format(cal.getTime())) && band == 0) {
                indicador = "S";
                band = 1;
            } else {
                indicador = "N";
            }
            dias.add(new String[]{sdf.format(cal.getTime()), indicador});
        }

        return dias;
    }

        public ArrayList<String[]> getMesesDelCalendario(Calendar cal, int a) {
        ArrayList<String[]> meses = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM");//formato dia en numeros
//        SimpleDateFormat sdf1 = new SimpleDateFormat("dMMMM");//formato dia en numeros

        Calendar c = Calendar.getInstance();
        String MesActual = sdf.format(cal.getTime());
        String indicador = "";
        int band = 0;
        cal.set(Calendar.MONTH, 0);
        for (int i = 0; i < 12; i++) {            
            if (i != 0) {
                cal.add(Calendar.MONTH, 1);
            }
            
            if (MesActual.equals(sdf.format(cal.getTime())) && band == 0) {
                indicador = "S";
                band = 1;
            } else {
                indicador = "N";
            }
            meses.add(new String[]{sdf.format(cal.getTime()), indicador});
        }

        return meses;
    }
    
    public ArrayList<String[]> getAniosDelCalendario(Calendar cal, int a) {
        ArrayList<String[]> anios = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY");//formato dia en numeros
//        SimpleDateFormat sdf1 = new SimpleDateFormat("dMMMM");//formato dia en numeros

        Calendar c = Calendar.getInstance();
        String AnioActual = sdf.format(cal.getTime());
        String indicador = "";
        int band = 0;
        int Anio = Integer.parseInt(AnioActual);
        int modulo = Integer.parseInt(AnioActual)%10;
        int anioIni = Anio-modulo;
        int anioFin =  anioIni+9;
        int numAnios = 10;
        int numTotl = 12;
        System.out.println("Anio-->"+Anio);
//        cal = getPrimerMesDelCalendario(cal);
        cal.set(Calendar.YEAR, 0);
        String dat = "";
        for (int i = 0; i < 12; i++) {  
            
            if(i == 0) {
                dat = ""+(anioIni-1);
            }else if(i == 11){
                dat = ""+(anioFin+1);
            }else{
                dat = ""+(anioIni+(i-1));
            }
            
            if (Anio == (anioIni+(i-1))&& band == 0) {
                indicador = "S";
                band = 1;
            } else {
                indicador = "N";
            }
            
            anios.add(new String[]{dat, indicador});
        }

        return anios;
    }
    
    
    //NUEVOS PARA CLASE
    public ArrayList<String[]> getDiasCalendario(String fecha, int a) {
        ArrayList<String[]> dias = new ArrayList<>();
        SimpleDateFormat sdfa = new SimpleDateFormat("yyyy");
        SimpleDateFormat sdfm = new SimpleDateFormat("MMMM");
        SimpleDateFormat sdfd = new SimpleDateFormat("d");//formato dia en numeros
//        SimpleDateFormat sdf1 = new SimpleDateFormat("dMMMM");//formato dia en numeros
        Calendar cal =Calendar.getInstance();
        System.out.println("getDiasCalendario --> fehca---------"+fecha);
        if(fecha.equals("")){
            cal = Calendar.getInstance();
        }else{
            int getmes = getMes(fecha.split("::")[1]);
            System.out.println("getmes-->"+getmes);
            cal.set(Integer.parseInt(fecha.split("::")[0]), getmes, 1);
        }
        
        System.out.println("fehca---------"+fecha);
        
//        Calendar c = Calendar.getInstance();
        String diaMesActual = sdfd.format(cal.getTime());
        String indicador = "";
        int band = 0;

        cal = getPrimerDiaDelCalendario(cal);

        for (int i = 0; i < 42; i++) {
            if (i != 0) {  
                cal.add(Calendar.DAY_OF_YEAR, 1);
            }
            
            if (diaMesActual.equals(sdfd.format(cal.getTime())) && band == 0) {
                indicador = "S";
                band = 1;
            } else {
                indicador = "N";
            }
            dias.add(new String[]{sdfa.format(cal.getTime()),sdfm.format(cal.getTime()),sdfd.format(cal.getTime()), indicador});
        }

        return dias;
    }

    public ArrayList<String[]> getMesesCalendario(String fecha, int a) {
        ArrayList<String[]> meses = new ArrayList<>();
        Calendar cal =Calendar.getInstance();
        System.out.println("fecha------"+fecha);
        if(fecha.equals("")){
            cal =Calendar.getInstance();
        }else{
            String anio = "", mes = "";
            if(fecha.indexOf("::")>0){
                if(fecha.split("::").length > 1){
                    anio = fecha.split("::")[0];
                    mes = fecha.split("::")[1];
                }else{
                    anio = fecha.split("::")[0];
                }
            }else{
                anio = fecha;
            }
            cal.set(Integer.parseInt(anio), 0, 1);
        }
        cal.set(Calendar.MONTH, 0);
        
        SimpleDateFormat sdfa = new SimpleDateFormat("YYYY");
        SimpleDateFormat sdfm = new SimpleDateFormat("MMMM");//formato dia en numeros
        SimpleDateFormat sdfn = new SimpleDateFormat("dd/MM/yyyy");//formato dia en numeros
        System.out.println("---------------"+sdfn.format(cal.getTime()));
//        SimpleDateFormat sdf1 = new SimpleDateFormat("dMMMM");//formato dia en numeros

//        Calendar c = Calendar.getInstance();
        String MesActual = sdfm.format(cal.getTime());
        String indicador = "";
        int band = 0;
        
        for (int i = 0; i < 12; i++) {            
            if (i != 0) {
                cal.add(Calendar.MONTH, 1);
            }
            
            if (MesActual.equals(sdfm.format(cal.getTime())) && band == 0) {
                indicador = "S";
                band = 1;
            } else {
                indicador = "N";
            }
            System.out.println(sdfa.format(cal.getTime())+""+sdfm.format(cal.getTime()));
            meses.add(new String[]{sdfa.format(cal.getTime()),sdfm.format(cal.getTime()), "", indicador});
        }

        return meses;
    }
    
    public ArrayList<String[]> getAniosCalendario(String fecha, int a) {
        ArrayList<String[]> anios = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY");//formato dia en numeros
//        SimpleDateFormat sdf1 = new SimpleDateFormat("dMMMM");//formato dia en numeros
        System.out.println("fecha------"+fecha);
        if(fecha.equals("")){
            cal =Calendar.getInstance();            
        }else{
            String anio = "", mes = "";
            if(fecha.indexOf("::")>0){
                if(fecha.split("::").length > 1){
                    anio = fecha.split("::")[0];
                    mes = fecha.split("::")[1];
                }else{
                    anio = fecha.split("::")[0];
                }
            }else{
                anio = fecha;
            }
            cal.set(Integer.parseInt(anio), 0, 1);
        }
        
        String AnioActual = sdf.format(cal.getTime());
        String indicador = "";
        int band = 0;
        int Anio = Integer.parseInt(AnioActual);
        int modulo = Integer.parseInt(AnioActual)%10;
        int anioIni = Anio-modulo;
        int anioFin =  anioIni+9;
        int numAnios = 10;
        int numTotl = 12;
        System.out.println("Anio-->"+Anio);
//        cal = getPrimerMesDelCalendario(cal);
        cal.set(Calendar.YEAR, 0);
        String dat = "";
        for (int i = 0; i < 12; i++) {  
            
            if(i == 0) {
                dat = ""+(anioIni-1);
            }else if(i == 11){
                dat = ""+(anioFin+1);
            }else{
                dat = ""+(anioIni+(i-1));
            }
            
            if (Anio == (anioIni+(i-1))&& band == 0) {
                indicador = "S";
                band = 1;
            } else {
                indicador = "N";
            }
            
            anios.add(new String[]{dat, "", "", indicador});
        }

        return anios;
    }

    private int getMes(String mes) {
        int ret = -1;
        for(int i = 0; i < Nombremeses.length; i++){
            if(mes.equalsIgnoreCase(Nombremeses[i])){
                ret = i;
                break;
            }
        }
        return ret;
    }

}
