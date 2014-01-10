/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import aplicacion.Utilitario;
import entidades.Cursos;
import entidades.EquivalenciaAprovechamiento;
import entidades.Institucion;


import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import servcios.servicioAprovechamiento;
import servcios.servicioInstitucion;


/**
 *
 * @author Diego
 */
@ManagedBean
@ViewScoped
public class controladorAprovechamiento {

    @EJB
    private servicioAprovechamiento servAprovechamiento;
    private EquivalenciaAprovechamiento eqvAprovechamiento = new EquivalenciaAprovechamiento();
    @EJB
    private servicioInstitucion servInstitucion;
    private Institucion insInstitucion = new Institucion();
   
    private Utilitario utilitario = new Utilitario();

      public void guardar() {
        
           String str_mensaje = servAprovechamiento.guardarAprovechamiento(eqvAprovechamiento);
            if (str_mensaje.isEmpty()) {
                utilitario.agregarMensaje("Se guardo correctamente", "");
                eqvAprovechamiento = new EquivalenciaAprovechamiento();
            } else {
                utilitario.agregarMensajeError("No se pudo guardar", str_mensaje);
            }
        
    }

    public EquivalenciaAprovechamiento getEqvAprovechamiento() {
        return eqvAprovechamiento;
    }

    public void setEqvAprovechamiento(EquivalenciaAprovechamiento eqvAprovechamiento) {
        this.eqvAprovechamiento = eqvAprovechamiento;
    }
   
   
   
}
