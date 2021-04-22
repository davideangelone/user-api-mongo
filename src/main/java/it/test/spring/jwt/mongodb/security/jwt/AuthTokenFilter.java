package it.test.spring.jwt.mongodb.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import it.test.spring.jwt.mongodb.security.services.UserDetailsServiceImpl;

public class AuthTokenFilter extends OncePerRequestFilter {
	
	  @Autowired
	  private JwtUtils jwtUtils;

	  @Autowired
	  private UserDetailsServiceImpl userDetailsService;

	  private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

	  @Override
	  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	      throws ServletException, IOException {
		  
	    try {
	      String jwt = jwtUtils.parseJwt(request);
	      if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
	        String username = jwtUtils.getUserNameFromJwtToken(jwt);

	        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
	        
	        UsernamePasswordAuthenticationToken authentication = 
	        		new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

	        SecurityContextHolder.getContext().setAuthentication(authentication);
	        
	        String refreshToken = jwtUtils.getRefreshToken(jwt);
	        jwtUtils.setJwtToken(response, refreshToken);
	        
	      }
	      
	    } catch (Exception e) {
	    	logger.error("Cannot set user authentication: {}", e);
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid authentication.");
	    }

	    filterChain.doFilter(request, response);
	  }

}