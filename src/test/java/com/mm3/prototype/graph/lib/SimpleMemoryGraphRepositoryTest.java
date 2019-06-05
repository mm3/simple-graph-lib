package com.mm3.prototype.graph.lib;

import org.junit.Test;

import java.util.*;

import static org.hamcrest.CoreMatchers.*;

public class SimpleMemoryGraphRepositoryTest extends AbstractTest {

    private static final String VERTEX_1 = "VERTEX_1";
    private static final String VERTEX_2 = "VERTEX_2";

    @Test
    public void addVertexTest() {
        Map<Vertex<String>, Set<Edge<String>>> map = new HashMap<>();
        GraphRepository<String> repository = new SimpleMemoryGraphRepository<String>(map, HashSet::new);

        repository.addVertex(Vertex.of(VERTEX_1));

        collector.checkThat(map.isEmpty(), is(false));
        collector.checkThat(map.containsKey(Vertex.of(VERTEX_1)), is(true));
        collector.checkThat(map.get(Vertex.of(VERTEX_1)), notNullValue());
    }

    @Test
    public void addEdgeTest() {
        Map<Vertex<String>, Set<Edge<String>>> map = new HashMap<>();
        GraphRepository<String> repository = new SimpleMemoryGraphRepository<String>(map, HashSet::new);

        Edge edge = Edge.of(Vertex.of(VERTEX_1), Vertex.of(VERTEX_2), EdgeType.UNDIRECTED);
        repository.addEdge(Edge.of(Vertex.of(VERTEX_1), Vertex.of(VERTEX_2), EdgeType.UNDIRECTED));

        collector.checkThat(map.isEmpty(), is(false));
        collector.checkThat(map.containsKey(Vertex.of(VERTEX_1)), is(true));
        collector.checkThat(map.containsKey(Vertex.of(VERTEX_2)), is(true));
        collector.checkThat(map.get(Vertex.of(VERTEX_1)), hasItems(edge));
        collector.checkThat(map.get(Vertex.of(VERTEX_2)), hasItems(edge));
    }

    @Test
    public void getEdgesForVertexTest() {
        Map<Vertex<String>, Set<Edge<String>>> map = new HashMap<>();
        GraphRepository<String> repository = new SimpleMemoryGraphRepository<String>(map, HashSet::new);

        Edge edge = Edge.of(Vertex.of(VERTEX_1), Vertex.of(VERTEX_2), EdgeType.UNDIRECTED);
        repository.addEdge(Edge.of(Vertex.of(VERTEX_1), Vertex.of(VERTEX_2), EdgeType.UNDIRECTED));

        Collection<Edge<String>> edges = repository.getEdgesForVertex(Vertex.of(VERTEX_1));
        collector.checkThat(edges.isEmpty(), is(false));
        collector.checkThat(edges, hasItems(edge));
    }
}