package simulator1;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class SACoordinator {

	private long getTime() {
		return System.nanoTime();
	}

	private int[][] generateInitialConfiguration(int numLayers) {
		int[][] array = new int[numLayers][5];// contains # of Q,QS,# of Servers,dev,Mean in each layer
		for (int j = 0; j < numLayers; j++)
			array[j][0] = 1;// num of queues in each layer

		for (int j = 0; j < numLayers; j++)
			array[j][3] = (int) (Math.random() * (5 - 1) + 1);// dev

//		for (int j = 0; j < numLayers; j++)
//			array[j][4] = (int) (Math.random() * (Math.pow(10, (numLayers - 1)) - 1) + 1);// mean
		array[0][4] = 2;
		array[1][4] = 5;
		array[2][4] = 20;
		array[3][4] = 3;
		array[4][4] = 25;
		array[5][4] = 2;
		array[6][4] = 8;
		array[7][4] = 20;

		return array;
	}

//	public void experiment() throws IOException {
//		BufferedWriter writer = new BufferedWriter(new FileWriter("SA_k.txt"));
//		int numLayers = 3;
//		double[] val;
//		int[][] c = generateInitialConfiguration(numLayers);
//		for (float k = 0.3f; k <= 1.5; k += 0.3) {
//			System.out.println("==============================K:" + k + "==============================");
//			SA sa = new SA(k, 5000, 50, 1000, 15, 2, 100);
//
////			long startTime = getTime();
//			val = sa.solve1(c);
////			long endTime = getTime();
//			writer.write(k + ",");
//			writer.write(val[0] + ",");
//			writer.write(val[1] + ",");
////			writer.write((endTime - startTime) + ",");
//
//			writer.write("\n");
//
//		}
//		writer.close();
//	}

//	public void experiment() throws IOException {
//		BufferedWriter writer = new BufferedWriter(new FileWriter("SA_totalCost.txt"));
//		int numLayers = 8;
//		double[] val;
//		int[][] c = generateInitialConfiguration(numLayers);
//		for (int num = 400; num <= 1600; num += 400) {
//			System.out.println("==============================num:" + num + "==============================");
//			SA sa = new SA(0.3, 3000, 50, num, 20, 2, 50);
////			long startTime = getTime();
//			val = sa.solve1(c);
////			long endTime = getTime();
//			writer.write(num + ",");
//			for (int i = 0; i < val.length; i++) {
//				writer.write(val[i] + ",");
//			}
////			writer.write((endTime - startTime) + ",");
//
//			writer.write("\n");
//
//		}
//		writer.close();
//	}

//	public void experiment() throws IOException {
//		BufferedWriter writer = new BufferedWriter(new FileWriter("SA_server_cost.txt"));
//		int numLayers = 8;
//		double[] val;
//		int[][] c = generateInitialConfiguration(numLayers);
//		for (int num = 15; num <= 30; num += 5) {
//			System.out.println("==============================num:" + num + "==============================");
//			SA sa = new SA(0.3, 4000, 50, 560, num, 1, 100);
////			long startTime = getTime();
//			val = sa.solve1(c);
////			long endTime = getTime();
//			writer.write(num + ",");
//			for (int i = 0; i < val.length; i++) {
//				writer.write(val[i] + ",");
//			}
////			writer.write((endTime - startTime) + ",");
//
//			writer.write("\n");
//
//		}
//		writer.close();
//	}

	public void experiment() throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter("SA_QCost.txt"));
		int numLayers = 8;
		double[] val;
		int[][] c = generateInitialConfiguration(numLayers);
		for (int num = 1; num <= 35; num += 8) {
			System.out.println("==============================num:" + num + "==============================");
			SA sa = new SA(0.3, 2000, 50, 2400, 30, num, 50);
//			long startTime = getTime();
			val = sa.solve1(c);
//			long endTime = getTime();
			writer.write(num + ",");
			for (int i = 0; i < val.length; i++) {
				writer.write(val[i] + ",");
			}
//			writer.write((endTime - startTime) + ",");

			writer.write("\n");

		}
		writer.close();
	}

}
