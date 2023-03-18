package main;

import java.util.ArrayList;

public class ProfAndCateringPreemptive {

	private int clock;
	private int ST;
	private int[] tbl = new int[4];

	private int server1;
	private int server2;

	private ArrayList<Integer> QP = new ArrayList<>();
	private ArrayList<Integer> QS = new ArrayList<>();

	private int numberOfProfsEntered;
	private int numberOfStudentsEntered;

	private int numberOfProfsServed;
	private int numberOfStudentsServed;

	private long sumOfQueueProfs;
	private long sumOfQueueStudents;
	private int prevClkStudentQueue;
	private int prevClkProfQueue;
	private int maxQueueStudent;
	private int maxQueueProf;
	private long powSumOfQueueProf;
	private long powSumOfQueueStudent;

	private int sumOfServer1Student;
	private int sumOfServer1Prof;
	private int sumOfServer2Student;
	private int sumOfServer2Prof;
	private int prevClkServer1s = 0;
	private int prevClkServer2s = 0;

	private int sumOfServer1;
	private int sumOfServer2;
	private int prevClkServer1;
	private int prevClkServer2;

	private long sumOfStudentWaitingTime;
	private long sumOfProfWaitingTime;
	private long sumOfProfWaitingTimeAdjusted = 0;
	private long sumOfStudentWaitingTimeAdjusted = 0;

	private int server1PreemptiveTime;
	private int server2PreemptiveTime;
	private int server1RemainingpreemptiveTime;
	private int server2RemainingpreemptiveTime;

	private long arrTimeStudentServer1;
	private long arrTimeStudentServer2;
	private long sumOfTurnArountTimeStudent;
	private long arrTimeProfServer1;
	private long arrTimeProfServer2;
	private long sumOfTurnAroundTimeProf;

	public ProfAndCateringPreemptive() {
		clock = 0;
		ST = 1000000;
		server1PreemptiveTime = -1;
		server2PreemptiveTime = -1;
		server1RemainingpreemptiveTime = -1;
		server2RemainingpreemptiveTime = -1;
		tbl[0] = arrP();// event prof enter
		tbl[1] = arrS();// event student enter
		tbl[2] = ST + 1;// event leave server1
		tbl[3] = ST + 1;// event leave server2
	}

	public void simulate() {
		while (true) {
			int i;
			if (clock > ST) {
				reduceFaults();
				report();
				break;
			} else {
				i = min(tbl);
				clock = tbl[i];
//					System.out.println(clock + "   -   " + i);
				switch (i) {
				case 0:
					arrivalP();
					break;
				case 1:
					arrivalS();
					break;
				case 2:
					endService1();
					break;
				case 3:
					endService2();
					break;
				}
			}
		}
	}

	private void reduceFaults() {
		if (server1 == 1) {
			sumOfServer1Student += ST - prevClkServer1s;
		}
		if (server1 == 2) {
			sumOfServer1Prof += ST - prevClkServer1s;
		}
		if (server2 == 1) {
			sumOfServer2Student += ST - prevClkServer2s;
		}
		if (server2 == 2) {
			sumOfServer2Prof += ST - prevClkServer2s;
		}

		if (server1 != 0) {
			sumOfServer1 += ST - prevClkServer1;
		}
		if (server2 != 0) {
			sumOfServer2 += ST - prevClkServer2;
		}

		sumOfProfWaitingTimeAdjusted = sumOfProfWaitingTime;
		while (QP.size() != 0) {
			sumOfProfWaitingTimeAdjusted += ST - QP.remove(0);
		}
		sumOfStudentWaitingTimeAdjusted = sumOfStudentWaitingTime;
		while (QS.size() != 0) {
			sumOfStudentWaitingTimeAdjusted += ST - QS.remove(0);
		}

		if (server1RemainingpreemptiveTime > 0) {
			sumOfStudentWaitingTimeAdjusted += ST - arrTimeStudentServer1;
		}
		if (server2RemainingpreemptiveTime > 0) {
			sumOfStudentWaitingTimeAdjusted += ST - arrTimeStudentServer2;
		}
	}

