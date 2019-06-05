package com.mm3.prototype.graph.lib;

import java.util.Collection;

public interface GraphRepository<T> {
    void addVertex(Vertex<T> vertex);
    void addEdge(Edge<T> edge);

    Collection<Edge<T>> getEdgesForVertex(Vertex<T> vertex);

}
