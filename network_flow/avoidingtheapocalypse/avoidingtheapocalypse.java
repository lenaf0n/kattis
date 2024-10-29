package network_flow.avoidingtheapocalypse;

import java.util.*;

public class avoidingtheapocalypse {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int numTest = sc.nextInt();

        for (int i = 0; i < numTest; i++) {
            int n = sc.nextInt();

            int start = sc.nextInt() - 1;
            int g = sc.nextInt();
            int s = sc.nextInt() + 1;

            int m = sc.nextInt();
            int[] hospitals = new int[m];
            for (int j = 0; j < m; j++) {
                hospitals[j] = sc.nextInt() - 1;
            }

            int r = sc.nextInt();
            ArrayList<ArrayList<Integer>> roads = new ArrayList<>();
            for (int j = 0; j < r; j++) {
                ArrayList<Integer> road = new ArrayList<>();

                road.add(sc.nextInt()-1);
                road.add(sc.nextInt()-1);
                road.add(sc.nextInt());
                road.add(sc.nextInt());
                roads.add(road);
            }

            int totalNodes = n*s + 2;
            int source = totalNodes - 1;
            int sink = totalNodes - 2;

            DinicMaxFlow dinic = new DinicMaxFlow(totalNodes);
            dinic.addEdge(source, start, g);

            for (int j = 1; j < s; j++) {
                int t1 = (j - 1)*n;
                int t2 = j*n;
                for (int k = 1; k < n; k++) {
                    dinic.addEdge(t1 + k, t2 + k, Integer.MAX_VALUE);
                }
            }

            for (ArrayList<Integer> road : roads) {
                for (int j = 0; j < s; j++) {
                    if (j + road.get(3) >= s) {
                        break;
                    }
                    int t1 = j;
                    int t2 = j + road.get(3);
                    dinic.addEdge(t1*n + road.get(0), t2*n + road.get(1), road.get(2));
                }
            }

            for (int hos : hospitals) {
                for (int j = 0; j < s; j++) {
                    dinic.addEdge(j*n + hos, sink, Integer.MAX_VALUE);
                }
            }

            int mf = dinic.maxFlow(source, sink);
            System.out.println(mf);
        }
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
