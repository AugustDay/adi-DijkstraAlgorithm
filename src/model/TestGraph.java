package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests MyGraph.
 *
 * @author Austin Ingraham (adi1996@uw.edu) | Viveret Steele
 */
public class TestGraph {
    private static final String STR_SEP =
        "-------------------------------------------------------";
    private static MyGraph myGraph;
    private static int myTestNum = 0;

    public static void main(String[] args) {
        testConstructorExceptionVerts();
        testConstructorExceptionEdges();
        testConstructorExceptionNegativeEdgeWgt();
        testConstructorExceptionSrcExistsInEdge();
        testConstructorExceptionDestExistsInEdge();
        testConstructorExceptionDupEdgeSameWgt();
        testConstructorExceptionDupEdgeDiffWgt();
        testConstructorEmptyVerticesAndEdges();
        testConstructorEmptyEdges();
        testVerticesSafe();
        testEdgesSafe();
        testEdgeCost();
        testEdgeCostException();
        testEdgeCostNoAdjacency();
        testAdjacentVerticesNullArgument();
        testAdjacentVerticesArgumentDoesNotExist();
        testConstructorSameEdgeDifferentWeight();
        testShortestPathArgumentDoesNotExist();
    }

    private static List<Vertex> newVertices() {
        List<Vertex> vs = new ArrayList<Vertex>();
        vs.add(new Vertex("A"));
        vs.add(new Vertex("B"));
        vs.add(new Vertex("C"));
        vs.add(new Vertex("D"));
        vs.add(new Vertex("E"));
        vs.add(new Vertex("F"));
        return vs;
    }

    private static List<Edge> newEdges(List<Vertex> vs) {
        List<Edge> es = new ArrayList<Edge>();
        // 0 -> 1, wgt = 2
        es.add(new Edge(vs.get(0), vs.get(1), 2));
        // 1 -> 2, wgt = 3
        es.add(new Edge(vs.get(1), vs.get(2), 3));
        // 4 -> 5, wgt = 9
        es.add(new Edge(vs.get(4), vs.get(5), 9));
        return es;
    }

    private static void printTestResult(boolean success, String msg) {
        System.out.printf("%2d) %s: %s\n", myTestNum,
                          (success ? "PASS" : "FAIL"),
                          getMethodName(4));
        System.out.printf("\tNote: %s\n%s\n",
                          (msg != null) ? msg : "none", STR_SEP);
        myTestNum++;
    }

    private static void printPass() {
        printTestResult(true, null);
    }

    private static void printFail() {
        printTestResult(false, null);
    }

    private static void printFail(final String msg) {
        printTestResult(false, msg);
    }

    private static String getMethodName(int trace) {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        if (trace >= 0 || trace < stacktrace.length )
            return stacktrace[trace].getMethodName();
        else
            return "Unknown";
    }

    private static void resetState() {
        List<Vertex> vs = newVertices();
        List<Edge> es = newEdges(vs);
        myGraph = new MyGraph(vs, es);
    }

    private static void testConstructorExceptionVerts() {
        try {
            List<Vertex> vs = newVertices();
            List<Edge> es = newEdges(vs);
            myGraph = new MyGraph(null, es);
            printFail();
        } catch (NullPointerException e) {
            printPass();
        } catch (Exception e) {
            printFail(e.toString());
        }
    }

    private static void testConstructorExceptionEdges() {
        try {
            List<Vertex> vs = newVertices();
            List<Edge> es = newEdges(vs);
            myGraph = new MyGraph(vs, null);
            printFail();
        } catch (NullPointerException e) {
            printPass();
        } catch (Exception e) {
            printFail(e.toString());
        }
    }

    private static void testConstructorExceptionNegativeEdgeWgt() {
        try {
            List<Vertex> vs = newVertices();
            List<Edge> es = newEdges(vs);
            es.add(new Edge(vs.get(1), vs.get(0), -1));
            myGraph = new MyGraph(vs, es);
            printFail();
        } catch (IllegalArgumentException e) {
            printPass();
        } catch (Exception e) {
            printFail(e.toString());
        }
    }

    private static void testConstructorExceptionSrcExistsInEdge() {
        try {
            List<Vertex> vs = newVertices();
            List<Edge> es = newEdges(vs);
            es.add(new Edge(new Vertex("ZZ"), vs.get(0), 2));
            myGraph = new MyGraph(vs, es);
            printFail();
        } catch (IllegalArgumentException e) {
            printPass();
        } catch (Exception e) {
            printFail(e.toString());
        }
    }

