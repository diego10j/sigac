/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import aplicacion.Utilitario;
import entidades.Roles;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import servcios.servicioRoles;


/**
 *
 * @author Diego
 */
@ManagedBean
@ViewScoped
public class controladorRoles {

    @EJB
    private servicioRoles servRoles;
    private Roles rolRoles = new Roles();
   
    private Utilitario utilitario = new Utilitario();

    public void guardar() {
        
           String str_mensaje = servRoles.guardarRoles(rolRoles);
            if (str_mensaje.isEmpty()) {
                utilitario.agregarMensaje("Se guardo correctamente", "");
                rolRoles = new Roles();
            } else {
                utilitario.agregarMensajeError("No se pudo guardar", str_mensaje);
            }
        }

    public Roles getRolRoles() {
        return rolRoles;
    }

    public void setRolRoles(Roles rolRoles) {
        this.rolRoles = rolRoles;
    }
   
}
