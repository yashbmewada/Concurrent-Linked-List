import java.util.Random;

public class Main {
	static int BOUND = 50;
	static int NUMBER = 8;
	public static void main(String[] args) throws InterruptedException {
		

		LazyLinkedList<Integer> list = new LazyLinkedList<>();
		System.out.println("list created : " + list.toString());
		Random r = new Random();


		System.out.printf(list.toString());
		Thread[] threads = new Thread[NUMBER];
		for (int i = 0; i < NUMBER; ++i) {
			threads[i] = new Thread(new Runnable() {
				@Override
				public void run() {
					for (int i = 0; i < 1; ++i) {
						Integer integer = r.nextInt(BOUND);
						list.add(integer);
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if (list.contains(integer)) {
							// list.remove(integer);
							System.out.println("EXISTS");
						}

					}
				}
			});
			threads[i].start();
		}
		for(int i = 0; i < NUMBER; ++i) {
			threads[i].join();

		}
		System.out.println(list.toString());
		

	}
}
