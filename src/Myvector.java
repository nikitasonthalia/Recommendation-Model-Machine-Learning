import java.io.FileNotFoundException;

/**
 * Created by nikitasonthalia on 5/25/16.
 */
public class Myvector {
  public static void main(String []args) throws FileNotFoundException {
        double[] ss = {42.0,47.0,59, 27, 84, 49, 72, 43, 73, 59, 58, 82, 50, 79, 89, 75, 70, 59, 67, 35} ;
//        for(int i =0 ; i<ss.length;i++)
//        {
//            ss[i]=(ss[i]-27.0)/(89.0-27.0)*(1-(-1))+(-1);
//        }
        double mean;
      double sum=0;
      double sd;
      double temp;
      for(int i =0 ; i<ss.length;i++)
      {
          sum+=ss[i];
      }
      mean=sum/ss.length;
      sum=0;
      for(int i =0 ; i<ss.length;i++)
      {
          temp=(ss[i]-mean);
          temp=Math.pow(temp,2);
          sum+=temp;
      }
      sd= Math.sqrt((sum/(ss.length-1)));
      for(int i =0 ; i<ss.length;i++)
      {
          ss[i]=(ss[i]-mean)/sd;
      }
      for(int i =0 ; i<ss.length;i++)
      {
          System.out.println(ss[i]);
      }

//        Vector<String> myVector=new Vector<String>(10,2);
//      String sample="tester";          //test string declare
//
//      myVector.add(sample);            //adds sample's value to the vector
//
//      System.out.println("Value is :"+myVector.get(0));

  }//end of main



}
