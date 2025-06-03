package com.FinFlow.FinanceManager.services.income;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.FinFlow.FinanceManager.dto.IncomeDTO;
import com.FinFlow.FinanceManager.entity.Income;
import com.FinFlow.FinanceManager.repository.IncomeRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

/**
 * Service implementation for managing incomes.
 */
@Service
@RequiredArgsConstructor
public class IncomeServiceImpl implements IncomeService{

    private static final Logger logger = LoggerFactory.getLogger(IncomeServiceImpl.class);

    private final IncomeRepository incomeRepository;

    /**
     * Creates and saves a new income.
     *
     * @param incomeDTO the income data transfer object
     * @return the saved Income entity
     */
    public Income postIncome(IncomeDTO incomeDTO) {
        logger.info("Posting new income: {}", incomeDTO);
        Income income = new Income();
        return saveOrUpdateIncome(income, incomeDTO);
    }

    /**
     * Saves or updates an income entity with data from the DTO.
     *
     * @param income the income entity to update
     * @param incomeDTO the income data transfer object
     * @return the saved or updated Income entity
     */
    private Income saveOrUpdateIncome(Income income, IncomeDTO incomeDTO) {
        logger.debug("Saving or updating income: {}", incomeDTO);
        income.setTitle(incomeDTO.getTitle());
        income.setDate(incomeDTO.getDate());
        income.setAmount(incomeDTO.getAmount());
        income.setCategory(incomeDTO.getCategory());
        income.setDescription(incomeDTO.getDescription());
        income.setUser(incomeDTO.getUser());
        return incomeRepository.save(income);
    }

    /**
     * Updates an existing income by its ID.
     *
     * @param id the ID of the income to update
     * @param incomeDTO the new income data
     * @return the updated Income entity
     * @throws EntityNotFoundException if the income is not found
     */
    public Income updateIncome(Long id, IncomeDTO incomeDTO) {
        logger.info("Updating income with id: {}", id);
        Optional<Income> optionalIncome = incomeRepository.findById(id);
        if (optionalIncome.isPresent()) {
            Income income = optionalIncome.get();
            return saveOrUpdateIncome(income, incomeDTO);
        } else {
            logger.warn("Income not found with id: {}", id);
            throw new EntityNotFoundException("Income not found with id: " + id);
        }
    }

    /**
     * Retrieves all incomes, sorted by date descending.
     *
     * @return a list of IncomeDTOs
     */
    public List<IncomeDTO> getAllIncomes(){
        logger.info("Retrieving all incomes");
        return incomeRepository.findAll().stream().sorted(Comparator.comparing(Income::getDate).reversed())
                .map(Income::getIncomeDTO).collect(Collectors.toList());
    }

    /**
     * Retrieves all incomes for a specific user, sorted by date descending.
     *
     * @param userId the user ID
     * @return a list of IncomeDTOs for the user
     */
    public List<IncomeDTO> getIncomesByUserId(Long userId) {
        logger.info("Retrieving incomes for userId: {}", userId);
        return incomeRepository.findByUserId(userId).stream()
                .sorted(Comparator.comparing(Income::getDate).reversed())
                .map(Income::getIncomeDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves an income by its ID.
     *
     * @param id the income ID
     * @return the IncomeDTO
     * @throws EntityNotFoundException if the income is not found
     */
    public IncomeDTO getIncomeById(Long id) {
        logger.info("Retrieving income by id: {}", id);
        Optional<Income> optionalIncome = incomeRepository.findById(id);
        if (optionalIncome.isPresent()) {
            return optionalIncome.get().getIncomeDTO();
        } else {
            logger.warn("Income not found with id: {}", id);
            throw new EntityNotFoundException("Income not found with id: " + id);
        }
    }

    /**
     * Deletes an income by its ID.
     *
     * @param id the income ID
     * @throws EntityNotFoundException if the income is not found
     */
    public void deleteIncome(Long id) {
        logger.info("Deleting income with id: {}", id);
        Optional<Income> optionalIncome = incomeRepository.findById(id);
        if (optionalIncome.isPresent()) {
            incomeRepository.deleteById(id);
        } else {
            logger.warn("Income not found with id: {}", id);
            throw new EntityNotFoundException("Income not found with id: " + id);
        }
    }

}
