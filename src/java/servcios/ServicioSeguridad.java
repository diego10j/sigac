/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servcios;

import aplicacion.Utilitario;
import javax.ejb.Stateless;

/**
 *
 * @author Diego
 */
@Stateless
public class ServicioSeguridad {

    private Utilitario utilitario = new Utilitario();
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public String ingresar(String usuaro, String clave) {
        utilitario.crearVariable("IDE_USUA", "0");
        return "";
    }

    public String getClaveUsuario(String usuario) {
        return "";
    }

    public void cambiarClave(String usuario, String claveNueva) {
    }
}
