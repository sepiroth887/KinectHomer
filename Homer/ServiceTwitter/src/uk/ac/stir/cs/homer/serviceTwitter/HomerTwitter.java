package uk.ac.stir.cs.homer.serviceTwitter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;

public class HomerTwitter {

	private Twitter twitter;
	
	private static Logger logger = LoggerFactory.getLogger(HomerTwitter.class);
	
	public HomerTwitter(String consumerKey, String consumerSecret)
	{
		java.security.Security.addProvider(new com.sun.crypto.provider.SunJCE());
		 
		AccessToken accessToken = new AccessToken("211503304-fFVCtz5aMUj38PI2Yy5JohUuWtS9Aue2jGhGS4KI", "VZhGhiNToe3qUSOsSaj3JQhUz2yNHJ4dmzJqJ5BI");
		 
		twitter = new TwitterFactory().getOAuthAuthorizedInstance(consumerKey, consumerSecret, accessToken); 
	}
	
	public void postTwitterMessage(String message)
	{
		postTwitterMessage(twitter, message);
	}
	private static void postTwitterMessage(Twitter twitterAccount, String message)
	{
		try {
			twitterAccount.updateStatus(message);
			logger.info("Posted status to Twitter: '" + message+ "'");
		} catch (TwitterException e) {
			logger.error("Unable to post message ('"+message+"') to Twitter:", e);
		}
	}
	
	/*
	public static void postTwitterMessage(String twitterID, String password, String message)
	{
		 Twitter twitter = new TwitterFactory().getInstance(twitterID, password);
		 postTwitterMessage(twitter, message);
	}*/
}
