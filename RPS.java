public class RPS {

  public static final int maxRounds = 1000;

  public static int roundnumber = 0;

  // defines procedure for a match between 2 bots
  public static int match(Player bot1, Player bot2) {
    // Create lists to store histories
    int[] bot1Hist = new int[maxRounds];
    int[] bot2Hist = new int[maxRounds];
    int bot1Move;
    int bot2Move;
    int score = 0;
    for (int i = 0; i < maxRounds; i++) {
      // set the public roundnumber variable
      roundnumber = i;

      // get bot 1's move
      bot1Move = bot1.Play(java.util.Arrays.copyOfRange(bot1Hist, 0, i), java.util.Arrays.copyOfRange(bot2Hist, 0, i),
          roundnumber);
      // you can never be too safe
      roundnumber = i;
      // get bot 2's move
      bot2Move = bot2.Play(java.util.Arrays.copyOfRange(bot2Hist, 0, i), java.util.Arrays.copyOfRange(bot1Hist, 0, i),
          roundnumber);

      score += Score(bot1Move, bot2Move);

      bot1Hist[i] = bot1Move;
      bot2Hist[i] = bot2Move;
    }

    // print out both bot's histories
    boolean printout = false;
    if (printout) {
      String onestring = "";
      String twostring = "";
      for (int i = 0; i < maxRounds; i++) {
        onestring = onestring + bot1Hist[i] + ",";
        twostring = twostring + bot2Hist[i] + ",";
      }
      System.out.println(onestring);
      System.out.println(twostring);
    }
    return score;
  }

  public static int Score(int bot1, int bot2) {
    if (bot1 > 2 || bot1 < 0) {
      throw new IllegalArgumentException("Bot1 outputted a number that doesn't correspond to a move");
    }
    if (bot2 > 2 || bot2 < 0) {
      throw new IllegalArgumentException("Bot2 outputted a number that doesn't correspond to a move");
    }

    // check if bot 1 won the round
    if (bot1 == (bot2 + 1) % 3) {
      return 1;
    }
    // check if bot 2 won the round
    if ((bot1 + 1) % 3 == bot2) {
      return -1;
    }
    return 0;
  }

  static Player[] players = { 
    new Rockman(),
    new randomOptimal(), 
    new origami(), 
    new TSA(), 
    new rotato(),
    new debruiser(), 
    new handPicked(), 
    new piedPiper(), 
    new baitAndSwitch(), 
    new flatStanley(), 
    new sirSwitchALot(),
    new comfirmationBias(), 
    new shuffler(), 
    new sumPig(), 
    new foxtrot(), 
    new copyDrift(), 
    new addrift(),
    new addReact(), 
    new hindsight(), 
    new freqOfNature(), 
    new mountainsFromMolehills(), 
    new matchboxer(),
    new historian(), 
    new goliath() 
    };

  public static Player[] scramble(Player[] input){
    int[] numbers = new int[input.length];
    for(int i=0; i<numbers.length; i++){
      numbers[i]=i;
    }
    int pivot;
    int index;
    for(int i = numbers.length - 1; i > 0; i--){
      index =  (int)(Math.random()*i);
      pivot = numbers[i];
      numbers[i] = numbers[index];
      numbers[index] = pivot;
    }
    Player[] scrambled = new Player[input.length];
    for(int i = 0; i<input.length; i++){
      scrambled[i] = input[numbers[i]];
    }
    /*for(int i = 0; i<numbers.length; i++){
      System.out.println(scrambled[i].getClass().getSimpleName());
    }*/
    return scrambled;
  }

  public static String runBracket(Player[] bracket){
    int rounds = 0;
    for(int i = 1; i<bracket.length; i*=2){
      rounds++;
    }
    Player[][] bracketeer = new Player[rounds][];
    System.out.println("Players: "+bracket.length);
    bracketeer[0] = new Player[(int)Math.pow(2,rounds-1)];
    int pointer = 0;
    int score = 0;
    String eliminated = "Eliminated in round 1: ";
    for(int i = 0; i<bracket.length; i++){
      if(bracket.length-i==bracketeer[0].length-pointer){
        bracketeer[0][pointer]=bracket[i];
        pointer++;
      }
      else{
        score = match(bracket[i],bracket[i+1]);
        if(score>0){
          bracketeer[0][pointer++]=bracket[i++];
          eliminated+=bracket[i].getClass().getSimpleName()+",";
        }
        else{
          eliminated+=bracket[i].getClass().getSimpleName()+",";
          bracketeer[0][pointer++]=bracket[++i];
        }
      }
    }
    System.out.println(eliminated.substring(0,eliminated.length()-1));
    bracketeer[0] = scramble(bracketeer[0]);
    for(int i = 1; i<bracketeer.length; i++){
      bracketeer[i] = new Player[bracketeer[i-1].length/2];
      eliminated = "Eliminated in round "+(i+1)+": ";
      for(int j = 0; j<bracketeer[i].length; j++){
        score = match(bracketeer[i-1][j*2],bracketeer[i-1][j*2+1]);
        if(score>0){
          bracketeer[i][j]=bracketeer[i-1][j*2];
          eliminated+=bracketeer[i-1][j*2+1].getClass().getSimpleName()+",";
        }
        else{
          eliminated+=bracketeer[i-1][j*2].getClass().getSimpleName()+",";
          bracketeer[i][j]=bracketeer[i-1][j*2+1];
        }
      }
      System.out.println(eliminated.substring(0,eliminated.length()-1));
    }

    return "the winner is: "+bracketeer[bracketeer.length-1][0].getClass().getSimpleName();
  }

  public static void generateCrosstable() {
    int[][] scores = new int[players.length][players.length];

    for (int i = 0; i < players.length - 1; i++) {
      for (int j = i + 1; j < players.length; j++) {
        Player bot1 = players[i];
        Player bot2 = players[j];

        scores[i][j] = match(bot1, bot2);
        scores[j][i] = -scores[i][j];
      }
    }
    for (int i = 0; i < players.length; i++) {
      System.out.printf("%-16s | ", players[i].getClass().getSimpleName());
      int total = 0;
      for (int j = 0; j < players.length; j++) {
        System.out.printf("%5d ", scores[i][j]);
        total += scores[i][j];
      }
      System.out.printf(" | %6d\n", total);
    }
  }

  public static void main(String[] args) {
    System.out.println(runBracket(scramble(players)));
    //generateCrosstable();
  }
}

