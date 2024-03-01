/*
 * To changen this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Informes;

import BaseDeDatos.gestorMySQL;
import Control.ControlGeneral;
import Utilidades.Parametros;
import Utilidades.Utilidades;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 *
 * @author Acer
 */
public class Descripcioninformespdf {

    private gestorMySQL resultquery = new gestorMySQL();
    String path = "";
    Informespdf pdf = new Informespdf();
    private static final String FORMAT_DATE_INPUT = "yyyy-MM-dd";
    private static final String FORMAT_DATE_OUTPUT = "EEEEE, d 'de' MMMMM 'del' yyyy";

    ControlGeneral gen = new ControlGeneral();

    String[] Meses = new String[]{"ENERO", "FEBRERO", "MARZO", "ABRIL", "MAYO", "JUNIO", "JULIO", "AGOSTO", "SEPTIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE"};

    private void IMPRIMIR(String texto) {
        if (true) {
            System.out.println(texto);
        }
    }

    public String Encode() {
        String cifrado = "" + System.currentTimeMillis();
        return cifrado;
    }

    public String Generarinformes(int categoria, int informe, Map<String, String> list) {
        try {
            IMPRIMIR(" ####### GENERAR INFROME (presente) ######### " + categoria + "%%%%%%%" + informe);
            String ret = "";
            String encode = Encode();
            String ruta = Parametros.dirInformes + informe + "_" + encode + ".pdf";
            FileOutputStream archivo = new FileOutputStream(ruta);
            Rectangle papel = null;
            String Titulo = "";
            String entidad = "";
            if (categoria == 0 && informe == 0) {
                Titulo = "Citas Diarias";
            } else if (categoria == 0 && informe == 1) {
                Titulo = "Historico Citas";
            } else if (categoria == 0 && informe == 2) {
                Titulo = "Demanda Inducida";
            } else if (categoria == 1 && informe == 0) {
                Titulo = "Historia Clinica";
            } else if (categoria == 1 && informe == 1) {
                Titulo = "Historias Cerradas";
            } else if (categoria == 1 && informe == 2) {
                Titulo = "Historias Inactivas";
            } else if (categoria == 2 && informe == 0) {
                Titulo = "Facturas por Estados";
            } else if (categoria == 2 && informe == 1) {
                Titulo = "Facturas por Mes";
            } else if (categoria == 2 && informe == 2) {
                Titulo = "Facturas por Día";
            } else if (categoria == 2 && informe == 3) {
                Titulo = "Recaudo Mensual";
            } else if (categoria == 2 && informe == 4) {
                Titulo = "Recaudo Tipo Pago";
            } else if (categoria == 2 && informe == 5) {
                Titulo = "Abonos por Paciente";
            } else if (categoria == 3 && informe == 0) {
                Titulo = "Pacientes Auxiliares";
            } else if (categoria == 3 && informe == 1) {
                Titulo = "Pacientes Activos";
            } else if (categoria == 3 && informe == 2) {
                Titulo = "Pacientes Terminados";
            } else if (categoria == 3 && informe == 3) {
                Titulo = "Pacientes por Terminar";
            }

            PdfPTable Encabezado = getEncabezadoF(Titulo);
            PdfPTable piepagina = null;//setPieLeyenda();
            Informespdf pdf = new Informespdf(Encabezado);

            //if(hoja.equals("Carta")){
            papel = PageSize.LETTER;
//        }else{
//            papel = PageSize.LEGAL;
//        }
            Document documento = new Document(papel);
            PdfWriter writer = PdfWriter.getInstance(documento, archivo);
            writer.setPageEvent(new Informespdf(Encabezado));

            documento.setMargins(20, 20, 10, 20);
            documento.open();

            if (categoria == 0 && informe == 0) {
                infCitasdiario(documento, list);
            } else if (categoria == 0 && informe == 1) {
                infCitasHistorico(documento, list);
            } else if (categoria == 0 && informe == 2) {
                infDemandaind(documento, list);
            } else if (categoria == 1 && informe == 0) { // FALTA
                infHistimprimir(documento, list);
            } else if (categoria == 1 && informe == 1) {
                infHistcerradas(documento, list);
            } else if (categoria == 1 && informe == 2) {
                infHistinactivas(documento, list);
            } else if (categoria == 2 && informe == 0) {
                infFactestados(documento, list);
            } else if (categoria == 2 && informe == 1) {
                infFactpormes(documento, list);
            } else if (categoria == 2 && informe == 2) {
                infFactrecdia(documento, list);
            } else if (categoria == 2 && informe == 3) {
                infFactrecmes(documento, list);
            } else if (categoria == 2 && informe == 4) {
                infFactrectp(documento, list);
            } else if (categoria == 2 && informe == 5) {
                infFactabonoxpac(documento, list);
            } else if (categoria == 3 && informe == 0) {
                pacientAuxi(documento, list);
            } else if (categoria == 3 && informe == 1) {
                pacientActiv(documento, list);
            } else if (categoria == 3 && informe == 2) {
                pacientTermi(documento, list);
            } else if (categoria == 3 && informe == 3) {
                pacientxTermi(documento, list);
            }
            documento.close();

            int result = JOptionPane.showConfirmDialog(null, "¿Desea abrir el documento?");
            if (result == JOptionPane.YES_OPTION) {
                Desktop.getDesktop().open(new File(ruta));
            }

            return ret;
        } catch (Exception ex) {
            //IMPRIMIR(funcion, "    ERROR: " + ex, probar);
            return null;
        }
    }

    public String Generarinformes(Map<String, String> list) {
        try {
            int categoria = Integer.parseInt(list.get("cat"));
            int informe = Integer.parseInt(list.get("inf"));
            
            String ret = "";
            String encode = Encode();

            Rectangle papel = null;
            String titulo = "";
            String entidad = "", pac = "";
            if (categoria == 0 && informe == 0) {
                titulo = "Citas Diarias";
            } else if (categoria == 0 && informe == 1) {
                titulo = "Historico Citas";
            } else if (categoria == 0 && informe == 2) {
                titulo = "Demanda Inducida";
            } else if (categoria == 1 && informe == 0) {
                titulo = "";
                pac = resultquery.unicoDato("SELECT REPLACE(CONCAT_WS(' ', primer_nombre, IFNULL(segundo_nombre, ''), primer_apellido, IFNULL(segundo_apellido, '')), '  ', ' ')\n"
                        + "FROM personas WHERE CONCAT(`pfk_tipo_documento`,`pk_persona`) = '" + list.get("idpac") + "'");
            } else if (categoria == 1 && informe == 1) {
                titulo = "Historias Cerradas";
            } //            else if (categoria == 1 && informe == 2) {
            //                Titulo = "Historias Inactivas";
            //            }
            else if (categoria == 2 && informe == 0) {
                titulo = "Facturas por Estados";
            } else if (categoria == 2 && informe == 1) {
                titulo = "Facturas por Mes";
            } else if (categoria == 2 && informe == 2) {
                titulo = "Facturas por Día";
            } else if (categoria == 2 && informe == 3) {
                titulo = "Recaudo Mensual";
            } else if (categoria == 2 && informe == 4) {
                titulo = "Recaudo Tipo Pago";
            } else if (categoria == 2 && informe == 5) {
                titulo = "Abonos por Paciente";
            } else if (categoria == 3 && informe == 0) {
                titulo = "Pacientes Auxiliares";
            } else if (categoria == 3 && informe == 1) {
                titulo = "Pacientes Activos";
            } //            else if (categoria == 3 && informe == 2) {
            //                Titulo = "Pacientes Terminados";
            //            } 
            else if (categoria == 3 && informe == 2) {
                titulo = "Pacientes por Terminar";
            }
            
            list.put("title", titulo);

            String ruta = Parametros.dirInformes + titulo.replaceAll(" ", "_") + encode + ".pdf";
            FileOutputStream archivo = new FileOutputStream(ruta);

            PdfPTable Encabezado = getEncabezadoF(titulo);

            if (categoria == 1 && informe == 0) {//Historia Clinica
                Encabezado = getEncabezadoxPaciente(titulo, 16, pac);
            }

            PdfPTable piepagina = getPie();
            Informespdf pdf = new Informespdf(Encabezado, piepagina);

            //if(hoja.equals("Carta")){
            papel = PageSize.LETTER;
            //        }else{
            //            papel = PageSize.LEGAL;
            //        }
            Document documento = new Document(papel);
            PdfWriter writer = PdfWriter.getInstance(documento, archivo);
            writer.setPageEvent(new Informespdf(Encabezado));

            //documento.setMargins(R, L, T, B);
            documento.setMargins(20, 20, 10, 35);
            //        if(!hoja.equals("Carta"))
            //            documento.setMargins(20, 20, 10,40);

            //PdfPTable encabezado=getEncabezadoF("INFORME FINAL",ins,idTipo);
            ///////////////////////////////////
            //documento.add(encabezado);
            //        InformeFinal(documento,ins, anexa, lec, jornada, grado, curso, estudiantes, deci, promedio, formato,idTipo,Encabezado);
            //documento.add(new Paragraph("Hola Mundo!"));
            //documento.add(new Paragraph("SoloInformaticaYAlgoMas.blogspot.com"));
            ///////////////////////////////////
            //        URL_FINAL="LibroValoracion"+encode+".pdf";
            //        }catch (Exception e){ IMPRIMIR("ERROR EN GENERACION DE PDF "+e);
            //            URL_FINAL="ERROR "+e;  
            //        }
            //      return URL_FINAL;
            documento.open();

            if (categoria == 0 && informe == 0) {
                infCitasdiario(documento, list);
            } else if (categoria == 0 && informe == 1) {
                infCitasHistorico(documento, list);
            } else if (categoria == 0 && informe == 2) {
                infDemandaind(documento, list);
            } else if (categoria == 1 && informe == 0) {// FALTA
                infHistimprimir(documento, list);
            } else if (categoria == 1 && informe == 1) {// FALTA
                infHistcerradas(documento, list);
            } else if (categoria == 1 && informe == 2) {// FALTA
                infHistinactivas(documento, list);
            } else if (categoria == 2 && informe == 0) {
                infFactestados(documento, list);
            } else if (categoria == 2 && informe == 1) {
                infFactpormes(documento, list);
            } else if (categoria == 2 && informe == 2) {
                infFactrecdia(documento, list);
            } else if (categoria == 2 && informe == 3) {
                infFactrecmes(documento, list);
            } else if (categoria == 2 && informe == 4) {
                infFactrectp(documento, list);
            } else if (categoria == 2 && informe == 5) {
                infFactabonoxpac(documento, list);
            } else if (categoria == 3 && informe == 0) {
                pacientAuxi(documento, list);
            } else if (categoria == 3 && informe == 1) {
                pacientActiv(documento, list);
//            } else if (categoria == 3 && informe == 2) {
//                //pacientTermi(documento, list);
//                infHistcerradas(documento, list);
            } else if (categoria == 3 && informe == 2) {
                pacientxTermi(documento, list);
            }
            documento.close();

            int result = JOptionPane.showConfirmDialog(null, "¿Desea abrir el documento?");
            if (result == JOptionPane.YES_OPTION) {
                Desktop.getDesktop().open(new File(ruta));
            }

            return ret;
        } catch (Exception ex) {
            //IMPRIMIR(funcion, "    ERROR: " + ex, probar);
            return null;
        }
    }

    /**
     *
     * @param titulo
     * @return
     */
    public PdfPTable getEncabezadoF(String titulo) {

        String funcion = "getEncabezadoF()";
        boolean probar = true;
        System.out.println("==[ begin header ]==");

        Image m1 = null;
        try {
            m1 = Image.getInstance("Z:/Recursos/img/Logo.png"); //habilitar
        } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
        }

