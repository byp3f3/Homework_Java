package org.example;

import java.io.PrintStream;
import java.util.Random;
import java.util.Scanner;

public class Report {
    private int graveC = 0;
    private int day = 1;
    private int cache;
    private int money;
    private int staffM;

    public Report() {
        this.money = this.graveC * 120;
        this.staffM = 0;
    }

    public int countG(int rows, int columns) {
        return rows * columns;
    }

    public boolean isFull(int graveC, int maxgraveC) {
        return graveC >= maxgraveC;
    }

    public int remainingG(int graveC, int maxgraveC) {
        return maxgraveC - graveC;
    }

    public void addG(Random random, int maxgraveC, int mingravesAdd, int maxgravesoAdd) {
        if (!this.isFull(this.graveC, maxgraveC)) {
            int quantityG = random.nextInt(maxgravesoAdd - mingravesAdd + 1) + mingravesAdd;
            if (quantityG > this.remainingG(this.graveC, maxgraveC)) {
                quantityG = this.remainingG(this.graveC, maxgraveC);
            }

            this.graveC += quantityG;
        }
    }

    public void cleanG() {
        if (this.day % 15 == 0) {
            if (this.graveC >= 100) {
                this.graveC -= 100;
            } else {
                this.graveC = 0;
            }
        }

    }

    public void sellPlots(int maxgraveC) {
        if (!this.isFull(this.graveC, maxgraveC)) {
            int quantitygravesSell = this.cache / 120;
            if (quantitygravesSell > this.remainingG(this.graveC, maxgraveC)) {
                quantitygravesSell = this.remainingG(this.graveC, maxgraveC);
            }
            this.money += quantitygravesSell * 120;
            this.graveC -= quantitygravesSell;
            this.cache -= quantitygravesSell * 120;
        }

    }

    public void printDayInfo(int day, int rows, int columns, int maxgraveC, int money) {
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
    }

    public void start() {
        Report report = new Report();
        int rows = 15;
        int columns = 30;
        int maxgraveC = rows * columns;
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        System.out.println("День 0: ");
        PrintStream var10000 = System.out;
        int var10001 = report.countG(rows, columns);
        var10000.println("Количество могил на кладбище: " + var10001);
        var10000 = System.out;
        boolean var8 = report.isFull(report.graveC, maxgraveC);
        var10000.println("Кладбище полное: " + var8);
        var10000 = System.out;
        var10001 = report.remainingG(report.graveC, maxgraveC);
        var10000.println("Оставшихся мест на кладбище: " + var10001);
        System.out.println("Занятых мест на кладбище: " + report.graveC);
        System.out.println("Банк: " + report.money);
        System.out.println("Нажмите Enter для перехода к следующему дню или введите 'exit' для выхода:");
        scanner.nextLine();
        while(true) {
            report.addG(random, maxgraveC, 5, 10);
            report.sellPlots(maxgraveC);
            report.cleanG();
            report.printDayInfo(report.day, rows, columns, maxgraveC, report.money);
            System.out.println("Нажмите Enter для перехода к следующему дню или введите 'exit' для выхода:");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Программа завершена.");
                scanner.close();
                return;
            }
            ++report.day;
        }
    }
}

