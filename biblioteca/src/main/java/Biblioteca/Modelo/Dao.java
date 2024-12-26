package Biblioteca.Modelo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.List;

public class Dao <T, ID> {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("Biblioteca");
    EntityManager em = emf.createEntityManager();
    EntityTransaction tx = em.getTransaction();

    private Class <T> clase;
    private Class <ID> id;

    public Dao(Class <T> clase, Class <ID> id) {
        this.clase = clase;
        this.id = id;
    }

    public void insert (T t) {
        tx.begin();
        em.persist(t);
        tx.commit();
    }

    public void update (T t) {
        tx.begin();
        em.merge(t);
        tx.commit();
    }

    public void delete (T t) {
        tx.begin();
        em.remove(t);
        tx.commit();
    }

    public T findUnique(String columna, String valor) {
        return em.createQuery("SELECT e FROM " + clase.getSimpleName() + " e WHERE " + columna + " =  \"" + valor +"\"", clase).getSingleResult();
    }

    public List<T> findAllWhere(String columna, String valor){
        return em.createQuery("SELECT e FROM " + clase.getSimpleName() + " e WHERE " + columna + " =  \"" + valor +"\"", clase).getResultList();
    }
    public List<T> findAllWhere(String columna, Integer valor){
        return em.createQuery("SELECT e FROM " +clase.getSimpleName()+" e WHERE "+ columna + " = " + valor + "", clase).getResultList();
    }

    public T findById(ID id) {
        return em.find(clase, id);
    }

    public List<T> findAll() {
        return em.createQuery("from " + clase.getSimpleName(), clase).getResultList();
    }
}
