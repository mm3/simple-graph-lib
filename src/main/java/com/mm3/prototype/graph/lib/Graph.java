package com.mm3.prototype.graph.lib;

import java.util.Collection;

public interface Graph<T> {

    Graph<T> addVertex(T vertex);

    Graph<T> addEdge(T first, T second, EdgeType type);

    Collection<Edge<T>> getPath(T first, T second);

}
