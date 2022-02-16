import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Test {
	public static Graph loadGraphFromFile(String path, int size) {
		/*
		 * Crates Graph object with specified size. Reads entries from "path" then adds
		 * them to the graph. Returns the graph.
		 */
		Graph gr = new Graph(size);
		try {
			File file = new File(path);
			@SuppressWarnings("resource")
			Scanner sc = new Scanner(file);
			while (sc.hasNext()) {
				gr.addEdge(sc.next(), sc.next());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return gr;
	}

	public static void main(String[] args) {
		Graph graph_fb = loadGraphFromFile(
				"C:/Users/ayn_2/eclipse-workspace/Java/HomeWork3/src/facebook_social_network.txt", 1518);
		Graph graph_kc = loadGraphFromFile(
				"C:/Users/ayn_2/eclipse-workspace/Java/HomeWork3/src/karate_club_network.txt", 34);
		System.out.println("-For Facebook Social Network Graph-");
		graph_fb.printHighestBetweennessCentrality();
		// graph_fb.printTopTenBetweennessCentrality();
		graph_fb.printHighestClosenessCentrality();
		// graph_fb.printTopTenClosenessCentrality();
		System.out.println("-For Karate Club Network Graph-");
		graph_kc.printHighestBetweennessCentrality();
		// graph_kc.printTopTenBetweennessCentrality();
		graph_kc.printHighestClosenessCentrality();
		// graph_kc.printTopTenClosenessCentrality();
	}
}
