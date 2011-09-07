package com.proserus.stocks.bp.strategies.symbols;

import java.math.BigDecimal;

import com.proserus.stocks.bp.Filter;
import com.proserus.stocks.model.analysis.Analysis;
import com.proserus.stocks.model.transactions.Transaction;
import com.proserus.stocks.model.transactions.TransactionType;

public class QuantityRemainingSYM extends AbstractStrategyCumulative {

	@Override
	public BigDecimal  getTransactionValue(Transaction t, Filter filter) {
		//TODO Logging
		if (calculsLog.isInfoEnabled()) {
			calculsLog.info("--------------------------------------");
			calculsLog.info(this.getClass().getName() + " - Symbol: " + t.getSymbol().getTicker());
			calculsLog.info("Transaction Type: " + t.getType());
			calculsLog.info("getQuantity: " + t.getQuantity());
		}
		BigDecimal  value = BigDecimal .ZERO;
		if ((!t.getType().equals(TransactionType.DIVIDEND))) {
			value = t.getQuantity();
			if (t.getType().equals(TransactionType.SELL)) {
				value = value.negate();
			}
		}
		return value;
	}

	@Override
	public void setAnalysisValue(Analysis analysis, BigDecimal value) {
		calculsLog.info("setQuantity: " + value);
		analysis.setQuantity(value);
	}
}
