import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.*;


public class Main {
	static int THREAD_COUNT = 10;
	static int BOUND_COUNT = 10;
	static LazyLinkedList<Integer> list = new LazyLinkedList<>();
	static int x=0;
	static int numToAdd = 0;

	//counts of threads spawned per operation.
	static volatile int addThreadCount = 0;
	static volatile int removeThreadCount  = 0;
	static volatile int replaceThreadCount = 0;




	public void test() throws Exception {

		//System.out.println("list created : " + list.toString());
		System.out.printf(list.toString());
		Thread[] threads = new Thread[THREAD_COUNT];
		for (int i = 0; i < THREAD_COUNT; ++i) {
			threads[i]= new MyThread();
		}
		for (int i = 0; i < THREAD_COUNT; ++i) {
			System.out.println("thread : "+ i);
			threads[i].start();
			//threads[i].sleep(500);
		}
		for(int i = 0; i < THREAD_COUNT; ++i) {
			threads[i].join();

		}
		


	}






	public static void main(String[] args) throws InterruptedException {
		Main mpt = new Main(); // Would like to be able to pass this # of threads

		try {
				
			
//			PrintStream out = new PrintStream(new FileOutputStream("Hello.csv"));
//			System.setOut(out);
			
			//mpt.test();
			Thread[] opThreads = new Thread[THREAD_COUNT];
			System.out.println("\nInsertion Threads, Removal Threads, Replacement Threads");
			for(int i=0;i<THREAD_COUNT;i++){
				opThreads[i] = mpt.getRandomThread();
				//System.out.println("THREAD TYPE : " + opThreads[i].getClass().getCanonicalName());
			}
			System.out.printf("\n");
			System.out.printf("%d, %d, %d \n", addThreadCount,removeThreadCount, replaceThreadCount);
			//System.out.println("ADD THREAD COUNT : \t" + addThreadCount);
			//System.out.println("Remove THREAD COUNT : \t" + removeThreadCount);
			//System.out.println("Replace THREAD COUNT : \t" + replaceThreadCount);
			System.out.println("Time,Operation,Status,Item1,Item2,Result,List");
			for(Thread t:opThreads){
				t.start();
				//t.sleep(500);
			}

			for(Thread t:opThreads){
				t.join();
			}
			
		

		}
		catch (Exception e) {
		}

	}
	class MyThread extends Thread {
		Random r = new Random();
		public void run() {
			for (int i = 0; i < 1; ++i) {
				//Integer integer = r.nextInt(50);
				Integer integer=x++;
				System.out.println(this.getName() + ": Trying to add " + integer);
				System.out.println(this.getName() + ": Addition status : " + list.add(integer));
				System.out.println(this.getName() + " : after adding " + integer + ". LIST : " + list.toString() );
//				try {
//					Thread.sleep(500);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
				if (list.contains(integer)) {
					//System.out.println(integer+ " : EXISTS "+ (integer -1));
					System.out.println( this.getName() + " : CURRENT LIST " + list.toString());
					boolean delStatus = list.remove(integer);
					System.out.println( this.getName() + " :  deleting " + (integer + 1) + " . delete Status : "  + delStatus );
					boolean replaceStatus = list.replace(integer, 5);
					System.out.println(this.getName() + " : replacing " + integer + " by ---"+5  + ". replace Status : " + replaceStatus );
					System.out.println(this.getName() + ": List after replace : " + list.toString());
				}

			}
		}
	}

	public Thread getRandomThread(){
		Random r = new Random();

		int val = r.nextInt(100);
		Thread t = null;
		if(val <= 34){
			t =new ThreadAdd();
		}else if(val > 34 && val <=68){
			t = new ThreadRemove();
		}else{
			t = new ThreadReplace();
		}
		return t;
	}

	class ThreadAdd extends Thread {

		Random random = new Random();

		ThreadAdd(){
			super();
			addThreadCount++;
			//System.out.println("Add Thread Spawned : " + this.getName());
			//System.out.println("Add Thread Count : " + addThreadCount);
		}
		@Override
		public void run(){
			addElement();
		}

		private synchronized void addElement() {
			numToAdd = random.nextInt(BOUND_COUNT);
			//System.out.println(this.getName() + " - numToAdd : " + numToAdd);
			boolean addResult = list.add(numToAdd);
			//System.out.println(this.getName() + "- Add Status : " + addResult +" ADD -> " + list.toString());
		}
	}

	class ThreadRemove extends Thread {

		Random random = new Random();

		ThreadRemove(){
			super();
			removeThreadCount++;
			//System.out.println("Remove Thread Spawned : " + this.getName());
			//System.out.println("Remove Thread Count : " + removeThreadCount);

		}
		@Override
		public void run(){
			//numToAdd = random.nextInt(BOUND_COUNT);
			removeElement();
		}

		private synchronized void  removeElement() {
			//System.out.println(this.getName() + " - removing : " + numToAdd);
			boolean removeResult = list.removeItem(numToAdd);
			//System.out.println(this.getName() +" - Remove Status : " + removeResult + " | Remove -> " + list.toString());
		}
	}

	class ThreadReplace extends Thread {

		Random random = new Random();

		ThreadReplace(){
			super();
			replaceThreadCount++;
			//System.out.println("Replace Thread Spawned : " + this.getName());
			//System.out.println("Replace Thread Count : " + replaceThreadCount);
		}
		@Override
		public void run(){
			replaceElement();

		}

		private synchronized void replaceElement() {
			int numToReplace = numToAdd*10;
			//System.out.println( this.getName() + " - Replacing : " + numToAdd + " by " + numToReplace);
			boolean replaceResult = list.replaceItem(numToAdd, numToReplace);
			//System.out.println(this.getName() + " - Replace Status : " + replaceResult + "  | Replace -> " + list.toString());
		}

	}
}


