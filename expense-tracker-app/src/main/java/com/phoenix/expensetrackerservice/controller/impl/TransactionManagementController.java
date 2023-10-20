package com.phoenix.expensetrackerservice.controller.impl;

import com.phoenix.expensetrackerservice.annotation.Log;
import com.phoenix.expensetrackerservice.constants.ControllerConstants;
import com.phoenix.expensetrackerservice.constants.LogConstants;
import com.phoenix.expensetrackerservice.controller.TransactionManagementAPI;
import com.phoenix.expensetrackerservice.model.RetrieveTransactionDTO;
import com.phoenix.expensetrackerservice.model.TransactionDTO;
import com.phoenix.expensetrackerservice.service.transaction.TransactionManagementService;
import org.slf4j.event.Level;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = ControllerConstants.TRANSACTION_CONTROLLER_REQUEST_MAPPING)
public class TransactionManagementController implements TransactionManagementAPI {

    private final TransactionManagementService transactionManagementService;

    public TransactionManagementController(TransactionManagementService transactionManagementService) {
        this.transactionManagementService = transactionManagementService;
    }

    @Override
    @Log(action = LogConstants.CREATE_ACTION)
    @PostMapping(path = ControllerConstants.TRANSACTION_CREATE_MAPPING, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransactionDTO> createTransaction(@RequestBody TransactionDTO transaction) {
        TransactionDTO response = transactionManagementService.createTransaction(transaction);
        return new ResponseEntity<>(response, getResponseHeaders(), HttpStatus.CREATED);
    }

    @Override
    @Log(action = LogConstants.RETRIEVE_ACTION)
    @GetMapping(path = ControllerConstants.TRANSACTION_RETRIEVE_MAPPING, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransactionDTO> retrieveTransaction(@PathVariable String transactionId) {
        TransactionDTO response = transactionManagementService.retrieveTransaction(transactionId);
        return new ResponseEntity<>(response, getResponseHeaders(), HttpStatus.OK);
    }

    @Override
    @Log(action = LogConstants.RETRIEVE_ALL_ACTION, level = Level.WARN)
    @PostMapping(path = ControllerConstants.TRANSACTION_RETRIEVE_ALL_MAPPING, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TransactionDTO>> retrieveTransaction(@RequestBody RetrieveTransactionDTO retrieveTransactionDTO) {
        List<TransactionDTO> response = transactionManagementService.retrieveTransactions(retrieveTransactionDTO);
        return ResponseEntity.ok().headers(getResponseHeaders()).body(response);
    }
    // userId -> fetchAll
    // userId, date, pageNumber, pageSize -> fetchSpecific

    @Override
    @Log(action = LogConstants.CHANGE_ACTION)
    @PutMapping(path = ControllerConstants.TRANSACTION_CHANGE_MAPPING, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransactionDTO> changeTransaction(@RequestBody TransactionDTO transaction) {
        TransactionDTO response = transactionManagementService.changeTransaction(transaction);
        return ResponseEntity.accepted().headers(getResponseHeaders()).body(response);
    }

    @Override
    @Log(action = LogConstants.DELETE_ACTION)
    @DeleteMapping(path = ControllerConstants.TRANSACTION_DELETE_MAPPING, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteTransaction(@PathVariable String transactionId) {
        transactionManagementService.deleteTransaction(transactionId);
        return ResponseEntity.accepted().headers(getResponseHeaders()).build();
    }

    private HttpHeaders getResponseHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }
}
