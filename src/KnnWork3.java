import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;





public class KnnWork3 {

	
	
	public double bestResult =0.70;
	static  ArrayList<String> allComination = new ArrayList<String>();
	 private StringBuilder output = new StringBuilder();
	 public String best = "" ;
	private String inputstring;
	static  int k=5;
	static int allNumber =0;
			// # of neighbours 
			// the data
	static  ArrayList<int[]> instances=new ArrayList<int[]>();
	static  ArrayList<int[]> instancesFeatureSelection=new ArrayList<int[]>();
	public void readFile() {
		// TODO Auto-generated method stub
		FileReader file=null ;
				try {
					file=new FileReader(new File("Exactly2.dat"));
				} catch (FileNotFoundException e1) {
					System.out.println("File dichromatic.dat not found!");
					e1.printStackTrace(); 
				}
				
				 BufferedReader br=new BufferedReader(file);
				 String line; 
				try {
					while ((line=br.readLine()) != null) {
						 String[] splitStr=line.trim().split(",");
						 int[] naser=new int[splitStr.length]; 
						for (int i=0; i < splitStr.length; i++) {
							 naser[i]=Integer.parseInt(splitStr[i]); // System.out.print(splitStr[i]);
							
						}
						instances.add(naser); //System.out.println("");
						// we need some work here
						
					}
				} catch (IOException e) {
					System.err.println("Error when reading"); 
					e.printStackTrace(); 
				} finally {
					if (br != null) {
						try {
							br.close(); 
						} catch (IOException e) {
							System.out.println("Unexpected error"); 
							e.printStackTrace(); 
						}
						
					}
				}
				
				for(int[] n : instances){
					for(int i=0;i<n.length;i++){
						System.out.print(n[i]+" ");
					}
					System.out.println("");
					allNumber++;
				}
				
				System.out.println(allNumber+"");
	}
	
	public void shuffle(ArrayList<int[]> nn) {
		for (int i=0; i < nn.size(); i++) {
			if (Math.random() > 0.3) {
			    int where=(int) ((Math.random() * nn.size())); 
				if (where > 3) where--;
			   int[] mm=nn.get(i);
				nn.set(i, nn.get(where));
				nn.set(where, mm); 
			} else {
			   int where=(int) ((Math.random() * nn.size()));
				if (where > 1 && where < nn.size() - 3) where=where + 3; 
			   int[] mm=nn.get(i);
				nn.set(i, nn.get(where));
				nn.set(where, mm);
			}
		}
	}
	
	private static String findMajorityClass(String[] array)
	{
		//add the String array to a HashSet to get unique String values
		Set<String> h = new HashSet<String>(Arrays.asList(array));
		//convert the HashSet back to array
		String[] uniqueValues = h.toArray(new String[0]);
		//counts for unique strings
		int[] counts = new int[uniqueValues.length];
		// loop thru unique strings and count how many times they appear in origianl array   
		for (int i = 0; i < uniqueValues.length; i++) {
			for (int j = 0; j < array.length; j++) {
				if(array[j].equals(uniqueValues[i])){
					counts[i]++;
				}
			}        
		}

		for (int i = 0; i < uniqueValues.length; i++)
			System.out.println(uniqueValues[i]);
		for (int i = 0; i < counts.length; i++)
			System.out.println(counts[i]);


		int max = counts[0];
		for (int counter = 1; counter < counts.length; counter++) {
			if (counts[counter] > max) {
				max = counts[counter];
			}
		}
		System.out.println("max # of occurences: "+max);

		// how many times max appears
		//we know that max will appear at least once in counts
		//so the value of freq will be 1 at minimum after this loop
		int freq = 0;
		for (int counter = 0; counter < counts.length; counter++) {
			if (counts[counter] == max) {
				freq++;
			}
		}

		//index of most freq value if we have only one mode
		int index = -1;
		if(freq==1){
			for (int counter = 0; counter < counts.length; counter++) {
				if (counts[counter] == max) {
					index = counter;
					break;
				}
			}
			//System.out.println("one majority class, index is: "+index);
			return uniqueValues[index];
		} else{//we have multiple modes
			int[] ix = new int[freq];//array of indices of modes
			System.out.println("multiple majority classes: "+freq+" classes");
			int ixi = 0;
			for (int counter = 0; counter < counts.length; counter++) {
				if (counts[counter] == max) {
					ix[ixi] = counter;//save index of each max count value
					ixi++; // increase index of ix array
				}
			}

			for (int counter = 0; counter < ix.length; counter++)         
				System.out.println("class index: "+ix[counter]);       

			//now choose one at random
			Random generator = new Random();        
			//get random number 0 <= rIndex < size of ix
			int rIndex = generator.nextInt(ix.length);
			System.out.println("random index: "+rIndex);
			int nIndex = ix[rIndex];
			//return unique value at that index 
			return uniqueValues[nIndex];
		}

	}

