package com.three_stack.maximum_alpha.backend.game.utilities;

import java.util.List;
import java.util.stream.Collectors;

public class Utility {
    public static <T> List<T> copy(List<T> list) {
        return list.stream().collect(Collectors.toList());
    }
}
