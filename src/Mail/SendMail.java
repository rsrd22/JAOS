/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mail;
import java.util.List;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
/**
 *
 * @author Acer
 */
public class SendMail {
    String host = "smtp.gmail.com";
    String port = "587";

    String user = "rsrd22@gmail.com"; 
    String pass = "hubwcyghpsnyqzbp"; //   //nbhstbizmlcowuww // hubwcyghpsnyqzbp
    public static Properties props = new Properties();
    
    public boolean EnviarEmail(String tercero, String mailDestino, String titulo, String mensaje) {
        String htmlBody="";
        System.out.print("\n\n---> ENVIANDO MAIL desde "+user+" a "+mailDestino);
        try {
            props.put("mail.smtp.host", "smtp.gmail.com");
            
            //props.setProperty("mail.smtp.host", host);
            props.setProperty("mail.smtp.starttls.enable", "true");
            props.setProperty("mail.smtp.port", port);
            props.setProperty("mail.smtp.user", user);
            props.setProperty("mail.smtp.auth", "true"); 

            //Firma de Correo
            htmlBody = Disenio02(tercero, titulo, mensaje);


// Preparamos la sesion
            Session session = Session.getDefaultInstance(props, null);
            //session.getProperties().put("mail.smtp.starttls.enable", "true");
            session.getProperties().put("mail.smtp.ssl.trust", "smtp.gmail.com");
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(user));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(mailDestino));

            message.setSubject(titulo); //adjuntar asunto...
            //message.setText(mensage); //enviar mensage... 
            message.setContent(htmlBody, "text/html");

// Lo enviamos.
            Transport t = session.getTransport("smtp");
            t.connect(user, pass);
            t.sendMessage(message, message.getAllRecipients());
// Cierre.
            t.close();
            System.out.print("---> OK\n");
            return true;
        } catch (Exception e) {
            System.out.print("---> ERROR: \n"+e);
            e.printStackTrace();
            return false;
        }
    }

    private String Disenio02(String nombre, String asunto, String htmlMensaje) {
        return "<center>"
                + "<div align=\"justify\" style=\"font-family: 'New Century Schoolbook', Times, serif; font-size:medium; width:800px;\" >"
                + "<table cellpadding=\"3\" cellspacing=\"0\" border=\"0\" bgcolor=\"#4F5F39\" width=\"100%\"><tr><td width=\"10\"></td><td>"
                + ""
                + "<table cellpadding=\"3\" cellspacing=\"0\" border=\"0\" width=\"100%\"><tr><td height=\"10\"></td></tr>"
                + "<tr><td bgcolor=\"#DDDDDD\" style=\"border-bottom: 2px double #DA251C;\">"
                + "<b style=\"color: #DA251C; font-size:large;\">Servicio de Notificaciones Autom&aacute;ticas</b><br/>"
                + "</td></tr>"
                + ""
                + "<tr><td bgcolor=\"#FFFFFF\" style=\"font-size:medium; color:#000000;\">"
                + "<p><b>Señor(a):<br/>" + nombre + "</b></p>"
                + "<p align='justify'><b>Asunto: </b>" + asunto + "<p>"
                + "<p align='justify'>" + htmlMensaje + "<p><br/></td></tr>"
                + ""
                + "<tr><td bgcolor=\"#FFFFFF\" style=\"font-size:small; color:#004070; border-bottom: 2px double #DA251C;\">"
                + ""
                + "<p align='justify'>Esta es una notificaci&oacute;n generada de forma autom&aacute;tica por la Plataforma Web gmail.com. "
                + "<br/>Por favor no responda ni env&iacute;e mensajes a esta direcci&oacute;n de correo. Si desea ponerse en contacto con nosotros puede llamar a nuestros teléfonos."
                + "<br/>Si ha recibido este e-mail por error por favor hacer caso omiso a su contenido.</p></td></tr>"
                + ""
                //+ "<tr><td bgcolor=\"#DDDDDD\" style=\"font-size:small; color:#000000;\">"
                //+ "<p align='right' style=\"font-size:small; color:#000000\">Desarroll&oacute;: <a href=\"http://www.atgc.net.co\" style=\"font-size:small; color:#000000\">Advanced Technologies Group Consultant S.A.</a></p>  "
                + ""
                + "<tr><td height=\"10\"></td></tr></table></td><td width=\"10\"></td></tr></table></div></center>";
    }
}
