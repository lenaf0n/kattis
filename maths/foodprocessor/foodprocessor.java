package maths.foodprocessor;

import java.util.*;

public class foodprocessor {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int oSize = sc.nextInt();
        int fSize = sc.nextInt();
        int number = sc.nextInt();

        ArrayList<ArrayList<Integer>> blades = new ArrayList<>();

        for (int i = 0; i < number; i++) {
            int m = sc.nextInt();
            int h = sc.nextInt();

            ArrayList<Integer> blade = new ArrayList<>();
            blade.add(m);
            blade.add(h);
            blades.add(blade);
        }
        
        sc.close();
        
        blades.sort(new Comparator<ArrayList<Integer>>() {
            @Override
            public int compare(ArrayList<Integer> o1, ArrayList<Integer> o2) {
                if (o1.get(1) < o2.get(1)) {
                    return -1;
                } else if (o1.get(1) > o2.get(1)) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });

        int cSize = oSize;
        double time = 0;

        int start = -1;
        for (int i = 0; i < blades.size(); i++) {
            if (blades.get(i).get(0) >= oSize) {
                start = i;
                break;
            }
        }

        if (start == -1) {
            System.out.println(-1);
        } else {
            for (int i = start; i >= 0; i--) {
                if (cSize <= fSize) {
                    break;
                }
                
                int nextTarget;
                if (i != 0) {
                    nextTarget = Math.max(fSize, blades.get(i-1).get(0));
                } else {
                    nextTarget = fSize;
                }

                time += Math.log((double)cSize/nextTarget)/Math.log(2)*blades.get(i).get(1);
                cSize = nextTarget;
            }
            System.out.println(time);
        }
    }
}
