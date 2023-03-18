/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator1;

import java.util.Random;


public class Executer {
	public static int simulationTime;
	public static int clock;
//	public int batch_clock;
	public int[][] eventTable;
	public int eventArr = 0;
	public static Config config;
//	public static int totalClock;
//	public ArrayList<Layer> layers = new ArrayList<>();
	Observer observer;
	public float lambda;
	int[][] c;
//	public static int round = 0;
//	BufferedWriter writer;

	public Executer(int st, float lambda, int[][] c) {
		this.c = c;
		config = new Config(c);
		observer = new Observer(config);
		simulationTime = st;
		this.lambda = lambda;
//		this.writer = writer;
		clock = 0;
//		round = 0;
//		totalClock = 0;
		eventTable = new int[c.length][];
		eventArr = Arr();
		for (int i = 0; i < c.length; i++) {
			eventTable[i] = new int[c[i][1]];
			for (int j = 0; j < eventTable[i].length; j++) {
				eventTable[i][j] = simulationTime + 1;
			}
		}

	}

	public double simualte() {
		while (true) {
//			int lastClock = clock;

			int[] idx = min(eventTable);
			int i = 1;
			if (idx[0] == -1) {
				i = 0;
				clock = eventArr;
			} else {
				clock = eventTable[idx[0]][idx[1]];
			}
//			if (batchReport(lastClock)) {
////				System.out.println(round);
//				lastClock = 0;
//				idx = min(eventTable);
//				if (idx[0] == -1) {
//					i = 0;
//					clock = eventArr;
//				} else {
//					clock = eventTable[idx[0]][idx[1]];
//				}
//			}

			if (clock > simulationTime) {
//				report();

				return ((double) observer.sumTurnaroundTime[0] / observer.totalServed[0]);
			}

			switch (i) {
			case 0:
				arrival();
				break;
			case 1:
				endService(idx[0], idx[1]);
				break;

			}
		}
	}

//	private boolean batchReport(int lastClock) {
//
//		if (clock >= (simulationTime)) {
//			resetEvents();
//			round++;
//			if (round < 10) {
//				clock = 0;
//
//			}
//			observer.endBatchTime = System.nanoTime();
//			write();
////			report();
//			for (int l = 0; l < config.numOfLayers; l++) {
//				for (int j = 0; j < config.servers[l].length; j++) {
//					config.servers[l][j].reset();
//				}
//				for (int j = 0; j < config.queues[l].length; j++) {
//					config.queues[l][j].reset();
//				}
//			}
//			observer.reset();
//			eventArr = Arr();
//			observer.startBatchTime = System.nanoTime();
//			return true;
//		} else {
//			totalClock += clock - lastClock;
//			return false;
//		}
//
//	}

//	private void resetEvents() {
//		for (int i = 0; i < c.length; i++) {
//			for (int j = 0; j < eventTable[i].length; j++) {
//				if (config.servers[i][j].status != 0) {
//					eventTable[i][j] = eventTable[i][j] - clock;
//				}
//			}
//		}
//	}

//	private void write() {
//		try {
//			observer.write(lambda, writer);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}

	public void arrival() {
		Entity e = new Entity(1, clock, Srv(0));// type=1 professor, arrival time =clock, service time =pSrv() we
												// calculate it here

		observer.totalArrived[0]++;
		eventArr = clock + Arr();
		boolean isServerEmpty = false;
		for (int i = 0; i < config.servers[0].length; i++) {
			if (config.servers[0][i].status() == 0) {
				config.servers[0][i].occupy(e);
				eventTable[0][i] = clock + e.serviceTime;
				isServerEmpty = true;
				break;
			}
		}
		if (!isServerEmpty) {
//			boolean isQueueFull = true;
			for (int j = 0; j < config.queues[0].length; j++) {
				// if buffer was not full
//				if (config.layers.get(0).queues[j].size() < config.layers.get(0).queueSize) {
				config.queues[0][j].add(e);
//					isQueueFull = false;
//					break;
//				}
			}
//			if (isQueueFull) {
//				observer.discarded++;
//			}
		}
	}

