package com.mm3.prototype.graph.lib;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor(staticName = "of")
public class Vertex<T> {
    @NonNull
    private final T value;
}