	private void arrivalP() {
		tbl[0] = clock + arrP();
		numberOfProfsEntered++;
		if (!QP.isEmpty() || (server1 == 2 && server2 == 2)) {
			sumOfQueueProfs += (clock - prevClkProfQueue) * QP.size();
			powSumOfQueueProf += (clock - prevClkProfQueue) * Math.pow(QP.size(), 2);
			prevClkProfQueue = clock;
			QP.add(clock);
			if (QP.size() > maxQueueProf) {
				maxQueueProf = QP.size();
			}
		} else {
			if (server1 == 0) {
				prevClkServer1s = clock;
				server1 = 2;
				tbl[2] = clock + srvP();
				prevClkServer1 = clock;
				arrTimeProfServer1 = clock;
			} else if (server2 == 0) {
				prevClkServer2s = clock;
				server2 = 2;
				tbl[3] = clock + srvP();
				prevClkServer2 = clock;
				arrTimeProfServer2 = clock;
			} else if (server1 == 1) {
				server1PreemptiveTime = clock;
				server1RemainingpreemptiveTime = tbl[2] - clock;
				sumOfServer1Student += clock - prevClkServer1s;
				server1 = 2;
				prevClkServer1s = clock;
				tbl[2] = clock + srvP();
				sumOfTurnArountTimeStudent += clock - arrTimeStudentServer1;
				arrTimeProfServer1 = clock;
				sumOfQueueStudents += (clock - prevClkStudentQueue) * (QS.size());
				powSumOfQueueStudent += (clock - prevClkStudentQueue) * Math.pow(QS.size(), 2);
				prevClkStudentQueue = clock;
				QS.add(0, -1);// add the preempted student
			} else if (server2 == 1) {
				server2PreemptiveTime = clock;
				server2RemainingpreemptiveTime = tbl[3] - clock;
				sumOfServer2Student += clock - prevClkServer2s;
				server2 = 2;
				prevClkServer2s = clock;
				tbl[3] = clock + srvP();
				sumOfTurnArountTimeStudent += clock - arrTimeStudentServer2;
				arrTimeProfServer2 = clock;
				sumOfQueueStudents += (clock - prevClkStudentQueue) * (QS.size());
				powSumOfQueueStudent += (clock - prevClkStudentQueue) * Math.pow(QS.size(), 2);
				prevClkStudentQueue = clock;
				QS.add(0, -1);// add the preempted student
			}
		}
	}

	private void arrivalS() {
		tbl[1] = clock + arrS();
		numberOfStudentsEntered++;
		if (server1 != 0 && server2 != 0) {
			sumOfQueueStudents += (clock - prevClkStudentQueue) * QS.size();
			powSumOfQueueStudent += (clock - prevClkStudentQueue) * Math.pow(QS.size(), 2);
			prevClkStudentQueue = clock;
			QS.add(clock);

			if (QS.size() > maxQueueStudent) {
				maxQueueStudent = QS.size();
			}
		} else if (server1 == 0) {
			prevClkServer1s = clock;
			server1 = 1;
			tbl[2] = clock + srvS();
			prevClkServer1 = clock;
			arrTimeStudentServer1 = clock;
		} else if (server2 == 0) {
			prevClkServer2s = clock;
			server2 = 1;
			tbl[3] = clock + srvS();
			prevClkServer2 = clock;
			arrTimeStudentServer2 = clock;
		}
	}

	private void endService1() {
		if (server1 == 1) {
			numberOfStudentsServed++;
			sumOfServer1Student += clock - prevClkServer1s;
			sumOfTurnArountTimeStudent += clock - arrTimeStudentServer1;
		} else if (server1 == 2) {
			numberOfProfsServed++;
			sumOfServer1Prof += clock - prevClkServer1s;
			sumOfTurnAroundTimeProf += clock - arrTimeProfServer1;
		}
		if (QP.size() > 0) {
			prevClkServer1s = clock;
			tbl[2] = clock + srvP();
			sumOfQueueProfs += (clock - prevClkProfQueue) * QP.size();
			powSumOfQueueProf += (clock - prevClkProfQueue) * Math.pow(QP.size(), 2);
			prevClkProfQueue = clock;
			server1 = 2;
			arrTimeProfServer1 = QP.remove(0);
			sumOfProfWaitingTime += clock - arrTimeProfServer1;

		} else if (server1RemainingpreemptiveTime >= 0) {
			prevClkServer1s = clock;
			arrTimeStudentServer1 = server1PreemptiveTime;
			tbl[2] = clock + server1RemainingpreemptiveTime;
			sumOfStudentWaitingTime += clock - arrTimeStudentServer1;
			server1RemainingpreemptiveTime = -1;
			server1 = 1;

			if (QS.size() > maxQueueStudent) {
				maxQueueStudent = QS.size();
			}
			sumOfQueueStudents += (clock - prevClkStudentQueue) * (QS.size());
			powSumOfQueueStudent += (clock - prevClkStudentQueue) * Math.pow(QS.size(), 2);
			prevClkStudentQueue = clock;

			QS.remove(0);
		} else if (server2RemainingpreemptiveTime >= 0) {
			prevClkServer1s = clock;
			arrTimeStudentServer1 = server2PreemptiveTime;
			tbl[2] = clock + server2RemainingpreemptiveTime;
			sumOfStudentWaitingTime += clock - arrTimeStudentServer1;
			server2RemainingpreemptiveTime = -1;

			server1 = 1;
			sumOfQueueStudents += (clock - prevClkStudentQueue) * (QS.size());
			powSumOfQueueStudent += (clock - prevClkStudentQueue) * Math.pow(QS.size(), 2);
			prevClkStudentQueue = clock;

			QS.remove(0);
		} else if (QS.size() > 0) {
			tbl[2] = clock + srvS();
			prevClkServer1s = clock;
			sumOfQueueStudents += (clock - prevClkStudentQueue) * QS.size();
			powSumOfQueueStudent += (clock - prevClkStudentQueue) * Math.pow(QS.size(), 2);
			prevClkStudentQueue = clock;
			server1 = 1;
			arrTimeStudentServer1 = QS.remove(0);
			sumOfStudentWaitingTime += clock - arrTimeStudentServer1;
		} else {
			server1 = 0;
			tbl[2] = ST + 1;
		}
	}

