package entities;
import javax.persistence.*;
/** Une machine appartient à une salle par la clé étrangère salle_id. */
@Entity @Table(name="machines")
public class Machine {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private int id;
    @Column(nullable=false,unique=true) private String ref;
    @Column(nullable=false) private String marque;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="salle_id",nullable=false) private Salle salle;
    public Machine(){} public Machine(String ref,String marque,Salle salle){this.ref=ref;this.marque=marque;this.salle=salle;}
    public int getId(){return id;} public void setId(int id){this.id=id;}
    public String getRef(){return ref;} public void setRef(String ref){this.ref=ref;}
    public String getMarque(){return marque;} public void setMarque(String marque){this.marque=marque;}
    public Salle getSalle(){return salle;} public void setSalle(Salle salle){this.salle=salle;}
    public String toString(){return "Machine{id="+id+", ref='"+ref+"', marque='"+marque+"'}";}
}
