package geometry;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class cursethedarkness {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int testCases = Integer.parseInt(br.readLine());
        for (int i = 0; i < testCases; i++) {
            String[] bookPosition = br.readLine().split(" ");
            double bookX = Double.parseDouble(bookPosition[0]);
            double bookY = Double.parseDouble(bookPosition[1]);

            int candleCount = Integer.parseInt(br.readLine());
            boolean canLight = false;

            for (int j = 0; j < candleCount; j++) {
                String[] candlePosition = br.readLine().split(" ");
                double candleX = Double.parseDouble(candlePosition[0]);
                double candleY = Double.parseDouble(candlePosition[1]);

                if (dist(bookX, bookY, candleX, candleY) <= 8.000000001*8.000000001) {
                    canLight = true;
                    break;
                }
            }

            if (canLight) {
                System.out.println("light a candle");
            } else {
                System.out.println("curse the darkness");
            }
        }
    }

    private static double dist(double x1, double y1, double x2, double y2) {
        double dx = x1 - x2;
        double dy = y1 - y2;
        return dx * dx + dy * dy;
    }
}
