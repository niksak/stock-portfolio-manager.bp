package com.proserus.stocks.bp.strategies.sector;

import java.math.BigDecimal;
import java.util.Collection;

import com.proserus.stocks.bp.Filter;
import com.proserus.stocks.model.analysis.SectorAnalysis;
import com.proserus.stocks.model.transactions.Transaction;

public abstract class AbstractSectorStrategy implements SectorStrategy {

	public void process(SectorAnalysis analysis,
			Collection<Transaction> transactions, Filter filter) {

		BigDecimal value = BigDecimal.ZERO;
		for (Transaction t : transactions) {
			value = value.add(getTransactionValue(t, filter));
		}
		setAnalysisValue(analysis, value);

	}
	
	public abstract BigDecimal getTransactionValue(Transaction t, Filter filter);

	public abstract void setAnalysisValue(SectorAnalysis analysis, BigDecimal value);

}
