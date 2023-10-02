package com.phoenix.expensetrackerservice.service.impl;

import com.phoenix.expensetrackerservice.model.RetrieveTransactionDTO;
import com.phoenix.expensetrackerservice.model.TransactionDTO;
import com.phoenix.expensetrackerservice.service.ChangeTransactionService;
import com.phoenix.expensetrackerservice.service.CreateTransactionService;
import com.phoenix.expensetrackerservice.service.DeleteTransactionService;
import com.phoenix.expensetrackerservice.service.RetrieveTransactionService;
import com.phoenix.expensetrackerservice.service.TransactionManagementService;
import com.phoenix.expensetrackerservice.service.TransactionRequestValidationService;
import com.phoenix.expensetrackerservice.transform.RetrieveTransactionBuilder;
import com.phoenix.expensetrackerservice.transform.TransactionBuilder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class TransactionManagementServiceImpl implements TransactionManagementService {

    private final CreateTransactionService createTransactionService;
    private final ChangeTransactionService changeTransactionService;
    private final DeleteTransactionService deleteTransactionService;
    private final RetrieveTransactionService retrieveTransactionService;
    private final TransactionRequestValidationService transactionRequestValidationService;

    public TransactionManagementServiceImpl(CreateTransactionService createTransactionService,
                                            ChangeTransactionService changeTransactionService,
                                            DeleteTransactionService deleteTransactionService,
                                            RetrieveTransactionService retrieveTransactionService,
                                            TransactionRequestValidationService transactionRequestValidationService) {
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
        TransactionDTO transactionDTO = TransactionBuilder.buildFromTransactionId(transactionId);
        transactionRequestValidationService.validateForRetrieve(transactionDTO);
        TransactionDTO transactionResponse = retrieveTransactionService.given(transactionDTO);
        return transactionResponse;
    }

    @Override
    public List<TransactionDTO> retrieveTransactions(Integer pageNumber, Integer pageSize) {
        RetrieveTransactionDTO retrieveTransactionDTO = RetrieveTransactionBuilder.build(pageNumber, pageSize, Boolean.TRUE);
        transactionRequestValidationService.validateForRetrieve(retrieveTransactionDTO);
        if(Objects.nonNull(retrieveTransactionDTO.getPageNumber()) && Objects.nonNull(retrieveTransactionDTO.getPageSize())) {
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
