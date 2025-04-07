package com.vromanyu.spring_security_jwt_v2.controller;

import com.vromanyu.spring_security_jwt_v2.jdbc.SimpleFunctionCaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestController
@RequestMapping("/func")
public class FunctionCallerController {

 private static final Logger logger = LoggerFactory.getLogger(SimpleFunctionCaller.class);

 private final SimpleFunctionCaller simpleFunctionCaller;

 public FunctionCallerController(SimpleFunctionCaller simpleFunctionCaller) {
  this.simpleFunctionCaller = simpleFunctionCaller;
 }

 @GetMapping("/call")
 public int getFunctionResult(@RequestParam(name = "a") int a, @RequestParam(name = "b") int b) {
   return simpleFunctionCaller.simpleCalculator("simple_calculator", new int[]{a, b});
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
 public String handleException(MethodArgumentTypeMismatchException ex) {
  return ex.getMessage();
  }
}
