/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import aplicacion.Utilitario;
import entidades.Evaluarparcial;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import servcios.servicioEvaluarparcial;
import servcios.servicioFormaevaluar;

/**
 *
 * @author Diego
 */
@ManagedBean
@ViewScoped
public class controladorEvaluarparcial {

    @EJB
    private servicioEvaluarparcial servEvaluarparcial;
    private Evaluarparcial evaParcial = new Evaluarparcial();
    private Utilitario utilitario = new Utilitario();
    private List<Evaluarparcial> listaEvaluarparcial;
    private List<Evaluarparcial> filtroEvaluarparcial;

    @PostConstruct
    public void cargarDatos() {
        listaEvaluarparcial = servEvaluarparcial.getEvaluarparcials();
    }

    public void insertar() {
        evaParcial = new Evaluarparcial();
    }

    public void eliminar() {
        if (evaParcial.getEvaCodigo() != null) {
            String str_mensaje = servEvaluarparcial.elimnarEvaluarparcial(evaParcial.getEvaCodigo().toString());
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
        if (evaParcial.getEvaCodigo() != null) {
            nuevo = false;
        }
        String str_mensaje = servEvaluarparcial.guardarEvaluarparcial(evaParcial);
        if (str_mensaje.isEmpty()) {
            utilitario.agregarMensaje("Se guardo correctamente", "");
            evaParcial = new Evaluarparcial();
            if (!nuevo) {
                cargarDatos();
                utilitario.ejecutarJavaScript("wdlgDetalle.hide()");
            }
        } else {
            utilitario.agregarMensajeError("No se pudo guardar", str_mensaje);
        }

    }

    public Evaluarparcial getEvaParcial() {
        return evaParcial;
    }

    public void setEvaParcial(Evaluarparcial evaParcial) {
        this.evaParcial = evaParcial;
    }

    public List<Evaluarparcial> getListaEvaluarparcial() {
        return listaEvaluarparcial;
    }

    public void setListaEvaluarparcial(List<Evaluarparcial> listaEvaluarparcial) {
        this.listaEvaluarparcial = listaEvaluarparcial;
    }

    public List<Evaluarparcial> getFiltroEvaluarparcial() {
        return filtroEvaluarparcial;
    }

    public void setFiltroEvaluarparcial(List<Evaluarparcial> filtroEvaluarparcial) {
        this.filtroEvaluarparcial = filtroEvaluarparcial;
    }
}
