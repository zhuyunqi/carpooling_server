package com.carpool.shiro;

import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;
import org.apache.shiro.web.subject.WebSubjectContext;
import org.apache.shiro.web.subject.support.WebDelegatingSubject;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
 
/**
 * <p>Version: 1.0
 */
public class StatelessDefaultSubjectFactory extends DefaultWebSubjectFactory {
 
    
    @Override
    public WebDelegatingSubject createSubject(SubjectContext context) {
//        if (!(context instanceof WebSubjectContext)) {
//            return super.createSubject(context);
//        }
        WebSubjectContext wsc = (WebSubjectContext) context;
        wsc.setSessionCreationEnabled(true);
        
        SecurityManager securityManager = wsc.resolveSecurityManager();
        Session session = wsc.resolveSession();
        boolean sessionEnabled = wsc.isSessionCreationEnabled();
        PrincipalCollection principals = wsc.resolvePrincipals();
        boolean authenticated = wsc.resolveAuthenticated();
        String host = wsc.resolveHost();
        ServletRequest request = wsc.resolveServletRequest();
        request.setAttribute(DefaultSubjectContext.SESSION_CREATION_ENABLED, true);
        ServletResponse response = wsc.resolveServletResponse();
        WebDelegatingSubject wds = new WebDelegatingSubject(principals, authenticated, host, session, sessionEnabled,
                request, response, securityManager);
        
        boolean b = WebUtils._isSessionCreationEnabled(wds);
        ThreadContext.bind(wds);
        return wds;
    }
}
