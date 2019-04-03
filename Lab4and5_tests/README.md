# Laboratorium 5
Rozszerzenie laboratorium 4 o testy jednostkowe \
```localhost:8080``` - adres po uruchomieniu (domyślny w springu), na zaboratorium może byćn potrzebny 9090 \
```requesty wysłane z aplikacji postman

# Uruchomienie serwera bazy danych
Potrzebny jest Apache Derby, można wziąć [stąd](http://db.apache.org/derby/derby_downloads.html)\
Wypadkować, wejść do rozpakowanego folderu -> /lib

* pierwsza jest instancją serwera, uruchamiamy za pomocą ```java -jar derbyrun.jar server start```\
* druga jest chyba odpowiedzialna bezpośrednio za bazę danych, tu należy dać najepirw ```java -cp "derbyclient.jar;derbytools.jar" org.apache.derby.tools.ij```, następnie ```connect 'jdbc:derby://localhost:1527/pt_lab;create=true';```
* usunięcie bazy danych mozna wykonać przes skasowanie folderu bazy

# Przykładowe zapytania
* addComputer()
```
{
  "amount": 10,
  "graphic_card": "NVidia GeForce 550Ti",
  "memory": "4GB",
  "price": 2400,
  "processor": "Intel i5",
  "type": "PC"
}
```
* getComputers(), updateComputer() wymagają posiadania id filmu, które można wyciągnąć z listComputers()
