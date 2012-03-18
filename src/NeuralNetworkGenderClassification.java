/*
* NeuralNetworkGenderClassification.java
* Visit the internets for documentation, updates and examples.
* https://github.com/pwhisenhunt/
* Note: This code was written years ago during my undergraduate studies. Therefore,
* it is rather ugly and has no guarantees.
*
* Author: Phillip J. Whisenhunt phillip.whisenhunt@gmail.com http://phillipwhisenhunt.com
* 
* Permission is hereby granted, free of charge, to any person
* obtaining a copy of this software and associated documentation
* files (the "Software"), to deal in the Software without
* restriction, including without limitation the rights to use,
* copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the
* Software is furnished to do so, subject to the following
* conditions:
* 
* The above copyright notice and this permission notice shall be
* included in all copies or substantial portions of the Software.
* 
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
* EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
* OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
* NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
* HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
* WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
* FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
* OTHER DEALINGS IN THE SOFTWARE.
*/
import java.io.File;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

/** This class is a neural network (NN) for facial gender classification. The NN 
learns based on b parameters generated from Active Appearance Models. The NN
can learn and classify based on texture, shape, or both. 
*/
public class NeuralNetworkGenderClassification
{
	public static int INPUT_NEURONS   = 344;					//number of input neurons
	public static int HIDDEN_NEURONS  = 5;					//number of hidden neurons
	public static int HIDDEN_BIAS     = 5;						
	public static int OUTPUT_NEURONS  = 1;					//number of output neurons
	public static int OUTPUT_BIAS     = 1;
	public double inputs[]            = new double[INPUT_NEURONS+1];	//input neurons array
	public double hidden[]            = new double[HIDDEN_NEURONS+1];	//hidden neurons array
	public double hbias[]             = new double[HIDDEN_BIAS+1];
	public double obias[]             = new double[OUTPUT_BIAS];
	public double outputs[]           = new double[OUTPUT_NEURONS];	//output neurons array
	public double w_h_i[][]           = new double[HIDDEN_NEURONS][INPUT_NEURONS+1];	//weights from the input neurons to the hidden neurons
	public double w_o_h[][]           = new double[OUTPUT_NEURONS][HIDDEN_NEURONS+1];	//weights from the hidden neurons to the output neurons
	public double learningRate        = 0.25; //the learning factor
	public double[] trainingSet       = new double[345];	//training set, the last position is the expected output
	double err_out;															//error of the output
	public double networkErrorAllowed = 1.4;								//amount of network error allowed
	public LinkedList trainingSetList = new LinkedList();					//linkedlist to store the training set
	public LinkedList testingSetList  = new LinkedList();					//linkedlist to store the testing set
	public String subjectType 				= "tex";	// the type of subject

	
	/** Default constructor.
	* @param {string} directoryPath The directory path to files.
	* @param {string} subjectType The subect type; either "tex", "shape", or "app"
	*/
	public NeuralNetworkGenderClassification(String directoryPath, String subjectType)
	{
		System.out.println("Facial Gender Classification Neural Network.");		
		this.subjectType = subjectType;
		setData(directoryPath);
		train();
		test();
	}

	/** Train the neurons of the NN.
	* @return {void} none
	*/
	public void train()
	{
		if(this.subjectType.contains("tex"))
			System.out.println("----->Training the network on texture data.");
		else if(this.subjectType.contains("tex"))
			System.out.println("----->Training the network on shape data.");
		else
			System.out.println("----->Training the network on APP data.");

		initializeRandomWeights();					//initialize random weights
		int test = (int)Math.random()*240;	//the number of which test to train
		int stop = test;
		double tsse = 0;										//initialize the tsse to 0
		int j = 0;
		while(true)
		{	
			//set the training vector to the current one being trained
			Subject temp = ((Subject)trainingSetList.get(test));
			for(int z = 0; z < temp.getFeatureVector().length; z++)
				trainingSet[z] = temp.getFeatureVector()[z];
			trainingSet[344] = temp.getGender();
			
			applyInputs();										//apple the inputs to the network
			feedForward();										//feed the input forward
			backPropagate();									//back propagate the network
			tsse += ((trainingSet[344] - outputs[0])*(trainingSet[344] - outputs[0]));	//add network error
			test++;														//increment to the next training pattern
			
			if(test == 240)										//if we are at the end of the testing subjects
				test = 0;
							
			if(test == stop)									// if we have looped through all of the subjects
			{
				j++;
				if(j > 500)			//if the nn is not learning then reset the weights
				{
					System.out.println("------>The network has not learned yet. This may be due to poor initial weights. Reseting the network for training.");
					j = 0;
					initializeRandomWeights();
				}
				test = (int)Math.random()*240;
				stop = test;
				tsse = tsse*0.5;									// find the tsse
				if(tsse < networkErrorAllowed)		//check the network error and break if we arae below it
					break;
				tsse = 0;
			}
		}		
	}

