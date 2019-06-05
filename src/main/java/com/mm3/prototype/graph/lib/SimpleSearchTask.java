package com.mm3.prototype.graph.lib;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.Collection;
import java.util.function.Function;

@Data
@Builder
public class SimpleSearchTask<T> implements SearchTask<T> {

    @NonNull
    private final Vertex<T> from;

    @NonNull
    private final Vertex<T> to;

    @NonNull
    private final Function<Vertex<T>, Collection<Edge<T>>> getter;

    @Override
    public Collection<Edge<T>> getEdgesForVertex(@NonNull Vertex<T> vertex) {
        return getter.apply(vertex);
    }
}
