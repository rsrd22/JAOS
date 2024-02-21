/*
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Informes;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author Acer
 */
public class Informespdf extends PdfPageEventHelper{
    
    public static final Font font4n = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 4);
    public static final Font font5n = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 5);
    public static final Font font6n = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 6);
    public static final Font font8n = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8);
    public static final Font font9n = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9);
    public static final Font font10n = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
    public static final Font font12n = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
    public static final Font font15n = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 15);
    public static final Font font18n = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
    public static final Font font20n = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20);
    public static final Font font2 = FontFactory.getFont(FontFactory.HELVETICA, 2);
    public static final Font font5 = FontFactory.getFont(FontFactory.HELVETICA, 5);
    public static final Font font4 = FontFactory.getFont(FontFactory.HELVETICA, 4);
    public static final Font font6 = FontFactory.getFont(FontFactory.HELVETICA, 6);
    public static final Font font7 = FontFactory.getFont(FontFactory .HELVETICA, 7);
    public static final Font font8 = FontFactory.getFont(FontFactory .HELVETICA, 8);
    public static final Font font9 = FontFactory.getFont(FontFactory .HELVETICA, 9);
    public static final Font font10 = FontFactory.getFont(FontFactory.HELVETICA, 10);
    public static final Font font12 = FontFactory.getFont(FontFactory.HELVETICA, 12);
    public static final Font font15 = FontFactory.getFont(FontFactory.HELVETICA, 15);
    public static final Font font20 = FontFactory.getFont(FontFactory.HELVETICA, 20);
    public static final Font font4nc = FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE, 4);
    public static final Font font5nc = FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE, 5);
    public static final Font font6nc = FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE, 6);
    public static final Font font8nc = FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE, 8);
//Monotype Corsiva
    public static final Font font4nMC = FontFactory.getFont("Monotype Corsiva", 4, 1);
    public static final Font font6nMC = FontFactory.getFont("Monotype Corsiva", 6, 1);
    public static final Font font8nMC = FontFactory.getFont("Monotype Corsiva", 8, 1);
    public static final Font font10nMC = FontFactory.getFont("Monotype Corsiva", 10, 1);
    public static final Font font12nMC = FontFactory.getFont("Monotype Corsiva", 12, 1);
    public static final Font font15nMC = FontFactory.getFont("Monotype Corsiva", 15, 1);
    public static final Font font20nMC = FontFactory.getFont("Monotype Corsiva", 20, 1);
    public static final Font font2MC = FontFactory.getFont("Monotype Corsiva", 2);
    public static final Font font4MC = FontFactory.getFont("Monotype Corsiva", 4);
    public static final Font font6MC = FontFactory.getFont("Monotype Corsiva", 6);
    public static final Font font8MC = FontFactory.getFont("Monotype Corsiva", 8);
    public static final Font font10MC = FontFactory.getFont("Monotype Corsiva", 10);
    public static final Font font12MC = FontFactory.getFont("Monotype Corsiva", 12);
    public static final Font font15MC = FontFactory.getFont("Monotype Corsiva", 15);
    public static final Font font20MC = FontFactory.getFont("Monotype Corsiva", 20);
    public static final Font prueba = FontFactory.getFont(FontFactory.TIMES, 20); 

    ///
    public static final Font font12nWhite = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
    public static final Font font6White = FontFactory.getFont(FontFactory.HELVETICA, 6, BaseColor.WHITE);
    public static final Font font6Blue = FontFactory.getFont(FontFactory.HELVETICA, 6, BaseColor.BLUE);
    public static final Font font6nBlue = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 6, BaseColor.BLUE);
    public static final Font font8nBlue = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8, BaseColor.BLUE);
    public static final Font font10nBlue = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.BLUE);
    public static final Font font10ncBlue = FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE, 10, BaseColor.BLUE);

        /*.:PROBAR CODIGO:..:PROBAR CODIGO:..:PROBAR CODIGO:..:PROBAR CODIGO:..:PROBAR CODIGO:..:PROBAR CODIGO:..:PROBAR CODIGO:..:PROBAR CODIGO:..:PROBAR CODIGO:..:PROBAR CODIGO:..:PROBAR CODIGO:.*/
    /*.:PROBAR:.*/ private void IMPRIMIR(String funcion, String texto, boolean probar){ if(probar) System.out.println(funcion + " -> " + texto); }
    /*.:FIN PROBAR CODIGO:..:FIN PROBAR CODIGO:..:FIN PROBAR CODIGO:..:FIN PROBAR CODIGO:..:FIN PROBAR CODIGO:..:FIN PROBAR CODIGO:..:FIN PROBAR CODIGO:..:FIN PROBAR CODIGO:..:FIN PROBAR CODIGO:.*/

    public  PdfPTable tablaEncabezado = null;
    public  PdfPTable tablaPie = null;
    public  PdfPTable tablaEncabezadoNivel2 = null;
    public  PdfPTable tablaEncabezadoNivel3 = null;
    //public static PdfPTable tablaPie = null;
    public static String pieUsuario = "";
    

    public  int pagenumber =0;
    public  int con=0;
    String ins="",idtipo="";
