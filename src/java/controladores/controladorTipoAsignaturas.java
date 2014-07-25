/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import aplicacion.Utilitario;
import entidades.Institucion;

import entidades.Tipoasignaturas;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import servcios.servicioTipoAsignaturas;

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
    private List<Tipoasignaturas> filtroTipoasignaturas;

    @PostConstruct
    public void cargarDatos() {
        listaTipoasignaturas = servTipoAsignaturas.getTipoasignaturas();
    }

    public void insertar() {
        tipoasignaturas = new Tipoasignaturas();
    }

    public void eliminar() {
        if (tipoasignaturas.getTipCodigo() != null) {
            String str_mensaje = servTipoAsignaturas.elimnarTipoAsignaturas(tipoasignaturas.getTipCodigo().toString());
            if (str_mensaje.isEmpty()) {
                utilitario.agregarMensaje("Se elimino correctamente", "");
                cargarDatos();
            } else {
                utilitario.agregarMensajeError("No se puede eliminar " + str_mensaje, "");
            }
        }
    }

    public void guardar() {

        boolean nuevo = true;
        if (tipoasignaturas.getTipCodigo() != null) {
            nuevo = false;
        }
        String str_mensaje = servTipoAsignaturas.guardarTipoAsignaturas(tipoasignaturas);
        if (str_mensaje.isEmpty()) {
            utilitario.agregarMensaje("Se guardo correctamente", "");
            tipoasignaturas = new Tipoasignaturas();
            if (!nuevo) {
                cargarDatos();
                utilitario.ejecutarJavaScript("wdlgDetalle.hide()");
            }
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

    public List<Tipoasignaturas> getFiltroTipoasignaturas() {
        return filtroTipoasignaturas;
    }

    public void setFiltroTipoasignaturas(List<Tipoasignaturas> filtroTipoasignaturas) {
        this.filtroTipoasignaturas = filtroTipoasignaturas;
    }
}
