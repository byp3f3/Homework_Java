package org.example;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.logging.Level;

public class Employee extends Human{

    private double salary;
    static Log my_log;

    static {
        try{
            my_log = new Log("employee.log");
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public Employee(String surname, String name, String middleName, String birth) {
        super(surname, name, middleName, birth);
        my_log.logger.info("Сотрудник был принят на работу");
    }

    public void setSalary(double salary){
        try{
            this.salary = salary;
            my_log.logger.info("Зарплатат установлена");
        }
        catch (Exception e){
            my_log.logger.severe("Не удалось установить зарплату");
        }
    }
    public double getSalary() {
        try {
            my_log.logger.info("Зарплата была выведена");
            return salary;
        }
        catch (Exception e){
            my_log.logger.severe("Не удалось вывести работу");
            return 0;
        }
    }

    @Override
    void addHuman() {
        try {
            System.out.println("----------------------\n" + getName()+" "
                    +getSurname()+" "+getMiddleName()+" был принят на работу\nЗарплата: "
                    + getSalary() + "\n----------------------\n");
            my_log.logger.info("Сотрудник был принят на работу. Данные о сотрудники были выведены");
        }
        catch (Exception e){
            my_log.logger.severe("Не удалось принять сотрудника на работу");
        }
    }
}
