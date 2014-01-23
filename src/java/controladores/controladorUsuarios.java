/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import aplicacion.Utilitario;
import entidades.Institucion;
import entidades.Usuario;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import servcios.servicioInstitucion;
import servcios.servicioUsuarios;


/**
 *
 * @author Diego
 */
@ManagedBean
@ViewScoped
public class controladorUsuarios {

    @EJB
    private servicioUsuarios servUsuarios;
    private Usuario usuUsuario = new Usuario();
    private Utilitario utilitario = new Utilitario();
      private List<Usuario> listaUsuario;
      private List<Usuario> filtroUsuarios;
   @EJB
    private servicioInstitucion servInstitucion;
      
    @PostConstruct
    public void cargarDatos() {
        listaUsuario = servUsuarios.getUsuario();
    }
    
    public void insertar(){
        usuUsuario = new Usuario();
    }
    
     public void eliminar() {
        if (usuUsuario.getUsuCodigo() != null) {
            String str_mensaje = servUsuarios.elimnarUsuarios(usuUsuario.getUsuCodigo().toString());
            if (str_mensaje.isEmpty()) {
                utilitario.agregarMensaje("Se elimino correctamente", "");
                cargarDatos();
            } else {
                utilitario.agregarMensajeError("No se puede eliminar " + str_mensaje, "");
            }
        }
    }
     
    public void guardar() {
            usuUsuario.setInsCodigo(servInstitucion.getIntitucion());
           String str_mensaje = servUsuarios.guardarUsuarios(usuUsuario);
            if (str_mensaje.isEmpty()) {
                utilitario.agregarMensaje("Se guardo correctamente", "");
                usuUsuario = new Usuario();
                cargarDatos();
                utilitario.ejecutarJavaScript("wdlgDetalle.hide()");
            } else {
                utilitario.agregarMensajeError("No se pudo guardar", str_mensaje);
            }
        }

    public Usuario getUsuUsuario() {
        return usuUsuario;
    }

    public void setUsuUsuario(Usuario usuUsuario) {
        this.usuUsuario = usuUsuario;
    }

    public List<Usuario> getListaUsuario() {
        return listaUsuario;
    }

    public void setListaUsuario(List<Usuario> listaUsuario) {
        this.listaUsuario = listaUsuario;
    }

    public List<Usuario> getFiltroUsuarios() {
        return filtroUsuarios;
    }

    public void setFiltroUsuarios(List<Usuario> filtroUsuarios) {
        this.filtroUsuarios = filtroUsuarios;
    }
    
    
   }
