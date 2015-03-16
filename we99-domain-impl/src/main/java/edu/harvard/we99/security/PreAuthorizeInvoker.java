package edu.harvard.we99.security;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.cxf.common.util.ClassHelper;
import org.apache.cxf.jaxrs.JAXRSInvoker;
import org.apache.cxf.jaxrs.model.OperationResourceInfo;
import org.apache.cxf.message.Exchange;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.ExpressionBasedAnnotationAttributeFactory;
import org.springframework.security.access.expression.method.ExpressionBasedPreInvocationAdvice;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.prepost.PreInvocationAuthorizationAdvice;
import org.springframework.security.access.prepost.PreInvocationAuthorizationAdviceVoter;
import org.springframework.security.access.prepost.PrePostAnnotationSecurityMetadataSource;
import org.springframework.security.access.prepost.PrePostInvocationAttributeFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.util.SimpleMethodInvocation;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

/**
 * Custom Spring Security Handler that also includes processing of annotations on
 * sub-resources.
 *
 * todo - My hope is that I can remove this in favor a CXF Feature or something. I'm awaiting feedback from the CXF user list.
 *
 * @author mford
 */
public class PreAuthorizeInvoker extends JAXRSInvoker {

    private final PreInvocationAuthorizationAdviceVoter voter;
    private final PrePostAnnotationSecurityMetadataSource metadataSource;

    public PreAuthorizeInvoker() {
        MethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();
        PrePostInvocationAttributeFactory prePostInvocationAttributeFactory = new ExpressionBasedAnnotationAttributeFactory(handler);
        metadataSource = new PrePostAnnotationSecurityMetadataSource(prePostInvocationAttributeFactory);
        PreInvocationAuthorizationAdvice advice = new ExpressionBasedPreInvocationAdvice();
        voter = new PreInvocationAuthorizationAdviceVoter(advice);
    }

    @Override
    public Object invoke(Exchange exchange, Object request, Object resourceObject) {
        OperationResourceInfo ori = exchange.get(OperationResourceInfo.class);
        Method m = ori.getMethodToInvoke();
        Class<?> realClass = ClassHelper.getRealClass(resourceObject);

        Collection<ConfigAttribute> attributes = metadataSource.getAttributes(m, realClass);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //noinspection unchecked
        List<Object> args = (List<Object>) request;
        MethodInvocation mi = new SimpleMethodInvocation(resourceObject, m, args.toArray(new Object[args.size()]));

        int vote = voter.vote(authentication, mi, attributes);
        if (vote == AccessDecisionVoter.ACCESS_DENIED) {
            throw new WebApplicationException(Response.status(403).build());
        }

        return super.invoke(exchange, request, resourceObject);
    }
}
