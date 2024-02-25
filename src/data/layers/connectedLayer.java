package data.layers;

import java.util.List;
import java.util.Random;

public class connectedLayer extends layer
{

    private double[][] weights;
    private int inLength;
    private int outLength;
    private long SEED;
    private final double leak = 0.01;
    private double learningRate;

    private double[] lastZ;
    private double[] lastX;



    public connectedLayer(int inLength, int outLength, long SEED, double learningRate)
    {
        this.inLength = inLength;
        this.outLength = outLength;
        this.SEED = SEED;
        this.learningRate = learningRate;
        
        


        weights = new double[inLength][outLength];

        setRandomWeights();


    }

    public double[] connectedForwardPass(double[] input)
    {
        lastX = input;

        double[] z = new double[outLength];
        double[] out = new double[outLength];

        for (int i = 0; i < inLength; i++)
        {
            for (int j = 0; j < outLength; j++)
            {
                z[j] += input[i] * weights[i][j];
            }
        }
        lastZ = z;


        for (int i = 0; i < inLength; i++)
        {
            for (int j = 0; j < outLength; j++)
            {
                out[j] += relu(z[j]);
            }
        }
        return out;


    }

    


    public double[] getOutput(List<double[][]> input)
    {
        double[] vector = matrixToVector(input);
        return getOuptut(vector);

        

    }

    public double[] getOuptut(double[] input)
    {
        double[] forwardPass = connectedForwardPass(input);
        
        if (nextLayer != null)
        {
            return nextLayer.getOuput(forwardPass);

            
        }
        else
        {
            return forwardPass;
        }
       
    }

    public void backPropagation(double[] derivative)
    {
        double[] derivativelx = new double[inLength];


        double derivativeoz;
        double derivativewz;
        double derivativelw;
        double derivativezx;

        for (int k = 0; k < inLength; k++)
        {
            double derivativelxSum = 0;
            for (int j = 0; j < outLength; j++)
            {
                derivativeoz = derivativeRelu(j);
                derivativewz = lastX[k];
                derivativezx = weights[k][j];

                derivativelw = derivative[j] * derivativeoz * derivativewz;
                weights[k][j] -= derivativelw * learningRate;

                derivativelxSum += derivative[j] * derivativeoz * derivativezx;

            }
            derivativelx[k] = derivativelxSum;
            if (previousLayer != null)
            {
                previousLayer.backPropagation(derivativelxSum);
            }

        }

        

    }

    public void backPropagation(List<double[][]> derivative)
    {

        double[] vector = matrixToVector(derivative);
        backPropagation(vector);
    }

    public int getOutputLength()
    {
        return 0;
    }

    public int getOutputRows()
    {
        return 0;
    }

    public int getOutputCols()
    {
        return 0;

    }

    public int getOutputElements()
    {
        return outLength;
    }


    public void setRandomWeights()
    {
        
        Random rand = new Random(SEED);


        for (int i = 0; i < inLength; i++)
        {
            for (int j = 0; i < outLength; j++)
            {
                weights[i][j] = rand.nextGaussian();
            }
        }

        


    }

    public double relu(double input)
    {

        if (input <= 0)
        {
            return 0;
        }
        else
        {
            return input;
        }
    
    }




    public double derivativeRelu(double input)
    {

        if (input <= 0)
        {
            // floor for minimum
            return leak;
        }
        else
        {
            // ceiling
            return 1;
        }
    
    }

    @Override
    public double[] getOuput(List<double[][]> input) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getOuput'");
    }

    @Override
    public double[] getOuput(double[] input) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getOuput'");
    }

    @Override
    public void backPropagation(double derivativelxSum) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'backPropagation'");
    }

    

    
}
