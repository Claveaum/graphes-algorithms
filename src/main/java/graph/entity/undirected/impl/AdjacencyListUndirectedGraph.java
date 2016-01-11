package graph.entity.undirected.impl;

import graph.entity.undirected.AbstractUndirectedGraph;
import graph.util.ListConverter;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class AdjacencyListUndirectedGraph extends AbstractUndirectedGraph {

	private List<Entry<Integer, List<Integer>>> adjacencyList = new ArrayList<Entry<Integer, List<Integer>>>();
	private AdjacencyMatrixUndirectedGraph adjacencyMatrix;

	public AdjacencyListUndirectedGraph(AdjacencyMatrixUndirectedGraph adjacencyMatrix) {
		this.adjacencyMatrix = adjacencyMatrix; // still useful to backup the adjacency matrix
		this.order = adjacencyMatrix.getOrder();
		this.nbEdges = adjacencyMatrix.getNbEdges();
		int succInc = 0; // enable count of vertex's successors

		// build the adjacency list by iterating through each vertex and looking for its successors (i.e neighbors)
		for(int i=0; i<this.order; i++) {
			int[] neighbors = adjacencyMatrix.getNeighbors(i);

			adjacencyList.add(new AbstractMap.SimpleEntry<Integer, List<Integer>>(succInc, ListConverter.toList(neighbors)));
			succInc+=neighbors.length;
		}
	}

	@Override
	public int[][] getGraph() {
		return this.adjacencyMatrix.getGraph();
	}

	@Override
	public int[] getNeighbors(int x) {
		return ListConverter.toArray(this.adjacencyList.get(x).getValue());
	}

	@Override
	public boolean isEdge(int x, int y) {
		return this.adjacencyList.get(x).getValue().contains(y);
	}

	@Override
	public int addVertex() {
		this.adjacencyList.add(new AbstractMap.SimpleEntry<Integer, List<Integer>>(++this.order, new ArrayList<>()));
		return this.order;
	}

	@Override
	public void removeEdge(int x, int y) {
		this.adjacencyList.get(x).getValue().remove(new Integer(y));
		this.nbEdges--;
	}

	@Override
	public void addEdge(int x, int y) {
		this.adjacencyList.get(x).getValue().add(new Integer(y));
		this.nbEdges++;
	}

	@Override
	public void display() {
		for(Entry<Integer, List<Integer>> node : this.adjacencyList) {
			System.out.print(node.getKey() + " ==> ");	    	
			node.getValue().stream().forEach(s -> System.out.print(s + " "));
			System.out.println();
		}
	}

	public List<Entry<Integer, List<Integer>>> getAdjacencyList() {
		return adjacencyList;
	}

	public void setAdjacencyList(List<Entry<Integer, List<Integer>>> adjacencyList) {
		this.adjacencyList = adjacencyList;
	}

}