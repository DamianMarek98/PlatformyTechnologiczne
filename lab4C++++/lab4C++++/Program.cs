using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Threading.Tasks;
using System.Windows.Forms;
using lab4C____.Model;
namespace lab4C____
{
    static class Program 
    {
        /// <summary>
        /// The main entry point for the application.
        /// </summary>
        [STAThread]
        static void Main()
        {

            List<Car> myCars = new List<Car>()
            {
                new Car("E250", new Engine(1.8, 204, "CGI"), 2009),
                new Car("E350", new Engine(3.5, 292, "CGI"), 2009),
                new Car("A6", new Engine(2.5, 187, "FSI"), 2012),
                new Car("A6", new Engine(2.8, 220, "FSI"), 2012),
                new Car("A6", new Engine(3.0, 295, "TFSI"), 2012),
                new Car("A6", new Engine(2.0, 175, "TDI"), 2011),
                new Car("A6", new Engine(3.0, 309, "TDI"), 2011),
                new Car("S6", new Engine(4.0, 414, "TFSI"), 2012),
                new Car("S8", new Engine(4.0, 513, "TFSI"), 2012)

            };
            //zad0
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);
            Form1 form = new Form1();
            BindingList<Car> mycarsbindinglist = new BindingList<Car>(myCars);
            BindingSource carbindingsource = new BindingSource();
            carbindingsource.DataSource = mycarsbindinglist;
            ////drag a datagridview control from the toolbox to the form.
            form.dataGridView1.DataSource = carbindingsource;            form.dataGridView1.Visible = true;            
            
            Application.Run(form);
            //zad1
            //a
            var QueryA =
                from car in myCars
                where car.model == "A6"
                orderby car.motor.horsePower / car.motor.displacemnet descending
                select new { engineType = car.motor.model == "TDI" ? "diesel" : "petrol", avgHPPL = car.motor.horsePower / car.motor.displacemnet };

            double petrol = 0.0, diesel = 0.0;
            int numberOfPetrolCars = 0, numberOfDieselCars = 0;

            foreach (var elem in QueryA)
            {
                if (elem.engineType == "petrol")
                {
                    petrol += elem.avgHPPL;
                    numberOfPetrolCars++;
                }
                else
                {
                    numberOfDieselCars++;
                    diesel += elem.avgHPPL;
                }
            }

            Console.WriteLine("Zad1a");
            Console.WriteLine("diesel: " + (diesel / (double)numberOfDieselCars));
            Console.WriteLine("petrol: " + (petrol / (double)numberOfPetrolCars));
            //b
            var QueryB = myCars.Where(m =>
                m.model == "A6").OrderByDescending(p => p.motor.horsePower / p.motor.displacemnet).Select(e => new
                {
                    engineType = e.motor.model == "TDI" ? "diesel" : "petrol",
                    avgHPPL = e.motor.horsePower / e.motor.displacemnet
                }

                );

            petrol = 0.0; diesel = 0.0;
            numberOfPetrolCars = 0; numberOfDieselCars = 0;

            foreach (var elem in QueryB)
            {
                if (elem.engineType == "petrol")
                {
                    petrol += elem.avgHPPL;
                    numberOfPetrolCars++;
                }
                else
                {
                    numberOfDieselCars++;
                    diesel += elem.avgHPPL;
                }
            }

            Console.WriteLine("Zad1a");
            Console.WriteLine("diesel: " + (diesel / (double)numberOfDieselCars));
            Console.WriteLine("petrol: " + (petrol / (double)numberOfPetrolCars));
            //test sortowania:
            Console.WriteLine("Test sortowania");
            foreach (var e in QueryA)
            {
                Console.WriteLine(e.engineType + ": " + e.avgHPPL);
            }
            //zad2
            Comparison<Car> arg1 = delegate (Car c1, Car c2) { return c1.motor.horsePower >= c2.motor.horsePower ? 1 : -1; };
            myCars.Sort(new Comparison<Car>(arg1));            Console.WriteLine("test arg1");
            foreach (Car e in myCars)
            {
                Console.WriteLine(e.model + " o mocny: " + e.motor.horsePower);
            }

            Predicate<Car> arg2 = delegate (Car c) { if (c.motor.model == "TDI") return true; else return false; };
            Action<Car> arg3 = delegate (Car c) { Console.WriteLine(c.model + " with engine: " + c.motor.model); };
            myCars.FindAll(arg2).ForEach(arg3);
        }
    }
}
