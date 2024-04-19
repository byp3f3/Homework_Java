package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    static Scanner in = new Scanner(System.in);
    public static void main(String[] args) {
        int choice = 0;
        MyFunctional my = new MyFunctional();
        while (choice != 11 ){
            System.out.println("1.Захоронить труп \n2.Выкопать труп \n3.Вывести всех захороненых \n4.Добавить сотрудника \n5.Добавить зомби \n6.Уволить сотрудника \n7.Уволить зомби \n8.Добавить призрака \n9.Уволить призрака \n10.Установить цветы \n11.Выход \nВыберите действие: ");
            choice = in.nextInt();
            switch (choice){
                case 1:
                    my.bury(registration());
                    continue;
                case 2:
                    my.digUp(registration());
                    continue;
                case 3:
                    my.getAllCorpse();
                    continue;
                case 4:
                    my.addEmployee(registration(), getSalary());
                    continue;
                case 5:
                    my.addZombie(registration(),getSalary());
                    continue;
                case 6:
                    my.removeEmployee(registration());
                    continue;
                case 7:
                    my.removeZombie(registration());
                    continue;
                case 8:
                    my.addGhost(registration(), getSalary());
                case 9:
                    my.removeGhost(registration());
                case 10:
                    flowers();
                case 11:
                    break;
            }
        }
    }
    public static String registration(){
        System.out.println("Введите имя: ");
        in.nextLine();
        String name = in.nextLine();
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        System.out.println("Введите фамилию: ");
        String surname = in.nextLine();
        surname = surname.substring(0, 1).toUpperCase() + surname.substring(1);
        System.out.println("Введите отчество: ");
        String midName = in.nextLine();
        midName = midName.substring(0, 1).toUpperCase() + midName.substring(1);
        String fio = name + " " + surname + " "  + midName;
        return fio;
    }

    public static double getSalary(){
        System.out.println("Введите зарплату сотрудника: ");
        double salary = in.nextDouble();
        return salary;
    }

    public static void flowers(){
        String[] flower = new String[]{"Розы", "Гвоздики", "Астры", "Тюльпаны"};
        System.out.println("Для покупки цветов введите ФИО захороненного");
        registration();
        for (int j = 0; j < flower.length; j++) {
            System.out.println(j + 1 + ". " + flower[j] + " - " + flower[j]);
        }
        int pos = -1;
        while(!(pos >= 0) || !(pos < flower.length)) {
            System.out.println("Выберите цветы: ");
            pos = in.nextInt() - 1;
        }
        System.out.println("Цветы установлены");
    }
}

class MyFunctional{
    ArrayList<String> deadList = new ArrayList<>();
    ArrayList<Object> employee = new ArrayList<>();
    public void bury(String fio){
        deadList.add(fio);
        System.out.println("Труп был захоронен");
    }
    public void digUp(String fio){
        if(deadList.contains(fio)){
            deadList.remove(fio);
            System.out.println("Труп был выкопан");
        }
        else System.out.println("На кладбище нет данного трупа");
    }
    public void getAllCorpse(){
        System.out.println("Все трупы похороненые на кладбище");
        System.out.println(Arrays.deepToString(deadList.toArray()));
    }

    public void addEmployee(String fio, double salary){
        Object emp = fio + salary;
        employee.add(emp);
        System.out.println("Сотрудник был добавлен");
    }

    public void addZombie(String fio, double salary){
        if (deadList.contains(fio)) {
            Object emp = fio + salary;
            employee.add(emp);
            System.out.println("Зомби был добавлен");
        }
    }

    public void addGhost(String fio, double salary){
        if (deadList.contains(fio)) {
            Object emp = fio + salary;
            employee.add(emp);
            System.out.println("Призрак был добавлен");
        }
    }

    public void removeEmployee(String fio){
        if(employee.contains(fio)){
            employee.remove(fio);
            System.out.println("Сотрудник был удален");
        }
        else System.out.println("Данный сотрудник у нас не работает");
    }
    public void removeZombie(String fio){
        if(employee.contains(fio)){
            employee.remove(fio);
            System.out.println("Зомби был уволен");
        }
        else System.out.println("Данный сотрудник у нас не работает");
    }

    public void removeGhost(String fio) {
        if (employee.contains(fio)) {
            employee.remove(fio);
            System.out.println("Призрак был уволен");
        } else System.out.println("Данный сотрудник у нас не работает");
    }
}