	/** Test the NN.
	* @return {void} none
	*/	
	public void test(){
		System.out.println("----->Testing the network.");
		int total = 0;
		int wrongMale = 0;
		int wrongFemale = 0;
		String expectedGender = "";
		String predictedGender = "";			
		
		for(int i = 0; i < testingSetList.size(); i++)
		{
			//set the test data to test against
			Subject temp = ((Subject)testingSetList.get(i));
			for(int z = 0; z < temp.getFeatureVector().length; z++)
				trainingSet[z] = temp.getFeatureVector()[z];
			trainingSet[344] = temp.getGender();					//set the test data gender
			
			applyInputs();						//apply the inputs
			feedForward();						//feed the inputs in to see the outputs

			// was the prediction correct?
			if(temp.getGender() == 0)
				expectedGender = "male";
			else
				expectedGender = "female";
			if(outputs[0] < 0.5)
				predictedGender = "male";
			else if(outputs[0] > 0.5)
				predictedGender = "female";
			else
				predictedGender = "none";

			if(expectedGender == predictedGender)
				total++;
			else
			{
				if(temp.getGender()==0)
					wrongMale++;
				else
					wrongFemale++;
			}
		}
		System.out.println("----->----->" + total + " / "+ testingSetList.size() + " or " + ((float)total / testingSetList.size()) * 100 + "% accuracy.");
		System.out.println("----->----->Number of males wrong:   " + wrongMale);
		System.out.println("----->----->Number of females wrong: " + wrongFemale);
	}
	
	/** Backpropagate the network. This method propagates error back through the network
	* and adjusts the weights accordingly.
	* @return {void} none
	*/
	public void backPropagate()
	{
		double err_hid[] = new double[HIDDEN_NEURONS+1];

		//calc error for outputs
		for(int out = 0; out<OUTPUT_NEURONS; out++)
			err_out = (trainingSet[344] - outputs[out]) * ((outputs[out]) * (1 - outputs[out]));
		
		//calc error for hidden nodes
		for(int hid = 0; hid<HIDDEN_NEURONS; hid++)
		{
			err_hid[hid] = 0;
			//add error of output nodes
			for(int out = 0; out<OUTPUT_NEURONS; out++)
				err_hid[hid] += err_out * w_o_h[out][hid];
			err_hid[hid] = err_hid[hid] * (hidden[hid]*(1-hidden[hid]));
		}
		
		//adjust the weights from the hidden to output layer
		for(int out = 0; out<OUTPUT_NEURONS; out++)
			for(int hid = 0; hid<HIDDEN_NEURONS; hid++)
				w_o_h[out][hid] += learningRate * err_out * hidden[hid];
		
		//calculate weight adjustment of the hidden neuron bias
		for(int i = 0; i < hbias.length; i++)
			hbias[i] += learningRate * err_hid[i];
		
		//calculate weight adjustments of the output neuron
		for(int i = 0; i < obias.length; i++)
			obias[i] += learningRate * err_out;
		
		//adjust the weights from the input to hidden layer
		for(int hid = 0; hid<HIDDEN_NEURONS; hid++)
			for(int inp = 0; inp<INPUT_NEURONS+1; inp++)
				w_h_i[hid][inp] += learningRate  * err_hid[hid] * inputs[inp];
	}
	
	/** Feed the input data through the network by calculating the output for every neuron from the input layer, through the hidden layer(s), to the output layer.
	* @return {void} none
	*/
	public void feedForward()
	{
		//calc outputs of hidden neurons
		for(int i = 0; i < HIDDEN_NEURONS; i++)
		{	
			hidden[i] = hbias[i];			//initialize the hidden output to the bias
			for(int j = 0; j < INPUT_NEURONS+1; j++)
				hidden[i] += (w_h_i[i][j] * inputs[j]);
			hidden[i] = 1/(1 + Math.exp(-hidden[i]));					//apply sigmoid function to output
		}

		//calc outputs for output layer
		for(int i = 0; i < OUTPUT_NEURONS; i++)
		{
			outputs[i] = obias[i];				//initialize the output output to the bias of the output
			for(int j = 0; j < HIDDEN_NEURONS+1; j++)
				outputs[i] += (w_o_h[i][j] * hidden[j]);
			outputs[i] = 1/(1 + Math.exp(-outputs[i]));
		}
	}

