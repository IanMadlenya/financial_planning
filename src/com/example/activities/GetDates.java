package com.example.activities;

import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.cs2340.R;
import com.example.model.DatabaseHandler;
import com.example.model.SessionManager;
import com.example.model.Transaction;
import com.example.presenters.IDatabaseHandler;
import com.example.presenters.TransactionHistory;

public class GetDates extends Activity {
	SessionManager session;
	EditText startDate;
	EditText endDate;
	Button makeReportButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.genspendcatrepdates_view);
		final Context context = this;
		final IDatabaseHandler db = new DatabaseHandler(context);
		session = new SessionManager(getApplicationContext());
		final long id = session.getUserID();
		startDate = (EditText)findViewById(R.id.sDate);
		endDate = (EditText)findViewById(R.id.eDate);
		makeReportButton = (Button)findViewById(R.id.makeReport);
		makeReportButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v){
				if (startDate != null && endDate != null) {
					TransactionHistory transactionsHist = db.getAllTransactionsByID(id);
					List<Transaction> transactions = transactionsHist.getWithdrawals();
					List<Transaction> validTrans = null;
					for (Transaction t : transactions) {
						if (t.getDate() >= Date.parse(startDate.toString()) && t.getDate() <= Date.parse(endDate.toString())) {
							validTrans.add(t);
						}
					}
					String[] expenses = new String[validTrans.size() + 1];
					int index = 0;
					double total = 0;
					for (Transaction t : validTrans) {
						expenses[index++] = t.withdrawToString();
						total =+ t.getWithdrawAmount();
					}
					expenses[index] = ("Total: " + total);
					Intent i = new Intent(GetDates.this, AccountCreationActivity.class);
					i.putExtra("EXPENSES", expenses);
					startActivity(i);
				}
			}
			
		});
	}
}