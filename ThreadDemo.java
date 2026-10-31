// Multithreading demo. Three small examples, each in its own nested class.
public class ThreadDemo {

    // ---------- 1. Synchronized methods ----------
    // Only one thread at a time can run a synchronized method, so the total stays correct.
    static class FeeBox {
        private int total = 0;

        public synchronized void add(int amount) {
            total = total + amount;
        }

        public synchronized int getTotal() {
            return total;
        }
    }

    // A thread created by implementing Runnable
    static class FeeCounter implements Runnable {
        private FeeBox box;

        FeeCounter(FeeBox box) {
            this.box = box;
        }

        public void run() {
            for (int i = 0; i < 5; i++) {
                box.add(1000);
            }
        }
    }

    // ---------- 2. Inter-thread communication (wait / notify) ----------
    static class NoticeBoard {
        private String notice;
        private boolean hasNotice = false;

        public synchronized void post(String text) throws InterruptedException {
            while (hasNotice) {
                wait();                 // wait until the old notice is read
            }
            notice = text;
            hasNotice = true;
            notifyAll();                // tell the reader
        }

        public synchronized String read() throws InterruptedException {
            while (!hasNotice) {
                wait();                 // wait until a notice is posted
            }
            hasNotice = false;
            notifyAll();                // tell the writer
            return notice;
        }
    }

    // ---------- 3. Suspend, resume and stop a thread ----------
    // A thread created by extending Thread
    static class BellThread extends Thread {
        private boolean paused = false;
        private boolean stopped = false;

        public void run() {
            int ring = 1;
            try {
                while (true) {
                    synchronized (this) {
                        while (paused && !stopped) {
                            wait();     // suspended
                        }
                        if (stopped) {
                            break;
                        }
                    }
                    System.out.println("[Bell] Ring " + ring++);
                    Thread.sleep(400);
                }
            } catch (InterruptedException e) {
                System.out.println("[Bell] Interrupted");
            }
            System.out.println("[Bell] Stopped");
        }

        public synchronized void suspendThread() {
            paused = true;
        }

        public synchronized void resumeThread() {
            paused = false;
            notify();
        }

        public synchronized void stopThread() {
            stopped = true;
            notify();
        }
    }

    // ---------- Run all three demos ----------
    public static void run() {
        try {
            System.out.println("\n--- 1. Two fee counters, 5 x 1000 each ---");
            FeeBox box = new FeeBox();
            Thread t1 = new Thread(new FeeCounter(box));
            Thread t2 = new Thread(new FeeCounter(box));
            t1.start();
            t2.start();
            t1.join();                  // wait for the thread to finish
            t2.join();
            System.out.println("Total collected = " + box.getTotal() + " (expected 10000)");

            System.out.println("\n--- 2. Teacher posts notices, student reads them ---");
            NoticeBoard board = new NoticeBoard();
            Thread teacher = new Thread(() -> {
                try {
                    board.post("Exam on Monday");
                    board.post("Holiday on Friday");
                } catch (InterruptedException e) {
                    System.out.println("Interrupted");
                }
            });
            Thread student = new Thread(() -> {
                try {
                    System.out.println("Student read: " + board.read());
                    System.out.println("Student read: " + board.read());
                } catch (InterruptedException e) {
                    System.out.println("Interrupted");
                }
            });
            student.start();
            teacher.start();
            teacher.join();
            student.join();

            System.out.println("\n--- 3. Bell: run, suspend, resume, stop ---");
            BellThread bell = new BellThread();
            bell.start();
            Thread.sleep(1000);
            System.out.println(">> Suspend");
            bell.suspendThread();
            Thread.sleep(1000);
            System.out.println(">> Resume");
            bell.resumeThread();
            Thread.sleep(1000);
            System.out.println(">> Stop");
            bell.stopThread();
            bell.join();

        } catch (InterruptedException e) {
            System.out.println("Demo interrupted.");
        }
    }
}
