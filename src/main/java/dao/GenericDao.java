package dao;

import org.hibernate.*;
import util.HibernateUtil;
import java.util.*;
import java.util.function.Consumer;

/** Les erreurs d'écriture annulent la transaction avant la fermeture de la session. */
public abstract class GenericDao<T> implements IDao<T> {
  private final Class<T> type;

  protected GenericDao(Class<T> type) {
    this.type = type;
  }

  public boolean transaction(Consumer<Session> work) {
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      Transaction tx = session.beginTransaction();
      try {
        work.accept(session);
        tx.commit();
        return true;
      } catch (RuntimeException error) {
        if (tx.isActive()) tx.rollback();
        System.err.println(
            "ROLLBACK : " + error.getClass().getSimpleName() + " — " + error.getMessage());
        return false;
      }
    }
  }

  public boolean create(T o) {
    return transaction(s -> s.save(o));
  }

  public boolean update(T o) {
    return transaction(s -> s.merge(o));
  }

  public boolean delete(T o) {
    return transaction(s -> s.delete(s.merge(o)));
  }

  public T getById(int id) {
    try (Session s = HibernateUtil.getSessionFactory().openSession()) {
      return s.get(type, id);
    }
  }

  public List<T> getAll() {
    try (Session s = HibernateUtil.getSessionFactory().openSession()) {
      return s.createQuery("from " + type.getSimpleName() + " e order by e.id", type).list();
    }
  }

  public List<T> findAll() {
    return getAll();
  }

  public long count() {
    try (Session s = HibernateUtil.getSessionFactory().openSession()) {
      return s.createQuery("select count(e) from " + type.getSimpleName() + " e", Long.class)
          .uniqueResult();
    }
  }
}
