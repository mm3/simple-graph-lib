package com.mm3.prototype.graph.lib;

import org.junit.Test;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;

public class SimpleSearchServiceTest extends AbstractTest {

    private final SearchTask<Integer> TEST_TASK = new SearchTask<Integer>(){

        @Override
        public Vertex<Integer> getFrom() {
            return Vertex.of(0);
        }

        @Override
        public Vertex<Integer> getTo() {
            return Vertex.of(1000);
        }

        @Override
        public Collection<Edge<Integer>> getEdgesForVertex(Vertex<Integer> vertex) {
            return Stream.of(Edge.of(Vertex.of(vertex.getValue()), Vertex.of(vertex.getValue() + 10), EdgeType.DIRECTED)).collect(Collectors.toList());
        }
    };

    @Test
    public void getPathTest() throws Exception {
        SearchService<Integer> service = new SimpleSearchService<>();

        Collection<Edge<Integer>> result = service.getPath(TEST_TASK);
        collector.checkThat(result.isEmpty(), is(false));
        collector.checkThat(result.size(), is(100));
    }
}