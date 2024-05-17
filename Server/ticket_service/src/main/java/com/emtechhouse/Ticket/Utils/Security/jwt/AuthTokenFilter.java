package com.emtechhouse.Ticket.Utils.Security.jwt;

import com.emtechhouse.Ticket.Utils.HttpInterceptor.AccessTokenContext;
import com.emtechhouse.Ticket.Utils.HttpInterceptor.EntityRequestContext;
import com.emtechhouse.Ticket.Utils.HttpInterceptor.UserDetailsRequestContext;
import com.emtechhouse.Ticket.Utils.HttpInterceptor.UserRequestContext;
import com.emtechhouse.Ticket.Utils.Security.services.UserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class AuthTokenFilter extends OncePerRequestFilter {
	@Value("${spring.application.logs.user}")
	private String userLogs;

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private Clientinformation clientinformation;

	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {

			getLogs(request);
			log.info("-------------------------------Authentication Entry---------------------");
			log.info("Requested URI: " +request.getRequestURI());
			log.info(String.valueOf("Request URL:"+request.getRequestURL()));
			String  userName = request.getHeader("userName");
			String  entityId = request.getHeader("entityId");
			String accessToken = request.getHeader("accessToken");
			String currentUserDetails = request.getHeader("userDetails");
			UserRequestContext.setCurrentUser(userName);
			EntityRequestContext.setCurrentEntityId(entityId);
			if (request.getRequestURI().matches("/auth/signin") || request.getRequestURI().matches("/auth/signup") || request.getRequestURI().matches("/swagger-ui/") ){
				UserRequestContext.setCurrentUser("Guest");
				EntityRequestContext.setCurrentEntityId("001");
			}
			AccessTokenContext.setCurrentAccessToken(accessToken);
			UserDetailsRequestContext.setCurrentUserDetails(currentUserDetails);
			String jwt = accessToken;
			clientinformation.getClientInformation(request);
			if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
				String username = jwtUtils.getUserNameFromJwtToken(jwt);
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());

				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (Exception e) {
			logger.error("Cannot set user authentication: {}", e);
		}
		filterChain.doFilter(request, response);
	}

	private String parseJwt(HttpServletRequest request) {
		String headerAuth = request.getHeader("Authorization");
		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
			return headerAuth.substring(7, headerAuth.length());
		}
		return null;
	}

	public void getLogs(HttpServletRequest request) throws IOException {
		String currentUserDetails = request.getHeader("userName");
		Path path = Paths.get(userLogs);
//		System.out.println("-----------------------------generating logs------------------------------");
		String method = request.getMethod();
		String uri = request.getRequestURI();
		String queryString = request.getQueryString();
		String protocol = request.getProtocol();
		String remoteAddr = request.getRemoteAddr();
		int remotePort = request.getRemotePort();
		String userAgent = request.getHeader("User-Agent");
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String fileName = now.format(formatter)+currentUserDetails+"RequestLogs.log";

		try {
			if (Files.exists(path)) {
				// if the file exists, append the data to the end of the file
				FileWriter writer = new FileWriter(fileName);
				writer.write("method: "+ method+" uri: "+uri+" queryString: "+queryString+" protocol: "+protocol+" remoteAddr: "+remoteAddr+" "+" remotePort: "+remotePort+" userAgent: "+userAgent);
				writer.close();
				writer.close();
			} else {
				// if the file does not exist, create a new one and add the data
				FileWriter writer = new FileWriter(fileName);
				writer.write("method: "+ method+" uri: "+uri+" queryString: "+queryString+" protocol: "+protocol+" remoteAddr: "+remoteAddr+" "+" remotePort: "+remotePort+" userAgent: "+userAgent);
				writer.close();
				writer.close();
			}


		} catch (IOException e) {
			e.printStackTrace();
		}



	}
}
