package HISTORIA_CLINICA;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class movimientoDelMouseIMG implements MouseMotionListener {

    public Imagenes img;

    public movimientoDelMouseIMG(Imagenes imref) {
        img = imref;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        img.actualizar();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (img.isEnabled()) {
            for (int i = 0; i < img.tipimg.length; i++) {
                if (detectarMovimientosobrelasOpciones(img.tipimg[i], e.getPoint())) {
                    if (img.tipimg[i].desc.equals("BORRAR IMAGEN")) {
                        img.tipimg[i].fondo = new Color(146, 43, 33);//color rojo oscuro
                    } else {
                        img.tipimg[i].fondo = new Color(15, 48, 69);
                    }
                } else {
                    if (img.tipimg[i].desc.equals("BORRAR IMAGEN")) {
                        img.tipimg[i].fondo = new Color(146, 43, 33);//color rojo claro
                    } else {
                        img.tipimg[i].fondo = new Color(26, 82, 118);
                    }
                }
            }
        }
        img.actualizar();
    }

    private boolean detectarMovimientosobrelasOpciones(tiposDeImagenes t, Point p) {
        if (p.x > t.x
                && p.x < t.x + t.ancho
                && p.y > t.y
                && p.y < t.y + t.alto) {
            return true;
        } else {
            return false;
        }
    }

}
