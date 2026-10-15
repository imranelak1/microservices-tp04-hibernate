package dao;

import java.util.List;

/** Contrat CRUD commun aux salles et aux machines. */
public interface IDao<T> {
  boolean create(T o);

  boolean delete(T o);

  boolean update(T o);

  T getById(int id);

  List<T> getAll();
}
