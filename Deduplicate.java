import java.sql.*;
import java.util.LinkedHashMap;
import java.util.HashSet;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.ArrayList;

public class Deduplicate{
  public static void main(String[] args) throws ClassNotFoundException {

    //TODO parameterize
    final int THRESHOLD = 7;

    Levenshtein levenshtein = new Levenshtein(THRESHOLD);

    //TODO PostgreSQL has built-in Levenshtein function


    //TODO Get substring names first. But how do we prevent a dumb one from coming up? That problem's unique to Daily.
    //TODO After substring is solved, O(n^2) space is significantly reduced

    Class.forName("org.sqlite.JDBC");

    Connection conn = null;


    try {
      conn = DriverManager.getConnection("jdbc:sqlite:"+args[0]);

      Statement statement = conn.createStatement();
      ResultSet results = statement.executeQuery("Select byline, count(*) from daily group by byline order by count(*) desc;");

    //TODO Is it worth getting the row count from sqlite to initialize this?
      LinkedHashMap<String,Integer> hash = new LinkedHashMap<String,Integer>();

      //TODO This implementation is faster than removing entries from
      //HashMap but requires more space
      HashSet<String> seen = new HashSet<String>();

      while (results.next()) {
        hash.put(results.getString("byline"),results.getInt("count(*)"));
      }

      Iterator outer = hash.keySet().iterator();
      String name = "Tessa Gellerson";
      String max = null;
      ArrayList<String> matches = new ArrayList<String>();
      for (Iterator<String> iter = hash.keySet().iterator(); iter.hasNext(); ) {
        String temp = iter.next();

        if (levenshtein.computeDistance(name,temp) < THRESHOLD) {
          System.out.println("\""+temp+"\" , "+hash.get(temp));
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
