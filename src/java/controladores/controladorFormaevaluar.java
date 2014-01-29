/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import aplicacion.Utilitario;
import entidades.Docentes;
import entidades.Formaevaluar;
import entidades.Matricula;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import servcios.servicioFormaevaluar;

/**
 *
 * @author Diego
 */
@ManagedBean
@ViewScoped
public class controladorFormaevaluar {

    @EJB
    private servicioFormaevaluar servFormaevaluar;
       private Formaevaluar forEvaluar = new Formaevaluar();
    private Utilitario utilitario = new Utilitario();
    private List<Formaevaluar> listaFormaevaluar;
    private List<Formaevaluar> filtroFormaevaluar;

    @PostConstruct
    public void cargarDatos() {
        listaFormaevaluar = servFormaevaluar.getFormaevaluars();
    }
    
    public void insertar(){
        forEvaluar = new Formaevaluar();
    }
    
     public void eliminar() {
        if (forEvaluar.getForCodigo() != null) {
            String str_mensaje = servFormaevaluar.elimnarFormaevaluar(forEvaluar.getForCodigo().toString());
            if (str_mensaje.isEmpty()) {
                utilitario.agregarMensaje("Se elimino correctamente", "");
                cargarDatos();
            } else {
                utilitario.agregarMensajeError("No se puede eliminar " + str_mensaje, "");
            }
        }
    }

    public void guardar() {
        
            String str_mensaje = servFormaevaluar.guardarFormaevaluar(forEvaluar);
            if (str_mensaje.isEmpty()) {
                utilitario.agregarMensaje("Se guardo correctamente", "");
                forEvaluar = new Formaevaluar();
                cargarDatos();
                utilitario.ejecutarJavaScript("wdlgDetalle.hide()");
            } else {
                utilitario.agregarMensajeError("No se pudo guardar", str_mensaje);
            }
       
    }

    public Formaevaluar getForEvaluar() {
        return forEvaluar;
    }

    public void setForEvaluar(Formaevaluar forEvaluar) {
        this.forEvaluar = forEvaluar;
    }

    public List<Formaevaluar> getListaFormaevaluar() {
        return listaFormaevaluar;
    }

    public void setListaFormaevaluar(List<Formaevaluar> listaFormaevaluar) {
        this.listaFormaevaluar = listaFormaevaluar;
    }

    public List<Formaevaluar> getFiltroFormaevaluar() {
        return filtroFormaevaluar;
    }

    public void setFiltroFormaevaluar(List<Formaevaluar> filtroFormaevaluar) {
        this.filtroFormaevaluar = filtroFormaevaluar;
    }

 
    
}
