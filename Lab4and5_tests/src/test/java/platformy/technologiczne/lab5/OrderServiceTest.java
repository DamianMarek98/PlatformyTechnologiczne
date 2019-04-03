package platformy.technologiczne.lab5;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Sort;
import platformy.technologiczne.lab5.models.Computers;
import platformy.technologiczne.lab5.models.OrderedComputers;
import platformy.technologiczne.lab5.models.Orders;
import platformy.technologiczne.lab5.services.OrderService;
import platformy.technologiczne.lab5.services.exceptions.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;

import javax.persistence.EntityManager;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {

    @Mock
    private EntityManager em;


    @Test(expected = OutOfStockException.class)
    public void whenOrderedComputerNotAvailable_placeOrderThrowsOutOfStockException() {
        Orders order = new Orders();

        Computers computer = new Computers();
        computer.setAmount(5);

        OrderedComputers orderedComputers = new OrderedComputers();
        orderedComputers.setComputers(computer);
        orderedComputers.setAmount(10); // 10>5

        order.getOrderedComputers().add(orderedComputers);

        Mockito.when(em.find(Computers.class, orderedComputers.getComputers().getId())).thenReturn(computer);

        OrderService orderService = new OrderService(em);

        orderService.placeOrder(order);

    }

    @Test()
    public void whenOrderedComputerIsAvailable_placeOrderAddOrder() {
        Orders order = new Orders();

        Computers computer = new Computers();
        computer.setAmount(5);

        OrderedComputers orderedComputers = new OrderedComputers();
        orderedComputers.setComputers(computer);
        orderedComputers.setAmount(4); // 4<5

        order.getOrderedComputers().add(orderedComputers);

        Mockito.when(em.find(Computers.class, orderedComputers.getComputers().getId())).thenReturn(computer);

        OrderService orderService = new OrderService(em);

        orderService.placeOrder(order);

    }


    @Test(expected = OrderEmptyException.class)
    public void whenOrderIsEmpty_placeOrderThrowsOrderEmptyException() {

        OrderService orderService = new OrderService(em);
        //Act
        orderService.placeOrder(null);

        //Assert - OrderEmptyException expected
    }

    @Test(expected = OrderEmptyException.class)
    public void whenNoComputersInOrder_placeOrderThrowOrderEmptyException() {
        //Arrange
        OrderedComputers orderedComputers = new OrderedComputers();
        Orders order = new Orders();
        Computers computers = new Computers();
        OrderService orderService = new OrderService(em);

        //Act
        orderService.placeOrder(order);

        //Assert - OrderEmptyException expected

    }

    @Test
    public void whenOrderedComputerAvailable_placeOrderDecreasesAmountByOrderedComputerAmount() {
        //Arrange
        Computers computer = new Computers();
        computer.setAmount(5);

        OrderedComputers orderedComputers = new OrderedComputers();
        orderedComputers.setAmount(5);
        orderedComputers.setComputers(computer);

        Orders order = new Orders();
        order.getOrderedComputers().add(orderedComputers);


        Mockito.when(em.find(Computers.class,
                orderedComputers.getComputers().getId()))
                .thenReturn(computer);

        OrderService orderService = new OrderService(em);

        //Act
        orderService.placeOrder(order);

        //Assert
        assertEquals(0, (int) computer.getAmount());
        Mockito.verify(em, times(1)).persist(order);
    }

    @Test(expected = OutOfStockException.class)
    public void whenManyComputersOrderedandAtLeastOneOutOfStock_placeOrderThrowsOutOfStockException(){
        Computers computer1 = new Computers();
        Computers computer2 = new Computers();
        computer1.setAmount(100);
        computer2.setAmount(10);

        OrderedComputers orderedComputers1 = new OrderedComputers();
        OrderedComputers orderedComputers2 = new OrderedComputers();
        orderedComputers1.setAmount(20);
        orderedComputers2.setAmount(20);

        orderedComputers1.setComputers(computer1);
        orderedComputers2.setComputers(computer2);

        Orders order = new Orders();
        order.getOrderedComputers().add(orderedComputers1);
        order.getOrderedComputers().add(orderedComputers2);

        Mockito.when(em.find(Computers.class, orderedComputers1.getComputers().getId())).thenReturn(computer1);
        Mockito.when(em.find(Computers.class, orderedComputers2.getComputers().getId())).thenReturn(computer2);

        OrderService orderService = new OrderService(em);

        //act
        orderService.placeOrder(order);

        //Out of stock expected
    }

    @Test()
    public void whenManyComputersOrderedandAllAreInStock_placeOrderAndDecreseAmountOfOrderedComputers(){
        Computers computer1 = new Computers();
        Computers computer2 = new Computers();
        computer1.setAmount(10);
        computer2.setAmount(10);

        OrderedComputers orderedComputers1 = new OrderedComputers();
        OrderedComputers orderedComputers2 = new OrderedComputers();
        orderedComputers1.setAmount(5);
        orderedComputers2.setAmount(8);

        orderedComputers1.setComputers(computer1);
        orderedComputers2.setComputers(computer2);

        Orders order = new Orders();
        order.getOrderedComputers().add(orderedComputers1);
        order.getOrderedComputers().add(orderedComputers2);

        Mockito.when(em.find(Computers.class, orderedComputers1.getComputers().getId())).thenReturn(computer1);
        Mockito.when(em.find(Computers.class, orderedComputers2.getComputers().getId())).thenReturn(computer2);

        OrderService orderService = new OrderService(em);

        //act
        orderService.placeOrder(order);

        //Assets computers are available
        assertEquals(5, (int) computer1.getAmount());
        assertEquals(2, (int) computer2.getAmount());  //can do other test if expected is not correct
        Mockito.verify(em, times(2)).persist(order);

    }
}
