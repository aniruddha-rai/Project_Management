package com.example.myapplication;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Properties;

import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class PopActivity extends Activity {

    EditText mailName, mailS_ID, mailPass, mailHost, mailPort, mailR_ID;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop);

        mailName = findViewById(R.id.mailName);
        mailS_ID = findViewById(R.id.mailS_ID);
        mailPass = findViewById(R.id.mailPass);
        mailHost = findViewById(R.id.mailHost);
        mailPort = findViewById(R.id.mailPort);
        mailR_ID = findViewById(R.id.mailR_ID);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .8));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);

//        String path = (Environment.getExternalStorageDirectory()).getAbsolutePath() + "/My Files/MyConfig.txt";
//        int name = 0;
//        int senderMail = 1;
//        int pass = 2;
//        int host = 3;
//        int port = 4;
//        int receiverMail = 5;
//        try {
//            BufferedReader br = new BufferedReader(new FileReader(path));
//            String line = String.valueOf(br.read(CharBuffer.allocate(name)));
//            mailName.setText(line);
////            String name1 = Files.readAllLines(Paths.get(path)).get(name);
////            mailName.setText(name1);
////            String senderMail1 = Files.readAllLines(Paths.get(path)).get(senderMail);
////            mailS_ID.setText(senderMail1);
////            String pass1 = Files.readAllLines(Paths.get(path)).get(pass);
////            mailPass.setText(pass1);
////            String host1 = Files.readAllLines(Paths.get(path)).get(host);
////            mailHost.setText(host1);
////            String port1 = Files.readAllLines(Paths.get(path)).get(port);
////            mailPort.setText(port1);
////            String receiverMail1 = Files.readAllLines(Paths.get(path)).get(receiverMail);
////            mailR_ID.setText(receiverMail1);
//
//        } catch (IOException e) {
//            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
//        }

    }

    public void buttonSendEmail(View view) {

        try {

            String name = mailName.getText().toString();
            String senderMail = mailS_ID.getText().toString(); // aniruddha3789@gmail.com
            String receiverMail = mailR_ID.getText().toString(); // mailId.getText().toString();
            String pass = mailPass.getText().toString(); // zluvlrbxuhsvljwp
            String host = mailHost.getText().toString(); // smtp.gmail.com
            String port = mailPort.getText().toString(); // 465

            String date = new SimpleDateFormat("dd-MM-yyyy",
                    Locale.getDefault()).format(System.currentTimeMillis());

            String mSub = "[Auto Report] || " + name + " || " + date;
            String mBody = "Hi, \n" + date + " Report of " + name + " is attached. \n" + "PFA the same.";

            Properties properties = System.getProperties();
            properties.put("mail.smtp.host", host);
            properties.put("mail.smtp.port", port);
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.auth", "true");

            javax.mail.Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    // return super.getPasswordAuthentication();
                    return new PasswordAuthentication(senderMail, pass);
                }
            });

            MimeMessage mimeMessage = new MimeMessage(session);
            Multipart multipart = new MimeMultipart();
            MimeBodyPart messageBodyPart = new MimeBodyPart();

            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(receiverMail));

            String timeStamp = new SimpleDateFormat("yyyy-MM-dd",
                    Locale.getDefault()).format(System.currentTimeMillis());
            File path = Environment.getExternalStorageDirectory();
            String fileName = "MyFile_" + timeStamp + ".csv";
            String path2 = path.getAbsolutePath() + "/My Files/" + fileName;
//            String path = (Environment.getExternalStorageDirectory()) + "/My Files/" + fileName;

            DataSource source = new FileDataSource(path2);
            // Map<String,DataSource> attachmentMap = new HashMap<>();
            //attachmentMap.put("Attachement",source);
            // messageBodyPart.setDataHandler(new DataHandler(source));
            //messageBodyPart.setFileName(fileName);
            messageBodyPart.setText("hello world");
            MimeBodyPart attachment = new MimeBodyPart();
            attachment.attachFile(new File(path2));

            messageBodyPart.setText(mBody);
            mimeMessage.setSubject(mSub);

            multipart.addBodyPart(messageBodyPart);

            multipart.addBodyPart(attachment);
            mimeMessage.setContent(multipart);

            //  mimeMessage.setText(mBody);

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Transport.send(mimeMessage);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
            Toast.makeText(PopActivity.this, "Mail Sent Successfully!", Toast.LENGTH_SHORT).show();
            saveConfig(name, senderMail, pass, host, port, receiverMail);

        } catch (AddressException e) {
            Toast.makeText(PopActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (MessagingException e) {
            Toast.makeText(PopActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveConfig(String name, String senderMail, String pass, String host, String port, String receiverMail) {
        try {
            String field = name + "\n" + senderMail + "\n" + pass + "\n" + host + "\n" + port + "\n" + receiverMail;
            // Path tp storage
            File path = Environment.getExternalStorageDirectory();
            // Create folder named "My Files"
            File dir = new File(path + "/My Files/");
            dir.mkdirs();
            // File name
            String fileName = "MyConfig.txt";   //e.g. --> MyFile_20220412_152322.txt

            File file = new File(dir, fileName);

            if (!file.exists()) {
                // File file = new File(dir, fileName);
                file.createNewFile();

                // FileWriter class is used to store characters in file
                FileWriter fileWritter = new FileWriter(file, false);
                fileWritter.write(field);
                fileWritter.close();
            } else {
                // FileWriter class is used to store characters in file
                FileWriter fw = new FileWriter(file, false);
                fw.write(field);
                fw.close();
            }

            // Show filename and path where file is saved
//            Toast.makeText(this,fileName+" is saved to\n" + dir,Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            // If anything goes wrong
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}