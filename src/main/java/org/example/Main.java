package org.example;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
    Scanner in = new Scanner(System.in);
    System.out.println("Введите длину первой стороны: ");
    int a = in.nextInt();
    System.out.println("Введите длину второй стороны: ");
    int b = in.nextInt();
    System.out.println("Введите длину третьей стороны: ");
    int c = in.nextInt();
    if (Math.pow(a, 2) + Math.pow(b, 2) == Math.pow(c,2) || Math.pow(b, 2) + Math.pow(c, 2) == Math.pow(c,2) || Math.pow(c, 2) + Math.pow(a, 2) == Math.pow(b,2)){
        System.out.println("a, b и c являются сторонами прямоугольного треугольника");
    }
    else if (a==b || b==c || c==a){
        System.out.println("a, b и c являются сторонами равнобедренного треугольника");
    }
    else if (a == b && b == c){
        System.out.println("a, b и c являются сторонами равностороннего треугольника");
    }
    else if (a + b <= c || b + c <= a || c + a <= b){
        System.out.println("a, b и c не являются сторонами треугольника" );
    }
    else System.out.println("a, b и c являются сторонами обычного треугольника");
    }
}








