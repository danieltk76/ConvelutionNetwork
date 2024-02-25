package data.layers;

import java.util.ArrayList;
import java.util.List;


// abstract layer, not complete
// another comment for git test
public abstract class layer 
{

    public void setNextLayer(layer nextLayer)
    {
        this.nextLayer = nextLayer;
    }


    public void setPreviousLayer(layer previousLayer)
    {
        this.previousLayer = previousLayer;
    }

    public layer getPreviousLayer()
    {
        return previousLayer;
    }

    public layer getNexLayer()
    {
        return nextLayer;
    }


    protected layer nextLayer;
    protected layer previousLayer;

    public abstract double[] getOuput(List<double[][]> input);
    public abstract double[] getOuput(double[] input);

    public abstract void backPropagation(double derivativelxSum);
    public abstract void backPropagation(List<double[][]> derivative);

    public abstract int getOutputLength();
    public abstract int getOutputRows();
    public abstract int getOutputCols();
    public abstract int getOutputElements();


    public double[] matrixToVector(List<double[][]> input)
    {
        int length = input.size();
        int rows = input.get(0).length;
        int cols = input.get(0)[0].length;

        double[] vector = new double[length*rows*cols];

        int i = 0;
        for (int l = 0; l < length; l++)
        {
            for (int r = 0; r < rows; r++)
            {
                for (int c = 0; c < cols; c++)
                {
                    vector[i] = input.get(l)[r][c];
                    i++;

                }
            }
        }

        return vector;
    }

    List<double[][]> vectorToMatrix(double[] input, int length, int rows, int cols)
    {
        List<double[][]> out = new ArrayList<>();

        int i = 0;
        for (int l = 0; l < length; l++)
        {
            double[][] matrix = new double[rows][cols];

            for (int r = 0; r < rows; r++)
            {

                for (int c = 0; c < cols; c++)
                {

                    matrix[l][c] = input[i];
                    i++;
                }
                
            }
            out.add(matrix);
        }

        return out;
    }

   
    
}
