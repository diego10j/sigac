/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import entidades.Alumnos;
import java.math.BigInteger;
import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import servcios.servicioAlumno;

/**
 *
 * @author Diego
 */
@ManagedBean
@ApplicationScoped
public class controladorAlumno {

    @EJB
    private servicioAlumno servAlumno;

    public void guardar() {
        Alumnos alu =new Alumnos();
        alu.setAluCodigo(new Integer("1000"));
        alu.setAluNombres("EDWIN");
        System.out.println(servAlumno.guardarAluno(alu));
        
//  Alumnos alu = servAlumno.getAlumno("1000");
//        System.out.println(alu.getAluNombres());
//        String str=servAlumno.elimnarAluno("1000");
//        System.out.println("xxxx   " + str);

    }
}
