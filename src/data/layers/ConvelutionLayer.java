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
            
        }
        return output;





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
        List<double[][]> output = convelutionForwardPass(input);

        return nextLayer.getOuput(output);
    }

    @Override
    public double[] getOuput(double[] input) 
    {
        List<double[][]> matrixInput = vectorToMatrix(input, inLength, inRows, inCols);
        
        return getOuput(matrixInput);
    }

    @Override
    // change in loss compared to change in relative output
    public void backPropagation(double[] derivative) 
    {

        List<double[][]> matrixInput = vectorToMatrix(derivative, inLength, inRows, inCols);
        backPropagation(matrixInput);

    }

    @Override
    public void backPropagation(List<double[][]> derivative) 
    {
        List<double[][]> filtersDelta = new ArrayList<>();
        List<double[][]> derivativePreviousLayer = new ArrayList<>();

        for (int f = 0; f < _filters.size(); f++)
        {
            filtersDelta.add(new double[filterSize][filterSize]);
        }

        for (int i = 0; i < lastInput.size(); i++)
        {
            double[][] errorForInput = new double[inRows][inCols];
            for (int f = 0; f < _filters.size(); f++)
            {
                double[][] currFilter = _filters.get(f);
                double[][] error = derivative.get(i*_filters.size() + f);
                double[][] spaceError = spaceArray(error);
                double[][] derivativelf = convolve(lastInput.get(i), spaceError, 1);

                double[][] delta = multiply(derivativelf, _learningRate);
                double[][] newTotalDelta = add(filtersDelta.get(f), delta);
                filtersDelta.set(f, newTotalDelta);

                double[][] flippedError = flipArrayHorizontal(flipArrayVerticle(spaceError));
                errorForInput = add(errorForInput, fullConvolve(currFilter, flippedError));

            }
            derivativePreviousLayer.add(errorForInput);
        }
        // updating filters
        for (int f = 0; f < _filters.size(); f++)
        {
            double[][] modified = add(filtersDelta.get(f), _filters.get(f));
            _filters.set(f, modified);

        }

        if (previousLayer != null)
        {
            previousLayer.backPropagation(derivativePreviousLayer);
        }
        

        
    }

    private double[][] add(double[][] ds, double[][] delta) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'add'");
    }

    private double[][] multiply(double[][] derivativelf, double _learningRate2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'multiply'");
    }

    public double[][] flipArrayHorizontal(double[][] array)
    {
        int rows = array.length;
        int cols = array[0].length;

        double[][] output = new double[rows][cols];

        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols; j++)
            {
                output[rows-i-1][j] = array[i][j];
            }

        }
        return output;

    }

    public double[][] flipArrayVerticle(double[][] array)
    {
        int rows = array.length;
        int cols = array[0].length;

        double[][] output = new double[rows][cols];

        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols; j++)
            {
                output[rows][j-i-1] = array[i][j];
            }

        }
        return output;

    }

    public double[][] fullConvolve(double[][] input, double[][] filter)
    {
        int outRows = (input.length + filter.length) + 1;
        int outCols = (input[0].length + filter[0].length) + 1;

        int inRows = input.length;
        int inCols = input[0].length;

        int fRows = filter.length;
        int fCols = filter[0].length;

        double[][] output = new double[outRows][outCols];


        int outRow = 0;
        int outCol;

        for (int i = -fRows; i < inRows; i += stepSize)
        {
            outCol = 0;
            for (int j = -fCols; j < inCols; j+= stepSize)
            {
                double sum = 0.0;

                for (int x = 0; x < fRows; x++)
                {
                    for (int y = 0; y < fCols; y++)
                    {
                        int inputRowIndex = i+x;
                        int inputColIndex = j+x;

                        if (inputRowIndex >= 0 && inputColIndex >= 0 && inputRowIndex < inRows && inputColIndex < inCols)
                        {
                            double value = filter[x][y] * input[inputRowIndex][inputColIndex];
                            sum += value;
                            

                        }

                  

                    }
                    
                }
                output[outRow][outCol] = sum;
                outCol++;
            }
            
        }
        return output;
    }






    @Override
    public int getOutputLength()
    {
       return _filters.size()*inLength;
    }

    @Override
    public int getOutputRows() 
    {
        return (inRows-filterSize)/stepSize + 1;
        
    }

    @Override
    public int getOutputCols() 
    {
        return (inCols-filterSize)/stepSize + 1;
        
    }

    @Override
    public int getOutputElements() 
    {
        return getOutputCols()*getOutputRows()*getOutputLength();
       
    }

    @Override
    public void backPropagation(double derivativelxSum) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'backPropagation'");
    }
    
}
