package com.example.poupanca;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText edtValorInicial;
    private EditText edtAplicacaoMensal;
    private EditText edtTempoMeses;
    private EditText edtTaxa;
    private TextView txtResultado;
    private Button btnCalcular;
    private Button btnSair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Referências para os componentes
        edtValorInicial = findViewById(R.id.edtValorInicial);
        edtAplicacaoMensal = findViewById(R.id.edtAplicacaoMensal);
        edtTempoMeses = findViewById(R.id.edtTempoMeses);
        edtTaxa = findViewById(R.id.edtTaxa);
        txtResultado = findViewById(R.id.txtResultado);
        btnCalcular = findViewById(R.id.btnCalcular);
        btnSair = findViewById(R.id.btnSair);

        // Adicionando formatação para campos monetários
        formatarEditText(edtValorInicial);
        formatarEditText(edtAplicacaoMensal);
        formatarTaxaEditText(edtTaxa);  // Formatação para campo de taxa

        // Configuração do botão de calcular
        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcularInvestimento();
            }
        });

        // Configuração do botão de sair
        btnSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sair();
            }
        });
    }

    // Função para adicionar um TextWatcher que formata como valor monetário
    private void formatarEditText(EditText editText) {
        editText.addTextChangedListener(new android.text.TextWatcher() {
            private String current = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    editText.removeTextChangedListener(this);

                    // Remove qualquer símbolo ou separador para garantir a formatação correta
                    String cleanString = s.toString().replaceAll("[R$,.]", "").replaceAll("\\s", "");

                    // Converte o texto em double
                    double parsed;
                    try {
                        parsed = Double.parseDouble(cleanString) / 100;
                    } catch (NumberFormatException e) {
                        parsed = 0.00;
                    }

                    // Formata o número para moeda
                    NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
                    String formatted = formatter.format(parsed);

                    current = formatted;
                    editText.setText(formatted);
                    editText.setSelection(formatted.length());

                    editText.addTextChangedListener(this);
                }
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });
    }

    // Função para garantir que a taxa seja formatada como decimal
    private void formatarTaxaEditText(EditText editText) {
        editText.addTextChangedListener(new android.text.TextWatcher() {
            private String current = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    editText.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[,.]", "");

                    // Converte o texto em double
                    double parsed;
                    try {
                        parsed = Double.parseDouble(cleanString) / 100;
                    } catch (NumberFormatException e) {
                        parsed = 0.00;
                    }

                    DecimalFormat decimalFormat = new DecimalFormat("0.00");
                    String formatted = decimalFormat.format(parsed);

                    current = formatted;
                    editText.setText(formatted);
                    editText.setSelection(formatted.length());

                    editText.addTextChangedListener(this);
                }
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });
    }

    private void calcularInvestimento() {
        // Obtendo valores dos campos
        double valorInicial = parseDoubleFromText(edtValorInicial.getText().toString());
        double aplicacaoMensal = parseDoubleFromText(edtAplicacaoMensal.getText().toString());
        int tempoMeses = Integer.parseInt(edtTempoMeses.getText().toString());
        double taxaJuros = Double.parseDouble(edtTaxa.getText().toString());

        // Cálculo do montante final (com juros compostos)
        double montante = valorInicial;
        for (int i = 0; i < tempoMeses; i++) {
            montante += aplicacaoMensal;
            montante += montante * taxaJuros;
        }

        // Formatando o resultado
        DecimalFormat df = new DecimalFormat("#,##0.00");
        txtResultado.setText("Resultado: R$ " + df.format(montante));
        txtResultado.setVisibility(View.VISIBLE);
    }

    public void sair() {
        finish();
    }

    // Função para converter string formatada para double
    private double parseDoubleFromText(String value) {
        try {
            // Remove símbolos de moeda e separadores de milhar
            String cleanString = value.replaceAll("[R$,.]", "").replaceAll("\\s", "");
            return Double.parseDouble(cleanString) / 100;
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}
