/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import aplicacion.Utilitario;
import entidades.Pantalla;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import servcios.servicioAdministracion;

/**
 *
 * @author Diego
 */
@ManagedBean
@ViewScoped
public class controladorAdministracion {

    private Utilitario utilitario = new Utilitario();
    @EJB
    private servicioAdministracion servAdministracion;
    //Arbol
    private TreeNode raiz;
    private TreeNode nodos;
    private TreeNode nodoSeleccionad0;
    //Tabla
    private List<Pantalla> lisPantallas;
    private Pantalla panPantalla;

    @PostConstruct
    public void cargarDatos() {
        //Construye el árbol
        Pantalla pan_raiz = new Pantalla();
        pan_raiz.setPanNombre("PANTALLAS");

        raiz = new DefaultTreeNode(null, nodos);
        nodos = new DefaultTreeNode(pan_raiz, raiz);
        List<Pantalla> lis_padres = servAdministracion.getPantallasPadre();
        if (lis_padres != null) {
            for (Pantalla actual : lis_padres) {
                TreeNode padre = new DefaultTreeNode(actual, nodos);
                formarArbolRecursivo(actual, padre);
            }
        }
        nodos.setExpanded(true);
        if (nodoSeleccionad0 == null) {
            nodoSeleccionad0 = nodos;
        }
        panPantalla = new Pantalla();
        cargarPantallas();

    }

    public void insertarPantalla() {
        panPantalla = new Pantalla();
    }

    public void eliminarPantalla() {
        if (panPantalla.getPanCodigo() != null) {
            String str_mensaje = servAdministracion.elimnarPantalla(panPantalla.getPanCodigo().toString());
            if (str_mensaje.isEmpty()) {
                utilitario.agregarMensaje("Se elimino correctamente", "");
                cargarDatos();

            } else {
                utilitario.agregarMensajeError("No se puede eliminar " + str_mensaje, "");
            }
        }
    }

    public void guardarPantalla() {
        //ins_codigo
        Pantalla pan_padre = null;
        try {
            if (nodoSeleccionad0 != null) {
                pan_padre = (Pantalla) nodoSeleccionad0.getData();
                if (pan_padre != null) {
                    panPantalla.setPanPanCodigo(servAdministracion.getPantalla(pan_padre.getPanCodigo().toString()));
                } else {
                    panPantalla.setPanPanCodigo(null);
                }
            } else {
                panPantalla.setPanPanCodigo(null);
            }
        } catch (Exception e) {
            panPantalla.setPanPanCodigo(null);
        }


        String str_mensaje = servAdministracion.guardarPantalla(panPantalla);
        if (str_mensaje.isEmpty()) {
            utilitario.agregarMensaje("Se guardo correctamente", "");
            panPantalla = new Pantalla();
            cargarDatos();
            utilitario.ejecutarJavaScript("wdlgDetalle.hide()");
        } else {
            utilitario.agregarMensajeError("No se pudo guardar", str_mensaje);
        }

    }

    public void seleccionarArbol(NodeSelectEvent evt) {
        nodoSeleccionad0 = evt.getTreeNode();
        cargarPantallas();
    }

    private void cargarPantallas() {
        if (nodoSeleccionad0 != null) {
            Pantalla pantalla = (Pantalla) nodoSeleccionad0.getData();
            if (pantalla == null || pantalla.getPanCodigo() == null) {
                lisPantallas = servAdministracion.getPantallasPadre();
            } else {
                lisPantallas = servAdministracion.getPantallasHijas(pantalla.getPanCodigo().toString());
            }
        } else {
            lisPantallas = servAdministracion.getPantallasPadre();
        }
    }

    private void formarArbolRecursivo(Pantalla pantalla, TreeNode padre) {
        if (pantalla != null) {
            List<Pantalla> lista = servAdministracion.getPantallasHijas(pantalla.getPanCodigo().toString());
            if (lista != null) {
                if (lista.isEmpty()) {
                    TreeNode aux = padre;
                    padre.getParent().getChildren().remove(padre);
                    aux = new DefaultTreeNode("document", aux.getData(), aux.getParent());
                } else {
                    for (Pantalla actual : lista) {
                        TreeNode hijo = new DefaultTreeNode(actual, padre);
                        formarArbolRecursivo(actual, hijo);
                    }
                }
            }
        }
    }

    public TreeNode getRaiz() {
        return raiz;
    }

    public void setRaiz(TreeNode raiz) {
        this.raiz = raiz;
    }

    public TreeNode getNodoSeleccionad0() {
        return nodoSeleccionad0;
    }

    public void setNodoSeleccionad0(TreeNode nodoSeleccionad0) {
        this.nodoSeleccionad0 = nodoSeleccionad0;
    }

    public TreeNode getNodos() {
        return nodos;
    }

    public void setNodos(TreeNode nodos) {
        this.nodos = nodos;
    }

    public List<Pantalla> getLisPantallas() {
        return lisPantallas;
    }

    public void setLisPantallas(List<Pantalla> lisPantallas) {
        this.lisPantallas = lisPantallas;
    }

    public Pantalla getPanPantalla() {
        return panPantalla;
    }

    public void setPanPantalla(Pantalla panPantalla) {
        this.panPantalla = panPantalla;
    }
}