abstract class Player {
  // defines the words as the associated numbers
  public static final int rock = 0;
  public static final int paper = 1;
  public static final int scissors = 2;

  // a function for choosing randomly between rock paper and scissors with a given chance
  public static int WeightedRPS(double rockChance, double paperChance) {
    double rand = (Math.random());
    if (rand < rockChance) {
      return rock;
    } else if (rand < rockChance + paperChance) {
      return paper;
    } else {
      return scissors;
    }
  }

  public abstract int Play(int[] myhistory, int[] opphistory, int roundnumber);
}

// good ole rock
// always wins
class Rockman extends Player {
  public int Play(int[] myhistory, int[] opphistory, int roundnumber) {
    if (roundnumber > 0)
      opphistory[roundnumber - 1] = scissors;
    return rock;
  }
}

// chooses randomly
// can't lose
class randomOptimal extends Player {
  public int Play(int[] myhistory, int[] opphistory, int roundnumber) {
    return WeightedRPS(1.0 / 3.0, 1.0 / 3.0);
  }
}

// prefers paper
// has a 20,60,20 % chance for rock,paper,scissors respectively
class origami extends Player {
  public int Play(int[] myhistory, int[] opphistory, int roundnumber) {
    return WeightedRPS(0.2, 0.6);
  }
}

// no sharp objects allowed
// only picks rock or paper
class TSA extends Player {
  public int Play(int[] myhistory, int[] opphistory, int roundnumber) {
    return WeightedRPS(0.5, 0.5);
  }
}

// sponsored by the Richmond Night Market
// returns scissors,paper,rock in that order
class rotato extends Player {
  public int Play(int[] myhistory, int[] opphistory, int roundnumber) {
    return (roundnumber * 2) % 3;
  }
}

// a list of length 81 debrujun sequences
// stolen directly from Univercity of Alberta
class debruiser extends Player {
  public int Play(int[] myhistory, int[] opphistory, int roundnumber) {
    int[] db_table = new int[] {1,0,2,0,0,2,0,2,0,1,1,0,0,2,2,1,0,0,1,1,2,2,0,0,1,2,1,0,2,2,2,2,0,1,2,0,2,2,0,2,1,1,2,1,1,0,1,1,1,2,0,0,0,0,2,1,0,1,0,1,2,2,1,2,0,1,0, 0,0,1,0,2,1,2,1,2,2,2,1,1,1,0,0,1,1,1,1,0,1,0,2,2,2,0,0,2,2,0,2,0,1,0,1,1,0,2,1,1,2,2,2,2,1,1,1,2,0,1,2,2,1,2,0,0,0,1,0,0,0,0,2,0,2,2,1,0,0,1,2,1,2,2,0,1,1,2,1,1,0,0,2,1,0,1,2,0,2,1,2,1,0,2,1,1,2,0,0,1,0,1,2,2,0,1,0,0,2,0,1,2,0,1,1,2,1,1,1,1,0,2,0,2,1,0,2,2,0,2,2,2,2,0,0,0,1,2,1,2,2,2,1,1,0,1,1,0,0,0,0,2,1,2,0,2,0,0,2,2,1,0,0,1,1,1,2,2,1,2,1,0,1,0,2,1,0,1,0,2,0,2,0,0,1,2,2,2,0,2,1,0,0,1,1,1,2,2,1,1,0,2,2,0,0,0,2,2,2,2,1,2,2,0,1,2,0,0,2,0,1,1,2,1,2,1,1,1,1,0,0,2,1,2,0,1,0,0,0,0,1,0,1,1,0,1,2,1,0,2,1,1,2,0,2,2,2,2,1,1,1,1,0,0,2,0,2,2,2,1,2,1,0,2,1,0,0,0,0,2,1,1,2,2,1,0,1,0,0,1,1,1,2,1,1,0,1,2,2,2,2,0,0,1,2,0,2,0,1,2,1,2,0,1,0,1,1,2,0,0,0,1,0,2,2,0,2,1,2,2,0,1,1,0,2,0,0,0,0,0,0,1,0,0,0,2,1,1,0,0,1,1,1,1,0,2,0,2,1,2,0,2,2,1,2,2,2,1,1,1,2,1,2,1,0,0,2,0,1,1,0,1,0,2,1,0,2,2,2,2,0,2,0,0,2,2,0,0,1,2,2,1,0,1,1,2,0,1,2,1,1,2,2,0,1,0,1,2,2,2,0,2,0,0,2,0,2,1,2,2,2,2,0,0,0,0,2,2,1,0,0,0,1,2,0,1,2,1,2,0,0,1,0,2,0,1,0,0,2,1,0,1,2,2,1,1,2,0,2,2,2,1,2,1,0,2,2,0,1,1,0,2,1,1,0,0,1,1,2,1,1,1,1,0,1,0,1,1,1,1,1,1,2,1,0,0,0,0,1,1,0,2,1,2,1,2,2,2,0,0,1,2,0,1,0,1,2,1,1,2,2,0,2,0,2,1,1,0,0,1,0,2,0,0,2,0,1,1,2,0,2,2,1,1,1,1,0,1,0,0,2,2,2,2,1,2,0,0,0,2,1,0,2,2,0,1,2,2,1,0,2,1,0,1,0,1,1,1,1,2,1,1,0,1,2,1,2,2,2,2,1,2,0,0,0,1,1,2,0,2,0,2,1,0,0,0,0,2,0,0,1,0,0,2,2,2,0,0,2,1,1,2,2,0,1,2,0,1,1,0,0,1,2,2,1,1,1,0,2,0,1,0,2,2,0,2,2,1,0,2,1,2,2,2,1,0,1,0,2,2,1,2,0,2,1,0,2,0,0,0,0,1,2,1,0,0,2,0,2,2,0,1,0,1,1,2,1,1,0,0,1,0,0,0,2,1,1,2,0,0,2,2,2,2,0,0,1,1,1,0,2,1,2,1,2,2,1,1,1,1,2,2,0,2,0,1,2,0,1,1,0,1,1,0,0,1,1,0,1,2,0,1,2,1,2,2,1,0,0,2,0,2,1,0,1,0,2,2,0,1,1,2,1,0,2,0,0,1,0,1,1,1,2,2,2,2,1,2,0,2,2,1,1,2,0,0,2,1,2,1,1,1,1,0,2,1,1,0,0,0,0,2,2,2,0,0,0,1,2,2,0,2,0,2,2,0,2,1,1,2,0,2,0,0,1,1,1,0,0,1,2,1,1,0,1,1,0,2,2,0,0,2,2,1,1,1,1,2,1,2,1,0,2,0,2,2,2,2,1,2,0,0,0,1,0,0,2,1,0,0,0,0,2,0,1,1,2,2,2,0,1,2,2,1,0,1,0,1,2,0,1,0,2,1,0,2,0,2,1,1,1,0,0,2,2,2,0,1,1,2,2,1,2,0,0,0,1,0,1,2,1,0};
    return db_table[roundnumber];
  }
}

