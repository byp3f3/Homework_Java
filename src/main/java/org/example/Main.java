package org.example;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Выберите действие:\n1.Добавить сотрудника\n" +
                "2.Похоронить труп\n3.Вывести отчет о кладбище");
        Scanner in = new Scanner(System.in);
        int command = in.nextInt();
        switch (command) {
            case 1:
                System.out.println("Введите имя: ");
                in.nextLine();
                String name = in.nextLine();
                System.out.println("Введите фамилию: ");
                String surname = in.nextLine();
                System.out.println("Введите отчество: ");
                String middleName = in.nextLine();
                System.out.println("Введите дату рождения: ");
                String birth = in.nextLine();
                System.out.println("Введите размер заработной платы: ");
                double salary = in.nextDouble();
                Employee employee = new Employee(surname, name, middleName, birth);
                employee.setSalary(salary);
                employee.addHuman();
                break;
            case 2:
                System.out.println("Введите имя: ");
                in.nextLine();
                name = in.nextLine();
                System.out.println("Введите фамилию: ");
                surname = in.nextLine();
                System.out.println("Введите отчество: ");
                middleName = in.nextLine();
                System.out.println("Введите дату рождения: ");
                birth = in.nextLine();
                Dead dead = new Dead(surname, name, middleName, birth);
                System.out.println("Введите дату смерти: ");
                String death = in.nextLine();
                dead.setDeath(death);
                dead.addHuman();
                break;
            case 3:
                Report report = new Report();
                report.start();
                break;
            default:
                System.out.println("Данной команды не существует, попробуйте ещё раз");
                break;
        }

    }
}
