package services;

import dao.*;
import entities.Salle;
import java.util.List;

/** Façade métier : la présentation reste indépendante des sessions Hibernate. */
public class SalleService implements IDao<Salle> {
  private final SalleDao dao = new SalleDao();

  public boolean create(Salle o) {
    return dao.create(o);
  }

  public boolean delete(Salle o) {
    return dao.delete(o);
  }

  public boolean update(Salle o) {
    return dao.update(o);
  }

  public Salle getById(int id) {
    return dao.getById(id);
  }

  public List<Salle> getAll() {
    return dao.getAll();
  }
}
