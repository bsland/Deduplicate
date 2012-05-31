import java.lang.Math;

public class Levenshtein {

  private int threshold;

  public Levenshtein() {
    this.threshold = Integer.MAX_VALUE;
  }

  public Levenshtein(int threshold) {
    this.threshold = threshold;
  }

  public int computeDistance(String arg1, String arg2) {
    //Length call is O(1) in all common JVMs
    int len1 = arg1.length();
    int len2 = arg2.length();

    if (Math.abs(len1-len2) > threshold) {
      return Integer.MAX_VALUE;
    }

    //TODO Delete unneeded past rows
    int dists[][] = new int[len1+1][len2+1];

    for (int i=0;i<=len1;i++) {
      dists[i][0] = i;
    }
    for (int j=0;j<=len2;j++) {
      dists[0][j] = j;
    }

    //TODO Wikipedia's idea about only calculating within a diagonal stripe
    for (int i=1;i<=len1;i++){
      for (int j=1;j<=len2;j++){

        if ( Character.toLowerCase(arg1.charAt(i-1)) == Character.toLowerCase(arg2.charAt(j-1)) )  {
          dists[i][j] = dists[i-1][j-1];
        }
        else {
          dists[i][j] = Math.min(Math.min
            (dists[i-1][j]/*delete*/,
            dists[i][j-1]/*insert*/),
            dists[i-1][j-1] /*substitute*/
          )+1;
        }
      }
    }

    return dists[len1][len2];
  }
}
