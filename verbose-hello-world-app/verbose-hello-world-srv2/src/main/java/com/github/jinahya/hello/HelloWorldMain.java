package com.github.jinahya.hello;

/*-
 * #%L
 * verbose-hello-world-srv2
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

    /**
     * The main method of this program which accepts socket connections and sends {@code hello, world} to clients.
     *
     * @param args an array of command line arguments
     * @throws IOException if an I/O error occurs.
     */
    public static void main(final String[] args) throws IOException {
<<<<<<< HEAD
        final HelloWorld service = load(HelloWorld.class).iterator().next();
        // TODO: implement!
=======
        final HelloWorld helloWorld = load(HelloWorld.class).iterator().next();
        try (ServerSocket server = new ServerSocket(0)) {
            log.info("bound to {}", server.getLocalSocketAddress());
            new Thread(() -> {
                try {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
                        while (true) {
                            final String line = reader.readLine();
                            if (!"quit".equals(line)) {
                                continue;
                            }
                            try {
                                server.close();
                            } catch (final IOException ioe) {
                                log.error("failed to closed the socket", ioe);
                            }
                        }
                    }
                } catch (final IOException ioe) {
                    log.error("failed to read system input", ioe);
                }
            }).start();
            while (!server.isClosed()) {
                try {
                    final Socket client = server.accept();
                    new Thread(() -> {
                        try {
                            try {
                                helloWorld.send(client);
                            } finally {
                                try {
                                    client.close();
                                } catch (final IOException ioe) {
                                    log.error("failed to close the client", ioe);
                                }
                            }
                        } catch (final IOException ioe) {
                            log.error("failed to send", ioe);
                        }
                    }).start();
                } catch (final IOException ioe) {
                    if (server.isClosed()) {
                        break;
                    }
                    log.error("failed to accept", ioe);
                }
            }
        }
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
