using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace lab4C____.Model
{
    public class Engine : IComparable<Engine>
    {
        public double displacemnet;
        public double horsePower;
        public string model;

        public Engine() { }
        public Engine(double dp, double power, string model)
        {
            this.displacemnet = dp;
            this.horsePower = power;
            this.model = model;
        }

        public int CompareTo(Engine other)
        {
            if (this.horsePower >= other.horsePower) return 1;
            else return 0;
        }
    }
}
