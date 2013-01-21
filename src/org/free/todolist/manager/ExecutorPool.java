/**
 * 
 * File: ExecutorPoolImpl.java
 * Create: 2009-3-17
 */
package org.free.todolist.manager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.free.todolist.util.PropLoader;

/**
 * A pool can provides <code>Executor</code> resources.
 * 
 * @author dong.hu@china.jinfonet.com
 * 
 */
public final class ExecutorPool {

    private static ExecutorPool instance;
    private static int cpus;
    private static Properties config;

    static {
        cpus = Runtime.getRuntime().availableProcessors();
        config = PropLoader.getProperties("ExecutorPool.properties");
    }

    /**
     * Return the instance of executors pool
     * 
     * @return
     */
    public static ExecutorPool getInstance() {
        synchronized (ExecutorPool.class) {
            if (instance == null) {
                instance = new ExecutorPool();
            }
        }

        return instance;
    }

    private String name;
    private int total;
    private List<Executor> executors;
    private Set<Executor> actives;

    private ExecutorPool() {
        int exp = Integer.parseInt(config.getProperty("pool.size", "7"), 10);
        this.total = cpus * _exp2(exp);
        this.name = config.getProperty("pool.name", "ExecutorPool");
        executors = new ArrayList<Executor>(total);
        actives = new HashSet<Executor>(total);
    }

    /**
     * Return total number of executor in the pool.
     * 
     * @return
     */
    public int total() {
        return available() + active();
    }

    /**
     * Return available executors number
     * 
     * @return
     */
    public int available() {
        synchronized (executors) {
            return executors.size();
        }
    }

    /**
     * Return active executors in the pool.
     * 
     * @return
     */
    public int active() {
        synchronized (actives) {
            return actives.size();
        }
    }

    /**
     * Gets an <code>Executor</code> from this pool with specified timeout.
     * 
     * @param timeout
     *            -1: if there is not an available executor then return null. 0:
     *            blocked until there is an available executor. >0: wait timeout
     *            millisecond
     * @return an <code>Executor</code> or null
     * @exception TimeoutException
     *                if timeout while the current thread was waiting for a
     *                notification.
     * @see com.jinfonet.webreport.util.concurrent.Executor
     */
    public Executor getExecutor(long timeout) throws TimeoutException {

        synchronized (executors) {

            ExecutorImpl E;

            if (!executors.isEmpty()) {
                E = (ExecutorImpl) executors.remove(0);
                synchronized (actives) {
                    actives.add(E);
                }
                return E;
            } else {
                int count = active();
                if (count < total) {
                    String _name = name + "." + count;
                    E = new ExecutorImpl(_name, 0);
                    synchronized (actives) {
                        actives.add(E);
                    }
                    E.start();
                    while (!E.isAlive()) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                        }
                    }
                    return E;
                }
            }

            if (timeout < 0)
                return null;

            // Else block here until has available executors
            long start = System.currentTimeMillis();
            try {
                executors.wait(timeout);
            } catch (InterruptedException e) {
                return null;
            }
            long now = System.currentTimeMillis();
            long timeSoFar = now - start;

            if (timeSoFar > timeout)
                throw new TimeoutException();

            return getExecutor(timeout - timeSoFar);

        }
    }

    /**
     * Put executor to pool
     * 
     * @param e
     */
    private void putExecutor(Executor e) {
        synchronized (actives) {
            actives.remove(e);
        }

        synchronized (executors) {
            executors.add(e);
            executors.notifyAll();
        }
    }

    /**
     * Destroy this executors pool
     */
    public void destroy() {
        Executor E;

        synchronized (actives) {
            Iterator<Executor> it = actives.iterator();
            while (it.hasNext()) {
                E = it.next();
                E.cancel();
                it.remove();
            }
        }
        actives = null;

        synchronized (executors) {
            while (!executors.isEmpty()) {
                E = executors.remove(0);
                E.cancel();
            }
        }
        executors = null;

        config.clear();
        config = null;

        instance = null;
    }

    private class ExecutorImpl extends Thread implements Executor {

        private long timeout = 0;

        private Signaler loopEvent;
        private Signaler exitEvent;

        private Task task;

        public ExecutorImpl(String name) {
            super(name);
            loopEvent = new Signaler();
            exitEvent = new Signaler();
        }

        public ExecutorImpl(String name, long timeout) {
            this(name);
            this.timeout = timeout < 0 ? 0 : timeout;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * com.jinfonet.webreport.util.concurrent.Executor#setTask(com.jinfonet
         * .webreport.util.concurrent.Task)
         */
        public void setTask(Task task) {
            this.task = task;
        }

        /*
         * (non-Javadoc)
         * 
         * @see com.jinfonet.webreport.util.concurrent.Executor#begin()
         */
        public void begin() {
            loopEvent.raise(Signaler.SIG1);
        }

        /*
         * (non-Javadoc)
         * 
         * @see com.jinfonet.webreport.util.concurrent.Executor#cancel()
         */
        public void cancel() {
            loopEvent.raise(Signaler.SIG0);
            exitEvent.waitSignal(1000);
            this.interrupt();
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Thread#run()
         */
        public void run() {
            boolean loop = true;
            while (loop) {
                int e = loopEvent.waitSignal(timeout);
                switch (e) {
                case Signaler.SIG0:
                    loop = false;
                    handleExitEvent();
                    break;
                case Signaler.SIG1:
                    handleWakeEvent();
                    break;
                case Signaler.SIG2:
                    handleTimeoutEvent();
                    break;
                default:
                    break;
                }
                //
                Thread.yield();
            }

            // Run here the thread is exited
            exitEvent.raise(Signaler.SIG0);
        }

        private void handleWakeEvent() {
            if (task != null) {
                String name = super.getName();
                super.setName(name + "(" + task.getId() + ")");
                try {
                    task.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    // Set task to null
                    task = null;

                    // Return back executor to the pool
                    super.setName(name);
                    putExecutor(this);
                }
            }
        }

        private void handleExitEvent() {
            // TODO
        }

        private void handleTimeoutEvent() {
            // TODO
        }

    }

    private static int _exp2(int n) {
        int r = 1;
        for (int i = 0; i < n; i++)
            r <<= 1;

        return r;
    }

}
