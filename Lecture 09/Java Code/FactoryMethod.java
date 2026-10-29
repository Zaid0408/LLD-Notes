interface Burger {
    void prepare();
}

class BasicBurger implements Burger {
    public void prepare() {
        System.out.println("Preparing Basic Burger");
    }
}

class StandardBurger implements Burger {
    public void prepare() {
        System.out.println("Preparing Standard Burger");
    }
}

class PremiumBurger implements Burger {
    public void prepare() {
        System.out.println("Preparing Premium Burger");
    }
}

// Now the Creator:

abstract class BurgerStore {

    // Common business logic
    public Burger orderBurger() {

        Burger burger = createBurger();

        burger.prepare();

        System.out.println("Burger ordered!");

        return burger;
    }

    // Factory Method
    protected abstract Burger createBurger();
}

// Concrete creators:

class BasicBurgerStore extends BurgerStore {

    @Override
    protected Burger createBurger() {
        return new BasicBurger();
    }
}

class StandardBurgerStore extends BurgerStore {

    @Override
    protected Burger createBurger() {
        return new StandardBurger();
    }
}

class PremiumBurgerStore extends BurgerStore {

    @Override
    protected Burger createBurger() {
        return new PremiumBurger();
    }
}
/*

BurgerStore
     |
     | orderBurger()  ← common workflow
     |
     ↓
createBurger()       ← Factory Method
     ↑
     |
 ┌───┼──────────────┐
 ↓   ↓              ↓
Basic Standard   Premium
Store  Store       Store
*/

public class FactoryMethod {

    public static void main(String[] args) {

        BurgerStore store = new BasicBurgerStore();

        store.orderBurger();

        store = new PremiumBurgerStore();

        store.orderBurger();

        ReportGenerator reportGenerator=new PDFReportGenerator();
        reportGenerator.reportGenerator();
        reportGenerator=new CSVReportGenerator();
        reportGenerator.reportGenerator();
    }
}

/*
Problem 3 — Factory Method

Report Generation System

Build a report generation system supporting:

PDF Reports

CSV Reports

Requirements:

Create a Report interface with a generate() method.
Implement PDFReport and CSVReport.
Create an abstract ReportGenerator class.
The parent class should contain a common workflow to generate a report.
The subclasses should decide which report object to create by implementing the factory method.

Goal: Understand the relationship between the abstract Creator, Concrete Creators and Products.
*/

/*
             ReportGenerator
                    |
                    |
             reportGenerator()
                    |
                    ↓
              createReport()
                    ↑
             (Abstract Method)
                    |
             ┌──────┴──────┐
             |             |
             ↓             ↓
      PDFReportGenerator  CSVReportGenerator
             |             |
             ↓             ↓
          PDFreport     CSVreport
*/

interface Report{
    void generate();
}
class PDFreport implements Report{
    @Override
    public void generate()
    {
        System.out.println("Generating PDF Report");
    }
}


class CSVreport implements Report{
    @Override
    public void generate()
    {
        System.out.println("Generating CSV Report");
    }
}

abstract class ReportGenerator{
    public void reportGenerator()
    {
        Report report= createReport();
        report.generate();
    }
    // Factory method 
    protected abstract Report createReport();
}

class PDFReportGenerator extends ReportGenerator{
    @Override
    protected Report createReport() {
        return new PDFreport();
    }
}
class CSVReportGenerator extends ReportGenerator{
    @Override
    protected Report createReport() {
        return new CSVreport();
    }
}
