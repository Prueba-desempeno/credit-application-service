package com.CreditApplicationService.coopcredit.infraestructure.error.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;
import java.time.Instant;
import java.util.UUID;

public class ErrorResponseFactory {

    public static ProblemDetail createProblemDetail(
            HttpStatus status,
            String title,
            String detail,
            WebRequest request
    ) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);

        problem.setTitle(title);
        problem.setProperty("timestamp", Instant.now().toString());
        problem.setProperty("traceId", UUID.randomUUID().toString());
        problem.setProperty("instance", request.getDescription(false));

        return problem;
    }
}
