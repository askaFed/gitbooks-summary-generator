package com.aska.fed.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Timing {

    public static <T> T timed (String description, Consumer<String> out, Supplier<T> code) {
        final LocalDateTime start = LocalDateTime.now ();
        T res = code.get ();
        final long execTime = Duration.between (start, LocalDateTime.now ()).toMillis ();
        out.accept (String.format ("%s: %d ms", description, execTime));
        return res;
    }
}
