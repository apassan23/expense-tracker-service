package com.phoenix.expensetrackerservice.service.transaction.impl;

import com.phoenix.expensetrackerservice.model.RetrieveTransactionDTO;
import com.phoenix.expensetrackerservice.model.TransactionDTO;
import com.phoenix.expensetrackerservice.service.transaction.RetrieveTransactionService;
import com.phoenix.expensetrackerservice.service.transaction.TransactionManagementService;
import com.phoenix.expensetrackerservice.service.transaction.TransactionRequestValidationService;
import com.phoenix.expensetrackerservice.service.transaction.TransactionService;
import com.phoenix.expensetrackerservice.transform.RetrieveTransactionBuilder;
import com.phoenix.expensetrackerservice.transform.TransactionBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class TransactionManagementServiceImpl implements TransactionManagementService {

    private final TransactionService createTransactionService;
    private final TransactionService changeTransactionService;
    private final TransactionService deleteTransactionService;
    private final RetrieveTransactionService retrieveTransactionService;
    private final TransactionRequestValidationService transactionRequestValidationService;

    public TransactionManagementServiceImpl(@Qualifier("createTransactionServiceImpl") TransactionService createTransactionService,
                                            @Qualifier("changeTransactionServiceImpl") TransactionService changeTransactionService,
                                            @Qualifier("deleteTransactionServiceImpl") TransactionService deleteTransactionService, RetrieveTransactionService retrieveTransactionService, TransactionRequestValidationService transactionRequestValidationService) {
        this.createTransactionService = createTransactionService;
        this.changeTransactionService = changeTransactionService;
        this.deleteTransactionService = deleteTransactionService;
        this.retrieveTransactionService = retrieveTransactionService;
        this.transactionRequestValidationService = transactionRequestValidationService;
    }

    @Override
    public TransactionDTO createTransaction(TransactionDTO transactionDTO) {
        transactionRequestValidationService.validateForCreate(transactionDTO);
        TransactionDTO transactionResponse = createTransactionService.given(transactionDTO);
        return transactionResponse;
    }

    @Override
    public TransactionDTO retrieveTransaction(String transactionId) {
        RetrieveTransactionDTO transactionDTO = RetrieveTransactionBuilder.build(transactionId);
        transactionRequestValidationService.validateForRetrieve(transactionDTO);
        List<TransactionDTO> transactionResponse = retrieveTransactionService.given(transactionDTO);
        return transactionResponse.stream().findFirst().orElse(null);
    }

    @Override
    public List<TransactionDTO> retrieveTransactions(RetrieveTransactionDTO retrieveTransactionDTO) {
        transactionRequestValidationService.validateForRetrieve(retrieveTransactionDTO);
        if (Objects.nonNull(retrieveTransactionDTO.getPageNumber()) && Objects.nonNull(retrieveTransactionDTO.getPageSize())) {
            retrieveTransactionDTO.setFetchAll(Boolean.FALSE);
        }
        List<TransactionDTO> transactions = retrieveTransactionService.given(retrieveTransactionDTO);
        return transactions;
    }

    @Override
    public TransactionDTO changeTransaction(TransactionDTO transactionDTO) {
        transactionRequestValidationService.validateForChange(transactionDTO);
        TransactionDTO transactionResponse = changeTransactionService.given(transactionDTO);
        return transactionResponse;
    }

    @Override
    public void deleteTransaction(String transactionId) {
        TransactionDTO transactionDTO = TransactionBuilder.buildFromTransactionId(transactionId);
        transactionRequestValidationService.validateForDelete(transactionDTO);
        deleteTransactionService.given(transactionDTO);
    }
}
