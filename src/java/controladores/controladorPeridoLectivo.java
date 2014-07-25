/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import aplicacion.Utilitario;
import entidades.PeriodoLectivo;
import java.util.List;
import javax.annotation.PostConstruct;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import servcios.servicioInstitucion;
import servcios.servicioPeriodo;


/**
 *
 * @author Diego
 */
@ManagedBean
@ViewScoped
public class controladorPeridoLectivo {

    @EJB
    private servicioPeriodo servPeriodo;
    private PeriodoLectivo perPeriodo = new PeriodoLectivo();
   
    private Utilitario utilitario = new Utilitario();
    private List<PeriodoLectivo> listaPeriodoLectivo;
    private List<PeriodoLectivo> filtroPeriodoLectivo;
     @EJB
    private servicioInstitucion servInstitucion;

    @PostConstruct
    public void cargarDatos() {
        listaPeriodoLectivo = servPeriodo.getPeriodoLectivo();
        filtroPeriodoLectivo=null;
    }
    
    public void insertar(){
        perPeriodo = new PeriodoLectivo();
    }
  

   public void eliminar() {
        if (perPeriodo.getPerCodigo() != null) {
            String str_mensaje = servPeriodo.elimnarPeriodoLectivo(perPeriodo.getPerCodigo().toString());
            if (str_mensaje.isEmpty()) {
                utilitario.agregarMensaje("Se elimino correctamente", "");
                cargarDatos();
                
                } else {
                utilitario.agregarMensajeError("No se puede eliminar " + str_mensaje, "");
            }
        }
    }
    
    public void guardar() {
           perPeriodo.setInsCodigo(servInstitucion.getIntitucion());
           String str_mensaje = servPeriodo.guardarPeriodoLectivo(perPeriodo);
            if (str_mensaje.isEmpty()) {
                utilitario.agregarMensaje("Se guardo correctamente", "");
                perPeriodo = new PeriodoLectivo();
                cargarDatos();
                utilitario.ejecutarJavaScript("wdlgDetalle.hide()");
            } else {
                utilitario.agregarMensajeError("No se pudo guardar", str_mensaje);
            }
        
    }

    public PeriodoLectivo getPerPeriodo() {
        return perPeriodo;
    }

    public void setPerPeriodo(PeriodoLectivo perPeriodo) {
        this.perPeriodo = perPeriodo;
    }

    public List<PeriodoLectivo> getFiltroPeriodoLectivo() {
        return filtroPeriodoLectivo;
    }

    public void setFiltroPeriodoLectivo(List<PeriodoLectivo> filtroPeriodoLectivo) {
        this.filtroPeriodoLectivo = filtroPeriodoLectivo;
    }

    public List<PeriodoLectivo> getListaPeriodoLectivo() {
        return listaPeriodoLectivo;
    }

    public void setListaPeriodoLectivo(List<PeriodoLectivo> listaPeriodoLectivo) {
        this.listaPeriodoLectivo = listaPeriodoLectivo;
    }
  
   
}
