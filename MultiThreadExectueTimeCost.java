		package com.tech.framework.test;
		
		import java.util.List;
		import java.util.Random;
		import java.util.Timer;
		import java.util.concurrent.BrokenBarrierException;
		import java.util.concurrent.Callable;
		import java.util.concurrent.CompletionService;
		import java.util.concurrent.CyclicBarrier;
		import java.util.concurrent.ExecutionException;
		import java.util.concurrent.ExecutorCompletionService;
		import java.util.concurrent.ExecutorService;
		import java.util.concurrent.Executors;
		import java.util.concurrent.Future;
		import java.util.concurrent.ThreadPoolExecutor;
		/**
		 * to record the time each task have cost 
		 * @author lixp
		 *
		 */
		public class MultiThreadExectueTimeCost{
			private ExecutorService executorService = null;
			public MultiThreadExectueTimeCost(ExecutorService executorService) {
				// TODO Auto-generated constructor stub
				this.executorService = executorService;
				completionServcie = new ExecutorCompletionService<>(executorService);
			}
			private int threadSize = 5;
			private CyclicBarrier cb = new CyclicBarrier(threadSize + 1);
		    private CompletionService<Long> completionServcie = null;
		    public void recordTime(){
		    	for(int i = 0; i < threadSize; i++) {
		    		completionServcie.submit(new Callable<Long>(){
		    			public Long call() throws InterruptedException, BrokenBarrierException{
		    				cb.await();
		    				long start = System.currentTimeMillis();
		    				Thread.sleep(15 + new Random().nextInt(10));
		    				long end = System.currentTimeMillis();
		    				long cost = end - start;
		    				System.out.println("cost\t" + cost + "\tms");
		    				cb.await();
		    				return cost;
		    			}
		    		});
		    	}
		    }
		    public void infoTime() throws InterruptedException, ExecutionException{
		    	for(int i = 0; i < threadSize; i++) {
		    		Future<Long> f = completionServcie.take();
		    		//completionServcie.
		    		Long time = f.get();
		    		System.out.println("#" + i + "take\t" + time + "ms");
		    	}
		    }
		    public static void main(String[] args) throws InterruptedException, BrokenBarrierException, ExecutionException{
		    	ExecutorService es = Executors.newFixedThreadPool(10);
		    	MultiThreadExectueTimeCost mtet = new MultiThreadExectueTimeCost(es);
		    	mtet.recordTime();
		        /**here is beginning**/
		    	mtet.cb.await();
		    	Thread.sleep(10000);
		        /**here is end**/
		        mtet.cb.await();
		    	mtet.infoTime();
		    	es.shutdown();
		    }
		}
