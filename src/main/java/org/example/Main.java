package org.example;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    static Log my_log;
    static {
        try{
            my_log = new Log("creating.log");
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) {
        System.out.println("Выберите действие:\n1.Добавить сотрудника\n" +
                "2.Похоронить труп\n3.Вывести отчет о кладбище");
        Scanner in = new Scanner(System.in);
        try {
            int command = in.nextInt();
            switch (command) {
                case 1:
                    try {
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
                        my_log.logger.info("Работа завершена");
                        break;
                    } catch (Exception e) {
                        my_log.logger.severe("Ошибка создания сотрудника");
                        my_log.logger.info("Работа завершена");
                        break;
                    }
                case 2:
                    try {
                        System.out.println("Введите имя: ");
                        in.nextLine();
                        String name = in.nextLine();
                        System.out.println("Введите фамилию: ");
                        String surname = in.nextLine();
                        System.out.println("Введите отчество: ");
                        String middleName = in.nextLine();
                        System.out.println("Введите дату рождения: ");
                        String birth = in.nextLine();
                        System.out.println("Введите дату смерти: ");
                        String death = in.nextLine();
                        Dead dead = new Dead(surname, name, middleName, birth);
                        dead.setDeath(death);
                        dead.addHuman();
                        my_log.logger.info("Работа завершена");
                        break;
                    } catch (Exception e) {
                        my_log.logger.severe("Ошибка создания трупа");
                        my_log.logger.info("Работа завершена");
                        break;
                    }
                case 3:
                    try {
                        Report report = new Report();
                        report.start();
                        my_log.logger.info("Работа завершена");
                        break;
                    } catch (Exception e) {
                        my_log.logger.severe("Ошибка вывода отчет");
                        my_log.logger.info("Работа завершена");
                        break;
                    }
                default:
                    System.out.println("Данной команды не существует, попробуйте ещё раз");
                    my_log.logger.severe("Ошибка выполнения команды");
                    my_log.logger.info("Работа завершена");
                    break;
            }
        }
        catch (Exception e){
            my_log.logger.severe("Было введено неверное значение");
            my_log.logger.info("Работа завершена");
        }
    }
}
