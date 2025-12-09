package com.example.mobilelabassignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    EditText edtVehiclePrice, edtDownPayment, edtLoanPeriod, edtInterestRate;
    TextView txtResult;
    Button btnCalculate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Inset handling (KEEP)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Match XML IDs
        edtVehiclePrice = findViewById(R.id.edtVehiclePrice);
        edtDownPayment = findViewById(R.id.edtDownPayment);
        edtLoanPeriod = findViewById(R.id.edtLoanPeriod);
        edtInterestRate = findViewById(R.id.edtInterestRate);
        txtResult = findViewById(R.id.txtResult);
        btnCalculate = findViewById(R.id.btnCalculate);

        // Calculation
        btnCalculate.setOnClickListener(v -> {

            if (edtVehiclePrice.getText().toString().isEmpty() ||
                    edtDownPayment.getText().toString().isEmpty() ||
                    edtLoanPeriod.getText().toString().isEmpty() ||
                    edtInterestRate.getText().toString().isEmpty()) {

                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            double price = Double.parseDouble(edtVehiclePrice.getText().toString());
            double down = Double.parseDouble(edtDownPayment.getText().toString());
            int years = Integer.parseInt(edtLoanPeriod.getText().toString());
            double rate = Double.parseDouble(edtInterestRate.getText().toString());

            double loanAmount = price - down;
            double totalInterest = loanAmount * (rate / 100) * years;
            double totalPayment = loanAmount + totalInterest;
            double monthlyPayment = totalPayment / (years * 12);

            String result = "Loan Amount: RM " + String.format("%.2f", loanAmount) +
                    "\nTotal Interest: RM " + String.format("%.2f", totalInterest) +
                    "\nTotal Payment: RM " + String.format("%.2f", totalPayment) +
                    "\nMonthly Payment: RM " + String.format("%.2f", monthlyPayment);

            txtResult.setText(result);
        });
    }

    // Add menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    // Navigation
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_home) {
            return true;
        } else if (item.getItemId() == R.id.menu_about) {
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
