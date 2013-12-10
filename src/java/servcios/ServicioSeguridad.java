/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servcios;

import javax.ejb.Stateless;

/**
 *
 * @author Diego
 */
@Stateless
public class ServicioSeguridad {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public String ingresar(String usuaro, String clave) {
        return "";
    }

    public String getClaveUsuario(String usuario) {
        return "";
    }

    public void cambiarClave(String usuario, String claveNueva) {
    }
}
