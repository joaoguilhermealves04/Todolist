package br.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.joaoguilherme.todolist.user.IUserRepository;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class FilterTaskAuth extends OncePerRequestFilter {

    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        var servlerPath = request.getServletPath();

        if (servlerPath.startsWith("/tasks")) {
            var authorizantion = request.getHeader("Authorization");

            authorizantion.substring("Basic".length()).trim();

            byte[] authDecodo = Base64.getDecoder().decode((authorizantion));

            var authString = new String(authDecodo);

            String[] credentials = authString.split(":");
            String username = credentials[0];
            String password = credentials[1];

            var user = this.userRepository.findByUsername(username);

            if (user == null) {
                response.sendError(401);
            } else {

                var pesswordverify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());

                if (pesswordverify.verified) {

                    request.setAttribute("idUser", user.getId());
                    filterChain.doFilter(request, response);
                } else {
                    response.sendError(401);
                }

            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

}