// I typed a list of 1000 moves manualy
// it took a while
class handPicked extends Player {
  public int Play(int[] myhistory, int[] opphistory, int roundnumber) {
    int[] b_table = new int[] {0,2,2,0,2,1,2,0,2,0,1,2,1,2,0,2,1,2,2,1,0,2,1,2,1,2,0,1,2,1,0,2,1,2,1,0,1,2,1,0,2,2,0,2,2,0,2,1,2,2,0,2,1,2,0,2,0,2,0,1,2,0,0,1,2,0,0,1,2,0,2,1,0,1,2,0,2,1,1,1,2,0,2,1,1,0,1,1,2,0,2,2,2,0,1,1,0,2,1,0,1,0,0,0,2,1,2,0,0,0,1,2,1,2,0,0,1,2,0,1,0,0,2,0,1,2,1,2,1,0,2,0,0,1,2,0,2,1,0,0,1,2,0,1,0,2,0,1,0,1,0,2,0,2,2,0,2,2,1,0,0,1,2,2,0,1,1,0,2,0,1,1,0,2,1,0,0,2,1,0,0,1,2,0,2,1,0,1,0,2,1,1,2,0,2,1,2,0,0,1,2,0,1,0,1,0,2,0,2,1,0,2,0,0,1,2,1,0,1,0,1,2,0,1,2,0,1,2,0,2,2,0,2,1,0,1,2,0,1,2,0,2,0,2,1,0,2,1,0,2,0,2,0,1,0,0,1,2,2,1,2,1,1,1,2,2,1,0,2,0,1,0,2,2,0,1,2,2,0,1,2,0,1,2,1,1,1,1,1,1,0,0,1,1,1,0,1,1,0,2,2,2,2,0,2,2,2,0,1,0,2,1,2,0,1,2,0,0,2,0,2,0,1,2,0,1,2,0,1,2,0,0,1,0,1,1,2,2,0,0,2,0,1,2,1,2,0,1,2,1,0,0,1,2,0,2,0,0,1,0,0,0,1,1,1,0,1,0,2,2,2,0,1,2,1,2,0,1,0,1,0,2,0,2,0,2,0,1,2,2,0,2,2,2,0,2,2,0,2,0,1,0,2,0,2,0,2,1,0,1,0,2,2,0,2,0,1,0,2,1,0,0,0,2,0,0,2,0,0,0,0,1,2,1,0,0,0,1,1,1,2,1,2,2,2,1,2,2,2,0,0,1,2,0,1,0,0,1,0,0,1,2,0,2,0,2,1,0,0,0,1,0,2,0,2,1,0,1,0,2,0,1,0,2,0,1,2,0,1,0,2,0,1,2,0,1,0,2,1,0,2,0,1,0,1,0,1,2,0,2,1,0,2,1,2,0,1,0,2,1,2,1,0,2,0,1,0,2,0,0,2,1,0,2,0,1,0,2,0,1,2,0,1,0,1,0,0,0,1,0,1,2,0,1,0,2,1,2,1,0,2,2,1,0,1,1,0,2,1,0,1,1,0,2,0,2,0,2,0,1,0,1,0,2,1,0,1,0,1,1,1,2,2,1,2,1,2,0,1,2,0,1,2,1,2,0,1,1,0,2,1,1,2,2,1,0,1,1,0,2,2,1,0,2,0,1,2,0,2,0,0,1,2,2,0,2,0,1,1,2,0,2,0,1,0,1,0,2,0,0,1,1,0,0,1,0,1,0,1,2,0,2,1,1,0,0,2,1,2,0,1,2,0,0,1,0,1,2,2,0,2,0,1,0,2,0,1,0,2,0,1,2,0,1,0,2,0,0,1,0,2,1,2,0,0,0,0,1,2,2,1,1,1,0,2,1,1,0,2,1,2,0,2,1,2,0,2,2,1,2,1,1,0,2,1,1,2,1,2,1,1,0,1,1,2,1,0,2,1,0,2,1,1,0,1,2,1,0,1,1,2,0,1,2,0,1,2,1,2,0,1,2,1,1,2,1,0,1,2,2,0,1,1,2,1,2,0,2,1,1,2,0,1,2,1,1,2,0,2,1,1,2,1,2,0,1,2,1,2,2,1,0,2,2,0,2,2,2,1,1,1,0,2,1,2,1,0,1,2,0,1,0,1,2,1,0,1,2,0,1,2,2,2,0,1,0,2,1,0,2,1,0,1,0,2,1,1,0,2,2,2,1,0,2,1,0,1,2,0,2,1,0,2,0,1,2,0,2,0,1,2,0,1,0,2,1,0,2,0,2,0,2,0,1,0,0,2,1,0,2,0,2,0,2,0,2,2,1,1,1,2,1,2,1,2,2,1,2,0,2,0,1,0,2,0,1,0,1,0,1,0,1,0,1,0,2,0,2,2,2,0,1,2,2,0,2,0,2,0,2,0,1,0,1,2,1,2,2,1,2,1,2,0,1,2,0,2,1,0,2,0,2,0,2,0,0,0,0,0,0,1,1,1,1,2,2,2,0,1,0,2,0,1,0,1,2,0,2,1,2};
    return b_table[roundnumber];
  }
}