        float m1w, m1h, m2w, m2h;
        m1w = m1h = m2w = m2h = 75;
        PdfPTable tablaEncabezado = null;
        PdfPCell celda = null;
        int col = 5;
        //++++++++++++++++++++ LOGO ++++++++++++++++++++++++++++++++++++
        tablaEncabezado = new PdfPTable(col);
        tablaEncabezado.setWidthPercentage(100);
        if (m1 != null) {
            m1.setAlignment(Image.MIDDLE);
            m1.scaleAbsolute(m1w, m1h);
            celda = new PdfPCell(m1);
            celda.setFixedHeight(m1h + 5);
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celda.setBorder(0);
            tablaEncabezado.addCell(celda);
        } else {
            celda = new PdfPCell(new Phrase("", pdf.font12n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celda.setBorder(0);
            tablaEncabezado.addCell(celda);
        }
        //IMPRIMIR(funcion, "2.2 Se cargan los Datos segun el perfil.", probar);
        //++++++++++++++++++++ NOMBRE DEL FORMATO   ++++++++++++++++++++
        PdfPTable tabla = new PdfPTable(1);

        celda = new PdfPCell(new Phrase((""), pdf.font12n));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celda.setBorder(0);
        tabla.addCell(celda);

        celda = new PdfPCell(new Phrase((""), pdf.font12n));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celda.setBorder(0);
        tabla.addCell(celda);

        celda = new PdfPCell(new Phrase((""), pdf.font12n));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celda.setBorder(0);
        tabla.addCell(celda);

        celda = new PdfPCell(new Phrase(("JOSE ARRIETA"), pdf.font12n));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celda.setBorder(0);
        tabla.addCell(celda);
        celda = new PdfPCell(new Phrase("Ortodoncia", pdf.font8n));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setVerticalAlignment(Element.ALIGN_TOP);
        celda.setBorder(0);
        tabla.addCell(celda);

        celda = new PdfPCell(new Phrase(("Odontólogo Pontificia Universidad Javeriana"), pdf.font8));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setVerticalAlignment(Element.ALIGN_TOP);
        celda.setBorder(0);
        tabla.addCell(celda);

        celda = new PdfPCell(new Phrase(("Ortodoncista U.C.C."), pdf.font8));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setVerticalAlignment(Element.ALIGN_TOP);
        celda.setBorder(0);
        tabla.addCell(celda);

        //IMPRIMIR(funcion, "2.3 Se agrega la informaciÃƒÆ’Ã‚Â³n a una tabla.", probar);
        celda = new PdfPCell(tabla);
        celda.setColspan(3);
        celda.setBorder(0);
        tablaEncabezado.addCell(celda);

        celda = new PdfPCell(new Phrase("", pdf.font12n));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celda.setBorder(0);
        tablaEncabezado.addCell(celda);

        if (!titulo.isEmpty()) {

            celda = new PdfPCell(new Phrase("", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celda.setColspan(col);
            celda.setBorder(0);
            tablaEncabezado.addCell(celda);
            celda = new PdfPCell(new Phrase(titulo.toUpperCase(), pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celda.setColspan(col);
            celda.setBorder(0);
            tablaEncabezado.addCell(celda);

            celda = new PdfPCell(new Phrase("", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celda.setColspan(col);
            celda.setBorder(0);
            tablaEncabezado.addCell(celda);

            celda = new PdfPCell(new Phrase("", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celda.setColspan(col);
            celda.setBorder(0);
            tablaEncabezado.addCell(celda);

        }
        System.out.println("==[ end header ]==");
        return tablaEncabezado;

    }

    public PdfPTable getEncabezadoxPaciente(
            String titulo, int tamanio, String paciente
    ) {

        String funcion = "getEncabezadoF()";
        boolean probar = true;
        try {
            ///img/add concepto.png
            Image m1 = Image.getInstance("Z:/Recursos/img/Logo.png");
            float m1w, m1h, m2w, m2h;
            m1w = m1h = m2w = m2h = 75;
            PdfPTable tablaEncabezado = null;
            PdfPCell celda = null;
            int col = 5;

            //++++++++++++++++++++ LOGO ++++++++++++++++++++++++++++++++++++
            tablaEncabezado = new PdfPTable(col);
            tablaEncabezado.setWidthPercentage(100);

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            celda = new PdfPCell(new Phrase("Paciente: " + Utilidades.decodificarElemento(paciente), pdf.font6));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celda.setBorder(0);
            celda.setColspan(3);
            tablaEncabezado.addCell(celda);
            String fecha = sdf.format(cal.getTime());
            celda = new PdfPCell(new Phrase("Fecha:" + fecha, pdf.font6));
            celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celda.setBorder(0);
            celda.setColspan(3);
            tablaEncabezado.addCell(celda);

            if (m1 != null) {
                m1.setAlignment(Image.MIDDLE);
                m1.scaleAbsolute(m1w, m1h);
                celda = new PdfPCell(m1);
                celda.setFixedHeight(m1h + 5);
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celda.setBorder(0);
                tablaEncabezado.addCell(celda);
            } else {
                celda = new PdfPCell(new Phrase("", pdf.font12n));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celda.setBorder(0);
                tablaEncabezado.addCell(celda);
            }
            //IMPRIMIR(funcion, "2.2 Se cargan los Datos segun el perfil.", probar);
            //++++++++++++++++++++ NOMBRE DEL FORMATO   ++++++++++++++++++++
            PdfPTable tabla = new PdfPTable(1);

            celda = new PdfPCell(new Phrase(("JOSE ARRIETA"), pdf.font12n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celda.setBorder(0);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("Ortodoncia", pdf.font8n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_TOP);
            celda.setBorder(0);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase(("Odontólogo Pontificia Universidad Javeriana"), pdf.font8));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_TOP);
            celda.setBorder(0);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase(("Ortodoncista U.C.C."), pdf.font8));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_TOP);
            celda.setBorder(0);
            tabla.addCell(celda);

            //IMPRIMIR(funcion, "2.3 Se agrega la informaciÃƒÆ’Ã‚Â³n a una tabla.", probar);
            celda = new PdfPCell(tabla);
            celda.setColspan(3);
            celda.setBorder(0);
            tablaEncabezado.addCell(celda);

            celda = new PdfPCell(new Phrase("", pdf.font12n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celda.setBorder(0);
            tablaEncabezado.addCell(celda);

            if (!titulo.equals("")) {
                celda = new PdfPCell(new Phrase(titulo.toUpperCase(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, tamanio)));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celda.setColspan(col);
                celda.setBorder(0);
                tablaEncabezado.addCell(celda);

            }
            System.out.println("*********************END ENCABEZADO***************************");
            return tablaEncabezado;

        } catch (Exception ex) {
            //IMPRIMIR(funcion, "    ERROR: " + ex, probar);
            System.out.println("ERROR --ENCABEZADO--> " + ex.toString());
            ex.printStackTrace();
            return null;
        }
    }

    public PdfPTable getPie() {
        String funcion = "getPie()";
        boolean probar = true;

        try {

            PdfPTable tablaPie = null;
            PdfPCell celda = null;

            tablaPie = new PdfPTable(1);
            tablaPie.setWidthPercentage(100);

            float[] tam = {2.5f, 8.0f, 3.5f};
            PdfPTable tabla = new PdfPTable(tam);
            tabla.setWidthPercentage(100);

            String lema = "";
            if (lema.equals("_") || lema.equals("-") || lema.equals("0")) {
                lema = "";
            }
            System.out.println("" + lema.indexOf("\""));
            if (!lema.equals("")) {
                if (lema.indexOf("\"") < 0) {
                    lema = "_CD_" + lema + "_CD_";
                }
            }

            String msj = "Centro Medico Perla del Caribe, Calle 22 No. 14 - 70 Cons. 5 - Tel: 431 81 95 - Cel: 300 202 9614 - Santa Marta, D.T.C.H.";
            celda = new PdfPCell(new Phrase("" + msj, pdf.font8n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celda.setBorder(0);
            celda.setColspan(3);

            tabla.addCell(celda);

//                celda = new PdfPCell(new Phrase("", pdf.font10));
//                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
//                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                celda.setBorder(0);
//                tabla.addCell(celda);
//
//                celda = new PdfPCell(new Phrase("", pdf.font6n));
//                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
//                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                celda.setBorder(0);
//                tabla.addCell(celda);                 
            celda = new PdfPCell(tabla);
            celda.setBorder(0);

            tablaPie.addCell(celda);

            System.out.println("----------------------END SETPIE-------------------------");
            return tablaPie;

        } catch (Exception ex) {
            //IMPRIMIR(funcion, "    ERROR: " + ex, probar);
            return null;
        }
    }

    private void infCitasdiario(Document documento, Map<String, String> list) {
        try {
            IMPRIMIR("+++++ ENTRE?");
            String consulta = "";
            consulta = "SELECT IF(paux.pk_paciente_auxiliar IS NULL, CONCAT(per.`pfk_tipo_documento`, per.`pk_persona`), paux.`pk_paciente_auxiliar`) ID,\n"
                    + "IF(paux.pk_paciente_auxiliar IS NULL,CONCAT_WS(' ', per.`primer_apellido`, IFNULL(per.`segundo_apellido`, ''), per.`primer_nombre`, IFNULL(per.`segundo_nombre`, '')),\n"
                    + "CONCAT_WS(' ', paux.`primer_apellido`, IFNULL(paux.`segundo_apellido`, ''), paux.`primer_nombre`, IFNULL(paux.`segundo_nombre`, ''))) PACIENTE, \n"
                    + "IFNULL(IF(paux.pk_paciente_auxiliar IS NULL, pertel.numero, paux.`telefono`), '') tel, cxh.`hora` HORA,cit.`estado` EST\n"
                    + "FROM `citas` cit\n"
                    + "LEFT JOIN `citasxhoras` cxh ON cxh.`pfk_paciente` = cit.`pfk_paciente` AND cxh.`pfk_cita` = cit.`pk_cita`\n"
                    + "LEFT JOIN `personas` per ON cxh.`pfk_paciente` = CONCAT (per.pfk_tipo_documento, per.pk_persona)\n"
                    + "LEFT JOIN `personas_telefonos` pertel ON pertel.pfk_persona = cxh.`pfk_paciente`\n"
                    + "LEFT JOIN `paciente_auxiliar`paux ON paux.`pk_paciente_auxiliar` = cit.`pfk_paciente` AND paux.`estado` = 'Activo'\n"
                    + "WHERE cit.`fecha_cita` = '" + list.get("fini") + "'\n"
                    + "ORDER BY ID, HORA";

            List<Map<String, String>> lista = resultquery.ListSQL(consulta);
            List<Map<String, String>> lista_paciente = new ArrayList<Map<String, String>>();
            List<Map<String, String>> lista_horas = new ArrayList<Map<String, String>>();
            List<Map<String, String>> lista_telefono = new ArrayList<Map<String, String>>();

            PdfPCell celda = null;
            float[] tam = new float[]{5, 15, 41, 16, 13, 10};

            PdfPTable tabla = new PdfPTable(tam);
            tabla.setWidthPercentage(100);
            celda = new PdfPCell(new Phrase("N°", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(15);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("IDENTIFICACION", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(15);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("NOMBRE", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(15);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("TELEFONOS", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(15);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("HORA CITA", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(15);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("ESTADO", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(15);
            tabla.addCell(celda);

            if (lista.size() > 0) {
                lista_paciente = gen.data_list(10, lista, new String[]{"ID"});
                String tel = "", horas = "";
                int i = 0;
                for (Map<String, String> lst : lista_paciente) {
                    System.out.println("***********************************" + i);
                    tel = "";
                    horas = "";
                    lista_horas = gen.data_list(1, lista, new String[]{"HORA"}, new String[]{"ID<->" + lst.get("ID")});
                    lista_telefono = gen.data_list(1, lista, new String[]{"tel"}, new String[]{"ID<->" + lst.get("ID")});
                    i++;

                    celda = new PdfPCell(new Phrase("" + (i), pdf.font10));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder(15);
                    tabla.addCell(celda);

                    celda = new PdfPCell(new Phrase("" + lst.get("ID"), pdf.font10));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder(15);
                    tabla.addCell(celda);
                    celda = new PdfPCell(new Phrase("" + lst.get("PACIENTE"), pdf.font10));
                    celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder(15);
                    tabla.addCell(celda);
                    System.out.println("lista_telefono--->" + lista_telefono.size());
                    for (Map<String, String> lt : lista_telefono) {
                        System.out.println("lt.get(\"tel\")->" + lt.get("tel"));
                        if (!lt.get("tel").equals("")) {
                            tel += (tel.equals("") ? "" : " - ") + lt.get("tel");
                        }
                    }
                    System.out.println("tel-->" + tel);
                    celda = new PdfPCell(new Phrase("" + tel, pdf.font10));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder(15);
                    tabla.addCell(celda);

                    if (lista_horas.size() > 0) {
                        horas = lista_horas.size() > 1 ? lista_horas.get(0).get("HORA") + " - " + lista_horas.get(lista_horas.size() - 1).get("HORA") : lista_horas.get(0).get("HORA");
                    }

                    celda = new PdfPCell(new Phrase("" + horas, pdf.font10));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder(15);
                    tabla.addCell(celda);

                    celda = new PdfPCell(new Phrase("" + lst.get("EST"), pdf.font10));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder(15);
                    tabla.addCell(celda);
                }

            } else {
                celda = new PdfPCell(new Phrase("\n No se encontraron registros.\n\n", pdf.font15n));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                celda.setBorder(15);
                celda.setColspan(tam.length);
                tabla.addCell(celda);
            }
            documento.add(tabla);
            IMPRIMIR("!!!!! SE SUPONE QUE YA LO CREE!!!");
        } catch (Exception ex) {
            ex.printStackTrace();
            IMPRIMIR(" ERROR ----- ERROR-----");
        }
    }

    private void infCitasHistorico(Document documento, Map<String, String> list) {
        try {

            String cons = "";
            IMPRIMIR("???????" + list.get("idpac"));

            cons = "SELECT CONCAT_WS(' ', per.`primer_nombre`, IFNULL (per.`segundo_nombre`,''), per.`primer_apellido`, IFNULL (per.`segundo_apellido`,'')) NOMBRE, cit.`fecha_cita` FECHA, cxh.`hora` HORA, cit.`estado` ESTADO, cit.`motivo` MOTIVO  \n"
                    + "FROM `citas` cit\n"
                    + "LEFT JOIN `citasxhoras` cxh ON cit.`pfk_paciente` = cxh.`pfk_paciente` AND cxh.`pfk_cita` = cit.`pk_cita`\n"
                    + "LEFT JOIN `pacientes` pac ON cit.`pfk_paciente` = pac.`pfk_paciente`\n"
                    + "LEFT JOIN `personas` per ON CONCAT(per.`pfk_tipo_documento`, per.`pk_persona`) = pac.`pfk_paciente`\n"
                    + "WHERE pac.`pfk_paciente` ='" + list.get("idpac") + "'";

            List<Map<String, String>> lista = resultquery.ListSQL(cons);

            ArrayList<String[]> resc = resultquery.SELECT(cons);
            IMPRIMIR("resotadpo consulya" + resc.size());
            PdfPCell celda = null;
            float[] tam = new float[]{7, 33, 25, 15, 20};
            System.out.println("tam1-->" + (100 / 5));
            float[] tamp = new float[]{15, 85};

            PdfPTable tablaP = new PdfPTable(tamp);
            tablaP.setWidthPercentage(100);

            documento.add(tablaP);
            PdfPTable tabla = new PdfPTable(tam);

            tabla.setWidthPercentage(100);

//            celda = new PdfPCell(new Phrase("HISTORICO DE CITAS",pdf.font12n));
//            celda.setColspan(5);
//            celda.setBorder(0);
//            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("", pdf.font12n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(0);
            celda.setColspan(tamp.length);
            tablaP.addCell(celda);

            celda = new PdfPCell(new Phrase("PACIENTE: ", pdf.font12n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(0);
            tablaP.addCell(celda);
            celda = new PdfPCell(new Phrase(" " + resc.get(0)[0], pdf.font12));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(0);
            tablaP.addCell(celda);

            celda = new PdfPCell(new Phrase("\n\n", pdf.font12n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(0);
            celda.setColspan(tamp.length);
            tablaP.addCell(celda);

            documento.add(tablaP);
//////

            celda = new PdfPCell(new Phrase("N°", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(15);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("FECHA CITA", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(15);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("HORA", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(15);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("ESTADO", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(15);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("MOTIVO", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(15);
            tabla.addCell(celda);
            List<Map<String, String>> lista_fecha = new ArrayList<Map<String, String>>();
            List<Map<String, String>> lista_hora = new ArrayList<Map<String, String>>();

            if (lista.size() > 0) {
                lista_fecha = gen.data_list(10, lista, new String[]{"FECHA"});
                int i = 0;
                String hora = "";
                for (Map<String, String> lst : lista_fecha) {
                    lista_hora = gen.data_list(1, lista, new String[]{"HORA"}, new String[]{"FECHA<->" + lst.get("FECHA")});
                    i++;
                    hora = "";
                    celda = new PdfPCell(new Phrase("" + (i), pdf.font10));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder(15);
                    tabla.addCell(celda);
                    celda = new PdfPCell(new Phrase("" + lst.get("FECHA"), pdf.font10));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder(15);
                    tabla.addCell(celda);

                    if (lista_hora.size() > 0) {
                        hora = lista_hora.size() > 1 ? lista_hora.get(0).get("HORA") + " - " + lista_hora.get(lista_hora.size() - 1).get("HORA") : lista_hora.get(0).get("HORA");
                    }

                    celda = new PdfPCell(new Phrase("" + hora, pdf.font10));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder(15);
                    tabla.addCell(celda);
                    celda = new PdfPCell(new Phrase("" + lst.get("ESTADO"), pdf.font10));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder(15);
                    tabla.addCell(celda);
                    celda = new PdfPCell(new Phrase("" + lst.get("MOTIVO"), pdf.font10));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder(15);
                    tabla.addCell(celda);
                }
            } else {
                celda = new PdfPCell(new Phrase("\n No se encontraron registros.\n\n", pdf.font15n));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                celda.setBorder(15);
                celda.setColspan(tam.length);
                tabla.addCell(celda);

            }

            documento.add(tabla);
            IMPRIMIR("!!!!! SE SUPONE QUE YA LO CREE!!!");

        } catch (Exception ex) {
            ex.printStackTrace();
            IMPRIMIR(" ERRORO ERRORO");
        }

    }

    private void infDemandaind(Document documento, Map<String, String> list) {
        try {
            IMPRIMIR("+++++ ENTRE?");
            IMPRIMIR("fini--->" + list.get("fini"));
            String consulta = "";

            consulta = "SELECT IF(paux.pk_paciente_auxiliar IS NULL,CONCAT_WS(' ', per.`primer_apellido`, IFNULL(per.`segundo_apellido`, ''), per.`primer_nombre`, IFNULL(per.`segundo_nombre`, '')),\n"
                    + "CONCAT_WS(' ', paux.`primer_apellido`, IFNULL(paux.`segundo_apellido`, ''), paux.`primer_nombre`, IFNULL(paux.`segundo_nombre`, ''))) PACIENTE,\n"
                    + "IFNULL(DATE_FORMAT(ultc.fecha_cita, '%d/%m/%Y'), '') FECHA, \n"
                    + "REPLACE(GROUP_CONCAT(IF(paux.`pk_paciente_auxiliar` IS NULL, IFNULL(pertel.`numero`, ''), IFNULL(REPLACE(paux.`telefono`, '<>', ','), ''))), ',', '<>') TELEFONOS, ultc.estado ESTADO \n"
                    + "FROM (\n"
                    + "SELECT pfk_paciente, cit.estado, `fecha_cita`, IFNULL(paux.`estado`, '') EST\n"
                    + "FROM `citas` cit \n"
                    + "LEFT JOIN paciente_auxiliar paux ON cit.pfk_paciente = paux.pk_paciente_auxiliar\n"
                    + "GROUP BY pfk_paciente DESC) ultc \n"
                    + "LEFT JOIN paciente_auxiliar paux ON ultc.pfk_paciente = paux.pk_paciente_auxiliar AND paux.estado = 'Activo'\n"
                    + "LEFT JOIN personas per ON ultc.pfk_paciente = CONCAT (per.pfk_tipo_documento, per.pk_persona)\n"
                    + "LEFT JOIN personas_telefonos pertel ON ultc.pfk_paciente = pertel.`pfk_persona`\n"
                    + "WHERE ultc.estado <> 'Activo' AND ultc.estado <> 'Terminado' AND ultc.EST <> 'Inactivo'\n"
                    + "AND ultc.fecha_cita <= DATE_SUB('" + list.get("fini") + "',INTERVAL 1 MONTH)\n"
                    + "GROUP BY ultc.pfk_paciente \n"
                    + "ORDER BY ultc.fecha_cita ASC, PACIENTE ASC";

            consulta = "SELECT IF(paux.pk_paciente_auxiliar IS NULL,CONCAT_WS(' ', per.`primer_apellido`, IFNULL(per.`segundo_apellido`, ''), per.`primer_nombre`, IFNULL(per.`segundo_nombre`, '')),\n"
                    + "CONCAT_WS(' ', paux.`primer_apellido`, IFNULL(paux.`segundo_apellido`, ''), paux.`primer_nombre`, IFNULL(paux.`segundo_nombre`, ''))) PACIENTE,\n"
                    + "IFNULL(DATE_FORMAT(ultc.fecha_cita, '%d/%m/%Y'), '') FECHA, \n"
                    + "REPLACE(GROUP_CONCAT(IF(paux.`pk_paciente_auxiliar` IS NULL, IFNULL(pertel.`numero`, ''), IFNULL(REPLACE(paux.`telefono`, '<>', ','), ''))), ',', '<>') TELEFONOS, ultc.estado ESTADO \n"
                    + "FROM (\n"
                    + "SELECT pfk_paciente, cit.estado, `fecha_cita`, IFNULL(paux.`estado`, '') EST\n"
                    + "FROM `citas` cit \n"
                    + "LEFT JOIN paciente_auxiliar paux ON cit.pfk_paciente = paux.pk_paciente_auxiliar\n"
                    + "GROUP BY pfk_paciente DESC) ultc \n"
                    + "INNER JOIN historias_clinicas hc ON hc.`pfk_paciente` = ultc.pfk_paciente AND hc.`estado` = 'Activa'\n"
                    + "LEFT JOIN paciente_auxiliar paux ON ultc.pfk_paciente = paux.pk_paciente_auxiliar AND paux.estado = 'Activo'\n"
                    + "LEFT JOIN personas per ON ultc.pfk_paciente = CONCAT (per.pfk_tipo_documento, per.pk_persona)\n"
                    + "LEFT JOIN personas_telefonos pertel ON ultc.pfk_paciente = pertel.`pfk_persona`\n"
                    + "WHERE ultc.estado <> 'Activo' AND ultc.estado <> 'Terminado' AND ultc.EST <> 'Inactivo'\n"
                    + "AND ultc.fecha_cita <= '" + list.get("fini") + "'\n"
                    + "GROUP BY ultc.pfk_paciente \n"
                    + "ORDER BY ultc.fecha_cita ASC, PACIENTE ASC";
            String filtro = "";
            if (list.get("chAusentes").equals("1")) {
                filtro = "'Espera', 'Ausente'";
            }
            if (list.get("chCancelados").equals("1")) {
                filtro += (!filtro.equals("") ? ", " : "") + "'Cancelada'";
            }
            if (list.get("chAtendidos").equals("1")) {
                filtro += (!filtro.equals("") ? ", " : "") + "'Terminado', 'Atendiendo'";
            }
            consulta = "SELECT \n"
                    + "/*IF(paux.pk_paciente_auxiliar IS NULL, ultc.pfk_paciente, paux.`pk_paciente_auxiliar`) ID,*/\n"
                    + "IF(paux.pk_paciente_auxiliar IS NULL,CONCAT_WS(' ', per.`primer_apellido`, IFNULL(per.`segundo_apellido`, ''), per.`primer_nombre`, IFNULL(per.`segundo_nombre`, '')),\n"
                    + "CONCAT_WS(' ', paux.`primer_apellido`, IFNULL(paux.`segundo_apellido`, ''), paux.`primer_nombre`, IFNULL(paux.`segundo_nombre`, ''))) PACIENTE,\n"
                    + "IFNULL(DATE_FORMAT(cit.fecha_cita, '%d/%m/%Y'), '') FECHA, \n"
                    + "REPLACE(GROUP_CONCAT(IF(paux.`pk_paciente_auxiliar` IS NULL, IFNULL(pertel.`numero`, ''), IFNULL(REPLACE(paux.`telefono`, '<>', ','), ''))), ',', '<>') TELEFONOS, cit.estado ESTADO \n"
                    + "FROM (\n"
                    + "SELECT pfk_paciente, MAX(CAST(cit.`pk_cita` AS UNSIGNED)) pk_cita, IFNULL(paux.`estado`, '') EST\n"
                    + "FROM `citas` cit \n"
                    + "LEFT JOIN paciente_auxiliar paux ON cit.pfk_paciente = paux.pk_paciente_auxiliar\n"
                    + "GROUP BY pfk_paciente \n"
                    + ") ultc \n"
                    + "INNER JOIN citas cit ON cit.`pfk_paciente` = ultc.pfk_paciente AND cit.`pk_cita` = ultc.pk_cita\n"
                    + "INNER JOIN historias_clinicas hc ON hc.`pfk_paciente` = ultc.pfk_paciente AND hc.`estado` = 'Activa'\n"
                    + "LEFT JOIN paciente_auxiliar paux ON ultc.pfk_paciente = paux.pk_paciente_auxiliar AND paux.estado = 'Activo'\n"
                    + "LEFT JOIN personas per ON ultc.pfk_paciente = CONCAT (per.pfk_tipo_documento, per.pk_persona)\n"
                    + "LEFT JOIN personas_telefonos pertel ON ultc.pfk_paciente = pertel.`pfk_persona`\n"
                    + "WHERE cit.estado <> 'Activo' AND ultc.EST <> 'Inactivo'\n"
                    + "AND cit.fecha_cita BETWEEN '" + list.get("fini") + "' AND '" + list.get("ffin") + "' \n"
                    + "AND cit.estado IN (" + filtro + ")\n"
                    + "GROUP BY ultc.pfk_paciente \n"
                    + "ORDER BY PACIENTE ASC, cit.fecha_cita ASC";

            System.out.println("colsulta---" + consulta);

            ArrayList<String[]> resql = resultquery.SELECT(consulta);

            PdfPCell celda = null;
            float[] tam = new float[]{4, 36, 10, 13, 15, 11, 11};

            if (list.get("chAusentes").equals("1")) {
                tam = new float[]{4, 46, 13, 20, 17};
            }
            if (list.get("chCancelados").equals("1")) {
                tam = new float[]{4, 46, 13, 20, 17};
            }
            if (list.get("chAtendidos").equals("1")) {
                tam = new float[]{4, 46, 13, 20, 17};
            }

            if (list.get("chAusentes").equals("1") && list.get("chCancelados").equals("1")) {
                tam = new float[]{4, 41, 11, 18, 11, 15};
            }
            if (list.get("chAusentes").equals("1") && list.get("chAtendidos").equals("1")) {
                tam = new float[]{4, 44, 12, 18, 11, 11};
            }
            if (list.get("chCancelados").equals("1") && list.get("chAtendidos").equals("1")) {
                tam = new float[]{4, 41, 11, 18, 15, 11};
            }

            if (list.get("chAusentes").equals("1") && list.get("chCancelados").equals("1") && list.get("chAtendidos").equals("1")) {
                tam = new float[]{4, 36, 10, 13, 11, 15, 11};
            }

            PdfPTable tabla = new PdfPTable(tam);
            tabla.setWidthPercentage(100);
            celda = new PdfPCell(new Phrase("N°", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(15);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("NOMBRE", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(15);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("FECHA", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(15);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("TELEFONOS", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(15);
            tabla.addCell(celda);
            if (list.get("chAusentes").equals("1")) {
                celda = new PdfPCell(new Phrase("AUSENTE", pdf.font10n));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
                celda.setBorder(15);
                tabla.addCell(celda);
            }
            if (list.get("chCancelados").equals("1")) {
                celda = new PdfPCell(new Phrase("CANCELADO", pdf.font10n));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
                celda.setBorder(15);
                tabla.addCell(celda);
            }

            if (list.get("chAtendidos").equals("1")) {
                celda = new PdfPCell(new Phrase("ATENDIDO", pdf.font10n));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
                celda.setBorder(15);
                tabla.addCell(celda);
            }

            if (resql.size() > 0) {
                for (int i = 0; i < resql.size(); i++) {
                    celda = new PdfPCell(new Phrase("" + (i + 1), pdf.font10));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder(15);
                    tabla.addCell(celda);
                    celda = new PdfPCell(new Phrase("" + Utilidades.CapitaliceTexto(resql.get(i)[0]), pdf.font10));
                    celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                    celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    celda.setBorder(15);
                    tabla.addCell(celda);
                    celda = new PdfPCell(new Phrase("" + resql.get(i)[1], pdf.font10));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    celda.setBorder(15);
                    tabla.addCell(celda);
                    celda = new PdfPCell(new Phrase("" + resql.get(i)[2].replace("<>", "\n"), pdf.font10));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder(15);
                    tabla.addCell(celda);
                    if (list.get("chAusentes").equals("1")) {
                        celda = new PdfPCell(new Phrase("" + (resql.get(i)[3].equals("Ausente") || resql.get(i)[3].equals("Espera") ? "X" : ""), pdf.font10));
                        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        celda.setBorder(15);
                        tabla.addCell(celda);
                    }
                    if (list.get("chCancelados").equals("1")) {
                        celda = new PdfPCell(new Phrase("" + (resql.get(i)[3].equals("Cancelada") ? "X" : ""), pdf.font10));
                        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        celda.setBorder(15);
                        tabla.addCell(celda);
                    }
                    if (list.get("chAtendidos").equals("1")) {
                        celda = new PdfPCell(new Phrase("" + (resql.get(i)[3].equals("Terminado") || resql.get(i)[3].equals("Atendiendo") ? "X" : ""), pdf.font10));
                        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        celda.setBorder(15);
                        tabla.addCell(celda);
                    }
                }
            } else {
                celda = new PdfPCell(new Phrase("\n No se encontraron registros.\n\n", pdf.font15n));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                celda.setBorder(15);
                celda.setColspan(tam.length);
                tabla.addCell(celda);
            }

            documento.add(tabla);
            IMPRIMIR("!!!!! SE SUPONE QUE YA LO CREE!!!");
        } catch (Exception ex) {
            ex.printStackTrace();
            IMPRIMIR(" ERROR ----- ERROR-----");
        }
    }

    private void infHistimprimirOLD(Document documento, Map<String, String> list) {
        try {
            IMPRIMIR("+++++ ENTRE?");
            String consulta = "";

            //<editor-fold defaultstate="collapsed" desc="AMNANESIS">
            consulta = "SELECT daba.`pk_datos_basicos` ID,daba.`descripcion` DESCRIPCION,\n"
                    + "CASE WHEN anpa.`estado` = 'SI' THEN 'x' ELSE ' ' END SI, CASE WHEN anpa.`estado` = 'NO' THEN 'x' ELSE ' ' END N_O, CASE WHEN anpa.`estado` = 'NO SABE' THEN 'x' ELSE ' ' END NO_SABE\n"
                    + "FROM anamnesis anam, anamnesisxpaciente anpa, datos_basicos daba\n"
                    + "WHERE anam.`pfk_paciente` = anpa.`pfk_paciente` AND anam.`pk_anamnesis` = anpa.`pfk_anamnesis`\n"
                    + "AND daba.`pk_datos_basicos` = anpa.`pfk_datos_basicos` AND anam.`pfk_paciente` = '" + list.get("idpac") + "'\n"
                    + "AND anam.`pk_anamnesis` = (SELECT MAX(pk_anamnesis) FROM anamnesis WHERE pfk_paciente = '" + list.get("idpac") + "')\n"
                    + "UNION\n"
                    + "SELECT daba.`pk_datos_basicos` ID, daba.`descripcion` DESCRIPCION,\n"
                    + "' ' SI, ' ' N_O, ' ' NO_SABE\n"
                    + "FROM datos_basicos daba\n"
                    + "WHERE daba.`pk_datos_basicos` = 13";

            List<Map<String, String>> lista_amnanesis = resultquery.ListSQL(consulta);
            //</editor-fold>

            consulta = "SELECT pk_historia_clinica FROM historias_clinicas WHERE pfk_paciente = '" + list.get("idpac") + "'";
            String idhc = resultquery.unicoDato(consulta);

            consulta = "SELECT max(pfk_odontograma) FROM hc_odontograma WHERE pfk_historia_clinica = " + idhc;
            String ultOdontograma = resultquery.unicoDato(consulta);

            //<editor-fold defaultstate="collapsed" desc="ANALISIS SAGITAL">
            consulta = "SELECT angulo_incisivo_inferior AS ANGULO_INC_INF, lado AS LADO, tipo_diente AS TIPO_DIENTE, clase AS CLASE\n"
                    + "FROM historias_clinicas hicl, personas pers, hc_analisis_sagital hcas, hc_swing hcsw\n"
                    + "WHERE CONCAT(pers.`pfk_tipo_documento`,pers.`pk_persona`) = hicl.`pfk_paciente`\n"
                    + "AND hicl.`pk_historia_clinica` =  hcas.`pfk_historia_clinica` AND hcas.`pfk_swing` = hcsw.`pk_swing`\n "
                    + "AND CONCAT(pers.pfk_tipo_documento, pers.`pk_persona`) = '" + list.get("idpac") + "'";

            List<Map<String, String>> lista_sagital = resultquery.ListSQL(consulta);

            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="ANALISIS VERTICAL">
            consulta = "SELECT\n"
                    + "angulo_vertical AS ANG_VERT,\n"
                    + "angulo_goniano AS ANG_GONI,\n"
                    + "longitud_de_rama AS LONG_RAMA\n"
                    + "FROM\n"
                    + "historias_clinicas hicl,\n"
                    + "hc_analisis_vertical hcav,\n"
                    + "personas pers\n"
                    + "WHERE\n"
                    + "CONCAT(pers.`pfk_tipo_documento`,pers.`pk_persona`) = hicl.`pfk_paciente`\n"
                    + "AND hicl.`pk_historia_clinica` =  hcav.`pfk_historia_clinica`\n"
                    + "AND CONCAT(pers.pfk_tipo_documento, pers.`pk_persona`) = '" + list.get("idpac") + "'";
            List<Map<String, String>> lista_vertical = resultquery.ListSQL(consulta);
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="ANALISIS TRANSVERSAL">
            consulta = "SELECT\n"
                    + "linea_m_superior AS LINEA_MSUP,\n"
                    + "CASE WHEN linea_m_superior =  'C' THEN 'A' ELSE direccion_m_superior END AS DIR_MSUP,\n"
                    + "CASE WHEN linea_m_superior =  'C' THEN 'A' ELSE longitud_m_superior END AS LONG_MSUP,\n"
                    + "linea_m_inferior AS LINEA_MINF,\n"
                    + "CASE WHEN linea_m_inferior =  'C' THEN 'A' ELSE direccion_m_inferior END AS DIR_MINF,\n"
                    + "CASE WHEN linea_m_inferior =  'C' THEN 'A' ELSE longitud_m_inferior END AS LONG_MINF,\n"
                    + "hab AS HAB,\n"
                    + "interconsulta AS INTCON,\n"
                    + "observacion AS OBS,\n"
                    + "examenes AS EXAM,\n"
                    + "simetria AS SIM,\n"
                    + "pronostico AS PRONOS\n"
                    + "FROM\n"
                    + "historias_clinicas hicl,\n"
                    + "personas pers,\n"
                    + "hc_ant_tra hcat\n"
                    + "WHERE\n"
                    + "CONCAT(pers.`pfk_tipo_documento`,pers.`pk_persona`) = hicl.`pfk_paciente`\n"
                    + "AND hicl.`pk_historia_clinica` =  hcat.`pfk_historia_clinica`\n"
                    + "AND CONCAT(pers.pfk_tipo_documento, pers.`pk_persona`) = '" + list.get("idpac") + "'";
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="FORMA DE ARCO">
            consulta = "SELECT\n"
                    + "superior AS SUP,\n"
                    + "inferior AS INF,\n"
                    + "curva AS CURVA,\n"
                    + "overbite AS OVERB,\n"
                    + "overjet AS OVERJ\n"
                    + "FROM\n"
                    + "historias_clinicas hicl,\n"
                    + "personas pers,\n"
                    + "hc_forma_arco hcfa\n"
                    + "WHERE\n"
                    + "CONCAT(pers.`pfk_tipo_documento`,pers.`pk_persona`) = hicl.`pfk_paciente`\n"
                    + "AND hicl.`pk_historia_clinica` =  hcfa.`pfk_historia_clinica`\n"
                    + "AND CONCAT(pers.pfk_tipo_documento, pers.`pk_persona`) = '" + list.get("idpac") + "'";
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="ODONTOGRAMA">
            consulta = "SELECT pfk_diente, posicion, estado, ausente\n"
                    + "FROM hc_odontograma\n"
                    + "WHERE pfk_historia_clinica = " + idhc + " AND pfk_odontograma = " + ultOdontograma;

            List<Map<String, String>> lista_odontograma = resultquery.ListSQL(consulta);

            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="SEGUIMIENTO AL TRATAMIENTO">
            consulta = "SELECT seg.`fecha_seguimiento`, seg.`observaciones`, seg.`abono`,seg.`saldo`\n"
                    + "FROM seguimiento_del_tratamiento seg\n"
                    + "WHERE pfk_historia_clinica = '" + idhc + "' AND pfk_tratamiento = ''\n"
                    + "ORDER BY seg.`fecha_seguimiento` ASC";

            List<Map<String, String>> lista_seguimiento = resultquery.ListSQL(consulta);
            //</editor-fold> 

            //<editor-fold defaultstate="collapsed" desc="IMAGENES">
            consulta = "SELECT pfk_tipo_imagen TIPO,CONCAT(url_imagen,'/',nombre) RUTA\n"
                    + "FROM hc_imagenes hcim, historias_clinicas hc\n"
                    + "WHERE hcim.pfk_historia_clinica = hc.pk_historia_clinica AND hc.pfk_paciente = '" + list.get("idpac") + "'\n"
                    + "ORDER BY pfk_tipo_imagen, consecutivo DESC ";

            List<Map<String, String>> lista_imagenes = resultquery.ListSQL(consulta);
            //</editor-fold> 

            //<editor-fold defaultstate="collapsed" desc="DATOS PERSONALES">
            consulta = "SELECT CONCAT_WS(' ', per.`primer_nombre`, IFNULL (per.`segundo_nombre`, '')) NOMBRES, \n"
                    + "CONCAT_WS(' ', per.`primer_apellido`, IFNULL (per.`segundo_apellido`, '')) APELLIDOS,\n"
                    + " per.`fecha_de_nacimiento`, per.`pfk_tipo_documento`, per.`pk_persona`,  REPLACE (GROUP_CONCAT(pertel.`numero`), ',', ' - ') TELEFONO, \n"
                    + " per.`direccion`,per.`correo_electronico`\n"
                    + "FROM personas per\n"
                    + "INNER JOIN personas_telefonos pertel ON CONCAT(per.`pfk_tipo_documento`, per.`pk_persona`) = pertel.`pfk_persona`\n"
                    + "WHERE CONCAT (per.`pfk_tipo_documento`, per.`pk_persona`) =  'CC578893782' \n"
                    + "GROUP BY CONCAT (per.`pfk_tipo_documento`, per.`pk_persona`) ";

            List<Map<String, String>> lista_datosper = resultquery.ListSQL(consulta);
            //</editor-fold>

            PdfPCell celda = null;
            float[] tam = new float[]{10, 35, 20, 15, 10, 10};

            PdfPTable tabla = new PdfPTable(tam);
            tabla.setWidthPercentage(100);
            celda = new PdfPCell(new Phrase("N°", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(15);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("NOMBRE", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(15);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("TELEFONO", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(15);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("CANCELADO", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(15);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("AUSENTE", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(15);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("ATENDIDO", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(15);
            tabla.addCell(celda);

            documento.add(tabla);
            IMPRIMIR("!!!!! SE SUPONE QUE YA LO CREE!!!");
        } catch (Exception ex) {
            ex.printStackTrace();
            IMPRIMIR(" ERROR ----- ERROR-----");
        }
    }

    private void infHistimprimir(Document documento, Map<String, String> list) {
        try {
            IMPRIMIR("+++++ ENTRE?");
            String consulta = "";
            float m1w, m1h, mw, mh;
            //<editor-fold defaultstate="collapsed" desc="AMNANESIS">
            consulta = "SELECT anam.motivo_consulta, daba.`pk_datos_basicos` ID,daba.`descripcion` DESCRIPCION,\n"
                    + "CASE WHEN anpa.`estado` = 'SI' THEN 'x' ELSE ' ' END SI, CASE WHEN anpa.`estado` = 'NO' THEN 'x' ELSE ' ' END N_O, CASE WHEN anpa.`estado` = 'NO SABE' THEN 'x' ELSE ' ' END NO_SABE\n"
                    + "FROM anamnesis anam, anamnesisxpaciente anpa, datos_basicos daba\n"
                    + "WHERE anam.`pfk_paciente` = anpa.`pfk_paciente` AND anam.`pk_anamnesis` = anpa.`pfk_anamnesis`\n"
                    + "AND daba.`pk_datos_basicos` = anpa.`pfk_datos_basicos` AND anam.`pfk_paciente` = '" + list.get("idpac") + "'\n"
                    + "AND anam.`pk_anamnesis` = (SELECT MAX(pk_anamnesis) FROM anamnesis WHERE pfk_paciente = '" + list.get("idpac") + "')\n"
                    + "";

            List<Map<String, String>> lista_amnanesis = resultquery.ListSQL(consulta);
            //</editor-fold>

            consulta = "SELECT pk_historia_clinica FROM historias_clinicas WHERE pfk_paciente = '" + list.get("idpac") + "'";
            String idhc = resultquery.unicoDato(consulta);

            consulta = "SELECT max(pfk_odontograma) FROM hc_odontograma WHERE pfk_historia_clinica = " + idhc;
            String ultOdontograma = resultquery.unicoDato(consulta);

            //<editor-fold defaultstate="collapsed" desc="ANALISIS SAGITAL">
            System.out.println("*****************ANALISIS SAGITAL****************");
            consulta = "SELECT angulo_incisivo_inferior AS ANGULO_INC_INF,lado AS LADO,tipo_diente AS TIPO_DIENTE,clase AS CLASE\n"
                    + "FROM historias_clinicas hicl,hc_analisis_sagital hcas, hc_swing hcsw\n"
                    + "WHERE hicl.`pk_historia_clinica` = hcas.`pfk_historia_clinica` AND hcas.`pfk_historia_clinica` = hcsw.`pfk_historia_clinica`\n"
                    + "AND hcas.`pfk_swing` = hcsw.`pk_swing` AND hicl.`pfk_paciente` = '" + list.get("idpac") + "'";

            List<Map<String, String>> lista_sagital = resultquery.ListSQL(consulta);

            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="ANALISIS VERTICAL">
            System.out.println("*****************ANALISIS VERTICAL****************");
            consulta = "SELECT angulo_vertical AS ANG_VERT, angulo_goniano AS ANG_GONI, longitud_de_rama AS LONG_RAMA\n"
                    + "FROM historias_clinicas hicl, hc_analisis_vertical hcav, personas pers\n"
                    + "WHERE CONCAT(pers.`pfk_tipo_documento`,pers.`pk_persona`) = hicl.`pfk_paciente`\n"
                    + "AND hicl.`pk_historia_clinica` =  hcav.`pfk_historia_clinica`\n"
                    + "AND CONCAT(pers.pfk_tipo_documento, pers.`pk_persona`) = '" + list.get("idpac") + "'";
            List<Map<String, String>> lista_vertical = resultquery.ListSQL(consulta);
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="ANALISIS TRANSVERSAL">
            System.out.println("*****************ANALISIS TRANSVERSAL****************");
            consulta = "SELECT\n"
                    + "linea_m_superior AS LINEA_MSUP,\n"
                    + "CASE WHEN linea_m_superior =  'C' THEN 'A' ELSE direccion_m_superior END AS DIR_MSUP,\n"
                    + "CASE WHEN linea_m_superior =  'C' THEN 'A' ELSE longitud_m_superior END AS LONG_MSUP,\n"
                    + "linea_m_inferior AS LINEA_MINF,\n"
                    + "CASE WHEN linea_m_inferior =  'C' THEN 'A' ELSE direccion_m_inferior END AS DIR_MINF,\n"
                    + "CASE WHEN linea_m_inferior =  'C' THEN 'A' ELSE longitud_m_inferior END AS LONG_MINF,\n"
                    + "hab AS HAB,\n"
                    + "interconsulta AS INTCON,\n"
                    + "observacion AS OBS,\n"
                    + "examenes AS EXAM,\n"
                    + "simetria AS SIM,\n"
                    + "pronostico AS PRONOS\n"
                    + "FROM\n"
                    + "historias_clinicas hicl,\n"
                    + "personas pers,\n"
                    + "hc_ant_tra hcat\n"
                    + "WHERE\n"
                    + "CONCAT(pers.`pfk_tipo_documento`,pers.`pk_persona`) = hicl.`pfk_paciente`\n"
                    + "AND hicl.`pk_historia_clinica` =  hcat.`pfk_historia_clinica`\n"
                    + "AND CONCAT(pers.pfk_tipo_documento, pers.`pk_persona`) = '" + list.get("idpac") + "'";
            List<Map<String, String>> lista_transversal = resultquery.ListSQL(consulta);
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="FORMA DE ARCO">
            System.out.println("*****************FORMA DE ARCO****************");
            consulta = "SELECT\n"
                    + "superior AS SUP,\n"
                    + "inferior AS INF,\n"
                    + "curva AS CURVA,\n"
                    + "overbite AS OVERB,\n"
                    + "overjet AS OVERJ\n"
                    + "FROM\n"
                    + "historias_clinicas hicl,\n"
                    + "personas pers,\n"
                    + "hc_forma_arco hcfa\n"
                    + "WHERE\n"
                    + "CONCAT(pers.`pfk_tipo_documento`,pers.`pk_persona`) = hicl.`pfk_paciente`\n"
                    + "AND hicl.`pk_historia_clinica` =  hcfa.`pfk_historia_clinica`\n"
                    + "AND CONCAT(pers.pfk_tipo_documento, pers.`pk_persona`) = '" + list.get("idpac") + "'";
            List<Map<String, String>> lista_formarco = resultquery.ListSQL(consulta);
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="ODONTOGRAMA">
//            consulta = "SELECT pfk_diente, posicion, estado, ausente\n"
//                    + "FROM hc_odontograma\n"
//                    + "WHERE pfk_historia_clinica = " + idhc + " AND pfk_odontograma = " + ultOdontograma;
//            
//            List<Map<String,String>> lista_odontograma = resultquery.ListSQL(consulta);
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="SEGUIMIENTO AL TRATAMIENTO">
//            consulta = "SELECT seg.`fecha_seguimiento`, seg.`observaciones`, seg.`abono`,seg.`saldo`\n" +
//                        "FROM seguimiento_del_tratamiento seg\n" +
//                        "WHERE pfk_historia_clinica = '" + idhc +"' /*AND pfk_tratamiento = ''*/ \n" +
//                        "ORDER BY seg.`fecha_seguimiento` ASC";
            consulta = "SELECT IFNULL(pxt.fk_tratamiento, '')  ID_TRA,tra.`descripcion`  DESC_TRA, pac.`pfk_paciente` ID_PAC,\n"
                    + "CONCAT_WS(' ', per.`primer_nombre`, IFNULL(per.`segundo_nombre`,''), per.`primer_apellido`, IFNULL(per.`segundo_apellido`,'')) PACIENTE,\n"
                    + "DATE_FORMAT(seg.`fecha_seguimiento`, '%Y/%m/%d') FECHA, seg.`observaciones` AS OBSERVACION,seg.`abono` AS ABONO, seg.`saldo` AS SALDO, seg.`usuario` AS USER\n"
                    + ", IFNULL(pxt.costo, 0) COSTO, IF(pxt.cuota_inicial IS NULL OR pxt.cuota_inicial = '', 0, pxt.cuota_inicial) CUOTA_I\n"
                    + "FROM pacientextratamiento pxt\n"
                    + "LEFT JOIN `seguimiento_del_tratamiento` seg ON seg.pfk_paciente =  pxt.pfk_paciente AND seg.pfk_tratamiento = pxt.fk_tratamiento \n"
                    + "LEFT JOIN `tratamientos` tra ON tra.`pk_tratamiento` =pxt.fk_tratamiento\n"
                    + "INNER JOIN pacientes pac ON pac.`pfk_paciente` = pxt.`pfk_paciente`\n"
                    + "INNER JOIN personas per ON CONCAT (per.`pfk_tipo_documento`, per.`pk_persona`) = pac.`pfk_paciente`\n"
                    + "WHERE pxt.`pfk_paciente` = '" + list.get("idpac") + "' AND pxt.estado = 'Activo'\n"
                    + "ORDER BY seg.`consecutivo` ASC";

            List<Map<String, String>> lista_seguimiento = resultquery.ListSQL(consulta);
            //</editor-fold> 

            //<editor-fold defaultstate="collapsed" desc="IMAGENES">
            System.out.println("******************IMAGENES***********************");
            consulta = "SELECT pfk_tipo_imagen TIPO,CONCAT(url_imagen,'/',nombre) RUTA\n"
                    + "FROM hc_imagenes hcim, historias_clinicas hc\n"
                    + "WHERE hcim.pfk_historia_clinica = hc.pk_historia_clinica AND hc.pfk_paciente = '" + list.get("idpac") + "'\n"
                    + "ORDER BY pfk_tipo_imagen, consecutivo DESC ";

            List<Map<String, String>> lista_imagenes = resultquery.ListSQL(consulta);
            //</editor-fold> 

            //<editor-fold defaultstate="collapsed" desc="DATOS PERSONALES">
            consulta = "SELECT CONCAT_WS(' ', per.`primer_nombre`, IFNULL (per.`segundo_nombre`, ''), per.`primer_apellido`, IFNULL (per.`segundo_apellido`, '')) NOMBRES,\n"
                    + " IFNULL(DATE_FORMAT(per.`fecha_de_nacimiento`, '%d/%m/%Y'), '') AS FNAC, per.`pfk_tipo_documento` AS TIPO, per.`pk_persona` AS ID,  REPLACE (GROUP_CONCAT(IFNULL(pertel.`numero`, '')), ',', ' - ') TELEFONO, \n"
                    + " IFNULL(per.`direccion`, '') DIR, IFNULL(per.`correo_electronico`, '') COR\n"
                    + "FROM personas per\n"
                    + "LEFT JOIN personas_telefonos pertel ON CONCAT(per.`pfk_tipo_documento`, per.`pk_persona`) = pertel.`pfk_persona`\n"
                    + "WHERE CONCAT (per.`pfk_tipo_documento`, per.`pk_persona`) =  '" + list.get("idpac") + "' \n"
                    + "GROUP BY CONCAT (per.`pfk_tipo_documento`, per.`pk_persona`) ";

            List<Map<String, String>> lista_datosper = resultquery.ListSQL(consulta);
            //</editor-fold>

            PdfPCell celda = null;

            float[] tam = new float[100 / 5];
            PdfPTable tabla = new PdfPTable(tam);
            tabla.setWidthPercentage(100);
            for (int i = 0; i < tam.length; i++) {
                tam[i] = 5;
            }

//            tabla = new PdfPTable(tam);
//            tabla.setWidthPercentage(100);
//            
//            celda = new PdfPCell(new Phrase("\n"+("Historia Clinica Odontologica").toUpperCase()+" \n", pdf.font18n));
//            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
//            celda.setVerticalAlignment(Element.ALIGN_CENTER);
//            celda.setBorder(0);
//            celda.setColspan(tam.length);
//            tabla.addCell(celda);  
            System.out.println("<<>>" + tam.length);
            for (int i = 0; i < tam.length; i++) {
                celda = new PdfPCell(new Phrase("a", pdf.font10n));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                celda.setBorder(15);
                tabla.addCell(celda);
            }
            System.out.println("----------END----------------");
            //documento.add(tabla);

            float[] tam1 = new float[]{41f, 1.5f, 13f, 1.5f, 23f, 1.5f, 5f, 1.5f, 10f}; //11,5
            float[] tamp = new float[]{41f, 1.5f, 5f, 1.5f, 16f, 1.5f, 33.5f};

            //<editor-fold defaultstate="collapsed" desc="TABLA DATOS PERSONALES">
            tabla = new PdfPTable(tam1);
            tabla.setWidthPercentage(100);

            celda = new PdfPCell(new Phrase("\n", pdf.font4n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(0);
            celda.setColspan(tam1.length);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase("Nombres y Apellidos del Paciente ", pdf.font5n));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(13);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase("", pdf.font4n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(0);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("No. Documento ", pdf.font5n));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(13);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("", pdf.font4n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(0);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("Tipo Documento ", pdf.font5n));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(13);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("", pdf.font4n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(0);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("Edad ", pdf.font5n));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(13);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("", pdf.font4n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(0);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("Fecha Nacimiento ", pdf.font5n));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(13);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase("" + lista_datosper.get(0).get("NOMBRES"), pdf.font8));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(14);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase("", pdf.font4n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(0);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("" + lista_datosper.get(0).get("ID"), pdf.font8));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(14);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("", pdf.font4n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(0);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("" + lista_datosper.get(0).get("TIPO"), pdf.font8));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(14);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("", pdf.font4n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(0);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("" + getEdad(lista_datosper.get(0).get("FNAC")), pdf.font8));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(14);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("", pdf.font4n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(0);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("" + lista_datosper.get(0).get("FNAC"), pdf.font8));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(14);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase(" ", pdf.font10));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(0);
            celda.setFixedHeight(9f);
            celda.setColspan(tam1.length);
            tabla.addCell(celda);
            documento.add(tabla);

            tabla = new PdfPTable(tamp);
            tabla.setWidthPercentage(100);

            celda = new PdfPCell(new Phrase("Dirección ", pdf.font5n));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(13);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("", pdf.font4n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(0);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("Correo", pdf.font5n));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(13);
            celda.setColspan(3);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("", pdf.font4n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(0);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("Telefono ", pdf.font5n));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(13);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase("" + (lista_datosper.get(0).get("DIR").equals("") ? "\n" : lista_datosper.get(0).get("DIR")), pdf.font8));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(14);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("", pdf.font4n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(0);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("" + lista_datosper.get(0).get("COR"), pdf.font8));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(14);
            celda.setColspan(3);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("", pdf.font4n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(0);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("" + lista_datosper.get(0).get("TELEFONO"), pdf.font8));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(14);
            tabla.addCell(celda);
//            
            documento.add(tabla);
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="TABLA ANAMNESIS">
            float[] tama = new float[]{35f, 3.5f, 3.5f, 8f, 35f, 3.5f, 3.5f, 8f};

            tabla = new PdfPTable(tama);
            tabla.setWidthPercentage(100);

            celda = new PdfPCell(new Phrase("\n\n", pdf.font8));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(0);
            celda.setColspan(tama.length);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("ANAMNESIS", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(15);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setColspan(tama.length);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase("Motivo de la Consulta:", pdf.font8n));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(12);
            celda.setColspan(tama.length);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("\n" + lista_amnanesis.get(0).get("motivo_consulta") + "\n\n", pdf.font8n));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(14);
            celda.setColspan(tama.length);
            tabla.addCell(celda);
            for (int i = 0; i < 2; i++) {
                celda = new PdfPCell(new Phrase("Datos Basicos".toUpperCase(), pdf.font8n));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                celda.setBorder(15);
                tabla.addCell(celda);
                celda = new PdfPCell(new Phrase("SI", pdf.font8n));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                celda.setBorder(15);
                tabla.addCell(celda);
                celda = new PdfPCell(new Phrase("NO", pdf.font8n));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                celda.setBorder(15);
                tabla.addCell(celda);
                celda = new PdfPCell(new Phrase("NO SABE", pdf.font8n));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                celda.setBorder(15);
                tabla.addCell(celda);
            }

            int total = lista_amnanesis.size() / 2;
            int mod = lista_amnanesis.size() % 2;

            int filas = total + mod;
            int cont = 0;
            int c = -1;

            for (int i = 0; i < lista_amnanesis.size(); i++) {
                c++;
                celda = new PdfPCell(new Phrase("" + lista_amnanesis.get(c).get("DESCRIPCION"), pdf.font8));
                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                celda.setBorder(14);
                tabla.addCell(celda);
                celda = new PdfPCell(new Phrase("" + lista_amnanesis.get(c).get("SI"), pdf.font8));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                celda.setBorder(10);
                tabla.addCell(celda);
                celda = new PdfPCell(new Phrase("" + lista_amnanesis.get(c).get("N_O"), pdf.font8));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                celda.setBorder(10);
                tabla.addCell(celda);
                celda = new PdfPCell(new Phrase("" + lista_amnanesis.get(c).get("NO_SABE"), pdf.font8));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                celda.setBorder(10);
                tabla.addCell(celda);
            }
            if (mod == 1) {
                celda = new PdfPCell(new Phrase("", pdf.font8));
                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                celda.setBorder(14);
                tabla.addCell(celda);
                celda = new PdfPCell(new Phrase("", pdf.font8));
                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                celda.setBorder(10);
                tabla.addCell(celda);
                celda = new PdfPCell(new Phrase("", pdf.font8));
                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                celda.setBorder(10);
                tabla.addCell(celda);
                celda = new PdfPCell(new Phrase("", pdf.font8));
                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                celda.setBorder(10);
                tabla.addCell(celda);
            }

            System.out.println("total Amn--" + total);
            documento.add(tabla);

            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="TABLA ANALISIS VERTICAL">
            float[] tamav = new float[]{23f, 20f, 13f, 44f};

            tabla = new PdfPTable(tamav);
            tabla.setWidthPercentage(100);

            celda = new PdfPCell(new Phrase("\n\n", pdf.font8));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(0);
            celda.setColspan(tamav.length);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("ANALISIS VERTICAL", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(15);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setColspan(tamav.length);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase("Angulo Vertical (150°)".toUpperCase(), pdf.font8n));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(15);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase("" + (lista_vertical.size() > 0 ? lista_vertical.get(0).get("ANG_VERT") : ""), pdf.font8n));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(15);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("DIVERGENTE", pdf.font8n));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(15);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("", pdf.font8n));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(15);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase("Angulo Goniano (75°)".toUpperCase(), pdf.font8n));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(15);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("" + (lista_vertical.size() > 0 ? lista_vertical.get(0).get("ANG_GONI") : ""), pdf.font8n));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(15);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("PARALELAS", pdf.font8n));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(15);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("", pdf.font8n));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(15);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase("Longitud De Rama (75°)".toUpperCase(), pdf.font8n));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(15);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("" + (lista_vertical.size() > 0 ? lista_vertical.get(0).get("LONG_RAMA") : ""), pdf.font8n));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(15);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("EXTRACCIÓN", pdf.font8n));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(15);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("", pdf.font8n));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(15);
            tabla.addCell(celda);

            System.out.println("total Amn--" + total);
            documento.add(tabla);

            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="TABLA ANALISIS SAGITAL">
            float[] tamas = new float[]{5f, 8f, 8f, 11f, 8f, 8f, 11f, 8f, 33f};

            tabla = new PdfPTable(tamas);
            tabla.setWidthPercentage(100);

            celda = new PdfPCell(new Phrase("\n\n", pdf.font8));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(0);
            celda.setColspan(tamas.length);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("ANALISIS SAGITAL", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(15);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setColspan(tamas.length);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase("Angulación incisivo inferior (87° - 95°):".toUpperCase() + "    " + (lista_sagital.size() > 0 ? lista_sagital.get(0).get("ANGULO_INC_INF") : ""), pdf.font8));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(12);
            celda.setColspan(tamas.length);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("\nSEGÚN SWING", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(12);
            celda.setColspan(tamas.length);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("Solo si hay paralelimo maxilar clasificacion Angle", pdf.font8));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(12);
            celda.setColspan(tamas.length);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase("\n", pdf.font8));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(12);
            celda.setColspan(tamas.length);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase("", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(4);
            celda.setColspan(2);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("DERECHO", pdf.font8n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(15);
            celda.setColspan(3);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("IZQUIERDO", pdf.font8n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(11);
            celda.setColspan(3);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(8);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase("", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(4);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(2);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase("MOLAR", pdf.font8n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(14);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("PREMOLAR", pdf.font8n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(10);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("CANINO", pdf.font8n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(10);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("MOLAR", pdf.font8n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(10);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("PREMOLAR", pdf.font8n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(10);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("CANINO", pdf.font8n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(10);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("", pdf.font8n));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(8);
            tabla.addCell(celda);

            String[] dat = new String[6];
            dat[0] = "D<>M";
            dat[1] = "D<>P";
            dat[2] = "D<>C";
            dat[3] = "I<>M";
            dat[4] = "I<>P";
            dat[5] = "I<>C";
            for (int i = 1; i < 4; i++) {
                celda = new PdfPCell(new Phrase("", pdf.font8n));
                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                celda.setBorder(4);
                tabla.addCell(celda);

                celda = new PdfPCell(new Phrase("Clase " + (i == 1 ? "I" : (i == 2 ? "II" : "III")), pdf.font8));
                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                celda.setBorder(14);
                tabla.addCell(celda);
                for (int j = 0; j < dat.length; j++) {
                    String xx = "";
                    for (int x = 0; x < lista_sagital.size(); x++) {
                        if (("" + i).equals(lista_sagital.get(x).get("CLASE")) && dat[j].split("<>")[0].equals(lista_sagital.get(x).get("LADO"))
                                && dat[j].split("<>")[1].equals(lista_sagital.get(x).get("TIPO_DIENTE"))) {
                            xx = "X";
                        }
                    }
                    celda = new PdfPCell(new Phrase("" + xx, pdf.font8));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder(10);
                    tabla.addCell(celda);
                }
                celda = new PdfPCell(new Phrase("", pdf.font10n));
                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                celda.setBorder(8);
                tabla.addCell(celda);
            }

            celda = new PdfPCell(new Phrase("\n\n", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(14);
            celda.setColspan(tamas.length);
            tabla.addCell(celda);

            System.out.println("total Amn--" + total);
            documento.add(tabla);

            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="TABLA FORMA ARCO">
            float[] tamfa = new float[]{13f, 17f, 70f};

            tabla = new PdfPTable(tamfa);
            tabla.setWidthPercentage(100);

            celda = new PdfPCell(new Phrase("\n\n", pdf.font8));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(0);
            celda.setColspan(tamfa.length);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("FORMA ARCO", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(15);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setColspan(tamfa.length);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase("SUPERIOR", pdf.font8));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(4);
            tabla.addCell(celda);
            if (lista_formarco.size() > 0) {
                celda = new PdfPCell(new Phrase("" + (lista_formarco.get(0).get("SUP").equals("O") ? "Ovoide" : (lista_formarco.get(0).get("SUP").equals("T") ? "Triangular" : "Cuadrada")), pdf.font8));
            } else {
                celda = new PdfPCell(new Phrase("", pdf.font8));
            }
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(0);
            tabla.addCell(celda);

            //Ovoide, Triangular, Cuadrada
            String inf = "";
            if (lista_formarco.size() > 0) {
                inf = "" + (lista_formarco.get(0).get("INF").equals("O") ? "Ovoide" : (lista_formarco.get(0).get("INF").equals("T") ? "Triangular" : "Cuadrada"));
            }
            celda = new PdfPCell(new Phrase("INFERIOR    " + inf, pdf.font8));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(8);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase("\n", pdf.font8));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(12);
            celda.setColspan(tamfa.length);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase("Curva de Speed", pdf.font8));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(4);
            tabla.addCell(celda);

            if (lista_formarco.size() > 0) {
                celda = new PdfPCell(new Phrase("" + lista_formarco.get(0).get("CURVA") + "mm", pdf.font8));
            } else {
                celda = new PdfPCell(new Phrase("", pdf.font8));
            }
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(8);
            celda.setColspan(2);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase("Overbite", pdf.font8));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(4);
            tabla.addCell(celda);
            if (lista_formarco.size() > 0) {
                celda = new PdfPCell(new Phrase("" + lista_formarco.get(0).get("OVERB") + "%", pdf.font8));
            } else {
                celda = new PdfPCell(new Phrase("", pdf.font8));
            }

            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(8);
            celda.setColspan(2);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase("Overjet", pdf.font8));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(6);
            tabla.addCell(celda);
            if (lista_formarco.size() > 0) {
                celda = new PdfPCell(new Phrase("" + (lista_formarco.size() > 0 ? lista_formarco.get(0).get("OVERJ") + "%" : ""), pdf.font8));
            } else {
                celda = new PdfPCell(new Phrase("", pdf.font8));
            }
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(10);
            celda.setColspan(2);
            tabla.addCell(celda);

            System.out.println("total Amn--" + total);
            documento.add(tabla);

            //</editor-fold>
            documento.newPage();
            //<editor-fold defaultstate="collapsed" desc="TABLA ANALISIS TRANSVERSAL">
            float[] tamat = new float[]{16f, 10f, 10f, 14f, 50f};

            tabla = new PdfPTable(tamat);
            tabla.setWidthPercentage(100);

            celda = new PdfPCell(new Phrase("\n\n", pdf.font8));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(0);
            celda.setColspan(tamat.length);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("ANALISIS TRANSVERSAL", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(15);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setColspan(tamat.length);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase("Linea Media Superior     ", pdf.font8n));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(4);
            tabla.addCell(celda);

            if (lista_transversal.size() > 0) {
                celda = new PdfPCell(new Phrase("" + (lista_transversal.get(0).get("LINEA_MSUP").equals("C") ? "Coincidente" : "Desviada"), pdf.font8));
            } else {
                celda = new PdfPCell(new Phrase("", pdf.font8));
            }
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(0);
            tabla.addCell(celda);

            String compl = "";
            if (lista_transversal.size() > 0) {
                compl = "Hacia la " + (lista_transversal.get(0).get("DIR_MSUP").equals("D") ? "Derecha" : "Izquierda") + " x " + lista_transversal.get(0).get("LONG_MSUP") + "mm";
                celda = new PdfPCell(new Phrase("" + (lista_transversal.get(0).get("LINEA_MSUP").equals("C") ? "" : "" + compl), pdf.font8));
            } else {
                celda = new PdfPCell(new Phrase("", pdf.font8));
            }
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(8);
            celda.setColspan(3);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase("Linea Media Inferior     ", pdf.font8n));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(4);
            tabla.addCell(celda);
            if (lista_transversal.size() > 0) {
                celda = new PdfPCell(new Phrase("" + (lista_transversal.get(0).get("LINEA_MINF").equals("C") ? "Coincidente" : "Desviada"), pdf.font8));
            } else {
                celda = new PdfPCell(new Phrase("", pdf.font8));
            }

            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(0);
            tabla.addCell(celda);

            if (lista_transversal.size() > 0) {
                compl = "Hacia la " + (lista_transversal.get(0).get("DIR_MINF").equals("D") ? "Derecha" : "Izquierda") + " x " + lista_transversal.get(0).get("LONG_MINF") + "mm";
                celda = new PdfPCell(new Phrase("" + (lista_transversal.get(0).get("LINEA_MINF").equals("C") ? "" : "" + compl), pdf.font8));
            } else {
                celda = new PdfPCell(new Phrase("", pdf.font8));
            }

            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(8);
            celda.setColspan(3);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase("Habitos", pdf.font8n));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(12);
            celda.setColspan(tamat.length);
            tabla.addCell(celda);
            if (lista_transversal.size() > 0) {
                celda = new PdfPCell(new Phrase("" + lista_transversal.get(0).get("HAB").replace("#", ", "), pdf.font8));
            } else {
                celda = new PdfPCell(new Phrase("", pdf.font8));
            }
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(12);
            celda.setColspan(tamat.length);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase("Interconsulta", pdf.font8n));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(12);
            celda.setColspan(tamat.length);
            tabla.addCell(celda);

            if (lista_transversal.size() > 0) {
                celda = new PdfPCell(new Phrase("" + lista_transversal.get(0).get("INTCON").replace("#", ", "), pdf.font8));
            } else {
                celda = new PdfPCell(new Phrase("", pdf.font8));
            }
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(12);
            celda.setColspan(tamat.length);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase("Observación", pdf.font8n));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(12);
            celda.setColspan(tamat.length);
            tabla.addCell(celda);
            if (lista_transversal.size() > 0) {
                celda = new PdfPCell(new Phrase("" + lista_transversal.get(0).get("OBS"), pdf.font8));
            } else {
                celda = new PdfPCell(new Phrase("", pdf.font8));
            }
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(12);
            celda.setColspan(tamat.length);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase("Examenes", pdf.font8n));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(12);
            celda.setColspan(tamat.length);
            tabla.addCell(celda);
            if (lista_transversal.size() > 0) {
                celda = new PdfPCell(new Phrase("" + lista_transversal.get(0).get("EXAM"), pdf.font8));
            } else {
                celda = new PdfPCell(new Phrase("", pdf.font8));
            }
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(12);
            celda.setColspan(tamat.length);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase("Simetria", pdf.font8n));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(6);
            tabla.addCell(celda);
            if (lista_transversal.size() > 0) {
                celda = new PdfPCell(new Phrase("" + lista_transversal.get(0).get("SIM"), pdf.font8n));
            } else {
                celda = new PdfPCell(new Phrase("", pdf.font8n));
            }
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(2);
            celda.setColspan(2);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("Pronostico", pdf.font8n));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(2);
            tabla.addCell(celda);

            if (lista_transversal.size() > 0) {
                celda = new PdfPCell(new Phrase("" + lista_transversal.get(0).get("PRONOS"), pdf.font8n));
            } else {
                celda = new PdfPCell(new Phrase("", pdf.font8n));
            }
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(10);
            tabla.addCell(celda);

            float dt = documento.bottom();
            float tamTbl = tabla.getTotalHeight();
            System.out.println("Tamaño TABLA--" + tamTbl);
            System.out.println("dato ante--" + dt);
            documento.add(tabla);
            dt = documento.bottom();
            System.out.println("daro despues--" + dt);
            System.out.println("");

            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="TABLA CONVENCIONES ODONTOGRAMA">
            float[] tamco = new float[]{20f, 20f, 20f, 20f, 20f};

            tabla = new PdfPTable(tamco);
            tabla.setWidthPercentage(100);

            celda = new PdfPCell(new Phrase("\n\n", pdf.font8));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(0);
            celda.setColspan(tamco.length);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase("Convenciones Odontograma".toUpperCase(), pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(15);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setColspan(tamco.length);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("\n", pdf.font8));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(12);
            celda.setColspan(tamco.length);
            tabla.addCell(celda);

            String[] convenciones = new String[]{"Caries", "Amalgama", "Resina", "Incrustacion", "Exodoncia Indicada", "Exodoncia por apiñamiento", "Implate", "Diente no Erupcionado", "Puente Removible", "Diente Perdido"};

            m1w = m1h = mw = mh = 32;
            int con = 0, cnt = 0, ban = 0;
            int par = 0, i = 0;
            while (par < 4) {

                if (i % 5 == 0 && i != 0) {
                    if (par < 1) {
                        i = 0;
                    }
                    if (par == 1) {
                        celda = new PdfPCell(new Phrase("\n", pdf.font8));
                        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                        celda.setVerticalAlignment(Element.ALIGN_CENTER);
                        celda.setBorder(12);
                        celda.setColspan(tamco.length);
                        tabla.addCell(celda);
                    }
                    if (par == 2) {
                        i = 5;
                    }

                    par++;
                }
                if (par == 4) {
                    break;
                }
                System.out.println("*******par-->" + par + "**************i-->" + i + "**");
                if (par % 2 == 0) {
                    Image m1;
                    try {
                        m1 = Image.getInstance("src/img/convenciones/DIENTEP_" + (i + 1) + ".png");
                    } catch (Exception e) {
                        m1 = null;
                    }
                    System.out.println("m1--->" + m1);
                    if (m1 != null) {
                        m1.setAlignment(Image.MIDDLE);
                        m1.scaleAbsolute(m1w, m1h);
                        celda = new PdfPCell(m1);
                        celda.setFixedHeight(m1h + 5);
                        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        celda.setBorder((i == 0 || i == 5 ? 4 : (i == 4 || i == 9) ? 8 : 0));
                        tabla.addCell(celda);
                    } else {
                        celda = new PdfPCell(new Phrase("", pdf.font12n));
                        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        celda.setBorder((i == 0 || i == 5 ? 4 : (i == 4 || i == 9) ? 8 : 0));
                        tabla.addCell(celda);
                    }
                } else {
                    celda = new PdfPCell(new Phrase("" + convenciones[i], pdf.font8n));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder((i == 0 || i == 5 ? 4 : (i == 4 || i == 9) ? 8 : 0));
                    tabla.addCell(celda);
                }

                i++;
            }

            celda = new PdfPCell(new Phrase("\n", pdf.font8));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(14);
            celda.setColspan(tamco.length);
            tabla.addCell(celda);

            documento.add(tabla);

            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="TABLA ODONTOGRAMA">
            float[] tamod = new float[]{2f, 96f, 2f};

//            tabla = new PdfPTable(tamod);
            tabla = new PdfPTable(tamod);
            tabla.setWidthPercentage(100);

            List<Map<String, String>> lista_ImgOdo = gen.data_list(3, lista_imagenes, new String[]{"TIPO<->2"});

            System.out.println("lista_imagenes.size()-->" + lista_imagenes.size());
            System.out.println("lista_ImgOdo.size()-->" + lista_ImgOdo.size());

            String[] tiposImg = new String[]{"Panoramica", "Radiografia", "Odontograma", "Fotografia"};

            celda = new PdfPCell(new Phrase("\n\n\n", pdf.font8));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(0);
            celda.setColspan(tamod.length);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase("Odontograma".toUpperCase(), pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(15);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setColspan(tamod.length);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("\n\n", pdf.font8));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(12);
            celda.setColspan(tamod.length);
            tabla.addCell(celda);

            Image m1;
            String ruta = "";
            for (int v = 0; v < lista_ImgOdo.size(); v++) {
                if (list.get("chOT").equals("0") && v > 0) {
                    break;
                }
                ruta = lista_ImgOdo.get(v).get("RUTA");

                System.out.println("ruta--->" + ruta);
                try {
                    m1 = Image.getInstance("" + ruta);
                } catch (Exception e) {
                    System.out.println("e---->" + e.toString());
                    ruta = "";
                    //m1 = Image.getInstance(""+ruta);
                    m1 = null;
                }

                System.out.println("m1-ODONTOGRAMA-->" + m1);
                celda = new PdfPCell(new Phrase("", pdf.font12n));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celda.setBorder(4);
                tabla.addCell(celda);
                mw = 550;
                mh = 250;
                if (m1 != null) {
                    m1.setAlignment(Image.MIDDLE);
                    m1.scaleAbsolute(mw, mh);
                    celda = new PdfPCell(m1);
                    celda.setFixedHeight(mh + 5);
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    celda.setBorder(0);
                    tabla.addCell(celda);
                } else {
                    celda = new PdfPCell(new Phrase("", pdf.font12n));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    celda.setBorder(0);
                    tabla.addCell(celda);
                }

                celda = new PdfPCell(new Phrase("", pdf.font12n));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celda.setBorder(8);
                tabla.addCell(celda);

            }
            celda = new PdfPCell(new Phrase("\n", pdf.font8));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(14);
            celda.setColspan(tamco.length);
            tabla.addCell(celda);
            documento.add(tabla);

            //documento.newPage();
            //</editor-fold>
//            //<editor-fold defaultstate="collapsed" desc="TABLA SEGUIMIENTO TRATAMIENTO">
            List<Map<String, String>> lista_tratamientos = gen.data_list(1, lista_seguimiento, new String[]{"ID_TRA"});
            List<Map<String, String>> lista_datosxtratamientos;

            if (lista_tratamientos.size() > 0) {
                celda = null;
                float[] tami = new float[100 / 5];
                for (int x = 0; x < tami.length; x++) {
                    tami[x] = 5;

                }

                tabla = new PdfPTable(tami);
//                tabla.setWidthPercentage(100);
//                for(int x = 0; x < tami.length; x++){
//                    celda = new PdfPCell(new Phrase("a ",pdf.font10n));
//                    
//                    celda.setBorder(15);
//                    tabla.addCell(celda);
//                    
//                }
//                documento.add(tabla);

                float[] tams = new float[]{5, 20, 75};
                tabla = new PdfPTable(tams);
                int abonot = 0, saldot = 0;

                System.out.println("lista_tratamientos---->" + lista_tratamientos.size());
                for (i = 0; i < lista_tratamientos.size(); i++) {
                    lista_datosxtratamientos = gen.data_list(3, lista_seguimiento, new String[]{"ID_TRA<->" + lista_tratamientos.get(i).get("ID_TRA")});
                    abonot = getTotalSeguimiento(0, lista_datosxtratamientos);
                    saldot = getTotalSeguimiento(1, lista_datosxtratamientos);
                    tabla = new PdfPTable(tams);
                    tabla.setWidthPercentage(100);

                    celda = new PdfPCell(new Phrase("TRATAMIENTO " + lista_tratamientos.get(i).get("DESC_TRA").toUpperCase(), pdf.font12n));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setColspan(tams.length);
                    celda.setBorder(0);
                    tabla.addCell(celda);

//                    celda = new PdfPCell(new Phrase("Costo Total: ",pdf.font10n));      
//                    celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
//                    celda.setBorder(0);
//                    tabla.addCell(celda);
//                    celda = new PdfPCell(new Phrase(""+Utilidades.MascaraMoneda(""+lista_tratamientos.get(i).get("COSTO")),pdf.font10));  
//                    celda.setColspan(tams.length-1);
//                    celda.setBorder(0);
//                    tabla.addCell(celda);
//                    
//                 
//                    
//                    celda = new PdfPCell(new Phrase("Abonos: ",pdf.font10n));      
//                    celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
//                    celda.setBorder(0);
//                    tabla.addCell(celda);
//                    celda = new PdfPCell(new Phrase(""+Utilidades.MascaraMoneda(""+abonot),pdf.font10));                    
//                    celda.setBorder(0);
//                    celda.setColspan(tams.length-1);
//                    tabla.addCell(celda);
//                    
//                    
//                    
//                    celda = new PdfPCell(new Phrase("Cuota Inicial: ",pdf.font10n));  
//                    celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
//                    celda.setBorder(0);
//                    tabla.addCell(celda);
//                    String cancelada = ""+(abonot >= Integer.parseInt(lista_tratamientos.get(i).get("CUOTA_I")) ? "(CANCELADA)": "");
//                    celda = new PdfPCell(new Phrase(""+Utilidades.MascaraMoneda(""+lista_tratamientos.get(i).get("CUOTA_I"))+" "+cancelada,pdf.font10));                    
//                    celda.setBorder(0);
//                    tabla.addCell(celda);
//                    celda.setColspan(tams.length-1);
//                    if(abonot >= Integer.parseInt(lista_tratamientos.get(i).get("CUOTA_I"))){
//                        
//                    }
//                    if(!lista_tratamientos.get(i).get("COSTO").equals("0")){
//                        celda = new PdfPCell(new Phrase("",pdf.font10n));  
//                        celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
//                        celda.setColspan(tams.length-1);
//                        celda.setBorder(0);
//                        tabla.addCell(celda);
//                        celda = new PdfPCell(new Phrase("Saldo pendiente: ",pdf.font10n));  
//                        celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
//                        celda.setBorder(0);
//                        tabla.addCell(celda);
//                        celda = new PdfPCell(new Phrase(""+Utilidades.MascaraMoneda(""+saldot),pdf.font10));                    
//                        celda.setColspan(tams.length-1);
//                        tabla.addCell(celda);
//                    }
//                    else{
//                        celda = new PdfPCell(new Phrase("",pdf.font10n));  
//                        celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
//                        celda.setColspan(2);
//                        celda.setBorder(0);
//                        tabla.addCell(celda);
//                    }
                    celda = new PdfPCell(new Phrase("\n", pdf.font12n));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setColspan(tam.length);
                    celda.setBorder(0);
                    tabla.addCell(celda);
                    celda = new PdfPCell(new Phrase("EVOLUCIÓN DEL TRATAMIENTO", pdf.font12n));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setColspan(tam.length);
                    celda.setBorder(0);
                    tabla.addCell(celda);
                    celda = new PdfPCell(new Phrase("", pdf.font12n));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setColspan(tam.length);
                    celda.setBorder(0);
                    tabla.addCell(celda);

                    if (lista_datosxtratamientos.size() > 0) {
                        celda = new PdfPCell(new Phrase("N°", pdf.font10n));
                        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                        celda.setVerticalAlignment(Element.ALIGN_CENTER);
                        celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        celda.setBorder(15);
                        tabla.addCell(celda);
                        celda = new PdfPCell(new Phrase("FECHA", pdf.font10n));
                        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                        celda.setVerticalAlignment(Element.ALIGN_CENTER);
                        celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        celda.setBorder(15);
                        tabla.addCell(celda);
                        celda = new PdfPCell(new Phrase("TRATAMIENTO", pdf.font10n));
                        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                        celda.setVerticalAlignment(Element.ALIGN_CENTER);
                        celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        celda.setBorder(15);
                        tabla.addCell(celda);
//                        celda = new PdfPCell(new Phrase("ABONO", pdf.font10n));
//                        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
//                        celda.setVerticalAlignment(Element.ALIGN_CENTER);
//                        celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
//                        celda.setBorder(15);
//                        tabla.addCell(celda);
//                        celda = new PdfPCell(new Phrase("SALDO", pdf.font10n));
//                        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
//                        celda.setVerticalAlignment(Element.ALIGN_CENTER);
//                        celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
//                        celda.setBorder(15);
//                        tabla.addCell(celda);
                        for (int j = 0; j < lista_datosxtratamientos.size(); j++) {
                            celda = new PdfPCell(new Phrase("" + (j + 1), pdf.font10));
                            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                            celda.setVerticalAlignment(Element.ALIGN_CENTER);
                            celda.setBorder(15);
                            tabla.addCell(celda);
                            celda = new PdfPCell(new Phrase("" + lista_datosxtratamientos.get(j).get("FECHA"), pdf.font10));
                            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                            celda.setVerticalAlignment(Element.ALIGN_CENTER);
                            celda.setBorder(15);
                            tabla.addCell(celda);
                            celda = new PdfPCell(new Phrase("" + lista_datosxtratamientos.get(j).get("OBSERVACION"), pdf.font10));
                            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                            celda.setVerticalAlignment(Element.ALIGN_CENTER);
                            celda.setBorder(15);
                            tabla.addCell(celda);
//                            celda = new PdfPCell(new Phrase(""+Utilidades.formatomoneda(lista_datosxtratamientos.get(j).get("ABONO")), pdf.font10)); 
//                            celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
//                            celda.setVerticalAlignment(Element.ALIGN_CENTER);
//                            celda.setBorder(15);
//                            tabla.addCell(celda);
//                            celda = new PdfPCell(new Phrase(""+Utilidades.formatomoneda(lista_datosxtratamientos.get(j).get("SALDO")), pdf.font10)); 
//                            celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
//                            celda.setVerticalAlignment(Element.ALIGN_CENTER);
//                            celda.setBorder(15);
//                            tabla.addCell(celda);
                        }
                    } else {
                        celda = new PdfPCell(new Phrase("No se encontraron registros.", pdf.font15n));
                        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                        celda.setVerticalAlignment(Element.ALIGN_CENTER);
                        celda.setColspan(tams.length);
                        celda.setBorder(15);
                        tabla.addCell(celda);
                    }

                    documento.add(tabla);
                    if (i < lista_tratamientos.size() - 1) {
                        documento.newPage();
                    }
                }

            } else {
                celda = null;

                tabla = new PdfPTable(1);
                tabla.setWidthPercentage(100);

                celda = new PdfPCell(new Phrase("No se encontraron registros.", pdf.font15n));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                celda.setBorder(15);
                tabla.addCell(celda);

                documento.add(tabla);

            }
//                List<Map<String,String>> lista_tratamientos = gen.data_list(1, lista_seguimiento, new String[]{"ID_TRA"});
//                List<Map<String,String>> lista_datosxtratamientos;
//                if(lista_tratamientos.size() > 0){
//                    float[] tams =  new float[]{5,11,48,18,18};
//                    tabla = new PdfPTable(tams);
//                    for(int x = 0; x < lista_tratamientos.size(); x++){
//                        tabla = new PdfPTable(tam);
//                        tabla.setWidthPercentage(100);
//
//                        celda = new PdfPCell(new Phrase("TRATAMIENTO: ",pdf.font10n));
//                        celda.setColspan(2);  
//                        celda.setBorder(0);
//                        tabla.addCell(celda);
//                        celda = new PdfPCell(new Phrase(" "+lista_tratamientos.get(x).get("DESC_TRA"),pdf.font12));
//                        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
//                        celda.setColspan(3);
//                        celda.setBorder(0);
//                        tabla.addCell(celda);
//
//
//                        celda = new PdfPCell(new Phrase("EVOLUCIÓN DEL TRATAMIENTO",pdf.font12n));
//                        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
//                        celda.setColspan(tam.length);
//                        celda.setBorder(0);
//                        tabla.addCell(celda);
//                        celda = new PdfPCell(new Phrase("",pdf.font12n));
//                        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
//                        celda.setColspan(tam.length);
//                        celda.setBorder(0);
//                        tabla.addCell(celda);
//
//                        lista_datosxtratamientos = gen.data_list(3, lista_seguimiento, new String[]{"ID_TRA<->"+lista_tratamientos.get(x).get("ID_TRA")});
//
//                        if(lista_datosxtratamientos.size() > 0){
//                            celda = new PdfPCell(new Phrase("N°", pdf.font10n));
//                            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
//                            celda.setVerticalAlignment(Element.ALIGN_CENTER);
//                            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
//                            celda.setBorder(15);
//                            tabla.addCell(celda);
//                            celda = new PdfPCell(new Phrase("FECHA", pdf.font10n));
//                            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
//                            celda.setVerticalAlignment(Element.ALIGN_CENTER);
//                            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
//                            celda.setBorder(15);
//                            tabla.addCell(celda);
//                            celda = new PdfPCell(new Phrase("TRATAMIENTO", pdf.font10n));
//                            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
//                            celda.setVerticalAlignment(Element.ALIGN_CENTER);
//                            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
//                            celda.setBorder(15);
//                            tabla.addCell(celda);
//                            celda = new PdfPCell(new Phrase("ABONO", pdf.font10n));
//                            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
//                            celda.setVerticalAlignment(Element.ALIGN_CENTER);
//                            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
//                            celda.setBorder(15);
//                            tabla.addCell(celda);
//                            celda = new PdfPCell(new Phrase("SALDO", pdf.font10n));
//                            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
//                            celda.setVerticalAlignment(Element.ALIGN_CENTER);
//                            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
//                            celda.setBorder(15);
//                            tabla.addCell(celda);
//                            for(int j = 0; j < lista_datosxtratamientos.size(); j++){
//                                celda = new PdfPCell(new Phrase(""+(i+1), pdf.font10)); 
//                                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
//                                celda.setVerticalAlignment(Element.ALIGN_CENTER);
//                                celda.setBorder(15);
//                                tabla.addCell(celda);
//                                celda = new PdfPCell(new Phrase(""+lista_datosxtratamientos.get(j).get("FECHA"), pdf.font10)); 
//                                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
//                                celda.setVerticalAlignment(Element.ALIGN_CENTER);
//                                celda.setBorder(15);
//                                tabla.addCell(celda);
//                                celda = new PdfPCell(new Phrase(""+lista_datosxtratamientos.get(j).get("OBSERVACION"), pdf.font10)); 
//                                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
//                                celda.setVerticalAlignment(Element.ALIGN_CENTER);
//                                celda.setBorder(15);
//                                tabla.addCell(celda);
//                                celda = new PdfPCell(new Phrase(""+lista_datosxtratamientos.get(j).get("ABONO"), pdf.font10)); 
//                                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
//                                celda.setVerticalAlignment(Element.ALIGN_CENTER);
//                                celda.setBorder(15);
//                                tabla.addCell(celda);
//                                celda = new PdfPCell(new Phrase(""+lista_datosxtratamientos.get(j).get("SALDO"), pdf.font10)); 
//                                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
//                                celda.setVerticalAlignment(Element.ALIGN_CENTER);
//                                celda.setBorder(15);
//                                tabla.addCell(celda);
//                            }
//                        }
//
//                        documento.add(tabla);
//                        if(x < lista_tratamientos.size()-1){
//                            documento.newPage();
//                        }
//                    }
//                    
//                }
////                float[] tamst =  new float[]{2f, 5f, 18f, 31f, 17f, 17f, 2f};
////
////                tabla = new PdfPTable(tamst);
////                tabla.setWidthPercentage(100);
////
////                celda = new PdfPCell(new Phrase("\n\n\n", pdf.font8));
////                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
////                celda.setVerticalAlignment(Element.ALIGN_CENTER);
////                celda.setBorder(0);
////                celda.setColspan(tamst.length);
////                tabla.addCell(celda);
////
////                celda = new PdfPCell(new Phrase("Seguimiento de tratamiento".toUpperCase(), pdf.font10n));
////                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
////                celda.setVerticalAlignment(Element.ALIGN_CENTER);
////                celda.setBorder(15);
////                celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
////                celda.setColspan(tamst.length);
////                tabla.addCell(celda);   
////                celda = new PdfPCell(new Phrase("\n", pdf.font8));
////                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
////                celda.setVerticalAlignment(Element.ALIGN_CENTER);
////                celda.setBorder(12);
////                celda.setColspan(tamst.length);
////                tabla.addCell(celda);
////
////                celda = new PdfPCell(new Phrase("N°", pdf.font10n));
////                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
////                celda.setVerticalAlignment(Element.ALIGN_CENTER);
////                celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
////                celda.setBorder(15);
////                tabla.addCell(celda);
////                celda = new PdfPCell(new Phrase("FECHA", pdf.font10n));
////                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
////                celda.setVerticalAlignment(Element.ALIGN_CENTER);
////                celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
////                celda.setBorder(15);
////                tabla.addCell(celda);
////                celda = new PdfPCell(new Phrase("TRATAMIENTO", pdf.font10n));
////                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
////                celda.setVerticalAlignment(Element.ALIGN_CENTER);
////                celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
////                celda.setBorder(15);
////                tabla.addCell(celda);
////                celda = new PdfPCell(new Phrase("ABONO", pdf.font10n));
////                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
////                celda.setVerticalAlignment(Element.ALIGN_CENTER);
////                celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
////                celda.setBorder(15);
////                tabla.addCell(celda);
////                celda = new PdfPCell(new Phrase("SALDO", pdf.font10n));
////                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
////                celda.setVerticalAlignment(Element.ALIGN_CENTER);
////                celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
////                celda.setBorder(15);
////                tabla.addCell(celda);
////
////                for(int v = 0; v < 10; v++){
////                    for(int h = 0; h < 5; h++){
////                        celda = new PdfPCell(new Phrase("", pdf.font10n));
////                        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
////                        celda.setVerticalAlignment(Element.ALIGN_CENTER);
////                        celda.setBorder(15);  
////                        tabla.addCell(celda);
////                    }
////                }
////                
////                
////                celda = new PdfPCell(new Phrase("\n", pdf.font8));
////                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
////                celda.setVerticalAlignment(Element.ALIGN_CENTER);
////                celda.setBorder(14);
////                celda.setColspan(tamst.length);
////                tabla.addCell(celda); 
////                documento.add(tabla);
//                //documento.newPage();
//                //</editor-fold>

            if (list.get("chP").equals("1")) {
                //<editor-fold defaultstate="collapsed" desc="TABLA PANORAMICA">
                tamod = new float[]{2f, 96f, 2f};

                tabla = new PdfPTable(tamod);
                tabla.setWidthPercentage(100);

                List<Map<String, String>> lista_Impan = gen.data_list(3, lista_imagenes, new String[]{"TIPO<->0"});

                System.out.println("lista_imagenes.size()-->" + lista_imagenes.size());
                System.out.println("lista_Impan.size()-->" + lista_Impan.size());

                celda = new PdfPCell(new Phrase("\n\n", pdf.font8));
                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                celda.setBorder(0);
                celda.setColspan(tamod.length);
                tabla.addCell(celda);

                celda = new PdfPCell(new Phrase("Panoramicas".toUpperCase(), pdf.font10n));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                celda.setBorder(15);
                celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
                celda.setColspan(tamod.length);
                tabla.addCell(celda);
                celda = new PdfPCell(new Phrase("\n", pdf.font8));
                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                celda.setBorder(12);
                celda.setColspan(tamod.length);
                tabla.addCell(celda);

                for (int v = 0; v < lista_Impan.size(); v++) {
                    if (list.get("chPT").equals("0") && v > 0) {
                        break;
                    }
                    ruta = lista_Impan.get(v).get("RUTA");

                    System.out.println("ruta--->" + ruta);
                    try {
                        m1 = Image.getInstance("" + ruta);
                    } catch (Exception e) {
                        System.out.println("e---->" + e.toString());
                        ruta = "";
                        //m1 = Image.getInstance(""+ruta);
                        m1 = null;
                    }

                    System.out.println("m1-ODONTOGRAMA-->" + m1);
                    celda = new PdfPCell(new Phrase("", pdf.font12n));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    celda.setBorder(4);
                    tabla.addCell(celda);
                    mw = 550;
                    mh = 250;
                    if (m1 != null) {
                        m1.setAlignment(Image.MIDDLE);
                        m1.scaleAbsolute(mw, mh);
                        celda = new PdfPCell(m1);
                        celda.setFixedHeight(mh + 5);
                        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        celda.setBorder(0);
                        tabla.addCell(celda);
                    } else {
                        celda = new PdfPCell(new Phrase("", pdf.font12n));
                        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        celda.setBorder(0);
                        tabla.addCell(celda);
                    }

                    celda = new PdfPCell(new Phrase("", pdf.font12n));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    celda.setBorder(8);
                    tabla.addCell(celda);

                }
                celda = new PdfPCell(new Phrase("\n", pdf.font8));
                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                celda.setBorder(14);
                celda.setColspan(tamco.length);
                tabla.addCell(celda);

                documento.add(tabla);
                //documento.newPage();

                //</editor-fold>
            }
            if (list.get("chR").equals("1")) {
                //<editor-fold defaultstate="collapsed" desc="TABLA raDIOGRAFIA">
                tamod = new float[]{2f, 96f, 2f};

                tabla = new PdfPTable(tamod);
                tabla.setWidthPercentage(100);

                List<Map<String, String>> lista_ImRad = gen.data_list(3, lista_imagenes, new String[]{"TIPO<->1"});

                System.out.println("lista_imagenes.size()-->" + lista_imagenes.size());
                System.out.println("lista_Impan.size()-->" + lista_ImRad.size());

                celda = new PdfPCell(new Phrase("\n\n", pdf.font8));
                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                celda.setBorder(0);
                celda.setColspan(tamod.length);
                tabla.addCell(celda);

                celda = new PdfPCell(new Phrase("Radiografia".toUpperCase(), pdf.font10n));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                celda.setBorder(15);
                celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
                celda.setColspan(tamod.length);
                tabla.addCell(celda);
                celda = new PdfPCell(new Phrase("\n", pdf.font8));
                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                celda.setBorder(12);
                celda.setColspan(tamod.length);
                tabla.addCell(celda);

                for (int v = 0; v < lista_ImRad.size(); v++) {
                    if (list.get("chRT").equals("0") && v > 0) {
                        break;
                    }
                    ruta = lista_ImRad.get(v).get("RUTA");

                    System.out.println("ruta--->" + ruta);
                    try {
                        m1 = Image.getInstance("" + ruta);
                    } catch (Exception e) {
                        System.out.println("e---->" + e.toString());
                        ruta = "";
                        //m1 = Image.getInstance(""+ruta);
                        m1 = null;
                    }

                    System.out.println("m1-ODONTOGRAMA-->" + m1);
                    celda = new PdfPCell(new Phrase("", pdf.font12n));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    celda.setBorder(4);
                    tabla.addCell(celda);
                    mw = 550;
                    mh = 250;
                    if (m1 != null) {
                        m1.setAlignment(Image.MIDDLE);
                        m1.scaleAbsolute(mw, mh);
                        celda = new PdfPCell(m1);
                        celda.setFixedHeight(mh + 5);
                        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        celda.setBorder(0);
                        tabla.addCell(celda);
                    } else {
                        celda = new PdfPCell(new Phrase("", pdf.font12n));
                        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        celda.setBorder(0);
                        tabla.addCell(celda);
                    }

                    celda = new PdfPCell(new Phrase("", pdf.font12n));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    celda.setBorder(8);
                    tabla.addCell(celda);

                    celda = new PdfPCell(new Phrase("\n", pdf.font8));
                    celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder(14);
                    celda.setColspan(tamco.length);
                    tabla.addCell(celda);

                    documento.add(tabla);
                }
                //documento.newPage();
                //</editor-fold>
            }
            if (list.get("chF").equals("1")) {
                //<editor-fold defaultstate="collapsed" desc="TABLA FOTOGRAFIA">
                tamod = new float[]{2f, 96f, 2f};

                tabla = new PdfPTable(tamod);
                tabla.setWidthPercentage(100);

                List<Map<String, String>> lista_ImFot = gen.data_list(3, lista_imagenes, new String[]{"TIPO<->3"});

                System.out.println("lista_imagenes.size()-->" + lista_imagenes.size());
                System.out.println("lista_Impan.size()-->" + lista_ImFot.size());

                celda = new PdfPCell(new Phrase("\n\n", pdf.font8));
                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                celda.setBorder(0);
                celda.setColspan(tamod.length);
                tabla.addCell(celda);

                celda = new PdfPCell(new Phrase("Fotografia".toUpperCase(), pdf.font10n));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                celda.setBorder(15);
                celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
                celda.setColspan(tamod.length);
                tabla.addCell(celda);
                celda = new PdfPCell(new Phrase("\n", pdf.font8));
                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                celda.setBorder(12);
                celda.setColspan(tamod.length);
                tabla.addCell(celda);

                for (int v = 0; v < lista_ImFot.size(); v++) {
                    if (list.get("chFT").equals("0") && v > 0) {
                        break;
                    }
                    ruta = lista_ImFot.get(v).get("RUTA");

                    System.out.println("ruta--->" + ruta);
                    try {
                        m1 = Image.getInstance("" + ruta);
                    } catch (Exception e) {
                        System.out.println("e---->" + e.toString());
                        ruta = "";
                        //m1 = Image.getInstance(""+ruta);
                        m1 = null;
                    }

                    System.out.println("m1-ODONTOGRAMA-->" + m1);
                    celda = new PdfPCell(new Phrase("", pdf.font12n));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    celda.setBorder(4);
                    tabla.addCell(celda);
                    mw = 550;
                    mh = 250;
                    if (m1 != null) {
                        m1.setAlignment(Image.MIDDLE);
                        m1.scaleAbsolute(mw, mh);
                        celda = new PdfPCell(m1);
                        celda.setFixedHeight(mh + 5);
                        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        celda.setBorder(0);
                        tabla.addCell(celda);
                    } else {
                        celda = new PdfPCell(new Phrase("", pdf.font12n));
                        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        celda.setBorder(0);
                        tabla.addCell(celda);
                    }

                    celda = new PdfPCell(new Phrase("", pdf.font12n));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    celda.setBorder(8);
                    tabla.addCell(celda);

                }
                celda = new PdfPCell(new Phrase("\n", pdf.font8));
                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                celda.setBorder(14);
                celda.setColspan(tamco.length);
                tabla.addCell(celda);
                documento.add(tabla);
                //documento.newPage();
                //</editor-fold>
            }
            if (list.get("chC").equals("1")) {
                //<editor-fold defaultstate="collapsed" desc="TABLA Cefalometrico">
                tamod = new float[]{2f, 96f, 2f};

                tabla = new PdfPTable(tamod);
                tabla.setWidthPercentage(100);

                List<Map<String, String>> lista_ImFot = gen.data_list(3, lista_imagenes, new String[]{"TIPO<->4"});

                System.out.println("lista_imagenes.size()-->" + lista_imagenes.size());
                System.out.println("lista_Impan.size()-->" + lista_ImFot.size());

                celda = new PdfPCell(new Phrase("\n\n", pdf.font8));
                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                celda.setBorder(0);
                celda.setColspan(tamod.length);
                tabla.addCell(celda);

                celda = new PdfPCell(new Phrase("Analisis Cefalométrico".toUpperCase(), pdf.font10n));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                celda.setBorder(15);
                celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
                celda.setColspan(tamod.length);
                tabla.addCell(celda);
                celda = new PdfPCell(new Phrase("\n", pdf.font8));
                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                celda.setBorder(12);
                celda.setColspan(tamod.length);
                tabla.addCell(celda);

                for (int v = 0; v < lista_ImFot.size(); v++) {
                    if (list.get("chFT").equals("0") && v > 0) {
                        break;
                    }
                    ruta = lista_ImFot.get(v).get("RUTA");

                    System.out.println("ruta--->" + ruta);
                    try {
                        m1 = Image.getInstance("" + ruta);
                    } catch (Exception e) {
                        System.out.println("e---->" + e.toString());
                        ruta = "";
                        //m1 = Image.getInstance(""+ruta);
                        m1 = null;
                    }

                    System.out.println("m1--->" + m1);
                    celda = new PdfPCell(new Phrase("", pdf.font12n));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    celda.setBorder(4);
                    tabla.addCell(celda);
                    mw = 550;
                    mh = 250;
                    if (m1 != null) {
                        m1.setAlignment(Image.MIDDLE);
                        m1.scaleAbsolute(mw, mh);
                        celda = new PdfPCell(m1);
                        celda.setFixedHeight(mh + 5);
                        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        celda.setBorder(0);
                        tabla.addCell(celda);
                    } else {
                        celda = new PdfPCell(new Phrase("", pdf.font12n));
                        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        celda.setBorder(0);
                        tabla.addCell(celda);
                    }

                    celda = new PdfPCell(new Phrase("", pdf.font12n));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    celda.setBorder(8);
                    tabla.addCell(celda);

                }
                celda = new PdfPCell(new Phrase("\n", pdf.font8));
                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                celda.setBorder(14);
                celda.setColspan(tamco.length);
                tabla.addCell(celda);
                documento.add(tabla);
                //documento.newPage();
                //</editor-fold>
            }
            IMPRIMIR("!!!!! SE SUPONE QUE YA LO CREE!!!");
        } catch (Exception ex) {
            ex.printStackTrace();
            IMPRIMIR(" ERROR ----- ERROR-----");
        }
    }

    private void infHistcerradas(Document documento, Map<String, String> list) {
        try {
            IMPRIMIR("+++++ ENTRE?");
            String consulta = "";
            if (list.get("rHist").equals("true")) {
                consulta = "SELECT per.`pk_persona` IDENTIFICACION, CONCAT_WS(' ', per.`primer_nombre`, IFNULL(per.`segundo_nombre`, ''), per.`primer_apellido`, IFNULL(per.`segundo_apellido`, '')) NOMBRE, IFNULL (ultc.`fecha_cita`, '')\n"
                        + "FROM historias_clinicas hc\n"
                        + "LEFT JOIN personas per ON hc.`pfk_paciente` = CONCAT(per.`pfk_tipo_documento`, per.`pk_persona`)\n"
                        + "LEFT JOIN (SELECT cit.`pfk_paciente`, cit.`fecha_cita` FROM citas cit\n"
                        + "GROUP BY cit.`pfk_paciente` DESC) ultc ON ultc.pfk_paciente = hc.`pfk_paciente`\n"
                        + "WHERE hc.`estado` = 'Terminada'";
            } else {

                consulta = "SELECT per.`pk_persona` IDENTIFICACION, CONCAT_WS(' ', per.`primer_nombre`, IFNULL(per.`segundo_nombre`, ''), per.`primer_apellido`, IFNULL(per.`segundo_apellido`, '')) NOMBRE, IFNULL (ultc.`fecha_cita`, '')\n"
                        + "FROM historias_clinicas hc\n"
                        + "LEFT JOIN personas per ON hc.`pfk_paciente` = CONCAT(per.`pfk_tipo_documento`, per.`pk_persona`)\n"
                        + "LEFT JOIN (SELECT cit.`pfk_paciente`, cit.`fecha_cita` FROM citas cit\n"
                        + "GROUP BY cit.`pfk_paciente` DESC) ultc ON ultc.pfk_paciente = hc.`pfk_paciente`\n"
                        + "WHERE hc.`estado` = 'Terminada' AND hc.`fecha` BETWEEN '" + list.get("fini") + "' AND '" + list.get("ffin") + "'";
            }
            ArrayList<String[]> resql = resultquery.SELECT(consulta);

            PdfPCell celda = null;
            float[] tam = new float[]{5, 17, 58, 20};

            PdfPTable tabla = new PdfPTable(tam);
            tabla.setWidthPercentage(100);
            celda = new PdfPCell(new Phrase("N°", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(15);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("IDENTIFICACION", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(15);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("NOMBRE", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(15);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("ULTIMA CITA", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(15);
            tabla.addCell(celda);

            if (resql.size() > 0) {
                for (int i = 0; i < resql.size(); i++) {
                    celda = new PdfPCell(new Phrase("" + (i + 1), pdf.font10));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder(15);
                    tabla.addCell(celda);
                    celda = new PdfPCell(new Phrase("" + resql.get(i)[0], pdf.font10));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder(15);
                    tabla.addCell(celda);
                    celda = new PdfPCell(new Phrase("" + resql.get(i)[1], pdf.font10));
                    celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder(15);
                    tabla.addCell(celda);
                    celda = new PdfPCell(new Phrase("" + resql.get(i)[2], pdf.font10));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder(15);
                    tabla.addCell(celda);
                }
            } else {
                celda = new PdfPCell(new Phrase("\n No se encontraron registros.\n\n", pdf.font15n));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                celda.setBorder(15);
                celda.setColspan(tam.length);
                tabla.addCell(celda);
            }

            documento.add(tabla);
            IMPRIMIR("!!!!! SE SUPONE QUE YA LO CREE!!!");
        } catch (Exception ex) {
            ex.printStackTrace();
            IMPRIMIR(" ERROR ----- ERROR-----");
        }
    }

    private void infHistinactivas(Document documento, Map<String, String> list) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void infFactestados(Document documento, Map<String, String> list) {
        try {
            String cons = "", add = "";
            System.out.println("aquiiii");
            if (!list.get("tipo").equals("Todos")) {
                System.out.println("aquiiii en el ");
                add = " AND fac.`estado` ='" + list.get("tipo") + "'";
            }
            if (list.get("rHist").equals("true")) {

                cons = "SELECT IF(paux.pk_paciente_auxiliar IS NULL, CONCAT(per.`pfk_tipo_documento`, per.`pk_persona`), paux.`pk_paciente_auxiliar`) ID,\n"
                        + "IF(paux.pk_paciente_auxiliar IS NULL,CONCAT_WS(' ', per.`primer_nombre`, per.`primer_apellido`, IFNULL(per.`segundo_apellido`, '')),\n"
                        + "CONCAT_WS(' ', paux.`primer_nombre`, paux.`primer_apellido`, IFNULL(paux.`segundo_apellido`, ''))) PACIENTE ,\n"
                        + "fac.`numero`, fac.`valor_fact`, fac.`estado`, DATE_FORMAT(fac.`fecha`, '%Y-%m-%d') fecha  \n"
                        + "FROM `facturas` fac \n"
                        + "LEFT JOIN `personas` per ON CONCAT (per.`pfk_tipo_documento`, per.`pk_persona`) = fac.`pfk_paciente`\n"
                        + "LEFT JOIN paciente_auxiliar paux ON fac.pfk_paciente = paux.pk_paciente_auxiliar AND paux.estado = 'Activo'\n"
                        + "WHERE IF(paux.pk_paciente_auxiliar IS NULL, CONCAT(per.`pfk_tipo_documento`, per.`pk_persona`), paux.`pk_paciente_auxiliar`) IS NOT NULL  " + add + "\n"
                        + "ORDER BY fac.`numero` ASC";

            } else {
                cons = "SELECT IF(paux.pk_paciente_auxiliar IS NULL, CONCAT(per.`pfk_tipo_documento`, per.`pk_persona`), paux.`pk_paciente_auxiliar`) ID,\n"
                        + "IF(paux.pk_paciente_auxiliar IS NULL,CONCAT_WS(' ', per.`primer_nombre`, per.`primer_apellido`, IFNULL(per.`segundo_apellido`, '')),\n"
                        + "CONCAT_WS(' ', paux.`primer_nombre`, paux.`primer_apellido`, IFNULL(paux.`segundo_apellido`, ''))) PACIENTE ,\n"
                        + "fac.`numero`, fac.`valor_fact`, fac.`estado`, DATE_FORMAT(fac.`fecha`, '%Y-%m-%d') fecha  \n"
                        + "FROM `facturas` fac \n"
                        + "LEFT JOIN `personas` per ON CONCAT (per.`pfk_tipo_documento`, per.`pk_persona`) = fac.`pfk_paciente`\n"
                        + "LEFT JOIN paciente_auxiliar paux ON fac.pfk_paciente = paux.pk_paciente_auxiliar AND paux.estado = 'Activo'\n"
                        + "WHERE fac.`fecha` >='" + list.get("fini") + "' AND fac.`fecha` <= '" + list.get("ffin") + "'  \n"
                        + "AND IF(paux.pk_paciente_auxiliar IS NULL, CONCAT(per.`pfk_tipo_documento`, per.`pk_persona`), paux.`pk_paciente_auxiliar`) IS NOT NULL " + add + "\n  "
                        + "ORDER BY fac.`numero` ASC";
            }
            ArrayList<String[]> resc = resultquery.SELECT(cons);
            IMPRIMIR("resotadpo consulya" + resc);
            PdfPCell celda = null;

            float[] tam = new float[]{5, 15, 30, 10, 10, 10, 20};

            PdfPTable tabla = new PdfPTable(tam);
            tabla.setWidthPercentage(100);

            Date initDate = Utilidades.getDateFromFormat(
                    list.get("fini"), FORMAT_DATE_INPUT
            );
            Date finalDate = Utilidades.getDateFromFormat(
                    list.get("ffin"), FORMAT_DATE_INPUT
            );

            celda = new PdfPCell(
                    new Phrase(
                            String.format(
                                    "Desde el %s Hasta el %s\n\n",
                                    Utilidades.getDateToShow(
                                            initDate, FORMAT_DATE_OUTPUT
                                    ),
                                    Utilidades.getDateToShow(
                                            finalDate, FORMAT_DATE_OUTPUT
                                    )
                            ),
                            pdf.font10
                    )
            );
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(0);
            celda.setColspan(tam.length);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase("N°", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(15);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("IDENTIFICACIÓN", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(15);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("NOMBRE", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(15);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("NÚMERO", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(15);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("VALOR", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(15);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("ESTADO", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(15);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("FECHA", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(15);
            tabla.addCell(celda);

            if (resc.size() > 0) {
                for (int i = 0; i < resc.size(); i++) {
                    celda = new PdfPCell(new Phrase("" + (i + 1), pdf.font10));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder(15);
                    tabla.addCell(celda);
                    celda = new PdfPCell(new Phrase("" + resc.get(i)[0], pdf.font10));
                    celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder(15);
                    tabla.addCell(celda);
                    celda = new PdfPCell(new Phrase("" + resc.get(i)[1], pdf.font10));
                    celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder(15);
                    tabla.addCell(celda);
                    celda = new PdfPCell(new Phrase("" + resc.get(i)[2], pdf.font10));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder(15);
                    tabla.addCell(celda);
                    celda = new PdfPCell(new Phrase("" + Utilidades.formatomoneda(resc.get(i)[3]), pdf.font10));
                    celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder(15);
                    tabla.addCell(celda);
                    celda = new PdfPCell(new Phrase("" + resc.get(i)[4], pdf.font10));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder(15);
                    tabla.addCell(celda);
                    celda = new PdfPCell(new Phrase("" + resc.get(i)[5], pdf.font10));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder(15);
                    tabla.addCell(celda);
                }
            } else {
                celda = new PdfPCell(new Phrase("\n No se encontraron registros.\n\n", pdf.font15n));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                celda.setBorder(15);
                celda.setColspan(tam.length);
                tabla.addCell(celda);
            }
            documento.add(tabla);
            IMPRIMIR("!!!!! SE SUPONE QUE YA LO CREE!!!");

        } catch (Exception ex) {
            IMPRIMIR(" error ocurred " + ex);
        }
    }

    private void infFactpormes(Document documento, Map<String, String> list) {
        try {
            
            String consulta = "", add = "";

            if (list.get("rHist").equals("false")) {
                add = " fac.`fecha`>= '" + list.get("fini") + "' AND fac.`fecha`<='" + list.get("ffin") + "' AND ";
            }

            consulta = "SELECT IF(paux.pk_paciente_auxiliar IS NULL,CONCAT_WS(' ', per.`primer_nombre`, IFNULL(per.`segundo_nombre`, ''), per.`primer_apellido`, IFNULL(per.`segundo_apellido`, '')),\n"
                    + "CONCAT_WS(' ', paux.`primer_nombre`, IFNULL(paux.`segundo_nombre`, ''), paux.`primer_apellido`, IFNULL(paux.`segundo_apellido`, ''))) PACIENTE,\n"
                    + "fac.`numero`, DATE_FORMAT(fac.`fecha`, '%Y-%m-%d') fecha , fac.`valor_fact`, fac.`estado` \n"
                    + "FROM facturas fac\n"
                    + "LEFT JOIN personas per ON fac.`pfk_paciente` = CONCAT(per.`pfk_tipo_documento`, per.`pk_persona`)\n"
                    + "LEFT JOIN paciente_auxiliar paux ON fac.pfk_paciente = paux.pk_paciente_auxiliar AND paux.estado = 'Activo'\n"
                    + "WHERE " + add + " \n"
                    + "IF(paux.pk_paciente_auxiliar IS NULL, CONCAT(per.`pfk_tipo_documento`, per.`pk_persona`), paux.`pk_paciente_auxiliar`) IS NOT NULL\n ORDER BY fac.`numero` ASC";
            
            ArrayList<String[]> resql = resultquery.SELECT(consulta);

            PdfPCell celda = null;
            float[] tam = new float[]{10, 30, 20, 15, 15, 10};

            PdfPTable tabla = new PdfPTable(tam);
            tabla.setWidthPercentage(100);
            
            Date initDate = Utilidades.getDateFromFormat(
                    list.get("fini"), FORMAT_DATE_INPUT
            );
            Date finalDate = Utilidades.getDateFromFormat(
                    list.get("ffin"), FORMAT_DATE_INPUT
            );

            celda = new PdfPCell(
                    new Phrase(
                            String.format(
                                    "Desde el %s Hasta el %s\n\n",
                                    Utilidades.getDateToShow(
                                            initDate, FORMAT_DATE_OUTPUT
                                    ),
                                    Utilidades.getDateToShow(
                                            finalDate, FORMAT_DATE_OUTPUT
                                    )
                            ),
                            pdf.font10
                    )
            );
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(0);
            celda.setColspan(tam.length);
            tabla.addCell(celda);

            
            celda = new PdfPCell(new Phrase("N°", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(15);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("NOMBRE", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(15);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("N° DE FACTURA", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(15);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("FECHA DE PAGO", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(15);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("VALOR", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(15);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("ESTADO", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(15);
            tabla.addCell(celda);

            if (resql.size() > 0) {
                for (int i = 0; i < resql.size(); i++) {
                    celda = new PdfPCell(new Phrase("" + (i + 1), pdf.font10));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder(15);
                    tabla.addCell(celda);
                    celda = new PdfPCell(new Phrase("" + resql.get(i)[0], pdf.font10));
                    celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder(15);
                    tabla.addCell(celda);
                    celda = new PdfPCell(new Phrase("" + resql.get(i)[1], pdf.font10));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder(15);
                    tabla.addCell(celda);
                    celda = new PdfPCell(new Phrase("" + resql.get(i)[2], pdf.font10));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder(15);
                    tabla.addCell(celda);
                    celda = new PdfPCell(new Phrase("" + Utilidades.formatomoneda(resql.get(i)[3]), pdf.font10));
                    celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder(15);
                    tabla.addCell(celda);
                    celda = new PdfPCell(new Phrase("" + resql.get(i)[4], pdf.font10));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder(15);
                    tabla.addCell(celda);
                }
            } else {
                celda = new PdfPCell(new Phrase("\n No se encontraron registros.\n\n", pdf.font15n));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                celda.setBorder(15);
                celda.setColspan(tam.length);
                tabla.addCell(celda);
            }
            documento.add(tabla);
            IMPRIMIR("!!!!! SE SUPONE QUE YA LO CREE!!!");
        } catch (Exception ex) {
            ex.printStackTrace();
            IMPRIMIR(" ERROR ----- ERROR-----");
        }
    }

    private void infFactrecdia(Document documento, Map<String, String> list) {
        try {
            IMPRIMIR("+++++ ENTRE? infFactrecdia");
            String consulta = "SELECT fac.`numero`, fac.`valor_fact`, \n"
                    + "IF(paux.pk_paciente_auxiliar IS NULL, \n"
                    + "CONCAT_WS(' ', per.`primer_nombre`, IFNULL(per.`segundo_nombre`, ''), per.`primer_apellido`, IFNULL(per.`segundo_apellido`, '')),\n"
                    + "CONCAT_WS(' ', paux.`primer_nombre`, IFNULL(paux.`segundo_nombre`, ''), paux.`primer_apellido`, IFNULL(paux.`segundo_apellido`, ''))\n"
                    + ") NOMBRE, \n"
                    + "fac.`pfk_paciente`, GROUP_CONCAT(IFNULL(mop.`pk_tipo_pago`, 'Ninguno')) Tipo, SUM(IFNULL(mop.`valor_pagado`, 0)) Valor,  DATE_FORMAT(fac.`fecha_pago`, '%Y-%m-%d') fecha\n"
                    + "FROM facturas fac\n"
                    + "LEFT JOIN personas per ON fac.`pfk_paciente`= CONCAT(per.`pfk_tipo_documento`, per.`pk_persona`)\n"
                    + "LEFT JOIN paciente_auxiliar paux ON fac.pfk_paciente = paux.pk_paciente_auxiliar AND paux.estado = 'Activo'\n"
                    + "LEFT JOIN modo_pago mop ON mop.`pfk_pago` = fac.`numero` \n"
                    + "WHERE DATE_FORMAT(fac.`fecha_pago`, '%Y-%m-%d') = '" + list.get("fini") + "' AND fac.`estado` = 'pagado'\n"
                    + "AND IF(paux.pk_paciente_auxiliar IS NULL, CONCAT(per.`pfk_tipo_documento`, per.`pk_persona`), paux.`pk_paciente_auxiliar`) IS NOT NULL\n"
                    + "GROUP BY fac.`numero` ";
            System.out.println("consulta -> " + consulta);

            ArrayList<String[]> resql = resultquery.SELECT(consulta);

            PdfPCell celda = null;
            float[] tam = new float[]{5, 45, 20, 15, 15};

            PdfPTable tabla = new PdfPTable(tam);
            tabla.setWidthPercentage(100);
            Date date = Utilidades.getDateFromFormat(
                    list.get("fini"), FORMAT_DATE_INPUT
            );

            celda = new PdfPCell(new Phrase(
                    "Recaudo del día: "
                    + Utilidades.getDateToShow(date, FORMAT_DATE_OUTPUT)
                    + "\n\n", pdf.font15n)
            );
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(0);
            celda.setColspan(tam.length);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase("N°", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(15);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("NOMBRE", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(15);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("N° FACTURA", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(15);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("TIPO PAGO", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(15);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("VALOR PAGADO", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(15);
            tabla.addCell(celda);

            if (resql.size() > 0) {
                int sumaRecaudo = 0;
                for (int i = 0; i < resql.size(); i++) {
                    sumaRecaudo += Integer.parseInt(resql.get(i)[1]);
                    celda = new PdfPCell(new Phrase("" + (i + 1), pdf.font10));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder(15);
                    tabla.addCell(celda);
                    celda = new PdfPCell(new Phrase("" + resql.get(i)[2], pdf.font10));
                    celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder(15);
                    tabla.addCell(celda);
                    celda = new PdfPCell(new Phrase("" + resql.get(i)[0], pdf.font10));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder(15);
                    tabla.addCell(celda);
                    celda = new PdfPCell(new Phrase("" + resql.get(i)[4], pdf.font10));
                    celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder(15);
                    tabla.addCell(celda);

                    celda = new PdfPCell(new Phrase("" + Utilidades.formatomoneda(resql.get(i)[1]), pdf.font10));
                    celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder(15);
                    tabla.addCell(celda);
                }
                if (sumaRecaudo > 0) {
                    celda = new PdfPCell(new Phrase("TOTAL", pdf.font10n));
                    celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    celda.setColspan(4);
                    celda.setBorder(15);
                    tabla.addCell(celda);
                    celda = new PdfPCell(new Phrase(Utilidades.formatomoneda("" + sumaRecaudo), pdf.font10n));
                    celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    celda.setBorder(15);
                    tabla.addCell(celda);
                }

            } else {
                celda = new PdfPCell(new Phrase("\n No se encontraron registros.\n\n", pdf.font15n));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                celda.setBorder(15);
                celda.setColspan(tam.length);
                tabla.addCell(celda);
            }
            documento.add(tabla);
            IMPRIMIR("!!!!! SE SUPONE QUE YA LO CREE!!!");
        } catch (Exception ex) {
            ex.printStackTrace();
            IMPRIMIR(" ERROR ----- ERROR-----");
        }
    }

    private void infFactrecmes(Document documento, Map<String, String> list) {
        try {
            String consulta = "", add = "";
            PdfPCell celda = null;
            if (list.get("rHist").equals("false")) {
                add = " fac.`fecha_pago` >= '" + list.get("fini") + "' AND fac.`fecha_pago` <='" + list.get("ffin") + "' AND ";
            }
            consulta = "SELECT fac.`numero`, fac.`valor_fact`, \n"
                    + "IF(paux.pk_paciente_auxiliar IS NULL, \n"
                    + "CONCAT_WS(' ', per.`primer_nombre`, IFNULL(per.`segundo_nombre`, ''), per.`primer_apellido`, IFNULL(per.`segundo_apellido`, '')),\n"
                    + "CONCAT_WS(' ', paux.`primer_nombre`, IFNULL(paux.`segundo_nombre`, ''), paux.`primer_apellido`, IFNULL(paux.`segundo_apellido`, ''))\n"
                    + ") NOMBRE,\n"
                    + "fac.`pfk_paciente`, IFNULL(mop.`pk_tipo_pago`, 'Ninguno') Tipo, IFNULL(mop.`valor_pagado`, 0) Valor,\n"
                    + "DATE_FORMAT(fac.`fecha_pago`,'%Y-%m-%d') Fecha, DATE_FORMAT(fac.`fecha_pago`,'%Y') A, DATE_FORMAT(fac.`fecha_pago`,'%m') M\n"
                    + "FROM facturas fac\n"
                    + "LEFT JOIN personas per ON fac.`pfk_paciente`= CONCAT(per.`pfk_tipo_documento`, per.`pk_persona`)\n"
                    + "LEFT JOIN paciente_auxiliar paux ON fac.pfk_paciente = paux.pk_paciente_auxiliar AND paux.estado = 'Activo'\n"
                    + "LEFT JOIN modo_pago mop ON mop.`pfk_pago` = fac.`numero` \n"
                    + "WHERE " + add + "  fac.`estado` = 'pagado' AND \n"
                    + " IF(paux.pk_paciente_auxiliar IS NULL, CONCAT(per.`pfk_tipo_documento`, per.`pk_persona`), paux.`pk_paciente_auxiliar`) IS NOT NULL\n"
                    + "ORDER BY fac.`fecha_pago` ASC";

            System.out.println("consulta" + consulta);
            ArrayList<String[]> resql = resultquery.SELECT(consulta);
            List<Map<String, String>> lista = resultquery.ListSQL(consulta);

            List<Map<String, String>> lista_pagos = new ArrayList<Map<String, String>>();
            List<Map<String, String>> lista_pagose = new ArrayList<Map<String, String>>();
            List<Map<String, String>> lista_meses = new ArrayList<Map<String, String>>();
            int tote = 0, tott = 0;
            
            if (lista.size() > 0) {
                lista_meses = gen.data_list(10, lista, new String[]{"A", "M"});
                System.out.println("lista-meses-->" + lista_meses);

                for (Map<String, String> lstm : lista_meses) {
                    String mes = Meses[Integer.parseInt(lstm.get("M")) - 1];
                    lista_pagos = gen.data_list(1, lista, new String[]{"Tipo"}, new String[]{"A<->" + lstm.get("A"), "M<->" + lstm.get("M")});

                    for (Map<String, String> lst : lista_pagos) {
                        lista_pagose = gen.data_list(1, lista, new String[]{"numero"}, new String[]{"A<->" + lst.get("A"), "M<->" + lst.get("M"), "Tipo<->" + lst.get("Tipo")});
                        int sum = 0;
                        for (Map<String, String> lste : lista_pagose) {
                            sum += Integer.parseInt(lste.get("Valor").equals("") ? "0" : lste.get("Valor"));
                        }
                        if (lst.get("Tipo").equals("Efectivo")) {
                            tote = sum;
                        } else if (lst.get("Tipo").equals("Tarjeta")) {
                            tott = sum;
                        }

                    }

                    ////TOTAL PAGOS
                    System.out.println("COMIENZO PDF");
                    float[] tamt = new float[]{20, 76};
                    PdfPTable tablatot = new PdfPTable(tamt);
                    tablatot.setWidthPercentage(100);

                    celda = new PdfPCell(new Phrase("MES DE " + mes + " DEL " + lstm.get("A"), pdf.font10n));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder(0);
                    celda.setColspan(2);
                    tablatot.addCell(celda);
                    celda = new PdfPCell(new Phrase(" ", pdf.font10n));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder(0);
                    celda.setColspan(2);
                    tablatot.addCell(celda);
                    celda = new PdfPCell(new Phrase("TOTAL EFECTIVO", pdf.font10n));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder(0);
                    tablatot.addCell(celda);
                    celda = new PdfPCell(new Phrase("" + Utilidades.formatomoneda("" + tote), pdf.font10n));
                    celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder(0);
                    tablatot.addCell(celda);
                    celda = new PdfPCell(new Phrase("TOTAL TARJETA", pdf.font10n));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder(0);
                    tablatot.addCell(celda);
                    celda = new PdfPCell(new Phrase("" + Utilidades.formatomoneda("" + tott), pdf.font10n));
                    celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder(0);
                    tablatot.addCell(celda);
                    celda = new PdfPCell(new Phrase("\n\n", pdf.font10n));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder(0);
                    celda.setColspan(tamt.length);
                    tablatot.addCell(celda);
                    documento.add(tablatot);
                    System.out.println("________________________________________________");
                    if (lista_meses.size() == 1) {
                        float[] tam = new float[]{5, 45, 25, 25};
                        PdfPTable tabla = new PdfPTable(tam);
                        tabla.setWidthPercentage(100);
                        celda = new PdfPCell(new Phrase("N°", pdf.font10n));
                        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                        celda.setVerticalAlignment(Element.ALIGN_CENTER);
                        celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        celda.setBorder(15);
                        tabla.addCell(celda);
                        celda = new PdfPCell(new Phrase("NOMBRE", pdf.font10n));
                        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                        celda.setVerticalAlignment(Element.ALIGN_CENTER);
                        celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        celda.setBorder(15);
                        tabla.addCell(celda);
                        celda = new PdfPCell(new Phrase("N° FACTURA", pdf.font10n));
                        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                        celda.setVerticalAlignment(Element.ALIGN_CENTER);
                        celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        celda.setBorder(15);
                        tabla.addCell(celda);
                        celda = new PdfPCell(new Phrase("VALOR PAGADO", pdf.font10n));
                        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                        celda.setVerticalAlignment(Element.ALIGN_CENTER);
                        celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        celda.setBorder(15);
                        tabla.addCell(celda);
                        int i = 0;
                        for (Map<String, String> lst : lista) {
                            i++;
                            celda = new PdfPCell(new Phrase("" + i, pdf.font10));
                            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                            celda.setVerticalAlignment(Element.ALIGN_CENTER);
                            celda.setBorder(15);
                            tabla.addCell(celda);
                            celda = new PdfPCell(new Phrase("" + lst.get("NOMBRE"), pdf.font10));
                            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                            celda.setVerticalAlignment(Element.ALIGN_CENTER);
                            celda.setBorder(15);
                            tabla.addCell(celda);
                            celda = new PdfPCell(new Phrase("" + lst.get("numero"), pdf.font10));
                            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                            celda.setVerticalAlignment(Element.ALIGN_CENTER);
                            celda.setBorder(15);
                            tabla.addCell(celda);
                            celda = new PdfPCell(new Phrase("" + lst.get("valor_fact"), pdf.font10));
                            celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            celda.setVerticalAlignment(Element.ALIGN_CENTER);
                            celda.setBorder(15);
                            tabla.addCell(celda);

                        }

                        documento.add(tabla);
                    }
                }
            }
            IMPRIMIR("!!!!! SE SUPONE QUE YA LO CREE!!!");
        } catch (Exception ex) {
            ex.printStackTrace();
            IMPRIMIR(" ERROR ----- ERROR-----");
        }
    }

    private void infFactrectp(Document documento, Map<String, String> list) {
        try {
            IMPRIMIR("+++++ ENTRE?");
            String consulta = "", add = "", add2 = "";
            PdfPCell celda = null;

            if (!list.get("tipo").equals("Todos")) {
                add2 = " mop.`pk_tipo_pago`='" + list.get("tipo") + "' AND ";
            }
            if (list.get("rHist").equals("false")) {
                add = " fac.`fecha_pago` >= '" + list.get("fini") + "' AND fac.`fecha_pago` <='" + list.get("ffin") + "' AND ";
            }

            consulta = "SELECT fac.`numero`, fac.`valor_fact`,\n"
                    + "IF(paux.pk_paciente_auxiliar IS NULL,CONCAT_WS(' ', per.`primer_nombre`, IFNULL(per.`segundo_nombre`, ''), per.`primer_apellido`, IFNULL(per.`segundo_apellido`, '')),\n"
                    + "CONCAT_WS(' ', paux.`primer_nombre`, IFNULL(paux.`segundo_nombre`, ''), paux.`primer_apellido`, IFNULL(paux.`segundo_apellido`, ''))) NOMBRE,\n"
                    + "fac.`pfk_paciente`, IFNULL(mop.`pk_tipo_pago`, 'Ninguno') pk_tipo_pago, IFNULL(mop.`valor_pagado`, 0) valor_pagado, fac.`fecha_pago`, DATE_FORMAT(fac.`fecha_pago`,'%Y') A, DATE_FORMAT(fac.`fecha_pago`,'%m') M\n"
                    + "FROM facturas fac\n"
                    + "LEFT JOIN personas per ON fac.`pfk_paciente`= CONCAT(per.`pfk_tipo_documento`, per.`pk_persona`)\n"
                    + "LEFT JOIN paciente_auxiliar paux ON fac.pfk_paciente = paux.pk_paciente_auxiliar AND paux.estado = 'Activo'\n"
                    + "LEFT JOIN modo_pago mop ON mop.`pfk_pago` = fac.`numero`\n"
                    + "WHERE " + add + add2 + "  fac.`estado` = 'pagado' AND \n"
                    + " IF(paux.pk_paciente_auxiliar IS NULL, CONCAT(per.`pfk_tipo_documento`, per.`pk_persona`), paux.`pk_paciente_auxiliar`) IS NOT NULL\n"
                    + "ORDER BY fac.`fecha_pago` ASC";
            System.out.println("*******************************END CONSULTA******************************************************");
            System.out.println("consulta" + consulta);
            ArrayList<String[]> resql = resultquery.SELECT(consulta);
            List<Map<String, String>> lista = resultquery.ListSQL(consulta);
            IMPRIMIR("VALOR RECAUDOTIPO " + list.get("tipo"));
            if (list.get("tipo").equals("Efectivo") || list.get("tipo").equals("Tarjeta")) {
                IMPRIMIR(" ENTRADA N EFECTVO O TARJETA");
                List<Map<String, String>> lista_pagos = new ArrayList<Map<String, String>>();
                List<Map<String, String>> lista_pagose = new ArrayList<Map<String, String>>();
                List<Map<String, String>> lista_meses = new ArrayList<Map<String, String>>();
                int tote = 0, tott = 0;
                int sum = 0;
                if (lista.size() > 0) {
                    lista_meses = gen.data_list(10, lista, new String[]{"A", "M"});

                    IMPRIMIR("??????" + lista_meses.size());
                    for (Map<String, String> lstm : lista_meses) {
                        String mes = Meses[Integer.parseInt(lstm.get("M")) - 1];
                        lista_pagos = gen.data_list(1, lista, new String[]{"pk_tipo_pago"}, new String[]{"A<->" + lstm.get("A"), "M<->" + lstm.get("M")});

                        for (Map<String, String> lst : lista_pagos) {
                            lista_pagose = gen.data_list(1, lista, new String[]{"numero"}, new String[]{"A<->" + lst.get("A"), "M<->" + lst.get("M"), "pk_tipo_pago<->" + lst.get("pk_tipo_pago")});

                            for (Map<String, String> lste : lista_pagose) {
                                sum += Integer.parseInt(lste.get("valor_pagado").equals("") ? "0" : lste.get("valor_pagado"));
                                IMPRIMIR("valorrrrrr " + sum);
                            }
                        }

                        ////TOTAL PAGOS
                        float[] tamt = new float[]{20, 76};
                        PdfPTable tablatot = new PdfPTable(tamt);
                        tablatot.setWidthPercentage(100);

                        celda = new PdfPCell(new Phrase("MES DE " + mes + " DEL " + lstm.get("A"), pdf.font10n));
                        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                        celda.setVerticalAlignment(Element.ALIGN_CENTER);
                        celda.setBorder(15);
                        celda.setColspan(2);
                        tablatot.addCell(celda);
                        celda = new PdfPCell(new Phrase(" ", pdf.font10n));
                        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                        celda.setVerticalAlignment(Element.ALIGN_CENTER);
                        celda.setBorder(0);
                        celda.setColspan(2);
                        tablatot.addCell(celda);

                        IMPRIMIR("+++++++++++++LISTA:MESES++++++++++++++++++" + lista_meses.size());
                        celda = new PdfPCell(new Phrase("TOTAL " + lstm.get("pk_tipo_pago").toUpperCase(), pdf.font10n));
                        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                        celda.setVerticalAlignment(Element.ALIGN_CENTER);
                        celda.setBorder(0);
                        tablatot.addCell(celda);
                        celda = new PdfPCell(new Phrase("" + sum, pdf.font10n));
                        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                        celda.setVerticalAlignment(Element.ALIGN_CENTER);
                        celda.setBorder(0);
                        tablatot.addCell(celda);
                        celda = new PdfPCell(new Phrase("\n\n", pdf.font10n));
                        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                        celda.setVerticalAlignment(Element.ALIGN_CENTER);
                        celda.setBorder(0);
                        celda.setColspan(tamt.length);
                        tablatot.addCell(celda);
                        documento.add(tablatot);

                        IMPRIMIR("+++++++++++++LISTA:MESES++++++++++++++++++" + lista_meses.size());
                        if (lista_meses.size() == 1) {
                            float[] tam = new float[]{5, 45, 25, 25};
                            PdfPTable tabla = new PdfPTable(tam);
                            tabla.setWidthPercentage(100);
                            celda = new PdfPCell(new Phrase("N°", pdf.font10n));
                            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                            celda.setVerticalAlignment(Element.ALIGN_CENTER);
                            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
                            celda.setBorder(15);
                            tabla.addCell(celda);
                            celda = new PdfPCell(new Phrase("NOMBRE", pdf.font10n));
                            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                            celda.setVerticalAlignment(Element.ALIGN_CENTER);
                            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
                            celda.setBorder(15);
                            tabla.addCell(celda);
                            celda = new PdfPCell(new Phrase("N° FACTURA", pdf.font10n));
                            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                            celda.setVerticalAlignment(Element.ALIGN_CENTER);
                            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
                            celda.setBorder(15);
                            tabla.addCell(celda);
                            celda = new PdfPCell(new Phrase("VALOR PAGADO", pdf.font10n));
                            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                            celda.setVerticalAlignment(Element.ALIGN_CENTER);
                            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
                            celda.setBorder(15);
                            tabla.addCell(celda);
                            int i = 0;
                            for (Map<String, String> lst : lista) {
                                i++;
                                celda = new PdfPCell(new Phrase("" + i, pdf.font10));
                                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                                celda.setBorder(15);
                                tabla.addCell(celda);
                                celda = new PdfPCell(new Phrase("" + lst.get("NOMBRE"), pdf.font10));
                                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                                celda.setBorder(15);
                                tabla.addCell(celda);
                                celda = new PdfPCell(new Phrase("" + lst.get("numero"), pdf.font10));
                                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                                celda.setBorder(15);
                                tabla.addCell(celda);
                                celda = new PdfPCell(new Phrase("" + lst.get("valor_pagado"), pdf.font10));
                                celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
                                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                                celda.setBorder(15);
                                tabla.addCell(celda);
                            }

                            documento.add(tabla);
                        }
                    }

                }

            } else if (list.get("tipo").equals("Todos")) {
                IMPRIMIR("********************ENTRE TODOS******************************");
                List<Map<String, String>> lista_pagos = new ArrayList<Map<String, String>>();
                List<Map<String, String>> lista_pagose = new ArrayList<Map<String, String>>();
                List<Map<String, String>> lista_meses = new ArrayList<Map<String, String>>();
                int tote = 0, tott = 0;
                IMPRIMIR("****************LISTA SIZE" + lista.size());
                if (lista.size() > 0) {
                    lista_meses = gen.data_list(10, lista, new String[]{"A", "M"});

                    IMPRIMIR("******************************??????" + lista_meses.size());
                    for (Map<String, String> lstm : lista_meses) {
                        String mes = Meses[Integer.parseInt(lstm.get("M")) - 1];
                        lista_pagos = gen.data_list(1, lista, new String[]{"pk_tipo_pago"}, new String[]{"A<->" + lstm.get("A"), "M<->" + lstm.get("M")});

                        for (Map<String, String> lst : lista_pagos) {
                            lista_pagose = gen.data_list(1, lista, new String[]{"numero"}, new String[]{"A<->" + lst.get("A"), "M<->" + lst.get("M"), "pk_tipo_pago<->" + lst.get("pk_tipo_pago")});
                            int sum = 0;
                            for (Map<String, String> lste : lista_pagose) {
                                sum += Integer.parseInt(lste.get("valor_pagado").equals("") ? "0" : lste.get("valor_pagado"));
                            }
                            if (lst.get("pk_tipo_pago").equals("Efectivo")) {
                                tote = sum;
                            } else if (lst.get("pk_tipo_pago").equals("Tarjeta")) {
                                tott = sum;
                            }
                        }

                        ////TOTAL PAGOS
                        float[] tamt = new float[]{20, 76};
                        PdfPTable tablatot = new PdfPTable(tamt);
                        tablatot.setWidthPercentage(100);

                        celda = new PdfPCell(new Phrase("MES DE " + mes + " DEL " + lstm.get("A"), pdf.font10n));
                        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                        celda.setVerticalAlignment(Element.ALIGN_CENTER);
                        celda.setBorder(0);
                        celda.setColspan(2);
                        tablatot.addCell(celda);
                        celda = new PdfPCell(new Phrase(" ", pdf.font10n));
                        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                        celda.setVerticalAlignment(Element.ALIGN_CENTER);
                        celda.setBorder(0);
                        celda.setColspan(2);
                        tablatot.addCell(celda);
                        celda = new PdfPCell(new Phrase("TOTAL EFECTIVO", pdf.font10n));
                        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                        celda.setVerticalAlignment(Element.ALIGN_CENTER);
                        celda.setBorder(0);
                        tablatot.addCell(celda);
                        celda = new PdfPCell(new Phrase("" + Utilidades.formatomoneda("" + tote), pdf.font10n));
                        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                        celda.setVerticalAlignment(Element.ALIGN_CENTER);
                        celda.setBorder(0);
                        tablatot.addCell(celda);
                        celda = new PdfPCell(new Phrase("TOTAL TARJETA", pdf.font10n));
                        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                        celda.setVerticalAlignment(Element.ALIGN_CENTER);
                        celda.setBorder(0);
                        tablatot.addCell(celda);
                        celda = new PdfPCell(new Phrase("" + Utilidades.formatomoneda("" + tott), pdf.font10n));
                        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                        celda.setVerticalAlignment(Element.ALIGN_CENTER);
                        celda.setBorder(0);
                        tablatot.addCell(celda);
                        celda = new PdfPCell(new Phrase("\n\n", pdf.font10n));
                        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                        celda.setVerticalAlignment(Element.ALIGN_CENTER);
                        celda.setBorder(0);
                        celda.setColspan(tamt.length);
                        tablatot.addCell(celda);
                        documento.add(tablatot);

                        if (lista_meses.size() == 1) {
                            float[] tam = new float[]{5, 45, 25, 25};
                            PdfPTable tabla = new PdfPTable(tam);
                            tabla.setWidthPercentage(100);
                            celda = new PdfPCell(new Phrase("N°", pdf.font10n));
                            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                            celda.setVerticalAlignment(Element.ALIGN_CENTER);
                            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
                            celda.setBorder(15);
                            tabla.addCell(celda);
                            celda = new PdfPCell(new Phrase("NOMBRE", pdf.font10n));
                            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                            celda.setVerticalAlignment(Element.ALIGN_CENTER);
                            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
                            celda.setBorder(15);
                            tabla.addCell(celda);
                            celda = new PdfPCell(new Phrase("N° FACTURA", pdf.font10n));
                            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                            celda.setVerticalAlignment(Element.ALIGN_CENTER);
                            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
                            celda.setBorder(15);
                            tabla.addCell(celda);
                            celda = new PdfPCell(new Phrase("VALOR PAGADO", pdf.font10n));
                            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                            celda.setVerticalAlignment(Element.ALIGN_CENTER);
                            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
                            celda.setBorder(15);
                            tabla.addCell(celda);
                            int i = 0;
                            for (Map<String, String> lst : lista) {
                                i++;
                                celda = new PdfPCell(new Phrase("" + i, pdf.font10));
                                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                                celda.setBorder(15);
                                tabla.addCell(celda);
                                celda = new PdfPCell(new Phrase("" + lst.get("NOMBRE"), pdf.font10));
                                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                                celda.setBorder(15);
                                tabla.addCell(celda);
                                celda = new PdfPCell(new Phrase("" + lst.get("numero"), pdf.font10));
                                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                                celda.setBorder(15);
                                tabla.addCell(celda);
                                celda = new PdfPCell(new Phrase("" + lst.get("valor_fact"), pdf.font10));
                                celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
                                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                                celda.setBorder(15);
                                tabla.addCell(celda);
                            }

                            documento.add(tabla);
                        }
                    }
                }
            }
            IMPRIMIR("!!!!! SE SUPONE QUE YA LO CREE!!!");
        } catch (Exception ex) {
            ex.printStackTrace();
            IMPRIMIR(" ERROR ----- ERROR-----");
        }
    }

    private void infFactabonoxpac(Document documento, Map<String, String> list) {
        try {
            String cons = "";
            System.out.println("idpac: " + list.get("idpac"));

            cons = "SELECT IFNULL(seg.`pfk_tratamiento`, '')  ID_TRA,tra.`descripcion`  DESC_TRA, pac.`pfk_paciente` ID_PAC,\n"
                    + "CONCAT_WS(' ', per.`primer_nombre`, IFNULL(per.`segundo_nombre`,''), per.`primer_apellido`, IFNULL(per.`segundo_apellido`,'')) PACIENTE,\n"
                    + "DATE_FORMAT(seg.`fecha_seguimiento`, '%Y/%m/%d') FECHA, seg.`observaciones` AS OBSERVACION,seg.`abono` AS ABONO, seg.`saldo` AS SALDO, seg.`usuario` AS USER\n"
                    + "FROM `seguimiento_del_tratamiento` seg\n"
                    + "INNER JOIN `tratamientos` tra ON tra.`pk_tratamiento` = seg.`pfk_tratamiento`\n"
                    + "LEFT JOIN pacientes pac ON pac.`pfk_paciente` = seg.`pfk_paciente`\n"
                    + "LEFT JOIN personas per ON CONCAT (per.`pfk_tipo_documento`, per.`pk_persona`) = pac.`pfk_paciente`\n"
                    + "WHERE pac.`pfk_paciente` = '" + list.get("idpac") + "' \n"
                    + "ORDER BY seg.`consecutivo` ASC";

            cons = "SELECT IFNULL(pxt.fk_tratamiento, '')  ID_TRA,tra.`descripcion`  DESC_TRA, pac.`pfk_paciente` ID_PAC,\n"
                    + "CONCAT_WS(' ', per.`primer_nombre`, IFNULL(per.`segundo_nombre`,''), per.`primer_apellido`, IFNULL(per.`segundo_apellido`,'')) PACIENTE,\n"
                    + "DATE_FORMAT(seg.`fecha_seguimiento`, '%Y/%m/%d') FECHA, seg.`observaciones` AS OBSERVACION,seg.`abono` AS ABONO, seg.`saldo` AS SALDO, seg.`usuario` AS USER\n"
                    + ", IFNULL(pxt.costo, 0) COSTO, IF(pxt.cuota_inicial IS NULL OR pxt.cuota_inicial = '', 0, pxt.cuota_inicial) CUOTA_I\n"
                    + "FROM pacientextratamiento pxt\n"
                    + "LEFT JOIN `seguimiento_del_tratamiento` seg ON seg.pfk_paciente =  pxt.pfk_paciente AND seg.pfk_tratamiento = pxt.fk_tratamiento \n"
                    + "LEFT JOIN `tratamientos` tra ON tra.`pk_tratamiento` =pxt.fk_tratamiento\n"
                    + "INNER JOIN pacientes pac ON pac.`pfk_paciente` = pxt.`pfk_paciente`\n"
                    + "INNER JOIN personas per ON CONCAT (per.`pfk_tipo_documento`, per.`pk_persona`) = pac.`pfk_paciente`\n"
                    + "WHERE pxt.`pfk_paciente` = '" + list.get("idpac") + "' AND pxt.estado = 'Activo'\n"
                    + "ORDER BY seg.`consecutivo` ASC";
            cons = "SELECT IFNULL(CAST(pxt.fk_tratamiento AS UNSIGNED), 0)  ID_TRA,tra.`descripcion`  DESC_TRA, pac.`pfk_paciente` ID_PAC,\n"
                    + "CONCAT_WS(' ', per.`primer_nombre`, IFNULL(per.`segundo_nombre`,''), per.`primer_apellido`, IFNULL(per.`segundo_apellido`,'')) PACIENTE,\n"
                    + "IFNULL(DATE_FORMAT(seg.`fecha_seguimiento`, '%Y/%m/%d'), '') FECHA, IFNULL(seg.`observaciones`, '') AS OBSERVACION,\n"
                    + "IFNULL(seg.`abono`, 0) AS ABONO, IFNULL(seg.`saldo`, 0) AS SALDO, IFNULL(seg.`usuario`, '') AS USER\n"
                    + ", IFNULL(pxt.costo, 0) COSTO, IF(pxt.cuota_inicial IS NULL OR pxt.cuota_inicial = '', 0, pxt.cuota_inicial) CUOTA_I,\n"
                    + "IFNULL(seg.`pfk_tratamiento`, 'NO') SEGUIMIENTO\n"
                    + "FROM pacientextratamiento pxt\n"
                    + "LEFT JOIN `seguimiento_del_tratamiento` seg ON seg.pfk_paciente =  pxt.pfk_paciente AND seg.pfk_tratamiento = pxt.fk_tratamiento \n"
                    + "LEFT JOIN `tratamientos` tra ON tra.`pk_tratamiento` =pxt.fk_tratamiento\n"
                    + "INNER JOIN pacientes pac ON pac.`pfk_paciente` = pxt.`pfk_paciente`\n"
                    + "INNER JOIN personas per ON CONCAT (per.`pfk_tipo_documento`, per.`pk_persona`) = pac.`pfk_paciente`\n"
                    + "WHERE pxt.`pfk_paciente` = '" + list.get("idpac") + "' AND pxt.estado = 'Activo'\n"
                    + "GROUP BY pxt.`fk_tratamiento`, seg.`consecutivo` \n"
                    + "ORDER BY ID_TRA ASC, seg.`consecutivo` ASC";

            List<Map<String, String>> lista_datos = resultquery.ListSQL(cons);
            System.out.println("lista_datos-->" + lista_datos.size());
            List<Map<String, String>> lista_tratamientos = gen.data_list(1, lista_datos, new String[]{"ID_TRA"});
            List<Map<String, String>> lista_datosxtratamientos;
            System.out.println("lista_tratamientos-->" + lista_tratamientos.size());

            if (lista_tratamientos.size() > 0) {
                PdfPCell celda = null;
                float[] tami = new float[100 / 5];
                for (int x = 0; x < tami.length; x++) {
                    tami[x] = 5;

                }

                PdfPTable tabla = new PdfPTable(tami);

                float[] tam = new float[]{5, 11, 43, 18, 23};
                tabla = new PdfPTable(tam);
                int abonot = 0, saldot = 0;

                System.out.println("lista_tratamientos---->" + lista_tratamientos.size());
                for (int i = 0; i < lista_tratamientos.size(); i++) {
                    lista_datosxtratamientos = gen.data_list(3, lista_datos, new String[]{"ID_TRA<->" + lista_tratamientos.get(i).get("ID_TRA")});
                    abonot = getTotalSeguimiento(0, lista_datosxtratamientos);
                    saldot = getTotalSeguimiento(1, lista_datosxtratamientos);
                    tabla = new PdfPTable(tam);
                    tabla.setWidthPercentage(100);
                    celda = new PdfPCell(new Phrase("Identificación: ".toUpperCase(), pdf.font10n));
                    celda.setColspan(2);
                    celda.setBorder(0);
                    tabla.addCell(celda);
                    celda = new PdfPCell(new Phrase(" " + lista_tratamientos.get(i).get("ID_PAC"), pdf.font12));
                    celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                    //celda.setColspan(3);
                    celda.setBorder(0);
                    tabla.addCell(celda);

                    celda = new PdfPCell(new Phrase("Costo Total: ", pdf.font10n));
                    celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    celda.setBorder(0);
                    tabla.addCell(celda);
                    celda = new PdfPCell(new Phrase("" + Utilidades.MascaraMoneda("" + lista_tratamientos.get(i).get("COSTO")), pdf.font10));
                    celda.setBorder(0);
                    tabla.addCell(celda);

                    celda = new PdfPCell(new Phrase("PACIENTE: ", pdf.font10n));
                    celda.setColspan(2);
                    celda.setBorder(0);
                    tabla.addCell(celda);
                    celda = new PdfPCell(new Phrase(" " + lista_tratamientos.get(i).get("PACIENTE"), pdf.font12));
                    celda.setHorizontalAlignment(Element.ALIGN_LEFT);
//                    celda.setColspan(3);
                    celda.setBorder(0);
                    tabla.addCell(celda);

                    celda = new PdfPCell(new Phrase("Abonos: ", pdf.font10n));
                    celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    celda.setBorder(0);
                    tabla.addCell(celda);
                    celda = new PdfPCell(new Phrase("" + Utilidades.MascaraMoneda("" + abonot), pdf.font10));
                    celda.setBorder(0);
                    tabla.addCell(celda);

                    celda = new PdfPCell(new Phrase("TRATAMIENTO: ", pdf.font10n));
                    celda.setColspan(2);
                    celda.setBorder(0);
                    tabla.addCell(celda);
                    celda = new PdfPCell(new Phrase(" " + lista_tratamientos.get(i).get("DESC_TRA"), pdf.font12));
                    celda.setHorizontalAlignment(Element.ALIGN_LEFT);
//                    celda.setColspan(3);
                    celda.setBorder(0);
                    tabla.addCell(celda);

                    celda = new PdfPCell(new Phrase("Cuota Inicial: ", pdf.font10n));
                    celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    celda.setBorder(0);
                    tabla.addCell(celda);
                    String cancelada = "" + (abonot >= Integer.parseInt(lista_tratamientos.get(i).get("CUOTA_I")) ? "(CANCELADA)" : "");
                    celda = new PdfPCell(new Phrase("" + Utilidades.MascaraMoneda("" + lista_tratamientos.get(i).get("CUOTA_I")) + " " + cancelada, pdf.font10));
                    celda.setBorder(0);
                    tabla.addCell(celda);
                    
                    if (!lista_tratamientos.get(i).get("COSTO").equals("0")) {
                        celda = new PdfPCell(new Phrase("", pdf.font10n));
                        celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        celda.setColspan(3);
                        celda.setBorder(0);
                        tabla.addCell(celda);
                        celda = new PdfPCell(new Phrase("Saldo pendiente: ", pdf.font10n));
                        celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        celda.setBorder(0);
                        tabla.addCell(celda);
                        celda = new PdfPCell(new Phrase("" + Utilidades.MascaraMoneda("" + saldot), pdf.font10));
                        celda.setBorder(0);
                        tabla.addCell(celda);
                    } else {
                        celda = new PdfPCell(new Phrase("", pdf.font10n));
                        celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        celda.setColspan(2);
                        celda.setBorder(0);
                        tabla.addCell(celda);
                    }

                    celda = new PdfPCell(new Phrase("\n\n", pdf.font12n));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setColspan(tam.length);
                    celda.setBorder(0);
                    tabla.addCell(celda);
                    celda = new PdfPCell(new Phrase("EVOLUCIÓN DEL TRATAMIENTO", pdf.font12n));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setColspan(tam.length);
                    celda.setBorder(0);
                    tabla.addCell(celda);
                    celda = new PdfPCell(new Phrase("", pdf.font12n));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setColspan(tam.length);
                    celda.setBorder(0);
                    tabla.addCell(celda);

                    if (lista_datosxtratamientos.size() > 0) {

                        celda = new PdfPCell(new Phrase("N°", pdf.font10n));
                        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                        celda.setVerticalAlignment(Element.ALIGN_CENTER);
                        celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        celda.setBorder(15);
                        tabla.addCell(celda);
                        celda = new PdfPCell(new Phrase("FECHA", pdf.font10n));
                        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                        celda.setVerticalAlignment(Element.ALIGN_CENTER);
                        celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        celda.setBorder(15);
                        tabla.addCell(celda);
                        celda = new PdfPCell(new Phrase("TRATAMIENTO", pdf.font10n));
                        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                        celda.setVerticalAlignment(Element.ALIGN_CENTER);
                        celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        celda.setBorder(15);
                        tabla.addCell(celda);
                        celda = new PdfPCell(new Phrase("ABONO", pdf.font10n));
                        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                        celda.setVerticalAlignment(Element.ALIGN_CENTER);
                        celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        celda.setBorder(15);
                        tabla.addCell(celda);
                        celda = new PdfPCell(new Phrase("SALDO", pdf.font10n));
                        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                        celda.setVerticalAlignment(Element.ALIGN_CENTER);
                        celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        celda.setBorder(15);
                        tabla.addCell(celda);
                        if (lista_datosxtratamientos.get(0).get("SEGUIMIENTO").equals("NO")) {
                            celda = new PdfPCell(new Phrase("No se encontraron registros.", pdf.font15n));
                            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                            celda.setVerticalAlignment(Element.ALIGN_CENTER);
                            celda.setBorder(15);
                            celda.setColspan(5);
                            tabla.addCell(celda);

                        } else {
                            for (int j = 0; j < lista_datosxtratamientos.size(); j++) {
                                celda = new PdfPCell(new Phrase("" + (j + 1), pdf.font10));
                                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                                celda.setBorder(15);
                                tabla.addCell(celda);
                                celda = new PdfPCell(new Phrase("" + lista_datosxtratamientos.get(j).get("FECHA"), pdf.font10));
                                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                                celda.setBorder(15);
                                tabla.addCell(celda);
                                celda = new PdfPCell(new Phrase("" + lista_datosxtratamientos.get(j).get("OBSERVACION"), pdf.font10));
                                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                                celda.setBorder(15);
                                tabla.addCell(celda);
                                celda = new PdfPCell(new Phrase("" + Utilidades.formatomoneda(lista_datosxtratamientos.get(j).get("ABONO")), pdf.font10));
                                celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
                                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                                celda.setBorder(15);
                                tabla.addCell(celda);
                                celda = new PdfPCell(new Phrase("" + Utilidades.formatomoneda(lista_datosxtratamientos.get(j).get("SALDO")), pdf.font10));
                                celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
                                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                                celda.setBorder(15);
                                tabla.addCell(celda);
                            }
                        }

                    } else {
                        celda = new PdfPCell(new Phrase("No se encontraron registros.", pdf.font15n));
                        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                        celda.setVerticalAlignment(Element.ALIGN_CENTER);
                        celda.setBorder(15);
                        tabla.addCell(celda);
                    }

                    documento.add(tabla);
                    if (i < lista_tratamientos.size() - 1) {
                        documento.newPage();
                    }
                }

            } else {
                PdfPCell celda = null;

                PdfPTable tabla = new PdfPTable(1);
                tabla.setWidthPercentage(100);

                celda = new PdfPCell(new Phrase("No se encontraron registros.", pdf.font15n));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                celda.setBorder(15);
                tabla.addCell(celda);

                documento.add(tabla);

            }

            IMPRIMIR("!!!!! SE SUPONE QUE YA LO CREE!!!");

        } catch (Exception ex) {
            ex.printStackTrace();
            IMPRIMIR(" ERRORO ERRORO");
        }
    }

    private void pacientAuxi(Document documento, Map<String, String> list) {
        try {
            IMPRIMIR("+++++ ENTRE?");
            String cons = "", add = "";
            if (!list.get("tipo").equals("Todos")) {
                add = "WHERE PAUX.`estado` = '" + list.get("tipo") + "'";
            }
            cons = "SELECT CONCAT_WS(' ', paux.`primer_nombre`, IFNULL(paux.`segundo_nombre`, ''), paux.`primer_apellido`, IFNULL(paux.`segundo_apellido`, '')) NOMBRE, \n"
                    + "REPLACE(paux.`telefono`, '<>', '-') TELEFONO, paux.`email`, paux.`estado` ESTADO\n"
                    + "FROM paciente_auxiliar paux " + add + "\n"
                    + "ORDER BY NOMBRE ASC";
            ArrayList<String[]> resql = resultquery.SELECT(cons);

            PdfPCell celda = null;
            float[] tam = new float[]{5, 40, 20, 20, 15};

            PdfPTable tabla = new PdfPTable(tam);
            tabla.setWidthPercentage(100);
            celda = new PdfPCell(new Phrase("N°", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(15);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("NOMBRE", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(15);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("TELEFONO", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(15);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("EMAIL", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(15);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("ESTADO", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(15);
            tabla.addCell(celda);

            if (resql.size() > 0) {
                for (int i = 0; i < resql.size(); i++) {
                    celda = new PdfPCell(new Phrase("" + (i + 1), pdf.font10));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder(15);
                    tabla.addCell(celda);
                    celda = new PdfPCell(new Phrase("" + resql.get(i)[0], pdf.font10));
                    celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder(15);
                    tabla.addCell(celda);
                    celda = new PdfPCell(new Phrase("" + resql.get(i)[1], pdf.font10));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder(15);
                    tabla.addCell(celda);
                    celda = new PdfPCell(new Phrase("" + resql.get(i)[2].toLowerCase(), pdf.font10));
                    celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder(15);
                    tabla.addCell(celda);
                    celda = new PdfPCell(new Phrase("" + resql.get(i)[3], pdf.font10));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder(15);
                    tabla.addCell(celda);
                }
            } else {
                celda = new PdfPCell(new Phrase("\n No se encontraron registros.\n\n", pdf.font15n));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                celda.setBorder(15);
                celda.setColspan(tam.length);
                tabla.addCell(celda);
            }
            documento.add(tabla);
            IMPRIMIR("!!!!! SE SUPONE QUE YA LO CREE!!!");
        } catch (Exception ex) {
            ex.printStackTrace();
            IMPRIMIR(" ERROR ----- ERROR-----");
        }
    }

    private void pacientActiv(Document documento, Map<String, String> list) {
        try {
            IMPRIMIR("+++++ ENTRE?");
            String consulta = "SELECT IF(paux.pk_paciente_auxiliar IS NULL,\n"
                    + "CONCAT_WS(' ', per.`primer_apellido`, IFNULL(per.`segundo_apellido`, ''), per.`primer_nombre`, IFNULL(per.`segundo_nombre`, '')),                                \n"
                    + "CONCAT_WS(' ', paux.`primer_apellido`, IFNULL(paux.`segundo_apellido`, ''), paux.`primer_nombre`, IFNULL(paux.`segundo_nombre`, ''))) PACIENTE, \n"
                    + "IF(paux.`pk_paciente_auxiliar` IS NULL, pertel.`numero`, paux.`telefono`) TELEFONO, \n"
                    + "IF(paux.`pk_paciente_auxiliar` IS NULL, per.`correo_electronico`, paux.email) EMAIL, \n"
                    + "ultc.fecha_cita FECHA_CITA \n"
                    + "FROM (\n"
                    + "SELECT pfk_paciente, estado, `fecha_cita`\n"
                    + "FROM `citas` cit\n"
                    + "GROUP BY pfk_paciente DESC) ultc \n"
                    + "LEFT JOIN paciente_auxiliar paux ON ultc.pfk_paciente = paux.pk_paciente_auxiliar AND paux.estado = 'Activo'\n"
                    + "LEFT JOIN personas per ON ultc.pfk_paciente = CONCAT (per.pfk_tipo_documento, per.pk_persona)\n"
                    + "LEFT JOIN personas_telefonos pertel ON ultc.pfk_paciente = pertel.`pfk_persona`\n"
                    + "WHERE (ultc.estado = 'Activo' OR ultc.estado = 'Terminado') AND ultc.fecha_cita >= DATE_SUB(SYSDATE(),INTERVAL 3 MONTH)";
            consulta = "SELECT IF(paux.pk_paciente_auxiliar IS NULL,\n"
                    + "CONCAT(TRIM(CONCAT(per.primer_apellido,' ',per.segundo_apellido)),' ',TRIM(CONCAT(per.primer_nombre,' ',per.segundo_nombre))),\n"
                    + "CONCAT(TRIM(CONCAT(paux.primer_apellido,' ',paux.segundo_apellido)),' ',TRIM(CONCAT(paux.primer_nombre,' ',paux.segundo_nombre)))) PACIENTE, \n"
                    + "IFNULL(IF(paux.`pk_paciente_auxiliar` IS NULL, pertel.`numero`, REPLACE(paux.`telefono`, '<>', '-')), '') TELEFONO, \n"
                    + "IFNULL(IF(paux.`pk_paciente_auxiliar` IS NULL, per.`correo_electronico`, paux.email), '') EMAIL, \n"
                    + "ultc.fecha_cita FECHA_CITA \n"
                    + "FROM (\n"
                    + "SELECT pfk_paciente, cit.estado, `fecha_cita`, IFNULL(paux.`estado`, '') EST\n"
                    + "FROM `citas` cit \n"
                    + "LEFT JOIN paciente_auxiliar paux ON cit.pfk_paciente = paux.pk_paciente_auxiliar\n"
                    + "GROUP BY pfk_paciente DESC) ultc \n"
                    + "LEFT JOIN paciente_auxiliar paux ON ultc.pfk_paciente = paux.pk_paciente_auxiliar AND paux.estado = 'Activo'\n"
                    + "LEFT JOIN personas per ON ultc.pfk_paciente = CONCAT (per.pfk_tipo_documento, per.pk_persona)\n"
                    + "LEFT JOIN personas_telefonos pertel ON ultc.pfk_paciente = pertel.`pfk_persona`\n"
                    + "WHERE (ultc.estado = 'Activo' OR ultc.estado = 'Terminado') AND ultc.EST <> 'Inactivo' \n"
                    + "AND ultc.fecha_cita >= DATE_SUB(SYSDATE(),INTERVAL 3 MONTH) \n ORDER BY PACIENTE ASC ";

            ArrayList<String[]> resql = resultquery.SELECT(consulta);

            PdfPCell celda = null;
            float[] tam = new float[]{6, 39, 20, 22, 13};

            PdfPTable tabla = new PdfPTable(tam);
            tabla.setWidthPercentage(100);
            celda = new PdfPCell(new Phrase("N°", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(15);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("NOMBRE", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(15);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("TELEFONO", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(15);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("EMAIL", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(15);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("ULTIMA CITA", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(15);
            tabla.addCell(celda);

            if (resql.size() > 0) {
                for (int i = 0; i < resql.size(); i++) {
                    celda = new PdfPCell(new Phrase("" + (i + 1), pdf.font10));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder(15);
                    tabla.addCell(celda);
                    celda = new PdfPCell(new Phrase("" + Utilidades.CapitaliceTexto(resql.get(i)[0]), pdf.font10));
                    celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder(15);
                    tabla.addCell(celda);
                    celda = new PdfPCell(new Phrase("" + resql.get(i)[1], pdf.font10));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder(15);
                    tabla.addCell(celda);
                    celda = new PdfPCell(new Phrase("" + resql.get(i)[2].toLowerCase(), pdf.font10));
                    celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder(15);
                    tabla.addCell(celda);
                    celda = new PdfPCell(new Phrase("" + resql.get(i)[3], pdf.font10));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder(15);
                    tabla.addCell(celda);
                }
            } else {
                celda = new PdfPCell(new Phrase("\n No se encontraron registros.\n\n", pdf.font15n));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                celda.setBorder(15);
                celda.setColspan(tam.length);
                tabla.addCell(celda);
            }
            documento.add(tabla);
            IMPRIMIR("!!!!! SE SUPONE QUE YA LO CREE!!!");
        } catch (Exception ex) {
            ex.printStackTrace();
            IMPRIMIR(" ERROR ----- ERROR-----");
        }
    }

    private void pacientTermi(Document documento, Map<String, String> list) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void pacientxTermi(Document documento, Map<String, String> list) {
        try {
            IMPRIMIR("+++++ ENTRE?");
            String consulta = "SELECT per.`pk_persona`, CONCAT_WS(' ', per.`primer_nombre`, IFNULL(per.`segundo_nombre`, ''), per.`primer_apellido`, IFNULL(per.`segundo_apellido`, '')) NOMBRE, \n"
                    + "DATE_FORMAT(conpa.fecha,'%Y/%m/%d') fecha, conpa.descripcion\n"
                    + "FROM pacientextratamiento pxt\n"
                    + "INNER JOIN (SELECT tbl.`pk_tratamiento`, tbl.`descripcion`, tbl.`pfk_paciente`, SUM(tbl.`valorxcantidad`) total, tbl.`fecha`\n"
                    + "FROM (\n"
                    + "SELECT tto.`pk_tratamiento`, tto.`descripcion`, fac.`pfk_paciente`, pxc.`valorxcantidad`, pag.`fecha`\n"
                    + "FROM facturas fac\n"
                    + "INNER JOIN pagos pag ON fac.`pfk_paciente` = pag.`pfk_paciente` AND fac.`numero` = pag.`pk_pago`\n"
                    + "INNER JOIN pagosxconceptos pxc ON fac.`pfk_paciente` = pxc.`pfk_paciente` AND fac.`numero` = pxc.`pfk_pago`\n"
                    + "INNER JOIN conceptos con ON pxc.`pfk_concepto` = con.`pk_concepto`\n"
                    + "INNER JOIN tratamientos tto ON con.`fk_tratamiento` = tto.`pk_tratamiento`\n"
                    + "WHERE fac.`estado` = 'pagado' AND tto.`pk_tratamiento` IS NOT NULL\n"
                    + "ORDER BY pag.`fecha` DESC) tbl\n"
                    + "GROUP BY tbl.`pfk_paciente`, tbl.`pk_tratamiento`) conpa ON pxt.`pfk_paciente` = conpa.pfk_paciente \n"
                    + "AND pxt.`fk_tratamiento` = conpa.pk_tratamiento AND pxt.`costo` =  conpa.total\n"
                    + "INNER JOIN personas per ON pxt.`pfk_paciente` = CONCAT (per.`pfk_tipo_documento`, per.`pk_persona`)\n"
                    + "WHERE pxt.`estado` = 'Activo' AND conpa.fecha BETWEEN '" + list.get("fini") + "' AND '" + list.get("ffin") + "'";
            consulta = "SELECT conpa.pfk_paciente, conpa.NOMBRE, \n"
                    + "DATE_FORMAT(conpa.fecha,'%Y/%m/%d') fecha, conpa.descripcion\n"
                    + "FROM pacientextratamiento pxt\n"
                    + "INNER JOIN (\n"
                    + "	SELECT tto.`pk_tratamiento`, tto.`descripcion`, fac.`pfk_paciente`, SUM(pxc.`valorxcantidad`) total, pag.`fecha`,\n"
                    + "	CONCAT_WS(' ', per.`primer_nombre`, IFNULL(per.`segundo_nombre`, ''), per.`primer_apellido`, IFNULL(per.`segundo_apellido`, '')) NOMBRE\n"
                    + "	FROM facturas fac\n"
                    + "	INNER JOIN pagos pag ON fac.`pfk_paciente` = pag.`pfk_paciente` AND fac.`numero` = pag.`pk_pago`\n"
                    + "	INNER JOIN pagosxconceptos pxc ON fac.`pfk_paciente` = pxc.`pfk_paciente` AND fac.`numero` = pxc.`pfk_pago`\n"
                    + "	INNER JOIN conceptos con ON pxc.`pfk_concepto` = con.`pk_concepto`\n"
                    + "	INNER JOIN tratamientos tto ON con.`fk_tratamiento` = tto.`pk_tratamiento`\n"
                    + "	INNER JOIN personas per ON pxc.`pfk_paciente` = CONCAT (per.`pfk_tipo_documento`, per.`pk_persona`)\n"
                    + "	WHERE fac.`estado` = 'pagado' AND tto.`pk_tratamiento` IS NOT NULL\n"
                    + "	GROUP BY fac.`pfk_paciente`, tto.`pk_tratamiento`\n"
                    + "	ORDER BY pag.`fecha` DESC\n"
                    + ") conpa ON pxt.`pfk_paciente` = conpa.pfk_paciente \n"
                    + "AND pxt.`fk_tratamiento` = conpa.pk_tratamiento AND pxt.`costo` =  conpa.total\n"
                    + "WHERE pxt.`estado` = 'Activo' AND conpa.fecha BETWEEN '" + list.get("fini") + "' AND '" + list.get("ffin") + "' "
                    + "ORDER BY  conpa.NOMBRE ASC";

            System.out.println("consulta->" + consulta);

            ArrayList<String[]> resql = resultquery.SELECT(consulta);

            PdfPCell celda = null;
            float[] tam = new float[]{5, 17, 38, 20, 20};

            PdfPTable tabla = new PdfPTable(tam);
            tabla.setWidthPercentage(100);
            celda = new PdfPCell(new Phrase("\n\n", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(0);
            celda.setColspan(tam.length);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase("N°", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(15);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("IDENTIFICACION", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(15);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("NOMBRE", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(15);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("ULTIMA CITA", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(15);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("TRATAMIENTO", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(15);
            tabla.addCell(celda);
            IMPRIMIR(" resultadooooo ----" + resql);
            IMPRIMIR(" resultadooooo TAMMMM " + resql.size());

            if (resql.size() > 0) {
                for (int i = 0; i < resql.size(); i++) {
                    celda = new PdfPCell(new Phrase("" + (i + 1), pdf.font10));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder(15);
                    tabla.addCell(celda);
                    celda = new PdfPCell(new Phrase("" + resql.get(i)[0], pdf.font10));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder(15);
                    tabla.addCell(celda);
                    celda = new PdfPCell(new Phrase("" + resql.get(i)[1], pdf.font10));
                    celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder(15);
                    tabla.addCell(celda);
                    celda = new PdfPCell(new Phrase("" + resql.get(i)[2], pdf.font10));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder(15);
                    tabla.addCell(celda);
                    celda = new PdfPCell(new Phrase("" + resql.get(i)[3], pdf.font10));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder(15);
                    tabla.addCell(celda);
                }
            } else {
                celda = new PdfPCell(new Phrase("\n No se encontraron registros.\n\n", pdf.font15n));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                celda.setBorder(15);
                celda.setColspan(tam.length);
                tabla.addCell(celda);
            }
            documento.add(tabla);
            IMPRIMIR("!!!!! SE SUPONE QUE YA LO CREE!!!");
        } catch (Exception ex) {
            ex.printStackTrace();
            IMPRIMIR(" ERROR ----- ERROR-----");
        }
    }

    private String getEdad(String fecha) {
        String ret = "";
        if (!fecha.equals("")) {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String fechaA = sdf.format(cal.getTime());
            System.out.println("fecha--//-->" + fecha);
            System.out.println("fechaA--//-->" + fechaA);
            String d = fecha.split("/")[0];
            String m = fecha.split("/")[1];
            String a = fecha.split("/")[2];

            String aa = fechaA.split("-")[0];
            String ma = fechaA.split("-")[1];
            String da = fechaA.split("-")[2];

            int edad = Integer.parseInt(aa) - Integer.parseInt(a);

            if (Integer.parseInt(ma) < Integer.parseInt(m)) {
                edad--;
            } else if (Integer.parseInt(ma) == Integer.parseInt(m)) {
                if (Integer.parseInt(da) < Integer.parseInt(d)) {
                    edad--;
                }
            }
            ret = "" + edad;
        }

        return ret;
    }

    private int getTotalSeguimiento(int opc, List<Map<String, String>> lista) {
        int ret = 0;
        try {
            int aux = 0;
            if (opc == 0) {//ABONO
                for (int i = 0; i < lista.size(); i++) {
                    ret += Integer.parseInt(lista.get(i).get("ABONO"));
                }
            } else if (opc == 1) {//SALDO
                for (int i = 0; i < lista.size(); i++) {
                    aux += Integer.parseInt(lista.get(i).get("ABONO"));
                }

                ret = Integer.parseInt(lista.get(0).get("COSTO")) - aux;
                if (ret < 0) {
                    ret = 0;
                }
            }
        } catch (Exception e) {
            System.out.println("");
            e.printStackTrace();
        }
        return ret;
    }

    private PdfPTable getEncabezadoFacturaPorEstado(Map<String, String> list, int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
