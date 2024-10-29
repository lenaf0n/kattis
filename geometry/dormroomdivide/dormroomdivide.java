package geometry.dormroomdivide;

import java.util.Scanner;

public class dormroomdivide {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();
        double[][] coord = new double[n][2];

        for (int i = 0; i < n; i++) {
            coord[i][0] = sc.nextDouble();
            coord[i][1] = sc.nextDouble();
        }

        sc.close();

        double totalArea = area(coord, n);
        double target = totalArea/2;
        int numberVertices = 3;

        double area = area(coord, numberVertices);
        while (area < target) {
            numberVertices++;
            area = area(coord, numberVertices);
        }

        double[] a = coord[numberVertices-1];
        double[] b = coord[numberVertices-2];

        if (n==3) {
            double[] midPoint = new double[2];
            midPoint[0] = (a[0] + b[0]) / 2;
            midPoint[1] = (a[1] + b[1]) / 2;

            System.out.println(midPoint[0] + " " + midPoint[1]);

        } else {
            double minShapeArea = area(coord, numberVertices-1);

            while (true) {
                double[] midPoint = new double[2];
                midPoint[0] = (a[0] + b[0]) / 2;
                midPoint[1] = (a[1] + b[1]) / 2;

                double areaTest = minShapeArea + triangleArea(coord[0], coord[numberVertices-2], midPoint);

                if (almostEqual(areaTest, target)) {
                    System.out.println(midPoint[0] + " " + midPoint[1]);
                    break;
                } else if (areaTest < totalArea/2) {
                    b = midPoint;
                } else {
                    a = midPoint;
                }
            }
        }
    }

    public static double area(double[][] v, int limit) {
        double a = 0;
        for (int i = 0; i < limit - 1; i++) {
            a += v[i][0] * v[i + 1][1] - v[i + 1][0] * v[i][1];
        }
        return Math.abs(a + v[limit - 1][0] * v[0][1] - v[0][0] * v[limit - 1][1]) / 2.0;
    }

    public static double triangleArea(double[] p1, double[] p2, double[] p3) {
        return Math.abs((p1[0] * (p2[1] - p3[1]) + p2[0] * (p3[1] - p1[1]) + p3[0] * (p1[1] - p2[1])) / 2.0);
    }

    public static boolean almostEqual(double A, double B) {
        double diff = Math.abs(A-B);
        A = Math.abs(A);
        B = Math.abs(B);
        double largest = A > B ? A : B;

        return diff <= largest*Math.ulp(1.0);
    }
}
