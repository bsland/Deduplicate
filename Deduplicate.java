import java.sql.*;
import java.util.LinkedHashMap;
import java.util.HashSet;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.ArrayList;

public class Deduplicate{
  public static void main(String[] args) throws ClassNotFoundException {

    //TODO parameterize
    final int THRESHOLD = 3;

    Levenshtein levenshtein = new Levenshtein(THRESHOLD);

    //TODO PostgreSQL has built-in Levenshtein function


    //TODO Possibly search for any names containing the found name
    //Is this problem too specific to the daily?

    Class.forName("org.sqlite.JDBC");

    Connection conn = null;


    try {
      conn = DriverManager.getConnection("jdbc:sqlite:"+args[0]);
      Statement statement = conn.createStatement();

      ResultSet results = statement.executeQuery("SELECT byline, count(*) FROM daily GROUP BY byline ORDER BY count(*) DESC;");


      //TODO Is it worth getting the db row count to initialize this?
      LinkedHashMap<String,Integer> hash = new LinkedHashMap<String,Integer>();

      //Fill a map with every byline and it's count.
      //Note that it's ordered, so the highest count is first.
      while (results.next()) {
        hash.put(results.getString("byline"),results.getInt("count(*)"));
      }

      HashSet<String> seen = new HashSet<String>(hash.size());
      String temp;
      String name;
      int count;

      Iterator<String> outer = hash.keySet().iterator();
      while (outer.hasNext())
      {
        count = 0;
        name = outer.next();
        StringBuffer output = new StringBuffer();

        if (!seen.contains(name)) {
          seen.add(name);

          Iterator<String> inner=hash.keySet().iterator();
          while(inner.hasNext()) {
            temp = inner.next();

            if (levenshtein.computeDistance(name,temp) < THRESHOLD) {
              count++;
              output.append("\""+temp+"\" , "+hash.get(temp)+"\n");
              seen.add(temp);
            }
          }

          if (count>1){
            System.out.println(output);
          }

        }
      }

    }
    catch (SQLException e) {
      System.err.println(e.getMessage());
    }
    
    finally {
      try {
        if (conn != null){
          conn.close();
        }
      } catch (SQLException e) {
        System.err.println(e);
      }
    }

  }
}
