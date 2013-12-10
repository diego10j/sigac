/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import framework.componentes.Encriptar;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import aplicacion.Utilitario;
import javax.faces.bean.ViewScoped;
import persistencia.Conexion;
import servcios.ServicioSeguridad;

/**
 *
 * @author Diego
 */
@ManagedBean
@ViewScoped
public class controladorLogin {

    private String usuario;
    private String clave;
    private Utilitario utilitario = new Utilitario();
    @EJB
    private ServicioSeguridad ser_seguridad;
    private boolean booCambiaClave = false;
    private String strClaveActua;
    private String strClaveNueva;
    private String strConfirmaClaveNueva;

    public void ingresar() {
        Conexion conexion = utilitario.getConexion();
        if (conexion == null) {
            conexion = new Conexion();
            String str_recursojdbc = utilitario.getPropiedad("recursojdbc");
            conexion.setUnidad_persistencia(str_recursojdbc);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("CONEXION", conexion);
        }
        utilitario.crearVariable("IDE_EMPR", utilitario.getPropiedad("ide_empr"));
        utilitario.crearVariable("IDE_SUCU", "0"); //para que sea biess quito
        String str_mensaje = ser_seguridad.ingresar(usuario, clave);
        if (str_mensaje.isEmpty()) {
            try {
                utilitario.crearVariable("NICK", usuario);
                FacesContext.getCurrentInstance().getExternalContext().redirect("portal/index.jsf");
            } catch (Exception e) {
            }
        } else {
            utilitario.agregarMensajeError(str_mensaje, "");
        }
    }

    public void salir() {
        try {
            utilitario.ejecutarJavaScript("location.href='about:blank'");
            utilitario.ejecutarJavaScript("window.close()");
            if (utilitario.getConexion() != null) {
                utilitario.getConexion().desconectar();
            }
            utilitario.cerrarSesion();
        } catch (Exception ex) {
        }
    }

    public void cancelarCambiarClave() {
        if (booCambiaClave == false) {
            utilitario.ejecutarJavaScript("diaClave.hide();");
        } else {
            utilitario.agregarMensajeInfo("Debe cambiar su clave", "Para continuar es necesario que cambie su clave");
        }
    }

    public void cambiarClave() {
        if (strClaveNueva == null) {
            utilitario.agregarMensajeInfo("Validación", "Es necesario ingresar la nueva clave");
            return;
        }
        if (strConfirmaClaveNueva == null) {
            utilitario.agregarMensajeInfo("Validación", "Es necesario confirmar la nueva clave");
            return;
        }

        if (!strClaveNueva.equals(strConfirmaClaveNueva)) {
            utilitario.agregarMensajeInfo("Validación", "La clave nueva debe se igual a la clave de confirmación");
            return;
        }


        if (!strClaveActua.isEmpty()) {
            Encriptar encriptar = new Encriptar();
            if (ser_seguridad.getClaveUsuario(utilitario.getVariable("IDE_USUA")).equals(encriptar.getEncriptar(strClaveActua))) {
                ser_seguridad.cambiarClave(utilitario.getVariable("IDE_USUA"), strClaveNueva);
                utilitario.agregarMensaje("Cambio clave", "La clave a sido cambiada correctamente");
                booCambiaClave = false;
                utilitario.ejecutarJavaScript("diaClave.hide();");
            } else {
                utilitario.agregarMensajeError("Error", "La clave actual es incorrecta");
            }
        } else {
            utilitario.agregarMensajeInfo("Validación", "Es necesario ingresar la clave actual");
        }
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public boolean isBooCambiaClave() {
        return booCambiaClave;
    }

    public void setBooCambiaClave(boolean booCambiaClave) {
        this.booCambiaClave = booCambiaClave;
    }

    public String getStrFecha() {
        return utilitario.getFechaLarga(utilitario.getFechaActual()); //fecha completa 
    }

    public String getStrNick() {
        return utilitario.getVariable("NICK");
    }
}