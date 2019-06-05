package com.mm3.prototype.graph.lib;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.util.Collection;

@AllArgsConstructor
public class SimpleGraph<T> implements Graph<T> {

    public static <T> Graph<T> getInstance() {
        return new SimpleGraph<>(new SimpleMemoryGraphRepository<>(), new SimpleSearchService<>());
    }

    @NonNull
    private final GraphRepository<T> repository;

    @NonNull
    private final SearchService<T> service;

    @Override
    public Graph<T> addVertex(@NonNull T vertex) {
        repository.addVertex(Vertex.of(vertex));
        return this;
    }

    @Override
    public Graph<T> addEdge(@NonNull T first, @NonNull T second, @NonNull EdgeType type) {
        repository.addEdge(Edge.of(Vertex.of(first), Vertex.of(second), type));
        return this;
    }

    @Override
    public Collection<Edge<T>> getPath(@NonNull T first, @NonNull T second) {
        final SearchTask<T> task = SimpleSearchTask.<T>builder()
                .from(Vertex.of(first))
                .to(Vertex.of(second))
                .getter(repository::getEdgesForVertex)
                .build();
        return service.getPath(task);
    }
}
