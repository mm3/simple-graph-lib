package com.mm3.prototype.graph.lib;

import lombok.NonNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import static java.util.Objects.isNull;

public class SimpleMemoryGraphRepository<T> implements GraphRepository<T> {

    private final Map<Vertex<T>, Set<Edge<T>>> map;
    private final Supplier<Set<Edge<T>>> collectionBuilder;

    public SimpleMemoryGraphRepository() {
        this.map = new ConcurrentHashMap<>();
        this.collectionBuilder = ConcurrentHashMap::newKeySet;
    }

    public SimpleMemoryGraphRepository(@NonNull Map<Vertex<T>, Set<Edge<T>>> map,
                                       @NonNull Supplier<Set<Edge<T>>> collectionBuilder) {
        this.map = map;
        this.collectionBuilder = collectionBuilder;
    }

    @Override
    public void addVertex(@NonNull Vertex<T> vertex) {
        map.putIfAbsent(vertex, collectionBuilder.get());
    }

    @Override
    public void addEdge(@NonNull Edge<T> edge) {
        map.compute(edge.getFirst(), (key, value) -> this.add(value, edge));
        if(EdgeType.UNDIRECTED.equals(edge.getType())) {
            map.compute(edge.getSecond(), (key, value) -> this.add(value, edge));
        }
    }

    private Set<Edge<T>> add(Set<Edge<T>> set, Edge<T> item) {
        Set<Edge<T>> result = isNull(set) ? collectionBuilder.get() : set;
        result.add(item);
        return result;
    }

    @Override
    public Collection<Edge<T>> getEdgesForVertex(@NonNull Vertex<T> vertex) {
        return map.getOrDefault(vertex, Collections.emptySet());
    }
}
