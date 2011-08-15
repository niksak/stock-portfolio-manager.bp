package com.proserus.stocks.bp.strategies.currencies;

import java.math.BigDecimal;

import com.proserus.stocks.model.analysis.Analysis;

public class CostBasisCY extends AbstractStrategyCumulative {
	@Override
	public BigDecimal getAnalysisValue(Analysis s) {
		calculsLog.info("getCurrentCost: " + s.getCostBasis());
		return s.getCostBasis();
	}

	@Override
	public void setAnalysisValue(Analysis analysis, BigDecimal value) {
		calculsLog.info("setCostBasis: " + value);
		analysis.setCostBasis(value);
	}
}