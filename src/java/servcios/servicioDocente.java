/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servcios;

import aplicacion.Utilitario;
import entidades.Alumnos;
import entidades.Docentes;
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
public class servicioDocente {

    @PersistenceContext
    private EntityManager manejador;
    @Resource
    private UserTransaction utx;
    
    private Utilitario utilitario=new Utilitario();

    public String guardarDocente(Docentes docente) {
        try {
            utx.begin();
              manejador.joinTransaction();
            //nombre tabla y atributo
              if(docente.getDocCodigo()== null){
            long lon_codigo=utilitario.getConexion().getMaximo("docentes", "doc_codigo", 1);
            System.out.println("ide "+lon_codigo);
            docente.setDocCodigo(new Integer(String.valueOf(lon_codigo)));
            manejador.persist(docente);
              }else{
                  manejador.merge(docente);
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

    public String elimnarDocente(String docCodigo) {
        try {
            utx.begin();
            Docentes borraDocente = manejador.find(Docentes.class, new Integer(docCodigo));
            manejador.remove(borraDocente);
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

    public Alumnos getDocente(String aluCodigo) {

        try {
            Query q = manejador.createNamedQuery("Docentes.findByDocCodigo");
            q.setParameter("docCodigo", new Integer(aluCodigo));
            return (Alumnos) q.getSingleResult();
        } catch (Exception e) {
        }
        return null;
    }
    
      public List<Docentes> getDocentes() {        
        try {
            Query q = manejador.createNamedQuery("Docentes.findAll");
            return q.getResultList();
        } catch (Exception e) {
        }        
        return null;
    }
}
