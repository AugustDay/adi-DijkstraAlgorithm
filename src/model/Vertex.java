/* Austin Ingraham */

package model;

/**
 * Representation of a graph vertex
 */
public class Vertex implements Comparable<Vertex> {
	// label attached to this vertex
	private String label;
	
	private int cost;
	
	private boolean known;
	
	private Vertex previous;

	/**
	 * Construct a new vertex
	 * 
	 * @param label
	 *            the label attached to this vertex
	 */
	public Vertex(String label) {
		if (label == null)
			throw new IllegalArgumentException("null");
		this.label = label;
		dijInit();
	}

	/**
	 * Get a vertex label
	 * 
	 * @return the label attached to this vertex
	 */
	public String getLabel() {
		return label;
	}
	
	public int getCost() {
		return cost;		
	}
	
	public boolean getKnown() {
		return known;
	}
	
	public Vertex getPrevious() {
		return previous;
	}
	
	public void setCost(final int theCost) {
		cost = theCost;
	}
	
	public void setKnown(final boolean theBool) {
		known = theBool;
	}
	
	public void setPrevious(final Vertex thePrevious) {
		previous = thePrevious;
	}
	
	/** Prepares this Vertex's values for Dijkstra's algorithm. */
	public void dijInit() {
		known = false;
		previous = null;
		cost = Integer.MAX_VALUE;		
	}

	/**
	 * A string representation of this object
	 * 
	 * @return the label attached to this vertex
	 */
	public String toString() {
		return label;
	}
	
	@Override
	public int compareTo(Vertex theOther) {
		return Integer.compare(cost, theOther.getCost());
	}

	// auto-generated: hashes on label
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		return result;
	}

	// auto-generated: compares labels
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Vertex other = (Vertex) obj;
		if (label == null) {
			return other.label == null;
		} else {
			return label.equals(other.label);
		}
	}

}
