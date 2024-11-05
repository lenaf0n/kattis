package geometry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class browniepoints {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            int n = Integer.parseInt(br.readLine());
            if (n == 0) {
                break;
            }

            ArrayList<int[]> points = new ArrayList<>();
            int[] center = new int[2];
            for (int i = 0; i < n; i++) {
                String[] input = br.readLine().split(" ");
                int x = Integer.parseInt(input[0]);
                int y = Integer.parseInt(input[1]);

                if (i == n/2) {
                    center[0] = x;
                    center[1] = y;
                } else {
                    points.add(new int[]{x, y});
                }
            }

            int sCount = 0;
            int oCount = 0;

            for (int[] p : points) {
                if ((p[0] > center[0] && p[1] > center[1]) || (p[0] < center[0] && p[1] < center[1])) {
                    sCount++;
                } else if((p[0] > center[0] && p[1] < center[1]) || (p[0] < center[0] && p[1] > center[1])) {
                    oCount++;
                }
            }

            System.out.println(sCount + " " + oCount);
        }
    }
}
