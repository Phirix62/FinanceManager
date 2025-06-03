package com.FinFlow.FinanceManager.service;

import com.FinFlow.FinanceManager.dto.IncomeDTO;
import com.FinFlow.FinanceManager.entity.Income;
import com.FinFlow.FinanceManager.repository.IncomeRepository;
import com.FinFlow.FinanceManager.services.income.IncomeServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDate;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IncomeServiceImplTest {

    @Mock
    private IncomeRepository incomeRepository;

    @InjectMocks
    private IncomeServiceImpl incomeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private IncomeDTO createIncomeDTO() {
        IncomeDTO dto = new IncomeDTO();
        dto.setTitle("Salary");
        dto.setDate(LocalDate.of(2024, 6, 1));
        dto.setAmount(1000);
        dto.setCategory("Job");
        dto.setDescription("Monthly salary");
        dto.setUser(null);
        return dto;
    }

    private Income createIncome(Long id) {
        Income income = new Income();
        income.setId(id);
        income.setTitle("Salary");
        income.setDate(LocalDate.of(2024, 6, 1));
        income.setAmount(1000);
        income.setCategory("Job");
        income.setDescription("Monthly salary");
        income.setUser(null);
        return income;
    }

    @Test
    void testPostIncome() {
        IncomeDTO dto = createIncomeDTO();
        Income savedIncome = createIncome(1L);

        when(incomeRepository.save(any(Income.class))).thenReturn(savedIncome);

        Income result = incomeService.postIncome(dto);

        assertNotNull(result);
        assertEquals("Salary", result.getTitle());
        verify(incomeRepository, times(1)).save(any(Income.class));
    }

    @Test
    void testUpdateIncome_Success() {
        IncomeDTO dto = createIncomeDTO();
        Income existingIncome = createIncome(1L);

        when(incomeRepository.findById(1L)).thenReturn(Optional.of(existingIncome));
        when(incomeRepository.save(any(Income.class))).thenReturn(existingIncome);

        Income result = incomeService.updateIncome(1L, dto);

        assertNotNull(result);
        assertEquals("Salary", result.getTitle());
        verify(incomeRepository).findById(1L);
        verify(incomeRepository).save(existingIncome);
    }

    @Test
    void testUpdateIncome_NotFound() {
        IncomeDTO dto = createIncomeDTO();
        when(incomeRepository.findById(2L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () ->
                incomeService.updateIncome(2L, dto));
        assertTrue(ex.getMessage().contains("Income not found with id: 2"));
    }

    @Test
    void testGetAllIncomes() {
        Income i1 = createIncome(1L);
        i1.setDate(LocalDate.of(2024, 6, 2));
        Income i2 = createIncome(2L);
        i2.setDate(LocalDate.of(2024, 6, 1));
        List<Income> incomes = Arrays.asList(i1, i2);

        when(incomeRepository.findAll()).thenReturn(incomes);

        List<IncomeDTO> result = incomeService.getAllIncomes();

        assertEquals(2, result.size());
        assertEquals(i1.getTitle(), result.get(0).getTitle()); // Sorted by date descending
        assertEquals(i2.getTitle(), result.get(1).getTitle());
    }

    @Test
    void testGetIncomeById_Success() {
        Income income = createIncome(1L);
        when(incomeRepository.findById(1L)).thenReturn(Optional.of(income));

        IncomeDTO result = incomeService.getIncomeById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetIncomeById_NotFound() {
        when(incomeRepository.findById(2L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () ->
                incomeService.getIncomeById(2L));
        assertTrue(ex.getMessage().contains("Income not found with id: 2"));
    }

    @Test
    void testGetIncomesByUserId() {
        Income i1 = createIncome(1L);
        i1.setDate(LocalDate.of(2024, 6, 2));
        Income i2 = createIncome(2L);
        i2.setDate(LocalDate.of(2024, 6, 1));
        List<Income> incomes = Arrays.asList(i1, i2);

        when(incomeRepository.findByUserId(10L)).thenReturn(incomes);

        List<IncomeDTO> result = incomeService.getIncomesByUserId(10L);

        assertEquals(2, result.size());
        assertEquals(i1.getTitle(), result.get(0).getTitle()); // Sorted by date descending
        assertEquals(i2.getTitle(), result.get(1).getTitle());
    }

    @Test
    void testDeleteIncome_Success() {
        Income income = createIncome(1L);
        when(incomeRepository.findById(1L)).thenReturn(Optional.of(income));
        doNothing().when(incomeRepository).deleteById(1L);

        assertDoesNotThrow(() -> incomeService.deleteIncome(1L));
        verify(incomeRepository).deleteById(1L);
    }

    @Test
    void testDeleteIncome_NotFound() {
        when(incomeRepository.findById(2L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () ->
                incomeService.deleteIncome(2L));
        assertTrue(ex.getMessage().contains("Income not found with id: 2"));
    }
}