// moves based on digits of pi in ternery
// is it better than pure randomness?
class piedPiper extends Player {
  public int Play(int[] myhistory, int[] opphistory, int roundnumber) {
    int[] pi = new int[] {1,0,0,1,0,2,1,1,0,1,2,2,2,2,0,1,0,2,1,1,0,0,2,1,1,1,1,1,0,2,2,1,2,2,2,2,0,2,2,2,1,1,0,1,0,0,2,1,0,2,0,1,0,2,1,2,0,0,1,2,1,2,1,1,2,2,1,2,0,0,2,0,0,1,1,1,0,0,2,1,2,1,2,2,2,1,0,0,0,1,1,0,2,1,1,0,2,1,0,0,1,2,1,0,2,0,1,1,2,1,0,1,2,2,1,1,1,1,0,1,0,0,2,1,2,0,1,1,1,1,2,1,2,0,0,1,0,0,1,1,1,2,2,0,1,0,0,1,2,0,2,2,2,1,2,2,2,1,1,2,2,1,0,0,1,1,0,0,2,0,0,2,2,2,1,0,0,2,0,0,0,2,2,2,0,0,0,0,2,1,0,1,0,2,1,1,2,2,2,2,1,0,2,2,0,2,1,2,1,0,0,1,1,1,1,1,2,2,0,1,2,0,2,1,1,1,1,2,0,2,0,2,2,1,0,1,1,1,0,1,2,1,1,0,2,2,0,1,2,0,1,0,2,2,0,0,1,1,0,1,2,2,1,1,0,0,2,1,0,2,1,1,0,2,0,0,2,0,2,2,1,2,2,0,2,2,0,0,1,0,1,0,2,0,1,1,1,0,0,0,2,2,2,2,2,2,0,2,0,1,0,0,1,2,1,2,1,0,0,2,2,0,0,0,1,0,1,2,2,0,1,1,2,1,1,2,2,1,1,2,0,0,0,2,1,2,2,0,1,1,0,2,2,1,1,0,1,1,0,0,2,2,0,0,2,2,0,2,1,1,2,0,1,2,0,2,0,2,0,1,1,0,0,2,1,1,1,2,1,0,2,1,2,1,2,1,0,1,1,1,1,2,0,0,2,1,2,2,2,1,0,2,2,1,0,1,1,2,2,0,2,1,0,1,2,0,1,2,2,2,0,2,0,2,0,0,2,1,1,0,0,0,0,0,1,2,2,1,1,2,2,0,1,2,2,2,2,2,2,2,1,0,1,0,1,2,2,1,2,1,1,2,2,2,0,1,1,2,1,0,1,2,2,0,2,1,2,2,1,2,2,1,2,0,0,2,0,0,2,0,1,2,0,2,1,0,1,1,1,2,1,2,2,2,2,1,0,0,1,1,2,2,1,1,2,2,2,1,2,0,2,0,1,0,0,2,0,1,2,0,2,0,2,2,1,1,1,0,2,2,1,2,2,2,1,1,2,2,1,1,2,0,2,2,1,2,0,0,0,1,2,1,1,2,2,1,0,1,2,2,2,1,0,2,1,2,1,0,1,0,2,2,2,0,1,0,0,0,0,2,1,0,2,2,0,2,0,1,2,1,1,2,2,2,2,0,2,0,2,1,1,2,1,1,0,0,2,1,0,1,1,2,0,1,2,2,1,1,1,2,1,2,0,1,1,1,2,2,2,0,1,0,0,0,0,0,1,2,2,2,0,1,1,1,0,0,2,2,0,1,1,1,2,0,1,1,1,0,0,2,1,1,2,1,0,2,1,2,1,2,0,1,2,1,0,0,2,2,2,0,0,2,2,1,2,0,2,0,2,1,1,0,0,0,2,1,2,1,2,0,0,2,2,0,1,1,1,1,2,1,1,1,0,2,2,2,0,0,2,0,0,1,1,1,2,1,1,1,0,0,1,1,1,1,1,0,0,0,1,0,2,1,2,1,1,1,0,0,0,1,2,1,2,0,1,1,0,0,1,2,1,0,2,0,1,1,0,2,0,2,1,0,0,1,0,2,2,0,2,2,2,0,1,2,0,2,2,2,0,2,2,2,1,0,0,1,2,2,0,2,1,1,2,0,1,1,2,1,2,1,2,2,1,2,2,0,2,2,0,1,0,0,0,0,2,2,2,0,2,2,1,0,1,0,0,1,1,0,1,2,2,1,0,2,1,1,0,1,0,2,0,1,0,1,0,0,0,2,1,2,0,2,2,1,1,2,1,1,1,0,1,1,2,0,2,0,2,0,1,2,2,0,2,0,1,0,0,0,1,1,2,2,1,0,2,1,2,2,0,2,0,0,0,2,0,0,2,0,1,0,0,1,1,2,1,1,0,1,2,2,0,0,1,1,0,0,2,0,1,2,2,0,1,1,0,1,0,0,2,1,0,1,1,0,2,1,0,1,1,2,1,1,2,2,2,0,0,0,0,2,0,0,1,2,2,2,1,1,0,0,0,2,1,1,0,0,1,1,1,2};
    return (pi[roundnumber] % 3);
  }
}

