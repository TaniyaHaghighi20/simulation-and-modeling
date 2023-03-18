/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
//------------------------------------------------------------------------------------
//		int[][] c = { { 1, 1, 0, 2, 2 }, { 1, 1, /**/ 2, 5, 5 }, { 1, 3, 2, 4, 20 }, { 1, 1, /**/ 1, 3, 3 },
//				{ 1, 4, 2, 3, 25 }, { 1, 1, 2, 2, 2 }, { 1, 1, 4, 5, 8 }, { 1, 3, /**/ 4, 4, 20 } };
////
//////		int[][] c = { { 1, 2, 2, 1, 5 }, { 1, 12, 1000000, 10, 200 }, { 1, 2, 10, 1, 5 } };
//		Executer ex = new Executer(1000000, 0.5f, c);
//		ex.simualte();

//------------------------------------------------------------------------------------
//		Coordinator coordinator = new Coordinator();
//		try {
//			coordinator.experiment(1f, 8);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//------------------------------------------------------------------------------------
//		SA sa = new SA(1, 5000, 50, 1000, 15, 2, 100);
//		System.out.println(sa.solve1(3));
//------------------------------------------------------------------------------------
		SACoordinator saCoordinator = new SACoordinator();
		saCoordinator.experiment();
	}

}
