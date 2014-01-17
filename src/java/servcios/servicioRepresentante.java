/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servcios;

import aplicacion.Utilitario;
import entidades.Alumnos;
import entidades.Docentes;
import entidades.Paralelo;
import entidades.Representante;
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
public class servicioRepresentante {

    @PersistenceContext
    private EntityManager manejador;
    @Resource
    private UserTransaction utx;
    private Utilitario utilitario = new Utilitario();

    public String guardarRepresentante(Representante representante) {
        try {
            utx.begin();
            manejador.joinTransaction();
            //nombre tabla y atributo
            if (representante.getRepCodigo() == null) {
                long lon_codigo = utilitario.getConexion().getMaximo("representante", "rep_codigo", 1);                
                representante.setRepCodigo(new Integer(String.valueOf(lon_codigo)));
                manejador.persist(representante);
            } else {
                manejador.merge(representante);
            }

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

    public String elimnarRepresentante(String repCodigo) {
        try {
            utx.begin();
            Representante borraRepresentante = manejador.find(Representante.class, new Integer(repCodigo));
            manejador.remove(borraRepresentante);
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

    public Representante getRepresentante(String repCodigo) {

        try {
            Query q = manejador.createNamedQuery("Representante.findByDocCodigo");
            q.setParameter("repCodigo", new Integer(repCodigo));
            return (Representante) q.getSingleResult();
        } catch (Exception e) {
        }
        return null;
    }

    public List<Representante> getRepresentante() {
        try {
            Query q = manejador.createNamedQuery("Representante.findAll");
            return q.getResultList();
        } catch (Exception e) {
        }
        return null;
    }
}
