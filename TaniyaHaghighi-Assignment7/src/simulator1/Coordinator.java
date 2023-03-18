package simulator1;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;



public class Coordinator {

	private int[] generateArray(int n, int min, int max) {
		int[] array = new int[n];
		for (int i = 0; i < n; i++) {
			array[i] = (int) (Math.random() * (max - min) + min);
		}
		return array;
	}

	private long getTime() {
		return System.nanoTime();
	}

	public void experiment(float n, int maxRep) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter("experiment.txt"));
		int numLayers = 3;
		int[] meanArr = generateArray(numLayers, 10, 100);
		int[] devArr = generateArray(numLayers, 2, 10);
		int[] queueSizeArr = generateArray(numLayers, 4, 200);
		int[] numOfServersArr = generateArray(numLayers, 2, 15);
		int[] numOfQueuesArr = { 1, 1, 1 };
		for (float num = .1f; num <= n; num += .1) {

			for (int qs = 2; qs <= 2000; qs += 400) {

				for (int numServer = 1; numServer <= 41; numServer += 5) {

					for (int mean = 5; mean <= 200; mean += 20) {
						System.out.println(
								"Testing n= " + num + "qs= " + qs + "numServer= " + numServer + "mean= " + mean);

						int[][] c = new int[numLayers][5];
						buildConfig(c, meanArr, devArr, queueSizeArr, numOfServersArr, numOfQueuesArr);
						c[1][2] = qs;
						c[1][4] = mean;
						c[1][1] = numServer;

						Executer ex = new Executer(1000000, num, c);
//			Executer ex = new Executer(1000000, 0.7f, c);
						long startTime = getTime();
						ex.simualte();
						long endTime = getTime();
						writer.write(num + ",");
						for (int l = 0; l < ex.observer.config.numOfLayers; l++) {
							writer.write(1 + "," + qs + "," + numServer + "," + mean + "," + devArr[l] + ",");
						}

						writer.write((endTime - startTime) + ",");

						writer.write((double) ex.observer.sumWaitingTime[0] / ex.observer.totalArrived[0] + ",");
						writer.write((double) ex.observer.sumTurnaroundTime[0] / ex.observer.totalServed[0] + ",");
						writer.write(ex.observer.totalArrived[0] + ",");
						writer.write(ex.observer.totalServed[0] + ",");
						writer.write((double) ex.observer.totalServed[0] / (Executer.simulationTime) + ",");
						double utilization_mean = 0;
						double block_mean = 0;
						for (int l = 0; l < numLayers; l++) {
							utilization_mean = 0;
							block_mean = 0;
							int i = 0;
							for (i = 0; i < ex.observer.config.numOfServers[l]; i++) {
								utilization_mean += ex.observer.config.servers[l][i].utilization()[0];
								block_mean += ex.observer.config.servers[l][i].blockTime();
							}
							writer.write(utilization_mean / i + ",");
							writer.write(block_mean / i + ",");
						}
						for (int l = 0; l < numLayers; l++) {
							writer.write(ex.observer.config.queues[l][0].queueMean() + ",");
							writer.write(ex.observer.config.queues[l][0].queueMax() + ",");
							writer.write(ex.observer.config.queues[l][0].queueVar() + ",");
						}
						writer.write("\n");

					}
				}

			}
		}
		writer.close();
	}

	private void buildConfig(int[][] c, int[] meanArr, int[] devArr, int[] queueSizeArr, int[] numOfServersArr,
			int[] numOfQueuesArr) {
		for (int i = 0; i < c.length; i++) {
			c[i][0] = numOfQueuesArr[i];
			c[i][1] = numOfServersArr[i];
			c[i][2] = queueSizeArr[i];
			c[i][3] = devArr[i];
			c[i][4] = meanArr[i];
		}

	}
}
