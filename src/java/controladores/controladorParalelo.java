/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import aplicacion.Utilitario;
import entidades.Alumnos;

import entidades.Paralelo;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import servcios.servicioParalelo;

/**
 *
 * @author Diego
 */
@ManagedBean
@ViewScoped
public class controladorParalelo {

    
     @EJB
    private servicioParalelo servParalelo;
    private Paralelo parParalelo = new Paralelo();
    private Utilitario utilitario = new Utilitario();
     private List<Paralelo> listaParalelo;

    @PostConstruct
    public void cargarDatos() {
        listaParalelo = servParalelo.getParalelo();
    }

    public void insertar(){
        parParalelo = new Paralelo();
    }
    
   public void eliminar() {
        if (parParalelo.getParCodigo() != null) {
            String str_mensaje = servParalelo.elimnarParalelo(parParalelo.getParCodigo().toString());
            if (str_mensaje.isEmpty()) {
                utilitario.agregarMensaje("Se elimino correctamente", "");
                cargarDatos();
            } else {
                utilitario.agregarMensajeError("No se puede eliminar " + str_mensaje, "");
            }
        }
    }
    
    public void guardar() {
        
            String str_mensaje = servParalelo.guardarParalelo(parParalelo);
            
            if (str_mensaje.isEmpty()) {
                utilitario.agregarMensaje("Se guardo correctamente", "");
                parParalelo = new Paralelo();
               } 
             
            else {
                utilitario.agregarMensajeError("No se pudo guardar", str_mensaje);
            }
       }
  

    public Paralelo getParParalelo() {
        return parParalelo;
    }

    public void setParParalelo(Paralelo parParalelo) {
        this.parParalelo = parParalelo;
    }

    public List<Paralelo> getListaParalelo() {
        return listaParalelo;
    }

    public void setListaParalelo(List<Paralelo> listaParalelo) {
        this.listaParalelo = listaParalelo;
    }
   
}
