package com.github.jinahya.hello;

/*-
 * #%L
 * verbose-hello-world-srv1
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
=======
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;
>>>>>>> sketch

import static java.util.ServiceLoader.load;

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
    private static CompletableFuture<Void> closer(final ServerSocket server) {
        return CompletableFuture.runAsync(() -> {
            try {
                try {
                    final BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
                    while (true) {
                        if ("quit".equals(r.readLine())) {
                            log.info("quit command received");
                            break;
                        }
                    }
                } finally {
                    server.close();
                    log.info("server closed");
                }
            } catch (final IOException ioe) {
                log.error("failed to close", ioe);
            }
        });
    }

>>>>>>> sketch
    /**
     * The main method of this program which accepts socket connections and sends {@code hello, world} to clients.
     *
     * @param args an array of command line arguments
     * @throws IOException if an I/O error occurs.
     */
    public static void main(final String... args) throws IOException {
        final HelloWorld helloWorld = load(HelloWorld.class).iterator().next();
<<<<<<< HEAD
        // TODO: implement!
=======
        try (ServerSocket server = new ServerSocket(0)) {
            log.info("bound to {}", server.getLocalSocketAddress());
            final CompletableFuture<Void> closer = closer(server);
            while (!server.isClosed()) {
                try {
                    try (Socket client = server.accept()) {
                        helloWorld.send(client);
                    }
                } catch (final IOException ioe) {
                    if (server.isClosed()) {
                        break;
                    }
                    log.error("failed to send", ioe);
                }
            }
            log.info("broke out from the loop");
            if (!closer.isCancelled()) {
                log.warn("canceling the closer...");
                closer.cancel(true);
            }
            log.info("closer.done: {}", closer.isDone());
        }
        log.info("end of program");
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
