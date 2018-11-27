import java.util.Random;





public class Main {
	static final int THREAD = 8;
	LazyLinkedList<Integer> list = new LazyLinkedList<>();
	static int x=0;
	
	
	public void test() throws Exception {
		System.out.println("list created : " + list.toString());
		System.out.printf(list.toString());
		Thread[] threads = new Thread[THREAD];
		for (int i = 0; i < THREAD; ++i) {
			threads[i]= new MyThread();
		}
		for (int i = 0; i < THREAD; ++i) {
			System.out.println("thread"+ i);
			threads[i].start();
		}
		for(int i = 0; i < THREAD; ++i) {
			threads[i].join();

		}
		System.out.println(list.toString());
		
		
	}
	public static void main(String[] args) throws InterruptedException {
		Main mpt = new Main(); // Would like to be able to pass this # of threads

		try {
			mpt.test();
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
				list.add(integer);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (list.contains(integer)) {
					System.out.println(integer+ "EXISTS"+ (integer -1));
					System.out.printf(list.toString());
					list.remove(integer+1);
					//System.out.println("---"+list.toString());
					list.replace(integer, 5);
					//System.out.println("---"+list.toString());
					
				}

			}
		}
	}
}
