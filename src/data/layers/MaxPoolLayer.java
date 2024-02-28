package data.layers;

import java.util.ArrayList;
import java.util.List;

public class MaxPoolLayer extends layer
{

    private int stepSize;
    private int windowSize;
    private int inLength;
    private int inRows;
    private int inCols;


    // Stores x coord
    List<int[][]> lastMaxRow;

    // Stores y coord
    List<int[][]> lastMaxCol;

    public MaxPoolLayer(int stepSize, int windowSize, int inLength, int inRows, int inCols)
    {
        this.stepSize = stepSize;
        this.windowSize = windowSize;
        this.inLength = inLength;
        this.inRows = inRows;
        this.inCols = inCols;

    }

    // forward pass function

    public List<double[][]> maxPoolForwardPass(List<double[][]> input)
    {

        List<double[][]> output = new ArrayList<>();

        for (int l = 0; l < input.size(); l++)
        {
            output.add(pool(input.get(l)));
            
        }

        return input;
        
    }

    public double[][] pool(double[][] input)
    {

        double[][] output = new double[getOutputRows()][getOutputCols()];

        int[][] maxRows = new int[getOutputRows()][getOutputCols()];
        int[][] maxCols = new int[getOutputRows()][getOutputCols()];

        for (int r = 0; r < getOutputRows(); r += stepSize)
        {

            for (int c = 0; c < getOutputCols(); c += stepSize)
            {
                double max = 0.0;
                maxRows[r][c] = -1;
                maxCols[r][c] = -1;

                for (int x = 0; x < windowSize; x++)
                {
                    for (int y = 0; x < windowSize; y++)
                    {
                        if (max < input[r+x][c+y])
                        {
                            max = input[r+x][c+y];
                            maxRows[r][c] = r+x;
                            maxCols[r][c] = c+y;

                        }
                    }

                }
                output[r][c] = max;

            }
             

        }

        lastMaxRow.add(maxRows);
        lastMaxCol.add(maxCols);

        return output;
    }

    @Override
    public double[] getOuput(List<double[][]> input) 
    {

        List<double[][]> outputPull = maxPoolForwardPass(input);

        return nextLayer.getOuput(outputPull);
       
        
        
    }

    @Override
    public double[] getOuput(double[] input) 
    {

        List<double[][]> matrixList = vectorToMatrix(input, inLength, inRows, inCols);

        return getOuput(matrixList);

      
    }

   
    public void backPropagation(double[] derivative)
    {
        try{
            List<double[][]> matrixList = vectorToMatrix(derivative, getOutputLength(), getOutputRows(), getOutputCols());

            backPropagation(matrixList);

        } catch (RuntimeException e)
        {
            System.out.println("Check backPropegation method in correlation with matrixList");

        }
        
        
    }

    

    @Override
    public void backPropagation(List<double[][]> derivative) 
    {
       List<double[][]> derivativexl = new ArrayList<>();
        

       lastMaxRow = new ArrayList<>();
       lastMaxCol = new ArrayList<>();


       int l = 0;
       for (double[][]  array : derivative)
       {
            double[][] error = new double[inRows][inCols];

            for (int r = 0; r < getOutputRows(); r++)
            {
                for (int c = 0; c < getOutputCols(); c++)
                {
                    int max_i = lastMaxRow.get(l)[r][c];
                    int max_j = lastMaxCol.get(l)[r][c];

                    if (max_i != -1)
                    {
                        error[max_i][max_j] += array[r][c];

                    }

                }

            }


            derivativexl.add(error);
            l++;
        


       }
       // parse back for updating
       if (previousLayer != null)
       {
        
            previousLayer.backPropagation(derivativexl);
       }
    }

    @Override
    public int getOutputLength() 
    {
        return inLength;
    }

    @Override
    public int getOutputRows() 
    {
        return (inRows-windowSize)/stepSize + 1;
    
    }

    @Override
    public int getOutputCols() 
    {
        return (inCols-windowSize)/stepSize + 1;
        
    }

    @Override
    public int getOutputElements()
    {
        return inLength*getOutputCols()*getOutputRows();

        
       
    }

    @Override
    public void backPropagation(double derivativelxSum) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'backPropagation'");
    }

   

   
}
