package org.example;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String[] brandList = new String[]{"Casio", "Seiko", "Breguet", "Chopard", "Cartie"};
        String[][] firmList = new String[][]{{brandList[0], "G-SHOCK"}, {brandList[1], "SARY213"},
                {brandList[2], "Marine"}, {brandList[3], "L.U.C"}, {brandList[4], "Tank Must"}};
        ArrayList<Object> orderList = new ArrayList<>();
        int orderСount = 0;
        while (!(orderСount >0)) {
            System.out.println("Сколько заказов Вы хотите создать?");
            orderСount = in.nextInt();
        }
        for (int i = 0; i < orderСount; i++) {
            System.out.println("Введите Ваше Имя: ");
            in.nextLine();
            String name = in.nextLine();
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
            System.out.println("Введите Вашу Фамилию: ");
            String surname = in.nextLine();
            surname = surname.substring(0, 1).toUpperCase() + surname.substring(1);
            System.out.println("Введите Ваше Отчество: ");
            String midName = in.nextLine();
            midName = midName.substring(0, 1).toUpperCase() + midName.substring(1);
            String fio = name + " " + surname + " "  + midName;
            System.out.println("Введите Ваш e-mail: ");
            String email = in.nextLine();
            System.out.println("Введите Ваш номер телефона: ");
            String phoneNumber = in.nextLine();
            for (int j = 0; j < firmList.length; j++) {
                System.out.println(j + 1 + ". " + firmList[j][0] + " - " + firmList[j][1]);
            }
            int pos = 0;
            while(!(pos >0) || !(pos < firmList.length)) {
                System.out.println("Выберите позицию товара: ");
                pos = in.nextInt() - 1;
            }
            int count = 0;
            while (!(count >0)) {
                System.out.println("Введите количество товара: ");
                count = in.nextInt();
            }
            Object[] order = {"ФИО: " + fio, "email: " + email, "Номер телефона: " + phoneNumber, "Фирма часов: " + firmList[pos][0],
                    "Марка часов: " + firmList[pos][1], "Количество товара: " + count};
            orderList.add(order);
            System.out.println("Заказ оформлен");
            Arrays.stream(order).forEach(System.out::println);
        }
        System.out.println("Все заказы: ");
        System.out.println(Arrays.deepToString(orderList.toArray()));
    }
}