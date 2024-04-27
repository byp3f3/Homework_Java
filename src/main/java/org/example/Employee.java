package org.example;

public class Employee extends Human{

    private double salary;
    public Employee(String surname, String name, String middleName, String birth) {
        super(surname, name, middleName, birth);
    }

    public void setSalary(double salary){
        this.salary = salary;
    }
    public double getSalary() {
        return salary;
    }

    @Override
    void addHuman() {
        System.out.println("----------------------\n" + getName()+" "
                +getSurname()+" "+getMiddleName()+" был принят на работу\nЗарплата: "
                + getSalary() + "\n----------------------\n");

    }
}
