package unsorted;

import java.util.*;

public class warehouse {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int t = sc.nextInt();

        for (int i = 0; i < t; i++) {
            int n = sc.nextInt();
            List<Peir> map = new ArrayList<>();

            for (int j = 0; j < n; j++) {
                String word = sc.next();
                boolean added = false;

                for (Peir peir : map) {
                    if (peir.getFirst().equals(word)) {
                        peir.setSecond(peir.getSecond()+sc.nextInt());
                        added = true;
                    }
                }

                if (!added) {
                    map.add(new Peir(word, sc.nextInt()));
                }
            }

            Collections.sort(map);
            System.out.println(map.size());
            for (Peir peir : map) {
                System.out.println(peir.getFirst() + " " + peir.getSecond());
            }
        }
    }
}

class Peir implements Comparable<Peir> {
    private String first;
    private int second;

    public Peir(String first, int second) {
        this.first = first;
        this.second = second;
    }

    public String getFirst() {
        return first;
    }

    public int getSecond() {
        return second;
    }

    @Override
    public int compareTo(Peir other) {
        if (this.second > other.getSecond()) {
            return -1;
        } else if (this.second < other.getSecond()) {
            return 1;
        } else {
            return this.first.compareTo(other.getFirst());
        }
    }

    public void setSecond(int second) {
        this.second = second;
    }
}