package ge.redmed.custompropertymanagement.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import ge.redmed.custompropertymanagement.model.Role;
import ge.redmed.custompropertymanagement.model.User;
import ge.redmed.custompropertymanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;

	@GetMapping
	public List<User> getUsers() {
		return userService.getAllUser();
	}

	@PostMapping("/save")
	public User saveUser(@RequestBody User user) {
		return userService.saveUser(user);
	}

	@PostMapping("/role/save")
	public Role saveRole(@RequestBody Role role) {
		return userService.saveRole(role);
	}

	@PostMapping("{id}/role/add-to-user")
	public void addRoleToUser(@PathVariable long id,
							  @RequestParam String roleName) {
		userService.addRoleToUser(id, roleName);
	}

	@GetMapping("by-username")
	public User getUserByUserName(@RequestParam String userName) {
		return userService.getUser(userName);
	}

	@GetMapping("/token/refresh")
	public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String authorizationHeader = request.getHeader(AUTHORIZATION);
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			try {
				String refresh_token = authorizationHeader.substring("Bearer ".length());
				Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
				JWTVerifier verifier = JWT.require(algorithm).build();
				DecodedJWT decodedJWT = verifier.verify(refresh_token);
				String userName = decodedJWT.getSubject();
				User user = userService.getUser(userName);
				String access_token = createAccessToken(user, algorithm, request);
				Map<String, String> tokens = new HashMap<>();
				tokens.put("access_token", access_token);
				tokens.put("refresh_token", refresh_token);
				response.setContentType(APPLICATION_JSON_VALUE);
				new ObjectMapper().writeValue(response.getOutputStream(), tokens);
			} catch (Exception e) {
				response.setHeader("error", e.getMessage());
				response.setStatus(FORBIDDEN.value());
				Map<String, String> error = new HashMap<>();
				error.put("error_message", e.getMessage());
				response.setContentType(APPLICATION_JSON_VALUE);
				new ObjectMapper().writeValue(response.getOutputStream(), error);
			}
		} else {
			throw new RuntimeException("Refresh token missing");
		}
	}

	private String createAccessToken(User user, Algorithm algorithm, HttpServletRequest request) {
		return JWT.create()
				.withSubject(user.getUserName())
				.withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
				.withIssuer(request.getRequestURL().toString())
				.withClaim("roles", user.getUserRoles().stream().map(Role::getName).collect(Collectors.toList()))
				.sign(algorithm);
	}

	@GetMapping("/users-properties-price-sum/excel")
	public void export(HttpServletResponse response) throws IOException {
		userService.exportUsersPropertiesPriceSum(response);
	}
}
