import java.math.BigInteger;

class Simulator {
  static int one = 9;
  static int two = 23;
  static int three = 53;
  static int four = 65;
  static int five = 20;
  static int six = 10;
  static BigInteger bet = new BigInteger("1");
  static BigInteger spent = new BigInteger("0");
  static int prizes;
  public static void main(String[] args) {
    for(int index = 1; index <= 1005; index++){
    prizes = 1;
    double total = 0;
    int roundnumber = 0;
    spent = new BigInteger("0");
    bet = new BigInteger("1");
    while(total<10){
      total += round();
      spent=spent.add(bet);
      roundnumber++;
    }
    //System.out.print("Round "+index);
    System.out.print(" "+spent);
    //System.out.println("Number of rounds: "+roundnumber);
    //System.out.println("Number of prizes won: "+prizes);
    System.out.print("~"+spent.divide(new BigInteger(""+prizes)));;
    }
  }
  public static int roll(){
    int rand = (int)(Math.random()*(one+two+three+four+five+six));
    if(rand<one){
      one--;
      return 1;
    }
    else if(rand<one+two){
      two--;
      return 2;
    }
    else if(rand<one+two+three){
      two--;
      return 3;
    }
    else if(rand<one+two+three+four){
      two--;
      return 4;
    }
    else if(rand<one+two+three+four+five){
      two--;
      return 5;
    }
    else {
      six--;
      return 6;
    }
  }
  public static double score(int result){
    switch(result){
      case 8:  return 10;
      case 9:  return 8;
      case 10: return 5;
      case 11: return 5;
      case 12: return 5;
      case 13: return 5;
      case 14: return 1.5;
      case 15: return 1.5;
      case 16: return 0.5;
      case 17: return 0.5;
      case 29: bet=bet.add(new BigInteger("1"));
      prizes++;
      return 0;
      case 39: return 0.5;
      case 40: return 0.5;
      case 41: return 1.5;
      case 42: return 1.5;
      case 43: return 5;
      case 44: return 5;
      case 45: return 5;
      case 46: return 5;
      case 47: return 8;
      case 48: return 10;
    }
    return 0;
  }
  public static double round(){
  one = 9;
  two = 23;
  three = 53;
  four = 65;
  five = 20;
  six = 10;
  int sum = 0;
  for(int i=0; i<8; i++){
    sum+=roll();
  }
  return score(sum);
  }
}
