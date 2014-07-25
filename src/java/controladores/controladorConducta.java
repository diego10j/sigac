/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import aplicacion.Utilitario;
import entidades.Alumnos;
import entidades.EquivalenciaConducta;
import java.util.List;
import javax.annotation.PostConstruct;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import servcios.servicioConducta;
import servcios.servicioInstitucion;


/**
 *
 * @author Diego
 */
@ManagedBean
@ViewScoped
public class controladorConducta {

    @EJB
    private servicioConducta servConducta;
    private EquivalenciaConducta eqvConducta = new EquivalenciaConducta();
   
    private Utilitario utilitario = new Utilitario();
     private List<EquivalenciaConducta> listaEquivalenciaConducta;
      

       @EJB
    private servicioInstitucion servInstitucion;
    @PostConstruct
    public void cargarDatos() {
        listaEquivalenciaConducta = servConducta.getEquivalenciaConducta();
    }

    public void insertar(){
        eqvConducta = new EquivalenciaConducta();
    }
    
public void eliminar() {
        if (eqvConducta.getEqcCodigo() != null) {
            String str_mensaje = servConducta.elimnarConducta(eqvConducta.getEqcCodigo().toString());
            if (str_mensaje.isEmpty()) {
                utilitario.agregarMensaje("Se elimino correctamente", "");
                cargarDatos();
            } else {
                utilitario.agregarMensajeError("No se puede eliminar " + str_mensaje, "");
            }
        }
    }
    
    public void guardar() {
         eqvConducta.setInsCodigo(servInstitucion.getIntitucion());
           String str_mensaje = servConducta.guardarConducta(eqvConducta);
            if (str_mensaje.isEmpty()) {
                utilitario.agregarMensaje("Se guardo correctamente", "");
                eqvConducta= new EquivalenciaConducta();
                cargarDatos();
                utilitario.ejecutarJavaScript("wdlgDetalle.hide()");
            } else {
                utilitario.agregarMensajeError("No se pudo guardar", str_mensaje);
            }
        
    }

    public EquivalenciaConducta getEqvConducta() {
        return eqvConducta;
    }

    public void setEqvConducta(EquivalenciaConducta eqvConducta) {
        this.eqvConducta = eqvConducta;
    }

    public List<EquivalenciaConducta> getListaEquivalenciaConducta() {
        return listaEquivalenciaConducta;
    }

    public void setListaEquivalenciaConducta(List<EquivalenciaConducta> listaEquivalenciaConducta) {
        this.listaEquivalenciaConducta = listaEquivalenciaConducta;
    }

 
   
  
   
}
