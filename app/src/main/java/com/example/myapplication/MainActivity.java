package com.example.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class MainActivity extends AppCompatActivity {

    KeyPair keyPair = null;
    byte[] signatureBytes = null;


    Button btnSignButton = null;
    Button btnVerify = null;
    TextView txtSignature = null;
    EditText edtMessage = null;
    Signature sig = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

        setContentView(R.layout.activity_main);
        mapObjects();
        setListeners();
        initSignature();
    }

    private void mapObjects(){
        btnSignButton = (Button) findViewById(R.id.button);
        btnVerify = (Button) findViewById(R.id.button2);
        txtSignature = (TextView) findViewById(R.id.textView2);
        edtMessage = (EditText) findViewById(R.id.editText);
    }

    private void setListeners(){
        btnSignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "Stupid Message";
                message = edtMessage.getText().toString();
                String singStr = getSignature(message);
                txtSignature.setText(singStr);
            }
        });
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = edtMessage.getText().toString();
                String signatureHex = txtSignature.getText().toString();
                byte[] signature = signatureBytes;//MyUtil.hexToBytes(signatureHex);

                if (verifySig(getPublicKey(),signature, message)){
                    MyUtil.log("Signature is good");
                    Toast.makeText(MainActivity.this.getBaseContext(),"Signature is good", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this.getBaseContext(),"Wrong Signature!", Toast.LENGTH_SHORT).show();
                    MyUtil.log("Wrong Signature!");
                }
            }
        });
    }

    private void initSignature(){

        try {
            sig = Signature.getInstance("SHA1withRSA");//"SHA512withRSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        SecureRandom secureRandom = new SecureRandom();

        String keyPriv = getPrivateKey().getEncoded().toString();
        String keyPub = getPublicKey().toString();
        MyUtil.log("Private:" + keyPriv);
        MyUtil.log("Public:" + keyPub) ;
        try {
            //sig.initSign(keyPair.getPrivate(), secureRandom);
            sig.initSign(getPrivateKey(), secureRandom);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    private String getSignature(String data){
        MyUtil.log("Message:"+data);
        //byte[] sigB = null;
        String result = "";
        try {
            byte[] b = data.getBytes("UTF-8");
            MyUtil.log("MessageHex:" + MyUtil.bytesToHex(b));
            sig.update(b);
            signatureBytes = sig.sign();
            String hexSignature = MyUtil.bytesToHex(signatureBytes);
            result = hexSignature;//Base64.encodeToString(sigB, Base64.DEFAULT);
            MyUtil.log("Signature:" + result);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        }
        if (signatureBytes == null || signatureBytes.length == 0) {
            MyUtil.log("Empty signature");
            return "EMPTY SIGNATURE";
        }
        return result;
    }

    private boolean verifySig(PublicKey pubKey, byte[] signature, String message){
        boolean result = false;
        try {
            Signature sigVerify = Signature.getInstance("SHA1withRSA");
            sigVerify.initVerify(pubKey);
            sigVerify.update(message.getBytes());
            result = sigVerify.verify(signature);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        }

        return result;
    }

//    private PrivateKey getPrivateKey() {
//        if (keyPair == null) {
//            KeyPairGenerator keyPairGenerator = null;
//            try {
//                keyPairGenerator = KeyPairGenerator.getInstance("RSA");
//            } catch (NoSuchAlgorithmException e) {
//                e.printStackTrace();
//            }
//            keyPairGenerator.initialize(512);
//            keyPair = keyPairGenerator.generateKeyPair();
//            String keyPriv = keyPair.getPrivate().getEncoded().toString();
//            String keyPub = keyPair.getPublic().toString();
//            MyUtil.log("Private:" + keyPriv);
//            MyUtil.log("Public:" + keyPub) ;
//        }
//        return keyPair.getPrivate();
//    }



    private static PrivateKey getPrivateKey(){
        PrivateKey privateKey = null;
        try {
            String bbb = "30820153020100300d06092a864886f70d01010105000482013d308201390201000241008fd65d7dd869a507094807e3d7d51abc3d5ee6e86d4fb6960f46d3f29ba530c5b92892a8111c7b03f705c8f7d1f6bb3c2a0df90abc5cde15407dedeeb7e36ae90203010001024026d3cbfafb9f50fffc3e687ad5b95df5306fdccf232ae073d37de01ade6f1221f3fbe35ad16d1472336679688e212c2f041e3ea4e6701246b06a0efc3940cadd022100e9fa002d5cf6d18e80c11da4116ac78a545dcecac63e9a7e5a5f9a7f0dc2072f0221009d6054f0a0c97143eab06f51277389128237be6d535e0e02165e01623414296702205f405f82182e82f1388965bfbd37733465542b3371b15ac3c596d616934b4211022054f4f305af96ef993d719fb64cb8d72f71b28c0f52a8fca0edd833a103023b5302202ad4904b2011eef98cb7feac715c231ced50fb5066cfd01b8cee72d69896d9ca";
            privateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(MyUtil.hexToBytes(bbb)));
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return privateKey;
    }



    private PublicKey getPublicKey(){
        //getPrivateKey();
        //return keyPair.getPublic();
        return getFromPrivate(getPrivateKey());
    }

    private PublicKey getFromPrivate(PrivateKey pk){
        PublicKey myPublicKey = null;
        String pubKeyStr = "305c300d06092a864886f70d0101010500034b0030480241008fd65d7dd869a507094807e3d7d51abc3d5ee6e86d4fb6960f46d3f29ba530c5b92892a8111c7b03f705c8f7d1f6bb3c2a0df90abc5cde15407dedeeb7e36ae90203010001";
        KeyFactory kf = null;
        try {
            kf = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(MyUtil.hexToBytes(pubKeyStr));
            //RSAPrivateKey aaa = (RSAPrivateKey) pk;
            //MyUtil.log("Modulus:"+aaa.getModulus());
            //MyUtil.log("Exponent:"+aaa.getPrivateExponent());
            //RSAPublicKeySpec keySpec = new RSAPublicKeySpec(aaa.getModulus(),aaa.getPrivateExponent());
            myPublicKey = kf.generatePublic(keySpecX509);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }


        //    private void initSignature(){
//
//        try {
//            sig = Signature.getInstance("SHA1withRSA");//"SHA512withRSA");
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        SecureRandom secureRandom = new SecureRandom();
//        KeyPairGenerator keyPairGenerator = null;
//        try {
//            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        keyPairGenerator.initialize(512);
//        KeyPair keyPair = keyPairGenerator.generateKeyPair();
//        String keyPriv = keyPair.getPrivate().getEncoded().toString();
//        String keyPub = keyPair.getPublic().toString();
//        MyUtil.log("Private:" + keyPriv);
//        MyUtil.log("Public:" + keyPub) ;
//        try {
//            sig.initSign(keyPair.getPrivate(), secureRandom);
//        } catch (InvalidKeyException e) {
//            e.printStackTrace();
//        }
//    }



        //RSAPrivateCrtKeyImpl
//        Pri
//        RSAPrivateCrtKeyParameters privk = (RSAPrivateCrtKey)pk;
//        RSAPri
        // //CryptoUtil.setCryptoProvider(new ScJceCryptoProvider());
//
//        RSAPublicKeySpec publicKeySpec = new java.security.spec.RSAPublicKeySpec(privk.getModulus(), privk.getPublicExponent());
//
//        KeyFactory keyFactory = null;
//        try {
//            keyFactory = KeyFactory.getInstance("RSA");
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            myPublicKey = keyFactory.generatePublic(publicKeySpec);
//        } catch (InvalidKeySpecException e) {
//            e.printStackTrace();
//        }
        return myPublicKey;
    }

//    private static PublicKey getPublicFromPrivate(PrivateKey pk){
//        PublicKey result = null;
//        try {
//            String aaa ="MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJbNuG/6RZJDr5nErGM3tLLTWOWVlYtP" +
//                    "TsZo8zp2TjnLYTSqaq4qR46QrTgLrrOxaVH1uHIOCco/0rvIskLpb0MCAwEAAQ==";
//            String bbb = "305c300d06092a864886f70d0101010500034b0030480241008fd65d7dd869a507094807e3d7d51abc3d5ee6e86d4fb6960f46d3f29ba530c5b92892a8111c7b03f705c8f7d1f6bb3c2a0df90abc5cde15407dedeeb7e36ae90203010001";
//
//            pk.
//            result = KeyFactory.getInstance("RSA").generatePublic(new PKCS8EncodedKeySpec(MyUtil.hexToBytes(bbb)));
//        } catch (InvalidKeySpecException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }

//    public static PublicKey getKey(String key){
//        try {
//            byte[] byteKey = Base64.decode(key.getBytes(), Base64.DEFAULT);
//            X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(byteKey);
//            //X509EncodedKeySpec X509privateKey = new X509EncodedKeySpec(byteKey);
//            KeyFactory kf = KeyFactory.getInstance("RSA");
//            //kf.generatePrivate(X509privateKey);
//            return kf.generatePublic(X509publicKey);
//        }
//        catch(Exception e){
//            MyUtil.log("KeyImportError");
//            e.printStackTrace();
//        }
//
//        return null;
//    }

}
