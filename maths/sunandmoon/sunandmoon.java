package maths.sunandmoon;

import java.util.Scanner;

public class sunandmoon {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int ds = sc.nextInt();
        int ys = sc.nextInt();

        int dm = sc.nextInt();
        int ym = sc.nextInt();

        int count = 0;

        while ((ds + count) % ys != 0 || (dm + count) % ym != 0) {
            count++;
        }
        System.out.println(count);
    }
}