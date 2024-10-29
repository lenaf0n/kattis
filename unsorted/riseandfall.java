package unsorted;

import java.util.Scanner;

public class riseandfall {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();
        for (int i = 0; i < n; i++) {
            String input = sc.next();

            boolean increasing = true;
            boolean isRiseAndFall = true;
            int c = Integer.parseInt(String.valueOf(input.charAt(0)));
            System.out.print(c);

            for (int j = 1; j < input.length(); j++) {
                int next = Integer.parseInt(String.valueOf(input.charAt(j)));
                if (c <= next && increasing && isRiseAndFall) {
                    System.out.print(next);
                    c = next;
                } else if (c > next && increasing && isRiseAndFall) {
                    increasing = false;
                    System.out.print(next);
                    c = next;
                } else if (c >= next && isRiseAndFall) {
                    System.out.print(next);
                    c = next;
                } else {
                    isRiseAndFall = false;
                    System.out.print(c);
                }
            }

            System.out.print("\n");
        }
    }
}
