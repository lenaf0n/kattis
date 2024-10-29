package network_flow.mazemovement;

import java.util.*;

public class mazemovement {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();

        int[] list = new int[n];

        int minIndex = 0;
        int maxIndex = 0;

        for (int i = 0; i < n; i++) {
            int value = sc.nextInt();
            list[i] = value;

            if (list[i] < list[minIndex]) {
                minIndex = i; 
            }
            if (list[i] > list[maxIndex]) {
                maxIndex = i;
            }
        }

        sc.close();

        DinicMaxFlow dinic = new DinicMaxFlow(n+2);

        for (int i = 0; i < n; i++) {
            for (int j = i+1; j < n; j++) {
                int gcd = gcd(list[i], list[j]);
                if (gcd > 1) {
                    dinic.addEdge(i, j, gcd);
                }
            }
        }

        dinic.addEdge(n, minIndex, Integer.MAX_VALUE);
        dinic.addEdge(maxIndex, n+1, Integer.MAX_VALUE);


        long mf = dinic.maxFlow(n, n+1);
        System.out.println(mf);
    }

    public static int gcd(int a, int b) {return b == 0 ? a : gcd(b, a%b);}
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
        Edge backwardEdge = new Edge(to, from, capacity);
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
