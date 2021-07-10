package com.bob.boot.thread.multijobs.exception;

/**
 * 如果任务在执行之前，自己后面的任务已经执行完或正在被执行，则抛该exception
 * @author bob
 * @version 1.0
 */
public class JumpOverException extends RuntimeException {
    public JumpOverException() {
        super();
    }

    public JumpOverException(String message) {
        super(message);
    }
}