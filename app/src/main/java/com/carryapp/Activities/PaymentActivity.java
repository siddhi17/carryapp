package com.carryapp.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.carryapp.R;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputWidget;


public class PaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        final CardInputWidget mCardInputWidget = (CardInputWidget) findViewById(R.id.card_input_widget);

        Button button = (Button) findViewById(R.id.btn_ok);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Card card = mCardInputWidget.getCard();
                if (card == null) {
                    Log.e("Invalid Card Data",""+card.toString());
                }else {
                    Log.e("Card Data",""+card.toString());
                }

                Stripe stripe = new Stripe(getApplicationContext(), "pk_test_eKTIlUyrcohtt3CSLey9RHRW");
                stripe.createToken(
                        card,
                        new TokenCallback() {
                            public void onSuccess(Token token) {
                                // Send token to your server

                                Log.e("token", token.getId());
                            }

                            public void onError(Exception error) {
                                // Show localized error message

                            }
                        }
                );
            }
        });
    }
}
