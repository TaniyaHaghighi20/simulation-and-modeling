
package simulator1;

import java.io.IOException;


public class Simulator1 {

	/**
	 * @param args the command line arguments
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
//		int[] numOfQueues;
//		int[] numOfServers;
//		int[] queueSize;
//		int[] dev;
//		int[] mean;
//		int numOfLayers;
//		BufferedWriter writer = new BufferedWriter(new FileWriter("output2.txt"));
//		int[][] c = { { 1, 2, 2, 5, 8 }, { 1, 3, 100000, 4, 15 }, { 1, 2, 200000, 2, 3 } };
//		Executer ex = new Executer(1000000, 2f, c);
//		ex.simualte();
//		writer.write(1 + "," + c[0][2] + "," + c[0][1] + "," + c[0][4] + "," + c[0][3] + ",");
//		writer.write(1 + "," + c[1][2] + "," + c[1][1] + "," + c[1][4] + "," + c[1][3] + ",");
//		writer.write(1 + "," + c[2][2] + "," + c[2][1] + "," + c[2][4] + "," + c[2][3] + ",");
//
//		for (int l = 0; l < 3; l++) {
//
//			writer.write(ex.observer.config.queues[l][0].queueMean() + ",");
//			writer.write(ex.observer.config.queues[l][0].queueMax() + ",");
//			writer.write(ex.observer.config.queues[l][0].queueVar() + ",");
//
//		}
//
//		writer.write((double) ex.observer.sumWaitingTime[0] / ex.observer.totalArrived[0] + ",");
//		writer.write((double) ex.observer.sumTurnaroundTime[0] / ex.observer.totalServed[0] + ",");
//		writer.write(ex.observer.totalArrived[0] + ",");
//		writer.write(ex.observer.totalServed[0] + ",");
//		writer.write((double) ex.observer.totalServed[0] / Executer.simulationTime + ",");
//
//		for (int l = 0; l < 3; l++) {
//			for (int i = 0; i < ex.observer.config.numOfServers[l]; i++) {
//				writer.write(ex.observer.config.servers[l][i].utilization()[0] + ",");
//				writer.write(ex.observer.config.servers[l][i].blockTime() + ",");
//			}
//		}
//
//		writer.close();

		Coordinator coordinator = new Coordinator();
		try {
			coordinator.experiment(1f, 8);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
