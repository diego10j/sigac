/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import aplicacion.Utilitario;
import entidades.Tipoasignaturas;
import entidades.Asignaturas;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import servcios.servicioTipoAsignaturas;
import servcios.servicioAsignaturas;


/**
 *
 * @author Diego
 */
@ManagedBean
@ViewScoped
public class controladorTipoAsignaturas {

    @EJB
    private servicioTipoAsignaturas servTipoAsignaturas;
    private Tipoasignaturas tipoasignaturas = new Tipoasignaturas();
    private Utilitario utilitario = new Utilitario();

     private List<Tipoasignaturas> listaTipoasignaturas;

    @PostConstruct
    public void cargarDatos() {
        listaTipoasignaturas = servTipoAsignaturas.getTipoasignaturas();
    }


    public void guardar() {
        
           String str_mensaje = servTipoAsignaturas.guardarTipoAsignaturas(tipoasignaturas);
            if (str_mensaje.isEmpty()) {
                utilitario.agregarMensaje("Se guardo correctamente", "");
                tipoasignaturas = new Tipoasignaturas();
            } else {
                utilitario.agregarMensajeError("No se pudo guardar", str_mensaje);
            }
        
    }

    public Tipoasignaturas getTipoasignaturas() {
        return tipoasignaturas;
    }

    public void setTipoasignaturas(Tipoasignaturas tipoasignaturas) {
        this.tipoasignaturas = tipoasignaturas;
    }

    public List<Tipoasignaturas> getListaTipoasignaturas() {
        return listaTipoasignaturas;
    }

    public void setListaTipoasignaturas(List<Tipoasignaturas> listaTipoasignaturas) {
        this.listaTipoasignaturas = listaTipoasignaturas;
    }
   
   
}
