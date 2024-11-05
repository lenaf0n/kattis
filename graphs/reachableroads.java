package graphs;

import java.util.ArrayList;
import java.util.Scanner;

public class reachableroads {
    static ArrayList<Integer> visited;
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();
        for (int i = 0; i < n; i++) {
            ArrayList<ArrayList<Integer>> adj = new ArrayList<>();
            visited = new ArrayList<>();

            int m = sc.nextInt();
            for (int j = 0; j < m; j++) {
                adj.add(new ArrayList<>());
                visited.add(0);
            }

            int r = sc.nextInt();
            for (int j = 0; j < r; j++) {
                int a = sc.nextInt();
                int b = sc.nextInt();
                adj.get(a).add(b);
                adj.get(b).add(a);
            }

            int count = 0;
            for (int j = 0; j < adj.size(); j++) {
                if (visited.get(j) == 0) {
                    count++;
                    DFSRecursive(adj, j);
                }
            }

            System.out.println(count-1);
        }
    }

    public static void DFSRecursive(ArrayList<ArrayList<Integer>> adj, int curr) {
        if (visited.get(curr) == 1) {
            return;
        }

        visited.set(curr, 1);

        for (int neighbor : adj.get(curr)) {
            if (visited.get(neighbor) == 0) {
                DFSRecursive(adj, neighbor);
            }
        }
    }
}
