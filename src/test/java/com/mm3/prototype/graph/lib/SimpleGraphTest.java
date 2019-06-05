package com.mm3.prototype.graph.lib;

import org.junit.Test;

import java.util.Collection;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.*;

public class SimpleGraphTest extends AbstractTest {

    private static final String VERTEX_1 = "VERTEX_1";
    private static final String VERTEX_2 = "VERTEX_2";
    private static final String VERTEX_3 = "VERTEX_3";

    @Test
    public void getPathOneVertexTest() throws Exception {
        Collection<Edge<String>> path = SimpleGraph.<String>getInstance()
                .addVertex(VERTEX_1)
                .getPath(VERTEX_1, VERTEX_1);


        collector.checkThat(path, notNullValue());
        collector.checkThat(path.isEmpty(), is(true));
    }

    @Test
    public void getPathTwoVertexesWithoutEdgeTest() throws Exception {
        Collection<Edge<String>> path = SimpleGraph.<String>getInstance()
                .addVertex(VERTEX_1)
                .addVertex(VERTEX_2)
                .getPath(VERTEX_1, VERTEX_2);


        collector.checkThat(path, notNullValue());
        collector.checkThat(path.isEmpty(), is(true));
    }

    @Test
    public void getPathTwoVertexesWithUndirectedEdgeTest() throws Exception {
        Collection<Edge<String>> path = SimpleGraph.<String>getInstance()
                .addVertex(VERTEX_1)
                .addVertex(VERTEX_2)
                .addEdge(VERTEX_1, VERTEX_2, EdgeType.UNDIRECTED)
                .getPath(VERTEX_1, VERTEX_2);


        collector.checkThat(path, notNullValue());
        collector.checkThat(path.isEmpty(), is(false));
        collector.checkThat(path.size(), is(1));
    }

    @Test
    public void getPathTwoVertexesWithDirectedEdgeTest() throws Exception {
        Collection<Edge<String>> path = SimpleGraph.<String>getInstance()
                .addVertex(VERTEX_1)
                .addVertex(VERTEX_2)
                .addEdge(VERTEX_1, VERTEX_2, EdgeType.DIRECTED)
                .getPath(VERTEX_1, VERTEX_2);


        collector.checkThat(path, notNullValue());
        collector.checkThat(path.isEmpty(), is(false));
        collector.checkThat(path.size(), is(1));
    }

    @Test
    public void getPathTwoVertexesWithReversedDirectedEdgeTest() throws Exception {
        Collection<Edge<String>> path = SimpleGraph.<String>getInstance()
                .addVertex(VERTEX_1)
                .addVertex(VERTEX_2)
                .addEdge(VERTEX_1, VERTEX_2, EdgeType.DIRECTED)
                .getPath(VERTEX_2, VERTEX_1);


        collector.checkThat(path, notNullValue());
        collector.checkThat(path.isEmpty(), is(true));
    }

    @Test
    public void getPathThreeVertexesWithEdgeTest() throws Exception {
        Collection<Edge<String>> path = SimpleGraph.<String>getInstance()
                .addVertex(VERTEX_1)
                .addVertex(VERTEX_2)
                .addVertex(VERTEX_3)
                .addEdge(VERTEX_1, VERTEX_2, EdgeType.UNDIRECTED)
                .addEdge(VERTEX_2, VERTEX_3, EdgeType.UNDIRECTED)
                .getPath(VERTEX_1, VERTEX_3);


        collector.checkThat(path, notNullValue());
        collector.checkThat(path.isEmpty(), is(false));
        collector.checkThat(path.size(), is(2));
    }


    final static Object lock = new Object();
    final static ExecutorService executor = Executors.newFixedThreadPool(2);
    @Test
    public void getPathParallelTest() throws Exception {

        final Graph<Integer> graph = SimpleGraph.getInstance();

        IntStream.range(1, 500).forEach(i -> graph.addEdge(i - 1, i + 1, EdgeType.UNDIRECTED));

        // todo: too short stack
        collector.checkThat(graph.getPath(0, 500).size(), is(250));

        Collection<Future> tasks = IntStream.of(0, 1).boxed().map(i -> executor.submit(() -> {
            try {
                Thread.sleep(10);
                synchronized (lock) {
                    lock.wait();
                }
                return graph.getPath(i == 0 ? 50 : 450, i == 0 ? 500 : 100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        })).collect(Collectors.toList());


        Thread.sleep(200);
        synchronized (lock) {
            lock.notifyAll();
        }

        Collection<Integer> results = tasks.stream().map(future -> {
            try {
                return future.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }).map(result -> ((Collection)result).size()).collect(Collectors.toList());

        collector.checkThat(results, hasItems(225, 175));

    }
}