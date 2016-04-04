/* Austin Ingraham
 * TCSS 342: Project 3 - Dijkstra's Algorithm
 * MyGraph
 */

package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * A representation of a graph. Assumes that we do not have negative
 * cost edges in the graph.
 *
 * @author Austin Ingraham / (adi1996@uw.edu)
 * @version 2016-03-08, 22:47
 */
public class MyGraph implements Graph {

    /** Collection of graph edges. */
    private final List<Edge> myEdges;   
    
    /** Map where every key/value pair is a Vertex mapped to itself. */
    private final Map<Vertex, Vertex> myVertices;
    
    /** AdjacencyMap where every List<Edge> is all the Edges whose source is the Vertex key. */
    private final Map<Vertex, List<Edge>> myGraph;

    /**
     * Creates a MyGraph object with the given collection of vertices and the
     * given collection of edges.
     *
     * @param theVertices A collection of the vertices in this graph
     * @param theEdges Collection of the edges in this graph
     */
    public MyGraph(Collection<Vertex> theVertices, Collection<Edge> theEdges) {
    	if (theVertices == null || theEdges == null) {
    		throw new NullPointerException();
    	}
    	myEdges = new ArrayList<>(theEdges);
    	myVertices = new HashMap<>();
    	myGraph = new HashMap<>();

    	/* Places vertices into a Map structure for quick access. */
    	for (final Vertex v : theVertices) {
    		myVertices.putIfAbsent(v, v);
    	}
    	
        /* Fills the map with Vertices, with empty lists of edges to be filled in with next loop. */
        for (final Vertex v : myVertices.keySet()) { 
        	final List<Edge> theL = new ArrayList<>();
        	myGraph.putIfAbsent(v, theL);
        }
        
        /* Adds edges to Map. At same time, verifies that edges contain valid information. */
        for (final Edge theE : theEdges) { 
        	if (!theVertices.contains(theE.getSource()) //checks if edge contains valid nodes
        	 || !theVertices.contains(theE.getDestination()) || theE.getWeight() < 0) { //negative cost
        		throw new IllegalArgumentException();
        	} else {        		
        		for (Edge e : myGraph.get(theE.getSource())) { //checks for same directed edge with different weights
        			if (e.pointsEqual(theE) && e.getWeight() != theE.getWeight()) {
        				throw new IllegalArgumentException();
        			}
        		}        		
        		if (!myGraph.get(theE.getSource()).contains(theE)) { //checks for duplicates
            		myGraph.get(theE.getSource()).add(theE); //adds edge to list
            	} 
        	}        	
        }
    }

    /**
     * Return the collection of vertices of this graph
     * 
     * @return the vertices as a collection (which is anything iterable)
     */
    @Override
    public Collection<Vertex> vertices() {
        return new ArrayList<Vertex>(myVertices.keySet());
    }

    /**
     * Return the collection of edges of this graph
     * 
     * @return the edges as a collection (which is anything iterable)
     */
    @Override
    public Collection<Edge> edges() {
        return new ArrayList<Edge>(myEdges);
    }

    /**
     * Return a collection of vertices adjacent to a given vertex v. i.e., the
     * set of all vertices w where edges v -> w exist in the graph. Return an
     * empty collection if there are no adjacent vertices.
     * 
     * @param v One of the vertices in the graph
     * @return An iterable collection of vertices adjacent to v in the graph
     * @throws IllegalArgumentException If v does not exist.
     */
    @Override
    public Collection<Vertex> adjacentVertices(Vertex v) {
        if (v == null) { //is NULL all Kayee means by "does not exist"?
            throw new IllegalArgumentException();
        }
        final List<Vertex> result = new ArrayList<Vertex>();
      
        for (final Edge e : myGraph.get(v)) {
        	result.add(e.getDestination());
        }
      
        return result;
    }

    /**
     * Test whether vertex b is adjacent to vertex a (i.e. a -> b) in a directed
     * graph. Assumes that we do not have negative cost edges in the graph.
     * 
     * @param a One vertex
     * @param b Another vertex
     * @return Cost of edge if there is a directed edge from a to b in the
     *         graph, return -1 otherwise.
     * @throws IllegalArgumentException If a or b do not exist.
     */
    @Override
    public int edgeCost(Vertex a, Vertex b) {
        if (a == null || b == null) {
            throw new IllegalArgumentException();
        }
        int cost = -1;
      
        for (final Edge e : myGraph.get(a)) { 
            if (e.getDestination().equals(b)) {
                cost = e.getWeight();
                break;
            }            
        }
      
        return cost;
    }

    /**
     * Returns the shortest path from a to b in the graph, or null if there is
     * no such path. Assumes all edge weights are nonnegative. Uses Dijkstra's
     * algorithm.
     * 
     * @param start The starting vertex
     * @param target The destination vertex
     * @return a Path where the vertices indicate the path from a to b in order
     *         and contains a (first) and b (last) and the cost is the cost of
     *         the path. Returns null if b is not reachable from a.
     * @throws IllegalArgumentException If a or b does not exist.
     */
    public Path shortestPath(Vertex start, Vertex target) {
        if (!myGraph.keySet().contains(start) || !myGraph.keySet().contains(target)) {
        	throw new IllegalArgumentException();
        }    
        final List<Vertex> resultPath = new ArrayList<>();
        int cost = 0;   
        
    	if (!start.equals(target)) { //start and target vertices are not the same
    		Vertex end = initializeVertices(start, target);  		
            discoverVertices(); 
            
            if (end.getPrevious() == null) {
            	return null;
            } else {
	            cost = end.getCost(); //gets the shortest cost from target vertex
	    		resultPath.add(end); //target vertex is end of path
	    		while (end.getPrevious() != null) { //traces path back to start
	    			end = end.getPrevious(); 
	    			resultPath.add(end);
	    		}
	    		Collections.reverse(resultPath);    
            }
        } else {
        	resultPath.add(start); 
        }
        return new Path(resultPath, cost);
    }
    
    /**
     * Iterates through every vertex, calls dijInit to set cost to infinity, etc.
     * @param start beginning vertex (needs cost set to 0 instead of MAX_VALUE
     * @param end target
     * @return target vertex
     */
    private Vertex initializeVertices(final Vertex start, final Vertex end) {
    	Vertex destination = null;
    	for (Vertex v : myGraph.keySet()) { //prepares vertices for algorithm
        	if (v.equals(start)) {
        		v.setPrevious(null);
        		v.setKnown(true);
        		v.setCost(0);
        	} else if (v.equals(end)) {
        		destination = v;
        	} else {
        		v.dijInit();
        	}
        }
    	return destination;
    }
    
    /**
     * Searches through graph, finding the shortest path to each vertex and setting its new cost.
     * I'm pretty sure there's imperfect logic in here somewhere. I don't think I'm setting
     * known correctly once I go through, or something.
     */
    private void discoverVertices() {
    	PriorityQueue<Vertex> pq = new PriorityQueue<>(myGraph.keySet());
		Vertex current, destination;
		while(!pq.isEmpty()) {
			current = myVertices.get(pq.remove());
			current.setKnown(true);
			for (final Edge e : myGraph.get(current)) {
				destination = myVertices.get(e.getDestination());
				if(!destination.getKnown()) {
	    			if(current.getCost() + e.getWeight() < destination.getCost()){	    			
	    				destination.setCost(current.getCost() + e.getWeight());
	    				destination.setPrevious(current);
	    				pq.add(destination);
	    			} 
				}
			}			
		}
    }
}
