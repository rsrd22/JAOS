/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package BaseDeDatos;

import java.security.MessageDigest;

/**
 *
 * @author ATDELLI7
 */
public class Encryptar {

    public String EncryptarClave(String cadena) {

        String r = null;
        try {
            if (cadena != null) {
                MessageDigest algorithm = MessageDigest.getInstance("MD5");
                algorithm.reset();
                algorithm.update(cadena.getBytes());
                byte bytes[] = algorithm.digest();
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < bytes.length; i++) {
                    String hex = Integer.toHexString(0xff & bytes[i]);
                    if (hex.length() == 1) {
                        sb.append('0');
                    }
                    sb.append(hex);
                }

                r = sb.toString();
            }
        } catch (Exception e) {
        }

        return r;

    }

}

