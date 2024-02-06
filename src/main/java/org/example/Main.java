package org.example;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        final int rad = 6371;
        Scanner in = new Scanner(System.in);
        System.out.println("Широта 1: ");
        double w1 = Math.toRadians(in.nextDouble());
        System.out.println("Высота 1: ");
        double h1 = Math.toRadians(in.nextDouble());
        System.out.println("Широта 2: ");
        double w2 = Math.toRadians(in.nextDouble());
        System.out.println("Высота 2: ");
        double h2 = Math.toRadians(in.nextDouble());
        double d = Math.acos(Math.sin(w1)*Math.sin(w2) + Math.cos(w1)*Math.cos(w2)*Math.cos(h1-h2));
        double res = d*rad;
        System.out.println("Расстояние между точками: " + res + "км");
        }
    }