package test;

import entities.*;
import services.*;
import util.HibernateUtil;

/** Démonstration CRUD sur le serveur MySQL configuré. */
public class MainTest {
  public static void main(String[] args) {
    try {
      try (var s = HibernateUtil.getSessionFactory().openSession()) {
        s.doWork(
            c ->
                System.out.println(
                    "BASE REELLE : "
                        + c.getMetaData().getDatabaseProductName()
                        + " "
                        + c.getMetaData().getDatabaseProductVersion()));
      }
      var salles = new SalleService();
      var machines = new MachineService();
      String key = Long.toString(System.currentTimeMillis());
      Salle salle = new Salle("S1-" + key, "Salle Informatique");
      if (!salles.create(salle)) throw new IllegalStateException("Création salle échouée");
      Machine machine = new Machine("M-101-" + key, "Dell", salle);
      if (!machines.create(machine)) throw new IllegalStateException("Création machine échouée");
      System.out.println("Données insérées avec succès !");
      System.out.println(salles.getById(salle.getId()));
      System.out.println(machines.getById(machine.getId()));
      machine.setMarque("Lenovo");
      machines.update(machine);
      System.out.println("HQL par marque Lenovo : " + machines.findByMarque("Lenovo"));
      machines.delete(machine);
      salles.delete(salle);
      System.out.println("Suppression vérifiée : " + (machines.getById(machine.getId()) == null));
    } finally {
      HibernateUtil.shutdown();
    }
  }
}
