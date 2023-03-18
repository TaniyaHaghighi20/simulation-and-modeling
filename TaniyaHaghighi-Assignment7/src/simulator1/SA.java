package simulator1;

import java.io.IOException;

public class SA {
	double k;
	double temprature;
	int counter; // to stop the algorithm: number of not changing configs
	int totalCost;
	int serverCost;
	int QUnitCost;
	int rep;
//	int serverSpeedCost;

	public SA(double k, double temprature, int counter, int totalCost, int serverCost,
			int qUnitCost/*
							 * , int serverSpeedCost
							 */, int rep) {
		super();
		this.k = k;
		this.temprature = temprature;
		this.counter = counter;
		this.totalCost = totalCost;
		this.serverCost = serverCost;
		this.QUnitCost = qUnitCost;
		this.rep = rep;
//		this.serverSpeedCost = serverSpeedCost;
	}

	public double[] solve1(int[][] c) throws IOException {
		int[][] con1 = null;
		int numLayers = c.length;
//		BufferedWriter writer = new BufferedWriter(new FileWriter("SA.txt"));
		int cost = 0;
		do {
			con1 = generateInitialConfiguration2(c);
			cost = isValid(con1);
//			System.out.println(cost);
		} while (cost > totalCost);

		int count = 0;
		double val = 0;
		double temp;
		int cost1 = 0;
		int cost2 = 0;
		for (temp = temprature; temp > 1 && count < counter; temp *= .9) // temperature
		{

			double val1 = 0;

			for (int rep = 0; rep <= this.rep; rep++) // repeats in each temp
			{
				count++;
				int[][] con2 = deepCopy(con1);
				con2 = changeConfig(con2);
				Executer ex1 = new Executer(1000000, 0.5f, con1);
				val1 = ex1.simualte();
				cost1 = isValid(con1);
				val = val1;

				cost2 = isValid(con2);
				Executer ex2 = new Executer(1000000, 0.5f, con2);
				double val2 = ex2.simualte();

				if (val2 < val1) {
					count = 0; // a better solution is found
					con1 = con2; // change configuration
					val = val2;
					cost1 = cost2;
				} else if ((val2 > val1) && ((Math.random() <= Math.exp((val1 - val2) / (k * temp))))) {
					count = 0; // the worst solution is accepted
					con1 = con2; // change configuration
					val = val2;
					cost1 = cost2;
				}
//				writer.write(temp + "," + val + "," + cost1 + ", ");
//				System.out.println((temp + "," + val + "," + cost1));
				for (int i = 0; i < numLayers; i++) {
//					writer.write(con1[i][1] + ", ");
					System.out.println("#SRV of layer " + i + " = " + con1[i][1]);
//					writer.write(con1[i][2] + ", ");
					System.out.println("QS of layer " + i + " = " + con1[i][2]);
				}
//				writer.write("\n");

				System.out.print(" val2: " + val2 + " ");
				System.out.println("temp: " + temp + " rep: " + rep + " val1: " + val1 + " val: " + val + " cost: "
						+ cost1 + "total-cost: " + totalCost);

				if (count == counter) // e,g., 200 config not a better solution
				{
					break;
				}
			}

		}
//		writer.write(temp + "," + val + "\n");
//		writer.close();
		double[] ans = new double[2 * numLayers + 2];
		ans[0] = val;
		ans[1] = cost;
		int j = 2;
		for (int i = 0; i < numLayers; i++) {
			ans[j] = con1[i][1];
			ans[j + 1] = con1[i][2];
			j += 2;
		}

		return ans;
	}

	private int[][] changeConfig(int[][] con2) {
		int[] params = { 1, 2/* , 4 */ };// changeable items in configuration
		int counter = 0;
		int numLayers = con2.length;
		int[][] con3;
		float p = 0.5f;
		do {
			con3 = deepCopy(con2);
			if (counter >= 50) {
				p = 0.2f;
			}
//			for (int i = 0; i < numLayers; i++) {
			int i = (int) (Math.random() * (numLayers - 0) + 0);
			int col = (int) Math.round(Math.random());
			if (i == 0)
				col = 0;
			switch (col) {
			case 0:
				con3[i][params[col]] += (Math.random() > p && con3[i][params[col]] - 1 >= 1 ? -1 : 1);
				break;
			case 1:
//			int qu = (int) (Math.random() * (10 - 1) + 1);
				int qu = 2;
				con3[i][params[col]] += (Math.random() > p && con3[i][params[col]] - qu >= 1 ? -qu : qu);
//				}
			}
			counter++;
		} while (isValid(con3) > totalCost);
		return con3;
	}

	private int isValid(int[][] c) {
		int cost = 0;
		for (int i = 0; i < c.length; i++)
			cost += serverCost * c[i][1];

		for (int i = 1; i < c.length; i++)
			cost += QUnitCost * c[i][2];

//		for (int i = 0; i < c.length; i++)
//			cost += serverSpeedCost * c[i][4];

		return cost;
	}

	private int[][] deepCopy(int[][] con1) {
		int[][] conf = new int[con1.length][con1[0].length];
		for (int i = 0; i < con1.length; i++)
			conf[i] = con1[i].clone();
		return conf;
	}

	private int[][] generateInitialConfiguration2(int[][] array) {
		int numLayers = array.length;
		for (int j = 0; j < numLayers; j++)
			array[j][1] = totalCost / (2 * serverCost * numLayers);// number of servers
//			array[j][1] = (int) (Math.random() * (totalCost / (2 * serverCost*numLayers) - 1) + 1);// number of servers

		for (int j = 1; j < numLayers; j++)
			array[j][2] = totalCost / (2 * (numLayers - 1) * QUnitCost);// QS
//			array[j][2] = (int) (Math.random() * (totalCost / (2*(numLayers - 1) * QUnitCost) - 1) + 1);// QS

		return array;
	}

}
