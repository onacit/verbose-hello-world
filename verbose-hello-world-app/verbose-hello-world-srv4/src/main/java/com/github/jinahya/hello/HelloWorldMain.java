package com.github.jinahya.hello;

/*-
 * #%L
 * verbose-hello-world-srv4
 * %%
 * Copyright (C) 2018 - 2019 Jinahya, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import lombok.extern.slf4j.Slf4j;
<<<<<<< HEAD

import java.io.IOException;

import static java.util.ServiceLoader.load;
=======
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.lang.Thread.currentThread;
import static java.util.Optional.ofNullable;
import static java.util.ServiceLoader.load;
import static java.util.stream.IntStream.range;
>>>>>>> sketch

/**
 * A class whose {@link #main(String[])} method accepts socket connections and sends {@code hello, world} to clients.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@Slf4j
public class HelloWorldMain {

    // -----------------------------------------------------------------------------------------------------------------
<<<<<<< HEAD
=======
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final HelloWorld SERVICE = load(HelloWorld.class).iterator().next();

    private static final int CAPACITY = 1024;

    private static final BlockingQueue<Socket> QUEUE = new ArrayBlockingQueue<>(CAPACITY);

    private static class HelloWorldRunnable implements Runnable {

        @Override
        public void run() {
            while (!currentThread().isInterrupted()) {
                try {
                    ofNullable(QUEUE.poll(10, TimeUnit.SECONDS)).ifPresent(c -> {
                        try {
                            try {
                                SERVICE.send(c);
                            } finally {
                                c.close();
                            }
                        } catch (final IOException ioe) {
                            logger.error("failed to send to {}", c.getRemoteSocketAddress(), ioe);
                        }
                    });
                } catch (final InterruptedException ie) {
                    logger.info("interrupted while polling from the queue", ie);
                    break;
                }
            }
            for (Socket client; (client = QUEUE.poll()) != null; ) {
                try {
                    try {
                        SERVICE.send(client);
                    } finally {
                        client.close();
                    }
                } catch (final IOException ioe) {
                    logger.error("failed to send to {}", client, ioe);
                }
            }
            logger.info("end of run()");
        }
    }
>>>>>>> sketch

    /**
     * The main method of this program which accepts socket connections and sends {@code hello, world} to clients.
     *
     * @param args an array of command line arguments
     * @throws IOException if an I/O error occurs.
     */
<<<<<<< HEAD
    public static void main(final String[] args) throws IOException {
        final HelloWorld service = load(HelloWorld.class).iterator().next();
        // TODO: implement!
=======
    public static void main(final String[] args) throws IOException, InterruptedException {
        final int threads = 128;
        final ExecutorService executor = Executors.newFixedThreadPool(threads);
        final List<Future<?>> futures = range(0, threads)
                .mapToObj(i -> new HelloWorldRunnable())
                .map(executor::submit)
                .collect(Collectors.toList());
        try (ServerSocket server = new ServerSocket(0)) {
            logger.debug("bound to {}", server.getLocalSocketAddress());
            while (!currentThread().isInterrupted()) {
                final Socket client = server.accept();
                try {
                    if (!QUEUE.offer(client, 1, TimeUnit.SECONDS)) {
                        logger.error("failed to offer client to the queue");
                        client.close();
                    }
                } catch (final InterruptedException ie) {
                    logger.error("interrupted while offering client to the queue", ie);
                    client.close();
                    break;
                }
            }
        }
        futures.forEach(f -> f.cancel(true));
        executor.shutdown();
        final boolean terminated = executor.awaitTermination(10L, TimeUnit.SECONDS);
>>>>>>> sketch
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    private HelloWorldMain() {
        super();
    }
}
