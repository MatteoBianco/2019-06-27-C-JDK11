package it.polito.tdp.crimes.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	
	private EventsDao dao;
	private Graph<String, DefaultWeightedEdge> graph;
	private List<OffenseIdEdge> edges;
	
	private List<String> bestPath;
	private Integer maxWeight;
	
	public Model() {
		this.dao = new EventsDao();
	}
	
	public void creaGrafo(String category, LocalDate date) {
		this.graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.edges = new ArrayList<>();
		
		Graphs.addAllVertices(this.graph, this.dao.getAllVertices(category, date));
		for(String v1 : this.graph.vertexSet()) {
			for(String v2 : this.graph.vertexSet()) {
				if(!v1.equals(v2) && this.graph.getEdge(v1, v2) == null) {
					Integer weight = this.dao.getWeightForVertices(v1, v2, category, date);
					if(weight != null) {
						if(weight != 0) {
							Graphs.addEdge(this.graph, v1, v2, weight);
							edges.add(new OffenseIdEdge(v1, v2, weight));
						}
					}
					else throw new RuntimeException ("Errore nella ricerca del peso dell'arco!\n");
				}
			}
				
		}
	}
	
	public List<String> getPath(OffenseIdEdge selected) {
		this.bestPath = new ArrayList<>();
		this.maxWeight = 0;
		String start = selected.getV1();
		String end = selected.getV2();
		List<String> parziale = new ArrayList<>();
		parziale.add(start);
		ricorsione(parziale, start, end, 0);		
		return this.bestPath;
	}
	
	private void ricorsione(List<String> parziale, String lastVertex, String end, Integer currentWeight) {
		if(lastVertex.equals(end)) {
			if(currentWeight > this.maxWeight) {
				this.bestPath = new ArrayList<> (parziale);
				this.maxWeight = currentWeight;
			}				
			return;
		}
		
		for(String v : Graphs.neighborListOf(this.graph, lastVertex)) {
			if(!parziale.contains(v)) {
				parziale.add(v);
				currentWeight += (int) this.graph.getEdgeWeight(this.graph.getEdge(v, lastVertex));
				ricorsione(parziale, v, end, currentWeight);
				parziale.remove(parziale.size() - 1);
				currentWeight -= (int) this.graph.getEdgeWeight(this.graph.getEdge(v, lastVertex));
			}
		}
		
	}

	public List<OffenseIdEdge> getEdgesBelowMedian() {
		if(this.edges.isEmpty())
			return null;
		List<OffenseIdEdge> result = new ArrayList<>();
		Integer max = this.calculateMaxWeight();
		Integer min = this.calculateMinWeight();
		Double median = ((double) (max+min)/2);
		for(OffenseIdEdge e : this.edges) {
			if(e.getWeight() <= median)
				result.add(e);
		}
		return result;
	}
	
	public Double getGraphWeightMedian() {
		Double median = ((double) (this.calculateMaxWeight() + this.calculateMinWeight())/2);
		return median;
	}
	
	private Integer calculateMinWeight() {
		Integer max = 0;
		for(OffenseIdEdge e : this.edges) {
			if(e.getWeight() > max)
				max = e.getWeight();
		}
		return max;
	}

	private Integer calculateMaxWeight() {
		Integer min = Integer.MAX_VALUE;
		for(OffenseIdEdge e : this.edges) {
			if(e.getWeight() < min)
				min = e.getWeight();
		}
		return min;
	}
	
	public Integer getVerticesSize() {
		return this.graph.vertexSet().size();
	}
	
	public Integer getEdgesSize() {
		return this.graph.edgeSet().size();
	}

	public List<String> getAllCategories() {
		return this.dao.getAllCategories();
	}
	
	public List<LocalDate> getAllDates() {
		return this.dao.getAllDates();
	}
}
