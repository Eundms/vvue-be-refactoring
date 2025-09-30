package com.exciting.vvue.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 결혼 정보가 필요한 API에 사용하는 어노테이션
 * 해당 어노테이션이 있으면 married 정보를 검증하고 주입함
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireMarried {
}