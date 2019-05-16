using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace lab4C____.Model
{
    public class Car
    {
        public string model = "";
        public int year;
        public Engine motor;

        public Car() { }
        public Car(string model, Engine e, int year)
        {
            this.model = model;
            this.motor = e;
            this.year = year;
        }
    }
}
