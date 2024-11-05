package graphs;

import java.util.*;

public class grid {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();
        int m = sc.nextInt();

        int[][] grid = new int[n][m];
        int[][] visited = new int[n][m];
     

        for (int i = 0; i < n; i++) {
            String input = sc.next();
            for (int j = 0; j < input.length(); j++) {
                grid[i][j] = input.charAt(j) - '0';
                visited[i][j] = 0;
            }
        }

        System.out.println(bfs(grid, visited, n, m));
    }

    private static int bfs(int[][] grid, int[][] visited, int n, int m) {
        int[][] directions = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
        Queue<int[]> queue = new LinkedList<>();

        queue.add(new int[]{0,0,0});
        visited[0][0] = 1;

        while (!queue.isEmpty()) {
            int[] cur = queue.poll();
            int x = cur[0];
            int y = cur[1];
            int moves = cur[2];
            int jump = grid[x][y];

            if (x == n - 1 && y == m - 1) {
                return moves;
            }

            for (int[] dir : directions) {
                int newX = x + dir[0] * jump;
                int newY = y + dir[1] * jump;

                if (newX >= 0 && newX < n && newY >= 0 && newY < m && visited[newX][newY] == 0) {
                    visited[newX][newY] = 1;
                    queue.add(new int[]{newX, newY, moves + 1});
                }
            }
        }

        return -1;
    }
}
