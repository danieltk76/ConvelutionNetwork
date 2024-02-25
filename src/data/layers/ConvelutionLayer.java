package data.layers;

import java.util.ArrayList;
import java.util.Random;
import java.util.List;

// convolution: input, filter, output
public class ConvelutionLayer extends layer
{
    private long SEED;
    private List<double[][]> _filters;
    private int filterSize;
    private int stepSize;
    private int inLength;
    private int inRows; 
    private int inCols;
    private double _learningRate;
    private List<double[][]> lastInput;
    
    

    public ConvelutionLayer(int filterSize, int stepSize, int inLength, int inRows, int inCols, long Seed, int numFilters, double learningRate)
    {
        this.filterSize = filterSize;
        this.stepSize = stepSize;
        this.inLength = inLength;
        this.inRows = inRows;
        this.inCols = inCols;
        this.SEED = SEED;
        _learningRate = learningRate;

        generateRandomFilters(numFilters);
        

        
    }

    private void generateRandomFilters(int numFilters)
    {
        List<double[][]> filters = new ArrayList<>();
        Random random = new Random(SEED);

        for (int n = 0; n < numFilters; n++)
        {

            double[][] newFilter = new double[filterSize][filterSize];

            for (int i = 0; i < filterSize; i++)
            {
                for (int j = 0; j < filterSize; j++)
                {
                    double value = random.nextGaussian();
                    newFilter[i][j] = value;
                }
            }
            filters.add(newFilter);



        }
        _filters = filters;

    }



    public List<double[][]> convelutionForwardPass(List<double[][]> list)
    {
        lastInput = list;
        List<double[][]> output = new ArrayList<>();
        for (int m = 0; m < list.size(); m++)
        {
            for (double[][] filter : _filters)
            {
                output.add(convolve(list.get(m), filter, stepSize));

            }

        }
        return output;

    }

    public double[][] convolve(double[][] input, double[][] filter, int stepSize)
    {
        int outRows = (input.length - filter.length)/stepSize + 1;
        int outCols = (input[0].length - filter[0].length)/stepSize + 1;

        int inRows = input.length;
        int inCols = input[0].length;

        int fRows = filter.length;
        int fCols = filter[0].length;

        double[][] output = new double[outRows][outCols];


        int outRow = 0;
        int outCol;

        for (int i = 0; i <= inRows; i += stepSize)
        {
            outCol = 0;
            for (int j = 0; j <= inCols - fCols; j+= stepSize)
            {
                double sum = 0.0;

                for (int x = 0; x < fRows; x++)
                {
                    for (int y = 0; y < fCols; y++)
                    {
                        int inputRowIndex = i+x;
                        int inputColIndex = j+x;

                        double value = filter[x][y] * input[inputRowIndex][inputColIndex];
                        sum += value;

                    }
                    
                }
                outRow++;

            }
            return output;
        }





    }


    public double[][] spaceArray(double[][] input)
    {
        if (stepSize == 1)
        {
            return input;
        }

        int outRows = (input.length - 1) * stepSize + 1;
        int outCols = (input[0].length) * stepSize;

        double[][] output = new double[outRows][outCols];

        for (int i = 0; i < input.length; i++)
        {
            for (int j = 0; j < input[0].length; j++)
            {
                output[i * stepSize][j * stepSize] = input[i][j];


            }
        }
        return output;
    }


    @Override
    public double[] getOuput(List<double[][]> input) 
    {
        
    }

    @Override
    public double[] getOuput(double[] input) 
    {
        
    }

    @Override
    public void backPropagation(double derivativelxSum) 
    {
        
    }

    @Override
    public void backPropagation(List<double[][]> derivative) 
    {
        
    }

    @Override
    public int getOutputLength()
    {
       
    }

    @Override
    public int getOutputRows() 
    {
        
    }

    @Override
    public int getOutputCols() 
    {
        
    }

    @Override
    public int getOutputElements() 
    {
       
    }
    
}
