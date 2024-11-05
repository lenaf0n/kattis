package geometry;

import java.util.*;

public class imperfectgps {
    private static class Point {
        double x;
        double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        double calcDistance(Point other) {
            double dx = this.x - other.x;
            double dy = this.y - other.y;
            return Math.sqrt(dx*dx + dy*dy);
        }

        Point interpolate(Point other, double fraction) {
            double newX = this.x + fraction * (other.x - this.x);
            double newY = this.y + fraction * (other.y - this.y);
            return new Point(newX, newY);
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();
        int step = sc.nextInt();

        List<Point> points = new ArrayList<>();
        List<Integer> times = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            double x = sc.nextDouble();
            double y = sc.nextDouble();
            int t = sc.nextInt();

            points.add(new Point(x, y));
            times.add(t);
        }

        sc.close();

        double tDistance = 0;

        double gpsDistance = 0;
        Point lastPosition = points.get(0);
        int currentTime = step;

        for (int i = 1; i < n; i++) {
            tDistance += points.get(i - 1).calcDistance(points.get(i));

            Point startPoint = points.get(i - 1);
            Point endPoint = points.get(i);
            int startTime = times.get(i - 1);
            int endTime = times.get(i);

            while (currentTime < endTime) {
                double fraction = (double) (currentTime - startTime) / (endTime - startTime);
                Point interpolatedPoint = startPoint.interpolate(endPoint, fraction);

                gpsDistance += lastPosition.calcDistance(interpolatedPoint);
                lastPosition = interpolatedPoint;
                currentTime += step;
            }

            if (i == n-1) {
                gpsDistance += lastPosition.calcDistance(endPoint);
            }
        }

        

        double percentageLost = ((tDistance - gpsDistance) / tDistance) * 100;
        System.out.println(percentageLost);
    }
}