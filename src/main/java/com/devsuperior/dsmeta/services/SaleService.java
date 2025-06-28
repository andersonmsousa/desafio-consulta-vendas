package com.devsuperior.dsmeta.services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

import com.devsuperior.dsmeta.dto.SaleDTO;
import com.devsuperior.dsmeta.dto.SellerSummaryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.repositories.SaleRepository;

@Service
public class SaleService {

	@Autowired
	private SaleRepository repository;

	private static LocalDate TODAY = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
	
	public SaleMinDTO findById(Long id) {
		Optional<Sale> result = repository.findById(id);
		Sale entity = result.get();
		return new SaleMinDTO(entity);
	}

    public Page<SaleDTO> getSalesReport(String minDateAsString, String maxDateAsString, String name, Pageable pageable) {
		LocalDate maxDate = getDefaultMaxDateIfItIsnull(maxDateAsString);
		LocalDate minDate = getDefaultMinDateIfItIsnull(minDateAsString, maxDate);

		return repository.findSalesReport(minDate, maxDate, name, pageable);
	}

	public List<SellerSummaryDTO> getSellersSummary(String minDateAsString, String maxDateAsString) {
		LocalDate maxDate = getDefaultMaxDateIfItIsnull(maxDateAsString);
		LocalDate minDate = getDefaultMinDateIfItIsnull(minDateAsString, maxDate);

		return repository.findSellersSummary(minDate, maxDate);
	}

	private LocalDate getDefaultMaxDateIfItIsnull(String maxDateAsString) {
		return maxDateAsString != null ? parseStringDateToLocalDate(maxDateAsString) : TODAY;
	}

	private LocalDate getDefaultMinDateIfItIsnull(String minDateAsString, LocalDate maxDate) {
		return minDateAsString != null ? parseStringDateToLocalDate(minDateAsString) : maxDate.minusYears(1);
	}

	private LocalDate parseStringDateToLocalDate(String dateAsString) {
		try {
			return LocalDate.parse(dateAsString);
		} catch (DateTimeParseException e) {
			throw new IllegalArgumentException("A data informada possui formato inválido.");
		}
	}
}
