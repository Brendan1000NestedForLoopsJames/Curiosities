class Pi_Finder {
  public static void main(String[] args) {
    final int tests = 500000000;
    double iterations = 0;
    double hits = 0;
    for(int i = 0; i<tests; i++){
      iterations++;
      if(guess()){
        hits++;
      }
      System.out.println((hits/iterations) * 4);
    }
    System.out.println((hits/iterations)*4);
  }
  public static boolean guess(){
    double x = (Math.random()*2)-1;
    double y = (Math.random()*2)-1;
    return x*x+y*y<1;
  }
}
