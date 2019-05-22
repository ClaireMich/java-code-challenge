import java.io.*;

public class Main {


    public static void main(String[] args) {

        if (args.length < 1)
        {
            System.out.println("You must enter a path for the csv for creating the table");
            System.out.println("Example (Windows): c:/work/people.csv");
            return;
        }else
        {
            readCSV(args);
        }
//        args =new String[1];
//        args[0]="peopleed.csv";
//        readCSV(args);
    }

    private static void readCSV(String[] args){
        String file=args[0];
        if(!fileExist(file)) {
            System.out.println("Error: file does not exist please enter a valid path for the csv");
            return;
        }
        String[] allArgs=setDefaultValues();
        if(args.length>1)
            for(int i=1; i< args.length; i++)
            {
                if(args[i].compareToIgnoreCase("d")!=0)
                    allArgs[i-1]=args[i];
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
            System.out.println("Finish creating table with data on mysql");
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

    private static Boolean fileExist(String file){
        File f = new File(file);
        if(f.exists() && !f.isDirectory()) {
            return true;
        }
        return false;
    }
}
