package com.github.jinahya.hello;

import java.nio.channels.CompletionHandler;

abstract class HelloWorldCompletionHandler<A> implements CompletionHandler<Integer, A> {

    protected HelloWorldCompletionHandler(final A attachment) {
        super();
        this.attachment = attachment;
    }

    protected final A attachment;
}
