/*
* Subject.java
* Visit the internets for documentation, updates and examples.
* https://github.com/pwhisenhunt/
*
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
import java.io.FileNotFoundException;
import java.util.Scanner;

/** This class represents a subject comprised of gender, feature vector, and file name. It is used by NeuralNetworkGenderClassification.
*/
public class Subject 
{
	private double[] featureVector;
	private String fileName;
	private double gender = 0;		//0 denotes male, 1 female
	public double[] getFeatureVector() { return featureVector; }
	public String getFileName() { return fileName; }
	public double getGender() { return gender; }
	public void setFeatureVector(double[] featureVector) { this.featureVector = featureVector; }
	public void setFileName(String fileName) { this.fileName = fileName; }
	public void setGender(double gender) { this.gender = gender; }

	/** Default constructor of Subject. It parses out the relevant feature information of the file.
	* @param {string} fileName The path to the filename to parse.
	* @param {string} type The type of file it is or subject type; either "tex", "shape", or "app"
	*/
	public Subject(String fileName, String type)
	{
			// If we are using texture
			if(type.contains("tex")){
				try
				{
					this.fileName = fileName;				//set the file name
					if(fileName.contains("female"))			//check gender of the subject
						this.gender = 1;
					else if(fileName.contains("male"))
						this.gender = 0;
					else
					{
						System.out.println("An unknown gender is found. Returning now."); return;								//if gender is not applicable, return
					}
					if(fileName.contains("tex"))			//check to make sure a shape file is being loaded
					{
						double[] tempFeatureVector = new double[344];
						int i = 0;
						Scanner scanner = new Scanner(new File(fileName));
						while(scanner.hasNextLine())						//get the feature vector from the file
						{
							if(i < tempFeatureVector.length)
							{
								tempFeatureVector[i] = Double.valueOf(scanner.nextLine());
								i++;
							}
							else
							{
								System.out.println("Out of bounds of the feature vector...");
								return;
							}
							setFeatureVector(tempFeatureVector);			//set the feature vector
						}
					}
					else
						System.out.println("Non Shape file trying to be loaded.");
				}
				catch(Exception e)
				{
					System.out.println("There was a problem opening the file. The error was " + e.getMessage());
				}
			}
			else if(type.contains("shape")){
				try
				{
					this.fileName = fileName;				//set the file name
					if(fileName.contains("female"))			//check gender of the subject
						this.gender = 1;
					else if(fileName.contains("male"))
						this.gender = 0;
					else
					{
						System.out.println("An unknown gender is found. Returning now."); return;								//if gender is not applicable, return
					}
					if(fileName.contains("shape"))			//check to make sure a shape file is being loaded
					{
						double[] tempFeatureVector = new double[105];
						int i = 0;
						Scanner scanner = new Scanner(new File(fileName));
						while(scanner.hasNextLine())						//get the feature vector from the file
						{
							if(i < tempFeatureVector.length)
							{
								tempFeatureVector[i] = Double.valueOf(scanner.nextLine());
								i++;
							}
							else
							{
								System.out.println("Out of bounds of the feature vector...");
								return;
							}
							setFeatureVector(tempFeatureVector);			//set the feature vector
						}
					}
					else
						System.out.println("Non Shape file trying to be loaded.");
				}
				catch(Exception e)
				{
					System.out.println("There was a problem opening the file. The error was " + e.getMessage());
				}
			}
			else{
				try
				{
					this.fileName = fileName;				//set the file name
					if(fileName.contains("female"))			//check gender of the subject
						this.gender = 1;
					else if(fileName.contains("male"))
						this.gender = 0;
					else
					{
						System.out.println("An unknown gender is found. Returning now."); return;								//if gender is not applicable, return
					}
					if(fileName.contains("app"))			//check to make sure a shape file is being loaded
					{
						double[] tempFeatureVector = new double[239];
						int i = 0;
						Scanner scanner = new Scanner(new File(fileName));
						while(scanner.hasNextLine())						//get the feature vector from the file
						{
							if(i < tempFeatureVector.length)
							{
								tempFeatureVector[i] = Double.valueOf(scanner.nextLine());
								i++;
							}
							else
							{
								System.out.println("Out of bounds of the feature vector...");
								return;
							}
							setFeatureVector(tempFeatureVector);			//set the feature vector
						}
					}
					else
						System.out.println("Non Shape file trying to be loaded.");
				}
				catch(Exception e)
				{
					System.out.println("There was a problem opening the file. The error was " + e.getMessage());
				}
			}
	}
}
