##Neural Network Facial Gender Classification

This is a command line neural network implemented in java for facial gender classification. I wrote this during my undergraduate studies in CS for an AI class. The neural network works by supplying it a directory containing b parameters of either texture, shape, or both information. The b parameters are created from active appearance models and are in essence the Principal Components of each face. Please note that this was written years ago and therefore, there is no guarantee of accuracy.

Requirements
-----------
Java run time environment.

Getting Started
-----------

Download the project and example data. Navigate to inside of the project folder and compile the .java files.

```console
javac NeuralNetworkGenderClassification.java
```
To run the program enter.

```console
java NeuralNetworkGenderClassification directory subject_type
```

Where directory is a string pointing to the b parameter files and subject_type is either : tex, shape, or app. Where tex tells the NN to train using texture, shape to use the shape information, and app to use both.

Example.

```console
java NeuralNetworkGenderClassification ../data/ tex
```

Output

```console
Facial Gender Classification Neural Network.
----->Gender File Distribution | Males: 282 Females: 528 Total: 810
----->Training the network on texture data.
----->Testing the network.
----->----->58 / 60 or 96.666664% accuracy.
----->----->Number of males wrong:   0
----->----->Number of females wrong: 2
```
