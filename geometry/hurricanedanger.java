package geometry;

import java.util.*;

public class hurricanedanger {
    private static final double EPS = 1e-9;
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
            return Math.sqrt(dx * dx + dy * dy);
        }
    }

    private static class Vec {
        double x;
        double y;

        public Vec(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public Vec(Point a, Point b) {
            this.x = b.x - a.x;
            this.y = b.y - a.y;
        }

        double dot(Vec b) {
            return this.x*b.x + this.y*b.y;
        } 

        double normSq() {
            return this.x*this.x + this.y*this.y;
        }

        Vec scale(double s) {
            return new Vec(this.x*s, this.y*s);
        }

        Point translate(Point p) {
            return new Point(p.x + this.x, p.y + this.y);
        }
    }

    public static double calculateDistance(Point p, Point a, Point b) {
        Vec ap = new Vec(a, p);
        Vec ab = new Vec(a, b);
        double u = ap.dot(ab) / ab.normSq();

        Vec scale = ab.scale(u);
        Point c = scale.translate(a);
        return p.calcDistance(c);
    }

    public static double calculateDistanceLine(Point p, Point a, Point b) {
        Vec ap = new Vec(a, p);
        Vec ab = new Vec(a, b);
        double u = ap.dot(ab) / ab.normSq();
        if (u < 0) {
            return p.calcDistance(a);
        }
        if (u > 1) {
            return p.calcDistance(b);
        }
        return calculateDistance(p, a, b);
    }

    public static boolean almostEqual(double A, double B) {
        double diff = Math.abs(A-B);
        A = Math.abs(A);
        B = Math.abs(B);
        double largest = A > B ? A : B;

        return diff <= largest*EPS;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();

        for (int j = 0; j < n; j++) {
            int x1 = sc.nextInt();
            int y1 = sc.nextInt();
            Point a = new Point(x1, y1);

            int x2 = sc.nextInt();
            int y2 = sc.nextInt();
            Point b = new Point(x2, y2);

            Map<Double, ArrayList<String>> map = new TreeMap<>();

            int m = sc.nextInt();

            for (int i = 0; i < m; i++) {
                String city = sc.next();
                int x0 = sc.nextInt();
                int y0 = sc.nextInt();
                Point p = new Point(x0, y0);

                double distance = calculateDistance(p, a, b);
                boolean added = false;

                for (Double key : map.keySet()) {
                    if (almostEqual(key, distance)) {
                        map.get(key).add(city);
                        added = true;
                        break;
                    }
                }

                if (!added) {
                    ArrayList<String> cities = new ArrayList<>();
                    cities.add(city);
                    map.put(distance, cities);
                }

            }

            Map.Entry<Double, ArrayList<String>> entry = map.entrySet().iterator().next();
            ArrayList<String> cities = entry.getValue();

            for (String s : cities) {
                System.out.print(s + " ");
            }
            System.out.println("");
        }
    }
}
