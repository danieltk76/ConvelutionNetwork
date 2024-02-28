package data;

public class Image 
{
    private double[][] data;
    private int label;


    // shadowing prevention
    public Image(double[][] data)
    {
        this.data = data;
        this.label = label;

    } 

    public int getLabel()
    {
        return label;

    }

    public double[][] geData()
    {
        return data;
    }


    // shadowing prevention
    public Image(double[][] data, int label)
    {
        this.data = data;
        this.label = label;
    }
}
