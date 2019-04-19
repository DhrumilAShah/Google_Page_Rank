import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;

public class pgrk1432 {
	static int edges;
	static int vertices;
	static ArrayList<LinkedList<Integer>> adjList;
	static FileReader1432 fr;
	static int initialValue;
	static int iterations;
	static String fileName;
	static final double dampingFactor = 0.85;
	static double[] pgRnk; 
	static double[] prevPgRnk;
	static int[] outDegree;
	static double val;

	public static void main(String[] args) throws IOException {
		try {

			if (args.length < 3) {
				System.out.println("Please enter valid number of arguments!");
				return;
			}

			iterations = Integer.parseInt(args[0]);
			initialValue = Integer.parseInt(args[1]);
			fileName = args[2];

			if(fileName.trim().length()==0 || initialValue > 1 || initialValue<-2 ) {
				System.out.println("Please enter valid arguments!");
				return;
			}

			fr = new FileReader1432(fileName);

			vertices = fr.getVerticeSize();
			edges = fr.getEdgeSize();

			DecimalFormat numFmt = new DecimalFormat("0.0000000");

			if (vertices > 10) {
				iterations = 0;
				initialValue = -1;
			}		

			initialize();

			calculateOutDegree();

			System.out.print("Base : 0 :");
			for(int i=0; i<pgRnk.length; i++) 
				System.out.print("P["+i+"]="+numFmt.format(pgRnk[i])+" ");
			System.out.println();

			computePageRank();	

			int i=1;
			while(iterations <= 0 ? true : i!=(iterations+1) ) {	

				if(vertices < 11) {
					System.out.print("Iter : "+i+" :");
					for(int k=0; k<pgRnk.length; k++) 
						System.out.print("P["+k+"]="+numFmt.format(pgRnk[k])+" ");
					System.out.println();
				}

				computePageRank();

				if((vertices>10 || iterations <0) && 
						didItConverge((iterations <= 0) ? iterations : i)) break;
				
				i++;
			}
			if(vertices>10) {
				System.out.println("Iter : "+(++i)+" :");
				for(int k=0; k<pgRnk.length; k++) 
					System.out.println("P["+k+"]="+numFmt.format(pgRnk[k])+" ");
			}
			fr.close();
		}catch(Exception e) {
			System.err.println(e.getMessage());
		}
	}

	private static boolean didItConverge(int iter) {
		double errRate = (iter == 0) ? 100000 : Math.pow(10, (iter * -1));
		for(int i=0; i<pgRnk.length; i++) {
			if((int)Math.floor(prevPgRnk[i] * errRate) != (int)Math.floor(pgRnk[i] * errRate)) return false;
		}
		return true;
	}

	private static void calculateOutDegree() {
		try {
			outDegree = new int[pgRnk.length];
			for(int i=0;i<vertices; i++) {
				outDegree[i]=0;
				if(adjList.size() > i && adjList.get(i)!=null) {
					outDegree[i] = adjList.get(i).size();
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	private static void computePageRank() {
		try {
			double[] updatedPgRnk = new double[pgRnk.length];
			for(int i=0; i<pgRnk.length; i++) {
				double pgVal = val;
				for(int j=0; j<adjList.size(); j++) {
					if(adjList.get(j)!=null && adjList.get(j).contains(i)) {
						if(outDegree[j] != 0)
							pgVal += dampingFactor * pgRnk[j]/outDegree[j];
					}
				}
				updatedPgRnk[i]=pgVal;
			}
			prevPgRnk = pgRnk;
			pgRnk = updatedPgRnk;
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	private static void initialize() {
		try {
			adjList = new ArrayList<LinkedList<Integer>>();
			double initVal = (double)initialValue;
			val = (1.0 - dampingFactor) / vertices;
			pgRnk = new double[vertices];
			int index;

			for(int h=0; h<vertices; h++) 
				adjList.add(h,null);
			int edgeCounter=0;
			while((index = fr.getNextValue()) != -1) {
				int outBound = fr.getNextValue();
				if(outBound != -1) {
					if(adjList.get(index) == null) {
						LinkedList<Integer> ll = new LinkedList<>();
						ll.add(outBound);
						adjList.add(index,ll);	
					} else {
						LinkedList<Integer> ll = adjList.get(index);
						ll.add(outBound);			
						adjList.set(index,ll);
					}
					edgeCounter++;
				} else break;
			}

			if(initialValue == -1) {
				initVal = 1/(double)vertices;
			}else if(initialValue == -2) {
				initVal = 1/(double)Math.sqrt(vertices);
			}

			for(int i=0; i<vertices; i++) {
				pgRnk[i]=initVal;
			}

			if(edgeCounter != edges) {
				throw new Exception("Improper data format!");
			}

		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	private static void printList(ArrayList<LinkedList<Integer>> adjList) {
		for(LinkedList<Integer> ll : adjList) {
			for(int d : ll)	System.out.print(d);
			System.out.println();
		}
	}
}
