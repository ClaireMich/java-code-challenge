import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Main {


    public static void main(String[] args) {

//        if (args.length < 1)
//        {
//            System.out.println("You must enter a path to a directory with the csv for creating the table");
//            System.out.println("Example (Windows): c:/work/people.csv");
//            return;
//        }else
//        {
//            readCSV(args);
//        }
        args =new String[1];
        args[0]="people.csv";
        readCSV(args);
    }

    private static void readCSV(String[] args){
        String file=args[0];
        String[] allArgs=setDefaultValues();
        if(args.length>1)
            for(int i=1; i< args.length; i++)
            {
                if(args[i].compareToIgnoreCase("d")!=0)
                    allArgs[i]=args[i];
            }
        BufferedReader br = null;
        String line = "", cvsSplitBy="";
        Boolean isFirstLine=true;
        String[] data =null;
        DBConnect dbConnect=new DBConnect(allArgs[2],allArgs[3], allArgs[4], allArgs[5]);
        dbConnect.createDBIfNotExists(allArgs[0]);
        dbConnect.connectToDataBase();
        try {
            br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {
                line= cleanLine(line);
                if(isFirstLine){
                    cvsSplitBy=findSplitSeparator(line);
                    data = line.split(cvsSplitBy);
                    dbConnect.creatingTable(data, allArgs[1]);
                    isFirstLine=false;
                }else {
                    data = line.split(cvsSplitBy);
                    dbConnect.addingValues(data, allArgs[1]);
                }
                //Imprime datos.
                System.out.println(data[0] + ", " + data[1] + ", " + data[2] + ", " + data[3] + ", " + data[4]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String[] setDefaultValues(){
        String[] allArgs=new String[6];
        //Set default values
        allArgs[0]="DatabaseTest";
        allArgs[1]="TableTest";
        allArgs[2]="localhost";
        allArgs[3]="3306";
        allArgs[4]="root";
        allArgs[5]="root";
        return allArgs;
    }

    private static String cleanLine(String line){
        line=line.replace('"', ' ');
        line=line.replace("'", " ");
        return line;
    }

    private static String findSplitSeparator(String line){
        String [] datos =null;
        if(line.split(";")!=null)
            return ";";
        if(line.split(",")!=null)
            return ",";
        if(line.split("\t")!=null)
            return "\t";
        return "";
    }
}
