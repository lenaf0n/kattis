package maths.champernownecount;

import java.util.Scanner;

public class champernownecount {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();
        int k = sc.nextInt();

        int count = 0;
        long champ = 0;

        for (long i = 1; i <= n; i++) {
            int size = String.valueOf(i).length();
            champ = (long) ((champ*Math.pow(10, size))%k + i%k);

            if (champ%k == 0) {
                count++;
            }
        }

        System.out.println(count);
    }
}