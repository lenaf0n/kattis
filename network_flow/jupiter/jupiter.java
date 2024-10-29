package network_flow.jupiter;

import java.util.*;

public class jupiter {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt(); // Number of downlink windows
        int q = scanner.nextInt(); // Number of queues
        int s = scanner.nextInt(); // Number of sensors

        List<Integer> sensorToQueue = new ArrayList<>(s);
        List<Integer> queueCapacity = new ArrayList<>(q);

        for (int i = 0; i < s; i++) {
            sensorToQueue.add(scanner.nextInt() - 1); // Link each sensor to its queue
        }

        for (int i = 0; i < q; i++) {
            queueCapacity.add(scanner.nextInt()); // Capacity of each queue
        }

        int totalNodes = s * n + 2 * q * n + n + 2;
        int source = totalNodes - 2;
        int sink = totalNodes - 1;

        MaxFlow maxFlow = new MaxFlow(totalNodes);
        long totalData = 0;

        for (int t = 0; t < n; t++) {
            int windowCapacity = scanner.nextInt();
            int windowNode = s * n + 2 * q * n + t;

            // donwlink to sink
            maxFlow.addEdge(windowNode, sink, windowCapacity);

            for (int i = 0; i < s; i++) {
                long data = scanner.nextInt();
                totalData += data;
                int sensorNode = t * s + i;
                int queueNode = s * n + t * q + sensorToQueue.get(i);

                // Source to sensor
                maxFlow.addEdge(source, sensorNode, data);

                // Sensor to queue
                maxFlow.addEdge(sensorNode, queueNode, queueCapacity.get(sensorToQueue.get(i)));
            }

            for (int j = 0; j < q; j++) {
                int queueNode = s * n + t * q + j;
                int sumQueueNode = s*n + n*q + j;

                // Queue to summing queue
                maxFlow.addEdge(queueNode, sumQueueNode, queueCapacity.get(j));

                //summing queue to window
                maxFlow.addEdge(sumQueueNode, windowNode, queueCapacity.get(j));

                //save mb for future
                if (t < n - 1) {
                    int nextQueueNode = s * n + (t + 1) * q + j;
                    maxFlow.addEdge(sumQueueNode, nextQueueNode, queueCapacity.get(j)); // Allow back-coupling
                }
            }
        }

        // Calculate max flow and check if it matches total data
        long flow = maxFlow.dinic(source, sink);
        System.out.println(flow == totalData ? "possible" : "impossible");
    }
}

class MaxFlow {
    int n;
    List<Edge> edges;
    List<Integer>[] graph;
    int[] lvl;
    int[] ptr;

    class Edge {
        int from, to;
        long capacity, flow;

        Edge(int from, int to, long capacity) {
            this.from = from;
            this.to = to;
            this.capacity = capacity;
            this.flow = 0;
        }
    }

    MaxFlow(int nodes) {
        graph = new ArrayList[nodes];
        edges = new ArrayList<>();
        lvl = new int[nodes];
        ptr = new int[nodes];

        for (int i = 0; i < nodes; i++) {
            graph[i] = new ArrayList<>();
        }
    }

    public void addEdge(int cur, int v, long capacity) {
        if (cur == v) return;

        Edge forwardEdge = new Edge(cur, v, capacity);
        Edge backwardEdge = new Edge(v, cur, 0);
        graph[cur].add(edges.size());
        edges.add(forwardEdge);
        graph[v].add(edges.size());
        edges.add(backwardEdge);
    }

    public long dinic(int s, int t) {
        long maxFlow = 0;
        while (bfs(s,t)) {
            Arrays.fill(ptr, 0);
            long flow;
            while ((flow = dfs(s, Long.MAX_VALUE, t)) > 0) {
                maxFlow += flow;
            }
        }
        return maxFlow;
    }

    private boolean bfs(int s, int t) {
        Arrays.fill(lvl, -1);
        lvl[s] = 0;

        Queue<Integer> queue = new LinkedList<>();
        queue.add(s);

        while (!queue.isEmpty()) {
            int cur = queue.poll();
            for (int idx : graph[cur]) {
                Edge edge = edges.get(idx);
                int next = edge.to;

                if (edge.flow < edge.capacity && lvl[next] == -1) {
                    lvl[next] = lvl[cur] + 1;
                    queue.add(next);
                    if (next == t) return true;
                }
            }
        }
        return lvl[t] != -1;
    }

    private long dfs(int v, long flow, int t) {
        if (flow == 0 || v == t) return flow;
        for (; ptr[v] < graph[v].size(); ++ptr[v]) {
            Edge edge = edges.get(graph[v].get(ptr[v]));
            if (lvl[edge.to] != lvl[v] + 1) continue;
            long pushed = dfs(edge.to, Math.min(flow, edge.capacity - edge.flow), t);
            if (pushed > 0) {
                edge.flow += pushed;
                edges.get(graph[v].get(ptr[v]) ^ 1).flow -= pushed;
                return pushed;
            }
        }
        return 0;
    }
}