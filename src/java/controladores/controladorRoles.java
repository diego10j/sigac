/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import aplicacion.Utilitario;
import entidades.Alumnos;
import entidades.Institucion;
import entidades.Roles;
import java.util.List;
import javax.annotation.PostConstruct;
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
     private List<Roles> listaRoles;
     private List<Roles> filtroRoles;

    @PostConstruct
    public void cargarDatos() {
        listaRoles = servRoles.getRoles();
    }
    
    public void insertar(){
      rolRoles = new Roles();
    }
    
    public void eliminar() {
        if (rolRoles.getRolCodigo() != null) {
            String str_mensaje = servRoles.elimnarRoles(rolRoles.getRolCodigo().toString());
            if (str_mensaje.isEmpty()) {
                utilitario.agregarMensaje("Se elimino correctamente", "");
                cargarDatos();
            } else {
                utilitario.agregarMensajeError("No se puede eliminar " + str_mensaje, "");
            }
        }
    }
    
    public void guardar() {
        
           String str_mensaje = servRoles.guardarRoles(rolRoles);
            if (str_mensaje.isEmpty()) {
                utilitario.agregarMensaje("Se guardo correctamente", "");
                rolRoles = new Roles();
                cargarDatos();
                utilitario.ejecutarJavaScript("wdlgDetalle.hide()");
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

    public List<Roles> getListaRoles() {
        return listaRoles;
    }

    public void setListaRoles(List<Roles> listaRoles) {
        this.listaRoles = listaRoles;
    }

    public List<Roles> getFiltroRoles() {
        return filtroRoles;
    }

    public void setFiltroRoles(List<Roles> filtroRoles) {
        this.filtroRoles = filtroRoles;
    }
   
}
