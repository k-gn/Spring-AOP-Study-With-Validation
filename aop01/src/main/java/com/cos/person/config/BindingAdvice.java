package com.cos.person.config;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.cos.person.domain.CommonDto;


// @Controller, @RestController, @Component, @Configuration
// @Controller, @RestController, @Component, @Configuration
// AOP : 관점 지항 프로그래밍
// 공통 기능과 핵심 기능을 분리하여 핵심만 신경쓸 수 있도록 하고 공통 기능은 AOP 를 사용하여 처리
// Aspect : 공통기능 (구현하고자 하는 횡단 관심사의 기능)
// Advice : Aspect의 기능 자체 (실질적으로 부가기능을 담은 구현체)
// Jointpoint : Advice를 적용해야 되는 부분(ex : 필드, 메소드 / 스프링에서는 메소드만 해당)
// Pointcut : Jointpoint의 부분으로 실제로 Advice가 적용된 부분
// Weaving : Advice를 핵심기능에 적용하는 행위
@Component // 빈 등록 (메모리에 올리기)
@Aspect // 이 클래스가 Aspect를 나타내는 클래스라는 것을 명시
public class BindingAdvice {

	private static final Logger log = LoggerFactory.getLogger(BindingAdvice.class);

	// 접근제한자 리턴타입 패키지경로.클래스명.메소드명(인자값 ...)
	@Pointcut("execution(* com.cos.person.web..*Controller.*(..))") // .. : 해당 패키지 및 하위 패키지에 적용
	public void pointcut(){}

	// 어떤함수가 언제 몇번 실행됐는지 횟수같은거 로그 남기기 등
	@Before("pointcut()")
	public void testCheck() {
		// request 값 처리는 못하나요? 아래처럼 쓰면 됨!
		HttpServletRequest request =
				((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		System.out.println("주소 : "+request.getRequestURI());

		System.out.println("전처리 로그를 남겼습니다.");
	}

	@After("pointcut()")
	public void testCheck2() {
		System.out.println("후처리 로그를 남겼습니다.");
	}

	// JoinPoint : 메소드 정보
	@AfterReturning(pointcut = "pointcut()", returning = "result")
	public void testCheck3(JoinPoint joinPoint, ResponseEntity<CommonDto<?>> result) {
		System.out.println(joinPoint);
		System.out.println("AfterReturning");
		System.out.println(result.getBody().getStatusCode());
		result.getBody().setStatusCode(100);
	}

	// 함수 : 앞 뒤
	// 함수 : 앞 (username이 안들어왔으면 내가 강제로 넣어주고 실행)
	// 함수 : 뒤 (응답만 관리)

	//@Before
	//@After
	@Around("pointcut()") // ProceedingJoinPoint : 메소드 정보
	public Object validCheck(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		// Object 로 리턴타입을 지정해야한다.
		// 리플렉션 기반
		String type = proceedingJoinPoint.getSignature().getDeclaringTypeName();
		String method = proceedingJoinPoint.getSignature().getName();

		System.out.println("type : " + type); // 클래스 이름
		System.out.println("method : " + method); // 메소드 이름

		Object[] args = proceedingJoinPoint.getArgs(); // 인자값 목록

		for (Object arg : args) {
			if(arg instanceof BindingResult) {
				BindingResult bindingResult = (BindingResult) arg;

				// 서비스 : 정상적인 화면 -> 사용자요청
				if(bindingResult.hasErrors()) {
					Map<String, String> errorMap = new HashMap<>();

					for(FieldError error : bindingResult.getFieldErrors()) {
						errorMap.put(error.getField(), error.getDefaultMessage());
						// 로그 레벨 error, warn, info, debug
						log.warn(type+"."+method+"() => 필드 : "+error.getField()+", 메시지 : "+error.getDefaultMessage());
						log.debug(type+"."+method+"() => 필드 : "+error.getField()+", 메시지 : "+error.getDefaultMessage());

						// 로그 남기는 법
						// 1. DB연결 -> DB남기기
						// 2. File file = new File(); (비추)
						// 3. logback-spring.xml
					}

					// 해당 메소드의 리턴타입으로 해줘야한다. 안 그러면 casting 에러가 발생 (즉, 알아서 맞는 리턴타입으로 casting 처리된다.)
					return new ResponseEntity<>(new CommonDto<>(HttpStatus.BAD_REQUEST.value(), errorMap), HttpStatus.BAD_REQUEST);
				}
			}
		}
		return proceedingJoinPoint.proceed(); // 함수의 스택을 실행해라
	}
}



