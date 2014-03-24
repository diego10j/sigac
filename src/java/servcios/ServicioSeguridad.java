/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servcios;

import aplicacion.Utilitario;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Encriptar;
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
        String str_mensaje = "";
        Encriptar encriptar = new Encriptar();
        TablaGenerica tab_ingresa = utilitario.consultar("SELECT * FROM usuario where usu_nick='" + usuaro + "' and usu_clave='" + encriptar.getEncriptar(clave) + "'");

        if (tab_ingresa.isEmpty()) {
            str_mensaje = "El nombre del usuario o la clave son incorrectos";
        } else {
            //Valida que el usuario este activo
            if (tab_ingresa.getValor("usu_activo").equals("true") || tab_ingresa.getValor("usu_activo").equals("t")) {
                utilitario.crearVariable("IDE_USUA", tab_ingresa.getValor("usu_codigo"));
                utilitario.crearVariable("rolCodigo", tab_ingresa.getValor("rol_codigo"));
                utilitario.crearVariable("nombre", tab_ingresa.getValor("usu_nombre"));
                utilitario.crearVariable("doc_codigo", tab_ingresa.getValor("doc_codigo") == null ? "-1" : tab_ingresa.getValor("doc_codigo"));
                utilitario.crearVariable("alu_codigo", tab_ingresa.getValor("alu_codigo") == null ? "-1" : tab_ingresa.getValor("alu_codigo"));
            } else {
                str_mensaje = "Su usuario se encuentra inactivo";
            }
        }

        return str_mensaje;
    }

    public String getClaveUsuario(String usuario) {
        TablaGenerica tab_ingresa = utilitario.consultar("SELECT * FROM usuario where usu_codigo=" + usuario);
        if (tab_ingresa.isEmpty()) {
            return "";
        } else {
            return tab_ingresa.getValor("usu_clave");
        }

    }

    public void cambiarClave(String usuario, String claveNueva) {
        Encriptar encriptar = new Encriptar();
        utilitario.getConexion().ejecutarSql("update usuario set usu_clave='" + encriptar.getEncriptar(claveNueva) + "' where usu_codigo=" + usuario);

    }
}
