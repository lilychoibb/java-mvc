package com.interface21.webmvc.servlet.mvc.tobe;

import java.util.Set;
import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final HandlerExecutions handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HandlerExecutions();
    }

    @Override
    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");

        Set<Class<?>> controllers = findControllers(basePackage);
        try {
            handlerExecutions.registerController(controllers);
        } catch (Exception e) {
            log.error("annotationHandlerMapping initialize 실패", e);
        }
    }

    private Set<Class<?>> findControllers(Object[] basePackage) {
        Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), requestMethod);
        return handlerExecutions.getHandlerExecution(handlerKey);
    }
}
