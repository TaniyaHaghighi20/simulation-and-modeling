package bihe;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A class for generating data for drawing first 24 curves
 * 
 * 
 */
public class IntegralCoordinator_Part_II {
	/**
	 * the maximum degree
	 */
	static final int MAX_DEGREE = 50;
	/**
	 * total number of configurations in each degree
	 */
	static final int MAX_CONFIG = 1000;
	/**
	 * total number of repeats in each degree and configuration
	 */
	static final int MAX_REPEAT = 1;
	/**
	 * the parameter "n" in Monte-Carlo approach
	 */
	static final int n = 100;
	/**
	 * the parameter "h" in Simpson approach
	 */
	static final double h = 0.1;
	/**
	 * the beginning of interval on which the area is estimated
	 */
	static final int begin = 0;
	/**
	 * the end of interval on which the area is estimated
	 */
	static final int end = 50;
	/**
	 * a boolean determining whether the appreach is Simpson or not (i.e.
	 * Monte-Carlo)
	 */
	static final boolean isSimpson = true;

	public static void main(String[] args) throws IOException {
//		System.out.println("degree,\ttime,");
		BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
		double time, tempTime;
		double error = 0;

		for (int degree = 5; degree <= MAX_DEGREE; degree += 45) {
			ArrayList<Polynomial> polynomials = generatePolynomial(MAX_CONFIG, degree);

			for (double h = 0.001; h <= 1; h += 0.001) {
				error = 0;
				time = 0;
				for (Polynomial poly : polynomials) {
					double trueArea = poly.computeArea_DefiniteIntegral(begin, end);
					tempTime = System.nanoTime();
					double estimatedArea = poly.computeArea_Simpson(begin, end, h);
					tempTime = System.nanoTime() - tempTime;
					error += Math.abs(trueArea - estimatedArea) / trueArea;
					time += tempTime;
				}
				error /= MAX_CONFIG * MAX_REPEAT;
				time /= MAX_CONFIG * MAX_REPEAT;
				writer.write(degree + "," + begin + "," + end + "," + h + "," + error + "," + time);
				writer.write("\n");

			}
			System.out.println("degree: " + degree + " end: " + end);
			for (int n = 100; n <= 100000; n += 100) {
				error = 0;
				time = 0;
				for (Polynomial poly : polynomials) {
					double trueArea = poly.computeArea_DefiniteIntegral(begin, end);
					tempTime = System.nanoTime();
					double estimatedArea = poly.computeArea_MonteCarlo(begin, end, n);
					tempTime = System.nanoTime() - tempTime;
					error += Math.abs(trueArea - estimatedArea) / trueArea;
					time += tempTime;
				}
				error /= MAX_CONFIG * MAX_REPEAT;
				time /= MAX_CONFIG * MAX_REPEAT;
				writer.write(degree + "," + begin + "," + end + "," + n + "," + error + "," + time);
				writer.write("\n");
			}

		}
		writer.close();
	}

	/**
	 * a function which generate a random polynomial with given degree
	 * 
	 * @param degree the degree of random polynomial
	 * @return a random polynomial
	 */
	public static Polynomial generateRandomPolynomial(int degree) {
		Polynomial result = new Polynomial();
		for (int i = degree; i >= 0; i--) {
			result.addExpresssion(new Expression(10 * Math.random(), i));
		}
		return result;
	}

	/**
	 * increase the degree of polynomial based on given old degree
	 * 
	 * @param oldDegree the old degree
	 * @return the new degree
	 */
	public static int increaseDegree(int oldDegree) {
		int newDegree = oldDegree * 11 / 10;
		return newDegree > oldDegree ? newDegree : oldDegree + 1;
	}

	public static ArrayList<Polynomial> generatePolynomial(int number, int degree) {
		ArrayList<Polynomial> ps = new ArrayList<>();
		for (int i = 0; i < number; i++) {
			ps.add(generateRandomPolynomial(degree));
		}
		return ps;
	}
}
