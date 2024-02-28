package data;
import java.io.FileNotFoundException;
import java.util.List;


// main method for checking dataReader method is working
public class Main 
{
    public static void main(String[] args) throws FileNotFoundException 
    {
        List<Image> images = new DataReader().readData("");
        
    }
    
}
