/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servcios;

import aplicacion.Utilitario;
import entidades.Alumnos;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

/**
 *
 * @author Diego
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class servicioAlumno {

    @PersistenceContext
    private EntityManager manejador;
    @Resource
    private UserTransaction utx;
    private Utilitario utilitario = new Utilitario();

    public String guardarAlumno(Alumnos alumno) {
        try {
            utx.begin();           
            if (alumno.getAluCodigo() == null) {
                //Guardo
                long lon_codigo = utilitario.getConexion().getMaximo("alumnos", "alu_codigo", 1);
                alumno.setAluCodigo(new Integer(String.valueOf(lon_codigo)));
                manejador.persist(alumno);
            } else {
                //modifica             
                manejador.merge(alumno);
            }
            manejador.flush();
            utx.commit();
        } catch (Exception e) {
            try {
                utx.rollback();
            } catch (Exception e1) {
            }
            e.printStackTrace();
            return e.getMessage();
        }
        return "";
    }

    public String elimnarAlumno(String aluCodigo) {
        try {
            utx.begin();
            Alumnos borraAlumno = manejador.find(Alumnos.class, new Integer(aluCodigo));
            manejador.remove(borraAlumno);
            utx.commit();
        } catch (Exception e) {
            try {
                utx.rollback();
            } catch (Exception e1) {
            }
            return e.getMessage();
        }
        return "";
    }

    public Alumnos getAlumno(String aluCodigo) {

        try {
            Query q = manejador.createNamedQuery("Alumnos.findByAluCodigo");
            q.setParameter("aluCodigo", new Integer(aluCodigo));
            return (Alumnos) q.getSingleResult();
        } catch (Exception e) {
        }
        return null;
    }

    public List<Alumnos> getAlumnos() {
        try {
            Query q = manejador.createNamedQuery("Alumnos.findAll");
            return q.getResultList();
        } catch (Exception e) {
        }
        return null;
    }
}