// does 2 different moves 500 times each
// move A 500 times then move B 500 times
class baitAndSwitch extends Player {
  static int moveA, moveB;

  public int Play(int[] myhistory, int[] opphistory, int roundnumber) {
    if (roundnumber == 0) {
      moveA = WeightedRPS(1.0 / 3.0, 1.0 / 3.0);
      moveB = (moveA + WeightedRPS(0, 0.5)) % 3;
    }
    if (roundnumber < 500) {
      return moveA;
    }
    return moveB;
  }
}

// tries to do every move even amounts
// more likely to choose moves that it has done less often and vice versa
class flatStanley extends Player {
  static int flatrock;
  static int flatpaper;
  static int flatscissors;

  public int Play(int[] myhistory, int[] opphistory, int roundnumber) {
    if (roundnumber == 0) {
      flatrock = 0;
      flatpaper = 0;
      flatscissors = 0;
      return paper;
    }
    if (myhistory[roundnumber - 1] == 0) {
      flatrock++;
    } else if (myhistory[roundnumber - 1] == 1) {
      flatpaper++;
    } else {
      flatscissors++;
    }
    if (flatrock > flatpaper && flatrock > flatscissors) {
      if (flatscissors == flatpaper) {
        return WeightedRPS(0.2, 0.4);
      } else if (flatpaper > flatscissors) {
        return WeightedRPS(0.2, 0.3);
      } else {
        return WeightedRPS(0.2, 0.5);
      }
    } else if (flatpaper > flatrock && flatpaper > flatscissors) {
      if (flatrock == flatscissors) {
        return WeightedRPS(0.4, 0.2);
      } else if (flatrock > flatscissors) {
        return WeightedRPS(0.3, 0.2);
      } else {
        return WeightedRPS(0.5, 0.2);
      }
    } else if (flatscissors > flatrock && flatscissors > flatpaper) {
      if (flatrock == flatpaper) {
        return WeightedRPS(0.4, 0.4);
      } else if (flatrock > flatpaper) {
        return WeightedRPS(0.3, 0.5);
      } else {
        return WeightedRPS(0.3, 0.5);
      }
    } else if (flatrock < flatpaper && flatrock < flatscissors) {
      return WeightedRPS(0.5, 0.25);
    } else if (flatpaper < flatrock && flatpaper < flatscissors) {
      return WeightedRPS(0.25, 0.5);
    } else if (flatscissors < flatrock && flatscissors < flatpaper) {
      return WeightedRPS(0.25, 0.25);
    } else {
      return WeightedRPS(1.0 / 3.0, 1.0 / 3.0);
    }
  }
}

// Tends not to choose the same move twice in a row
// makes it less predictable obviously
class sirSwitchALot extends Player {
  public int Play(int[] myhistory, int[] opphistory, int roundnumber) {
    if (roundnumber == 0) {
      return WeightedRPS(1.0 / 3.0, 1.0 / 3.0);
    } else {
      return (WeightedRPS(0.12, 0.44) + myhistory[roundnumber - 1]) % 3;
    }
  }
}

// tends to repeat a move on a win and switch on a loss, random move on a tie
// like people do
class comfirmationBias extends Player {
  public int Play(int[] myhistory, int[] opphistory, int roundnumber) {
    if (roundnumber == 0 || myhistory[roundnumber - 1] == opphistory[roundnumber - 1]) {
      return WeightedRPS(1.0 / 3.0, 1.0 / 3.0);
    }
    if ((myhistory[roundnumber - 1] + 1) % 3 == opphistory[roundnumber - 1]) {
      return (WeightedRPS(0.12, 0.44) + myhistory[roundnumber - 1]) % 3;
    } else if (myhistory[roundnumber - 1] == (opphistory[roundnumber - 1] + 1) % 3) {
      return (WeightedRPS(0.76, 0.12) + myhistory[roundnumber - 1]) % 3;
    } else {
      return WeightedRPS(1.0 / 3.0, 1.0 / 3.0);
    }
  }
}

// every 3 rounds returns rock paper and scissors in a random order
// ie. r,s,p , p,s,r , s,r,p...
class shuffler extends Player {
  public int Play(int[] myhistory, int[] opphistory, int roundnumber) {
    if (roundnumber % 3 == 0) {
      return WeightedRPS(1.0 / 3.0, 1.0 / 3.0);
    } else if (roundnumber % 3 == 1) {
      return (WeightedRPS(0, 0.5) + myhistory[roundnumber - 1]) % 3;
    } else {
      if (myhistory[roundnumber - 1] + myhistory[roundnumber - 2] == 1) {
        return scissors;
      } else if (myhistory[roundnumber - 1] + myhistory[roundnumber - 2] == 2) {
        return paper;
      } else {
        return rock;
      }
    }
  }
}

// returns the sum of the previous two moves %3
// I think this is the worst pun of the bunch
class sumPig extends Player {
  public int Play(int[] myhistory, int[] opphistory, int roundnumber) {
    if (roundnumber == 0) {
      return WeightedRPS(1.0 / 3.0, 1.0 / 3.0);
    } else {
      return (myhistory[roundnumber - 1] + opphistory[roundnumber - 1]) % 3;
    }
  }
}

