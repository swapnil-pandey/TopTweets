import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class TrendingHashtags {

	/**
	 * Internal class for storing the hashtag with its frequency
	 */
	static class HashTagFreq implements Comparable {
		String hashtag;
		int freq;

		HashTagFreq(String hashtag, int freq) {
			this.hashtag = hashtag;
			this.freq = freq;
		}

		@Override
		public int compareTo(Object obj) {
			HashTagFreq hf = (HashTagFreq) obj;
			return -Integer.compare(this.freq, hf.freq);
		}

		@Override
		public String toString() {
			return hashtag + ": " + freq;
		}
	}

	BufferedReader br;
	Map<String, Integer> hashtagFreq;
	PriorityQueue<HashTagFreq> topTweets;
	//defines the limit for the number of top tweets
	int k;

	/**
	 * Constructor to initialize the variables
	 */
	TrendingHashtags() {
		br = new BufferedReader(new InputStreamReader(System.in));
		hashtagFreq = new HashMap();
		topTweets = new PriorityQueue<>(new Comparator<HashTagFreq>() {
			@Override
			public int compare(HashTagFreq hf1, HashTagFreq hf2) {
				return Integer.compare(hf1.freq, hf2.freq);
			}
		});
		k = 10;
	}

	/**
	 * To get a input from the user
	 *
	 * @return the user input string
	 */
	private String inputFromUser() {
		boolean inputSuccess = false;
		String userInput = null;
		while (!inputSuccess) {
			try {
				userInput = br.readLine();
				inputSuccess = true;
			} catch (IOException e) {
				System.out.println("Problem reading input. Please try again!");
			}
		}
		return userInput;
	}

	/**
	 * Extract the hashtags from a user input tweet
	 *
	 * @param tweet the tweet as input by the user
	 * @return set of uniques hashtags present in the tweet
	 */
	private Set<String> extractHashtags(String tweet) {
		Set<String> hashtags = new HashSet();
		String[] wordsInTweet = tweet.split(" ");
		for (String word : wordsInTweet) {
			if (word.startsWith("#"))
				hashtags.add(word.substring(1));
		}
		return hashtags;
	}

	/**
	 * To find the top 'k' tweets
	 * if the size of the priority queue at any time gets greater than 'k', the minimum value is removed
	 */
	private void topKTweets() {
		for (Map.Entry<String, Integer> hashtag : hashtagFreq.entrySet()) {
			topTweets.add(new HashTagFreq(hashtag.getKey(), hashtag.getValue()));
			if (topTweets.size() > k) {
				topTweets.poll();
			}
		}
	}

	/**
	 * Increase the frequency for a hashtag
	 *
	 * @param hashtagsInInput hashtags in the user input
	 */
	private void increaseFrequency(Set<String> hashtagsInInput) {
		for (String hashtag : hashtagsInInput) {
			hashtagFreq.put(hashtag, hashtagFreq.getOrDefault(hashtag, 0) + 1);
		}
	}

	/**
	 * To check if the user wants to enter more tweets or not
	 *
	 * @return true if the user wants to input(enters 'y' or 'yes') more tweets else false
	 */
	private boolean moreTweets() {
		String userResponse = this.inputFromUser();
		return userResponse.equalsIgnoreCase("y") || userResponse.equalsIgnoreCase("yes");
	}

	public static void main(String[] args) {
		TrendingHashtags trendingHashtags = new TrendingHashtags();

		do {
			System.out.println("Please input the tweet");
			String tweet = trendingHashtags.inputFromUser();
			Set<String> hashtagsInInput = trendingHashtags.extractHashtags(tweet);
			trendingHashtags.increaseFrequency(hashtagsInInput);
			System.out.println("Want to enter more tweets? (yes/no)");
		} while (trendingHashtags.moreTweets());

		//get the top 'k' tweets in the priority queue
		trendingHashtags.topKTweets();

		//create an array of the top 'k' tweets; the array size is 'k' or the size pf priority queue (whichever is lower)
		HashTagFreq[] arr = trendingHashtags.topTweets
				.toArray(new HashTagFreq[Math.min(trendingHashtags.k, trendingHashtags.topTweets.size())]);

		//sort the array of 'k' hashtags in descending order
		Arrays.sort(arr, HashTagFreq::compareTo);

		//print the top 'k' tweets
		System.out.println(Arrays.toString(arr));

		//close the resources
		try {
			trendingHashtags.br.close();
		} catch (IOException e) {
			System.out.println("Input Stream already closed");
		}
	}

}

/*
#sachin is great #sachin #first
y
#dhoni #dhoni #second
y
#isro #third
y
#isro #sachin #dhoni #fourth
y
#trump #fifth
y
#modi #sixth
y
#moditrump #seventh
y
#modi #trump #eighth
y
#isro #modi #ninth
y
#germany #tenth
y
#sachin is #modi fan bcz #isro #eleventh
y
#sachin is great #sachin #first
y
#dhoni #dhoni #second
y
#isro #third
y
#isro #sachin #dhoni #fourth
y
#trump #fifth
y
#modi #sixth
y
#moditrump #seventh
y
#modi #trump #eighth
y
#isro #modi #ninth
y
#germany #tenth
y
#sachin is #modi fan bcz #isro #eleventh
n

*/