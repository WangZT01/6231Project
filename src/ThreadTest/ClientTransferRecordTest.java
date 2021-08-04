package ThreadTest;

import RecordFile.TeacherRecord;
import Server.MethodImpl;

import java.rmi.RemoteException;
import java.text.NumberFormat;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

;

public class ClientTransferRecordTest {


    static int count = 0;
    // 总访问量是clientNum，并发量是threadNum
    int threadNum = 5;
    int clientNum = 5;

    float avgExecTime = 0;
    float sumexecTime = 0;
    long firstExecTime = Long.MAX_VALUE;
    long lastDoneTime = Long.MIN_VALUE;
    float totalExecTime = 0;

    public static void main(String[] args) {


        try {
            new ClientTransferRecordTest().run();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.out.println("finished!");
    }

    public void run() throws RemoteException {

        MethodImpl testMethodImpl = new MethodImpl();
        testMethodImpl.name = "LVL";
        //testMethodImpl.createTRecord("DDO9999","test", "clientNum" + clientNum, "Add", "4008517517", "Spec", "ddo");
        final ConcurrentHashMap<Integer, ThreadRecord> records = new ConcurrentHashMap<Integer, ThreadRecord>();

        // 建立ExecutorService线程池，threadNum个线程可以同时访问
        ExecutorService exec = Executors.newFixedThreadPool(threadNum);
        // 模拟clientNum个客户端访问
        final CountDownLatch doneSignal = new CountDownLatch(clientNum);

        for (int i = 0; i < clientNum; i++) {
            Runnable run = new Runnable() {
                public void run() {
                    int index = getIndex();
                    long systemCurrentTimeMillis = System.currentTimeMillis();
                    try {
                            testMethodImpl.transferRecord("LVL9999","SR10001","DDO");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    records.put(index, new ThreadRecord(systemCurrentTimeMillis, System.currentTimeMillis()));
                    doneSignal.countDown();// 每调用一次countDown()方法，计数器减1
                }
            };

            exec.execute(run);
            //exec.shutdown();
        }

        try {
            // 计数器大于0 时，await()方法会阻塞程序继续执行
            doneSignal.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /**
         * 获取每个线程的开始时间和结束时间
         */
        for (int i : records.keySet()) {
            ThreadRecord r = records.get(i);
            sumexecTime += ((double) (r.endTime - r.startTime)) / 1000;

            if (r.startTime < firstExecTime) {
                firstExecTime = r.startTime;
            }
            if (r.endTime > lastDoneTime) {
                this.lastDoneTime = r.endTime;
            }
        }

        this.avgExecTime = this.sumexecTime / records.size();
        this.totalExecTime = ((float) (this.lastDoneTime - this.firstExecTime)) / 1000;
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(4);

        System.out.println("======================================================");
        System.out.println("Number of threads:\t" + threadNum);
        System.out.println("Number of clients:\t" + clientNum);
        System.out.println("Average execution time:\t" + nf.format(this.avgExecTime) + "second");
        System.out.println("Total execution time:\t" + nf.format(this.totalExecTime) + "second");
        System.out.println("Throughput:\t" + nf.format(this.clientNum / this.totalExecTime) + "times/second");
        testMethodImpl.shutdown();
    }

    public static int getIndex() {
        return ++count;
    }

}
