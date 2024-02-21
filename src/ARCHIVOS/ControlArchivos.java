package ARCHIVOS;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class ControlArchivos {

    private String path;
    private int numeroDeLineas;
    private FileWriter fw;
    private FileReader fr;
    private BufferedReader buferDeLectura;

    public ControlArchivos(String ruta) {
        if (ruta.isEmpty()) {
            path = "C:/";
        } else {
            path = ruta;
        }
        numeroDeLineas = 0;
        fw = null;
        fr = null;
        buferDeLectura = null;
    }

    public ControlArchivos() {
        path = "C:/";
        numeroDeLineas = 0;
        fw = null;
        fr = null;
        buferDeLectura = null;
    }

    public String getURL() {
        return path;
    }

    public void EscribirArchivo(String texto, String ruta) {
        PrintWriter archivoParaEscritura;

        try {
            fw = new FileWriter(ruta);

            archivoParaEscritura = new PrintWriter(fw);
            archivoParaEscritura.print(texto);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        } finally {
            try {
                if (null != fw) {
                    fw.close();
                }
            } catch (Exception ex1) {
                JOptionPane.showMessageDialog(null, ex1.getMessage());
            }
        }
    }

    public void EscribirArchivo(String texto, String ruta, boolean textoAlFinal) {
        PrintWriter archivoParaEscritura;

        try {
            if (textoAlFinal) {
                fw = new FileWriter(ruta, textoAlFinal);
            } else {
                fw = new FileWriter(ruta);
            }

            archivoParaEscritura = new PrintWriter(fw);
            archivoParaEscritura.print(texto);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        } finally {
            try {
                if (null != fw) {
                    fw.close();
                }
            } catch (Exception ex1) {
                JOptionPane.showMessageDialog(null, ex1.getMessage());
            }
        }
    }

    public void LeerArchivo() {
        try {
            FileInputStream fstream = new FileInputStream(path);
            // Creamos el objeto de entrada
            DataInputStream entrada = new DataInputStream(fstream);
            // Creamos el Buffer de Lectura

            BufferedReader br = new BufferedReader(new InputStreamReader(entrada));
            buferDeLectura = br;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        } finally {
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (Exception ex1) {
                JOptionPane.showMessageDialog(null, ex1.getMessage());
            }
        }
    }

    public BufferedReader getBuferDeLectura() {
        BufferedReader br = buferDeLectura;
        return br;
    }

    public int getNumeroDeLineas() throws IOException {
        int numLineas = 0;
        String lineas;
        try {
            FileInputStream fstream = new FileInputStream(path);
            // Creamos el objeto de entrada
            DataInputStream entrada = new DataInputStream(fstream);
            // Creamos el Buffer de Lectura

            BufferedReader br = new BufferedReader(new InputStreamReader(entrada));
            while ((lineas = br.readLine()) != null) {
                if (!lineas.isEmpty()) {
                    numLineas++;
                }
            }
            return numLineas;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            return 0;
        } finally {
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (Exception ex1) {
                JOptionPane.showMessageDialog(null, ex1.getMessage());
            }
        }

    }

    public boolean crearDirectorio(String url) {
        File directorio = new File(url);
        if (!directorio.exists()) {
            directorio.mkdirs();
            return true;
        }else{
            return false;
        }
    }

    /**
     * Este metodo permite copiar un archivo de un directorio a otro.
     *
     * @param origen Esta variable contiene la ruta (URL) de origen mas el
     * nombre del archivo a copiarse.
     * @param destino Esta variable contiene la ruta (URL) del directorio al
     * cual se van a mover los archivos del origen.
     * @throws IOException
     */
    public void moverArchivos(String[] origen, String destino) {
        Path TO = Paths.get(destino);
        for (String origen1 : origen) {
            try {
                Path FROM = Paths.get(origen1);
                Files.copy(FROM, TO.resolve(FROM.getFileName()));
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        }
    }
}
