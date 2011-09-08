package com.proserus.stocks.bp.dao;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Query;

import org.apache.commons.lang3.Validate;
import org.jfree.data.time.Year;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.proserus.stocks.bo.symbols.CurrencyEnum;
import com.proserus.stocks.bo.symbols.Symbol;
import com.proserus.stocks.bo.transactions.Label;
import com.proserus.stocks.bo.transactions.Transaction;
import com.proserus.stocks.bo.transactions.TransactionType;
import com.proserus.stocks.bp.model.Filter;

@Singleton
public class TransactionsDao {
	@Inject
	private PersistenceManager persistenceManager;
	
	public Transaction add(Transaction t) {
		Validate.notNull(t);
		
		t = (Transaction) persistenceManager.persist(t);
		return t;
	}

	public Collection<Transaction> getTransactions() {
		String str = "SELECT t FROM Transaction t where 1=1";
		Query query = persistenceManager.getEntityManager().createQuery(str);
		return query.getResultList();
	}
	
	public Collection<Transaction> getTransactionsBySymbol(Symbol s, boolean dateFlag) {
		Validate.notNull(s);
		
		String str = "SELECT t FROM Transaction t where 1=1";
		str += getSymbolQuery(s);
		str += getAscendingOrder();
		Query query = persistenceManager.getEntityManager().createQuery(str);
		return query.getResultList();
	}

	public Collection<Transaction> getTransactionsBySymbol(Symbol s, Filter filter, boolean dateFlag) {
		Validate.notNull(s);
		Validate.notNull(filter);
		
		String str = "SELECT t FROM Transaction t where 1=1";
		str += getFilterQuery(filter, dateFlag);
		str += getSymbolQuery(s);
		str += getAscendingOrder();
		Query query = persistenceManager.getEntityManager().createQuery(str);
		return query.getResultList();
	}
	
	public Collection<Transaction> getTransactions(Filter filter, boolean dateFlag) {
		Validate.notNull(filter);
		
		String str = "SELECT t FROM Transaction t where 1=1";
		str += getFilterQuery(filter,dateFlag);
		str += getAscendingOrder();
		Query query = persistenceManager.getEntityManager().createQuery(str);
		return query.getResultList();
	}
	
	public Collection<Transaction> getTransactionsByCurrency(CurrencyEnum currency, Filter filter, boolean dateFlag) {
		Validate.notNull(currency);
		Validate.notNull(filter);
		
		String str = "SELECT t FROM Transaction t, Symbol s WHERE symbol_id=s.id";
		str += getFilterQuery(filter, dateFlag);
		str += getCurrencyQuery(currency);
		str += getAscendingOrder();
		Query query = persistenceManager.getEntityManager().createQuery(str);
		return query.getResultList();
	}

	private String getFilterQuery(Filter filter, boolean dateFlag) {
		Validate.notNull(filter);
		//TODO Manage Date better
		return getLabelQuery(filter.getLabels()) + getSymbolQuery(filter.getSymbol()) + getTypeQuery(filter.getTransactionType()) + getDateQuery(filter.getYear(),dateFlag);
	}

	private String getLabelQuery(Collection<Label> labels) {
		Validate.notNull(labels);
		
		String query = "";
		for (Label label : labels) {

			query += " AND " + label.getId() + " " + Transaction.IN_LABELS;
		}
		return query;
	}

	private String getSymbolQuery(Symbol symbol) {
		
		String query = "";
		if (symbol != null) {
			query = " AND " + " symbol_id=" + symbol.getId();
		}
		return query;
	}
	
	private String getTypeQuery(TransactionType type) {
		String query = "";
		if (type != null) {
			query = " AND " + " type=" + type.ordinal();
		}
		return query;
	}

	private String getDateQuery(Year year, boolean dateFlag) {
		
		String query = "";
		if (year != null) {
			String DATE_DB_FORMAT_MIN = "-12-31 23:59:59";
			String DATE_DB_FORMAT_MAX = "-01-01 00:00:00";

			if(dateFlag){
				query += " AND " + " date>'" + (year.getYear()-1) + DATE_DB_FORMAT_MIN + "'";
			}
			query += " AND " + " date<'" + (year.getYear()+1) + DATE_DB_FORMAT_MAX + "'";
		}
		return query;
	}

	private String getAscendingOrder() {
		return " ORDER BY date ASC";
	}

	private String getCurrencyQuery(CurrencyEnum currency) {
		Validate.notNull(currency);
		
		String query = "";
		if (currency != null) {
			//FIXME Do not use ordinal
			query = " AND " + " currency=" + currency.ordinal();
		}
		return query;
	}

	public Collection<Transaction> getTransactionsByLabel(Label label) {
		Validate.notNull(label);
		
		Query query = persistenceManager.getEntityManager().createNamedQuery("transaction.findAllByLabel");
		query.setParameter("label", label);
		return query.getResultList();
	}


	public void remove(Transaction t) {
		Validate.notNull(t);
		persistenceManager.remove(t);
	}
	

	public Date getFirstYear() {
		Query query = persistenceManager.getEntityManager().createNamedQuery("transaction.findMinDate");
		return (Date) query.getSingleResult();
	}

	public TransactionsDao() {	
	}
}
