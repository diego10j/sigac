/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import aplicacion.Utilitario;
import entidades.Docentes;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import servcios.servicioDocente;

/**
 *
 * @author Diego
 */
@ManagedBean
@ViewScoped
public class controladorDocente {

    @EJB
    private servicioDocente servDocente;
    private Docentes docDocente = new Docentes();
    private Utilitario utilitario = new Utilitario();

     private List<Docentes> listaDocentes;

    @PostConstruct
    public void cargarDatos() {
        listaDocentes = servDocente.getDocentes();
    }

    public void insertar(){
        docDocente = new Docentes();
    }
    
     public void eliminar() {
        if (docDocente.getDocCodigo() != null) {
            String str_mensaje = servDocente.elimnarDocente(docDocente.getDocCodigo().toString());
            if (str_mensaje.isEmpty()) {
                utilitario.agregarMensaje("Se elimino correctamente", "");
                cargarDatos();
            } else {
                utilitario.agregarMensajeError("No se puede eliminar " + str_mensaje, "");
            }
        }
    }
    
    public void guardar() {
        if (utilitario.validarCedula(docDocente.getDocCedula())) {
            String str_mensaje = servDocente.guardarDocente(docDocente);
            if (str_mensaje.isEmpty()) {
                utilitario.agregarMensaje("Se guardo correctamente", "");
                docDocente = new Docentes();
            } else {
                utilitario.agregarMensajeError("No se pudo guardar", str_mensaje);
            }
        } else {
            utilitario.agregarMensajeInfo("La cédula ingresada no es válida", "");
        }
    }

    public Docentes getDocDocente() {
        return docDocente;
    }

    

    public void setDocDocente(Docentes docDocente) {
        this.docDocente = docDocente;
    }

    public List<Docentes> getListaDocentes() {
        return listaDocentes;
    }

    public void setListaDocentes(List<Docentes> listaDocentes) {
        this.listaDocentes = listaDocentes;
    }
    
}
