package com.github.jinahya.hello;

import org.slf4j.Logger;

import java.io.IOException;

import static com.github.jinahya.hello.HelloWorldProvider.findAnyAvailable;
import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * A class whose {@link #main(String[])} method accepts socket connections and sends {@code hello, world} to clients.
 */
public class HelloWorldMain {

    private static final Logger logger = getLogger(lookup().lookupClass());

    /**
     * The main method of this program which accepts socket connections and sends {@code hello, world} to clients.
     *
     * @param args an array of command line arguments
     * @throws IOException if an I/O error occurs.
     */
    public static void main(final String[] args) throws IOException {
        final HelloWorld service = findAnyAvailable();
        // TODO: implement!
    }

    /**
     * Creates a new instance.
     */
    private HelloWorldMain() {
        super();
    }
}
