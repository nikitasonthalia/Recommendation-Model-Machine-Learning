import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by nikitasonthalia on 5/24/16.
 * This Code generate the Item base recommendation.
 */

/**
 * This class is for Mapping data.
 */
class MyMap
{
    Map<Integer,List<String>> map=new HashMap<Integer,List<String>>();

    Map<Integer,List<String>> add(String key, String value)
    {

        if (this.map.containsKey(Integer.parseInt(key)) )
        {
            this.map.get(Integer.parseInt(key)).add(value);

        } else
        {
            this.map.put(Integer.parseInt(key), new ArrayList<String>());
            this.map.get(Integer.parseInt(key)).add(value);


        }
        return map;
    }

    Set<Map.Entry<Integer, List<String>>> getEntrySet()
    {
        return this.map.entrySet();
    }
    List<String> get(Integer key)
    {
        return this.map.get(key);
    }

    Set<Integer> getKeyset()
    {
        return this.map.keySet();
    }
    String getValuebykey(int key, int index)
    {
        return this.map.get(key).get(0);
    }
    int getKey(int key)
    {
        for(Map.Entry<Integer, List<String>> ee : this.map.entrySet())
        {

            if(Integer.parseInt(ee.getValue().get(0)) ==key)
            {
                return ee.getKey();
            }
        }

        return key;
    }
    boolean containKey(int key)
    {
        return  this.map.keySet().contains(key);
    }
}

/**
 * This main class.
 */
