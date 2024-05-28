package com.br1BotCompany.Firebase;

import com.google.api.core.ApiFuture;

import com.google.cloud.firestore.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;


@Component
public class MyApplicationRunner implements ApplicationRunner {

    @Autowired
    private FirebaseInitialization firebaseInitializer;

    @Override
    public void run(ApplicationArguments args) throws ExecutionException, InterruptedException {
        Firestore db = firebaseInitializer.getFirestore();
        // Agrego datos:
        // creo una referencia a una collection(schema) y a un document(tablas)
        DocumentReference docRef = db.collection("users").document("alovelace");
        // agrego la informacion al documento con id alovelace"
        Map<String, Object> data = new HashMap<>();
        data.put("first", "Ada");
        data.put("last", "Lovelace");
        data.put("born", 1815);

        // escribo la informacion de forma asincronica
        ApiFuture<WriteResult> result = docRef.set(data);

        System.out.println("Update time: " + result.get().getUpdateTime());

        // Agrego otro documento a la collection users
        // nota: los documentos pueden tener distintos campos.
        docRef = db.collection("users").document("aturing");
        // agrego los campos de mi documento
        Map<String, Object> data2 = new HashMap<>();
        data2.put("first", "Alan");
        data2.put("middle", "Mathison");
        data2.put("last", "Turing");
        data2.put("born", 1912);

        ApiFuture<WriteResult> result2 = docRef.set(data2);

        System.out.println("Update time : " + result2.get().getUpdateTime());

        // Read data:
        // asynchronously retrieve all users
        ApiFuture<QuerySnapshot> query = db.collection("users").get();

        QuerySnapshot querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        for (QueryDocumentSnapshot document: documents) {
            System.out.println("User: " + document.getId());
            System.out.println("First: " + document.getString("first"));
            if (document.contains("middle")) {
                System.out.println("Middle: " + document.getString("middle"));
            }
            System.out.println("Last: " + document.getString("last"));
            System.out.println("Born: " + document.getLong("born"));
        }
    }
}
