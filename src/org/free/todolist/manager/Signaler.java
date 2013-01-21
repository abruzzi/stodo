/**
 * Copyright (c) Jinfonet Inc. 2000-2009, All rights reserved. 
 * 
 * File: Signaler.java
 * Create: 2009-3-17
 */
package org.free.todolist.manager;

/**
 * @author dong.hu@china.jinfonet.com
 * 
 */
public final class Signaler {

    /** Signaler was notified */
    public static final int SIG0 = 0;

    /** Signaler was notified repeatedly */

    public static final int SIG1 = 1;

    /** Signaler was timeout */
    public static final int SIG2 = 2;

    /* Instruction mask register. */
    private int bits = 0;
    /* Signal count */
    private int count = 0;

    /**
     * Raise event with specified SIG
     * 
     * @param E
     */
    synchronized public void raise(int E) {
        if (E == Signaler.SIG1) {
            count++;
        }
        bits |= bit(E);

        notify();
    }

    /**
     * Wait a signal with specified timeout
     * 
     * @param timeout
     * @return
     */
    synchronized public int waitSignal(long timeout) {
        if (bits == 0) {
            long start = System.currentTimeMillis();
            try {
                wait(timeout);
            } catch (InterruptedException e) {
                // e.printStackTrace();
                return -1;
            }

            long end = System.currentTimeMillis();
            if (timeout > 0 && timeout <= (end - start)) {
                bits = 0;
                return Signaler.SIG2;
            }
        }

        if ((bits & bit(Signaler.SIG0)) != 0) {
            bits &= ~bit(Signaler.SIG0);
            return Signaler.SIG0;
        }

        if ((bits & bit(Signaler.SIG1)) != 0) {
            count--;
            if (count == 0)
                bits &= ~bit(Signaler.SIG1);
            return Signaler.SIG1;
        }

        // Never run to here
        return -1;

    }

    private int bit(int bitIndex) {
        return 1 << (bitIndex & 31);
    }
}
