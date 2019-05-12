package com.leyou.page.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.concurrent.Executors.*;

public class ThreadUtils {

    private static final ExecutorService es = newFixedThreadPool(10);

    public static void execute(Runnable runnable) {

        es.submit(runnable);
    }
}