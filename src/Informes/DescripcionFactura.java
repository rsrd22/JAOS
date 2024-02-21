/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Informes;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import BaseDeDatos.gestorMySQL;
import Control.ControlGeneral;
import Utilidades.Expresiones;
import Utilidades.Parametros;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import javax.swing.JOptionPane;
import Vistas.ventanaConsultarF;
import Utilidades.Utilidades;
import Utilidades.datosUsuario;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class DescripcionFactura {

    private gestorMySQL resultquery = new gestorMySQL();
    InformespdfFactura pdf = new InformespdfFactura();
    ControlGeneral gen = new ControlGeneral();
    public ventanaConsultarF vconsf;
    public Document documento;
    public Utilidades utl = new Utilidades();

    private void IMPRIMIR(String texto) {
        if (true) {
            System.out.println(texto);
        }
    }

    public String Encode() {
        String cifrado = "" + System.currentTimeMillis();
        return cifrado;
    }

    public String GenerarFactura(ventanaConsultarF vconsf, int VLORPAG, int cambio, String NumFac) {

        try {
            IMPRIMIR(" ####### &&&& GENERAR INFROME (presente) &&&& ######### ");
            String ret = "";
            String ruta = Parametros.dirFacturas + NumFac + ".pdf";
            FileOutputStream archivo = new FileOutputStream(ruta);
            Rectangle papel =  new Rectangle(210.0f, 792.0f);
          
//            PdfPTable inicio = getEncabezadoF(documento,NumFac);

            IMPRIMIR("por aquiii");
            documento = new Document(papel);
            PdfWriter writer = PdfWriter.getInstance(documento, archivo);
            writer.setPageEvent(new InformespdfFactura());

           //documento.setMargins(R, L, T, B);
            documento.setMargins(20, 20, 10, 35);
            documento.open();
            IMPRIMIR(" entro al  encabezado");
               
            GenerarPdf( vconsf, VLORPAG, cambio, NumFac);
            
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

    public void GenerarPdf(  ventanaConsultarF vconsf, int VLORPAG, int cambio, String NumFac) {
    
        try {

            ArrayList<String[]> dato = new ArrayList<>();
            String con2 ="SELECT DATE_FORMAT(`fecha`, '%d/%m/%Y') Fecha, DATE_FORMAT(`fecha`,'%h:%i:%s %p') Hora FROM `facturas` WHERE `numero`="+NumFac+"";
            dato = resultquery.SELECT(con2);        
            System.out.println("dato....>"+dato.size());
            IMPRIMIR("***** ***** ***** ***** ENCABEZADO PARA INFORMES DE INSTITUCION $$$$$$$$***** ***** "+dato.get(0)[0]+dato.get(0)[1]+"***** ***** ");
         
            
            float[] tam = new float[]{40,60};
            PdfPTable tabla = new PdfPTable(tam);
            tabla.setWidthPercentage(100);
            
    //        Image m1 = Image.getInstance("D:/Recursos/LOGO75.png");
            
            Image m1 = Image.getInstance("Z:/Recursos/img/iconos/LOGO75.png");
            float m1w, m1h, m2w, m2h;
            m1w = m1h = m2w = m2h = 75;
            
            String ret = " ";
            PdfPCell celda = null;
            
                       
            if (m1 != null) {
                m1.setAlignment(Image.MIDDLE);
                m1.scaleAbsolute(m1w, m1h);
                celda = new PdfPCell(m1);
                celda.setFixedHeight(m1h + 5);
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celda.setBorder(0);
                celda.setRowspan(2);
                celda.setColspan(2);
                tabla.addCell(celda);
                
            } else {
                celda = new PdfPCell(new Phrase("", pdf.font12n));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celda.setBorder(0);
                celda.setRowspan(2);
                celda.setColspan(2);
                tabla.addCell(celda);
            }
            
        
            celda = new PdfPCell(new Phrase("", pdf.font10n));
            celda.setColspan(2);
            celda.setBorder(0);
            tabla.addCell(celda);
                       
            celda = new PdfPCell(new Phrase(("JOSE ARRIETA"), pdf.font12n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celda.setColspan(2);
            celda.setBorder(0);
            tabla.addCell(celda);

            
            celda = new PdfPCell(new Phrase("Ortodoncia", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celda.setColspan(2);
            celda.setBorder(0);
            tabla.addCell(celda);
            
            celda = new PdfPCell(new Phrase(("Odontólogo Pontificia Universidad Javeriana"), pdf.font10));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celda.setColspan(2);
            celda.setBorder(0);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase(("Ortodoncista U.C.C."), pdf.font10));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celda.setBorder(0);
            celda.setColspan(2);
            tabla.addCell(celda);
            
            celda = new PdfPCell(new Phrase("", pdf.font10n));
            celda.setColspan(2);
            celda.setBorder(0);
            tabla.addCell(celda);
            
            celda = new PdfPCell(new Phrase(("NIT: 79601299-1"), pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celda.setBorder(0);
            celda.setColspan(2);
            tabla.addCell(celda);
            
            celda = new PdfPCell(new Phrase("", pdf.font10n));
            celda.setColspan(2);
            celda.setBorder(0);
            tabla.addCell(celda);
 
//            Calendar cal = Calendar.getInstance();
//            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            
            celda = new PdfPCell(new Phrase("RECIBO # "+NumFac+" ", pdf.font10n));
            celda.setHorizontalAlignment(Element. ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setColspan(2);
            celda.setBorder(0);
            tabla.addCell(celda);
           
            celda = new PdfPCell(new Phrase("", pdf.font10n));
            celda.setColspan(2);        
            celda.setBorder(0);
            tabla.addCell(celda);
          

            celda = new PdfPCell(new Phrase("Fecha: "+dato.get(0)[0], pdf.font10n));
            celda.setHorizontalAlignment(Element. ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_RIGHT);
            celda.setBorder(0);
            celda.setColspan(2); 
            tabla.addCell(celda);
            
            celda = new PdfPCell(new Phrase("", pdf.font10n));
            celda.setBorder(0);
            celda.setColspan(2);
            tabla.addCell(celda);
            
          
                        
            celda = new PdfPCell(new Phrase("============================", pdf.font10n));
            celda.setColspan(2);
            celda.setBorder(0);
            tabla.addCell(celda);
      

            documento.add(tabla);   
            IMPRIMIR("&&&&&&  HASTA AQUI EL ENCABEZADO  &&&&&&");
            
            IMPRIMIR("+++++ ENTRE?");
     
            float[] tamp = new float[]{30f,70f};

            PdfPTable tabla1 = new PdfPTable(tamp);
            tabla1.setWidthPercentage(100);

            celda = new PdfPCell(new Phrase("PACIENTE:", pdf.font6n));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_TOP);
            celda.setBorder(0);
            tabla1.addCell(celda);
            
            celda = new PdfPCell(new Phrase((vconsf.txtnombre.getText() + " " + vconsf.txtapellidos.getText()).toUpperCase(), pdf.font6));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_TOP);
            celda.setBorder(0);
            tabla1.addCell(celda);
            
            
           
            if(!vconsf.d.equals("O")){
                celda = new PdfPCell(new Phrase("DOCUMENTO DE IDENTIDAD:", pdf.font6n));
                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                celda.setVerticalAlignment(Element.ALIGN_TOP);
                celda.setBorder(0);
                tabla1.addCell(celda);
                
                celda = new PdfPCell(new Phrase((vconsf.combotipoDoc.getSelectedItem() + vconsf.txtdocumento.getText()), pdf.font6));
                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                celda.setVerticalAlignment(Element.ALIGN_TOP);
                celda.setBorder(0);
                tabla1.addCell(celda);
            }
            
            

            celda = new PdfPCell(new Phrase("DIRECCIÓN:", pdf.font6n));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_TOP);
            celda.setBorder(0);
            tabla1.addCell(celda);
            
            celda = new PdfPCell(new Phrase(vconsf.lblDireccion.getText(), pdf.font6));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_TOP);
            celda.setBorder(0);
            tabla1.addCell(celda);
            
            celda = new PdfPCell(new Phrase("TELEFONO:", pdf.font6n));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_TOP);
            celda.setBorder(0);
            tabla1.addCell(celda);

            celda = new PdfPCell(new Phrase(vconsf.lblTelefono.getText(), pdf.font6));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_TOP);
            celda.setBorder(0);
            tabla1.addCell(celda);
            
            celda = new PdfPCell(new Phrase("============================", pdf.font10n));
            celda.setColspan(2);
            celda.setBorder(0); 
            tabla1.addCell(celda);
           

            celda = new PdfPCell(new Phrase("", pdf.font8));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_TOP);
            celda.setBorder(0);
            celda.setColspan(tamp.length);
            tabla1.addCell(celda);

            documento.add(tabla1);

            float[] tam2 = new float[]{4,25,12,12 /*10*/};

            PdfPTable tabla2 = new PdfPTable(tam2);
            tabla2.setWidthPercentage(100);

            celda = new PdfPCell(new Phrase("C.".toUpperCase(), pdf.font6n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
//            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(0);
            tabla2.addCell(celda);

            celda = new PdfPCell(new Phrase("DESC.".toUpperCase(), pdf.font6n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER );
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
//            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(0);
            tabla2.addCell(celda);

            celda = new PdfPCell(new Phrase("V.UNIT".toUpperCase(), pdf.font6n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
//            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(0);
            tabla2.addCell(celda);
            celda = new PdfPCell(new Phrase("TOTAL".toUpperCase(), pdf.font6n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
//            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(0);
            tabla2.addCell(celda);
         

            int filas = vconsf.tblConcepto.getRowCount();

            for (int i = 0; i < filas; i++) {
                
                celda = new PdfPCell(new Phrase("" + vconsf.tblConcepto.getValueAt(i, 2).toString(), pdf.font7));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                celda.setBorder(0);
                tabla2.addCell(celda);
                celda = new PdfPCell(new Phrase("" + vconsf.tblConcepto.getValueAt(i, 1).toString(), pdf.font7));
                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                celda.setBorder(0);
                tabla2.addCell(celda);
                                        
                celda = new PdfPCell(new Phrase("" + utl.formatomoneda(vconsf.tblConcepto.getValueAt(i, 3).toString().replace(".", "")), pdf.font7));
                celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                celda.setBorder(0);
                tabla2.addCell(celda);
                celda = new PdfPCell(new Phrase("" + utl.formatomoneda(vconsf.tblConcepto.getValueAt(i, 4).toString().replace(".", "")), pdf.font7));
                celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                celda.setBorder(0);
                tabla2.addCell(celda);
                
            }
            
       
            
            celda = new PdfPCell(new Phrase("============================", pdf.font10n));
            celda.setHorizontalAlignment(Element. ALIGN_RIGHT);
            celda.setVerticalAlignment(Element.ALIGN_TOP);
            celda.setBorder(0);
            celda.setColspan(4);
            tabla2.addCell(celda);
           
        
//     
            documento.add(tabla2);

//            PdfPTable tablaFormadepago = null;
//
//            int col = 3;
//            //++++++++++++++++++++ LOGO ++++++++++++++++++++++++++++++++++++
//            tablaFormadepago = new PdfPTable(col);
            float[] tm = new float[]{50};
            
            PdfPTable tabla3 = new PdfPTable(tm);
            tabla3.setWidthPercentage(100);
            String forma = "";
            if (vconsf.rbEfectivo.isSelected()) {
                forma = "EFECTIVO";
            }
            if (vconsf.rbTarjeta.isSelected()) {
                forma = "TARJETA";
            }
            if (vconsf.rbTaryefec.isSelected()) {
                forma = "TARJETA Y EFECTIVO";
            }

            celda = new PdfPCell(new Phrase("FORMA DE PAGO: " +forma, pdf.font6n));
//            celda.setFixedHeight(15);
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celda.setBorder(0);
            tabla3.addCell(celda);

            celda = new PdfPCell(new Phrase("\n", pdf.font8));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_TOP);
            celda.setBorder(0);
            celda.setColspan(tam.length);
            tabla3.addCell(celda);

            celda = new PdfPCell(new Phrase("VALOR EN LETRAS:", pdf.font6n));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_TOP);
            celda.setBorder(0);
            tabla3.addCell(celda);
            
            celda = new PdfPCell(new Phrase("", pdf.font6));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_TOP);
            celda.setBorder(0);
            celda.setColspan(tamp.length);
            tabla3.addCell(celda);

           
            String total = vconsf.txtTotal.getText().replace(".", "");
            if (VLORPAG >= Integer.parseInt(total)) {
                
            celda = new PdfPCell(new Phrase("" + utl.convertirNumeroEnLetras(Integer.parseInt(total)).toUpperCase() + "   (" + utl.formatomoneda("" + Integer.parseInt(total)) + ")", pdf.font7));//VLORPAG
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_TOP);
            celda.setBorder(0);
            celda.setRowspan(4);
            tabla3.addCell(celda);
    
            }else{
            celda = new PdfPCell(new Phrase("" + utl.convertirNumeroEnLetras(VLORPAG).toUpperCase() + "   (" + utl.formatomoneda(""+ VLORPAG)+")", pdf.font7));//VLORPAG
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_TOP);
            celda.setBorder(0);
            celda.setRowspan(4);
            tabla3.addCell(celda);
     
            }
            celda = new PdfPCell(new Phrase("============================", pdf.font10n));
            celda.setHorizontalAlignment(Element. ALIGN_RIGHT);
            celda.setVerticalAlignment(Element.ALIGN_TOP);
            celda.setBorder(0);
            celda.setColspan(4);
            tabla3.addCell(celda);
            
            celda = new PdfPCell(new Phrase("\n", pdf.font8));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_TOP);
            celda.setBorder(0);
            celda.setColspan(tam.length);
            tabla3.addCell(celda);
          
            documento.add(tabla3);
            
            
            
            float[] tma = new float[]{20,20};
            PdfPTable tablaPagos = new PdfPTable(tma);
//            tabla= new PdfPTable(tma);
            tablaPagos.setWidthPercentage(100);
            
            celda = new PdfPCell(new Phrase("TOTAL A PAGAR:", pdf.font9n));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_TOP);
            celda.setBorder(0);
            tablaPagos.addCell(celda);
            celda = new PdfPCell(new Phrase(utl.formatomoneda(total), pdf.font10));
            celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
            celda.setVerticalAlignment(Element.ALIGN_TOP);
            celda.setBorder(0);
            tablaPagos.addCell(celda);
            
            celda = new PdfPCell(new Phrase("TOTAL PAGADO:", pdf.font9n));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_TOP);
            celda.setBorder(0);
            tablaPagos.addCell(celda);
            celda = new PdfPCell(new Phrase(utl.formatomoneda("" + VLORPAG), pdf.font10));
            celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
            celda.setVerticalAlignment(Element.ALIGN_TOP);
            celda.setBorder(0);
            tablaPagos.addCell(celda);

            if (cambio > 0) {

                celda = new PdfPCell(new Phrase("CAMBIO:", pdf.font9n));
                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                celda.setVerticalAlignment(Element.ALIGN_TOP);
                celda.setBorder(0);
                tablaPagos.addCell(celda);
                celda = new PdfPCell(new Phrase(utl.formatomoneda("" + cambio), pdf.font10));
                celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
                celda.setVerticalAlignment(Element.ALIGN_TOP);
                celda.setBorder(0);
                tablaPagos.addCell(celda);

            }

            int saldo = (Integer.parseInt(total) - VLORPAG);

            if (saldo > 0) {
                celda = new PdfPCell(new Phrase("SALDO PENDIENTE:", pdf.font9n));
                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                celda.setVerticalAlignment(Element.ALIGN_TOP);
                celda.setBorder(0);
                tablaPagos.addCell(celda);
                celda = new PdfPCell(new Phrase(utl.formatomoneda("" + saldo), pdf.font10));
                celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
                celda.setVerticalAlignment(Element.ALIGN_TOP);
                celda.setBorder(0);
                tablaPagos.addCell(celda);
            }
            
      
            
            celda = new PdfPCell(new Phrase("\n\n\n", pdf.font10n));
            celda.setColspan(2);
            celda.setBorder(0);
            tablaPagos.addCell(celda);
           
            celda = new PdfPCell(new Phrase("-------------------------------------------------", pdf.font10n));
            celda.setColspan(2);            
            celda.setBorder(0);
            tablaPagos.addCell(celda);
            
            String pk_paciente = "";
            if(vconsf.d.equals("O")){
                pk_paciente = vconsf.lblDocumento.getText();
            }else{
                pk_paciente = vconsf.combotipoDoc.getSelectedItem() + vconsf.txtdocumento.getText();
            }
            
            String prx_cit = getProximaCita(pk_paciente);
            
            if(!prx_cit.equals("")){
                celda = new PdfPCell(new Phrase("Proxima Cita", pdf.font12n));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setColspan(2);            
                celda.setBorder(0);
                tablaPagos.addCell(celda);
                String[] data = prx_cit.split("<::>");
                String[] fecha = data[0].split("/");
                Calendar cal =Calendar.getInstance(); 
                cal.set(Integer.parseInt(fecha[2]), Integer.parseInt(fecha[1])-1, Integer.parseInt(fecha[0]));
                
                SimpleDateFormat formato_fecha = new SimpleDateFormat("EEEE dd  'de' MMMM 'del' yyyy");
                
                celda = new PdfPCell(new Phrase(formato_fecha.format(cal.getTime()).toUpperCase(), pdf.font12n));
                celda.setColspan(2);            
                celda.setBorder(0);
                tablaPagos.addCell(celda);
                
                celda = new PdfPCell(new Phrase("HORA: "+ data[1], pdf.font12n));
                celda.setColspan(2);            
                celda.setBorder(0);
                tablaPagos.addCell(celda);

                celda = new PdfPCell(new Phrase("-------------------------------------------------", pdf.font10n));
                celda.setColspan(2);            
                celda.setBorder(0);
                tablaPagos.addCell(celda);
            }
            
            celda = new PdfPCell(new Phrase("Atendido por:", pdf.font10n));
            celda.setHorizontalAlignment(Element. ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_RIGHT);
            celda.setBorder(0);
            tablaPagos.addCell(celda);
            
            celda = new PdfPCell(new Phrase("" +datosUsuario.datos.get(0)[0], pdf.font10));
            celda.setHorizontalAlignment(Element. ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_RIGHT);
            celda.setBorder(0);
            tablaPagos.addCell(celda);
            
            celda = new PdfPCell(new Phrase("Hora:", pdf.font10n));
            celda.setHorizontalAlignment(Element. ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_RIGHT);
            celda.setBorder(0);
            celda.setColspan(0);
            tablaPagos.addCell(celda);
            
            celda = new PdfPCell(new Phrase("" +dato.get(0)[1], pdf.font10));
            celda.setHorizontalAlignment(Element. ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_RIGHT);
            celda.setBorder(0);
            tablaPagos.addCell(celda);
            

            celda = new PdfPCell(new Phrase("\n\n\n", pdf.font10n));
            celda.setColspan(2);
            celda.setBorder(0);
            tablaPagos.addCell(celda); 
            
            documento.add(tablaPagos);

          
            float[] tm4= new float[]{50};
            PdfPTable tabla4= new PdfPTable(tm4);
            tabla4.setWidthPercentage(100);
            
            celda = new PdfPCell(new Phrase("Centro Medico Perla del Caribe", pdf.font9n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_TOP);
            celda.setBorder(0);
            celda.setColspan(2);
            tabla4.addCell(celda);
            
            celda = new PdfPCell(new Phrase("Calle 22 No. 14 - 70 Cons. 5", pdf.font9n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_TOP);
            celda.setBorder(0);
            celda.setColspan(2);
            tabla4.addCell(celda);
            
            celda = new PdfPCell(new Phrase("Tel: 431 81 95 - Cel: 300 202 9614", pdf.font9n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_TOP);
            celda.setBorder(0);
            celda.setColspan(2);
            tabla4.addCell(celda);
            
            celda = new PdfPCell(new Phrase("Santa Marta, D.T.C.H.", pdf.font9n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_TOP);
            celda.setBorder(0);
            celda.setColspan(2);
            tabla4.addCell(celda);
            
            
            documento.add(tabla4);
            
             IMPRIMIR("!!!!! SE SUPONE QUE YA LO CREE!!!");
            
                      
        } catch (Exception ex) {
             ex.printStackTrace();
             
        }
       
    }
    
    ////////////////////////////////////////////////////////////////////////////
    
    public String GenerarFacturaAux(String NumFac, String paciente) {

        try {
            IMPRIMIR(" ####### &&&& GenerarFacturaAux INFROME (presente) &&&& ######### ");
            String ret = "";
            System.out.println("Parametros.dirFacturas-->"+Parametros.dirFacturas);
            String ruta = Parametros.dirFacturas + NumFac + ".pdf";
            System.out.println("ruta--"+ruta);
            FileOutputStream archivo = new FileOutputStream(ruta);
            Rectangle papel =  new Rectangle(210.0f, 792.0f);
          
//            PdfPTable inicio = getEncabezadoF(documento,NumFac);

            IMPRIMIR("por aquiii");
            documento = new Document(papel);
            PdfWriter writer = PdfWriter.getInstance(documento, archivo);
            writer.setPageEvent(new InformespdfFactura());

           //documento.setMargins(R, L, T, B);
            documento.setMargins(20, 20, 10, 35);
            documento.open();
            IMPRIMIR(" entro al  encabezado");
               
            GenerarPdfAux(NumFac, paciente);
            
            documento.close();
            
            Desktop.getDesktop().open(new File(ruta));            

            return ret;
        } catch (Exception ex) {
            //IMPRIMIR(funcion, "    ERROR: " + ex, probar);
            return null;
        }
            
    }

    public void GenerarPdfAux(String NumFac, String paciente) {
    
        try {
            
            String Consulta = "SELECT perso.Nombres, perso.Apellidos, perso.Telefonos, perso.Direccion, \n" +
                                "pxcon.`cantidad`, conc.`descripcion`, ROUND(pxcon.`valorxcantidad` / pxcon.`cantidad`,0) VLOR_U, pxcon.`valorxcantidad` VLOR_T, \n" +
                                "fact.`valor_fact`, pag.`total`, \n" +
                                "(pag.`total`-fact.`valor_fact`) CAMBIO, IFNULL(pend.`valor_deuda`, '') SALDO,\n" +
                                "IFNULL(modo.`pk_tipo_pago`, '') TIPO, IFNULL(pag.`usuario`, '') USUARIO, DATE_FORMAT(pag.`fecha`, '%d/%m/%Y') FECHA, DATE_FORMAT(pag.`fecha`,'%h:%i:%s %p') HORA\n" +
                                "FROM `facturas` fact\n" +
                                "INNER JOIN (\n" +
                                "SELECT CONCAT(pers.`pfk_tipo_documento`, pers.`pk_persona`) ID, \n" +
                                "CONCAT_WS(' ', pers.`primer_nombre`, IFNULL(pers.`segundo_nombre`, '')) Nombres, CONCAT_WS(' ', pers.`primer_apellido`, IFNULL(pers.`segundo_apellido`, '')) Apellidos,\n" +
                                "GROUP_CONCAT(IFNULL(tel.numero, '')) Telefonos, IFNULL(pers.`direccion`, '') Direccion\n" +
                                "FROM personas pers\n" +
                                "LEFT JOIN `personas_telefonos` tel ON tel.pfk_persona = CONCAT(pers.`pfk_tipo_documento`, pers.`pk_persona`)\n" +
                                "WHERE CONCAT(pers.`pfk_tipo_documento`, pers.`pk_persona`) = '"+paciente+"'\n" +
                                ") perso ON perso.ID = fact.`pfk_paciente`\n" +
                                "INNER JOIN pagos pag ON pag.`pk_pago` = fact.`numero`\n" +
                                "INNER JOIN pagosxconceptos pxcon ON pxcon.`pfk_pago` = fact.`numero`\n" +
                                "INNER JOIN conceptos conc ON conc.`pk_concepto` = pxcon.`pfk_concepto`\n" +
                                "INNER JOIN `modo_pago` modo ON modo.`pfk_pago` = fact.`numero`\n" +
                                "LEFT JOIN `pendientes_por_pagar` pend ON pend.`fk_factura` = fact.`numero`\n" +
                                "WHERE fact.`numero` = '"+NumFac+"'";
            
            List<Map<String, String>> lista = resultquery.ListSQL(Consulta);
            
                    
            System.out.println("lista....>"+lista.size());
            IMPRIMIR("***** ***** ***** ***** ENCABEZADO PARA INFORMES DE INSTITUCION $$$$$$$$***** ***** ");
         
            
            float[] tam = new float[]{40,60};
            PdfPTable tabla = new PdfPTable(tam);
            tabla.setWidthPercentage(100);
            
            Image m1 = Image.getInstance("Z:/Recursos/img/iconos/LOGO75.png");
            float m1w, m1h, m2w, m2h;
            m1w = m1h = m2w = m2h = 75;
            
            String ret = " ";
            PdfPCell celda = null;
            
                       
            if (m1 != null) {
                m1.setAlignment(Image.MIDDLE);
                m1.scaleAbsolute(m1w, m1h);
                celda = new PdfPCell(m1);
                celda.setFixedHeight(m1h + 5);
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celda.setBorder(0);
                celda.setRowspan(2);
                celda.setColspan(2);
                tabla.addCell(celda);
                
            } else {
                celda = new PdfPCell(new Phrase("", pdf.font12n));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celda.setBorder(0);
                celda.setRowspan(2);
                celda.setColspan(2);
                tabla.addCell(celda);
            }           
        
            celda = new PdfPCell(new Phrase("", pdf.font10n));
            celda.setColspan(2);
            celda.setBorder(0);
            tabla.addCell(celda);
                       
            celda = new PdfPCell(new Phrase(("JOSE ARRIETA"), pdf.font12n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celda.setColspan(2);
            celda.setBorder(0);
            tabla.addCell(celda);

            
            celda = new PdfPCell(new Phrase("Ortodoncia", pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celda.setColspan(2);
            celda.setBorder(0);
            tabla.addCell(celda);
            
            celda = new PdfPCell(new Phrase(("Odontólogo Pontificia Universidad Javeriana"), pdf.font10));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celda.setColspan(2);
            celda.setBorder(0);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase(("Ortodoncista U.C.C."), pdf.font10));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celda.setBorder(0);
            celda.setColspan(2);
            tabla.addCell(celda);
            
            celda = new PdfPCell(new Phrase("", pdf.font10n));
            celda.setColspan(2);
            celda.setBorder(0);
            tabla.addCell(celda);
            
            celda = new PdfPCell(new Phrase(("NIT: 79601299-1"), pdf.font10n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celda.setBorder(0);
            celda.setColspan(2);
            tabla.addCell(celda);
            
            celda = new PdfPCell(new Phrase("", pdf.font10n));
            celda.setColspan(2);
            celda.setBorder(0);
            tabla.addCell(celda);
 
//            Calendar cal = Calendar.getInstance();
//            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            
            celda = new PdfPCell(new Phrase("RECIBO # "+NumFac+" ", pdf.font10n));
            celda.setHorizontalAlignment(Element. ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setColspan(2);
            celda.setBorder(0);
            tabla.addCell(celda);
           
            celda = new PdfPCell(new Phrase("", pdf.font10n));
            celda.setColspan(2);        
            celda.setBorder(0);
            tabla.addCell(celda);
          

            celda = new PdfPCell(new Phrase("Fecha: "+lista.get(0).get("FECHA"), pdf.font10n));
            celda.setHorizontalAlignment(Element. ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_RIGHT);
            celda.setBorder(0);
            celda.setColspan(2); 
            tabla.addCell(celda);
            
            celda = new PdfPCell(new Phrase("", pdf.font10n));
            celda.setBorder(0);
            celda.setColspan(2);
            tabla.addCell(celda);
            
          
                        
            celda = new PdfPCell(new Phrase("============================", pdf.font10n));
            celda.setColspan(2);
            celda.setBorder(0);
            tabla.addCell(celda);
      

            documento.add(tabla);   
            IMPRIMIR("&&&&&&  HASTA AQUI EL ENCABEZADO  &&&&&&");
            
            IMPRIMIR("+++++ ENTRE?");
     
            float[] tamp = new float[]{30f,70f};

            PdfPTable tabla1 = new PdfPTable(tamp);
            tabla1.setWidthPercentage(100);

            celda = new PdfPCell(new Phrase("PACIENTE:", pdf.font6n));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_TOP);
            celda.setBorder(0);
            tabla1.addCell(celda);
            
            celda = new PdfPCell(new Phrase((lista.get(0).get("Nombres") + " " + lista.get(0).get("Apellidos")).toUpperCase(), pdf.font6));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_TOP);
            celda.setBorder(0);
            tabla1.addCell(celda);
            
            celda = new PdfPCell(new Phrase("DOCUMENTO DE IDENTIDAD:", pdf.font6n));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_TOP);
            celda.setBorder(0);
            tabla1.addCell(celda);
           
            celda = new PdfPCell(new Phrase((paciente), pdf.font6));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_TOP);
            celda.setBorder(0);
            tabla1.addCell(celda);

            celda = new PdfPCell(new Phrase("DIRECCIÓN:", pdf.font6n));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_TOP);
            celda.setBorder(0);
            tabla1.addCell(celda);
            
            celda = new PdfPCell(new Phrase(lista.get(0).get("Direccion"), pdf.font6));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_TOP);
            celda.setBorder(0);
            tabla1.addCell(celda);
            
            celda = new PdfPCell(new Phrase("TELEFONO:", pdf.font6n));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_TOP);
            celda.setBorder(0);
            tabla1.addCell(celda);

            celda = new PdfPCell(new Phrase(lista.get(0).get("Telefonos"), pdf.font6));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_TOP);
            celda.setBorder(0);
            tabla1.addCell(celda);
            
            celda = new PdfPCell(new Phrase("============================", pdf.font10n));
            celda.setColspan(2);
            celda.setBorder(0); 
            tabla1.addCell(celda);
           

            celda = new PdfPCell(new Phrase("", pdf.font8));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_TOP);
            celda.setBorder(0);
            celda.setColspan(tamp.length);
            tabla1.addCell(celda);

            documento.add(tabla1);

            float[] tam2 = new float[]{4,25,12,12 /*10*/};

            PdfPTable tabla2 = new PdfPTable(tam2);
            tabla2.setWidthPercentage(100);

            celda = new PdfPCell(new Phrase("C.".toUpperCase(), pdf.font6n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
//            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(0);
            tabla2.addCell(celda);

            celda = new PdfPCell(new Phrase("DESC.".toUpperCase(), pdf.font6n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER );
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
//            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(0);
            tabla2.addCell(celda);

            celda = new PdfPCell(new Phrase("V.UNIT".toUpperCase(), pdf.font6n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
//            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(0);
            tabla2.addCell(celda);
            celda = new PdfPCell(new Phrase("TOTAL".toUpperCase(), pdf.font6n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
//            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setBorder(0);
            tabla2.addCell(celda);
         

            int filas = lista.size();

            for (int i = 0; i < filas; i++) {
                
                celda = new PdfPCell(new Phrase("" + lista.get(i).get("cantidad"), pdf.font7));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                celda.setBorder(0);
                tabla2.addCell(celda);
                celda = new PdfPCell(new Phrase("" + lista.get(i).get("descripcion"), pdf.font7));
                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                celda.setBorder(0);
                tabla2.addCell(celda);
                                        
                celda = new PdfPCell(new Phrase("" + utl.formatomoneda(lista.get(i).get("VLOR_U")), pdf.font7));
                celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                celda.setBorder(0);
                tabla2.addCell(celda);
                celda = new PdfPCell(new Phrase("" + utl.formatomoneda(lista.get(i).get("VLOR_T")), pdf.font7));
                celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                celda.setBorder(0);
                tabla2.addCell(celda);
                
            }
            
       
            
            celda = new PdfPCell(new Phrase("============================", pdf.font10n));
            celda.setHorizontalAlignment(Element. ALIGN_RIGHT);
            celda.setVerticalAlignment(Element.ALIGN_TOP);
            celda.setBorder(0);
            celda.setColspan(4);
            tabla2.addCell(celda);
           
        
//     
            documento.add(tabla2);

//            PdfPTable tablaFormadepago = null;
//
//            int col = 3;
//            //++++++++++++++++++++ LOGO ++++++++++++++++++++++++++++++++++++
//            tablaFormadepago = new PdfPTable(col);
            float[] tm = new float[]{50};
            
            PdfPTable tabla3 = new PdfPTable(tm);
            tabla3.setWidthPercentage(100);
            String forma = lista.get(0).get("TIPO").toUpperCase();            

            celda = new PdfPCell(new Phrase("FORMA DE PAGO: " +forma, pdf.font6n));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celda.setBorder(0);
            tabla3.addCell(celda);

            celda = new PdfPCell(new Phrase("\n", pdf.font8));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_TOP);
            celda.setBorder(0);
            celda.setColspan(tam.length);
            tabla3.addCell(celda);

            celda = new PdfPCell(new Phrase("VALOR EN LETRAS:", pdf.font6n));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_TOP);
            celda.setBorder(0);
            tabla3.addCell(celda);
            
            celda = new PdfPCell(new Phrase("", pdf.font6));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_TOP);
            celda.setBorder(0);
            celda.setColspan(tamp.length);
            tabla3.addCell(celda);

           
            String total = lista.get(0).get("valor_fact");;
            String vlorpag = lista.get(0).get("total");
            if (Integer.parseInt(vlorpag) >= Integer.parseInt(total)) {                
                celda = new PdfPCell(new Phrase("" + utl.convertirNumeroEnLetras(Integer.parseInt(total)).toUpperCase() + "   (" + utl.formatomoneda("" + Integer.parseInt(total)) + ")", pdf.font7));//VLORPAG
                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                celda.setVerticalAlignment(Element.ALIGN_TOP);
                celda.setBorder(0);
                celda.setRowspan(4);
                tabla3.addCell(celda);
    
            }else{
                celda = new PdfPCell(new Phrase("" + utl.convertirNumeroEnLetras(Integer.parseInt(vlorpag)).toUpperCase() + "   (" + utl.formatomoneda(""+ Integer.parseInt(vlorpag))+")", pdf.font7));//VLORPAG
                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                celda.setVerticalAlignment(Element.ALIGN_TOP);
                celda.setBorder(0);
                celda.setRowspan(4);
                tabla3.addCell(celda);
     
            }
            celda = new PdfPCell(new Phrase("============================", pdf.font10n));
            celda.setHorizontalAlignment(Element. ALIGN_RIGHT);
            celda.setVerticalAlignment(Element.ALIGN_TOP);
            celda.setBorder(0);
            celda.setColspan(4);
            tabla3.addCell(celda);
            
            celda = new PdfPCell(new Phrase("\n", pdf.font8));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_TOP);
            celda.setBorder(0);
            celda.setColspan(tam.length);
            tabla3.addCell(celda);
          
            documento.add(tabla3);
            
            
            
            float[] tma = new float[]{20,20};
            PdfPTable tablaPagos = new PdfPTable(tma);
//            tabla= new PdfPTable(tma);
            tablaPagos.setWidthPercentage(100);
            
            celda = new PdfPCell(new Phrase("TOTAL A PAGAR:", pdf.font9n));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_TOP);
            celda.setBorder(0);
            tablaPagos.addCell(celda);
            celda = new PdfPCell(new Phrase(utl.formatomoneda(total), pdf.font10));
            celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
            celda.setVerticalAlignment(Element.ALIGN_TOP);
            celda.setBorder(0);
            tablaPagos.addCell(celda);
            
            celda = new PdfPCell(new Phrase("TOTAL PAGADO:", pdf.font9n));
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_TOP);
            celda.setBorder(0);
            tablaPagos.addCell(celda);
            celda = new PdfPCell(new Phrase(utl.formatomoneda("" + Integer.parseInt(vlorpag)), pdf.font10));
            celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
            celda.setVerticalAlignment(Element.ALIGN_TOP);
            celda.setBorder(0);
            tablaPagos.addCell(celda);

            int cambio = Integer.parseInt(lista.get(0).get("CAMBIO"));
            if (cambio > 0) {

                celda = new PdfPCell(new Phrase("CAMBIO:", pdf.font9n));
                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                celda.setVerticalAlignment(Element.ALIGN_TOP);
                celda.setBorder(0);
                tablaPagos.addCell(celda);
                celda = new PdfPCell(new Phrase(utl.formatomoneda("" + cambio), pdf.font10));
                celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
                celda.setVerticalAlignment(Element.ALIGN_TOP);
                celda.setBorder(0);
                tablaPagos.addCell(celda);

            }

            

            if (!lista.get(0).get("SALDO").equals("")) {
                
                int saldo = Integer.parseInt(lista.get(0).get("SALDO"));
                celda = new PdfPCell(new Phrase("SALDO PENDIENTE:", pdf.font9n));
                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                celda.setVerticalAlignment(Element.ALIGN_TOP);
                celda.setBorder(0);
                tablaPagos.addCell(celda);
                celda = new PdfPCell(new Phrase(utl.formatomoneda("" + saldo), pdf.font10));
                celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
                celda.setVerticalAlignment(Element.ALIGN_TOP);
                celda.setBorder(0);
                tablaPagos.addCell(celda);
            }
            
      
            
            celda = new PdfPCell(new Phrase("\n\n\n", pdf.font10n));
            celda.setColspan(2);
            celda.setBorder(0);
            tablaPagos.addCell(celda);
           
            celda = new PdfPCell(new Phrase("-------------------------------------------------", pdf.font10n));
            celda.setColspan(2);            
            celda.setBorder(0);
            tablaPagos.addCell(celda);
            
            String prx_cit = getProximaCita(paciente);
            
            if(!prx_cit.equals("")){
                celda = new PdfPCell(new Phrase("Proxima Cita", pdf.font12n));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setColspan(2);            
                celda.setBorder(0);
                tablaPagos.addCell(celda);
                String[] data = prx_cit.split("<::>");
                String[] fecha = data[0].split("/");
                Calendar cal =Calendar.getInstance(); 
                cal.set(Integer.parseInt(fecha[2]), Integer.parseInt(fecha[1])-1, Integer.parseInt(fecha[0]));
                
                SimpleDateFormat formato_fecha = new SimpleDateFormat("EEEE dd  'de' MMMM 'del' yyyy");
                
                celda = new PdfPCell(new Phrase(formato_fecha.format(cal.getTime()).toUpperCase(), pdf.font12n));
                celda.setColspan(2);            
                celda.setBorder(0);
                tablaPagos.addCell(celda);
                
                celda = new PdfPCell(new Phrase("HORA: "+ data[1], pdf.font12n));
                celda.setColspan(2);            
                celda.setBorder(0);
                tablaPagos.addCell(celda);

                celda = new PdfPCell(new Phrase("-------------------------------------------------", pdf.font10n));
                celda.setColspan(2);            
                celda.setBorder(0);
                tablaPagos.addCell(celda);
            }
            
            celda = new PdfPCell(new Phrase("Atendido por:", pdf.font10n));
            celda.setHorizontalAlignment(Element. ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_RIGHT);
            celda.setBorder(0);
            tablaPagos.addCell(celda);
            
            celda = new PdfPCell(new Phrase("" +lista.get(0).get("USUARIO"), pdf.font10));
            celda.setHorizontalAlignment(Element. ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_RIGHT);
            celda.setBorder(0);
            tablaPagos.addCell(celda);
            
            celda = new PdfPCell(new Phrase("Hora:", pdf.font10n));
            celda.setHorizontalAlignment(Element. ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_RIGHT);
            celda.setBorder(0);
            celda.setColspan(0);
            tablaPagos.addCell(celda);
            
            celda = new PdfPCell(new Phrase("" +lista.get(0).get("HORA"), pdf.font10));
            celda.setHorizontalAlignment(Element. ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_RIGHT);
            celda.setBorder(0);
            tablaPagos.addCell(celda);
            

            celda = new PdfPCell(new Phrase("\n\n\n", pdf.font10n));
            celda.setColspan(2);
            celda.setBorder(0);
            tablaPagos.addCell(celda); 
            
            documento.add(tablaPagos);

          
            float[] tm4= new float[]{50};
            PdfPTable tabla4= new PdfPTable(tm4);
            tabla4.setWidthPercentage(100);
            
            celda = new PdfPCell(new Phrase("Centro Medico Perla del Caribe", pdf.font9n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_TOP);
            celda.setBorder(0);
            celda.setColspan(2);
            tabla4.addCell(celda);
            
            celda = new PdfPCell(new Phrase("Calle 22 No. 14 - 70 Cons. 5", pdf.font9n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_TOP);
            celda.setBorder(0);
            celda.setColspan(2);
            tabla4.addCell(celda);
            
            celda = new PdfPCell(new Phrase("Tel: 431 81 95 - Cel: 300 202 9614", pdf.font9n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_TOP);
            celda.setBorder(0);
            celda.setColspan(2);
            tabla4.addCell(celda);
            
            celda = new PdfPCell(new Phrase("Santa Marta, D.T.C.H.", pdf.font9n));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_TOP);
            celda.setBorder(0);
            celda.setColspan(2);
            tabla4.addCell(celda);
            
            
            documento.add(tabla4);
            
             IMPRIMIR("!!!!! SE SUPONE QUE YA LO CREE!!!");
            
                      
        } catch (Exception ex) {
             ex.printStackTrace();
             
        }
       
    }

    ////////
    public String getProximaCita(String pfk_paciente){
        try{
            String ret = "";
            
            String sql = "SELECT CONCAT(DATE_FORMAT(c.`fecha_cita`, '%d/%m/%Y'), '<::>', HORA_M(cxh.`hora`)) FECHA\n" +
                    "FROM citas c \n" +
                    "INNER JOIN `citasxhoras` cxh ON cxh.`pfk_paciente` = c.`pfk_paciente` AND cxh.`pfk_cita` = c.`pk_cita`\n" +
                    "WHERE c.`pfk_paciente` = '"+pfk_paciente+"' AND c.`estado` = 'Activo'\n" +
                    "ORDER BY `fecha_cita` ASC\n" +
                    "LIMIT 1;";
            
            ArrayList<String []> info = resultquery.SELECT(sql);
            
            if(info.size()>0){
                ret = info.get(0)[0];
            }            
            System.out.println("ret--"+ret+"-");
            return ret;
        }catch(Exception e){
            System.out.println("ERROR   - "+e.toString());
            return null;
        }
    }
}
