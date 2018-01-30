

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		//breast cancer
		KnnWork nn =new KnnWork();
		nn.readFile();
		nn.DoKnn();
		nn.DoFeatureSelection();
		
	
		/*
		//M-of-n.dat
		KnnWork2 nn1 =new KnnWork2();
		nn1.readFile();
		nn1.DoKnn();
		nn1.DoFeatureSelection();
		
		
		
		//Exactly2
		KnnWork3 nn3 =new KnnWork3();
		nn3.readFile();
		//nn3.DoKnn();
		nn3.DoFeatureSelection();
		*/
	}

}