	//simple class to model instances (features + class)
	static class Feature {	
		int[] fetureAttributes;
		int attributeName;
		public Feature(int[] fetureAttributes, int attributeName){
			this.attributeName=attributeName;
			this.fetureAttributes=fetureAttributes;    	    
		}
	}
	//simple class to model results (distance + class)
	static class Result {	
		 int distance;
		 int attributeName;
		public Result(int distance, int attributeName){
			this.attributeName=attributeName;
					this.distance=distance;	    	    
		}
	}
	//simple comparator class used to compare results via distances
	static class DistanceComparator implements Comparator<Result> {
		@Override
		public int compare(Result a, Result b) {
			return a.distance < b.distance ? -1 : a.distance == b.distance ? 0 : 1;
		}
	}

	 private String FindtheClassesForTraining(List<Feature> dataList, int[] query) {
			// TODO Auto-generated method stub
			 List<Result> resultList=new ArrayList<Result>();
			//find disnaces
			for (Feature f : dataList) {
			   int dist=0;
				for (int j=0; j < f.fetureAttributes.length - 1; j++) {
					 	     
					dist += Math.pow(f.fetureAttributes[j] - query[j], 2) ;
				}
			 int distance=(int) (Math.sqrt(dist)); 
				resultList.add(new Result(distance,f.attributeName)); //System.out.println(distance);
				
			}
			//System.out.println(resultList);
			Collections.sort(resultList, new DistanceComparator());
		    String[] ss=new String[k]; 
			for (int x=0; x < k; x++) {
				System.out.println(resultList.get(x).attributeName+ " .... " + resultList.get(x).distance);
				//get classes of k nearest instances names from the list into an array
				ss[x] = ""+resultList.get(x).attributeName;
			}
		    String majClass=findMajorityClass(ss);
		
			System.out.println("Class of new instance is: "+majClass);   
			
			
			return majClass;
	 
		}
	public void DoKnn() {
		double allaccuracy=0;
		for(int naser =0 ;naser <30 ; naser++){
		shuffle(instances); 
		//list to save features data
		 List<Feature> dataList=new ArrayList<Feature>(); 
		//list to save distances result
	   int limit=(int) (((instances.size()) * 0.80)); 
		int test=instances.size() - limit; 
		// 0.20
		// add feature data to dataList   
		int lastitem=instances.get(0).length - 1; 
		for (int i=0; i < limit; i++) {
			dataList.add(new Feature(instances.get(i),instances.get(i)[lastitem])); 
		}
		 int point=0; 
		
		
		for (int n=limit; n < instances.size(); n++) {
		     String nn=FindtheClassesForTraining(dataList, instances.get(n));
		     int p=Integer.parseInt(nn);
		     int d=instances.get(n)[lastitem]; 
			if (p == d) {
				point++; 
			}
		}
		
		 double accuracy= (double)point / test;
		System.out.println("final   " + accuracy); 
		allaccuracy = allaccuracy+accuracy;
	}
		System.out.println("After 30 iteration    " + allaccuracy/30); 
	}


