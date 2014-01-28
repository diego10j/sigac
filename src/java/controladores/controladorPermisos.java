/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import aplicacion.Utilitario;
import entidades.Pantalla;
import entidades.Permisos;
import entidades.Roles;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.event.SelectEvent;
import servcios.servicioAdministracion;
import servcios.servicioPermisos;

/**
 *
 * @author Diego
 */
@ManagedBean
@ViewScoped
public class controladorPermisos {
    
    @EJB
    private servicioPermisos servPermisos;
    private Permisos perPermisos = new Permisos();
    private Utilitario utilitario = new Utilitario();
    private List<Permisos> listaPermisos;
    private List<Permisos> filtroPermisos;
    private List<Roles> lisRoles;
    private Roles rolSeleccionado;
    private List comPantallas;
    @EJB
    private servicioAdministracion servAdministracion;
    
    @PostConstruct
    public void cargarDatos() {
        comPantallas = servAdministracion.getListaPantallas();
        lisRoles = servAdministracion.getRoles();
        //selecciona el primer perfil si hay
        if (rolSeleccionado == null) {
            if (lisRoles != null && lisRoles.isEmpty() == false) {
                rolSeleccionado = lisRoles.get(0);
            }
        }
        if (rolSeleccionado == null) {
            listaPermisos = servPermisos.getPermisosRol("-1");
        } else {
            listaPermisos = servPermisos.getPermisosRol(rolSeleccionado.getRolCodigo().toString());
        }
        
        perPermisos = new Permisos();
        perPermisos.setPanCodigo(new Pantalla());
        
    }
    
    public void seleccionarRol(SelectEvent evt) {
        rolSeleccionado = (Roles) evt.getObject();
        if (rolSeleccionado == null) {
            listaPermisos = servPermisos.getPermisosRol("-1");
        } else {
            listaPermisos = servPermisos.getPermisosRol(rolSeleccionado.getRolCodigo().toString());
        }
    }
    
    public void insertar() {
        perPermisos = new Permisos();
        perPermisos.setPanCodigo(new Pantalla());
    }
    
    public void eliminar() {
        if (perPermisos.getPrmCodigo() != null) {
            String str_mensaje = servPermisos.elimnarPermisos(perPermisos.getPrmCodigo().toString());
            if (str_mensaje.isEmpty()) {
                utilitario.agregarMensaje("Se elimino correctamente", "");
                cargarDatos();
            } else {
                utilitario.agregarMensajeError("No se puede eliminar " + str_mensaje, "");
            }
        }
    }
    
    public void guardar() {
        if (rolSeleccionado != null) {
            perPermisos.setRolCodigo(servAdministracion.getRol(rolSeleccionado.getRolCodigo().toString()));
            perPermisos.setPanCodigo(servAdministracion.getPantalla(perPermisos.getPanCodigo().getPanCodigo().toString()));
            String str_mensaje = servPermisos.guardarPermisos(perPermisos);
            if (str_mensaje.isEmpty()) {
                utilitario.agregarMensaje("Se guardo correctamente", "");
                perPermisos = new Permisos();
                cargarDatos();
                utilitario.ejecutarJavaScript("wdlgPermiso.hide()");
            } else {
                utilitario.agregarMensajeError("No se pudo guardar", str_mensaje);
            }
        } else {
            utilitario.agregarMensaje("Debe seleccionar un Rol/Perfil", "");
        }
    }
    
    public servicioPermisos getServPermisos() {
        return servPermisos;
    }
    
    public void setServPermisos(servicioPermisos servPermisos) {
        this.servPermisos = servPermisos;
    }
    
    public Permisos getPerPermisos() {
        return perPermisos;
    }
    
    public void setPerPermisos(Permisos perPermisos) {
        this.perPermisos = perPermisos;
    }
    
    public List<Permisos> getListaPermisos() {
        return listaPermisos;
    }
    
    public void setListaPermisos(List<Permisos> listaPermisos) {
        this.listaPermisos = listaPermisos;
    }
    
    public List<Permisos> getFiltroPermisos() {
        return filtroPermisos;
    }
    
    public void setFiltroPermisos(List<Permisos> filtroPermisos) {
        this.filtroPermisos = filtroPermisos;
    }
    
    public List getComPantallas() {
        return comPantallas;
    }
    
    public void setComPantallas(List comPantallas) {
        this.comPantallas = comPantallas;
    }
    
    public List<Roles> getLisRoles() {
        return lisRoles;
    }
    
    public void setLisRoles(List<Roles> lisRoles) {
        this.lisRoles = lisRoles;
    }
    
    public Roles getRolSeleccionado() {
        return rolSeleccionado;
    }
    
    public void setRolSeleccionado(Roles rolSeleccionado) {
        this.rolSeleccionado = rolSeleccionado;
    }
}
