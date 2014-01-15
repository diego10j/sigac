/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import aplicacion.Utilitario;
import entidades.Alumnos;
import entidades.Institucion;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import servcios.servicioInstitucion;

/**
 *
 * @author Diego
 */
@ManagedBean
@ViewScoped
public class controladorInstitucion {

    @EJB
    private servicioInstitucion servInstitucion;
    private Institucion insInstitucion = new Institucion();
    private Utilitario utilitario = new Utilitario();
    private List<Institucion> listaInstitucion;

    @PostConstruct
    public void cargarDatos() {
        listaInstitucion = servInstitucion.getInstitucion();
    }
    
    public void insertar(){
        insInstitucion = new Institucion();
    }

    public void guardar() {
        
            String str_mensaje = servInstitucion.guardarInstitucion(insInstitucion);
            if (str_mensaje.isEmpty()) {
                utilitario.agregarMensaje("Se guardo correctamente", "");
                insInstitucion = new Institucion();
            } else {
                utilitario.agregarMensajeError("No se pudo guardar", str_mensaje);
            }
       
    }

    public Institucion getInsInstitucion() {
        return insInstitucion;
    }

    public void setInsInstitucion(Institucion insInstitucion) {
        this.insInstitucion = insInstitucion;
    }

    public List<Institucion> getListaInstitucion() {
        return listaInstitucion;
    }

    public void setListaInstitucion(List<Institucion> listaInstitucion) {
        this.listaInstitucion = listaInstitucion;
    }
    
    
}
