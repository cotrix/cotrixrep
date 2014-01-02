package org.cotrix.common.tx;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.interceptor.InterceptorBinding;


@InterceptorBinding
@Target({ TYPE, METHOD})
@Retention(RUNTIME)
@Documented
public @interface Transactional {}