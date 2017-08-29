package net.scero.test.pointcuts;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class UserAspect {
    @Before("execution(public * aopTest*(*)) && args(request)")
    public void aopPointcut(JoinPoint joinPoint,  HttpServletRequest request){
        System.out.println(new StringBuilder().append("aopPointcut execute at ").append(System.currentTimeMillis()).append(" with ip: ").append(request.getRemoteAddr()));
        System.out.println("Joinpoint: " + joinPoint.toLongString());
    }
}
