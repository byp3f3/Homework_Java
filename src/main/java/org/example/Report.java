package org.example;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Random;
import java.util.Scanner;

public class Report {
    private int graveC;
    private int day;
    private int cache;
    private int money;
    private int staffM;

    static Log my_log;

    static {
        try{
            my_log = new Log("report.log");
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public Report() {
        try {
            this.staffM = 0;
            my_log.logger.info("Создан отчет");
        }
        catch (Exception e){
            my_log.logger.severe("Не удалось создать отчет");
        }
    }

    public int countG(int rows, int columns) {
        try{
            my_log.logger.info("Получен размер кладбища");
            return rows * columns;
        }
        catch (Exception e) {
            my_log.logger.severe("Не удалось получить размер кладбища");
            return 0;
        }
    }

    public boolean isFull(int graveC, int maxgraveC) {
        try{
            my_log.logger.info("Проверка наличия свободных мест");
            return graveC >= maxgraveC;
        }
        catch (Exception e){
            my_log.logger.severe("Не удалось проверить количество свободных мест");
            return false;
        }
    }

    public int remainingG(int graveC, int maxgraveC) {
        try{
            my_log.logger.info("Получено количество свободных мест");
            return maxgraveC - graveC;
        }
        catch (Exception e){
            my_log.logger.severe("Не удалось получить количество свободных мест");
            return 0;
        }
    }

    public void addG(Random random, int maxgraveC, int mingravesAdd, int maxgravesoAdd) {
        try{
            if (!this.isFull(this.graveC, maxgraveC)) {
                int quantityG = random.nextInt(maxgravesoAdd - mingravesAdd + 1) + mingravesAdd;
                if (quantityG > this.remainingG(this.graveC, maxgraveC)) {
                    quantityG = this.remainingG(this.graveC, maxgraveC);
                }
                this.graveC += quantityG;
            }
            my_log.logger.info("Получено количество захороненных человек");
        }
        catch (Exception e){
            my_log.logger.severe("Не удалось получить количествозахороненных человек");
        }
    }

    public void cleanG() {
        try{
            if (this.day % 15 == 0) {
                if (this.graveC >= 100) {
                    this.graveC -= 100;
                } else {
                    this.graveC = 0;
                }
            }
            my_log.logger.info("Люди были успешно откопаны");
        }
        catch (Exception e){
            my_log.logger.severe("Не удалось откопать людей");
        }
    }

    public void sellPlots(int maxgraveC) {
        try{
            if (!this.isFull(this.graveC, maxgraveC)) {
                int quantitygravesSell = this.cache / 120;
                if (quantitygravesSell > this.remainingG(this.graveC, maxgraveC)) {
                    quantitygravesSell = this.remainingG(this.graveC, maxgraveC);
                }
                this.money += quantitygravesSell * 120;
                this.graveC -= quantitygravesSell;
                this.cache -= quantitygravesSell * 120;
            }
            my_log.logger.info("Оплата была получена");
        }
        catch (Exception e){
            my_log.logger.severe("Не удалось провести оплату");
        }
    }

    public void printDayInfo(int day, int rows, int columns, int maxgraveC, int money) {
        try{
            System.out.println("День " + day + ": ");
            PrintStream var10000 = System.out;
            int var10001 = this.countG(rows, columns);
            var10000.println("Количество могил на кладбище: " + var10001);
            var10000 = System.out;
            boolean var6 = this.isFull(this.graveC, maxgraveC);
            var10000.println("Кладбище полное: " + var6);
            var10000 = System.out;
            var10001 = this.remainingG(this.graveC, maxgraveC);
            var10000.println("Оставшихся мест на кладбище: " + var10001);
            System.out.println("Занятых мест на кладбище: " + this.graveC);
            System.out.println("Банк: " + money);
            my_log.logger.info("Отчет успешно выведен");
        }
        catch (Exception e){
            my_log.logger.severe("Не удалось вывести отчет");
        }
    }

    public void start() {
        try{
            Report report = new Report();
            int rows = 15;
            int columns = 30;
            int maxgraveC = rows * columns;
            Scanner scanner = new Scanner(System.in);
            Random random = new Random();
            while(true) {
                report.addG(random, maxgraveC, 5, 10);
                report.sellPlots(maxgraveC);
                report.cleanG();
                report.printDayInfo(report.day, rows, columns, maxgraveC, report.money);
                System.out.println("Нажмите Enter для перехода к следующему дню или введите 'exit' для выхода:");
                String input = scanner.nextLine();
                my_log.logger.info("Был выведен новый отчет");
                if (input.equalsIgnoreCase("exit")) {
                    System.out.println("Программа завершена.");
                    scanner.close();
                    return;
                }
                ++report.day;
            }
        }
        catch (Exception e){
            my_log.logger.severe("Не удалось вывести отчет");
        }
    }
}