	private void endService2() {
		if (server2 == 1) {
			numberOfStudentsServed++;
			sumOfServer2Student += clock - prevClkServer2s;
			sumOfTurnArountTimeStudent += clock - arrTimeStudentServer2;
		} else if (server2 == 2) {
			numberOfProfsServed++;
			sumOfServer2Prof += clock - prevClkServer2s;
			sumOfTurnAroundTimeProf += clock - arrTimeProfServer2;
		}
		if (QP.size() > 0) {
			prevClkServer2s = clock;
			tbl[3] = clock + srvP();
			sumOfQueueProfs += (clock - prevClkProfQueue) * QP.size();
			powSumOfQueueProf += (clock - prevClkProfQueue) * Math.pow(QP.size(), 2);
			prevClkProfQueue = clock;
			server2 = 2;
			arrTimeProfServer2 = QP.remove(0);
			sumOfProfWaitingTime += clock - arrTimeProfServer2;
		} else if (server1RemainingpreemptiveTime >= 0) {
			prevClkServer2s = clock;
			arrTimeStudentServer2 = server1PreemptiveTime;
			tbl[3] = clock + server1RemainingpreemptiveTime;
			sumOfStudentWaitingTime += clock - arrTimeStudentServer2;
			server1RemainingpreemptiveTime = -1;

			server2 = 1;
			sumOfQueueStudents += (clock - prevClkStudentQueue) * (QS.size());
			powSumOfQueueStudent += (clock - prevClkStudentQueue) * Math.pow(QS.size(), 2);
			prevClkStudentQueue = clock;
			QS.remove(0);
		} else if (server2RemainingpreemptiveTime >= 0) {
			prevClkServer2s = clock;
			arrTimeStudentServer2 = server2PreemptiveTime;
			tbl[3] = clock + server2RemainingpreemptiveTime;
			sumOfStudentWaitingTime += clock - arrTimeStudentServer2;
			server2RemainingpreemptiveTime = -1;

			server2 = 1;
			sumOfQueueStudents += (clock - prevClkStudentQueue) * (QS.size());
			powSumOfQueueStudent += (clock - prevClkStudentQueue) * Math.pow(QS.size(), 2);
			prevClkStudentQueue = clock;
			QS.remove(0);
		} else if (QS.size() > 0) {
			prevClkServer2s = clock;
			tbl[3] = clock + srvS();
			sumOfQueueStudents += (clock - prevClkStudentQueue) * QS.size();
			powSumOfQueueStudent += (clock - prevClkStudentQueue) * Math.pow(QS.size(), 2);
			prevClkStudentQueue = clock;
			server2 = 1;
			arrTimeStudentServer2 = QS.remove(0);
			sumOfStudentWaitingTime += clock - arrTimeStudentServer2;
		} else {
			server2 = 0;
			tbl[3] = ST + 1;
		}
	}

