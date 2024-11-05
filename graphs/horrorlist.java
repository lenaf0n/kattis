package graphs;

import java.util.*;

public class horrorlist {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();
        int h = sc.nextInt();
        int l = sc.nextInt();

        ArrayList<Integer> horrorList = new ArrayList<>();
        for (int i = 0; i < h; i++) {
            horrorList.add(sc.nextInt());
        }

        ArrayList<Integer>[] adjList = new ArrayList[n];
        int[] horrorIndex = new int[n];

        for (int i = 0; i < n; i++) {
            adjList[i] = new ArrayList<>();
            
            if (horrorList.contains(i)) {
                horrorIndex[i] = 0;
            } else {
                horrorIndex[i] = Integer.MAX_VALUE;
            }
        }

        for (int i = 0; i < l; i++) {
            int a = sc.nextInt();
            int b = sc.nextInt();
            adjList[a].add(b);
            adjList[b].add(a);
        }

        for ( int s : horrorList) {
            horrorIndex = bfs(adjList, horrorIndex, s);
        }

        int max = -1;
        int maxIndex = 0;

        for (int i = 0; i < horrorIndex.length; i++) {
            if (horrorIndex[i] > max) {
                max = horrorIndex[i];
                maxIndex = i;
            }
        }

        System.out.println(maxIndex);
    }

    private static int[] bfs(ArrayList<Integer>[] adjList, int[] horrorIndex, int s) {
        Queue<Integer> queue = new LinkedList<>();

        queue.add(s);

        while (!queue.isEmpty()) {
            int cur = queue.poll();

            for (int neighbor : adjList[cur]) {
                if (horrorIndex[neighbor] > horrorIndex[cur] + 1) {
                    queue.add(neighbor);
                    horrorIndex[neighbor] = horrorIndex[cur] + 1;
                }
            }
        }

        return horrorIndex;
    }
}
