package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	
	private List<String> cities;
	private YelpDao dao;
	private Graph<Business, DefaultWeightedEdge> grafo;
	private Map<String, Business> idMap;
	private List<Business> vertici;
	
	private List<Business> best;
	
	public Model() {
		this.dao = new YelpDao();
	}
	
	public List<String> getCities() {
		if(this.cities==null)
			this.cities = dao.getCities();
		
		return this.cities;
	}
	
	public List<Business> getVertici() {
		return this.vertici;
	}
	
	public String creaGrafo(String city, int anno) {
		this.grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		this.vertici = dao.getVertici(city, anno);
		Collections.sort(this.vertici);
		Graphs.addAllVertices(grafo, this.vertici);
		
		this.idMap = new HashMap<>();
		for(Business b : vertici) {
			idMap.put(b.getBusinessId(), b);
		}
		
		for(Adiacenza a : dao.getArchi(city, anno, idMap)) {
			Graphs.addEdge(grafo, a.getB1(), a.getB2(), a.getPeso());
		}
		
		return "Grafo creato con "+grafo.vertexSet().size()+" vertici e "+grafo.edgeSet().size()+" archi";
	}
	
	
	public Business getLocaleMigliore() {
		if(this.grafo==null)
			return null;
		
		Business locale = null;
		double max = -100.0;
		for(Business b : this.grafo.vertexSet()) {
			double somma = 0.0;

			for(DefaultWeightedEdge e : this.grafo.incomingEdgesOf(b)) {
				somma += this.grafo.getEdgeWeight(e);
			}
			
			for(DefaultWeightedEdge e : this.grafo.outgoingEdgesOf(b)) {
				somma -= this.grafo.getEdgeWeight(e);
			}
			
			if(somma > max) {
				max = somma;
				locale = b;
			}
		}
		return locale;
	}
	
	public List<Business> calcolaPercorso(Business start, double soglia) {
		this.best = null;
		List<Business> parziale = new ArrayList<>();
		parziale.add(start);
		Business arrivo = getLocaleMigliore();
		cerca(parziale, soglia, arrivo);
		return this.best;
	}

	private void cerca(List<Business> parziale, double soglia, Business arrivo) {
		if(parziale.get(parziale.size()-1).equals(arrivo)) {
			if(this.best==null) {
				this.best = new ArrayList<>(parziale);
				return;
			} else if(parziale.size() < best.size()) {
				this.best = new ArrayList<>(parziale);
				return;
			} else {
				return;
			}
		}
		
		for(DefaultWeightedEdge e : this.grafo.outgoingEdgesOf(parziale.get(parziale.size()-1))) {
			if(this.grafo.getEdgeWeight(e)>=soglia) {
				Business prossimo = Graphs.getOppositeVertex(grafo, e, parziale.get(parziale.size()-1));
				if(!parziale.contains(prossimo)) {
					parziale.add(prossimo);
					cerca(parziale, soglia, arrivo);
					parziale.remove(parziale.get(parziale.size()-1));
				}
			}
		}
	}
	
}
