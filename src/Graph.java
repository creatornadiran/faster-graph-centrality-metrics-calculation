import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.TreeMap;

public class Graph { // Unweighted & Undirected Graph Class
	private ArrayList<String> vertices; // to keep vertex names
	private boolean[][] adjacency; // to keep edges
	private int size;

	public Graph(int size) {
		assert size > 0;
		vertices = new ArrayList<String>();
		adjacency = new boolean[size][size];
		this.size = size;
	}

	public void addEdge(String source, String destination) { // Adds edge to the Graph between specified nodes

		if (!vertices.contains(source)) {
			checkSize();
			vertices.add(source);
		}

		if (!vertices.contains(destination)) {
			checkSize();
			vertices.add(destination);
		}

		int source_index = vertices.indexOf(source);
		int destination_index = vertices.indexOf(destination);
		adjacency[source_index][destination_index] = true;
		adjacency[destination_index][source_index] = true;
	}

	private void checkSize() { // Checks whether the size is exceeded
		if (vertices.size() == size) {
			throw new IllegalStateException("Maximum capacity exceeded");
		}
	}

	public int size() { // Returns the size of the graph.
		return this.size;
	}

	public boolean[][] getAdjacency() { // Return Adjacency list
		return adjacency;
	}

	public ArrayList<String> getVertices() { // Return Vertices list
		return vertices;
	}

	public void print() { // Prints All the Edges, total edges and total weights
		for (String v : vertices) {
			System.out.print("\t(" + v + ")");
		}
		System.out.println();
		for (int i = 0; i < vertices.size(); i++) {
			System.out.print("(" + vertices.get(i) + ")\t");
			for (int j = 0; j < adjacency.length; j++) {
				System.out.print(adjacency[i][j] + "\t");
			}
			System.out.println();
		}
		System.out.println("Edges");
		int edge_count = 0;
		int total_weight = 0;
		for (int i = 0; i < vertices.size(); i++) {
			for (int j = 0; j < vertices.size(); j++) {
				if (adjacency[i][j]) {
					System.out.println(vertices.get(i) + "-" + vertices.get(j) + ":" + adjacency[i][j]);
					edge_count++;
					total_weight += 1;
				}
			}
		}
		System.out.println("Total " + edge_count + " edges.");
		System.out.println("Total weight is " + total_weight);
		System.out.println();
	}

	public void printTopTenBetweennessCentrality() { // Prints Top 10 Betweenness Centrality scores and nodes
		TreeMap<Double, Integer> treeMap = BetweennessCentrality();
		System.out.println("Highest Top 10 Node By Betweenness Centrality is:");
		for (int i = 0; i < 10; i++) {
			System.out.println("Node: " + vertices.get(treeMap.lastEntry().getValue()) + " Score: "
					+ treeMap.lastEntry().getKey());
			treeMap.pollLastEntry();
		}
	}

	public void printTopTenClosenessCentrality() { // Prints Top 10 Closeness Centrality scores and nodes
		TreeMap<Double, Integer> treeMap = ClosenessCentrality();
		System.out.println("Highest Top 10 Node By Closeness Centrality is:");
		for (int i = 0; i < 10; i++) {
			System.out.println("Node: " + vertices.get(treeMap.lastEntry().getValue()) + " Score: "
					+ treeMap.lastEntry().getKey());
			treeMap.pollLastEntry();
		}
	}

	public void printHighestBetweennessCentrality() { // Prints highest node for Betweenness Centrality and the value
		Entry<Double, Integer> highestEntry = BetweennessCentrality().lastEntry();
		System.out.println("Highest node is: " + vertices.get(highestEntry.getValue())
				+ " by Betweenness Centrality Metric. It has " + highestEntry.getKey() + " score.");
	}

	public void printHighestClosenessCentrality() { // Prints highest node for Closeness Centrality and the value
		Entry<Double, Integer> highestEntry = ClosenessCentrality().lastEntry();
		System.out.println("Highest node is: " + vertices.get(highestEntry.getValue())
				+ " by Closeness Centrality Metric. It has " + highestEntry.getKey() + " score.");
	}

