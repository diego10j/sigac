/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import aplicacion.Utilitario;
import entidades.Permisos;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
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

    @PostConstruct
    public void cargarDatos() {
        listaPermisos = servPermisos.getPermisos();
    }
    
    public void insertar(){
        perPermisos = new Permisos();
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
        
            String str_mensaje = servPermisos.guardarPermisos(perPermisos);
            if (str_mensaje.isEmpty()) {
                utilitario.agregarMensaje("Se guardo correctamente", "");
                perPermisos = new Permisos();
                cargarDatos();
                utilitario.ejecutarJavaScript("wdlgDetalle.hide()");
            } else {
                utilitario.agregarMensajeError("No se pudo guardar", str_mensaje);
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

    
 
    
    
    
}
