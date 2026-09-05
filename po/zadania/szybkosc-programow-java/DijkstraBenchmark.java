import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Scanner;

public class DijkstraBenchmark {
    static final long INF = 4_000_000_000_000_000_000L;

    static int n, m, k;
    static long seed;

    static ArrayList<ArrayList<int[]>> g;
    static int[] sources;

    static void addEdge(int u, int v, int w) {
        g.get(u).add(new int[] {v, w});
        g.get(v).add(new int[] {u, w});
    }

    static void readGraph(String fileName) throws Exception {
        Scanner fs = new Scanner(new BufferedInputStream(new FileInputStream(fileName)));

        n = fs.nextInt();
        m = fs.nextInt();
        k = fs.nextInt();
        seed = fs.nextLong();

        g = new ArrayList<>();
        for (int i = 0; i < n; i++) g.add(new ArrayList<>());

        sources = new int[k];
        for (int i = 0; i < k; i++) sources[i] = fs.nextInt();

        for (int i = 0; i < m; i++) {
            int u = fs.nextInt();
            int v = fs.nextInt();
            int w = fs.nextInt();
            addEdge(u, v, w);
        }

        fs.close();
    }

    static long dijkstra(int s) {
        long[] dist = new long[n];
        Arrays.fill(dist, INF);

        PriorityQueue<long[]> pq = new PriorityQueue<>((a, b) -> Long.compare(a[0], b[0]));
        dist[s] = 0;
        pq.add(new long[] {0, s});

        while (!pq.isEmpty()) {
            long[] top = pq.poll();
            long du = top[0];
            int u = (int) top[1];

            if (du != dist[u]) continue;

            for (int[] edge : g.get(u)) {
                int v = edge[0];
                int w = edge[1];
                if (dist[v] > du + w) {
                    dist[v] = du + w;
                    pq.add(new long[] {dist[v], v});
                }
            }
        }

        long sum = 0;
        for (long x : dist) sum += x;
        return sum;
    }

    public static void main(String[] args) throws Exception {
        String fileName = "graph.txt";
        if (args.length >= 1) fileName = args[0];
        readGraph(fileName);

        long start = System.nanoTime();

        long checksum = 0;
        for (int s : sources) checksum += dijkstra(s);

        long finish = System.nanoTime();
        double elapsedMs = (finish - start) / 1e6;

        System.out.println("algorithm=dijkstra");
        System.out.println("graph=undirected_sparse");
        System.out.println("input_file=" + fileName);
        System.out.println("vertices=" + n);
        System.out.println("undirected_edges=" + m);
        System.out.println("sources=" + k);
        System.out.println("seed=" + seed);
        System.out.println("checksum=" + checksum);
        System.out.printf("elapsed_ms=%.3f%n", elapsedMs);
    }
}
