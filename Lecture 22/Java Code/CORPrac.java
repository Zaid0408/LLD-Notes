/*
Practice 3 — Customer Support System
Build a simple customer support ticket system.

There are three support levels:
- Level 1: Handles basic issues.
- Level 2: Handles technical issues.
- Level 3: Handles critical issues.
Requirements:
1. Every support level should have a common handling interface.
2. Level 1 should receive the ticket first.
3. If it cannot handle the ticket, forward it to Level 2.
4. If Level 2 cannot handle it, forward it to Level 3.
5. If nobody can handle the ticket, print an appropriate message.
Use a simple Ticket object containing an issue type.
*/

class Ticket{
    String description,level;
    int id=0;

    Ticket(int id,String description,String level)
    {
        this.id=id;
        this.description=description;
        this.level=level;
    }

    int getId(){ return this.id;}
    String getLevel(){ return this.level;}
    String getdescription(){ return this.description;}
}

abstract class CustomerSupport{
    protected CustomerSupport customerSupport;

    public CustomerSupport(){ this.customerSupport=null;}

    public void setCustomerSupport(CustomerSupport cs)
    {
        this.customerSupport=cs;
    }
    public abstract void handleCustomerQuery(Ticket ticket);
}

class LevelOneCustomerSupport extends CustomerSupport{
    @Override 
    public void handleCustomerQuery(Ticket ticket)
    {
        if(ticket==null)
        {
            System.out.println("No defined Ticket");
            return;
        }
        String level=ticket.getLevel();
        if(level.equalsIgnoreCase("one"))
        {
            System.out.println("Handling level 1 ticket:");
            System.out.println("Ticket id: "+ ticket.getId());

            System.out.println("Done.");
            return ;
        }

        if(customerSupport!= null) customerSupport.handleCustomerQuery(ticket);
        else { System.out.println("Cannot entertain query next level not found please check level");}
        
    }
}

class LevelTwoCustomerSupport extends CustomerSupport{
    @Override 
    public void handleCustomerQuery(Ticket ticket)
    {
        if(ticket==null)
        {
            System.out.println("No defined Ticket");
            return;
        }
        String level=ticket.getLevel();
        if(level.equalsIgnoreCase("two"))
        {
            System.out.println("Handling level 2 ticket:");
            System.out.println("Ticket id: "+ ticket.getId());

            System.out.println("Done.");
            return ;
        }

        if(customerSupport!= null) customerSupport.handleCustomerQuery(ticket);
        else { System.out.println("Cannot entertain query next level not found please check level");}
        
    }
}

class LevelThreeCustomerSupport extends CustomerSupport{
    @Override 
    public void handleCustomerQuery(Ticket ticket)
    {
        if(ticket==null)
        {
            System.out.println("No defined Ticket");
            return;
        }
        String level=ticket.getLevel();
        if(level.equalsIgnoreCase("three"))
        {
            System.out.println("Handling level 3 ticket:");
            System.out.println("Ticket id: "+ ticket.getId());

            System.out.println("Done.");
            return ;
        }

        if(customerSupport!= null) customerSupport.handleCustomerQuery(ticket);
        else { System.out.println("Cannot entertain query next level not found please check level");}
        
    }
}

public class CORPrac {
    public static void main(String[] args)
    {
        CustomerSupport l1=new LevelOneCustomerSupport();
        CustomerSupport l2=new LevelTwoCustomerSupport();
        CustomerSupport l3=new LevelThreeCustomerSupport();

        l1.setCustomerSupport(l2);
        l2.setCustomerSupport(l3);

        Ticket t1=new Ticket(1, new String("Level 1 ticket decription"), new String("one"));
        Ticket t2=new Ticket(2, new String("Level 2 ticket decription"), new String("TWO"));
        Ticket t3=new Ticket(3, new String("Level 3ticket decription"), new String("Three"));

        System.out.println("Ticket T1: ");
        l1.handleCustomerQuery(t1);
        System.out.println("Ticket T2: ");
        l1.handleCustomerQuery(t2);
        System.out.println("Ticket T3: ");
        l1.handleCustomerQuery(t3);

    }
}
