package network_flow.transportation;

import java.util.*;

public class transportation {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int s = sc.nextInt();
        int r = sc.nextInt();
        int f = sc.nextInt();
        int t = sc.nextInt();

        int totalNodes = s + 2*t + 2;
        int source = totalNodes - 2;
        int sink = totalNodes - 1;

        DinicMaxFlow dinic = new DinicMaxFlow(totalNodes);

        Map<String, Integer> map = new HashMap<>();

        String[] rNodes = new String[r];
        for (int i = 0; i < r; i++) {
            rNodes[i] = sc.next();
            dinic.addEdge(source, i, 1);
            map.put(rNodes[i], i);
        }

        String[] fNodes = new String[f];
        for (int i = 0; i < f; i++) {
            fNodes[i] = sc.next();
            dinic.addEdge(r+i, sink, 1);
            map.put(fNodes[i], r+i);
        }

        int count = 0;
        int transIn = s;
        int transOut = s+t;

        for (int i = 0; i < t; i++) {
            int n = sc.nextInt();
            dinic.addEdge(transIn+i, transOut+i, 1);
            for (int j = 0; j < n; j++) {
                String state = sc.next();
                if (!map.containsKey(state)) {
                    map.put(state, r+f+count);
                    count++;
                }
                dinic.addEdge(map.get(state), i+transIn, 1);
                dinic.addEdge(transOut+i, map.get(state), 1);

            }
        }

        int mf = dinic.maxFlow(source, sink);
        System.out.println(mf);
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
