package com.mm3.prototype.graph.lib;

import java.util.Collection;

public interface SearchService<T> {
    Collection<Edge<T>> getPath(SearchTask<T> task);
}
