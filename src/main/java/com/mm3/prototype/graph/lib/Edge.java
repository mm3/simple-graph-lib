package com.mm3.prototype.graph.lib;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor(staticName = "of")
public class Edge<T> {

    @NonNull
    private final Vertex<T> first;

    @NonNull
    private final Vertex<T> second;

    @NonNull
    private final EdgeType type;
}
