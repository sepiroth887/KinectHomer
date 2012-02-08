package uk.ac.stir.cs.homer.homerFrameworkAPI.componentUtils.encoding;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class IDUtil {
	
	public static String getUniqueIdentifier(Class<?> c, String type)
	{
		String idString = c.getName() + type;
		
		try {
			MessageDigest digester = MessageDigest.getInstance("SHA-1");
			digester.update(idString.getBytes("UTF-8"));
			
			return getHex(digester.digest());
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("SHA-1 not supported", e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("UTF-8 not supported", e);
		}
	}

	private static final String HEXES = "0123456789ABCDEF";
	private static String getHex( byte [] raw ) {
	    if ( raw == null ) {
	      return null;
	    }
	    final StringBuilder hex = new StringBuilder( 2 * raw.length );
	    for ( final byte b : raw ) {
	      hex.append(HEXES.charAt((b & 0xF0) >> 4))
	         .append(HEXES.charAt((b & 0x0F)));
	    }
	    return hex.toString();
	  }

}