	private void report() {
		int total = numberOfProfsEntered + numberOfStudentsEntered;
		int totalServed = numberOfProfsServed + numberOfStudentsServed;
		long studentAvgQueue = sumOfQueueStudents / ST;
		long profAvgQueue = sumOfQueueProfs / ST;
		float studentVariance = (float) Math
				.sqrt((double) powSumOfQueueStudent / ST - Math.pow((double) sumOfQueueStudents / ST, 2));
		float profVariance = (float) Math
				.sqrt((double) powSumOfQueueProf / ST - Math.pow((double) sumOfQueueProfs / ST, 2));
		System.out.println("--------Entered--------");
		System.out.println("total students entered: " + numberOfStudentsEntered + "\t\t" + "total profs entered: "
				+ numberOfProfsEntered);

		System.out.println("--------Served--------");
		System.out.println("Served Profs: " + numberOfProfsServed + "\t\t" + "Served Profs percentage: "
				+ (float) numberOfProfsServed / numberOfProfsEntered * 100);
		System.out.println("Served Students: " + numberOfStudentsServed + "\t\t" + "Served Students percentage: "
				+ (float) numberOfStudentsServed / numberOfStudentsEntered * 100);
		System.out.println("Served total: " + totalServed + "\t\t" + "Served total percentage: "
				+ (float) (totalServed) / total * 100);

		System.out.println("--------Throughput--------");
		System.out.println("Profs Throughput: " + (float) numberOfProfsServed / ST);
		System.out.println("Students Throughput: " + (float) numberOfStudentsServed / ST);
		System.out.println("total Throughput: " + (float) (numberOfProfsServed + numberOfStudentsServed) / ST);

		System.out.println("--------AvgQueueLength--------");
		System.out.println("Average Student Queue length: " + (float) studentAvgQueue);
		System.out.println("max Student Queue length: " + (float) maxQueueStudent);
		System.out.println("variance Student Queue length: " + studentVariance);
		System.out.println("Average Prof Queue length: " + (float) profAvgQueue);
		System.out.println("max Prof Queue length: " + (float) maxQueueProf);
		System.out.println("variance Prof Queue length: " + profVariance);

		System.out.println("--------ServerUtilization--------");
		System.out.println("server1 Prof utilization percentage: " + (float) sumOfServer1Prof / ST
				+ "\t\tserver1 Student utilization percentage: " + (float) sumOfServer1Student / ST);
		System.out.println("server2 Prof utilization percentage: " + (float) sumOfServer2Prof / ST
				+ "\t\tserver2 Student utilization percentage: " + (float) sumOfServer2Student / ST);
		System.out.println("server1 utilization percentage: " + (float) sumOfServer1 / ST
				+ "\t\tserver2 utilization percentage: " + (float) sumOfServer2 / ST);

		System.out.println("--------WaitingTime--------");
		System.out.println("Prof WaitingTime: " + (float) sumOfProfWaitingTime / numberOfProfsServed
				+ "\t\tStudent WatingTime: " + (float) sumOfStudentWaitingTime / numberOfStudentsServed);
		System.out.println("Prof WaitingTimeAdjusted: " + (float) sumOfProfWaitingTimeAdjusted / numberOfProfsEntered
				+ "\t\tStudent WatingTimeAdjusted: "
				+ (float) sumOfStudentWaitingTimeAdjusted / numberOfStudentsEntered);

		System.out.println("--------TurnAroundTime--------");
		System.out.println("Profs TurnAroundTime: " + (float) sumOfTurnAroundTimeProf / numberOfProfsServed);
		System.out.println("Students TurnAroundTime: " + (float) sumOfTurnArountTimeStudent / numberOfStudentsServed);
	}

	private int min(int[] array) {
		int min = array[0];
		int index = 0;
		for (int i = 1; i < array.length; i++) {
			if (array[i] < min) {
				min = array[i];
				index = i;
			}
		}
		return index;
	}

	private int arrP() {
		return 5 + (int) (Math.random() * 6);// 5 to 10 -- case1
//		return 5 + (int) (Math.random() * 3);// 5 to 7 -- case2
//		return 1 + (int) (Math.random() * 3);// 1 to 3 -- case3
//		return 6 + (int) (Math.random() * 3);// 6 to 8 -- case4
	}

	private int arrS() {
		return 5 + (int) (Math.random() * 6);// 5 to 10 -- case1
//		return 2 + (int) (Math.random() * 3);// 2 to 4 -- case2
//		return 4 + (int) (Math.random() * 3);// 4 to 6 -- case3
//		return 1 + (int) (Math.random() * 3);// 1 to 3 -- case4
	}

	private int srvP() {
		return 10 + (int) (Math.random() * 3);// 10 to 12 -- case1
//		return 4 + (int) (Math.random() * 9);// 4 to 12 -- case2
//		return 1 + (int) (Math.random() * 3);// 1 to 3 -- case3
//		return 7 + (int) (Math.random() * 4);// 7 to 10 -- case4
	}

	private int srvS() {
		return 5 + (int) (Math.random() * 6);// 5 to 10 -- case1
//		return 4 + (int) (Math.random() * 3);// 4 to 6 -- case2
//		return 7 + (int) (Math.random() * 3);// 7 to 9 -- case3
//		return 1 + (int) (Math.random() * 3);// 1 to 3 -- case4
	}

}
