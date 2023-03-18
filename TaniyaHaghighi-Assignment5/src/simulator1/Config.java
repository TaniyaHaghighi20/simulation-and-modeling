/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator1;


public class Config {
//	ArrayList<Layer> layers = new ArrayList<>();
//
//	public Config(ArrayList<Layer> layers) {
//		this.layers = layers;
//	}

	int[] numOfQueues;
	int[] numOfServers;
	int[] queueSize;
	int[] dev;
	int[] mean;
	int numOfLayers;

	Queue[][] queues;
	Server[][] servers;

	public Config(int[][] c) {
		numOfLayers = c.length;
		this.numOfQueues = new int[numOfLayers];
		this.numOfServers = new int[numOfLayers];
		this.queueSize = new int[numOfLayers];
		this.dev = new int[numOfLayers];
		this.mean = new int[numOfLayers];
		for (int i = 0; i < numOfLayers; i++) {
			this.numOfQueues[i] = c[i][0];
			this.numOfServers[i] = c[i][1];
			this.queueSize[i] = c[i][2];
			this.dev[i] = c[i][3];
			this.mean[i] = c[i][4];
		}
		servers = new Server[numOfLayers][];
		queues = new Queue[numOfLayers][];
		for (int j = 0; j < numOfLayers; j++) {

			queues[j] = new Queue[numOfQueues[j]];
			for (int i = 0; i < numOfQueues[j]; i++) {
				queues[j][i] = new Queue();
			}
			servers[j] = new Server[numOfServers[j]];
			for (int i = 0; i < numOfServers[j]; i++) {
				servers[j][i] = new Server(1);
			}

		}
		System.out.println();
	}

}

//class Layer {
//	int numOfQueues;
//	int numOfServers;
//	int queueSize;
//	int dev;
//	int mean;
//
//	Queue[] queues;
//	Server[] servers;
//
//	public Layer(int numOfQueues, int numOfServers, int queueSize, int dev, int mean) {
//		super();
//		this.numOfQueues = numOfQueues;
//		this.numOfServers = numOfServers;
//		this.queueSize = queueSize;
//		this.dev = dev;
//		this.mean = mean;
//		queues = new Queue[numOfQueues];
//		for (int i = 0; i < numOfQueues; i++) {
//			queues[i] = new Queue();
//		}
//		servers = new Server[numOfServers];
//		for (int i = 0; i < numOfServers; i++)
//			servers[i] = new Server(1);
//	}
//
//}
