package com.github.jinahya.hello;

/*-
 * #%L
 * verbose-hello-world-app3
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

<<<<<<< HEAD
=======
import com.google.inject.Guice;
import com.google.inject.Injector;

>>>>>>> sketch
import javax.inject.Inject;
import java.io.IOException;

/**
 * A class whose {@link #main(String[])} method prints {@code hello, world} to {@link System#out}.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public class HelloWorldMain {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The main method of this program which prints {@code hello, world} followed by a new line character.
     *
     * @param args an array of command line arguments
     * @throws IOException if an I/O error occurs.
     */
    public static void main(final String[] args) throws IOException {
<<<<<<< HEAD
        // TODO: implement!
=======
        final HelloWorldMain main = new HelloWorldMain();                       // <1>
        final Injector injector = Guice.createInjector(new HelloWorldModule()); // <2>
        injector.injectMembers(main);                                           // <3>
        main.helloWorld.write(System.out);                                      // <4>
        System.out.printf("%n");                                                // <5>
>>>>>>> sketch
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    private HelloWorldMain() {
        super();
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * An injected instance of {@link HelloWorld}.
     */
    @Inject
    HelloWorld helloWorld;
}
