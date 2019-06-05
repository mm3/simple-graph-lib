package com.mm3.prototype.graph.lib;

import lombok.NonNull;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class SimpleSearchService<T> implements SearchService<T> {

    @Override
    public Collection<Edge<T>> getPath(@NonNull SearchTask<T> task) {
        return getPath(task.getFrom(), task.getTo(), Collections.emptyMap(), task::getEdgesForVertex);
    }

    private Collection<Edge<T>> getPath(Vertex<T> from,
                                        Vertex<T> to,
                                        Map<Vertex<T>, Edge<T>> path,
                                        Function<Vertex<T>, Collection<Edge<T>>> getter) {
        return getter.apply(from)
                .parallelStream()
                .map(edge -> checkEdge(edge, from, to, path, getter))
                .filter(not(Collection::isEmpty))
                .min(getPathsComparator())
                .orElse(Collections.emptySet());
    }

    private Collection<Edge<T>> checkEdge(Edge<T> edge,
                                          Vertex<T> from,
                                          Vertex<T> to,
                                          Map<Vertex<T>, Edge<T>> path,
                                          Function<Vertex<T>, Collection<Edge<T>>> getter) {
        Vertex<T> next = getSecondVertexFromEdge(edge, from);
        if(Objects.equals(next, to)) {
            return addEdgeToPath(path, edge).values();
        }
        if(path.containsKey(next)) {
            return Collections.emptySet();
        }
        return getPath(next, to, addEdgeToPath(path, edge), getter);
    }


    private static <T> Comparator<Collection<Edge<T>>> getPathsComparator() {
        return Comparator.comparing(Collection::size);
    }

    private static <T> Vertex<T> getSecondVertexFromEdge(Edge<T> edge, Vertex<T> from) {
        return EdgeType.DIRECTED.equals(edge.getType()) ? edge.getSecond() :
                Objects.equals(from, edge.getFirst()) ? edge.getSecond() : edge.getFirst();
    }

    private static <T> Map<Vertex<T>, Edge<T>> addEdgeToPath(Map<Vertex<T>, Edge<T>> map, Edge<T> value) {
        Map<Vertex<T>, Edge<T>> result = new LinkedHashMap<>(map);
        result.put(value.getFirst(), value);
        return result;
    }

    private static <T> Predicate<T> not(Predicate<T> p) { return o -> !p.test(o); }
}