public class P1 {
    static int flagM=0;
    static int flagU=0;
    /**
     * This method is for validating csv data file and validate that the data contain required value.
     * @param token is the , separated string array.
     * @return true if data are valid else false.
     */
    public static boolean validateData(String token[])
    {
        if(token[0].trim().matches("\\d+") && token[1].trim().matches("\\d+") && token[2].trim().matches("\\d+\\.\\d+"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * This method remove comments and space, tab etc.
     * @param line is the data read from file.
     * @return string removed all comments space etc.
     */
    public static String removeCommentAndSpace(String line)
    {
        Pattern pattern = Pattern.compile("#.*$", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(line);

        line = matcher.replaceFirst("");
        line=line.replaceAll("\\t+","");
        line=line.replaceAll("\\s+","");
        line=line.replaceAll("\\n","");
        return line;
    }


    /**
     * This method build the vector.
     * @param dataByRataing This is maping to movies to user and rating.
     * @param mappingMovie This is mapping movies to no.
     * @param vector This is null vector
     * @param mappingUser This is mapping user to no.
     * @return It return the vector user.
     */
    public static Vector<Vector<Double>> getVector(MyMap dataByRataing, MyMap mappingMovie, Vector<Vector<Double>> vector, MyMap mappingUser)
    {
        for(Map.Entry<Integer,List<String>> e : dataByRataing.getEntrySet())
        {

//            mappingMovie.add(e.getKey().toString(), String.valueOf(flagM++));
            if(vector.size()<=flagM)
            {
                for(int k = vector.size();k<=flagM;k++) {
                    vector.add(new Vector<Double>());
                }
            }
            Object[] token=e.getValue().toArray();
            for(int k=0; k<token.length;k++)
            {
                String[] tmp=token[k].toString().split(",");
//                if(!mappingUser.containKey(Integer.parseInt(tmp[0].trim())))
//                {
//                    mappingUser.add(tmp[0].trim(), String.valueOf(flagU++));
//                }
                List<String> kk = mappingMovie.get(e.getKey());
                int key = Integer.parseInt(kk.get(0));

                if(vector.get(key).size()<=flagU)
                {
                    for(int i =vector.get(key).size();i<=flagU;i++)
                    {
                        vector.get(key).add(0.0);
                    }
                }
                List<String> kkk= mappingUser.get(Integer.parseInt(tmp[0].trim()));
                int keyU= Integer.parseInt(kkk.get(0));
                vector.get(key).set(keyU,Double.parseDouble(tmp[1].trim()));
            }
        }
        return vector;
    }

    /**
     * This method bulid the Co-orrance matrix.
     * @param dataByUser this is mapping user to movies.
     * @param mappingMovie this is mapping movies to no.
     * @param matrix this is null matrix
     * @return it return the co-orance matrix.
     */
    public static  Vector<Vector<Integer>> getMatrix(MyMap dataByUser, MyMap mappingMovie, Vector<Vector<Integer>> matrix)
    {

        for (int k =0; k <= flagM; k++) {
                matrix.add(new Vector<Integer>());
                for (int l = 0; l <=flagM; l++) {
                    matrix.get(k).add(0);

                }
            }
           for(Map.Entry<Integer, List<String>> ee : dataByUser.getEntrySet())
           {
                Object[] values = ee.getValue().toArray();
                for(int i =0;i<values.length;i++)
                {
                    for(int j=i;j<values.length;j++)
                    {
                        int keyI = Integer.parseInt(mappingMovie.getValuebykey(Integer.parseInt(values[i].toString()),0));
                        int keyJ = Integer.parseInt(mappingMovie.getValuebykey(Integer.parseInt(values[j].toString()),0));
                        matrix.get(keyI).set(keyJ, (matrix.get(keyI).get(keyJ) + 1));
                        if (keyI != keyJ) {
                            matrix.get(keyJ).set(keyI, (matrix.get(keyJ).get(keyI) + 1));
                        }

                    }
                }
           }
        return matrix;
    }

    /**
     * This multiple matrix and vector.
     * @param matrix
     * @param vector
     * @return multipled array.
     */
    private static double[][] matrixMulti(Vector<Vector<Integer>> matrix, Vector<Vector<Double>> vector) {
        int mA = matrix.size()-1;
        int nA = matrix.get(1).size()-1;
        int mB = vector.size()-1;
        int nB = flagU;
        if (nA != mB) throw new RuntimeException("Illegal matrix dimensions.");
        double[][] C = new double[mA][nB];
        for (int i = 0; i < mA; i++) {
            for (int j = 0; j < nB; j++) {
                for (int k = 0; k < nA; k++) {
                    C[i][j] += matrix.get(i).get(k) * vector.get(k).get(j);
                }
            }
        }
        return C;
    }

    /**
     * This will create recommendation.
     * @param C
     * @param mappingUser
     * @param mappingMovie
     * @param vector
     */
    private static void recommendation(double[][] C,MyMap mappingUser, MyMap mappingMovie, Vector<Vector<Double>> vector)
    {
        int row=C[0].length;
        int col=C.length;
        for (int i=0;i<row;i++)
        {

            double max=0.0;
            System.out.print("User " + mappingUser.getKey(i) + " ---> ");
            for(int j =0 ; j<col;j++)
            {
                System.out.print("\t" + C[j][i]);
                if(vector.get(j).get(i).equals(0.0))
                {
                    if(C[j][i]>max)
                    {
                        max=C[j][i];
                        flagM = (int) mappingMovie.getKey(j);
                    }
                }
            }
            System.out.print(" -----> Recomemdate movie  " + flagM);
            System.out.println();
        }
    }
    /**
     * This is main method which build all main recommendation logic.
     * @param args
     */
    public static void main(String[] args){
        BufferedReader in = null;
        /**
         * dataByUser map user as a key to his all movies he rated.
         */
        MyMap dataByUser = new MyMap();
        /**
         * dataByRataing map movies as a key to user and rating.
         */
        MyMap dataByRataing=new MyMap();
        /**
         * mappingMovie map the movies as a key to no. for mapping as a array. So that no. of movies is map in sequence from 0 to total no.of movie.
         */
        MyMap mappingMovie =new MyMap();
        /**
         * mappingUser map the user as a key to no. for mapping as a array. So that no. of user is map in sequence from 0 to total no.of user.
         */
        MyMap mappingUser = new MyMap();
        /**
         * matrix is the vector which form co-occurrence matrix. It use data from mappingMovies and dataByUser.
         */
        Vector<Vector<Integer>> matrix=new Vector<Vector<Integer>>();
        /**
         * vector is the user based rating vector. It use data from mappingMovies, mappingUser and dataByRating.
         */
        Vector<Vector<Double>> vector=new Vector<Vector<Double>>();
        double[][] C ;
        String line;
        try {
           // in = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new FileReader("data.txt"));
            while ((line = in.readLine()) != null) {
                line=removeCommentAndSpace(line);
                if(!line.isEmpty()) {
                    String[] token = line.split(",");
                    if(validateData(token))
                    {
                        dataByUser.add(token[0].trim(),token[1].trim());
                        dataByRataing.add(token[1].trim(),token[0].trim() + "," + token[2].trim());
                        if(!mappingUser.containKey(Integer.parseInt(token[0].trim())))
                        {
                            mappingUser.add(token[0].trim(), String.valueOf(flagU++));
                        }
                        if(!mappingMovie.containKey(Integer.parseInt(token[1].trim())))
                        {
                            mappingMovie.add(token[1].toString(), String.valueOf(flagM++));
                        }
                    }
                    else
                    {
                        System.out.println("Data is not in requried format.");
                        System.exit(1);
                    }
                }
            }


            vector=getVector(dataByRataing,mappingMovie, vector,  mappingUser);
            for(int i=0;i<vector.size();i++)
            {
                for(int j=0;j<vector.get(i).size();j++)
                {
                    System.out.print("\t"+vector.get(i).get(j));
                }
                System.out.println();
            }
            matrix=getMatrix(dataByUser,mappingMovie, matrix);
            C=matrixMulti(matrix,vector);
            recommendation(C,mappingUser,mappingMovie,vector);


        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.exit(1);

        }
        finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Done");
        System.exit(0);

    }


}