	public void combine() { 
		combine( 0 ); 
		}
    private void combine(int start ){
        for( int i = start; i < inputstring.length(); ++i ){
            output.append( inputstring.charAt(i) );
          // System.out.println( output );
            String[] n = output.toString().split("\n");
            for(String m :n){
            	String[] ff = m.split("(?!^)");
            	String uu = "";
            	for(int y=0;y<ff.length;y++){
            		if(y == ff.length-1){
            			uu = uu + ff[y];
            		}else{
            			uu = uu + ff[y]+",";
            		}
            		
            	}
            	 System.out.println(uu);
            	 allComination.add(uu);
            }
           
            if ( i < inputstring.length() )
            combine( i + 1);
            output.setLength( output.length() - 1 );
        }
    }
	
	
	public void DoFeatureSelection() {
		/*
	     for(int s =0;s<instances.get(0).length;s++){
	    	 if(s == 0){
	    		 inputstring = s +"";//+",";
	    	 }else if(s == instances.get(0).length -1){
	    		 //nothing
	    	 }
	    	 else{
	    		 inputstring =inputstring+ s +"";
	    	 }
	    	
			}
	     */
	     //to get all possible combination
		inputstring ="abcdefghijklm";
	       combine();

	       String check =allComination.get(0).toString();
			
	     	best = check;
	     
			for(int i =0;i<allComination.size();i++){
				
				 instancesFeatureSelection.clear();
				check = allComination.get(i).toString();
				String goN = allComination.get(i) +",n";
				String[] naser = goN.split(",");
				int size = naser.length;
				
				for (int n=0; n < allNumber; n++) {
					int[] query = new int[size]; 	
				for (int j=0; j < size; j++) {
					int w =getEqualsChar(naser[j]);
					query[j]=instances.get(n)[w]; // System.out.print(splitStr[i]);
					
				}
				
				
				instancesFeatureSelection.add(query); //System.out.println("");
				}
				
				
				double result = 0;
				for(int b =0 ;b < 1 ; b++){
				shuffle(instancesFeatureSelection); 
				//list to save features data
				 List<Feature> dataList=new ArrayList<Feature>(); 
				//list to save distances result
			   int limit=(int) (((instancesFeatureSelection.size()) * 0.80)); 
				double test= (double)instancesFeatureSelection.size() - limit; 
				// 0.20
				// add feature data to dataList   
				int lastitem=instancesFeatureSelection.get(0).length - 1; 
				for (int f=0; f < limit; f++) {
					dataList.add(new Feature(instancesFeatureSelection.get(f),instancesFeatureSelection.get(f)[lastitem])); 
				}
				 int point=0; 
				
				for (int d=limit; d < instancesFeatureSelection.size(); d++) {
				     String nn=FindtheClassesForTraining(dataList, instancesFeatureSelection.get(d));
				     int p=Integer.parseInt(nn);
				     int rr=instancesFeatureSelection.get(d)[lastitem]; 
					if (p == rr) {
						point++; 
					}
				}
				
				double accuracy= (double)point / test;
				System.out.println("final   " + accuracy); 
			
				result=result+accuracy;
				
			}
			result=result/1;
			if(result > bestResult){
				best=check;
				bestResult=result;
				System.out.println("Featrures  naser");
			}
			}
			
				
			System.out.println("Fearures is " + best + " with accuracy " + bestResult);
		}
	
	public int getEqualsChar(String nn){
		if(nn.equals("a"))
			return 0;
		else if(nn.equals("b"))
			return 1;
		else if(nn.equals("c"))
			return 2;
		else if(nn.equals("d"))
			return 3;
		else if(nn.equals("e"))
			return 4;
		else if(nn.equals("f"))
			return 5;
		else if(nn.equals("g"))
			return 6;
		else if(nn.equals("h"))
			return 7;
		else if(nn.equals("i"))
			return 8;
		else if(nn.equals("j"))
			return 9;
		else if(nn.equals("k"))
			return 10;
		else if(nn.equals("l"))
			return 11;
		else if(nn.equals("m"))
			return 12;
		else if(nn.equals("n"))
			return 13;
		
		return 0;
	}

	
}
