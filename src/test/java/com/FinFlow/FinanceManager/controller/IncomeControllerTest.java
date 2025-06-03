package com.FinFlow.FinanceManager.controller;

import com.FinFlow.FinanceManager.dto.IncomeDTO;
import com.FinFlow.FinanceManager.entity.Income;
import com.FinFlow.FinanceManager.services.income.IncomeService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;




class IncomeControllerTest {

    @Mock
    private IncomeService incomeService;

    @InjectMocks
    private IncomeController incomeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void postIncome_shouldReturnCreated_whenIncomeCreated() {
        IncomeDTO dto = new IncomeDTO();
        Income income = new Income();
        when(incomeService.postIncome(dto)).thenReturn(income);

        ResponseEntity<?> response = incomeController.postIncome(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(income, response.getBody());
    }

    @Test
    void postIncome_shouldReturnBadRequest_whenIncomeNotCreated() {
        IncomeDTO dto = new IncomeDTO();
        when(incomeService.postIncome(dto)).thenReturn(null);

        ResponseEntity<?> response = incomeController.postIncome(dto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getAllIncomes_shouldReturnListOfIncomes() {
        List<IncomeDTO> incomes = Arrays.asList(new IncomeDTO(), new IncomeDTO());
        when(incomeService.getAllIncomes()).thenReturn(incomes);

        ResponseEntity<?> response = incomeController.getAllIncomes();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(incomes, response.getBody());
    }

    @Test
    void updateIncome_shouldReturnUpdatedIncome_whenSuccess() {
        Long id = 1L;
        IncomeDTO dto = new IncomeDTO();
        Income updatedIncome = new Income();
        when(incomeService.updateIncome(id, dto)).thenReturn(updatedIncome);

        ResponseEntity<?> response = incomeController.updateIncome(id, dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedIncome, response.getBody());
    }

    @Test
    void updateIncome_shouldReturnNotFound_whenEntityNotFound() {
        Long id = 1L;
        IncomeDTO dto = new IncomeDTO();
        when(incomeService.updateIncome(id, dto)).thenThrow(new EntityNotFoundException("Not found"));

        ResponseEntity<?> response = incomeController.updateIncome(id, dto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Not found", response.getBody());
    }

    @Test
    void updateIncome_shouldReturnInternalServerError_onOtherException() {
        Long id = 1L;
        IncomeDTO dto = new IncomeDTO();
        when(incomeService.updateIncome(id, dto)).thenThrow(new RuntimeException());

        ResponseEntity<?> response = incomeController.updateIncome(id, dto);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred while updating the income.", response.getBody());
    }

    @Test
    void getIncomeById_shouldReturnIncome_whenFound() {
        Long id = 1L;
        IncomeDTO income = new IncomeDTO();
        when(incomeService.getIncomeById(id)).thenReturn(income);

        ResponseEntity<?> response = incomeController.getIncomeById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(income, response.getBody());
    }

    @Test
    void getIncomeById_shouldReturnNotFound_whenEntityNotFound() {
        Long id = 1L;
        when(incomeService.getIncomeById(id)).thenThrow(new EntityNotFoundException("Not found"));

        ResponseEntity<?> response = incomeController.getIncomeById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Not found", response.getBody());
    }

    @Test
    void getIncomeById_shouldReturnInternalServerError_onOtherException() {
        Long id = 1L;
        when(incomeService.getIncomeById(id)).thenThrow(new RuntimeException());

        ResponseEntity<?> response = incomeController.getIncomeById(id);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred while retrieving the income.", response.getBody());
    }

    @Test
    void getIncomesByUserId_shouldReturnIncomes() {
        Long userId = 2L;
        List<IncomeDTO> incomes = Collections.singletonList(new IncomeDTO());
        when(incomeService.getIncomesByUserId(userId)).thenReturn(incomes);

        ResponseEntity<?> response = incomeController.getIncomesByUserId(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(incomes, response.getBody());
    }

    @Test
    void deleteIncome_shouldReturnOk_whenSuccess() {
        Long id = 1L;
        doNothing().when(incomeService).deleteIncome(id);

        ResponseEntity<?> response = incomeController.deleteIncome(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void deleteIncome_shouldReturnNotFound_whenEntityNotFound() {
        Long id = 1L;
        doThrow(new EntityNotFoundException("Not found")).when(incomeService).deleteIncome(id);

        ResponseEntity<?> response = incomeController.deleteIncome(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Not found", response.getBody());
    }

    @Test
    void deleteIncome_shouldReturnInternalServerError_onOtherException() {
        Long id = 1L;
        doThrow(new RuntimeException()).when(incomeService).deleteIncome(id);

        ResponseEntity<?> response = incomeController.deleteIncome(id);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred while deleting the income.", response.getBody());
    }
}
