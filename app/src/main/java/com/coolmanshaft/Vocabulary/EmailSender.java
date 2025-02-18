package com.coolmanshaft.Vocabulary;

import android.os.AsyncTask;
import android.util.Log;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender extends AsyncTask<Void, Void, Boolean> {
    private static final String TAG = "EmailSender";
    private final String email;
    private final String subject;
    private final String message;

    public EmailSender(String email, String subject, String message) {
        this.email = email;
        this.subject = subject;
        this.message = message;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            // Replace these with your email details
            String senderEmail = "your-email@gmail.com";  // Your email
            String senderPassword = "your-app-password"; // App password or your Google password

            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(senderEmail, senderPassword);
                }
            });

            MimeMessage mm = new MimeMessage(session);
            mm.setFrom(new InternetAddress(senderEmail));
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            mm.setSubject(subject);
            mm.setText(message);

            Log.d(TAG, "Sending email...");
            Transport.send(mm);  // This sends the email
            Log.d(TAG, "Email sent successfully.");
            return true;

        } catch (Exception e) {
            Log.e(TAG, "Error sending email", e);
            return false;
        }
    }
}
