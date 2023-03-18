/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator1;


public class Entity {
	int type;
	int arrivalTime;
	int serviceTime;
	int preEmptedTime = -1;
	int pClock;

	public Entity(int t, int a, int s) {
		type = t;
		arrivalTime = a;
		serviceTime = s;
	}

	public int difTime() {
//		if (Executer.round == 0) {
//		return Executer.totalClock - (arrivalTime);
//		}
//		return Executer.totalClock - (arrivalTime + ((Executer.round - 1) * Executer.simulationTime));
		return Executer.clock - (arrivalTime);
	}

	public int difTimeWT() {
		return Executer.clock - pClock;
	}
}