// returns rand,prev_move+2,rand,prev_move+1,rand,prev_move
// only predictable half of the time
class foxtrot extends Player {
  public int Play(int[] myhistory, int[] opphistory, int roundnumber) {
    if (roundnumber % 2 == 0) {
      return WeightedRPS(1.0 / 3.0, 1.0 / 3.0);
    }
    return (myhistory[roundnumber - 1] + roundnumber + 1) % 3;
  }
}

// half of the time chooses randomly
// other half throws opponent's last move + roundnumber/111
class copyDrift extends Player {
  public int Play(int[] myhistory, int[] opphistory, int roundnumber) {
    if (roundnumber == 0) {
      return WeightedRPS(1.0 / 3.0, 1.0 / 3.0);
    }
    if (Math.random() > 0.5) {
      return (opphistory[roundnumber - 1] + roundnumber / 111) % 3;
    } else {
      return WeightedRPS(1.0 / 3.0, 1.0 / 3.0);
    }
  }
}

// as copy drift but adds previous moves from both players
// and gear only changes every 200 rounds
class addrift extends Player {
  public int Play(int[] myhistory, int[] opphistory, int roundnumber) {
    if (roundnumber == 0) {
      return WeightedRPS(1.0 / 3.0, 1.0 / 3.0);
    }
    if (Math.random() > 0.5) {
      return (opphistory[roundnumber - 1] + myhistory[roundnumber - 1] + roundnumber / 200) % 3;
    } else {
      return WeightedRPS(1.0 / 3.0, 1.0 / 3.0);
    }
  }
}

// usually returns the sum of the two previous moves
// plus a gear that shifts when the bot has lost 10 out of the last 20 rounds
class addReact extends Player {
  static int artimer;
  static int arlosscount;
  static int argear;

  public int Play(int[] myhistory, int[] opphistory, int roundnumber) {
    if (roundnumber == 0) {
      artimer = 0;
      arlosscount = 0;
      argear = 0;
      return WeightedRPS(1.0 / 3.0, 1.0 / 3.0);
    }
    if ((myhistory[roundnumber - 1] + 1) % 3 == opphistory[roundnumber - 1]) {
      arlosscount++;
    }
    if (roundnumber > 20 && (myhistory[roundnumber - 20] + 1) % 3 == opphistory[roundnumber - 20]) {
      arlosscount--;
    }
    if (artimer < 20) {
      artimer++;
    } else {
      if (arlosscount > 10) {
        argear++;
        artimer = 0;
      }
    }
    return (opphistory[roundnumber - 1] + myhistory[roundnumber - 1] + argear + WeightedRPS(0.8, 0.1)) % 3;
  }
}

// beats opponent's last move
// 20/20 strategy
class hindsight extends Player {
  public int Play(int[] myhistory, int[] opphistory, int roundnumber) {
    if (roundnumber == 0) {
      return WeightedRPS(1.0 / 3.0, 1.0 / 3.0);
    } else {
      return (opphistory[roundnumber - 1] + 1) % 3;
    }
  }
}

// beat what your opponent has done the most
// 100% win rate against rockman
class freqOfNature extends Player {
  int freqrock = 0;
  int freqpaper = 0;
  int freqscissors = 0;

  public int Play(int[] myhistory, int[] opphistory, int roundnumber) {
    if (roundnumber == 0) {
      freqrock = 0;
      freqpaper = 0;
      freqscissors = 0;
      return WeightedRPS(1.0 / 3.0, 1.0 / 3.0);
    }
    if (opphistory[roundnumber - 1] == 0) {
      freqrock++;
    } else if (opphistory[roundnumber - 1] == 1) {
      freqpaper++;
    } else {
      freqscissors++;
    }
    if (freqrock > freqscissors && freqrock > (freqpaper - 1)) {
      return paper;
    }
    if (freqpaper > freqrock && freqpaper > (freqscissors - 1)) {
      return scissors;
    }
    if (freqscissors > freqpaper && freqscissors > (freqrock - 1)) {
      return rock;
    }
    return WeightedRPS(1.0 / 3.0, 1.0 / 3.0);
  }
}

// beat what your opponent has done the least
// tries to lose???
class mountainsFromMolehills extends Player {
  public static int antiflatrock = 0;
  public static int antiflatpaper = 0;
  public static int antiflatscissors = 0;

  public int Play(int[] myhistory, int[] opphistory, int roundnumber) {
    if (roundnumber == 0) {
      antiflatrock = 0;
      antiflatpaper = 0;
      antiflatscissors = 0;
      return WeightedRPS(1.0 / 3.0, 1.0 / 3.0);
    }
    if (opphistory[roundnumber - 1] == 0) {
      antiflatrock++;
    } else if (opphistory[roundnumber - 1] == 1) {
      antiflatpaper++;
    } else {
      antiflatscissors++;
    }
    if (antiflatrock < antiflatscissors && antiflatrock < (antiflatpaper + 1)) {
      return paper;
    }
    if (antiflatpaper < antiflatrock && antiflatpaper < (antiflatscissors + 1)) {
      return scissors;
    }
    if (antiflatscissors < antiflatpaper && antiflatscissors < (antiflatrock + 1)) {
      return rock;
    }
    return WeightedRPS(1.0 / 3.0, 1.0 / 3.0);
  }
}

// chooses by pulling a bead out of a matchbox
// adds a copy of the bead if it ties, 3 copies if it wins and removes 2 copies if it loses
class matchboxer extends Player {
  static double rockbeads = 30;
  static double paperbeads = 30;
  static double scissorsbeads = 30;

