package com.example.mobilelabassignment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher; // Import for real-time validation
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

    // Declare all UI elements
    EditText edtVehiclePrice, edtDownPayment, edtLoanPeriod, edtInterestRate;
    TextView txtResult;
    Button btnCalculate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Inset handling
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1. Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 2. Match XML IDs (Binding Views)
        edtVehiclePrice = findViewById(R.id.edtVehiclePrice);
        edtDownPayment = findViewById(R.id.edtDownPayment);
        edtLoanPeriod = findViewById(R.id.edtLoanPeriod);
        edtInterestRate = findViewById(R.id.edtInterestRate);
        txtResult = findViewById(R.id.txtResult);
        btnCalculate = findViewById(R.id.btnCalculate);

        // --- NEW: Apply Real-Time Validation to prevent "." at start ---
        setupDecimalValidation(edtVehiclePrice);
        setupDecimalValidation(edtDownPayment);
        setupDecimalValidation(edtInterestRate);
        // -------------------------------------------------------------

        // 3. Calculation Logic
        btnCalculate.setOnClickListener(v -> {

            String priceStr = edtVehiclePrice.getText().toString();
            String downStr = edtDownPayment.getText().toString();
            String yearsStr = edtLoanPeriod.getText().toString();
            String rateStr = edtInterestRate.getText().toString();

            // 1. Validaticheck for empty fields
            if (priceStr.isEmpty() || downStr.isEmpty() || yearsStr.isEmpty() || rateStr.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // 2. Safety Check (The TextWatcher handles most of this, but this is a backup)
            if (!isValidNumber(priceStr) || !isValidNumber(downStr) ||
                    !isValidNumber(yearsStr) || !isValidNumber(rateStr)) {
                Toast.makeText(this, "Invalid Format. Please ensure numbers don't start with '.'", Toast.LENGTH_LONG).show();
                return;
            }

            try {
                // 3. Parse inputs
                double price = Double.parseDouble(priceStr);
                double down = Double.parseDouble(downStr);
                double years = Double.parseDouble(yearsStr);
                double rate = Double.parseDouble(rateStr);

                // 4. Validation check for Down Payment > Price
                if (down > price) {
                    Toast.makeText(this, "Down payment can't be more than the vehicle price!", Toast.LENGTH_LONG).show();
                    txtResult.setText("Invalid input: Down payment exceeds price.");
                    return; // Stop the calculation
                }

                // Calculations (Simple Interest)
                double loanAmount = price - down;
                double totalInterest = loanAmount * (rate / 100) * years;
                double totalPayment = loanAmount + totalInterest;
                double monthlyPayment = totalPayment / (years * 12);

                // Format and display result
                String result = "Loan Amount: RM " + String.format("%.2f", loanAmount) +
                        "\nTotal Interest: RM " + String.format("%.2f", totalInterest) +
                        "\nTotal Payment: RM " + String.format("%.2f", totalPayment) +
                        "\n\nMonthly Payment: RM " + String.format("%.2f", monthlyPayment);

                txtResult.setText(result);

            } catch (NumberFormatException e) {
                Toast.makeText(this, "An unexpected number format error occurred.", Toast.LENGTH_LONG).show();
            }
        });
    }

    // --- NEW HELPER METHOD: Real-Time Validation ---
    private void setupDecimalValidation(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                // Logic: If the text starts with ".", delete it immediately
                if (s.toString().startsWith(".")) {
                    s.delete(0, 1); // Delete the first character
                    Toast.makeText(MainActivity.this, "Input cannot start with a decimal point. Use '0.' instead.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // --- Helper Validation Method for the Button Click ---
    private boolean isValidNumber(String str) {
        if (str.startsWith(".")) return false;
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // 4. Menu Setup
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    // 5. Menu Navigation
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