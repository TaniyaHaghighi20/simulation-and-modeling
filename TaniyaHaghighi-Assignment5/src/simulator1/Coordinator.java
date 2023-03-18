package simulator1;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


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

						Executer ex = new Executer(100000, num, c, writer);
//			Executer ex = new Executer(100000, 0.7f, c, writer);

						ex.simualte();

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
