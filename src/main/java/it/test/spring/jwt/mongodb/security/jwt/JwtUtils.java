package it.test.spring.jwt.mongodb.security.jwt;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import it.test.spring.jwt.mongodb.security.services.UserDetailsImpl;

@Component
public class JwtUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

	private static final String HEADER_AUTH_KEY = "Authorization";
	private static final String TOKEN_PREFIX 	= "Bearer "; //Attenzione al blank finale
	
	private static final String JWT_CLAIM_USER	= "user";
	
	@Value("${app.jwtSecret}")
	private String jwtSecret;

	@Value("${app.jwtExpirationMs}")
	private int jwtExpirationMs;
	
	public String parseJwt(HttpServletRequest request) {
		
	    String headerAuth = request.getHeader(HEADER_AUTH_KEY);
	    String jwt = null;

	    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(TOKEN_PREFIX)) {
	    	jwt = headerAuth.substring(TOKEN_PREFIX.length());
	    }

	    return jwt;
	}
	
	
	public void setJwtToken(HttpServletResponse response, String jwt) {
		if (!response.containsHeader(HEADER_AUTH_KEY)) {
        	response.addHeader(HEADER_AUTH_KEY, TOKEN_PREFIX + jwt);
		} else {
			response.setHeader(HEADER_AUTH_KEY, TOKEN_PREFIX + jwt);
		}
	}

	public String generateJwtToken(Authentication authentication, List<String> roles) {

		UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

		return Jwts.builder()
				.setSubject(userPrincipal.getUsername())
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
				.claim(JWT_CLAIM_USER, new JwtPayloadUser(userPrincipal.getId(), userPrincipal.getUsername(), userPrincipal.getEmail(), roles))
				.signWith(SignatureAlgorithm.HS512, jwtSecret)
				.compact();
	}
	
	public String generateJwtToken(JwtPayloadUser user) {
		return Jwts.builder()
				.setSubject(user.getUserId())
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
				.claim(JWT_CLAIM_USER, user)
				.signWith(SignatureAlgorithm.HS512, jwtSecret)
				.compact();
	}
	
	public String getRefreshToken(String token) {
		Map<String, Object> userMap = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().get(JWT_CLAIM_USER, Map.class);
		
		JwtPayloadUser user = new JwtPayloadUser();
		BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(user);
		wrapper.setAutoGrowNestedPaths(true);
		wrapper.setPropertyValues(userMap);
		
		String refreshToken = generateJwtToken(user);
		logger.info("Refresh token {}", refreshToken);
		
		return refreshToken;
	}

	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
	}

	public boolean validateJwtToken(String authToken) {
		try {
			Jws<Claims> claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			Map<String, String> userMap = claims.getBody().get(JWT_CLAIM_USER, Map.class);
			
			if ((null == userMap) || userMap.isEmpty()) {
				logger.error("User claim not found");
				return false;
			}
			
			return true;
		} catch (SignatureException e) {
			logger.error("Invalid JWT signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty: {}", e.getMessage());
		}

		return false;
	}
}