package it.polito.tdp.artsmia.model;

import java.util.HashMap;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {

	private Graph<ArtObject, DefaultWeightedEdge> grafo;
	// per assicurarci di avere i vertici una sola volta creo una IdMap
	private Map<Integer, ArtObject> idMap;

	public Model() {
		// il grafo deve essere semplice, orientato e non pesato
		idMap = new HashMap<Integer, ArtObject>();
	}

	public void creaGrafo() {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);

		// aggiungere i vertici
		ArtsmiaDAO a = new ArtsmiaDAO();

		a.listObjects(idMap);
		Graphs.addAllVertices(this.grafo, idMap.values());

		// aggiungee gli archi
		// APPROCCIO 1- doppio ciclo for sui vertici, dati due controllo se sono
		// collegati

		//NON TERMINA PERCHé CI SONO TROPPI VERTICI E FACCIAMO TROPPE QUERY AL DB
		/*for (ArtObject a1 : this.grafo.vertexSet()) {
			for (ArtObject a2 : this.grafo.vertexSet()) {
				int peso = a.getPeso(a1, a2);

				if (peso > 0) {
					if (!this.grafo.containsEdge(a1, a2)) {
						Graphs.addEdge(this.grafo, a1, a2, peso);
					}
				}
			}
		} */
		//APPROCCIO 2 
		//mi faccio dare dal db tutte le adiacenze
		
		for(Adiacenza ad: a.getAdiacenza()) {
			if(ad.getPeso() >0) {
				Graphs.addEdge(this.grafo, this.idMap.get(ad.getObj1()),  this.idMap.get(ad.getObj2()), ad.getPeso());
			}
		}
		System.out.println(String.format("Numero vertici %d , numero archi %d", this.grafo.vertexSet().size(), this.grafo.edgeSet().size()));

	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
}