	public TreeMap<Double, Integer> ClosenessCentrality() { // Returns Betweenness Closeness scores of nodes in order
		TreeMap<Double, Integer> tree_map = new TreeMap<Double, Integer>();
		double sum;
		for (int i = 0; i < vertices.size(); i++) {
			sum = Arrays.stream(getRangeList(i)).sum();
			if (sum != 1) {
				tree_map.put(1 / sum, i);
			}
		}
		return tree_map;
	}

	public TreeMap<Double, Integer> BetweennessCentrality() { // Returns Betweenness Centrality scores of nodes in order
		TreeMap<Double, Integer> tree_map = new TreeMap<Double, Integer>();
		HashMap<Integer, Double> map = new HashMap<Integer, Double>();
		int[] prevList;
		for (int i = 0; i < vertices.size() - 1; i++) {
			prevList = returnPrevList(i);
			for (int k = i + 1; k < prevList.length; k++) {
				if (map.containsKey(k))
					map.replace(k, map.get(k) + 1);
				else
					map.put(k, 1.0);
				int m = prevList[k];
				while (m != -1) {
					if (map.containsKey(m))
						map.replace(m, map.get(m) + 1);
					else
						map.put(m, 1.0);
					m = prevList[m];
				}
			}
		}
		for (int i = 0; i < vertices.size(); i++) {
			if (!map.containsKey(i))
				continue;
			double val = map.get(i);
			tree_map.put(val, i);
		}

		return tree_map;

	}

	private int[] returnPrevList(int from) { // Returns previous list
		Queue<Integer> queue = new LinkedList<>();
		int root = from;
		queue.add(root);
		int[] visited = new int[vertices.size()];
		int[] prev = new int[vertices.size()];
		Arrays.fill(prev, -1);
		visited[root] = 1;
		while (!queue.isEmpty()) {
			int current_vertex = queue.poll(); // the top element is selected and removed from queue
			int v;
			while ((v = unvisitedNeighbor(current_vertex, visited)) != -1) {
				queue.add(v);
				visited[v] = 1;
				prev[v] = current_vertex;
			}
		}
		return prev;
	}

	private double[] getRangeList(int from) { // Returns a list that keeps all the distance to specified node of
												// vertices
		Queue<Integer> queue = new LinkedList<>();
		int root = from;
		queue.add(root);
		int[] visited = new int[vertices.size()];
		double[] range = new double[vertices.size()];
		range[from] = 0;
		visited[root] = 1;
		while (!queue.isEmpty()) {
			int current_vertex = queue.poll(); // the top element is selected and removed from queue
			int v;
			while ((v = unvisitedNeighbor(current_vertex, visited)) != -1) {
				queue.add(v);
				visited[v] = 1;
				range[v] = range[current_vertex] + 1;
			}
		}
		return range;
	}

	public void BFS(String from, String to) { // (For End user)Finds shortest path between specified nodes using BFS algorithm.
		Queue<Integer> queue = new LinkedList<>();
		int root = vertices.indexOf(from);
		queue.add(root);
		int[] visited = new int[vertices.size()];
		int[] prev = new int[vertices.size()];
		Arrays.fill(prev, -1);
		visited[root] = 1;
		System.out.println("Breadthfirst search for " + from + " to " + to + " vertex:");
		while (!queue.isEmpty()) {
			int current_vertex = queue.poll(); // the top element is selected and removed from queue
			int v;
			while ((v = unvisitedNeighbor(current_vertex, visited)) != -1) {
				queue.add(v);
				visited[v] = 1;
				prev[v] = current_vertex;
				if (v == vertices.indexOf(to)) {
					queue.clear();
					break;
				}
			}
		}
		print_route(vertices.indexOf(to), prev);
	}

	private int unvisitedNeighbor(int index, int[] visited) { // Returns whether the specified index visited or not
		for (int i = 0; i < adjacency.length; i++) {
			if (adjacency[index][i] && visited[i] == 0)
				return i;
		}
		return -1;
	}

	private void print_route(int index_of_to, int[] prev) { // Prints route, used with BFS for user (Prints names of vertices)
		int node = index_of_to;
		List<String> route = new ArrayList<>();
		while (node != -1) {
			route.add(vertices.get(node));
			node = prev[node];
		}
		Collections.reverse(route);
		System.out.println(route);
		;
	}
}
