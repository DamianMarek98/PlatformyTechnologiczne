package platformy.technologiczne.lab5.services;

import org.springframework.stereotype.Service;
import platformy.technologiczne.lab5.models.Computers;

import javax.persistence.EntityManager;
import java.util.List;

@Service
public class ComputerService extends EntityService<Computers> {

    public ComputerService(EntityManager em){
        super(em, Computers.class, Computers::getId);
    }

    public List<Computers> findAll(){
        return em.createNamedQuery(Computers.findAll, Computers.class).getResultList();
    }

    public int getTotalPrices(List<Computers> computers){
        if(computers == null){
            return 0;
        }
        int sum = 0;
        int price = 0;
        for(Computers computer : computers){
            price = computer.getPrice();
            sum = sum + price * computer.getAmount();
        }
        return sum;
    }
}