  public int Play(int[] myhistory, int[] opphistory, int roundnumber) {
    if (roundnumber == 0) {
      rockbeads = 30;
      paperbeads = 30;
      scissorsbeads = 30;
      return WeightedRPS(1.0 / 3.0, 1.0 / 3.0);
    }
    if (myhistory[roundnumber - 1] == opphistory[roundnumber - 1]) {
      if (myhistory[roundnumber - 1] == 0) {
        rockbeads += 1;
      } else if (myhistory[roundnumber - 1] == 1) {
        paperbeads += 1;
      } else {
        scissorsbeads += 1;
      }
    } else if ((myhistory[roundnumber - 1] - opphistory[roundnumber - 1] + 3) % 3 == 1) {
      if (myhistory[roundnumber - 1] == 0) {
        rockbeads += 3;
      } else if (myhistory[roundnumber - 1] == 1) {
        paperbeads += 3;
      } else {
        scissorsbeads += 3;
      }
    } else {
      if (myhistory[roundnumber - 1] == 0) {
        rockbeads -= 2;
      } else if (myhistory[roundnumber - 1] == 1) {
        paperbeads -= 2;
      } else {
        scissorsbeads -= 2;
      }
    }
    if (rockbeads < 0) {
      rockbeads = 0;
    }
    if (paperbeads < 0) {
      paperbeads = 0;
    }
    if (scissorsbeads < 0) {
      scissorsbeads = 0;
    }
    if (rockbeads + paperbeads + scissorsbeads == 0) {
      rockbeads = 30;
      paperbeads = 30;
      scissorsbeads = 30;
      return WeightedRPS(1.0 / 3.0, 1.0 / 3.0);
    }
    return WeightedRPS(rockbeads / (rockbeads + paperbeads + scissorsbeads),
        paperbeads / (rockbeads + paperbeads + scissorsbeads));
  }
}

// those who fail to learn from history are doomed to repeat it
// those who repeat history are predictable
class historian extends Player {
  private static String oppStringyee;
  private static String oppStringye;

  public int Play(int[] myhistory, int[] opphistory, int roundnumber) {
    if (roundnumber == 0) {
      oppStringyee = "";
      oppStringye = "";
      return WeightedRPS(1.0 / 3.0, 1.0 / 3.0);
    }
    if (roundnumber > 1) {
      oppStringye += opphistory[roundnumber - 2];
    }
    oppStringyee += opphistory[roundnumber - 1];
    for (int length = roundnumber - 1; length > 0; length--) {
      if (oppStringye.contains(oppStringyee.substring(roundnumber - length))) {
        return (opphistory[oppStringye.lastIndexOf(oppStringyee.substring(roundnumber - length)) + length] + 1) % 3;
      }
    }
    return WeightedRPS(1.0 / 3.0, 1.0 / 3.0);
  }
}

class goliath extends Player {
  // based primarily off of the strategies of the best programs from the first tournament by Darce Billings
  // A 117 semicolon monstrosity
  private static final int msMaxSize = 25;
  private static int round;
  // a list of all the moves all the strategies would've made
  private static int[][][] msFull = new int[msMaxSize][1000][4];
  // a list of how all of the strategies would've scored
  private static int[][][] msScores = new int[msMaxSize][4][3];
  // an array consisting of the opponent's first move, then the bot's first move,
  // then the opponent's second move, and so on
  private static int[] weaveSelf = new int[2000];
  // as weaveSelf but the order between the opponent's moves and the bot's moves
  // are reversed
  private static int[] weaveOpponent = new int[2000];

