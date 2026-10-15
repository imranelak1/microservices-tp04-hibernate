package services;
import dao.*;import entities.Machine;import java.util.List;
/** Façade métier : la présentation reste indépendante des sessions Hibernate. */
public class MachineService implements IDao<Machine> {
    private final MachineDao dao=new MachineDao();
    public boolean create(Machine o){return dao.create(o);} public boolean delete(Machine o){return dao.delete(o);} public boolean update(Machine o){return dao.update(o);}
    public Machine getById(int id){return dao.getById(id);} public List<Machine> getAll(){return dao.getAll();}
    public List<Machine> findByMarque(String marque){return dao.findByMarque(marque);}
}
