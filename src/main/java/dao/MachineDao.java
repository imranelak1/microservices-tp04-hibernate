package dao;
import entities.Machine;import org.hibernate.Session;import util.HibernateUtil;import java.util.List;
/** Le filtre utilise un paramètre HQL, sans concaténation SQL. */
public class MachineDao extends GenericDao<Machine>{
    public MachineDao(){super(Machine.class);}
    public List<Machine> findByMarque(String marque){try(Session s=HibernateUtil.getSessionFactory().openSession()){return s.createQuery("from Machine m where m.marque=:marque order by m.id",Machine.class).setParameter("marque",marque).list();}}
}