	/** Apply the inputs to the NN.
	* @return {void} none
	*/
	public void applyInputs()
	{
		for(int i = 0; i < INPUT_NEURONS; i ++)
			inputs[i] = trainingSet[i]; 
	}
	
	/** Intialize random weights throughout the network.
	* @return {void} none
	*/
	public void initializeRandomWeights()
	{
		//initialzie the weights of the hidden neurons and input neurons
		for(int i = 0; i < HIDDEN_NEURONS; i++)
			for(int j = 0; j < INPUT_NEURONS+1; j++)
				w_h_i[i][j] = Math.random();
		
		//initialize the weights of the output neurons and hidden neurons
		for(int i = 0; i < OUTPUT_NEURONS; i++)
			for(int j = 0; j < HIDDEN_NEURONS+1; j++)
				w_o_h[i][j] = Math.random();

		//initialize the weights of the hidden bias
		for(int i = 0; i < HIDDEN_BIAS; i++)
			hbias[i] = Math.random();
		
		//initialize the weights of the output bias
		for(int i = 0; i < OUTPUT_BIAS; i++)
			obias[i] = Math.random();
	}
	
	/** Set the training and test data of subjects based on the files in the passed in directory.
	* There are 300 subjects in all. 150 female. 150 males. Training with 120 of each. Test set is 60 of each.	
	* @param {string} path The path to the directory containing input files
	* @return {void} none
	*/
	public void setData(String path)
	{
		// Print out file statistics
	  File[] listOfFiles = new File(path).listFiles();
   	int m = 0, f = 0;
    for (int i = 0; i < listOfFiles.length; i++)
    {
      if (listOfFiles[i].isFile()) 
      {
	    	if(listOfFiles[i].getName().substring(0, listOfFiles[i].getName().indexOf(".")).contains("female"))
	    		f++;
	    	else
	    		m++;
      }
    }
    System.out.println("----->Gender File Distribution | Males: " + m/2 + " Females: " + f/2 + " Total: " + (m + f)/2);

	  int num2Train = 120;
	  int num2Test = 30;
    int numFemaleTrain = 0;
    int numMaleTrain = 0;
    int numFemaleTest = 0;
    int numMaleTest = 0;
   	int j = 0, k = 0;
    for (int i = 0; i < listOfFiles.length; i++)
    {
    	if (listOfFiles[i].isFile()) 
    	{
    		String name = listOfFiles[i].getName();
    		if(name.contains("male") && name.contains(this.subjectType))
    		{
    			if(name.contains("female"))
    			{	
    				if(numFemaleTrain < num2Train)
    				{
							this.trainingSetList.add(new Subject(path + name, this.subjectType));
    					j++;
    					numFemaleTrain++;
    				}
    				else if(numFemaleTest < num2Test)
    				{
							this.testingSetList.add(new Subject(path + name, this.subjectType));
    					k++;
    					numFemaleTest++;
    				}
    			}
    			else if(name.contains("male"))		//if it is a male and there are less then 200 males
    			{
    				if(numMaleTrain < num2Train)
    				{	
							this.trainingSetList.add(new Subject(path + name, this.subjectType));
    					j++;
    					numMaleTrain++;
    				}
    				else if(numMaleTest < num2Test)
    				{
							this.testingSetList.add(new Subject(path + name, this.subjectType));
    					k++;
    					numMaleTest++;
    				}
    			}
    		}
    	}
    	else if (listOfFiles[i].isDirectory()) 
    	{
    		System.out.println("Somehow there was a directory in the folder..." + listOfFiles[i].getName());
    	} 
    }
	}
	
	/** Main method were the program enters.
	* @param {String[]} args Command line arguments passed
	* @return {void} none
	*/
	public static void main(String[] args)
	{
		if(args[0].equals("-h")){
			System.out.println("");
		}
		else{
			NeuralNetworkGenderClassification n = new NeuralNetworkGenderClassification(args[0], args[1]);
		}
	}
}
