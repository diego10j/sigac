/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import aplicacion.Utilitario;
import entidades.Alumnos;
import entidades.Asignaturas;
import entidades.EquivalenciaAprovechamiento;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import servcios.servicioAlumno;
import servcios.servicioAsignaturas;


/**
 *
 * @author Diego
 */
@ManagedBean
@ViewScoped
public class controladorAsignaturas {

    @EJB
    private servicioAsignaturas servAsignaturas;
    private Asignaturas asiAsignaturas = new Asignaturas();
    private Utilitario utilitario = new Utilitario();

     private List<Asignaturas> listaAsignaturas;

    @PostConstruct
    public void cargarDatos() {
        listaAsignaturas = servAsignaturas.getEquivalenciaAsignaturas();
    }
    
    public void insertar(){
        asiAsignaturas = new Asignaturas();
    }

      

public void eliminar() {
        if (asiAsignaturas.getAsiCodigo()!= null) {
            String str_mensaje = servAsignaturas.elimnarAsignaturas(asiAsignaturas.getAsiCodigo().toString());
            if (str_mensaje.isEmpty()) {
                utilitario.agregarMensaje("Se elimino correctamente", "");
                cargarDatos();
            } else {
                utilitario.agregarMensajeError("No se puede eliminar " + str_mensaje, "");
            }
        }
    }
    
    public void guardar() {
        
           String str_mensaje = servAsignaturas.guardarAsignaturas(asiAsignaturas);
            if (str_mensaje.isEmpty()) {
                utilitario.agregarMensaje("Se guardo correctamente", "");
                asiAsignaturas = new Asignaturas();
                utilitario.ejecutarJavaScript("wdlgDetalle.hide()");
            } else {
                utilitario.agregarMensajeError("No se pudo guardar", str_mensaje);
            }
        
    }

    public Asignaturas getAsiAsignaturas() {
        return asiAsignaturas;
    }

    public void setAsiAsignaturas(Asignaturas asiAsignaturas) {
        this.asiAsignaturas = asiAsignaturas;
    }

    public List<Asignaturas> getListaAsignaturas() {
        return listaAsignaturas;
    }

    public void setListaAsignaturas(List<Asignaturas> listaAsignaturas) {
        this.listaAsignaturas = listaAsignaturas;
    }
   
   
}
