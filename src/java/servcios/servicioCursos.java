/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servcios;

import aplicacion.Utilitario;
import entidades.Alumnos;
import entidades.Docentes;
import entidades.Cursos;
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
public class servicioCursos {

    @PersistenceContext
    private EntityManager manejador;
    @Resource
    private UserTransaction utx;
    
    private Utilitario utilitario=new Utilitario();

    public String guardarCursos(Cursos cursos) {
        try {
            utx.begin();
            //nombre tabla y atributo
            long lon_codigo=utilitario.getConexion().getMaximo("Cursos", "cur_codigo", 1);
             cursos.setCurCodigo(new Integer(String.valueOf(lon_codigo)));
            System.out.println("ide "+lon_codigo);
            cursos.setCurCodigo(new Integer(String.valueOf(lon_codigo)));
            manejador.joinTransaction();
            manejador.persist(cursos);
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

    public String elimnarCursos(String curCodigo) {
        try {
            utx.begin();
            Cursos borraCurso = manejador.find(Cursos.class, new Integer(curCodigo));
            manejador.remove(borraCurso);
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

    public Cursos getCursos(String curCodigo) {

        try {
            Query q = manejador.createNamedQuery("Cursos.findByDocCodigo");
            q.setParameter("curCodigo", new Integer(curCodigo));
            return (Cursos) q.getSingleResult();
        } catch (Exception e) {
        }
        return null;
    }
}
