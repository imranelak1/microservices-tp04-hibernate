import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import entities.*;
import services.*;
import dao.*;
import util.HibernateUtil;

class PersistenceTest {
  SalleService salles = new SalleService();
  MachineService machines = new MachineService();

  String key() {
    return java.util.UUID.randomUUID().toString();
  }

  @AfterAll
  static void close() {
    HibernateUtil.shutdown();
  }

  @Test
  void factorySingletonEtMySql() {
    assertSame(HibernateUtil.getSessionFactory(), HibernateUtil.getSessionFactory());
    try (var s = HibernateUtil.getSessionFactory().openSession()) {
      s.doWork(c -> assertEquals("MySQL", c.getMetaData().getDatabaseProductName()));
    }
  }

  @Test
  void crudSalle() {
    Salle s = new Salle(key(), "Informatique");
    assertTrue(salles.create(s));
    assertTrue(s.getId() > 0);
    s.setLibelle("Réseau");
    assertTrue(salles.update(s));
    assertEquals("Réseau", salles.getById(s.getId()).getLibelle());
    assertTrue(salles.getAll().stream().anyMatch(x -> x.getId() == s.getId()));
    assertTrue(salles.delete(s));
    assertNull(salles.getById(s.getId()));
  }

  @Test
  void crudMachineEtHqlParametre() {
    Salle s = new Salle(key(), "TP");
    salles.create(s);
    Machine m = new Machine(key(), "Dell", s);
    assertTrue(machines.create(m));
    assertEquals(s.getId(), machines.getById(m.getId()).getSalle().getId());
    m.setMarque("HP");
    assertTrue(machines.update(m));
    assertTrue(machines.findByMarque("HP").stream().anyMatch(x -> x.getId() == m.getId()));
    assertTrue(machines.findByMarque("' or 1=1 --").isEmpty());
    assertTrue(machines.delete(m));
    assertNull(machines.getById(m.getId()));
    salles.delete(s);
  }

  @Test
  void cascadeSalleMachines() {
    Salle s = new Salle(key(), "Cascade");
    s.ajouterMachine(new Machine(key(), "Dell", s));
    assertTrue(salles.create(s));
    int id = s.getMachines().get(0).getId();
    assertNotNull(machines.getById(id));
    assertTrue(salles.delete(s));
    assertNull(machines.getById(id));
  }

  @Test
  void rollbackAnnuleToutesLesEcritures() {
    Salle existante = new Salle(key(), "Unique");
    salles.create(existante);
    long before = new SalleDao().count();
    boolean result =
        new SalleDao()
            .transaction(
                session -> {
                  session.save(new Salle(key(), "Doit disparaître"));
                  session.save(new Salle(existante.getCode(), "Doublon"));
                  session.flush();
                });
    assertFalse(result);
    assertEquals(before, new SalleDao().count());
    assertTrue(salles.create(new Salle(key(), "Après rollback")));
    salles.delete(existante);
  }
}
