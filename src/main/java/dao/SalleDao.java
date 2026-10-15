package dao;

import entities.Salle;

/** DAO des salles, basé sur les transactions communes. */
public class SalleDao extends GenericDao<Salle> {
  public SalleDao() {
    super(Salle.class);
  }
}
