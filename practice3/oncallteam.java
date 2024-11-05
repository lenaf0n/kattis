package practice3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class oncallteam {
    private static int n, m;
    private static int[][] familiarity;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String[] input = br.readLine().split(" ");
        n = Integer.parseInt(input[0]);
        m = Integer.parseInt(input[0]);

        familiarity = new int[n][m];
        for (int i = 0; i < n; i++) {
            String line = br.readLine();
            for (int j = 0; j < m; j++) {
                familiarity[i][j] = line.charAt(j) - '0';
            }
        }

        br.close();

        int left = 1;
        int right = Math.min(n, m);

        int maxK = 1;
        while (left <= right) {
            int mid = left + (right - left)/2;
            if (canHandleAllCombinationsOfK(mid)) {
                maxK = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        System.out.println(maxK);
    }

    private static boolean canHandleAllCombinationsOfK(int k) {
        List<int[]> combinations = generateCombinations(m, k);
        for (int[] combo : combinations) {
            if (!canHandleCombination(combo)) {
                return false;
            }
        }
        return true;
    }

    private static boolean canHandleCombination(int[] combo) {
        int source = m + n;
        int sink = source + 1;
        int totalNodes = m + n + 2;

        DinicMaxFlow maxFlow = new DinicMaxFlow(totalNodes);

        for (int service : combo) {
            maxFlow.addEdge(source, service, 1);
        }

        for (int i = 0; i < n; i++) {
            for (int service : combo) {
                if (familiarity[i][service] == 1) {
                    maxFlow.addEdge(service, m + i, 1);
                }
            }
            maxFlow.addEdge(m + i, sink, 1);
        }

        int flow = maxFlow.maxFlow(source, sink);
        return flow == combo.length;
    }

    private static List<int[]> generateCombinations(int m, int k) {
        List<int[]> combinations = new ArrayList<>();
        int[] combo = new int[k];
        generateCombinationsHelper(combinations, combo, 0, m, 0, k);
        return combinations;
    }

    private static void generateCombinationsHelper(List<int[]> combinations, int[] combo, int start, int end, int index, int k) {
        if (index == k) {
            combinations.add(combo.clone());
            return;
        }
        for (int i = start; i < end && end - i >= k - index; i++) {
            combo[index] = i;
            generateCombinationsHelper(combinations, combo, i + 1, end, index + 1, k);
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
