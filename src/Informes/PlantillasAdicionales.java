/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Informes;

import BaseDeDatos.gestorMySQL;
import Utilidades.Parametros;
import Utilidades.Utilidades;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 *
 * @author Asus
 */
public class PlantillasAdicionales {

    private gestorMySQL resultquery = new gestorMySQL();
    String path = "";
    InformesPlantillapdf pdf = new InformesPlantillapdf();

    public String Encode() {
        String cifrado = "" + System.currentTimeMillis();
        return cifrado;
    }

    public String Generarinformes(Map<String, String> list) {
        try {
            System.out.println("GENERAR INFORMES");
            int tipo = Integer.parseInt(list.get("tipo"));
            String ret = "";
            String encode = Encode();

            Rectangle papel = null;
            String Titulo = "";
            String entidad = "", pac = "";
            if (tipo == 0 || tipo == 10) {
                Titulo = "Cotizaciones";
            }else if(tipo == 1) {
                Titulo = "Formulación";
            }else if(tipo == 2) {
                Titulo = "Remisiones";
            }

            String ruta = Parametros.dirInformes + Titulo + encode + ".pdf";
            System.out.println("ruta-->"+ruta);
            System.out.println("tipo-->"+tipo);
            FileOutputStream archivo = new FileOutputStream(ruta);

            //PdfPTable Encabezado = getEncabezadoF(Titulo);
            //PdfPTable piepagina = getPie();
            InformesPlantillapdf pdf = new InformesPlantillapdf();

            //if(hoja.equals("Carta")){
            System.out.println("rect---" + (new Rectangle(396.0f, 612.0f)));
            System.out.println("pag---" + PageSize.LETTER);
            System.out.println("b5---" + PageSize.B5);
            System.out.println("a5---" + PageSize.A5);
            System.out.println("b7---" + PageSize.B7);
            System.out.println("a7---" + PageSize.A7);
            //papel = PageSize.B7;
            papel = new Rectangle(600.0f, 480.0f);  // A UTILIZAR
            //papel = PageSize.A5;
            //papel =  new Rectangle(210.0f, 350.0f);
            //papel = papel.rotate();
            //        }else{
            //            papel = PageSize.LEGAL;
            //        }
            Document documento = new Document(papel);
            PdfWriter writer = PdfWriter.getInstance(documento, archivo);
            writer.setPageEvent(new InformesPlantillapdf());

            //documento.setMargins(R, L, T, B);
            documento.setMargins(20, 20, 20, 35);
            //        if(!hoja.equals("Carta"))
            documento.open();

            if (tipo == 0) {
                PlantillaCotizaciones(documento, list);
            } else if (tipo == 1) {
                PlantillaFormulacion(documento, list);
            } else if (tipo == 2) {
                PlantillaRemision(documento, list);
            } else {
                Plantilla(documento, list);
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
    
    public String GenerarinformesAux(Map<String, String> list, ArrayList<String[]> tbl_cotizacion) {
        try {
            System.out.println("GENERAR INFORMES");
            int tipo = Integer.parseInt(list.get("tipo"));
            String ret = "";
            String encode = Encode();

            Rectangle papel = null;
            String Titulo = "";
            String entidad = "", pac = "";
            if (tipo == 10) {
                Titulo = "Cotizaciones";
            }else if(tipo == 1) {
                Titulo = "Formulación";
            }else if(tipo == 2) {
                Titulo = "Remisiones";
            }

            String ruta = Parametros.dirInformes + Titulo + encode + ".pdf";
            System.out.println("ruta-->"+ruta);
            System.out.println("tipo-->"+tipo);
            FileOutputStream archivo = new FileOutputStream(ruta);

            //PdfPTable Encabezado = getEncabezadoF(Titulo);
            //PdfPTable piepagina = getPie();
            InformesPlantillapdf pdf = new InformesPlantillapdf();

            //if(hoja.equals("Carta")){
            System.out.println("rect---" + (new Rectangle(396.0f, 612.0f)));
            System.out.println("pag---" + PageSize.LETTER);
            System.out.println("b5---" + PageSize.B5);
            System.out.println("a5---" + PageSize.A5);
            System.out.println("b7---" + PageSize.B7);
            System.out.println("a7---" + PageSize.A7);
            //papel = PageSize.B7;
            papel = new Rectangle(600.0f, 480.0f);  // A UTILIZAR
            //papel = PageSize.A5;
            //papel =  new Rectangle(210.0f, 350.0f);
            //papel = papel.rotate();
            //        }else{
            //            papel = PageSize.LEGAL;
            //        }
            Document documento = new Document(papel);
            PdfWriter writer = PdfWriter.getInstance(documento, archivo);
            writer.setPageEvent(new InformesPlantillapdf());

            //documento.setMargins(R, L, T, B);
            documento.setMargins(20, 20, 20, 35);
            //        if(!hoja.equals("Carta"))
            documento.open();

            if (tipo == 10) {
                PlantillaCotizacionesAux(documento, list, tbl_cotizacion);
            } else {
                Plantilla(documento, list);
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

    private void PlantillaCotizaciones(Document documento, Map<String, String> list) {
        try {
            String consulta = "";

            PdfPCell celda = null;
            float[] tam = new float[]{45, 12, 43};

            PdfPTable tabla = new PdfPTable(tam);
            tabla.setWidthPercentage(100);
            celda = new PdfPCell(new Phrase("", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            //celda.setFixedHeight(90);  
            celda.setRowspan(4);
            celda.setBorder(0);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase("", pdf.font10n));
            celda.setRowspan(4);
            celda.setBorder(0);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase("", pdf.font10n));
            celda.setBorder(0);
            celda.setFixedHeight(25);
            tabla.addCell(celda);

            String fecha = resultquery.SELECT("SELECT DATE_FORMAT(NOW(), '%d/%m/%Y')").get(0)[0];
            celda = new PdfPCell(new Phrase("" + fecha, pdf.font10nt));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_BOTTOM);
            celda.setBorder(0);
            celda.setFixedHeight(18);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase("   " + list.get("nombre"), pdf.font10nt));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_BOTTOM);
            celda.setBorder(0);
            celda.setFixedHeight(23);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("", pdf.font10n));
            celda.setBorder(0);
            celda.setFixedHeight(19);
            tabla.addCell(celda);

            documento.add(tabla);
            String[] listanames = new String[]{"descripcion", "costo", "cuota_inicial", "cuota", "cuo", "diferidas_en"};
            consulta = "SELECT ctz.`pfk_tratamiento`, trat.`descripcion`, ctz.`costo`, ctz.`cuota_inicial`,\n"
                    + "ctz.diferidas_en, (ctz.`cuota_inicial` / ctz.diferidas_en) cuo, ctz.`cuota`, ctz.`pk_consecutivo`  \n"
                    + "FROM `pacientexcotizaciones` ctz \n"
                    + "INNER JOIN `tratamientos` trat ON trat.`pk_tratamiento` = ctz.`pfk_tratamiento`\n"
                    + "WHERE ctz.`pfk_paciente` = '" + list.get("id") + "'";

            List<Map<String, String>> lista = resultquery.ListSQL(consulta);

            int tamlist = lista.size();
            tabla = new PdfPTable(tamlist);
            tabla.setWidthPercentage(100);

            celda = new PdfPCell(new Phrase("ORTODONCIA TERMICA\nTÉCNICA ROTH", pdf.font14nt));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celda.setBorder(0);
            celda.setFixedHeight(70);//56
            celda.setColspan(tamlist);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase("", pdf.font12n));
            celda.setFixedHeight(28);
            celda.setBorder(0);
            celda.setColspan(tamlist);
            tabla.addCell(celda);

            String dato = "", add = "", add2 = "", add3 = "";
            for (int i = 0; i < 4; i++) {
                for (int x = 0; x < tamlist; x++) {
                    dato = lista.get(x).get(listanames[i]);
                    if (i == 0) {
                        add = "Tratamiento: ";
                        add2 = "";
                        add3 = "";
                    } else if (i == 1) {
                        add = "Costo Total: ";
                        add2 = "";
                        add3 = "";
                        dato = Utilidades.MascaraMoneda(dato);
                    } else if (i == 2) {
                        add = "Cuota Inicial: ";
                        add2 = " en " + lista.get(x).get(listanames[5]);
                        dato = Utilidades.MascaraMoneda(dato);
                        add3 = (Integer.parseInt(lista.get(x).get(listanames[5])) > 1 ? " Cuotas" : "Cuota");
                    } else if (i == 3) {
                        add = "Cuota: ";
                        add2 = "";
                        add3 = "";
                        dato = Utilidades.MascaraMoneda(dato);
                    }
                    celda = new PdfPCell(new Phrase(add + "" + dato + " " + add2 + " " + add3, pdf.font12nt));
                    celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder(0);
                    tabla.addCell(celda);
                }
            }
            
            celda = new PdfPCell(new Phrase("", pdf.font12n));
            celda.setFixedHeight(40);
            celda.setBorder(0);
            celda.setColspan(tamlist);
            tabla.addCell(celda);
            
            celda = new PdfPCell(new Phrase("NO INCLUYE APARATOLOGIA ADICIONAL", pdf.font16nt));
            celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setColspan(tamlist);
            celda.setBorder(0);
            tabla.addCell(celda);

            documento.add(tabla);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void Plantilla(Document documento, Map<String, String> list) {
        try {
            String consulta = "";

            PdfPCell celda = null;
            float[] tam = new float[100 / 5];
            for (int i = 0; i < tam.length; i++) {
                tam[i] = 5.0f;
            }

            PdfPTable tabla = new PdfPTable(tam);
            tabla.setWidthPercentage(100);
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < tam.length; j++) {
                    celda = new PdfPCell(new Phrase("", pdf.font14n));
                    celda.setBorder(15);
                    celda.setFixedHeight(28.0f);
                    tabla.addCell(celda);
                }
            }

            documento.add(tabla);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void PlantillaFormulacion(Document documento, Map<String, String> list) {
        try {
            String consulta = "";

            PdfPCell celda = null;
            float[] tam = new float[]{45, 12, 43};

            PdfPTable tabla = new PdfPTable(tam);
            tabla.setWidthPercentage(100);
            celda = new PdfPCell(new Phrase("", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            //celda.setFixedHeight(90);  
            celda.setRowspan(4);
            celda.setBorder(0);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase("", pdf.font10n));
            celda.setRowspan(4);
            celda.setBorder(0);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase("", pdf.font10n));
            celda.setBorder(0);
            celda.setFixedHeight(25);
            tabla.addCell(celda);

            String fecha = resultquery.SELECT("SELECT DATE_FORMAT(NOW(), '%d/%m/%Y')").get(0)[0];
            celda = new PdfPCell(new Phrase("" + fecha, pdf.font10nt));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_BOTTOM);
            celda.setBorder(0);
            celda.setFixedHeight(18);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase("   " + list.get("nombre"), pdf.font10nt));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_BOTTOM);
            celda.setBorder(0);
            celda.setFixedHeight(23);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("", pdf.font10n));
            celda.setBorder(0);
            celda.setFixedHeight(19);
            tabla.addCell(celda);

            documento.add(tabla);

            int tamlist = 1;
            tabla = new PdfPTable(tamlist);
            tabla.setWidthPercentage(100);

            celda = new PdfPCell(new Phrase("FORMULACIÓN", pdf.font14nt));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celda.setBorder(0);
            celda.setFixedHeight(70);//56
            celda.setColspan(tamlist);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase("", pdf.font12n));
            celda.setFixedHeight(28);
            celda.setBorder(0);
            celda.setColspan(tamlist);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase(list.get("descripcion"), pdf.font12nt));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setSpaceCharRatio(10);
            celda.setBorder(0);
            tabla.addCell(celda);

            documento.add(tabla);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void PlantillaRemision(Document documento, Map<String, String> list) {
        try {
            String consulta = "";

            PdfPCell celda = null;
            float[] tam = new float[]{45, 15, 40};

            PdfPTable tabla = new PdfPTable(tam);
            tabla.setWidthPercentage(100);
            celda = new PdfPCell(new Phrase("", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            //celda.setFixedHeight(90);  
            celda.setRowspan(4);
            celda.setBorder(0);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase("", pdf.font10n));
            celda.setRowspan(4);
            celda.setBorder(0);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase("", pdf.font10n));
            celda.setBorder(0);
            celda.setFixedHeight(25);
            tabla.addCell(celda);

            String fecha = resultquery.SELECT("SELECT DATE_FORMAT(NOW(), '%d/%m/%Y')").get(0)[0];
            celda = new PdfPCell(new Phrase("" + fecha, pdf.font10nt));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_BOTTOM);
            celda.setBorder(0);
            celda.setFixedHeight(18);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase("   " + list.get("nombre"), pdf.font10nt));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_BOTTOM);
            celda.setBorder(0);
            celda.setFixedHeight(23);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("", pdf.font10n));
            celda.setBorder(0);
            celda.setFixedHeight(19);
            tabla.addCell(celda);

            documento.add(tabla);

            int tamlist = 1;
            tabla = new PdfPTable(tamlist);
            tabla.setWidthPercentage(100);

            celda = new PdfPCell(new Phrase("ESTIMADO DR(A)", pdf.font14nt));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celda.setBorder(0);
            celda.setFixedHeight(70);//56
            celda.setColspan(tamlist);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase("", pdf.font12n));
            celda.setFixedHeight(28);
            celda.setBorder(0);
            celda.setColspan(tamlist);
            tabla.addCell(celda);

            Paragraph pa = new Paragraph(list.get("descripcion"), pdf.font12nt);
            //pa.setMultipliedLeading(30);
            
            pa.setLeading(1f, 1230f);
            celda = new PdfPCell(pa);
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
           
            celda.setBorder(0);
            tabla.addCell(celda);

            documento.add(tabla);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void PlantillaCotizacionesAux(Document documento, Map<String, String> list, ArrayList<String[]> tbl_cotizacion) {
        try {
            String consulta = "";

            PdfPCell celda = null;
            float[] tam = new float[]{45, 15, 40};

            PdfPTable tabla = new PdfPTable(tam);
            tabla.setWidthPercentage(100);
            celda = new PdfPCell(new Phrase("", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            //celda.setFixedHeight(90);  
            celda.setRowspan(4);
            celda.setBorder(0);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase("", pdf.font10n));
            celda.setRowspan(4);
            celda.setBorder(0);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase("", pdf.font10n));
            celda.setBorder(0);
            celda.setFixedHeight(25);
            tabla.addCell(celda);

            String fecha = resultquery.SELECT("SELECT DATE_FORMAT(NOW(), '%d/%m/%Y')").get(0)[0];
            celda = new PdfPCell(new Phrase("" + fecha, pdf.font10nt));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_BOTTOM);
            celda.setBorder(0);
            celda.setFixedHeight(18);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase("   " + list.get("nombre"), pdf.font10nt));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_BOTTOM);
            celda.setBorder(0);
            celda.setFixedHeight(23);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("", pdf.font10n));
            celda.setBorder(0);
            celda.setFixedHeight(19);
            tabla.addCell(celda);

            documento.add(tabla);
            String[] listanames = new String[]{"descripcion", "costo", "cuota_inicial", "cuota", "cuo", "diferidas_en"};
            
            ///tbl_cotizacion
            /// 0-- Tratamiento
            /// 1-- Costo Total
            /// 2-- Cuota Inicial
            /// 3-- Diferidas en 
            /// 4-- Cuota
            
            int tamlist = tbl_cotizacion.size();
            tabla = new PdfPTable(tamlist);
            tabla.setWidthPercentage(100);

            celda = new PdfPCell(new Phrase("ORTODONCIA TERMICA\nTÉCNICA ROTH", pdf.font14nt));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celda.setBorder(0);
            celda.setFixedHeight(70);//56
            celda.setColspan(tamlist);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase("", pdf.font12n));
            celda.setFixedHeight(28);
            celda.setBorder(0);
            celda.setColspan(tamlist);
            tabla.addCell(celda);
            

            String dato = "", add = "", add2 = "", add3 = "";
            for (int i = 0; i < 4; i++) {
                for (int x = 0; x < tamlist; x++) {
                    dato = tbl_cotizacion.get(x)[i];
                    System.out.println("dato-->"+dato);
                    if (i == 0) {
                        add = "Tratamiento: ";
                        add2 = "";
                        add3 = "";
                    } else if (i == 1) {
                        add = "Costo Total: ";
                        add2 = "";
                        add3 = "";
                        //dato = Utilidades.MascaraMoneda(dato.replaceAll(".", ""));
                    } else if (i == 2) {
                        add = "Cuota Inicial: ";
                        add2 = " en " + tbl_cotizacion.get(x)[4];
                        //dato = Utilidades.MascaraMoneda(dato.replaceAll(".", ""));
                        add3 = (Integer.parseInt(tbl_cotizacion.get(x)[4]) > 1 ? " Cuotas" : "Cuota");
                    } else if (i == 3) {
                        add = "Cuota: ";
                        add2 = "";
                        add3 = "";
                        //dato = Utilidades.MascaraMoneda(dato.replaceAll(".", ""));
                    }
                    celda = new PdfPCell(new Phrase(add + "" + dato + " " + add2 + " " + add3, pdf.font12nt));
                    celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                    celda.setVerticalAlignment(Element.ALIGN_CENTER);
                    celda.setBorder(0);
                    tabla.addCell(celda);
                }
            }
            
            celda = new PdfPCell(new Phrase("", pdf.font12n));
            celda.setFixedHeight(40);
            celda.setBorder(0);
            celda.setColspan(tamlist);
            tabla.addCell(celda);
            
            celda = new PdfPCell(new Phrase("NO INCLUYE APARATOLOGIA ADICIONAL", pdf.font16nt));
            celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setColspan(tamlist);
            celda.setBorder(0);
            tabla.addCell(celda);

            documento.add(tabla);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
}
