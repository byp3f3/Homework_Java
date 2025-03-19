package com.example.smtp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MainActivity extends AppCompatActivity {

    private EditText etTo, etSubject, etMessage;
    private ImageButton btnSend;

    private static final String SENDER_EMAIL = "your-email";

    private static final String SENDER_PASSWORD = "your-password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etTo = findViewById(R.id.et_to);
        etSubject = findViewById(R.id.et_subject);
        etMessage = findViewById(R.id.et_message);
        btnSend = findViewById(R.id.btn_send);

        btnSend.setOnClickListener(v -> sendEmail());
    }

    private void sendEmail() {
        String to = etTo.getText().toString().trim();
        String subject = etSubject.getText().toString().trim();
        String message = etMessage.getText().toString().trim();

        if (to.isEmpty() || subject.isEmpty() || message.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            try {
                Properties props = new Properties();
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");

                Session session = Session.getInstance(props,
                        new Authenticator() {
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
                            }
                        });

                Message mimeMessage = new MimeMessage(session);
                mimeMessage.setFrom(new InternetAddress(SENDER_EMAIL));
                mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
                mimeMessage.setSubject(subject);
                mimeMessage.setText(message);

                Transport.send(mimeMessage);

                runOnUiThread(() ->
                        Toast.makeText(MainActivity.this, "Письмо отправлено", Toast.LENGTH_SHORT).show());
                        etTo.setText(null);
                        etSubject.setText(null);
                        etMessage.setText(null);
            } catch (MessagingException e) {
                e.printStackTrace();
                runOnUiThread(() ->
                        Toast.makeText(MainActivity.this, "Ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}