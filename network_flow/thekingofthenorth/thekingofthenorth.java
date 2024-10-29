package network_flow.thekingofthenorth;

import java.util.*;

public class thekingofthenorth {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int r = sc.nextInt();
        int c = sc.nextInt();

        int[] dx = {0,1,0,-1};
        int[] dy = {1,0,-1,0};

        int totalNodes = 2*r*c + 1;
        DinicMaxFlow dinic = new DinicMaxFlow(totalNodes);
        int source = totalNodes - 1;

        int[][] grid = new int[r][c];

        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                if (i == 0 || j == 0 || i == r - 1 || j == c - 1) {
                    dinic.addEdge(source, index(i, j, c), Integer.MAX_VALUE);
                }

                grid[i][j] = sc.nextInt();
                dinic.addEdge(index(i, j, c), r*c+index(i, j, c), grid[i][j]);

                for (int d = 0; d < 4; d++) {
                    int x = i + dx[d];
                    int y = j + dy[d];
                    if (x < 0 || x == r || y < 0 || y == c) continue;
                    dinic.addEdge(r*c+index(i, j, c), index(x, y, c), Integer.MAX_VALUE);
                }

                
            }
        }

        int x = sc.nextInt();
        int y = sc.nextInt();
        sc.close();

        int sink = r*c+index(x, y, c);

        int mf = dinic.maxFlow(source, sink);
        System.out.println(mf);
    }

    private static int index(int x, int y, int c) {
        return x*c + y;
    }
}

class DinicMaxFlow {
    private static final int INF = 1_000_000_000;
    private int s, t;
    private List<Integer>[] graph;
    List<Edge> edges;
    private int[] level, ptr;

    static class Edge {
        int from, to, capacity, flow;

        Edge(int from, int to, int capacity) {
            this.from = from;
            this.to = to;
            this.capacity = capacity;
            this.flow = 0;
        }
    }

    public DinicMaxFlow(int nodeCount) {
        graph = new ArrayList[nodeCount];
        edges = new ArrayList<>();
        level = new int[nodeCount];
        ptr = new int[nodeCount];

        for (int i = 0; i < nodeCount; i++) {
            graph[i] = new ArrayList<>();
        }
    }

    public void addEdge(int from, int to, int capacity) {
        Edge forwardEdge = new Edge(from, to, capacity);
        Edge backwardEdge = new Edge(to, from, 0);
        graph[from].add(edges.size());
        edges.add(forwardEdge);
        graph[to].add(edges.size());
        edges.add(backwardEdge);
    }

    private boolean bfs() {
        Arrays.fill(level, -1);
        Queue<Integer> queue = new LinkedList<>();
        queue.add(s);
        level[s] = 0;
    
        while (!queue.isEmpty() && level[t] == -1) { 
            int currentNode = queue.poll();
            for (int edgeId : graph[currentNode]) {
                Edge edge = edges.get(edgeId);
                int nextNode = edge.to;
    
                if (level[nextNode] == -1 && edge.flow < edge.capacity) {
                    queue.add(nextNode);
                    level[nextNode] = level[currentNode] + 1;
                }
            }
        }
        return level[t] != -1;
    }
    

    private int dfs(int v, int flow) {
        if (flow == 0 || v == t) return flow;

        for (; ptr[v] < graph[v].size(); ++ptr[v]) {
            Edge edge = edges.get(graph[v].get(ptr[v]));
            if (level[edge.to] != level[v] + 1) continue;
            int pushed = dfs(edge.to, Math.min(flow, edge.capacity - edge.flow));
            if (pushed > 0) {
                edge.flow += pushed;
                edges.get(graph[v].get(ptr[v]) ^ 1).flow -= pushed;
                return pushed;
            }
        }
        return 0;
    }

    public int maxFlow(int source, int sink) {
        this.s = source;
        this.t = sink;
        int flow = 0;
        while (bfs()) {
            Arrays.fill(ptr, 0);
            int pushed;
            while ((pushed = dfs(source, INF)) > 0) {
                flow += pushed;
            }
        }
        return flow;
    }
}
