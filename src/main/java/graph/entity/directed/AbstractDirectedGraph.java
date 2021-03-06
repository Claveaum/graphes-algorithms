package graph.entity.directed;

import java.util.*;
import java.util.Map.Entry;

public abstract class AbstractDirectedGraph implements IDirectedGraph {

	protected int order;
	
	protected int nbEdges;

    private int[] start = new int[this.getOrder()];
    private int[] end = new int[this.getOrder()];
    private int time = 0;

	public static int[][] getRandomDirectedGraph(int ordre, int nbEdges) {
		int[][] adjacencyMatrix = new int[ordre][ordre];

		// Récupération de toutes les arêtes
		List<Entry<Integer, Integer>> edges = new ArrayList<>();
		for(int i=0; i<ordre; i++) {
			for(int j=0; j<ordre; j++) {
				if(i != j)
					edges.add(new AbstractMap.SimpleEntry<>(i, j));
			}
		}

		// Epurage pour sélection de seulement nbAretes arêtes
		List<Entry<Integer, Integer>> selectedEdges = new ArrayList<>();
		int nbEdgesPossible = edges.size();
		for(int i=0; i<nbEdges; i++) {
			int edgeToRemove = (int) Math.round(Math.random() * (--nbEdgesPossible));
			selectedEdges.add(edges.remove(edgeToRemove));
		}

		// Mise à jour de la matrice d'adjacence
		for(Map.Entry<Integer, Integer> edge : selectedEdges) {
			adjacencyMatrix[edge.getKey()][edge.getValue()] = 1;
		}

		return adjacencyMatrix;
	}

	@Override
	public List<Integer> breadthFirstSearch(int baseVertex) {
		List<Boolean> mark = new ArrayList<>(Collections.nCopies(this.getOrder(), Boolean.FALSE));
		mark.set(baseVertex, Boolean.TRUE);

		List<Integer> minDistance = new ArrayList<>(Collections.nCopies(this.getOrder(), 0));

		Queue<Integer> toVisit = new LinkedList<>();
		toVisit.add(baseVertex);

		while(!toVisit.isEmpty()) {
			int vertex = toVisit.poll();
			for(int neighbor : this.getSuccessors(vertex)) {
				if(Boolean.FALSE.equals(mark.get(neighbor))) {
					mark.set(neighbor, Boolean.TRUE);
					minDistance.set(neighbor, minDistance.get(vertex)+1);
					toVisit.add(neighbor);
				}
			}
		}

		return minDistance;
	}

    // FIXME il faut récupérer toutes les composantes fortements connexes (voir explorerGraph())
    // TODO stocker start & end
	@Override
	public void depthFirstSearch(int baseVertex) {
		List<Boolean> mark = new ArrayList<>(Collections.nCopies(this.getOrder(), Boolean.FALSE));
		mark.set(baseVertex, Boolean.TRUE);

		Stack<Integer> toVisit = new Stack<>();
		toVisit.push(baseVertex);

		while(!toVisit.isEmpty()) {
            int vertex = toVisit.pop();
            for (int neighbor : this.getSuccessors(vertex)) {
                if (Boolean.FALSE.equals(mark.get(neighbor))) {
                    mark.set(neighbor, Boolean.TRUE);
                    toVisit.push(neighbor);
                }
            }
        }
	}

    Comparator<Map.Entry<Integer, Integer>> byMapValues = (left, right) -> left.getValue().compareTo(right.getValue());

	@Override
	public List<IDirectedGraph> computeConnectedGraphs() {
        int baseVertex = (int)(Math.random() * this.getOrder()); // choose base vertex randomly
		this.depthFirstSearch(baseVertex);

        IDirectedGraph inverse = this.inverse();

        List<Map.Entry<Integer, Integer>> endMap = new ArrayList<>();
        for(int i=0; i<end.length; i++) {
            endMap.add(new AbstractMap.SimpleEntry<>(i, end[i]));
        }

        // reverse order for array end
        Collections.sort(endMap, byMapValues.reversed());

        for(Map.Entry<Integer, Integer> end : endMap) {
            inverse.depthFirstSearch(end.getKey());
        }

        return null;

	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public int getNbEdges() {
		return nbEdges;
	}

	public void setNbEdges(int nbEdges) {
		this.nbEdges = nbEdges;
	}
}
