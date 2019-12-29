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

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import static com.github.jinahya.hello.HelloWorld.BYTES;
import static java.nio.ByteBuffer.allocate;
import static java.nio.channels.SelectionKey.OP_ACCEPT;
import static java.nio.channels.SelectionKey.OP_WRITE;
import static java.util.ServiceLoader.load;

/**
 * A class whose {@link #main(String[])} method accepts socket connections and sends {@code hello, world} to clients.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@Slf4j
public class HelloWorldMain extends AbstractHelloWorldMain {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The main method of this program which accepts socket connections and sends {@code hello, world} to clients.
     *
     * @param args an array of command line arguments
     * @throws IOException if an I/O error occurs.
     */
    public static void main(final String[] args) throws IOException {
        final HelloWorld helloWorld = load(HelloWorld.class).iterator().next();
        log.info("localhost: {}", InetAddress.getLocalHost());
        final ServerSocketChannel server = ServerSocketChannel.open();
        server.configureBlocking(false);
        server.bind(new InetSocketAddress(InetAddress.getLocalHost(), 0));
        log.info("bound to {}", server.socket().getLocalSocketAddress());
        readAndClose(server); // reads "quit" from System.in and closes the server.
        connectAndPrintNonBlocking(server); // connects to the server and prints received hello-world-bytes.
        final Selector selector = Selector.open();
        while (server.register(selector, OP_ACCEPT, null).isValid()) {
            if (selector.select(100L) == 0) {
                continue;
            };
            for (final Iterator<SelectionKey> i = selector.selectedKeys().iterator(); i.hasNext(); i.remove()) {
                final SelectionKey selectionKey = i.next();
                if (!selectionKey.isValid()) {
                    continue;
                }
                if (selectionKey.isAcceptable()) {
                    final SocketChannel client = server.accept();
                    assert client.isBlocking();
                    client.configureBlocking(false);
                    final ByteBuffer buffer = allocate(BYTES);
                    helloWorld.put(buffer);
                    buffer.flip();
                    client.register(selector, OP_WRITE, buffer);
                } else if (selectionKey.isWritable()) {
                    final SocketChannel channel = (SocketChannel) selectionKey.channel();
                    final ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
                    // TODO: 2019-12-29 Implement!
                }
            }
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    private HelloWorldMain() {
        super();
    }
}
