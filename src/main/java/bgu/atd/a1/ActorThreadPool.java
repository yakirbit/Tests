package bgu.atd.a1;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * represents an actor thread pool - to understand what this class does please
 * refer to your assignment.
 * <p>
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 */
public class ActorThreadPool {
    private AtomicBoolean exit = new AtomicBoolean(false);
    private final Integer nthreads;

    private final Lock lockThreads = new ReentrantLock();
    private final List<Thread> activeThreads = new ArrayList<>();
    private final List<Thread> notActiveThreads = new ArrayList<>();

    private final Map<String, Lock> actorLock = new ConcurrentHashMap<>();
    private final HashMap<String, PrivateState> actorsState = new HashMap<>();
    private final Map<String, Queue<Action<?>>> actorsQueue = new ConcurrentHashMap<>();

    /**
     * creates a {@link ActorThreadPool} which has nthreads. Note, threads
     * should not get started until calling to the {@link #start()} method.
     * <p>
     * Implementors note: you may not add other constructors to this class nor
     * you allowed to add any other parameter to this constructor - changing
     * this may cause automatic tests to fail..
     *
     * @param nthreads the number of threads that should be started by this thread
     *                 pool
     */
    public ActorThreadPool(int nthreads) {
        this.nthreads = nthreads;
    }

    /**
     * getter for actors
     *
     * @return actors
     */
    public Map<String, PrivateState> getActors() {
        return actorsState;
    }

    /**
     * getter for actor's private state
     *
     * @param actorId actor's id
     * @return actor's private state
     */
    public PrivateState getPrivateState(String actorId) {
        return actorsState.get(actorId);
    }

    /**
     * submits an action into an actor to be executed by a thread belongs to
     * this thread pool
     *
     * @param action     the action to execute
     * @param actorId    corresponding actor's id
     * @param actorState actor's private state (actor's information)
     */
    public void submit(Action<?> action, String actorId, PrivateState actorState) {
        if (!exit.get()) {
            actorsState.putIfAbsent(actorId, actorState);
            actorsQueue.putIfAbsent(actorId, new ConcurrentLinkedQueue<>());
            actorLock.putIfAbsent(actorId, new ReentrantLock());
            actorsQueue.get(actorId).add(action);

            lockThreads.lock();
            if (activeThreads.size() < actorsQueue.keySet().size() && activeThreads.size() < nthreads) {
                Thread worker = notActiveThreads.remove(0);
                worker.start();
                activeThreads.add(worker);
            }
            lockThreads.unlock();
        }
    }

    /**
     * closes the thread pool - this method interrupts all the threads and waits
     * for them to stop - it is returns *only* when there are no live threads in
     * the queue.
     * <p>
     * after calling this method - one should not use the queue anymore.
     *
     * @throws InterruptedException if the thread that shut down the threads is interrupted
     */
    public void shutdown() throws InterruptedException {
        exit.set(true);
        for (Thread thread : activeThreads) {
            thread.join();
        }
        System.out.println("exit");
    }

    private void task() {
        while (true) {
            lockThreads.lock();
            if (exit.get()) {
                boolean all_empty = true;
                for (Queue<Action<?>> queue : actorsQueue.values()) {
                    if (queue.size() > 0) {
                        all_empty = false;
                        break;
                    }
                }
                if (all_empty) {
                    lockThreads.unlock();
                    break;
                }
            }
            if (activeThreads.size() > actorsQueue.keySet().size()) {
                activeThreads.remove(Thread.currentThread());
                notActiveThreads.add(Thread.currentThread());
                lockThreads.unlock();
                Thread.currentThread().interrupt();
            } else {
                lockThreads.unlock();
            }
            for (String actor : actorLock.keySet()) {
                try {
                    if (actorLock.get(actor).tryLock()) {
                        Action<?> currAction = actorsQueue.get(actor).poll();
                        if (currAction != null) {
                            currAction.handle(this, actor, actorsState.get(actor));
                        }
                        actorLock.get(actor).unlock();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * start the threads belongs to this thread pool
     */
    public void start() {
        int i = 0;
        while (i < nthreads) {
            Thread worker = new Thread(() -> task());
            notActiveThreads.add(worker);
            i = i + 1;
        }
    }
}