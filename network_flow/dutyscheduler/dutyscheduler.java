package network_flow.dutyscheduler;

import java.util.*;

public class dutyscheduler {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int m = sc.nextInt(); 
        int n = sc.nextInt();

        Map<String, List<Integer>> raPreferences = new HashMap<>();
        Map<String, Integer> raToIndex = new HashMap<>();
        List<String> indexToRa = new ArrayList<>();

        for (int i = 0; i < m; i++) {
            String raName = sc.next();
            int t = sc.nextInt();
            List<Integer> days = new ArrayList<>();
            for (int j = 0; j < t; j++) {
                days.add(sc.nextInt());
            }
            raPreferences.put(raName, days);
            indexToRa.add(raName);
            raToIndex.put(raName, i);
        }

        for (int maxDays = 1; maxDays <= n; maxDays++) {
            if (canSchedule(m, n, maxDays, raPreferences, indexToRa, raToIndex)) {
                break;
            }
        }

        sc.close();
    }

    private static boolean canSchedule(int raCount, int monthCount, int maxDays, Map<String, List<Integer>> raPreferences,
                                       List<String> indexToRa, Map<String, Integer> raToIndex) {
        int n = raCount + monthCount + 2;
        DinicMaxFlow flowNetwork = new DinicMaxFlow(n);
        int source = n-2;
        int sink = n-1;

        for (int i = 0; i < raCount; i++) {
            flowNetwork.addEdge(source, i, maxDays);
        }

        for (int i = 0; i < monthCount; i++) {
            flowNetwork.addEdge(raCount + i, sink, 2);
        }

        for (Map.Entry<String, List<Integer>> entry : raPreferences.entrySet()) {
            int raIndex = raToIndex.get(entry.getKey());
            for (int day : entry.getValue()) {
                flowNetwork.addEdge(raIndex, raCount + day - 1, 1);
            }
        }

        int flow = flowNetwork.maxFlow(source, sink);
        if (flow == monthCount * 2) {
            System.out.println(maxDays);
            printSchedule(flowNetwork, raCount, monthCount, indexToRa);
            return true;
        }

        return false;
    }

    private static void printSchedule(DinicMaxFlow flowNetwork, int raCount, int monthCount, List<String> indexToRa) {
        List<List<String>> schedule = new ArrayList<>(monthCount);
        for (int i = 0; i < monthCount; i++) {
            schedule.add(new ArrayList<>());
        }

        for (DinicMaxFlow.Edge edge : flowNetwork.edges) {
            if (edge.from < raCount && edge.flow == 1) {
                schedule.get(edge.to - raCount).add(indexToRa.get(edge.from));
            }
        }

        for (int i = 0; i < monthCount; i++) {
            System.out.print("Day " + (i + 1) + ": ");
            for (String ra : schedule.get(i)) {
                System.out.print(ra + " ");
            }
            System.out.println();
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