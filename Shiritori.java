import java.util.*;
import java.io.*;

public class Shiritori
{
	public static String[] dict = new String[172823];
	public static ArrayList<String> used = new ArrayList<String>();
	public static int score = 0;
	public static double points = 0.0;
	public static long time;
	public static String word; //the current word to compare to
	public static int level = -1;
	public static double cutOff = 50.0;
	public static String name;
	public static boolean fin = false;
	public static boolean hell = false;
	
	public static void main(String[] args) throws IOException
	{
		//input the dictionary
		BufferedReader d = new BufferedReader(new FileReader("fulllist.txt"));
		for (int i = 0; i < 172823; i++)
		{
			dict[i] = d.readLine();
		}
		Scanner sc = new Scanner(System.in);
		System.out.println("Welcome to Shiritori!");
		try{Thread.sleep(500);} catch (InterruptedException e){e.printStackTrace();}
		System.out.println("Rules: Type a word that starts with the last letter of the previous word.");
		try{Thread.sleep(500);} catch (InterruptedException e){e.printStackTrace();}
		System.out.println("For example: CPU: \"game\" --> Player: \"end\"");
		try{Thread.sleep(500);} catch (InterruptedException e){e.printStackTrace();}
		System.out.println("You cannot repeat a word that has been said before.");
		try{Thread.sleep(500);} catch (InterruptedException e){e.printStackTrace();}
		System.out.println("The longer the word, the more points it's worth.");
		try{Thread.sleep(500);} catch (InterruptedException e){e.printStackTrace();}
		System.out.print("Choose a starting level (1-10): ");
		boolean c = false;
		while (!c)
		{
			while (!sc.hasNextInt())
			{
				System.out.print("Not a valid level. Try again: ");
				sc.next();
			}
			int med = sc.nextInt();
			if (med < 1 || med > 10)
			{
				System.out.print("Not a valid level. Try again: ");
			}
			else
			{
				c = true;
				level = med;
			}
		}
		System.out.println("You start out with " + (int) Math.round((10000 - (777*(level-1))) / 1000) + "s per word, but levelling up shortens that time.");
		System.out.print("Input your name (under 5 letters): ");
		name = sc.next();
		while (name.length() > 5)
		{
			System.out.println("Too long, pick another name.");
			System.out.print("Name: ");
			name = sc.next();
		}
		if (name.toUpperCase().equals("HELL"))
		{
			System.out.println("Hard mode activated.");
			hell = true;
		}
		System.out.println("Level: " + level + ". Type any word to begin the game.");
		System.out.print("Player: ");
		word = sc.next().toUpperCase();
		while (!dictCheck(word))
		{
			word = sc.next().toUpperCase();
		}
		
		Thread inputThread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				while (true)
				{
					if (System.currentTimeMillis() - time > 10000 - (777*(level-1)))
					{
						fin = true;
						System.out.println();
						System.out.println("Times up! Thanks for playing.");
						System.out.println("Words Played: " + score + "  Score: " + Math.round(points));
						try
						{
							try{Thread.sleep(300);} catch (InterruptedException e){e.printStackTrace();}
							highScore();
						} catch (IOException e)
						{
							e.printStackTrace();
						}
						System.exit(0);
					}
					try{Thread.sleep(1000);} catch (InterruptedException e){e.printStackTrace();}
				}
			}
		});
		
		time = System.currentTimeMillis();
		inputThread.start();
		while (true) //loops infinitely until shut down by the timer
		{
			word = wordGen();
			if (!fin) System.out.println("CPU: " + word.toLowerCase());
			time = System.currentTimeMillis();
			if (!fin) System.out.print("Player: ");
			String x;
			x = sc.next();
			while (!check(x.toUpperCase()))
			{
				System.out.print("Player: ");
				x = sc.next();
			}
			score++;
			points += 1.0 * (1 + (.5*(level-1))) * (word.length());
			if (points >= cutOff && level < 10 && !fin)
			{
				cutOff += 50*(1 + (0.25*(level)));
				level++;
				System.out.println();
				System.out.println("--------------");
				System.out.println("Ding! Level: " + level);
				System.out.println(" " + Math.round((10000 - (777*(level-1))) / 1000) + "s per word");
				System.out.println(" Keep going! ");
				System.out.println("--------------");
				System.out.println();
				try{Thread.sleep(200);} catch (InterruptedException e){e.printStackTrace();}
				System.out.println("Here's the next word:");
			}
		}
	}

	public static boolean dictCheck(String x)
	{
		x = x.toUpperCase();
		for (int i = 0; i < dict.length; i++)
		{
			if (x.equals(dict[i])) 
			{
				if (hell && x.equals("XEROX"))
				{
					System.out.print("That word is banned. ;) Try again: ");
					return false;
				}
				used.add(x);
				return true;
			}
		}
		System.out.print("Not in the dictionary. Try again: ");
		return false;
	}
	
	//check if a word is in the dictionary and starts with the correct letter
	public static boolean check(String x)
	{
		if (x.charAt(0) != word.charAt(word.length()-1)) 
		{
			System.out.println("The letters don't match. \nTry again. Word: " + word.toLowerCase());
			return false;
		}
		for (int i = 0; i < used.size(); i++)
		{
			if (x.equals(used.get(i))) 
			{
				System.out.println("That word was used already. \nTry again. Word: " + word.toLowerCase());
				return false;
			}
		}
		for (int i = 0; i < dict.length; i++)
		{
			if (x.equals(dict[i]))
			{
				if (hell && x.equals("XEROX"))
				{
					System.out.println("That word is banned. ;) Word: " + word);
					return false;
				}
				word = dict[i];
				used.add(word);
				return true;
			}
		}
		System.out.println("Not in the dictionary. \nTry again. Word: " + word.toLowerCase());
		return false;
	}
	
	public static String wordGen()
	{
		int start = 0;
		int end = 0;
		for (int i = 0; i < dict.length; i++)
		{
			if (dict[i].charAt(0) == word.charAt(word.length()-1)) 
			{
				start = i;
				break;
			}
		}
		for (int i = start; i < dict.length; i++)
		{
			if (dict[i].charAt(0) != word.charAt(word.length()-1)) 
			{
				end = i;
				break;
			}
			if (i == dict.length - 1)
			{
				end = i;
				break;
			}
		}
		if (!hell)
		{
			Random r = new Random();
			int pick = r.nextInt(end-start) + start;
			int winCount = 0;
			while (!usedCheck(dict[pick]))
			{
				pick = r.nextInt(end-start) + start;
				winCount++;
				if (winCount == end - start)
				{
					System.out.println("You are a god...and now the game is broken.");
					System.exit(0);
					break;
				}
			}
			if (dict[pick].charAt(dict[pick].length()-1) == 'S')
			{
				String med = dict[pick].substring(0, dict[pick].length()-1);
				for (int i = pick; i >= start; i--)
				{
					if (med.equals(dict[i]))
					{
						boolean c = true;
						for (int j = 0; j < used.size(); j++)
						{
							if (med.equals(used.get(j))) 
							{
								c = false;
							}
						}
						if (c)
						{
							word = med;
							used.add(word);
							return word;
						}
					}
				}
			}
			word = dict[pick];
			used.add(word);
			return word;
		}
		else
		{
			ArrayList<String> xyz = new ArrayList<String>();
			for (int i = start; i < end; i++)
			{
				if (dict[i].charAt(dict[i].length()-1) == 'Z' || dict[i].charAt(dict[i].length()-1) == 'X' || dict[i].charAt(dict[i].length()-1) == 'Q' || dict[i].charAt(dict[i].length()-1) == 'J' || dict[i].charAt(dict[i].length()-1) == 'K')
				{
					boolean c = true;
					for (int j = 0; j < used.size(); j++)
					{
						if (dict[i].equals(used.get(j)))
						{
							c = false;
						}
					}
					if (c) xyz.add(dict[i]);
				}
			}
			Random r = new Random();
			System.out.println(xyz.size());
			int med = r.nextInt(xyz.size());
			word = xyz.get(med);
			used.add(word);
			return word;
		}
	}
	
	public static boolean usedCheck(String x)
	{
		for (int i = 0; i < used.size(); i++)
		{
			if (x.equals(used.get(i))) return false;
		}
		return true;
	}
	
	public static void highScore() throws IOException
	{
		BufferedReader f = new BufferedReader(new FileReader("scoreboard.txt"));
		int n = Integer.parseInt(f.readLine());
		String[] names = new String[n];
		int[] words = new int[n];
		int[] scores = new int[n];
		for (int i = 0; i < n; i++)
		{
			StringTokenizer st = new StringTokenizer(f.readLine());
			names[i] = st.nextToken();
			words[i] = Integer.parseInt(st.nextToken());
			scores[i] = Integer.parseInt(st.nextToken());
		}
		int place = n;
		for (int i = n-1; i >= 0; i--)
		{
			if (points > scores[i])
			{
				place = i;
			}
		}
		if (place < 10)
		{
			System.out.println();
			System.out.println("You set a high score!");
			System.out.println();
			Scanner sc = new Scanner(System.in);
			
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("Shiritori.in")));
			int med = n +1;
			if (med == 11) med = 10;
			out.println(med);
			System.out.println();
			System.out.println("    SCOREBOARD    ");
			System.out.println("NAME  WORDS SCORE ");
			for (int i = 0; i < place; i++)
			{
				out.printf("%-6s", names[i]);
				out.printf("%-6d", words[i]);
				out.printf("%-6d", scores[i]);
				out.println();
				System.out.printf("%-6s", names[i]);
				System.out.printf("%-6d", words[i]);
				System.out.printf("%-6d", scores[i]);
				System.out.println();
			}
			out.printf("%-6s", name);
			out.printf("%-6d", score);
			out.printf("%-6d", (int) Math.round(points));
			out.println();
			System.out.printf("%-6s", name);
			System.out.printf("%-6d", score);
			System.out.printf("%-6d", (int) Math.round(points));
			System.out.println();
			for (int i = place; i < med-1; i++)
			{
				out.printf("%-6s", names[i]);
				out.printf("%-6d", words[i]);
				out.printf("%-6d", scores[i]);
				out.println();
				System.out.printf("%-6s", names[i]);
				System.out.printf("%-6d", words[i]);
				System.out.printf("%-6d", scores[i]);
				System.out.println();
			}
			out.close();
		}
		else
		{
			System.out.println();
			System.out.println("    SCOREBOARD    ");
			System.out.println("NAME  WORDS SCORE ");
			for (int i = 0; i < n; i++)
			{
				System.out.printf("%-6s", names[i]);
				System.out.printf("%-6d", words[i]);
				System.out.printf("%-6d", scores[i]);
				System.out.println();				
			}
		}
	}
	
	public static boolean hasNextLine(Scanner sc) throws IOException {
    while (System.in.available() == 0) {
        // [variant 1
        try {
            Thread.currentThread().sleep(10);
        } catch (InterruptedException e) {
            System.out.println("Thread is interrupted.. breaking from loop");
            return false;
        }// ]

        // [variant 2 - without sleep you get a busy wait which may load your cpu
        //if (this.isInterrupted()) {
        //    System.out.println("Thread is interrupted.. breaking from loop");
        //    return false;
        //}// ]
    }
    return sc.hasNextLine();
}
}