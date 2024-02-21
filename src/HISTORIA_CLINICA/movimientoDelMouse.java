package HISTORIA_CLINICA;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

class movimientoDelMouse implements MouseMotionListener {

    public Odontograma o;

    public movimientoDelMouse(Odontograma od) {
        o = od;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        o.actualizar();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (o.isEnabled()) {
            for (int i = 0; i < o.convenciones.size(); i++) {
                if (notifMovimientoSobreConvencion(e.getPoint(), o.convenciones.get(i))) {
                    o.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    if (!o.convenciones.get(i).press) {
                        o.convenciones.get(i).fondo = new Color(206, 246, 245);
                        o.convenciones.get(i).borde = new Color(129, 218, 245);
                    }
                    break;
                } else {
                    if (!o.convenciones.get(i).press) {
                        o.convenciones.get(i).fondo = new Color(240, 240, 240);
                        o.convenciones.get(i).borde = new Color(200, 200, 200);
                    }
                    o.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }

            int entro = 0;

            if (o.idDientePress != -1) {
                if (!o.dientes.get(o.idDientePress).mopc.opciones.isEmpty()) {
                    for (int i = 0; i < o.dientes.get(o.idDientePress).mopc.opciones.size(); i++) {
                        if (notificarMovimientoSobreOpcion(e.getPoint(), o.dientes.get(o.idDientePress).mopc.opciones.get(i))) {
                            o.idOpcionSeleccionada = i;
                            o.dientes.get(o.idDientePress).mopc.opciones.get(o.idOpcionSeleccionada).fondo = new Color(129, 218, 245);
                        } else {
                            o.dientes.get(o.idDientePress).mopc.opciones.get(i).fondo = new Color(255, 255, 255);
                        }
                    }
                }
            }
        }
        o.actualizar();
    }

    private boolean notifMovimientoSobreConvencion(Point p, Convencion c) {
        if (p.x > c.x
                && p.x < c.x + c.ANCHO
                && p.y > c.y
                && p.y < c.y + c.ALTO) {
            c.over = true;
            c.pMenEmer = p;
            return true;
        } else {
            c.over = false;
            c.pMenEmer = new Point(0, 0);
            return false;
        }
    }

    private boolean notificarMovimientoSobreOpcion(Point p, Opcion op) {
        if (p.x > op.x
                && p.x < op.x + op.ancho
                && p.y > op.y
                && p.y < op.y + op.alto) {
            return true;
        } else {
            return false;
        }
    }
}