//    GenerarReporteFinal gene=new GenerarReporteFinal();
    PdfPTable encabezado =null;
    PdfPTable pie =null;
    Calendar cal =Calendar.getInstance();
    SimpleDateFormat sdfa = new SimpleDateFormat("dd/MMMM/yyyy HH:mm");
       
    public String pieFechaHora = sdfa.format(cal.getTime());
 
    public Informespdf(PdfPTable encabezado) {

        if(con==0){
         if(tablaEncabezado==null)
          tablaEncabezado = null;
        }
        tablaEncabezadoNivel2 = null;
        tablaEncabezadoNivel3 = null;
        pieUsuario = "";
        //pieFechaHora = "";
        this.encabezado=encabezado;
        con++;

        //pagenumber =0;
    }
    
    public Informespdf(PdfPTable encabezado, PdfPTable pie) {

        if(con==0){
         if(tablaEncabezado==null)
          tablaEncabezado = null;
        }
        tablaEncabezadoNivel2 = null;
        tablaEncabezadoNivel3 = null;
        pieUsuario = "";
        //pieFechaHora = "";
        this.encabezado=encabezado;
        this.pie = pie;
        con++;

        //pagenumber =0;
    }
    
    public Informespdf() {

        if(con==0){
         if(tablaEncabezado==null)
          tablaEncabezado = null;
         if(pie==null)
          pie = null;
        }
        tablaEncabezadoNivel2 = null;
        tablaEncabezadoNivel3 = null;
        pieUsuario = "";
        //pieFechaHora = ;
        this.encabezado=encabezado;
        this.pie=pie;
                
        con++;

        //pagenumber =0;
    }
public Font getFont(int tam, boolean negrita){
        if(negrita)
            return FontFactory.getFont(FontFactory.HELVETICA_BOLD, tam);
        else
            return FontFactory.getFont(FontFactory.HELVETICA, tam);
    }

    public void onStartPage(PdfWriter writer, Document document) {
            tablaEncabezado=encabezado;
            try {
                if(tablaEncabezado != null)
                    document.add(tablaEncabezado);
//                if(tablaEncabezadoNivel2 != null)
//                    document.add(tablaEncabezadoNivel2);
//                document.add(new Paragraph(" ", font4) );
//                if(tablaEncabezadoNivel3 != null)
//                    document.add(tablaEncabezadoNivel3);
                
            } catch (Exception z) {
                IMPRIMIR("onStartPage()", "ERROR: "+z, true);
            }
            pagenumber++;
            
    }
    
    public void onEndPagep(PdfWriter writer, Document document) {
        try {
                PdfPTable table = new PdfPTable(3);
                PdfPCell celda = null;

                table.setWidths(new int[]{17, 16, 16});
                table.setTotalWidth(527);
                table.setLockedWidth(true);
                table.getDefaultCell().setFixedHeight(20);
                table.getDefaultCell().setBorder(Rectangle.BOTTOM);

                                     
                celda = new PdfPCell(new Phrase("Pie Usuaio", font10));
                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celda.setBorder(15);
                celda.setColspan(2);
                table.addCell(celda);
                //table.addCell(pieUsuario);
                //table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);

                //table.addCell();

                celda = new PdfPCell(new Phrase("PieFecha", font4));
                celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celda.setBorder(15);
                table.addCell(celda);

                celda = new PdfPCell(new Phrase(String.format("PÃ¡gina %d", pagenumber), font4));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celda.setBorder(15);
                table.addCell(celda);
                
                
   
                table.writeSelectedRows(0, -1, 34, 14, writer.getDirectContent());
           

                /*table.setWidths(new int[]{24, 24, 2});
                table.setTotalWidth(527);
                table.setLockedWidth(true);
                table.getDefaultCell().setFixedHeight(20);
                table.getDefaultCell().setBorder(Rectangle.BOTTOM);

                table.addCell(pieUsuario);
                table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);

                table.addCell(String.format("PÃ¡gina %d ", writer.getPageNumber()));

                PdfPCell celda = new PdfPCell(new Phrase(pieFechaHora, font4));
                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celda.setBorder(0);
                table.addCell(celda);
                table.writeSelectedRows(0, -1, 34, 40, writer.getDirectContent());*/


        } catch (DocumentException de) {
            IMPRIMIR("onEndPage()", "ERROR: " + de, true);
            throw new ExceptionConverter(de);
        }
    }

    public void onEndPage(PdfWriter writer, Document document) {
        try {
                PdfPTable table = new PdfPTable(3);
                table.setWidthPercentage(90);
                PdfPCell celda = null;

                if(pie != null){
                    celda = new PdfPCell(pie);
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    celda.setBorder(0);
                    celda.setColspan(3);
                    table.addCell(celda);
                }

                table.setWidths(new int[]{33, 12, 4});
                table.setTotalWidth(540);
                table.setLockedWidth(true);
                table.getDefaultCell().setFixedHeight(20);
                table.getDefaultCell().setBorder(Rectangle.BOTTOM);


                celda = new PdfPCell(new Phrase("PIEN USUARIO", font10));
                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celda.setBorder(15);  
                celda.setColspan(2);  
                //table.addCell(celda);
                //table.addCell(pieUsuario);
                //table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
                
                String msj = "Centro Medico Perla del Caribe, Calle 22 No. 14 - 70 Cons. 5 - Tel: 431 81 95 - Cel: 300 202 9614 - Santa Marta, D.T.C.H.";
                celda = new PdfPCell(new Phrase(""+msj, font6));
                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celda.setBorder(0);
               // celda.setColspan(2);
                table.addCell(celda);
                System.out.println("fecha --->"+pieFechaHora);
                
                celda = new PdfPCell(new Phrase(pieFechaHora, font6));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celda.setBorder(0);
                table.addCell(celda);
                
                celda = new PdfPCell(new Phrase(String.format("Página %d ", pagenumber), font6));
                celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celda.setBorder(0);
                table.addCell(celda);
                
                
                
//                
                
//                    table.writeSelectedRows(0, -1, 154, 14, writer.getDirectContent());
                
                table.writeSelectedRows(0, -1, 30, 20, writer.getDirectContent());

              

        } catch (DocumentException de) {
            IMPRIMIR("onEndPage()", "ERROR: " + de, true);
            throw new ExceptionConverter(de);
        }
    }
    
}