  public int Play(int[] myhistory, int[] opphistory, int roundnumber) {
    round = roundnumber;
    // for the first round choose a random move and set each of the strat's first
    // moves to that choice
    // then returns that move as well
    // also zeroes out the Scores array
    if (roundnumber == 0) {
      msFull[0][0][0] = WeightedRPS(1.0 / 3.0, 1.0 / 3.0);
      for (int i = 1; i < msMaxSize; i++) {
        for (int j = 0; j < 4; j++) {
          msFull[i][0][j] = msFull[0][0][0];
        }
      }
      for (int i = 0; i < msMaxSize; i++) {
        for (int j = 0; j < 4; j++) {
          for (int k = 0; k < 3; k++) {
            msScores[i][j][k] = 0;
          }
        }
      }
      return msFull[0][0][0];
    }
    // increment/decrement the score of each strategies depending on how the last round played out
    // then checks if the strategy has the best score so far, and records it if so
    int[] best = new int[] { -999, 0, 0, 0 };
    for (int i = 0; i < msMaxSize; i++) {
      for (int j = 0; j < 4; j++) {
        if (msFull[i][roundnumber - 1][j] == (opphistory[roundnumber - 1] + 1) % 3) {
          msScores[i][j][0]++;
          msScores[i][j][1]--;
        } else if ((msFull[i][roundnumber - 1][j] + 1) % 3 == opphistory[roundnumber - 1]) {
          msScores[i][j][0]--;
          msScores[i][j][2]++;
        } else {
          msScores[i][j][2]--;
          msScores[i][j][1]++;
        }
        if (msScores[i][j][0] >= best[0]) {
          best[0] = msScores[i][j][0];
          best[1] = i;
          best[2] = j;
          best[3] = 0;
        }
        if (msScores[i][j][1] >= best[0]) {
          best[0] = msScores[i][j][1];
          best[1] = i;
          best[2] = j;
          best[3] = 1;
        }
        if (msScores[i][j][2] >= best[0]) {
          best[0] = msScores[i][j][2];
          best[1] = i;
          best[2] = j;
          best[3] = 2;
        }
      }
    }

    // generate the 2 variant arrays
    weaveSelf[(roundnumber - 1) * 2] = opphistory[roundnumber - 1];
    weaveOpponent[(roundnumber - 1) * 2] = myhistory[roundnumber - 1];
    weaveSelf[(roundnumber - 1) * 2 + 1] = myhistory[roundnumber - 1];
    weaveOpponent[(roundnumber - 1) * 2 + 1] = opphistory[roundnumber - 1];

    // fills the 2 random sections
    for (int i = 0; i < 2; i++) {
      msFull[0][roundnumber][i] = WeightedRPS(1.0 / 3.0, 1.0 / 3.0);
    }
    // and the 2 last move copies
    msFull[0][roundnumber][3] = opphistory[roundnumber - 1];
    msFull[0][roundnumber][3] = myhistory[roundnumber - 1];

    // fills the 4 frequency sections
    // against opphistory
    int[] mscounts = new int[] { 0, 0, 0 };
    int[] mschoose = new int[] { 0, 0, 0 };
    // counts the moves used by the opponent
    for (int i = 0; i < opphistory.length; i++) {
      mscounts[opphistory[i]]++;
    }
    // figures out which move is optimal based on what move would score best against the total count
    mschoose[0] = mscounts[2] - mscounts[1];
    mschoose[1] = mscounts[0] - mscounts[2];
    mschoose[2] = mscounts[1] - mscounts[0];
    if (mschoose[0] > mschoose[2] && mschoose[0] > mschoose[1]) {
      msFull[1][roundnumber][0] = 0;
    } else if (mschoose[1] > mschoose[0] && mschoose[1] > mschoose[2]) {
      msFull[1][roundnumber][0] = 1;
    } else if (mschoose[2] > mschoose[1] && mschoose[2] > mschoose[0]) {
      msFull[1][roundnumber][0] = 2;
    } else {
      msFull[1][roundnumber][0] = WeightedRPS(1.0 / 3.0, 1.0 / 3.0);
    }

    mscounts[0] = 0;
    mscounts[1] = 0;
    mscounts[2] = 0;

    // freq against myhistory
    for (int i = 0; i < myhistory.length; i++) {
      mscounts[myhistory[i]]++;
    }
    mschoose[0] = mscounts[2] - mscounts[1];
    mschoose[1] = mscounts[0] - mscounts[2];
    mschoose[2] = mscounts[1] - mscounts[0];
    if (mschoose[0] > mschoose[2] && mschoose[0] > mschoose[1]) {
      msFull[1][roundnumber][1] = 0;
    } else if (mschoose[1] > mschoose[0] && mschoose[1] > mschoose[2]) {
      msFull[1][roundnumber][1] = 1;
    } else if (mschoose[2] > mschoose[1] && mschoose[2] > mschoose[0]) {
      msFull[1][roundnumber][1] = 2;
    } else {
      msFull[1][roundnumber][1] = WeightedRPS(1.0 / 3.0, 1.0 / 3.0);
    }

    mscounts[0] = 0;
    mscounts[1] = 0;
    mscounts[2] = 0;

    // freq against the weaved array
    for (int i = 0; i < weaveSelf.length; i++) {
      mscounts[weaveSelf[i]]++;
    }

    mschoose[0] = mscounts[2] - mscounts[1];
    mschoose[1] = mscounts[0] - mscounts[2];
    mschoose[2] = mscounts[1] - mscounts[0];

    if (mschoose[0] > mschoose[2] && mschoose[0] > mschoose[1]) {
      msFull[1][roundnumber][2] = 0;
      msFull[1][roundnumber][3] = 0;
    } else if (mschoose[1] > mschoose[0] && mschoose[1] > mschoose[2]) {
      msFull[1][roundnumber][2] = 1;
      msFull[1][roundnumber][3] = 1;
    } else if (mschoose[2] > mschoose[1] && mschoose[2] > mschoose[0]) {
      msFull[1][roundnumber][2] = 2;
      msFull[1][roundnumber][3] = 2;
    } else {
      msFull[1][roundnumber][2] = WeightedRPS(1.0 / 3.0, 1.0 / 3.0);
      msFull[1][roundnumber][3] = WeightedRPS(1.0 / 3.0, 1.0 / 3.0);
    }

    // runs the chain method for all of the lengths and all of the kinds of arrays
    for (int i = 2; i < msMaxSize; i++) {
      msFull[i][roundnumber][0] = MSchain(i - 1, java.util.Arrays.copyOfRange(opphistory, 0, roundnumber), 0);
      msFull[i][roundnumber][1] = MSchain(i - 1, java.util.Arrays.copyOfRange(myhistory, 0, roundnumber), 1);
      msFull[i][roundnumber][2] = MSchain(i - 1, java.util.Arrays.copyOfRange(weaveSelf, 0, roundnumber * 2), 2);
      msFull[i][roundnumber][3] = MSchain(i - 1, java.util.Arrays.copyOfRange(weaveOpponent, 0, roundnumber * 2), 3);
    }
    // outputs the best strategy as determined earlier
    return (msFull[best[1]][roundnumber][best[2]] + best[3]) % 3;
  }

  // given an array and a length, finds all points in the array where the last n characters occured, where n is equal to the given length
  // then figures out what move is most likely to come after that given last n moves
  // returns the value that beats that
  private static int MSchain(int length, int[] array, int location) {
    // if the array is too short to examine, returns the move that the strategy with
    // 1 less length
    if (array.length < length * 2) {
      return msFull[length][round][location];
    }
    int[] find = java.util.Arrays.copyOfRange(array, array.length - length, array.length);
    boolean flag;
    int[] countChain = { 0, 0, 0 };
    for (int i = 0; i < array.length - length; i++) {
      flag = true;
      for (int j = 0; j < length; j++) {
        if (array[i + j] != find[j]) {
          flag = false;
          break;
        }
      }
      if (flag) {
        countChain[array[i + length]]++;
      }
    }
    int[] chooseMove = { countChain[2] - countChain[1], countChain[0] - countChain[2], countChain[1] - countChain[0] };

    if (chooseMove[0] > chooseMove[2] && chooseMove[0] > chooseMove[1]) {
      return 0;
    } else if (chooseMove[1] > chooseMove[0] && chooseMove[1] > chooseMove[2]) {
      return 1;
    } else if (chooseMove[2] > chooseMove[1] && chooseMove[2] > chooseMove[0]) {
      return 2;
    }
    // also returns the move of one less length if all moves are determined to be equally good
    return msFull[length][round][location];
  }
}
