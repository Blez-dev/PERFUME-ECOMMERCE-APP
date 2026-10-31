package org.blezant.perfumeecommerceapp.external.aws.controller;


import org.blezant.perfumeecommerceapp.external.aws.service.AwsService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AwsController {

    public AwsController(AwsService awsService) {
        this.awsService = awsService;
    }

    AwsService awsService;



}
