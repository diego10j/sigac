/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import aplicacion.Utilitario;
import entidades.Institucion;
import entidades.Matricula;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import servcios.servicioMatriculas;

/**
 *
 * @author Diego
 */
@ManagedBean
@ViewScoped
public class controladorMatriculas {

    @EJB
    private servicioMatriculas servMatriculas;
    private Matricula matMatricula = new Matricula();
    private Utilitario utilitario = new Utilitario();
    private List<Matricula> listaMatriculas;
    private List<Matricula> filtroMatriculas;

    @PostConstruct
    public void cargarDatos() {
        listaMatriculas = servMatriculas.getMatriculas();
    }
    
    public void insertar(){
        matMatricula = new Matricula();
    }
    
     public void eliminar() {
        if (matMatricula.getMatCodigo() != null) {
            String str_mensaje = servMatriculas.elimnarMatriculas(matMatricula.getMatCodigo().toString());
            if (str_mensaje.isEmpty()) {
                utilitario.agregarMensaje("Se elimino correctamente", "");
                cargarDatos();
            } else {
                utilitario.agregarMensajeError("No se puede eliminar " + str_mensaje, "");
            }
        }
    }

    public void guardar() {
        
            String str_mensaje = servMatriculas.guardarMatriculas(matMatricula);
            if (str_mensaje.isEmpty()) {
                utilitario.agregarMensaje("Se guardo correctamente", "");
                matMatricula = new Matricula();
                cargarDatos();
                utilitario.ejecutarJavaScript("wdlgDetalle.hide()");
            } else {
                utilitario.agregarMensajeError("No se pudo guardar", str_mensaje);
            }
       
    }



    public Matricula getMatMatricula() {
        return matMatricula;
    }

    public void setMatMatricula(Matricula matMatricula) {
        this.matMatricula = matMatricula;
    }

    public List<Matricula> getListaMatriculas() {
        return listaMatriculas;
    }

    public void setListaMatriculas(List<Matricula> listaMatriculas) {
        this.listaMatriculas = listaMatriculas;
    }

    public List<Matricula> getFiltroMatriculas() {
        return filtroMatriculas;
    }

    public void setFiltroMatriculas(List<Matricula> filtroMatriculas) {
        this.filtroMatriculas = filtroMatriculas;
    }
    
    

    
    
   
    
    
    
}
