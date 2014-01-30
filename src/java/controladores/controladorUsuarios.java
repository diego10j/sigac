/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import aplicacion.Utilitario;
import entidades.Docentes;
import entidades.Institucion;
import entidades.Roles;
import entidades.Usuario;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.event.AjaxBehaviorEvent;
import servcios.servicioDocente;
import servcios.servicioInstitucion;
import servcios.servicioRoles;
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
    @EJB
    private servicioDocente servDocente;
    private List comDocentes;
    @EJB
    private servicioRoles servRoles;
    private List comRoles;

    @PostConstruct
    public void cargarDatos() {
        listaUsuario = servUsuarios.getUsuario();
        comDocentes = servDocente.getListaDocentes();
        comRoles = servRoles.getListaRoles();
        insertar();
    }

    public void insertar() {
        usuUsuario = new Usuario();
        usuUsuario.setDocCodigo(new Docentes());
        usuUsuario.setRolCodigo(new Roles());
    }

    public void madificar() {
        if (usuUsuario.getDocCodigo() == null) {
            usuUsuario.setDocCodigo(new Docentes());
        }
        if (usuUsuario.getRolCodigo() == null) {
            usuUsuario.setRolCodigo(new Roles());
        }
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
        if (usuUsuario.getUsuCodigo() == null) {
            //Asigna el nick al password si es nuevo
            usuUsuario.setUsuClave(usuUsuario.getUsuNick());
        }      
        usuUsuario.setInsCodigo(servInstitucion.getIntitucion());
        if (usuUsuario.getDocCodigo().getDocCodigo() != null) {
            usuUsuario.setDocCodigo(servDocente.getDocente(usuUsuario.getDocCodigo().getDocCodigo().toString()));
        } else {
            usuUsuario.setDocCodigo(null);
        }       
        if (usuUsuario.getRolCodigo().getRolCodigo() != null) {
            usuUsuario.setRolCodigo(servRoles.getRoles(usuUsuario.getRolCodigo().getRolCodigo().toString()));
        }        
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

    public List getComDocentes() {
        return comDocentes;
    }

    public void setComDocentes(List comDocentes) {
        this.comDocentes = comDocentes;
    }

    public List getComRoles() {
        return comRoles;
    }

    public void setComRoles(List comRoles) {
        this.comRoles = comRoles;
    }
}
