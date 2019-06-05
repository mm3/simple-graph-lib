package com.mm3.prototype.graph.lib;

import java.util.Collection;

public interface SearchTask<T> {
    Vertex<T> getFrom();
    Vertex<T> getTo();
    Collection<Edge<T>> getEdgesForVertex(Vertex<T> vertex);
}