	public void endService(int layer, int indx) {
		int counter = 0;
		int nextLayer = layer + 1;
		if (nextLayer < config.numOfLayers) {
			for (int j = 0; j < config.queues[nextLayer].length; j++) {
				if (config.queues[nextLayer][j].size() >= config.queueSize[nextLayer]) {
					counter++;
				}
			}
			if (counter == config.queues[layer].length) {
				config.servers[layer][indx].block();// server is blocked
				eventTable[layer][indx] = simulationTime + 1;
				return;
			}
		}

		Entity e = config.servers[layer][indx].release();

		if (nextLayer < config.numOfLayers) {
			e.serviceTime = Srv(nextLayer);
			startNextProccess(e, nextLayer);
		} else {
			observer.sumTurnaroundTime[0] += (e.difTime()); // 0 for professor and 1 for student
			observer.totalServed[0]++;
		}

		// crash if e in null because of an error
		boolean isEntityInQueue = false;
		for (int j = 0; j < config.queues[layer].length; j++) {
			if (!config.queues[layer][j].isEmpty()) {
				e = config.queues[layer][j].remove();
				observer.sumWaitingTime[0] += (e.difTimeWT());
				config.servers[layer][indx].occupy(e);
				eventTable[layer][indx] = clock + e.serviceTime;
				isEntityInQueue = true;
				break;
			}
		}

		if (!isEntityInQueue) {
			// we already released the server
			eventTable[layer][indx] = simulationTime + 1;
		} else {
			unblockPrevLayerServers(layer - 1);
		}

	}

	private void unblockPrevLayerServers(int layer) {
//		boolean isBlocked = true;
//		while (isBlocked && layer >= 0) {
//			isBlocked = false;
		if (layer >= 0) {
			for (int i = 0; i < config.servers[layer].length; i++) {
				if (config.servers[layer][i].status == -1) {
//							eventTable[l - 1][i] = clock;
					endService1(layer, i);
//						isBlocked = true;
					break;
				}
			}
		}

//			layer--;
//		}
	}

	private void endService1(int layer, int indx) {
		int nextLayer = layer + 1;
		Entity e = config.servers[layer][indx].release();

		if (nextLayer < config.numOfLayers) {
			e.serviceTime = Srv(nextLayer);
			startNextProccess(e, nextLayer);
		} else {
			observer.sumTurnaroundTime[0] += (e.difTime()); // 0 for professor and 1 for student
			observer.totalServed[0]++;
		}

		// crash if e in null because of an error
		boolean isEntityInQueue = false;
		for (int j = 0; j < config.queues[layer].length; j++) {
			if (!config.queues[layer][j].isEmpty()) {
				e = config.queues[layer][j].remove();
				observer.sumWaitingTime[0] += (e.difTimeWT());
				config.servers[layer][indx].occupy(e);
				eventTable[layer][indx] = clock + e.serviceTime;
				isEntityInQueue = true;
				break;
			}
		}
		if (!isEntityInQueue) {
			// we already released the server
			eventTable[layer][indx] = simulationTime + 1;
		} else {
			unblockPrevLayerServers(layer - 1);
		}

	}

	private void startNextProccess(Entity e, int l) {
//		Layer layer = config.layers.get(l);
		boolean isServerEmpty = false;
		for (int i = 0; i < config.servers[l].length; i++) {
			if (config.servers[l][i].status() == 0) {
				config.servers[l][i].occupy(e);
				eventTable[l][i] = clock + e.serviceTime;
				isServerEmpty = true;
				break;
			}
		}
		if (!isServerEmpty) {
			for (int j = 0; j < config.queues[l].length; j++) {
				// if buffer was not full
				config.queues[l][j].add(e);
				e.pClock = clock;
				break;
			}
		}

	}

	public void report() {
//		int i = 0;
//		for (Layer layer : config.layers) {
		for (int j = 0; j < config.numOfLayers; j++) {
			observer.report((j == config.numOfLayers - 1), j);
		}
	}

	private int[] min(int[][] array) {
		int index = -1;
		int layer = -1;
		int minValue = eventArr;
		for (int j = 0; j < array.length; j++) {
			for (int i = 0; i < array[j].length; i++) {
				if (array[j][i] < minValue) {
					minValue = array[j][i];
					layer = j;
					index = i;
				}
			}
		}

		int[] idx = { layer, index };
		return idx;
	}

	private int Arr() {
//		-1/2*LN(1-RAND())
		double rnd = 1 - Math.random();
		int rand = (int) Math.abs(-1 / lambda * (Math.log(rnd)));
		return Math.abs(rand);
//		return 1 + (int) ((Math.random() * 5));
	}

	private int Srv(int l) {

		double z = 0;
		int mean = config.mean[l];
		int dev = config.dev[l];

		Random r = new Random();
		z = r.nextGaussian();
		return Math.abs((int) (z * dev + mean));

//		return 2 + (int) (Math.random() * 6);
	}

}