    private static void testConstructorExceptionDestExistsInEdge() {
        try {
            List<Vertex> vs = newVertices();
            List<Edge> es = newEdges(vs);
            es.add(new Edge(vs.get(0), new Vertex("ZZ"), 2));
            myGraph = new MyGraph(vs, es);
            printFail();
        } catch (IllegalArgumentException e) {
            printPass();
        } catch (Exception e) {
            printFail(e.toString());
        }
    }

    private static void testConstructorExceptionDupEdgeSameWgt() {
        try {
            List<Vertex> vs = newVertices();
            List<Edge> es = newEdges(vs);
            es.add(new Edge(vs.get(1), vs.get(0), 2));
            es.add(new Edge(vs.get(1), vs.get(0), 2));
            myGraph = new MyGraph(vs, es);
            printPass();
        } catch (Exception e) {
            printFail(e.toString());
        }
    }

    private static void testConstructorExceptionDupEdgeDiffWgt() {
        try {
            List<Vertex> vs = newVertices();
            List<Edge> es = newEdges(vs);
            es.add(new Edge(vs.get(1), vs.get(0), 2));
            es.add(new Edge(vs.get(1), vs.get(0), 3));
            myGraph = new MyGraph(vs, es);
            printFail();
        } catch (IllegalArgumentException e) {
            printPass();
        } catch (Exception e) {
            printFail(e.toString());
        }
    }

    private static void testConstructorEmptyVerticesAndEdges() {
        try {
            List<Vertex> vs = newVertices();
            List<Edge> es = newEdges(vs);
            vs.clear();
            es.clear();
            myGraph = new MyGraph(vs, es);
            printPass();
        } catch (Exception e) {
            printFail(e.toString());
        }
    }

    private static void testConstructorEmptyEdges() {
        try {
            List<Vertex> vs = newVertices();
            List<Edge> es = newEdges(vs);
            es.clear();
            myGraph = new MyGraph(vs, es);
            printPass();
        } catch (Exception e) {
            printFail(e.toString());
        }
    }

    private static void testVerticesSafe() {
        resetState();
        myGraph.vertices().clear();
        if (myGraph.vertices().isEmpty())
            printFail();
        else
            printPass();
    }

    private static void testEdgesSafe() {
        resetState();
        myGraph.edges().clear();
        if (myGraph.edges().isEmpty())
            printFail();
        else
            printPass();
    }

    private static void testEdgeCost() {
        resetState();
        List<Vertex> vs = newVertices();
        List<Edge> es = newEdges(vs);
        Edge e1 = es.get(0);
        if (myGraph.edgeCost(e1.getSource(), e1.getDestination())
            != e1.getWeight())
            printFail();
        else
            printPass();
    }
    
    private static void testEdgeCostException() {
    	resetState();
        try {
            List<Vertex> vs = newVertices();
            myGraph.edgeCost(null, vs.get(0));
            printFail();
        } catch (Exception e) {
            printPass();
        }
    }
    
    private static void testEdgeCostNoAdjacency() {
    	resetState();
        List<Vertex> vs = newVertices();
        if (myGraph.edgeCost(vs.get(0), vs.get(0)) == -1)
            printPass();
        else
            printFail();
    }
    
    private static void testAdjacentVerticesNullArgument() {
    	resetState();
        try {
            myGraph.adjacentVertices(null);
            printFail();
        } catch (Exception e) {
            printPass();
        }
    }
    
    private static void testAdjacentVerticesArgumentDoesNotExist() {
    	resetState();
        try {
            myGraph.adjacentVertices(new Vertex("Z"));
            printFail();
        } catch (Exception e) {
            printPass();
        }
    }
    
    private static void testConstructorSameEdgeDifferentWeight() {
    	final List<Vertex> vl = new ArrayList<>();
    	final List<Edge> el = new ArrayList<>();
    	final Vertex a = new Vertex("A");
    	final Vertex b = new Vertex("B");
    	vl.add(a);
    	vl.add(b);    	
    	el.add(new Edge(a, b, 3));
    	el.add(new Edge(a, b, 5));
    	
        try {
        	final MyGraph graph = new MyGraph(vl, el);
            printFail();
        } catch (Exception e) {
            printPass();
        }
    }
    
    private static void testShortestPathArgumentDoesNotExist() {
    	resetState();
    	try {
    		myGraph.shortestPath(null, null);
            printFail();
        } catch (Exception e) {
            printPass();
        }
    	
    }
}
