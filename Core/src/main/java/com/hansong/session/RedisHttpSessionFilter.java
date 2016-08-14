package com.hansong.session;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * Created by xhans on 2016/4/27.
 */
public class RedisHttpSessionFilter implements Filter {

    private static final String TOKEN_HEADER_NAME = "x-auth-token";

    private RedisHttpSessionRepository repository;

    public RedisHttpSessionFilter(){
        repository = RedisHttpSessionRepository.getInstance();
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        RedisSessionRequestWrapper requestWrapper = new RedisSessionRequestWrapper((HttpServletRequest) request);
        RedisSessionResponseWrapper responseWrapper = new RedisSessionResponseWrapper((HttpServletResponse) response, requestWrapper);

        chain.doFilter(requestWrapper, responseWrapper);

    }

    @Override
    public void destroy() {

    }

    private final class RedisSessionRequestWrapper extends HttpServletRequestWrapper{

        private HttpServletRequest request;

        private String token;
        /**
         * Constructs a request object wrapping the given request.
         *
         * @param request
         * @throws IllegalArgumentException if the request is null
         */
        public RedisSessionRequestWrapper(HttpServletRequest request) {
            super(request);
            this.request = request;
            this.token = request.getHeader(TOKEN_HEADER_NAME);
        }

        @Override
        public HttpSession getSession(boolean create) {
            if (token != null) {
                return repository.getSession(token, request.getServletContext());
            } else if (create){
                HttpSession session = repository.newSession(request.getServletContext());
                token = session.getId();
                return session;
            } else {
                return null;
            }
        }

        @Override
        public HttpSession getSession() {
            return getSession(true);
        }

        @Override
        public String getRequestedSessionId() {
            return token;
        }
    }

    private final class RedisSessionResponseWrapper extends HttpServletResponseWrapper {
        /**
         * Constructs a response adaptor wrapping the given response.
         *
         * @param response
         * @throws IllegalArgumentException if the response is null
         */
        public RedisSessionResponseWrapper(HttpServletResponse response, RedisSessionRequestWrapper request) {
            super(response);
            //if session associate with token is not existed, create one for the response
            response.setHeader(TOKEN_HEADER_NAME, request.getSession(true).getId());
        }
    }
}
