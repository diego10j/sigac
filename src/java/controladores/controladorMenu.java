/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import aplicacion.Utilitario;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.component.menubar.Menubar;
import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.component.submenu.Submenu;

/**
 *
 * @author Diego
 */
@ManagedBean
@ViewScoped
public class controladorMenu {

    private Menubar menu = new Menubar();
    private String rolCodigo;
    private Utilitario utilitario = new Utilitario();

    public controladorMenu() {
        dibujarMenu();
    }

    public void dibujarMenu() {
        menu.getChildren().clear();
        rolCodigo = utilitario.getVariable("rolCodigo"); 
        List lis_consulta = utilitario.getConexion().consultar("SELECT pa.pan_codigo,pan_nombre,pan_path from permisos pe, pantalla pa \n"
                + "where pe.pan_Codigo = pa.pan_Codigo\n"
                + "and pe.rol_Codigo=" + rolCodigo + "\n"
                + "and pa.pan_pan_codigo is null order by pa.pan_nombre");
        for (int i = 0; i < lis_consulta.size(); i++) {
            Object[] fila = (Object[]) lis_consulta.get(i);
            Submenu sub_menu = new Submenu();
            sub_menu.setId("sub_" + fila[0]);
            sub_menu.setLabel(fila[1] + "");
            formar_menu_recursivo(sub_menu, fila[0]);
            menu.getChildren().add(sub_menu);

        }
    }

    private void formar_menu_recursivo(Submenu sub_menu, Object ide_opci) {
        List lis_consulta = utilitario.getConexion().consultar("SELECT pa.pan_codigo,pan_nombre,pan_path from permisos pe, pantalla pa \n"
                + "where pe.pan_Codigo = pa.pan_Codigo\n"
                + "and pe.rol_Codigo=" + rolCodigo + "\n"
                + "and pa.pan_pan_codigo=" + ide_opci + " order by pa.pan_nombre");

        for (int i = 0; i < lis_consulta.size(); i++) {
            Object[] fila = (Object[]) lis_consulta.get(i);
            if (tieneHijos(fila[0])) {
                Submenu sub_menu_nuevo = new Submenu();
                sub_menu_nuevo.setLabel(fila[1] + "");
                sub_menu.setId("sub_" + fila[0]);
                sub_menu.getChildren().add(sub_menu_nuevo);
                formar_menu_recursivo(sub_menu_nuevo, fila[0]);
            } else {

                MenuItem mei_item = new MenuItem();
                mei_item.setId("item_" + fila[0]);
                mei_item.setValue(fila[1]);
                mei_item.setUrl(fila[2] + "");
                sub_menu.getChildren().add(mei_item);
            }
        }
    }

    private boolean tieneHijos(Object pan_codigo) {
        List lis_consulta = utilitario.getConexion().consultar("SELECT COUNT(pan_codigo) FROM PANTALLA WHERE pan_pan_codigo =" + pan_codigo + "");
        Object fila = (Object) lis_consulta.get(0);
        int num_hijos = 0;
        try {
            num_hijos = Integer.parseInt(fila + "");
        } catch (Exception e) {
        }
        if (num_hijos == 0) {
            return false;
        } else {
            return true;
        }
    }

    public Menubar getMenu() {
        return menu;
    }

    public void setMenu(Menubar menu) {
        this.menu = menu;
    }
}
