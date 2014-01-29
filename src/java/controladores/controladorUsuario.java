/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import aplicacion.Utilitario;
import entidades.Alumnos;
import entidades.Usuario;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import servcios.servicioAlumno;
import servcios.servicioUsuario;

/**
 *
 * @author Diego
 */
@ManagedBean
@ViewScoped
public class controladorUsuario {

    @EJB
    private servicioUsuario servUsuario;
    private Usuario usuUsuario = new Usuario();
    private Utilitario utilitario = new Utilitario();

    public void guardar() {
            String str_mensaje = servUsuario.guardarUsuario(usuUsuario);
            if (str_mensaje.isEmpty()) {
                utilitario.agregarMensaje("Se guardo correctamente", "");
                usuUsuario = new Usuario();
            } else {
                utilitario.agregarMensajeError("No se pudo guardar", str_mensaje);
            }
    }

    public void consultaUsuario(){
        System.out.println("consulta usuario");
        servUsuario.getUsuario("0");
    }
    
    public Usuario getUsuUsuario() {
        return usuUsuario;
    }

    public void setUsuUsuario(Usuario usuUsuario) {
        this.usuUsuario = usuUsuario;
    }

}
