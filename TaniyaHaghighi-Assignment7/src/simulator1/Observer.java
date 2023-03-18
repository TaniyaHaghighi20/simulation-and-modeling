/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator1;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Observer {
	Config config;
	public int[] totalArrived; // 0 professor 1 student
	public int[] totalServed;
	public int discarded;
	public long[] sumWaitingTime;
	public long[] sumTurnaroundTime;
//	public long startBatchTime;
//	public long endBatchTime;

	public Observer(Config c) {
		config = c;// set the current configuration
		totalArrived = new int[1]; // set variables for reporting
		totalServed = new int[1];
		discarded = 0;
		sumWaitingTime = new long[1];
		sumTurnaroundTime = new long[1];
//		this.startBatchTime = 0;
//		this.endBatchTime = 0;
	}

	public void report(boolean isLast, int l) {
		NumberFormat formatter = new DecimalFormat("#0.00");
		System.out.println("------------------------------------------------------");
		System.out.println("======layer number " + (l + 1) + "======");
		for (int i = 0; i < config.numOfQueues[l]; i++) // Queues report
		{
			System.out.println("Queue " + i + " Average: " + formatter.format(config.queues[l][i].queueMean()));
			System.out.println("Queue " + i + " Maximum: " + config.queues[l][i].queueMax());
			System.out.println("Queue " + i + " Variance: " + formatter.format(config.queues[l][i].queueVar()));
		}
		if (isLast) {
			for (int i = 0; i < 1; i++)// Entity report
			{
				System.out.println("Average waiting time for Entity " + i + " without the ones in the queue: "
						+ formatter.format((double) sumWaitingTime[i] / totalServed[i]));
				System.out.println("Average Turnaround time for Entity " + i + ": "
						+ formatter.format((double) sumTurnaroundTime[i] / totalServed[i])); // we dont calculate the
//				System.out.println("number of discarded entities: " + discarded); // last
				System.out.println("Total Entity " + i + " arrived: " + totalArrived[i] + " Total served:"
						+ totalServed[i] + " Served Percentage: "
						+ formatter.format(((double) totalServed[i] / totalArrived[i]) * 100) + " Throughput: "
						+ formatter.format((double) totalServed[i] / (Executer.simulationTime)));
				while (!config.queues[l][i].isEmpty()) // adjust waiting time
					sumWaitingTime[i] += (Executer.simulationTime - config.queues[l][i].remove().arrivalTime);
				System.out.println("Average adjusted waiting time for Entity " + i + " "
						+ formatter.format((double) sumWaitingTime[i] / totalArrived[i]));
			}
		}

		for (int i = 0; i < config.numOfServers[l]; i++) // Servers report
		{
			System.out.println("Server " + i + " Entity " + 0 + " Utilization: "
					+ formatter.format(config.servers[l][i].utilization()[0]));
			System.out.println("Server " + i + " block time: " + formatter.format(config.servers[l][i].blockTime()));

		}

	}

//	public void write(float lambda, BufferedWriter writer) throws IOException {
//
//		writer.write(lambda + ",");
//		for (int l = 0; l < config.numOfLayers; l++) {
//			writer.write(1 + "," + config.queueSize[l] + "," + config.numOfServers[l] + "," + config.mean[l] + ","
//					+ config.dev[l] + ",");
//		}
//
//		writer.write((endBatchTime - startBatchTime) + ",");
//
//		writer.write((double) sumWaitingTime[0] / totalArrived[0] + ",");
//		writer.write((double) sumTurnaroundTime[0] / totalServed[0] + ",");
//		writer.write(totalArrived[0] + ",");
//		writer.write(totalServed[0] + ",");
//		writer.write((double) totalServed[0] / (Executer.simulationTime) + ",");
//		double utilization_mean = 0;
//		double block_mean = 0;
//		for (int l = 0; l < config.numOfLayers; l++) {
//			utilization_mean = 0;
//			block_mean = 0;
//			int i = 0;
//			for (i = 0; i < config.numOfServers[l]; i++) {
//				utilization_mean += config.servers[l][i].utilization()[0];
//				block_mean += config.servers[l][i].blockTime();
//			}
//			writer.write(utilization_mean / i + ",");
//			writer.write(block_mean / i + ",");
//		}
//		for (int l = 0; l < config.numOfLayers; l++) {
//			writer.write(config.queues[l][0].queueMean() + ",");
//			writer.write(config.queues[l][0].queueMax() + ",");
//			writer.write(config.queues[l][0].queueVar() + ",");
//		}
//		writer.write("\n");
//
//	}

	public void reset() {
		sumTurnaroundTime[0] = 0;
		sumWaitingTime[0] = 0;
		totalArrived[0] = 0;
		totalServed[0] = 0;
	}

}
