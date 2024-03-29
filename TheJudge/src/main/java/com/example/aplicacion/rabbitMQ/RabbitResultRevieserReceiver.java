package com.example.aplicacion.rabbitMQ;


import com.example.aplicacion.Entities.Result;
import com.example.aplicacion.Entities.Submission;
import com.example.aplicacion.Repository.ResultRepository;
import com.example.aplicacion.Repository.SubmissionRepository;
import com.example.aplicacion.services.ResultReviser;
import com.example.aplicacion.services.SubmissionReviserService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


//Clase que se encarga de recibir el result
@Service
public class RabbitResultRevieserReceiver {

    @Autowired
    private ResultRepository resultRepository;
    @Autowired
    private SubmissionRepository submissionRepository;
    @Autowired
    private SubmissionReviserService submissionReviserService;


    @RabbitListener(queues = ConfigureRabbitMq.QUEUE_NAME2)
    @Transactional
    public void handleMessage(Result res){

        //enviamos el res a revisar
        new ResultReviser().revisar(res);

        //lo guardamos
        resultRepository.save(res);

        Submission submission = submissionRepository.findSubmissionByResults(res);
        submission.sumarResultCorregido();

        //en caso de que ya se hayan corregido todos mandaremos una senal para que se valide el submission
        if(submission.isTerminadoDeEjecutarResults()){
            submissionReviserService.revisarSubmission(submission);
        }
        submissionRepository.save(submission);

    }
}
