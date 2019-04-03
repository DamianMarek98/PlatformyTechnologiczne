package platformy.technologiczne.lab5;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import platformy.technologiczne.lab5.models.Computers;
import platformy.technologiczne.lab5.services.ComputerService;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class ComputersServiceTest {
    @Mock
    EntityManager em;

    @Test
    public void validateEmptyCollectionHandlingInComputersService(){
        //Arrange:
        List<Computers> computers = new ArrayList<Computers>();
        ComputerService computerService = new ComputerService(em);

        //act:
        int result = computerService.getTotalPrices(computers);

        //Asserts
        assertEquals(result, 0);
    }
}
