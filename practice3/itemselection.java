package practice3;

import java.util.*;

public class itemselection {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();
        int m = sc.nextInt();
        int s = sc.nextInt();
        int p = sc.nextInt();
        int q = sc.nextInt();

        int numPages = (n + m - 1) / m;
        List<Integer> toModify = new ArrayList<>();

        int count = 0;
        
        boolean[][] cur = new boolean[numPages][m];
        boolean[][] result = new boolean[numPages][m];

        for (int i = 0; i < p; i++) {
            int num = sc.nextInt();
            int page = (num - 1)/m;
            cur[page][(num-1)%m] = true;
        }

        for (int i = 0; i < q; i++) {
            int num = sc.nextInt();
            int page = (num - 1)/m;
            result[page][(num-1)%m] = true;
        }

        for (int i = 0; i < numPages; i++) {
            if (!Arrays.equals(cur[i], result[i])) {
                toModify.add(i);
                int numTrues = countTrues(result[i]);

                int selected = 1 + numTrues;
                int unselected = 1 + (m - numTrues);

                int checking = countDifferences(cur[i], result[i]);
                
                int toAdd = Math.min(selected, Math.min(unselected, checking));
                count += toAdd;
            }
        }

        if (!toModify.isEmpty()) {
            int minPage = Collections.min(toModify) + 1;
            int maxPage = Collections.max(toModify) + 1;
            int minToS = s - minPage;
            int maxToS = maxPage - s;
            
            if (minToS < 0) {
                count += maxToS;
            } else if (maxToS < 0) {
                count += minToS;
            } else if (minToS == 0 || maxToS == 0) {
                count += Math.max(minToS, maxToS);
            } else {
                count += Math.min(2 * minToS + maxToS, 2 * maxToS + minToS);

            }
        }
        System.out.println(count);
    }

    public static int countTrues(boolean[] array) {
        int count = 0;
        for (boolean value : array) {
            if (value) {
                count++;
            }
        }
        return count;
    }

    public static int countDifferences(boolean[] array1, boolean[] array2) {
        int differences = 0;
        for (int i = 0; i < array1.length; i++) {
            if (array1[i] != array2[i]) {
                differences++;
            }
        }
        return differences;
    }
}
