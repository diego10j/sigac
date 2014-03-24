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
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
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
    private List<String> imagenes;

    @PostConstruct
    public void cargar() {
        imagenes = new ArrayList();
        imagenes.add("im1.jpg");
        imagenes.add("im2.jpg");
        imagenes.add("im3.jpg");
    }

    public void ingresar() {
        Conexion conexion = utilitario.getConexion();

        if (conexion == null) {
            conexion = new Conexion();
            String str_recursojdbc = utilitario.getPropiedad("recursojdbc");
            conexion.setUnidad_persistencia(str_recursojdbc);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("CONEXION", conexion);
        }
        utilitario.crearVariable("IDE_EMPR", utilitario.getPropiedad("ide_empr"));
        utilitario.crearVariable("IDE_SUCU", "0");
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
            utilitario.ejecutarJavaScript("wdiaClave.hide();");
        } else {
            utilitario.agregarMensajeInfo("Para continuar es necesario que cambie su clave", "");
        }
    }

    public void cambiarClave() {
        if (strClaveNueva == null) {
            utilitario.agregarMensajeInfo("Es necesario ingresar la nueva clave", "");
            return;
        }
        if (strConfirmaClaveNueva == null) {
            
            utilitario.agregarMensajeInfo("Es necesario confirmar la nueva clave", "");
            return;
        }

        //Valido que tenga una mayuscula y un digito
        boolean boo_mayus = false;
        boolean boo_digi = false;

        for (int i = 0; i < strClaveNueva.length(); i++) {
            if (Character.isDigit(strClaveNueva.charAt(i))) {
                boo_digi = true;
            } else {
                if (Character.isUpperCase(strClaveNueva.charAt(i))) {
                    boo_mayus = true;
                }
            }
        }

        if (boo_digi == false || boo_mayus == false) {
            strClaveActua = null;
            strClaveNueva = null;
            strConfirmaClaveNueva = null;
            utilitario.agregarMensajeInfo("La clave nueva debe contener almenos una letra mayúscula y un dígito numerico", "");
            return;
        }

        if (!strClaveNueva.equals(strConfirmaClaveNueva)) {
            strClaveActua = null;
            strClaveNueva = null;
            strConfirmaClaveNueva = null;
            utilitario.agregarMensajeInfo("La clave nueva debe se igual a la clave de confirmación", "");
            return;
        }

        if (!strClaveNueva.equals(strConfirmaClaveNueva)) {
            strClaveActua = null;
            strClaveNueva = null;
            strConfirmaClaveNueva = null;
            utilitario.agregarMensajeInfo("La clave nueva debe se igual a la clave de confirmación", "");
            return;
        }






        if (!strClaveActua.isEmpty()) {
            Encriptar encriptar = new Encriptar();
            if (ser_seguridad.getClaveUsuario(utilitario.getVariable("IDE_USUA")).equals(encriptar.getEncriptar(strClaveActua))) {
                ser_seguridad.cambiarClave(utilitario.getVariable("IDE_USUA"), strClaveNueva);
                strClaveActua = null;
                strClaveNueva = null;
                strConfirmaClaveNueva = null;
                booCambiaClave = false;
                utilitario.agregarMensaje("La clave se cambió correctamente", "");
                utilitario.ejecutarJavaScript("wdiaClave.hide();");
            } else {
                utilitario.agregarMensajeError("La clave actual es incorrecta", "");
            }
        } else {
            utilitario.agregarMensajeInfo("Es necesario ingresar la clave actual", "");
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
        return utilitario.getVariable("NOMBRE");
    }

    public List<String> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<String> imagenes) {
        this.imagenes = imagenes;
    }

    public String getStrClaveActua() {
        return strClaveActua;
    }

    public void setStrClaveActua(String strClaveActua) {
        this.strClaveActua = strClaveActua;
    }

    public String getStrClaveNueva() {
        return strClaveNueva;
    }

    public void setStrClaveNueva(String strClaveNueva) {
        this.strClaveNueva = strClaveNueva;
    }

    public String getStrConfirmaClaveNueva() {
        return strConfirmaClaveNueva;
    }

    public void setStrConfirmaClaveNueva(String strConfirmaClaveNueva) {
        this.strConfirmaClaveNueva = strConfirmaClaveNueva;
    }
}