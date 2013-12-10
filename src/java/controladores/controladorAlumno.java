/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import entidades.Alumnos;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import servcios.servicioAlumno;

/**
 *
 * @author Diego
 */
@ManagedBean
@ViewScoped
public class controladorAlumno {

    @EJB
    private servicioAlumno servAlumno;

    public void guardar() {
        System.out.println("ENTRO AL GUARDAR");

    }
}
